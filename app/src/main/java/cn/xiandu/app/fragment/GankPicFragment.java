package cn.xiandu.app.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.weavey.loading.lib.LoadingLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xiandu.app.activity.PicViewActivity;
import cn.xiandu.app.activity.R;
import cn.xiandu.app.adapter.PicAdapter;
import cn.xiandu.app.bean.GankPicData;
import cn.xiandu.app.utils.CommonTool;
import cn.xiandu.app.utils.Constant;
import cn.xiandu.app.utils.ToastUtils;
import okhttp3.Call;

/**
 * A simple {@link Fragment} subclass.
 */
public class GankPicFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.sports_recyclerview)
    RecyclerView picRecyclerview;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    @BindView(R.id.loading_layout)
    LoadingLayout loadingLayout;
    private View view;
    private int MAX_SIZE = 10;
    private PicAdapter mAdapter;
    private Gson gson;
    private int pageCode = 1;
    private List<GankPicData> list = new ArrayList<>();
    private boolean isRefresh ;
    private StaggeredGridLayoutManager layoutManager;
    public GankPicFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_gank_pic, container, false);
            ButterKnife.bind(this, view);
            gson = new Gson();
            mAdapter = new PicAdapter(getActivity(), R.layout.pic_item_layout, list);

            layoutManager = new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL);
            //防止 滑动后回到顶部时， item 交错
            layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
            picRecyclerview.setLayoutManager(layoutManager);
            picRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    layoutManager.invalidateSpanAssignments(); //防止第一行到顶部有空白区域
                }
            });

            picRecyclerview.setAdapter(mAdapter);
            picRecyclerview.addOnItemTouchListener(new OnItemClickListener() {
                @Override
                public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                    GankPicData data = list.get(i);
                    Intent intent = new Intent(getActivity(), PicViewActivity.class);
                    intent.putExtra("url",data.getUrl());
                    intent.putExtra("id",data.get_id());
                    startActivity(intent);
                }
            });
            mAdapter.openLoadMore(MAX_SIZE);
            mAdapter.setOnLoadMoreListener(loadMoreListener);

            swipeLayout.setOnRefreshListener(this);
            swipeLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
            loadingLayout.setStatus(LoadingLayout.Loading);
            loadingLayout.setOnReloadListener(new LoadingLayout.OnReloadListener() {
                @Override
                public void onReload(View v) {
                    loadingLayout.setStatus(LoadingLayout.Loading);
                    list.clear();
                    getData();
                }
            });
            getData();
        }
        return view;
    }

    private void getData() {
        if (!CommonTool.isNetworkConnected(getActivity())){
            ToastUtils.showMyToast("检查网络连接");
            if (swipeLayout.isRefreshing()){
                swipeLayout.setRefreshing(false);
            }
            loadingLayout.setStatus(LoadingLayout.No_Network);
            return ;
        }
        Logger.d(Constant.GANK_PIC_URL + pageCode);
        OkHttpUtils
                .get()
                .url(Constant.GANK_PIC_URL + pageCode)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Logger.i("", "e:" + e.getMessage());
                        if (swipeLayout.isRefreshing()) {
                            swipeLayout.setRefreshing(false);
                        }
                        if (pageCode == 1){
                            loadingLayout.setStatus(LoadingLayout.Error);
                        }else{
                            ToastUtils.showMyToast("服务端异常，请重试");
                        }
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        Logger.json(response);
                        if (swipeLayout.isRefreshing()) {
                            swipeLayout.setRefreshing(false);
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean error = jsonObject.optBoolean("error");
                            if (!error) {
                                loadingLayout.setStatus(LoadingLayout.Success);//加载成功
                                JSONArray ja = jsonObject.optJSONArray("results");
                                if (ja == null || ja.length() == 0){
                                    loadingLayout.setStatus(LoadingLayout.Empty);//加载成功
                                    return;
                                }
                                if (ja.length() < MAX_SIZE) {
                                    mAdapter.loadComplete();
                                }
                                Type t = new TypeToken<List<GankPicData>>() {}.getType();
                                List<GankPicData> ls = gson.fromJson(ja.toString(), t);
//                                list.addAll(ls);
                                if (!isRefresh){
                                    mAdapter.setHeights(list);
                                }
                                mAdapter.addData(ls);
                                pageCode ++;
                            }else{
                                if (pageCode == 1){
                                    loadingLayout.setStatus(LoadingLayout.Error);
                                }else{
                                    ToastUtils.showMyToast("服务端异常，请重试");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }


    @Override
    public void onRefresh() {
        pageCode = 1;
        isRefresh = true ;
        list.clear();
        getData();
    }

    //加载更多回调
    BaseQuickAdapter.RequestLoadMoreListener loadMoreListener = new BaseQuickAdapter.RequestLoadMoreListener() {
        @Override
        public void onLoadMoreRequested() {
            isRefresh = false;
            getData();
        }
    };

}

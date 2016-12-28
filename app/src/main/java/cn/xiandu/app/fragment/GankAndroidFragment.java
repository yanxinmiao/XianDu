package cn.xiandu.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import cn.xiandu.app.activity.R;
import cn.xiandu.app.activity.WebViewActivity;
import cn.xiandu.app.adapter.GankAndAdapter;
import cn.xiandu.app.bean.GankAnd;
import cn.xiandu.app.utils.CommonTool;
import cn.xiandu.app.utils.Constant;
import cn.xiandu.app.utils.ToastUtils;
import okhttp3.Call;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link .} interface
 * to handle interaction events.
 */
public class GankAndroidFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.android_recyclerview)
    RecyclerView homeRecyclerview;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    @BindView(R.id.loading_layout)
    LoadingLayout loadingLayout;
    private View view;
    private int MAX_SIZE = 10;
    private GankAndAdapter mAdapter;
    private Gson gson;
    private int pageCode = 1;
    private List<GankAnd> list = new ArrayList<>();
    private String title ;
    public GankAndroidFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString("title");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_gank_android, container, false);
            ButterKnife.bind(this, view);

            gson = new Gson();
            mAdapter = new GankAndAdapter(getActivity(), R.layout.home_item_layout, list);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            homeRecyclerview.setLayoutManager(layoutManager);
            homeRecyclerview.setAdapter(mAdapter);
            homeRecyclerview.addOnItemTouchListener(new OnItemClickListener() {
                @Override
                public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                    intent.putExtra("url", list.get(i).getUrl());
                    intent.putExtra("title", "干货集中营");
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
                    pageCode = 1;
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
        OkHttpUtils
                .get()
                .url(Constant.ALL_GANK_URL + title + "/10/" + pageCode)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (swipeLayout.isRefreshing()) {
                            swipeLayout.setRefreshing(false);
                        }
                        if (pageCode == 1){
                            loadingLayout.setStatus(LoadingLayout.Error);
                        }else{
                            ToastUtils.showMyToast("服务端异常，请重试");
                        }
                        Logger.i("", "e:" + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (swipeLayout.isRefreshing()) {
                            swipeLayout.setRefreshing(false);
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean error = jsonObject.optBoolean("error");
                            if (!error) {
                                JSONArray ja = jsonObject.optJSONArray("results");
                                if (ja == null || ja.length() == 0){
                                    loadingLayout.setStatus(LoadingLayout.Empty);//加载成功
                                    return ;
                                }
                                if (ja.length() < MAX_SIZE) {
                                    mAdapter.loadComplete();
                                }
                                Type t = new TypeToken<List<GankAnd>>() {
                                }.getType();
                                List<GankAnd> ls = gson.fromJson(ja.toString(), t);
                                mAdapter.addData(ls);
                                pageCode++;
                                loadingLayout.setStatus(LoadingLayout.Success);//加载成功
                            }else{
                                loadingLayout.setStatus(LoadingLayout.Error);
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
        list.clear();
        mAdapter.notifyDataSetChanged();
        getData();
    }

    //加载更多回调
    BaseQuickAdapter.RequestLoadMoreListener loadMoreListener = new BaseQuickAdapter.RequestLoadMoreListener() {
        @Override
        public void onLoadMoreRequested() {
            getData();
        }
    };
}

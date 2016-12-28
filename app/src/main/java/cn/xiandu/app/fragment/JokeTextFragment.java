package cn.xiandu.app.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baoyz.widget.PullRefreshLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
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
import cn.xiandu.app.adapter.JokeTextAdapter;
import cn.xiandu.app.bean.JokeTextBean;
import cn.xiandu.app.utils.CommonTool;
import cn.xiandu.app.utils.Constant;
import cn.xiandu.app.utils.SharedPreferenceUtils;
import cn.xiandu.app.utils.ThemeManager;
import cn.xiandu.app.utils.ToastUtils;
import okhttp3.Call;

/**
 * A simple {@link Fragment} subclass.
 */
public class JokeTextFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener ,PullRefreshLayout.OnRefreshListener{
    @BindView(R.id.joke_recyclerview)
    RecyclerView sportsRecyclerview;
    @BindView(R.id.swipeLayout)
    PullRefreshLayout swipeLayout;
    @BindView(R.id.loading_layout)
    LoadingLayout loadingLayout;
    private View view;
    private int MAX_SIZE = 10;
    private JokeTextAdapter mAdapter;
    private Gson gson;
    private int pageCode = 1;
    private List<JokeTextBean> list = new ArrayList<>();
    private String title;

    public JokeTextFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_joke_text, container, false);
            ButterKnife.bind(this, view);
            gson = new Gson();
            mAdapter = new JokeTextAdapter(getActivity(),R.layout.joke_item_layout, list);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            sportsRecyclerview.setLayoutManager(layoutManager);
            sportsRecyclerview.setAdapter(mAdapter);

            mAdapter.openLoadMore(MAX_SIZE);

            mAdapter.setOnLoadMoreListener(loadMoreListener);
            swipeLayout.setOnRefreshListener(this);
            swipeLayout.setRefreshStyle(PullRefreshLayout.STYLE_SMARTISAN);
//            swipeLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
            loadingLayout.setStatus(LoadingLayout.Loading);
            loadingLayout.setOnReloadListener(new LoadingLayout.OnReloadListener() {
                @Override
                public void onReload(View v) {
                    loadingLayout.setStatus(LoadingLayout.Loading);
                    list.clear();
                    getData();
                }
            });
            initTheme();
            getData();
        }
        return view;
    }
    private void initTheme() {
        int themeMode = SharedPreferenceUtils.getInt(Constant.THEMEMODE);
        if (themeMode == 1) {
            ThemeManager.setThemeMode(ThemeManager.ThemeMode.NIGHT);
        } else {
            ThemeManager.setThemeMode(ThemeManager.ThemeMode.DAY);
        }
        sportsRecyclerview.setBackgroundColor(getResources().getColor(ThemeManager.getCurrentThemeRes(getActivity(), R.color.backgroundColor)));
    }
    private void getData() {
        if (!CommonTool.isNetworkConnected(getActivity())){
            ToastUtils.showMyToast("检查网络连接");
//            if (swipeLayout.isRefreshing()){
//                swipeLayout.setRefreshing(false);
//            }
            swipeLayout.setRefreshing(false);
            loadingLayout.setStatus(LoadingLayout.No_Network);
            return ;
        }
        OkHttpUtils
                .get()
                .url(Constant.JOKE_TEXT_URL)
                .addHeader("apikey", Constant.API_KEY)
                .addParams("page", pageCode + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Logger.i("", "e:" + e.getMessage());
//                        if (swipeLayout.isRefreshing()) {
//                            swipeLayout.setRefreshing(false);
//                        }
                        swipeLayout.setRefreshing(false);
                        if (pageCode == 1){
                            loadingLayout.setStatus(LoadingLayout.Error);
                        }else{
                            ToastUtils.showMyToast("服务端异常，请重试");
                        }
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        Logger.json(response);
                        if (response == null){
                            if (pageCode == 1){
                                loadingLayout.setStatus(LoadingLayout.Error);
                                return ;
                            }
                        }
//                        if (swipeLayout.isRefreshing()) {
//                            swipeLayout.setRefreshing(false);
//                        }
                        swipeLayout.setRefreshing(false);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int code = jsonObject.optInt("showapi_res_code");
                            if (code == 0) {
                                JSONObject jos = jsonObject.optJSONObject("showapi_res_body");
                                if (jos == null){
                                    loadingLayout.setStatus(LoadingLayout.Error);
                                    return ;
                                }
                                JSONArray ja = jos.optJSONArray("contentlist");
                                if (ja == null){
                                    if (pageCode == 1){
                                        loadingLayout.setStatus(LoadingLayout.Error);
                                        return ;
                                    }else{
                                        ToastUtils.showMyToast("加载出错");
                                    }
                                }
                                loadingLayout.setStatus(LoadingLayout.Success);//加载成功
                                if (ja.length() < MAX_SIZE) {
                                    mAdapter.loadComplete();
                                }
                                Type t = new TypeToken<List<JokeTextBean>>() {
                                }.getType();
                                List<JokeTextBean> ls = gson.fromJson(ja.toString(), t);
                                Logger.d(ls.get(0));
                                mAdapter.addData(ls);
                                pageCode++;
                            } else {
                                if (pageCode == 1){
                                    loadingLayout.setStatus(LoadingLayout.Error);
                                }else{
                                    ToastUtils.showMyToast(jsonObject.optString("showapi_res_error"));
                                }
                            }
                        } catch (JSONException e) {
                            loadingLayout.setStatus(LoadingLayout.Error);
                            e.printStackTrace();
                        }
                    }
                });

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            swipeLayout.setRefreshing(true);
            pageCode = 1;
            list.clear();
            getData();
        }
    }

    @Override
    public void onRefresh() {
        pageCode = 1;
        list.clear();
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

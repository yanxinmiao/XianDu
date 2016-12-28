package cn.xiandu.app.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.weavey.loading.lib.LoadingLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
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
import cn.xiandu.app.activity.WxListActivity;
import cn.xiandu.app.adapter.HomeAdapter;
import cn.xiandu.app.bean.HomeData;
import cn.xiandu.app.bean.ThemeMode;
import cn.xiandu.app.utils.CommonTool;
import cn.xiandu.app.utils.Constant;
import cn.xiandu.app.utils.SharedPreferenceUtils;
import cn.xiandu.app.utils.ThemeManager;
import cn.xiandu.app.utils.ToastUtils;
import okhttp3.Call;


/**
 * A simple {@link Fragment} subclass.
 */
public class WxHotFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.home_recyclerview)
    RecyclerView homeRecyclerview;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    @BindView(R.id.loading_layout)
    LoadingLayout loadingLayout;
    @BindView(R.id.container)
    FrameLayout container;
    private View view;
    private int MAX_SIZE = 10;
    private HomeAdapter mAdapter;
    private Gson gson;
    private int pageCode = 1;
    private List<HomeData> list = new ArrayList<>();

    public WxHotFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_wx_hot, container, false);
            initView();
            getData();
        }
        return view;
    }

    private void initView() {
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        initTheme();
        gson = new Gson();
        mAdapter = new HomeAdapter(getActivity(), R.layout.home_item_layout, list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        homeRecyclerview.setLayoutManager(layoutManager);
        homeRecyclerview.setAdapter(mAdapter);
        homeRecyclerview.addOnItemTouchListener(simpleClickListener);
        mAdapter.openLoadMore(MAX_SIZE);
        mAdapter.setOnLoadMoreListener(loadMoreListener);

        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        loadingLayout.setStatus(LoadingLayout.Loading);
        loadingLayout.setOnReloadListener(reloadListener);
    }
    private void initTheme() {
        int themeMode = SharedPreferenceUtils.getInt(Constant.THEMEMODE);
        if (themeMode == 1) {
            ThemeManager.setThemeMode(ThemeManager.ThemeMode.NIGHT);
        } else {
            ThemeManager.setThemeMode(ThemeManager.ThemeMode.DAY);
        }
        homeRecyclerview.setBackgroundColor(getResources().getColor(ThemeManager.getCurrentThemeRes(getActivity(), R.color.backgroundColor)));
    }
    SimpleClickListener simpleClickListener = new SimpleClickListener() {
        @Override
        public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
            Intent intent = new Intent(getActivity(), WebViewActivity.class);
            intent.putExtra("flag", 1);
            HomeData homeData = list.get(i);
            intent.putExtra("data", homeData);
            startActivity(intent);
        }

        @Override
        public void onItemLongClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

        }

        @Override
        public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
            Intent intent = new Intent(getActivity(), WxListActivity.class);
            intent.putExtra("title", list.get(i).getDescription());
            startActivity(intent);
        }

        @Override
        public void onItemChildLongClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

        }
    };
    LoadingLayout.OnReloadListener reloadListener = new LoadingLayout.OnReloadListener() {
        @Override
        public void onReload(View v) {
            loadingLayout.setStatus(LoadingLayout.Loading);
            pageCode = 1;
            list.clear();
            getData();
        }
    };
    private void getData() {
        if (!CommonTool.isNetworkConnected(getActivity())) {
            ToastUtils.showMyToast("检查网络连接");
            if (swipeLayout.isRefreshing()) {
                swipeLayout.setRefreshing(false);
            }
            loadingLayout.setStatus(LoadingLayout.No_Network);
            return;
        }
        OkHttpUtils
                .get()
                .url(Constant.WX_HOT_URL)
                .addHeader("apikey", "b1d04a07a5a857be7dbea2ac738ec6e4")
                .addParams("num", 10 + "")
                .addParams("page", pageCode + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Logger.i("", "e:" + e.getMessage());
                        if (swipeLayout.isRefreshing()) {
                            swipeLayout.setRefreshing(false);
                        }
                        if (pageCode == 1) {
                            loadingLayout.setStatus(LoadingLayout.Error);
                        } else {
                            ToastUtils.showMyToast("服务端异常，请重试");
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (response == null) {
                            if (pageCode == 1) {
                                loadingLayout.setStatus(LoadingLayout.Error);
                                return;
                            }
                        }
                        Logger.json(response);
                        if (swipeLayout.isRefreshing()) {
                            swipeLayout.setRefreshing(false);
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int code = jsonObject.optInt("code");
                            if (code == 200) {
                                loadingLayout.setStatus(LoadingLayout.Success);//加载成功
                                JSONArray ja = jsonObject.optJSONArray("newslist");
                                if (ja.length() < MAX_SIZE) {
                                    mAdapter.loadComplete();
                                }
                                Type t = new TypeToken<List<HomeData>>() {
                                }.getType();
                                List<HomeData> ls = gson.fromJson(ja.toString(), t);
                                mAdapter.addData(ls);
                                pageCode++;
                            } else {
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

    /**
     *  /**
     * 事件处理
     * 所有事件处理方法必需是public void类型的，并且只有一个参数表示EventType
     *
     * @param
     * @Subscribe 表示订阅了MessageEvent
     *
     * @Subscribe(threadMode = ThreadMode.POSTING( 为回调所在的线程 ),
     *
     * priority = 0 (优先级), sticky = true(是否 接收粘性事件))
     * <p/>
     * StickyEvent=false的订阅者能否接收postSticky的事件
     *
     * @param theme
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateTheme(ThemeMode theme){
        if (theme.getMode() == 1){
            ThemeManager.setThemeMode(ThemeManager.ThemeMode.NIGHT);
        }else if (theme.getMode() == 2){
            ThemeManager.setThemeMode(ThemeManager.ThemeMode.DAY);
        }
        initTheme();
        mAdapter.notifyDataSetChanged();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

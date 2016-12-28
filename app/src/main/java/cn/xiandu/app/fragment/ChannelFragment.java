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
import android.widget.FrameLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.weavey.loading.lib.LoadingLayout;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
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
import cn.xiandu.app.activity.WebViewActivity;
import cn.xiandu.app.adapter.ChannelAdapter;
import cn.xiandu.app.bean.ChannelData;
import cn.xiandu.app.utils.CommonTool;
import cn.xiandu.app.utils.Constant;
import cn.xiandu.app.utils.SharedPreferenceUtils;
import cn.xiandu.app.utils.ThemeManager;
import cn.xiandu.app.utils.ToastUtils;
import okhttp3.Call;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link } interface
 * to handle interaction events.
 */
public class ChannelFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.channel_recyclerview)
    RecyclerView channelRecyclerview;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    @BindView(R.id.loading_layout)
    LoadingLayout loadingLayout;
    @BindView(R.id.fl_channel)
    FrameLayout flChannel;
    private View view;
    private int MAX_SIZE = 10;
    private ChannelAdapter mAdapter;
    private Gson gson;
    private int pageCode = 1;
    private List<ChannelData> list = new ArrayList<>();
    private String channel, title;

    public ChannelFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            channel = getArguments().getString("channel");
            title = getArguments().getString("title");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_channel, container, false);
            ButterKnife.bind(this, view);
            gson = new Gson();
            mAdapter = new ChannelAdapter(getActivity(), R.layout.channel_item_layout, list);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            channelRecyclerview.setLayoutManager(layoutManager);
//            channelRecyclerview.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
//                    .color(getActivity().getResources().getColor(R.color.line_colr))
////                    .sizeResId(R.dimen.divider)
//                    .marginResId(R.dimen.leftmargin, R.dimen.rightmargin)
//                    .build());
            channelRecyclerview.setAdapter(mAdapter);
            channelRecyclerview.addOnItemTouchListener(new SimpleClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                    intent.putExtra("flag", 2);
                    intent.putExtra("data", list.get(i));
                    startActivity(intent);
                }

                @Override
                public void onItemLongClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

                }

                @Override
                public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                    if (!list.get(i).isHavePic()) {
                        return;
                    }
                    Intent intent = new Intent(getActivity(), PicViewActivity.class);
                    intent.putExtra("url", list.get(i).getImageurls().get(0).getUrl());
                    intent.putExtra("id", CommonTool.getMD5Str(list.get(i).getImageurls().get(0).getUrl()));
                    startActivity(intent);
                }

                @Override
                public void onItemChildLongClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

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
            initTheme();
            getData();
        }
        return view;
    }
    private void initTheme(){
        int themeMode = SharedPreferenceUtils.getInt(Constant.THEMEMODE);
        if (themeMode == 1) {
            ThemeManager.setThemeMode(ThemeManager.ThemeMode.NIGHT);
        } else {
            ThemeManager.setThemeMode(ThemeManager.ThemeMode.DAY);
        }
        channelRecyclerview.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                .color(getActivity().getResources().getColor(ThemeManager.getCurrentThemeRes(getActivity(), R.color.line_colr)))
                .marginResId(R.dimen.leftmargin, R.dimen.rightmargin)
                .build());
        channelRecyclerview.setBackgroundColor(getResources().getColor(ThemeManager.getCurrentThemeRes(getActivity(), R.color.backgroundColor)));
    }
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
                .url(Constant.CHANNAL_NEWS_URL)
                .addHeader("apikey", Constant.API_KEY)
                .addParams("channelId", channel)
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
                        Logger.json(response);
                        if (swipeLayout.isRefreshing()) {
                            swipeLayout.setRefreshing(false);
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int code = jsonObject.optInt("showapi_res_code");
                            if (code == 0) {
                                loadingLayout.setStatus(LoadingLayout.Success);//加载成功
                                JSONObject jo = jsonObject.optJSONObject("showapi_res_body").optJSONObject("pagebean");
                                if (jo == null) {
                                    return;
                                }
                                JSONArray ja = jo.optJSONArray("contentlist");
                                if (ja.length() < MAX_SIZE) {
                                    mAdapter.loadComplete();
                                }
                                Logger.json(ja.getJSONObject(0).toString());
                                Type t = new TypeToken<List<ChannelData>>() {
                                }.getType();
                                List<ChannelData> ls = gson.fromJson(ja.toString(), t);
                                mAdapter.addData(ls);
                                pageCode++;
                            } else {
                                if (pageCode == 1) {
                                    loadingLayout.setStatus(LoadingLayout.Error);
                                } else {
                                    ToastUtils.showMyToast(jsonObject.optString("showapi_res_error"));
                                }
                            }
                        } catch (JSONException e) {
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

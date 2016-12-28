package cn.xiandu.app.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
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
import cn.xiandu.app.adapter.HomeAdapter;
import cn.xiandu.app.bean.HomeData;
import cn.xiandu.app.utils.CommonTool;
import cn.xiandu.app.utils.Constant;
import cn.xiandu.app.utils.SharedPreferenceUtils;
import cn.xiandu.app.utils.ThemeManager;
import cn.xiandu.app.utils.ToastUtils;
import okhttp3.Call;

public class WxListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.home_recyclerview)
    RecyclerView homeRecyclerview;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    private int MAX_SIZE = 10;
    private HomeAdapter mAdapter;
    private Gson gson;
    private int pageCode = 1;
    private List<HomeData> list = new ArrayList<>();
    private String title ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wx_list);
        ButterKnife.bind(this);
        title = getIntent().getStringExtra("title");
        toolbar.setTitle(title);
        toolbar.setTitleTextAppearance(this, R.style.Theme_ToolBar_Base_Title);//修改主标题的外观，包括文字颜色，文字大小等
        toolbar.setContentInsetStartWithNavigation(0);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        gson = new Gson();
        mAdapter = new HomeAdapter(this, R.layout.home_item_layout, list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        homeRecyclerview.setLayoutManager(layoutManager);
        homeRecyclerview.setAdapter(mAdapter);
        homeRecyclerview.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent intent = new Intent(WxListActivity.this, WebViewActivity.class);
                intent.putExtra("url", list.get(i).getUrl());
                intent.putExtra("title", title);
                startActivity(intent);
            }
        });
        mAdapter.openLoadMore(MAX_SIZE);
//            mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
//            mAdapter.setDuration(400);

        mAdapter.setOnLoadMoreListener(loadMoreListener);

        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        initTheme();
        getData();
    }
    private void initTheme(){
        int themeMode = SharedPreferenceUtils.getInt(Constant.THEMEMODE);
        if (themeMode == 1) {
            ThemeManager.setThemeMode(ThemeManager.ThemeMode.NIGHT);
        } else {
            ThemeManager.setThemeMode(ThemeManager.ThemeMode.DAY);
        }
        toolbar.setBackgroundColor(getResources().getColor(ThemeManager.getCurrentThemeRes(this, R.color.colorPrimary)));
        // 设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(ThemeManager.getCurrentThemeRes(this, R.color.colorPrimary)));
        }
        homeRecyclerview.setBackgroundColor(getResources().getColor(ThemeManager.getCurrentThemeRes(this, R.color.backgroundColor)));
    }
    private void getData() {
        if (!CommonTool.isNetworkConnected(this)){
            ToastUtils.showMyToast("检查网络连接");
            if (swipeLayout.isRefreshing()){
                swipeLayout.setRefreshing(false);
            }
            return ;
        }
        OkHttpUtils
                .get()
                .url(Constant.WX_HOT_URL)
                .addHeader("apikey", "b1d04a07a5a857be7dbea2ac738ec6e4")
                .addParams("src",title)
                .addParams("num", 10 + "")
                .addParams("page", pageCode + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Logger.i("", "e:" + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (swipeLayout.isRefreshing()) {
                            swipeLayout.setRefreshing(false);
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int code = jsonObject.optInt("code");
                            if (code == 200) {
                                JSONArray ja = jsonObject.optJSONArray("newslist");
                                if (ja.length() < MAX_SIZE) {
                                    mAdapter.loadComplete();
                                }
                                Type t = new TypeToken<List<HomeData>>() {
                                }.getType();
                                List<HomeData> ls = gson.fromJson(ja.toString(), t);
                                mAdapter.addData(ls);
                                pageCode++;
                            }else{
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

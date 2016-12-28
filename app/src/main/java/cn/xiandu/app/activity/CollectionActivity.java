package cn.xiandu.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.badoo.mobile.util.WeakHandler;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.weavey.loading.lib.LoadingLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xiandu.app.adapter.HomeAdapter;
import cn.xiandu.app.bean.HomeData;
import cn.xiandu.app.db.DbUtil;
import cn.xiandu.app.db.HomeDataHelper;

/**
 * 收藏列表
 */
public class CollectionActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collect_recyclerview)
    RecyclerView collectRecyclerview;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    @BindView(R.id.loading_layout)
    LoadingLayout loadingLayout;
    private HomeDataHelper homeDataHelper;
    private List<HomeData> list = new ArrayList<>();
    private WeakHandler weakHandler ;
    private HomeAdapter homeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        ButterKnife.bind(this);
        weakHandler = new WeakHandler();
        toolbar.setTitle("收藏");
        toolbar.setContentInsetStartWithNavigation(0);
        toolbar.setTitleTextAppearance(this, R.style.Theme_ToolBar_Base_Title);//修改主标题的外观，包括文字颜色，文字大小等
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        loadingLayout.setEmptyText("暂无收藏记录");
        loadingLayout.setStatus(LoadingLayout.Loading);
        weakHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                homeDataHelper = DbUtil.getHomeDataHelperHelper();
                List<HomeData> ls = homeDataHelper.queryAll();
                if (ls == null || ls.size() == 0){
                    loadingLayout.setStatus(LoadingLayout.Empty);
                    return ;
                }
                homeAdapter.addData(ls);
                loadingLayout.setStatus(LoadingLayout.Success);
            }
        },1000);

        homeAdapter = new HomeAdapter(this, R.layout.home_item_layout, list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(CollectionActivity.this);
        collectRecyclerview.setLayoutManager(layoutManager);
        collectRecyclerview.setAdapter(homeAdapter);
        collectRecyclerview.addOnItemTouchListener(simpleClickListener);
    }
    SimpleClickListener simpleClickListener = new SimpleClickListener() {
        @Override
        public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
            Intent intent = new Intent(CollectionActivity.this, WebViewActivity.class);
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
            Intent intent = new Intent(CollectionActivity.this, WxListActivity.class);
            intent.putExtra("title", list.get(i).getDescription());
            startActivity(intent);
        }
        @Override
        public void onItemChildLongClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

        }
    };

    @Override
    public void onRefresh() {
        weakHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeLayout.setRefreshing(false);
            }
        },1000);
    }
}

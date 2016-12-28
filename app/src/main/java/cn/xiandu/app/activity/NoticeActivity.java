package cn.xiandu.app.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.badoo.mobile.util.WeakHandler;
import com.weavey.loading.lib.LoadingLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xiandu.app.utils.CommonTool;

public class NoticeActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.notice_recyclerview)
    RecyclerView noticeRecyclerview;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    @BindView(R.id.loading_layout)
    LoadingLayout loadingLayout;
    private WeakHandler weakHandler ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        ButterKnife.bind(this);
        weakHandler = new WeakHandler();
        toolbar.setTitle("通知");
        toolbar.setContentInsetStartWithNavigation(0);
        toolbar.setTitleTextAppearance(this, R.style.Theme_ToolBar_Base_Title);//修改主标题的外观，包括文字颜色，文字大小等
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        loadingLayout.setEmptyText("暂无通知");
        if (!CommonTool.isNetworkConnected(this)){
            loadingLayout.setStatus(LoadingLayout.No_Network);
            return ;
        }
        loadingLayout.setStatus(LoadingLayout.Loading);
        weakHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingLayout.setStatus(LoadingLayout.Empty);
            }
        },1500);
    }
}

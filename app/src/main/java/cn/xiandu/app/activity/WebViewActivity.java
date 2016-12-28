package cn.xiandu.app.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.weavey.loading.lib.LoadingLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xiandu.app.bean.ChannelData;
import cn.xiandu.app.bean.HomeData;
import cn.xiandu.app.db.DbUtil;
import cn.xiandu.app.db.HomeDataHelper;
import cn.xiandu.app.utils.CommonTool;
import cn.xiandu.app.utils.ToastUtils;

public class WebViewActivity extends BaseActivity {

    @BindView(R.id.web_view)
    WebView webView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.loading_layout)
    LoadingLayout loadingLayout;
    private ClipboardManager mClipboard;
//    String url;
//    String title;
    private HomeData homeData ;
    private ChannelData channelData ;
    private HomeDataHelper homeDataHelper;
    private int flag; // 1 微信精选 ，2 新闻
    private boolean isCollection ;
    MenuItem menuItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        initView();
        if (!CommonTool.isNetworkConnected(this)){
            loadingLayout.setStatus(LoadingLayout.No_Network);
            return ;
        }
        flag = getIntent().getIntExtra("flag",0);
        loadingLayout.setStatus(LoadingLayout.Loading);
        menuItem = toolbar.getMenu().getItem(0);
        if (flag == 1){
            menuItem.setVisible(true);
            homeDataHelper = DbUtil.getHomeDataHelperHelper();
            homeData = getIntent().getParcelableExtra("data");
            List<HomeData> list = homeDataHelper.query("where url=?",homeData.getUrl());
            if (list != null && list.size() > 0){//已收藏
                menuItem.setTitle("已收藏");
                isCollection = true;
            }else{
                isCollection = false;
                menuItem.setTitle("收藏");
            }
            toolbar.setTitle("微信精选");
            webView.loadUrl(homeData.getUrl());
        }else if(flag == 2){
            menuItem.setVisible(false);
            channelData = getIntent().getParcelableExtra("data");
            toolbar.setTitle(channelData.getTitle());
            webView.loadUrl(channelData.getLink());
        }
    }

    private void initView() {
        ButterKnife.bind(this);
        mClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        toolbar.setTitleTextAppearance(this, R.style.Theme_ToolBar_Base_Title);//修改主标题的外观，包括文字颜色，文字大小等
        toolbar.setContentInsetStartWithNavigation(0);
        toolbar.inflateMenu(R.menu.web_toolbar_meun);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(onMenuItemClickListener);
        WebSettings webSettings = webView.getSettings();
        //启用支持javascript
        webSettings.setJavaScriptEnabled(true);
        //自适应屏幕
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);

        // 开启DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
        String cacheDirPath = getFilesDir().getAbsolutePath() + "cache";
        webSettings.setDatabasePath(cacheDirPath); // API 19 deprecated
        // 设置Application caches缓存目录
        webSettings.setAppCachePath(cacheDirPath);
        // 开启Application Cache功能
        webSettings.setAppCacheEnabled(true);
        webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        webSettings.setLoadWithOverviewMode(true);
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        webView.setWebViewClient(webViewClient);
    }

    Toolbar.OnMenuItemClickListener onMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_item3:
                    String url = flag == 1 ? homeData.getUrl() : channelData.getLink();
                    ClipData myClip = ClipData.newPlainText("text", url);
                    mClipboard.setPrimaryClip(myClip);
                    ToastUtils.showMyToast("已复制到剪切板");
                    break;
                case R.id.action_collection:
                    collection();
                    break;
            }
            return false;
        }
    };
    private void collection(){
        if (flag == 1){//微信精选收藏
            if (isCollection){
                ToastUtils.showMyToast("已经收藏过了");
                return ;
            }
            homeDataHelper.save(homeData);
            menuItem.setTitle("已收藏");
            ToastUtils.showMyToast("收藏完成");
        }else if (flag == 2){

        }
    }
    WebViewClient webViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            loadingLayout.setStatus(LoadingLayout.Success);
        }
    };

    //改写物理按键——返回的逻辑
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (webView.canGoBack()) {
//                webView.goBack();//返回上一页面
//                return true;
//            }
//        }
        return super.onKeyDown(keyCode, event);
    }
}

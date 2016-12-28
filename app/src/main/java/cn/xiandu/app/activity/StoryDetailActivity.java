package cn.xiandu.app.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;

import com.orhanobut.logger.Logger;
import com.weavey.loading.lib.LoadingLayout;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.sufficientlysecure.htmltextview.HtmlResImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xiandu.app.utils.CommonTool;
import cn.xiandu.app.utils.Constant;
import cn.xiandu.app.utils.SharedPreferenceUtils;
import cn.xiandu.app.utils.ThemeManager;

public class StoryDetailActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_desc)
    HtmlTextView tvDesc;
    @BindView(R.id.loading_layout)
    LoadingLayout loadingLayout;
    @BindView(R.id.rootLayout)
    CoordinatorLayout rootLayout;
    private String link;
    private String title;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.STORYDETAIL_LOAD_SUCCESS:
                    loadingLayout.setStatus(LoadingLayout.Success);
                    String article = (String) msg.obj;
                    tvDesc.setHtml(article , new HtmlResImageGetter(tvDesc));
                    break;
                case Constant.STORYDETAIL_LOAD_FAILURE:
                    loadingLayout.setStatus(LoadingLayout.Error);
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);
        ButterKnife.bind(this);
        link = getIntent().getStringExtra("link");
        title = getIntent().getStringExtra("title");
        toolbar.setTitle(title);
        toolbar.setContentInsetStartWithNavigation(0);
        toolbar.setTitleTextAppearance(this, R.style.Theme_ToolBar_Base_Title);//修改主标题的外观，包括文字颜色，文字大小等
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        loadingLayout.setStatus(LoadingLayout.Loading);
        int themeMode = SharedPreferenceUtils.getInt(Constant.THEMEMODE);
        if (themeMode == 1) {
            ThemeManager.setThemeMode(ThemeManager.ThemeMode.NIGHT);
        } else {
            ThemeManager.setThemeMode(ThemeManager.ThemeMode.DAY);
        }
        initTheme();
        getData();
    }

    private void initTheme(){
        toolbar.setBackgroundColor(getResources().getColor(ThemeManager.getCurrentThemeRes(this, R.color.colorPrimary)));
        // 设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(ThemeManager.getCurrentThemeRes(this, R.color.colorPrimary)));
        }
        tvDesc.setTextColor(getResources().getColor(ThemeManager.getCurrentThemeRes(this, R.color.textColor)));
        rootLayout.setBackgroundColor(getResources().getColor(ThemeManager.getCurrentThemeRes(this, R.color.backgroundColor)));
    }
    private void getData() {
        if (!CommonTool.isNetworkConnected(this)) {
            loadingLayout.setStatus(LoadingLayout.No_Network);
            return;
        }

        new Thread() {
            @Override
            public void run() {
                super.run();
                Message msg = Message.obtain();
                try {
                    Document document = Jsoup.connect("http://www.xigushi.com" + link).get();
                    Elements articles = document.getElementsByClass("by");
//                    Elements elements = document.select("div.by");
                    String article = articles.select("p").html();
                    Logger.d("article:" + article);
                    msg.what = Constant.STORYDETAIL_LOAD_SUCCESS;
                    msg.obj = article;
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    msg.what = Constant.STORYDETAIL_LOAD_FAILURE;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            }
        }.start();
    }
}

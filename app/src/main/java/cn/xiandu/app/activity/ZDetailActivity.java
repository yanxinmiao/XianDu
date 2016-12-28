package cn.xiandu.app.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.orhanobut.logger.Logger;
import com.weavey.loading.lib.LoadingLayout;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.io.IOException;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xiandu.app.utils.CommonTool;
import cn.xiandu.app.utils.Constant;

public class ZDetailActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.web_view)
    WebView webView;
    @BindView(R.id.loading_layout)
    LoadingLayout loadingLayout;
    @BindView(R.id.tv_html)
    HtmlTextView tvHtml;
    private String url, title;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.NOVEL_LOAD_SUCCESS:
                    String bodyHtml = (String) msg.obj;
//                    tvHtml.setHtml(bodyHtml, new HtmlResImageGetter(tvHtml));
                    webView.loadDataWithBaseURL("http://www.zhihu.com", bodyHtml, "text/html", "utf-8", null);
                    loadingLayout.setStatus(LoadingLayout.Success);
                    break;
                case Constant.NOVEL_LOAD_FAILURE:
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
        setContentView(R.layout.activity_zdetail);
        ButterKnife.bind(this);
        initView();

        getData();
    }

    private void initView() {
        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        Logger.i(title);
        ButterKnife.bind(this);
        toolbar.setTitle(title);
        toolbar.setTitleTextAppearance(this, R.style.Theme_ToolBar_Base_Title);//修改主标题的外观，包括文字颜色，文字大小等
        toolbar.setContentInsetStartWithNavigation(0);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        loadingLayout.setStatus(LoadingLayout.Loading);
        webView.setWebViewClient(webViewClient);
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
                StringBuilder sb = new StringBuilder("https://www.zhihu.com").append(url);
                Logger.d("url:" + sb.toString());

                Message msg = Message.obtain();
                try {
                    Document document = Jsoup.connect(sb.toString()).timeout(5000).userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6").get();
                    Element bodyAnswer = document.getElementById("zh-question-answer-wrap");
                    Elements bodys = bodyAnswer.select("div.zm-item-answer");
                    Elements headElements = document.getElementsByTag("head");
                    headElements.iterator().next();
                    String head = headElements.iterator().next().outerHtml();

                    String html = "";
                    if (bodys.iterator().hasNext()) {
                        Iterator iterator = bodys.iterator();
                        if (iterator.hasNext()) {
                            Element element = (Element) iterator.next();
                            String body = "<body>" + element.select("div.zm-item-rich-text.expandable.js-collapse-body").iterator().next().outerHtml() + "</body>";
                            html = "<html lang=\"en\" xmlns:o=\"http://www.w3.org/1999/xhtml\">" + head + body + "</html>";

                            Document docu = Jsoup.parse(html);
                            Elements elements = docu.getElementsByTag("img");
                            Iterator iter = elements.iterator();
                            while (iter.hasNext()) {
                                Element imgElement = (Element) iter.next();
                                String result = imgElement.attr("data-actualsrc");
                                if (TextUtils.isEmpty(result)) {
                                    result = imgElement.attr("data-original");
                                }
                                imgElement.attr("src", result);
                            }
                            html = docu.outerHtml();
                            msg.what = Constant.NOVEL_LOAD_SUCCESS;
                            msg.obj = html;
                            handler.sendMessage(msg);
                        }
                    }

                } catch (IOException e) {
                    msg.what = Constant.NOVEL_LOAD_FAILURE;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            }
        }.start();
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
        }
    };
}

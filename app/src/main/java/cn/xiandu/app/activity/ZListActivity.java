package cn.xiandu.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.weavey.loading.lib.LoadingLayout;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xiandu.app.adapter.ZlistAdapter;
import cn.xiandu.app.bean.TopicDetali;
import cn.xiandu.app.utils.CommonTool;
import cn.xiandu.app.utils.Constant;
import cn.xiandu.app.utils.ThemeManager;

public class ZListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.zh_recyclerview)
    RecyclerView zhRecyclerview;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    @BindView(R.id.loading_layout)
    LoadingLayout loadingLayout;
    private Gson gson;
    private int pageCode = 1;
    private final int MAX_SIZE = 10;
    private ZlistAdapter mAdapter;
    private List<TopicDetali> list = new ArrayList<>();
    String title;
    String url;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (swipeLayout.isRefreshing()) {
                swipeLayout.setRefreshing(false);
            }
            switch (msg.what) {
                case Constant.NOVEL_LOAD_SUCCESS:
                    loadingLayout.setStatus(LoadingLayout.Success);
                    List<TopicDetali> ls = (List<TopicDetali>) msg.obj;
                    mAdapter.addData(ls);
                    pageCode++;
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
        setContentView(R.layout.activity_zlist);
        ButterKnife.bind(this);

        title = getIntent().getStringExtra("title");
        url = getIntent().getStringExtra("url");
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
        mAdapter = new ZlistAdapter(R.layout.zlist_item_layout, list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        zhRecyclerview.setLayoutManager(layoutManager);
        zhRecyclerview.setAdapter(mAdapter);
        zhRecyclerview.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent intent = new Intent(ZListActivity.this,ZDetailActivity.class);
                intent.putExtra("url",list.get(i).getQuestionUrl());
                intent.putExtra("title",title);
                startActivity(intent);

            }
        });
        zhRecyclerview.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .color(getResources().getColor(ThemeManager.getCurrentThemeRes(this, R.color.line_colr)))
                .marginResId(R.dimen.leftmargin, R.dimen.rightmargin)
                .build());
//        mAdapter.openLoadMore(MAX_SIZE);

//        mAdapter.setOnLoadMoreListener(loadMoreListener);

        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimaryDark));
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

    private void getData() {
        if (!CommonTool.isNetworkConnected(this)) {
            if (swipeLayout.isRefreshing()) {
                swipeLayout.setRefreshing(false);
            }
            loadingLayout.setStatus(LoadingLayout.No_Network);
            return;
        }
        new Thread() {
            @Override
            public void run() {
                super.run();
                StringBuilder sb = new StringBuilder("http://www.zhihu.com").append(url);
                Logger.d("url:" + sb.toString());
                Message msg = Message.obtain();
                try {
                    List<TopicDetali> ls = new ArrayList<>();
                    Document document = Jsoup.connect(sb.toString() + "#zh-question-collapsed-wrap").timeout(5000).userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6").get();

                    Element bodyAnswer = document.getElementById("zh-question-answer-wrap");
                    Elements bodys = bodyAnswer.select("div.zm-item-answer");
                    Element bodyWrapAnswer = document.getElementById("zh-question-collapsed-wrap");
                    bodys.addAll(bodyWrapAnswer.select("div.zm-item-answer.zm-item-expanded"));

                    if (bodys.iterator().hasNext()) {
                        Iterator iterator = bodys.iterator();
                        while (iterator.hasNext()) {
                            TopicDetali answersModel = new TopicDetali();
                            Element element = (Element) iterator.next();
                            String url = element.getElementsByTag("link").attr("href");
                            String vote = element.select("span.count").text();
                            String content = element.select("div.zh-summary.summary.clearfix").text();
                            if (content.length() > 4) {
                                content = content.substring(0, content.length() - 4);
                            }
                            String user = element.select("a.author-link").text();
                            answersModel.setAuthor(user);
                            answersModel.setDesc(content);
                            answersModel.setQuestionUrl(url);
                            answersModel.setAgreeNum(vote);
                            ls.add(answersModel);

                        }
                    }
                    Logger.d("bodys : " + bodys.size());
                    msg.what = Constant.NOVEL_LOAD_SUCCESS;
                    msg.obj = ls;
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    msg.what = Constant.NOVEL_LOAD_FAILURE;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            }
        }.start();
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
//            getData();
        }
    };
}

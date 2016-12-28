package cn.xiandu.app.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
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
import cn.xiandu.app.activity.R;
import cn.xiandu.app.activity.ZListActivity;
import cn.xiandu.app.adapter.ZHAdapter;
import cn.xiandu.app.bean.TopicDetali;
import cn.xiandu.app.utils.CommonTool;
import cn.xiandu.app.utils.Constant;
import cn.xiandu.app.utils.SharedPreferenceUtils;
import cn.xiandu.app.utils.ThemeManager;


/**
 * https://www.zhihu.com/topic/19550434/top-answers?page=2
 *
 * 使用jsoup 解析
 *
 * A simple {@link Fragment} subclass.
 */
public class ZListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{
    android.app.Fragment fragment;
    private int topicId;
    private static final String BUNDLE_ID = "topic_id";
    @BindView(R.id.zh_recyclerview)
    RecyclerView zhRecyclerview;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    @BindView(R.id.loading_layout)
    LoadingLayout loadingLayout;
    private String url;
    private View view;
    private ZHAdapter mAdapter;
    private List<TopicDetali> list = new ArrayList<>();
    private int pageCode = 1;
    private int MAX_SIZE = 10;
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
    public ZListFragment() {

    }
    public static ZListFragment newInstance(int topic) {
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_ID, topic);
        ZListFragment fragment = new ZListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            topicId = getArguments().getInt(BUNDLE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null){
            view = inflater.inflate(R.layout.fragment_zlist, container, false);
            ButterKnife.bind(this, view);
            initView();
            getData();
        }
        return view ;
    }
    private void initView() {
        int themeMode = SharedPreferenceUtils.getInt(Constant.THEMEMODE);
        if (themeMode == 1) {
            ThemeManager.setThemeMode(ThemeManager.ThemeMode.NIGHT);
        } else {
            ThemeManager.setThemeMode(ThemeManager.ThemeMode.DAY);
        }
        initTheme();
        mAdapter = new ZHAdapter(getActivity(),R.layout.zh_item_layout, list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        zhRecyclerview.setLayoutManager(layoutManager);
        zhRecyclerview.setAdapter(mAdapter);
        zhRecyclerview.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent intent = new Intent(getActivity(), ZListActivity.class);
                intent.putExtra("url",list.get(i).getQuestionUrl());
                intent.putExtra("title",list.get(i).getTitle());
                getActivity().startActivity(intent);
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
    }
    private void initTheme() {
        zhRecyclerview.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                .color(getActivity().getResources().getColor(ThemeManager.getCurrentThemeRes(getActivity(), R.color.line_colr)))
                .marginResId(R.dimen.leftmargin, R.dimen.rightmargin)
                .build());
        zhRecyclerview.setBackgroundColor(getResources().getColor(ThemeManager.getCurrentThemeRes(getActivity(), R.color.backgroundColor)));
    }
    private void getData(){
        if (!CommonTool.isNetworkConnected(getActivity())){
            if (swipeLayout.isRefreshing()){
                swipeLayout.setRefreshing(false);
            }
            loadingLayout.setStatus(LoadingLayout.No_Network);
            return ;
        }
        new Thread(){
            @Override
            public void run() {
                super.run();
                StringBuilder sb = new StringBuilder("https://www.zhihu.com/topic/");
                sb.append(topicId)
                        .append("/top-answers?page=")
                        .append(pageCode);
                Logger.d("url:" + sb.toString());
                Message msg = Message.obtain();
                try {
                    List<TopicDetali> ls = new ArrayList<>();
                    Document document = Jsoup.connect(sb.toString()).get();
                    Elements contentLinks = document.select("div.content");
                    Iterator iterator = contentLinks.iterator();
                    while (iterator.hasNext()) {
                        TopicDetali answers = new TopicDetali();
                        Element body = (Element) iterator.next();
                        Elements questionLinks = body.select("a.question_link");
                        if (questionLinks.iterator().hasNext()) {
                            Element questionLink = questionLinks.iterator().next();
                            answers.setTitle(questionLink.text());
                            answers.setQuestionUrl(questionLink.attr("href"));
                        }
                        Elements authors = body.select("zm-item-rich-text expandable js-collapse-body");
                        if (authors.size() > 0){
                            if (authors.iterator().hasNext()){
                                Element aVotes = authors.iterator().next();
                                answers.setAuthor(aVotes.attr("data-author-name"));
                            }
                        }
                        Elements votes = body.select("a.zm-item-vote-count.js-expand.js-vote-count");
                        if (votes.size() > 0) {
                            if (votes.iterator().hasNext()) {
                                Element aVotes = votes.iterator().next();
                                Logger.d("aVotes : " + aVotes.text());
                                answers.setAgreeNum(aVotes.text());
                            }
                        }
                        Elements divs = body.select("div.zh-summary.summary.clearfix");
                        String descBody = divs.text();
                        if (descBody.length() > 4) {
                            descBody = descBody.substring(0, descBody.length() - 4);
                        }
                        answers.setDesc(descBody);
                        if (divs.size() > 0) {
                            if (divs.iterator().hasNext()) {
                                Element aDiv = divs.iterator().next();

                                Element img = aDiv.children().first();
                                if (img.tagName().equals("img")) {
                                    String imgUrl = img.attr("src");
                                    answers.setImageUrl(imgUrl);
                                }
                            }
                        }
                        if (!TextUtils.isEmpty(answers.getTitle()) && !TextUtils.isEmpty(answers.getQuestionUrl())) {
                            ls.add(answers);
                        }
                    }
//                    Elements elements = document.getElementsByClass("question_link");
//                    Elements bodys = document.getElementsByClass("zh-summary summary clearfix");
//                    Elements authors = document.getElementsByClass("zm-item-rich-text expandable js-collapse-body");
//                    Elements counts = document.getElementsByClass("count");
//                    Logger.d("bodys : " + bodys.size());
//                    Logger.d("counts : " + counts.size());
//                    Logger.d("elements : " + elements.size());
//                    for (int i = 0 ; i < 20 ; i++){
//                        TopicDetali topicDetali = new TopicDetali();
//                        if (i == 0){
//                            Logger.d("href : " + elements.get(i).attr("href"));
//                        }
//                        topicDetali.setQuestionUrl(elements.get(i).attr("href"));
//                        topicDetali.setTitle(elements.get(i).text());
//                        if (i < counts.size()){
//                            topicDetali.setAgreeNum(counts.get(i).text());
//                        }
//                        if (i < bodys.size()){
//                            String desc = bodys.get(i).text();
//                            if (i == 0){
//                                Logger.d("desc : " + desc);
//                            }
//                            topicDetali.setDesc(desc);
//                        }
//                        String image = bodys.get(i).select("img").attr("src");
//                        if (i == 0){
//                            Logger.d("image : " + image);
//                        }
//                        if (!TextUtils.isEmpty(image)){
//                            topicDetali.setImageUrl(image);
//                        }
//                        String author = authors.get(i).attr("data-author-name");
//                        topicDetali.setAuthor(author);
//                        ls.add(topicDetali);
//                    }
                    msg.what = Constant.NOVEL_LOAD_SUCCESS;
                    msg.obj = ls;
                    handler.sendMessage(msg);
                }catch (IOException e) {
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

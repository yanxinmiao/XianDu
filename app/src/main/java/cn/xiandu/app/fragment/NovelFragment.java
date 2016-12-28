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
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xiandu.app.activity.ProseDetailActivity;
import cn.xiandu.app.activity.R;
import cn.xiandu.app.adapter.NovelAdapter;
import cn.xiandu.app.bean.NovelBean;
import cn.xiandu.app.utils.CommonTool;
import cn.xiandu.app.utils.Constant;
import cn.xiandu.app.utils.SharedPreferenceUtils;
import cn.xiandu.app.utils.ThemeManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class NovelFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{
    @BindView(R.id.novel_recyclerview)
    RecyclerView novelRecyclerview;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    @BindView(R.id.loading_layout)
    LoadingLayout loadingLayout;
    private String url;
    private View view;
    private NovelAdapter mAdapter;
    private List<NovelBean> list = new ArrayList<>();
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
                    List<NovelBean> ls = (List<NovelBean>) msg.obj;
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

    public NovelFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            url = getArguments().getString("url");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null){
            view = inflater.inflate(R.layout.fragment_novel, container, false);
            ButterKnife.bind(this, view);
            initView();
            getData();
        }
        return view;
    }
    private void initView() {
        int themeMode = SharedPreferenceUtils.getInt(Constant.THEMEMODE);
        if (themeMode == 1) {
            ThemeManager.setThemeMode(ThemeManager.ThemeMode.NIGHT);
        } else {
            ThemeManager.setThemeMode(ThemeManager.ThemeMode.DAY);
        }
        initTheme();
        mAdapter = new NovelAdapter(getActivity(),R.layout.novel_item_layout, list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        novelRecyclerview.setLayoutManager(layoutManager);
//        novelRecyclerview.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
//                .color(getActivity().getResources().getColor(R.color.line_colr))
//                .marginResId(R.dimen.leftmargin, R.dimen.rightmargin)
//                .build());
        novelRecyclerview.setAdapter(mAdapter);
        novelRecyclerview.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent intent = new Intent(getActivity(), ProseDetailActivity.class);
                intent.putExtra("type",1);
                intent.putExtra("link",list.get(i).getLink());
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
        novelRecyclerview.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                .color(getActivity().getResources().getColor(ThemeManager.getCurrentThemeRes(getActivity(), R.color.line_colr)))
                .marginResId(R.dimen.leftmargin, R.dimen.rightmargin)
                .build());
        novelRecyclerview.setBackgroundColor(getResources().getColor(ThemeManager.getCurrentThemeRes(getActivity(), R.color.backgroundColor)));
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
                StringBuilder sb = new StringBuilder(url);
                sb.append(pageCode + Constant.PROSE_SUFFIX);
                Logger.d("url:" + sb.toString());
                Message msg = Message.obtain();
                try {
                    List<NovelBean> ls = new ArrayList<>();
                    Document document = Jsoup.connect(sb.toString()).get();
                    Elements elements = document.getElementsByClass("listbox");
                    Elements lis = elements.select("li");
                    for (int i = 0 ; i < lis.size() ; i++){
                        NovelBean novelBean = new NovelBean();
                        String link = lis.get(i).getElementsByClass("preview").attr("href");
//                        String link = lis.get(i).select("a").attr("href");
                        String imageUrl = lis.get(i).select("a").select("img").attr("src");
                        String title = lis.get(i).getElementsByClass("title").text();
                        String desc = lis.get(i).select("p").text();
                        if (i == 0){
                            Logger.d(desc);
                        }
                        novelBean.setTitle(title);
                        novelBean.setDesc(desc);
                        novelBean.setImageUrl(imageUrl);
                        novelBean.setLink(link);
                        ls.add(novelBean);
                    }
                    Logger.d("ls:" + ls.size());
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

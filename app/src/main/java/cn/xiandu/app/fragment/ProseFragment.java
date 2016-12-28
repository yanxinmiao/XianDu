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
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xiandu.app.activity.ProseDetailActivity;
import cn.xiandu.app.activity.R;
import cn.xiandu.app.adapter.ProseAdampter;
import cn.xiandu.app.bean.ProseBean;
import cn.xiandu.app.utils.CommonTool;
import cn.xiandu.app.utils.Constant;
import cn.xiandu.app.utils.SharedPreferenceUtils;
import cn.xiandu.app.utils.ThemeManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProseFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    String url;
    @BindView(R.id.prose_recyclerview)
    RecyclerView proseRecyclerview;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    @BindView(R.id.loading_layout)
    LoadingLayout loadingLayout;
    private View view;
    private ProseAdampter mAdapter;
    private List<ProseBean> list = new ArrayList<>();
    private int pageCode = 1;
    private int MAX_SIZE = 10;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (swipeLayout.isRefreshing()){
                swipeLayout.setRefreshing(false);
            }
            switch (msg.what){
                case Constant.PROSE_LOAD_SUCCESS:
                    loadingLayout.setStatus(LoadingLayout.Success);
                    List<ProseBean> ls = (List<ProseBean>) msg.obj;
                    mAdapter.addData(ls);
                    pageCode ++;
                    break;
                case Constant.PROSE_LOAD_FAILURE:
                    loadingLayout.setStatus(LoadingLayout.Error);
                    break;
                default:
                    break;
            }
        }
    };
    public ProseFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            url = getArguments().getString("url");
            Logger.d("url:" + url);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null){
            view = inflater.inflate(R.layout.fragment_prose, container, false);
            initView();
            initTheme();
            getData();
        }
        return view;
    }

    private void initView() {
        ButterKnife.bind(this, view);

        mAdapter = new ProseAdampter(getActivity(), R.layout.prose_item_layout, list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        proseRecyclerview.setLayoutManager(layoutManager);
//        proseRecyclerview.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
//                .color(getActivity().getResources().getColor(R.color.line_colr))
//                .marginResId(R.dimen.leftmargin, R.dimen.rightmargin)
//                .build());
        proseRecyclerview.setAdapter(mAdapter);
        proseRecyclerview.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent intent = new Intent(getActivity(), ProseDetailActivity.class);
                intent.putExtra("link",list.get(i).getLink());
                intent.putExtra("title",list.get(i).getTitle());
                intent.putExtra("type",2);
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

    private void initTheme(){
        int themeMode = SharedPreferenceUtils.getInt(Constant.THEMEMODE);
        if (themeMode == 1) {
            ThemeManager.setThemeMode(ThemeManager.ThemeMode.NIGHT);
        } else {
            ThemeManager.setThemeMode(ThemeManager.ThemeMode.DAY);
        }
        proseRecyclerview.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                .color(getActivity().getResources().getColor(ThemeManager.getCurrentThemeRes(getActivity(), R.color.line_colr)))
                .marginResId(R.dimen.leftmargin, R.dimen.rightmargin)
                .build());
        proseRecyclerview.setBackgroundColor(getResources().getColor(ThemeManager.getCurrentThemeRes(getActivity(), R.color.backgroundColor)));
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
//                Logger.d("url:" + sb.toString());
                Message msg = Message.obtain();
                try {
                    Document document = Jsoup.connect(sb.toString()).get();
                    Elements imcons = document.select("div.lmcon");

                    Elements elements = document.select("div.info");
                    List<ProseBean> list = new ArrayList<>();
                    for (int i = 0; i < elements.size() ; i++){

                        String original = null;
                        Elements lis = imcons.get(0).select("li");
                        if (lis != null){
                            original = lis.get(i).select("a").select("img").attr("data-original");
                            Logger.d("original:" + original);
                        }

                        ProseBean proseBean = new ProseBean();
                        Element e = elements.get(i);
                        String title = e.select("a").attr("title");
                        proseBean.setTitle(title);

                        String link = e.select("a").attr("href");
                        proseBean.setLink(link);

                        String desc = e.select("p").text();
                        proseBean.setDesc(desc);

                        Elements elements1 = e.select("div.uinfo");
                        Elements as = elements1.get(0).select("span").select("a");
                        String span = elements1.get(0).select("span").text();
                        String[] spans = span.split(" ");
                        proseBean.setAuthor(spans[1]);
                        proseBean.setTime(spans[2]);
                        proseBean.setImageUrl(original);
                        list.add(proseBean);
                    }
                    msg.what = Constant.PROSE_LOAD_SUCCESS;
                    msg.obj = list;
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    msg.what = Constant.PROSE_LOAD_FAILURE;
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

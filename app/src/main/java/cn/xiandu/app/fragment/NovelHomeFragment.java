package cn.xiandu.app.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.astuetz.PagerSlidingTabStrip;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xiandu.app.activity.R;
import cn.xiandu.app.adapter.PagerAdapter;
import cn.xiandu.app.utils.CommonTool;
import cn.xiandu.app.utils.Constant;
import cn.xiandu.app.utils.SharedPreferenceUtils;
import cn.xiandu.app.utils.ThemeManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class NovelHomeFragment extends BaseFragment {

    @BindView(R.id.novel_tabs)
    PagerSlidingTabStrip novelTabs;
    @BindView(R.id.novel_pager)
    ViewPager novelPager;
    @BindView(R.id.fl)
    FrameLayout fl;
    private View view;
    private List<Fragment> list = new ArrayList<Fragment>();
    private List<String> titleList = new ArrayList<>();
    private List<String> urls = new ArrayList<>();

    public NovelHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_novel_home, container, false);
            ButterKnife.bind(this, view);
            titleList.add("短篇爱情");
            urls.add("http://www.aikann.com/news/1/list_1_");
            titleList.add("校园爱情");
            urls.add("http://www.aikann.com/news/2/list_2_");
            titleList.add("短篇言情");
            urls.add("http://www.aikann.com/news/7/list_7_");
            titleList.add("爱情故事");
            urls.add("http://www.aikann.com/news/3/list_3_");
            titleList.add("搞笑短篇");
            urls.add("http://www.aikann.com/news/4/list_4_");
            titleList.add("感人故事");
            urls.add("http://www.aikann.com/news/10/list_8_");
            titleList.add("小故事");
            urls.add("http://www.aikann.com/news/xiaogushi/list_9_");

            Logger.d("titleList: " + titleList.size());
            for (int i = 0; i < titleList.size(); i++) {
                NovelFragment proseFragment = new NovelFragment();
                Bundle bundle = new Bundle();
                bundle.putString("url", urls.get(i));
                proseFragment.setArguments(bundle);
                list.add(proseFragment);
            }
            Logger.d("list: " + list.size());
            PagerAdapter pagerAdapter = new PagerAdapter(getFragmentManager(), list, titleList);
            novelPager.setAdapter(pagerAdapter);

            novelTabs.setViewPager(novelPager);
            novelTabs.setShouldExpand(true);
            novelTabs.setIndicatorHeight(12);
            novelTabs.setTextSize(CommonTool.sp2px(getActivity(), 14));
            novelTabs.setTextColor(getResources().getColor(R.color.white));
            novelTabs.setDividerColor(getResources().getColor(R.color.transparent));
            novelTabs.setIndicatorColor(getResources().getColor(R.color.white));
            initTheme();
        }
        return view;
    }

    private void initTheme() {
        int themeMode = SharedPreferenceUtils.getInt(Constant.THEMEMODE);
        if (themeMode == 1) {
            ThemeManager.setThemeMode(ThemeManager.ThemeMode.NIGHT);
        } else {
            ThemeManager.setThemeMode(ThemeManager.ThemeMode.DAY);
        }
        novelTabs.setTextColor(getResources().getColor(ThemeManager.getCurrentThemeRes(getActivity(), R.color.white)));
        novelTabs.setIndicatorColor(getResources().getColor(ThemeManager.getCurrentThemeRes(getActivity(), R.color.white)));
        novelTabs.setBackgroundColor(getResources().getColor(ThemeManager.getCurrentThemeRes(getActivity(), R.color.colorPrimary)));
        fl.setBackgroundColor(getResources().getColor(ThemeManager.getCurrentThemeRes(getActivity(), R.color.backgroundColor)));
    }
}

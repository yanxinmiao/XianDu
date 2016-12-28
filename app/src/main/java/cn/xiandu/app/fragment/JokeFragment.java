package cn.xiandu.app.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.astuetz.PagerSlidingTabStrip;

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
public class JokeFragment extends BaseFragment {


    @BindView(R.id.fl_joke)
    FrameLayout flJoke;
    private View view;
    @BindView(R.id.joke_tabs)
    PagerSlidingTabStrip tabLayout;
    @BindView(R.id.joke_pager)
    ViewPager pager;
    private List<Fragment> list = new ArrayList<Fragment>();
    private List<String> titleList = new ArrayList<>();

    public JokeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_joke, container, false);
            ButterKnife.bind(this, view);
            JokeTextFragment jokeTextFragment = new JokeTextFragment();
            JokePicFragment picJokeFragment = new JokePicFragment();
            list.add(jokeTextFragment);
            list.add(picJokeFragment);
            titleList.add("段子");
            titleList.add("趣图");
            PagerAdapter pagerAdapter = new PagerAdapter(getFragmentManager(), list, titleList);
            pager.setAdapter(pagerAdapter);
//            tabLayout.setShouldExpand(true);
            tabLayout.setViewPager(pager);
            tabLayout.setIndicatorHeight(12);
            tabLayout.setTextSize(CommonTool.sp2px(getActivity(), 14));
            tabLayout.setTextColor(getResources().getColor(R.color.white));
            tabLayout.setDividerColor(getResources().getColor(R.color.transparent));
            tabLayout.setIndicatorColor(getResources().getColor(R.color.white));
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
        tabLayout.setTextColor(getResources().getColor(ThemeManager.getCurrentThemeRes(getActivity(), R.color.white)));
        tabLayout.setIndicatorColor(getResources().getColor(ThemeManager.getCurrentThemeRes(getActivity(), R.color.white)));
        tabLayout.setBackgroundColor(getResources().getColor(ThemeManager.getCurrentThemeRes(getActivity(), R.color.colorPrimary)));
        flJoke.setBackgroundColor(getResources().getColor(ThemeManager.getCurrentThemeRes(getActivity(), R.color.backgroundColor)));
    }
}

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
public class StoryHomeFragment extends Fragment {
    @BindView(R.id.story_tabs)
    PagerSlidingTabStrip tabs;
    @BindView(R.id.story_pager)
    ViewPager pager;
    @BindView(R.id.rl_story)
    FrameLayout rl;
    private View view;
    private List<Fragment> list = new ArrayList<Fragment>();
    private List<String> titleList = new ArrayList<>();
    private List<String> urls = new ArrayList<>();

    public StoryHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_story_home, container, false);
            ButterKnife.bind(this, view);
            titleList.add("幽默故事");
            urls.add("http://www.xigushi.com/ymgs/list_3_");
            titleList.add("儿童故事");
            urls.add("http://www.xigushi.com/thgs/list_2_");
            titleList.add("爱情故事");
            urls.add("http://www.xigushi.com/aqgs/list_4_");
            titleList.add("职场故事");
            urls.add("http://www.xigushi.com/jcgs/list_5_");
            titleList.add("励志故事");
            urls.add("http://www.xigushi.com/lzgs/list_6_");
            titleList.add("哲理故事");
            urls.add("http://www.xigushi.com/zlgs/list_7_");
            titleList.add("校园故事");
            urls.add("http://www.xigushi.com/xygs/list_8_");
            titleList.add("人生故事");
            urls.add("http://www.xigushi.com/rsgs/list_9_");
            titleList.add("寓言故事");
            urls.add("http://www.xigushi.com/yygs/list_10_");

            titleList.add("名人故事");
            urls.add("http://www.xigushi.com/mrgs/list_11_");
            titleList.add("亲情故事");
            urls.add("http://www.xigushi.com/qqgs/list_12_");
            titleList.add("友情故事");
            urls.add("http://www.xigushi.com/yqgs/list_1_");
            Logger.d("titleList: " + titleList.size());
            for (int i = 0; i < titleList.size(); i++) {
                StoryFragment proseFragment = new StoryFragment();
                Bundle bundle = new Bundle();
                bundle.putString("url", urls.get(i));
                proseFragment.setArguments(bundle);
                list.add(proseFragment);
            }
            Logger.d("list: " + list.size());
            PagerAdapter pagerAdapter = new PagerAdapter(getFragmentManager(), list, titleList);
            pager.setAdapter(pagerAdapter);

            tabs.setViewPager(pager);
            tabs.setShouldExpand(true);
            tabs.setIndicatorHeight(12);
            tabs.setTextSize(CommonTool.sp2px(getActivity(), 14));
            tabs.setTextColor(getResources().getColor(R.color.white));
            tabs.setDividerColor(getResources().getColor(R.color.transparent));
            tabs.setIndicatorColor(getResources().getColor(R.color.white));
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
        tabs.setTextColor(getResources().getColor(ThemeManager.getCurrentThemeRes(getActivity(), R.color.white)));
        tabs.setIndicatorColor(getResources().getColor(ThemeManager.getCurrentThemeRes(getActivity(), R.color.white)));
        tabs.setBackgroundColor(getResources().getColor(ThemeManager.getCurrentThemeRes(getActivity(), R.color.colorPrimary)));
        rl.setBackgroundColor(getResources().getColor(ThemeManager.getCurrentThemeRes(getActivity(), R.color.backgroundColor)));
    }
}

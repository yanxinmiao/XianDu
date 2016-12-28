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
public class ProseHomeFragment extends BaseFragment {


    @BindView(R.id.prose_tabs)
    PagerSlidingTabStrip tabs;
    @BindView(R.id.prose_pager)
    ViewPager pager;
    @BindView(R.id.rl_prose)
    FrameLayout rlProse;
    private View view;
    private List<Fragment> list = new ArrayList<Fragment>();
    private List<String> titleList = new ArrayList<>();
    private List<String> urls = new ArrayList<>();

    public ProseHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_prose_home, container, false);
            ButterKnife.bind(this, view);
            titleList.add("经典美文");
            urls.add("http://www.lookmw.cn/jdmw/list_6_");
            titleList.add("原创美文");
            urls.add("http://www.lookmw.cn/yc/list_64477_");
            titleList.add("伤感文章");
            urls.add("http://www.lookmw.cn/shanggan/list_64482_");
            titleList.add("情感美文");
            urls.add("http://www.lookmw.cn/qingganmeiwen/list_64480_");
            titleList.add("感人文章");
            urls.add("http://www.lookmw.cn/ganrenwenzhang/list_64484_");
            titleList.add("生活随笔");
            urls.add("http://www.lookmw.cn/suibi/list_64483_");
            titleList.add("励志美文");
            urls.add("http://www.lookmw.cn/lizhi/list_63448_");
            titleList.add("人生哲理");
            urls.add("http://www.lookmw.cn/renshengzheli/list_64486_");
            Logger.d("titleList: " + titleList.size());
            for (int i = 0; i < titleList.size(); i++) {
                ProseFragment proseFragment = new ProseFragment();
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
        rlProse.setBackgroundColor(getResources().getColor(ThemeManager.getCurrentThemeRes(getActivity(), R.color.backgroundColor)));
    }
}

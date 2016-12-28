package cn.xiandu.app.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xiandu.app.activity.R;
import cn.xiandu.app.adapter.PagerAdapter;
import cn.xiandu.app.utils.CommonTool;

/**
 * A simple {@link Fragment} subclass.
 */
public class GankFragment extends BaseFragment {

    @BindView(R.id.gank_tabs)
    PagerSlidingTabStrip tabLayout;
    @BindView(R.id.gank_pager)
    ViewPager pager;
    private View view;
    private List<Fragment> list = new ArrayList<Fragment>();
    private List<String> titleList = new ArrayList<>();
    public GankFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_gank, container, false);
            ButterKnife.bind(this, view);
            titleList.add("all");
            titleList.add("Android");
            titleList.add("iOS");
            titleList.add("前端");
            titleList.add("拓展资源");
            for (int i = 0; i < titleList.size(); i ++){
                GankAndroidFragment channelFragment = new GankAndroidFragment();
                Bundle bundle = new Bundle();
                bundle.putString("title", titleList.get(i));
                channelFragment.setArguments(bundle);
                list.add(channelFragment);
            }
            titleList.add("休息视频");
            GankVideoFragment videoFragment = new GankVideoFragment();
            Bundle bundle = new Bundle();
            bundle.putString("title", "休息视频");
            videoFragment.setArguments(bundle);
            list.add(videoFragment);

            PagerAdapter pagerAdapter = new PagerAdapter(getFragmentManager(), list, titleList);
            pager.setAdapter(pagerAdapter);
//            tabLayout.setShouldExpand(true);
//            tabLayout.setAllCaps(false);
            tabLayout.setViewPager(pager);
            tabLayout.setIndicatorHeight(12);
            tabLayout.setTextSize(CommonTool.sp2px(getActivity(), 14));
            tabLayout.setTextColor(getResources().getColor(R.color.white));
            tabLayout.setDividerColor(getResources().getColor(R.color.transparent));
            tabLayout.setIndicatorColor(getResources().getColor(R.color.white));
        }
        return view;
    }

}

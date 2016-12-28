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
import cn.xiandu.app.bean.TopicModel;
import cn.xiandu.app.utils.CommonTool;
import cn.xiandu.app.utils.TopicHelper;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link .} interface
 * to handle interaction events.
 */
public class ZHomeFragnemnt extends BaseFragment {

    @BindView(R.id.zh_tabs)
    PagerSlidingTabStrip zhTabs;
    @BindView(R.id.zh_pager)
    ViewPager zhPager;
    @BindView(R.id.rl_zh)
    FrameLayout rlZh;
    private View view;
    private List<Fragment> list = new ArrayList<Fragment>();
    private List<String> titleList = new ArrayList<>();
    public ZHomeFragnemnt() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_zhome_fragnemnt, container, false);
            ButterKnife.bind(this, view);
            TopicHelper helper = TopicHelper.getInstance();
            List<TopicModel> topics = helper.getAllTopics();
            for (int i = 0; i < topics.size(); i++){
                ZListFragment listFragment = ZListFragment.newInstance(topics.get(i).getTopic());
                titleList.add(topics.get(i).getName());
                list.add(listFragment);
            }
            PagerAdapter pagerAdapter = new PagerAdapter(getFragmentManager(), list, titleList);
            zhPager.setAdapter(pagerAdapter);

            zhTabs.setViewPager(zhPager);
            zhTabs.setShouldExpand(true);
            zhTabs.setIndicatorHeight(12);
            zhTabs.setTextSize(CommonTool.sp2px(getActivity(), 14));
            zhTabs.setTextColor(getResources().getColor(R.color.white));
            zhTabs.setDividerColor(getResources().getColor(R.color.transparent));
            zhTabs.setIndicatorColor(getResources().getColor(R.color.white));
        }
        return view;
    }
}

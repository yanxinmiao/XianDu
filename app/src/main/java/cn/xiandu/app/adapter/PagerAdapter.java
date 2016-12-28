package cn.xiandu.app.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created by dell on 2016/12/2.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> list ;
    private List<String> titles;
    public PagerAdapter(FragmentManager fm , List<Fragment> list, List<String> titles) {
        super(fm);
        this.list = list;
        this.titles = titles;
        Logger.d("list: " + list.size());
        Logger.d("titleList: " + titles.size());


    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}

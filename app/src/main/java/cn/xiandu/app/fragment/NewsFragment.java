package cn.xiandu.app.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.astuetz.PagerSlidingTabStrip;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xiandu.app.activity.DragActivity;
import cn.xiandu.app.activity.MainActivity;
import cn.xiandu.app.activity.R;
import cn.xiandu.app.adapter.PagerAdapter;
import cn.xiandu.app.bean.ChannelBean;
import cn.xiandu.app.utils.CommonTool;
import cn.xiandu.app.utils.Constant;
import cn.xiandu.app.utils.SharedPreferenceUtils;
import cn.xiandu.app.utils.ThemeManager;
import okhttp3.Call;

/**
 * A simple {@link Fragment} subclass.
 * <p>
 * 新闻聚合网页
 */
public class NewsFragment extends BaseFragment {
    @BindView(R.id.tabs)
    PagerSlidingTabStrip tabLayout;
    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.tv_sort)
    ImageView tvSort;
    @BindView(R.id.fl_news)
    FrameLayout flNews;
    private View view;
    private Gson gson;
    private List<Fragment> list = new ArrayList<Fragment>();
    private List<String> titleList = new ArrayList<>();
    private List<ChannelBean> ls;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_news, container, false);
            ButterKnife.bind(this, view);
            gson = new Gson();
            String channel = SharedPreferenceUtils.getString(Constant.CHANNEL_KEY);
            Logger.json(channel);
            if (TextUtils.isEmpty(channel)) {
                getData();
            } else {
                Type t = new TypeToken<List<ChannelBean>>() {
                }.getType();
                ls = gson.fromJson(channel, t);
                for (int i = 0; i < ls.size(); i++) {
                    if (i > 10) {
                        break;
                    }
                    ChannelFragment channelFragment = new ChannelFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("channel", ls.get(i).getChannelId());
                    bundle.putString("title", ls.get(i).getName());
                    channelFragment.setArguments(bundle);
                    list.add(channelFragment);
                    titleList.add(ls.get(i).getName().substring(0, ls.get(i).getName().length() - 2));
                }
                PagerAdapter pagerAdapter = new PagerAdapter(getFragmentManager(), list, titleList);
                pager.setAdapter(pagerAdapter);
                tabLayout.setViewPager(pager);
            }
            tabLayout.setShouldExpand(true);
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
        flNews.setBackgroundColor(getResources().getColor(ThemeManager.getCurrentThemeRes(getActivity(), R.color.backgroundColor)));
        tvSort.setBackgroundColor(getResources().getColor(ThemeManager.getCurrentThemeRes(getActivity(), R.color.colorPrimary)));
    }
    @OnClick(R.id.tv_sort)
    public void onClick() {
        Intent intent = new Intent(getActivity(), DragActivity.class);
        intent.putExtra("list", (ArrayList<ChannelBean>) ls);
        startActivity(intent);

    }

    /**
     * 获取频道id 、name
     */
    private void getData() {
        if (!CommonTool.isNetworkConnected(getActivity())) {
            return;
        }
        OkHttpUtils
                .get()
                .url(Constant.CHANNAL_ID_URL)
                .addHeader("apikey", Constant.API_KEY)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Logger.i("", "e:" + e.getMessage());
                        startActivity(new Intent(getActivity(), MainActivity.class));
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Logger.json(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int code = jsonObject.optInt("showapi_res_code");
                            if (code == 0) {
                                JSONArray ja = jsonObject.optJSONObject("showapi_res_body").optJSONArray("channelList");
                                if (ja != null && ja.length() > 0) {
                                    SharedPreferenceUtils.setString(Constant.CHANNEL_KEY, ja.toString());
                                }
                                Type t = new TypeToken<List<ChannelBean>>() {
                                }.getType();
                                ls = gson.fromJson(ja.toString(), t);
                                for (int i = 0; i < ls.size(); i++) {
                                    if (i > 10) {
                                        break;
                                    }
                                    ChannelFragment channelFragment = new ChannelFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("channel", ls.get(i).getChannelId());
                                    bundle.putString("title", ls.get(i).getName());
                                    channelFragment.setArguments(bundle);
                                    list.add(channelFragment);
                                    titleList.add(ls.get(i).getName().substring(0, ls.get(i).getName().length() - 2));
                                }
                                PagerAdapter pagerAdapter = new PagerAdapter(getFragmentManager(), list, titleList);
                                pager.setAdapter(pagerAdapter);
                                tabLayout.setViewPager(pager);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }
}

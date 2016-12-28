package cn.xiandu.app.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.Window;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xiandu.app.bean.ThemeMode;
import cn.xiandu.app.fragment.GankPicFragment;
import cn.xiandu.app.fragment.JokeFragment;
import cn.xiandu.app.fragment.NewsFragment;
import cn.xiandu.app.fragment.NovelHomeFragment;
import cn.xiandu.app.fragment.ProseHomeFragment;
import cn.xiandu.app.fragment.StoryHomeFragment;
import cn.xiandu.app.fragment.WxHotFragment;
import cn.xiandu.app.fragment.ZHomeFragnemnt;
import cn.xiandu.app.utils.AppManager;
import cn.xiandu.app.utils.Constant;
import cn.xiandu.app.utils.SharedPreferenceUtils;
import cn.xiandu.app.utils.ThemeManager;
import cn.xiandu.app.utils.ToastUtils;

public class MainActivity extends BaseActivity {

    @BindView(R.id.navigation)
    NavigationView navigation;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    private WxHotFragment wxHotFragment;
    private GankPicFragment sportsFragment;
    private NewsFragment newsFragment;
    private JokeFragment jokeFragment ;
    private ProseHomeFragment proseHomeFragment ;
    private StoryHomeFragment storyHomeFragment;
    private NovelHomeFragment novelHomeFragment;
    private ZHomeFragnemnt zHomeFragnemnt;
    private String title = "微信精选";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        int themeMode = SharedPreferenceUtils.getInt(Constant.THEMEMODE);
        if (themeMode == 1) {
            ThemeManager.setThemeMode(ThemeManager.ThemeMode.NIGHT);
        } else {
            ThemeManager.setThemeMode(ThemeManager.ThemeMode.DAY);
        }
        initTheme();
        //我们在使用 Toolbar 时候需要先隐藏掉系统原先的导航栏
        toolbar.setTitle("微信精选");
        toolbar.setContentInsetStartWithNavigation(0);
        toolbar.setTitleTextAppearance(this, R.style.Theme_ToolBar_Base_Title);//修改主标题的外观，包括文字颜色，文字大小等
        toolbar.inflateMenu(R.menu.base_toolbar_menu);//设置右上角的填充菜单
        /* findView */
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open,
                R.string.drawer_close);
        mDrawerToggle.syncState();
        drawerLayout.addDrawerListener(mDrawerToggle);

        navigation.setItemIconTintList(null);//设置菜单图标恢复本来的颜色
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                hideFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (id) {
                    case R.id.navItem1:
                        title = "微信精选";
                        if (wxHotFragment != null) {
                            transaction.show(wxHotFragment);
                        } else {
                            wxHotFragment = new WxHotFragment();
                            transaction.add(R.id.container, wxHotFragment);
                        }
                        transaction.commit();
                        break;
                    case R.id.navItem2:
                        title = "妹纸";
                        if (sportsFragment != null) {
                            transaction.show(sportsFragment);
                        } else {
                            sportsFragment = new GankPicFragment();
                            transaction.add(R.id.container, sportsFragment);
                        }
                        transaction.commit();
                        break;
                    case R.id.navItem3:
                        title = "新闻";
                        if (newsFragment != null) {
                            transaction.show(newsFragment);
                        } else {
                            newsFragment = new NewsFragment();
                            transaction.add(R.id.container, newsFragment);
                        }
                        transaction.commit();
                        break;
                    case R.id.navItem4:
                        title = "段子";
                        if (jokeFragment != null) {
                            transaction.show(jokeFragment);
                        } else {
                            jokeFragment = new JokeFragment();
                            transaction.add(R.id.container, jokeFragment);
                        }
                        transaction.commit();
                        break;
                    case R.id.navItem5:
                        title = "散文" ;
                        if (proseHomeFragment != null) {
                            transaction.show(proseHomeFragment);
                        } else {
                            proseHomeFragment = new ProseHomeFragment();
                            transaction.add(R.id.container, proseHomeFragment);
                        }
                        transaction.commit();
                        break;
                    case R.id.navItem6:
                        title = "故事";
                        if (storyHomeFragment != null) {
                            transaction.show(storyHomeFragment);
                        } else {
                            storyHomeFragment = new StoryHomeFragment();
                            transaction.add(R.id.container, storyHomeFragment);
                        }
                        transaction.commit();
                        break;
                    case R.id.navItem7:
                        title = "小说";
                        if (novelHomeFragment != null) {
                            transaction.show(novelHomeFragment);
                        } else {
                            novelHomeFragment = new NovelHomeFragment();
                            transaction.add(R.id.container, novelHomeFragment);
                        }
                        transaction.commit();
                        break;
                    case R.id.navItem8:
                        title = "知乎精选";
                        if (zHomeFragnemnt != null) {
                            transaction.show(zHomeFragnemnt);
                        } else {
                            zHomeFragnemnt = new ZHomeFragnemnt();
                            transaction.add(R.id.container, zHomeFragnemnt);
                        }
                        transaction.commit();
                        break;
                    default:
                        break;
                }
                toolbar.setTitle(title);
                drawerLayout.closeDrawer(Gravity.LEFT);
                return true;
            }
        });
        initSearchView();
        //  添加右上角菜单点击事件
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                if (menuItemId == R.id.action_notification) {
                    startActivity(NoticeActivity.class);
                } else if (menuItemId == R.id.action_item1) {
                    startActivity(SettingActivity.class);
                } else if (menuItemId == R.id.action_item2) {
                    startActivity(AboutusActivity.class);
                }
                return true;
            }
        });

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (wxHotFragment == null) {
            wxHotFragment = new WxHotFragment();
            transaction.add(R.id.container, wxHotFragment);
        } else {
            transaction.show(wxHotFragment);
        }
        transaction.commit();
    }
    private void hideFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (wxHotFragment != null) {
            transaction.hide(wxHotFragment);
        }
        if (sportsFragment != null) {
            transaction.hide(sportsFragment);
        }
        if (newsFragment != null) {
            transaction.hide(newsFragment);
        }
        if (jokeFragment != null){
            transaction.hide(jokeFragment);
        }
        if (proseHomeFragment != null){
            transaction.hide(proseHomeFragment);
        }
        if (storyHomeFragment != null){
            transaction.hide(storyHomeFragment);
        }
        if (novelHomeFragment != null){
            transaction.hide(novelHomeFragment);
        }
        if (zHomeFragnemnt != null){
            transaction.hide(zHomeFragnemnt);
        }
        transaction.commit();
    }
    private void initSearchView() {
        final SearchView searchView = (SearchView) toolbar.getMenu().findItem(R.id.action_search).getActionView();
        searchView.setQueryHint("搜索…");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
                return false;
            }
        });
    }
    private void initTheme() {
        toolbar.setBackgroundColor(getResources().getColor(ThemeManager.getCurrentThemeRes(this, R.color.colorPrimary)));
        // 设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(ThemeManager.getCurrentThemeRes(this, R.color.colorPrimaryDark)));
        }
        drawerLayout.setBackgroundColor(getResources().getColor(ThemeManager.getCurrentThemeRes(this, R.color.backgroundColor)));
    }
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }else{
                exitApp();
            }
            return false;
        } else {
            return super.onKeyUp(keyCode, event);
        }
    }

    /**
     * 退出应用
     */
    private long exitTime;
    public void exitApp() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            // 点击间隔大于两秒，做出提示
            ToastUtils.showMyToast(R.string.logout);
            exitTime = System.currentTimeMillis();
        } else {
            // 连续点击量两次，进行应用退出的处理
            AppManager.getAppManager().AppExit(MainActivity.this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateTheme(ThemeMode theme){
        if (theme.getMode() == 1){
            ThemeManager.setThemeMode(ThemeManager.ThemeMode.NIGHT);
        }else if (theme.getMode() == 2){
            ThemeManager.setThemeMode(ThemeManager.ThemeMode.DAY);
        }
        initTheme();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

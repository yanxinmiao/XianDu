package cn.xiandu.app.activity;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.badoo.mobile.util.WeakHandler;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xiandu.app.bean.ThemeMode;
import cn.xiandu.app.utils.Constant;
import cn.xiandu.app.utils.DataCleanManager;
import cn.xiandu.app.utils.SharedPreferenceUtils;
import cn.xiandu.app.utils.ThemeManager;

public class SettingActivity extends BaseActivity implements ThemeManager.OnThemeChangeListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_cache_size)
    TextView tvCacheSize;
    @BindView(R.id.rl_clear_cache)
    RelativeLayout rlClearCache;
    @BindView(R.id.tv_collection)
    TextView tvCollection;
    @BindView(R.id.tv_privacy)
    TextView tvPrivacy;
    @BindView(R.id.switch_day)
    Switch switchDay;
    @BindView(R.id.tv_cache)
    TextView tvCache;
    @BindView(R.id.tv_day)
    TextView tvDay;
    @BindView(R.id.tv_feedback)
    TextView tvFeedback;
    @BindView(R.id.activity_setting)
    LinearLayout activitySetting;
//    @BindView(R.id.v1)
//    View v1;
//    @BindView(R.id.v2)
//    View v2;
//    @BindView(R.id.v3)
//    View v3;
//    @BindView(R.id.v4)
//    View v4;
//    @BindView(R.id.v5)
//    View v5;
    private WeakHandler weakHandler;
    private int themeMode;//模式标记 ，1 夜间， 2 日照

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        ThemeManager.registerThemeChangeListener(this);
        FeedbackAPI.init(getApplication(), Constant.ALIBAICHUAN_KEY);
        themeMode = SharedPreferenceUtils.getInt(Constant.THEMEMODE);
        if (themeMode == 1) {
            switchDay.setChecked(true);
            ThemeManager.setThemeMode(ThemeManager.ThemeMode.NIGHT);
        } else {
            switchDay.setChecked(false);
            ThemeManager.setThemeMode(ThemeManager.ThemeMode.DAY);
        }
        initTheme();
        weakHandler = new WeakHandler();
        toolbar.setTitle("更多");
        toolbar.setContentInsetStartWithNavigation(0);
        toolbar.setTitleTextAppearance(this, R.style.Theme_ToolBar_Base_Title);//修改主标题的外观，包括文字颜色，文字大小等

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        try {
            String size = DataCleanManager.getTotalCacheSize(this);
            tvCacheSize.setText(size);
        } catch (Exception e) {
            e.printStackTrace();
        }

        switchDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ThemeManager.setThemeMode(ThemeManager.ThemeMode.NIGHT);
                    SharedPreferenceUtils.setInt(Constant.THEMEMODE, 1);
                } else {
                    ThemeManager.setThemeMode(ThemeManager.ThemeMode.DAY);
                    SharedPreferenceUtils.setInt(Constant.THEMEMODE, 2);
                }
            }
        });
    }

    @OnClick({R.id.rl_clear_cache, R.id.tv_collection, R.id.tv_privacy,R.id.tv_feedback})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_clear_cache:
                if (builder == null) {
                    initClearDialog();
                }
                builder.show();
                break;
            case R.id.tv_collection:
                startActivity(CollectionActivity.class);
                break;
            case R.id.tv_privacy:
                startActivity(PrivacyActivity.class);
                break;
            case R.id.tv_feedback:
                FeedbackAPI.openFeedbackActivity();
                break;
            default:
                break;
        }
    }

    /**
     * 初始化清除缓存dialog
     */
    AlertDialog.Builder builder;

    private void initClearDialog() {
        builder = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("确定要清除本地缓存吗")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataCleanManager.clearAllCache(SettingActivity.this);
                        tvCacheSize.setText("0k");
                    }
                });
    }

    public void initTheme() {
        tvPrivacy.setTextColor(getResources().getColor(ThemeManager.getCurrentThemeRes(this, R.color.textColor)));
        tvFeedback.setTextColor(getResources().getColor(ThemeManager.getCurrentThemeRes(this, R.color.textColor)));
        tvCache.setTextColor(getResources().getColor(ThemeManager.getCurrentThemeRes(this, R.color.textColor)));
        tvCollection.setTextColor(getResources().getColor(ThemeManager.getCurrentThemeRes(this, R.color.textColor)));
        tvCacheSize.setTextColor(getResources().getColor(ThemeManager.getCurrentThemeRes(this, R.color.textColor)));
        tvDay.setTextColor(getResources().getColor(ThemeManager.getCurrentThemeRes(this, R.color.textColor)));
//        v1.setBackgroundColor(getResources().getColor(ThemeManager.getCurrentThemeRes(this, R.color.line_colr)));
//        v2.setBackgroundColor(getResources().getColor(ThemeManager.getCurrentThemeRes(this, R.color.line_colr)));
//        v3.setBackgroundColor(getResources().getColor(ThemeManager.getCurrentThemeRes(this, R.color.line_colr)));
//        v4.setBackgroundColor(getResources().getColor(ThemeManager.getCurrentThemeRes(this, R.color.line_colr)));
//        v5.setBackgroundColor(getResources().getColor(ThemeManager.getCurrentThemeRes(this, R.color.line_colr)));
        activitySetting.setBackgroundColor(getResources().getColor(ThemeManager.getCurrentThemeRes(this, R.color.backgroundColor)));
        toolbar.setBackgroundColor(getResources().getColor(ThemeManager.getCurrentThemeRes(this, R.color.colorPrimary)));
        // 设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(ThemeManager.getCurrentThemeRes(this, R.color.colorPrimaryDark)));
        }
    }

    @Override
    public void onThemeChanged() {
        initTheme();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        int mode = SharedPreferenceUtils.getInt(Constant.THEMEMODE);
        if (mode != themeMode) {
            ThemeMode tm = new ThemeMode();
            tm.setMode(mode);
            Logger.d("mode:" + mode);
            EventBus.getDefault().post(tm);
        }
        ThemeManager.unregisterThemeChangeListener(this);
    }

}

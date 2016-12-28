package cn.xiandu.app.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.badoo.mobile.util.WeakHandler;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xiandu.app.utils.CommonTool;
import cn.xiandu.app.utils.Constant;
import cn.xiandu.app.utils.SharedPreferenceUtils;
import okhttp3.Call;

public class WelcomeActivity extends BaseActivity {

    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.activity_welcome)
    RelativeLayout activityWelcome;
    private WeakHandler weakHandler;

    private int[] images = new int[]{R.drawable.welcome, R.drawable.welcome1, R.drawable.welcome2, R.drawable.welcome3, R.drawable.welcome4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        // 不显示系统的标题栏，保证windowBackground和界面activity_main的大小一样，显示在屏幕不会有错位（去掉这一行试试就知道效果了）
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);
        weakHandler = new WeakHandler();
        //随机生成启动页面背景图
        int max = 3;
        int min = 0;
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;//生成指定区间内的随机数
        image.setImageResource(images[s]);
        String channel = SharedPreferenceUtils.getString(Constant.CHANNEL_KEY);
        if (!TextUtils.isEmpty(channel)) {
            weakHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isFinishing()){
                        return ;
                    }
                    startActivity(MainActivity.class);
                    finish();
                }
            }, 1000);
            return ;
        }
        getData();
    }

    /**
     * 获取频道id 、name
     */
    private void getData() {
        if (!CommonTool.isNetworkConnected(this)) {
            weakHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isFinishing()){
                        return ;
                    }
                    startActivity(MainActivity.class);
                }
            }, 2000);
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
                        startActivity(MainActivity.class);
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
                            }
                            if (isFinishing()){
                                return ;
                            }
                            startActivity(MainActivity.class);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }
}

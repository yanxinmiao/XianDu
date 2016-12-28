package cn.xiandu.app.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.umeng.analytics.MobclickAgent;

import cn.xiandu.app.utils.AppManager;

/**
 * Created by Clock on 2016/2/3.
 */
public class BaseActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppManager.getAppManager().addActivity(this);
        /**
         * Toolbar 时候需要先隐藏掉系统原先的导航栏，网上很多人都说给Activity设置一个NoActionBar的Theme。但个人觉得有点小题大做了，
         * 所以这里我直接在BaseActivity中调用 supportRequestWindowFeature(Window.FEATURE_NO_TITLE) 去掉了默认的导航栏
         *
         * （注意，我的BaseActivity是继承了AppCompatActivity的，如果是继承Activity就应该调用requestWindowFeature(Window.FEATURE_NO_TITLE)）；
         *
         */
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    /**
     *
     * @param cls
     * @param key
     * @param value
     * @param requestCode
     */
    protected void startActivityForResult(Class<?> cls,String key, String value ,int requestCode) {
        Intent intent = new Intent(this, cls);
        intent.putExtra(key,value);
        startActivityForResult(intent, requestCode);
        //overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
    }
    protected void startActivityForResult(Class<?> cls,String key, int value ,int requestCode) {
        Intent intent = new Intent(this, cls);
        intent.putExtra(key, value);
        startActivityForResult(intent, requestCode);
        //overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
    }
    /**
     *
     * @param className
     * @param key
     * @param value
     */
    public void startActivity(Class<?> className, String key, String value) {
        Intent intent = new Intent(this, className);
        intent.putExtra(key, value);
        startActivity(intent);
        //overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }
    public void startActivity(Class<?> className, String key, int value) {
        Intent intent = new Intent(this, className);
        intent.putExtra(key, value);
        startActivity(intent);
        //overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    /**
     *
     * @param className
     */
    public void startActivity(Class<?> className) {
        Intent intent = new Intent(this, className);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    /**
     *
     * @return
     */
    public boolean isNetworkConnected() {
        // TODO Auto-generated method stub
        ConnectivityManager mConnectivityManager = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable();
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }
}

package cn.xiandu.app.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;

import cn.xiandu.app.MyApplication;

public class PathUtils {
    
    private static final String TAG = "PathUtils";
    
	private static final String DEFAULT_PACKAGENAME_PATH = Environment.getExternalStorageDirectory() + "/Android/data/jokeapp.demo.com.jokeapp";
	private static String DEFAULT_COLOR_TEST_PATH = DEFAULT_PACKAGENAME_PATH + "/color_test/";
	private static String DEFAULT_PUB_IMAGE_CACHE = DEFAULT_PACKAGENAME_PATH + "/image_pub/";
	private static String DEFAULT_TEST_RESULT_PATH = DEFAULT_PACKAGENAME_PATH + "/result/";
	
	static {
	    try {
    	    String mCachePath = "";
    	    Context context = MyApplication.getApplication().getApplicationContext();
            if(context != null){
                File cacheDir = context.getExternalCacheDir();
                if(cacheDir != null){
                    mCachePath = cacheDir.getPath();
                }
            }
            if (!TextUtils.isEmpty(mCachePath)) {
                DEFAULT_COLOR_TEST_PATH = mCachePath + "/color_test/";
                DEFAULT_PUB_IMAGE_CACHE = mCachePath + "/image_pub/";
                DEFAULT_TEST_RESULT_PATH = mCachePath + "/result/";
            }
	    }catch(Exception e) {
	        Log.w(TAG, e);
	    }
	}
    /**
     * 获取视觉测试路径
     * @return
     */
    public static String getColorTestPath() {
        File saveFolder = new File(DEFAULT_COLOR_TEST_PATH);
        if (!saveFolder.exists()) {
            saveFolder.mkdirs();
        }
        return DEFAULT_COLOR_TEST_PATH;
    }
    
    public static String getPubImageCachePath() {
        File cacheFolder = new File(DEFAULT_PUB_IMAGE_CACHE);
        if(!cacheFolder.exists()) {
            cacheFolder.mkdirs();
        }
        return DEFAULT_PUB_IMAGE_CACHE;
    }
    
    public static String getTestResultPath() {
        File cacheFolder = new File(DEFAULT_TEST_RESULT_PATH);
        if(!cacheFolder.exists()) {
            cacheFolder.mkdirs();
        }
        return DEFAULT_TEST_RESULT_PATH;
    }
}

package cn.xiandu.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import cn.xiandu.app.MyApplication;


public class SharedPreferenceUtils {

	public static void setString(String key,String value){
		SharedPreferences sp = MyApplication.getApplication().getSharedPreferences(Constant.SHAREDPREFERENCE_NAME, Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putString(key, value);
		edit.commit();
	}

	public static void setBoolean(String key,boolean value){
		SharedPreferences sp = MyApplication.getApplication().getSharedPreferences(Constant.SHAREDPREFERENCE_NAME,
				Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putBoolean(key, value);
		edit.commit();
	}
	public static String getString(String key){
		SharedPreferences sp = MyApplication.getApplication().getSharedPreferences(Constant.SHAREDPREFERENCE_NAME,
				Context.MODE_PRIVATE);
		String str = sp.getString(key, "");
		return str;
	}

	/**
	 *
	 * @param key
	 * @param flag 默认值
	 * @return
	 */
	public static boolean getBoolean(String key,boolean flag){
		SharedPreferences sp = MyApplication.getApplication().getSharedPreferences(Constant.SHAREDPREFERENCE_NAME, Context.MODE_PRIVATE);
		boolean str = sp.getBoolean(key, flag);
		return str;
	}

	public static void clearString(String key){
		SharedPreferences sp = MyApplication.getApplication().getSharedPreferences(Constant.SHAREDPREFERENCE_NAME, Context.MODE_PRIVATE);
		Editor edit = sp.edit().remove(key);
		edit.commit();
	}
	public static void setInt(String key,int value){
		SharedPreferences sp = MyApplication.getApplication().getSharedPreferences(Constant.SHAREDPREFERENCE_NAME, Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putInt(key, value);
		edit.commit();
	}

	public static int getInt(String key){
		SharedPreferences sp = MyApplication.getApplication().getSharedPreferences(Constant.SHAREDPREFERENCE_NAME, Context.MODE_PRIVATE);
		int i = sp.getInt(key, 0);
		return i;
	}
	public static int getInt(String key,int f){
		SharedPreferences sp = MyApplication.getApplication().getSharedPreferences(Constant.SHAREDPREFERENCE_NAME, Context.MODE_PRIVATE);
		int i = sp.getInt(key, f);
		return i;
	}
	public static void setLong(String key,long value){
		SharedPreferences sp = MyApplication.getApplication().getSharedPreferences(Constant.SHAREDPREFERENCE_NAME, Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putLong(key, value);
		edit.commit();
	}

	public static long getLong(String key){
		SharedPreferences sp = MyApplication.getApplication().getSharedPreferences(Constant.SHAREDPREFERENCE_NAME, Context.MODE_PRIVATE);
		long i = sp.getLong(key, 0);
		return i;
	}

	public static void setString(Context context,String key,String value){
		SharedPreferences sp = context.getSharedPreferences(Constant.SHAREDPREFERENCE_NAME,Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putString(key, value);
		edit.commit();				
	}
	
	public static String getString(Context context,String key){
		SharedPreferences sp = context.getSharedPreferences(Constant.SHAREDPREFERENCE_NAME,Context.MODE_PRIVATE);
		String str = sp.getString(key, "");		
		return str;
	}

	public static void clearString(Context context,String key){
		SharedPreferences sp = context.getSharedPreferences(Constant.SHAREDPREFERENCE_NAME,Context.MODE_PRIVATE);
		Editor edit = sp.edit().remove(key);
		edit.commit();
	}
	public static void setInt(Context context,String key,int value){
		SharedPreferences sp = context.getSharedPreferences(Constant.SHAREDPREFERENCE_NAME,Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putInt(key, value);
		edit.commit();				
	}
	
	public static int getInt(Context context,String key){
		SharedPreferences sp = context.getSharedPreferences(Constant.SHAREDPREFERENCE_NAME,Context.MODE_PRIVATE);
		int i = sp.getInt(key, 0);		
		return i;
	}
	public static void setLong(Context context,String key,long value){
		SharedPreferences sp = context.getSharedPreferences(Constant.SHAREDPREFERENCE_NAME,Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putLong(key, value);
		edit.commit();
	}

	public static long getLong(Context context,String key){
		SharedPreferences sp = context.getSharedPreferences(Constant.SHAREDPREFERENCE_NAME,Context.MODE_PRIVATE);
		long i = sp.getLong(key, 0);
		return i;
	}
}

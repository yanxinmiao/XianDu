package cn.xiandu.app.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cn.xiandu.app.MyApplication;
import cn.xiandu.app.activity.R;


/**
 * Toast提示工具类
 * 
 * @author liulm
 *
 */
public class ToastUtils {

	private static Toast mToast;

	/**
	 * 显示一个居于界面正中间显示的Toast(LENGTH_SHORT)
	 * @param message  要显示的文字
	 */
	public static void showMyToast(Context context ,String message) {
		try {
			View view = View.inflate(context, R.layout.toast_view, null);
			TextView tv_toast_content = (TextView) view.findViewById(R.id.tv_toast_content);
			hideToast();
			mToast = new Toast(MyApplication.getApplication());
			mToast.setDuration(Toast.LENGTH_LONG);
			tv_toast_content.setText(message);
			mToast.setView(view);
			mToast.setGravity(Gravity.BOTTOM, 0, 100);
			mToast.show();
		} catch (Exception e) {
		}
	}

	/**
	 *
	 * @param context
	 * @param message 资源文件
	 */
	public static void showMyToast(Context context, int message) {
		try {
			View view = View.inflate(context, R.layout.toast_view, null);
			TextView tv_toast_content = (TextView) view.findViewById(R.id.tv_toast_content);

			mToast = new Toast(context);
			mToast.setDuration(Toast.LENGTH_LONG);
			mToast.setView(view);
			tv_toast_content.setText(message);
			mToast.setGravity(Gravity.BOTTOM, 0, 100);
			mToast.show();
		} catch (Exception e) {
		}
	}
	/**
	 * 关闭一个正在显示的Toast
	 */
	public static void hideToast() {
		if (mToast != null) {
			mToast.cancel();
		}
	}

	public static void showMyToast(String message) {
		try {
			if (mToast == null){
				View view = View.inflate(MyApplication.getApplication(), R.layout.toast_view, null);
				TextView tvContent = (TextView) view.findViewById(R.id.tv_toast_content);
//			hideToast();
				mToast = new Toast(MyApplication.getApplication());
				mToast.setDuration(Toast.LENGTH_SHORT);
				tvContent.setText(message);
				mToast.setView(view);
				mToast.setGravity(Gravity.BOTTOM, 0, 100);
			}else{
				TextView tvContent = (TextView) mToast.getView().findViewById(R.id.tv_toast_content);
				tvContent.setText(message);
			}
			mToast.show();
		} catch (Exception e) {
		}
	}
	public static void showMyToast(int message) {
		try {
			if (mToast == null){
				View view = View.inflate(MyApplication.getApplication(), R.layout.toast_view, null);
				TextView tvContent = (TextView) view.findViewById(R.id.tv_toast_content);
//			hideToast();
				mToast = new Toast(MyApplication.getApplication());
				mToast.setDuration(Toast.LENGTH_SHORT);
				tvContent.setText(message);
				mToast.setView(view);
				mToast.setGravity(Gravity.BOTTOM, 0, 100);
			}else{
				TextView tvContent = (TextView) mToast.getView().findViewById(R.id.tv_toast_content);
				tvContent.setText(message);
			}
			mToast.show();
		} catch (Exception e) {
		}
	}
}

package com.zmide.lit.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.WindowManager;

import com.blankj.utilcode.util.ScreenUtils;

/**
 * The type M windows utils.
 */
public class MWindowsUtils {
	@SuppressLint("StaticFieldLeak")
	private static Activity activity;
	
	/**
	 * Init.
	 *
	 * @param ac the ac
	 */
	public static void init(Activity ac) {
		activity = ac;
	}
	
	/**
	 * Get width int.
	 *
	 * @return the int
	 */
	public static int getWidth() {
		WindowManager wm1 = activity.getWindowManager();
		return wm1.getDefaultDisplay().getWidth();
	}
	
	/**
	 * Get height int.
	 *
	 * @return the int
	 */
	public static int getHeight() {
		WindowManager wm1 = activity.getWindowManager();
		return wm1.getDefaultDisplay().getHeight();
	}
	
	/**
	 * Dp 2 px int.
	 *
	 * @param dpValue the dp value
	 * @return the int
	 */
	public static int dp2px(float dpValue) {
		float scale = activity.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
	
	
	/**
	 * Gets status bar height.
	 *
	 * @return the status bar height
	 */
	//获取状态栏高度
	public static int getStatusBarHeight() {
		int statusHeight = -1;
		
		try {
			@SuppressLint("PrivateApi") Class e = Class.forName("com.android.internal.R$dimen");
			Object object = e.newInstance();
			int height = Integer.parseInt("0" + e.getField("status_bar_height").get(object));
			statusHeight = activity.getResources().getDimensionPixelSize(height);
		} catch (Exception var5) {
			var5.printStackTrace();
		}
		
		return statusHeight;
	}
	
	
	/**
	 * Switch full screen.
	 *
	 * @param isChecked the is checked
	 */
	public static void switchFullScreen(Activity a, boolean isChecked) {
		/*if (isChecked) {
			//切换到全屏模式
			//添加一个全屏的标记
			activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
			BarUtils.setNavBarVisibility(activity,false);
		} else {
			//切换到默认模式
			//清除全屏标记
			final WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
			attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
			activity.getWindow().setAttributes(attrs);
			activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		}*/
		if (isChecked)
			ScreenUtils.setFullScreen(a);
		else
			ScreenUtils.setNonFullScreen(a);
	}
}

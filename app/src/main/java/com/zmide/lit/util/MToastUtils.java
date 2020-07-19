package com.zmide.lit.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.zmide.lit.R;
import com.zmide.lit.view.ToastBox;

import java.lang.reflect.Field;

public class MToastUtils {
	public final static int LENGTH_SHORT = 0;
	public final static int LENGTH_LONG = 1;
	@SuppressLint("StaticFieldLeak")
	private static Context context;
	private static ToastBox toast;
	
	public static void init(Context c) {
		context = c;
	}
	
	public static Toast makeText(String msg, int time) {
		Toast toast = new Toast(context);
		@SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.utils_toast, null);
		((TextView) view.findViewById(R.id.toastText)).setText(msg);
		view.findViewById(R.id.toastButton).setVisibility(View.GONE);
		toast.setDuration(time);
		toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 400);
		toast.setView(view);
		return toast;
	}
	
	public static Toast makeText(String msg) {
		Toast toast = new Toast(context);
		@SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.utils_toast, null);
		((TextView) view.findViewById(R.id.toastText)).setText(msg);
		view.findViewById(R.id.toastButton).setVisibility(View.GONE);
		toast.setDuration(MToastUtils.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 400);
		toast.setView(view);
		return toast;
	}
	
	public static ToastBox makeText(Activity context, String msg, String action, OnClickListener listener, int time) {
		/*Toast toast = new Toast(context);
		@SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.utils_toast, null);
		((TextView) view.findViewById(R.id.toastText)).setText(msg);
		if (listener != null)
			view.findViewById(R.id.toastButton).setOnClickListener(listener);
		((TextView) view.findViewById(R.id.toastButton)).setText(action);
		toast.setDuration(time);
		toast.setView(view);
		toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 400);
		try {
			Object mTN;
			mTN = getField(toast, "mTN");
			if (mTN != null) {
				Object mParams = getField(mTN, "mParams");
				if (mParams instanceof WindowManager.LayoutParams) {
					WindowManager.LayoutParams params = (WindowManager.LayoutParams) mParams;
					//显示与隐藏动画
					params.windowAnimations = R.style.ClickToast;
					//Toast可点击
					params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
							| WindowManager.LayoutParams.FLAG_LOCAL_FOCUS_MODE;
					
					//设置viewgroup宽高
					params.width = WindowManager.LayoutParams.WRAP_CONTENT; //设置Toast宽度为屏幕宽度
					params.height = WindowManager.LayoutParams.WRAP_CONTENT; //设置高度
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		if (toast == null)
			toast = new ToastBox(context);
		if (time == MToastUtils.LENGTH_SHORT)
			time = 2000;
		else if (time == MToastUtils.LENGTH_LONG)
			time = 3000;
		toast.setContext(msg, action, listener, time);
		return toast;
	}
	
	public static Toast makeText(String msg, String action, OnClickListener listener) {
		Toast toast = new Toast(context);
		@SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.utils_toast, null);
		((TextView) view.findViewById(R.id.toastText)).setText(msg);
		if (listener != null)
			view.findViewById(R.id.toastButton).setOnClickListener(listener);
		((TextView) view.findViewById(R.id.toastButton)).setText(action);
		toast.setDuration(MToastUtils.LENGTH_SHORT);
		toast.setView(view);
		toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 400);
		try {
			Object mTN;
			mTN = getField(toast, "mTN");
			if (mTN != null) {
				Object mParams = getField(mTN, "mParams");
				if (mParams instanceof WindowManager.LayoutParams) {
					WindowManager.LayoutParams params = (WindowManager.LayoutParams) mParams;
					//显示与隐藏动画
					params.windowAnimations = R.style.ClickToast;
					//Toast可点击
					params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
							| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
					
					//设置viewgroup宽高
					params.width = WindowManager.LayoutParams.WRAP_CONTENT; //设置Toast宽度为屏幕宽度
					params.height = WindowManager.LayoutParams.WRAP_CONTENT; //设置高度
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return toast;
	}
	
	/**
	 * 反射字段
	 *
	 * @param object    要反射的对象
	 * @param fieldName 要反射的字段名称
	 */
	private static Object getField(Object object, String fieldName)
			throws NoSuchFieldException, IllegalAccessException {
		Field field = object.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		return field.get(object);
	}
	
}

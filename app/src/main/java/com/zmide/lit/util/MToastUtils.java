package com.zmide.lit.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.zmide.lit.R;
import com.zmide.lit.view.ToastBox;

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

	public static ToastBox makeText(Activity activity, String msg, Exception e) {
		StringBuilder stacks = new StringBuilder();
		for (StackTraceElement stack : e.getStackTrace()) {
			stacks.append(stack).append("\n");
		}
		String se = "Cause:\n" + e.getCause() + "\n\nMessage:\n" + e.getMessage() + "\n\nStackTrace:\n" + stacks;
		return makeText(activity, msg, "错误详情", view -> new MDialogUtils.Builder(activity)
				.setCopyText("复制")
				.setCopyContext(se)
				.setMessage(se)
				.setPositiveButton("知道了", (dialog, which) -> {
				})
				.setTitle("错误详情")
				.create().show(), Toast.LENGTH_LONG);
	}

	public static ToastBox makeText(Activity activity, String msg, String action, OnClickListener listener, int time) {
		toast = new ToastBox(activity);
		if (time == MToastUtils.LENGTH_SHORT)
			time = 2000;
		else if (time == MToastUtils.LENGTH_LONG)
			time = 3000;
		toast.setContext(msg, action, listener, time);
		return toast;
	}
/*
	public static ToastBox makeText(String msg, String action, OnClickListener listener, int time) {

	}*/

}

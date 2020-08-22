package com.zmide.lit.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Arrays;

public class MExceptionUtils {
	@SuppressLint("StaticFieldLeak")
	private static Activity activity;
	private static ArrayList<Exception> temp = new ArrayList<>();

	public static void init(Activity c) {
		activity = c;
		for (Exception e : temp) {
			if ("true".equals(MSharedPreferenceUtils.getSharedPreference().getString("debug", "false"))) {
				MToastUtils.makeText(activity, "发生异常", e).show();
			}
		}
	}

	public static void reportException(Exception e) {
		if (activity != null) {
			if ("true".equals(MSharedPreferenceUtils.getSharedPreference().getString("debug", "false"))) {
				MToastUtils.makeText(activity, "发生异常", e).show();
			}
		} else {
			temp.add(e);
		}
		MobclickAgent.reportError(activity, e);
		Log.e("Exception", "\nMessage:	" + e.getMessage() + "Stack:	" + "\n" + Arrays.toString(e.getStackTrace()).replace(",", "\n"));
	}
}

package com.zmide.lit.util;

import android.annotation.SuppressLint;
import android.content.Context;

import com.umeng.analytics.MobclickAgent;

public class MExceptionUtils {
	@SuppressLint("StaticFieldLeak")
	private static Context context;
	
	public static void init(Context c) {
		context = c;
	}
	
	public static void reportException(Exception e) {
		MobclickAgent.reportError(context, e);
	}
}

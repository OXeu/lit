package com.zmide.lit.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.umeng.analytics.MobclickAgent;

import java.util.Arrays;

public class MExceptionUtils {
	@SuppressLint("StaticFieldLeak")
	private static Context context;
	
	public static void init(Context c) {
		context = c;
	}
	
	public static void reportException(Exception e) {
		MobclickAgent.reportError(context, e);
		Log.e("Exception", "\nMessage:	" + e.getMessage() + "Stack:	" + "\n" + Arrays.toString(e.getStackTrace()).replace(",", "\n"));
	}
}

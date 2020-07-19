package com.zmide.lit.util;

import android.content.Context;
import android.content.SharedPreferences;

public class MSharedPreferenceUtils {
	private static SharedPreferences mSharedPreferenceSetting, mSharedPreferenceWebView;
	
	public static void init(Context contextTemp) {
		mSharedPreferenceSetting = contextTemp.getSharedPreferences("setting", Context.MODE_PRIVATE);
		mSharedPreferenceWebView = contextTemp.getSharedPreferences("setting_webview", Context.MODE_PRIVATE);
	}
	
	public static SharedPreferences getSharedPreference() {
		return mSharedPreferenceSetting;
	}
	
	
	public static SharedPreferences getWebViewSharedPreference() {
		return mSharedPreferenceWebView;
	}
	
	
}

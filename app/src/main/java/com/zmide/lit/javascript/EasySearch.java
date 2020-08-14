package com.zmide.lit.javascript;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.webkit.JavascriptInterface;

import com.zmide.lit.main.WebEnvironment;

public class EasySearch {
	@SuppressLint("StaticFieldLeak")
	private static Activity ma;
	private static EasySearch instance;
	private static WebEnvironment mWebE;
	
	public static void init(Activity context, WebEnvironment we) {
		ma = context;
		mWebE = we;
		
	}
	
	public static EasySearch getInstance() {
		if (instance == null)
			instance = new EasySearch();
		return instance;
	}
	
	@JavascriptInterface
	public void postMessage(String msg) {
		if (msg.contains("activate")) {
			//打开搜索框
			//ma.runOnUiThread(() -> mWebE.openSearchBar());
		}
	}
}

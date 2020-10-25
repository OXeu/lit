package com.zmide.lit.main;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.zmide.lit.R;
import com.zmide.lit.ui.MainActivity;
import com.zmide.lit.util.MFileUtils;
import com.zmide.lit.util.MSharedPreferenceUtils;
import com.zmide.lit.util.MWebStateSaveUtils;
import com.zmide.lit.view.LitWebView;
import com.zmide.lit.main.bottom.ClassicalBottomBar;
import com.zmide.lit.main.bottom.FunctionalBottomBar;
import com.zmide.lit.main.bottom.SimplifyBottomBar;
import android.content.Context;
import android.app.Activity;
import android.widget.RelativeLayout;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;
import com.zmide.lit.util.MToastUtils;
import android.util.Log;
import com.zmide.lit.view.TouchableRelativeLayout;

public class MWeb {
	public int sid;
	private Activity a;
	/**
	 * 功能
	 * 初始化WebView
	 * 获取WebView基本信息
	 * 获取WebView，并进行防空
	 */
	private View view;
	private String url;
	private String title;
	private String icon;
	private SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener = (sharedPreferences, key) -> {
		canRefresh();
		initBtBar();
	};

	public MWeb(Activity activity) {
		@SuppressLint("InflateParams") View view = LayoutInflater.from(activity).inflate(R.layout.web_layout, null);
		this.view = view;
		this.a = activity;
		getSwipe().setColorSchemeResources(R.color.accentColor, R.color.accentColor2);
		getSwipe().setOnRefreshListener(() -> getWebView().reload());
		getSwipe().setOnChildScrollUpCallback((parent, child) -> getWebView().getScrollY() > 0);
		canRefresh();
		initBtBar();
		MSharedPreferenceUtils.getWebViewSharedPreference().registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
	}
	
	public MWeb(Activity activity, WebView webView) {
		@SuppressLint("InflateParams") View view = LayoutInflater.from(activity).inflate(R.layout.web_layout, null);
		LitWebView LitWebView = view.findViewById(R.id.mainWebView);
		if (webView instanceof LitWebView)
			LitWebView.setCodeId(((LitWebView) webView).getCodeId());
		LitWebView.loadUrl(webView.getUrl());
		this.view = view;
		this.a = activity;
		getSwipe().setColorSchemeResources(R.color.accentColor, R.color.accentColor2);
		getSwipe().setOnRefreshListener(()->getWebView().reload());
		getSwipe().setOnChildScrollUpCallback((parent, child) -> getWebView().getScrollY() > 0);
		canRefresh();
		MSharedPreferenceUtils.getWebViewSharedPreference().registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
	}
	
	private void canRefresh() {
		if (MSharedPreferenceUtils.getWebViewSharedPreference().getString("can_refresh", "false").equals("true")) {
			getSwipe().setEnabled(true);
		} else {
			getSwipe().setEnabled(false);
		}
	}
	
	public View getView() {
		return view;
	}
	
	public void setView(View view) {
		this.view = view;
	}
	
	public String getUrl() {
		return url == null ? getWebView().getUrl()+"" : ""+url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getTitle() {
		return title == null ? getWebView().getTitle()+"" : ""+title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getIcon() {
		Bitmap favicon = getWebView().getFavicon();
		if (favicon != null)
		return icon == null ? MFileUtils.saveFile(favicon, MFileUtils.getBitmapMd5(favicon) + ".png", false) + "" : "" + icon;
		return null;
	}
	
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	public LitWebView getWebView() {
		if (view != null) {
			LitWebView webView = view.findViewById(R.id.mainWebView);
			if (webView == null) {
				if (sid == 0)
					return createWebView();
				else
					return resumeWebView(sid);
			}
			return webView;
		}
		//火星救援
		return createWebView();
	}
	
	private LitWebView createWebView() {
		@SuppressLint("InflateParams") View view = LayoutInflater.from(this.view.getContext()).inflate(R.layout.web_layout, null);
		LitWebView LitWebView = view.findViewById(R.id.mainWebView);
		this.view = view;
		if (!url.equals("")) {
			initWebView(LitWebView, url);
		}
		return LitWebView;
	}
	
	private LitWebView resumeWebView(int sid) {
		url = MWebStateSaveUtils.resumeState(sid);
		@SuppressLint("InflateParams") View view = LayoutInflater.from(this.view.getContext()).inflate(R.layout.web_layout, null);
		LitWebView LitWebView = view.findViewById(R.id.mainWebView);
		this.view = view;
		if (!url.equals("")) {
			initWebView(LitWebView, url);
		}
		return LitWebView;
	}
	
	public SwipeRefreshLayout getSwipe() {
		if (view != null) {
			return view.findViewById(R.id.mainSwipe);
		}
		return null;
	}
	
	public TouchableRelativeLayout getBtParent() {
		if (view != null) {
			return view.findViewById(R.id.bt_bar);
		}
		return null;
	}
	
	private void initWebView(LitWebView litWebView, String url) {
	
	}
	
	
	
	public int getCode() {
		return getWebView().getCodeId();
	}
	
	public void initBtBar(){
		getBtParent().setOnTouchListener(new OnTouchListener(){

				@Override
				public boolean onTouch(View view, MotionEvent event) {
					view.getParent().requestDisallowInterceptTouchEvent(true);
					RecyclerView rv = MainViewBindUtils.getWebRecyclerView();
					
					rv.onTouchEvent(event);
					return true;
				}
				
			});
		String type = MSharedPreferenceUtils.getSharedPreference().getString("bt_bar","0");
		switch(type){
			case "0"://经典底栏
			ClassicalBottomBar.loadBar();
			break;
			case "1"://手势底栏
			FunctionalBottomBar.loadBar();
			break;
			case "2"://简洁底栏
			SimplifyBottomBar.loadBar();
			break;
			}
		}
	
	
}

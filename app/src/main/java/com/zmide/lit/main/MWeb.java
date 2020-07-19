package com.zmide.lit.main;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.zmide.lit.R;
import com.zmide.lit.ui.MainActivity;
import com.zmide.lit.util.MFileUtils;
import com.zmide.lit.util.MWebStateSaveUtils;
import com.zmide.lit.view.LitWebView;

public class MWeb {
	public int sid;
	private MainActivity a;
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
	
	public MWeb(MainActivity activity) {
		@SuppressLint("InflateParams") View view = LayoutInflater.from(activity).inflate(R.layout.web_layout, null);
		this.view = view;
		this.a = activity;
		getSwipe().setColorSchemeResources(R.color.accentColor, R.color.accentColor2);
		getSwipe().setOnRefreshListener(() -> getWebView().reload());
		getSwipe().setOnChildScrollUpCallback((parent, child) -> getWebView().getScrollY() > 0);
	}
	
	
	public MWeb(MainActivity activity, WebView webView) {
		@SuppressLint("InflateParams") View view = LayoutInflater.from(activity).inflate(R.layout.web_layout, null);
		LitWebView LitWebView = view.findViewById(R.id.mainWebView);
		if (webView instanceof LitWebView)
			LitWebView.setCodeId(((LitWebView) webView).getCodeId());
		LitWebView.loadUrl(webView.getUrl());
		this.view = view;
		this.a = activity;
		getSwipe().setColorSchemeResources(R.color.accentColor, R.color.accentColor2);
		getSwipe().setOnRefreshListener(() -> getWebView().reload());
		getSwipe().setOnChildScrollUpCallback((parent, child) -> getWebView().getScrollY() > 0);
	}
	
	public View getView() {
		return view;
	}
	
	public void setView(View view) {
		this.view = view;
	}
	
	public String getUrl() {
		return url == null ? getWebView().getUrl() + "" : "" + url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getTitle() {
		return title == null ? getWebView().getTitle() + "" : "" + title;
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
	
	private void initWebView(LitWebView litWebView, String url) {
	
	}
	
	
	public int getCode() {
		return getWebView().getCodeId();
	}
}
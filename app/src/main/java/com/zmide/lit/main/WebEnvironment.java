package com.zmide.lit.main;

import android.view.View;

import com.zmide.lit.ui.MainActivity;

import static com.zmide.lit.main.MainViewBindUtils.getMainTitle;

public class WebEnvironment {
	private static MainActivity activity;
	
	/**
	 * WebEnvironment
	 * 提供WebContainer所需环境
	 * 初始化主页类
	 */
	
	public static void init(MainActivity mainActivity) {
		if (activity == null)
			activity = mainActivity;
		IndexEnvironment.init(activity);
	}
	
	
	public static void refreshFrame() {
		View view = WebContainer.getView();
		if (view != null) {
			WebContainerPlus.getViewHolder().getWebFrame().removeAllViews();
			WebContainerPlus.getViewHolder().getWebFrame().addView(view);
			MWeb mWeb = WebContainer.getWindow();
			getMainTitle().setText(mWeb.getTitle());
		}
	}
	
	public static void start() {
		//refreshFrame();
	}
}

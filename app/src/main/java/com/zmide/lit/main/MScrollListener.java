package com.zmide.lit.main;

import com.zmide.lit.view.LitWebView;

public class MScrollListener implements LitWebView.OnScrollChangeListener {
	@Override
	public void onPageToTop() {
		BallEnvironment.expandBallWithSetting();
	}
	
	@Override
	public void onPageToBottom() {
		BallEnvironment.shrinkBallWithSetting();
	}
}

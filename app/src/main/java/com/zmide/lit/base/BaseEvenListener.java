package com.zmide.lit.base;

import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;

public class BaseEvenListener {
	public void back(View view) {
		ActivityUtils.getActivityByView(view).finish();
	}
}

package com.zmide.lit.main;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.webkit.WebView;

import com.blankj.utilcode.util.BarUtils;
import com.zmide.lit.ui.MainActivity;
import com.zmide.lit.util.MBitmapUtils;

public class StatusEnvironment {
	private static MainActivity activity;
	
	public static void init(MainActivity mainActivity) {
		if (activity == null)
			activity = mainActivity;
	}
	
	public static void updateStatusColor(WebView view) {
		Bitmap var1 = MBitmapUtils.getBitmapFromView(view);
		if (var1 != null) {
			int var2 = var1.getPixel(0, 0);
			int var3 = Color.red(var2);
			int var4 = Color.green(var2);
			int var5 = Color.blue(var2);
			if (Build.VERSION.SDK_INT >= 23) {
				//updateViewColor(0);
				if (var3 > 200 && var4 > 200 && var5 > 200)
					activity.getWindow().getDecorView().setSystemUiVisibility(8192);
				else {
					activity.getWindow().getDecorView().setSystemUiVisibility(0);
					//updateViewColor(1);
				}
			}
			if (BarUtils.isStatusBarVisible(activity))
				BarUtils.setStatusBarColor(activity, var2);
			BarUtils.setNavBarColor(activity, var2);
			var1.recycle();
		}
	}
}

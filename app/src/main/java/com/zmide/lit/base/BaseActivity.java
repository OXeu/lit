package com.zmide.lit.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.BarUtils;
import com.umeng.analytics.MobclickAgent;
import com.zmide.lit.R;
import com.zmide.lit.interfaces.Dark;
import com.zmide.lit.skin.SkinFactory;
import com.zmide.lit.skin.SkinManager;
import com.zmide.lit.util.MToastUtils;
import com.zmide.lit.util.MWindowsUtils;
import com.zmide.lit.view.SwipeBackLayout;

public abstract class BaseActivity extends Activity implements Dark {
	
	
	/***封装toast对象**/
	private static Toast toast;
	
	/***获取TAG的activity名称**/
	public final String TAG = this.getClass().getSimpleName();
	
	private SwipeBackLayout layout;
	private SharedPreferences mSharedPreferences;
	
	private SkinFactory skinFactory = new SkinFactory();
	
	private int themeMode;
	
	@SuppressLint("InflateParams")
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MWindowsUtils.init(this);
		skinFactory = new SkinFactory();
		mSharedPreferences = getSharedPreferences("setting", Context.MODE_PRIVATE);
		changeFullscreen();
		LayoutInflater.from(this).setFactory2(skinFactory);
		//设置布局
		layout = (SwipeBackLayout) LayoutInflater.from(this).inflate(
				R.layout.base, null);
		layout.attachToActivity(this);
		//初始化控件
		//初始化背景
		//GradientDrawable aDrawable=new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,new int[]{0xffffff,0xffffff});
		//	new int[]{Color.parseColor("#a9a9ff"),Color.parseColor("#4ea2ff")});
		//findViewById(initBackground()).setBackground(aDrawable);
		//所有的activity 继承于此 就可以知道哪个activity在活动了
		
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			changeMode();
			changeFullscreen();
		}
	}
	
	@Override
	public void changeFullscreen() {
		if (mSharedPreferences.getString("isfullscreen", "false").equals("true")) {
			MWindowsUtils.switchFullScreen(this, true);
			BarUtils.setStatusBarColor(this, 0x00000000);
		} else {
			MWindowsUtils.switchFullScreen(this, false);
			BarUtils.transparentStatusBar(this);
		}
	}
	
	@Override
	public void changeMode() {
		themeMode = 0;
		switch (Integer.parseInt(mSharedPreferences.getString("themeMode", "2"))) {
			case 0://DAY
				themeMode = 0;
				break;
			case 1:
				themeMode = 1;
				break;
			default:
				int currentNightMode = getResources().getConfiguration().uiMode
						& Configuration.UI_MODE_NIGHT_MASK;
				switch (currentNightMode) {
					case Configuration.UI_MODE_NIGHT_NO: {
						themeMode = 0;
						// Night mode is not active, we're in day time
						break;
					}
					case Configuration.UI_MODE_NIGHT_YES: {
						themeMode = 1;
						// Night mode is active, we're at night!
						break;
					}
					case Configuration.UI_MODE_NIGHT_UNDEFINED: {
						// We don't know what mode we're in, assume notnight
					}
				}
				break;
		}
		if (themeMode == 0) {
			if (!SkinManager.getInstance().getSkinName().equals(""))
				SkinManager.getInstance().loadSkin("");
			BarUtils.setStatusBarLightMode(this, true);
			BarUtils.setNavBarColor(this, 0xffffffff);
			BarUtils.setNavBarLightMode(this, true);
			//StatusBarUtil.setNavigationColor(this,true);
		} else if (themeMode == 1) {
			if (!SkinManager.getInstance().getSkinName().equals("skin/dark.apk"))
				SkinManager.getInstance().loadSkin("skin/dark.apk");
			BarUtils.setStatusBarLightMode(this, false);
			BarUtils.setNavBarColor(this, 0xff303030);
			BarUtils.setNavBarLightMode(this, false);
			//StatusBarUtil.setNavigationColor(this,false);
		}
		runOnUiThread(() -> skinFactory.apply());
		layout.requestLayout();
	}
	
	@Override
	public boolean isNight() {
		themeMode = 0;
		switch (Integer.parseInt(mSharedPreferences.getString("themeMode", "2"))) {
			case 0://DAY
				themeMode = 0;
				break;
			case 1:
				themeMode = 1;
				break;
			default:
				int currentNightMode = getResources().getConfiguration().uiMode
						& Configuration.UI_MODE_NIGHT_MASK;
				switch (currentNightMode) {
					case Configuration.UI_MODE_NIGHT_NO: {
						themeMode = 0;
						// Night mode is not active, we're in day time
						break;
					}
					case Configuration.UI_MODE_NIGHT_YES: {
						themeMode = 1;
						// Night mode is active, we're at night!
						break;
					}
					case Configuration.UI_MODE_NIGHT_UNDEFINED: {
						// We don't know what mode we're in, assume notnight
					}
				}
				break;
		}
		return themeMode != 0;
	}
	
	
	public int getThemeMode() {
		return themeMode;
	}
	
	public void setThemeMode(int mode) {
		themeMode = mode;
	}
	
	
	@Override
	public void onConfigurationChanged(@NonNull Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		themeMode = 2;
		switch (Integer.parseInt(mSharedPreferences.getString("themeMode", "2"))) {
			case 0://DAY
			case 1:
				themeMode = 2;
				break;
			default:
				int currentNightMode = getResources().getConfiguration().uiMode
						& Configuration.UI_MODE_NIGHT_MASK;
				switch (currentNightMode) {
					case Configuration.UI_MODE_NIGHT_NO: {
						themeMode = 0;
						// Night mode is not active, we're in day time
						break;
					}
					case Configuration.UI_MODE_NIGHT_YES: {
						themeMode = 1;
						// Night mode is active, we're at night!
						break;
					}
					case Configuration.UI_MODE_NIGHT_UNDEFINED: {
						// We don't know what mode we're in, assume notnight
					}
				}
				break;
		}
		if (themeMode == 0) {
			if (!SkinManager.getInstance().getSkinName().equals(""))
				SkinManager.getInstance().loadSkin("");
			BarUtils.setStatusBarLightMode(this, true);
			BarUtils.setNavBarColor(this, 0xffffffff);
			BarUtils.setNavBarLightMode(this, true);
			//todo StatusBarUtil.setTranslucentStatus(this, true);
		} else if (themeMode == 1) {
			if (!SkinManager.getInstance().getSkinName().equals("skin/dark.apk"))
				SkinManager.getInstance().loadSkin("skin/dark.apk");
			BarUtils.setStatusBarLightMode(this, false);
			BarUtils.setNavBarColor(this, 0xff303030);
			BarUtils.setNavBarLightMode(this, false);
			
			//todo StatusBarUtil.setTranslucentStatus(this, false);
		}
		runOnUiThread(() -> skinFactory.apply());
	}
	
	/**
	 * 显示短toast
	 *
	 * @param msg string
	 */
	public void toast(final String msg) {
		runOnUiThread(() -> {
			toast = MToastUtils.makeText(msg);
			toast.show();
		});
	}
	
	@Override
	public void finish() {
		super.finish();
		//if(!TAG .equals( "MainActivity" ))
		//overridePendingTransition(0, R.anim.base_slide_right_out);
		//else
		/*
		 * R.anim.slide_in_right:新的Activity进入时的动画，这里是指OtherActivity进入时的动画
		 * R.anim.slide_out_left：旧的Activity出去时的动画，这里是指this进入时的动画
		 */
		//overridePendingTransition(0, R.anim.main_exit);
	}
	
	
	@Override
	protected void onStart() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = getWindow();
			//不加这个clearFlags好像就没效果
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.getDecorView().setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
							| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
			//设置导航栏透明(就是虚拟键那一栏)
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			//设置状态栏(或者叫通知栏)透明
			//ic_window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			window.setStatusBarColor(Color.TRANSPARENT);
		}
		super.onStart();
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
	}
	
	// Press the back button in mobile phone
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(0, R.anim.base_slide_right_out);
	}
	
}


package com.zmide.lit.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;

import com.swift.sandhook.SandHook;
import com.swift.sandhook.SandHookConfig;
import com.swift.sandhook.annotation.MethodParams;
import com.swift.sandhook.wrapper.HookErrorException;
import com.swift.sandhook.xposedcompat.XposedCompat;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.commonsdk.utils.Common;
import com.zmide.lit.BuildConfig;
import com.zmide.lit.base.hooker.ActivityHooker;
import com.zmide.lit.skin.SkinManager;
import com.zmide.lit.ui.MainActivity;
import com.zmide.lit.util.Chiper;
import com.zmide.lit.util.MDataBaseSettingUtils;
import com.zmide.lit.util.MExceptionUtils;
import com.zmide.lit.util.MFileUtils;
import com.zmide.lit.util.MSharedPreferenceUtils;
import com.zmide.lit.util.MToastUtils;
import com.zmide.lit.util.MWebStateSaveUtils;

import java.util.ArrayList;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class MApplication extends Application {
	
	private static Context mContext;
	
	public static Context getContext() {
		return mContext;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		mContext = getApplicationContext();
		MWebStateSaveUtils.init(this);
		MFileUtils.getDarkPath(this);
		SkinManager.getInstance().init(this);
		MSharedPreferenceUtils.init(this);
		MDataBaseSettingUtils.init(this);
		MExceptionUtils.init(this);
		MToastUtils.init(this);
		Common.init(this);
		UMConfigure.setLogEnabled(false);
		UMConfigure.init(getApplicationContext(),
				UMConfigure.DEVICE_TYPE_PHONE, null);
		
		
		
		SandHookConfig.DEBUG = BuildConfig.DEBUG;
		SandHook.disableVMInline();
		SandHook.tryDisableProfile(getPackageName());
		SandHook.disableDex2oatInline(false);
		try {
			hook();
		} catch (PackageManager.NameNotFoundException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (SandHookConfig.SDK_INT >= Build.VERSION_CODES.P) {
			SandHook.passApiCheck();
		}
	
		/*
		try {
			SandHook.addHookClass(VideoReplacer.SurfaceHooker.class, ActivityHooker.class);
		} catch (HookErrorException e) {
			e.printStackTrace();
			MToastUtils.makeText("Wrong").show();
		}*/
	}
	
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		/*XposedBridge.hookMethod(RWebViewFactory.getWebViewContextAndSetProvider.getMethod(),
				new XC_MethodHook() {
					@Override
					protected void afterHookedMethod(MethodHookParam param) throws Throwable {
						super.afterHookedMethod(param);
						Context webViewContext = (Context) param.getResult();
					}
				})*/
		//MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.LEGACY_AUTO);
	}
	
	public void hook() throws PackageManager.NameNotFoundException, ClassNotFoundException {
		//setup for xposed
		XposedCompat.cacheDir = getCacheDir();
		XposedCompat.context = this;
		XposedCompat.classLoader = getClassLoader();
		XposedCompat.isFirstApplication= true;
//do hook
		
		Context mmsCtx = getContext().createPackageContext("com.google.android.webview",
				Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
		
		@SuppressLint("PrivateApi") Class<?> mStubClass = Class.forName("org.chromium.media.MediaPlayerBridge",true,mmsCtx.getClassLoader());
		//2.拿到本地对象类
		/*XposedHelpers.findAndHookMethod(mStubClass,"setDataSource",String.class,String.class,String.class,boolean.class, new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
				super.beforeHookedMethod(param);
				Log.e("XposedCompat", "beforeHookedMethod: " + param.method.getName());
			}
			
			@Override
			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
				super.afterHookedMethod(param);
				Log.e("XposedCompat", "afterHookedMethod: " + param.method.getName());
				//Surface surface = ((Surface)param.getResult())
			}
		});
		*/
		XposedHelpers.findAndHookConstructor(SurfaceView.class, Context.class, new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
				super.beforeHookedMethod(param);
				Log.e("XposedCompat", "beforeHookedMethod: " + param.method.getName());
			}
			
			@Override
			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
				super.afterHookedMethod(param);
				Log.e("XposedCompat", "afterHookedMethod: " + param.method.getName());
				//SurfaceView surface = ((SurfaceView)param.getResult());
			}
		});
	}
}

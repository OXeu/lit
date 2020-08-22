package com.zmide.lit.base;

import android.app.Application;
import android.content.Context;

import com.umeng.commonsdk.UMConfigure;
import com.umeng.commonsdk.utils.Common;
import com.zmide.lit.skin.SkinManager;
import com.zmide.lit.util.MDataBaseSettingUtils;
import com.zmide.lit.util.MFileUtils;
import com.zmide.lit.util.MSharedPreferenceUtils;
import com.zmide.lit.util.MToastUtils;
import com.zmide.lit.util.MWebStateSaveUtils;

public class MApplication extends Application {
	
	private static Context mContext;
	
	public static Context getContext() {
		return mContext;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		mContext = getApplicationContext();
		MSharedPreferenceUtils.init(this);
		MToastUtils.init(this);
		MWebStateSaveUtils.init(this);
		MFileUtils.getDarkPath(this);
		SkinManager.getInstance().init(this);
		MDataBaseSettingUtils.init(this);
		Common.init(this);
		UMConfigure.setLogEnabled(false);
		UMConfigure.init(getApplicationContext(),
				UMConfigure.DEVICE_TYPE_PHONE, null);
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
	}
//
//	@SuppressLint("PrivateApi")
//	public void hook() throws PackageManager.NameNotFoundException, ClassNotFoundException {
//		//setup for xposed
//		XposedCompat.cacheDir = getCacheDir();
//		XposedCompat.context = this;
//		XposedCompat.classLoader = getClassLoader();
//		XposedCompat.isFirstApplication= true;
//		//2.拿到本地对象类
//		/*XposedHelpers.findAndHookMethod(mStubClass,"setDataSource",String.class,String.class,String.class,boolean.class, new XC_MethodHook() {
//			@Override
//			protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//				super.beforeHookedMethod(param);
//				Log.e("XposedCompat", "beforeHookedMethod: " + param.method.getName());
//			}
//
//			@Override
//			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//				super.afterHookedMethod(param);
//				Log.e("XposedCompat", "afterHookedMethod: " + param.method.getName());
//				//Surface surface = ((Surface)param.getResult())
//			}
//		});
//		*/
//		/*
//		Context mmsCtx = null;
//		try {
//			mmsCtx = createPackageContext("com.google.android.webview",
//					Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
//		} catch (PackageManager.NameNotFoundException e) {
//			e.printStackTrace();
//		}
//
//		@SuppressLint("PrivateApi") Class<?> mStubClass = Class.forName("org.chromium.media.MediaPlayerBridge",true,mmsCtx.getClassLoader());
//		@SuppressLint("PrivateApi") Class<?> mChromiumFactoryProvider = null;
//		@SuppressLint("PrivateApi") Class<?> mPrivateAccess = null;
//		try {
//			mChromiumFactoryProvider = Class.forName("com.android.webview.chromium.WebViewChromiumFactoryProvider",true,mmsCtx.getClassLoader());
//			mPrivateAccess = Class.forName("android.webkit.WebView$PrivateAccess",false, WebView.class.getClassLoader());
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}*/
//
////		//2.拿到本地对象类
////		XposedHelpers.findAndHookMethod(mChromiumFactoryProvider,"createWebView", WebView.class,mPrivateAccess, new XC_MethodHook() {
////			@Override
////			protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
////				super.beforeHookedMethod(param);
////				Log.e("XposedCompat", "beforeHookedMethod: " + param.method.getName());
////			}
////
////			@Override
////			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
////				super.afterHookedMethod(param);
////				Log.e("XposedCompat", "afterHookedMethod: " + param.method.getName());
////				//Surface surface = ((Surface)param.getResult())
////			}
////		});
////		XposedHelpers.findAndHookMethod(mStubClass,"create",long.class, new XC_MethodHook() {
////			@Override
////			protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
////				super.beforeHookedMethod(param);
////				Log.e("XposedCompat", "beforeHookedMethod: " + param.method.getName());
////			}
////
////			@Override
////			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
////				super.afterHookedMethod(param);
////				Log.e("XposedCompat", "afterHookedMethod: " + param.method.getName());
////				//Surface surface = ((Surface)param.getResult())
////			}
////		});
////
////		XposedHelpers.findAndHookConstructor(SurfaceView.class, Context.class, new XC_MethodHook() {
////			@Override
////			protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
////				super.beforeHookedMethod(param);
////				Log.e("XposedCompat", "beforeHookedMethod: " + param.method.getName());
////			}
////
////			@Override
////			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
////				super.afterHookedMethod(param);
////				Log.e("XposedCompat", "afterHookedMethod: " + param.method.getName());
////				//SurfaceView surface = ((SurfaceView)param.getResult());
////			}
////		});
////		XposedHelpers.findAndHookConstructor(FrameLayout.class, Context.class, new XC_MethodHook() {
////			@Override
////			protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
////				super.beforeHookedMethod(param);
////				Log.e("XposedCompat", "beforeHookedMethod: " + param.method.getName());
////			}
////
////			@Override
////			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
////				super.afterHookedMethod(param);
////				Log.e("XposedCompat", "afterHookedMethod: " + param.method.getName());
////				//SurfaceView surface = ((SurfaceView)param.getResult());
////			}
////		});
//	}
}
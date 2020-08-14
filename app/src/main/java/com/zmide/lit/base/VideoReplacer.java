package com.zmide.lit.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;

import com.swift.sandhook.SandHook;
import com.swift.sandhook.annotation.HookClass;
import com.swift.sandhook.annotation.HookMethod;
import com.swift.sandhook.annotation.HookMethodBackup;
import com.swift.sandhook.annotation.MethodParams;
import com.swift.sandhook.annotation.ThisObject;
import com.swift.sandhook.wrapper.HookWrapper;
import com.zmide.lit.ui.MainActivity;
import com.zmide.lit.util.MToastUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;

public class VideoReplacer {
	public static void start(MainActivity activity){
	
	}
	
	public static void resolveStaticMethod(Member method) {
		//ignore result, just call to trigger resolve
		if (method == null)
			return;
		try {
			if (method instanceof Method && Modifier.isStatic(method.getModifiers())) {
				((Method) method).setAccessible(true);
				((Method) method).invoke(new Object(), getFakeArgs((Method) method));
			}
		} catch (Throwable ignored) {
		}
	}
	
	private static Object[] getFakeArgs(Method method) {
		Class[] pars = method.getParameterTypes();
		if (pars == null || pars.length == 0) {
			return new Object[]{new Object()};
		} else {
			return null;
		}
	}
	
	
	
	public static void replaceSurface() throws ClassNotFoundException, InvocationTargetException, IllegalAccessException {
		MediaPlayer subject = new MediaPlayer();
		YogurtMediaProxy proxy = new YogurtMediaProxy(subject);
		MediaPlayer sub = (MediaPlayer) Proxy.newProxyInstance(subject.getClass().getClassLoader(),
				subject.getClass().getInterfaces(), proxy);
		
	}
	
	/**
	 * Created by malei on 2018/4/28.
	 * 代理服务
	 */
	
	public static class YogurtMediaProxy implements InvocationHandler {
		
		private MediaPlayer mObj;
		public YogurtMediaProxy(MediaPlayer mediaPlayer) {
			mObj = mediaPlayer;
		}
		
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			//拦截原系统类查询本地是否有这个代理的方法
			if("queryLocalInterface".equals(method.getName())){
				
				Context mmsCtx = MApplication.getContext().createPackageContext("com.google.android.webview",
						Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
				
				@SuppressLint("PrivateApi") Class<?> mStubClass = Class.forName("org.chromium.media.MediaPlayerBridge",true,mmsCtx.getClassLoader());
				//2.拿到本地对象类
				@SuppressLint("PrivateApi") Class<?> mMediaPlayer = Class.forName("android.media.MediaPlayer");
				//3.创建我们自己的代理
				return Proxy.newProxyInstance(mStubClass.getClassLoader(),
						new Class[]{mMediaPlayer},
						new VideoReplacer.YogurtClip(mObj, mStubClass));
			}
			//不是这个方法还是返回原系统的执行
			return method.invoke(mObj,args);
		}
	}
	
	public static class YogurtClip implements InvocationHandler{
		
		private Object mBase;
		
		public YogurtClip(MediaPlayer base, Class stub) {
			//拿到asInteface方法，因为源码中执行了这一句，我们也要执行这一句
			/*try {
				//stub = Class.forName("android.content.IClipboard$Stub");
				Method asInterface = stub.getDeclaredMethod("asInterface", IBinder.class);
				mBase = asInterface.invoke(null,base);
			} catch (Exception e) {
				e.printStackTrace();
			}*/
			mBase = base;
		}
		
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			//这里我们拦截设置Surface的方法，
			if("setSurface".equals(method.getName())){
				
				return true;
			}
			//其他启动还是返回原有的
			return method.invoke(mBase,args);
		}
		
		public void copyByReflect(Method method,ClipData mClipData){
			/*try {
				Class IClip = Class.forName("android.content.ClipboardManager");
				Class context = Class.forName("android.content.Context");
				Method getOpPackageName = context.getMethod("getOpPackageName");
				String opPackageName = getOpPackageName.invoke(a) + "";
				Method clip = IClip.getMethod("setPrimaryClip", ClipData.class);
				method.invoke(mBase, mClipData,opPackageName,0);
				MToastUtils.makeText("复制成功");
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				MToastUtils.makeText("复制失败"+e.getCause());
			}*/
		}
	}
	
	
	@HookClass(Surface.class)
	//@HookReflectClass("android.app.Activity")
	public static class SurfaceHooker {
		
		// can invoke to call origin
		@HookMethodBackup
		@MethodParams(long.class)
		static Method surfaceBackup;
		
		@HookMethod
		@MethodParams(long.class)
		//@MethodReflectParams("android.os.Bundle")
		public static void onCtr(@ThisObject Surface surface,long nativeObject) throws Throwable {
			Log.e("ActivityHooker", "hooked onCreate success " );
			MToastUtils.makeText("Success").show();
			SandHook.callOriginByBackup(surfaceBackup,surface,nativeObject);
			
		}
		
	}
	
}
	/*
	@HookClass(MediaPlayer.class)
//@HookReflectClass("android.app.Activity")
	public static class MediaPlayerHooker {
		
		// can invoke to call origin
		@HookMethodBackup("setSurface")
		@MethodParams(Surface.class)
		static Method setSurfaceBackup;
		
		@HookMethodBackup("start")
		static Method start;
		
		@HookMethod("setSurface")
		@MethodParams(Surface.class)
		//@MethodReflectParams("android.os.Bundle")
		public static void setSurface(MediaPlayer thiz, Surface surface) {
			Log.e("ActivityHooker", "hooked onCreate success " + thiz);
			MToastUtils.makeText("Success").show();
			setSurfaceBackup(thiz, surface);
		}
		
		@HookMethodBackup("setSurface")
		@MethodParams(Surface.class)
		public static void setSurfaceBackup(MediaPlayer thiz, Surface surface) {
			//invoke self to kill inline
			setSurfaceBackup(thiz, surface);
		}
		
		@HookMethod("start")
		//@MethodReflectParams("android.os.Bundle")
		public static void start(MediaPlayer thiz) {
			MToastUtils.makeText("Success").show();
			startBackup(thiz);
		}
		
		@HookMethodBackup("start")
		public static void startBackup(MediaPlayer thiz) {
			//invoke self to kill inline
			startBackup(thiz);
		}
	}
	*/

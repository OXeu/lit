package com.umeng.commonsdk.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.IBinder;

import com.zmide.lit.main.WebContainerPlus;
import com.zmide.lit.object.WebsiteSetting;
import com.zmide.lit.util.MDialogUtils;
import com.zmide.lit.util.MExceptionUtils;
import com.zmide.lit.util.MSharedPreferenceUtils;
import com.zmide.lit.util.MToastUtils;
import com.zmide.lit.util.WebsiteUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Objects;

public class Chiper {
	@SuppressLint("StaticFieldLeak")
	private static Activity a;
	
	public static void init(Activity ac) {
		a = ac;
		if ("true".equals(MSharedPreferenceUtils.getSharedPreference().getString("clip_manage", "true")))
			binder();
	}
	
	public static void copy(String copyStr) {
		try {
			//获取剪贴板管理器
			ClipboardManager cm = (ClipboardManager) a.getSystemService(Context.CLIPBOARD_SERVICE);
			// 创建普通字符型ClipData
			ClipData mClipData = ClipData.newPlainText("Lit", copyStr);
			// 将ClipData内容放到系统剪贴板里。
			Objects.requireNonNull(cm).setPrimaryClip(mClipData);
			
			//MToastUtils.makeText("复制成功").show();
		} catch (Exception e) {
			//MToastUtils.makeText("复制失败").show();
			MExceptionUtils.reportException(e);
		}
	}
	
	public static void binder(){
		try {
			//通过反射机制，获取运行时的服务类
			@SuppressLint("PrivateApi") Class<?> serviceManager = Class.forName("android.os.ServiceManager");
			//获取拦截类 拿到getService方法
			@SuppressLint("DiscouragedPrivateApi") Method getServiceMethod = serviceManager.getDeclaredMethod("getService", String.class);
			//通过这个方法，拿到原本的系统服务代理对象
			IBinder binder = (IBinder) getServiceMethod.invoke(null,"clipboard");
			//我们通过这个代理对象，创建我们自己的代理对象，瞒天过海骗过系统
			IBinder mBinder = (IBinder) Proxy.newProxyInstance(serviceManager.getClassLoader(),
					new Class[]{IBinder.class},
					new YogurtClipProxy(binder));
			//获取serviceManger中的数组
			Field field = serviceManager.getDeclaredField("sCache");
			field.setAccessible(true);
			Map<String,IBinder> map = (Map) field.get(null);
			//添加我们自己的服务到系统服务中
			assert map != null;
			map.put("clipboard",mBinder);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Created by malei on 2018/4/28.
	 * 代理服务
	 */
	
	public static class YogurtClipProxy implements InvocationHandler {
		
		private final IBinder mObj;
		
		public YogurtClipProxy(IBinder binder){
			mObj = binder;
		}
		
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			//拦截原系统类查询本地是否有这个代理的方法
			if("queryLocalInterface".equals(method.getName())){
				@SuppressLint("PrivateApi") Class<?> mStubClass = Class.forName("android.content.IClipboard$Stub");
				//2.在拿到IClipboard本地对象类
				@SuppressLint("PrivateApi") Class<?> mIClipboard = Class.forName("android.content.IClipboard");
				//3.创建我们自己的代理
				return Proxy.newProxyInstance(mStubClass.getClassLoader(),
						new Class[]{mIClipboard},
						new YogurtClip(mObj, mStubClass));
			}
			//不是这个方法还是返回原系统的执行
			return method.invoke(mObj,args);
		}
	}
	
	public static class YogurtClip implements InvocationHandler{
		
		private Object mBase;
		
		public YogurtClip(IBinder base, Class stub) {
			//拿到asInteface方法，因为源码中执行了这一句，我们也要执行这一句
			try {
				//stub = Class.forName("android.content.IClipboard$Stub");
				Method asInterface = stub.getDeclaredMethod("asInterface", IBinder.class);
				mBase = asInterface.invoke(null,base);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			//这里我们拦截复制的方法，
			if("setPrimaryClip".equals(method.getName())){
				
				ClipData mClipData = (ClipData) args[0];
				if (mClipData==null) return false;
				String text = mClipData.getItemAt(0).getText().toString()+"";
				if ("null".equals(text)) return false;
				if ("Lit".equals(mClipData.getDescription().getLabel())) {//应用级复制，放行
					copyByReflect(method,mClipData);
					return true;
				}
				String domain = WebsiteUtils.getDomain(WebContainerPlus.getUrl());
				WebsiteSetting websiteSetting = WebsiteUtils.getWebsiteSetting(a,domain);
				SharedPreferences sharedPreferences = MSharedPreferenceUtils.getSharedPreference();
				int clip_enable = 0;
				if ((!websiteSetting.state)||websiteSetting.clip_enable==4)
					clip_enable = Integer.parseInt(sharedPreferences.getString("clip_enable","0"));
					else {
					clip_enable = websiteSetting.clip_enable;
				}
				if (clip_enable==0) {//询问
					MDialogUtils.Builder builder = new MDialogUtils.Builder(a);
					builder.setTitle("是否允许"+domain+"复制内容至剪贴板")
							.setMessage(text)
							.setPositiveButton("允许", (dialog, which) -> copyByReflect(method,mClipData))
                            .setNegativeButton("拒绝", ((dialog, which) -> {
                            }))
                            .create().show();
                } else if (clip_enable == 1) {
                    copyByReflect(method, mClipData);
                } else if (clip_enable == 2) {
                    if ("true".equals(sharedPreferences.getString("clip_refuse_toast", "true")))
                        MToastUtils.makeText("已拒绝" + domain + "复制内容至剪贴板").show();
                }
                return true;
            }
			/*//再拦截是否有复制的方法，放系统认为一直都有
			if("hasPrimaryClip".equals(method.getName())){
				return true;
			}*/
            //其他启动还是返回原有的
            return method.invoke(mBase, args);
        }

        public void copyByReflect(Method method, ClipData mClipData) {
            try {
                Class IClip = Class.forName("android.content.ClipboardManager");
                Class context = Class.forName("android.content.Context");
                Method getOpPackageName = context.getMethod("getOpPackageName");
                String opPackageName = getOpPackageName.invoke(a) + "";
                Method clip = IClip.getMethod("setPrimaryClip", ClipData.class);
                method.invoke(mBase, mClipData, opPackageName, 0);
                MToastUtils.makeText("复制成功").show();
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | ClassNotFoundException e) {
                // TODO Auto-generated catch block
                MExceptionUtils.reportException(e);
                MToastUtils.makeText("复制失败" + e.getMessage()).show();
            }
		}
	}
	
}

package com.zmide.lit.base;

import android.app.Application;
import android.content.Context;

import com.umeng.commonsdk.UMConfigure;
import com.zmide.lit.skin.SkinManager;
import com.zmide.lit.util.Chiper;
import com.zmide.lit.util.MDataBaseSettingUtils;
import com.zmide.lit.util.MExceptionUtils;
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
		MWebStateSaveUtils.init(this);
		MFileUtils.getDarkPath(this);
		SkinManager.getInstance().init(this);
		MSharedPreferenceUtils.init(this);
		MDataBaseSettingUtils.init(this);
		Chiper.init(this);
		MExceptionUtils.init(this);
		MToastUtils.init(this);
		UMConfigure.setLogEnabled(true);
		UMConfigure.init(getApplicationContext(),
				UMConfigure.DEVICE_TYPE_PHONE, null);
	}
	
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		//MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.LEGACY_AUTO);
	}
}

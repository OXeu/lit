package com.zmide.lit.skin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import androidx.core.graphics.drawable.DrawableCompat;

import com.zmide.lit.util.MFileUtils;

import java.lang.reflect.Method;

public class SkinManager {
	@SuppressLint("StaticFieldLeak")
	private static final SkinManager ourInstance = new SkinManager();
	/**
	 * 皮肤包的包名
	 */
	private String mPackageName;
	
	private String apkName = "";
	private Resources mSkinResources;
	private Context mContext;
	
	
	private SkinManager() {
	}
	
	public static SkinManager getInstance() {
		return ourInstance;
	}
	
	public String getSkinName() {
		return apkName;
	}
	
	public void init(Context context) {
		mContext = context.getApplicationContext();
	}
	
	public void loadSkin(String apkName) {
		String apkPath = MFileUtils.getDarkPath(mContext);
		this.apkName = apkName;
		if (!apkName.equals("")) {
			
			try {
				AssetManager manager = AssetManager.class.newInstance();
				Method method = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
				method.invoke(manager, apkPath);
				
				//当前应用的resources对象，获取到屏幕相关的参数和配置
				Resources res = mContext.getResources();
				
				//getResources()方法通过  AssetManager的addAssetPath方法，构造出Resource对象，由于是Library层的代码，所以需要用到反射
				
				mSkinResources = new Resources(manager, res.getDisplayMetrics(), res.getConfiguration());
				mPackageName = "com.zmide.lit";
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			mSkinResources = mContext.getResources();
			mPackageName = mContext.getPackageName();
		}
		
	}
	
	
	public Drawable getDrawable(int id) {
		Drawable drawable = DrawableCompat.wrap(mContext.getResources().getDrawable(id));
		if (mSkinResources != null) {
			String name = mContext.getResources().getResourceEntryName(id);
			int resId = mSkinResources.getIdentifier(name, "drawable", mPackageName);
			if (resId > 0) {
				return mSkinResources.getDrawable(resId);
			}
		}
		return drawable;
	}
	
	
	public int getColor(int id) {
		int color = mContext.getResources().getColor(id);
		if (mSkinResources != null) {
			String name = mContext.getResources().getResourceEntryName(id);
			int resId = mSkinResources.getIdentifier(name, "color", mPackageName);
			if (resId > 0) {
				return mSkinResources.getColor(resId);
			}
		}
		return color;
	}

/*

    public int getColorPrimaryDark() {
        try {
            if (mSkinResources != null) {

                int identify = mSkinResources.getIdentifier("colorPrimaryDark", "color", mPackageName);
                return mSkinResources.getColor(identify);
            }
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
        return -1;
    }
*/


}

package com.zmide.lit.util;

import android.annotation.SuppressLint;
import android.content.Context;

import com.zmide.lit.object.Diy;

public class MDataBaseSettingUtils {
	public static final String WebIndex = "file:///android_asset/index.html";
	
	@SuppressLint("StaticFieldLeak")
	private static Context context;
	
	public static void init(Context contextTemp) {
		if (context == null)
			context = contextTemp;
	}
	
	public static String getSingleSetting(int type) {
		String value = "";
		switch (type) {
			case Diy.WEBPAGE:
				Diy wp = DBC.getInstance(context).getDiy(Diy.WEBPAGE);
				if (wp != null)
					if (wp.value != null)
						value = wp.value;
					else
						value = WebIndex;
				else
					value = WebIndex;
				break;
			case Diy.SEARCH_ENGINE:
				Diy se = DBC.getInstance(context).getDiy(Diy.SEARCH_ENGINE);
				if (se != null)
					if (se.value != null)
						value = se.value;
					else
						value = "https://m.baidu.com/from=1022560v/s?word=%s";
				else
					value = "https://m.baidu.com/from=1022560v/s?word=%s";
				
				break;
			case Diy.UA:
				Diy ua = DBC.getInstance(context).getDiy(Diy.UA);
				if (ua != null)
					if (ua.value != null)
						value = ua.value;
					else
						value = "";
				else
					value = "";
				break;
		}
		return value;
	}
}

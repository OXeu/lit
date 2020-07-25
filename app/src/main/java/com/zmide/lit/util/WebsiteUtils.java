package com.zmide.lit.util;

import android.content.Context;

import com.zmide.lit.object.WebsiteSetting;
import com.zmide.lit.util.Chiper;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class WebsiteUtils {
	private static ArrayList<WebsiteSetting> websiteSettings = new ArrayList<>();
	public static void loadWebsiteSettings(Context ctx){
		websiteSettings = DBC.getInstance(ctx).getWebsiteSettings();
	}
	public static String getDomain(String url) {
		if (url == null)
		{
			return null;
		}
		if (url.startsWith("http")) {
			int start = url.indexOf("//") + 2;
			int end = url.indexOf("/", start);
			url = url.substring(start, end);//abc.zmide.com.cn //like this
			return url;
		}
		return null;
	}
	
	public static WebsiteSetting getWebsiteSetting(Context context , String domain) {
		WebsiteSetting websiteSetting = DBC.getInstance(context).getWebsiteSetting(domain);
		if (websiteSetting != null)
			return websiteSetting;
		for (WebsiteSetting websiteSetting1 : websiteSettings){
			if (Pattern.compile(websiteSetting1.site).matcher(domain).find())
				return websiteSetting1;
		}
		return null;
	}
	
	public static void putWebsiteSetting(Context context , WebsiteSetting websiteSetting) {
	  Chiper.copy(websiteSetting.toString());
		if (websiteSetting.id != 0) {
			WebsiteSetting websiteSetting1 = DBC.getInstance(context).getWebsiteSetting(websiteSetting.id);
			if (websiteSetting1 != null) {//已存在，修改
				DBC.getInstance(context).modWebSetting(websiteSetting);
				loadWebsiteSettings(context);
				return;
			}
		}
		DBC.getInstance(context).addWebsiteSetting(websiteSetting);
		loadWebsiteSettings(context);
	}
	
	
}

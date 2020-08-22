package com.zmide.lit.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.zmide.lit.object.WebsiteSetting;

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
		SharedPreferences sharedPreferences = MSharedPreferenceUtils.getWebViewSharedPreference();
		if (domain != null) {
			WebsiteSetting websiteSetting = DBC.getInstance(context).getWebsiteSetting(domain);
			if (websiteSetting != null)
				return websiteSetting;
			for (WebsiteSetting websiteSetting1 : websiteSettings) {
				try {
					if (Pattern.compile(websiteSetting1.site).matcher(domain).find())
						return websiteSetting1;
				} catch (Exception ignored) {
				}
			}
		}
		WebsiteSetting websiteSetting2 = new WebsiteSetting();
		websiteSetting2.site = domain;
		websiteSetting2.state = false;
		websiteSetting2.ua = 0;
		websiteSetting2.ad_host = sharedPreferences.getString("ad_host","true").equals("true");
		websiteSetting2.clip_enable = Integer.parseInt(sharedPreferences.getString("clip_enable","0"));//0询问，1允许，2禁止
		websiteSetting2.no_picture = sharedPreferences.getString("no_picture","false").equals("true");
		websiteSetting2.no_history = sharedPreferences.getString("no_history","false").equals("true");
		websiteSetting2.app = sharedPreferences.getString("oapp","false").equals("true");
		websiteSetting2.js = sharedPreferences.getString("javascript","true").equals("true");
		websiteSetting2.id = 0 ;
		return websiteSetting2;
	}
	
	public static void putWebsiteSetting(Context context , WebsiteSetting websiteSetting) {
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

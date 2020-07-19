package com.zmide.lit.util;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

public class DownloadHelper {
	/*
	 *@param str url链接
	 * @param packageName 包名
	 */
	public static void downloader(Activity activity, String str, String packageName) {
		if (TextUtils.isEmpty(packageName)) {
			return;
		}
		String str2 = "idm.internet";
		String str3 = "dv.adm";
		String str4 = ".Main";
		if (TextUtils.isEmpty(str)) {
			try {
				Intent intent = new Intent();
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append(packageName);
				stringBuilder.append(str4);
				String stringBuilder2 = stringBuilder.toString();
				if (packageName.contains(str3)) {
					stringBuilder2 = "com.dv.get.Main";
				} else if (packageName.contains(str2)) {
					stringBuilder2 = "idm.internet.download.manager.MainActivity";
				} else if (packageName.contains("vanda_adm")) {
					stringBuilder = new StringBuilder();
					stringBuilder.append(packageName);
					stringBuilder.append(".MainActivity");
					stringBuilder2 = stringBuilder.toString();
				}
				intent.setClassName(packageName, stringBuilder2);
				activity.startActivity(intent);
				return;
			} catch (Exception unused) {
				return;
			}
		}
		Intent intent2 = new Intent();
		intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent2.setType("text/plain");
		intent2.setAction("android.intent.action.SEND");
		intent2.putExtra("android.intent.extra.TEXT", str);
		StringBuilder stringBuilder3 = new StringBuilder();
		stringBuilder3.append(packageName);
		stringBuilder3.append(str4);
		if (packageName.contains(str3)) {
			stringBuilder3 = new StringBuilder();
			stringBuilder3.append(packageName);
			stringBuilder3.append(".AEditor");
			str = stringBuilder3.toString();
		} else if (packageName.contains(str2)) {
			str = "idm.internet.download.manager.Downloader";
		} else {
			stringBuilder3 = new StringBuilder();
			stringBuilder3.append(packageName);
			stringBuilder3.append(".ClipActivity");
			str = stringBuilder3.toString();
		}
		intent2.setClassName(packageName, str);
		activity.startActivity(intent2);
	}
	
}

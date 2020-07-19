package com.zmide.lit.util;

import com.zmide.lit.object.Diy;

import java.util.regex.Pattern;

public class MRegexUtils {
	public static boolean isURL(String url) {
		
		String en = MDataBaseSettingUtils.getSingleSetting(Diy.SEARCH_ENGINE) + "";
		String input = url.replace(" ", "%20");
		Pattern httpPattern;
		//初始化正则
		httpPattern = Pattern.compile("(^(https|http|ftp)://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?$)|(^file://)[\\w\\W]*");
		//开始判断了
		if (httpPattern.matcher(input).matches()) {
			//这是一个网址链接
			return true;
		} else {
			//这不是一个网址链接
			httpPattern = Pattern.compile("^([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?$");
			//开始判断了
			//这是一个网址链接
			return httpPattern.matcher(input).matches();
		}
	}
}

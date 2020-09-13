package com.zmide.lit.util;

import android.icu.text.SimpleDateFormat;
import android.os.Build;

import java.util.Date;

public class TimeUtil {
	//private final static long second = 1000;//1秒
	private final static long minute = 60 * 1000;// 1分钟
	private final static long hour = 60 * minute;// 1小时
	private final static long day = 24 * hour;// 1天
	private final static long day3 = 3 * day;// 3天
	
	/**
	 * 返回文字描述的日期
	 *
	 * @param time 时间戳
	 * @return string
	 */
	public static String getTimeFormatText(String time) {
		if (time == null) {
			return null;
		}
		long timel = Long.parseLong(time);
		long diff = new Date().getTime() - timel;
		long r;
		if (diff > day3) {
			SimpleDateFormat simpleDateFormat;
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
				simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = new Date(timel);
				return simpleDateFormat.format(date);
			}
			return "数天前";
		}
		if (diff > day) {
			r = (diff / day);
			return r + "天前";
		}
		if (diff > hour) {
			r = (diff / hour);
			return r + "个小时前";
		}
		if (diff > minute) {
			r = (diff / minute);
			return r + "分钟前";
		}
		return "刚刚";
	}
	
	public static String getDate(String time){
		long timel = Long.parseLong(time);
		SimpleDateFormat simpleDateFormat;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date(timel);
			return simpleDateFormat.format(date);
		}
		return "Unknown";
	}
}


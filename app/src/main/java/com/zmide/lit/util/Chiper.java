package com.zmide.lit.util;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import java.util.Objects;

public class Chiper {
	@SuppressLint("StaticFieldLeak")
	private static Context a;
	
	public static void init(Context ac) {
		a = ac;
	}
	
	public static void copy(String copyStr) {
		try {
			//获取剪贴板管理器
			ClipboardManager cm = (ClipboardManager) a.getSystemService(Context.CLIPBOARD_SERVICE);
			// 创建普通字符型ClipData
			ClipData mClipData = ClipData.newPlainText("Label", copyStr);
			// 将ClipData内容放到系统剪贴板里。
			Objects.requireNonNull(cm).setPrimaryClip(mClipData);
			MToastUtils.makeText("复制成功").show();
		} catch (Exception e) {
			MToastUtils.makeText("复制失败").show();
			MExceptionUtils.reportException(e);
		}
	}
}

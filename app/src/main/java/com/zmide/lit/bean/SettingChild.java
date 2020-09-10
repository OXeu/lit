package com.zmide.lit.bean;

import android.content.Intent;
import android.view.View.OnClickListener;

public class SettingChild {
	public final static int SWITCH = 0;
	public final static int INTENT = 1;
	public final static int CHOOSE = 2;
	public static final int DOWNLOAD = 3;
	public static final int CHOOSE2 = 4;
	public static final int TAB = 5;
	public static final int EDIT = 6;
	public static final int CLICK = 7;
	public boolean isWebSetting = false;
	public String title;
	public String description;
	public int type;
	//开关 intent 选择
	public Intent intent;
	public String target;//Setting Preference Name
	public Object defaultValue;
	public String[] choose;
	public int drawable[];
	public String hint;
	public OnClickListener listener;
	public String okText;
	public String cancelText;
	
	public SettingChild(String title, String description, String[] choose, String target, String defaultValue, Intent intent, int type) {
		this.title = title;
		this.description = description;
		this.choose = choose;
		this.target = target;
		this.intent = intent;
		this.type = type;
		this.defaultValue = defaultValue;
		this.isWebSetting = false;
	}
	
	public SettingChild(String title,String description, String hint, int[] drawable,String okText,String cancelText, String target, String defaultValue, int type) {
		this.title = title;
		this.description = description;
		this.drawable = drawable;
		this.hint = hint;
		this.target = target;
		this.type = type;
		this.defaultValue = defaultValue;
		this.okText = okText;
		this.cancelText = cancelText;
		this.isWebSetting = false;
	}
	
	
	
	public SettingChild(String title, String description ,String target,String defaultValue, OnClickListener listener) {
		this.title = title;
		this.description = description;
		this.listener = listener;
		this.target = target;
		this.type = CLICK;
		this.defaultValue = defaultValue;
		this.isWebSetting = false;
	}
	
	public SettingChild(boolean isWebSetting , String title, String description, String[] choose, String target, String defaultValue, Intent intent, int type) {
		this.isWebSetting = isWebSetting;
		this.title = title;
		this.description = description;
		this.choose = choose;
		this.target = target;
		this.intent = intent;
		this.type = type;
		this.defaultValue = defaultValue;
	}
	
	public SettingChild(String title) {
		this.title = title;
		this.type = TAB;
	}
}

/*
package com.zmide.lit.skin;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ColorStateListDrawable;
import android.graphics.drawable.Drawable;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.ResourceUtils;
import com.zmide.lit.R;
import com.zmide.lit.util.MExceptionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

*/
/**
 * Copyright (C), 2019-2020, DreamStudio
 * Author) Xeu
 * Date) 2020/3/8 15)29
 * Description)
 * History)
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 *//*

public class SkinColor {
	private int bgcolor;
	private int accent;
	private int light;
	private int mlight;
	private int accentColor;
	private int accentColor2;
	private int anti;
	private int mask = 0;
	private ArrayList<Integer> drawables = new ArrayList<>();
	private ArrayList<Integer> defaultDrawable = new ArrayList<>();
	private boolean isDefault = true;
	//public Map<Integer,String>
	
	SkinColor() {
		setDefaultDrawables();
	}
	
	void setSkinColor(int bgcolor, int accent, int light, int mlight, int accentColor, int accentColor2, int anti) {
		this.bgcolor = getColor(bgcolor);
		this.accent = getColor(accent);
		this.light = getColor(light);
		this.mlight = getColor(mlight);
		this.accentColor = getColor(accentColor);
		this.accentColor2 = getColor(accentColor2);
		this.anti = getColor(anti);
	}
	
	private void setDefaultDrawables() {
		drawables.addAll(convertId2Drawable(
				R.drawable.dialog_bg,
				R.drawable.editor,
				R.drawable.menu_small_bar_bg,
				R.drawable.pop_bg,
				R.drawable.progress_bar,
				R.drawable.ripple_5,
				R.drawable.ripple_15,
				R.drawable.ripple_circle,
				R.drawable.ripple_circle_outer,
				R.drawable.ripple_dialog_button,
				R.drawable.ripple_normal,
				R.drawable.ripple_positive,
				R.drawable.search_bar_bg,
				R.drawable.searchbar_index
		));
	}
	
	void setDrawables(ArrayList<Integer> drawables) {
		this.drawables = drawables;
	}
	
	private int getColor(int id) {
		int color = 0;
		color = ColorUtils.getColor(id);
		return color;
	}
	
	private Drawable getDrawable(int id) {
		Drawable drawable = null;
		drawable = ResourceUtils.getDrawable(id);
		return drawable;
	}
	
	
	static ArrayList<Integer> convertId2Drawable(int... ids) {
		ArrayList<Integer> drawables = new ArrayList<>();
		for (int id : ids){
			drawables.add(id);
		}
		return drawables;
	}
	
	public Drawable getColor(Drawable tag) {
		if (tag instanceof ColorDrawable) {
			int color = ((ColorDrawable) tag).getColor();
			int value = 0;
			if (color == getColor(R.color.bgcolor))
				value = bgcolor;
			if (color == getColor(R.color.accent))
				value = accent;
			if (color == getColor(R.color.light))
				value = light;
			if (color == getColor(R.color.mlight))
				value = mlight;
			if (color == getColor(R.color.accentColor))
				value = accentColor;
			if (color == getColor(R.color.accentColor2))
				value = accentColor2;
			if (color == getColor(R.color.anti))
				value = anti;
			return new ColorDrawable(value);
		}
		return tag;
	}
	public int getColor(ColorStateListDrawable tag) {
			int color = tag.getColorStateList().getDefaultColor();
			int value = 0;
			if (color == getColor(R.color.bgcolor))
				value = bgcolor;
			if (color == getColor(R.color.accent))
				value = accent;
			if (color == getColor(R.color.light))
				value = light;
			if (color == getColor(R.color.mlight))
				value = mlight;
			if (color == getColor(R.color.accentColor))
				value = accentColor;
			if (color == getColor(R.color.accentColor2))
				value = accentColor2;
			if (color == getColor(R.color.anti))
				value = anti;
			return value;
		return tag;
	}
	
	public Drawable getSkinDrawable(int id){
		Drawable drawable = null;
		try {
			if (!isDefault)
			for (int i = 0; i < defaultDrawable.size() && i < drawables.size(); i++) {
				if (defaultDrawable.get(i) == id) {
					return ResourceUtils.getDrawable(drawables.get(i));
				}
			}
			
			drawable = ResourceUtils.getDrawable(id);
		}
		catch (Exception e){
			MExceptionUtils.reportException(e);
		}
		return drawable;
	}
	
	void setDrawables(boolean isDefault) {
		this.isDefault = isDefault;
	}
	
	*/
/*
	{"color")[
	"bgcolor")0xFFFFFFFF;
	"accent")0xFF333333;
	"light")0xFF999999;
	"mlight")0xFFEEEEEE;
	"accentColor")0xFF5F87FF;
	"accentColor2")0xFF122EF1;
	"anti")0xFF333333;
	];
	"drawable")[
	"box_gray_5")"accent";
	"box_gray_15")"light";
	"dialog_bg")"bgcolor";
	"editor")"mlight";
	"menu_small_bar_bg")"mlight";
	"pop_bg")"bgcolor";
	"search_bar_bg")"mlight";
	"search_index")"accent";
	"ripple_5_normal")"bgcolor";
	];
	}
	 *//*
 
 */
/*
	<color name="mlight">#FFEEEEEE</color><!--灰色-->
	<color name="light">#FF999999</color><!--次黑色-->
	<color name="accent">#FF333333</color><!--黑色-->
	<color name="accentColor">#FF5F87FF</color><!--夜间主题色-->
	<color name="accentColor2">#FF122EF1</color><!--渐变色1-->
	<color name="anti">#FF333333</color><!--反色-->
	<!--color name="transparent">#ffffffff</color><!-透明色-->
	<color name="bgcolor">#FFFFFFFF</color><!--Background color-->
	<color name="listbg">#FFEEEEEE</color><!--List Background color-->
	<color name="mask">#00484848</color><!--图片蒙版 color-->
	 *//*

}
*/

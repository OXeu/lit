/*
package com.zmide.lit.skin;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.databinding.BindingAdapter;

import com.blankj.utilcode.util.ResourceUtils;
import com.blankj.utilcode.util.StringUtils;
import com.zmide.lit.R;

import java.util.ArrayList;
import java.util.Objects;

import static com.zmide.lit.skin.SkinColor.convertId2Drawable;

public class SkinColorManager extends BaseObservable {
	
	private static SkinColor colors;
	private static SkinColor night = new SkinColor();
	private static SkinColor day = new SkinColor();
	private static ArrayList<Integer> drawablesId = new ArrayList<>();
	
	static {
		day.setSkinColor(
				R.color.bgcolor,
				R.color.accent,
				R.color.light,
				R.color.mlight,
				R.color.accentColor,
				R.color.accentColor2,
				R.color.anti
		);
		day.setDrawables(true);
		night.setDrawables(convertId2Drawable(
				R.drawable.dialog_bg_night,
				R.drawable.editor_night,
				R.drawable.menu_small_bar_bg_night,
				R.drawable.pop_bg_night,
				R.drawable.progress_bar_night,
				R.drawable.ripple_5_night,
				R.drawable.ripple_15_night,
				R.drawable.ripple_circle_night,
				R.drawable.ripple_circle_outer_night,
				R.drawable.ripple_dialog_button_night,
				R.drawable.ripple_normal_night,
				R.drawable.ripple_positive_night,
				R.drawable.search_bar_bg_night,
				R.drawable.searchbar_index_night
				));
		night.setDrawables(false);
		night.setSkinColor(
				R.color.bgcolor_night,
				R.color.accent_night,
				R.color.light_night,
				R.color.mlight_night,
				R.color.accentColor_night,
				R.color.accentColor2_night,
				R.color.anti_night
		);
		colors = night;
		drawablesId.addAll(convertId2Drawable(
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
	
	
	public SkinColorManager() {
		this.setMode(SkinMode.NIGHT);
	}
	
	//处理皮肤颜色
	@BindingAdapter("android:background")
	public static void setBackground(View v, Drawable name) {
		Drawable color;
		if (colors == null)
			color = name;
		else
			try {
				color = colors.getColor(name);
			} catch (NullPointerException e) {
				color = name;
			}
		v.setBackground(color);
	}
	//处理皮肤颜色
	@BindingAdapter("android:tint")
	public static void setTint(View v, Drawable name) {
		int color;
		if (colors == null)
			color = name;
		else
			try {
				color = colors.getColor(name);
			} catch (NullPointerException e) {
				color = name;
			}
		v.setBackgroundTintList(ColorStateList.valueOf(color));
	}
	
	//处理皮肤颜色
	@BindingAdapter("background")
	public static void setBackground(View v, String id_name) {
		int color = 0;
		if (Objects.equals(id_name, getIdString(R.string.id_dialog_bg)))
			color = drawablesId.get(0);
		if (Objects.equals(id_name, getIdString(R.string.id_editor)))
			color = drawablesId.get(1);
		if (Objects.equals(id_name, getIdString(R.string.id_menu_small_bar_bg)))
			color = drawablesId.get(2);
		if (Objects.equals(id_name, getIdString(R.string.id_pop_bg)))
			color = drawablesId.get(3);
		if (Objects.equals(id_name, getIdString(R.string.id_progress_bar)))
			color = drawablesId.get(4);
		if (Objects.equals(id_name, getIdString(R.string.id_ripple_5)))
			color = drawablesId.get(5);
		if (Objects.equals(id_name, getIdString(R.string.id_ripple_15)))
			color = drawablesId.get(6);
		if (Objects.equals(id_name, getIdString(R.string.id_ripple_circle)))
			color = drawablesId.get(7);
		if (Objects.equals(id_name, getIdString(R.string.id_ripple_circle_outer)))
			color = drawablesId.get(8);
		if (Objects.equals(id_name, getIdString(R.string.id_ripple_dialog_button)))
			color = drawablesId.get(9);
		if (Objects.equals(id_name, getIdString(R.string.id_ripple_normal)))
			color = drawablesId.get(10);
		if (Objects.equals(id_name, getIdString(R.string.id_ripple_positive)))
			color = drawablesId.get(11);
		if (Objects.equals(id_name, getIdString(R.string.id_search_bar_bg)))
			color = drawablesId.get(12);
		if (Objects.equals(id_name, getIdString(R.string.id_searchbar_index)))
			color = drawablesId.get(13);
		
		Drawable color2;
		if (colors == null)
			color2 = ResourceUtils.getDrawable(color);
		else
			try {
				color2 = colors.getSkinDrawable(color);
			} catch (NullPointerException e) {
				color2 = ResourceUtils.getDrawable(color);
			}
		v.setBackground(color2);
	}
	
	
	public void setMode(int mode) {
		if (mode == SkinMode.DAY)
			colors = day;
		else
			colors = night;
		//notifyPropertyChanged(BR.skin);
	}
	
	public int getMode() {
		return colors == day ? SkinMode.DAY : SkinMode.NIGHT;
	}
	
	private static String getIdString(int id) {
		return StringUtils.getString(id);
	}
	
	
	public static class SkinMode {
		public static final int DAY = 0;
		public static final int NIGHT = 1;
	}
}
*/

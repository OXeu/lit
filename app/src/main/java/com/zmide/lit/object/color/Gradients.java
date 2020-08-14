package com.zmide.lit.object.color;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import java.util.HashMap;
import java.util.Random;

public class Gradients {
	private static int[][] gradientColors = {
			{0xee9ca7,0xffdde1},
			{0x373B44,0x4286f4},
			{0x8E2DE2,0x4A00E0},
			{0x7F7FD5,0x86A8E7,0x91EAE4},
			{0xf12711,0xf5af19},
			{0x4e54c8,0x8f94fb},
			{0x11998e,0x38ef7d},
			{0xFC5C7D,0x6A82FB},
			{0xff9966,0xff5e62},
			{0x396afc,0x2948ff},
			{0x56CCF2,0x2F80ED},
			{0x6190E8,0xA7BFE8},
			{0xee0979,0xff6a00},
			{0xf4c4f3,0xfc67fa},
			{0xFF5F6D,0xFFC371},
			{0xee9ca7,0xffdde1},
			{0xffb347,0xffcc33},
			{0x757F9A,0xD7DDE8},
			{0x9796f0,0xfbc7d4}
	};
	private static HashMap<String,Integer> mapping = new HashMap<>();
	private static int randomStart = new Random().nextInt(gradientColors.length);
	
	public static Drawable getDrawable(String name){
		Integer num = mapping.get(name);
		num = num==null?-1:num;
		if(num==-1){
			num = (randomStart+mapping.size())%gradientColors.length;
			mapping.put(name,num);
			return getDrawable(gradientColors[num]);
		}else{
			return getDrawable(gradientColors[num]);
		}
		
	}
	
	public static Drawable getDrawable(int... colors){
			
			int roundRadius = 15;     // 15dp 圆角半径
			GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TL_BR, colors);
			gd.setColor(0x484848);
			gd.setCornerRadius(roundRadius);
			gd.setShape(GradientDrawable.RECTANGLE);
			gd.setGradientType(GradientDrawable.LINEAR_GRADIENT);
			return gd;
	}
}

package com.zmide.lit.main;

import com.zmide.lit.ui.MainActivity;

public class MProgressManager {
	private static MainActivity activity;
	
	public static void init(MainActivity mainActivity) {
		if (activity == null)
			activity = mainActivity;
	}
	
	public static void setProgress(int newProgress) {
		MainViewBindUtils.getProgressBar().setLitProgressBar(newProgress);
	}
}

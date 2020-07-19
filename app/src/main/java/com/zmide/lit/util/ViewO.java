package com.zmide.lit.util;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class ViewO {
	public static void hideView(final View view) {
		Animation animation = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t) {
				interpolatedTime = 1 - interpolatedTime;
				view.setAlpha(interpolatedTime);
				if (interpolatedTime == 0) {
					view.setVisibility(View.GONE);
				}
			}
			
			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};
		animation.setDuration(200);
		view.startAnimation(animation);
	}
	
	public static void showView(final View view) {
		view.setVisibility(View.VISIBLE);
		view.setAlpha(0);
		Animation animation = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t) {
				view.setAlpha(interpolatedTime);
			}
			
			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};
		animation.setDuration(500);
		view.startAnimation(animation);
	}
	
	public static void makeViewTranslucent(View view, float endAlpha) {
		float startAlpha = view.getAlpha();
		Animation animation = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t) {
				view.setAlpha(startAlpha + (endAlpha - startAlpha) * interpolatedTime);
			}
			
			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};
		animation.setDuration(500);
		view.startAnimation(animation);
	}
}

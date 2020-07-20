package com.zmide.lit.util;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.Utils;

public class MKeyboardUtils {
	
	private static int sDecorViewDelta = 0;
	private static ViewTreeObserver.OnGlobalLayoutListener listener;
	
	
	/**
	 * Fix the bug of 5497 in Android.
	 * <p>Don't set adjustResize</p>
	 *
	 * @param activity The activity.
	 */
	public static void fixAndroidBug5497(@NonNull final Activity activity) {
		fixAndroidBug5497(activity.getWindow());
	}
	
	/**
	 * Fix the bug of 5497 in Android.
	 * <p>Don't set adjustResize</p>
	 *
	 * @param window The window.
	 */
	public static void fixAndroidBug5497(@NonNull final Window window) {
//        int softInputMode = window.getAttributes().softInputMode;
//        window.setSoftInputMode(softInputMode & ~WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		final FrameLayout contentView = window.findViewById(android.R.id.content);
		final View contentViewChild = contentView.getChildAt(0);
		final int paddingBottom = contentViewChild.getPaddingBottom();
		final int[] contentViewInvisibleHeightPre5497 = {getContentViewInvisibleHeight(window)};
		if (listener == null)
		listener = () -> {
			int height = getContentViewInvisibleHeight(window);
			if (contentViewInvisibleHeightPre5497[0] != height) {
				contentViewChild.setPadding(
						contentViewChild.getPaddingLeft(),
						contentViewChild.getPaddingTop(),
						contentViewChild.getPaddingRight(),
						paddingBottom + getDecorViewInvisibleHeight(window)
				);
				contentViewInvisibleHeightPre5497[0] = height;
			}
		};
		contentView.getViewTreeObserver()
				.addOnGlobalLayoutListener(listener);
	}
	
	public static void ignoreAndroidBug5497(@NonNull final Window window) {
//        int softInputMode = window.getAttributes().softInputMode;
//        window.setSoftInputMode(softInputMode & ~WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		final FrameLayout contentView = window.findViewById(android.R.id.content);
		final View contentViewChild = contentView.getChildAt(0);
		final int[] contentViewInvisibleHeightPre5497 = {getContentViewInvisibleHeight(window)};
		if (listener != null)
		contentView.getViewTreeObserver()
				.removeOnGlobalLayoutListener(listener);
	}
	
	
	private static int getContentViewInvisibleHeight(final Window window) {
		final View contentView = window.findViewById(android.R.id.content);
		if (contentView == null) return 0;
		final Rect outRect = new Rect();
		contentView.getWindowVisibleDisplayFrame(outRect);
		Log.d("KeyboardUtils", "getContentViewInvisibleHeight: "
				+ (contentView.getBottom() - outRect.bottom));
		int delta = Math.abs(contentView.getBottom() - outRect.bottom);
		if (delta <= getStatusBarHeight() + getNavBarHeight()) {
			return 0;
		}
		return delta;
	}
	
	
	private static int getDecorViewInvisibleHeight(@NonNull final Window window) {
		final View decorView = window.getDecorView();
		final Rect outRect = new Rect();
		decorView.getWindowVisibleDisplayFrame(outRect);
		Log.d("KeyboardUtils", "getDecorViewInvisibleHeight: "
				+ (decorView.getBottom() - outRect.bottom));
		int delta = Math.abs(decorView.getBottom() - outRect.bottom);
		if (delta <= getNavBarHeight() + getStatusBarHeight()) {
			sDecorViewDelta = delta;
			return 0;
		}
		return delta - sDecorViewDelta;
	}
	
	private static int getStatusBarHeight() {
		Resources resources = Utils.getApp().getResources();
		int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
		return resources.getDimensionPixelSize(resourceId);
	}
	
	private static int getNavBarHeight() {
		Resources res = Utils.getApp().getResources();
		int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
		if (resourceId != 0) {
			return res.getDimensionPixelSize(resourceId);
		} else {
			return 0;
		}
	}
}

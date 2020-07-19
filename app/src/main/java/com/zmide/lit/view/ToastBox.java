package com.zmide.lit.view;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zmide.lit.R;

/**
 * @author chaowen
 */
public class ToastBox {
	
	private static final int ANIMATION_DURATION = 600;
	
	private int HIDE_DELAY = 5000;
	
	private View mContainer;
	
	private int gravity = Gravity.CENTER;
	
	private TextView mTextView;
	
	private Handler mHandler;
	
	private AlphaAnimation mFadeInAnimation;
	
	private AlphaAnimation mFadeOutAnimation;
	private final Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mContainer.startAnimation(mFadeOutAnimation);
		}
	};
	private TextView mButton;
	
	public ToastBox(Context context) {
		ViewGroup container = ((Activity) context)
				.findViewById(android.R.id.content);
		View v = ((Activity) context).getLayoutInflater().inflate(
				R.layout.utils_toast, container);
		//this.gravity = gravity;
		init(container, v);
	}
	
	private void init(ViewGroup container, View v) {
		mContainer = v.findViewById(R.id.toastContainer);
		mContainer.setVisibility(View.GONE);
		mTextView = v.findViewById(R.id.toastText);
		mButton = v.findViewById(R.id.toastButton);
		mFadeInAnimation = new AlphaAnimation(0.0f, 1.0f);
		mFadeOutAnimation = new AlphaAnimation(1.0f, 0.0f);
		mFadeOutAnimation.setDuration(ANIMATION_DURATION);
		mFadeOutAnimation
				.setAnimationListener(new Animation.AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						mContainer.setVisibility(View.GONE);
						container.removeView(v);
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
					}
				});
		
		mHandler = new Handler();
		
	}
	
	public ToastBox setContext(String msg, String action, View.OnClickListener listener, int time) {
		mTextView.setText(msg);
		this.HIDE_DELAY = time;
		if (action != null && listener != null) {
			mButton.setText(action);
			mButton.setOnClickListener((view) -> {
				mContainer.setVisibility(View.GONE);
				((ViewGroup) ((Activity) view.getContext())
						.findViewById(android.R.id.content)).removeView(mContainer);
				listener.onClick(view);
			});
		}
		return this;
	}
	
	public void show() {
		mContainer.setVisibility(View.VISIBLE);
		((LinearLayout) mContainer).setGravity(Gravity.CENTER);
		mFadeInAnimation.setDuration(ANIMATION_DURATION);
		mContainer.startAnimation(mFadeInAnimation);
		mHandler.postDelayed(mHideRunnable, HIDE_DELAY);
	}
	
}
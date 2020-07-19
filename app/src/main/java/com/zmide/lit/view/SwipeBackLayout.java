package com.zmide.lit.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;

import androidx.viewpager.widget.ViewPager;

import com.zmide.lit.R;
import com.zmide.lit.base.BaseActivity;

import java.util.LinkedList;
import java.util.List;

/**
 * @author xiaanming
 * @blog http://blog.csdn.net/xiaanming
 */
public class SwipeBackLayout extends FrameLayout {
	private View mContentView;
	private int mTouchSlop;
	private int downX;
	private int downY;
	private int tempX;
	private Scroller mScroller;
	private int viewWidth;
	private boolean isSilding;
	private boolean isFinish;
	private Drawable mShadowDrawable;
	private BaseActivity mActivity;
	private List<ViewPager> mViewPagers = new LinkedList<>();
	
	public SwipeBackLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public SwipeBackLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		mScroller = new Scroller(context);
		
		mShadowDrawable = getResources().getDrawable(R.drawable.shadow_left);
	}
	
	
	public void attachToActivity(BaseActivity activity) {
		mActivity = activity;
		TypedArray a = activity.getTheme().obtainStyledAttributes(
				new int[]{android.R.attr.windowBackground});
		int background = a.getResourceId(0, 0);
		a.recycle();
		ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
		ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
		decorChild.setBackgroundResource(background);
		decor.removeView(decorChild);
		addView(decorChild);
		setContentView(decorChild);
		decor.addView(this);
	}
	
	private void setContentView(View decorChild) {
		mContentView = (View) decorChild.getParent();
	}
	
	/**
	 * Redesign By Xeu
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		//ViewPager
		if (!"MainActivity".equals(mActivity.TAG) && !"ModBallSetting".equals(mActivity.TAG) && !"VideoPlayerActivity".equals(mActivity.TAG)) {
			ViewPager mViewPager = getTouchViewPager(mViewPagers, ev);
			
			
			if (mViewPager != null && mViewPager.getCurrentItem() != 0) {
				return super.onInterceptTouchEvent(ev);
			}
			
			switch (ev.getAction()) {
				case MotionEvent.ACTION_DOWN:
					downX = tempX = (int) ev.getRawX();
					downY = (int) ev.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					int moveX = (int) ev.getRawX();
					// SildingFinishLayout Touch Event
					if (moveX - downX > mTouchSlop
							&& Math.abs((int) ev.getRawY() - downY) < mTouchSlop) {
						return true;
					}
					break;
			}
			
			return super.onInterceptTouchEvent(ev);
		}
		return false;
	}
	
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!mActivity.TAG.equals("MainActivity")) {
			switch (event.getAction()) {
				case MotionEvent.ACTION_MOVE:
					int moveX = (int) event.getRawX();
					int deltaX = tempX - moveX;
					tempX = moveX;
					if (downX < viewWidth && moveX >= downX) {
						if (moveX - downX > mTouchSlop
								&& Math.abs((int) event.getRawY() - downY) < mTouchSlop) {
							isSilding = true;
						}
						
						if (moveX - downX >= 0 && isSilding) {
							mContentView.scrollBy(deltaX, 0);
							
						}
					}
					break;
				case MotionEvent.ACTION_UP:
					isSilding = false;
					if (mContentView.getScrollX() <= -viewWidth / 3) {
						isFinish = true;
						scrollRight();
					} else {
						scrollOrigin();
						isFinish = false;
					}
					break;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * SwipeBackLayout ViewPager
	 *
	 * @param mViewPagers viewpager
	 * @param parent      parent
	 */
	private void getAlLViewPager(List<ViewPager> mViewPagers, ViewGroup parent) {
		int childCount = parent.getChildCount();
		for (int i = 0; i < childCount; i++) {
			View child = parent.getChildAt(i);
			if (child instanceof ViewPager) {
				mViewPagers.add((ViewPager) child);
			} else if (child instanceof ViewGroup) {
				getAlLViewPager(mViewPagers, (ViewGroup) child);
			}
		}
	}
	
	
	/**
	 * touch ViewPager
	 *
	 * @param mViewPagers list
	 * @param ev          ev
	 * @return pager
	 */
	private ViewPager getTouchViewPager(List<ViewPager> mViewPagers, MotionEvent ev) {
		if (mViewPagers == null || mViewPagers.size() == 0) {
			return null;
		}
		Rect mRect = new Rect();
		for (ViewPager v : mViewPagers) {
			v.getHitRect(mRect);
			
			if (mRect.contains((int) ev.getX(), (int) ev.getY())) {
				return v;
			}
		}
		return null;
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed) {
			viewWidth = this.getWidth();
			
			getAlLViewPager(mViewPagers, this);
			
		}
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (mShadowDrawable != null && mContentView != null) {
			
			int left = mContentView.getLeft()
					- mShadowDrawable.getIntrinsicWidth();
			int right = left + mShadowDrawable.getIntrinsicWidth();
			int top = mContentView.getTop();
			int bottom = mContentView.getBottom();
			mShadowDrawable.setBounds(left, top, right, bottom);
			if (viewWidth != 0)
				mShadowDrawable.setAlpha((viewWidth - mContentView.getScrollX() * -255) / viewWidth);
			mShadowDrawable.draw(canvas);
		}
		
	}
	
	
	/**
	 *
	 */
	private void scrollRight() {
		final int delta = (viewWidth + mContentView.getScrollX());
		
		mScroller.startScroll(mContentView.getScrollX(), 0, -delta + 1, 0,
				Math.abs(delta));
		postInvalidate();
	}
	
	private void scrollOrigin() {
		int delta = mContentView.getScrollX();
		mScroller.startScroll(mContentView.getScrollX(), 0, -delta, 0,
				Math.abs(delta));
		postInvalidate();
	}
	
	@Override
	public void computeScroll() {
		
		if (mScroller.computeScrollOffset()) {
			mContentView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
			
			if (mScroller.isFinished() && isFinish) {
				mActivity.finish();
			}
		}
	}
	
	
}

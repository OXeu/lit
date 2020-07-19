package com.zmide.lit.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.recyclerview.widget.RecyclerView;

import com.zmide.lit.R;

/**
 * Copyright (C), 2019-2020, DreamStudio
 * Author: Xeu
 * Date: 2020/2/27 16:27
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class MaxHeightRecyclerView extends RecyclerView {
	private int mMaxHeight;
	
	public MaxHeightRecyclerView(Context context) {
		super(context);
	}
	
	public MaxHeightRecyclerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context, attrs);
	}
	
	public MaxHeightRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initialize(context, attrs);
	}
	
	private void initialize(Context context, AttributeSet attrs) {
		TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.MaxHeightRecyclerView);
		mMaxHeight = arr.getLayoutDimension(R.styleable.MaxHeightRecyclerView_maxHeight, mMaxHeight);
		arr.recycle();
		setOverScrollMode(OVER_SCROLL_NEVER);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (mMaxHeight > 0) {
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.AT_MOST);
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}
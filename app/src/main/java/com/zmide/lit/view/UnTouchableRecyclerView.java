package com.zmide.lit.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.recyclerview.widget.RecyclerView;

import com.zmide.lit.R;
import android.view.MotionEvent;

/**
 * Copyright (C), 2019-2020, DreamStudio
 * Author: Xeu
 * Date: 2020/2/27 16:27
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class UnTouchableRecyclerView extends RecyclerView {
	private int mMaxHeight;

	public UnTouchableRecyclerView(Context context) {
		super(context);
	}

	public UnTouchableRecyclerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}

	public UnTouchableRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		
		return false;
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev){
		return super.dispatchTouchEvent(ev);
	} 
	
	
}

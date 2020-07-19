package com.zmide.lit.interfaces;

import android.view.MotionEvent;
import android.view.View;

import com.zmide.lit.object.Tag;

public interface TagViewOperate {
	void TagLongClickListener(View view, MotionEvent ev, Tag tag);
	
	void onSizeChanged(int size);
	
	void onLoadChildIndex(String child);
}

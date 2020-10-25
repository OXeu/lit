package com.zmide.lit.view;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import com.zmide.lit.base.MApplication;

public class TouchableRelativeLayout extends RelativeLayout {

	public TouchableRelativeLayout(Context context) {
		super(context);
	}

	public TouchableRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
	}

	public TouchableRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}


	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		getParent().requestDisallowInterceptTouchEvent(false);
        return super.dispatchTouchEvent(event);
	}



}

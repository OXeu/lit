package com.zmide.lit.view;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import com.zmide.lit.base.MApplication;

public class TouchableRelativeLayout extends RelativeLayout {

	public TouchableRelativeLayout(Context context){
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
		int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //不允许父View拦截事件
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return super.onTouchEvent(event);
	}
    
	
    
}

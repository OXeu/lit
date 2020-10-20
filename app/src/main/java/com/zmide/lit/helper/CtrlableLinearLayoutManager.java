package com.zmide.lit.helper;



import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;

public class CtrlableLinearLayoutManager extends LinearLayoutManager {
    private boolean mCanVerticalScroll = true;

	private boolean mCanHorizontalScroll = false;;

    public CtrlableLinearLayoutManager(Context context,int direction,boolean b) {
        super(context,direction,b);
    }


	@Override
    public boolean canScrollVertically() {
        if (!mCanVerticalScroll){
            return false;
        }else {
            return super.canScrollVertically();
        }
    }
	
	@Override
    public boolean canScrollHorizontally() {
        if (!mCanHorizontalScroll){
            return false;
        }else {
            return super.canScrollHorizontally();
        }
    }
	
    public void setCanHorizontalScroll(boolean b){
        mCanVerticalScroll = b;
    }
	public void setCanVerticalScroll(boolean b){
        mCanHorizontalScroll = b;
    }
}



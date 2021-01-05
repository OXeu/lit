package com.zmide.lit.main.bottom;

import android.widget.RelativeLayout;

public class SimplifyBottomBar extends BaseBottom {

	private static BaseBottom instance;

	public static BaseBottom getInstance(){
		if(instance == null)
			instance = new BaseBottom();
		return instance;
	}
	
	
	@Override
    public void loadBar(RelativeLayout btParent){

	}


}

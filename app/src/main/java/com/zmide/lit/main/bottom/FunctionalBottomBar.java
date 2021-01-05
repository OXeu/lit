package com.zmide.lit.main.bottom;
import android.widget.RelativeLayout;

public class FunctionalBottomBar extends BaseBottom {

	private static FunctionalBottomBar instance;
    
	public static BaseBottom getInstance(){
		if(instance == null)
			instance = new FunctionalBottomBar();
		return instance;
	}
	
	
	@Override
    public void loadBar(RelativeLayout btParent){

	}
    
    
}

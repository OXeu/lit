package com.zmide.lit.main.bottom;
import android.widget.RelativeLayout;
import android.view.View;
import android.view.LayoutInflater;

public class ClassicalBottomBar {

	public static void loadBar(RelativeLayout btParent) {
		View view = LayoutInflater.from(btParent.getContext()).inflate(R.layout.bottom_bar);
		btParent.addView(view);
	}
    
	
}

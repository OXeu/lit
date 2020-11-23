package com.zmide.lit.main.bottom;
import com.zmide.lit.R;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public class ClassicalBottomBar {

	public static void loadBar(RelativeLayout btParent) {
		View view = LayoutInflater.from(btParent.getContext()).inflate(R.layout.bottom_bar,null);
		btParent.addView(view);
	}
    
	
}

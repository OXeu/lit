package com.zmide.lit.main.bottom;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zmide.lit.R;

public class ClassicalBottomBar {

	public static void loadBar(RelativeLayout btParent) {
		View view = LayoutInflater.from(btParent.getContext()).inflate(R.layout.bottom_bar, null);
		ImageView leftBt = view.findViewById(R.id.leftBt);
		ImageView rightBt = view.findViewById(R.id.rightBt);
		TextView title = view.findViewById(R.id.title);


		btParent.addView(view);
	}
    
	
}

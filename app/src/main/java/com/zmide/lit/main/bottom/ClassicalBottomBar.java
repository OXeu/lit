package com.zmide.lit.main.bottom;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zmide.lit.R;
import android.view.View.OnClickListener;
import com.zmide.lit.main.WindowsManager;
import com.zmide.lit.main.MenuDialog;
import com.zmide.lit.main.SearchEnvironment;

public class ClassicalBottomBar extends BaseBottom{
	
	private TextView title;
	@Override
	public void loadBar(RelativeLayout btParent) {
		View view = LayoutInflater.from(btParent.getContext()).inflate(R.layout.bottom_bar, null);
		ImageView leftBt = view.findViewById(R.id.leftBt);
		ImageView rightBt = view.findViewById(R.id.rightBt);
		title = view.findViewById(R.id.title);
		leftBt.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					WindowsManager.loadWindows();
				}
			});
			
		rightBt.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					MenuDialog.showDialog();
				}
			});
			
		title.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					SearchEnvironment.openSearchBar(null);
				}
			});

		btParent.addView(view);
	}

	@Override
	public void onTitleChanged(String title) {
		super.onTitleChanged(title);
		this.title.setText(title);
	}
	
	
    
	
}

package com.zmide.lit.main.bottom;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zmide.lit.R;
import android.view.View.OnClickListener;
import com.zmide.lit.main.WindowsManager;
import com.zmide.lit.main.MenuDialog;
import com.zmide.lit.main.SearchEnvironment;
import com.zmide.lit.util.MToastUtils;

public class ClassicalBottomBar extends BaseBottom {

	
	
	private TextView titleBar ;

	private RelativeLayout btParent;
	@Override
	public void loadBar(RelativeLayout btParent) {
		this.btParent = btParent;
		View view = LayoutInflater.from(btParent.getContext()).inflate(R.layout.bottom_bar, null);
		View leftBt = view.findViewById(R.id.leftBt);
		View rightBt = view.findViewById(R.id.rightBt);
		this.titleBar = view.findViewById(R.id.title_bar);
		titleBar.setText("酸奶酪Yogurt");
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
			
		titleBar.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					SearchEnvironment.openSearchBar(null);
				}
			});

		btParent.addView(view);
		
		leftBt.setOnTouchListener((view2, event) -> {
			return false;
		});
		rightBt.setOnTouchListener((view2, event) -> {
			return false;
		});
		titleBar.setOnTouchListener((view2, event) -> {
			return false;
		});
	}

	@Override
	public void onTitleChanged(String title) {
		View view = LayoutInflater.from(btParent.getContext()).inflate(R.layout.bottom_bar, null);
		this.titleBar = view.findViewById(R.id.title_bar);
		//MToastUtils.makeText(title).show();
		titleBar.setText(title);
		MToastUtils.makeText(titleBar.getText().toString()+title).show();
	}
	
	
    
	
}

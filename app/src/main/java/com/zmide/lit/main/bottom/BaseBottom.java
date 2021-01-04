package com.zmide.lit.main.bottom;
import android.widget.RelativeLayout;
import com.zmide.lit.interfaces.WindowsInterface;
import com.zmide.lit.interfaces.WebInterface;
import android.graphics.Bitmap;

public class BaseBottom implements WebInterface {

	@Override
	public void onTitleChanged(String title) {
	}

	@Override
	public void onIconChanged(Bitmap favicon) {
	}

	@Override
	public void onUrlChanged(String url) {
	}
	
	private static BaseBottom instance;
	public static BaseBottom getInstance(){
		if(instance == null)
			instance = new BaseBottom();
		return instance;
	}
	
	public void loadBar(RelativeLayout btParent){}
    
	
}

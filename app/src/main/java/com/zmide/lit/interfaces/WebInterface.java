package com.zmide.lit.interfaces;

import android.graphics.Bitmap;

public interface WebInterface {//该接口由WebView发出
	
	void onTitleChanged(String title);
	
	void onIconChanged(Bitmap favicon);
	
	void onUrlChanged(String url);
}

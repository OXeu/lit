package com.zmide.lit.javascript;

import android.app.Activity;
import android.webkit.JavascriptInterface;

import com.zmide.lit.main.BallEnvironment;
import com.zmide.lit.main.SearchEnvironment;
import com.zmide.lit.main.WebEnvironment;

public class Jsi {
	
	private static Activity ac;
	private static WebEnvironment we;
	private static Jsi instance;
	
	public static void init(Activity context, WebEnvironment wei) {
		ac = context;
		we = wei;
	}
	
	public static Jsi getInstance() {
		if (instance == null)
			instance = new Jsi();
		return instance;
	}
	
	
	@JavascriptInterface
	public void resetBall() {
		BallEnvironment.resetBall();
	}
	
	@JavascriptInterface
	public void search(final String input) {
		ac.runOnUiThread(() -> {
			SearchEnvironment.Search(input);
		});
	}
}

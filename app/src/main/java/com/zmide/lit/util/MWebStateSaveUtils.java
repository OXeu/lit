package com.zmide.lit.util;

import android.annotation.SuppressLint;
import android.content.Context;

import com.zmide.lit.object.WebState;

import java.util.ArrayList;

public class MWebStateSaveUtils {
	@SuppressLint("StaticFieldLeak")
	private static Context context;
	
	public static void init(Context contextTemp) {
		context = contextTemp;
	}
	
	public static void saveState(int codeId, String url) {
		DBC.getInstance(context).saveState(codeId, url);
	}
	
	public static String resumeState(int sid) {
		String url = DBC.getInstance(context).getState(sid);
		if (url == null)
			return "";
		return url;
	}
	
	public static ArrayList<WebState> resumeAllStates() {
		return DBC.getInstance(context).getStates();
	}
	
	public static boolean hasStates() {
		return DBC.getInstance(context).hasStates();
	}
	
	public static void deleteAllStates() {
		DBC.getInstance(context).deleteAllState();
	}
	
	public static void deleteStates(int code) {
		DBC.getInstance(context).deleteState(code);
	}
}

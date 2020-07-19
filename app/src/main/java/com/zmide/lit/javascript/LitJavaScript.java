package com.zmide.lit.javascript;

import android.app.Activity;
import android.webkit.JavascriptInterface;

import com.zmide.lit.object.Diy;
import com.zmide.lit.util.DBC;
import com.zmide.lit.util.MFileUtils;
import com.zmide.lit.util.MToastUtils;

import org.json.JSONException;
import org.json.JSONObject;


public class LitJavaScript {
	private static Activity ac;
	private static LitJavaScript instance;
	
	public static void init(Activity context) {
		ac = context;
	}
	
	public static LitJavaScript getInstance() {
		if (instance == null)
			instance = new LitJavaScript();
		return instance;
	}
	
	
	@JavascriptInterface
	public void addon(final String addon) {
		String decode = MFileUtils.setDecrypt(addon);
		try {
			JSONObject jsonObject = new JSONObject(decode);
			int id = jsonObject.getInt("id");
			String author = jsonObject.getString("author");
			String name = jsonObject.getString("name");
			String url = jsonObject.getString("url");
			String code = jsonObject.getString("code");
			if (!DBC.getInstance(ac).isDiyExist(id + "")) {
				DBC.getInstance(ac).addDiy(id + "", name, "Author:" + author, MFileUtils.setDecrypt(code), "[#]" + url, Diy.PLUGIN, true);
				MToastUtils.makeText("导入成功").show();
			} else {
				DBC.getInstance(ac).deleteDiys(id + "");
				MToastUtils.makeText("卸载成功").show();
			}
			
		} catch (JSONException e) {
			MToastUtils.makeText("导入失败，解析失败").show();
		}
		
	}
	
	@JavascriptInterface
	public String getInstalledAddonID() {
		return DBC.getInstance(ac).getInstalled();
	}
	
}


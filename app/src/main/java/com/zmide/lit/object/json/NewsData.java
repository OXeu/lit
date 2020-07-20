package com.zmide.lit.object.json;

import java.util.Map;

public class NewsData {
	public int code;
	public String title;
	public String message;
	public String url;
	public String time;
	
	public NewsData(Map<String, Object> data) {
		title = data.get("title") + "";
		message = data.get("message") + "";
		url = data.get("url") + "";
		time = data.get("time") + "";
	}
}

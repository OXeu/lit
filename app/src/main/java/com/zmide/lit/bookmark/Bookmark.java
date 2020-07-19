package com.zmide.lit.bookmark;

/*
 * Copyright (C), 2019-2020, DreamStudio
 * Author: Xeu
 * Date: 2020/2/28 12:22
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Deque;

public class Bookmark {
	
	public String name;
	public String URL;
	public String description;
	public String date;
	public ArrayList<String> tag = new ArrayList<>();
	
	@NonNull
	@Override
	public String toString() {
		String sep = " ; ";
		return "\"" + name + "\"" + sep
				+ URL + sep
				+ "\"" + description + "\"" + sep
				+ "\"" + date + "\"" + sep
				+ "\"" + tag + "\"";
		
		//return tag.size() + " " + s;
		//return String.join(".",tag);
	}
	
	void addDeque(Deque<String> deque) {
		tag = new ArrayList<>();
		tag.addAll(deque);
	}
}

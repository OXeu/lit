package com.zmide.lit.object;

import java.util.HashMap;

public class GMap {
	
	public static Object getOrDefault(HashMap<String, Object> map, String key, Object defaultValue) {
		Object value = map.get(key);
		if (value == null)
			return defaultValue;
		return value;
	}
	
	
}

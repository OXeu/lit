package com.zmide.lit.object.json;

import java.util.Map;

/**
 * Copyright (C), 2019-2020, DreamStudio
 * Author: Xeu
 * Date: 2020/2/24 22:18
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class DataUpdate {
	public int code;
	public String msg;
	public UpdateData data;
	
	/**
	 * Copyright (C), 2019-2020, DreamStudio
	 * Author: Xeu
	 * Date: 2020/2/24 21:31
	 * Description:
	 * History:
	 * <author> <time> <version> <desc>
	 * 作者姓名 修改时间 版本号 描述
	 */
	public static class UpdateData {
		public String id;
		public String version;
		public String version_id;
		public String update_log;
		public String url;
		public String update_time;
		
		public UpdateData(Map<String, Object> data) {
			id = data.get("id") + "";
			version = data.get("version") + "";
			version_id = data.get("version_id") + "";
			update_log = data.get("update_log") + "";
			update_time = data.get("update_time") + "";
			url = data.get("url") + "";
		}
	}
}

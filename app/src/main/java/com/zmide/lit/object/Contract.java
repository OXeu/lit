package com.zmide.lit.object;

import android.provider.BaseColumns;

public final class Contract {
	private Contract() {
	}
	
	public static final class MarkEntry implements BaseColumns {
		
		public final static String TABLE_NAME = "marks";
		public final static String _ID = BaseColumns._ID;
		public final static String _NAME = "name";
		public final static String _URL = "url";
		public final static String _TIME = "time";
		public final static String _PARENT = "parent";
		public final static String _ICON = "icon";
		public final static String _DID = "did";
		public final static String _LEVEL = "level";
		
	}
	
	public static final class ParentEntry implements BaseColumns {
		public final static String TABLE_NAME = "parents";
		public final static String _ID = BaseColumns._ID;
		public final static String _NAME = "name";
		public final static String _PARENT = "parent";
		public final static String _ICON = "icon";
		public final static String _DID = "did";
		public final static String _LEVEL = "level";
		public final static String _TIME = "time";
	}
	
	public static final class HistoryEntry implements BaseColumns {
		public final static String TABLE_NAME = "historys";
		public final static String _ID = BaseColumns._ID;
		public final static String _NAME = "name";
		public final static String _TIME = "time";
		public final static String _ICON = "icon";
		public final static String _URL = "url";
	}
	
	public static final class DiyEntry implements BaseColumns {
		public final static String TABLE_NAME = "diys";
		public final static String _ID = BaseColumns._ID;
		public final static String _NAME = "title";
		public final static String _TIME = "time";
		public final static String _EXTRA = "extra";
		public final static String _VALUE = "value";
		public final static String _TYPE = "type";
		public final static String _ISRUN = "isrun";
		public final static String _DESCRIPTION = "description";
		public final static String _SID = "id";
	}
	
	public static final class StateEntry implements BaseColumns {
		public final static String TABLE_NAME = "state";
		public final static String _ID = BaseColumns._ID;
		public final static String _URL = "url";
		public final static String _SID = "sid";
	}
	
	public static final class WebsiteEntry implements BaseColumns {
		public final static String TABLE_NAME = "website";
		public final static String _ID = BaseColumns._ID;
		public final static String _SITE = "site";
		public final static String _STATE = "state";
		public final static String _NO_HISTORY = "no_history";
		public final static String _JS = "js";
		public final static String _NO_PIC = "no_picture";
		public final static String _UA = "ua";
		public final static String _APP = "app";
		public final static String _AD_HOST = "ad_host";
	}
	
}

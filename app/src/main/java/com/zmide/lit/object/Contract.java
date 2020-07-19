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
		
	}
	
	public static final class ParentEntry implements BaseColumns {
		public final static String TABLE_NAME = "parents";
		public final static String _ID = BaseColumns._ID;
		public final static String _NAME = "name";
		public final static String _PARENT = "parent";
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
	
}

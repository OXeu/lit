package com.zmide.lit.util;
/*
 * Database helper for Lit app. Manages database creation and version management.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zmide.lit.object.Contract;

public class DbHelper extends SQLiteOpenHelper {
	
	/**
	 * Name of the database file
	 */
	private static final String DATABASE_NAME = "browse.db";
	
	/**
	 * Database version. If you change the database schema, you must increment the database version.
	 */
	private static final int DATABASE_VERSION = 11;
	
	/**
	 * Constructs a new instance of {@link DbHelper}.
	 *
	 * @param context of the app
	 */
	DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	/**
	 * This is called when the database is created for the first time.
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// Create a String that contains the SQL statement to create the marks table
		String SQL_CREATE_MARKS_TABLE = "CREATE TABLE " + Contract.MarkEntry.TABLE_NAME + " ("
				+ Contract.MarkEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ Contract.MarkEntry._NAME + " TEXT NOT NULL, "
				+ Contract.MarkEntry._URL + " TEXT NOT NULL, "
				+ Contract.MarkEntry._TIME + " INTEGER NOT NULL DEFAULT 0, "
				+ Contract.MarkEntry._ICON + " TEXT, "
				+ Contract.ParentEntry._DID + " INTEGER NOT NULL DEFAULT 0, "
				+ Contract.ParentEntry._LEVEL + " INTEGER NOT NULL DEFAULT 0, "
				+ Contract.MarkEntry._PARENT + " INTEGER NOT NULL DEFAULT 0);";
		
		// Create a String that contains the SQL statement to create the parents table
		String SQL_CREATE_PARENTS_TABLE = "CREATE TABLE " + Contract.ParentEntry.TABLE_NAME + " ("
				+ Contract.ParentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ Contract.ParentEntry._NAME + " TEXT NOT NULL, "
				+ Contract.ParentEntry._ICON + " TEXT, "
				+ Contract.ParentEntry._DID + " INTEGER, "
				+ Contract.ParentEntry._LEVEL + " INTEGER, "
				+ Contract.ParentEntry._TIME + " INTEGER NOT NULL, "
				+ Contract.ParentEntry._PARENT + " INTEGER NOT NULL DEFAULT 0);";
		
		// Create a String that contains the SQL statement to create the historys table
		String SQL_CREATE_HISTORYS_TABLE = "CREATE TABLE " + Contract.HistoryEntry.TABLE_NAME + " ("
				+ Contract.HistoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ Contract.HistoryEntry._NAME + " TEXT NOT NULL, "
				+ Contract.HistoryEntry._URL + " TEXT NOT NULL, "
				+ Contract.HistoryEntry._TIME + " INTEGER NOT NULL DEFAULT 0, "
				+ Contract.HistoryEntry._ICON + " TEXT);";
		
		String SQL_CREATE_DIYS_TABLE = "CREATE TABLE " + Contract.DiyEntry.TABLE_NAME + " ("
				+ Contract.DiyEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ Contract.DiyEntry._NAME + " TEXT NOT NULL, "
				+ Contract.DiyEntry._VALUE + " TEXT NOT NULL, "
				+ Contract.DiyEntry._DESCRIPTION + " TEXT NOT NULL, "
				+ Contract.DiyEntry._TIME + " INTEGER NOT NULL DEFAULT 0, "
				+ Contract.DiyEntry._TYPE + " INTEGER NOT NULL DEFAULT 0, "
				+ Contract.DiyEntry._ISRUN + " TEXT NOT NULL DEFAULT 'false', "
				+ Contract.DiyEntry._SID + " TEXT,"
				+ Contract.DiyEntry._EXTRA + " TEXT);";
		
		String SQL_CREATE_STATES_TABLE = "CREATE TABLE " + Contract.StateEntry.TABLE_NAME + " ("
				+ Contract.StateEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ Contract.StateEntry._URL + " TEXT NOT NULL, "
				+ Contract.StateEntry._SID + " INTEGER NOT NULL);";
		
		
		String SQL_CREATE_WEBSITES_TABLE = "CREATE TABLE " + Contract.WebsiteEntry.TABLE_NAME + " ("
				+ Contract.WebsiteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ Contract.WebsiteEntry._SITE + " TEXT NOT NULL, "
				+ Contract.WebsiteEntry._AD_HOST + " TEXT, "
				+ Contract.WebsiteEntry._APP + " BOOLEAN, "
				+ Contract.WebsiteEntry._JS + " BOOLEAN, "
				+ Contract.WebsiteEntry._NO_HISTORY + " BOOLEAN, "
				+ Contract.WebsiteEntry._NO_PIC + " BOOLEAN, "
				+ Contract.WebsiteEntry._UA + " INTEGER, "
				+ Contract.WebsiteEntry._STATE + " BOOLEAN );";
		
		
		// Execute the SQL statement
		db.execSQL(SQL_CREATE_MARKS_TABLE);
		db.execSQL(SQL_CREATE_PARENTS_TABLE);
		db.execSQL(SQL_CREATE_HISTORYS_TABLE);
		db.execSQL(SQL_CREATE_DIYS_TABLE);
		db.execSQL(SQL_CREATE_STATES_TABLE);
		db.execSQL(SQL_CREATE_WEBSITES_TABLE);
	}
	
	
	/**
	 * This is called when the database needs to be upgraded.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// The database is still at version 1, so there's nothing to do be done here.
		switch (oldVersion) {
			case 1:
				String SQL_CREATE_DIYS_TABLE = "CREATE TABLE " + Contract.DiyEntry.TABLE_NAME + " ("
						+ Contract.DiyEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
						+ Contract.DiyEntry._NAME + " TEXT NOT NULL, "
						+ Contract.DiyEntry._VALUE + " TEXT NOT NULL, "
						+ Contract.DiyEntry._DESCRIPTION + " TEXT NOT NULL, "
						+ Contract.DiyEntry._TIME + " INTEGER NOT NULL DEFAULT 0, "
						+ Contract.DiyEntry._TYPE + " INTEGER NOT NULL DEFAULT 0, "
						+ Contract.DiyEntry._ISRUN + " TEXT NOT NULL DEFAULT 'false', "
						+ Contract.DiyEntry._SID + "TEXT"
						+ Contract.DiyEntry._EXTRA + " TEXT);";
				db.execSQL(SQL_CREATE_DIYS_TABLE);
				break;
			case 2:
				db.execSQL("alter table " + Contract.DiyEntry.TABLE_NAME + " add column " + Contract.DiyEntry._DESCRIPTION + " integer");
				break;
			case 3:
				db.execSQL("alter table " + Contract.DiyEntry.TABLE_NAME + " add column " + Contract.DiyEntry._ISRUN + " integer");
				break;
			case 4:
				db.execSQL("alter table " + Contract.DiyEntry.TABLE_NAME + " add column " + Contract.DiyEntry._SID + " TEXT");
				break;
		}
		switch (oldVersion){
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
				String SQL_CREATE_STATES_TABLE = "CREATE TABLE " + Contract.StateEntry.TABLE_NAME + " ("
						+ Contract.StateEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
						+ Contract.StateEntry._URL + " TEXT NOT NULL, "
						+ Contract.StateEntry._SID + " INTEGER NOT NULL);";
				db.execSQL(SQL_CREATE_STATES_TABLE);
				break;
		}
		if (oldVersion <= 6) {
			db.execSQL("alter table " + Contract.MarkEntry.TABLE_NAME + " add column " + Contract.MarkEntry._DID + " integer");
			db.execSQL("alter table " + Contract.MarkEntry.TABLE_NAME + " add column " + Contract.MarkEntry._LEVEL + " integer");
			db.execSQL("alter table " + Contract.ParentEntry.TABLE_NAME + " add column " + Contract.ParentEntry._DID + " integer");
			db.execSQL("alter table " + Contract.ParentEntry.TABLE_NAME + " add column " + Contract.ParentEntry._LEVEL + " integer");
			db.execSQL("alter table " + Contract.ParentEntry.TABLE_NAME + " add column " + Contract.ParentEntry._TIME + " integer");
		}
		if (oldVersion <= 7) {
			db.execSQL("alter table " + Contract.ParentEntry.TABLE_NAME + " add column " + Contract.ParentEntry._ICON + " text");
		}
		if (oldVersion < 10) {
			db.execSQL("update " + Contract.MarkEntry.TABLE_NAME + " set " + Contract.MarkEntry._TIME + " = " + 10000);
			db.execSQL("update " + Contract.ParentEntry.TABLE_NAME + " set " + Contract.ParentEntry._TIME + " = " + 10000);
		}
		if (oldVersion < 11) {
			String SQL_CREATE_WEBSITES_TABLE = "CREATE TABLE " + Contract.WebsiteEntry.TABLE_NAME + " ("
					+ Contract.WebsiteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ Contract.WebsiteEntry._SITE + " TEXT NOT NULL, "
					+ Contract.WebsiteEntry._AD_HOST + " TEXT, "
					+ Contract.WebsiteEntry._APP + " INTEGER, "
					+ Contract.WebsiteEntry._JS + " INTEGER, "
					+ Contract.WebsiteEntry._NO_HISTORY + " INTEGER, "
					+ Contract.WebsiteEntry._NO_PIC + " INTEGER, "
					+ Contract.WebsiteEntry._UA + " INTEGER, "
					+ Contract.WebsiteEntry._STATE + " INTEGER );";
			db.execSQL(SQL_CREATE_WEBSITES_TABLE);
		}
	}
}

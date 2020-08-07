package com.zmide.lit.util;

/*
 * DBC 操作我们数据库的工具类  我们一般写成单例模式
 * 单例模式 ：  在整个应用程序中  不管什么地方（类）  获得的都是同一个对象实例
 * @author Administrator
 */

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zmide.lit.bookmark.Bookmark;
import com.zmide.lit.object.Contract;
import com.zmide.lit.object.Diy;
import com.zmide.lit.object.History;
import com.zmide.lit.object.MarkBean;
import com.zmide.lit.object.Parent;
import com.zmide.lit.object.ParentBean;
import com.zmide.lit.object.WebState;
import com.zmide.lit.object.WebsiteSetting;

import java.util.ArrayList;

public class DBC {
	
	//public static 为静态类型  要调用就要有一个静态的变量    为私有的
	private static DBC instance;
	//把数据库创建出来
	private DbHelper dbHelper;
	private SQLiteDatabase db;
	private int id;
	private boolean isExist;
	private String name;
	
	
	//单例模式
	//不能让每一个类都能new一个  那样就不是同一个对象了 所以首先构造函数要私有化    以上下文作为参数
	private DBC(Context ctx) {
		
		//由于数据库只需要调用一次，所以在单例中建出来
		dbHelper = new DbHelper(ctx);
	}
	
	//常用方法  增删改查
	
	//既然DBC类是私有的  那么别的类就不能够调用    那么就要提供一个public static（公共的  共享的）的方法
	//方法名为getInstance 参数为上下文    返回值类型为DBC
//要加上一个synchronized（同步的）
//如果同时有好多线程 同时去调用getInstance()方法  就可能会出现一些创建（new）多个DBC的现象  所以要加上synchronized
	public static synchronized DBC getInstance(Context ctx) {
		
		//就可以判断  如果为空 就创建一个， 如果不为空就还用原来的  这样整个应用程序中就只能获的一个实例
		if (instance == null) {
			instance = new DBC(ctx);
		}
		return instance;
	}
	
	public void addHistory(String title, String icon, String url) {
		// Gets the data repository in write mode
		// Create a new map of values, where column names are the keys
		db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Contract.HistoryEntry._NAME, title);
		values.put(Contract.HistoryEntry._ICON, icon);
		values.put(Contract.HistoryEntry._URL, url);
		values.put(Contract.HistoryEntry._TIME, System.currentTimeMillis() / 1000);
		// Insert the new row, returning the primary key value of the new row
		db.insert(Contract.HistoryEntry.TABLE_NAME, null, values);
	}
	
	public void addMark(String title, String icon, String url, int parent) {
		// Gets the data repository in write mode
		// Create a new map of values, where column names are the keys
		db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Contract.MarkEntry._PARENT, parent);
		values.put(Contract.MarkEntry._NAME, title);
		values.put(Contract.MarkEntry._ICON, icon);
		values.put(Contract.MarkEntry._URL, url);
		values.put(Contract.ParentEntry._LEVEL, 0);
		values.put(Contract.MarkEntry._DID, MSharedPreferenceUtils.getSharedPreference().getString("did", "1"));
		values.put(Contract.MarkEntry._TIME, System.currentTimeMillis() / 1000);
		// Insert the new row, returning the primary key value of the new row
		db.insert(Contract.MarkEntry.TABLE_NAME, null, values);
	}
	
	public void addMark(MarkBean mark) {
		// Gets the data repository in write mode
		// Create a new map of values, where column names are the keys
		db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Contract.MarkEntry._PARENT, mark.pid);
		values.put(Contract.MarkEntry._NAME, mark.title);
		values.put(Contract.MarkEntry._ICON, mark.icon);
		values.put(Contract.MarkEntry._URL, mark.url);
		values.put(Contract.MarkEntry._TIME, mark.mod_time);
		values.put(Contract.MarkEntry._DID, mark.did);
		values.put(Contract.MarkEntry._ID, mark.oid);
		values.put(Contract.MarkEntry._LEVEL, mark.level);
		// Insert the new row, returning the primary key value of the new row
		db.insert(Contract.MarkEntry.TABLE_NAME, null, values);
	}
	
	public void addParent(ParentBean mark) {
		// Gets the data repository in write mode
		// Create a new map of values, where column names are the keys
		db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Contract.ParentEntry._PARENT, mark.pid);
		values.put(Contract.ParentEntry._NAME, mark.title);
		values.put(Contract.ParentEntry._TIME, mark.mod_time);
		values.put(Contract.ParentEntry._DID, mark.did);
		values.put(Contract.ParentEntry._ID, mark.oid);
		values.put(Contract.ParentEntry._LEVEL, mark.level);
		// Insert the new row, returning the primary key value of the new row
		db.insert(Contract.ParentEntry.TABLE_NAME, null, values);
	}
	
	public boolean addParent(String title, String parent) {
		// Gets the data repository in write mode
		// Create a new map of values, where column names are the keys
		if (!isParentExist(title, parent)) {
			db = dbHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(Contract.ParentEntry._PARENT, parent);
			values.put(Contract.ParentEntry._NAME, title);
			values.put(Contract.ParentEntry._DID, MSharedPreferenceUtils.getSharedPreference().getString("did", "1"));
			values.put(Contract.ParentEntry._LEVEL, 0);
			values.put(Contract.ParentEntry._TIME, System.currentTimeMillis() / 1000);
			// Insert the new row, returning the primary key value of the new row
			db.insert(Contract.ParentEntry.TABLE_NAME, null, values);
			return true;
		}
		return false;
	}
	
	private boolean isParentExist(String title, String parent) {
		//创建游标对象
		db = dbHelper.getReadableDatabase();
		String[] projection = new String[]{
				Contract.ParentEntry._ID,
		};
		//Filter results WHERE "title" = 'My Title'
		String selection =
				Contract.ParentEntry._PARENT + " = ? AND " +
						Contract.ParentEntry._NAME + " = ?";
		String[] selectionArgs = {parent, title};
		
		// How you want the results sorted in the resulting Cursor
		Cursor cursor = db.query(Contract.ParentEntry.TABLE_NAME,
				projection,
				selection,
				selectionArgs,
				null,
				null,
				null
		);
		isExist = cursor.getCount() > 0;
		// 关闭游标，释放资源
		cursor.close();
		return isExist;
	}
	
	public boolean getMark(String url) {
		//创建游标对象
		db = dbHelper.getReadableDatabase();
		String[] projection = new String[]{
				Contract.MarkEntry._ID};
		// Filter results WHERE "title" = 'My Title'
		String selection = Contract.MarkEntry._URL + " = ?";
		String[] selectionArgs = {url + ""};
		
		// How you want the results sorted in the resulting Cursor
		@SuppressLint("Recycle") Cursor cursor = db.query(Contract.MarkEntry.TABLE_NAME,
				projection,
				selection,
				selectionArgs,
				null,
				null,
				null,
				"100");
		return cursor.getCount() > 0;
	}
	
	public void deleteMark(String url) {
		//创建游标对象
		db = dbHelper.getReadableDatabase();
		// Filter results WHERE "title" = 'My Title'
		String selection = Contract.MarkEntry._URL + " = ?";
		String[] selectionArgs = {url + ""};
		
		// How you want the results sorted in the resulting Cursor
		db.delete(Contract.MarkEntry.TABLE_NAME,
				selection,
				selectionArgs);
	}
	
	public void deleteAllMarks() {
		//创建游标对象
		db = dbHelper.getReadableDatabase();
		// Filter results WHERE "title" = 'My Title'
		
		// How you want the results sorted in the resulting Cursor
		db.delete(Contract.MarkEntry.TABLE_NAME,
				null,
				null);
	}
	
	public void deleteHistory(String id) {
		//创建游标对象
		db = dbHelper.getReadableDatabase();
		// Filter results WHERE "title" = 'My Title'
		String selection = Contract.HistoryEntry._ID + " = ?";
		String[] selectionArgs = {id + ""};
		
		// How you want the results sorted in the resulting Cursor
		db.delete(Contract.HistoryEntry.TABLE_NAME,
				selection,
				selectionArgs);
	}
	
	public ArrayList<MarkBean> getAllMarks() {
		//创建游标对象
		db = dbHelper.getReadableDatabase();
		String[] projection = new String[]{
				Contract.MarkEntry._PARENT,
				Contract.MarkEntry._ICON,
				Contract.MarkEntry._ID,
				Contract.MarkEntry._NAME,
				Contract.MarkEntry._TIME,
				Contract.MarkEntry._DID,
				Contract.MarkEntry._LEVEL,
				Contract.MarkEntry._URL};
		// How you want the results sorted in the resulting Cursor
		String sortOrder =
				Contract.MarkEntry._TIME + " DESC";
		Cursor cursor = db.query(Contract.MarkEntry.TABLE_NAME,
				projection,
				null,
				null,
				null,
				null,
				sortOrder);
		//利用游标遍历所有数据对象
		ArrayList<MarkBean> marks = new ArrayList<>();
		while (cursor.moveToNext()) {
			String name = cursor.getString(cursor.getColumnIndex(Contract.MarkEntry._NAME));
			long time = cursor.getLong(cursor.getColumnIndex(Contract.MarkEntry._TIME));
			String url = cursor.getString(cursor.getColumnIndex(Contract.MarkEntry._URL));
			int id = cursor.getInt(cursor.getColumnIndex(Contract.MarkEntry._ID));
			int did = cursor.getInt(cursor.getColumnIndex(Contract.MarkEntry._DID));
			int pid = cursor.getInt(cursor.getColumnIndex(Contract.MarkEntry._PARENT));
			int level = cursor.getInt(cursor.getColumnIndex(Contract.MarkEntry._LEVEL));
			String icon = cursor.getString(cursor.getColumnIndex(Contract.MarkEntry._ICON));
			MarkBean mark = new MarkBean();
			mark.oid = id;
			mark.did = did;
			mark.level = level;
			mark.pid = pid;
			mark.icon = icon;
			mark.title = name;
			mark.url = url;
			mark.mod_time = time;
			marks.add(mark);
		}
		// 关闭游标，释放资源
		cursor.close();
		return marks;
	}

	public ArrayList<MarkBean> getMarks(String parent) {
		//创建游标对象
		db = dbHelper.getReadableDatabase();
		String[] projection = new String[]{
				Contract.MarkEntry._PARENT,
				Contract.MarkEntry._ICON,
				Contract.MarkEntry._ID,
				Contract.MarkEntry._NAME,
				Contract.MarkEntry._TIME,
				Contract.MarkEntry._URL};
		// Filter results WHERE "title" = 'My Title'
		String selection = Contract.MarkEntry._PARENT + " = ?";
		String[] selectionArgs = {parent + ""};

		// How you want the results sorted in the resulting Cursor
		String sortOrder =
				Contract.MarkEntry._TIME + " DESC";
		Cursor cursor = db.query(Contract.MarkEntry.TABLE_NAME,
				projection,
				selection,
				selectionArgs,
				null,
				null,
				sortOrder,
				"100");
		//利用游标遍历所有数据对象
		ArrayList<MarkBean> marks = new ArrayList<>();
		while (cursor.moveToNext()) {
			String name = cursor.getString(cursor.getColumnIndex(Contract.MarkEntry._NAME));
			long time = cursor.getLong(cursor.getColumnIndex(Contract.MarkEntry._TIME));
			String url = cursor.getString(cursor.getColumnIndex(Contract.MarkEntry._URL));
			String id = cursor.getString(cursor.getColumnIndex(Contract.MarkEntry._ID));
			String icon = cursor.getString(cursor.getColumnIndex(Contract.MarkEntry._ICON));
			MarkBean mark = new MarkBean();
			mark.id = id;
			mark.icon = icon;
			mark.title = name;
			mark.url = url;
			mark.mod_time = time;
			marks.add(mark);
		}

		// 关闭游标，释放资源
		cursor.close();
		return marks;
	}
	
	public int getParent(String child) {
		//创建游标对象
		db = dbHelper.getReadableDatabase();
		String[] projection = new String[]{
				Contract.MarkEntry._PARENT,
				Contract.MarkEntry._ID};
		// Filter results WHERE "id" = '1'
		String selection = Contract.MarkEntry._ID + " = ?";
		String[] selectionArgs = {child + ""};
		
		// How you want the results sorted in the resulting Cursor
		String sortOrder =
				Contract.MarkEntry._TIME + " DESC";
		Cursor cursor = db.query(Contract.MarkEntry.TABLE_NAME,
				projection,
				selection,
				selectionArgs,
				null,
				null,
				sortOrder,
				"100");
		id = 0;
		if (cursor.moveToFirst()) {
			id = cursor.getInt(cursor.getColumnIndex(Contract.MarkEntry._PARENT));
		}
		// 关闭游标，释放资源
		cursor.close();
		return id;
	}
	
	public int getParentByFolder(String parentId) {
		//创建游标对象
		db = dbHelper.getReadableDatabase();
		String[] projection = new String[]{
				Contract.ParentEntry._PARENT,
				Contract.ParentEntry._ID};
		// Filter results WHERE "id" = '1'
		String selection = Contract.ParentEntry._ID + " = ?";
		String[] selectionArgs = {parentId};
		
		// How you want the results sorted in the resulting Cursor
		Cursor cursor = db.query(Contract.ParentEntry.TABLE_NAME,
				projection,
				selection,
				selectionArgs,
				null,
				null,
				null,
				"1");
		id = 0;
		if (cursor.moveToFirst()) {
			id = cursor.getInt(cursor.getColumnIndex(Contract.ParentEntry._PARENT));
		}
		// 关闭游标，释放资源
		cursor.close();
		return id;
	}
	
	public String getIndexName(String index) {
		//创建游标对象
		db = dbHelper.getReadableDatabase();
		String[] projection = new String[]{
				Contract.ParentEntry._NAME,
				Contract.ParentEntry._ID};
		// Filter results WHERE "id" = '1'
		String selection = Contract.ParentEntry._ID + " = ?";
		String[] selectionArgs = {index + ""};
		
		// How you want the results sorted in the resulting Cursor
		Cursor cursor = db.query(Contract.ParentEntry.TABLE_NAME,
				projection,
				selection,
				selectionArgs,
				null,
				null,
				null,
				"1");
		if (cursor.moveToFirst()) {
			name = cursor.getString(cursor.getColumnIndex(Contract.ParentEntry._NAME));
		}
		// 关闭游标，释放资源
		cursor.close();
		return name;
	}
	
	/**
	 * 获取指定文件夹下文件夹
	 *
	 * @param parentf 父文件夹ID
	 **/
	public ArrayList<Parent> getParents(String parentf) {
		//创建游标对象
		db = dbHelper.getReadableDatabase();
		String[] projection = new String[]{
				Contract.ParentEntry._PARENT,
				Contract.ParentEntry._ID,
				Contract.ParentEntry._NAME
		};
		// Filter results WHERE "title" = 'My Title'
		String selection = Contract.ParentEntry._PARENT + " = ?";
		String[] selectionArgs = {parentf + ""};
		
		// How you want the results sorted in the resulting Cursor
		String sortOrder =
				Contract.ParentEntry._NAME + "";
		Cursor cursor = db.query(Contract.ParentEntry.TABLE_NAME,
				projection,
				selection,
				selectionArgs,
				null,
				null,
				sortOrder);
		//利用游标遍历所有数据对象
		ArrayList<Parent> parents = new ArrayList<>();
		while (cursor.moveToNext()) {
			String name = cursor.getString(cursor.getColumnIndex(Contract.ParentEntry._NAME));
			String id = cursor.getString(cursor.getColumnIndex(Contract.ParentEntry._ID));
			Parent parent = new Parent();
			parent.id = id;
			parent.name = name;
			parents.add(parent);
		}
		// 关闭游标，释放资源
		cursor.close();
		return parents;
	}
	
	/**
	 * 获取指定文件夹下文件夹
	 **/
	public ArrayList<ParentBean> getAllParents() {
		//创建游标对象
		db = dbHelper.getReadableDatabase();
		String[] projection = new String[]{
				Contract.ParentEntry._PARENT,
				Contract.ParentEntry._ID,
				Contract.ParentEntry._NAME,
				Contract.ParentEntry._TIME,
				Contract.ParentEntry._DID,
				Contract.ParentEntry._LEVEL,
				Contract.ParentEntry._ICON,
			
		};
		// Filter results WHERE "title" = 'My Title'
		
		// How you want the results sorted in the resulting Cursor
		String sortOrder =
				Contract.ParentEntry._NAME + "";
		Cursor cursor = db.query(Contract.ParentEntry.TABLE_NAME,
				projection,
				null,
				null,
				null,
				null,
				sortOrder);
		//利用游标遍历所有数据对象
		ArrayList<ParentBean> parents = new ArrayList<>();
		while (cursor.moveToNext()) {
			String name = cursor.getString(cursor.getColumnIndex(Contract.ParentEntry._NAME));
			int time = cursor.getInt(cursor.getColumnIndex(Contract.ParentEntry._TIME));
			String icon = cursor.getString(cursor.getColumnIndex(Contract.ParentEntry._ICON));
			int id = cursor.getInt(cursor.getColumnIndex(Contract.ParentEntry._ID));
			int pid = cursor.getInt(cursor.getColumnIndex(Contract.ParentEntry._PARENT));
			int did = cursor.getInt(cursor.getColumnIndex(Contract.ParentEntry._DID));
			int lv = cursor.getInt(cursor.getColumnIndex(Contract.ParentEntry._LEVEL));
			ParentBean parent = new ParentBean();
			parent.oid = id;
			parent.pid = pid;
			parent.did = did;
			parent.mod_time = time;
			parent.level = lv;
			parent.icon = icon;
			parent.title = name;
			parents.add(parent);
		}
		// 关闭游标，释放资源
		cursor.close();
		return parents;
	}
	
	public ArrayList<History> getHistorys() {
		//创建游标对象
		db = dbHelper.getReadableDatabase();
		String[] projection = new String[]{
				Contract.HistoryEntry._ICON,
				Contract.HistoryEntry._ID,
				Contract.HistoryEntry._NAME,
				Contract.HistoryEntry._TIME,
				Contract.HistoryEntry._URL};
		// Filter results WHERE "title" = 'My Title'
		//String selection = Contract.MarkEntry._PARENT + " = ?";
		//String[] selectionArgs = { parent+"" };
		
		// How you want the results sorted in the resulting Cursor
		String sortOrder =
				Contract.MarkEntry._TIME + " DESC";
		Cursor cursor = db.query(Contract.HistoryEntry.TABLE_NAME,
				projection,
				null,
				null,
				null,
				null,
				sortOrder,
				"100");
		//利用游标遍历所有数据对象
		ArrayList<History> marks = new ArrayList<>();
		while (cursor.moveToNext()) {
			String name = cursor.getString(cursor.getColumnIndex(Contract.HistoryEntry._NAME));
			String time = cursor.getString(cursor.getColumnIndex(Contract.HistoryEntry._TIME));
			String url = cursor.getString(cursor.getColumnIndex(Contract.HistoryEntry._URL));
			String id = cursor.getString(cursor.getColumnIndex(Contract.HistoryEntry._ID));
			String icon = cursor.getString(cursor.getColumnIndex(Contract.HistoryEntry._ICON));
			History mark = new History();
			mark.id = id;
			mark.icon = icon;
			mark.name = name;
			mark.url = url;
			mark.time = time;
			marks.add(mark);
		}
		// 关闭游标，释放资源
		cursor.close();
		return marks;
	}
	
	public ArrayList<Diy> getDiys(int type, boolean OnlyRun) {
		//创建游标对象
		db = dbHelper.getReadableDatabase();
		String[] projection = new String[]{
				Contract.DiyEntry._TYPE,
				Contract.DiyEntry._ID,
				Contract.DiyEntry._NAME,
				Contract.DiyEntry._TIME,
				Contract.DiyEntry._VALUE,
				Contract.DiyEntry._EXTRA,
				Contract.DiyEntry._DESCRIPTION,
				Contract.DiyEntry._ISRUN};
		//Filter results WHERE "title" = 'My Title'
		String selection = Contract.DiyEntry._TYPE + " = ?";
		String[] selectionArgs = {type + ""};
		if (OnlyRun) {
			selection = Contract.DiyEntry._TYPE + " = ? AND " +
					Contract.DiyEntry._ISRUN + " = ?";
			selectionArgs = new String[]{type + "", "1"};
		}
		
		// How you want the results sorted in the resulting Cursor
		String sortOrder =
				Contract.DiyEntry._TIME + " DESC";
		Cursor cursor = db.query(Contract.DiyEntry.TABLE_NAME,
				projection,
				selection,
				selectionArgs,
				null,
				null,
				sortOrder,
				"100");
		//利用游标遍历所有数据对象
		ArrayList<Diy> marks = new ArrayList<>();
		while (cursor.moveToNext()) {
			String name = cursor.getString(cursor.getColumnIndex(Contract.DiyEntry._NAME));
			String time = cursor.getString(cursor.getColumnIndex(Contract.DiyEntry._TIME));
			String extra = cursor.getString(cursor.getColumnIndex(Contract.DiyEntry._EXTRA));
			int id = cursor.getInt(cursor.getColumnIndex(Contract.DiyEntry._ID));
			boolean isrun = (cursor.getString(cursor.getColumnIndex(Contract.DiyEntry._ISRUN)) + "").equals("1");
			String value = cursor.getString(cursor.getColumnIndex(Contract.DiyEntry._VALUE));
			String description = cursor.getString(cursor.getColumnIndex(Contract.DiyEntry._DESCRIPTION));
			Diy mark = new Diy();
			mark.id = id;
			mark.value = value;
			mark.title = name;
			mark.extra = extra;
			mark.time = time;
			mark.isrun = isrun;
			mark.description = description;
			marks.add(mark);
		}
		// 关闭游标，释放资源
		cursor.close();
		return marks;
	}
	
	public Diy getDiy(int type) {
		//创建游标对象
		db = dbHelper.getReadableDatabase();
		String[] projection = new String[]{
				Contract.DiyEntry._TYPE,
				Contract.DiyEntry._ID,
				Contract.DiyEntry._NAME,
				Contract.DiyEntry._TIME,
				Contract.DiyEntry._VALUE,
				Contract.DiyEntry._EXTRA,
				Contract.DiyEntry._DESCRIPTION,
				Contract.DiyEntry._ISRUN};
		//Filter results WHERE "title" = 'My Title'
		String selection = Contract.DiyEntry._TYPE + " = ? AND " +
				Contract.DiyEntry._ISRUN + " = ?";
		String[] selectionArgs = {type + "", "1"};
		
		// How you want the results sorted in the resulting Cursor
		Cursor cursor = db.query(Contract.DiyEntry.TABLE_NAME,
				projection,
				selection,
				selectionArgs,
				null,
				null,
				null
		);
		//利用游标遍历所有数据对象
		Diy mark = new Diy();
		while (cursor.moveToNext()) {
			String name = cursor.getString(cursor.getColumnIndex(Contract.DiyEntry._NAME));
			String time = cursor.getString(cursor.getColumnIndex(Contract.DiyEntry._TIME));
			String extra = cursor.getString(cursor.getColumnIndex(Contract.DiyEntry._EXTRA));
			int id = cursor.getInt(cursor.getColumnIndex(Contract.DiyEntry._ID));
			boolean isrun = (cursor.getString(cursor.getColumnIndex(Contract.DiyEntry._ISRUN)) + "").equals("1");
			String value = cursor.getString(cursor.getColumnIndex(Contract.DiyEntry._VALUE));
			String description = cursor.getString(cursor.getColumnIndex(Contract.DiyEntry._DESCRIPTION));
			mark.id = id;
			mark.value = value;
			mark.title = name;
			mark.extra = extra;
			mark.time = time;
			mark.isrun = isrun;
			mark.description = description;
		}
		// 关闭游标，释放资源
		cursor.close();
		return mark;
	}
	
	private boolean isDiyNotExist(String value, int type) {
		//创建游标对象
		db = dbHelper.getReadableDatabase();
		String[] projection = new String[]{
				Contract.DiyEntry._ID,
		};
		//Filter results WHERE "title" = 'My Title'
		String selection =
				Contract.DiyEntry._VALUE + " = ? AND " +
						Contract.DiyEntry._TYPE + " = ?";
		String[] selectionArgs = {value + "", type + ""};
		
		// How you want the results sorted in the resulting Cursor
		Cursor cursor = db.query(Contract.DiyEntry.TABLE_NAME,
				projection,
				selection,
				selectionArgs,
				null,
				null,
				null
		);
		isExist = cursor.getCount() > 0;
		// 关闭游标，释放资源
		cursor.close();
		return !isExist;
	}
	
	public boolean isDiyExist(String id) {
		//创建游标对象
		db = dbHelper.getReadableDatabase();
		String[] projection = new String[]{
				Contract.DiyEntry._ID,
		};
		//Filter results WHERE "title" = 'My Title'
		String selection =
				Contract.DiyEntry._SID + " = ? AND " +
						Contract.DiyEntry._TYPE + " = ?";
		String[] selectionArgs = {id + "", Diy.PLUGIN + ""};
		
		// How you want the results sorted in the resulting Cursor
		Cursor cursor = db.query(Contract.DiyEntry.TABLE_NAME,
				projection,
				selection,
				selectionArgs,
				null,
				null,
				null
		);
		isExist = cursor.getCount() > 0;
		// 关闭游标，释放资源
		cursor.close();
		return isExist;
	}
	
	public String getInstalled() {
		//创建游标对象
		db = dbHelper.getReadableDatabase();
		String[] projection = new String[]{
				Contract.DiyEntry._SID,
		};
		//Filter results WHERE "title" = 'My Title'
		String selection =
				Contract.DiyEntry._TYPE + " = ?";
		String[] selectionArgs = {Diy.PLUGIN + ""};
		
		// How you want the results sorted in the resulting Cursor
		Cursor cursor = db.query(Contract.DiyEntry.TABLE_NAME,
				projection,
				selection,
				selectionArgs,
				null,
				null,
				null
		);
		ArrayList<String> sids = new ArrayList<>();
		while (cursor.moveToNext()) {
			String sid = cursor.getString(cursor.getColumnIndex(Contract.DiyEntry._SID));
			if (sid != null && !sid.equals("null"))
				sids.add(sid);
		}
		// 关闭游标，释放资源
		cursor.close();
		return sids.toString();
	}
	
	public int getDiyCount() {
		//创建游标对象
		db = dbHelper.getReadableDatabase();
		String[] projection = new String[]{
				Contract.DiyEntry._TYPE,
				Contract.DiyEntry._ID,
		};
		//Filter results WHERE "title" = 'My Title'
		// String selection = Contract.DiyEntry._TYPE + " = ?";
		//String[] selectionArgs = { type+"" };
		
		// How you want the results sorted in the resulting Cursor
		String sortOrder =
				Contract.DiyEntry._TIME + " DESC";
		Cursor cursor = db.query(Contract.DiyEntry.TABLE_NAME,
				projection,
				null,
				null,
				null,
				null,
				sortOrder,
				null);
		//利用游标遍历所有数据对象
		int count = cursor.getCount();
		// 关闭游标，释放资源
		cursor.close();
		return count;
	}
	
	public void addDiy(String title, String description, String value, String extra, int type, boolean isrun) {
		// Gets the data repository in write mode
		// Create a new map of values, where column names are the keys
		if (isDiyNotExist(value, type)) {
			db = dbHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(Contract.DiyEntry._TYPE, type);
			values.put(Contract.DiyEntry._NAME, title);
			values.put(Contract.DiyEntry._EXTRA, extra);
			values.put(Contract.DiyEntry._VALUE, value);
			values.put(Contract.DiyEntry._ISRUN, isrun);
			values.put(Contract.DiyEntry._TIME, System.currentTimeMillis() /1000);
			values.put(Contract.DiyEntry._DESCRIPTION, description);
			// Insert the new row, returning the primary key value of the new row
			db.insert(Contract.DiyEntry.TABLE_NAME, null, values);
		}
	}
	
	public void addDiy(String id, String title, String description, String value, String extra, int type, boolean isrun) {
		// Gets the data repository in write mode
		// Create a new map of values, where column names are the keys
		if (isDiyNotExist(value, type)) {
			db = dbHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(Contract.DiyEntry._SID, id);
			values.put(Contract.DiyEntry._TYPE, type);
			values.put(Contract.DiyEntry._NAME, title);
			values.put(Contract.DiyEntry._EXTRA, extra);
			values.put(Contract.DiyEntry._VALUE, value);
			values.put(Contract.DiyEntry._ISRUN, isrun);
			values.put(Contract.DiyEntry._TIME, System.currentTimeMillis() /1000);
			values.put(Contract.DiyEntry._DESCRIPTION, description);
			// Insert the new row, returning the primary key value of the new row
			db.insert(Contract.DiyEntry.TABLE_NAME, null, values);
		}
	}
	
	public void deleteDiys(String id) {
		//创建游标对象
		db = dbHelper.getReadableDatabase();
		// Filter results WHERE "title" = 'My Title'
		String selection = Contract.DiyEntry._SID + " = ?";
		String[] selectionArgs = {id + ""};
		
		// How you want the results sorted in the resulting Cursor
		db.delete(Contract.DiyEntry.TABLE_NAME,
				selection,
				selectionArgs);
	}
	
	public void deleteDiys(int id) {
		//创建游标对象
		db = dbHelper.getReadableDatabase();
		// Filter results WHERE "title" = 'My Title'
		String selection = Contract.DiyEntry._ID + " = ?";
		String[] selectionArgs = {id + ""};
		
		// How you want the results sorted in the resulting Cursor
		db.delete(Contract.DiyEntry.TABLE_NAME,
				selection,
				selectionArgs);
	}
	
	public void modAllRun(boolean isrun, int type) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Contract.DiyEntry._ISRUN, isrun);//key为字段名，value为值
//把person表中personid等于1的记录的name字段的值改为“小小”。
		db.update(Contract.DiyEntry.TABLE_NAME, values, Contract.DiyEntry._TYPE + "=?", new String[]{type + ""});
		db.close();
		
	}
	
	public void modRun(boolean isrun, int id) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Contract.DiyEntry._ISRUN, isrun);//key为字段名，value为值
//把person表中personid等于1的记录的name字段的值改为“小小”。
		db.update(Contract.DiyEntry.TABLE_NAME, values, Contract.DiyEntry._ID + "=?", new String[]{id + ""});
		db.close();
		
	}
	
	public void modDiy(String title, String description, String value, String extra, String id) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Contract.DiyEntry._NAME, title);//key为字段名，value为值
		values.put(Contract.DiyEntry._DESCRIPTION, description);
		values.put(Contract.DiyEntry._VALUE, value);
		values.put(Contract.DiyEntry._EXTRA, extra);
//把person表中personid等于1的记录的name字段的值改为“小小”。
		db.update(Contract.DiyEntry.TABLE_NAME, values, Contract.DiyEntry._ID + "=?", new String[]{id + ""});
		db.close();
		
	}
	
	public void deleteParent(String id) {
		db = dbHelper.getReadableDatabase();
		// Filter results WHERE "title" = 'My Title'
		String selection = Contract.ParentEntry._ID + " = ?";
		String[] selectionArgs = {id + ""};
		
		// How you want the results sorted in the resulting Cursor
		db.delete(Contract.ParentEntry.TABLE_NAME,
				selection,
				selectionArgs);
	}
	
	public void deleteAllParents() {
		db = dbHelper.getReadableDatabase();
		// Filter results WHERE "title" = 'My Title'
		// How you want the results sorted in the resulting Cursor
		db.delete(Contract.ParentEntry.TABLE_NAME,
				null,
				null);
	}
	
	public void deleteMarkAndFolder(String folderId) {
		db = dbHelper.getReadableDatabase();
		// Filter results WHERE "title" = 'My Title'
		String selection = Contract.ParentEntry._PARENT + " = ?";
		String[] selectionArgs = {folderId};
		
		// How you want the results sorted in the resulting Cursor
		db.delete(Contract.ParentEntry.TABLE_NAME,
				selection,
				selectionArgs);
		String selection2 = Contract.MarkEntry._PARENT + " = ?";
		String[] selectionArgs2 = {folderId};
		
		// How you want the results sorted in the resulting Cursor
		db.delete(Contract.MarkEntry.TABLE_NAME,
				selection2,
				selectionArgs2);
	}
	
	public void modMark(String title0, String url0, String newTitle, String newUrl, String folderId) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Contract.MarkEntry._NAME, newTitle);//key为字段名，value为值
		values.put(Contract.MarkEntry._URL, newUrl);
		values.put(Contract.MarkEntry._PARENT, folderId);
		values.put(Contract.MarkEntry._TIME, System.currentTimeMillis() /1000);
		db.update(Contract.MarkEntry.TABLE_NAME, values, Contract.MarkEntry._NAME + "=? and " + Contract.MarkEntry._URL + "=?", new String[]{title0, url0});
		db.close();
	}
	
	public void modParent(String id, String name, String folderId) {
		if (isParentExist(name, folderId)) {
			return;
		}
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Contract.ParentEntry._NAME, name);//key为字段名，value为值
		values.put(Contract.ParentEntry._PARENT, folderId);
		values.put(Contract.ParentEntry._TIME, System.currentTimeMillis() /1000);
		db.update(Contract.ParentEntry.TABLE_NAME, values, Contract.ParentEntry._ID + "=?", new String[]{id});
		db.close();
	}
	
	public void addMarkWithParentName(Bookmark bookmark) {
		int parentId = 0;
		for (String tag : bookmark.tag) {//遍历父目录名称,不存在就创建，并获取ID
			addParent(tag, parentId + "");
			parentId = getParentByName(tag, parentId);
		}
		//将书签添加到最后的目录
		addMark(bookmark.name, "", bookmark.URL, parentId);
	}
	
	private int getParentByName(String title, int parent) {
		//创建游标对象
		db = dbHelper.getReadableDatabase();
		String[] projection = new String[]{
				Contract.ParentEntry._ID,
		};
		//Filter results WHERE "title" = 'My Title'
		String selection =
				Contract.ParentEntry._PARENT + " = ? AND " +
						Contract.ParentEntry._NAME + " = ?";
		String[] selectionArgs = {parent + "", title};
		
		// How you want the results sorted in the resulting Cursor
		Cursor cursor = db.query(Contract.ParentEntry.TABLE_NAME,
				projection,
				selection,
				selectionArgs,
				null,
				null,
				null
		);
		id = 0;
		if (cursor.moveToFirst()) {
			id = cursor.getInt(cursor.getColumnIndex(Contract.ParentEntry._ID));
		}
		// 关闭游标，释放资源
		cursor.close();
		return id;
	}
	
	public void deleteAllHistory() {
		db = dbHelper.getReadableDatabase();
		// Filter results WHERE "title" = 'My Title'
		//String selection = Contract.HistoryEntry._ID + " = ?";
		//String[] selectionArgs = {id + ""};
		
		// How you want the results sorted in the resulting Cursor
		db.delete(Contract.HistoryEntry.TABLE_NAME,
				null,
				null);
	}
	
	public void saveState(int sid, String url) {
		if (isStateExist(sid))
			modWebState(sid, url);
		else
			addWebState(sid, url);
	}
	
	private void addWebState(int sid, String url) {
		// Gets the data repository in write mode
		// Create a new map of values, where column names are the keys
		db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Contract.StateEntry._SID, sid);
		values.put(Contract.StateEntry._URL, url);
		// Insert the new row, returning the primary key value of the new row
		db.insert(Contract.StateEntry.TABLE_NAME, null, values);
	}
	
	private void modWebState(int sid, String url) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Contract.StateEntry._URL, url);//key为字段名，value为值
		values.put(Contract.StateEntry._SID, sid);
		db.update(Contract.StateEntry.TABLE_NAME, values, Contract.StateEntry._SID + "=?", new String[]{sid + ""});
		db.close();
	}
	
	private boolean isStateExist(int sid) {
		//创建游标对象
		db = dbHelper.getReadableDatabase();
		String[] projection = new String[]{
				Contract.StateEntry._SID,
		};
		//Filter results WHERE "title" = 'My Title'
		String selection =
				Contract.StateEntry._SID + " = ? ";
		String[] selectionArgs = {sid + ""};
		
		// How you want the results sorted in the resulting Cursor
		Cursor cursor = db.query(Contract.StateEntry.TABLE_NAME,
				projection,
				selection,
				selectionArgs,
				null,
				null,
				null
		);
		isExist = cursor.getCount() > 0;
		// 关闭游标，释放资源
		cursor.close();
		return isExist;
	}
	
	public boolean hasStates() {
		//创建游标对象
		db = dbHelper.getReadableDatabase();
		String[] projection = new String[]{
				Contract.StateEntry._SID,
		};
		//Filter results WHERE "title" = 'My Title'
		
		// How you want the results sorted in the resulting Cursor
		Cursor cursor = db.query(Contract.StateEntry.TABLE_NAME,
				projection,
				null,
				null,
				null,
				null,
				null
		);
		isExist = cursor.getCount() > 0;
		// 关闭游标，释放资源
		cursor.close();
		return isExist;
	}
	
	public String getState(int sid) {
		//创建游标对象
		db = dbHelper.getReadableDatabase();
		String[] projection = new String[]{
				Contract.StateEntry._SID,
				Contract.StateEntry._URL,
		};
		//Filter results WHERE "title" = 'My Title'
		String selection =
				Contract.StateEntry._SID + " = ? ";
		String[] selectionArgs = {sid + ""};
		
		// How you want the results sorted in the resulting Cursor
		Cursor cursor = db.query(Contract.StateEntry.TABLE_NAME,
				projection,
				selection,
				selectionArgs,
				null,
				null,
				null
		);
		String url = "";
		if (cursor.moveToFirst()) {
			url = cursor.getString(cursor.getColumnIndex(Contract.StateEntry._URL));
		}
		// 关闭游标，释放资源
		cursor.close();
		return url;
	}
	
	public ArrayList<WebState> getStates() {
		//创建游标对象
		db = dbHelper.getReadableDatabase();
		String[] projection = new String[]{
				Contract.StateEntry._SID,
				Contract.StateEntry._URL};
		// Filter results WHERE "title" = 'My Title'
		
		// How you want the results sorted in the resulting Cursor
		Cursor cursor = db.query(Contract.StateEntry.TABLE_NAME,
				projection,
				null,
				null,
				null,
				null,
				null,
				"100");
		//利用游标遍历所有数据对象
		ArrayList<WebState> states = new ArrayList<>();
		while (cursor.moveToNext()) {
			WebState state = new WebState();
			state.url = cursor.getString(cursor.getColumnIndex(Contract.StateEntry._URL));
			state.sid = cursor.getInt(cursor.getColumnIndex(Contract.StateEntry._SID));
			states.add(state);
		}
		// 关闭游标，释放资源
		cursor.close();
		return states;
	}
	
	public void deleteAllState() {
		db = dbHelper.getReadableDatabase();
		// Filter results WHERE "title" = 'My Title'
		//String selection = Contract.HistoryEntry._ID + " = ?";
		//String[] selectionArgs = {id + ""};
		
		// How you want the results sorted in the resulting Cursor
		db.delete(Contract.StateEntry.TABLE_NAME,
				null,
				null);
	}
	
	public void deleteState(int sid) {
		//创建游标对象
		db = dbHelper.getReadableDatabase();
		//Filter results WHERE "title" = 'My Title'
		String selection =
				Contract.StateEntry._SID + " = ? ";
		String[] selectionArgs = {sid + ""};
		
		// How you want the results sorted in the resulting Cursor
		db.delete(Contract.StateEntry.TABLE_NAME,
				selection,
				selectionArgs
		);
	}
	
	//以下为Website所需
	public WebsiteSetting getWebsiteSetting(String website) {
		//创建游标对象
		db = dbHelper.getReadableDatabase();
		String[] projection = new String[]{
				Contract.WebsiteEntry._ID,
				Contract.WebsiteEntry._SITE,
				Contract.WebsiteEntry._STATE,
				Contract.WebsiteEntry._UA,
				Contract.WebsiteEntry._NO_PIC,
				Contract.WebsiteEntry._NO_HISTORY,
				Contract.WebsiteEntry._JS,
				Contract.WebsiteEntry._APP,
				Contract.WebsiteEntry._AD_HOST,
		};
		//Filter results WHERE "title" = 'My Title'
		String selection =
				Contract.WebsiteEntry._SITE + " = ? ";
		String[] selectionArgs = {website + ""};
		
		// How you want the results sorted in the resulting Cursor
		Cursor cursor = db.query(Contract.WebsiteEntry.TABLE_NAME,
				projection,
				selection,
				selectionArgs,
				null,
				null,
				null
		);
		WebsiteSetting websiteSetting = new WebsiteSetting();
		if (cursor.moveToFirst()) {
			websiteSetting.id = cursor.getInt(cursor.getColumnIndex(Contract.WebsiteEntry._ID));
			websiteSetting.ad_host = cursor.getInt(cursor.getColumnIndex(Contract.WebsiteEntry._AD_HOST)) == 1;
			websiteSetting.app = cursor.getInt(cursor.getColumnIndex(Contract.WebsiteEntry._APP)) == 1;
			websiteSetting.js = cursor.getInt(cursor.getColumnIndex(Contract.WebsiteEntry._JS)) == 1;
			websiteSetting.no_history = cursor.getInt(cursor.getColumnIndex(Contract.WebsiteEntry._NO_HISTORY)) == 1;
			websiteSetting.no_picture = cursor.getInt(cursor.getColumnIndex(Contract.WebsiteEntry._NO_PIC)) == 1;
			websiteSetting.site = cursor.getString(cursor.getColumnIndex(Contract.WebsiteEntry._SITE));
			websiteSetting.state = cursor.getInt(cursor.getColumnIndex(Contract.WebsiteEntry._STATE)) == 1;
			websiteSetting.ua = cursor.getInt(cursor.getColumnIndex(Contract.WebsiteEntry._UA));
		}else
			websiteSetting = null;
		// 关闭游标，释放资源
		cursor.close();
		return websiteSetting;
	}
	
	public WebsiteSetting getWebsiteSetting(int id) {
		//创建游标对象
		db = dbHelper.getReadableDatabase();
		String[] projection = new String[]{
				Contract.WebsiteEntry._ID,
				Contract.WebsiteEntry._SITE,
				Contract.WebsiteEntry._STATE,
				Contract.WebsiteEntry._UA,
				Contract.WebsiteEntry._NO_PIC,
				Contract.WebsiteEntry._NO_HISTORY,
				Contract.WebsiteEntry._JS,
				Contract.WebsiteEntry._APP,
				Contract.WebsiteEntry._AD_HOST,
		};
		//Filter results WHERE "title" = 'My Title'
		String selection =
				Contract.WebsiteEntry._ID + " = ? ";
		String[] selectionArgs = {id + ""};
		
		// How you want the results sorted in the resulting Cursor
		Cursor cursor = db.query(Contract.WebsiteEntry.TABLE_NAME,
				projection,
				selection,
				selectionArgs,
				null,
				null,
				null
		);
		WebsiteSetting websiteSetting = new WebsiteSetting();
		if (cursor.moveToFirst()) {
			websiteSetting.id = cursor.getInt(cursor.getColumnIndex(Contract.WebsiteEntry._ID));
			websiteSetting.ad_host = cursor.getInt(cursor.getColumnIndex(Contract.WebsiteEntry._AD_HOST)) == 1;
			websiteSetting.app = cursor.getInt(cursor.getColumnIndex(Contract.WebsiteEntry._APP)) == 1;
			websiteSetting.js = cursor.getInt(cursor.getColumnIndex(Contract.WebsiteEntry._JS)) == 1;
			websiteSetting.no_history = cursor.getInt(cursor.getColumnIndex(Contract.WebsiteEntry._NO_HISTORY)) == 1;
			websiteSetting.no_picture = cursor.getInt(cursor.getColumnIndex(Contract.WebsiteEntry._NO_PIC)) == 1;
			websiteSetting.site = cursor.getString(cursor.getColumnIndex(Contract.WebsiteEntry._SITE));
			websiteSetting.state = cursor.getInt(cursor.getColumnIndex(Contract.WebsiteEntry._STATE)) == 1;
			websiteSetting.ua = cursor.getInt(cursor.getColumnIndex(Contract.WebsiteEntry._UA));
		}else
			websiteSetting = null;
		// 关闭游标，释放资源
		cursor.close();
		return websiteSetting;
	}
	
	public ArrayList<WebsiteSetting> getWebsiteSettings() {
		//创建游标对象
		db = dbHelper.getReadableDatabase();
		String[] projection = new String[]{
				Contract.WebsiteEntry._ID,
				Contract.WebsiteEntry._SITE,
				Contract.WebsiteEntry._STATE,
				Contract.WebsiteEntry._UA,
				Contract.WebsiteEntry._NO_PIC,
				Contract.WebsiteEntry._NO_HISTORY,
				Contract.WebsiteEntry._JS,
				Contract.WebsiteEntry._APP,
				Contract.WebsiteEntry._AD_HOST,
		};
		//Filter results WHERE "title" = 'My Title'
		
		// How you want the results sorted in the resulting Cursor
		Cursor cursor = db.query(Contract.WebsiteEntry.TABLE_NAME,
				projection,
				null,
				null,
				null,
				null,
				null
		);
		ArrayList<WebsiteSetting> websiteSettings = new ArrayList<>();
		while (cursor.moveToNext()) {
			WebsiteSetting websiteSetting = new WebsiteSetting();
			websiteSetting.id = cursor.getInt(cursor.getColumnIndex(Contract.WebsiteEntry._ID));
			websiteSetting.ad_host = cursor.getInt(cursor.getColumnIndex(Contract.WebsiteEntry._AD_HOST)) == 1;
			websiteSetting.app = cursor.getInt(cursor.getColumnIndex(Contract.WebsiteEntry._APP)) == 1;
			websiteSetting.js = cursor.getInt(cursor.getColumnIndex(Contract.WebsiteEntry._JS)) == 1;
			websiteSetting.no_history = cursor.getInt(cursor.getColumnIndex(Contract.WebsiteEntry._NO_HISTORY)) == 1;
			websiteSetting.no_picture = cursor.getInt(cursor.getColumnIndex(Contract.WebsiteEntry._NO_PIC)) == 1;
			websiteSetting.site = cursor.getString(cursor.getColumnIndex(Contract.WebsiteEntry._SITE));
			websiteSetting.state = cursor.getInt(cursor.getColumnIndex(Contract.WebsiteEntry._STATE)) == 1;
			websiteSetting.ua = cursor.getInt(cursor.getColumnIndex(Contract.WebsiteEntry._UA));
			websiteSettings.add(websiteSetting);
		}
		// 关闭游标，释放资源
		cursor.close();
		return websiteSettings;
	}
	
	public void deleteAllWebsiteSettings() {
		db = dbHelper.getReadableDatabase();
		// Filter results WHERE "title" = 'My Title'
		//String selection = Contract.HistoryEntry._ID + " = ?";
		//String[] selectionArgs = {id + ""};
		
		// How you want the results sorted in the resulting Cursor
		db.delete(Contract.WebsiteEntry.TABLE_NAME,
				null,
				null);
	}
	
	public void addWebsiteSetting(WebsiteSetting setting) {
		// Gets the data repository in write mode
		// Create a new map of values, where column names are the keys
		db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Contract.WebsiteEntry._AD_HOST, setting.ad_host);
		values.put(Contract.WebsiteEntry._APP, setting.app);
		values.put(Contract.WebsiteEntry._UA, setting.ua);
		values.put(Contract.WebsiteEntry._JS, setting.js);
		values.put(Contract.WebsiteEntry._NO_PIC, setting.no_picture);
		values.put(Contract.WebsiteEntry._NO_HISTORY, setting.no_history);
		values.put(Contract.WebsiteEntry._SITE, setting.site);
		values.put(Contract.WebsiteEntry._STATE, setting.state);
		// Insert the new row, returning the primary key value of the new row
		db.insert(Contract.WebsiteEntry.TABLE_NAME, null, values);
	}
	
	public void modWebSetting(WebsiteSetting setting) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Contract.WebsiteEntry._AD_HOST, setting.ad_host);
		values.put(Contract.WebsiteEntry._APP, setting.app);
		values.put(Contract.WebsiteEntry._UA, setting.ua);
		values.put(Contract.WebsiteEntry._JS, setting.js);
		values.put(Contract.WebsiteEntry._NO_PIC, setting.no_picture);
		values.put(Contract.WebsiteEntry._NO_HISTORY, setting.no_history);
		values.put(Contract.WebsiteEntry._SITE, setting.site);
		values.put(Contract.WebsiteEntry._STATE, setting.state);
		db.update(Contract.WebsiteEntry.TABLE_NAME, values, Contract.WebsiteEntry._ID + "=?", new String[]{setting.id + ""});
		db.close();
	}
	
	public void deleteWebsiteSetting(int id) {
		//创建游标对象
		db = dbHelper.getReadableDatabase();
		// Filter results WHERE "title" = 'My Title'
		String selection = Contract.WebsiteEntry._ID + " = ?";
		String[] selectionArgs = {id + ""};
		
		// How you want the results sorted in the resulting Cursor
		db.delete(Contract.WebsiteEntry.TABLE_NAME,
				selection,
				selectionArgs);
	}
}


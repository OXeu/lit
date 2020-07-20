package com.zmide.lit.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.zmide.lit.bookmark.Bookmark;
import com.zmide.lit.bookmark.BookmarkParser;
import com.zmide.lit.util.DBC;
import com.zmide.lit.util.MExceptionUtils;
import com.zmide.lit.util.MToastUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Copyright (C), 2019-2020, DreamStudio
 * Author: Xeu
 * Date: 2020/2/28 14:18
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class BookmarkImport extends Activity {
	private static final int REQUEST_CHOOSE_FILE = 123;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//选择文件【调用系统的文件管理】
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		//intent.setType(“image/*”);//选择图片
		//intent.setType(“audio/*”); //选择音频
		//intent.setType(“video/*”); //选择视频 （mp4 3gp 是android支持的视频格式）
		//intent.setType(“video/*;image/*”);//同时选择视频和图片
		intent.setType("text/html");//htm/html
		intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		startActivityForResult(intent, REQUEST_CHOOSE_FILE);
	}
	
	private String readTextFromUri(Uri uri) throws IOException {
		StringBuilder stringBuilder = new StringBuilder();
		try (InputStream inputStream =
				     getContentResolver().openInputStream(uri);
		     BufferedReader reader = new BufferedReader(
				     new InputStreamReader(Objects.requireNonNull(inputStream)))) {
			String line;
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
			}
		}
		return stringBuilder.toString();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {//选择文件返回
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_CHOOSE_FILE) {
				Uri uri = data.getData();
				if (uri != null) {
					try {
						String html = readTextFromUri(uri);
						if ("".equals(html))
							MToastUtils.makeText("解析错误或文件为空").show();
						else {
							BookmarkParser bookmarkParser = new BookmarkParser(html);
							bookmarkParser.parse();
							ArrayList<Bookmark> bookmarks = bookmarkParser.getResult();
							for (Bookmark bookmark : bookmarks) {
								DBC.getInstance(this).addMarkWithParentName(bookmark);
							}
							MToastUtils.makeText("导入成功", MToastUtils.LENGTH_SHORT).show();
							finish();
						}
					} catch (IOException e) {
						MToastUtils.makeText("导入失败IOException", MToastUtils.LENGTH_SHORT).show();
						MExceptionUtils.reportException(e);
					} catch (SecurityException e) {
						MToastUtils.makeText("不支持的路径，请使用其他方式").show();
						MExceptionUtils.reportException(e);
					} catch (Exception e) {
						MToastUtils.makeText("导入失败" + e.getCause() + "").show();
						MExceptionUtils.reportException(e);
					}
				}
			}
		}
		finish();
	}
}

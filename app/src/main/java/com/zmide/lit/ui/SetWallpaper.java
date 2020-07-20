package com.zmide.lit.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.zmide.lit.R;
import com.zmide.lit.base.BaseActivity;
import com.zmide.lit.util.MFileUtils;
import com.zmide.lit.util.MToastUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class SetWallpaper extends BaseActivity {
	
	private static final int PHOTO_ALBUM_REQUEST = 0x101;
	public String path;
	//private Uri uri;
	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		path = "wallpaper.png";// new File(getExternalCacheDir(), "wallpaper.png").getAbsolutePath();
		//uri = Uri.parse(new File(getFilesDir(),"wallpaper"+Bitmap.CompressFormat.PNG.toString()).getAbsolutePath());
		sp = getSharedPreferences("setting", Context.MODE_PRIVATE);
		getWallpapers();
	}
	
	private void getWallpapers() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		// 构建一个内容选择的Intent
		// 设置选择类型为图片类型
		intent.setType("image/*");
		intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
		// 打开图片选择
		startActivityForResult(intent, PHOTO_ALBUM_REQUEST);
		
	}
	
	/**
	 * 将content类型的Uri转化为文件类型的Uri
	 *
	 * @param uri uri
	 * @return bitmap
	 */
	private Bitmap convertUri(Uri uri) {
		InputStream is;
		try {
			is = getContentResolver().openInputStream(uri);
			Bitmap bm = BitmapFactory.decodeStream(is);
			// 关闭流
			Objects.requireNonNull(is).close();
			return bm;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 通过Uri传递图像信息以供裁剪
	 */
//    private void startImageZoom(Uri uri) {
//        // 构建隐式Intent来启动裁剪程序
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        // 设置数据uri和类型为图片类型
//        intent.setDataAndType(uri, "image/*");
//        // 显示View为可裁剪的
//        intent.putExtra("crop", true);
//        // 裁剪的宽高的比例为1:1
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        // 输出图片的宽高均为150
//        intent.putExtra("outputX", 150);
//        intent.putExtra("outputY", 150);
//        // 裁剪之后的数据是通过Intent返回
//        intent.putExtra("return-data", true);
//        startActivityForResult(intent, CROP_CODE);
//    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == PHOTO_ALBUM_REQUEST && resultCode == RESULT_OK) {
			try {
				Uri uri;
				// 获取到用户所选图片的Uri
				uri = data.getData();
				Bitmap bm = convertUri(uri);
				if (uri == null || bm == null) {
					MToastUtils.makeText("设置失败，获取图片失败").show();
				}else {
					// 返回的Uri为content类型的Uri,不能进行复制等操作,需要转换为文件Uri
					MFileUtils.saveFileOnFile(bm, path);
					sp.edit().putBoolean("wall", !sp.getBoolean("wall", true)).apply();
					//startImageZoom(uri);
				}
			}
			catch (SecurityException e){
				MToastUtils.makeText("不支持的路径，请使用其他方式").show();
			}
		}
		/*
		if(requestCode == CROP_CODE&&data!=null){
			Bundle extras = data.getExtras();
			if (extras != null) {
				// 获取到裁剪后的图像
				Bitmap bm = extras.getParcelable("data");
			String path =	FileTool.saveFileOnFile(this,bm,FileReader.getBitmapMd5(bm));
			sp.edit().putString("wallpaper",path).commit();
			}
		}
		*/
		finish();
		overridePendingTransition(0, R.anim.base_slide_right_out);
	}
	
	
}

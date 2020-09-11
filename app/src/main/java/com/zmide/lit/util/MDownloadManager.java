package com.zmide.lit.util;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.webkit.URLUtil;

import com.blankj.utilcode.util.ConvertUtils;

import java.util.Objects;
import android.content.BroadcastReceiver;
import android.content.Intent;
import java.io.File;
import android.database.Cursor;

public class MDownloadManager {
	public static void downloadBySystem(Context context, String url, String fileName) {
		// 指定下载地址
		DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
		// 设置通知的显示类型，下载进行时和完成后显示通知
		request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		// 设置通知栏的标题，如果不设置，默认使用文件名
//        request.setTitle("This is title");
		// 设置通知栏的描述
//        request.setDescription("This is description");
		// 允许在计费流量下下载
		request.setAllowedOverMetered(true);
		// 允许漫游时下载
		request.setAllowedOverRoaming(true);
		// 允许下载的网路类型
		request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
		// 设置下载文件保存的路径和
//		String uris =MSharedPreferenceUtils.getSharedPreference().getString("download_uri",null);
//		Uri uri = null;
//		if(uris!=null)
//			uri = Uri.parse(uris+"/"+fileName);
		/*if(uri!=null)
		request.setDestinationUri(uri);
		else*/
		request.setDestinationInExternalFilesDir(context, "", fileName);
		
//        另外可选一下方法，自定义下载路径
//        request.setDestinationUri()
//        request.setDestinationInExternalFilesDir()
		final DownloadManager downloadManager = (DownloadManager) context.getSystemService(Service.DOWNLOAD_SERVICE);
		// 添加一个下载任务
		Objects.requireNonNull(downloadManager).enqueue(request);
	}
	
	
	public static void downloadFile(Activity activity, final String url, final String fileName,long length) {
		SharedPreferences mSharedPreferences = activity.getSharedPreferences("setting", Context.MODE_PRIVATE);
		MDialogUtils.Builder dialog = new MDialogUtils.Builder(activity);
		String size = "未知大小";
		if (length>0)
			size = ConvertUtils.byte2FitMemorySize(length);
		dialog.setDownloadLink(url);
		dialog.setTitle("下载"+ "("+size+")")
				.setMessage("是否下载" + fileName)
				.setNegativeButton("不允许", (di, p2) -> di.cancel())
				.setPositiveButton("允许", (di, p2) -> {
					String packageName = mSharedPreferences.getString("downloader", "");
					if (packageName.equals("")) {
						downloadBySystems(activity, url, fileName);
					} else {
						try {
							DownloadHelper.downloader(activity, url, packageName);
						} catch (Exception e) {
							MToastUtils.makeText("没有找到合适的下载器").show();
						}
					}
					
					di.cancel();
				}).create().show();
	}
	
	
	private static void downloadBySystems(Activity activity, final String url, final String fileName) {
		MDownloadManager.downloadBySystem(activity, url, fileName);
		MToastUtils.makeText("开始下载").show();
		/*new PermissionHelper(activity).requestPermission(allGranted -> {
			if (allGranted) {
			
			} else {
				MToastUtils.makeText("无存储权限，无法下载").show();
			}
		}, Permission.PERMISSIONS);
		*/
	}
	
	public static void downloadFile(Activity activity, String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
		downloadFile(activity, url, URLUtil.guessFileName(url, contentDisposition, mimetype),contentLength);
	}
	
	/**
	 * Created by YuShuangPing on 2018/9/4.
	 */
	public class DownLoadCompleteReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())){
				//在广播中取出下载任务的id
				long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
				DownloadManager.Query query=new DownloadManager.Query();
				DownloadManager dm= (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
				query.setFilterById(id);
				Uri fileUriOrigin = dm.getUriForDownloadedFile(id);
				Cursor c = dm.query(query);
				if (c!=null){
					try {
						if (c.moveToFirst()){
							//获取文件下载路径
							String fileName = c.getString(c.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_FILENAME));
							String uris =MSharedPreferenceUtils.getSharedPreference().getString("download_uri",null);
							Uri uri = null;
							if(uris!=null){
								uri = Uri.parse(uris+"/"+fileName);
							int status = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));
							if (status==DownloadManager.STATUS_SUCCESSFUL){
								//移动文件
								MFileUtils.moveFile(fileUriOrigin,uri);
							}
							}
						}
					}catch (Exception e){
						e.printStackTrace();
						return;
					}finally {
						c.close();
					}

				}
			}
		}
	}
	
}

package com.zmide.lit.receiver;

/**
 * Created by YuShuangPing on 2018/9/4.
 */
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import com.zmide.lit.util.MFileUtils;
import com.zmide.lit.util.MSharedPreferenceUtils;
import com.zmide.lit.util.MToastUtils;

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
								MToastUtils.makeText("下载成功");
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


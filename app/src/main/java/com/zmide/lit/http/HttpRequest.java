package com.zmide.lit.http;

import android.app.Activity;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zmide.lit.interfaces.UpdateInterface;
import com.zmide.lit.object.DataUpdate;
import com.zmide.lit.object.UpdateData;
import com.zmide.lit.util.MDialogUtils;
import com.zmide.lit.util.MDownloadManager;

public class HttpRequest {
	public static void getUpdate(Activity activity, UpdateInterface listener) {
		OkHttpUtils.get().url(HttpHelper.UPDATE)
				.addParams("version", AppUtils.getAppVersionCode() + "")
				.build()
				.execute(new StringCallback() {
					@Override
					public void onError(Request request, Exception e) {
						listener.onError(e);
					}
					
					@Override
					public void onResponse(String response) {
						try {
							DataUpdate data = GsonUtils.fromJson(response, DataUpdate.class);
							if (data.code == 12) {
								final UpdateData update = data.data;
								//Have Update
								new MDialogUtils.Builder(activity)
										.setTitle("Ver" + AppUtils.getAppVersionName() + "->Ver" + update.version)
										.setMessage(update.update_log)
										.setNegativeButton("忽略", (di, p2) -> di.cancel())
										.setPositiveButton("更新", (di, p2) -> {
											MDownloadManager.downloadFile(activity, update.url, "LitBrowser_" + update.version + ".apk");
											di.cancel();
										})
										
										.create().show();
							} else if (data.code == 11) {
								//Newest
								listener.onNewest();
							}
						} catch (Exception e) {
							listener.onError(e);
						}
					}
				});
	}
}

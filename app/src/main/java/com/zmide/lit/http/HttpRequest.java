package com.zmide.lit.http;

import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zmide.lit.R;
import com.zmide.lit.databinding.LoginDB;
import com.zmide.lit.interfaces.UpdateInterface;
import com.zmide.lit.main.WebContainer;
import com.zmide.lit.object.MarkBean;
import com.zmide.lit.object.ParentBean;
import com.zmide.lit.object.json.DataStd;
import com.zmide.lit.object.json.MarksData;
import com.zmide.lit.object.json.NewsData;
import com.zmide.lit.skin.SkinManager;
import com.zmide.lit.ui.LoginActivity;
import com.zmide.lit.util.DBC;
import com.zmide.lit.util.MDialogUtils;
import com.zmide.lit.util.MDownloadManager;
import com.zmide.lit.util.MExceptionUtils;
import com.zmide.lit.util.MSharedPreferenceUtils;
import com.zmide.lit.util.MToastUtils;

import java.util.HashMap;
import java.util.Objects;

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
							DataStd data = GsonUtils.fromJson(response, DataStd.class);
							if (data.code == 12) {
								final HashMap<String, Object> update = data.data;
								//Have Update
								new MDialogUtils.Builder(activity)
										.setTitle("Ver" + AppUtils.getAppVersionName() + "->Ver" + update.get("version"))
										.setMessage(update.get("update_log") + "")
										.setNegativeButton("忽略", (di, p2) -> di.cancel())
										.setPositiveButton("更新", (di, p2) -> {
											MDownloadManager.downloadFile(activity, update.get("url") + "", "LitBrowser_" + update.get("version") + ".apk",0);
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
	
	public static void getNews(Activity activity) {
		OkHttpUtils.get().url(HttpHelper.NEWS)
				.addParams("version", AppUtils.getAppVersionCode() + "")
				.addParams("time", MSharedPreferenceUtils.getSharedPreference().getString("news_time", "0"))
				.build()
				.execute(new StringCallback() {
					@Override
					public void onError(Request request, Exception e) {
						MExceptionUtils.reportException(e);
					}
					
					@Override
					public void onResponse(String response) {
						try {
							NewsData data = GsonUtils.fromJson(response, NewsData.class);
							if (data != null) {
								if (data.code == 12) {
									//Have News
									MDialogUtils.Builder builder = new MDialogUtils.Builder(activity)
											.setTitle(data.title)
											.setMessage(data.message)
											.setPositiveButton("知道了", (di, p2) -> {
												di.cancel();
											});
									if (!Objects.equals(data.url, ""))
										builder.setNegativeButton("详情", (di, p2) -> {
											WebContainer.loadUrl(data.url);
											di.cancel();
										});
									builder.create().show();
									MSharedPreferenceUtils.getSharedPreference().edit().putString("news_time", data.time).apply();
								}
							}
						} catch (Exception e) {
							MExceptionUtils.reportException(e);
						}
					}
				});
	}
	
	public static void MarkSync(Activity activity) {
		String token = MSharedPreferenceUtils.getSharedPreference().getString("token", "");
		if ("".equals(token)) {
			MToastUtils.makeText("请先登录").show();
			activity.startActivity(new Intent(activity, LoginActivity.class));
		} else {
			String marks = GsonUtils.toJson(DBC.getInstance(activity).getAllMarks());
			String parents = GsonUtils.toJson(DBC.getInstance(activity).getAllParents());
			OkHttpUtils.post().url(HttpHelper.MARK)
					.addParams("version", AppUtils.getAppVersionCode() + "")
					.addParams("last_time", MSharedPreferenceUtils.getSharedPreference().getString("last_time", "0"))
					.addParams("marks", marks)
					.addParams("parents", parents)
					.addParams("token", token)
					.build()
					.execute(new StringCallback() {
						@Override
						public void onError(Request request, Exception e) {
							MToastUtils.makeText("同步失败" + e.getCause()).show();
							MExceptionUtils.reportException(e);
						}
						
						@Override
						public void onResponse(String response) {
							try {
								MarksData data = GsonUtils.fromJson(response, MarksData.class);
								if (data != null) {
									if (data.code == 0) {
										//Have News
										DBC.getInstance(activity).deleteAllMarks();
										DBC.getInstance(activity).deleteAllParents();
										for (MarkBean mark : data.data.marks) {
											DBC.getInstance(activity).addMark(mark);
										}
										for (ParentBean parentBean : data.data.parents) {
											DBC.getInstance(activity).addParent(parentBean);
										}
										MToastUtils.makeText("同步成功！").show();
										MSharedPreferenceUtils.getSharedPreference().edit().putString("last_time", System.currentTimeMillis() / 1000 + "").apply();
									} else {
										MToastUtils.makeText(data.msg + "").show();
									}
								}
							} catch (Exception e) {
								MExceptionUtils.reportException(e);
							}
						}
					});
		}
	}
	
	
	public static void Login(Activity activity, String emailEditor, String pwEditor) {
		OkHttpUtils.post().url(HttpHelper.LOGIN)
				.addParams("version", AppUtils.getAppVersionCode() + "")
				.addParams("did", MSharedPreferenceUtils.getSharedPreference().getString("last_time", "0"))
				.addParams("user", emailEditor)
				.addParams("pw", pwEditor)
				.build()
				.execute(new StringCallback() {
					@Override
					public void onError(Request request, Exception e) {
						MToastUtils.makeText("登录失败" + e.getMessage()).show();
						MExceptionUtils.reportException(e);
					}
					
					@Override
					public void onResponse(String response) {
						try {
							DataStd data = GsonUtils.fromJson(response, DataStd.class);
							if (data != null) {
								if (data.code == 0) {
									//Have News
									MSharedPreferenceUtils.getSharedPreference().edit().putString("token", data.data.get("token") + "").apply();
									MSharedPreferenceUtils.getSharedPreference().edit().putString("did", data.data.get("did") + "").apply();
									MToastUtils.makeText("登录成功！").show();
									activity.finish();
								} else {
									MToastUtils.makeText(data.msg).show();
								}
							}
						} catch (Exception e) {
							MToastUtils.makeText("登录失败" + e.getMessage()).show();
							MExceptionUtils.reportException(e);
						}
					}
				});
	}
	
	public static void Register(LoginDB db, String nickname, String email, String verify, String pw) {
		OkHttpUtils.post().url(HttpHelper.REG)
				.addParams("version", AppUtils.getAppVersionCode() + "")
				.addParams("nickname", nickname)
				.addParams("email", email)
				.addParams("ecode", verify)
				.addParams("pw", pw)
				.build()
				.execute(new StringCallback() {
					@Override
					public void onError(Request request, Exception e) {
						MToastUtils.makeText("注册失败" + e.getMessage()).show();
						MExceptionUtils.reportException(e);
					}
					
					@Override
					public void onResponse(String response) {
						try {
							DataStd data = GsonUtils.fromJson(response, DataStd.class);
							if (data != null) {
								if (data.code == 0) {
									//Have News
									MToastUtils.makeText("注册成功！").show();
									db.lor.performClick();
								} else {
									MToastUtils.makeText(data.msg).show();
								}
							}
						} catch (Exception e) {
							MToastUtils.makeText("注册失败" + e.getMessage()).show();
							MExceptionUtils.reportException(e);
						}
					}
				});
	}
	
	public static void Forget(LoginDB db, String email, String verify, String pw) {
		OkHttpUtils.post().url(HttpHelper.FORGET)
				.addParams("version", AppUtils.getAppVersionCode() + "")
				.addParams("email", email)
				.addParams("ecode", verify)
				.addParams("pw", pw)
				.build()
				.execute(new StringCallback() {
					@Override
					public void onError(Request request, Exception e) {
						MToastUtils.makeText("重置失败" + e.getMessage()).show();
						MExceptionUtils.reportException(e);
					}
					
					@Override
					public void onResponse(String response) {
						try {
							DataStd data = GsonUtils.fromJson(response, DataStd.class);
							if (data != null) {
								if (data.code == 0) {
									//Have News
									MToastUtils.makeText("重置成功！").show();
									db.forget.performClick();
								} else {
									MToastUtils.makeText(data.msg).show();
								}
							}
						} catch (Exception e) {
							MToastUtils.makeText("重置失败" + e.getMessage()).show();
							MExceptionUtils.reportException(e);
						}
					}
				});
	}
	
	public static void sendCode(LoginDB db, String email, boolean forget) {
		OkHttpUtils.post().url(HttpHelper.SEND_MAIL)
				.addParams("version", AppUtils.getAppVersionCode() + "")
				.addParams("email", email)
				.addParams("forget", forget + "")
				.build()
				.execute(new StringCallback() {
					@Override
					public void onError(Request request, Exception e) {
						MToastUtils.makeText("发送失败" + e.getMessage()).show();
						MExceptionUtils.reportException(e);
					}
					
					@Override
					public void onResponse(String response) {
						try {
							DataStd data = GsonUtils.fromJson(response, DataStd.class);
							if (data != null) {
								if (data.code == 0) {
									//Have News
									MToastUtils.makeText("发送成功！").show();
									CountDownTimer timer = new CountDownTimer(1000 * 60, 1000) {
										@Override
										public void onTick(long millisUntilFinished) {
											db.verifyEditor.setBtDrawable(SkinManager.getInstance().getDrawable(R.drawable.gray_15));
											db.verifyEditor.setBtText(millisUntilFinished / 1000 + "秒后可重新获取");
										}
										
										@Override
										public void onFinish() {
											db.verifyEditor.setBtDrawable(SkinManager.getInstance().getDrawable(R.drawable.ripple_blue_15));
											db.verifyEditor.setBtText("获取验证码");
											
										}
									}.start();
								} else {
									MToastUtils.makeText(data.msg).show();
								}
							}
						} catch (Exception e) {
							MToastUtils.makeText("发送失败" + e.getMessage()).show();
							MExceptionUtils.reportException(e);
						}
					}
				});
	}
}

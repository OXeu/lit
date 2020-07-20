package com.zmide.lit.http;

import android.app.Activity;
import android.content.Intent;

import com.zmide.lit.ui.LoginActivity;
import com.zmide.lit.util.MToastUtils;

public class UserUtils {
	public static void checkCode(Activity activity, int code, String msg) {
		String realMsg = "";
		switch (code) {
			case 1:
				realMsg = "参数错误";
				break;
			case 3:
				realMsg = "用户不存在";
				break;
			case 4:
				realMsg = "请先登录";
				activity.startActivity(new Intent(activity, LoginActivity.class));
				break;
			case 5:
				realMsg = "身份认证已过期，请重新登录";
				activity.startActivity(new Intent(activity, LoginActivity.class));
				break;
			case 6:
				realMsg = "账号密码已更改，请重新登录";
				activity.startActivity(new Intent(activity, LoginActivity.class));
				break;
			case 7:
			case 8:
			case 11:
				realMsg = "已是最新版本";
				break;
			case 9:
				realMsg = "删除失败";
				break;
			case 10:
				realMsg = "创建失败";
				break;
			case 12:
				realMsg = "有新版本可更新";
				break;
			case 13:
				realMsg = "没有数据";
				break;
			case 14:
				realMsg = "验证码错误或已过期";
				break;
			case 15:
				realMsg = "用户名或密码错误";
				break;
			case 101:
				realMsg = "服务器错误";
				break;
			default:
				realMsg = msg;
				break;
		}
		if ("".equals(realMsg))
			MToastUtils.makeText(msg).show();
		else
			MToastUtils.makeText(realMsg).show();
	}
}

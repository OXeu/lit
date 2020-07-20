package com.zmide.lit.ui;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.blankj.utilcode.util.AppUtils;
import com.zmide.lit.R;
import com.zmide.lit.base.BaseActivity;
import com.zmide.lit.base.BaseEvenListener;
import com.zmide.lit.bean.AboutBean;
import com.zmide.lit.databinding.AboutDB;
import com.zmide.lit.http.HttpRequest;
import com.zmide.lit.interfaces.UpdateInterface;
import com.zmide.lit.util.MDialogUtils;
import com.zmide.lit.util.MToastUtils;

public class AboutActivity extends BaseActivity {
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AboutDB db = DataBindingUtil.setContentView(this, R.layout.activity_about);
		db.setBean(new AboutBean(AppUtils.getAppVersionName()));
		db.setEvent(new EventListener());
		db.icon.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher));
	}
	
	public class EventListener extends BaseEvenListener {
		public void UpdateClick(View v) {
			HttpRequest.getUpdate(AboutActivity.this, new UpdateInterface() {
				@Override
				public void onError(Exception e) {
					new MDialogUtils.Builder(AboutActivity.this)
							.setTitle("错误")
							.setMessage(e.getMessage())
							.setPositiveButton("知道了", (di, p2) -> di.cancel())
							.create().show();
				}
				
				@Override
				public void onNewest() {
					toast("已是最新版本");
				}
			});
		}
		
		public void joinQQGroup(View view) {
			Intent intent = new Intent();
			intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + "UNlTud9KrFtaP2uoXv1W3ZF0fHtTX94C"));
			// 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
			try {
				startActivity(intent);
			} catch (Exception e) {
				// 未安装手Q或安装的版本不支持
				MToastUtils.makeText("未安装QQ或安装的版本不支持").show();
			}
		}
		
		public void Green(View view) {
			Intent i = new Intent();
			i.putExtra("url", "https://green-android.org/")
					.setData(null)
					.setPackage(getPackageName())
					.putExtra("ifNew", true)
					.setAction(Intent.ACTION_VIEW);
			view.getContext().startActivity(i);
		}
	}
	
}

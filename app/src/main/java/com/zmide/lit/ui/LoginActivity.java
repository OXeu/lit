package com.zmide.lit.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.zmide.lit.R;
import com.zmide.lit.base.BaseActivity;
import com.zmide.lit.base.BaseEvenListener;
import com.zmide.lit.databinding.LoginDB;
import com.zmide.lit.http.HttpRequest;

import java.util.Objects;

public class LoginActivity extends BaseActivity {
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_login);
		LoginDB db = DataBindingUtil.setContentView(this, R.layout.activity_login);
		db.setBean(new Bean());
		db.setEvent(new EventListener(db));
		db.verifyEditor.setBtOnClick(v -> {
			if (!db.verifyEditor.getBtText().contains("重新"))
				HttpRequest.sendCode(db, db.userEditor.getText() + "", db.nicknameEditor.getVisibility() == View.GONE);
		});
	}
	
	public static class Bean {
	
	}
	
	public class EventListener extends BaseEvenListener {
		public LoginDB db;
		
		public EventListener(LoginDB db) {
			this.db = db;
		}
		
		public void RunGo(View v) {
			if (db.nicknameEditor.getVisibility() == View.VISIBLE) {
				//Register
				HttpRequest.Register(db, db.nicknameEditor.getText() + "", db.userEditor.getText() + "", db.verifyEditor.getText() + "", db.pwEditor.getText() + "");
			} else if (db.verifyEditor.getVisibility() == View.VISIBLE) {
				//Forget
				HttpRequest.Forget(db, db.userEditor.getText() + "", db.verifyEditor.getText() + "", db.pwEditor.getText() + "");
			} else {
				//Login
				HttpRequest.Login(LoginActivity.this, db.userEditor.getText() + "", db.pwEditor.getText() + "");
			}
		}
		
		public void change(View v) {
			TextView view = (TextView) v;
			if (Objects.equals("注册", view.getText() + "")) {
				//登录状态
				db.nicknameEditor.setVisibility(View.VISIBLE);
				db.verifyEditor.setVisibility(View.VISIBLE);
				view.setText("返回登录");
			} else {
				db.nicknameEditor.setVisibility(View.GONE);
				db.verifyEditor.setVisibility(View.GONE);
				view.setText("注册");
			}
		}
		
		public void forget(View v) {
			TextView view = (TextView) v;
			if (Objects.equals("忘记密码", view.getText() + "")) {
				//登录状态
				db.nicknameEditor.setVisibility(View.GONE);
				db.verifyEditor.setVisibility(View.VISIBLE);
				db.lor.setVisibility(View.GONE);
				db.lor.setText("注册");
				db.pwEditor.setHint("新密码");
				view.setText("返回登录");
			} else {
				db.nicknameEditor.setVisibility(View.GONE);
				db.verifyEditor.setVisibility(View.GONE);
				db.lor.setVisibility(View.VISIBLE);
				db.pwEditor.setHint("密码");
				view.setText("忘记密码");
			}
		}
		
		
	}
	
}

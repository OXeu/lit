package com.zmide.lit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zmide.lit.R;
import com.zmide.lit.adapter.SettingAdapter;
import com.zmide.lit.base.BaseActivity;
import com.zmide.lit.bean.Setting;
import com.zmide.lit.object.Mode;

import java.util.ArrayList;

public class SettingActivity extends BaseActivity {
	private RecyclerView mSettingRecyclerView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		initView();
		loadSetting();
	}
	
	private void initView() {
		ImageView mSettingBack = findViewById(R.id.settingBack);
		mSettingRecyclerView = findViewById(R.id.settingRecyclerView);
		mSettingBack.setOnClickListener(view -> finish());
	}
	
	private ArrayList<Setting> getSettings() {
		ArrayList<Setting> list = new ArrayList<>();
		String[] titles = {"通用",
				"壁纸",
				"小球",
				"实验室",
				"高级",
				"备份&还原",
				"关于",
				"隐私政策",
				"用户协议",};
		Intent[] intents = {
				new Intent(SettingActivity.this, SettingChildActivity.class).putExtra("mode", Mode.NORMAL),
				new Intent(SettingActivity.this, SettingChildActivity.class).putExtra("mode", Mode.CUSTOM),
				new Intent(SettingActivity.this, SettingChildActivity.class).putExtra("mode", Mode.BALL),
				new Intent(SettingActivity.this, SettingChildActivity.class).putExtra("mode", Mode.LAB),
				new Intent(SettingActivity.this, SettingChildActivity.class).putExtra("mode", Mode.DIY),
				new Intent(SettingActivity.this, SettingChildActivity.class).putExtra("mode", Mode.BACKUP),
				new Intent(this, AboutActivity.class),
		};
		for (int i = 0; i < titles.length && i < intents.length; i++) {
			Setting setting = new Setting(titles[i], intents[i]);
			list.add(setting);
		}
		return list;
	}
	
	private void loadSetting() {
		ArrayList<Setting> Settings = getSettings();
		LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);//这里我们使用默认的线性布局管理器,将其设为垂直显示
		mSettingRecyclerView.setLayoutManager(mLayoutManager);//设置布局管理器
		SettingAdapter mAdapter = new SettingAdapter(this, Settings);//实例化适配器
		mSettingRecyclerView.setAdapter(mAdapter);//设置适配器
	}
	
}

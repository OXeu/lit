package com.zmide.lit.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import com.zmide.lit.adapter.SettingChildAdapter;
import com.zmide.lit.base.BaseActivity;
import com.zmide.lit.bean.SettingChild;
import com.zmide.lit.object.Diy;
import com.zmide.lit.object.Mode;
import java.util.ArrayList;
import java.util.Arrays;
import com.zmide.lit.util.MSharedPreferenceUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.blankj.utilcode.util.PathUtils;
import com.zmide.lit.R;
import com.zmide.lit.interfaces.Dark;
import com.zmide.lit.bookmark.BookmarkExport;
import com.zmide.lit.view.Editor;
import com.zmide.lit.interfaces.EditClick;
import java.util.Date;
import com.zmide.lit.util.MFileUtils;
import android.widget.Toast;
import com.zmide.lit.util.MToastUtils;
import com.zmide.lit.util.TimeUtil;

public class SettingChildActivity extends BaseActivity {
	private RecyclerView mSettingRecyclerView;
	private int mode;
	private SettingChild[] st;
	private SharedPreferences.OnSharedPreferenceChangeListener sharedlistener = (share, name) -> ((Dark) SettingChildActivity.this).changeFullscreen();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		mode = getIntent().getIntExtra("mode", Mode.NORMAL);
		initView();
		loadSetting();
	}

	private void initView() {
		ImageView mSettingBack = findViewById(R.id.settingBack);
		mSettingRecyclerView = findViewById(R.id.settingRecyclerView);
		mSettingBack.setOnClickListener( (view) -> finish());
	}

	private SettingChild[] getSetting(int mode) {
		Date date =  new Date();
		String filename4Export = "LitBrowserBookmark "+ TimeUtil.getDate(System.currentTimeMillis()+"");
		switch (mode) {
			case Mode.NORMAL:
				st = new SettingChild[]{
					new SettingChild("通用设置"),
					new SettingChild("主题模式", "设置主题模式", new String[]{"日间模式", "夜间模式", "跟随系统"}, "themeMode", "2", null, SettingChild.CHOOSE),
					new SettingChild("全屏模式", "隐藏状态栏和导航栏", new String[]{""}, "isfullscreen", "false", null, SettingChild.SWITCH),


					new SettingChild("网页设置"),
					new SettingChild(true, "允许JavaScript", "关闭将导致部分网页异常", new String[]{""}, "javascript", "true", null, SettingChild.SWITCH),
					new SettingChild(false, "剪贴板权限管理", "如开启后无法复制，请关闭（关闭后下面两项设置项不会生效），重启后生效", new String[]{""}, "clip_manage", "true", null, SettingChild.SWITCH),
					new SettingChild(false, "剪贴板权限", "修改网页写入剪贴板权限", new String[]{"询问", "允许", "拒绝"}, "clip_enable", "0", null, SettingChild.CHOOSE),
					new SettingChild(false, "剪贴板权限拒绝提示", "拒绝网页写入剪贴板权限后提示", new String[]{""}, "clip_refuse_toast", "true", null, SettingChild.SWITCH),
					new SettingChild(true, "字体大小", "调整网页字体大小", new String[]{"小", "中", "大"}, "webfont", "1", null, SettingChild.CHOOSE),
					new SettingChild(true, "缓存策略", "更改网页缓存策略", new String[]{"本地优先（默认）", "本地优先（无论是否过期）", "仅从本地", "仅从网络"}, "cache_mode", "0", null, SettingChild.CHOOSE2),
					new SettingChild(true, "支持缩放", "支持缩放", new String[]{""}, "zoom", "true", null, SettingChild.SWITCH),
					new SettingChild(true, "插件执行成功提示", "插件执行成功后弹出提示框", new String[]{""}, "plugin_suc", "false", null, SettingChild.SWITCH),
					new SettingChild(true, "下拉刷新", "下拉刷新当前浏览的网页", new String[]{""}, "can_refresh", "false", null, SettingChild.SWITCH),
					new SettingChild(true, "插件执行失败提示", "插件执行失败后弹出提示框", new String[]{""}, "plugin_fail", "false", null, SettingChild.SWITCH),
					new SettingChild(true, "允许打开外部应用", "允许打开外部应用", new String[]{""}, "oapp", "true", null, SettingChild.SWITCH),

					new SettingChild("其他设置"),
					new SettingChild("启动恢复", "启动时恢复上次未关闭标签页", new String[]{"禁用","询问","总是"}, "state_resume_type", "0", null, SettingChild.CHOOSE),
					new SettingChild("下载路径", "自定义下载路径", "download_uri_path", "应用私有文件夹", new OnClickListener(){
							@Override
							public void onClick(View v) {
								openDirectory();
							}
						}),
					new SettingChild("默认下载器", "下载默认调用的下载器", new String[]{""}, "downloader", "", null, SettingChild.DOWNLOAD),
				};
				break;
			case Mode.DIY:
				st = new SettingChild[]{
					new SettingChild("主页", "设置默认主页", new String[]{""}, "webpage", "经典主页", new Intent(SettingChildActivity.this, DiyActivity.class), SettingChild.INTENT),
					new SettingChild("UA", "设置User Agent", new String[]{""}, "ua", "", new Intent(SettingChildActivity.this, DiyActivity.class).putExtra("type", Diy.UA), SettingChild.INTENT),
					new SettingChild("插件", "JavaScript脚本", new String[]{""}, "plugin", "", new Intent(SettingChildActivity.this, DiyActivity.class).putExtra("type", Diy.PLUGIN), SettingChild.INTENT),
					new SettingChild("搜索引擎", "切换简洁版搜索引擎", new String[]{""}, "search_engine", "", new Intent(SettingChildActivity.this, DiyActivity.class).putExtra("type", Diy.SEARCH_ENGINE), SettingChild.INTENT),
				};
				break;
			case Mode.CUSTOM:
				st = new SettingChild[]{
					new SettingChild("自定义壁纸", "使用自定义壁纸", new String[]{""}, "is_apply_wallpaper", "false", null, SettingChild.SWITCH),
					new SettingChild("设置壁纸", "设置主页壁纸", new String[]{""}, "wallpaper", "", new Intent(SettingChildActivity.this, SetWallpaper.class), SettingChild.INTENT),
					new SettingChild("主页图标颜色", "调整首页图标颜色", new String[]{"跟随主题", "浅色", "深色"}, "title_mode", "0", null, SettingChild.CHOOSE),
					new SettingChild("隐藏Logo", "隐藏首页Logo", new String[]{""}, "logo_display", "false", null, SettingChild.SWITCH),
				};
				break;
			case Mode.LAB:
				st = new SettingChild[]{
					new SettingChild("负片模式", "夜间模式下对网页色彩进行负片处理", new String[]{""}, "web_isdark", "false", null, SettingChild.SWITCH),
					new SettingChild("反转手势", "反转小球前进后退手势", new String[]{""}, "isChangeGesture", "false", null, SettingChild.SWITCH),
					new SettingChild("检测更新", "进入应用进行检测更新", new String[]{""}, "is_check_update", "true", null, SettingChild.SWITCH),
					new SettingChild("调试模式", "异常时显示完整错误", new String[]{""}, "debug", "false", null, SettingChild.SWITCH),
                };
				break;
			case Mode.BACKUP:
				st = new SettingChild[]{
					new SettingChild("导入书签", "支持html/htm书签导入", new String[]{""}, "", "", new Intent(this, BookmarkImport.class), SettingChild.INTENT),
					new SettingChild("导出书签", "导出书签至下载目录","请输入导出书签文件名称",new int[]{R.drawable.quill_pen_line,R.drawable.quill_pen_fill},"导出","取消",null,filename4Export,new EditClick(){
							@Override
							public void onClick(View v,Editor editor) {
								String uris = MSharedPreferenceUtils.getWritablePath();
								Uri uri = null;
								if(uris!=null){
									uri = Uri.parse(uris);
									String fName = editor.getText()+"";
									String text = new BookmarkExport().toString();
									MFileUtils.writeText(text,fName,"text/html",uri);
									MToastUtils.makeText("导出成功").show();
									}
									else{
										openDirectory();
										MToastUtils.makeText("请设置文件保存路径后重试").show();
									}
									
								
							}
						}),
					//new SettingChild("反转手势", "反转小球前进后退手势", new String[]{""}, "isChangeGesture", "false", null, SettingChild.SWITCH),
				};
				break;
			case Mode.BALL:
				st = new SettingChild[]{
					new SettingChild("小球图标样式", "设置小球图标", new String[]{"默认", "显示多窗口数量", "自定义"}, "is_apply_ball", "0", null, SettingChild.CHOOSE2),
					new SettingChild("设置图标", "设置小球图标", new String[]{""}, "ball_icon", "", new Intent(SettingChildActivity.this, SetBallIcon.class), SettingChild.INTENT),
					new SettingChild("小球对齐方式", "调整小球位置", new String[]{"左", "中", "右"}, "ball_align", "2", null, SettingChild.CHOOSE),
					//new SettingChild("允许隐藏小球", "允许滑动网页时自动显示/隐藏小球", new String[]{""}, "canHide", "true", null, SettingChild.SWITCH),
					new SettingChild("自动展开标题栏", "允许滑动网页时自动展开标题栏", new String[]{""}, "autoshow", "true", null, SettingChild.SWITCH),
					new SettingChild("自动收缩标题栏", "允许滑动网页时自动收缩标题栏", new String[]{""}, "autoclose", "true", null, SettingChild.SWITCH),
					new SettingChild("自动锁定小球", "允许滑动网页时自动锁定/解锁小球，锁定后无法进行滑动操作", new String[]{""}, "canHide", "false", null, SettingChild.SWITCH),
					new SettingChild("小球长按移动", "支持小球长按移动", new String[]{""}, "movable", "true", null, SettingChild.SWITCH),
					new SettingChild("双子模式", "小球最小化时保留多窗口小球", new String[]{""}, "is_double_ball", "true", null, SettingChild.SWITCH),
					//new SettingChild("小球灵敏度", "调整小球灵敏度", new String[]{""}, "", "", new Intent(SettingChildActivity.this, ModBallSetting.class), SettingChild.INTENT),
				};
				break;
		}
		return st;
	}

	public void openDirectory() {
		// Choose a directory using the system's file picker.
		Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);

		// Provide read access to files and sub-directories in the user-selected
		// directory.
		intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

		// Optionally, specify a URI for the directory that should be opened in
		// the system file picker when it loads.

		//intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uriToLoad);
		startActivityForResult(intent, 1479);
	}

	private void loadSetting() {
		ArrayList<SettingChild> Settings = new ArrayList<>(Arrays.asList(getSetting(mode)));
		LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);//这里我们使用默认的线性布局管理器,将其设为垂直显示
		mSettingRecyclerView.setLayoutManager(mLayoutManager);//设置布局管理器
		SettingChildAdapter mAdapter = new SettingChildAdapter(this, Settings);//实例化适配器
		mSettingRecyclerView.setAdapter(mAdapter);//设置适配器
		SharedPreferences sp = getSharedPreferences("setting", Context.MODE_PRIVATE);
		sp.registerOnSharedPreferenceChangeListener(sharedlistener);
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode,
								 Intent resultData) {
		if (requestCode == 1479 && resultCode == Activity.RESULT_OK) {
			// The result data contains a URI for the document or directory that
			// the user selected.
			Uri uri = null;
			if (resultData != null) {
				uri = resultData.getData();
				MSharedPreferenceUtils.getSharedPreference().edit().putString("download_uri_path", uri.getLastPathSegment()).commit();
				MSharedPreferenceUtils.getSharedPreference().edit().putString("download_uri", uri.toString()).commit();
			}
		}
	}


}

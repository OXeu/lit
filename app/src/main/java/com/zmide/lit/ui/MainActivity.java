package com.zmide.lit.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.umeng.commonsdk.utils.Chiper;
import com.zmide.lit.R;
import com.zmide.lit.base.BaseActivity;
import com.zmide.lit.http.HttpRequest;
import com.zmide.lit.interfaces.UpdateInterface;
import com.zmide.lit.interfaces.WindowsInterface;
import com.zmide.lit.javascript.LitJavaScript;
import com.zmide.lit.main.BallEnvironment;
import com.zmide.lit.main.IndexEnvironment;
import com.zmide.lit.main.MProgressManager;
import com.zmide.lit.main.MainViewBindUtils;
import com.zmide.lit.main.MenuDialog;
import com.zmide.lit.main.SearchEnvironment;
import com.zmide.lit.main.StatusEnvironment;
import com.zmide.lit.main.WebContainer;
import com.zmide.lit.main.WebEnvironment;
import com.zmide.lit.main.WindowsManager;
import com.zmide.lit.main.firstGuide;
import com.zmide.lit.object.Diy;
import com.zmide.lit.skin.SkinManager;
import com.zmide.lit.util.DBC;
import com.zmide.lit.util.MExceptionUtils;
import com.zmide.lit.util.MSharedPreferenceUtils;
import com.zmide.lit.util.MToastUtils;
import com.zmide.lit.util.MWebStateSaveUtils;
import com.zmide.lit.util.MWindowsUtils;
import com.zmide.lit.view.LitWebView;

import java.util.ArrayList;

import static com.zmide.lit.main.MainViewBindUtils.getBallCardView;
import static com.zmide.lit.main.MainViewBindUtils.getBallText;
import static com.zmide.lit.main.MainViewBindUtils.getWebFrame;

public class MainActivity extends BaseActivity implements WindowsInterface {
	private static final long TIME_EXIT = 2000;
	private long mBackPressed;
	private boolean isResume = false;
	public static ArrayList<SurfaceView> surfaceViews = new ArrayList<>();
	@SuppressLint("SetTextI18n")
	@Override
	public void onWindowsCountChanged(int count) {
		getBallText().setText(count +"");
	}
	
	@Override
	public void onWindowsStateChanged() {
	
	}

	/**
	 * 功能
	 * 初始化小球
	 * 初始化Web环境
	 * 初始化搜索
	 * 选择性初始化使用引导
	 */


	@SuppressLint("PrivateApi")
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MExceptionUtils.init(this);
		if (MWebStateSaveUtils.hasStates()) {
			switch (MSharedPreferenceUtils.getSharedPreference().getString("state_resume_type", "0")) {
				case "0"://不处理
					MWebStateSaveUtils.deleteAllStates();
					break;
				case "1"://询问
					MToastUtils.makeText(this, "是否恢复上次未关闭的标签页", "恢复", new View.OnClickListener() {


						@Override
						public void onClick(View v) {
							WebContainer.resumeData();
							isResume = true;
						}
					},5000).show();
					new Handler().postDelayed(()->{
						if (!isResume)
							MWebStateSaveUtils.deleteAllStates();
					},5000);
					break;
				case "2"://打开
					WebContainer.resumeData();
					break;
			}
		}

		Chiper.init(this);
		StatusEnvironment.init(MainActivity.this);
		MWindowsUtils.init(MainActivity.this);
		MainViewBindUtils.init(MainActivity.this);
		MProgressManager.init(MainActivity.this);
		BallEnvironment.init(MainActivity.this);
		SearchEnvironment.init(MainActivity.this);
		WebEnvironment.init(MainActivity.this);
		WebContainer.init(MainActivity.this, getIntent());//容器加载时处理外部请求
		WindowsManager.init(MainActivity.this);
		LitJavaScript.init(this);
		MenuDialog.init(MainActivity.this);
		MenuDialog.initDialog();
		MSharedPreferenceUtils.getSharedPreference().registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
		BallEnvironment.start();
		SearchEnvironment.start();
		IndexEnvironment.start();
		WebEnvironment.start();
		findViewById(R.id.mainMask).setVisibility(View.GONE);
		MThemeConfig();
		SearchEnvironment.setAdapter();
		if (MSharedPreferenceUtils.getSharedPreference().getString("is_check_update", "true").equals("true"))
			HttpRequest.getUpdate(this, new UpdateInterface() {
				@Override
				public void onError(Exception e) {
				}
				
				@Override
				public void onNewest() {
				}
			});
		HttpRequest.getNews(this);
		//VideoReplacer.binder();
		
	}
	
	public static ArrayList<SurfaceView> getSurfaceView(){
		return surfaceViews;
	}
	
	private SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener = (sharedPreferences, key) -> {
		MThemeConfig();
		initRendering();
	};
	
	
	public static int methodBeHooked(int a, int b) {
		a = a + 1 + 2;
		b = b + a + 3;
		Log.e("MainActivity", "call methodBeHooked origin");
		return a + b;
	}
	private void MThemeConfig(){
		getBallCardView().post(() -> {
			getBallCardView().setCardBackgroundColor(SkinManager.getInstance().getColor(R.color.bgcolor));
			RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) getBallCardView().getLayoutParams();
			lp.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
			lp.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			lp.removeRule(RelativeLayout.CENTER_HORIZONTAL);
			switch (MSharedPreferenceUtils.getSharedPreference().getString("ball_align", "2") + "") {
				case "0":
					lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
					break;
				case "1":
					lp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
					break;
				default:
					lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
					break;
			}
			getBallCardView().setLayoutParams(lp);
			getBallCardView().requestLayout();
		});
		changeMode();
		changeFullscreen();
	}
	
	
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);//must store the new intent unless getIntent() will return the old one
		handleIntent(intent);
	}
	
	private void handleIntent(Intent intent) {
		String url = null;
		Bundle extra = intent.getExtras();
		if (Intent.ACTION_WEB_SEARCH.equals(intent.getAction())) {
			if (extra != null) {
				String query = extra.getString("query");
				SearchEnvironment.Search(query);
			}

		} else {
			if (intent.getData() != null) {
				url = intent.getData().toString();
			} else if (extra != null) {
				url = extra.getString("url");
			}
			if (url != null) {
				if (intent.getBooleanExtra("ifNew", true))
					WebContainer.createWindow(url, true);
				else
					WebContainer.loadUrl(url);
			}
		}
	}
	
	
	
	@Override
	protected void onStart() {
		super.onStart();
		firstGuide.init(this);
		if (MSharedPreferenceUtils.getSharedPreference().getBoolean("isfirst", true)) {
			firstGuide.showGuide();
			firstInit();
			/*MDialogUtils.Builder builder = new MDialogUtils.Builder(MainActivity.this);
			MDialogUtils dialog = builder.setTitle("操作引导")
					.setMessage("是否需要进行简单的操作引导")
					.setNegativeButton("不需要", (dialog2, which) -> dialog2.cancel())
					.setPositiveButton("需要", (dialog2, which) -> {
						firstGuide.showGuide();
						dialog2.cancel();
					})
					.create();
			dialog.setCancelable(false);
			dialog.show();*/
		}
		MSharedPreferenceUtils.getSharedPreference().edit().putBoolean("isfirst", false).apply();
		/*if (MSharedPreferenceUtils.getSharedPreference().getBoolean("isfirst", true)) {
			MDialogUtils.Builder builder = new MDialogUtils.Builder(MainActivity.this);
			MDialogUtils dialogs = builder.setTitle("权限请求")
					.setMessage("为保证本应用能够正常运行，需要以下权限\n*读写手机存储（用于下载文件）")
					.setNegativeButton("忽略", (dialog, which) -> dialog.cancel())
					.setPositiveButton("同意", (dialog, which) -> {
						getPermission(true);
						dialog.cancel();
					})
					.create();
			dialogs.setCancelable(false);
			dialogs.show();
			firstInit();
		} else {
			getPermission(false);
		}*/
	}
	
	private static final float[] NEGATIVE_COLOR = {
			-1.0f, 0, 0, 0, 255, // Red
			0, -1.0f, 0, 0, 255, // Green
			0, 0, -1.0f, 0, 255, // Blue
			0, 0, 0, 1.0f, 0     // Alpha
	};
	public void initRendering() {
		if (getWebFrame() != null) {
			if (MSharedPreferenceUtils.getSharedPreference().getString("web_isdark", "false").equals("true") && isNight()) {
				Paint paint = new Paint();
				ColorMatrix matrix = new ColorMatrix();
				matrix.set(NEGATIVE_COLOR);
				ColorMatrix gcm = new ColorMatrix();
				gcm.setSaturation(0);
				ColorMatrix concat = new ColorMatrix();
				concat.setConcat(matrix, gcm);
				ColorMatrixColorFilter filter = new ColorMatrixColorFilter(concat);
				paint.setColorFilter(filter);
				// maybe sometime LAYER_TYPE_NONE would better?
				getWebFrame().setLayerType(View.LAYER_TYPE_HARDWARE, paint);
			} else {
				getWebFrame().setLayerType(View.LAYER_TYPE_HARDWARE, null);
			}
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
				initRendering();
			
		
	}
	
	/*private void getPermission(final boolean isFirst) {
		new PermissionHelper(this).requestPermission(allGranted -> {
			if (allGranted) {
				//成功
				if (isFirst) {
					MDialogUtils.Builder builder = new MDialogUtils.Builder(MainActivity.this);
					MDialogUtils dialog = builder.setTitle("操作引导")
							.setMessage("是否需要进行简单的操作引导")
							.setNegativeButton("不需要", (dialog2, which) -> dialog2.cancel())
							.setPositiveButton("需要", (dialog2, which) -> {
								firstGuide.showGuide();
								dialog2.cancel();
							})
							.create();
					dialog.setCancelable(false);
					dialog.show();
				}
				MSharedPreferenceUtils.getSharedPreference().edit().putBoolean("isfirst", false).apply();
			} else {
				//失败
				MDialogUtils.Builder builder = new MDialogUtils.Builder(MainActivity.this);
				builder.setTitle("权限不足")
						.setMessage("未授予本应用足够权限，可能会导致程序无法正常运行")
						.setNegativeButton("忽略", (dialog, which) -> dialog.cancel())
						.setPositiveButton("重新请求", (dialog, which) -> {
							getPermission(true);
							dialog.cancel();
						})
						.create()
						.show();
			}
		}, Permission.PERMISSIONS);
	}*/
	
	@Override
	public void onBackPressed() {
		LitWebView mWebView = WebContainer.getWebView();
		if (WindowsManager.isWindowsShowing()) {
			WindowsManager.hideWindows();
		} else if (SearchEnvironment.isShowing()) {
			SearchEnvironment.hide();
		} else {
			if (mWebView.canGoBack())
				mWebView.goBack();
			else if (WebContainer.getWindowCount() != 1) {
				//不是最后一个页面
				WebContainer.removeWindow(WebContainer.getWindowId());
			} else {
				Toast toast = MToastUtils.makeText("再次点击退出程序", MToastUtils.LENGTH_SHORT);
				if (mBackPressed + TIME_EXIT > System.currentTimeMillis()) {
					toast.cancel();
					finish();
					new Handler().postDelayed(() -> System.exit(0), 0);
				} else {
					toast.show();
					mBackPressed = System.currentTimeMillis();
				}
			}
		}
	}
	
	public void firstInit(){
		DBC.getInstance(this).addDiy("百度","","https://m.baidu.com/from=1022560v/s?word=%s","", Diy.SEARCH_ENGINE,false);
		DBC.getInstance(this).addDiy("必应","","https://cn.bing.com/search?q=%s","",Diy.SEARCH_ENGINE,false);
		DBC.getInstance(this).addDiy("秘迹","","https://m.mijisou.com/?q=%s","",Diy.SEARCH_ENGINE,false);
		DBC.getInstance(this).addDiy("Google","","https://www.google.com/search?q=%s","",Diy.SEARCH_ENGINE,false);
		DBC.getInstance(this).addDiy("Magi","","https://magi.com/search?q=%s","",Diy.SEARCH_ENGINE,false);
		DBC.getInstance(this).addDiy("默认","默认UA","","",Diy.UA,false);
		DBC.getInstance(this).addDiy("简单搜索","去除百度广告","Mozilla/5.0 (Linux; Android 10; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.23.184.0 Mobile Safari/537.36 Edge/18.18362 SearchCraft/2.8.2 Baidu;P1 10.0","http(s|)://(\\w*\\.|)baidu.com(^\\s*)",Diy.UA,false);
		DBC.getInstance(this).addDiy("微信","可以访问部分仅限微信打开的网站","Mozilla/5.0 (Linux; Android 5.0; SM-N9100 Build/LRX21V) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/37.0.0.0 Mobile Safari/537.36 MicroMessenger/6.0.2.56_r958800.520 NetType/WIFI","http(s|)://mp.qq.com(^\\s*)",Diy.UA,false);
		DBC.getInstance(this).addDiy("电脑(Chrome)","Chrome Windows UA","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.87 Safari/537.36","http(s|)://(\\w*\\.|)baidu.com(^\\s*)",Diy.UA,false);
		DBC.getInstance(this).addDiy("默认主页","简洁默认主页","file:///android_asset/index.html","",Diy.WEBPAGE,true);
		DBC.getInstance(this).addDiy("网页版主页","经典网页主页","file:///android_asset/index2.html","",Diy.WEBPAGE,false);
		
	}
	
}

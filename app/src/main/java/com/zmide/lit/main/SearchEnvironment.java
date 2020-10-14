package com.zmide.lit.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.zmide.lit.adapter.EngineAdapter;
import com.zmide.lit.object.Diy;
import com.zmide.lit.object.GMap;
import com.zmide.lit.ui.MainActivity;
import com.zmide.lit.util.DBC;
import com.zmide.lit.util.MDataBaseSettingUtils;
import com.zmide.lit.util.MExceptionUtils;
import com.zmide.lit.util.MKeyboardUtils;
import com.zmide.lit.util.MSharedPreferenceUtils;
import com.zmide.lit.util.MWindowsUtils;
import com.zmide.lit.util.ViewO;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Pattern;


import static com.zmide.lit.main.MainViewBindUtils.getSearchButton;
import static com.zmide.lit.main.MainViewBindUtils.getSearchEdit;
import static com.zmide.lit.main.MainViewBindUtils.getSearchEngineList;
import static com.zmide.lit.main.MainViewBindUtils.getSearchParent;

public class SearchEnvironment {
	private static MainActivity activity;
	
	
	public static void init(MainActivity mainActivity) {
		if (activity == null)
			activity = mainActivity;
		initSearch();
		initSearchEngineList();
	}
	
	private static void initSearchEngineList() {
		LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity);//这里我们使用默认的线性布局管理器,将其设为垂直显示
		mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		getSearchEngineList().setLayoutManager(mLayoutManager);//设置布局管理器
		setAdapter();
	}
	
	public static void setAdapter() {
		ArrayList<Diy> engine = DBC.getInstance(activity).getDiys(Diy.SEARCH_ENGINE, false);
		EngineAdapter mAdapter = new EngineAdapter(activity, engine);//实例化适配器
		getSearchEngineList().setAdapter(mAdapter);//设置适配器
	}
	
	
	private static void initSearch() {
		getSearchButton().setOnClickListener(view -> Search(getSearchEdit().getText().toString()));
		getSearchButton().setOnLongClickListener(v -> {
			SearchTurn180(getSearchEdit().getText().toString());
			return false;
		});
		getSearchEdit().setOnKeyListener((v, keyCode, event) -> {
			if (keyCode == KeyEvent.KEYCODE_ENTER) {
				Search(getSearchEdit().getText().toString());
			}
			return false;
		});
		/*
		 **	https://blog.csdn.net/c1392851600/article/details/87867177
		 */
		/*getSearchEdit().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			private int statusBarHeight;
			
			@Override
			public void onGlobalLayout() {
				Rect r = new Rect();
				// 使用最外层布局填充，进行测算计算
				getSearchParent().getWindowVisibleDisplayFrame(r);
				int screenHeight = getSearchParent().getRootView().getHeight();
				int heightDiff = screenHeight - (r.bottom - r.top);
				if (heightDiff > 100) {
					// 如果超过100个像素，它可能是一个键盘。获取状态栏的高度
					statusBarHeight = 0;
				}
				try {
					@SuppressLint("PrivateApi") Class<?> c = Class.forName("com.android.internal.R$dimen");
					Object obj = c.newInstance();
					Field field = c.getField("status_bar_height");
					int x = Integer.parseInt("0" + field.get(obj));
					statusBarHeight = activity.getResources().getDimensionPixelSize(x);
				} catch (Exception e) {
					e.printStackTrace();
				}
				// 软键盘的高度
				int realKeyboardHeight;
				if (MSharedPreferenceUtils.getSharedPreference().getString("isfullscreen", "false").equals("false")) {
					realKeyboardHeight = heightDiff - statusBarHeight + MWindowsUtils.dp2px(0);
				} else {
					realKeyboardHeight = heightDiff + MWindowsUtils.dp2px(0);
				}
				if (realKeyboardHeight > 400 && !isEditUp) {
					isEditUp = true;
					int navigationHeight = BarUtils.isNavBarVisible(MainActivity.this) ? BarUtils.getNavBarHeight() : 0;
					distance_to_move = realKeyboardHeight - navigationHeight;
					putEditTextUp(distance_to_move);
					//键盘弹起
				} else if (realKeyboardHeight < 400 && !isEditDown) {
					isEditDown = true;
					putEditDown(distance_to_move);
				}
			}
		});
		*/
	}
	
	public static void Search(String key) {
		String en = MDataBaseSettingUtils.getSingleSetting(Diy.SEARCH_ENGINE);
		String input = key.replace(" ", "%20");
		Pattern httpPattern;
		//初始化正则
		httpPattern = Pattern.compile("(^(https|http|ftp)://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?$)|(^file://)[\\w\\W]*");
		//开始判断了
		if (httpPattern.matcher(input).matches()) {
			//这是一个网址链接
			WebContainer.loadUrl(input);
		} else {
			//这不是一个网址链接
			httpPattern = Pattern.compile("^([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?$");
			//开始判断了
			if (httpPattern.matcher(input).matches()) {
				//这是一个网址链接
				WebContainer.loadUrl("http://" + input);
			} else {
				try {
					WebContainer.loadUrl(en.replace("%s", input));
				} catch (Exception e) {
					WebContainer.loadUrl(en + input);
				}
			}
		}
		hide();
	}
	
	public static void SearchTurn180(String key) {
		String en = MDataBaseSettingUtils.getSingleSetting(Diy.SEARCH_ENGINE);
		String input = key.replace(" ", "%20");
		if ("lit:reset".equals(input)) {
			BallEnvironment.resetBall();
		} else {
			Pattern httpPattern;
			//初始化正则
			httpPattern = Pattern.compile("(^((https|http|ftp)://|)([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?$)|(^file://)[\\w\\W]*");
			//开始判断了
			if (httpPattern.matcher(input).matches()) {
				//这是一个网址链接,直接搜索
				try {
					WebContainer.loadUrl(en.replace("%s", input));
				} catch (Exception e) {
					WebContainer.loadUrl(en + input);
				}
			} else {
				//这不是一个网址链接
				WebContainer.loadUrl(input);
			}
		}
		hide();
	}
	
	public static void SearchAnimation(boolean isshow) {
		int ty = 0;
		int tx = 0;
		int th = 0;
		int y = 0;
		RelativeLayout mIndexSearchBar = WebContainerPlus.getViewHolder().getIndexSearchBar();
		int x = mIndexSearchBar.getWidth();
		int h = mIndexSearchBar.getHeight();
		int oY = MWindowsUtils.dp2px(0);
		int oX = MWindowsUtils.getWidth() - ((RelativeLayout.LayoutParams) mIndexSearchBar.getLayoutParams()).leftMargin * 2;
		int oH = MWindowsUtils.dp2px(48);
		int tY = MWindowsUtils.dp2px(48);
		int defaultMargin = MWindowsUtils.dp2px(32);
		if (mIndexSearchBar.getAnimation() != null)
			if (!mIndexSearchBar.getAnimation().hasEnded()) {
				RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mIndexSearchBar.getLayoutParams();
				lp.width = tx;//x + ((tx - x));
				lp.height = h + ((th - h));
				lp.setMargins(defaultMargin, (ty), defaultMargin, 0);
				mIndexSearchBar.setLayoutParams(lp);
				mIndexSearchBar.requestLayout();
				
				return;
			}
		if (isshow) {//是否执行展开动画
			ty = tY;
			tx = oX;
			th = oH;
		} else {
			ty = oY;
			tx = oX;
			th = oH;
		}
		int finalTx = tx;
		int finalTh = th;
		int finalTy = ty;
		Animation animation = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t) {
				RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mIndexSearchBar.getLayoutParams();
				lp.width = finalTx;//x + (int) ((finalTx - x) * interpolatedTime);
				lp.height = h + (int) ((finalTh - h) * interpolatedTime);
				int offY = (int) (y + (finalTy - y) * interpolatedTime);
				lp.setMargins(defaultMargin, offY, defaultMargin, 0);
				mIndexSearchBar.setLayoutParams(lp);
				mIndexSearchBar.requestLayout();
			}
			
			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};
		animation.setDuration(500);
		mIndexSearchBar.startAnimation(animation);
	}
	
	public static void openSearchBar(String url) {
		if (url == null)
			url = WebContainer.getUrl() + "";
		try {
			url = URLDecoder.decode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			MExceptionUtils.reportException(e);
		}
		getSearchEdit().setText(url);
		KeyboardUtils.showSoftInput(activity);
		ViewO.showView(getSearchParent());
		getSearchEdit().requestFocus();
		MKeyboardUtils.fixAndroidBug5497(activity);
	}
	
	public static void asKey(String tip) {
		getSearchEdit().setText(tip);
	}
	
	public static void start() {
		//启动函数
		initSearch();
	}
	
	public static boolean isShowing() {
		return getSearchParent().getVisibility() == View.VISIBLE;
	}
	
	public static void hide() {
//		MKeyboardUtils.ignoreAndroidBug5497(activity.getWindow());
		ViewO.hideView(getSearchParent());
		KeyboardUtils.hideSoftInput(activity);
	}
	
	public static void onSearchEngine(String engine) {
		
		String en;
		if (!engine.equals(""))
			en = engine;
		else
			en = MDataBaseSettingUtils.getSingleSetting(Diy.SEARCH_ENGINE) + "";
		String input = getSearchEdit().getText().toString();
		try {
			WebContainer.loadUrl(en.replace("%s", input));
		} catch (Exception e) {
			WebContainer.loadUrl(en + input);
		}
		hide();
	}
}

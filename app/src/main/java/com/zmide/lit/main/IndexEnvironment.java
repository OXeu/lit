package com.zmide.lit.main;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.zmide.lit.R;
import com.zmide.lit.interfaces.Dark;
import com.zmide.lit.ui.MainActivity;
import com.zmide.lit.util.MDataBaseSettingUtils;
import com.zmide.lit.util.MSharedPreferenceUtils;
import com.zmide.lit.util.MToastUtils;
import com.zmide.lit.util.MWindowsUtils;
import com.zmide.lit.util.ViewO;

import java.io.FileNotFoundException;
import java.util.Objects;

import static com.zmide.lit.main.MainViewBindUtils.getBallCardView;
import static com.zmide.lit.main.MainViewBindUtils.getTitleParent;
import com.zmide.lit.adapter.web.WebAdapter;
import com.zmide.lit.adapter.web.WebAdapter.MyViewHolder;

public class IndexEnvironment {
	/**
	* 老规矩，写了睡觉
	* 2020 10.10 01:02
	* 首页工具类最开始是基于静态获取主页控件的实例来进行操作的
	* 想要实现重构并不难，甚至不需要改动IndexEnvironment的代码
	* 直接改动绑定类的获取控件代码就行了
	* 不过调用IndexEnvironment的初始化代码可能需要在Adapter的绑定事件里初始化
	*/
	
	
	
	private  MainActivity activity;
	private  SharedPreferences mSharedPreferences = MSharedPreferenceUtils.getSharedPreference();

	private  WebAdapter.MyViewHolder holder;
	
	public IndexEnvironment(MainActivity mainActivity,WebAdapter.MyViewHolder viewholder) {
			activity = mainActivity;
			if(holder == null){
				holder = viewholder;
				initIndex();
				MSharedPreferenceUtils.getSharedPreference().registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
			}
			
	}
	
	public IndexEnvironment() {

	}
	
	private SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener = (sharedPreferences, key) -> {
		initIndex();
	};
	
	public void hideIndex() {
		if(holder==null||activity==null)
			return;
		StatusEnvironment.updateStatusColor(WebContainerPlus.getWebView());
		ImageView mIndexWallpaper = holder.getIndexWallpaper();
		RelativeLayout mIndexParent = holder.getIndexParent();
		FrameLayout mWebFrame = holder.getWebFrame();
		LinearLayout mTitleParent = getTitleParent();
		if (mIndexWallpaper.getVisibility() == View.VISIBLE) {
			mIndexWallpaper.setVisibility(View.GONE);
		}
		if (mWebFrame.getVisibility() == View.GONE) {
			mWebFrame.setVisibility(View.VISIBLE);
		}
		mIndexParent.setVisibility(View.GONE);
		getBallCardView().setElevation(MWindowsUtils.dp2px(12));
	}
	
	
	public void showIndex() {
		if(holder==null||activity==null)
			return;
		BarUtils.setStatusBarColor(activity, 0x00000000);
		ImageView mIndexWallpaper = holder.getIndexWallpaper();
		RelativeLayout mIndexParent = holder.getIndexParent();
		FrameLayout mWebFrame = holder.getWebFrame();
		LinearLayout mTitleParent = getTitleParent();
		mIndexParent.setVisibility(View.VISIBLE);
		mWebFrame.setVisibility(View.GONE);
		ViewO.showView(mIndexParent);
		initIndex();
		SearchEnvironment.SearchAnimation(false);
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
			getBallCardView().setElevation(0);
		if (getBallCardView().getAnimation() != null)
			if (!getBallCardView().getAnimation().hasEnded()) {
				return;
			}
		BallEnvironment.shrinkBall();
	}
	
	
	@SuppressLint("ClickableViewAccessibility")
	private void initIndex() {
		if(holder==null|activity==null)
			return;
		RelativeLayout mIndexSearchBar = holder.getIndexSearchBar();
		TextView mIndexTitle = holder.getIndexTitle();
		RelativeLayout mIndexParent = holder.getIndexParent();
		Typeface customFont = Typeface.createFromAsset(activity.getAssets(), "font/a.ttf");
		mIndexTitle.setTypeface(customFont);
		mIndexTitle.setOnLongClickListener(v -> {
			mSharedPreferences.edit().putFloat("balloffsety", 0).apply();
			mSharedPreferences.edit().putFloat("balloffsetx", 0).apply();
			getBallCardView().setTranslationY(0f);
			getBallCardView().setTranslationX(0f);
			MToastUtils.makeText("小球已复原", MToastUtils.LENGTH_SHORT).show();
			return false;
		});
		int targetY = MWindowsUtils.dp2px(50);
		int defaultMargin = MWindowsUtils.dp2px(32);
		int oY = 0;
		View.OnTouchListener listener = new View.OnTouchListener() {
			private float oldY;
			private float y;
			
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent ev) {
				switch (ev.getAction()) {
					case MotionEvent.ACTION_DOWN:
						oldY = ev.getRawY();
						y = ev.getRawY();
						break;
					case MotionEvent.ACTION_MOVE:
						y = ev.getRawY();
						float vec = y - oldY;
						float offsetY = targetY * (1 - targetY / (vec + targetY)) + oY;
						if (vec > 0 && offsetY > oY) {
							RelativeLayout.LayoutParams lm = ((RelativeLayout.LayoutParams) mIndexSearchBar.getLayoutParams());
							lm.setMargins(defaultMargin, (int) offsetY, defaultMargin, 0);
							mIndexSearchBar.setLayoutParams(lm);
							mIndexSearchBar.requestLayout();
						}
						break;
					case MotionEvent.ACTION_UP:
						vec = y - oldY;
						y -= BarUtils.getStatusBarHeight();
						if ((targetY + oY) * 0.5 < vec && vec > 15) {// || ((vec <= 15 && vec >= 0 && (y < mIndexSearchBar.getBottom() && y > mIndexSearchBar.getTop())))) {
							SearchEnvironment.openSearchBar("");
							break;
						}
				}
				return false;
			}
		};
		mIndexParent.setOnTouchListener(listener);
		mIndexSearchBar.setOnTouchListener(listener);
		mIndexSearchBar.setOnClickListener(v -> SearchEnvironment.openSearchBar(""));
		int title_mode = Integer.parseInt(Objects.requireNonNull(mSharedPreferences.getString("title_mode", "0")));
		boolean isDark = ((Dark) activity).isNight();
		if (title_mode == 1 || (title_mode == 0 && isDark))//白色
		{
			mIndexTitle.setTextColor(0xccffffff);
			mIndexSearchBar.setBackgroundResource(R.drawable.searchbar_index_white);
		} else if (title_mode == 2 || title_mode == 0)//暗色
		{
			mIndexTitle.setTextColor(0xff333333);
			mIndexSearchBar.setBackgroundResource(R.drawable.searchbar_index_dark);
		}
		if (Objects.equals(mSharedPreferences.getString("logo_display", "false"), "true"))//隐藏
		{
			mIndexTitle.setVisibility(View.GONE);
		} else {
			mIndexTitle.setVisibility(View.VISIBLE);
		}
		ImageView mIndexWallpaper =  holder.getIndexWallpaper();
		if (mSharedPreferences.getString("is_apply_wallpaper", "false").equals("true")) {
			try {
				
				new Thread(() -> activity.runOnUiThread(() -> {
					
					Bitmap wallpaper = null;
					try {
						wallpaper = BitmapFactory.decodeStream(activity.openFileInput("wallpaper.png"));
						if (wallpaper != null) {
							mIndexWallpaper.setVisibility(View.VISIBLE);
							mIndexWallpaper.setImageBitmap(wallpaper);
						} else {
							mIndexWallpaper.setVisibility(View.GONE);
						}
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				})).start();
			} catch (Exception e) {
				mIndexWallpaper.setVisibility(View.GONE);
			}
		} else {
			mIndexWallpaper.setVisibility(View.GONE);
		}
		if (!Objects.equals(WebContainerPlus.getUrl(), MDataBaseSettingUtils.WebIndex)){
			mIndexWallpaper.setVisibility(View.GONE);
		}
	}
	
	
}

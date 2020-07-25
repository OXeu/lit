package com.zmide.lit.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.AdaptScreenUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zmide.lit.R;
import com.zmide.lit.adapter.SugAdapter;
import com.zmide.lit.object.BallData;
import com.zmide.lit.object.Diy;
import com.zmide.lit.object.WebsiteSetting;
import com.zmide.lit.object.json.BaiduSug;
import com.zmide.lit.ui.MainActivity;
import com.zmide.lit.util.MDataBaseSettingUtils;
import com.zmide.lit.util.MRegexUtils;
import com.zmide.lit.util.MSharedPreferenceUtils;
import com.zmide.lit.util.MToastUtils;
import com.zmide.lit.util.MWebsiteSettingDialog;
import com.zmide.lit.util.MWindowsUtils;
import com.zmide.lit.util.ViewO;
import com.zmide.lit.util.WebsiteUtils;
import com.zmide.lit.view.LitWebView;

import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;

import static com.zmide.lit.main.MainViewBindUtils.getBall;
import static com.zmide.lit.main.MainViewBindUtils.getBallCardView;
import static com.zmide.lit.main.MainViewBindUtils.getBallImage;
import static com.zmide.lit.main.MainViewBindUtils.getBallText;
import static com.zmide.lit.main.MainViewBindUtils.getBallWindowButton;
import static com.zmide.lit.main.MainViewBindUtils.getIndexWallpaper;
import static com.zmide.lit.main.MainViewBindUtils.getMainBallParent;
import static com.zmide.lit.main.MainViewBindUtils.getMainGestureColor;
import static com.zmide.lit.main.MainViewBindUtils.getMainGestureImage;
import static com.zmide.lit.main.MainViewBindUtils.getSearchButton;
import static com.zmide.lit.main.MainViewBindUtils.getSearchEdit;
import static com.zmide.lit.main.MainViewBindUtils.getSearchParent;
import static com.zmide.lit.main.MainViewBindUtils.getSugRecyclerView;
import static com.zmide.lit.main.MainViewBindUtils.getTitleParent;
import static com.zmide.lit.main.WebContainer.getUrl;
import static com.zmide.lit.main.WebContainer.getWindowId;

public class BallEnvironment {
	private static MainActivity activity;
	private static SharedPreferences mSharedPreferences = MSharedPreferenceUtils.getSharedPreference();
	private static View.OnTouchListener touchNormal;
	private static BallData ballData = new BallData();
	private static View.OnTouchListener touchMove;
	private static View.OnTouchListener touchWindow;
	private static SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener = (sharedPreferences, key) -> {
		loadBallStyle();
	};
	/*
	private int statusBarHeight;
	private int distance_to_move;
	private boolean isEditUp;
	private boolean isEditDown;
	private int realKeyboardHeight;
	private HashMap<String, Object> setting;
	private Dialog dialog;
	private static View.OnTouchListener touchMove;
	private boolean canMove;
	private float mLastY, mLastX;
	private float firstLevel;
	private boolean Movable;
	private PopupWindow pop;*/
	
	public static void init(MainActivity activityTemp) {
		if (activity == null)
			activity = activityTemp;
	}
	
	public static void hideBall() {
		MainViewBindUtils.getBallCardView().setVisibility(View.GONE);
	}
	
	public static void showBall() {
		MainViewBindUtils.getBallCardView().setVisibility(View.VISIBLE);
	}
	
	public static void expandBallWithSetting() {
		CardView mCardview = getBallCardView();
		int targetWidth = MWindowsUtils.getWidth() - mCardview.getPaddingRight();
		int zero = 0;
		int initialWidth = MSharedPreferenceUtils.getSharedPreference().getString("is_double_ball","true").equals("true")
				?
				AdaptScreenUtils.pt2Px(48):MWindowsUtils.dp2px(48);
		final int width = mCardview.getWidth();
		if (mCardview.getAnimation() != null)
			if (!mCardview.getAnimation().hasEnded()) {
				return;
			}
		
		//未展开，小球已显示，展开标题栏
		if (width == initialWidth && mCardview.getAlpha() == 1.0f && mSharedPreferences.getString("autoshow", "true").equals("true")) {
			final float targetX;
			targetX = mSharedPreferences.getFloat("balloffsetx", 0f);
			Animation animation = new Animation() {
				@Override
				protected void applyTransformation(float interpolatedTime, Transformation t) {
					ViewGroup.LayoutParams lp = mCardview.getLayoutParams();
					lp.width = initialWidth + (int) ((targetWidth - initialWidth) * interpolatedTime);
					float transX = targetX * (1 - interpolatedTime);
					mCardview.setTranslationX(transX);
					mCardview.setLayoutParams(lp);
					mCardview.requestLayout();
					if (interpolatedTime == 1) {
						getTitleParent().setVisibility(View.VISIBLE);
					} else {
						getTitleParent().setVisibility(View.GONE);
					}
				}
				
				@Override
				public boolean willChangeBounds() {
					return true;
				}
			};
			animation.setDuration(200);
			mCardview.startAnimation(animation);
		}
		
		//小球已隐藏，宽度为0，显示小球
		if (mCardview.getAlpha() != 1.0f) {
			ViewO.makeViewTranslucent(mCardview, 1.0f);
						/*
						Animation animation = new Animation() {
							@Override
							protected void applyTransformation(float interpolatedTime, Transformation t) {
								ViewGroup.LayoutParams lp = mCardview.getLayoutParams();
								lp.width = initialWidth + (int) ((zero - initialWidth) * (1 - interpolatedTime));
								mCardview.setLayoutParams(lp);
								mCardview.requestLayout();
							}
							
							@Override
							public boolean willChangeBounds() {
								return true;
							}
						};
						animation.setDuration(200);
						mCardview.startAnimation(animation);*/
		}
	}
	
	public static void shrinkBallWithSetting() {
		CardView mCardview = getBallCardView();
		int targetWidth = MWindowsUtils.getWidth() - mCardview.getPaddingRight();
		int zero = 0;
		int initialWidth = MSharedPreferenceUtils.getSharedPreference().getString("is_double_ball","true").equals("true")
				?
				AdaptScreenUtils.pt2Px(48):MWindowsUtils.dp2px(48);
		final int width = mCardview.getWidth();
		if (mCardview.getAnimation() != null)
			if (!mCardview.getAnimation().hasEnded()) {
				return;
			}
		
		
		if (width > initialWidth && mSharedPreferences.getString("autoclose", "true").equals("true")) {//已展开，收缩
			final float targetX;
			targetX = mSharedPreferences.getFloat("balloffsetx", 0f);
			Animation animation = new Animation() {
				@Override
				protected void applyTransformation(float interpolatedTime, Transformation t) {
					interpolatedTime = 1 - interpolatedTime;
					float transX = targetX * (1 - interpolatedTime);
					mCardview.setTranslationX(transX);
					ViewGroup.LayoutParams lp = mCardview.getLayoutParams();
					lp.width = initialWidth + (int) ((targetWidth - initialWidth) * interpolatedTime);
					mCardview.setLayoutParams(lp);
					mCardview.requestLayout();
				}
				
				@Override
				public boolean willChangeBounds() {
					return true;
				}
			};
			animation.setDuration(200);
			mCardview.startAnimation(animation);
		}
		int targetWidthT = 0;
		if (width == initialWidth)//未展开，仅存在小球，收缩消失
			if (!((targetWidthT == zero && mSharedPreferences.getString("canHide", "false").equals("false")))) {
				ViewO.makeViewTranslucent(mCardview, 0.2f);
							/*Animation animation = new Animation() {
								@Override
								protected void applyTransformation(float interpolatedTime, Transformation t) {
									ViewGroup.LayoutParams lp = mCardview.getLayoutParams();
									lp.width = initialWidth + (int) ((zero - initialWidth) * interpolatedTime);
									mCardview.setLayoutParams(lp);
									mCardview.requestLayout();
								}
								
								@Override
								public boolean willChangeBounds() {
									return true;
								}
							};
							animation.setDuration(200);
							mCardview.startAnimation(animation);*/
			}
		
	}
	
	
	public static void shrinkBall() {//收缩小球
		int targetWidth = MWindowsUtils.getWidth() - getBallCardView().getPaddingRight();
		int initialWidth = MSharedPreferenceUtils.getSharedPreference().getString("is_double_ball","true").equals("true")
				?
				AdaptScreenUtils.pt2Px(48):MWindowsUtils.dp2px(48);
		final int width = getBallCardView().getWidth();
		if (width != initialWidth) {//已展开，收缩
			Animation animation = new Animation() {
				@Override
				protected void applyTransformation(float interpolatedTime, Transformation t) {
					interpolatedTime = 1 - interpolatedTime;
					ViewGroup.LayoutParams lp = getBallCardView().getLayoutParams();
					lp.width = initialWidth + (int) ((targetWidth - initialWidth) * interpolatedTime);
					getBallCardView().setLayoutParams(lp);
					getBallCardView().requestLayout();
					if (interpolatedTime == 1) {
						getTitleParent().setVisibility(View.VISIBLE);
					} else {
						getTitleParent().setVisibility(View.GONE);
					}
				}
				
				@Override
				public boolean willChangeBounds() {
					return true;
				}
			};
			animation.setDuration(200);
			getBallCardView().startAnimation(animation);
		}
	}
	
	
	@SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
	private static void initBall() {
		getBallCardView().setTranslationY(mSharedPreferences.getFloat("balloffsety", 0));
		getBallCardView().setTranslationX(mSharedPreferences.getFloat("balloffsetx", 0));
		shrinkBall();
		int width = MWindowsUtils.getWidth();
		int paddingRight = getMainBallParent().getPaddingRight();
		getTitleParent().setVisibility(View.VISIBLE);
		getSearchParent().setVisibility(View.GONE);
		getSearchParent().setOnClickListener(view -> {
			if (getSearchEdit().getText().toString().equals(getUrl()) || getSearchEdit().getText().toString().equals("")) {
				ViewO.hideView(view);
				if (WebContainer.isIndex())
					ViewO.showView(getIndexWallpaper());
				InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
				if (inputMethodManager != null) {
					inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
				}
				SearchEnvironment.SearchAnimation(false);
			}
		});
		getTitleParent().setOnClickListener(view -> SearchEnvironment.openSearchBar(null));
		getSearchEdit().setSelectAllOnFocus(true);
		getSugRecyclerView().setVisibility(View.GONE);
		getSearchEdit().addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				
			}
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if (MRegexUtils.isURL(s.toString())) {
					getSearchButton().setText("访问");
				} else {
					getSearchButton().setText("搜索");
				}
				OkHttpUtils.get()
						.url("https://sp0.baidu.com/5a1Fazu8AA54nxGko9WTAnF6hhy/su?wd=" + s.toString())
						.build()
						.execute(new StringCallback() {
							@Override
							public void onError(Request request, Exception e) {
								
							}
							
							@Override
							public void onResponse(String response) {
								try {
									SugAdapter sugAdapter = new SugAdapter(activity);
									String source = response.substring(17, response.length() - 1);
									BaiduSug sug = GsonUtils.fromJson(source, BaiduSug.class);
									if (sug.s.size() != 0) {
										getSugRecyclerView().setVisibility(View.VISIBLE);
										sugAdapter.addTip(sug.s);
										getSugRecyclerView().setAdapter(sugAdapter);
										getSugRecyclerView().setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, true));
									} else {
										//没有搜索建议
										getSugRecyclerView().setVisibility(View.GONE);
									}
								} catch (Exception ignore) {
								}
							}
						});
			}
		});
		
		getBallWindowButton().setOnClickListener(view -> WindowsManager.loadWindows());
		getBall().setOnClickListener(new View.OnClickListener() {
			
			private int clickCount;
			
			@Override
			public void onClick(final View view) {
				clickCount++;
				final Handler handler = new Handler();
				handler.postDelayed(() -> {
					if (clickCount == 1) {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
							if (getBallCardView().getElevation() != 0) {
								if (getBallCardView().getAnimation() != null)
									if (!getBallCardView().getAnimation().hasEnded()) {
										return;
									}
								if (getBallCardView().getAlpha() == 1.0f) {
									final float targetX;
									targetX = mSharedPreferences.getFloat("balloffsetx", 0f);
									final int targetWidth = width - paddingRight;
									int initialWidth = MSharedPreferenceUtils.getSharedPreference().getString("is_double_ball","true").equals("true")
											?
											AdaptScreenUtils.pt2Px(48):MWindowsUtils.dp2px(48);
									final int width = getBallCardView().getWidth();
									Animation animation = new Animation() {
										@Override
										protected void applyTransformation(float interpolatedTime, Transformation t) {
											ViewGroup.LayoutParams lp = getBallCardView().getLayoutParams();
											if (width != initialWidth) {
												interpolatedTime = 1 - interpolatedTime;
											}
											float transX = targetX * (1 - interpolatedTime);
											getBallCardView().setTranslationX(transX);
											lp.width = initialWidth + (int) ((targetWidth - initialWidth) * interpolatedTime);
											getBallCardView().setLayoutParams(lp);
											getBallCardView().requestLayout();
											if (interpolatedTime == 1) {
												getTitleParent().setVisibility(View.VISIBLE);
												//getBallCardView().setTranslationX(0);
											} else {
												getTitleParent().setVisibility(View.GONE);
												//getBallCardView().setTranslationX(targetX);
											}
										}
										
										@Override
										public boolean willChangeBounds() {
											return true;
										}
									};
									animation.setDuration(200);
									getBallCardView().startAnimation(animation);
								} else {
									ViewO.makeViewTranslucent(getBallCardView(), 1.0f);
								}
							}
						}
					} else if (clickCount == 2) {
						WindowsManager.loadWindows();
					}
					handler.removeCallbacksAndMessages(null);
					//清空handler延时，并防内存泄漏
					clickCount = 0;//计数清零
				}, 200);//延时timeout后执行run方法中的代码
				
				
			}
		});
		getBall().setClickable(true);
		ballData.setFirstLevel(mSharedPreferences.getFloat("firstLevel", AdaptScreenUtils.pt2Px(20)));
		touchNormal = new View.OnTouchListener() {
			private float downX;
			private float downY;
			private int orientation = 'n';
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				//在触发时回去到起始坐标
				float x = event.getX();
				float y = event.getY();
				float secondLevel = mSharedPreferences.getFloat("secondLevel", AdaptScreenUtils.pt2Px(40));
				boolean isChangeGesture = !mSharedPreferences.getString("isChangeGesture", "false").equals("false");
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						//将按下时的坐标存储
						downX = x;
						downY = y;
						break;
					case MotionEvent.ACTION_MOVE:
						//获取到距离差
						float dxs = x - downX;
						float dys = y - downY;
						double moveX = Math.sqrt(dxs * dxs + dys * dys);//移动位移长度
						if (moveX > secondLevel) {
							getMainGestureColor().setBackground(activity.getResources().getDrawable(R.drawable.state_true));
						} else if (moveX > ballData.getFirstLevel()) {
							getMainGestureColor().setBackground(activity.getResources().getDrawable(R.drawable.state_false));
						} else {
							getMainGestureColor().setVisibility(View.GONE);
						}
						//防止是按下也判断
						if (moveX > ballData.getFirstLevel()) {
							getMainGestureColor().setVisibility(View.VISIBLE);
							//通过距离差判断方向
							//if(orientation=='n'){
							orientation = getOrientation(dxs, dys);
							//}else{
							switch (orientation) {
								case 'r':
									getMainGestureImage().setImageResource(R.drawable.back);
									getMainGestureImage().setRotation(isChangeGesture ? 0 : 180);
									break;
								case 'l':
									getMainGestureImage().setImageResource(R.drawable.back);
									getMainGestureImage().setRotation(isChangeGesture ? 180 : 0);
									break;
								case 't':
									getMainGestureImage().setImageResource(R.drawable.home);
									getMainGestureImage().setRotation(0);
									break;
								case 'b':
									getMainGestureImage().setImageResource(R.drawable.ic_menu);
									getMainGestureImage().setRotation(0);
									break;
								case 'c'://切换窗口Left
									getMainGestureImage().setImageResource(R.drawable.forward);
									getMainGestureImage().setRotation(isChangeGesture ? 180 : 0);
									break;
								case 'd'://切换窗口Right
									getMainGestureImage().setImageResource(R.drawable.forward);
									getMainGestureImage().setRotation(isChangeGesture ? 0 : 180);
									break;
								case 'm'://多窗口
									getMainGestureImage().setImageResource(R.drawable.ic_window);
									getMainGestureImage().setRotation(0);
									break;
								case 'q'://刷新
									getMainGestureImage().setImageResource(R.drawable.refresh);
									getMainGestureImage().setRotation(0);
									break;
							}
							
						}
						break;
					case MotionEvent.ACTION_UP:
						getMainGestureColor().setVisibility(View.GONE);
						//获取到距离差
						float dx = x - downX;
						float dy = y - downY;
						moveX = Math.sqrt(dx * dx + dy * dy);//移动位移长度
						//防止是按下也判断
						if (moveX > secondLevel) {
							//通过距离差判断方向
							LitWebView mView = WebContainer.getWebView();
							switch (orientation) {
								case 'r':
									if (!isChangeGesture)
										mView.goForward();
									else
										mView.goBack();
									break;
								case 'l':
									if (!isChangeGesture)
										mView.goBack();
									else
										mView.goForward();
									break;
								case 't':
									mView.loadUrl(MDataBaseSettingUtils.getSingleSetting(Diy.WEBPAGE));
									break;
								case 'b':
									MenuDialog.showDialog();
									break;
								case 'c'://切换窗口
									if (!isChangeGesture)
										WebContainer.changeWindow(-1);
									else
										WebContainer.changeWindow(1);
									break;
								case 'd':
									if (!isChangeGesture)
										WebContainer.changeWindow(1);
									else
										WebContainer.changeWindow(-1);
									break;
								case 'm'://多窗口
									WindowsManager.loadWindows();
									break;
								case 'q'://刷新
									mView.reload();
									break;
							}
						}
						orientation = 'n';
						break;
				}
				return true;
			}
		};
		
		//让view能够随手自由拖动
		touchMove = (v, event) -> {
			int x = (int) event.getRawX(); //触摸点相对于屏幕的横坐标
			int y = (int) event.getRawY(); //触摸点相对于屏幕的纵坐标
			if (ballData.getLastY() == 0)
				ballData.setLastY(y);
			if (ballData.getLastX() == 0)
				ballData.setLastX(x);
			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					ballData.setLastX(x);
					ballData.setLastY(y);
					break;
				case MotionEvent.ACTION_MOVE: //当手势类型为移动时
					if (isBallShrink()) {
						float deltaX = x - ballData.getLastX();
						float translationX = (int) getBallCardView().getTranslationX() + deltaX;
						getBallCardView().setTranslationX(translationX);
					}
					//重新设置此view相对父容器的偏移量
					float deltaY = y - ballData.getLastY();//两次移动的y的距离差
					float translationY = (int) getBallCardView().getTranslationY() + deltaY;
					getBallCardView().setTranslationY(translationY);
					break;
				case MotionEvent.ACTION_UP:
					ballData.setCanMove(false);
					mSharedPreferences.edit().putFloat("balloffsety", getBallCardView().getTranslationY()).apply();
					
					if (isBallShrink())
						mSharedPreferences.edit().putFloat("balloffsetx", getBallCardView().getTranslationX()).apply();
					break;
				default:
					break;
			}
			//记录上一次移动的坐标
			ballData.setLastX(x);
			ballData.setLastY(y);
			return true;
		};
		ballData.setMovable(mSharedPreferences.getString("movable", "true").equals("true"));
		// 按下时长设置
		// 移动大于20像素
		//添加你长按之后的方法
		//getDrawingXY();
		//添加你长按之后的方法
		//getDrawingXY();
		View.OnTouchListener touchIf = new View.OnTouchListener() {
			float startX, startY;
			Timer timer;
			
			@Override
			public boolean onTouch(final View v, final MotionEvent ev) {
				
				switch (ev.getAction()) {
					case MotionEvent.ACTION_DOWN:
						
						startX = ev.getX();
						startY = ev.getY();
						timer = new Timer();
						timer.schedule(new TimerTask() {
							@Override
							public void run() {
								ballData.setCanMove(true);
								touchMove.onTouch(v, ev);
							}
						}, 500); // 按下时长设置
						break;
					case MotionEvent.ACTION_MOVE:
						float dx = ev.getX();
						float dy = ev.getY();
						double moveX = Math.sqrt(dx * dx + dy * dy);//移动位移长度
						if (moveX > ballData.getFirstLevel() && timer != null) { // 移动大于20像素
							timer.cancel();
							timer = null;
						}
						if (ballData.isCanMove() && ballData.isMovable()) {
							//添加你长按之后的方法
							//getDrawingXY();
							timer = null;
							touchMove.onTouch(v, ev);
						} else if (getBallCardView().getAlpha() == 1.0f) {
							touchNormal.onTouch(v, ev);
						}
						break;
					case MotionEvent.ACTION_UP:
						if (ballData.isCanMove() && ballData.isMovable()) {
							//添加你长按之后的方法
							//getDrawingXY();
							timer = null;
							touchMove.onTouch(v, ev);
						} else if (getBallCardView().getAlpha() == 1.0f) {
							touchNormal.onTouch(v, ev);
						}
						if (timer != null) {
							timer.cancel();
							timer = null;
						}
				}
				return false;
			}
		};
		getBall().setOnTouchListener(touchIf);
		
		touchWindow = new View.OnTouchListener() {
			private float downX;
			private float downY;
			private int orientation = 'n';
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				//在触发时回去到起始坐标
				float x = event.getX();
				float y = event.getY();
				float secondLevel = mSharedPreferences.getFloat("secondLevel", AdaptScreenUtils.pt2Px(40));
				boolean isChangeGesture = !mSharedPreferences.getString("isChangeGesture_window", "false").equals("false");
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						//将按下时的坐标存储
						downX = x;
						downY = y;
						break;
					case MotionEvent.ACTION_MOVE:
						//获取到距离差
						float dxs = x - downX;
						float dys = y - downY;
						double moveX = Math.sqrt(dxs * dxs + dys * dys);//移动位移长度
						if (moveX > secondLevel) {
							getMainGestureColor().setBackground(activity.getResources().getDrawable(R.drawable.state_true));
						} else if (moveX > ballData.getFirstLevel()) {
							getMainGestureColor().setBackground(activity.getResources().getDrawable(R.drawable.state_false));
						} else {
							getMainGestureColor().setVisibility(View.GONE);
						}
						//防止是按下也判断
						if (moveX > ballData.getFirstLevel()) {
							getMainGestureColor().setVisibility(View.VISIBLE);
							//通过距离差判断方向
							//if(orientation=='n'){
							orientation = getOrientation(dxs, dys);
							//}else{
							switch (orientation) {
								case 't':
								case 'm'://上
									getMainGestureImage().setImageResource(R.drawable.delete_line);
									getMainGestureImage().setRotation(0);
									break;
								case 'b':
								case 'q':
									getMainGestureImage().setImageResource(R.drawable.ic_menu);
									getMainGestureImage().setRotation(0);
									break;
								case 'l':
								case 'c'://切换窗口Left
									getMainGestureImage().setImageResource(R.drawable.forward);
									getMainGestureImage().setRotation(isChangeGesture ? 180 : 0);
									break;
								case 'r':
								case 'd'://切换窗口Right
									getMainGestureImage().setImageResource(R.drawable.forward);
									getMainGestureImage().setRotation(isChangeGesture ? 0 : 180);
									break;
							}
							
						}
						break;
					case MotionEvent.ACTION_UP:
						getMainGestureColor().setVisibility(View.GONE);
						//获取到距离差
						float dx = x - downX;
						float dy = y - downY;
						moveX = Math.sqrt(dx * dx + dy * dy);//移动位移长度
						//防止是按下也判断
						if (moveX > secondLevel) {
							//通过距离差判断方向
							LitWebView mView = WebContainer.getWebView();
							switch (orientation) {
								case 'r':
								case 'd'://右
									if (!isChangeGesture)
										WebContainer.changeWindow(1);
									else
										WebContainer.changeWindow(-1);
									break;
								case 'l':
								case 'c'://左
									if (!isChangeGesture)
									WebContainer.changeWindow(-1);
									else
									WebContainer.changeWindow(1);
									break;
								case 't':
								case 'm'://上
									WebContainer.removeWindow(getWindowId());
									break;
								case 'b':
								case 'q'://下
									String domain = WebsiteUtils.getDomain(WebContainer.getUrl());
									WebsiteSetting websiteSetting = WebsiteUtils.getWebsiteSetting(activity,domain);
									Chiper.copy(
									  "id:"+websiteSetting.id+";\n"
									  "site:"+websiteSetting.site+";\n"
									  "js:"+websiteSetting.js+";\n"
									  "ua:"+websiteSetting.ua+";\n"
									  "app:"+websiteSetting.app+";\n"
									  "state:"+websiteSetting.state+";\n"
									  "no_history:"+websiteSetting.no_history+";\n"
									  "no_picture:"+websiteSetting.no_picture+";\n"
									);
									if (websiteSetting==null) {
									  MToastUtils.makeText("Create New Website Setting").show();
										websiteSetting = new WebsiteSetting();
										websiteSetting.site = domain;
										websiteSetting.state = false;
									}
									new MWebsiteSettingDialog.Builder(activity).setItems(websiteSetting).create().show();
									break;
							}
						}
						orientation = 'n';
						break;
				}
				return false;
			}
		};
		getBallWindowButton().setOnTouchListener(touchWindow);
		getBallWindowButton().setClickable(true);
		getBallWindowButton().setOnLongClickListener((view)->{
			WebContainer.createWindow(null,true);
			return true;
		});
	}
	
	
	private static boolean isBallShrink() {//是否已收缩小球
		int initialWidth = MSharedPreferenceUtils.getSharedPreference().getString("is_double_ball","true").equals("true")
				?
				AdaptScreenUtils.pt2Px(48):MWindowsUtils.dp2px(48);
		final int width = getBallCardView().getWidth();
		return width == initialWidth;//不等于即表示已展开
	}
	
	
	/**
	 * 根据距离差判断 滑动方向
	 *
	 * @param dx X轴的距离差
	 * @param dy Y轴的距离差
	 * @return 滑动的方向
	 */
	private static int getOrientation(float dx, float dy) {
		//一级灵敏度，小球滑动距离超过改值显示灰色图标
		float firstLevel = mSharedPreferences.getFloat("firstLevel", AdaptScreenUtils.pt2Px(20));
		//一级灵敏度，小球滑动距离超过改值显示长滑图标
		float thirdLevel = mSharedPreferences.getFloat("thirdLevel", AdaptScreenUtils.pt2Px(90));
		double moveX = Math.sqrt(dx * dx + dy * dy);//移动位移长度
		boolean isX = Math.abs(dx) > Math.abs(dy);
		if (moveX > thirdLevel) {//长划
			if (isX) {
				//X轴移动
				if (dx > 0)//切换窗口(右)
					return 'd';
				if (dx < 0)//切换窗口(左)
					return 'c';
				return 'n';
			} else {
				//Y轴移动
				if (dy > 0)//刷新
					return 'q';
				else if (dy < 0)//多窗口
					return 'm';
				return 'n';
			}
		} else if (moveX > firstLevel) {
			if (isX) {
				//X轴移动
				if (dx > 0)//向右
					return 'r';
				if (dx < 0)//向左
					return 'l';
				return 'n';
			} else {
				//Y轴移动
				if (dy > 0)//下
					return 'b';
				else if (dy < -0)//上
					return 't';
				return 'n';
			}
		}
		return 'n';
	}
	
	
	@SuppressLint("SetTextI18n")
	private static void loadBallStyle() {
		int ballType = Integer.parseInt(mSharedPreferences.getString("is_apply_ball", "0"));
		//Typeface customFont = Typeface.createFromAsset(getAssets(), "font/a.ttf");
		//mBallText.setTypeface(customFont);
		switch (ballType) {
			case 1://多窗口
				getBallImage().setVisibility(View.GONE);
				getBallText().setVisibility(View.VISIBLE);
				getBallText().setText(WebContainer.getWindowCount() + "");
				break;
			case 2:
				try {
					new Thread(() -> activity.runOnUiThread(() -> {
						Bitmap wallpaper;
						try {
							wallpaper = BitmapFactory.decodeStream(activity.openFileInput("ball_icon.png"));
							if (wallpaper != null) {
								getBallImage().setVisibility(View.VISIBLE);
								getBallText().setVisibility(View.GONE);
								getBallImage().setImageBitmap(wallpaper);
								getBallImage().setPadding(0, 0, 0, 0);
							} else {
								useDefaultLogo();
							}
						} catch (FileNotFoundException e) {
							useDefaultLogo();
						}
					})).start();
				} catch (Exception e) {
					useDefaultLogo();
				}
				break;
			default:
				useDefaultLogo();
				break;
		}
	}
	
	
	private static void useDefaultLogo() {
		getBallImage().setVisibility(View.VISIBLE);
		getBallText().setVisibility(View.GONE);
		getBallImage().setImageResource(R.drawable.dialog);
		int pd = MWindowsUtils.dp2px(16);
		getBallImage().setPadding(pd, pd, pd, pd);
	}
	
	
	public static void resetBall() {
		mSharedPreferences.edit().putFloat("balloffsety", 0).apply();
		mSharedPreferences.edit().putFloat("balloffsetx", 0).apply();
		getBallCardView().setTranslationY(0f);
		getBallCardView().setTranslationX(0f);
		MToastUtils.makeText("小球已复原，可能需要重启APP", MToastUtils.LENGTH_SHORT).show();
	}
	
	public static void start() {
		//启动函数
		initBall();
		loadBallStyle();
		mSharedPreferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
	}
}

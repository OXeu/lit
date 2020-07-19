package com.zmide.lit.main;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zmide.lit.R;
import com.zmide.lit.ui.MainActivity;
import com.zmide.lit.util.MSharedPreferenceUtils;

public class firstGuide {
	private static MainActivity activity;
	private static PopupWindow pop;
	
	public static void init(MainActivity activityTemp) {
		if (activity == null)
			activity = activityTemp;
	}
	
	public static void showGuide() {
		View mBall = MainViewBindUtils.getBall();
		mBall.setBackgroundResource(R.color.accentColor);
		initPop("小球在这里");
		pop.showAsDropDown(mBall);
		pop.setOnDismissListener(() -> {
			mBall.setBackgroundResource(R.drawable.ripple_circle);
			initPop("向上滑动返回主页\n向下滑动打开菜单\n左右滑动前进/后退\n向上长划/双击小球打开多窗口\n左右长划返回上一个窗口\n非默认主页点击展开/收缩标题栏");
			pop.showAtLocation(MainViewBindUtils.getMainBallParent(), Gravity.CENTER, 0, 200);
			pop.setOnDismissListener(() -> MSharedPreferenceUtils.getSharedPreference().edit().putBoolean("isfirst", false).apply());
		});
	}
	
	
	/**
	 * 创建PopupWindow
	 */
	private static void initPop(String text) {
		//加载布局
		View contentView = View.inflate(activity, R.layout.text, null);
		//创建pop窗口
		//1.contentView 内部布局
		//2.pop窗口的宽度与高度一般设置成 WRAP_CONTENT
		//3.最后一个参数 代表是否聚集
		pop = new PopupWindow(contentView,
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, true);
		TextView mPopText = contentView.findViewById(R.id.textText);
		mPopText.setText(text);
		//在此pop的区域 外点击关闭此窗口
		pop.setOutsideTouchable(true);
		//设置一个背景
		//pop.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.ic_launcher));
		//设置一个空背景
		Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), 100);
		pop.setBackgroundDrawable(new BitmapDrawable(activity.getResources(), bitmap));
	}
}

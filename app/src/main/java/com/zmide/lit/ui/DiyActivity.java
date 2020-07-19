package com.zmide.lit.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zmide.lit.R;
import com.zmide.lit.adapter.DiyAdapter;
import com.zmide.lit.base.BaseActivity;
import com.zmide.lit.object.Diy;
import com.zmide.lit.util.DBC;
import com.zmide.lit.util.MWindowsUtils;

import java.util.ArrayList;


public class DiyActivity extends BaseActivity implements DiyAdapter.DiyViewOperate {
	
	private RecyclerView mDiyRecyclerView;
	private DiyAdapter mAdapter;
	private PopupWindow pop;
	private TextView mPopDelete;
	private TextView mDiyEmpty;
	private int type;
	private ArrayList<Diy> Diys;
	private SharedPreferences mSharedPreferences;
	private TextView mPopEdit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_diy);
		type = getIntent().getIntExtra("type", Diy.WEBPAGE);
		initView();
		loadDiy();
		mSharedPreferences = getSharedPreferences("setting", Context.MODE_PRIVATE);
	}
	
	private void insertDefault() {
		switch (type) {
			case Diy.PLUGIN:
				
				break;
			case Diy.SEARCH_ENGINE:
				DBC.getInstance(this).addDiy("百度", "", "https://m.baidu.com/from=1022560v/s?word=%s", "", Diy.SEARCH_ENGINE, false);
				DBC.getInstance(this).addDiy("必应", "", "https://cn.bing.com/search?q=%s", "", Diy.SEARCH_ENGINE, false);
				DBC.getInstance(this).addDiy("秘迹", "", "https://m.mijisou.com/?q=%s", "", Diy.SEARCH_ENGINE, false);
				DBC.getInstance(this).addDiy("Google", "", "https://www.google.com/search?q=%s", "", Diy.SEARCH_ENGINE, false);
				DBC.getInstance(this).addDiy("Magi", "", "https://magi.com/search?q=%s", "", Diy.SEARCH_ENGINE, false);
				break;
			case Diy.UA:
				DBC.getInstance(this).addDiy("默认", "默认UA", "", "", Diy.UA, false);
				DBC.getInstance(this).addDiy("简单搜索", "去除百度广告", "Mozilla/5.0 (Linux; Android 10; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.23.184.0 Mobile Safari/537.36 Edge/18.18362 SearchCraft/2.8.2 Baidu;P1 10.0", "http(s|)://(\\w*\\.|)baidu.com(^\\s*)", Diy.UA, false);
				DBC.getInstance(this).addDiy("微信", "可以访问部分仅限微信打开的网站", "Mozilla/5.0 (Linux; Android 5.0; SM-N9100 Build/LRX21V) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/37.0.0.0 Mobile Safari/537.36 MicroMessenger/6.0.2.56_r958800.520 NetType/WIFI", "http(s|)://mp.qq.com(^\\s*)", Diy.UA, false);
				DBC.getInstance(this).addDiy("电脑(Chrome)", "Chrome Windows UA", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.87 Safari/537.36", "http(s|)://(\\w*\\.|)baidu.com(^\\s*)", Diy.UA, false);
				break;
			case Diy.WEBPAGE:
				DBC.getInstance(this).addDiy("默认主页", "简洁默认主页", "file:///android_asset/index.html", "", Diy.WEBPAGE, true);
				DBC.getInstance(this).addDiy("网页版主页", "经典网页主页", "file:///android_asset/index2.html", "", Diy.WEBPAGE, false);
				break;
		}
		loadDiy();
	}
	
	private void initView() {
		ImageView mDiyBack = findViewById(R.id.diyBack);
		mDiyRecyclerView = findViewById(R.id.diyRecyclerView);
		mDiyEmpty = findViewById(R.id.diyEmpty);
		TextView mDiyTitle = findViewById(R.id.diyTitle);
		ImageView mDiyAdd = findViewById(R.id.diyAdd);
		TextView mDiyDefault = findViewById(R.id.diyDefault);
		mDiyEmpty.setVisibility(View.GONE);
		mDiyDefault.setOnClickListener(view -> insertDefault());
		mDiyBack.setOnClickListener(view -> finish());
		String title = "";
		switch (type) {
			case Diy.PLUGIN:
				title = "插件";
				mDiyDefault.setVisibility(View.GONE);
				break;
			case Diy.SEARCH_ENGINE:
				title = "搜索引擎";
				break;
			case Diy.UA:
				title = "UA";
				break;
			case Diy.WEBPAGE:
				title = "主页";
				break;
		}
		mDiyTitle.setText(title);
		mDiyAdd.setOnClickListener(view -> {
			Intent i = new Intent(DiyActivity.this, DiyNewActivity.class);
			i.putExtra("type", type);
			startActivity(i);
		});
	}
	
	private void loadDiy() {
		Diys = DBC.getInstance(this).getDiys(type, false);
		onSizeChanged(Diys.size());
		LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);//这里我们使用默认的线性布局管理器,将其设为垂直显示
		mDiyRecyclerView.setLayoutManager(mLayoutManager);//设置布局管理器
		mAdapter = new DiyAdapter(this, Diys, type);//实例化适配器
		mDiyRecyclerView.setAdapter(mAdapter);//设置适配器
	}
	
	/**
	 * 创建PopupWindow
	 */
	public void initPop() {
		//加载布局
		View contentView = View.inflate(this, R.layout.diy_tag_pop, null);
		//创建pop窗口
		//1.contentView 内部布局
		//2.pop窗口的宽度与高度一般设置成 WRAP_CONTENT
		//3.最后一个参数 代表是否聚集
		pop = new PopupWindow(contentView,
				MWindowsUtils.dp2px(120),
				ViewGroup.LayoutParams.WRAP_CONTENT, true);
		mPopDelete = contentView.findViewById(R.id.popDelete);
		mPopEdit = contentView.findViewById(R.id.popEdit);
		//在此pop的区域 外点击关闭此窗口
		pop.setOutsideTouchable(true);
		//设置一个背景
		//pop.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_launcher));
		//设置一个空背景
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), 100);
		pop.setBackgroundDrawable(new BitmapDrawable(getResources(), bitmap));
		pop.setOnDismissListener(() -> mDiyRecyclerView.setLayoutFrozen(false));
		
	}
	
	
	@SuppressLint("RtlHardcoded")
	@Override
	public void DiyLongClickListener(View view, MotionEvent ev, final Diy tag) {
		initPop();
		mPopEdit.setOnClickListener(view1 -> {
			Intent i = new Intent(DiyActivity.this, DiyNewActivity.class);
			i.putExtra("title", tag.title);
			i.putExtra("description", tag.description);
			i.putExtra("url", tag.value);
			i.putExtra("id", tag.id);
			i.putExtra("type", type);
			i.putExtra("extra", tag.extra);
			startActivity(i);
			pop.dismiss();
		});
		mPopDelete.setOnClickListener(view12 -> {
			DBC.getInstance(view12.getContext()).deleteDiys(tag.id);
			int position = mAdapter.getPosition(tag);
			mAdapter.remove(position);
			mAdapter.notifyItemRemoved(position);
			pop.dismiss();
		});
		int x = (int) ev.getRawX();
		int y = (int) ev.getRawY();
		int statusBarHeight = MWindowsUtils.getStatusBarHeight();
		int height = MWindowsUtils.getHeight();
		int width = MWindowsUtils.getWidth();
		int xOffset;
		if (x < width / 2) {
			//如果点击的位置是左边，则显示到点击处右边（当前列表是两列的网格布局）
			xOffset = x;
			
		} else {
			xOffset = x - MWindowsUtils.dp2px(120);
		}
		int yOffset;
		if (y < (height - statusBarHeight) / 2) {
			//如果点击的位置是左边，则显示到点击处右边（当前列表是两列的网格布局）
			yOffset = y;
		} else {
			yOffset = y - view.getMeasuredHeight() - statusBarHeight;
		}
		//如果点击的位置是右边，则显示到点击处左边（当前列表是两列的网格布局）
		pop.showAtLocation(view,
				Gravity.TOP | Gravity.LEFT,
				xOffset,
				yOffset);
		mDiyRecyclerView.setLayoutFrozen(true);
		pop.showAsDropDown(view);
	}
	
	@Override
	public void onSizeChanged(int size) {
		if (size == 0) {
			mDiyEmpty.setVisibility(View.VISIBLE);
			mDiyRecyclerView.setVisibility(View.GONE);
		} else {
			mDiyEmpty.setVisibility(View.GONE);
			mDiyRecyclerView.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		loadDiy();
	}
	
	
	@Override
	public void onDataChanged(ArrayList<Diy> diys) {
		for (int i = 0; i < diys.size(); i++) {
			if (Diys.size() == i) {
				mAdapter.notifyItemRangeInserted(i, diys.size() - i);
				break;
			}
			if (Diys.get(i) != diys.get(i)) {
				if (mDiyRecyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE && (!mDiyRecyclerView.isComputingLayout())) {
					mAdapter.notifyItemChanged(i);
				}
			}
		}
		this.Diys = diys;
		mSharedPreferences.edit().putBoolean("setc", !mSharedPreferences.getBoolean("setc", true)).apply();
	}
}

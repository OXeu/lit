package com.zmide.lit.ui;

import android.annotation.SuppressLint;
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
import com.zmide.lit.adapter.TagAdapter;
import com.zmide.lit.base.BaseActivity;
import com.zmide.lit.interfaces.TagViewOperate;
import com.zmide.lit.object.History;
import com.zmide.lit.object.Tag;
import com.zmide.lit.util.DBC;
import com.zmide.lit.util.MDialogUtils;
import com.zmide.lit.util.MWindowsUtils;
import com.zmide.lit.util.TimeUtil;

import java.util.ArrayList;

public class HistoryActivity extends BaseActivity implements TagViewOperate {
	
	private RecyclerView mHistoryRecyclerView;
	private TagAdapter mAdapter;
	private PopupWindow pop;
	private TextView mPopDelete;
	private TextView mHistoryEmpty;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history);
		initView();
		loadHistory();
	}
	
	private void initView() {
		ImageView mHistoryBack = findViewById(R.id.historyBack);
		ImageView mHistoryClear = findViewById(R.id.historyClear);
		mHistoryRecyclerView = findViewById(R.id.historyRecyclerView);
		mHistoryEmpty = findViewById(R.id.historyEmpty);
		mHistoryEmpty.setVisibility(View.GONE);
		mHistoryBack.setOnClickListener(view -> finish());
		mHistoryClear.setOnClickListener((view) -> new MDialogUtils.Builder(view.getContext())
				.setTitle("清空历史记录")
				.setMessage("确认清除所有历史记录吗？这将无法进行回溯")
				.setNegativeButton("放弃", (dialog, which) -> dialog.cancel())
				.setPositiveButton("清空", (dialog, which) -> {
					dialog.cancel();
					DBC.getInstance(view.getContext()).deleteAllHistory();
					loadHistory();
				})
				.create().show());
	}
	
	private void loadHistory() {
		ArrayList<Tag> historys = new ArrayList<>();
		for (History history : DBC.getInstance(this).getHistorys()) {
			Tag tag = new Tag();
			tag.canForward = false;
			tag.title = history.name;
			tag.remark = history.url;
			tag.id = history.id;
			tag.icon = history.icon;
			tag.time = TimeUtil.getTimeFormatText(history.time);
			historys.add(tag);
		}
		onSizeChanged(historys.size());
		LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);//这里我们使用默认的线性布局管理器,将其设为垂直显示
		mHistoryRecyclerView.setLayoutManager(mLayoutManager);//设置布局管理器
		mAdapter = new TagAdapter(this, historys);//实例化适配器
		mHistoryRecyclerView.setAdapter(mAdapter);//设置适配器
	}
	
	@Override
	public void onLoadChildIndex(String child) {
	
	}
	
	/**
	 * 创建PopupWindow
	 */
	public void initPop() {
		//加载布局
		View contentView = View.inflate(this, R.layout.history_tag_pop, null);
		//创建pop窗口
		//1.contentView 内部布局
		//2.pop窗口的宽度与高度一般设置成 WRAP_CONTENT
		//3.最后一个参数 代表是否聚集
		pop = new PopupWindow(contentView,
				MWindowsUtils.dp2px(120),
				ViewGroup.LayoutParams.WRAP_CONTENT, true);
		mPopDelete = contentView.findViewById(R.id.popDelete);
		//在此pop的区域 外点击关闭此窗口
		pop.setOutsideTouchable(true);
		//设置一个背景
		//pop.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_launcher));
		//设置一个空背景
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), 100);
		pop.setBackgroundDrawable(new BitmapDrawable(getResources(), bitmap));
		pop.setOnDismissListener(() -> mHistoryRecyclerView.setLayoutFrozen(false));
	}
	
	
	@SuppressLint("RtlHardcoded")
	@Override
	public void TagLongClickListener(View view, MotionEvent ev, final Tag tag) {
		initPop();
		mPopDelete.setOnClickListener(view1 -> {
			DBC.getInstance(view1.getContext()).deleteHistory(tag.id);
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
		mHistoryRecyclerView.setLayoutFrozen(true);
	}
	
	@Override
	public void onSizeChanged(int size) {
		if (size == 0) {
			mHistoryEmpty.setVisibility(View.VISIBLE);
			mHistoryRecyclerView.setVisibility(View.GONE);
		} else {
			mHistoryEmpty.setVisibility(View.GONE);
			mHistoryRecyclerView.setVisibility(View.VISIBLE);
		}
	}
	
}

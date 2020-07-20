package com.zmide.lit.main;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zmide.lit.R;
import com.zmide.lit.adapter.WindowsAdapter;
import com.zmide.lit.object.GMap;
import com.zmide.lit.skin.SkinManager;
import com.zmide.lit.ui.MainActivity;

import static com.zmide.lit.util.MDataBaseSettingUtils.WebIndex;

public class WindowsManager {
	private static MainActivity activity;
	private static PopupWindow pop;
	private static RecyclerView mPopRv;
	private static WindowsAdapter mAdapter;//实例化适配器
	@SuppressLint("StaticFieldLeak")
	private static LinearLayout mPopAdd,mPopCancel;
	
	public static void init(MainActivity mainActivity) {
		if (activity == null) {
			activity = mainActivity;
			mAdapter = new WindowsAdapter(activity,WebContainer.getAllWindows());
		}
		initWindows();
	}
	
	
	private static void initWindows() {
		initWindowsPop();
		LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity);//这里我们使用默认的线性布局管理器,将其设为垂直显示
		mPopRv.setLayoutManager(mLayoutManager);//设置布局管理器
		mPopRv.setAdapter(mAdapter);//设置适配器
		mPopAdd.setOnClickListener(view -> {
			WebContainer.createWindow(null,true);
			pop.dismiss();
		});
		mPopCancel.setOnClickListener(view -> pop.dismiss());
	}
	
	
	private static void initWindowsPop() {
		
		/*
		 * 创建PopupWindow
		 */
		//加载布局
		View contentView = View.inflate(activity, R.layout.windows_layout, null);
		//创建pop窗口
		//1.contentView 内部布局
		//2.pop窗口的宽度与高度一般设置成 WRAP_CONTENT
		//3.最后一个参数 代表是否聚集
		pop = new PopupWindow(contentView,
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, true);
		mPopRv = contentView.findViewById(R.id.mainWindowsRecyclerView);
		mPopAdd = contentView.findViewById(R.id.mainWindowAdd);
		mPopCancel = contentView.findViewById(R.id.mainWindowCancel);
		RelativeLayout mPopParent = contentView.findViewById(R.id.mainWindowsParents);
		mPopParent.setBackground(SkinManager.getInstance().getDrawable(R.drawable.ripple_5));
		//在此pop的区域 外点击关闭此窗口
		pop.setOutsideTouchable(true);
		//设置一个背景
		//pop.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_launcher));
		//设置一个空背景
		Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), 100);
		pop.setBackgroundDrawable(new BitmapDrawable(activity.getResources(), bitmap));
	}
	
	
	public static void loadWindows() {
		mAdapter.updateWeb(WebContainer.getAllWindows());
		mAdapter.notifyDataSetChanged();
		new ItemTouchHelper(new ItemTouchHelper.Callback() {
			private RecyclerView.ViewHolder vh;
			
			@Override
			public boolean isItemViewSwipeEnabled() {
				return true;
			}
			
			@Override
			public boolean isLongPressDragEnabled() {
				return false;
			}
			
			@Override
			public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder
					viewHolder) {
				// 拖拽的标记，这里允许上下左右四个方向
				int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT |
						ItemTouchHelper.RIGHT;
				// 滑动的标记，这里允许左右滑动
				int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
				return makeMovementFlags(dragFlags, swipeFlags);
			}
			
			/*
			 这个方法会在某个Item被拖动和移动的时候回调，这里我们用来播放动画，当viewHolder不为空时为选中状态
			 否则为释放状态
			 */
			@Override
			public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
				super.onSelectedChanged(viewHolder, actionState);
				if (viewHolder != null) {
					vh = viewHolder;
					pickUpAnimation(viewHolder.itemView);
				} else {
					if (vh != null) {
						putDownAnimation(vh.itemView);
					}
				}
			}
			
			void pickUpAnimation(View view) {
				ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationZ", 0f, 8f);
				animator.setInterpolator(new DecelerateInterpolator());
				animator.setDuration(300);
				animator.start();
			}
			
			void putDownAnimation(View view) {
				ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationZ", 8f, 0f);
				animator.setInterpolator(new DecelerateInterpolator());
				animator.setDuration(300);
				animator.start();
			}
			
			@Override
			public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
								  @NonNull RecyclerView.ViewHolder target) {
				// 移动时更改列表中对应的位置并返回true
				return false;
			}
			
			/*
			 当onMove返回true时调用
			 */
			@Override
			public void onMoved(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, int
					fromPos, @NonNull RecyclerView.ViewHolder target, int toPos, int x, int y) {
				super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
				// 移动完成后刷新列表
				mAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target
						.getAdapterPosition());
			}
			
			@Override
			public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
				// 刷新列表
				WebContainer.removeWindow(viewHolder.getAdapterPosition());
				mAdapter.updateWeb(WebContainer.getAllWindows());
				mAdapter.notifyDataSetChanged();
			}
			
		}).attachToRecyclerView(mPopRv);
		//float height = mCardview.getTranslationY()*-2;//将Y轴偏移近似转换为距离底部高度
		/*if (height > Screen.getHeight()){
			//偏移量过大，已到屏幕上半部分
			pop.showAsDropDown(mCardview,0,Screen.dp2px(16),Gravity.CENTER_HORIZONTAL);
		}else{
			pop.showAsDropDown(mCardview,0,Screen.dp2px(16), Gravity.CENTER_HORIZONTAL|Gravity.TOP);
		}*/
		pop.showAtLocation(MainViewBindUtils.getBallCardView(), Gravity.CENTER_HORIZONTAL, 0, 0);
		//pop.showAsDropDown(mCardview,0,100);
	}
	
	public static void hideWindows() {
		if (pop != null)
			pop.dismiss();
	}
	
	public static boolean isWindowsShowing() {
		return pop.isShowing();
	}
}

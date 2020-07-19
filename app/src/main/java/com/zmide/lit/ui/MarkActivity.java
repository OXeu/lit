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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.zmide.lit.R;
import com.zmide.lit.adapter.TagAdapter;
import com.zmide.lit.base.BaseActivity;
import com.zmide.lit.interfaces.TagViewOperate;
import com.zmide.lit.object.Mark;
import com.zmide.lit.object.Parent;
import com.zmide.lit.object.Tag;
import com.zmide.lit.util.DBC;
import com.zmide.lit.util.MDialogUtils;
import com.zmide.lit.util.MWindowsUtils;
import com.zmide.lit.util.MarkEditDialog;

import java.util.ArrayList;
import java.util.Objects;

public class MarkActivity extends BaseActivity implements TagViewOperate {
	
	private RecyclerView mMarkRecyclerView;
	private TagAdapter mAdapter;
	private PopupWindow pop;
	private TextView mPopDelete;
	private TextView mMarkEmpty, mMarkTitle;
	private String parentId = "0";
	private SwipeRefreshLayout mSwipe;
	private TextView mPopEdit;
	
	public MarkActivity() {
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mark);
		initView();
		loadMark("0");
	}
	
	private void initView() {
		ImageView mMarkBack = findViewById(R.id.markBack);
		mMarkTitle = findViewById(R.id.markTitle);
		mMarkRecyclerView = findViewById(R.id.markRecyclerView);
		mMarkEmpty = findViewById(R.id.markEmpty);
		mMarkEmpty.setVisibility(View.GONE);
		ImageView mMarkAdd = findViewById(R.id.markAdd);
		mSwipe = findViewById(R.id.markSwipe);
		mSwipe.setColorSchemeResources(R.color.accentColor, R.color.accentColor2);
		mSwipe.setOnRefreshListener(() -> loadMark(parentId));
		/*mMarkAdd.setOnClickListener(v -> {
			FolderId = parentId;
			Dialog dialog = new Dialog(this);
			View layout = LayoutInflater.from(this).inflate(R.layout.dialog_mark_editor, null);
			dialog.setContentView(layout);
			TextView ok = layout.findViewById(R.id.editDialogOk);
			TextView cancel = layout.findViewById(R.id.editDialogCancel);
			TextView title = layout.findViewById(R.id.editDialogTitle);
			Editor titleEditor = layout.findViewById(R.id.editDialogTitleEditor);
			Editor urlEditor = layout.findViewById(R.id.editDialogUrl);
			TextView indexChooser = layout.findViewById(R.id.editDialogIndex);
			title.setText(R.string.mark_add);
			String indexName;
			if (Objects.equals(parentId, "0"))
				indexName = "根目录";
			else
				indexName = DBC.getInstance(this).getIndexName(parentId);
			if (Objects.equals(indexName, "") || indexName == null)
				indexChooser.setText(R.string.root_index);
			else
				indexChooser.setText(indexName);
			indexChooser.setOnClickListener((view) -> {
				
				MDialogUtils.Builder dialogs = new MDialogUtils.Builder(view.getContext());
				@SuppressLint("InflateParams") View dv = LayoutInflater.from(view.getContext()).inflate(R.layout.rv_button, null);
				rv = dv.findViewById(R.id.recyclerView);
				mLayoutManager = new LinearLayoutManager(v.getContext());//这里我们使用默认的线性布局管理器,将其设为垂直显示
				mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
				rv.setLayoutManager(mLayoutManager);//设置布局管理器
				FolderAdapter adapter = new FolderAdapter(view.getContext(), DBC.getInstance(view.getContext()).getParents(parentId));
				rv.setAdapter(adapter);
				ImageView back = dv.findViewById(R.id.rvBack);
				back.setOnClickListener((view1) -> {
					if (Objects.equals(FolderId, "0") || FolderId == null)
						toast("已经是根目录了");
					else {
						String parent = DBC.getInstance(MarkActivity.this).getParentByFolder(FolderId) + "";
						onLoadFoldersIndex(parent);
						FolderId = parent;
					}
				});
				tt = dv.findViewById(R.id.rvTitle);
				tt.setText(indexChooser.getText());
				Dialog dialog1 = dialogs.setContentView(dv).create();
				TextView cancel2 = dv.findViewById(R.id.rvCancel);
				cancel2.setOnClickListener((view1) -> {
					dialog1.cancel();
				});
				cancel2.setText("取消");
				TextView ok2 = dv.findViewById(R.id.rvOk);
				ok2.setText("选择");
				ok2.setOnClickListener((view1) -> {
					indexChooser.setText(DBC.getInstance(view.getContext()).getIndexName(FolderId));
					dialog1.cancel();
				});
				dialog1.show();
			});
			ok.setOnClickListener((view) -> {
				String name = titleEditor.getText().toString();
				//String url = urlEditor.getText().toString();
				if (name.equals(""))//||url.equals(""))
				{
					toast("请输入标题");
				} else {
					if (!DBC.getInstance(this).addParent(name, FolderId))
						MToastUtils.makeText(this, "添加失败，目录已存在", MToastUtils.LENGTH_SHORT).show();
					loadMark(FolderId);
					dialog.cancel();
				}
			});
			cancel.setOnClickListener((view) -> {
				dialog.cancel();
			});
			WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
			lp.gravity = Gravity.BOTTOM;
			lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
			lp.width = WindowManager.LayoutParams.MATCH_PARENT;
			lp.dimAmount = 0.2f;
			dialog.getWindow().setAttributes(lp);
			//设置该属性，dialog可以铺满屏幕
			dialog.getWindow().setBackgroundDrawable(null);
			View holder = layout;
			FrameLayout.LayoutParams lps = (FrameLayout.LayoutParams) holder.getLayoutParams();
			lps.setMargins(30, 30, 30, 30);
			holder.setLayoutParams(lps);
			dialog.show();
		});*/
		mMarkAdd.setOnClickListener((view) -> new MarkEditDialog(this).createFolderEditDialog(this, parentId, "", false));
		mMarkBack.setOnClickListener(view -> {
			if (Objects.equals(parentId, "0") || parentId == null)
				finish();
			else {
				String parent = DBC.getInstance(MarkActivity.this).getParentByFolder(parentId) + "";
				loadMark(parent);
				parentId = parent;
			}
		});
	}
	/*
	@Override
	public void onLoadFoldersIndex(String id) {
		FolderAdapter adapter = new FolderAdapter(this, DBC.getInstance(this).getParents(id));
		rv.setAdapter(adapter);
		FolderId = id;
		String indexName;
		if (Objects.equals(FolderId, "0"))
			indexName = "根目录";
		else
			indexName = DBC.getInstance(this).getIndexName(FolderId);
		if (Objects.equals(indexName, "") || indexName == null)
			tt.setText(R.string.root_index);
		else
			tt.setText(indexName);
	}*/
	
	@Override
	public void onBackPressed() {
		if (Objects.equals(parentId, "0") || parentId == null)
			finish();
		else {
			String parent = DBC.getInstance(MarkActivity.this).getParentByFolder(parentId) + "";
			loadMark(parent);
			parentId = parent;
		}
	}
	
	private void loadMark(String id) {
		parentId = id;
		if (Objects.equals(id, "0")) {
			mMarkTitle.setText("书签");
		} else {
			String title = DBC.getInstance(this).getIndexName(id);
			if (Objects.equals(title, "") || title == null)
				mMarkTitle.setText("书签");
			else
				mMarkTitle.setText(title);
		}
		ArrayList<Tag> marks = new ArrayList<>();
		for (Parent parent : DBC.getInstance(this).getParents(id)) {
			Tag tag = new Tag();
			tag.canForward = true;
			tag.title = parent.name;
			tag.remark = "";
			tag.id = parent.id;
			marks.add(tag);
		}
		for (Mark mark : DBC.getInstance(this).getMarks(id)) {
			Tag tag = new Tag();
			tag.canForward = false;
			tag.title = mark.name;
			tag.remark = mark.url;
			tag.id = mark.id;
			tag.icon = mark.icon;
			marks.add(tag);
		}
		onSizeChanged(marks.size());
		LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);//这里我们使用默认的线性布局管理器,将其设为垂直显示
		mMarkRecyclerView.setLayoutManager(mLayoutManager);//设置布局管理器
		mAdapter = new TagAdapter(this, marks);//实例化适配器
		mMarkRecyclerView.setAdapter(mAdapter);//设置适配器
		mSwipe.setRefreshing(false);
	}
	
	/**
	 * 创建PopupWindow
	 */
	public void initPop() {
		//加载布局
		View contentView = View.inflate(this, R.layout.mark_tag_pop, null);
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
		pop.setOnDismissListener(() -> mMarkRecyclerView.setLayoutFrozen(false));
	}
	
	@Override
	public void onLoadChildIndex(String child) {
		this.parentId = child;
		loadMark(child);
	}
	
	/*{
		pop.dismiss();
		FolderId = parentId;
		Dialog dialog = new Dialog(this);
		View layout = LayoutInflater.from(this).inflate(R.layout.dialog_mark_editor, null);
		dialog.setContentView(layout);
		TextView ok = layout.findViewById(R.id.editDialogOk);
		TextView cancel = layout.findViewById(R.id.editDialogCancel);
		TextView title = layout.findViewById(R.id.editDialogTitle);
		Editor titleEditor = layout.findViewById(R.id.editDialogTitleEditor);
		titleEditor.setText(tag.title);
		Editor urlEditor = layout.findViewById(R.id.editDialogUrl);
		urlEditor.setVisibility(View.GONE);
		TextView indexChooser = layout.findViewById(R.id.editDialogIndex);
		title.setText(R.string.mark_add);
		String indexName;
		if (Objects.equals(parentId, "0"))
			indexName = "根目录";
		else
			indexName = DBC.getInstance(this).getIndexName(parentId);
		if (Objects.equals(indexName, "") || indexName == null)
			indexChooser.setText(R.string.root_index);
		else
			indexChooser.setText(indexName);
		indexChooser.setOnClickListener((view2) -> {
			
			MDialogUtils.Builder dialogs = new MDialogUtils.Builder(view.getContext());
			@SuppressLint("InflateParams") View dv = LayoutInflater.from(view.getContext()).inflate(R.layout.rv_button, null);
			rv = dv.findViewById(R.id.recyclerView);
			mLayoutManager = new LinearLayoutManager(v.getContext());//这里我们使用默认的线性布局管理器,将其设为垂直显示
			mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
			rv.setLayoutManager(mLayoutManager);//设置布局管理器
			FolderAdapter adapter = new FolderAdapter(view.getContext(), DBC.getInstance(view.getContext()).getParents(parentId));
			rv.setAdapter(adapter);
			ImageView back = dv.findViewById(R.id.rvBack);
			back.setOnClickListener((view1) -> {
				if (Objects.equals(FolderId, "0") || FolderId == null)
					toast("已经是根目录了");
				else {
					String parent = DBC.getInstance(MarkActivity.this).getParentByFolder(FolderId) + "";
					onLoadFoldersIndex(parent);
					FolderId = parent;
				}
			});
			tt = dv.findViewById(R.id.rvTitle);
			tt.setText(indexChooser.getText());
			Dialog dialog1 = dialogs.setContentView(dv).create();
			TextView cancel2 = dv.findViewById(R.id.rvCancel);
			cancel2.setOnClickListener((view1) -> {
				dialog1.cancel();
			});
			cancel2.setText("取消");
			TextView ok2 = dv.findViewById(R.id.rvOk);
			ok2.setText("选择");
			ok2.setOnClickListener((view1) -> {
				String indexName2;
				if (Objects.equals(parentId, "0"))
					indexName2 = "根目录";
				else
					indexName2 = DBC.getInstance(this).getIndexName(parentId);
				if (Objects.equals(indexName2, "") || indexName2 == null)
					indexChooser.setText(R.string.root_index);
				else
					indexChooser.setText(indexName2);
				dialog1.cancel();
			});
			dialog1.show();
		});
		ok.setOnClickListener((view2) -> {
			String name = titleEditor.getText().toString();
			//String url = urlEditor.getText().toString();
			if (name.equals(""))//||url.equals(""))
			{
				toast("请输入标题");
			} else {
				if (!DBC.getInstance(this).modParent(tag.id, name, FolderId))
					MToastUtils.makeText(this, "修改失败，目录已存在", MToastUtils.LENGTH_SHORT).show();
				else {
					loadMark(FolderId);
					dialog.cancel();
				}
			}
		});
		cancel.setOnClickListener((view2) -> {
			dialog.cancel();
		});
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.gravity = Gravity.BOTTOM;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.dimAmount = 0.2f;
		dialog.getWindow().setAttributes(lp);
		//设置该属性，dialog可以铺满屏幕
		dialog.getWindow().setBackgroundDrawable(null);
		View holder = layout;
		FrameLayout.LayoutParams lps = (FrameLayout.LayoutParams) holder.getLayoutParams();
		lps.setMargins(30, 30, 30, 30);
		holder.setLayoutParams(lps);
		dialog.show();
	}*/
	@SuppressLint("RtlHardcoded")
	@Override
	public void TagLongClickListener(View view, MotionEvent ev, final Tag tag) {
		initPop();
		if (!tag.canForward)
			mPopEdit.setOnClickListener(v -> {
				pop.dismiss();
				new MarkEditDialog(this).createMarkEditDialog(this, parentId, tag.title, tag.remark, false);
			});
		else
			mPopEdit.setOnClickListener(v -> {
				pop.dismiss();
				new MarkEditDialog(this).createFolderEditDialog(this, parentId, tag.id, false);
			});
		mPopDelete.setOnClickListener(view12 -> {
			if (tag.canForward) {
				MDialogUtils.Builder builder = new MDialogUtils.Builder(view12.getContext());
				builder.setMessage("是否删除" + tag.title + "文件夹？这将同时删除该文件夹内所有子文件夹和书签")
						.setNegativeButton("取消", null)
						.setTitle("删除文件夹")
						.setPositiveButton("删除", (dialog, which) -> {
							DBC.getInstance(view12.getContext()).deleteParent(tag.id);
							DBC.getInstance(view12.getContext()).deleteMarkAndFolder(tag.id);
							int position = mAdapter.getPosition(tag);
							mAdapter.remove(position);
							mAdapter.notifyItemRemoved(position);
							dialog.cancel();
						}).create().show();
			} else {
				DBC.getInstance(view12.getContext()).deleteMark(tag.remark);
				int position = mAdapter.getPosition(tag);
				mAdapter.remove(position);
				mAdapter.notifyItemRemoved(position);
			}
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
		mMarkRecyclerView.setLayoutFrozen(true);
	}
	
	@Override
	public void onSizeChanged(int size) {
		if (size == 0) {
			mMarkEmpty.setVisibility(View.VISIBLE);
			mMarkRecyclerView.setVisibility(View.GONE);
		} else {
			mMarkEmpty.setVisibility(View.GONE);
			mMarkRecyclerView.setVisibility(View.VISIBLE);
		}
	}
	
}

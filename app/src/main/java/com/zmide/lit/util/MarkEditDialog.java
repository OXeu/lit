package com.zmide.lit.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zmide.lit.R;
import com.zmide.lit.adapter.FolderAdapter;
import com.zmide.lit.interfaces.ParentViewOperate;
import com.zmide.lit.interfaces.TagViewOperate;
import com.zmide.lit.view.Editor;

import java.util.Objects;

/**
 * Copyright (C), 2019-2020, DreamStudio
 * Author: Xeu
 * Date: 2020/2/27 14:00
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class MarkEditDialog implements ParentViewOperate {
	
	private Activity activity;
	private RecyclerView rv;
	private TextView tt;
	private String nativeFolderId;
	
	public MarkEditDialog(Activity activity) {
		this.activity = activity;
	}
	
	//创建一个文件夹编辑对话框
	public void createFolderEditDialog(Activity activity, String FolderId, String id, boolean hasMore) {
		nativeFolderId = FolderId;
		Dialog dialog = new Dialog(activity);
		@SuppressLint("InflateParams") View layout = LayoutInflater.from(activity).inflate(R.layout.dialog_mark_editor, null);
		dialog.setContentView(layout);
		TextView ok = layout.findViewById(R.id.editDialogOk);//书签编辑确认
		TextView cancel = layout.findViewById(R.id.editDialogCancel);//放弃按钮
		TextView title = layout.findViewById(R.id.editDialogTitle);//标题（书签）
		Editor titleEditor = layout.findViewById(R.id.editDialogTitleEditor);//标题编辑框
		Editor urlEditor = layout.findViewById(R.id.editDialogUrl);//链接编辑框
		urlEditor.setVisibility(View.VISIBLE);
		
		//是否新建文件夹
		if (!Objects.equals(id, "") && id != null) {//否
			setIndexText(activity, id, titleEditor);
			ok.setText(R.string.mod);
			title.setText(R.string.folder_mod);//设置标题（目录修改）
		} else {
			ok.setText(R.string.new_add);
			title.setText(R.string.folder_add);//设置标题（新建目录）
		}
		urlEditor.setVisibility(View.GONE);//关闭URl
		TextView indexChooser = layout.findViewById(R.id.editDialogIndex);//目录选择器
		
		setIndexText(activity, nativeFolderId, indexChooser);
		indexChooser.setOnClickListener((view2) -> createIndexChooserDialog(activity, nativeFolderId, id, indexChooser, hasMore));
		
		ok.setOnClickListener((view2) -> {
			String name = titleEditor.getText().toString();
			//String url = urlEditor.getText().toString();
			if (name.equals("")) {
				MToastUtils.makeText("请输入标题", MToastUtils.LENGTH_SHORT).show();
			} else {
				if (Objects.equals(id, "") || id == null)
					DBC.getInstance(activity).addParent(name, nativeFolderId);
				else
					DBC.getInstance(activity).modParent(id, name, nativeFolderId);
				dialog.cancel();
				if (activity instanceof TagViewOperate)
					((TagViewOperate) activity).onLoadChildIndex(nativeFolderId);
			}
		});
		cancel.setText(R.string.cancel);
		cancel.setOnClickListener((view2) -> dialog.cancel());
		WindowManager.LayoutParams lp = Objects.requireNonNull(dialog.getWindow()).getAttributes();
		lp.gravity = Gravity.BOTTOM;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.dimAmount = 0.2f;
		dialog.getWindow().setAttributes(lp);
		//设置该属性，dialog可以铺满屏幕
		dialog.getWindow().setBackgroundDrawable(null);
		FrameLayout.LayoutParams lps = (FrameLayout.LayoutParams) layout.getLayoutParams();
		lps.setMargins(30, 30, 30, 30);
		layout.setLayoutParams(lps);
		dialog.show();
	}
	
	//创建一个书签编辑对话框
	public void createMarkEditDialog(Activity activity, String FolderId, String defaultName, String defaultUrl, boolean hasMore) {
		nativeFolderId = FolderId;
		Dialog dialog = new Dialog(activity);
		@SuppressLint("InflateParams") View layout = LayoutInflater.from(activity).inflate(R.layout.dialog_mark_editor, null);
		dialog.setContentView(layout);
		TextView ok = layout.findViewById(R.id.editDialogOk);//书签编辑确认
		TextView cancel = layout.findViewById(R.id.editDialogCancel);//放弃按钮
		TextView title = layout.findViewById(R.id.editDialogTitle);//标题（书签）
		Editor titleEditor = layout.findViewById(R.id.editDialogTitleEditor);//标题编辑框
		Editor urlEditor = layout.findViewById(R.id.editDialogUrl);//链接编辑框
		urlEditor.setVisibility(View.VISIBLE);
		titleEditor.setText(defaultName);//默认值
		urlEditor.setText(defaultUrl);//默认值
		TextView indexChooser = layout.findViewById(R.id.editDialogIndex);//目录选择器
		title.setText(R.string.mark);//设置标题（书签）
		setIndexText(activity, nativeFolderId, indexChooser);
		indexChooser.setOnClickListener((view2) -> createIndexChooserDialog(activity, nativeFolderId, "", indexChooser, hasMore));
		if (!Objects.equals(defaultName, "") && defaultName != null) {//否
			ok.setText(R.string.mod);
		} else {
			ok.setText(R.string.new_add);
		}
		ok.setOnClickListener((view2) -> {
			String name = titleEditor.getText().toString();
			String url = urlEditor.getText().toString();
			if (name.equals("")) {
				MToastUtils.makeText("请输入标题", MToastUtils.LENGTH_SHORT).show();
			} else if (url.equals("")) {
				MToastUtils.makeText("请输入链接", MToastUtils.LENGTH_SHORT).show();
			} else {
				if (Objects.equals(defaultName, "") && Objects.equals(defaultUrl, ""))//新建
					DBC.getInstance(activity).addMark(name, "", url, Integer.parseInt(nativeFolderId));
				else
					DBC.getInstance(activity).modMark(defaultName, defaultUrl, name, url, nativeFolderId);
				dialog.cancel();
				if (activity instanceof TagViewOperate)
					((TagViewOperate) activity).onLoadChildIndex(nativeFolderId);
			}
		});
		cancel.setText(R.string.cancel);
		cancel.setOnClickListener((view2) -> dialog.cancel());
		WindowManager.LayoutParams lp = Objects.requireNonNull(dialog.getWindow()).getAttributes();
		lp.gravity = Gravity.BOTTOM;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.dimAmount = 0.2f;
		dialog.getWindow().setAttributes(lp);
		//设置该属性，dialog可以铺满屏幕
		dialog.getWindow().setBackgroundDrawable(null);
		FrameLayout.LayoutParams lps = (FrameLayout.LayoutParams) layout.getLayoutParams();
		lps.setMargins(30, 30, 30, 30);
		layout.setLayoutParams(lps);
		dialog.show();
	}
	
	private void createIndexChooserDialog(Activity activity, String FolderId, String id, TextView indexChooser, boolean hasMore) {//
		nativeFolderId = FolderId;
		MDialogUtils.Builder dialogs = new MDialogUtils.Builder(activity);
		@SuppressLint("InflateParams") View dv = LayoutInflater.from(activity).inflate(R.layout.rv_button, null);
		rv = dv.findViewById(R.id.recyclerView);
		LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity);//这里我们使用默认的线性布局管理器,将其设为垂直显示
		mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		rv.setLayoutManager(mLayoutManager);//设置布局管理器
		FolderAdapter adapter = new FolderAdapter(activity, DBC.getInstance(activity).getParents(nativeFolderId), id);
		adapter.setInterface(this);
		rv.setAdapter(adapter);
		ImageView back = dv.findViewById(R.id.rvBack);
		back.setOnClickListener((view1) -> {
			if (Objects.equals(nativeFolderId, "0") || nativeFolderId == null)
				MToastUtils.makeText("已经是根目录了", MToastUtils.LENGTH_SHORT).show();
			else {
				String parent = DBC.getInstance(activity).getParentByFolder(nativeFolderId) + "";
				onLoadFoldersIndex(parent, id);
				nativeFolderId = parent;
			}
		});
		tt = dv.findViewById(R.id.rvTitle);
		setIndexText(activity, nativeFolderId, tt);
		Dialog dialog1 = dialogs.setContentView(dv).create();
		TextView cancel2 = dv.findViewById(R.id.rvCancel);
		TextView more2 = dv.findViewById(R.id.rvMore);
		if (hasMore) {
			more2.setVisibility(View.VISIBLE);
			more2.setOnClickListener((view1) -> {
				Dialog dialog3 = new Dialog(activity);
				@SuppressLint("InflateParams") View layout3 = LayoutInflater.from(activity).inflate(R.layout.dialog_mark_editor, null);
				dialog3.setContentView(layout3);
				TextView ok3 = layout3.findViewById(R.id.editDialogOk);
				TextView cancel3 = layout3.findViewById(R.id.editDialogCancel);
				TextView title3 = layout3.findViewById(R.id.editDialogTitle);
				Editor titleEditor3 = layout3.findViewById(R.id.editDialogTitleEditor);
				Editor urlEditor3 = layout3.findViewById(R.id.editDialogUrl);
				urlEditor3.setVisibility(View.GONE);
				TextView indexChooser3 = layout3.findViewById(R.id.editDialogIndex);
				setIndexText(activity, nativeFolderId, indexChooser3);
				
				title3.setText(R.string.mark_add);
				indexChooser3.setVisibility(View.GONE);
				ok3.setOnClickListener((view3) -> {
					String name = titleEditor3.getText().toString();
					//String url = urlEditor.getText().toString();
					if (name.equals(""))//||url.equals(""))
					{
						MToastUtils.makeText("请输入标题", MToastUtils.LENGTH_SHORT).show();
					} else {
						if (!DBC.getInstance(activity).addParent(name, nativeFolderId))
							MToastUtils.makeText("添加失败，目录已存在", MToastUtils.LENGTH_SHORT).show();
						onLoadFoldersIndex(nativeFolderId, id);
						dialog3.cancel();
					}
				});
				cancel3.setOnClickListener((view3) -> dialog3.cancel());
				WindowManager.LayoutParams lp = Objects.requireNonNull(dialog3.getWindow()).getAttributes();
				lp.gravity = Gravity.BOTTOM;
				lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
				lp.width = WindowManager.LayoutParams.MATCH_PARENT;
				lp.dimAmount = 0.2f;
				dialog3.getWindow().setAttributes(lp);
				//设置该属性，dialog可以铺满屏幕
				dialog3.getWindow().setBackgroundDrawable(null);
				FrameLayout.LayoutParams lps = (FrameLayout.LayoutParams) layout3.getLayoutParams();
				lps.setMargins(30, 30, 30, 30);
				layout3.setLayoutParams(lps);
				dialog3.show();
			});
		} else {
			more2.setVisibility(View.GONE);
		}
		cancel2.setOnClickListener((view1) -> dialog1.cancel());
		cancel2.setText("取消");
		TextView ok2 = dv.findViewById(R.id.rvOk);
		ok2.setText("选择");
		ok2.setOnClickListener((view1) -> {
			setIndexText(activity, nativeFolderId, indexChooser);
			
			dialog1.cancel();
		});
		dialog1.show();
	}
	
	@Override
	public void onLoadFoldersIndex(String id, String ownId) {
		FolderAdapter adapter = new FolderAdapter(activity, DBC.getInstance(activity).getParents(id), ownId);
		adapter.setInterface(this);
		rv.setAdapter(adapter);
		nativeFolderId = id;
		setIndexText(activity, id, tt);
	}
	
	private void setIndexText(Activity activity, String FolderId, TextView indexChooser) {
		String indexName2;
		if (Objects.equals(FolderId, "0"))
			indexName2 = "根目录";
		else
			indexName2 = DBC.getInstance(activity).getIndexName(FolderId);
		if (Objects.equals(indexName2, "") || indexName2 == null)
			indexChooser.setText(R.string.root_index);
		else
			indexChooser.setText(indexName2);
	}
	
	private void setIndexText(Activity activity, String FolderId, Editor indexChooser) {
		String indexName2;
		if (Objects.equals(FolderId, "0"))
			indexName2 = "根目录";
		else
			indexName2 = DBC.getInstance(activity).getIndexName(FolderId);
		if (Objects.equals(indexName2, "") || indexName2 == null)
			indexChooser.setText(activity.getResources().getString(R.string.root_index));
		else
			indexChooser.setText(indexName2);
	}
}

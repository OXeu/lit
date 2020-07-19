package com.zmide.lit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zmide.lit.R;
import com.zmide.lit.interfaces.ParentViewOperate;
import com.zmide.lit.object.Parent;
import com.zmide.lit.skin.SkinManager;
import com.zmide.lit.util.MToastUtils;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by xeu on 2019/12/03.
 */

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.MyViewHolder> {
	
	private String id;
	private ArrayList<Parent> parents;
	private LayoutInflater mInflater;
	private ParentViewOperate interfaces;
	
	//提供一个合适的构造方法
	public FolderAdapter(Context context, ArrayList<Parent> parents, String id) {
		this.parents = parents;
		this.id = id;
		mInflater = LayoutInflater.from(context);
	}
	
	/**
	 * 将布局转换为View并传递给自定义的MyViewHolder
	 *
	 * @param viewGroup v
	 * @param viewType  v
	 * @return v
	 */
	@NonNull
	@Override
	public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
		View view = mInflater.inflate(R.layout.tag_item, viewGroup, false);
		return new MyViewHolder(view);
	}
	
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public void onBindViewHolder(final MyViewHolder viewHolder, final int position) {
		final Parent parent = parents.get(position);
		
		viewHolder.mParentTitle.setText(parent.name);
		viewHolder.mParentIcon.setImageResource(R.drawable.folder_line);
		viewHolder.mParentRemark.setVisibility(View.GONE);
		viewHolder.mParentForward.setVisibility(View.GONE);
		viewHolder.mParentTime.setVisibility(View.GONE);
		viewHolder.mParentParent.setOnTouchListener((view, e) -> {
			return false;
		});
		
		viewHolder.mParentParent.setOnClickListener(view -> {
			//子目录
			if (Objects.equals(parent.id, id))
				MToastUtils.makeText("不能选择自己做为父目录", MToastUtils.LENGTH_LONG).show();
			else if (view.getContext() instanceof ParentViewOperate)
				((ParentViewOperate) view.getContext()).onLoadFoldersIndex(parent.id, id);
			else if (interfaces != null)
				interfaces.onLoadFoldersIndex(parent.id, id);
			else
				MToastUtils.makeText("子目录加载失败", MToastUtils.LENGTH_LONG).show();
		});
	}
	
	
	/**
	 * 获取item的数目
	 *
	 * @return int
	 */
	@Override
	public int getItemCount() {
		return parents.size();
	}
	
	public void setInterface(ParentViewOperate interfaces) {
		this.interfaces = interfaces;
	}
	
	//自定义的ViewHolder，持有item的所有控件
	public static class MyViewHolder extends RecyclerView.ViewHolder {
		private ImageView mParentForward;
		private ImageView mParentIcon;
		private TextView mParentTitle;
		private TextView mParentRemark;
		private RelativeLayout mParentParent;
		private TextView mParentTime;
		
		public MyViewHolder(View view) {
			super(view);
			mParentIcon = view.findViewById(R.id.tagIcon);
			mParentTitle = view.findViewById(R.id.tagTitle);
			mParentRemark = view.findViewById(R.id.tagRemark);
			mParentForward = view.findViewById(R.id.tagForward);
			mParentParent = view.findViewById(R.id.tagParent);
			mParentTime = view.findViewById(R.id.tagTime);
			mParentTitle.setTextColor(SkinManager.getInstance().getColor(R.color.accent));
			mParentRemark.setTextColor(SkinManager.getInstance().getColor(R.color.light));
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				mParentForward.setImageTintList(ColorStateList.valueOf(SkinManager.getInstance().getColor(R.color.accent)));
			}
			mParentTime.setTextColor(SkinManager.getInstance().getColor(R.color.light));
			mParentParent.setBackground(SkinManager.getInstance().getDrawable(R.drawable.ripple_normal));
		}
	}
	
}

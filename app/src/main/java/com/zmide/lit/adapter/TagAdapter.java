package com.zmide.lit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zmide.lit.R;
import com.zmide.lit.interfaces.TagViewOperate;
import com.zmide.lit.object.Tag;
import com.zmide.lit.skin.SkinManager;

import java.util.ArrayList;

/**
 * Created by xeu on 2019/12/03.
 */

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.MyViewHolder> {
	
	private Context mContext;
	private ArrayList<Tag> tags;
	private LayoutInflater mInflater;
	private int size;
	
	private MotionEvent ev;
	
	//提供一个合适的构造方法
	public TagAdapter(Context context, ArrayList<Tag> tags) {
		this.mContext = context;
		this.tags = tags;
		mInflater = LayoutInflater.from(context);
	}
	
	public void remove(int position) {
		tags.remove(position);
		if (tags.size() != size) {
			if (mContext instanceof TagViewOperate)
				((TagViewOperate) mContext).onSizeChanged(tags.size());
			else
				throw new RuntimeException("Can't notify size changed");
		}
	}
	
	public int getPosition(Tag tag) {
		for (Tag m : tags) {
			if (m == tag) {
				return tags.indexOf(m);
			}
		}
		return 0;
	}
	
	/**
	 * 将布局转换为View并传递给自定义的MyViewHolder
	 *
	 * @param viewGroup group
	 * @param viewType  int
	 * @return holder
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
		final Tag tag = tags.get(position);
		
		viewHolder.mTagTitle.setText(tag.title);
		Bitmap bitmap = BitmapFactory.decodeFile(tag.icon);
		if (bitmap != null)
			viewHolder.mTagIcon.setImageBitmap(bitmap);
		else if (!tag.canForward)
			viewHolder.mTagIcon.setImageResource(R.drawable.global_line);
		else
			viewHolder.mTagIcon.setImageResource(R.drawable.folder_line);
		if (tag.remark.equals(""))
			viewHolder.mTagRemark.setVisibility(View.GONE);
		else {
			viewHolder.mTagRemark.setVisibility(View.VISIBLE);
			viewHolder.mTagRemark.setText(tag.remark);
		}
		if (tag.canForward)
			viewHolder.mTagForward.setVisibility(View.VISIBLE);
		else
			viewHolder.mTagForward.setVisibility(View.GONE);
		if (tag.time == null || tag.time.equals(""))
			viewHolder.mTagTime.setVisibility(View.GONE);
		else {
			viewHolder.mTagTime.setVisibility(View.VISIBLE);
			viewHolder.mTagTime.setText(tag.time);
		}
		viewHolder.mTagParent.setOnTouchListener((view, e) -> {
			ev = e;
			return false;
		});
		
		viewHolder.mTagParent.setOnLongClickListener(view -> {
			if (view.getContext() instanceof TagViewOperate)
				((TagViewOperate) view.getContext()).TagLongClickListener(view, ev, tag);
			else
				Toast.makeText(view.getContext(), "长按失败", Toast.LENGTH_LONG).show();
			return false;
		});
		
		viewHolder.mTagParent.setOnClickListener(view -> {
			if (!tag.canForward) {//普通书签历史
				Intent i = new Intent();
				i.putExtra("url", tag.remark)
						.setData(null)
						.setPackage(mContext.getPackageName())
						.putExtra("ifNew", false)
						.setAction(Intent.ACTION_VIEW);
				view.getContext().startActivity(i);
			} else {
				//子目录
				if (view.getContext() instanceof TagViewOperate)
					((TagViewOperate) view.getContext()).onLoadChildIndex(tag.id);
				else
					Toast.makeText(view.getContext(), "子目录加载失败", Toast.LENGTH_LONG).show();
			}
		});
	}
	
	/*
	public void updateTag(ArrayList<Tag> tags) {
		this.tags = tags;
		if (tags.size() != size) {
			if (mContext instanceof TagViewOperate)
				((TagViewOperate) mContext).onSizeChanged(tags.size());
			else
				throw new RuntimeException("Can't notify size changed");
		}
	}*/
	
	/**
	 * 获取item的数目
	 *
	 * @return int
	 */
	@Override
	public int getItemCount() {
		if (tags.size() != size) {
			if (mContext instanceof TagViewOperate)
				((TagViewOperate) mContext).onSizeChanged(tags.size());
			else
				throw new RuntimeException("Can't notify size changed");
		}
		size = tags.size();
		return size;
	}
	
	//自定义的ViewHolder，持有item的所有控件
	public static class MyViewHolder extends RecyclerView.ViewHolder {
		private ImageView mTagForward;
		private ImageView mTagIcon;
		private TextView mTagTitle;
		private TextView mTagRemark;
		private RelativeLayout mTagParent;
		private TextView mTagTime;
		
		public MyViewHolder(View view) {
			super(view);
			mTagIcon = view.findViewById(R.id.tagIcon);
			mTagTitle = view.findViewById(R.id.tagTitle);
			mTagRemark = view.findViewById(R.id.tagRemark);
			mTagForward = view.findViewById(R.id.tagForward);
			mTagParent = view.findViewById(R.id.tagParent);
			mTagTime = view.findViewById(R.id.tagTime);
			mTagTitle.setTextColor(SkinManager.getInstance().getColor(R.color.accent));
			mTagRemark.setTextColor(SkinManager.getInstance().getColor(R.color.light));
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				mTagForward.setImageTintList(ColorStateList.valueOf(SkinManager.getInstance().getColor(R.color.accent)));
			}
			mTagTime.setTextColor(SkinManager.getInstance().getColor(R.color.light));
			mTagParent.setBackground(SkinManager.getInstance().getDrawable(R.drawable.ripple_normal));
		}
	}
}

/*
package com.zmide.lit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zmide.lit.R;
import com.zmide.lit.dialog.DialogList;
import com.zmide.lit.skin.SkinManager;

import java.util.ArrayList;

*/
/**
 * Created by xeu on 2019/12/13.
 * <p>
 * 将布局转换为View并传递给自定义的MyViewHolder
 *
 * @param viewGroup viewGroup
 * @param viewType  int
 * @return ViewHolder
 * <p>
 * 建立起MyViewHolder中视图与数据的关联
 * @param viewHolder ViewHolder
 * @param position   int
 * <p>
 * 获取item的数目
 * @return int
 * <p>
 * 将布局转换为View并传递给自定义的MyViewHolder
 * @param viewGroup viewGroup
 * @param viewType  int
 * @return ViewHolder
 * <p>
 * 建立起MyViewHolder中视图与数据的关联
 * @param viewHolder ViewHolder
 * @param position   int
 * <p>
 * 获取item的数目
 * @return int
 *//*


public class SelectItemAdapter extends RecyclerView.Adapter<SelectItemAdapter.MyViewHolder> {
	
	private final ArrayList<View.OnClickListener> listeners;
	private final DialogList d;
	private LayoutInflater mInflater;
	private ArrayList<String> names;
	
	//提供一个合适的构造方法
	public SelectItemAdapter(Context context, ArrayList<String> names, ArrayList<View.OnClickListener> listeners, DialogList d) {
		this.names = names;
		this.listeners = listeners;
		this.d = d;
		mInflater = LayoutInflater.from(context);
	}
	
	*/
/**
 * 将布局转换为View并传递给自定义的MyViewHolder
 *
 * @param viewGroup viewGroup
 * @param viewType  int
 * @return ViewHolder
 *//*

	@NonNull
	@Override
	public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
		View view = mInflater.inflate(R.layout.choose_item, viewGroup, false);
		return new MyViewHolder(view);
	}
	
	*/
/**
 * 建立起MyViewHolder中视图与数据的关联
 *
 * @param viewHolder ViewHolder
 * @param position   int
 *//*

	@Override
	public void onBindViewHolder(@NonNull final MyViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
		String name = names.get(position);
		viewHolder.mTitle.setText(name);
		viewHolder.mParent.setOnClickListener(v -> {
			d.cancel();
			listeners.get(position).onClick(v);
		});
	}
	
	
	*/
/**
 * 获取item的数目
 *
 * @return int
 *//*

	@Override
	public int getItemCount() {
		return names.size();
	}
	
	//自定义的ViewHolder，持有item的所有控件
	public static class MyViewHolder extends RecyclerView.ViewHolder {
		private TextView mTitle;
		private LinearLayout mParent;
		
		public MyViewHolder(View view) {
			super(view);
			mTitle = view.findViewById(R.id.chooseItemTextView);
			mParent = view.findViewById(R.id.chooseItemParent);
			mTitle.setTextColor(SkinManager.getInstance().getColor(R.color.accent));
			mParent.setBackground(SkinManager.getInstance().getDrawable(R.drawable.ripple_normal));
		}
	}
	
	
}
*/

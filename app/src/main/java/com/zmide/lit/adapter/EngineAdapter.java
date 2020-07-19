package com.zmide.lit.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zmide.lit.R;
import com.zmide.lit.main.SearchEnvironment;
import com.zmide.lit.object.Diy;
import com.zmide.lit.skin.SkinManager;
import com.zmide.lit.ui.MainActivity;

import java.util.ArrayList;
/*
 *
 * Created by xeu on 2020/1/1 23:20.
 * <p>
 * 将布局转换为View并传递给自定义的MyViewHolder
 *
 * @param viewGroup viewGroup
 * @param viewType int
 * @return holder
 * <p>
 * 建立起MyViewHolder中视图与数据的关联
 * @param viewHolder holder
 * @param position int
 * <p>
 * 获取item的数目
 * @return int
 
 */


public class EngineAdapter extends RecyclerView.Adapter<EngineAdapter.MyViewHolder> {
	
	private MainActivity mActivity;
	
	private LayoutInflater mInflater;
	
	private ArrayList<Diy> diys;
	
	//提供一个合适的构造方法
	public EngineAdapter(MainActivity ac, ArrayList<Diy> diys) {
		this.mActivity = ac;
		mInflater = LayoutInflater.from(mActivity);
		this.diys = diys;
	}
	
	/**
	 * 将布局转换为View并传递给自定义的MyViewHolder
	 *
	 * @param viewGroup viewGroup
	 * @param viewType  int
	 * @return holder
	 */
	
	@NonNull
	@Override
	public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
		View view = mInflater.inflate(R.layout.engine_item, viewGroup, false);
		return new MyViewHolder(view);
	}
	
	/**
	 * 建立起MyViewHolder中视图与数据的关联
	 *
	 * @param viewHolder holder
	 * @param position   int
	 */
	
	@Override
	public void onBindViewHolder(final MyViewHolder viewHolder, int position) {
		final Diy activity_diy = diys.get(position);
		viewHolder.mTitle.setTextColor(0xff333333);
		viewHolder.mTitle.setBackground(SkinManager.getInstance().getDrawable(R.drawable.ripple_circle_outer));
		viewHolder.mTitle.setText(activity_diy.title);
		viewHolder.mTitle.setOnClickListener(view -> SearchEnvironment.onSearchEngine(activity_diy.value));
	}
	
	
	/**
	 * 获取item的数目
	 *
	 * @return int
	 */
	
	@Override
	public int getItemCount() {
		return diys.size();
	}
	
	//自定义的ViewHolder，持有item的所有控件
	public static class MyViewHolder extends RecyclerView.ViewHolder {
		private TextView mTitle;
		
		public MyViewHolder(View view) {
			super(view);
			mTitle = view.findViewById(R.id.engineItemText);
			
		}
	}
	
	
}

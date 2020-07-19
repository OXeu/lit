package com.zmide.lit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.zmide.lit.BR;
import com.zmide.lit.R;
import com.zmide.lit.bean.Setting;
import com.zmide.lit.databinding.SettingAdapterDB;

import java.util.ArrayList;

/**
 * Created by xeu on 2019/12/13.
 */

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.MyViewHolder> {
	private ArrayList<Setting> settings;
	private LayoutInflater mInflater;
	
	//提供一个合适的构造方法
	public SettingAdapter(Context context, ArrayList<Setting> settings) {
		this.settings = settings;
		mInflater = LayoutInflater.from(context);
	}
	
	/**
	 * 将布局转换为View并传递给自定义的MyViewHolder
	 *
	 * @param viewGroup view
	 * @param viewType  int
	 * @return holder
	 */
	@NonNull
	@Override
	public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
		SettingAdapterDB db = DataBindingUtil.inflate(mInflater, R.layout.setting_item, viewGroup, false);
		MyViewHolder holder = new MyViewHolder(db.getRoot().getRootView());
		holder.setDb(db);
		return holder;
	}
	
	/**
	 * 建立起MyViewHolder中视图与数据的关联
	 *
	 * @param viewHolder holder
	 * @param position   int
	 */
	@Override
	public void onBindViewHolder(MyViewHolder viewHolder, int position) {
		viewHolder.setContext(settings.get(position));
	}
	
	
	/**
	 * 获取item的数目
	 *
	 * @return int
	 */
	@Override
	public int getItemCount() {
		return settings.size();
	}
	
	//自定义的ViewHolder，持有item的所有控件
	public static class MyViewHolder extends RecyclerView.ViewHolder {
		private SettingAdapterDB db;
		
		public MyViewHolder(View view) {
			super(view);
		}
		
		public SettingAdapterDB getDb() {
			return db;
		}
		
		void setDb(SettingAdapterDB db) {
			this.db = db;
		}
		
		public void setContext(Setting t) {
			db.setVariable(BR.mbean, t);
			db.executePendingBindings();
		}
	}
	
}

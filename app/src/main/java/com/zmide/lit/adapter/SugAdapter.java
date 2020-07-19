package com.zmide.lit.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zmide.lit.R;
import com.zmide.lit.main.SearchEnvironment;
import com.zmide.lit.skin.SkinManager;
import com.zmide.lit.ui.MainActivity;

import java.util.ArrayList;

/**
 * Created by xeu on 2020/1/1 23:20.
 */


public class SugAdapter extends RecyclerView.Adapter<SugAdapter.MyViewHolder> {
	
	private MainActivity mActivity;
	
	private LayoutInflater mInflater;
	
	private ArrayList<String> tips = new ArrayList<>();
	
	//提供一个合适的构造方法
	public SugAdapter(MainActivity ac) {
		this.mActivity = ac;
		mInflater = LayoutInflater.from(mActivity);
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
		View view = mInflater.inflate(R.layout.sug_item, viewGroup, false);
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
		final String tip = tips.get(position);
		viewHolder.mTip.setTextColor(0xff333333);
		viewHolder.mParent.setBackground(SkinManager.getInstance().getDrawable(R.drawable.ripple_normal));
		viewHolder.mUse.setBackground(SkinManager.getInstance().getDrawable(R.drawable.ripple_circle));
		viewHolder.mTip.setText(tip);
		viewHolder.mParent.setOnClickListener(view -> SearchEnvironment.Search(tip));
		viewHolder.mUse.setOnClickListener(view -> SearchEnvironment.asKey(tip));
		
	}
	
	
	/**
	 * 获取item的数目
	 *
	 * @return int
	 */
	
	@Override
	public int getItemCount() {
		return tips.size();
	}
	
	public void addTip(ArrayList<String> s) {
		tips.addAll(s);
	}
	
	//自定义的ViewHolder，持有item的所有控件
	public static class MyViewHolder extends RecyclerView.ViewHolder {
		private TextView mTip;
		private RelativeLayout mParent;
		private ImageView mUse;
		
		public MyViewHolder(View view) {
			super(view);
			mTip = view.findViewById(R.id.sugTip);
			mParent = view.findViewById(R.id.sugParent);
			mUse = view.findViewById(R.id.sugUse);
			
		}
	}
	
	
}

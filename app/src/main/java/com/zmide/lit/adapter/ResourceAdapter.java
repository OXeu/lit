package com.zmide.lit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zmide.lit.R;
import com.zmide.lit.bean.SettingChild;
import com.zmide.lit.object.color.Gradients;
import com.zmide.lit.skin.SkinManager;
import com.zmide.lit.util.MDialogUtils;
import com.zmide.lit.util.MSharedPreferenceUtils;

import java.net.URLConnection;
import java.util.List;

/**
 * Created by xeu on 2019/12/13.
 */

public class ResourceAdapter extends RecyclerView.Adapter<ResourceAdapter.MyViewHolder> {
	
	private List<Uri> urls;
	private LayoutInflater mInflater;
	private SharedPreferences sharedPreferences;
	
	//提供一个合适的构造方法
	public ResourceAdapter(Context context, List<Uri> urls) {
		this.urls = urls;
		mInflater = LayoutInflater.from(context);
		
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
		View view = mInflater.inflate(R.layout.resource_item, viewGroup, false);
		return new MyViewHolder(view);
	}
	
	/**
	 * 建立起MyViewHolder中视图与数据的关联
	 *
	 * @param viewHolder holder
	 * @param position   int
	 */
	@Override
	public void onBindViewHolder(@NonNull final MyViewHolder viewHolder, @SuppressLint("RecyclerView")  int position) {
		String url = urls.get(position)+"";
		String type = URLConnection.guessContentTypeFromName(url);
		viewHolder.mType.setText(type);
		viewHolder.mType.setBackground(Gradients.getDrawable(type));
		viewHolder.mTitle.setTextColor(SkinManager.getInstance().getColor(R.color.light));
		viewHolder.mTitle.setText(url);
		viewHolder.mParent.setOnClickListener(view -> {
		
		});
	}
	
	
	/**
	 * 获取item的数目
	 *
	 * @return int
	 */
	@Override
	public int getItemCount() {
		return urls.size();
	}
	
	//自定义的ViewHolder，持有item的所有控件
	static class MyViewHolder extends RecyclerView.ViewHolder {
		private TextView mTitle;
		private TextView mType;
		private LinearLayout mParent;
		
		MyViewHolder(View view) {
			super(view);
			mTitle = view.findViewById(R.id.link);
			mType = view.findViewById(R.id.type);
			mParent = view.findViewById(R.id.parent);
			mTitle.setTextColor(SkinManager.getInstance().getColor(R.color.light));
			mParent.setBackground(SkinManager.getInstance().getDrawable(R.drawable.ripple_normal));
		}
	}
	
	
}

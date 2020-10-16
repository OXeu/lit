package com.zmide.lit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zmide.lit.R;
import com.zmide.lit.main.MWeb;
import com.zmide.lit.main.WebContainerPlus;
import com.zmide.lit.main.WindowsManager;

import java.util.ArrayList;

/**
 * Created by wuminmiao on 2016/10/13.
 */

public class WindowsAdapter extends RecyclerView.Adapter<WindowsAdapter.MyViewHolder> {
	
	private ArrayList<MWeb> webs;
	private LayoutInflater mInflater;
	
	
	//提供一个合适的构造方法
	public WindowsAdapter(Context context, ArrayList<MWeb> webs) {
		this.webs = webs;
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
		View view = mInflater.inflate(R.layout.window_item, viewGroup, false);
		return new MyViewHolder(view);
	}
	
	/**
	 * 建立起MyViewHolder中视图与数据的关联
	 *
	 * @param viewHolder holder
	 * @param position   int
	 */
	@SuppressLint("SetTextI18n")
	@Override
	public void onBindViewHolder(final MyViewHolder viewHolder, final int position) {
		viewHolder.mWindowTitle.setText(webs.get(position).getTitle() + "");
		viewHolder.mWindowUrl.setText(webs.get(position).getUrl() + "");
		if (position == WebContainerPlus.getWindowId())
			viewHolder.mWindowParent.setBackgroundResource(R.drawable.box_blue_15_normal);
		else
			viewHolder.mWindowParent.setBackgroundResource(R.drawable.box_gray_15_normal);
			
		Bitmap bitmap2 = BitmapFactory.decodeFile(webs.get(position).getIcon());
		if (bitmap2 != null)
			viewHolder.mWindowIcon.setImageBitmap(bitmap2);
		else
			viewHolder.mWindowIcon.setImageResource(R.drawable.dialog);
		viewHolder.mWindowParent.setOnClickListener(view -> {
				WebContainerPlus.switchWindow(position);
				WindowsManager.hideWindows();
		});
		Animation animation = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t) {
				viewHolder.mWindowParent.setAlpha(interpolatedTime);
				if (interpolatedTime == 1) {
					viewHolder.mWindowParent.setVisibility(View.VISIBLE);
				}
			}
			
			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};
		animation.setDuration(500);
		viewHolder.mWindowParent.startAnimation(animation);
		
	}
	
	
	public void updateWeb(ArrayList<MWeb> webs) {
		this.webs = webs;
	}
	
	/**
	 * 获取item的数目
	 *
	 * @return int
	 */
	@Override
	public int getItemCount() {
		return webs.size();
	}
	
	public interface WebViewOperate {
		void OnWebChange(ArrayList<MWeb> webs, int position);
	}
	
	//自定义的ViewHolder，持有item的所有控件
	static class MyViewHolder extends RecyclerView.ViewHolder {
		private ImageView mWindowIcon;
		private TextView mWindowTitle;
		private TextView mWindowUrl;
		private RelativeLayout mWindowParent;
		
		MyViewHolder(View view) {
			super(view);
			mWindowIcon = view.findViewById(R.id.windowIcon);
			mWindowTitle = view.findViewById(R.id.windowTitle);
			mWindowUrl = view.findViewById(R.id.windowUrl);
			mWindowParent = view.findViewById(R.id.windowParent);
		}
	}
}

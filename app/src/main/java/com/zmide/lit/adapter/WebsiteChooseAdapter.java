package com.zmide.lit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zmide.lit.R;
import com.zmide.lit.bean.SettingChild;
import com.zmide.lit.object.WebsiteSetting;
import com.zmide.lit.skin.SkinManager;
import com.zmide.lit.util.MDialogUtils;
import com.zmide.lit.util.MSharedPreferenceUtils;
import com.zmide.lit.util.WebsiteUtils;

/**
 * Created by xeu on 2019/12/13.
 */

public class WebsiteChooseAdapter extends RecyclerView.Adapter<WebsiteChooseAdapter.MViewHolder> {
	
	private WebsiteSetting websiteSettings;
	private Context mContext;
	private int p;
	private LayoutInflater mInflater;
	
	private SharedPreferences sharedPreferences;
	
	private TextView tv;
	
	private MDialogUtils d;
	
	//提供一个合适的构造方法
	WebsiteChooseAdapter(Context context, WebsiteSetting websiteSetting,int p, TextView tv, MDialogUtils d) {
		this.d = d;
		this.tv = tv;
		this.p = p;
		this.websiteSettings = websiteSetting;
		this.mContext = context;
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
	public MViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
		View view = mInflater.inflate(R.layout.choose_item, viewGroup, false);
		return new MViewHolder(view);
	}
	
	/**
	 * 建立起MyViewHolder中视图与数据的关联
	 *
	 * @param viewHolder holder
	 * @param position   int
	 */
	@Override
	public void onBindViewHolder(@NonNull final MViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
		final String msg = websiteSettings.get(p).chooses[position];
		
		if (msg.contentEquals(tv.getText()))
			viewHolder.mTitle.setTextColor(0xff2196f3);
		viewHolder.mTitle.setText(msg);
		viewHolder.mParent.setOnClickListener(view -> {
			int id = 0;
			if (websiteSettings.get(p).ids==null)
				id = position;
			else if (websiteSettings.get(p).ids.length<=position)
				id = position;
			else
				id = websiteSettings.get(p).ids[position];
			
			websiteSettings.set(p,id);
			WebsiteUtils.putWebsiteSetting(mContext,websiteSettings);
			d.cancel();
			tv.setText(msg);
		});
	}
	
	
	/**
	 * 获取item的数目
	 *
	 * @return int
	 */
	@Override
	public int getItemCount() {
		return websiteSettings.get(p).chooses.length;
	}
	
	//自定义的ViewHolder，持有item的所有控件
	static class MViewHolder extends RecyclerView.ViewHolder {
		private TextView mTitle;
		private LinearLayout mParent;
		
		MViewHolder(View view) {
			super(view);
			mTitle = view.findViewById(R.id.chooseItemTextView);
			mParent = view.findViewById(R.id.chooseItemParent);
			mTitle.setTextColor(SkinManager.getInstance().getColor(R.color.accent));
			mParent.setBackground(SkinManager.getInstance().getDrawable(R.drawable.ripple_normal));
		}
	}
	
	
}

package com.zmide.lit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zmide.lit.R;
import com.zmide.lit.main.SearchEnvironment;
import com.zmide.lit.object.WebsiteSetting;
import com.zmide.lit.skin.SkinManager;
import com.zmide.lit.ui.MainActivity;
import com.zmide.lit.util.DBC;
import com.zmide.lit.util.WebsiteUtils;

import java.util.ArrayList;

/**
 * Created by xeu on 2020/1/1 23:20.
 */


public class WebsiteSettingAdapter extends RecyclerView.Adapter<WebsiteSettingAdapter.MyViewHolder> {
	
	private Context mActivity;
	
	private LayoutInflater mInflater;
	
	private WebsiteSetting websiteSetting ;
	
	//提供一个合适的构造方法
	public WebsiteSettingAdapter(Context ac) {
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
		View view = mInflater.inflate(R.layout.website_item, viewGroup, false);
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
		final WebsiteSetting.setting tip = websiteSetting.get(position);
		viewHolder.mTip.setTextColor(0xff333333);
		viewHolder.mParent.setBackground(SkinManager.getInstance().getDrawable(R.drawable.ripple_normal));
		viewHolder.mMod.setBackground(SkinManager.getInstance().getDrawable(R.drawable.ripple_circle));
		viewHolder.mTip.setText(tip.name);
		viewHolder.mSwitch.setChecked(setting.state);
		viewHolder.mParent.setOnClickListener(view -> {
			viewHolder.mSwitch.setChecked(!viewHolder.mSwitch.isChecked());
			if (tip.state &&tip.isSwitch) {
				//viewHolder.mDiyItemSwitch.setChecked(true);
				websiteSetting = websiteSetting.set(position,viewHolder.mSwitch.isChecked());
				WebsiteUtils.putWebsiteSetting(mActivity,websiteSetting);
			}
		});
		viewHolder.mMod.setOnClickListener(view -> {
			//todo 修改按钮
		});
		if (setting.isSwitch){
		viewHolder.mMod.setVisibility(View.GONE);
		viewHolder.mSwitch.setVisibility(View.VISIBLE);
		}else{
		viewHolder.mSwitch.setVisibility(View.GONE);
		viewHolder.mMod.setVisibility(View.VISIBLE);
		}
		viewHolder.mSwitch.setOnCheckedChangeListener((cb, ischecked) -> {
			if (cb.isPressed()) {
				viewHolder.mParent.performClick();
//					if(Diy.isLimit(type)){
//						DBC.getInstance(mContext).modAllRun(false,type);
//					}
//					if(Diy.isrun&&Diy.isLimit(type)){
//							//viewHolder.mDiyItemSwitch.setChecked(true);
//							DBC.getInstance(mContext).modRun(true,Diy.id);
//					}else{
//					DBC.getInstance(mContext).modRun(ischecked,Diy.id);
//				    Diys = DBC.getInstance(mContext).getDiys(type);
//					((DiyViewOperate)cb.getContext()).onDataChanged(Diys);
//					}
			}
		});
	}
	
	
	/**
	 * 获取item的数目
	 *
	 * @return int
	 */
	
	@Override
	public int getItemCount() {
		return 6;
	}
	
	public void addTip(WebsiteSetting s) {
		websiteSetting = s;
	}
	
	//自定义的ViewHolder，持有item的所有控件
	public static class MyViewHolder extends RecyclerView.ViewHolder {
		private TextView mTip;
		private TextView mMod;
		private RelativeLayout mParent;
		private Switch mSwitch;
		
		public MyViewHolder(View view) {
			super(view);
			mTip = view.findViewById(R.id.name);
			mMod = view.findViewById(R.id.mod);
			mParent = view.findViewById(R.id.parent);
			mSwitch = view.findViewById(R.id.dialog_switch);
			
		}
	}
	
	
}

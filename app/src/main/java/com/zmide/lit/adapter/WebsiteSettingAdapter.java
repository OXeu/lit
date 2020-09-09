package com.zmide.lit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zmide.lit.R;
import com.zmide.lit.animation.Slide;
import com.zmide.lit.object.Diy;
import com.zmide.lit.object.WebsiteSetting;
import com.zmide.lit.skin.SkinManager;
import com.zmide.lit.util.DBC;
import com.zmide.lit.util.MDialogUtils;
import com.zmide.lit.util.WebsiteUtils;

import java.util.Objects;

/**
 * Created by xeu on 2020/1/1 23:20.
 */


public class WebsiteSettingAdapter extends RecyclerView.Adapter<WebsiteSettingAdapter.MyViewHolder> {
	
	private TextView tt;
	private LinearLayoutManager mLayoutManager;
	private RecyclerView rv;
	private MDialogUtils d;
	private Context mActivity;
	
	private LayoutInflater mInflater;
	
	private WebsiteSetting websiteSetting ;
	
	//提供一个合适的构造方法
	public WebsiteSettingAdapter(Context ac) {
		this.mActivity = ac;
		mInflater = LayoutInflater.from(mActivity);
		@SuppressLint("InflateParams") View dv = LayoutInflater.from(ac).inflate(R.layout.rv, null);
		rv = dv.findViewById(R.id.recyclerView);
		mLayoutManager = new LinearLayoutManager(ac);//这里我们使用默认的线性布局管理器,将其设为垂直显示
		mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		rv.setLayoutManager(mLayoutManager);//设置布局管理器
		tt = dv.findViewById(R.id.rvTitle);
		MDialogUtils.Builder dialog = new MDialogUtils.Builder(ac);
		d = dialog.setContentView(dv).create();
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
		final WebsiteSetting.setting tip = websiteSetting.get()[position];
		viewHolder.mParent.setBackground(SkinManager.getInstance().getDrawable(R.drawable.ripple_normal));
		viewHolder.mMod.setBackground(SkinManager.getInstance().getDrawable(R.drawable.ripple_circle));
		viewHolder.mTip.setText(tip.name);
		viewHolder.mTip.setTextColor(SkinManager.getInstance().getColor(R.color.accent));
		viewHolder.mSwitch.setChecked(tip.state);
		viewHolder.mParent.setOnClickListener(view -> {
			viewHolder.mSwitch.setChecked(!viewHolder.mSwitch.isChecked());
			if (tip.isSwitch) {
				//viewHolder.mDiyItemSwitch.setChecked(true);
				websiteSetting = websiteSetting.set(position,viewHolder.mSwitch.isChecked());
				WebsiteUtils.putWebsiteSetting(mActivity,websiteSetting);
			}else {
				viewHolder.mMod.performLongClick();
			}
		});
		viewHolder.mMod.setOnClickListener(view -> {
			d.show();
			Slide.slideToUp(Objects.requireNonNull(d.getWindow()).getDecorView());
			tt.setText(tip.name);
			rv.setLayoutManager(mLayoutManager);
			rv.setAdapter(new WebsiteChooseAdapter(mActivity, websiteSetting,position, viewHolder.mMod, d));
		});
		if (tip.isSwitch){
		viewHolder.mMod.setVisibility(View.GONE);
		viewHolder.mSwitch.setVisibility(View.VISIBLE);
		}else{
		viewHolder.mSwitch.setVisibility(View.GONE);
		viewHolder.mMod.setVisibility(View.VISIBLE);
		if ("User Agent".equals(tip.name)){
			String ua = "";
			if (websiteSetting.ua!=0)//自定义UA
				try {
					if (DBC.getInstance(mActivity).isDiyExist(websiteSetting.ua + "",Diy.UA))
						ua = DBC.getInstance(mActivity).getDiy(Diy.UA, websiteSetting.ua + "").title;
					else
						ua = DBC.getInstance(mActivity).getDiy(Diy.UA).title;
				}catch (Exception ignored){}
			if (ua==null||"".equals(ua))
				ua = "跟随默认";
			viewHolder.mMod.setText(ua);
		}
		if (tip.name.contains("剪")){
			String clip = "";
			int type = 4;
			if (websiteSetting.clip_enable!=4) {//自定义规则
				type = websiteSetting.clip_enable;
			}
			clip = type == 0 ? "询问" : type ==1 ? "允许" :type==2 ? "拒绝" : "跟随默认";
			viewHolder.mMod.setText(clip);
		}
		}
		viewHolder.mSwitch.setOnCheckedChangeListener((cb, ischecked) -> {
			if (cb.isPressed()) {
				viewHolder.mParent.performClick();
				viewHolder.mSwitch.setChecked(ischecked);
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
		return websiteSetting.get().length;
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

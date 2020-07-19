package com.zmide.lit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zmide.lit.R;
import com.zmide.lit.object.Diy;
import com.zmide.lit.skin.SkinManager;
import com.zmide.lit.util.DBC;

import java.util.ArrayList;

/**
 * Created by xeu on 2019/12/18.
 */

public class DiyAdapter extends RecyclerView.Adapter<DiyAdapter.MyViewHolder> {
	
	private Context mContext;
	private ArrayList<Diy> Diys;
	private LayoutInflater mInflater;
	
	private int size;
	
	private int type;
	private MotionEvent ev;
	
	//提供一个合适的构造方法
	public DiyAdapter(Context context, ArrayList<Diy> Diys, int type) {
		this.mContext = context;
		this.Diys = Diys;
		this.type = type;
		mInflater = LayoutInflater.from(context);
	}
	
	public void remove(int position) {
		Diys.remove(position);
		if (Diys.size() != size) {
			if (mContext instanceof DiyViewOperate)
				((DiyViewOperate) mContext).onSizeChanged(Diys.size());
			else
				throw new RuntimeException("Can't notify size changed");
		}
	}
	
	public int getPosition(Diy Diy) {
		for (com.zmide.lit.object.Diy m : Diys) {
			if (m == Diy) {
				return Diys.indexOf(m);
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
		View view = mInflater.inflate(R.layout.diy_item, viewGroup, false);
		return new MyViewHolder(view);
	}
	
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public void onBindViewHolder(final MyViewHolder viewHolder, final int position) {
		final Diy Diy = Diys.get(position);
		viewHolder.mDiyItemTitle.setText(Diy.title);
		viewHolder.mDiyItemDescription.setText(Diy.description);
		viewHolder.mDiyItemSwitch.setChecked(Diy.isrun);
		viewHolder.mDiyItemParent.setOnTouchListener((view, e) -> {
			ev = e;
			return false;
		});
		
		viewHolder.mDiyItemParent.setOnLongClickListener(view -> {
			if (view.getContext() instanceof DiyViewOperate)
				((DiyViewOperate) view.getContext()).DiyLongClickListener(view, ev, Diy);
			else
				Toast.makeText(view.getContext(), "长按失败", Toast.LENGTH_LONG).show();
			return false;
		});
		
		viewHolder.mDiyItemParent.setOnClickListener(view -> {
			/*
			Intent i= new Intent();
				i.setData(Uri.parse(Diy.remark))
						.setPackage(mContext.getPackageName())
						.setAction(Intent.ACTION_VIEW);
					view.getContext().startActivity(i);
				}else{
					Toast.makeText(view.getContext(),"暂不支持打开本地页面",Toast.LENGTH_LONG).show();
				}
				*/
			viewHolder.mDiyItemSwitch.setChecked(!viewHolder.mDiyItemSwitch.isChecked());
			if (Diy.isLimit(type)) {
				DBC.getInstance(mContext).modAllRun(false, type);
			}
			if (Diy.isrun && Diy.isLimit(type)) {
				//viewHolder.mDiyItemSwitch.setChecked(true);
				DBC.getInstance(mContext).modRun(true, Diy.id);
			} else
				DBC.getInstance(mContext).modRun(!Diy.isrun, Diy.id);
			Diys = DBC.getInstance(mContext).getDiys(type, false);
			((DiyViewOperate) view.getContext()).onDataChanged(Diys);
		});
		
		viewHolder.mDiyItemSwitch.setOnCheckedChangeListener((cb, ischecked) -> {
			if (cb.isPressed()) {
				viewHolder.mDiyItemParent.performClick();
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
		if (Diys.size() != size) {
			if (mContext instanceof DiyViewOperate)
				((DiyViewOperate) mContext).onSizeChanged(Diys.size());
			else
				throw new RuntimeException("Can't notify size changed");
		}
		size = Diys.size();
		return size;
	}
	
	public interface DiyViewOperate {
		void DiyLongClickListener(View view, MotionEvent ev, Diy Diy);
		
		void onSizeChanged(int size);
		
		void onDataChanged(ArrayList<Diy> diys);
	}
	
	//自定义的ViewHolder，持有item的所有控件
	static class MyViewHolder extends RecyclerView.ViewHolder {
		//private ImageView mDiyItemImage;
		private TextView mDiyItemDescription;
		private TextView mDiyItemTitle;
		private RelativeLayout mDiyItemParent;
		
		private Switch mDiyItemSwitch;
		
		MyViewHolder(View view) {
			super(view);
			mDiyItemParent = view.findViewById(R.id.diyParent);
			//mDiyItemImage = view.findViewById(R.id.diyImage);
			mDiyItemTitle = view.findViewById(R.id.diyItemTitle);
			mDiyItemDescription = view.findViewById(R.id.diyItemDescription);
			mDiyItemSwitch = view.findViewById(R.id.diyItemSwitch);
			mDiyItemTitle.setTextColor(SkinManager.getInstance().getColor(R.color.accent));
			mDiyItemDescription.setTextColor(SkinManager.getInstance().getColor(R.color.light));
			mDiyItemParent.setBackground(SkinManager.getInstance().getDrawable(R.drawable.ripple_normal));
		}
	}
}

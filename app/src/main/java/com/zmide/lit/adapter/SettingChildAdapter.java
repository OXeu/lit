package com.zmide.lit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zmide.lit.R;
import com.zmide.lit.animation.Slide;
import com.zmide.lit.bean.SettingChild;
import com.zmide.lit.skin.SkinManager;
import com.zmide.lit.util.MDialogUtils;
import com.zmide.lit.util.MSharedPreferenceUtils;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by xeu on 2019/12/13.
 */

public class SettingChildAdapter extends RecyclerView.Adapter<SettingChildAdapter.MyViewHolder> {
	
	private Context mContext;
	private ArrayList<SettingChild> settings;
	private LayoutInflater mInflater;
	private MDialogUtils d;
	private SharedPreferences sharedPreferences;
	
	private RecyclerView rv;
	
	private LinearLayoutManager mLayoutManager;
	
	private TextView tt;
	
	//提供一个合适的构造方法
	public SettingChildAdapter(Context context, ArrayList<SettingChild> settings) {
		this.mContext = context;
		this.settings = settings;
		mInflater = LayoutInflater.from(context);
		MDialogUtils.Builder dialog = new MDialogUtils.Builder(context);
		@SuppressLint("InflateParams") View dv = LayoutInflater.from(context).inflate(R.layout.rv, null);
		rv = dv.findViewById(R.id.recyclerView);
		mLayoutManager = new GridLayoutManager(context, 3);//这里我们使用默认的线性布局管理器,将其设为垂直显示
		//mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		rv.setLayoutManager(mLayoutManager);//设置布局管理器
		tt = dv.findViewById(R.id.rvTitle);
		d = dialog.setContentView(dv).create();
	}
	
	/**
	 * 将布局转换为View并传递给自定义的MyViewHolder
	 *
	 * @param viewGroup viewGroup
	 * @param viewType  type
	 * @return holder
	 */
	@NonNull
	@Override
	public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
		View view = mInflater.inflate(R.layout.setting_item_2, viewGroup, false);
		return new MyViewHolder(view);
	}
	
	/**
	 * 建立起MyViewHolder中视图与数据的关联
	 *
	 * @param viewHolder holder
	 * @param position   int
	 */
	@Override
	public void onBindViewHolder(final MyViewHolder viewHolder, final int position) {
		final SettingChild Setting = settings.get(position);
		Log.i("Setting", Setting.title + " " + Setting.isWebSetting);
		if (Setting.isWebSetting)
			sharedPreferences = MSharedPreferenceUtils.getWebViewSharedPreference();
		else
			sharedPreferences = MSharedPreferenceUtils.getSharedPreference();
		
		viewHolder.mSettingItemTitle.setText(Setting.title);
		if (Setting.description.equals(""))
			viewHolder.mSettingItemDescription.setVisibility(View.GONE);
		else
			viewHolder.mSettingItemDescription.setText(Setting.description);
		if (Setting.type == SettingChild.CHOOSE) {//选择
			viewHolder.mSettingItemSwitch.setVisibility(View.GONE);
			viewHolder.mSettingItemTo.setVisibility(View.VISIBLE);
			viewHolder.mSettingItemValue.setVisibility(View.VISIBLE);
			viewHolder.mSettingItemValue.setText(Setting.choose[Integer.parseInt(Objects.requireNonNull(sharedPreferences.getString(Setting.target, Setting.defaultValue + "")))]);
			OnClickListener listener = view -> {
				d.show();
				Slide.slideToUp(Objects.requireNonNull(d.getWindow()).findViewById(R.id.rvLinear));
				tt.setText(Setting.title);
				rv.setLayoutManager(mLayoutManager);
				rv.setAdapter(new ChooseItemAdapter(mContext, Setting, viewHolder.mSettingItemValue, d));
			};
			viewHolder.mSettingItemParent.setOnClickListener(listener);
		} else if (Setting.type == SettingChild.CHOOSE2) {//选择
			viewHolder.mSettingItemSwitch.setVisibility(View.GONE);
			viewHolder.mSettingItemTo.setVisibility(View.VISIBLE);
			viewHolder.mSettingItemValue.setVisibility(View.VISIBLE);
			viewHolder.mSettingItemValue.setText(Setting.choose[Integer.parseInt(Objects.requireNonNull(sharedPreferences.getString(Setting.target, Setting.defaultValue + "")))]);
			OnClickListener listener = view -> {
				d.show();
				Slide.slideToUp(Objects.requireNonNull(d.getWindow()).findViewById(R.id.rvLinear));
				tt.setText(Setting.title);
				rv.setLayoutManager(new LinearLayoutManager(mContext));
				rv.setAdapter(new ChooseItemAdapter(mContext, Setting, viewHolder.mSettingItemValue, d));
			};
			viewHolder.mSettingItemParent.setOnClickListener(listener);
		} else if (Setting.type == SettingChild.INTENT) {//Intent
			viewHolder.mSettingItemSwitch.setVisibility(View.GONE);
			viewHolder.mSettingItemTo.setVisibility(View.VISIBLE);
			viewHolder.mSettingItemValue.setVisibility(View.GONE);
			OnClickListener listener = view -> mContext.startActivity(Setting.intent);
			viewHolder.mSettingItemParent.setOnClickListener(listener);
		} else if (Setting.type == SettingChild.DOWNLOAD) {
			viewHolder.mSettingItemSwitch.setVisibility(View.GONE);
			viewHolder.mSettingItemTo.setVisibility(View.VISIBLE);
			viewHolder.mSettingItemValue.setVisibility(View.VISIBLE);
			viewHolder.mSettingItemValue.setVisibility(View.GONE);
			viewHolder.mSettingItemValue.setText(sharedPreferences.getString(Setting.target, Setting.defaultValue + ""));
			OnClickListener listener = view -> {
				ArrayList<String> appName = new ArrayList<>();
				ArrayList<String> packName = new ArrayList<>();
				d.show();
				Slide.slideToUp(Objects.requireNonNull(d.getWindow()).findViewById(R.id.rvLinear));
				tt.setText(Setting.title);
				rv.setLayoutManager(new LinearLayoutManager(mContext));
				PackageManager packageManager = mContext.getPackageManager();
				for (String str : new String[]{"com.dv.adm.pay", "idm.internet.download.manager.plus", "com.dv.adm", "idm.internet.download.manager", "com.vanda_adm.vanda"}) {
					try {
						PackageInfo packageInfo = packageManager.getPackageInfo(str, 0);
						ApplicationInfo applicationInfo = packageManager.getApplicationInfo(str, PackageManager.GET_META_DATA);
						if (packageInfo != null && packageInfo.applicationInfo.enabled) {
							appName.add(packageManager.getApplicationLabel(applicationInfo).toString());
							packName.add(str);
						}
					} catch (Exception ignored) {
					}
				}
				appName.add("系统下载器");
				packName.add("");
				rv.setAdapter(new DownloadItemAdapter(mContext, appName, packName, viewHolder.mSettingItemValue, d));
			};
			viewHolder.mSettingItemParent.setOnClickListener(listener);
		} else {//开关
			viewHolder.mSettingItemSwitch.setVisibility(View.VISIBLE);
			viewHolder.mSettingItemTo.setVisibility(View.GONE);
			viewHolder.mSettingItemValue.setVisibility(View.GONE);
			viewHolder.mSettingItemSwitch.setChecked(Objects.equals(sharedPreferences.getString(Setting.target, Setting.defaultValue + ""), "true"));
			OnClickListener listener = view -> {
				boolean result = viewHolder.mSettingItemSwitch.isChecked();
				viewHolder.mSettingItemSwitch.setChecked(!result);
			};
			viewHolder.mSettingItemParent.setOnClickListener(listener);
			viewHolder.mSettingItemSwitch.setOnCheckedChangeListener((c, isChecked) -> {
				if (Setting.isWebSetting)
					sharedPreferences = MSharedPreferenceUtils.getWebViewSharedPreference();
				else
					sharedPreferences = MSharedPreferenceUtils.getSharedPreference();
				sharedPreferences.edit().putString(Setting.target, isChecked + "").apply();
			});
		}
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
	static class MyViewHolder extends RecyclerView.ViewHolder {
		private RelativeLayout mSettingItemParent;
		private TextView mSettingItemTitle;
		private ImageView mSettingItemTo;
		private Switch mSettingItemSwitch;
		private TextView mSettingItemValue;
		
		private TextView mSettingItemDescription;
		
		MyViewHolder(View view) {
			super(view);
			mSettingItemParent = view.findViewById(R.id.settingItem2Parent);
			mSettingItemTitle = view.findViewById(R.id.settingItem2Title);
			mSettingItemTo = view.findViewById(R.id.settingItem2To);
			mSettingItemSwitch = view.findViewById(R.id.settingItem2Switch);
			mSettingItemValue = view.findViewById(R.id.settingItem2Value);
			mSettingItemDescription = view.findViewById(R.id.settingItem2Description);
			SkinManager mSkinManager = SkinManager.getInstance();
			mSettingItemParent.setBackground(mSkinManager.getDrawable(R.drawable.ripple_normal));
			mSettingItemValue.setTextColor(mSkinManager.getColor(R.color.light));
			mSettingItemTitle.setTextColor(mSkinManager.getColor(R.color.accent));
			mSettingItemDescription.setTextColor(mSkinManager.getColor(R.color.light));
			mSettingItemTo.setImageTintList(ColorStateList.valueOf(mSkinManager.getColor(R.color.accent)));
		}
	}
	
}

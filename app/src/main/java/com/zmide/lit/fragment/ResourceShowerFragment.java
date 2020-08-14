package com.zmide.lit.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.ScreenUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.zmide.lit.R;
import com.zmide.lit.adapter.ResourceAdapter;
import com.zmide.lit.main.ResourceCatcher;
import com.zmide.lit.object.WebsiteSetting;
import com.zmide.lit.view.MNestedScrollView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.ArrayList;
import java.util.List;

public class ResourceShowerFragment extends BottomSheetDialogFragment {
	private BottomSheetBehavior mBehavior;
	private String[] mTitleDataList = {"全部", "视频", "音频", "图像", "JS", "CSS", "其他"};
	private int toolBarPositionY;
	
	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
		View view = View.inflate(getContext(), R.layout.bottom_sheet_resource, null);
		
		dialog.setContentView(view);
		mBehavior = BottomSheetBehavior.from((View) view.getParent());
		return dialog;
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
	}
	
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = View.inflate(getContext(), R.layout.bottom_sheet_resource, null);
		ViewPager mViewPager = view.findViewById(R.id.view_pager);
		MagicIndicator magicIndicator = view.findViewById(R.id.magic_indicator);
		//MNestedScrollView scrollView = view.findViewById(R.id.scroll_view);
		CommonNavigator commonNavigator = new CommonNavigator(getContext());
		commonNavigator.setAdapter(new CommonNavigatorAdapter() {
			
			@Override
			public int getCount() {
				return mTitleDataList == null ? 0 : mTitleDataList.length;
			}
			
			@Override
			public IPagerTitleView getTitleView(Context context, final int index) {
				ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
				colorTransitionPagerTitleView.setNormalColor(Color.GRAY);
				colorTransitionPagerTitleView.setSelectedColor(Color.BLACK);
				colorTransitionPagerTitleView.setText(mTitleDataList[index]);
				colorTransitionPagerTitleView.setOnClickListener(view1 -> mViewPager.setCurrentItem(index));
				return colorTransitionPagerTitleView;
			}
			
			@Override
			public IPagerIndicator getIndicator(Context context) {
				LinePagerIndicator indicator = new LinePagerIndicator(context);
				indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
				return indicator;
			}
		});
		magicIndicator.setNavigator(commonNavigator);
		ViewPagerHelper.bind(magicIndicator, mViewPager);
		mViewPager.setAdapter(new PagerAdapter(getChildFragmentManager(), new int[]{0, 1, 2, 3, 4, 5, 6}));
		mViewPager.post(() -> {
			toolBarPositionY = magicIndicator.getHeight();
			ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
			params.height = ScreenUtils.getScreenHeight() - toolBarPositionY - magicIndicator.getHeight()+1;
			mViewPager.setLayoutParams(params);
		});
		return view;
	}
	
	@Override
		public void onStart()
		{
			super.onStart();
			//默认全屏展开
			mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
		}
		
		
		
		public void doClick(View v)
		{
			//点击任意布局关闭
			mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
		}
	
	public static class PagerAdapter extends FragmentPagerAdapter
	{
		private int[] indicators;
		private Fragment pageFragment;
		public PagerAdapter(FragmentManager fm,int[] indicator_id)
		{
			super(fm,FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
			indicators = indicator_id;
		}
		@NonNull
		@Override
		public Fragment getItem(int position) {
			return new ResourceFragment(indicators[position]);
		}
		
		@Override
		public int getCount() {
			return indicators.length;
		}
		
		public static class ResourceFragment extends Fragment{
			private int id;
			
			public ResourceFragment(int id){
				this.id = id;
			}
			
			
			@Override
			public void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
			}
			
			@Override
			public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
				List<Uri> urls = ResourceCatcher.getResources(id);
				View view = inflater.inflate(R.layout.fragment_resource, container, false);
				TextView empty = view.findViewById(R.id.empty);
				RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
				if (urls.size()==0){
					empty.setVisibility(View.VISIBLE);
					recyclerView.setVisibility(View.GONE);
				}else {
					empty.setVisibility(View.GONE);
					recyclerView.setVisibility(View.VISIBLE);
					//recyclerView.setNestedScrollingEnabled(true);// 解决滑动冲突
					recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
					ResourceAdapter adapter = new ResourceAdapter(inflater.getContext(), urls);
					recyclerView.setAdapter(adapter);
				}
				return view;
			}
			
			@Override
			public void onAttach(@NonNull Context context) {
				super.onAttach(context);
			}
			
			@Override
			public void onDetach() {
				super.onDetach();
			}
		}
	}
}

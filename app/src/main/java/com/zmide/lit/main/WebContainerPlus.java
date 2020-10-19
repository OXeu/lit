package com.zmide.lit.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.zmide.lit.interfaces.WindowsInterface;
import com.zmide.lit.object.Diy;
import com.zmide.lit.object.Index;
import com.zmide.lit.object.WebState;
import com.zmide.lit.ui.MainActivity;
import com.zmide.lit.util.MDataBaseSettingUtils;
import com.zmide.lit.util.MWebStateSaveUtils;
import com.zmide.lit.util.ViewO;
import com.zmide.lit.view.LitWebView;
import java.util.ArrayList;
import java.util.Objects;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.zmide.lit.adapter.web.WebAdapter;
import com.zmide.lit.helper.WindowsLayoutManager;
import com.zmide.lit.helper.WindowsSnapHelper;

public class WebContainerPlus {

	/*
	 WebContainer需要实现的方法：
	 #数据保存：数据一旦变动，立即保存至数据库
	 #数据恢复：提供公开方法从数据库恢复数据
	 #WebView 防空：公用getWebView方法，获取当前WebView，并进行防空判断，若WebView为空，则通过数据库恢复
	 #创建新标签页，并发出数据变动通知
	 #移除新标签页，并发出通知
	 #切换当前标签页，并将该WebView插入最前方
	 #获取所有Windows，便于多窗口处理

	 #WebContainer最大的任务，就是防止Exception发生


	 ##通过分析WebContainer代码，使用静态类管理RecyclerView和其Adapter是最好的办法
	 # 需要初始化
	 # 滑动切换即按下解锁RecyclerView允许滑动，滑动，松手锁定滑动(自动对齐)
	 
	 #首页改造完成
	 
	 10.11任务
	 完成获取WebView等WebContainerPlus的重构
	 */
	private static WindowsInterface mWindowsInterface;
	
	private static MainActivity activity;
	private static RecyclerView rv ;
	private static WindowsLayoutManager layoutManager;
	private static WebAdapter adapter;

	private static WindowsSnapHelper snapHelper;

	public static void init(MainActivity activityTemp, Intent intent) {
		if (activity == null) {
			activity = activityTemp;
			rv = MainViewBindUtils.getWebRecyclerView();
			layoutManager = new WindowsLayoutManager();
			layoutManager.setHeightScale(1);
			layoutManager.setWidthScale(1);
			layoutManager.setInfinite(true);
			rv.setLayoutManager(layoutManager);
			
			adapter = new WebAdapter(activity);
			rv.setAdapter(adapter);
			
			snapHelper = new WindowsSnapHelper();
			snapHelper.setInfinite(true);
			snapHelper.attachToRecyclerView(rv);
			
			//设置适配器
			mWindowsInterface = activityTemp;
		}
		if (intent != null)
		initWebs(intent);
	}
	
	
	//获取当前ViewHolder
	public static WebAdapter.MyViewHolder getViewHolder(int position) {
                    //得到要更新的item的view
		View view = rv.getLayoutManager().findViewByPosition(position);
            if (null != rv.getChildViewHolder(view)) {
                WebAdapter.MyViewHolder viewHolder = (WebAdapter.MyViewHolder) rv.getChildViewHolder(view);
                //do something
				return viewHolder;
            }
		
		return null;
    }
	
	
	public static WebAdapter.MyViewHolder getViewHolder() {
		return getViewHolder(getWindowId());
    }
	
	public static MWeb getWindow(WebView webView) {
		ArrayList<MWeb> mWebs = adapter.getMWebs();
		for (MWeb mWeb : mWebs) {
			if (Objects.equals(getWebView(mWeb), webView))
				return mWeb;
		}
		return createWindow(webView);
	}

	public static MWeb getWindow() {
		ArrayList<MWeb> mWebs = adapter.getMWebs();
		MWeb web = mWebs.get(getWindowId());
		if (web != null)
			return web;
		else {
			return createWindow(MDataBaseSettingUtils.getSingleSetting(Diy.WEBPAGE), getWindowId());
		}
	}
	
	
	public static LitWebView getWebView() {
		return getWindow().getWebView();
	}

	public static LitWebView getWebView(MWeb mWeb) {
		return mWeb.getWebView();
	}
	
	public static MWeb createWindow(String url, boolean isOnTop) {
		MWeb web = new MWeb(activity);
		if (url != null && !Objects.equals(url, "")) {
			web.getWebView().loadUrl(url);
		}else{
			web.getWebView().loadUrl(MDataBaseSettingUtils.getSingleSetting(Diy.WEBPAGE));
		}
		if (isOnTop) {
			adapter.createWindow(web);
			switchWindow(getWindowPosition(web));
		} else {
			adapter.createWindow(getWindowId() + 1, web);
		}
		mWindowsInterface.onWindowsCountChanged(adapter.getItemCount());
		return web;
	}

	public static MWeb createWindow(String url, int id) {
		MWeb web = new MWeb(activity);
		if (url != null && !Objects.equals(url, "")) {
			web.getWebView().loadUrl(url);
		}
		web.getWebView().setCodeId(id);
		adapter.createWindow(web);
		mWindowsInterface.onWindowsCountChanged(adapter.getItemCount());
		return web;
	}

	public static MWeb createWindow(WebView webView) {
		
		//包装裸露的WebView
		MWeb web = new MWeb(activity, webView);
		adapter.createWindow(web);
		mWindowsInterface.onWindowsCountChanged(adapter.getItemCount());
		return web;
	}
	
	public static ArrayList<MWeb> getAllWindows() {
		return adapter.getMWebs();
	}
	
	
	public static void resumeData() {
		ArrayList<WebState> states = MWebStateSaveUtils.resumeAllStates();
		if (states != null) {
			if (states.size() != 0) {
				for (WebState state : states) {
					createWindow(state.url, state.sid);
				}
				removeWindow(0);
				changeWindow(0);
			}
		}
	}
	
	public static void removeWindow(int wid) {
		if (wid < adapter.getItemCount()) {
			MWebStateSaveUtils.deleteStates(adapter.getMWeb(wid).getCode());
			if (wid == getWindowId()) {//移除当前Web
				adapter.removeWindow(wid);
				if (wid == 0)
					switchWindow(0);
				else if (wid >= adapter.getItemCount())
					switchWindow(adapter.getItemCount() - 1);
				else
					switchWindow(wid - 1);
			} else {//移除其他Web
				MWeb web = getWindow();
				adapter.removeWindow(wid);
				switchWindow(adapter.getIndex(web));
			}
			mWindowsInterface.onWindowsCountChanged(adapter.getItemCount());
		}
	}
	
	public static int getWindowId() {
		if (adapter.getItemCount() > 0){
			int index =  layoutManager.findFirstVisibleItemPosition();
			if(index>0)
				return index;
		}else
			createWindow(null,true);
		return 0;
	}

	public static int getWindowCount() {
		return adapter.getItemCount();
	}


	public static String getUrl() {
		return getWindow().getUrl();
	}
	
	
	public static boolean isIndex() {
		return Objects.equals(getWebView().getUrl(), MDataBaseSettingUtils.getSingleSetting(Diy.WEBPAGE));
	}

	public static void loadUrl(String url) {
		getWebView().loadUrl(url);
	}

	public static String getTitle() {
		return getWebView().getTitle();
	}
	
	/**
     * 目标项是否在最后一个可见项之后
     */
    private boolean mShouldScroll;
    /**
     * 记录目标项位置
     */
    private int mToPosition;

    /**
     * 滑动到指定位置
     *
     * @param mRecyclerView
     * @param position
     */
    private static void smoothMoveToPosition(RecyclerView mRecyclerView, final int position) {
		mRecyclerView.smoothScrollToPosition(position);
    }

	private static void switchWindow(int wid) {
		smoothMoveToPosition(rv,wid);
	}


	public static void changeWindow(int wid) {
		if(getWindowId() >= getWindowCount() - 1 && wid == 1)//最后一个页面,且向后滑
			switchWindow(0);
		else if(getWindowId() <= 0 && wid == -1)//第一个页面,且向前滑
			switchWindow(getWindowCount() - 1);
		else
			switchWindow(getWindowId() + wid);
	}
	
	
	
	
	
	
	
	
	
	
	

	//初始化窗口，用于首次启动初始化
	//参数 外部打开链接的 Intent 参数
	private static void initWebs(Intent intent) {
		String url = null;
		Bundle extra = intent.getExtras();
		if (Intent.ACTION_WEB_SEARCH.equals(intent.getAction())) {
			if (extra != null) {
				String query = extra.getString("query");
				SearchEnvironment.Search(query != null ? query : "");
			}
		} else {
			if (intent.getData() != null) {
				url = intent.getData().toString();
			} else if (extra != null) {
				url = extra.getString("url");
			}
		}
		if (url != null) {
			if (intent.getBooleanExtra("ifNew", true))
			createWindow(url, true);
			else
			loadUrl(url);
		}
		if (adapter.getItemCount()==0) {
			createWindow(null, true);
			//WebEnvironment.refreshFrame();
			WindowsManager.hideWindows();
		}
	}

	
	

	


	

	
	

	
	
	/* 1 2 3 4 5 6 7   4->6 size=7 , expectId=6 ,7-1<6不成立，正常
	 * 0 1 2 3 4 5 6   //检验
	 * 1 2 3 5 6 7
	 * 0 1 2 3 4 5 */
	private static void swapIndex(int wid, int expectId) {
		MWeb webTemp = adapter.getMWeb(wid);
		if (webTemp == null || adapter.getItemCount() - 1 < expectId) {
			//该Web不存在，或者指针溢出，说明出现了问题，通知防爆组
			sortContainer();
		} else {
			adapter.createWindow(expectId, adapter.removeWindow(wid));
		}
	}


	//Todo 只有这个无关紧要的东西了
	private static void sortContainer() {
		//整理Container
		/* * 已知问题有
		 * 指针异常（空指针，溢出）均会调用该方法
		 * WebView不存在（通过包装解决，sort不进行处理）
		 */
	}


	public static void setRefreshing(boolean isRefreshing) {
		getWindow().getSwipe().setRefreshing(isRefreshing);
	}

	public static int getWindowPosition(MWeb web) {
		return adapter.getIndex(web);
	}

	public static View getView() {
		return getWindow().getView();
	}
}

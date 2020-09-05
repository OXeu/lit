package com.zmide.lit.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

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

public class WebContainer {
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
	 */
    private static ArrayList<MWeb> mWebs = new ArrayList<>();
    private static WindowsInterface mWindowsInterface;
    private static Index webIndex = new Index();
    private static MainActivity activity;

    public static void init(MainActivity activityTemp, Intent intent) {
		if (activity == null) {
			activity = activityTemp;
			mWindowsInterface = activityTemp;
		}
		if (intent != null)
			initWebs(intent);
	}


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
        if (mWebs.isEmpty()) {
            createWindow(null, true);
            WebEnvironment.refreshFrame();
            WindowsManager.hideWindows();
        }
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
	
	public static LitWebView getWebView() {
		return getWindow().getWebView();
	}
	
	public static LitWebView getWebView(MWeb mWeb) {
		return mWeb.getWebView();
	}
	
	public static ArrayList<MWeb> getAllWindows() {
		return mWebs;
	}
	
	
	public static MWeb createWindow(String url, boolean isOnTop) {
		MWeb web = new MWeb(activity);
		if (url != null && !Objects.equals(url, "")) {
			web.getWebView().loadUrl(url);
		}else{
			web.getWebView().loadUrl(MDataBaseSettingUtils.getSingleSetting(Diy.WEBPAGE));
		}
		if (isOnTop) {
			mWebs.add(web);
			switchWindow(getWindowPosition(web));
		} else {
			mWebs.add(getWindowId() + 1, web);
		}
		mWindowsInterface.onWindowsCountChanged(mWebs.size());
		return web;
	}
	
	public static MWeb createWindow(String url, int id) {
		MWeb web = new MWeb(activity);
		if (url != null && !Objects.equals(url, "")) {
			web.getWebView().loadUrl(url);
		}
		web.getWebView().setCodeId(id);
		mWebs.add(web);
		mWindowsInterface.onWindowsCountChanged(mWebs.size());
		return web;
	}
	
	public static MWeb createWindow(WebView webView) {
		//包装裸露的WebView
		MWeb web = new MWeb(activity, webView);
		mWebs.add(web);
		mWindowsInterface.onWindowsCountChanged(mWebs.size());
		return web;
	}
	
	public static void removeWindow(int wid) {
		if (wid < mWebs.size()) {
			MWebStateSaveUtils.deleteStates(mWebs.get(wid).getCode());
			if (wid == getWindowId()) {//移除当前Web
				mWebs.remove(wid);
				if (wid == 0)
					switchWindow(0);
				else if (wid >= mWebs.size())
					switchWindow(mWebs.size() - 1);
				else
					switchWindow(wid - 1);
			} else {//移除其他Web
				MWeb web = getWindow();
				mWebs.remove(wid);
				switchWindow(mWebs.indexOf(web));
			}
			mWindowsInterface.onWindowsCountChanged(mWebs.size());
		}
	}
	
	public static MWeb getWindow(WebView webView) {
		for (MWeb mWeb : mWebs) {
			if (Objects.equals(getWebView(mWeb), webView))
				return mWeb;
		}
		return createWindow(webView);
	}
	
	public static MWeb getWindow() {
		MWeb web = mWebs.get(getWindowId());
		if (web != null)
			return web;
		else {
			return createWindow(MDataBaseSettingUtils.getSingleSetting(Diy.WEBPAGE), getWindowId());
		}
	}
	
	public static int getWindowId() {
		if (mWebs.size() != 0)
		return webIndex.getIndex();
		else
			createWindow(null,true);
		return 0;
	}
	
	public static void setWindowId(int id) {
		webIndex.setIndex(id);
	}
	
	public static int getWindowCount() {
		return mWebs.size();
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
	
	
	public static void switchWindow(int wid) {
		setWindowId(wid);
		WebEnvironment.refreshFrame();
        if (getWebView() != null)
            if (!Objects.equals(MDataBaseSettingUtils.WebIndex, getWebView().getUrl())) {
                IndexEnvironment.hideIndex();
                ViewO.showView(getWebView());
            } else
                IndexEnvironment.showIndex();
	}
	
	
	public static void changeWindow(int wid) {
		if(getWindowId() >= getWindowCount() - 1 && wid == 1)//最后一个页面,且向后滑
			switchWindow(0);
		else if(getWindowId() <= 0 && wid == -1)//第一个页面,且向前滑
			switchWindow(getWindowCount() - 1);
		else
			switchWindow(getWindowId() + wid);
	}
	
	/* 1 2 3 4 5 6 7   4->6 size=7 , expectId=6 ,7-1<6不成立，正常
	 * 0 1 2 3 4 5 6   //检验
	 * 1 2 3 5 6 7
	 * 0 1 2 3 4 5 */
	private static void swapIndex(int wid, int expectId) {
		MWeb webTemp = mWebs.get(wid);
		if (webTemp == null || mWebs.size() - 1 < expectId) {
			//该Web不存在，或者指针溢出，说明出现了问题，通知防爆组
			sortContainer();
		} else {
			mWebs.add(expectId, mWebs.remove(wid));
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
		return mWebs.indexOf(web);
	}
	
	public static View getView() {
		return getWindow().getView();
	}
}

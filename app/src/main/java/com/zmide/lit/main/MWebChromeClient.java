package com.zmide.lit.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Message;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.zmide.lit.util.MBitmapUtils;
import com.zmide.lit.util.MExceptionUtils;
import com.zmide.lit.util.MFileUtils;
import com.zmide.lit.util.MWindowsUtils;
import com.zmide.lit.util.ViewO;

import static com.zmide.lit.main.MainViewBindUtils.getBallCardView;


public class MWebChromeClient extends WebChromeClient {
	private Activity a;
	private View mCustomView;
	private CustomViewCallback mCustomViewCallback;
	
	public MWebChromeClient(Activity activity) {
		this.a = activity;
	}
	
	@Override
	public void onProgressChanged(WebView view, int newProgress) {
		super.onProgressChanged(view, newProgress);
		MProgressManager.setProgress(newProgress);
//					if(((BaseActivity)a).isNight()){
//						view.evaluateJavascript(nightJs,null);
//					}
	}
	
	@Override
	public void onReceivedTitle(WebView view, String title) {
		super.onReceivedTitle(view, title);
		try {
			MWeb web = WebContainer.getWindow(view);
			if (web != null) {
				int p = WebContainer.getWindowPosition(web);
				if (p == WebContainer.getWindowId())
					MainViewBindUtils.getMainTitle().setText(title);
				web.setTitle(title);
			}
		} catch (Exception e) {
			MExceptionUtils.reportException(e);
		}
	}
	
	@Override
	public void onReceivedIcon(WebView view, Bitmap bitmap) {
		super.onReceivedIcon(view, bitmap);
		try {
			Bitmap bm2 = MBitmapUtils.bitmapRound(bitmap, 15f);
			MWeb web = WebContainer.getWindow(view);
			if (web != null) {
				web.setIcon(MFileUtils.saveFile(bm2, null, false));
			}
			/*
			WebContainer.get.set(WebContainer.getWindowPosition(web), web);
			mAdapter.updateWeb(webs);
			mAdapter.notifyDataSetChanged();*/
		} catch (Exception ignore) {
		}
	}
	
	@SuppressLint("SourceLockedOrientationActivity")
	@Override
	public void onShowCustomView(View view, CustomViewCallback callback) {
		super.onShowCustomView(view, callback);
		try {
            if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }
            mCustomView = view;
			WebContainerPlus.getViewHolder().getWebFrame().addView(mCustomView);
            mCustomViewCallback = callback;
            //mWebView.setVisibility(View.GONE);
            ViewO.hideView(getBallCardView());
            MWindowsUtils.switchFullScreen(a, true);
            ScreenUtils.setLandscape(a);
            BarUtils.setNavBarVisibility(a, false);
        } catch (Exception e) {
			MExceptionUtils.reportException(e);
		}
	}
	
	@SuppressLint("SourceLockedOrientationActivity")
	@Override
	public void onHideCustomView() {
        super.onHideCustomView();
        ViewO.showView(getBallCardView());
        //mWebView.setVisibility(View.VISIBLE);
        if (mCustomView == null) {
            return;
        }
        MWindowsUtils.back2DefaultScreen(a);
        mCustomView.setVisibility(View.GONE);
        WebContainerPlus.getViewHolder().getWebFrame().removeView(mCustomView);
        mCustomViewCallback.onCustomViewHidden();
        mCustomView = null;
        BarUtils.setNavBarVisibility(a, true);
        ScreenUtils.setPortrait(a);
    }
	
	@Override
	public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
		WebView web2 = new WebView(a);//新创建一个webview
		web2.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				WebContainer.createWindow(url, true);//将拦截到url交由新WebView打开
				return true;
			}
		});
		WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
		//以下的操作应该就是让新的webview去加载对应的url等操作。
		transport.setWebView(web2);
		resultMsg.sendToTarget();
		return true;
	}
	
	@Override
	public void onCloseWindow(WebView view) {
		WebContainer.removeWindow(WebContainer.getWindowPosition(WebContainer.getWindow(view)));
	}
}

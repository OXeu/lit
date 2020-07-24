package com.zmide.lit.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.blankj.utilcode.util.ActivityUtils;
import com.zmide.lit.javascript.EasySearch;
import com.zmide.lit.javascript.LitJavaScript;
import com.zmide.lit.main.MScrollListener;
import com.zmide.lit.main.MWebChromeClient;
import com.zmide.lit.main.MWebViewClient;
import com.zmide.lit.object.Diy;
import com.zmide.lit.util.MDataBaseSettingUtils;
import com.zmide.lit.util.MDialogUtils;
import com.zmide.lit.util.MDownloadManager;
import com.zmide.lit.util.MFileUtils;
import com.zmide.lit.util.MSharedPreferenceUtils;
import com.zmide.lit.util.MToastUtils;
import org.adblockplus.libadblockplus.android.webview.AdblockWebView;

import java.util.Objects;

public class LitWebView extends AdblockWebView {
	
	private Activity activity = ActivityUtils.getActivityByView(this);
	private OnScrollChangeListener mOnScrollChangeListener;
	private int codeId = hashCode();
	private int oldY;
	private SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener = (sharedPreferences, key) -> {
		initWebView("");
	};
	
	public LitWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initWebView(null);
		MSharedPreferenceUtils.getWebViewSharedPreference().registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
	}
	
	public int getCodeId() {
		return codeId;
	}
	
	public void setCodeId(int code) {
		this.codeId = code;
	}
	
	public void initWebView(String url) {
		WebSettings webSettings = getSettings();
		webSettings.setJavaScriptEnabled(MSharedPreferenceUtils.getWebViewSharedPreference().getString("javascript", "true").equals("true"));
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		webSettings.setUserAgentString(MSharedPreferenceUtils.getWebViewSharedPreference().getString("ua", "") + "");
		
		addJavascriptInterface(LitJavaScript.getInstance(), "via");
		addJavascriptInterface(EasySearch.getInstance(), "Viaduct");//简单搜索Js连接桥
		addJavascriptInterface(LitJavaScript.getInstance(), "lit");
		//缩放操作
		webSettings.setSupportZoom(MSharedPreferenceUtils.getWebViewSharedPreference().getString("zoom", "true").equals("true")); //支持缩放，默认为true。是下面那个的前提。
		webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该LitWebView不可缩放
		webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
		webSettings.setUseWideViewPort(true);
		webSettings.setDomStorageEnabled(true);
		webSettings.setAppCacheEnabled(true);
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);//支持js调用window.open方法
		webSettings.setAllowFileAccess(true);// 设置允许访问文件数据
		webSettings.setSupportMultipleWindows(true);// 设置允许开启多窗口
		int cacheMode;
		switch (Objects.requireNonNull(MSharedPreferenceUtils.getWebViewSharedPreference().getString("cache_mode", "0"))) {
			case "1":
				cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK;
				break;
			case "2":
				cacheMode = WebSettings.LOAD_NO_CACHE;
				break;
			case "3":
				cacheMode = WebSettings.LOAD_CACHE_ONLY;
				break;
			default:
				cacheMode = WebSettings.LOAD_DEFAULT;
				break;
		}
		webSettings.setCacheMode(cacheMode);
		webSettings.setTextZoom(Integer.parseInt("0" + MSharedPreferenceUtils.getWebViewSharedPreference().getString("webfont", "1")) * 50 + 50);
		webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
		setDownloadListener((url1, userAgent, contentDisposition, mimetype, contentLength) ->
				MDownloadManager.downloadFile(activity, url1, userAgent, contentDisposition, mimetype, contentLength));
		setOnLongClickListener(v -> {
			MDialogUtils.Builder builder = new MDialogUtils.Builder(activity);
			HitTestResult result = getHitTestResult();
			if (null == result)
				return false;
			int type = result.getType();
			switch (type) {
				case HitTestResult.EDIT_TEXT_TYPE: // 选中的文字类型
					break;
				case HitTestResult.PHONE_TYPE: // 处理拨号
					break;
				case HitTestResult.EMAIL_TYPE: // 处理Email
					break;
				case HitTestResult.GEO_TYPE: // 　地图类型
					break;
				case HitTestResult.SRC_ANCHOR_TYPE: // 超链接
					break;
				case HitTestResult.SRC_IMAGE_ANCHOR_TYPE: // 带有链接的图片类型
				case HitTestResult.IMAGE_TYPE: // 处理长按图片的菜单项
					final String url12 = result.getExtra();
					String fileName = URLUtil.guessFileName(url12, null, "image/*");
					try {
						if (!TextUtils.isEmpty(fileName)) {
							String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
							if (!MimeTypeMap.getSingleton().hasExtension(suffix)) {
								suffix = MimeTypeMap.getSingleton().getExtensionFromMimeType("image/*");
								TextUtils.isEmpty(suffix);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (URLUtil.isValidUrl(url12))
						builder.setMessage(url12);
					else
						builder.setMessage("Base64图片");
					builder.setDownloadLink(url12)
							.setTitle("图片")
							.setNegativeButton("关闭", (dialog1, which) -> dialog1.cancel())
							.setPositiveButton("保存", (dialog12, which) -> {
								if (URLUtil.isValidUrl(url12))
									MDownloadManager.downloadFile(activity, url12, (url12 + "emptyFileName").substring(0, 10) + ".png");
								else if (url12 != null && url12.contains("base64")) {
									MFileUtils.saveBase64Picture(activity, url12);
								} else {
									MToastUtils.makeText("保存失败，该图片暂不支持保存", MToastUtils.LENGTH_SHORT).show();
								}
								dialog12.cancel();
							})
							.create().show();
					//if (mOnSelectItemListener != null && url != null && URLUtil.isValidUrl(url))
					//	 mOnSelectItemListener.onSelected(touchX, touchY, result.getType(), url);
					return false;
				case HitTestResult.UNKNOWN_TYPE: //未知
					break;
			}
			return false;
		});
		
		setOnScrollChangeListener(new MScrollListener());
		setWebViewClient(new MWebViewClient(activity));
		setWebChromeClient(new MWebChromeClient(activity));
		
		
		if (url != null && !url.equals(""))
			loadUrl(url);
		else if (url == null)
			loadUrl(MDataBaseSettingUtils.getSingleSetting(Diy.WEBPAGE));
	}
	
	@Override
	public void setWebChromeClient(WebChromeClient client) {
		super.setWebChromeClient(client);
		
	}
	
	@Override
	protected void onScrollChanged(int l, int y, int oldl, int oldt) {
		super.onScrollChanged(l, y, oldl, oldt);
		// webview的高度
		//float webcontent = getContentHeight() * getScale();
		// 当前webview的高度
		//float webnow = getHeight() + getScrollY();
		if (y - oldY < -300) {
			oldY = y;
			mOnScrollChangeListener.onPageToTop();
		} else if (y - oldY > 300) {
			oldY = y;
			mOnScrollChangeListener.onPageToBottom();
		}
	}
	
	public void setOnScrollChangeListener(OnScrollChangeListener listener) {
		this.mOnScrollChangeListener = listener;
	}
	
	
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (this.getScrollY() <= 0)
				this.scrollTo(0, 1);
		}
		return super.onTouchEvent(event);
	}
	
	public interface OnScrollChangeListener {
		
		void onPageToTop();
		
		void onPageToBottom();
		
	}
	
}

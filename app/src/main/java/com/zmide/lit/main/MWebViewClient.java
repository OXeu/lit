package com.zmide.lit.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.BarUtils;
import com.zmide.lit.object.Diy;
import com.zmide.lit.object.WebsiteSetting;
import com.zmide.lit.util.DBC;
import com.zmide.lit.util.WebsiteUtils;
import com.zmide.lit.util.MDialogUtils;
import com.zmide.lit.util.MFileUtils;
import com.zmide.lit.util.MSharedPreferenceUtils;
import com.zmide.lit.util.MToastUtils;
import com.zmide.lit.util.MWebStateSaveUtils;
import com.zmide.lit.view.LitWebView;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static com.zmide.lit.main.IndexEnvironment.hideIndex;
import static com.zmide.lit.main.IndexEnvironment.showIndex;
import static com.zmide.lit.main.StatusEnvironment.updateStatusColor;
import static com.zmide.lit.util.MDataBaseSettingUtils.WebIndex;

public class MWebViewClient extends WebViewClient {
	private Activity a;
	
	public MWebViewClient(Activity mainActivity) {
		this.a = mainActivity;
		WebsiteUtils.loadWebsiteSettings(a);
	}
	
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, final String url) {
		// 处理自定义scheme
		String domain = WebsiteUtils.getDomain(WebContainer.getUrl());
		WebsiteSetting websiteSetting = WebsiteUtils.getWebsiteSetting(a,domain);
		if (!url.startsWith("http")
				&& !url.startsWith("file")
				&& ((MSharedPreferenceUtils.getWebViewSharedPreference().getString("oapp", "true").equals("true")
					&&!websiteSetting.state)||(websiteSetting.state&&websiteSetting.app))
			) {
			try {
				// 以下固定写法
				MToastUtils.makeText(a,"是否允许打开外部应用", "允许", v -> {
					try {
						//处理intent协议
						if (url.startsWith("intent://")) {
							Intent intent;
							try {
								intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
								intent.addCategory("android.intent.category.BROWSABLE");
								intent.setComponent(null);
								intent.setSelector(null);
								List<ResolveInfo> resolves = a.getPackageManager().queryIntentActivities(intent, 0);
								if (resolves.size() > 0) {
									a.startActivityIfNeeded(intent, -1);
								}
							} catch (URISyntaxException e) {
								e.printStackTrace();
							}
						}
						// 处理自定义scheme协议
						if (!url.startsWith("http")) {
							try {
								// 以下固定写法
								final Intent intent = new Intent(Intent.ACTION_VIEW,
										Uri.parse(url));
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
										| Intent.FLAG_ACTIVITY_SINGLE_TOP);
								a.startActivity(intent);
							} catch (Exception e) {
								// 防止没有安装的情况
								MToastUtils.makeText(a,"没有可执行此操作的客户端", "详细", v1 -> {
									MDialogUtils.Builder dialog = new MDialogUtils.Builder(a);
									dialog.setDownloadLink(url);
									dialog.setTitle("详细信息")
											.setMessage(url)
											.setPositiveButton("关闭", (di, p2) -> di.cancel())
											.create().show();
								}, 2000)
										.show();
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}, MToastUtils.LENGTH_LONG).show();
				
			} catch (Exception e) {
				// 防止没有安装的情况
				e.printStackTrace();
				MToastUtils.makeText("没有可执行此操作的客户端", MToastUtils.LENGTH_LONG)
						.show();
			}
			return true;
			
			
		}
		return false;
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onPageStarted(WebView view, String url, Bitmap icon) {
		//view.loadUrl(jss);
					/*
					 if(((BaseActivity)a).getThemeMode()==1){
					 view.evaluateJavascript("javascript:"+FileReader.getJS(a,"expand/func.js"),null);
					 view.evaluateJavascript("javascript:litDark()",null);
					 }
					 */
		if (!url.equals(WebIndex)) {
			hideIndex();
			updateStatusColor(view);
			MWebStateSaveUtils.saveState(((LitWebView) view).getCodeId(), url);
		} else
			showIndex();
		WebSettings settings = view.getSettings();
		String domain = WebsiteUtils.getDomain(WebContainer.getUrl());
		WebsiteSetting websiteSetting = WebsiteUtils.getWebsiteSetting(a,domain);
		
		//无图模式
		if ((websiteSetting.state&&websiteSetting.no_picture) ||
				(!websiteSetting.state
						&& MSharedPreferenceUtils.getWebViewSharedPreference().getString("no_picture", "false").equals("true")
				)) {
			settings.setLoadsImagesAutomatically(false);
		}else {
			settings.setLoadsImagesAutomatically(true);
		}
		
		/*//广告拦截
		if ((websiteSetting.state&&websiteSetting.ad_host) ||
				(!websiteSetting.state
						&& MSharedPreferenceUtils.getWebViewSharedPreference().getString("ad_host", "true").equals("true")
				))
			settings.setBlockNetworkImage(true);//Todo
		else
			settings.setBlockNetworkImage(false);//Todo ad host
		*/
		//允许js
		if ((websiteSetting.state&&websiteSetting.js) ||
				(!websiteSetting.state
						&& MSharedPreferenceUtils.getWebViewSharedPreference().getString("javascript", "true").equals("true")
				))
			settings.setJavaScriptEnabled(true);
		else
			settings.setJavaScriptEnabled(false);
		
		String ua = "";
		if (websiteSetting.state&&websiteSetting.ua!=0) {//自定义UA
			if (DBC.getInstance(a).isDiyExist(websiteSetting.ua + "")) {
				ua = (DBC.getInstance(a).getDiy(Diy.UA, websiteSetting.ua + "").value);
			} else {
				ua = (DBC.getInstance(a).getDiy(Diy.UA).value);
			}
		}else{
			ua = (DBC.getInstance(a).getDiy(Diy.UA).value);
		}
			settings.setUserAgentString(ua);
		
		ResourceCatcher.clearResources();
	}
	
	@Override
	public void onPageFinished(WebView view, String url) {
					/*
					 if(((BaseActivity)a).getThemeMode()==1){
					 view.evaluateJavascript("javascript:"+FileReader.getJS(a,"expand/func.js"),null);
					 view.evaluateJavascript("javascript:litDark()",null);

					 }*/
		MProgressManager.setProgress(100);
		WebContainer.setRefreshing(false);
		if (!url.equals(WebIndex))
			updateStatusColor(view);
		else {
			BarUtils.transparentStatusBar(a);
			BarUtils.setStatusBarColor(a, 0x00000000);
		}
		if (view.getUrl() == null)
			return;
		String domain = WebsiteUtils.getDomain(WebContainer.getUrl());
		WebsiteSetting websiteSetting = WebsiteUtils.getWebsiteSetting(a,domain);
		//不是本地页面 并且 网站独立设置关闭&无痕已关闭 或者 网站独立设置打开&独立设置无痕已关闭
		if (
				(!view.getUrl().startsWith("file://"))
				&&
				(
						( !websiteSetting.state
								&&
								Objects.equals(MSharedPreferenceUtils.getSharedPreference().getString("no_history", "false"), "false")
						)
						||
						( websiteSetting.state && !websiteSetting.no_history )
				)
			) {
			DBC.getInstance(a).addHistory(view.getTitle(), MFileUtils.saveFile(view.getFavicon(), null, false), view.getUrl());
		}
		for (Diy diy : DBC.getInstance(view.getContext()).getDiys(Diy.PLUGIN, false)) {
			Pattern httpPattern;
			String regex = diy.extra;
			//初始化正则
			if (regex == null)
				regex = "^\\s*";
			if (regex.equals(""))
				regex = "^\\s*";
			if (regex.startsWith("[#]")) {
				regex = regex.replace("[#]", "").replace("*", "[^\\s]*").replace(".", "\\.").replace(",", "|");
			}
			try {
				httpPattern = Pattern.compile(regex + "");
			} catch (PatternSyntaxException e) {
				if (MSharedPreferenceUtils.getWebViewSharedPreference().getString("pf", "false").equals("true"))
					MToastUtils.makeText(diy.title + " 作用域异常，无法执行", MToastUtils.LENGTH_SHORT).show();
				continue;
			}
			//开始判断了
			if (diy.isrun && httpPattern.matcher(url).find()) {//Run
				view.evaluateJavascript(diy.value, null);
				if (MSharedPreferenceUtils.getWebViewSharedPreference().getString("ps", "false").equals("true"))
					MToastUtils.makeText("脚本已执行", MToastUtils.LENGTH_SHORT).show();
			}
		}
	}
	
	@Nullable
	@Override
	public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
		
		ResourceCatcher.sendResource(request.getUrl());
		return null;
	}
}

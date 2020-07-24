package com.zmide.lit.util;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zmide.lit.R;
import com.zmide.lit.adapter.WebsiteSettingAdapter;
import com.zmide.lit.animation.Slide;
import com.zmide.lit.object.WebsiteSetting;
import com.zmide.lit.skin.SkinFactory;

import java.util.ArrayList;
import java.util.Objects;

public class MWebsiteSettingDialog extends Dialog {
		public MWebsiteSettingDialog(Context context) {
			super(context);
		}
		
		public MWebsiteSettingDialog(Context context, int theme) {
			super(context, theme);
		}
		
		public static class Builder {
			private Context context;
			private String domain;
			private WebsiteSetting items;
			private String positiveButtonText;
			private View contentView;
			
			private String moreButtonText;
			private OnClickListener moreButtonClickListener;
			
			public Builder(Context context) {
				this.context = context;
			}

			
			public com.zmide.lit.util.MWebsiteSettingDialog.Builder setItems(WebsiteSetting items) {
				this.items = items;
				return this;
			}
			public com.zmide.lit.util.MWebsiteSettingDialog create() {
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				// instantiate the dialog with the custom Theme
				final com.zmide.lit.util.MWebsiteSettingDialog dialog = new com.zmide.lit.util.MWebsiteSettingDialog(context);
				@SuppressLint("InflateParams") View layout = Objects.requireNonNull(inflater).inflate(R.layout.dialog_website, null);
				dialog.addContentView(layout, new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
				// set the dialog title
				((TextView) layout.findViewById(R.id.dialogTextTitle)).setText(items.site);
				RecyclerView recyclerView = layout.findViewById(R.id.dialogRv);
				// set the content message
				if (items != null) {
					WebsiteSettingAdapter adapter = new WebsiteSettingAdapter(context);
					adapter.addTip(items);
					recyclerView.setAdapter(adapter);
					LinearLayoutManager layoutManager = new LinearLayoutManager(context);
					layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
					recyclerView.setLayoutManager(layoutManager);
				} else if (contentView != null) {
					// if no message set
					// add the contentView to the dialog body
					((RelativeLayout) layout.findViewById(R.id.dialogTextParent))
							.removeAllViews();
					((RelativeLayout) layout.findViewById(R.id.dialogTextParent))
							.addView(contentView, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
									RelativeLayout.LayoutParams.WRAP_CONTENT));
				}
				dialog.setContentView(layout);
				//设置主题
				SkinFactory skinFactory = new SkinFactory();
				dialog.getLayoutInflater().setFactory2(skinFactory);
				skinFactory.apply();
				
				WindowManager.LayoutParams lp = Objects.requireNonNull(dialog.getWindow()).getAttributes();
				lp.gravity = Gravity.BOTTOM;
				lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
				lp.width = WindowManager.LayoutParams.MATCH_PARENT;
				lp.dimAmount = 0.2f;
				dialog.getWindow().setAttributes(lp);
				//设置该属性，dialog可以铺满屏幕
				dialog.getWindow().setBackgroundDrawable(null);
				Slide.slideToUp(layout);
				FrameLayout.LayoutParams lps = (FrameLayout.LayoutParams) layout.getLayoutParams();
				lps.setMargins(30, 30, 30, 30);
				layout.setLayoutParams(lps);
				return dialog;
			}
		}
	
}

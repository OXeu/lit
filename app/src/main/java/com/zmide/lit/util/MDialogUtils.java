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
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zmide.lit.R;
import com.zmide.lit.animation.Slide;
import com.zmide.lit.skin.SkinFactory;

import java.util.Objects;

/**
 * Created by LiBin on 2016/6/16.
 */
public class MDialogUtils extends Dialog {
	
	public MDialogUtils(Context context) {
		super(context);
	}
	
	public MDialogUtils(Context context, int theme) {
		super(context, theme);
	}
	
	public static class Builder {
		private Context context;
		private String title;
		private String message;
		private String positiveButtonText;
		private String downloadLink;
		private String negativeButtonText;
		private View contentView;
		private DialogInterface.OnClickListener positiveButtonClickListener;
		private DialogInterface.OnClickListener negativeButtonClickListener;
		
		private String moreButtonText;
		private OnClickListener moreButtonClickListener;
		
		public Builder(Context context) {
			this.context = context;
		}
		
		public Builder setDownloadLink(String downloadLink) {
			this.downloadLink = downloadLink;
			return this;
		}

		/*public String getDownloadLink()
		{
			return downloadLink;
		}
*/
		
		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}
		
		/*
		 * Set the Dialog message from resource
		 *
		 * @param
		 * @return
		 */
		/*public Builder setMessage(int message)
		{
			this.message = (String) context.getText(message);
			return this;
		}*/
		
		/**
		 * Set the Dialog title from resource
		 *
		 * @param title
		 * @return
		 */
		public Builder setTitle(int title) {
			this.title = (String) context.getText(title);
			return this;
		}
		
		/**
		 * Set the Dialog title from String
		 *
		 * @param title
		 * @return
		 */
		
		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}
		
		public Builder setContentView(View v) {
			this.contentView = v;
			return this;
		}
		
		/**
		 * Set the positive button resource and it's listener
		 *
		 * @param positiveButtonText
		 * @return
		 */
		/*public Builder setPositiveButton(int positiveButtonText,
										 DialogInterface.OnClickListener listener)
		{
			this.positiveButtonText = (String) context
				.getText(positiveButtonText);
			this.positiveButtonClickListener = listener;
			return this;
		}*/
		public Builder setPositiveButton(String positiveButtonText,
										 DialogInterface.OnClickListener listener) {
			this.positiveButtonText = positiveButtonText;
			this.positiveButtonClickListener = listener;
			return this;
		}

		/*public Builder setNegativeButton(int negativeButtonText,
										 DialogInterface.OnClickListener listener)
		{
			this.negativeButtonText = (String) context
				.getText(negativeButtonText);
			this.negativeButtonClickListener = listener;
			return this;
		}*/
		
		public Builder setNegativeButton(String negativeButtonText,
										 DialogInterface.OnClickListener listener) {
			this.negativeButtonText = negativeButtonText;
			this.negativeButtonClickListener = listener;
			return this;
		}
		
		public Builder setMoreButton(String moreButtonText,
									 DialogInterface.OnClickListener listener) {
			this.moreButtonText = moreButtonText;
			this.moreButtonClickListener = listener;
			return this;
		}
		
		public MDialogUtils create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			final MDialogUtils dialog = new MDialogUtils(context);
			@SuppressLint("InflateParams") View layout = Objects.requireNonNull(inflater).inflate(R.layout.dialog_text, null);
			dialog.addContentView(layout, new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
			// set the dialog title
			((TextView) layout.findViewById(R.id.dialogTextTitle)).setText(title);
			// set the confirm button
			if (positiveButtonText != null) {
				((TextView) layout.findViewById(R.id.dialogTextOk))
						.setText(positiveButtonText);
				if (positiveButtonClickListener != null) {
					layout.findViewById(R.id.dialogTextOk)
							.setOnClickListener(v -> {
								dialog.cancel();
								positiveButtonClickListener.onClick(dialog,
										DialogInterface.BUTTON_POSITIVE);
							});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.dialogTextOk).setVisibility(
						View.GONE);
			}
			// set the cancel button
			if (negativeButtonText != null) {
				((TextView) layout.findViewById(R.id.dialogTextCancel))
						.setText(negativeButtonText);
				if (negativeButtonClickListener != null) {
					layout.findViewById(R.id.dialogTextCancel)
							.setOnClickListener(v -> {
								dialog.cancel();
								negativeButtonClickListener.onClick(dialog,
										DialogInterface.BUTTON_NEGATIVE);
							});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.dialogTextCancel).setVisibility(
						View.GONE);
			}
			
			if (moreButtonText != null) {
				((TextView) layout.findViewById(R.id.dialogTextMore))
						.setText(moreButtonText);
				if (moreButtonClickListener != null) {
					layout.findViewById(R.id.dialogTextMore);
					layout.setOnClickListener(v -> {
						dialog.cancel();
						moreButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEUTRAL);
					});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.dialogTextMore).setVisibility(
						View.GONE);
			}
			
			//下载链接
			View down = layout.findViewById(R.id.dialogCopy);
			if (down != null)
				if (downloadLink != null) {
					down.setOnClickListener(view -> {
						Chiper.copy(downloadLink);
						dialog.dismiss();
					});
					down.setVisibility(View.VISIBLE);
				} else {
					down.setVisibility(View.INVISIBLE);
				}
			
			// set the content message
			if (message != null) {
				((TextView) layout.findViewById(R.id.dialogTextText)).setText(message);
				((TextView) layout.findViewById(R.id.dialogTextText)).setMovementMethod(ScrollingMovementMethod.getInstance());
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


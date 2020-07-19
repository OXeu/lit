package com.zmide.lit.skin;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.ArrayList;

/**
 * 自定义View的填充factory
 *
 * @author onexzgj
 */
public class SkinFactory implements LayoutInflater.Factory2 {
	
	private static final String[] sClassPrefixList = {
			"android.widget.",
			"android.view.",
			"android.webkit.",
			"android.support.v7.widget.",
	};
	//private Activity mActivity;
	private ArrayList<SkinViewItem> viewItems = new ArrayList<>();
	
	public SkinFactory() {
		// this.mActivity = activity;
	}
	
	@Override
	public View onCreateView(View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attributeSet) {
		
		View view = createView(name, context, attributeSet);
		
		if (view != null) {
			passSkinViewAttr(view, context, attributeSet);
		}
		return view;
	}
	
	@Override
	public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attributeSet) {
		
		View view = createView(name, context, attributeSet);
		
		if (view != null) {
			passSkinViewAttr(view, context, attributeSet);
		}
		return view;
	}
	
	
	/**
	 * 开始换肤的方法
	 */
	public void apply() {
		
		for (int i = 0; i < viewItems.size(); i++) {
			SkinViewItem skinViewItem = viewItems.get(i);
			skinViewItem.apply();
		}
		
		
	}
	
	
	/**
	 * 2,解析反射生成的View的属性
	 *
	 * @param view         view
	 * @param context      ctx
	 * @param attributeSet android:background=@drawable/bg
	 */
	private void passSkinViewAttr(View view, Context context, AttributeSet attributeSet) {
		ArrayList<SkinViewAttr> viewAttrs = new ArrayList<>();
		for (int i = 0; i < attributeSet.getAttributeCount(); i++) {
			try {
				//获取到属性的name,value
				String attrName = attributeSet.getAttributeName(i);   //background
				String attrValue = attributeSet.getAttributeValue(i);  //@12346622
				
				if (attrValue.contains("@") && !attrValue.equals("@null")) {
					
					int id = Integer.parseInt(attrValue.substring(1));   // 即R资源中对应的Id @12346622
					String typeName = context.getResources().getResourceTypeName(id);  //drawable
					String entryName = context.getResources().getResourceEntryName(id);  //bg
					SkinViewAttr attr = new SkinViewAttr(id, attrName, typeName, entryName);
					viewAttrs.add(attr);
					
				}
				if (!viewAttrs.isEmpty()) {
					//假如说属性不为空
					viewItems.add(new SkinViewItem(view, viewAttrs));
				}
			} catch (Exception e) {
				Log.i("SkinException", "value:" + attributeSet + " msg:" + e.getMessage() + " ALL:" + e.toString());
			}
		}
	}
	
	/**
	 * 1,通过反射创建View
	 *
	 * @param name         view的名称
	 * @param context      ctx
	 * @param attributeSet view的属性
	 * @return view
	 */
	private View createView(String name, Context context, AttributeSet attributeSet) {
		View view = null;
		
		if (name.contains(".")) {  //view的名称中带点，说明是自定义View
			view = SkinUtils.crateView(name, context, attributeSet);
		} else {  //系统的View,直接通过反射创建
			for (String s : sClassPrefixList) { //比如android.widget.Button
				String viewName = s + name;
				view = SkinUtils.crateView(viewName, context, attributeSet);
				if (view != null) {
					break;
				}
			}
		}
		return view;
	}
	
	
}


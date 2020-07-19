package com.zmide.lit.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import java.lang.reflect.Constructor;

/**
 * The type Skin utils.
 */
public class SkinUtils {

//    public static int getStatusBarColorResId(Context context){
//        context.getResources().getIdentifier("id_layout_test_image", "id", getPackageName());
//        return ;
//    }
	
	
	/**
	 * Crate view view.
	 *
	 * @param name         the name
	 * @param context      the context
	 * @param attributeSet the attribute set
	 * @return the view
	 */
	public static View crateView(String name, Context context, AttributeSet attributeSet) {
		View view;
		try {
			Class<?> aClass = context.getClassLoader().loadClass(name);
			Constructor<?> constructor = aClass.getConstructor(Context.class, AttributeSet.class);
			view = (View) constructor.newInstance(context, attributeSet);
		} catch (Exception e) {
			return null;
		}
		return view;
	}
	
}

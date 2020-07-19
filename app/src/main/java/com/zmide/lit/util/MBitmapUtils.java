package com.zmide.lit.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * The type Bitmap utils.
 */
public class MBitmapUtils {
	
	
	/**
	 * Bitmap round bitmap.
	 *
	 * @param mBitmap the mBitmap
	 * @param index   the index
	 * @return the bitmap
	 */
	public static Bitmap bitmapRound(Bitmap mBitmap, float index) {
		Bitmap bitmap = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), Bitmap.Config.ARGB_4444);
		
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		
		//设置矩形大小
		Rect rect = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
		RectF rectf = new RectF(rect);
		
		// 相当于清屏
		canvas.drawARGB(0, 0, 0, 0);
		//画圆角
		canvas.drawRoundRect(rectf, index, index, paint);
		// 取两层绘制，显示上层
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		
		// 把原生的图片放到这个画布上，使之带有画布的效果
		canvas.drawBitmap(mBitmap, rect, rect, paint);
		return bitmap;
		
	}
	
	/**
	 * Gets bitmap from view.
	 *
	 * @param var1 the var 1
	 * @return the bitmap from view
	 */
	//状态栏变色
	public static Bitmap getBitmapFromView(View var1) {
		Bitmap var2 = Bitmap.createBitmap(var1.getWidth() + 10, var1.getHeight() + 10, Bitmap.Config.RGB_565);
		Canvas var3 = new Canvas(var2);
		var1.layout(var1.getLeft(), var1.getTop(), var1.getRight(), var1.getBottom());
		Drawable var4 = var1.getBackground();
		if (var4 != null) {
			var4.draw(var3);
		} else {
			var3.drawColor(-1);
		}
		var1.draw(var3);
		return var2;
	}
	
	/**
	 * Crop bitmap bitmap.
	 *
	 * @param bitmap the bitmap
	 * @return the bitmap
	 */
	public static Bitmap cropBitmap(Bitmap bitmap) {
		int w = bitmap.getWidth(); // 得到图片的宽，高
		return Bitmap.createBitmap(bitmap, 0, 0, w, w, null, false);
	}
	
	static Bitmap bitmapCompress(Bitmap bitmap, boolean isLong) {
		Bitmap bm;
		if (isLong) {
			bm = Bitmap.createScaledBitmap(bitmap, 300, 600, true);
			bm = cropBitmap(bm);
		} else
			bm = Bitmap.createScaledBitmap(bitmap, 128, 128, true);
		return MBitmapUtils.bitmapRound(bm, 15f);
	}
	
}

package com.zmide.lit.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.UriUtils;
import com.zmide.lit.base.MApplication;

import androidx.documentfile.provider.DocumentFile;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.FileInputStream;
import android.os.ParcelFileDescriptor;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;

/**
 * The type File utils.
 */
public class MFileUtils {
	private static File outFile;
	private static char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	private static MessageDigest messagedigest;
	
	/*
	 * MessageDigest初始化
	 */
	static {
		try {
			messagedigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			MExceptionUtils.reportException(e);
		}
	}

	/**
	 * Gets bitmap md 5.
	 *
	 * @param bm the bm
	 * @return the bitmap md 5
	 */
	public static String getBitmapMd5(Bitmap bm) {
		ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, BAOS); // BM是位图对象
		byte[] bitmapBytes = BAOS.toByteArray();
		return getMD5String(bitmapBytes);
	}
	
	/**
	 * Gets js.
	 *
	 * @param context  the context
	 * @param fileName the file name
	 * @return the js
	 */
	public static String getJS(Context context, String fileName) {
		InputStream inputStream = null;
		ByteArrayOutputStream outputStream = null;
		try {
			inputStream = context.getAssets().open(fileName);
			outputStream = new ByteArrayOutputStream();
			int len;
			byte[] buffer = new byte[2048];
			while ((len = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, len);
			}
			return new String(outputStream.toByteArray());
		} catch (IOException e) {
			MExceptionUtils.reportException(e);
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					MExceptionUtils.reportException(e);
				}
			}
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					MExceptionUtils.reportException(e);
				}
			}
		}
		return null;
	}
	
	private static String getMD5String(byte[] bytes) {
		messagedigest.update(bytes);
		return bufferToHex(messagedigest.digest());
	}
	
	private static String bufferToHex(byte[] bytes) {
		return bufferToHex(bytes, bytes.length);
	}
	
	private static String bufferToHex(byte[] bytes, int n) {
		StringBuilder stringbuffer = new StringBuilder(2 * n);
		for (int l = 0; l < n; l++) {
			char c0 = hexDigits[(bytes[l] & 0xf0) >> 4];
			char c1 = hexDigits[bytes[l] & 0xf];
			stringbuffer.append(c0);
			stringbuffer.append(c1);
		}
		return stringbuffer.toString();
	}
	
	
	/**
	 * Gets dark path.
	 *
	 * @param context the context
	 * @return the dark path
	 */
	public static String getDarkPath(Context context) {
		try {
			File cacheDir = context.getExternalCacheDir();
			outFile = new File(cacheDir, "dark.apk");
			InputStream is = context.getAssets().open("skin/dark.apk");
			if (outFile.exists()) {
				if (outFile.length() == is.available()) return outFile.getAbsolutePath();
				else
					copySkin(is);
			} else {
				return createSkin(is);
			}
			
		} catch (IOException e) {
			MExceptionUtils.reportException(e);
		}
		
		return "";
	}
	
	private static String createSkin(InputStream is) throws IOException {
		
		boolean res = outFile.createNewFile();
		if (res) {
			FileOutputStream fos = new FileOutputStream(outFile);
			byte[] buffer = new byte[is.available()];
			int byteCount;
			while ((byteCount = is.read(buffer)) != -1) {
				fos.write(buffer, 0, byteCount);
			}
			fos.flush();
			is.close();
			fos.close();
			return outFile.getAbsolutePath();
			
		} else {
			return outFile.getAbsolutePath();
		}
	}
	
	private static void copySkin(InputStream is) throws IOException {
		FileOutputStream fos = new FileOutputStream(outFile);
		byte[] buffer = new byte[is.available()];
		int byteCount;
		while ((byteCount = is.read(buffer)) != -1) {
			fos.write(buffer, 0, byteCount);
		}
		fos.flush();
		is.close();
		fos.close();
	}
	
	/**
	 * Set decrypt string.
	 *
	 * @param encodeWord the encode word
	 * @return the string
	 */
	public static String setDecrypt(String encodeWord) {
		return new String(Base64.decode(encodeWord, Base64.NO_WRAP), StandardCharsets.UTF_8);
	}
	
	
	/*
	 * 保存Bitmap图片为本地文件
	 */
	
	/**
	 * Save file string.
	 *
	 * @param bitmap the bitmap
	 * @param name   the name
	 * @param isLong the is long
	 * @return the string
	 */
	public static String saveFile(Bitmap bitmap, String name, boolean isLong) {
		if (bitmap == null)
			return null;
		FileOutputStream fileOutputStream;
		String cache = PathUtils.getExternalAppPicturesPath() + "/favicon/";
		if (name == null)
			name = getBitmapMd5(bitmap);
		String path = cache + name + ".favicon";
		FileUtils.createOrExistsFile(path);
		File filename = new File(path);
		try {
			fileOutputStream = new FileOutputStream(filename);
			Bitmap bm = MBitmapUtils.bitmapCompress(bitmap, isLong);
			bm.compress(Bitmap.CompressFormat.PNG, 80, fileOutputStream);
			fileOutputStream.flush();
			fileOutputStream.close();
			return filename.toString();
		} catch (Exception e) {
			MExceptionUtils.reportException(e);
		}
		return null;
	}
	
	/**
	 * Save file on file.
	 *
	 * @param bitmap the bitmap
	 * @param name   the name
	 */
	public static void saveFileOnFile(Bitmap bitmap, String name) {
		if (bitmap == null)
			return;
		FileOutputStream fileOutputStream;
		//String file = MApplication.getContext().getExternalFilesDir("picture")+"";
		if (name == null)
			name = MFileUtils.getBitmapMd5(bitmap);
		//File filename = new File(file, name);
		try {
			fileOutputStream = MApplication.getContext().openFileOutput(name, Context.MODE_PRIVATE);
			bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2, true);
			bitmap.compress(Bitmap.CompressFormat.PNG, 80, fileOutputStream);
			fileOutputStream.flush();
			fileOutputStream.close();
		} catch (Exception e) {
			MExceptionUtils.reportException(e);
		}
	}
	
	public static void saveBase64Picture(Activity activity, String base64DataStr) {
		// 1.去掉base64中的前缀
		String base64Str = base64DataStr.substring(base64DataStr.indexOf(",") + 1);
		// 获取手机相册的路径地址
		final String galleryPath = Environment.getExternalStorageDirectory()
				+ File.separator + Environment.DIRECTORY_DCIM
				+ File.separator + "Camera" + File.separator;
		//创建文件来保存，第二个参数是文件名称，可以根据自己来命名
		File file = new File(galleryPath, System.currentTimeMillis() + ".png");
		String fileName = file.toString();
		// 3. 解析保存图片
		byte[] data = Base64.decode(base64Str, Base64.DEFAULT);
		for (int i = 0; i < data.length; i++) {
			if (data[i] < 0) {
				data[i] += 256;
			}
		}
		try {
			OutputStream os = new FileOutputStream(fileName);
			os.write(data);
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//通知相册更新
			Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			Uri uri = Uri.fromFile(file);
			intent.setData(uri);
			activity.sendBroadcast(intent);
			activity.runOnUiThread(() -> MToastUtils.makeText("已保存至" + galleryPath, MToastUtils.LENGTH_SHORT).show());
		}
	}
	
	
	/**
     * 复制单个文件
     *
     * @param oldPath String  原文件路径  如：c:/fqf.txt
     * @param newPath String  复制后路径  如：f:/fqf.txt
     * @return boolean
     */
    public static void moveFile(Uri oldPath, Uri newPath) {
       // try {
			FileUtils.move(UriUtils.uri2File(oldPath),UriUtils.uri2File(newPath));
            
       

    }
	
	
	public static String copyFile(Uri uri ,String filename, String extension, Uri treeUri) {
		InputStream in = null;
		OutputStream out = null;
		String error = null;
		final int takeFlags =  (Intent.FLAG_GRANT_READ_URI_PERMISSION
            | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
// Check for the freshest data.
		MApplication.getContext().getContentResolver().takePersistableUriPermission(treeUri, takeFlags);
		DocumentFile pickedDir = DocumentFile.fromTreeUri(MApplication.getContext(), treeUri);
		try {
			DocumentFile newFile = pickedDir.createFile(extension, filename);
			out = MApplication.getContext().getContentResolver().openOutputStream(newFile.getUri());
			in = new FileInputStream(UriUtils.uri2File(uri));

			byte[] buffer = new byte[1024];
			int read;
			while ((read = in.read(buffer)) != -1) {
				out.write(buffer, 0, read);
			}
			in.close();
			// write the output file (You have now copied the file)
			out.flush();
			out.close();

		} catch (FileNotFoundException fnfe1) {
			error = fnfe1.getMessage();
		} catch (Exception e) {
			error = e.getMessage();
		}
		return error;
	}
}

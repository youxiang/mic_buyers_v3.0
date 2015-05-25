package com.focustech.common.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Pattern;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.Selection;
import android.text.Spannable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.widget.EditText;

/**********************************************************
 * @文件名称：Utils.java
 * @文件作者：xiongjiangwei
 * @创建时间：2014年8月19日 下午2:59:22
 * @文件描述：常用工具类
 * @修改历史：2014年8月19日创建初始版本
 **********************************************************/
public class Utils
{
	static final String LOG_TAG = "PullToRefresh";

	private Utils()
	{
	}

	public static void warnDeprecation(String depreacted, String replacement)
	{
		Log.w(LOG_TAG, "You're using the deprecated " + depreacted + " attr, please switch over to " + replacement);
	}

	/**
	 * 获得屏幕宽度
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public static int getWindowWidthPix(Activity context)
	{
		int ver = Build.VERSION.SDK_INT;
		Display display = context.getWindowManager().getDefaultDisplay();
		int width = 0;
		if (ver < 13)
		{
			DisplayMetrics dm = new DisplayMetrics();
			display.getMetrics(dm);
			width = dm.widthPixels;
		}
		else
		{
			Point point = new Point();
			display.getSize(point);
			width = point.x;
		}
		return width;
	}

	/**
	 * 获得屏幕高度
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public static int getWindowHeightPix(Activity context)
	{
		int ver = Build.VERSION.SDK_INT;
		Display display = context.getWindowManager().getDefaultDisplay();
		int height = 0;
		if (ver < 13)
		{
			DisplayMetrics dm = new DisplayMetrics();
			display.getMetrics(dm);
			height = dm.heightPixels;
		}
		else
		{
			Point point = new Point();
			display.getSize(point);
			height = point.y;
		}
		return height;
	}

	/**
	 * 返回当前程序版本名  
	 * @param context
	 * @return
	 */
	public static String getAppVersionName(Context context)
	{
		String versionName = "1.00.00";
		try
		{
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
			if (versionName == null || versionName.length() <= 0)
			{
				return versionName;
			}
		}
		catch (Exception e)
		{
			Log.e("VersionInfo", "Exception", e);
		}
		return versionName;
	}

	/***
	 * 获得手机设备的串号
	 * @param context
	 * @return
	 */
	public static String getIMEI(Context context)
	{
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		// Get deviceId
		String deviceId = tm.getDeviceId();
		// If running on an emulator
		if (deviceId == null || deviceId.trim().length() == 0 || deviceId.matches("0+"))
		{
			deviceId = (new StringBuilder("EMU")).append((new Random(System.currentTimeMillis())).nextLong())
					.toString();
		}
		return deviceId;
	}

	/**
	 * 设置输入框光标位置
	 * @param editText
	 */
	public static void setEditTextCursorPosition(EditText editText)
	{
		CharSequence text = editText.getText();
		if (text instanceof Spannable)
		{
			Spannable spanText = (Spannable) text;
			Selection.setSelection(spanText, text.length());
		}
	}

	/**
	 * 通过反射取私有变量
	 * @param fClass
	 * @param obj
	 * @param fieldName
	 * @return
	 */
	public static Object getDeclaredField(Class<?> fClass, Object obj, String fieldName)
	{
		Field field = null;
		Object o = null;
		try
		{
			field = fClass.getDeclaredField(fieldName);
			field.setAccessible(true);
			o = field.get(obj);
		}
		catch (Exception e)
		{
		}
		return o;
	}

	/**
	 * 设置控件的Selector
	 * @param on
	 * @param off
	 * @return
	 */
	public static StateListDrawable setImageButtonState(Drawable on, Drawable off, Context context)
	{
		StateListDrawable states = new StateListDrawable();
		states.addState(new int[]
		{ android.R.attr.stateNotNeeded }, on);
		states.addState(new int[]
		{ android.R.attr.state_pressed, android.R.attr.state_enabled }, on);
		states.addState(new int[]
		{ android.R.attr.state_focused, android.R.attr.state_enabled }, off);
		states.addState(new int[]
		{ android.R.attr.state_enabled }, off);
		states.addState(new int[]
		{ -android.R.attr.state_enabled }, on);
		return states;
	}

	/**
	 * 设置控件的Selector
	 * @param on
	 * @param off
	 * @return
	 */
	public static StateListDrawable setImageButtonState(int onResId, int offResId, Context context)
	{
		StateListDrawable states = new StateListDrawable();
		states.addState(new int[]
		{ android.R.attr.stateNotNeeded }, context.getResources().getDrawable(onResId));
		states.addState(new int[]
		{ android.R.attr.state_pressed, android.R.attr.state_enabled }, context.getResources().getDrawable(onResId));
		// states.addState(new int[]
		// { android.R.attr.state_focused, android.R.attr.state_enabled },
		// context.getResources().getDrawable(offResId));
		states.addState(new int[]
		{ android.R.attr.state_enabled }, context.getResources().getDrawable(offResId));
		// states.addState(new int[]
		// { -android.R.attr.state_enabled }, context.getResources().getDrawable(onResId));
		return states;
	}

	/**
	 * 修改CheckBox样式
	 * @param on
	 * @param off
	 * @param context
	 * @return
	 */
	public static StateListDrawable setCheckBoxState(int onResId, int offResId, Context context)
	{
		StateListDrawable states = new StateListDrawable();
		states.addState(new int[]
		{ android.R.attr.state_checked }, context.getResources().getDrawable(onResId));
		states.addState(new int[]
		{ -android.R.attr.state_checked }, context.getResources().getDrawable(offResId));
		return states;
	}

	/**
	 * 获取配置文件中的数据
	 * @param context
	 * @return
	 */
	public static String getAppMetaData(Context context, String metaName)
	{
		String msg = "";
		ApplicationInfo appInfo = null;
		try
		{
			appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),
					PackageManager.GET_META_DATA);
		}
		catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		if (appInfo != null)
		{
			msg = appInfo.metaData.getString(metaName);
		}
		return msg;
	}

	/**
	 * 判断服务是否正在运行
	 * @param serviceName	true:运行中
	 * @return
	 */
	public static boolean isServiceRunning(Context context, String serviceName)
	{
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo info : am.getRunningServices(Integer.MAX_VALUE))
		{
			if (info.service.getClassName().toString().equals(serviceName))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否安装了sdcard
	 * @return
	 */
	public static boolean isHaveSDcard()
	{
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
	}

	/**
	 * 判断值是否为空
	 * @param value
	 * @return
	 */
	public static boolean isEmpty(String value)
	{
		return (value == null || "".equals(value)) ? true : false;
	}

	/**
	 * 判断值是否为空
	 * @param value
	 * @return
	 */
	public static boolean isEmpty(ArrayList<String> value)
	{
		return (value == null || (value != null && value.size() == 0)) ? true : false;
	}

	/**
	 * 检查Email是否合法
	 * @param email
	 * @return
	 */
	public static boolean checkEmail(String email)
	{
		String regex = "[a-zA-Z0-9-._]{1,50}@[a-zA-Z0-9-.]{1,65}.([a-zA-Z]{2,3}|([a-zA-Z]{2,3}.[a-zA-Z]{2}))";
		return Pattern.matches(regex, email);
	}

	/**
	 * Will throw AssertionError, if expression is not true
	 *
	 * @param expression    result of your asserted condition
	 * @param failedMessage message to be included in error log
	 * @throws java.lang.AssertionError
	 */
	public static void asserts(final boolean expression, final String failedMessage)
	{
		if (!expression)
		{
			throw new AssertionError(failedMessage);
		}
	}

	/**
	 * Will throw IllegalArgumentException if provided object is null on runtime
	 *
	 * @param argument object that should be asserted as not null
	 * @param name     name of the object asserted
	 * @throws java.lang.IllegalArgumentException
	 */
	public static <T> T notNull(final T argument, final String name)
	{
		if (argument == null)
		{
			throw new IllegalArgumentException(name + " should not be null!");
		}
		return argument;
	}

	/**
	 * 像素转换为dp
	 * @param context
	 * @param px
	 * @return
	 */
	public static int toDip(Context context, int px)
	{
		if (context == null)
			return px;
		final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, context.getResources()
				.getDisplayMetrics());
		return pageMargin;
	}

	public static String getPath(final Context context, final Uri uri)
	{

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri))
		{
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri))
			{
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type))
				{
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}

				// TODO handle non-primary volumes
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri))
			{
				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri))
			{
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type))
				{
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				}
				else if ("video".equals(type))
				{
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				}
				else if ("audio".equals(type))
				{
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[]
				{ split[1] };

				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme()))
		{

			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();

			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme()))
		{
			return uri.getPath();
		}

		return null;
	}

	public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs)
	{

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection =
		{ column };

		try
		{
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst())
			{
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		}
		finally
		{
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	public static boolean isDownloadsDocument(Uri uri)
	{
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	public static boolean isMediaDocument(Uri uri)
	{
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	public static boolean isExternalStorageDocument(Uri uri)
	{
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	public static boolean isGooglePhotosUri(Uri uri)
	{
		return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}
}

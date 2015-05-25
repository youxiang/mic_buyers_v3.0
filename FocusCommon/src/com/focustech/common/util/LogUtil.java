package com.focustech.common.util;

import android.util.Log;

/**********************************************************
 * @文件名称：LogUtil.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年3月10日 下午4:54:01
 * @文件描述：日志工具类
 * @修改历史：2015年3月10日创建初始版本
 **********************************************************/
public class LogUtil
{
	private static boolean isOpen = true;

	public static void e(String tag, String msg)
	{
		if (isOpen)
		{
			Log.e(tag, msg);
		}
	}

	public static void d(String tag, String msg)
	{
		if (isOpen)
		{
			Log.d(tag, msg);
		}
	}

	public static void i(String tag, String msg)
	{
		if (isOpen)
		{
			Log.i(tag, msg);
		}
	}

	public static void w(String tag, String msg)
	{
		if (isOpen)
		{
			Log.w(tag, msg);
		}
	}

	public static void v(String tag, String msg)
	{
		if (isOpen)
		{
			Log.v(tag, msg);
		}
	}

	public static String makeLogTag(Class<?> cls)
	{
		return "Androidpn_" + cls.getSimpleName();
	}
}

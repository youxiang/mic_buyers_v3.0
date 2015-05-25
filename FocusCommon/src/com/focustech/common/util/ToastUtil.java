package com.focustech.common.util;

import android.content.Context;
import android.widget.Toast;

/**********************************************************
 * @文件名称：ToastUtil.java
 * @文件作者：xiongjiangwei
 * @创建时间：2014年10月22日 上午11:16:49
 * @文件描述：提醒工具类
 * @修改历史：2014年10月22日创建初始版本
 **********************************************************/
public class ToastUtil
{
	public static void toast(Context context, Object obj)
	{
		if (context != null && obj != null)
		{
			Toast.makeText(context, obj.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	public static void toast(Context context, int resId)
	{
		if (context != null)
		{
			toast(context, context.getString(resId));
		}
	}
}

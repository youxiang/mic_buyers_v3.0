package com.micen.buyers.reveiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

/**********************************************************
 * @文件名称：ActivityFinishReceiver.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年3月9日 下午3:44:31
 * @文件描述：自定义广播接收器，关闭传入的Activity
 * @修改历史：2015年3月9日创建初始版本
 **********************************************************/
public class ActivityFinishReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
		if (context instanceof Activity)
		{
			((Activity) context).finish();
		}
		else if (context instanceof FragmentActivity)
		{
			((FragmentActivity) context).finish();
		}
	}

}

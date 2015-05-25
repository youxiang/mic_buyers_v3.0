package com.focustech.common.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.focustech.common.broadcast.xmpp.NotificationService;
import com.focustech.common.broadcast.xmpp.ServiceManager;
import com.focustech.common.util.Utils;

/**********************************************************
 * @文件名称：AlarmReceiver.java
 * @文件作者：xiongjiangwei
 * @创建时间：2014年8月19日 下午3:19:02
 * @文件描述：闹钟广播接收器
 * @修改历史：2014年8月19日创建初始版本
 **********************************************************/
public class AlarmReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
		if (!Utils.isServiceRunning(context, NotificationService.SERVICE_NAME))
		{
			ServiceManager serviceManager = new ServiceManager(context);
			serviceManager.startService();
		}
	}

}

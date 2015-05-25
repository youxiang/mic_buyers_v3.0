package com.focustech.common.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.focustech.common.broadcast.xmpp.NotificationService;
import com.focustech.common.broadcast.xmpp.ServiceManager;
import com.focustech.common.util.Utils;

/**********************************************************
 * @�ļ����ƣ�AlarmReceiver.java
 * @�ļ����ߣ�xiongjiangwei
 * @����ʱ�䣺2014��8��19�� ����3:19:02
 * @�ļ����������ӹ㲥������
 * @�޸���ʷ��2014��8��19�մ�����ʼ�汾
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
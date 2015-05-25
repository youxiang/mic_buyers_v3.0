package com.focustech.common.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.focustech.common.broadcast.xmpp.Constants;

/**********************************************************
 * @�ļ����ƣ�NotificationReceiver.java
 * @�ļ����ߣ�xiongjiangwei
 * @����ʱ�䣺2013-11-25 ����11:15:09
 * @�ļ��������㲥�����������
 * @�޸���ʷ��2013-11-25������ʼ�汾
 **********************************************************/
public abstract class NotificationReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
		String action = intent.getAction();

		if (Constants.ACTION_SHOW_NOTIFICATION.equals(action))
		{
			onAction(context, intent);
			// String notificationId = intent.getStringExtra(Constants.NOTIFICATION_ID);
			// String notificationApiKey = intent.getStringExtra(Constants.NOTIFICATION_API_KEY);
			// String notificationTitle = intent.getStringExtra(Constants.NOTIFICATION_TITLE);
			// String notificationMessage = intent.getStringExtra(Constants.NOTIFICATION_MESSAGE);
			// String notificationUri = intent.getStringExtra(Constants.NOTIFICATION_URI);
			//
			// Notifier notifier = new Notifier(context);
			// notifier.notify(notificationId, notificationApiKey, notificationTitle, notificationMessage,
			// notificationUri);
		}
	}

	public abstract void onAction(Context context, Intent intent);

}

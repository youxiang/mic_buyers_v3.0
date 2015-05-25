package com.focustech.common.broadcast.xmpp;

import java.util.Random;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.util.Log;
import android.widget.Toast;

import com.focustech.common.util.LogUtil;

/**********************************************************
 * @文件名称：Notifier.java
 * @文件作者：xiongjiangwei
 * @创建时间：2013-11-25 下午03:17:23
 * @文件描述：通知管理器
 * @修改历史：2013-11-25创建初始版本
 **********************************************************/
public class Notifier
{

	private static final String LOGTAG = LogUtil.makeLogTag(Notifier.class);

	private static final Random random = new Random(System.currentTimeMillis());

	private Context context;

	private NotificationManager notificationManager;
	private boolean isNotificationEnabled = true;
	private boolean isNotificationToastEnabled = false;
	private boolean isNotificationSoundEnabled = true;
	private boolean isNotificationVibrateEnabled = true;

	public Notifier(Context context)
	{
		this.context = context;
		this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	public void setNotificationEnabled(boolean isNotificationEnabled)
	{
		this.isNotificationEnabled = isNotificationEnabled;
	}

	public void setNotificationToastEnabled(boolean isNotificationToastEnabled)
	{
		this.isNotificationToastEnabled = isNotificationToastEnabled;
	}

	public void setNotificationSoundEnabled(boolean isNotificationSoundEnabled)
	{
		this.isNotificationSoundEnabled = isNotificationSoundEnabled;
	}

	public void setNotificationVibrateEnabled(boolean isNotificationVibrateEnabled)
	{
		this.isNotificationVibrateEnabled = isNotificationVibrateEnabled;
	}

	public void notify(int notificationIcon, Class<?> notifyDetailClass, String notificationId, String apiKey,
			String title, String message, String uri)
	{
		if (VERSION.SDK_INT >= 11)
		{
			notifyThatExceedLv11(notificationIcon, notifyDetailClass, notificationId, apiKey, title, message, uri);
		}
		else
		{
			notifyThatUnderLv11(notificationIcon, notifyDetailClass, notificationId, apiKey, title, message, uri);
		}
	}

	private void notifyThatExceedLv11(int notificationIcon, Class<?> notifyDetailClass, String notificationId,
			String apiKey, String title, String message, String uri)
	{
		Log.d(LOGTAG, "notifyThatExceedLv11...");

		Log.d(LOGTAG, "notificationId=" + notificationId);
		Log.d(LOGTAG, "notificationApiKey=" + apiKey);
		Log.d(LOGTAG, "notificationTitle=" + title);
		Log.d(LOGTAG, "notificationMessage=" + message);
		Log.d(LOGTAG, "notificationUri=" + uri);

		if (isNotificationEnabled)
		{
			// Show the toast
			if (isNotificationToastEnabled)
			{
				Toast.makeText(context, message, Toast.LENGTH_LONG).show();
			}

			// Notification
			Notification.Builder notification = new Notification.Builder(context);
			notification.setSmallIcon(notificationIcon);
			notification.setContentTitle(title);
			notification.setContentText(message);
			int defaultsValue = Notification.DEFAULT_LIGHTS;
			if (isNotificationSoundEnabled)
			{
				defaultsValue |= Notification.DEFAULT_SOUND;
			}
			if (isNotificationVibrateEnabled)
			{
				defaultsValue |= Notification.DEFAULT_VIBRATE;
			}
			notification.setDefaults(defaultsValue);
			notification.setAutoCancel(true);
			notification.setWhen(System.currentTimeMillis());
			notification.setTicker(message);
			notification.setContentIntent(getNotificationContentIntent(notifyDetailClass, notificationId, apiKey,
					title, message, uri));
			notify(notification.getNotification());
		}
		else
		{
			Log.w(LOGTAG, "Notificaitons disabled.");
		}
	}

	@SuppressWarnings("deprecation")
	private void notifyThatUnderLv11(int notificationIcon, Class<?> notifyDetailClass, String notificationId,
			String apiKey, String title, String message, String uri)
	{

		Log.d(LOGTAG, "notifyThatUnderLv11...");

		Log.d(LOGTAG, "notificationId=" + notificationId);
		Log.d(LOGTAG, "notificationApiKey=" + apiKey);
		Log.d(LOGTAG, "notificationTitle=" + title);
		Log.d(LOGTAG, "notificationMessage=" + message);
		Log.d(LOGTAG, "notificationUri=" + uri);

		if (isNotificationEnabled)
		{
			// Show the toast
			if (isNotificationToastEnabled)
			{
				Toast.makeText(context, message, Toast.LENGTH_LONG).show();
			}
			// Notification
			Notification notification = new Notification();
			notification.icon = notificationIcon;
			notification.defaults = Notification.DEFAULT_LIGHTS;
			if (isNotificationSoundEnabled)
			{
				notification.defaults |= Notification.DEFAULT_SOUND;
			}
			if (isNotificationVibrateEnabled)
			{
				notification.defaults |= Notification.DEFAULT_VIBRATE;
			}
			notification.flags = Notification.FLAG_AUTO_CANCEL;
			notification.when = System.currentTimeMillis();
			notification.tickerText = message;

			notification.setLatestEventInfo(context, title, message,
					getNotificationContentIntent(notifyDetailClass, notificationId, apiKey, title, message, uri));
			// notification.contentIntent = contentIntent;
			notify(notification);
		}
		else
		{
			Log.w(LOGTAG, "Notificaitons disabled.");
		}

	}

	private PendingIntent getNotificationContentIntent(Class<?> notifyDetailClass, String notificationId,
			String apiKey, String title, String message, String uri)
	{
		Intent intent = new Intent(context, notifyDetailClass);
		intent.putExtra(Constants.NOTIFICATION_ID, notificationId);
		intent.putExtra(Constants.NOTIFICATION_API_KEY, apiKey);
		intent.putExtra(Constants.NOTIFICATION_TITLE, title);
		intent.putExtra(Constants.NOTIFICATION_MESSAGE, message);
		intent.putExtra(Constants.NOTIFICATION_URI, uri);
		// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		// intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		// intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		return contentIntent;
	}

	public void notify(Notification notification)
	{
		notificationManager.notify(random.nextInt(), notification);
	}

}

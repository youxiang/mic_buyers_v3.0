package com.focustech.common.broadcast;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;

import com.focustech.common.broadcast.xmpp.Constants;

/**********************************************************
 * @文件名称：WatchService.java
 * @文件作者：xiongjiangwei
 * @创建时间：2013-11-21 下午01:30:45
 * @文件描述：用于监视XMPP长连接服务的守护服务
 * @修改历史：2013-11-21创建初始版本
 **********************************************************/
public class WatchService extends Service
{
	private NotificationReceiver notificationReceiver;
	private SharedPreferences sharedPrefs;
	/**
	 * 默认间隔时间
	 */
	private int defaultIntervalTime = 5 * 1000;
	private AlarmManager alarmMgr;

	@Override
	public void onCreate()
	{
		alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		try
		{
			sharedPrefs = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
			Class<?> cls = Class.forName(sharedPrefs.getString(Constants.NOTIFICATION_RECEIVER_CLASS_NAME, ""));
			notificationReceiver = (NotificationReceiver) cls.newInstance();
			registerNotificationReceiver();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onStart(Intent intent, int startId)
	{
		Intent it = new Intent(this, AlarmReceiver.class);
		PendingIntent pendIntent = PendingIntent.getBroadcast(this, 0, it, PendingIntent.FLAG_UPDATE_CURRENT);
		// 5秒后发送广播，然后每隔5秒重复发广播。广播都是直接发到AlarmReceiver的
		long triggerAtTime = SystemClock.elapsedRealtime() + defaultIntervalTime;
		alarmMgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, getIntervalTime(), pendIntent);
	}

	@Override
	public void onDestroy()
	{
		Intent intent = new Intent(this, AlarmReceiver.class);
		PendingIntent pendIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		// 与上面的intent匹配（filterEquals(intent)）的闹钟会被取消
		alarmMgr.cancel(pendIntent);
		unregisterNotificationReceiver();
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	@Override
	public void onRebind(Intent intent)
	{

	}

	@Override
	public boolean onUnbind(Intent intent)
	{
		return true;
	}

	private int getIntervalTime()
	{
		return sharedPrefs == null ? defaultIntervalTime : sharedPrefs.getInt(Constants.WATCH_SERVICE_INTERVAL_TIME,
				defaultIntervalTime);
	}

	private void registerNotificationReceiver()
	{
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.ACTION_SHOW_NOTIFICATION);
		// filter.addAction(Constants.ACTION_NOTIFICATION_CLICKED);
		// filter.addAction(Constants.ACTION_NOTIFICATION_CLEARED);
		registerReceiver(notificationReceiver, filter);
	}

	private void unregisterNotificationReceiver()
	{
		unregisterReceiver(notificationReceiver);
	}
}

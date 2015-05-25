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
 * @�ļ����ƣ�WatchService.java
 * @�ļ����ߣ�xiongjiangwei
 * @����ʱ�䣺2013-11-21 ����01:30:45
 * @�ļ����������ڼ���XMPP�����ӷ�����ػ�����
 * @�޸���ʷ��2013-11-21������ʼ�汾
 **********************************************************/
public class WatchService extends Service
{
	private NotificationReceiver notificationReceiver;
	private SharedPreferences sharedPrefs;
	/**
	 * Ĭ�ϼ��ʱ��
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
		// 5����͹㲥��Ȼ��ÿ��5���ظ����㲥���㲥����ֱ�ӷ���AlarmReceiver��
		long triggerAtTime = SystemClock.elapsedRealtime() + defaultIntervalTime;
		alarmMgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, getIntervalTime(), pendIntent);
	}

	@Override
	public void onDestroy()
	{
		Intent intent = new Intent(this, AlarmReceiver.class);
		PendingIntent pendIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		// �������intentƥ�䣨filterEquals(intent)�������ӻᱻȡ��
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

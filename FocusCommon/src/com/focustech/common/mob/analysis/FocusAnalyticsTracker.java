package com.focustech.common.mob.analysis;
import java.util.Calendar;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.focustech.common.mob.MobileTools;
/**
 * ����һ��tracker��
 * 
 * @author chenkangpeng
 * 
 */
public class FocusAnalyticsTracker
{
	private EventManager em;
	private static FocusAnalyticsTracker instance;
	private ServiceConnection sConn;
	private FocusAnalyticsTracker()
	{
	}
	public static FocusAnalyticsTracker getInstances()
	{
		if (instance == null)
		{
			instance= new FocusAnalyticsTracker();
		}
		return instance;
	}
	/**
	 * ͨ���÷�������������һ����Ϊ��������
	 * 
	 * @param productName ��Ʒ���� ���� ��ttnet��
	 * @param productChannel �������� �� ��self��
	 * @param timeSpace ʱ��������ʾ�೤ʱ�䷢��һ����Ϊ��������
	 * @param activity ��ʾһ��activity ����
	 */
	public void startNewSession(String productName, String productChannel, int timeSpace, Activity activity)
	{
		sConn= new MyServiceConnection();
		Intent intent= new Intent(activity, AnalyticService.class);
		intent.putExtra("productName", productName);
		intent.putExtra("productChannel", productChannel);
		intent.putExtra("time_space", timeSpace);
		activity.bindService(intent, sConn, Context.BIND_AUTO_CREATE);
	}
	
	
	public void startNewSession(String productName, String productChannel, int timeSpace, Application application)
	{
		sConn= new MyServiceConnection();
		Intent intent= new Intent(application, AnalyticService.class);
		intent.putExtra("productName", productName);
		intent.putExtra("productChannel", productChannel);
		intent.putExtra("time_space", timeSpace);
		application.bindService(intent, sConn, Context.BIND_AUTO_CREATE);
	}
	
	/***
	 * �ռ�һ����Ϊ
	 * @param context �����Ķ���
	 * @param eventName ��Ϊ���ƣ� ���硰��¼��
	 */
	public void trackEvent(Context context, String eventName)
	{
		em= new EventManager(context);
		BaseEvent event= new BaseEvent();
		event.setEventName(eventName);
		event.setEventTime(Calendar.getInstance().getTimeInMillis() + "");
		em.saveEvent(event);
	}
	/***
	 * �ռ�һ����Ϊ
	 * @param context �����Ķ��� 
	 * @param eventName ��Ϊ���ƣ� ���硰��¼��
	 * @param params ��Ϊ�ĸ������������� "�û���"
	 */
	public void trackEvent(Context context, String eventName, String params)
	{
		if (params != null && params.trim().length() > 0)
		{
			params= "android " + MobileTools.getMobileInfo(context).getDeviceFirmwareVersion() + "," + params;
		}
		else
		{
			params= "android " + MobileTools.getMobileInfo(context).getDeviceFirmwareVersion();
		}
		em= new EventManager(context);
		BaseEvent event= new BaseEvent();
		event.setEventName(eventName);
		event.setEventTime(Calendar.getInstance().getTimeInMillis() + "");
		event.setParams(params);
		em.saveEvent(event);
	}
	/***
	 * �ռ�һ����Ϊ����
	 * @param context ��ʾ�����Ķ���
	 * @param event һ����Ϊ����
	 */
	public void trackEvent(Context context, BaseEvent event)
	{
		em= new EventManager(context);
		event.setEventTime(Calendar.getInstance().getTimeInMillis() + "");
		em.saveEvent(event);
	}
	public void cancelTrack(Activity activity)
	{
		activity.unbindService(sConn);
	}
	class MyServiceConnection implements ServiceConnection
	{
		@Override
		public void onServiceConnected(ComponentName name, IBinder service)
		{
		}
		@Override
		public void onServiceDisconnected(ComponentName name)
		{
		}
	}
}

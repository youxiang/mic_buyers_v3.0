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
 * 定义一个tracker类
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
	 * 通过该方法，可以启动一个行为分析服务
	 * 
	 * @param productName 产品名称 例如 “ttnet”
	 * @param productChannel 渠道名称 如 “self”
	 * @param timeSpace 时间间隔，表示多长时间发送一次行为到服务器
	 * @param activity 表示一个activity 对象
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
	 * 收集一个行为
	 * @param context 上下文对象
	 * @param eventName 行为名称， 例如“登录”
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
	 * 收集一个行为
	 * @param context 上下文对象 
	 * @param eventName 行为名称， 例如“登录”
	 * @param params 行为的附带参数，例如 "用户名"
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
	 * 收集一个行为数据
	 * @param context 表示上下文对象
	 * @param event 一个行为对象
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

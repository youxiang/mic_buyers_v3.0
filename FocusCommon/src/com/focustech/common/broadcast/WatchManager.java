package com.focustech.common.broadcast;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Message;

import com.focustech.common.broadcast.xmpp.Constants;
import com.focustech.common.mob.register.Register;
import com.focustech.common.mob.register.Register.RegisterCommListener;

/**********************************************************
 * @文件名称：WatchManager.java
 * @文件作者：xiongjiangwei
 * @创建时间：2013-11-25 下午03:09:36
 * @文件描述：守护服务管理器
 * @修改历史：2013-11-25创建初始版本
 **********************************************************/
public class WatchManager
{
	private SharedPreferences sharedPrefs;
	private Context mContext;
	private Intent serviceIntent;
	private final int REGISTER_SUCCESS = 0x00000001;

	public WatchManager(Context context, NotificationReceiver notificationReceiver)
	{
		this.mContext = context;
		sharedPrefs = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPrefs.edit();
		editor.putString(Constants.NOTIFICATION_RECEIVER_CLASS_NAME, notificationReceiver.getClass().getName());
		editor.commit();
	}

	public WatchManager(Context context, NotificationReceiver notificationReceiver, int intervalTime)
	{
		this(context, notificationReceiver);
		Editor editor = sharedPrefs.edit();
		editor.putInt(Constants.WATCH_SERVICE_INTERVAL_TIME, intervalTime);
		editor.commit();
	}

	/**
	 * 开始连接
	 * @param productName	产品名称
	 * @param productChannel	产品渠道
	 */
	public void startConnect(String productName, String productChannel)
	{
		final Register register = new Register();
		register.register(mContext, productName, productChannel, new RegisterCommListener()
		{

			@Override
			public void result(boolean result)
			{
				if (result)
				{
					mHandler.sendEmptyMessage(REGISTER_SUCCESS);
				}

			}
		});
	}

	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case REGISTER_SUCCESS:
				startService();
				break;
			}
		};
	};

	public void startService()
	{
		if (serviceIntent == null)
		{
			serviceIntent = new Intent(mContext, WatchService.class);
		}
		mContext.startService(serviceIntent);
	}

	public void stopService()
	{
		if (serviceIntent != null)
		{
			mContext.stopService(serviceIntent);
		}
	}
}

package com.focustech.common.mob.analysis;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.focustech.common.mob.register.Register;
import com.focustech.common.mob.register.Register.RegisterCommListener;

/***
 * 这是一个后台服务，该服务定时向服务器发送用户行为
 * 
 * @author chenkangpeng
 * 
 */
public class AnalyticService extends Service
{
	/**
	 * 表示一个定时器，通过该定时器可间隔的向服务器发送用户行为
	 */
	private Timer timer;
	/**
	 * 表示一个时间间隔，该时间间隔表示多长时间向服务器发送一次用户行为
	 */
	private int timeSpace;
	/***
	 * 设置一个默认的时间间隔,该时间间隔表示每隔五分钟后向服务器发送一个行为采集的数据包
	 */
	private final int default_timespace = 60000;
	/**
	 * 事件的一个管理类，通过该类可发送事件到服务器
	 */
	private EventManager em;
	private String productName;
	private String productChannel;
	Register register;
	boolean isRegister = false;

	@Override
	public void onCreate()
	{
		em = new EventManager(this);
		timer = new Timer();
		register = new Register();
		super.onCreate();
	}

	@Override
	public boolean onUnbind(Intent intent)
	{
		return super.onUnbind(intent);
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		productName = intent.getStringExtra("productName");
		productChannel = intent.getStringExtra("productChannel");
		this.timeSpace = intent.getIntExtra("time_space", default_timespace);
		timer.schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				handler.sendEmptyMessage(1);
			}
		}, 60000, timeSpace);
		return new MyIBinder();
	}

	// 发送行为数据到服务器
	private void sendEventToServer()
	{
		if (isRegister)
		{
			SendEventParams params = new SendEventParams(this, productName, Register.getDevicePosition(this),
					productChannel);
			em.sendEvent(params);

		}
		else
		{
			// 注册一下
			register.register(this, productName, productChannel, new RegisterCommListener()
			{

				@Override
				public void result(boolean result)
				{
					isRegister = result;
				}
			});
		}

	}

	Handler handler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			sendEventToServer();
		}

	};

	class MyIBinder extends Binder
	{
	}

	@Override
	public void onDestroy()
	{
		timer.cancel();
		super.onDestroy();
	}
}

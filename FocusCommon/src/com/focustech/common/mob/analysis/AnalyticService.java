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
 * ����һ����̨���񣬸÷���ʱ������������û���Ϊ
 * 
 * @author chenkangpeng
 * 
 */
public class AnalyticService extends Service
{
	/**
	 * ��ʾһ����ʱ����ͨ���ö�ʱ���ɼ����������������û���Ϊ
	 */
	private Timer timer;
	/**
	 * ��ʾһ��ʱ��������ʱ������ʾ�೤ʱ�������������һ���û���Ϊ
	 */
	private int timeSpace;
	/***
	 * ����һ��Ĭ�ϵ�ʱ����,��ʱ������ʾÿ������Ӻ������������һ����Ϊ�ɼ������ݰ�
	 */
	private final int default_timespace = 60000;
	/**
	 * �¼���һ�������࣬ͨ������ɷ����¼���������
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

	// ������Ϊ���ݵ�������
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
			// ע��һ��
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

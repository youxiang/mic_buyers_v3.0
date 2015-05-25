package com.focustech.common.mob.analysis;

import java.util.List;

import android.content.Context;

import com.focustech.common.http.FocusClient;
import com.focustech.common.mob.MobJsonHttpResponseHandler;
import com.focustech.common.mob.BaseUrl;
import com.focustech.common.mob.DisposeDataListener;
import com.loopj.android.http.RequestParams;

/***
 * �����ʾ���¼���һЩ���������罫�¼����浽���ݿ⣬������Ϊ��������
 * 
 * @author chenkangpeng
 * 
 */
public class EventManager
{

	private EventSqliteOperate eventSqliteOperate;

	public EventManager(Context context)
	{
		eventSqliteOperate = new EventSqliteOperate(context);
	}

	/**
	 * �������ϵ��û���Ϊ���浽���ݿ���
	 * 
	 * @param event һ����Ϊ����
	 */
	public void saveEvent(BaseEvent event)
	{
		if (event == null)
		{
			return;
		}
		eventSqliteOperate.saveEvent(event);
	}

	/**
	 * 
	 * @param context ��ʾһ�������Ķ���
	 * @param productName ��Ʒm����
	 * @param productChannel ����
	 * @throws Exception
	 */
	public void sendEvent(SendEventParams sendEventParams)
	{
		// �����ݿ��л��event����
		final List<BaseEvent> evs = eventSqliteOperate.getEvent();
		if (evs == null || evs.size() == 0)
		{
			return;
		}
		try
		{
			String json = EventChangeUtil.changeToJson(evs);
			sendEventParams.setEventJson(json);
			send(sendEventParams, new DisposeDataListener()
			{

				@Override
				public void onSuccess(Object obj)
				{
					try
					{
						boolean b = anaJson(obj.toString());
						if (b)
						{
							eventSqliteOperate.delEvent(evs);
						}
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}

				@Override
				public void onFailure(Object failedReason)
				{

				}
			});
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void send(SendEventParams sendEventParams, DisposeDataListener listener)
	{
		RequestParams rparams = new RequestParams();
		rparams.add("userInfoData", sendEventParams.getEventJson());
		rparams.add("userPkId", sendEventParams.getUserPkId());
		rparams.add("productName", sendEventParams.getProductName());
		rparams.add("platformName", sendEventParams.getPlatformName());
		rparams.add("productChannel", sendEventParams.getProductChannel());
		rparams.add("productVersion", sendEventParams.getProductVersion());
		FocusClient.post(BaseUrl.BASE_URL_R + "/userActInfo" + "/collect.do", rparams, new MobJsonHttpResponseHandler(
				listener));
	}

	public boolean anaJson(String res)
	{
		if (res == null || res.length() == 0 || "error".equals(res))
		{
			return false;
		}
		try
		{
			return "true".equals(res) ? true : false;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
}

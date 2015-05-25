package com.focustech.common.mob.analysis;

import java.util.List;

import android.content.Context;

import com.focustech.common.http.FocusClient;
import com.focustech.common.mob.MobJsonHttpResponseHandler;
import com.focustech.common.mob.BaseUrl;
import com.focustech.common.mob.DisposeDataListener;
import com.loopj.android.http.RequestParams;

/***
 * 该类表示对事件的一些操作，例如将事件保存到数据库，发送行为到服务器
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
	 * 将界面上的用户行为保存到数据库中
	 * 
	 * @param event 一个行为对象
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
	 * @param context 表示一个上下文对象
	 * @param productName 产品m名称
	 * @param productChannel 渠道
	 * @throws Exception
	 */
	public void sendEvent(SendEventParams sendEventParams)
	{
		// 从数据库中获得event数据
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

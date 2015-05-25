package com.focustech.common.mob.analysis;

import android.content.Context;

import com.focustech.common.mob.BaseInfoParams;

public class SendEventParams extends BaseInfoParams
{

	/**
	 * 
	 */
	private static final long serialVersionUID= 1L;
	private String eventJson;
	public SendEventParams(Context context, String productName, String devicePosition, String productChannel)
	{
		super(context, productName, devicePosition, productChannel);
	}
	public String getEventJson()
	{
		return eventJson;
	}
	public void setEventJson(String eventJson)
	{
		this.eventJson= eventJson;
	}
	
}

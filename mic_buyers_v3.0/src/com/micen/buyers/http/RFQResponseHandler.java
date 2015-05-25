package com.micen.buyers.http;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.focustech.common.http.CommonJsonHttpResponseHandler;
import com.focustech.common.mob.DisposeDataListener;
import com.micen.buyers.module.rfq.RFQResponseCode;

/**********************************************************
 * @文件名称：RFQResponseHandler.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年5月4日 下午2:10:52
 * @文件描述：发布和编辑RFQ请求响应体
 * @修改历史：2015年5月4日创建初始版本
 **********************************************************/
public class RFQResponseHandler extends CommonJsonHttpResponseHandler
{
	public RFQResponseHandler(DisposeDataListener listener)
	{
		super(listener);
	}

	@Override
	public void onSuccess(JSONObject response)
	{
		disposeResponseData(response, mListener);
	}

	/**
	 * 处理请求返回的数据
	 * @param response
	 * @param listener
	 */
	public void disposeResponseData(JSONObject response, DisposeDataListener listener)
	{
		if (response == null)
		{
			listener.onFailure("Sorry, a data exception occurred, please retry.");
			return;
		}
		try
		{
			if (response.has("code"))
			{
				JSONArray codeArray = response.getJSONArray("code");
				if (codeArray.length() > 0)
				{
					if ("0".equals(codeArray.get(0).toString()))
					{
						listener.onSuccess(RFQResponseCode.RFQ_SUCCESS);
					}
					else
					{
						listener.onFailure(codeArray.get(0).toString());
					}
				}
				else
				{
					listener.onFailure("Sorry, a data exception occurred, please retry.");
				}
			}
			else
			{
				listener.onFailure("Sorry, a data exception occurred, please retry.");
			}
		}
		catch (JSONException e)
		{
			listener.onFailure("Sorry, a data exception occurred, please retry.");
			e.printStackTrace();
		}
	}
}

package com.focustech.common.mob.update;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

import com.focustech.common.http.ResponseEntityToModule;
import com.loopj.android.http.JsonHttpResponseHandler;

/**********************************************************
 * @文件名称：UpdateResponseHandler.java
 * @文件作者：xiongjiangwei
 * @创建时间：2014年10月23日 下午1:41:55
 * @文件描述：检查更新请求响应处理器
 * @修改历史：2014年10月23日创建初始版本
 **********************************************************/
public class UpdateResponseHandler extends JsonHttpResponseHandler
{
	protected UpdateListener mListener;

	public UpdateResponseHandler(UpdateListener listener)
	{
		this.mListener = listener;
	}

	@Override
	public void onSuccess(int statusCode, Header[] headers, JSONArray response)
	{
		disposeResponseData(response);
	}

	/**
	 * 处理服务器返回的数据
	 * @param jsonObj
	 * @param listener
	 * @param cls
	 */
	public void disposeResponseData(JSONArray jsonArray)
	{
		if (jsonArray == null)
		{
			mListener.onFailure();
			return;
		}
		try
		{
			Update update = (Update) ResponseEntityToModule.parseJsonObjectToModule(jsonArray.getJSONObject(0),
					Update.class);
			if (update != null)
			{
				mListener.onSuccess(update.isNewVersion(), update.isForceUpdate(), update);
			}
			else
			{
				mListener.onFailure();
			}
		}
		catch (JSONException e)
		{
			mListener.onFailure();
			e.printStackTrace();
		}
	}
}

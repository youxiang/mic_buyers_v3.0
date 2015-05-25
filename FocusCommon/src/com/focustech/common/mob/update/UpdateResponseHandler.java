package com.focustech.common.mob.update;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

import com.focustech.common.http.ResponseEntityToModule;
import com.loopj.android.http.JsonHttpResponseHandler;

/**********************************************************
 * @�ļ����ƣ�UpdateResponseHandler.java
 * @�ļ����ߣ�xiongjiangwei
 * @����ʱ�䣺2014��10��23�� ����1:41:55
 * @�ļ�������������������Ӧ������
 * @�޸���ʷ��2014��10��23�մ�����ʼ�汾
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
	 * ������������ص�����
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

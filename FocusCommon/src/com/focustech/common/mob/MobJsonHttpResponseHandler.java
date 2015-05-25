package com.focustech.common.mob;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;

public class MobJsonHttpResponseHandler extends JsonHttpResponseHandler
{
	private DisposeDataListener listener;

	public MobJsonHttpResponseHandler(DisposeDataListener listener)
	{
		this.listener = listener;
	}

	@Override
	public void onSuccess(int statusCode, Header[] headers, JSONArray response)
	{
		listener.onSuccess(response);
	}

	@Override
	public void onSuccess(int statusCode, Header[] headers, JSONObject response)
	{
		listener.onSuccess(response);
	}
}

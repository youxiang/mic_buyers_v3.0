package com.focustech.common.http;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import com.focustech.common.mob.DisposeDataListener;
import com.focustech.common.util.LogUtil;
import com.loopj.android.http.JsonHttpResponseHandler;

/**********************************************************
 * @文件名称：BaseHttpResponseHandler.java
 * @文件作者：xiongjiangwei
 * @创建时间：2013-12-30 下午12:00:03
 * @文件描述：所有信息回调的基类
 * @修改历史：2013-12-30创建初始版本
 **********************************************************/
public abstract class BaseHttpResponseHandler extends JsonHttpResponseHandler
{
	protected DisposeDataListener mListener;
	protected Class<?> mClass;

	public BaseHttpResponseHandler(DisposeDataListener listener)
	{
		this.mListener = listener;
	}

	public BaseHttpResponseHandler(DisposeDataListener listener, Class<?> clz)
	{
		this.mListener = listener;
		this.mClass = clz;
	}

	public void onSuccess(JSONObject response)
	{
	}

	public void onSuccess(JSONArray response)
	{
	}

	public void onSuccess(String responseString)
	{
	}

	public void onFailure(Throwable throwable)
	{
	}

	@Override
	public void onSuccess(int statusCode, Header[] headers, JSONObject response)
	{
		LogUtil.i("====HttpResponse====", response.toString());
		onSuccess(response);
	}

	@Override
	public void onSuccess(int statusCode, Header[] headers, JSONArray response)
	{
		LogUtil.i("====HttpResponse====", response.toString());
		onSuccess(response);
	}

	@Override
	public void onSuccess(int statusCode, Header[] headers, String responseString)
	{
		LogUtil.i("====HttpResponse====", responseString);
		onSuccess(responseString);
	}

	@Override
	public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
	{
		onFailure(throwable);
	}

	@Override
	public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse)
	{
		onFailure(throwable);
	}

	@Override
	public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
	{
		onFailure(throwable);
	}

}

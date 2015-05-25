package com.focustech.common.http;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;

import com.focustech.common.mob.DisposeDataListener;

/**********************************************************
 * @文件名称：CommonJsonHttpResponseHandler.java
 * @文件作者：xiongjiangwei
 * @创建时间：2014年6月5日 上午9:05:01
 * @文件描述：请求返回数据处理回调
 * @修改历史：2014年6月5日创建初始版本
 **********************************************************/
public class CommonJsonHttpResponseHandler extends BaseHttpResponseHandler
{
	public CommonJsonHttpResponseHandler(DisposeDataListener listener)
	{
		super(listener);
	}

	public CommonJsonHttpResponseHandler(DisposeDataListener listener, Class<?> cls)
	{
		super(listener, cls);
	}

	@Override
	public void onSuccess(JSONObject response)
	{
		disposeResponseData(response, mListener, mClass);
	}

	@Override
	public void onFailure(Throwable throwable)
	{
		switch (HttpException.getValueByTag(throwable.getClass().getSimpleName()))
		{
		case SocketException:
		case SocketTimeoutException:
			mListener.onFailure("Sorry, request timed out, please retry.");
			break;
		case ConnectException:
		case UnknownError:
		case UnknownHostException:
			mListener.onFailure("No internet access, please check your network and retry.");
			break;
		case InterruptedException:
			mListener.onFailure("Sorry, service request was interrupted, please retry.");
			break;
		default:
			mListener.onFailure("Sorry, a data exception occurred, please retry.");
			break;
		}
	}

	/**
	 * 处理服务器返回的数据
	 * @param jsonObj
	 * @param listener
	 * @param cls
	 */
	public void disposeResponseData(JSONObject jsonObj, DisposeDataListener listener, Class<?> cls)
	{
		if (jsonObj == null)
		{
			listener.onFailure("Sorry, a data exception occurred, please retry.");
			return;
		}
		try
		{
			if (jsonObj.has("code"))
			{
				if ("0".equals(jsonObj.getString("code")))
				{
					if (cls == null)
					{
						listener.onSuccess(jsonObj);
					}
					else
					{
						Object obj = ResponseEntityToModule.parseJsonObjectToModule(jsonObj, cls);
						if (obj != null)
						{
							listener.onSuccess(obj);
						}
						else
						{
							listener.onFailure("Sorry, a data exception occurred, please retry.");
						}
					}
				}
				else if ("99999".equals(jsonObj.getString("code")))
				{
					if (null != FocusClient.getHttpConfiguration())
					{
						// 发送广播通知
						Intent intent = new Intent();
						intent.putExtra("code", 99999);
						if (null != FocusClient.getHttpConfiguration().context)
						{
							FocusClient.getHttpConfiguration().context.sendBroadcast(intent);
						}
					}
				}
				else
				{
					if (jsonObj.has("err"))
					{
						listener.onFailure(jsonObj.getString("err"));
					}
					else
					{
						listener.onFailure("Sorry, a data exception occurred, please retry.");
					}
				}
			}
			else
			{
				if (jsonObj.has("err"))
				{
					listener.onFailure(jsonObj.getString("err"));
				}
				else
				{
					listener.onFailure("Sorry, a data exception occurred, please retry.");
				}
			}
		}
		catch (JSONException e)
		{
			listener.onFailure("Sorry, a data exception occurred, please retry.");
			e.printStackTrace();
		}
	}
}

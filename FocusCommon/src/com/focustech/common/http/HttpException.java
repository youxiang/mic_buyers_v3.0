package com.focustech.common.http;

import android.text.TextUtils;

/**********************************************************
 * @文件名称：HttpException.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年5月13日 下午5:29:51
 * @文件描述：网络请求异常枚举
 * @修改历史：2015年5月13日创建初始版本
 **********************************************************/
public enum HttpException
{
	SocketTimeoutException("SocketTimeoutException"), ConnectException("ConnectException"), SocketException(
			"SocketException"), UnknownHostException("UnknownHostException"), InterruptedException(
			"InterruptedException"), UnknownError("UnknownError");

	private String value;

	private HttpException(String value)
	{
		this.value = value;
	}

	public static HttpException getValueByTag(String type)
	{
		if (TextUtils.isEmpty(type))
		{
			return UnknownHostException;
		}
		for (HttpException status : HttpException.values())
		{
			if (status.value.equals(type))
			{
				return status;
			}
		}
		return UnknownHostException;
	}

	@Override
	public String toString()
	{
		return value;
	}
}

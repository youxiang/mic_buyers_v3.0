package com.focustech.common.http;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.ClientContext;

import android.content.Context;

import com.focustech.common.util.LogUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**********************************************************
 * @�ļ����ƣ�FocusClient.java
 * @�ļ����ߣ�xiongjiangwei
 * @����ʱ�䣺2013-10-25 ����10:20:43
 * @�ļ����������ӳأ����͸�������
 * @�޸���ʷ��2013-10-25������ʼ�汾
 **********************************************************/
public class FocusClient
{
	private static AsyncHttpClient client = new AsyncHttpClient();
	private final static String TAG = "======FocusClient======";

	private static HttpConfiguration config;

	//set httpconfiguration
	public static void init(HttpConfiguration httpConfiguration)
	{
		config = httpConfiguration;
	}

	//get httpconfiguration
	public static HttpConfiguration getHttpConfiguration()
	{
		return config;
	}

	private static void log(String url, RequestParams params)
	{
		LogUtil.i(TAG, url + (params != null ? "--->" + params.toString() : ""));
	}

	private static void log(String url)
	{
		log(url, null);
	}

	/**
	 * get����
	 * @param url
	 * @param responseHandler
	 */
	public static void get(String url, AsyncHttpResponseHandler responseHandler)
	{
		log(url);
		client.get(url, responseHandler);
	}

	/**
	 * get����
	 * @param url
	 * @param params
	 * @param responseHandler
	 */
	public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler)
	{
		log(url, params);
		client.get(url, params, responseHandler);
	}

	/**
	 * get����
	 * @param context
	 * @param url
	 * @param responseHandler
	 */
	public static void get(Context context, String url, AsyncHttpResponseHandler responseHandler)
	{
		log(url);
		client.get(context, url, responseHandler);
	}

	/**
	 * get����
	 * @param context
	 * @param url
	 * @param params
	 * @param responseHandler
	 */
	public static void get(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler)
	{
		log(url, params);
		client.get(context, url, params, responseHandler);
	}

	/**
	 * get����
	 * @param context
	 * @param url
	 * @param headers
	 * @param params
	 * @param responseHandler
	 */
	public static void get(Context context, String url, Header[] headers, RequestParams params,
			AsyncHttpResponseHandler responseHandler)
	{
		log(url, params);
		client.get(context, url, headers, params, responseHandler);
	}

	/**
	 * post����
	 * @param url
	 * @param responseHandler
	 */
	public static void post(String url, AsyncHttpResponseHandler responseHandler)
	{
		log(url);
		client.post(url, responseHandler);
	}

	/**
	 * post����
	 * @param url
	 * @param params
	 * @param responseHandler
	 */
	public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler)
	{
		log(url, params);
		client.post(url, params, responseHandler);
	}

	/**
	 * post����
	 * @param context
	 * @param url
	 * @param params
	 * @param responseHandler
	 */
	public static void post(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler)
	{
		log(url, params);
		client.post(context, url, params, responseHandler);
	}

	/**
	 * post����
	 * @param context
	 * @param url
	 * @param entity
	 * @param contentType
	 * @param responseHandler
	 */
	public static void post(Context context, String url, HttpEntity entity, String contentType,
			AsyncHttpResponseHandler responseHandler)
	{
		log(url);
		client.post(context, url, entity, contentType, responseHandler);
	}

	/**
	 * post����
	 * @param context
	 * @param url
	 * @param headers
	 * @param params
	 * @param contentType
	 * @param responseHandler
	 */
	public static void post(Context context, String url, Header[] headers, RequestParams params, String contentType,
			AsyncHttpResponseHandler responseHandler)
	{
		log(url, params);
		client.post(context, url, headers, params, contentType, responseHandler);
	}

	/**
	 * post����
	 * @param context
	 * @param url
	 * @param headers
	 * @param entity
	 * @param contentType
	 * @param responseHandler
	 */
	public static void post(Context context, String url, Header[] headers, HttpEntity entity, String contentType,
			AsyncHttpResponseHandler responseHandler)
	{
		log(url);
		client.post(context, url, headers, entity, contentType, responseHandler);
	}

	/**
	 * put����
	 * @param url
	 * @param responseHandler
	 */
	public static void put(String url, AsyncHttpResponseHandler responseHandler)
	{
		log(url);
		client.put(url, responseHandler);
	}

	/**
	 * put����
	 * @param url
	 * @param params
	 * @param responseHandler
	 */
	public static void put(String url, RequestParams params, AsyncHttpResponseHandler responseHandler)
	{
		log(url, params);
		client.put(url, params, responseHandler);
	}

	/**
	 * put����
	 * @param context
	 * @param url
	 * @param params
	 * @param responseHandler
	 */
	public static void put(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler)
	{
		log(url, params);
		client.put(context, url, params, responseHandler);
	}

	/**
	 * put����
	 * @param context
	 * @param url
	 * @param entity
	 * @param contentType
	 * @param responseHandler
	 */
	public static void put(Context context, String url, HttpEntity entity, String contentType,
			AsyncHttpResponseHandler responseHandler)
	{
		log(url);
		client.put(context, url, entity, contentType, responseHandler);
	}

	/**
	 * put����
	 * @param context
	 * @param url
	 * @param headers
	 * @param entity
	 * @param contentType
	 * @param responseHandler
	 */
	public static void put(Context context, String url, Header[] headers, HttpEntity entity, String contentType,
			AsyncHttpResponseHandler responseHandler)
	{
		log(url);
		client.put(context, url, headers, entity, contentType, responseHandler);
	}

	/**
	 * delete����
	 * @param url
	 * @param responseHandler
	 */
	public static void delete(String url, AsyncHttpResponseHandler responseHandler)
	{
		log(url);
		client.delete(url, responseHandler);
	}

	/**
	 * delete����
	 * @param context
	 * @param url
	 * @param responseHandler
	 */
	public static void delete(Context context, String url, AsyncHttpResponseHandler responseHandler)
	{
		log(url);
		client.delete(context, url, responseHandler);
	}

	/**
	 * delete����
	 * @param context
	 * @param url
	 * @param headers
	 * @param responseHandler
	 */
	public static void delete(Context context, String url, Header[] headers, AsyncHttpResponseHandler responseHandler)
	{
		log(url);
		client.delete(context, url, headers, responseHandler);
	}

	/**
	 * ����HTTP��������
	 * @param id
	 * @param obj
	 */
	public static void setHttpContextAttribute(String id, Object obj)
	{
		client.getHttpContext().setAttribute(id, obj);
	}

	/**
	 * ��ȡHTTP������ĳ������
	 * @param id
	 * @return
	 */
	public static Object getHttpContextAttribute(String id)
	{
		return client.getHttpContext().getAttribute(id);
	}

	/**
	 * ɾ��HTTP����ĳ������
	 * @param id
	 */
	public static void removeHttpContextAttribute(String id)
	{
		client.getHttpContext().removeAttribute(id);
	}

	/**
	 *  ����Cookie��HTTP������
	 * @param cookieStore
	 */
	public static void setCookieStore(CookieStore cookieStore)
	{
		client.setCookieStore(cookieStore);
	}

	/**
	 * ɾ�����������õ�Cookie
	 */
	public static void removeCookieStore()
	{
		removeHttpContextAttribute(ClientContext.COOKIE_STORE);
	}

	/**
	 * ���ó�ʱʱ��(��ʱ������ʹ�ñ�ģ�������������Ч)
	 * @param timeout
	 */
	public static void setTimeout(int timeout)
	{
		client.setTimeout(timeout);
	}
}

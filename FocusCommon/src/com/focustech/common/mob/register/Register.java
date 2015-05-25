package com.focustech.common.mob.register;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.focustech.common.http.FocusClient;
import com.focustech.common.mob.BaseInfoParams;
import com.focustech.common.mob.MobJsonHttpResponseHandler;
import com.focustech.common.mob.BaseUrl;
import com.focustech.common.mob.DisposeDataListener;
import com.focustech.common.mob.MobileTools;
import com.focustech.common.mob.PhoneConfigParams;
import com.focustech.common.mob.SystemTools;
import com.loopj.android.http.RequestParams;

/**
 * ********************************************************
 * @文件名称：Register.java
 * @文件作者：chenkangpeng
 * @创建时间：2014年5月19日 上午9:16:46
 * @文件描述：该类提供注册等方法
 * @修改历史：2014年5月19日创建初始版本
 *********************************************************
 */
public class Register
{
	/***
	 * 该方法用于注册
	 * @param context 一个上下文对象
	 * @param productName 产品名称
	 * @param productChannel 产品渠道
	 * @param listener 注册的监听器
	 */
	public void register(Context context, String productName, String productChannel, final RegisterCommListener listener)
	{
		BaseInfoParams baseInfoParams = getBaseParams(context, productName, productChannel);
		PhoneConfigParams phoneConfigParams = MobileTools.getMobileInfo(context);
		HashMap<String, String> maps = baseInfoParams.toMap();
		if (phoneConfigParams != null)
		{
			maps.putAll(phoneConfigParams.toMap());
		}
		RequestParams params = new RequestParams(maps);
		FocusClient.post(BaseUrl.BASE_URL_R + "/broadcast/registerUserPost", params, new MobJsonHttpResponseHandler(
				new DisposeDataListener()
				{

					@Override
					public void onSuccess(Object obj)
					{
						try
						{
							listener.result(analysisRegist((JSONObject) obj));
						}
						catch (Exception e)
						{
							listener.result(false);
						}
					}

					@Override
					public void onFailure(Object failedReason)
					{
						listener.result(false);
					}
				}));
	}

	/***
	 * 获得一个基本的参数
	 * @param context 
	 * @param productName 产品名称
	 * @param productChannel 渠道名称
	 * @return
	 */
	public BaseInfoParams getBaseParams(Context context, String productName, String productChannel)
	{
		BaseInfoParams baseConfig = new BaseInfoParams(MobileTools.getIMEI(context), productName, "android",
				SystemTools.getProductVersion(context), getDevicePosition(context), productChannel);
		return baseConfig;
	}

	/**
	 * 绑定或解绑
	 * @param params 如果appAcount 为空，则视为绑定， 否则为解除绑定
	 * @param listener 监听器，返回ture 表示操作成功，false 表示操作失败
	 */
	public void boundAccountOrNot(BaseInfoParams params, final RegisterCommListener listener)
	{
		RequestParams rparams = new RequestParams(params.toMap());
		FocusClient.get(BaseUrl.BASE_URL_R + "/fbc/boundAccountOrNot.do", rparams, new MobJsonHttpResponseHandler(
				new DisposeDataListener()
				{

					@Override
					public void onSuccess(Object obj)
					{
						try
						{
							listener.result(analysisCommResult((JSONObject) obj));
						}
						catch (Exception e)
						{
							e.printStackTrace();
							listener.result(false);
						}
					}

					@Override
					public void onFailure(Object failedReason)
					{
						listener.result(false);
					}
				}));
	}

	/**
	 * 
	 * @param params 基本参数
	 * @param pushInfo 
	 * @param isBoundAccount
	 * @param listener
	 */
	public void setEquipmentMark(BaseInfoParams params, String pushInfo, Boolean isBoundAccount,
			final RegisterCommListener listener)
	{
		RequestParams rparams = new RequestParams();
		rparams.add("pushInfo", pushInfo);
		rparams.add("productName", params.getProductName());
		rparams.add("userPkId", params.getUserPkId());
		rparams.add("platformName", params.getPlatformName());
		rparams.add("productChannel", params.getProductChannel());
		rparams.add("productVersion", params.getProductVersion());
		if (params.getAppAccount() != null && !"".equals(params.getAppAccount()))
		{
			rparams.add("appAccount", params.getAppAccount());
		}
		if (params.getIsReceive() != null)
		{
			rparams.add("isReceive", params.getIsReceive());
		}
		if (isBoundAccount != null && isBoundAccount)
		{
			rparams.add("isBoundAccount", isBoundAccount ? "1" : "0");
		}
		FocusClient.get(BaseUrl.BASE_URL_R + "/broadcast/updateEquip.do", rparams, new MobJsonHttpResponseHandler(
				new DisposeDataListener()
				{

					@Override
					public void onSuccess(Object obj)
					{
						try
						{
							listener.result(analysisCommResult((JSONObject) obj));
						}
						catch (Exception e)
						{
							e.printStackTrace();
							listener.result(false);
						}
					}

					@Override
					public void onFailure(Object failedReason)
					{
						listener.result(false);
					}
				}));
	}

	/***
	 * 设置推送开关，通过设置该信息，可以控制后台服务是否主动推送消息到客户端
	 * @param params BaseInfoParams
	 * @param listener
	 */
	public void setPushState(BaseInfoParams params, final RegisterCommListener listener)
	{
		RequestParams rparams = new RequestParams(params.toMap());
		FocusClient.get(BaseUrl.BASE_URL_R + "/fbc/setPushState.do", rparams, new MobJsonHttpResponseHandler(
				new DisposeDataListener()
				{

					@Override
					public void onSuccess(Object obj)
					{
						try
						{
							listener.result(analysisCommResult((JSONObject) obj));
						}
						catch (Exception e)
						{
							e.printStackTrace();
							listener.result(false);
						}
					}

					@Override
					public void onFailure(Object failedReason)
					{
						listener.result(false);
					}
				}));
	}

	public static String getDevicePosition(Context context)
	{
		return context.getSharedPreferences("devicePosition", 0).getString("devicePosition", "其他");
	}

	/***
	 * 解析注册的返回值
	 * 
	 * @param res
	 * @return
	 * @throws JSONException 
	 */
	public boolean analysisRegist(JSONObject jsonObject) throws JSONException
	{
		String isScuss = jsonObject.getString("registerUserRst");
		boolean b = "true".equals(isScuss) ? true : false;
		return b;
	}

	/**
	 * 解析其他返回值，通用
	 * @param str
	 * @return
	 * @throws JSONException
	 */
	public boolean analysisCommResult(String str) throws JSONException
	{
		JSONObject jsonObject = new JSONObject(str);
		String code = jsonObject.getString("rstCode");
		if ("1".equals(code))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * 解析其他返回值，通用
	 * @param str
	 * @return
	 * @throws JSONException
	 */
	public boolean analysisCommResult(JSONObject jsonObj) throws JSONException
	{
		String code = jsonObj.getString("rstCode");
		if ("1".equals(code))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public interface RegisterCommListener
	{
		public void result(boolean result);
	}
}

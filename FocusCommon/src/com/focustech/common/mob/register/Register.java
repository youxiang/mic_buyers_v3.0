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
 * @�ļ����ƣ�Register.java
 * @�ļ����ߣ�chenkangpeng
 * @����ʱ�䣺2014��5��19�� ����9:16:46
 * @�ļ������������ṩע��ȷ���
 * @�޸���ʷ��2014��5��19�մ�����ʼ�汾
 *********************************************************
 */
public class Register
{
	/***
	 * �÷�������ע��
	 * @param context һ�������Ķ���
	 * @param productName ��Ʒ����
	 * @param productChannel ��Ʒ����
	 * @param listener ע��ļ�����
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
	 * ���һ�������Ĳ���
	 * @param context 
	 * @param productName ��Ʒ����
	 * @param productChannel ��������
	 * @return
	 */
	public BaseInfoParams getBaseParams(Context context, String productName, String productChannel)
	{
		BaseInfoParams baseConfig = new BaseInfoParams(MobileTools.getIMEI(context), productName, "android",
				SystemTools.getProductVersion(context), getDevicePosition(context), productChannel);
		return baseConfig;
	}

	/**
	 * �󶨻���
	 * @param params ���appAcount Ϊ�գ�����Ϊ�󶨣� ����Ϊ�����
	 * @param listener ������������ture ��ʾ�����ɹ���false ��ʾ����ʧ��
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
	 * @param params ��������
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
	 * �������Ϳ��أ�ͨ�����ø���Ϣ�����Կ��ƺ�̨�����Ƿ�����������Ϣ���ͻ���
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
		return context.getSharedPreferences("devicePosition", 0).getString("devicePosition", "����");
	}

	/***
	 * ����ע��ķ���ֵ
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
	 * ������������ֵ��ͨ��
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
	 * ������������ֵ��ͨ��
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

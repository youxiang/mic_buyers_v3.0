package com.focustech.common.mob.feedback;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.focustech.common.http.FocusClient;
import com.focustech.common.mob.BaseInfoParams;
import com.focustech.common.mob.MobJsonHttpResponseHandler;
import com.focustech.common.mob.BaseUrl;
import com.focustech.common.mob.DisposeDataListener;
import com.loopj.android.http.RequestParams;

/**
 * ********************************************************
 * @�ļ����ƣ�HttpMsg.java
 * @�ļ����ߣ�chenkangpeng
 * @����ʱ�䣺2014��5��19�� ����10:04:45
 * @�ļ��������û�����
 * @�޸���ʷ��2014��5��19�մ�����ʼ�汾
 *********************************************************
 */
public class HttpMsg
{

	/**
	 * ������Ϣ��������
	 * 
	 * @param sendData ��ʾһ�������͵���Ϣ����
	 * @return boolean true ��ʾ������Ϣ�ɹ���false ��ʾ������Ϣʧ��
	 */
	public void sendMsg(Msg msg, BaseInfoParams baseInfoparams, DisposeDataListener listener)
	{
		RequestParams rparams = new RequestParams();
		rparams.add("userPkId", baseInfoparams.getUserPkId());
		rparams.add("productName", baseInfoparams.getProductName());
		rparams.add("platformName", baseInfoparams.getPlatformName());
		rparams.add("productChannel", baseInfoparams.getProductChannel());
		rparams.add("productVersion", baseInfoparams.getProductVersion());
		rparams.add("submitType", msg.getMsgType());
		if (msg.getMsgType().equals(Msg.TYPE_TEXT))
		{
			// ��������get��������еĿո�,�� %20����
			String info = msg.getMsgContent();
			info = info.replace(" ", "%20");
			rparams.add("submitInfo", info);
		}
		else
		{
			try
			{
				rparams.put("submitInfo", new File(msg.getMsgContent()));
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
		}
		if ("get".equals(baseInfoparams.getMethod()))
		{
			FocusClient.get(BaseUrl.BASE_URL_R + "/fbInterface" + "/submitInfo.do", rparams,
					new MobJsonHttpResponseHandler(listener));
		}
		else
		{
			FocusClient.post(BaseUrl.BASE_URL_R + "/fbInterface" + "/submitInfo.do", rparams,
					new MobJsonHttpResponseHandler(listener));
		}
	}

	public void getMsgFormServer(String state, BaseInfoParams params, DisposeDataListener listener)
	{
		RequestParams rparams = new RequestParams();
		rparams.add("userPkId", params.getUserPkId());
		rparams.add("productName", params.getProductName());
		rparams.add("platformName", params.getPlatformName());
		rparams.add("productChannel", params.getProductChannel());
		rparams.add("productVersion", params.getProductVersion());
		rparams.add("replyState", state);
		FocusClient.get(BaseUrl.BASE_URL_R + "/fbInterface" + "/respInfo.do", rparams, new MobJsonHttpResponseHandler(
				listener));

	}

	/**
	 * {"rstCode":"1","rstInfo":[{"replyInto":"�յ�","replyTime":"2013-01-09 10:05:15","replyType":"1001"}],"rstMsg":
	 * "�����ɹ�"}
	 */
	public List<Msg> analysisMsgResult(JSONObject jsonObject)
	{
		try
		{
			JSONArray jsonArray = jsonObject.getJSONArray("rstInfo");
			List<Msg> msgs = new ArrayList<Msg>();
			for (int i = 0; i < jsonArray.length(); i++)
			{
				JSONObject jb = jsonArray.getJSONObject(i);
				Msg msg = new Msg();
				msg.setMsgContent(jb.getString("replyInto"));
				msg.setMsgState(true);
				msg.setMsgType(jb.getString("replyType"));
				msg.setOut_or_in(Msg.IN);
				msg.setTime(jb.getString("replyTime"));
				msgs.add(msg);
			}
			return msgs;
		}
		catch (Exception e)
		{
			return null;
		}
	}

	/**
	 * �鿴�ظ�
	 */
	public void searchCallback()
	{
	}

	/***
	 * ����������Ϣ��ķ���ֵ
	 * 
	 * @param json ������������
	 * @return ����һ��boolean ���͵����ݣ�true ��ʾ���ͳɹ���false��ʾ����ʧ��
	 */
	public boolean analysisSendResult(JSONObject jsonObject)
	{
		try
		{
			String rstCode = jsonObject.getString("rstCode");
			// ֵ1 ��ʾ���ͳɹ�
			return "1".equals(rstCode);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
}

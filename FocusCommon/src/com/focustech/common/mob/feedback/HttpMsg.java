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
 * @文件名称：HttpMsg.java
 * @文件作者：chenkangpeng
 * @创建时间：2014年5月19日 上午10:04:45
 * @文件描述：用户反馈
 * @修改历史：2014年5月19日创建初始版本
 *********************************************************
 */
public class HttpMsg
{

	/**
	 * 发送信息到服务器
	 * 
	 * @param sendData 表示一个待发送的信息数据
	 * @return boolean true 表示发送信息成功，false 表示发送信息失败
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
			// 处理请求get请求参数中的空格,用 %20代替
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
	 * {"rstCode":"1","rstInfo":[{"replyInto":"收到","replyTime":"2013-01-09 10:05:15","replyType":"1001"}],"rstMsg":
	 * "操作成功"}
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
	 * 查看回复
	 */
	public void searchCallback()
	{
	}

	/***
	 * 解析发送信息后的返回值
	 * 
	 * @param json 待解析的数据
	 * @return 返回一个boolean 类型的数据，true 表示发送成功，false表示发送失败
	 */
	public boolean analysisSendResult(JSONObject jsonObject)
	{
		try
		{
			String rstCode = jsonObject.getString("rstCode");
			// 值1 表示发送成功
			return "1".equals(rstCode);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
}

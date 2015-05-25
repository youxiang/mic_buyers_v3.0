package com.focustech.common.mob.analysis;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 该类负责将事件对象转化成json描述数据
 * @author chenkangpeng
 *
 */
public class EventChangeUtil
{
	/**
	 * 这是一个静态方法，该方法可以将事件对象解析成json表示的数据
	 * @param events 一系列的事件对象
	 * @return 返回一个json数据
	 * @throws Exception
	 */
	public static String changeToJson(List<BaseEvent> events) throws Exception
	{
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < events.size(); i++)
		{
			BaseEvent event = events.get(i);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("eventName", event.getEventName());
			jsonObject.put("time", dataFormart(event.getEventTime()));
			jsonObject.put("param", event.getParams() == null ? "" : event.getParams());
			jsonArray.put(jsonObject);
		}
		return jsonArray.toString();
	}

	/**
	 * 这是一个静态方法，可以将长整型表示时间转成字符串 "yyyy-MM-dd HH:mm:dd" 类型的数据表示
	 * @param longTime 一个字符串表示的长整型数据
	 * @return 
	 */
	public static String dataFormart(String longTime)
	{
		Date date = new Date(Long.parseLong(longTime));
		SimpleDateFormat simpleDateFromat = new SimpleDateFormat("yyyy-MM-dd HH:mm:dd");
		return simpleDateFromat.format(date);
	}
}

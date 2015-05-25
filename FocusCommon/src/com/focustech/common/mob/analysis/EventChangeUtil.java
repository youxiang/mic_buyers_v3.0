package com.focustech.common.mob.analysis;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * ���ฺ���¼�����ת����json��������
 * @author chenkangpeng
 *
 */
public class EventChangeUtil
{
	/**
	 * ����һ����̬�������÷������Խ��¼����������json��ʾ������
	 * @param events һϵ�е��¼�����
	 * @return ����һ��json����
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
	 * ����һ����̬���������Խ������ͱ�ʾʱ��ת���ַ��� "yyyy-MM-dd HH:mm:dd" ���͵����ݱ�ʾ
	 * @param longTime һ���ַ�����ʾ�ĳ���������
	 * @return 
	 */
	public static String dataFormart(String longTime)
	{
		Date date = new Date(Long.parseLong(longTime));
		SimpleDateFormat simpleDateFromat = new SimpleDateFormat("yyyy-MM-dd HH:mm:dd");
		return simpleDateFromat.format(date);
	}
}

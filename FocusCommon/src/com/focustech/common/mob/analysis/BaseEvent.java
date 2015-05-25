package com.focustech.common.mob.analysis;

/***
 * ����һ��������¼�
 * 
 * @author chenkangpeng
 * 
 */
public class BaseEvent
{
	/**
	 * �ó����ֶα�ʾһ��ҳ���¼�
	 */
	public static final int PAGE_EVENT_TYPE = 1;
	/***
	 * �ó����ֶα�ʾһ������¼�
	 */
	public static final int CLICK_EVENT_TYPE = 2;
	/***
	 * ��ʾ���¼���id��ţ��ñ�������ݿ����ǵ�����
	 */
	private int id;
	/** �¼����� */
	private String eventName;
	/**
	 * �¼�������ʱ��
	 */
	private String eventTime;
	/***
	 * �¼�����
	 */
	private int eventType;
	/**
	 * ����¼�������
	 * 
	 * @return
	 */
	private String params;

	public int getEventType()
	{
		return eventType;
	}

	public void setEventType(int eventType)
	{
		this.eventType = eventType;
	}

	public String getEventTime()
	{
		return eventTime;
	}

	public void setEventTime(String eventTime)
	{
		this.eventTime = eventTime;
	}

	public String getEventName()
	{
		return eventName;
	}

	public void setEventName(String eventName)
	{
		this.eventName = eventName;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getParams()
	{
		return params;
	}

	public void setParams(String params)
	{
		this.params = params;
	}
}

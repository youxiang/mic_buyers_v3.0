package com.focustech.common.mob.feedback;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ����һ����Ϣ�����
 * 
 * @author chenkangpeng
 * 
 */
public class Msg
{
	/** �ַ�����������ʾ��Ϣ���������� */
	public static String TYPE_TEXT = "1001";
	/** �ַ�����������ʾ��Ϣ��ͼƬ���� */
	public static String TYPE_IMAGE = "1002";
	/** �ַ�����������ʾ��Ϣ����Ƶ���� */
	public static String TYPE_AUDIO = "1003";
	/** �ַ�����������ʾ��Ϣ���ͳɹ� */
	public static String STATE_SENDSUCCESS = "1";
	/** �ַ�����������ʾ��Ϣδ���� */
	public static String STATE_SENDFAIL = "0";
	/** ��Ϣ��id */
	private int id;
	/** int ��������ʾ�û����͵���Ϣ */
	public static int OUT = 0;
	/** int ��������ʾ�û����ܵ���Ϣ */
	public static int IN = 1;
	/** ��Ϣ���� */
	private String msgType;
	/** ��Ϣ���� */
	private String msgContent;
	/** �Ƿ��ͳ�ȥ�ģ����ǽ��ܵ��ģ�����ǽ��յ��� IN������Ƿ��ͳ�ȥ�� OUT */
	private int out_or_in;
	/** ��Ϣ���͵�ʱ�� */
	private String time;
	/** ��Ϣ����״̬��Ĭ����δ���� */
	private String msgState = STATE_SENDFAIL;

	public Msg(String msgType, String msgContent)
	{
		super();
		this.msgType = msgType;
		this.msgContent = msgContent;
	}

	public Msg()
	{
		super();
	}

	public String getMsgType()
	{
		return msgType;
	}

	public void setMsgType(String msgType)
	{
		this.msgType = msgType;
	}

	public String getMsgContent()
	{
		return msgContent;
	}

	public void setMsgContent(String msgContent)
	{
		this.msgContent = msgContent;
	}

	public String getTime()
	{
		return time;
	}

	public void setTime(String sendTime)
	{
		this.time = sendTime;
	}

	public int getOut_or_in()
	{
		return out_or_in;
	}

	public void setOut_or_in(int out_or_in)
	{
		this.out_or_in = out_or_in;
	}

	public String getMsgState()
	{
		return msgState;
	}

	public void setMsgState(String msgState)
	{
		this.msgState = msgState;
	}

	public void setMsgState(boolean isSendSuccss)
	{
		if (isSendSuccss)
		{
			this.msgState = STATE_SENDSUCCESS;
		}
		else
		{
			this.msgState = STATE_SENDFAIL;
		}
	}

	/**
	 * ��ø�ʽ��ʱ�� ,"yyyy-mm-dd hh:mm:ss"
	 * 
	 * @return
	 */
	public String getSendTimeFormat()
	{
		// "yyyy-MM-dd HH:mm:ss" ��24Сʱ�Ƶ�ʱ���ʽ�� �� "yyyy-MM-dd hh:mm:ss" ��12 Сʱ�Ƶ�ʱ���ʽ��
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(Long.parseLong(time));
		return simpleDateFormat.format(date);
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}
}

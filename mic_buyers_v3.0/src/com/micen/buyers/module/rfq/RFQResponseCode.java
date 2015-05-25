package com.micen.buyers.module.rfq;

import android.text.TextUtils;

/**********************************************************
 * @文件名称：RFQResponseCode.java
 * @文件作者：xiongjiangwei
 * @创建时间：2014-2-21 下午03:04:09
 * @文件描述：提交或编辑RFQ服务器返回的错误码枚举
 * @修改历史：2014-2-21创建初始版本
 **********************************************************/
public enum RFQResponseCode
{
	RFQ_SUCCESS("0"),
	RFQ_FAIL("1"),
	RFQ_NOT_AUTHORITY("12001"),
	RFQ_ID_CANNOT_NULL("12002"),
	RFQ_COM_IS_NULL("12003"),
	RFQ_COM_NOT_EXIST("12004"),
	RFQ_OPERATORNO_NOT_EXIST("12005"),
	RFQ_USER_NOT_EXIST("12006"),
	RFQ_SUBJECT_CANNOT_NULL("12007"),
	RFQ_SUBJECT_FLUNK("12008"),
	RFQ_SUBJECT_OVERDO("12009"),
	RFQ_CATEGORYID_NOT_EXIST("12010"),
	RFQ_DESCRIPTION_NOT_EXIST("12011"),
	RFQ_DESCRIPTION_FLUNK("12012"),
	RFQ_DESCRIPTION_OVERDO("12013"),
	RFQ_QUANTITY_NOT_EXIST("12014"),
	RFQ_QUANTITY_IS_NUMBER("12015"),
	RFQ_ENDTIME_NOT_EXIST("12016"),
	RFQ_UNKNOW_ERROR("12999");

	private String value;

	private RFQResponseCode(String value)
	{
		this.value = value;
	}

	public static RFQResponseCode getCodeValueByTag(String type)
	{
		if (TextUtils.isEmpty(type))
		{
			return RFQ_UNKNOW_ERROR;
		}
		for (RFQResponseCode code : RFQResponseCode.values())
		{
			if (code.value.equals(type))
			{
				return code;
			}
		}
		return RFQ_UNKNOW_ERROR;
	}

	public String toString()
	{
		return value;
	}
}

package com.micen.buyers.module.rfq;

import android.text.TextUtils;

/**********************************************************
 * @文件名称：RFQStatus.java
 * @文件作者：xiongjiangwei
 * @创建时间：2014-1-28 下午04:47:04
 * @文件描述：RFQ状态枚举
 * @修改历史：2014-1-28创建初始版本
 **********************************************************/
public enum RFQStatus
{
	Pending("pending,processing"), Quotation("responding,quoted"), Rejected("reedit,stopped"), Closed("no result,deleted");

	private String value;

	private RFQStatus(String value)
	{
		this.value = value;
	}

	public static RFQStatus getValueByTag(String type)
	{
		if (TextUtils.isEmpty(type))
		{
			return Quotation;
		}
		for (RFQStatus status : RFQStatus.values())
		{
			if (status.value.equals(type))
			{
				return status;
			}
		}
		return Quotation;
	}

	@Override
	public String toString()
	{
		return value;
	}
}

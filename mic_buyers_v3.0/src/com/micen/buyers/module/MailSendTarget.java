package com.micen.buyers.module;

import android.text.TextUtils;

/**********************************************************
 * @文件名称：MailSendTarget.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年4月14日 下午1:48:48
 * @文件描述：发送邮件类型
 * @修改历史：2015年4月14日创建初始版本
 **********************************************************/
public enum MailSendTarget
{
	None("none"), Reply("reply"), SendByCatcode("catCode"), SendByProductId("productId"), SendByCompanyId("companyId");

	private String value;

	private MailSendTarget(String value)
	{
		this.value = value;
	}

	public static MailSendTarget getValueByTag(String type)
	{
		if (TextUtils.isEmpty(type))
		{
			return None;
		}
		for (MailSendTarget target : MailSendTarget.values())
		{
			if (target.value.equals(type))
			{
				return target;
			}
		}
		return None;
	}

	public static String getValue(MailSendTarget target)
	{
		return target.value;
	}
}

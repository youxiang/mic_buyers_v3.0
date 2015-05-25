package com.micen.buyers.module;

import android.text.TextUtils;

/**********************************************************
 * @文件名称：LoginTarget.java
 * @文件作者：xiongjiangwei
 * @创建时间：2014-2-11 下午04:17:11
 * @文件描述：登录完成后的目的地
 * @修改历史：2014-2-11创建初始版本
 **********************************************************/
public enum LoginTarget
{
	None("none"), Message("message"), Quotation("quotation"), Sourcing("sourcing"), Member("member"), Favorate_Product(
			"favorate_product"), Favorate_Supplier("favorate_supplier"), Favorate_Category("favorate_category"), PostRFQ(
			"postRFQ"), SentInquiry("sentInquiry"), ClickForFavorite("clickForFavorite");

	private String value;

	private LoginTarget(String value)
	{
		this.value = value;
	}

	public static LoginTarget getValueByTag(String type)
	{
		if (TextUtils.isEmpty(type))
		{
			return None;
		}
		for (LoginTarget target : LoginTarget.values())
		{
			if (target.value.equals(type))
			{
				return target;
			}
		}
		return None;
	}

	public static String getValue(LoginTarget target)
	{
		return target.value;
	}
}

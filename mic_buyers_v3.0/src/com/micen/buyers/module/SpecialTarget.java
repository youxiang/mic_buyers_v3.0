package com.micen.buyers.module;

import android.text.TextUtils;

/**********************************************************
 * @文件名称：SpecialTarget.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年5月8日 上午11:43:10
 * @文件描述：专题行为类型
 * @修改历史：2015年5月8日创建初始版本
 **********************************************************/
public enum SpecialTarget
{
	None("-1"), Showroom("0"), ProductDetail("2"), WebAddress("4"), SpecialDetail("5");

	private String value;

	private SpecialTarget(String value)
	{
		this.value = value;
	}

	public static SpecialTarget getValueByTag(String type)
	{
		if (TextUtils.isEmpty(type))
		{
			return None;
		}
		for (SpecialTarget target : SpecialTarget.values())
		{
			if (target.value.equals(type))
			{
				return target;
			}
		}
		return None;
	}

	public static String getValue(SpecialTarget target)
	{
		return target.value;
	}
}

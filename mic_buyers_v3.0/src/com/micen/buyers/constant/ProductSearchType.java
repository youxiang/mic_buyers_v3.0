package com.micen.buyers.constant;

import android.text.TextUtils;

/**********************************************************
 * @文件名称：ProductSearchType.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年3月10日 上午10:32:45
 * @文件描述：产品搜索方式
 * @修改历史：2015年3月10日创建初始版本
 **********************************************************/
public enum ProductSearchType
{

	Unknow("-1"), Keyword("0"), Category("1"), CompanyId("2"), All("3");

	private String value;

	private ProductSearchType(String value)
	{
		this.value = value;
	}

	public static ProductSearchType getSearchTypeByTag(String tag)
	{
		if (TextUtils.isEmpty(tag))
		{
			return Unknow;
		}
		for (ProductSearchType searchType : ProductSearchType.values())
		{
			if (searchType.value.equals(tag))
			{
				return searchType;
			}
		}
		return Unknow;
	}

	public String getValue()
	{
		return value;
	}
}

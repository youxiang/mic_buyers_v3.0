package com.micen.buyers.module.category;

import java.io.Serializable;
import java.util.ArrayList;

/**********************************************************
 * @文件名称：Category.java
 * @文件作者：xiongjiangwei
 * @创建时间：2014年10月20日 上午10:45:44
 * @文件描述：目录详情数据模型
 * @修改历史：2014年10月20日创建初始版本
 **********************************************************/
public class Category implements Comparable<Category>, Serializable
{
	private static final long serialVersionUID = -9184199055609547103L;
	public String catCode;
	public String catLevel;
	public String catNameEn;
	public String catProperty;
	public String catStatus;
	public String catType;
	public String childCount;
	public String isFavorite;
	public String linkCatCode;
	public String parentCatCode;
	public String recId;
	public String indexChar;
	public String imgUrl;
	public String property;
	public ArrayList<Category> childCategories = new ArrayList<Category>();

	/**
	 *对应一级目录的图片
	 */
	public int order;

	public boolean isFavorite()
	{
		return "true".equals(isFavorite);
	}

	@Override
	public int compareTo(Category catagories)
	{
		int num = this.catNameEn.compareTo(catagories.catNameEn);
		if (num == 0)
		{
			return this.catCode.compareTo(catagories.catCode);
		}
		return num;
	}
}

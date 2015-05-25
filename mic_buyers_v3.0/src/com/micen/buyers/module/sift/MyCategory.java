package com.micen.buyers.module.sift;

import java.util.ArrayList;

/**********************************************************
 * @文件名称：MyCategory.java
 * @文件作者：renzhiqiang
 * @创建时间：2014年9月19日 上午10:36:26
 * @文件描述：真正的目录结构
 * @修改历史：2014年9月19日创建初始版本e
 **********************************************************/
public class MyCategory
{
	public String choosen;
	public String hasChiren;
	public String isAvailable;
	public String key;
	public String name;
	public String num;
	public int level = 0;
	public boolean isExpanded;
	public MyCategory parentCategory = null;
	public ArrayList<MyCategory> subCatalogs;
}

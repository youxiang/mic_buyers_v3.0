package com.micen.buyers.module.sift;

import java.io.Serializable;

/**********************************************************
 * @文件名称：SiftCategory.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年3月20日 下午2:27:17
 * @文件描述：
 * @修改历史：2015年3月20日创建初始版本
 **********************************************************/
public class SiftCategory implements Serializable
{
	private static final long serialVersionUID = 1L;
	public String choosen;
	public String hasChiren;
	public String isAvailable;
	public String key;
	public String name;
	public String num;
	//增加Int,用于排序
	public int intnum;
}

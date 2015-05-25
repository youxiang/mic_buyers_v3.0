package com.micen.buyers.module.user;

import java.io.Serializable;

/**********************************************************
 * @文件名称：TempUserInfo.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年2月4日 下午1:18:34
 * @文件描述：存储用户临时填写的信息
 * @修改历史：2015年2月4日创建初始版本
 **********************************************************/
public class TempUserInfo implements Serializable
{
	private static final long serialVersionUID = 1L;
	public String tempEmail;
	public String tempFullname;
	public String tempCompanyname;
	public String tempTelphone1;
	public String tempTelphone2;
	public String tempTelphone3;
	public String tempGender;
}

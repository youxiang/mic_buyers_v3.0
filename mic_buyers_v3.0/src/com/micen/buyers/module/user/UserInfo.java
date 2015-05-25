package com.micen.buyers.module.user;

/**********************************************************
 * @文件名称：UserInfo.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年4月27日 上午9:25:52
 * @文件描述：用户个人信息
 * @修改历史：2015年4月27日创建初始版本
 **********************************************************/
public class UserInfo
{
	/**
	 * 未读询盘邮件
	 */
	public String unreadMail;
	/**
	 * 未读报价条数 
	 */
	public String unreadQuotation;
	/**
	 * 用户名称
	 */
	public String fullName;
	/**
	 * 性别称谓：
	 * 	0：Mr.
	 * 	1：Mrs.
	 *	2：Ms.
	 *	3：Miss
	 */
	public String gender;
	/**
	 * 电子邮箱地址
	 */
	public String email;
	/**
	 * 邮箱是否验证：true验证
	 */
	public String isEmailConfirmed;
	/**
	 * 备用Email
	 */
	public String backEmail;
	/**
	 * 所在部门
	 */
	public String department;
	/**
	 * 职位
	 */
	public String position;
	/**
	 * 移动电话
	 */
	public String mobile;
	/**
	 * 当前操作人Id
	 */
	public String operatorId;
	/**
	 * 用户角色（发邮件时用）
	 */
	public String userRole;

	public void resetValue(UserInfo userInfo)
	{
		this.unreadMail = userInfo.unreadMail;
		this.unreadQuotation = userInfo.unreadQuotation;
		this.fullName = userInfo.fullName;
		this.email = userInfo.email;
		this.isEmailConfirmed = userInfo.isEmailConfirmed;
		this.backEmail = userInfo.backEmail;
		this.department = userInfo.department;
		this.position = userInfo.position;
		this.mobile = userInfo.mobile;
	}

}

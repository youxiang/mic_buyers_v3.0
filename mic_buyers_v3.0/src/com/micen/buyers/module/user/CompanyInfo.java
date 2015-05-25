package com.micen.buyers.module.user;

/**********************************************************
 * @文件名称：CompanyInfo.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年4月27日 上午9:26:00
 * @文件描述：用户公司信息
 * @修改历史：2015年4月27日创建初始版本
 **********************************************************/
public class CompanyInfo
{
	/**
	 * 所在公司Id
	 */
	public String companyId;
	/**
	 * 公司状态：
	 *	0：New
	 *	1：Approved
	 *	2：Rejected
	 *	3：Revised
	 *	4：Suspended
	 *	5：Deleted
	 */
	public String companyStat;
	/**
	 * 公司logo url
	 */
	public String logo;
	/**
	 * 公司名
	 */
	public String companyName;
	/**
	 * 会员类型：
	 *	1：金牌会员
	 *	2：合作会员
	 *	4：国内免费会员
	 *	8：国外免费（完整注册）
	 *	16：国外免费（无公司信息）
	 *	32：国外免费（公司信息不完整）
	 *	64：国外GB会员
	 *	128：国外PB会员
	 */
	public String memberType;
	/**
	 * 公司简介
	 */
	public String description;
	/**
	 * 交易类型
	 */
	public String businessType;
	/**
	 * 交易范围
	 */
	public String businessRange;
	/**
	 * 产品关键字
	 */
	public String productKeyword;
	/**
	 * 雇员数
	 */
	public String employeeNumber;
	/**
	 * 年营业额
	 */
	public String annualTurnover;
	/**
	 * 交易市场
	 */
	public String trademark;
	/**
	 * 国家
	 */
	public String country;
	/**
	 * 时区
	 */
	public String timeZone;
	/**
	 * 省份
	 */
	public String province;
	/**
	 * 城市
	 */
	public String city;
	/**
	 * 地址
	 */
	public String companyAddress;
	/**
	 * 电话号码
	 */
	public String telephone;
	/**
	 * 邮编
	 */
	public String zipCode;
	/**
	 * 传真
	 */
	public String fax;
	/**
	 * 主页URL
	 */
	public String homepage;
	/**
	 * 发件人IP地址
	 */
	public String ipAddress;
	/**
	 * IP地址对应区域
	 */
	public String ipLocation;
	/**
	 * 公司标识（发邮件时需要）
	 */
	public String companyIdentity;
	/**
	 * 会员级别（发邮件时需要）
	 */
	public String memberLevel;
	/**
	 * 是否是准会员(发邮件需要)
	 */
	public String isBeforePremium;

	public void resetValue(CompanyInfo companyInfo)
	{
		this.companyId = companyInfo.companyId;
		this.companyStat = companyInfo.companyStat;
		this.logo = companyInfo.logo;
		this.companyName = companyInfo.companyName;
		this.memberType = companyInfo.memberType;
		this.description = companyInfo.description;
		this.businessType = companyInfo.businessType;
		this.businessRange = companyInfo.businessRange;
		this.productKeyword = companyInfo.productKeyword;
		this.employeeNumber = companyInfo.employeeNumber;
		this.annualTurnover = companyInfo.annualTurnover;
		this.trademark = companyInfo.trademark;
		this.country = companyInfo.country;
		this.timeZone = companyInfo.timeZone;
		this.province = companyInfo.province;
		this.city = companyInfo.city;
		this.companyAddress = companyInfo.companyAddress;
		this.telephone = companyInfo.telephone;
		this.zipCode = companyInfo.zipCode;
		this.fax = companyInfo.fax;
		this.homepage = companyInfo.homepage;
		this.ipAddress = companyInfo.ipAddress;
		this.ipLocation = companyInfo.ipLocation;
	}

}

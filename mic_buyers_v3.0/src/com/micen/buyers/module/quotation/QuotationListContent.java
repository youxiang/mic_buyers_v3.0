package com.micen.buyers.module.quotation;

import java.util.ArrayList;

public class QuotationListContent
{
	public String messageReplaied;
	public String buyerRead;
	/**
	 * 邮件地址
	 */
	public String quoteEmail;
	public String rfqReader;
	/**
	 * Audited Supplier for x time
	 */
	public String asAuditTimes;
	public String id;
	public String quoteSource;
	public String quoteNo;
	public String fileAudit;
	public String gold;
	public String AS;
	public String baseAudit;
	/**
	 * 联系人名字
	 */
	public String quoteName;
	public String status;
	public String updaterName;
	public String updaterNO;
	public String repliedStatus;
	public String rfqId;
	/**
	 * Business Type
	 */
	public ArrayList<String> supplierType;
	/**
	 * 公司名
	 */
	public String quoteCompanyName;
	/**
	 * 公司Id
	 */
	public String comId;
	/**
	 * systemCertification
	 */
	public String systemCertification;
	public QuotationListTime quoteTime;
	public QuotationListTime rfqReceieveTime;
	public QuotationListTime updateTime;
	public ArrayList<QuotationListItem> items;
}

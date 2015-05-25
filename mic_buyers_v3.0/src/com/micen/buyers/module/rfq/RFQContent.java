package com.micen.buyers.module.rfq;

import java.io.Serializable;
import java.util.ArrayList;

public class RFQContent implements Serializable
{
	private static final long serialVersionUID = -1518966278205923639L;
	public String orderByType;
	public String targetPriceUnit;
	public String subject = "0";
	public String supplierEmployeesType;
	public String returnAdvise;
	public String supplierLocation;
	public String adderNo;
	public String detailDescription;
	public String pageNum;
	public String estimatedQuantity;
	public String quoteLeft;
	public String orderByField;
	public String estimatedQuantityType;
	public String status;
	public String shipmentTerms;
	public String unreadCount;
	public String destinationPort;
	public String categoryId;
	public String pageSize;
	public String targetPrice;
	public String supplierTypeOther;
	public String rfqId;
	public String category;
	public String supplierCertification;
	public String comId;
	public String rfqType;
	public RFQContentAddTime addTime;
	public String paymentTerms;
	public RFQContentValidateTimeEnd validateTimeEnd;
	public ArrayList<String> supplierType;
	public ArrayList<String> exportMarket;
	public ArrayList<RFQContentFiles> rfqFiles;
	/**
	 * RFQ编辑时临时存放从服务器获取的所有供应商类型信息，以逗号隔开
	 */
	public String allSupplierType;
	/**
	 * RFQ编辑时临时存放从服务器获取的所有出口地区信息，以逗号隔开
	 */
	public String allExportMarket;
	/**
	 * RFQ编辑时临时存放当前操作者ID
	 */
	public String operatorNo;
}

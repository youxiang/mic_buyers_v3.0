package com.micen.buyers.http;

import java.io.File;
import java.io.FileNotFoundException;

import android.graphics.BitmapFactory;
import android.util.Log;

import com.focustech.common.http.CommonJsonHttpResponseHandler;
import com.focustech.common.http.FocusClient;
import com.focustech.common.mob.DisposeDataListener;
import com.focustech.common.util.NetworkUtils;
import com.loopj.android.http.RequestParams;
import com.micen.buyers.activity.R;
import com.micen.buyers.constant.UrlConstants;
import com.micen.buyers.db.SharedPreferenceManager;
import com.micen.buyers.module.BaseResponse;
import com.micen.buyers.module.CheckResult;
import com.micen.buyers.module.RecommendProducts;
import com.micen.buyers.module.auditprofile.ARDetails;
import com.micen.buyers.module.category.Categories;
import com.micen.buyers.module.category.SearchCategory;
import com.micen.buyers.module.easysourcing.RfqAndQuotations;
import com.micen.buyers.module.favorite.Favorites;
import com.micen.buyers.module.mail.MailDetails;
import com.micen.buyers.module.mail.Mails;
import com.micen.buyers.module.mail.OperateMail;
import com.micen.buyers.module.product.ProductDetail;
import com.micen.buyers.module.quotation.QuotationDetail;
import com.micen.buyers.module.quotation.QuotationList;
import com.micen.buyers.module.rfq.RFQ;
import com.micen.buyers.module.rfq.RFQContent;
import com.micen.buyers.module.rfq.UploadFile;
import com.micen.buyers.module.search.SearchAuditReports;
import com.micen.buyers.module.search.SearchCompanies;
import com.micen.buyers.module.search.SearchProducts;
import com.micen.buyers.module.search.SearchSuggest;
import com.micen.buyers.module.showroom.CompanyDetail;
import com.micen.buyers.module.showroom.ProductGroups;
import com.micen.buyers.module.special.Special;
import com.micen.buyers.module.special.detail.SpecialDetail;
import com.micen.buyers.module.user.User;
import com.micen.buyers.util.BuyerApplication;
import com.micen.buyers.util.Util;

/**********************************************************
 * @文件名称：RequestManager.java
 * @文件作者：xiongjiangwei
 * @创建时间：2013-12-30 上午11:54:23
 * @文件描述：联网请求管理器
 * @修改历史：2013-12-30创建初始版本
 **********************************************************/
public class RequestCenter
{
	/**
	 * 用户登录请求
	 * @param username	用户名
	 * @param password	密码
	 * @param context
	 * @param lisener	登录结果回调
	 */
	public static void userLogin(String username, String password, DisposeDataListener lisener)
	{
		RequestParams params = new RequestParams();
		params.put("loginId", username);
		params.put("password", password);
		params.put("region", "MIC for Buyer Android");
		FocusClient.post(UrlConstants.USER_LOGIN, params, new CommonJsonHttpResponseHandler(lisener, User.class));
	}

	/**
	 * 用户注册请求
	 * @param country
	 * @param countryName
	 * @param email
	 * @param memberId
	 * @param password
	 * @param gender
	 * @param fullName
	 * @param companyName
	 * @param telephoneCountryCode
	 * @param telephoneAreaCode
	 * @param telephoneNumber
	 * @param context
	 * @param lisener
	 */
	public static void userRegister(String country, String countryName, String email, String password, String gender,
			String fullName, String companyName, String telephoneCountryCode, String telephoneAreaCode,
			String telephoneNumber, DisposeDataListener lisener)
	{
		RequestParams params = new RequestParams();
		params.put("country", country);
		params.put("countryName", countryName);
		params.put("email", email);
		params.put("password", password);
		params.put("gender", gender);
		params.put("fullName", fullName);
		params.put("companyName", companyName);
		params.put("telephoneCountryCode", telephoneCountryCode);
		params.put("telephoneAreaCode", "".equals(telephoneAreaCode) ? "0" : telephoneAreaCode);
		params.put("telephoneNumber", telephoneNumber);
		params.put("region", "MIC for Buyer Android");
		params.put("companyRole", "1");
		FocusClient.post(UrlConstants.USER_REGISTER, params, new CommonJsonHttpResponseHandler(lisener, User.class));
	}

	/**
	 * 推荐产品请求
	 * @param context
	 * @param lisener
	 */
	public static void getRecommendProducts(DisposeDataListener lisener)
	{
		RequestParams params = new RequestParams();
		params.put("sessionid", BuyerApplication.getInstance().getUser().sessionid);
		params.put("lastKeyword", SharedPreferenceManager.getInstance().getString("lastSearchKeyword", ""));
		params.put("userName", BuyerApplication.getInstance().getUser().content.userInfo.email);
		params.put("companyId", BuyerApplication.getInstance().getUser().content.companyInfo.companyId);
		params.put("count", "8");
		FocusClient.post(UrlConstants.GET_RECOMMEND_PRODUCTS, params, new CommonJsonHttpResponseHandler(lisener,
				RecommendProducts.class));
	}

	/**
	 * 收藏数据请求
	 * @param comId
	 * @param operateNo
	 * @param sourceType
	 * @param currentPage
	 * @param pageCount
	 * @param lisener
	 */
	public static void getFavoritesData(String sourceType, int pageNum, int pageSize, DisposeDataListener lisener)
	{
		RequestParams params = new RequestParams();
		params.add("comId", BuyerApplication.getInstance().getUser().content.companyInfo.companyId);
		params.add("operatorNo", BuyerApplication.getInstance().getUser().content.userInfo.operatorId);
		params.put("sourceType", sourceType);
		params.put("pageNum", String.valueOf(pageNum));
		params.put("pageSize", String.valueOf(pageSize));
		FocusClient.post(UrlConstants.GET_FAVORITES, params,
				new CommonJsonHttpResponseHandler(lisener, Favorites.class));
	}

	/**
	 * 用户详细信息请求
	 * @param context
	 * @param lisener
	 */
	public static void getStaffProfile(DisposeDataListener lisener)
	{
		getStaffProfile(lisener, BuyerApplication.getInstance().getUser().content.companyInfo.companyId, null, null);
	}

	/**
	 * 用户详细信息请求
	 * @param context
	 * @param lisener
	 */
	public static void getStaffProfile(DisposeDataListener lisener, String companyId, String mailId, String mailType)
	{
		RequestParams params = new RequestParams();
		params.put("sessionid", BuyerApplication.getInstance().getUser().sessionid);
		params.put("companyId", companyId);
		if (mailId != null)
		{
			params.put("mailId", mailId);
		}
		if (mailType != null)
		{
			params.put("mailType", mailType);
		}
		params.put("operatorId", BuyerApplication.getInstance().getUser().content.userInfo.operatorId);
		FocusClient
				.post(UrlConstants.GET_STAFF_PROFILE, params, new CommonJsonHttpResponseHandler(lisener, User.class));
	}

	/**
	 * RFQ列表请求
	 * @param context
	 * @param lisener
	 */
	public static void getRFQList(DisposeDataListener lisener, String status, int pageNo, int pageSize,
			String orderByType, String orderByField)
	{
		RequestParams params = new RequestParams();
		params.put("comId", BuyerApplication.getInstance().getUser().content.companyInfo.companyId);
		params.put("status", status);
		params.put("needExpired", "YES");
		params.put("pageNum", String.valueOf(pageNo));
		params.put("pageSize", String.valueOf(pageSize));
		params.put("orderByType", orderByType);
		params.put("orderByField", orderByField);// "VALIDATE_TIME_END"
		FocusClient.post(UrlConstants.GET_RFQ_LIST, params, new CommonJsonHttpResponseHandler(lisener, RFQ.class));
	}

	/**
	 * QR列表请求
	 * @param context
	 * @param lisener
	 */
	public static void getQuotationList(DisposeDataListener lisener, String rfqID)
	{
		RequestParams params = new RequestParams();
		params.put("comId", BuyerApplication.getInstance().getUser().content.companyInfo.companyId);
		params.put("rfqId", rfqID);
		FocusClient.post(UrlConstants.GET_QUOTATION_LIST, params, new CommonJsonHttpResponseHandler(lisener,
				QuotationList.class));
	}

	/**
	 * RFQ详情
	 * @param context
	 * @param lisener
	 */
	public static void getQuotationDetail(DisposeDataListener lisener, String rfqId, String quotationId)
	{
		RequestParams params = new RequestParams();
		params.put("comId", BuyerApplication.getInstance().getUser().content.companyInfo.companyId);
		params.put("rfqId", rfqId);
		params.put("quotationId", quotationId);
		FocusClient.post(UrlConstants.GET_QUOTATION_DETAIL, params, new CommonJsonHttpResponseHandler(lisener,
				QuotationDetail.class));
	}

	/***
	 * 删除收藏数据
	 * @param context
	 * @param listener
	 * @param data
	 */
	public static void delFavoriteData(String soureId, String soureType, DisposeDataListener listener)
	{
		RequestParams params = new RequestParams();
		params.put("comId", BuyerApplication.getInstance().getUser().content.companyInfo.companyId);
		params.put("operatorNo", BuyerApplication.getInstance().getUser().content.userInfo.operatorId);
		params.put("sourceType", soureType);
		params.put("sourceId", soureId);
		FocusClient.post(UrlConstants.DEL_FAVORITES_DATA, params, new CommonJsonHttpResponseHandler(listener,
				BaseResponse.class));
	}

	/**
	 * 添加收藏数据
	 * @param context
	 * @param soureType
	 * @param soureId
	 * @param sendFlag
	 * @param soureUrl
	 * @param soureSubject
	 * @param soureOfferType
	 * @param listener
	 */
	public static void addFavoriteData(String soureType, String soureId, String sendFlag, String soureUrl,
			String soureSubject, String soureOfferType, DisposeDataListener listener)
	{
		RequestParams params = new RequestParams();
		params.add("comId", BuyerApplication.getInstance().getUser().content.companyInfo.companyId);
		params.add("operatorNo", BuyerApplication.getInstance().getUser().content.userInfo.operatorId);
		params.add("sourceType", soureType);
		params.add("sourceId", soureId);
		params.add("sendFlag", sendFlag);
		// "2014/1/27 12:2:3"
		params.add("addTime", Util.getFormartTime("yyyy/MM/dd HH:mm:ss"));
		params.add("sourceUrl", soureUrl);
		params.add("sourceSubject", soureSubject);
		params.add("sourceOfferType", soureOfferType);
		FocusClient.post(UrlConstants.ADD_FAVORITES_DATA, params, new CommonJsonHttpResponseHandler(listener,
				BaseResponse.class));
	}

	/**
	 * RFQ详情请求
	 * @param context
	 * @param lisener
	 */
	public static void getRFQDetail(DisposeDataListener lisener, String rfqId)
	{
		RequestParams params = new RequestParams();
		params.put("comId", BuyerApplication.getInstance().getUser().content.companyInfo.companyId);
		params.put("rfqId", rfqId);
		FocusClient.post(UrlConstants.GET_RFQ_DETAIL, params, new CommonJsonHttpResponseHandler(lisener, RFQ.class));
	}

	/**
	 * 根据关键字获取目录列表请求
	 * @param context
	 * @param lisener
	 * @param productName
	 */
	public static void getCategoryListByKeyword(DisposeDataListener lisener, String keyword, boolean isFromSearch)
	{
		RequestParams params = new RequestParams();
		if (!isFromSearch)
		{
			if (BuyerApplication.getInstance().getUser() != null)
			{
				params.put("logUserName", BuyerApplication.getInstance().getUser().content.userInfo.email);
			}
			params.put("prodName", keyword);
		}
		else
		{
			params.put("keyword", keyword);
		}
		FocusClient.post(isFromSearch ? UrlConstants.GET_CATEGORY_BY_KEYWORD
				: UrlConstants.GET_CATEGORY_BY_PRODUCT_NAME, params, new CommonJsonHttpResponseHandler(lisener,
				SearchCategory.class));
	}

	/**
	 * 上传附件请求
	 * @param context
	 * @param lisener
	 * @param filePath
	 */
	public static void uploadFile(DisposeDataListener lisener, String filePath)
	{
		File file = new File(filePath);
		if (!file.exists())
		{
			return;
		}
		RequestParams params = new RequestParams();
		params.put("name", file.getName().substring(0, file.getName().indexOf(".")));
		params.put("originalFileName", file.getName());
		params.put("contentType", "rfqAttachment");
		try
		{
			params.put("uploadPhoto", file);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		params.put("comId", BuyerApplication.getInstance().getUser().content.companyInfo.companyId);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		params.put("picWidth", String.valueOf(options.outWidth));
		params.put("picHeight", String.valueOf(options.outHeight));
		params.put("fileSize", String.valueOf(file.length()));
		FocusClient
				.post(UrlConstants.UPLOAD_FILE, params, new CommonJsonHttpResponseHandler(lisener, UploadFile.class));
	}

	/**
	 * 添加或编辑RFQ请求
	 * @param context
	 * @param lisener
	 * @param url
	 * @param rfqContent
	 */
	public static void addOrUpdateRFQ(DisposeDataListener lisener, String url, RFQContent rfqContent)
	{
		RequestParams params = new RequestParams();
		params.put("comId", BuyerApplication.getInstance().getUser().content.companyInfo.companyId);
		params.put("subject", rfqContent.subject);
		params.put("detailDescription", rfqContent.detailDescription);
		params.put("estimatedQuantity", rfqContent.estimatedQuantity);
		params.put("estimatedQuantityUnit", rfqContent.estimatedQuantityType);
		params.put("validateTimeEnd", rfqContent.validateTimeEnd.time);
		params.put("rfqType", "1");
		if (rfqContent.rfqFiles != null && rfqContent.rfqFiles.size() > 0)
		{
			String attachmentIds = "";
			for (int i = 0; i < rfqContent.rfqFiles.size(); i++)
			{
				attachmentIds = attachmentIds + rfqContent.rfqFiles.get(i).fileId
						+ (i == rfqContent.rfqFiles.size() - 1 ? "" : ",");
			}
			if (!"".equals(attachmentIds))
			{
				params.put("attachmentIds", attachmentIds);
			}
		}
		params.put("categoryId", rfqContent.categoryId != null ? rfqContent.categoryId : "-1");
		if (rfqContent.rfqId != null)
		{
			params.put("rfqId", rfqContent.rfqId);
		}
		if (rfqContent.operatorNo != null)
		{
			params.put("operatorNo", rfqContent.operatorNo);
		}
		if (rfqContent.supplierLocation != null)
		{
			params.put("supplierLocation", rfqContent.supplierLocation);
		}
		if (rfqContent.supplierEmployeesType != null)
		{
			params.put("supplierEmployeesType", rfqContent.supplierEmployeesType);
		}
		if (rfqContent.supplierCertification != null)
		{
			params.put("supplierCertification", rfqContent.supplierCertification);
		}
		if (rfqContent.allSupplierType != null)
		{
			String[] types = rfqContent.allSupplierType.split(",");
			String[] localValues = BuyerApplication.getInstance().getResources()
					.getStringArray(R.array.mic_rfq_business_type);
			String typeValue = "";
			String otherTypeValue = "";
			boolean isOther = true;
			for (int i = 0; i < types.length; i++)
			{
				isOther = true;
				for (int j = 0; j < localValues.length; j++)
				{
					if (types[i].equals(localValues[j]))
					{
						isOther = false;
						break;
					}
				}
				if (!isOther)
				{
					typeValue = typeValue + types[i] + ",";
				}
				else
				{
					otherTypeValue = otherTypeValue + types[i] + ",";
				}
			}
			if (!"".equals(typeValue))
			{
				typeValue = typeValue.substring(0, typeValue.lastIndexOf(","));
			}
			if (!"".equals(otherTypeValue))
			{
				otherTypeValue = otherTypeValue.substring(0, otherTypeValue.lastIndexOf(","));
				params.put("supplierTypeOther", otherTypeValue);
				if ("".equals(typeValue))
				{
					typeValue = BuyerApplication.getInstance().getString(R.string.mic_rfq_other);
				}
				else
				{
					typeValue = typeValue + "," + BuyerApplication.getInstance().getString(R.string.mic_rfq_other);
				}
			}
			params.put("supplierType", typeValue);
		}
		if (rfqContent.allExportMarket != null)
		{
			params.put("exportMarket", rfqContent.allExportMarket);
		}
		if (rfqContent.shipmentTerms != null)
		{
			params.put("shipmentTerms", rfqContent.shipmentTerms);
		}
		if (rfqContent.targetPrice != null)
		{
			params.put("targetPrice", rfqContent.targetPrice);
		}
		if (rfqContent.targetPriceUnit != null)
		{
			params.put("targetPriceUnit", rfqContent.targetPriceUnit);
		}
		if (rfqContent.destinationPort != null)
		{
			params.put("destinationPort", rfqContent.destinationPort);
		}
		if (rfqContent.paymentTerms != null)
		{
			params.put("paymentTerms", rfqContent.paymentTerms);
		}
		FocusClient.post(url, params, new RFQResponseHandler(lisener));
	}

	/***
	 * 删除RFQ数据
	 * @param context
	 * @param rfq
	 * @param listener
	 */
	public static void delRFQByRFQId(DisposeDataListener listener, String rfqId)
	{
		RequestParams params = new RequestParams();
		params.put("comId", BuyerApplication.getInstance().getUser().content.companyInfo.companyId);
		params.put("rfqId", rfqId);
		FocusClient.post(UrlConstants.DEL_RFQ, params, new CommonJsonHttpResponseHandler(listener, BaseResponse.class));
	}

	/**
	 * 检查邮件请求
	 * @param context
	 * @param lisener
	 */
	public static void checkEmail(DisposeDataListener lisener, String email)
	{
		RequestParams params = new RequestParams();
		params.put("email", email);
		FocusClient.post(UrlConstants.CHECK_EMAIL, params,
				new CommonJsonHttpResponseHandler(lisener, CheckResult.class));
	}

	/**
	 * 搜索联想请求
	 * @param context
	 * @param lisener
	 */
	public static void searchSuggest(DisposeDataListener lisener, String value, String type)
	{
		RequestParams params = new RequestParams();
		params.put("nowText", value);
		params.put("searchType", type);
		FocusClient.post(UrlConstants.SEARCH_SUGGEST, params, new CommonJsonHttpResponseHandler(lisener,
				SearchSuggest.class));
	}

	/**
	 * 获取公司详情
	 * @param context
	 * @param lisener
	 */
	public static void getCompanyDetail(DisposeDataListener lisener, String comId)
	{
		RequestParams params = new RequestParams();
		if (BuyerApplication.getInstance().getUser() != null)
		{
			params.put("comId", BuyerApplication.getInstance().getUser().content.companyInfo.companyId);
			params.put("operatorId", BuyerApplication.getInstance().getUser().content.userInfo.operatorId);
		}
		params.put("companyId", comId);
		FocusClient.post(UrlConstants.GET_COMPANYDETAIL, params, new CommonJsonHttpResponseHandler(lisener,
				CompanyDetail.class));
	}

	/**
	 * 获取关键词搜索产品列表
	 * @param context
	 * @param lisener
	 */
	public static void searchProducts(DisposeDataListener lisener, String keyword, String category, String companyId,
			String isDisplay, String pageIndex, String pageSize, String needGM, String needAudit, String location,
			String property)
	{
		RequestParams params = new RequestParams();
		if (keyword == null)
		{
			params.put("keyword", "");
		}
		else
		{
			params.put("keyword", keyword);
		}
		if (category == null)
		{
			params.put("category", "");
		}
		else
		{
			params.put("category", category);
		}

		params.put("companyId", companyId);
		params.put("isDisplay", isDisplay);
		params.put("logonStatus", BuyerApplication.getInstance().getUser() != null ? "1" : "0");
		params.put("pageIndex", pageIndex);
		params.put("pageSize", pageSize);
		params.put("needGM", needGM);
		params.put("needAudit", needAudit);
		if (location != null && !"".equals(location))
		{
			params.put("location", location);
		}
		if (property != null && !"".equals(property))
		{
			params.put("property", property);
		}
		if (BuyerApplication.getInstance().getUser() != null)
		{
			params.put("comId", BuyerApplication.getInstance().getUser().content.companyInfo.companyId);
			params.put("operatorId", BuyerApplication.getInstance().getUser().content.userInfo.operatorId);
		}
		FocusClient.post(UrlConstants.SEARCH_PRODUCT, params, new CommonJsonHttpResponseHandler(lisener,
				SearchProducts.class));
	}

	/**
	 * 获取关键词搜索产品列表
	 * @param context
	 * @param lisener
	 */
	public static void searchMyProducts(DisposeDataListener lisener, String keyword, String category, String companyId,
			String isDisplay, String pageIndex, String pageSize, String needGM, String needAudit, String location,
			String property)
	{
		RequestParams params = new RequestParams();
		params.put("keyword", keyword);
		params.put("category", category);
		params.put("companyId", companyId);
		params.put("isDisplay", isDisplay);
		params.put("logonStatus", BuyerApplication.getInstance().getUser() != null ? "1" : "0");
		params.put("pageIndex", pageIndex);
		params.put("pageSize", pageSize);
		params.put("needGM", needGM);
		params.put("needAudit", needAudit);
		if (location != null && !"".equals(location))
		{
			params.put("location", location);
		}
		if (property != null && !"".equals(property))
		{
			params.put("property", property);
		}
		if (BuyerApplication.getInstance().getUser() != null)
		{
			params.put("comId", BuyerApplication.getInstance().getUser().content.companyInfo.companyId);
			params.put("operatorId", BuyerApplication.getInstance().getUser().content.userInfo.operatorId);
		}
		FocusClient.post(UrlConstants.SEARCH_MY_PRODUCT, params, new CommonJsonHttpResponseHandler(lisener,
				SearchProducts.class));
	}

	/**
	 * 获取公司产品列表
	 * @param context
	 * @param lisener
	 */
	public static void searchProducts(DisposeDataListener lisener, String keyword, String category, String companyId,
			String isDisplay, String pageIndex, String pageSize, String needGM, String needAudit)
	{
		searchProducts(lisener, keyword, category, companyId, isDisplay, pageIndex, pageSize, needGM, needAudit, null,
				"");
	}

	/**
	 * 获取关键词搜索公司列表
	 * @param context
	 * @param lisener
	 */
	public static void searchCompanies(DisposeDataListener lisener, String keyword, String category, String pageIndex,
			String pageSize, String needGM, String needAudit, String location)
	{
		RequestParams params = new RequestParams();
		params.put("keyWord", keyword);
		params.put("category", category);
		params.put("pageIndex", pageIndex);
		params.put("pageSize", pageSize);
		params.put("needGM", needGM);
		params.put("needAudit", needAudit);
		params.put("location", location);
		if (BuyerApplication.getInstance().getUser() != null)
		{
			params.put("comId", BuyerApplication.getInstance().getUser().content.companyInfo.companyId);
			params.put("operatorId", BuyerApplication.getInstance().getUser().content.userInfo.operatorId);
		}
		FocusClient.post(UrlConstants.SEARCH_COMPANY, params, new CommonJsonHttpResponseHandler(lisener,
				SearchCompanies.class));
	}

	/**
	 * 根据关键词搜索AR报告
	 * @param context
	 * @param lisener
	 * @param keyword
	 * @param pageIndex
	 * @param pageSize
	 */
	public static void searchAuditReports(DisposeDataListener lisener, String keyword, String pageIndex, String pageSize)
	{
		RequestParams params = new RequestParams();
		params.put("keyword", keyword);
		params.put("pageIndex", pageIndex);
		params.put("pageSize", pageSize);
		FocusClient.post(UrlConstants.SEARCH_AUDITREPORT, params, new CommonJsonHttpResponseHandler(lisener,
				SearchAuditReports.class));
	}

	/**
	 * 获取公司产品组列表
	 * @param context
	 * @param lisener
	 * @param companyId
	 * @param groupId
	 * @param pageIndex
	 * @param pageSize
	 * @param isDisplay
	 */
	public static void getGroupedProducts(DisposeDataListener lisener, String companyId, String groupId,
			String groupLevel, String pageIndex, String pageSize, String isDisplay)
	{
		RequestParams params = new RequestParams();
		params.put("companyId", companyId);
		params.put("groupId", groupId);
		params.put("groupLevel", groupLevel);
		params.put("pageIndex", pageIndex);
		params.put("pageSize", pageSize);
		params.put("isDisplay", isDisplay);
		params.put("logonStatus", BuyerApplication.getInstance().getUser() != null ? "7" : "0");
		if (BuyerApplication.getInstance().getUser() != null)
		{
			params.put("comId", BuyerApplication.getInstance().getUser().content.companyInfo.companyId);
			params.put("operatorId", BuyerApplication.getInstance().getUser().content.userInfo.operatorId);
		}
		FocusClient.post(UrlConstants.SEARCH_GROUPED_PRODUCT, params, new CommonJsonHttpResponseHandler(lisener,
				SearchProducts.class));
	}

	/**
	 * 发送邮件
	 * @param context
	 * @param lisener
	 * @param subject
	 * @param body
	 * @param receiverId
	 * @param currentEmail
	 * @param fullName
	 * @param gender
	 * @param wantAS
	 * @param wantMICInfo
	 * @param senderCompanyId
	 * @param companyName
	 * @param comTelphone1
	 * @param comTelphone2
	 * @param comTelphone3
	 * @param catCode
	 * @param sourceType
	 * @param sourceId
	 * @param logonStatus
	 * @param companyIdentity
	 * @param senderRole
	 * @param senderHomepage
	 * @param senderCountry
	 * @param memberLevel
	 * @param isBeforePremium
	 * @param sessionid
	 * @param operatorId
	 * @param isReplyQuotation	是否回复报价
	 */
	public static void sendMail(DisposeDataListener lisener, String subject, String body, String receiverId,
			String currentEmail, String fullName, String gender, String wantAS, String wantMICInfo,
			String senderCompanyId, String companyName, String comTelphone1, String comTelphone2, String comTelphone3,
			String catCode, String sourceType, String sourceId, String logonStatus, String companyIdentity,
			String senderRole, String senderHomepage, String senderCountry, String memberLevel, String isBeforePremium,
			String sessionid, String operatorId, boolean isReplyQuotation)
	{
		RequestParams params = new RequestParams();
		params.put("region", "MIC for Buyer Android" + (isReplyQuotation ? "_quotation" : ""));
		params.put("subject", subject);
		params.put("body", body);
		params.put("receiverId", receiverId);
		params.put("currentEmail", currentEmail);
		params.put("fullName", fullName);
		params.put("gender", gender);
		params.put("wantAS", wantAS);
		params.put("wantMICInfo", wantMICInfo);
		params.put("senderCompanyId", senderCompanyId);
		params.put("companyName", companyName);
		params.put("comTelphone1", comTelphone1);
		params.put("comTelphone2", comTelphone2);
		params.put("comTelphone3", comTelphone3);
		params.put("catCode", catCode);
		params.put("sourceType", sourceType);
		params.put("sourceId", sourceId);
		params.put("logonStatus", logonStatus);
		params.put("companyIdentity", companyIdentity);
		params.put("senderRole", senderRole);
		params.put("senderHomepage", senderHomepage);
		params.put("senderCountry", senderCountry);
		params.put("memberLevel", memberLevel);
		params.put("isBeforePremium", isBeforePremium);
		params.put("sessionid", sessionid);
		params.put("operatorId", operatorId);
		params.put("senderIp", NetworkUtils.getIPAddress());
		FocusClient.post(UrlConstants.SEND_MAIL, params, new CommonJsonHttpResponseHandler(lisener, OperateMail.class));
	}

	/**
	 * 回复邮件
	 * @param context
	 * @param lisener
	 * @param subject
	 * @param body
	 * @param gender
	 * @param mailSource
	 * @param receiverCompanyName
	 * @param receiverName
	 * @param mailId
	 * @param comTel1
	 * @param comTel2
	 * @param comTel3
	 * @param comFax1
	 * @param comFax2
	 * @param comFax3
	 */
	public static void replyMail(DisposeDataListener lisener, String subject, String body, String gender,
			String mailSource, String receiverCompanyName, String receiverName, String mailId, String comTel1,
			String comTel2, String comTel3, String comFax1, String comFax2, String comFax3)
	{
		User user = BuyerApplication.getInstance().getUser();
		RequestParams params = new RequestParams();
		params.put("region", "MIC for Buyer Android_reply");
		params.put("subject", subject);
		params.put("body", body);
		params.put("sessionid", user.sessionid);
		params.put("companyId", user.content.companyInfo.companyId);// 登陆用户所在公司id
		params.put("operatorId", user.content.userInfo.operatorId);// 登陆用户操作id
		params.put("senderEmail", user.content.userInfo.email);
		params.put("senderName", user.content.userInfo.fullName);
		params.put("senderSex", gender);
		params.put("wantAS", "true");
		params.put("mailSource", mailSource);
		params.put("senderCompanyName", user.content.companyInfo.companyName);
		params.put("senderCountry", user.content.companyInfo.country);
		params.put("senderHomepage", user.content.companyInfo.homepage);
		params.put("receiverCompanyName", receiverCompanyName);
		params.put("receiverName", receiverName);
		params.put("mailId", mailId);
		params.put("comTel1", comTel1);
		params.put("comTel2", comTel2);
		params.put("comTel3", comTel3);
		params.put("comFax1", comFax1);
		params.put("comFax2", comFax2);
		params.put("comFax3", comFax3);
		params.put("senderIp", NetworkUtils.getIPAddress());
		FocusClient
				.post(UrlConstants.REPLY_MAIL, params, new CommonJsonHttpResponseHandler(lisener, OperateMail.class));
	}

	/**
	 * 删除邮件
	 * @param context
	 * @param lisener
	 * @param mailId
	 * @param action
	 */
	public static void deleteMail(DisposeDataListener lisener, String mailId, String action)
	{
		User user = BuyerApplication.getInstance().getUser();
		RequestParams params = new RequestParams();
		params.put("sessionid", user.sessionid);
		params.put("companyId", user.content.companyInfo.companyId);
		params.put("operatorId", user.content.userInfo.operatorId);
		params.put("mailId", mailId);
		params.put("action", action);
		FocusClient.post(UrlConstants.DELETE_MAIL, params,
				new CommonJsonHttpResponseHandler(lisener, OperateMail.class));
	}

	/**
	 * 获取收件箱或发件箱邮件详情
	 * @param context
	 * @param lisener
	 * @param mailId
	 * @param isSent	是否为发件箱
	 */
	public static void getMailDetail(DisposeDataListener lisener, String mailId, boolean isSent)
	{
		User user = BuyerApplication.getInstance().getUser();
		RequestParams params = new RequestParams();
		params.put("sessionid", user.sessionid);
		params.put("companyId", user.content.companyInfo.companyId);
		params.put("operatorId", user.content.userInfo.operatorId);
		params.put("emailId", mailId);
		FocusClient.post(isSent ? UrlConstants.GET_SENT_DETAIL : UrlConstants.GET_INBOX_DETAIL, params,
				new CommonJsonHttpResponseHandler(lisener, MailDetails.class));
	}

	/**
	 * 获取邮件列表
	 * @param context
	 * @param lisener
	 * @param keyword
	 * @param pageIndex
	 * @param pageSize
	 * @param scope
	 * @param action
	 */
	public static void getMailList(DisposeDataListener lisener, String keyword, String pageIndex, String pageSize,
			String scope, String action)
	{
		User user = BuyerApplication.getInstance().getUser();
		RequestParams params = new RequestParams();
		params.put("sessionid", user.sessionid);
		params.put("companyId", user.content.companyInfo.companyId);
		params.put("operatorId", user.content.userInfo.operatorId);
		params.put("keyword", keyword);
		params.put("pageIndex", pageIndex);
		params.put("pageSize", pageSize);
		params.put("scope", scope);
		params.put("action", action);
		params.put("assignFlag", "");
		FocusClient.post(UrlConstants.GET_MAIL_LIST, params, new CommonJsonHttpResponseHandler(lisener, Mails.class));
	}

	/**
	 * 获取AR报告详情
	 * @param context
	 * @param lisener
	 * @param companyId
	 */
	public static void getARDetail(DisposeDataListener lisener, String companyId)
	{
		RequestParams params = new RequestParams();
		params.put("companyId", companyId);
		FocusClient.post(UrlConstants.GET_AR_DETAIL, params,
				new CommonJsonHttpResponseHandler(lisener, ARDetails.class));
	}

	/**
	 * 获取行业目录列表
	 * @param context
	 * @param lisener
	 * @param catCode
	 * @param catLevel
	 */
	public static void getCategoryList(DisposeDataListener lisener, String catCode, String catLevel, String repTime)
	{
		RequestParams params = new RequestParams();
		params.put("catCode", catCode);
		params.put("catLevel", catLevel);
		params.put("repTime", repTime);
		// 如果已经登录，请求增加该参数可查询目录是否收藏
		if (BuyerApplication.getInstance().getUser() != null)
		{
			params.put("operatorId", BuyerApplication.getInstance().getUser().content.userInfo.operatorId);
			params.put("comId", BuyerApplication.getInstance().getUser().content.companyInfo.companyId);
		}
		FocusClient.post(UrlConstants.GET_CATEGORYLIST, params, new CommonJsonHttpResponseHandler(lisener,
				Categories.class));
	}

	/**
	 * 获取产品详情
	 * @param context
	 * @param lisener
	 * @param productId
	 */
	public static void getProductDetail(DisposeDataListener lisener, String productId)
	{
		RequestParams params = new RequestParams();
		params.put("productId", productId);
		params.put("isDisplay", SharedPreferenceManager.getInstance().getBoolean("isDisplaySafeImage", false) ? "2"
				: "1");
		params.put("logonStatus", BuyerApplication.getInstance().getUser() != null ? "7" : "0");
		if (BuyerApplication.getInstance().getUser() != null)
		{
			params.put("comId", BuyerApplication.getInstance().getUser().content.companyInfo.companyId);
			params.put("operatorId", BuyerApplication.getInstance().getUser().content.userInfo.operatorId);
		}
		FocusClient.post(UrlConstants.GET_PRODUCTDETAIL, params, new CommonJsonHttpResponseHandler(lisener,
				ProductDetail.class));
	}

	/**
	 * 检查是否收藏请求
	 * @param context
	 * @param lisener
	 * @param sourceId
	 * @param sourceType
	 */
	public static void checkFavourite(DisposeDataListener lisener, String sourceId, String sourceType)
	{
		RequestParams params = new RequestParams();
		if (BuyerApplication.getInstance().getUser() != null)
		{
			params.put("comId", BuyerApplication.getInstance().getUser().content.companyInfo.companyId);
			params.put("operatorNo", BuyerApplication.getInstance().getUser().content.userInfo.operatorId);
		}
		params.put("sourceId", sourceId);
		params.put("sourceType", sourceType);
		FocusClient.post(UrlConstants.CHECK_FAVOURITE, params, new CommonJsonHttpResponseHandler(lisener,
				CheckResult.class));
	}

	/**
	 * 获取公司产品分组列表
	 * @param context
	 * @param lisener
	 * @param companyId
	 */
	public static void getCompanyGroup(DisposeDataListener lisener, String companyId)
	{
		RequestParams params = new RequestParams();
		params.put("comId", companyId);
		FocusClient.post(UrlConstants.GET_COMPANY_GROUP, params, new CommonJsonHttpResponseHandler(lisener,
				ProductGroups.class));
	}

	/**
	 * 发送找回密码请求请求
	 * @param context
	 * @param lisener
	 * @param companyId
	 */
	public static void requestPassword(DisposeDataListener lisener, String email)
	{
		RequestParams params = new RequestParams();
		params.put("email", email);
		FocusClient.post(UrlConstants.REQUEST_PASSWORD, params, new CommonJsonHttpResponseHandler(lisener,
				BaseResponse.class));
	}

	/**
	 * 发送Feedback请求
	 * @param context
	 * @param listener
	 * @param ret
	 * @param comment
	 * @param senderMail
	 * @param senderName
	 * @param senderGender
	 * @param senderCom
	 * @param contactSubject
	 * @param logonStatus
	 * @param operatorNo
	 */
	public static void sendFeedBack(DisposeDataListener listener, String comment, String senderMail, String senderName,
			String senderGender, String contactSubject)
	{
		RequestParams params = new RequestParams();
		/**
		 * 放入必填项，必须不能为空
		 */
		params.put("contactSubject", contactSubject);
		params.put("comment", comment);
		params.put("senderMail", senderMail);
		params.put("senderGender", senderGender);
		params.put("senderName", senderName);
		params.put("logonStatus", "0");
		/**
		 * 放入选填字段
		 */
		if (BuyerApplication.getInstance().getUser() != null)
		{
			User user = BuyerApplication.getInstance().getUser();
			params.put("relevantUrl", user.content.userInfo.email);
			params.put("relevantAddress", user.content.companyInfo.companyAddress);
			params.put("logonStatus", "7");
			params.put("comId", user.content.companyInfo.companyId);
			params.put("senderCom", user.content.companyInfo.companyName);
			params.put("operatorNo", user.content.userInfo.operatorId);
		}

		FocusClient.post(UrlConstants.SEND_FEEDBACK, params, new CommonJsonHttpResponseHandler(listener,
				BaseResponse.class));
	}

	/**
	 * 上传异常日志 
	 */
	public static void uploadExceptionFile(DisposeDataListener listener, File file)
	{
		if (!file.exists())
		{
			return;
		}

		RequestParams params = new RequestParams();
		try
		{
			// 临时参数，待确定
			params.put("exceptionFile", file);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		Log.e("=============", file.length() + " ");
		// 临时请求路径，待确定
		FocusClient.post(UrlConstants.UPLOAD_FILE, params, new CommonJsonHttpResponseHandler(listener,
				BaseResponse.class));
	}

	/**
	 * 获取专题列表
	 * @param listener
	 */
	public static void getSpecialList(DisposeDataListener listener)
	{
		FocusClient.post(UrlConstants.GET_SPECIAL_LIST, new CommonJsonHttpResponseHandler(listener, Special.class));
	}

	/**
	 * 获取专题详情
	 * @param listener
	 * @param specialId
	 */
	public static void getSpecialDetail(DisposeDataListener listener, String specialId)
	{
		RequestParams params = new RequestParams();
		params.put("specialId", specialId);
		FocusClient.post(UrlConstants.GET_SPECIAL_DETAIL, params, new CommonJsonHttpResponseHandler(listener,
				SpecialDetail.class));
	}

	/**
	 * 获取EasySourcing页按目录数量统计RFQ和Quotation总数
	 * @param listener
	 */
	public static void getEasySourcingCategory(DisposeDataListener listener)
	{
		FocusClient.post(UrlConstants.GET_EASY_SOURCING_CATEGORY, new CommonJsonHttpResponseHandler(listener,
				RfqAndQuotations.class));
	}
}

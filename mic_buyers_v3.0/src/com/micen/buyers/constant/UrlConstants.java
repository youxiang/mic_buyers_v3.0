package com.micen.buyers.constant;

/**********************************************************
 * @文件名称：UrlConstants.java
 * @文件作者：xiongjiangwei
 * @创建时间：2013-12-31 上午11:28:55
 * @文件描述：接口请求地址常量
 * @修改历史：2013-12-31创建初始版本
 **********************************************************/
public class UrlConstants
{
	/**
	 * 请求头地址
	 */
	// public static final String BASE_HOST = "http://192.168.16.254:7001";
	public static final String BASE_HOST = "http://mic.sp.wfeature.com";
	/**
	 * 请求协议名称
	 */
	public static final String HOST_NAME = "/openapi_mic/";
	/**
	 * 登录请求地址
	 */
	public static final String USER_LOGIN = BASE_HOST + HOST_NAME + "login";
	/**
	 * 注册请求地址
	 */
	public static final String USER_REGISTER = BASE_HOST + HOST_NAME + "register";
	/**
	 * 获取推荐产品请求地址
	 */
	public static final String GET_RECOMMEND_PRODUCTS = BASE_HOST + HOST_NAME + "getRecommendProductList";
	/**
	 * 获取登录用户的最新信息请求地址
	 */
	public static final String GET_STAFF_PROFILE = BASE_HOST + HOST_NAME + "profile";
	/**
	 * RFQ列表请求地址
	 */
	public static final String GET_RFQ_LIST = BASE_HOST + HOST_NAME + "getRFQList";
	/***
	 * 获得收藏数据的请求地址
	 */
	public static final String GET_FAVORITES = BASE_HOST + HOST_NAME + "getFavoriteList";
	/***
	 * 报价列表请求地址
	 */
	public static final String GET_QUOTATION_LIST = BASE_HOST + HOST_NAME + "getQuotationList";
	/***
	 * RFQ详情
	 */
	public static final String GET_RFQ_DETAIL = BASE_HOST + HOST_NAME + "getRFQDetail";
	/***
	 * 报价详情
	 */
	public static final String GET_QUOTATION_DETAIL = BASE_HOST + HOST_NAME + "getQuotationDetail";
	/**
	 * 删除收藏
	 */
	public static final String DEL_FAVORITES_DATA = BASE_HOST + HOST_NAME + "deleteFavorite";
	/**
	 * 添加收藏
	 */
	public static final String ADD_FAVORITES_DATA = BASE_HOST + HOST_NAME + "addFavorite";
	/***
	 * 获得目录数据
	 */
	public static final String GET_CATEGORYLIST = BASE_HOST + HOST_NAME + "getCategoryList";
	/***
	 * 获得产品详情数据
	 */
	public static final String GET_PRODUCTDETAIL = BASE_HOST + HOST_NAME + "getProductDetail";
	/**
	 * 获得公司详情数据的接口
	 */
	public static final String GET_COMPANYDETAIL = BASE_HOST + HOST_NAME + "getCompanyDetail";
	/**
	 * 根据产品名称获取目录列表
	 */
	public static final String GET_CATEGORY_BY_PRODUCT_NAME = BASE_HOST + HOST_NAME + "getCategoryListByProductName";
	/**
	 * 根据目录名称获取目录列表
	 */
	public static final String GET_CATEGORY_BY_KEYWORD = BASE_HOST + HOST_NAME + "getCategoryListByKeyword";
	/**
	 * 文件上传
	 */
	public static final String UPLOAD_FILE = BASE_HOST + HOST_NAME + "uploadFile";
	/**
	 * 添加RFQ
	 */
	public static final String ADD_RFQ = BASE_HOST + HOST_NAME + "addRFQ";
	/**
	 * 编辑RFQ
	 */
	public static final String UPDATE_RFQ = BASE_HOST + HOST_NAME + "updateRFQ";
	/***
	 * 删除RFQ
	 */
	public static final String DEL_RFQ = BASE_HOST + HOST_NAME + "deleteRFQ";
	/**
	 * 检查邮件是否被注册
	 */
	public static final String CHECK_EMAIL = BASE_HOST + HOST_NAME + "checkEmail";
	/**
	 * 搜索联想
	 */
	public static final String SEARCH_SUGGEST = BASE_HOST + HOST_NAME + "suggestProduct";
	/**
	 * 关键词搜索产品(公司产品列表)
	 */
	public static final String SEARCH_PRODUCT = BASE_HOST + HOST_NAME + "getProductList";
	/**
	 * 关键词搜索产品(公司产品列表)
	 */
	public static final String SEARCH_MY_PRODUCT = BASE_HOST + HOST_NAME + "getMyProductList";
	/**
	 * 关键词搜索公司
	 */
	public static final String SEARCH_COMPANY = BASE_HOST + HOST_NAME + "getCompanyListByKeyword";
	/**
	 * 关键词搜索AR报告
	 */
	public static final String SEARCH_AUDITREPORT = BASE_HOST + HOST_NAME + "getCompanyListByAR";
	/**
	 * 查询分组产品列表
	 */
	public static final String SEARCH_GROUPED_PRODUCT = BASE_HOST + HOST_NAME + "getGroupedProductList";
	/**
	 * 发送邮件
	 */
	public static final String SEND_MAIL = BASE_HOST + HOST_NAME + "sendMail";
	/**
	 * 回复邮件
	 */
	public static final String REPLY_MAIL = BASE_HOST + HOST_NAME + "replyMail";
	/**
	 * 删除邮件
	 */
	public static final String DELETE_MAIL = BASE_HOST + HOST_NAME + "deleteMail";
	/**
	 * 获取收件箱邮件详情
	 */
	public static final String GET_INBOX_DETAIL = BASE_HOST + HOST_NAME + "getInboxDetail";
	/**
	 * 获取发件箱邮件详情
	 */
	public static final String GET_SENT_DETAIL = BASE_HOST + HOST_NAME + "getSentDetail";
	/**
	 * 获取邮件列表
	 */
	public static final String GET_MAIL_LIST = BASE_HOST + HOST_NAME + "getMailList";
	/**
	 * 获取AR报告详情
	 */
	public static final String GET_AR_DETAIL = BASE_HOST + HOST_NAME + "getARDetail";
	/**
	 * 检查是否收藏
	 */
	public static final String CHECK_FAVOURITE = BASE_HOST + HOST_NAME + "checkFavourite";
	/**
	 * 获取公司分组列表
	 */
	public static final String GET_COMPANY_GROUP = BASE_HOST + HOST_NAME + "getCompanyGroup";

	/**
	 * 发送找回密码请求
	 */
	public static final String REQUEST_PASSWORD = BASE_HOST + HOST_NAME + "getPassword";
	/**
	 * 发送Feedback请求
	 */
	public static final String SEND_FEEDBACK = BASE_HOST + HOST_NAME + "sendFeedBack";
	/**
	 * 获取专题列表
	 */
	public static final String GET_SPECIAL_LIST = BASE_HOST + HOST_NAME + "getSpecialList";
	/**
	 * 获取专题详情
	 */
	public static final String GET_SPECIAL_DETAIL = BASE_HOST + HOST_NAME + "getSpecialDetail";
	/**
	 * 获取EasySourcing页按目录数量统计RFQ和Quotation总数
	 */
	public static final String GET_EASY_SOURCING_CATEGORY = BASE_HOST + HOST_NAME + "getEasySourcingCategory";
	/**
	 * 联系我们网址
	 */
	public static final String CONTANCT_US = "http://m.made-in-china.com/contactusformobile.do?xcase=contact&cancelUrl=/";
	public static final String TWITTER_REQUEST_URL = "https://api.twitter.com/oauth/request_token";
	public static final String TWITTER_ACCESS_URL = "https://api.twitter.com/oauth/access_token";
	public static final String TWITTER_AUTHORIZE_URL = "https://api.twitter.com/oauth/authorize";
	public static final String OAUTH_CALLBACK_URL = "http://pmobser.made-in-china.com/mic/oauth_callback";
	public static final String AR_PAGE_URL = "file:///android_asset/ar_as/ar.html";
	public static final String AS_PAGE_URL = "file:///android_asset/ar_as/as.html";
	public static final String SE_PAGE_URL = "file:///android_asset/ar_as/service.html";
	public static final String TERM_CONDITION = "file:///android_asset/ar_as/terms.html";
}

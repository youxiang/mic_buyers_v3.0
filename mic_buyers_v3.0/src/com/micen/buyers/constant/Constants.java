package com.micen.buyers.constant;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;

import com.micen.buyers.activity.R;
import com.micen.buyers.activity.rfq.RFQDetailActivity;
import com.micen.buyers.module.category.Category;
import com.micen.buyers.module.user.TempUserInfo;

/**********************************************************
 * @文件名称：Constants.java
 * @文件作者：xiongjiangwei
 * @创建时间：2013-12-25 下午04:44:08
 * @文件描述：全局静态变量类
 * @修改历史：2013-12-25创建初始版本
 **********************************************************/
public class Constants
{
	public static Activity currentActivity = null;
	public static float density = 0;
	public static String enDateTemplate = "MMM dd, yyyy";
	public static Category selectedCategories = null;
	public static ArrayList<Activity> categoryActivityList = null;
	public static RFQDetailActivity reEditDetailActivity = null;
	public static ArrayList<String> selectedExportMarket = new ArrayList<String>();
	public static ArrayList<String> selectedBusinessType = new ArrayList<String>();
	public static String rfqEditTextDigits = "~!@#$%^&*()_+'{}|\":?<>[];\\,./=-0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ\r\n ";
	public static String numEditTextDigits = "~!@#$%^&*()_+'{}|\":?<>[];\\,./=-0123456789 ";
	public static Activity sentInquiryUnLoginActivity = null;
	public static String sharedPreDBName = "smallDatabase";
	// App标示，用于消息推送注册以及接收信息的验证
	public static String APP_KEY = "mic";
	public static String APP_FINISH_KEY = "com.micen.buyers.finish";
	/**
	 * 目录历史Intent传值的Key
	 */
	public static String CATEGORY_HISTORY_KEY = "categoryHistory";

	public static String APP_ID = "159249044991";
	// public static String CONSUMER_KEY = "VAG2yB8V2ezZLtHzPzeBgA";
	// public static String CONSUMER_SECRET =
	// "GPB8eBJQyoyZ0xxXVbjjo01RLCg200rv9k6QglbeWo";
	public static String CONSUMER_KEY = "SNM3rVcsvp5qzE9saFrwtw";
	public static String CONSUMER_SECRET = "Oagj65Ar8qRviOFWVSFtRAB1kk2ZicQtJbBUjfMtN8";
	// ShareData.CONSUMER_KEY, ShareData.CONSUMER_SECRET
	// LinkedIn
	public static final String LN_API_KEY = "dib40imce9ek";
	public static final String LN_API_SECRET = "xprSVNi8v7USZzTZ";

	// 注册用常用国家
	public static final String[] POPULAR_COUNTRY = new String[]
	{ "Australia", "Brazil", "Canada", "China", "India", "Malaysia", "Pakistan", "Philippines", "United Kingdom",
			"United States" };
	public static final String[] POPULAR_COUNTRY_VALUE = new String[]
	{ "Australia", "Brazil", "Canada", "China", "India", "Malaysia", "Pakistan", "Philippines", "United_Kingdom",
			"United_States" };
	public static final String[] POPULAR_COUNTRY_CODE = new String[]
	{ "13", "30", "38", "44", "100", "130", "164", "171", "231", "232" };
	public static final String[] POPULAR_TEL_COUNTRY_CODE = new String[]
	{ "61", "55", "1", "86", "91", "60", "92", "63", "44", "1" };
	public static final int[] POPULAR_COUNTRY_FLAG = new int[]
	{ R.drawable.country_australia, R.drawable.country_brazil, R.drawable.country_canada, R.drawable.country_china,
			R.drawable.country_india, R.drawable.country_malaysia, R.drawable.country_pakistan,
			R.drawable.country_philippines, R.drawable.country_united_kingdom, R.drawable.country_united_states_america };

	public static final String SE_VALUES = "se";
	public static final String AR_VALUES = "ar";
	public static final String AS_VALUES = "as";
	public static final String AR_AS_KEYWORD = "aras";
	public static final String INQ_C = "0";// 对公司发询盘
	public static final String INQ_P = "1";// 对产品发询盘
	public static final Long DAY_TIME = 86400000L;
	/**
	 * 登录完成后收藏的Code
	 */
	public static final int LOGIN_FAVOURITE = 0x000001;
	/**
	 * 邮件回复时，默认原文最大字符数
	 */
	public static final int REPLAY_MAIL_LENGTH = 4000;

	public static final int[] FIRST_CATEGORY = new int[]
	{ R.drawable.categories_0, R.drawable.categories_1, R.drawable.categories_2, R.drawable.categories_3,
			R.drawable.categories_4, R.drawable.categories_5, R.drawable.categories_6, R.drawable.categories_7,
			R.drawable.categories_8, R.drawable.categories_9, R.drawable.categories_10, R.drawable.categories_11,
			R.drawable.categories_12, R.drawable.categories_13, R.drawable.categories_14, R.drawable.categories_15,
			R.drawable.categories_16, R.drawable.categories_17, R.drawable.categories_18, R.drawable.categories_19,
			R.drawable.categories_20, R.drawable.categories_21, R.drawable.categories_22, R.drawable.categories_23,
			R.drawable.categories_24, R.drawable.categories_25, R.drawable.categories_26 };

	public static final String INITIAL_TIME = "2000-01-01";

	/**
	 * 多维搜索的关键词
	 */
	public static String KEY_WORD = "";

	/**
	 * 日期选择器最小时间常量（距离今天的时间差）
	 */
	public static int DAY_OFFSET = 6;
	public static int DEFAULT_DAY_OFFSET = 40;
	/**
	 * 快捷服务页产生的图片对象
	 */
	public static Bitmap easySourcingBitmap = null;

	/**
	 * 发送询盘请求码
	 */
	public static int SEND_INQUIRE_CODE = 0x00;

	/**
	 * 发送询盘标题长度 
	 */
	public static int INQUIRE_SUBJECT_LENGTH = 128;
	/**
	 * 发送询盘内容长度 
	 */
	public static int INQUIRE_CONTENT_LENGTH = 4000;

	/**
	 * 用户临时注册对象
	 */
	public static TempUserInfo userInfo;
	/**
	 * 临时用户是否是已注册用户
	 */
	public static boolean isRegister;

	public static final int DELETE_RECENT_KEYWORDS = 0x02;
	public static final int GET_SUGGEST_DATA = 0x03;
	/**
	 * 默认最大历史目录值
	 */
	public static final int MAX_RECENT_CATEGORY_NUM = 6;
	/**
	 *目录级数 
	 */
	public static final String SECOND_CATEGORY = "2";
	public static final String THIRD_CATEGORY = "3";
	public static final int RRFINE_PRODUCT = 0x04;
	public static final int RRFINE_COMPANY = 0x05;

	/**
	 * Flag for develop mode
	 */
	public static final boolean DEV_MODE = false;

}

package com.focustech.common.mob;
import java.io.Serializable;
import java.util.HashMap;

import android.content.Context;
/***
 * 定义一个最基本的配置信息类
 * 
 * @author chenkangpeng
 * 
 */
public class BaseInfoParams extends BaseNetParams implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID= 1L;
	/***
	 * 设备的串号
	 */
	private String userPkId;
	/***
	 * 产品名称,默认为空
	 */
	private String productName;
	/***
	 * 平台名称,默认为android 平台
	 */
	private String platformName;
	/***
	 * 版本号,默认为1.0
	 */
	private String productVersion;
	/**
	 * 地理位置
	 */
	private String devicePosition = "";
	/**
	 * 产品平台通道,默认为self
	 */
	private String productChannel= "self";
	/***
	 * 账号
	 */
	private String appAccount;
	/***
	 * 是否接受[0:不接受， 1：接受]
	 */
	private String isReceive ="1";
	public final static String CHANNEL_SELF= "self";
	public final static String PLAT_ANDROID= "android";
	public BaseInfoParams(
			String userPkId,
			String productName,
			String platformName,
			String productVersion,
			String devicePosition,
			String productChannel)
	{
		super();
		this.userPkId= userPkId;
		this.productName= productName;
		this.platformName= platformName;
		this.productVersion= productVersion;
		this.devicePosition= devicePosition;
		this.productChannel= productChannel;
	}
	public BaseInfoParams(Context context, String productName, String devicePosition, String productChannel)
	{
		super();
		this.productName= productName;
		this.devicePosition= devicePosition;
		this.productChannel= productChannel;
		this.userPkId= MobileTools.getIMEI(context);
		this.platformName= "android";
		this.productVersion= SystemTools.getProductVersion(context);
	}
	
	public BaseInfoParams(Context context,String productName,String productChannel) {
		super();
		this.productName= productName;
		this.productChannel= productChannel;
		this.userPkId= MobileTools.getIMEI(context);
		this.platformName= "android";
		this.productVersion= SystemTools.getProductVersion(context);
	}
	public String getProductChannel()
	{
		return productChannel;
	}
	public void setProductChannel(String productChannel)
	{
		this.productChannel= productChannel;
	}
	public String getUserPkId()
	{
		return userPkId;
	}
	public void setUserPkId(String userPkId)
	{
		this.userPkId= userPkId;
	}
	public String getProductName()
	{
		return productName;
	}
	public void setProductName(String productName)
	{
		this.productName= productName;
	}
	public String getPlatformName()
	{
		return platformName;
	}
	public void setPlatformName(String platformName)
	{
		this.platformName= platformName;
	}
	public String getProductVersion()
	{
		return productVersion;
	}
	public void setProductVersion(String productVersion)
	{
		this.productVersion= productVersion;
	}
	public String getDevicePosition()
	{
		return devicePosition;
	}
	public void setDevicePosition(String devicePosition)
	{
		this.devicePosition= devicePosition;
	}
	public String getAppAccount()
	{
		return appAccount;
	}
	public void setAppAccount(String appAccount)
	{
		this.appAccount= appAccount;
	}
	public String getIsReceive()
	{
		return isReceive;
	}
	public void setIsReceive(String isReceive)
	{
		this.isReceive= isReceive;
	}
	public HashMap<String, String> toMap(){
		map =  new HashMap<String, String>();
		putEntry("userPkId", getUserPkId());
		putEntry("productName", getProductName());
		putEntry("platformName", getPlatformName());
		putEntry("productChannel", getProductChannel());
		putEntry("productVersion", getProductVersion());
		putEntry("devicePosition", getDevicePosition());
		putEntry("appAccount", getAppAccount());
		putEntry("isReceive", getIsReceive());
		return map;
	}
	private void putEntry(String key,String value){
		if(value!=null){
			map.put(key, value);
		}
	}
	HashMap<String, String> map;
}

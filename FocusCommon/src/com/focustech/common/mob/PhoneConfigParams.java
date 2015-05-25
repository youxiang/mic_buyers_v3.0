package com.focustech.common.mob;
import java.io.Serializable;
import java.util.HashMap;
/**
 * 定义一个描述手机配置的类
 * 
 * @author chenkangpeng
 * 
 */
public class PhoneConfigParams implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID= 1L;
	/**
	 * 用户唯一表示
	 */
	private String userPkId;
	/**
	 * 设备的唯一哈稀值
	 */
	private String deviceUniqueid= "";
	/***
	 * 网络状态
	 */
	private String netState= "1";
	/**
	 * 设备制作商的名称
	 */
	private String deviceUniqueidName= "";
	/***
	 * 用户所属运营商的名称
	 */
	private String belongedBusiness= "";
	/***
	 * 设备上运行的固件版本
	 */
	private String deviceFirmwareVersion= "";
	/***
	 * 设备上运行的硬件版本
	 */
	private String deviceHardwareVersion= "";
	/**
	 * 设备名称
	 */
	private String deiviceName= "";
	/***
	 * 设备 的地理位置
	 */
	private String devicePosition= "";
	/***
	 * 设备的屏幕分辨力
	 */
	private String deviceShowratio= "";
	/***
	 * 设备的物理RAM大小
	 */
	private String deviceTotalMemory= "";
	/**
	 * 用户是否已经部署设备的物理硬件键盘，就是外接键盘
	 */
	private String isKeyboardDeployed= "";
	/**
	 * 是否包含物理硬件键盘
	 */
	private String isKeyboardPersent= "";
	/**
	 * 是否已经拥有前置摄相头
	 */
	private String isownCamera= "";
	/***
	 * 是否已经外接电源
	 */
	private String powerSource= "";
	/***
	 * 手机电话号码
	 */
	private String phoneNumber= "";
	/**
	 * 系统版本
	 */
	private String systemVersion= "";
	
	public String getDeviceUniqueid()
	{
		return deviceUniqueid;
	}
	public void setDeviceUniqueid(String deviceUniqueid)
	{
		this.deviceUniqueid= deviceUniqueid;
	}
	public String getNetState()
	{
		return netState;
	}
	public void setNetState(String netState)
	{
		this.netState= netState;
	}
	public String getDeviceUniqueidName()
	{
		return deviceUniqueidName;
	}
	public void setDeviceUniqueidName(String deviceUniqueidName)
	{
		this.deviceUniqueidName= deviceUniqueidName;
	}
	public String getBelongedBusiness()
	{
		return belongedBusiness;
	}
	public void setBelongedBusiness(String belongedBusiness)
	{
		this.belongedBusiness= belongedBusiness;
	}
	public String getDeviceFirmwareVersion()
	{
		return deviceFirmwareVersion;
	}
	public void setDeviceFirmwareVersion(String deviceFirmwareVersion)
	{
		this.deviceFirmwareVersion= deviceFirmwareVersion;
	}
	public String getDeviceHardwareVersion()
	{
		return deviceHardwareVersion;
	}
	public void setDeviceHardwareVersion(String deviceHardwareVersion)
	{
		this.deviceHardwareVersion= deviceHardwareVersion;
	}
	public String getDeiviceName()
	{
		return deiviceName;
	}
	public void setDeiviceName(String deiviceName)
	{
		this.deiviceName= deiviceName;
	}
	public String getDevicePosition()
	{
		return devicePosition;
	}
	public void setDevicePosition(String devicePosition)
	{
		this.devicePosition= devicePosition;
	}
	public String getDeviceShowratio()
	{
		return deviceShowratio;
	}
	public void setDeviceShowratio(String deviceShowratio)
	{
		this.deviceShowratio= deviceShowratio;
	}
	public String getDeviceTotalMemory()
	{
		return deviceTotalMemory;
	}
	public void setDeviceTotalMemory(String deviceTotalMemory)
	{
		this.deviceTotalMemory= deviceTotalMemory;
	}
	public String getIsKeyboardDeployed()
	{
		return isKeyboardDeployed;
	}
	public void setIsKeyboardDeployed(String isKeyboardDeployed)
	{
		this.isKeyboardDeployed= isKeyboardDeployed;
	}
	public String getIsKeyboardPersent()
	{
		return isKeyboardPersent;
	}
	public void setIsKeyboardPersent(String isKeyboardPersent)
	{
		this.isKeyboardPersent= isKeyboardPersent;
	}
	public String getIsownCamera()
	{
		return isownCamera;
	}
	public void setIsownCamera(String isownCamera)
	{
		this.isownCamera= isownCamera;
	}
	public String getPowerSource()
	{
		return powerSource;
	}
	public void setPowerSource(String powerSource)
	{
		this.powerSource= powerSource;
	}
	public String getUserPkId()
	{
		return userPkId;
	}
	public void setUserPkId(String userPkId)
	{
		this.userPkId= userPkId;
	}
	public String getPhoneNumber()
	{
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber)
	{
		this.phoneNumber= phoneNumber;
	}
	public String getSystemVersion()
	{
		return systemVersion;
	}
	public void setSystemVersion(String systemVersion)
	{
		this.systemVersion= systemVersion;
	}
	public HashMap<String, String> toMap(){
		map =  new HashMap<String, String>();
		putEntry("deviceUniqueid", getDeviceUniqueid());
		putEntry("deviceHardWareVersion", getDeviceHardwareVersion());
		putEntry("deviceName", getDeiviceName());
		putEntry("deviceShowratio", getDeviceShowratio());
		putEntry("deviceTotalMemory", getDeviceTotalMemory());
		putEntry("iskeyboardDeployed", getIsKeyboardDeployed());
		putEntry("belongedBusiness", getBelongedBusiness());
		putEntry("deviceFirmwareVersion", getDeviceFirmwareVersion());
		putEntry("netState", getNetState());
		putEntry("powerSource", getPowerSource());
		putEntry("isownCamera", getIsownCamera());
		putEntry("phoneNumber", getPhoneNumber());
		return map;
	}
	private void putEntry(String key,String value){
		if(value!=null){
			map.put(key, value);
		}
	}
	HashMap<String, String> map;
}

package com.focustech.common.mob;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;
/**
 * 手机 的一个工具类
 * 
 * @author chenkangpeng
 * 
 */
public class MobileTools
{
	/***
	 * 获得手机设备的串号
	 * 
	 * @param context
	 * @return
	 */
	public static String getIMEI(Context context)
	{
		try
		{
			TelephonyManager tm= (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			String imeiCode= tm.getDeviceId();
			return imeiCode;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "";
		}
	}
	/**
	 * 获得SIM卡的唯一识别号
	 * 
	 * @param context
	 * @return
	 */
	public static String getIMSI(Context context)
	{
		try
		{
			TelephonyManager tm= (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			String imsiCode= tm.getSubscriberId();
			return imsiCode;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "";
		}
	}
	/***
	 * 获得手机的电话号码
	 * 
	 * @param context
	 * @return
	 */
	public static String getPhoneNumber(Context context)
	{
		try
		{
			TelephonyManager tm= (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			return tm.getLine1Number();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "";
		}
	}
	/**
	 * 获得手机配置参数 需要的权限：READ_PHONE_STATE
	 * 
	 * @param context
	 * @return
	 */
	public static PhoneConfigParams getMobileInfo(Context context)
	{
		try
		{
			PhoneConfigParams mobileInfo= new PhoneConfigParams();
			TelephonyManager tm= (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			//获得设备的唯一ID
			mobileInfo.setDeviceUniqueid(tm.getDeviceId());
			mobileInfo.setUserPkId(MobileTools.getIMEI(context));
			//获得运营商名称
			mobileInfo.setBelongedBusiness(tm.getSimOperatorName());
			//获得屏幕分辨率
			DisplayMetrics dm= new DisplayMetrics();
			WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			wm.getDefaultDisplay().getMetrics(dm);
			mobileInfo.setDeviceShowratio(dm.widthPixels + "*" + dm.heightPixels);
			String[] versionInfo= getVersion();
			//获得手机RAM大小
			mobileInfo.setDeviceTotalMemory(getTotalMemory());
			if (versionInfo != null)
			{
				//获得设备上运行的硬件版本
				mobileInfo.setDeviceHardwareVersion(versionInfo[0]);
				//获得设备上运行的固件版本
				mobileInfo.setDeviceFirmwareVersion(versionInfo[1]);
				//设置设备名称
				mobileInfo.setDeiviceName(versionInfo[2]);
			}
			mobileInfo.setPhoneNumber(MobileTools.getPhoneNumber(context));
			return mobileInfo;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 获得手机内存大小的信息RAM大小
	 */
	@SuppressWarnings("resource")
	public static String getTotalMemory()
	{
		ArrayList<String> infos= new ArrayList<String>();
		String str1= "/proc/meminfo";
		String str2= "";
		try
		{
			FileReader fr= new FileReader(str1);
			BufferedReader localBufferedReader= new BufferedReader(fr, 8192);
			while ((str2= localBufferedReader.readLine()) != null)
			{
				String[] strs= str2.split(":");
				infos.add(strs[1].trim());
			}
			return (infos.size() > 0 ? infos.get(0).trim() : "");
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return "";
		}
	}
	/**
	 * 获得手机版本信息[0] 表示内核版本 ,[1] 表示android 版本,[2] 型号 ,[3] 版本号
	 * 
	 * @return
	 */
	public static String[] getVersion()
	{
		String[] version=
		{"null", "null", "null", "null"};
		String str1= "/proc/version";
		String str2;
		String[] arrayOfString;
		try
		{
			FileReader localFileReader= new FileReader(str1);
			BufferedReader localBufferedReader= new BufferedReader(localFileReader, 8192);
			str2= localBufferedReader.readLine();
			arrayOfString= str2.split("\\s+");
			version[0]= arrayOfString[2];//KernelVersion   
			localBufferedReader.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
		version[1]= Build.VERSION.RELEASE;// firmware version   
		version[2]= Build.MODEL;//model   
		version[3]= Build.DISPLAY;//system version   
		return version;
	}
	/***
	 * 获得设备的唯一哈希值
	 * 
	 * @param context
	 * @return
	 */
	public String getDeviceUniqueid(Context context)
	{
		final TelephonyManager tm= (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		final String tmDevice, tmSerial, androidId;
		tmDevice= "" + tm.getDeviceId();
		tmSerial= "" + tm.getSimSerialNumber();
		androidId= ""
				+ android.provider.Settings.Secure.getString(
						context.getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);
		UUID deviceUuid= new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
		String uniqueId= deviceUuid.toString();
		return uniqueId;
	}
}

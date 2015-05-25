package com.focustech.common.broadcast.xmpp;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;

/**********************************************************
 * @文件名称：PackageManager.java
 * @文件作者：xiongjiangwei
 * @创建时间：2013-11-25 下午02:03:33
 * @文件描述：安装包管理器
 * @修改历史：2013-11-25创建初始版本
 **********************************************************/
public class PackageManager
{
	private static PackageManager manager = null;
	private Context mContext;

	private PackageManager(Context context)
	{
		this.mContext = context;
	}

	public static PackageManager getInstance(Context context)
	{
		if (manager == null)
		{
			manager = new PackageManager(context);
		}
		return manager;
	}

	/**
	 * 获得所有用户安装的应用信息
	 * @return
	 */
	public ArrayList<AppInfo> getAllUserPackage()
	{
		ArrayList<AppInfo> appList = new ArrayList<AppInfo>(); // 用来存储获取的应用信息数据
		List<PackageInfo> packages = mContext.getPackageManager().getInstalledPackages(0);
		for (int i = 0; i < packages.size(); i++)
		{
			PackageInfo packageInfo = packages.get(i);
			AppInfo tmpInfo = new AppInfo();
			tmpInfo.appName = packageInfo.applicationInfo.loadLabel(mContext.getPackageManager()).toString();
			tmpInfo.packageName = packageInfo.packageName;
			tmpInfo.versionName = packageInfo.versionName;
			tmpInfo.versionCode = packageInfo.versionCode;
			tmpInfo.appIcon = packageInfo.applicationInfo.loadIcon(mContext.getPackageManager());
			// Only display the non-system app info
			if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
			{
				appList.add(tmpInfo);// 如果非系统应用，则添加至appList
			}
		}
		return appList;
	}

	/**
	 * 根据应用包名获取该应用的相关信息
	 * @param keyword
	 * @return
	 */
	public AppInfo getAppInfoByKeyword(String keyword)
	{
		ArrayList<AppInfo> appList = getAllUserPackage();
		AppInfo appInfo = null;
		for (int i = 0; i < appList.size(); i++)
		{
			if (appList.get(i).packageName.equals(keyword))
			{
				appInfo = appList.get(i);
				break;
			}
		}
		return appInfo;
	}
}

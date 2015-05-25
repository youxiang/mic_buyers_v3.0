package com.micen.buyers.manager;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import android.app.Activity;

/**********************************************************
 * @文件名称：ActivityManager.java
 * @文件作者：xiongjiangwei
 * @创建时间：2014年11月6日 上午11:38:23
 * @文件描述：Activity管理类，确保能完全退出
 * @修改历史：2014年11月6日创建初始版本
 **********************************************************/
public class ActivityManager
{
	private static ActivityManager manager = null;
	private static HashMap<String, SoftReference<Activity>> activityMap;
	static
	{
		manager = new ActivityManager();
		activityMap = new HashMap<String, SoftReference<Activity>>();
	}

	private ActivityManager()
	{

	}

	public static ActivityManager getInstance()
	{
		return manager;
	}

	public void put(Activity act)
	{
		activityMap.put(act.toString(), new SoftReference<Activity>(act));
	}

	public void remove(Activity act)
	{
		activityMap.remove(act.toString());
	}

	public void finishAllActivity()
	{
		Set<String> set = activityMap.keySet();
		Iterator<String> iter = set.iterator();
		while (iter.hasNext())
		{
			String actName = iter.next();
			Activity currentAct = activityMap.get(actName).get();
			if (currentAct != null)
			{
				currentAct.finish();
				currentAct = null;
			}
		}
		activityMap.clear();
		activityMap = null;
	}
}

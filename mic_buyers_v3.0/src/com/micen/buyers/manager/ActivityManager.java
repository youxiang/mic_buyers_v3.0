package com.micen.buyers.manager;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import android.app.Activity;

/**********************************************************
 * @�ļ����ƣ�ActivityManager.java
 * @�ļ����ߣ�xiongjiangwei
 * @����ʱ�䣺2014��11��6�� ����11:38:23
 * @�ļ�������Activity�����࣬ȷ������ȫ�˳�
 * @�޸���ʷ��2014��11��6�մ�����ʼ�汾
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

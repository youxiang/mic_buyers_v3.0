package com.micen.buyers.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.micen.buyers.constant.Constants;
import com.micen.buyers.util.BuyerApplication;

/**********************************************************
 * @文件名称：SharedPreferenceManager.java
 * @文件作者：xiongjiangwei
 * @创建时间：2012-10-22 上午09:37:57
 * @文件描述：存取简单的数据
 * @修改历史：2012-10-22创建初始版本
 **********************************************************/
public class SharedPreferenceManager
{
	private static SharedPreferences sp = null;

	private static SharedPreferenceManager spManager = null;

	private static Editor editor = null;

	private SharedPreferenceManager()
	{
		sp = BuyerApplication.getInstance().getSharedPreferences(Constants.sharedPreDBName, Context.MODE_PRIVATE);
		editor = sp.edit();
	}

	public static SharedPreferenceManager getInstance()
	{
		if (spManager == null || sp == null || editor == null)
		{
			spManager = new SharedPreferenceManager();
		}
		return spManager;
	}

	public void putInt(String key, int value)
	{
		editor.putInt(key, value);
		editor.commit();
	}

	public int getInt(String key, int defaultValue)
	{
		return sp.getInt(key, defaultValue);
	}

	public void putString(String key, String value)
	{
		editor.putString(key, value);
		editor.commit();
	}

	public String getString(String key, String defaultValue)
	{
		return sp.getString(key, defaultValue);
	}

	public void putFloat(String key, float value)
	{
		editor.putFloat(key, value);
		editor.commit();
	}

	public boolean isKeyExist(String key)
	{
		return sp.contains(key);
	}

	public float getFloat(String key, float defaultValue)
	{
		return sp.getFloat(key, defaultValue);
	}

	public void putBoolean(String key, boolean value)
	{
		editor.putBoolean(key, value);
		editor.commit();
	}

	public boolean getBoolean(String key, boolean defaultValue)
	{
		return sp.getBoolean(key, defaultValue);
	}

	public void remove(String key)
	{
		editor.remove(key);
		editor.commit();
	}

}
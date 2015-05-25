package com.micen.buyers.manager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Build.VERSION;
import android.view.inputmethod.InputMethodManager;

import com.focustech.common.mob.BaseInfoParams;
import com.focustech.common.mob.analysis.FocusAnalyticsTracker;
import com.focustech.common.mob.register.Register;
import com.focustech.common.mob.register.Register.RegisterCommListener;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.micen.buyers.constant.Constants;
import com.micen.buyers.util.BuyerApplication;
import com.micen.buyers.util.ImageUtil;

/**********************************************************
 * @文件名称：SysManager.java
 * @文件作者：xiongjiangwei
 * @创建时间：2014-1-6 下午02:25:49
 * @文件描述：系统管理类
 * @修改历史：2014-1-6创建初始版本
 **********************************************************/
public class SysManager
{
	private static SysManager instance;
	private static Context mContext;

	private SysManager()
	{
	}

	public static SysManager getInstance()
	{
		if (instance == null)
		{
			instance = new SysManager();
		}
		return instance;
	}

	/**
	 * 退出系统
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	public static void exitSystem(Context context)
	{
		ImageUtil.getImageLoader().clearMemoryCache();
		Intent intent = new Intent();
		intent.setAction(Constants.APP_FINISH_KEY); // 说明动作
		if (VERSION.SDK_INT >= 12)
		{
			intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
		}
		if (context != null)
		{
			context.sendBroadcast(intent);// 该函数用于发送广播
		}
		else
		{
			BuyerApplication.getInstance().sendBroadcast(intent);
		}
	}

	/***
	 * 让软键盘消失
	 */
	public void dismissInputKey(Activity context)
	{
		try
		{
			((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
					context.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void showInputKey(Activity context)
	{
		try
		{
			((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(
					context.getCurrentFocus(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 收集行为
	 * @param context 上下文对象
	 * @param actionType 行为类别
	 * @param actionDescribe 行为描述
	 */
	public static void analysis(Context context, String actionType, String actionDescribe)
	{
		EasyTracker tracker = EasyTracker.getInstance(context);
		tracker.send(MapBuilder.createEvent(null, actionType, actionDescribe, null).build());

		// 新增加mobservice的事件统计
		FocusAnalyticsTracker mbtracker = FocusAnalyticsTracker.getInstances();
		mbtracker.trackEvent(context, actionDescribe);
	}

	/**
	 * 收集行为
	 * @param actionTypeID String's resource id
	 * @param actionDescribeID String's resouce id
	 */
	public static void analysis(int actionTypeID, int actionDescribeID)
	{
		if (mContext == null)
		{
			mContext = BuyerApplication.getAppContext();
		}
		String actionType = mContext.getString(actionTypeID);
		String actionDescribe = mContext.getString(actionDescribeID);
		
		EasyTracker tracker = EasyTracker.getInstance(mContext);
		tracker.send(MapBuilder.createEvent(null, actionType, actionDescribe, null).build());
		// 新增加mobservice的事件统计
		FocusAnalyticsTracker mbtracker = FocusAnalyticsTracker.getInstances();
		mbtracker.trackEvent(mContext, actionDescribe);
	}

	/**
	 * 推送功能绑定账户
	 */
	public static void boundAccount(String idCard)
	{
		BaseInfoParams p = new BaseInfoParams(BuyerApplication.getInstance(), Constants.APP_KEY, "self");
		p.setAppAccount(idCard);
		new Register().boundAccountOrNot(p, new RegisterCommListener()
		{
			@Override
			public void result(boolean arg0)
			{

			}
		});
	}
}

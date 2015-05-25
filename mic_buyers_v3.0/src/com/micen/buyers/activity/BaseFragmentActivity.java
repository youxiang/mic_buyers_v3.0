package com.micen.buyers.activity;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.micen.buyers.constant.Constants;
import com.micen.buyers.manager.ActivityManager;
import com.micen.buyers.reveiver.ActivityFinishReceiver;
import com.micen.buyers.util.Util;

/**********************************************************
 * @文件名称：BaseFragmentActivity.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年3月10日 下午5:10:29
 * @文件描述：所有FragmentActivity的父类
 * @修改历史：2015年3月10日创建初始版本
 **********************************************************/
@EActivity
public class BaseFragmentActivity extends FragmentActivity implements OnClickListener
{
	@ViewById(R.id.common_title_back_button)
	protected ImageView titleLeftButton;
	@ViewById(R.id.common_title_name)
	protected TextView titleText;
	@ViewById(R.id.common_title_right_button1)
	protected ImageView titleRightButton1;
	@ViewById(R.id.common_title_right_button2)
	protected ImageView titleRightButton2;
	@ViewById(R.id.common_title_right_button3)
	protected ImageView titleRightButton3;

	protected ActivityFinishReceiver finishReceiver = new ActivityFinishReceiver();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Util.startDeveloperMode();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Constants.currentActivity = this;
		ActivityManager.getInstance().put(this);
		registerFinishReceiver();
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		//EasyTracker.getInstance(this).activityStart(this);
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		//EasyTracker.getInstance(this).activityStop(this);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		ActivityManager.getInstance().remove(this);
		unRegisterFinishReceiver();
	}

	protected void registerFinishReceiver()
	{
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.APP_FINISH_KEY);
		registerReceiver(finishReceiver, filter); // 注册
	}

	protected void unRegisterFinishReceiver()
	{
		unregisterReceiver(finishReceiver);
	}

	@Override
	public void onClick(View v)
	{

	}

}

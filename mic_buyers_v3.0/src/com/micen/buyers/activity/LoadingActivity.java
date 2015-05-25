package com.micen.buyers.activity;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.focustech.common.mob.DisposeDataListener;
import com.focustech.common.util.ToastUtil;
import com.focustech.common.util.Utils;
import com.micen.buyers.activity.account.setting.GuideActivity_;
import com.micen.buyers.activity.home.HomeActivity_;
import com.micen.buyers.constant.Constants;
import com.micen.buyers.db.SharedPreferenceManager;
import com.micen.buyers.http.RequestCenter;
import com.micen.buyers.manager.SysManager;
import com.micen.buyers.module.user.User;
import com.micen.buyers.util.BuyerApplication;
import com.micen.buyers.util.Util;

/**********************************************************
 * @文件名称：LoadingActivity.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年3月11日 上午10:35:54
 * @文件描述：加载页
 * @修改历史：2015年3月11日创建初始版本
 **********************************************************/
@EActivity
public class LoadingActivity extends BaseActivity
{
	@ViewById(R.id.auto_logon_layout)
	protected LinearLayout autoLogonLayout;
	@ViewById(R.id.iv_auto_logon)
	protected ImageView ivAutoLogon;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
		Constants.currentActivity = this;
		Constants.density = Util.getWindowDensity();
		initVersionParams();
		if (SharedPreferenceManager.getInstance().getBoolean("isAutoLogon", false))
		{
			autoLogonLayout.setVisibility(View.VISIBLE);
			ivAutoLogon.setBackgroundResource(R.drawable.bg_auto_logon);
			((AnimationDrawable) ivAutoLogon.getBackground()).start();
			RequestCenter.userLogin(SharedPreferenceManager.getInstance().getString("lastLoginName", ""),
					SharedPreferenceManager.getInstance().getString("lastLoginPassword", ""), loginListener);
			handler.sendEmptyMessageDelayed(1, 15000);
		}
		else
		{
			handler.sendEmptyMessageDelayed(0, 2000);
		}
	}

	private void initVersionParams()
	{
		String latestVersion = SharedPreferenceManager.getInstance().getString("latestVersion", "");
		if ("".equals(latestVersion) || latestVersion.equals(Utils.getAppVersionName(this)))
		{
			SharedPreferenceManager.getInstance().remove("isClickSetting");
			SharedPreferenceManager.getInstance().remove("isHaveNewVersion");
		}
	}

	private Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case 0:
				SharedPreferences sp = getSharedPreferences(Constants.sharedPreDBName, Context.MODE_PRIVATE);
				if (sp.getBoolean("isFirst", true))
				{
					startActivity(new Intent(LoadingActivity.this, GuideActivity_.class));
				}
				else
				{
					startActivity(new Intent(LoadingActivity.this, HomeActivity_.class));
				}
				finish();
				break;
			case 1:
				if (isFinishing())
					return;
				ToastUtil.toast(LoadingActivity.this, R.string.request_error2);
				sendEmptyMessage(0);
				break;
			}
		};
	};

	private DisposeDataListener loginListener = new DisposeDataListener()
	{
		@Override
		public void onSuccess(Object result)
		{
			if (!isFinishing())
			{
				BuyerApplication.getInstance().setUser((User) result);
				SysManager.boundAccount(((User) result).sessionid);
				ToastUtil.toast(LoadingActivity.this, R.string.Login_Successful);
				handler.sendEmptyMessage(0);
			}
		}

		@Override
		public void onFailure(Object failedReason)
		{
			if (!isFinishing())
			{
				ToastUtil.toast(LoadingActivity.this, failedReason);
				handler.sendEmptyMessage(0);
			}
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			SysManager.exitSystem(this);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}

package com.micen.buyers.activity.account.setting;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.focustech.common.http.FocusClient;
import com.focustech.common.util.ToastUtil;
import com.micen.buyers.activity.BaseActivity;
import com.micen.buyers.activity.R;
import com.micen.buyers.db.SharedPreferenceManager;
import com.micen.buyers.manager.CheckUpdateManager;
import com.micen.buyers.manager.SysManager;
import com.micen.buyers.util.BuyerApplication;

/**********************************************************
 * @文件名称：SettingActivity.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年3月30日 下午2:09:35
 * @文件描述：设置页
 * @修改历史：2015年3月30日创建初始版本
 **********************************************************/
@EActivity
public class SettingActivity extends BaseActivity
{
	@ViewById(R.id.cb_setting_safe_image)
	protected CheckBox cbSafeImage;
	@ViewById(R.id.rl_setting_safe_image)
	protected RelativeLayout btnSafeImage;
	@ViewById(R.id.tv_setting_safe_image_status)
	protected TextView tvSafeImageStatus;
	@ViewById(R.id.rl_setting_feature)
	protected RelativeLayout btnFeature;
	@ViewById(R.id.rl_setting_aboutus)
	protected RelativeLayout btnAboutus;
	@ViewById(R.id.rl_setting_terms_condition)
	protected RelativeLayout btnTermsCondition;
	@ViewById(R.id.rl_setting_contactus)
	protected RelativeLayout btnFeedback;
	@ViewById(R.id.rl_setting_update)
	protected RelativeLayout btnUpdate;
	@ViewById(R.id.ic_update_new)
	protected ImageView ivUpdateNew;
	@ViewById(R.id.tv_setting_signout)
	protected TextView btnSignOut;
	@ViewById(R.id.ll_setting_signout_layout)
	protected LinearLayout signOutLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		// 事件统计$10031 设置页 页面事件
		SysManager.analysis(R.string.a_type_page, R.string.p10031);

		showSignOut();
	}

	@AfterViews
	protected void initView()
	{
		titleLeftButton.setImageResource(R.drawable.ic_title_back);
		titleText.setText(R.string.mic_home_meun_text6);
		titleRightButton1.setVisibility(View.GONE);
		titleRightButton2.setVisibility(View.GONE);
		titleRightButton3.setVisibility(View.GONE);
		if (SharedPreferenceManager.getInstance().getBoolean("isHaveNewVersion", false))
		{
			ivUpdateNew.setVisibility(View.VISIBLE);
		}
		btnSignOut.setOnClickListener(this);
		btnFeature.setOnClickListener(this);
		btnFeedback.setOnClickListener(this);
		btnUpdate.setOnClickListener(this);
		titleLeftButton.setOnClickListener(this);
		btnAboutus.setOnClickListener(this);
		btnTermsCondition.setOnClickListener(this);
		btnSafeImage.setOnClickListener(this);

		initViewData();
	}

	private void initViewData()
	{
		if (SharedPreferenceManager.getInstance().getBoolean("isDisplaySafeImage", false))
		{
			cbSafeImage.setChecked(false);
			tvSafeImageStatus.setText(getString(R.string.setting_safe_image_display));
		}
		else
		{
			cbSafeImage.setChecked(true);
			tvSafeImageStatus.setText(getString(R.string.setting_safe_image_display_off));
		}
	}

	@Override
	public void onClick(View view)
	{
		super.onClick(view);
		switch (view.getId())
		{
		case R.id.common_title_back_button:
			finish();
			break;
		case R.id.rl_setting_aboutus:
			startActivity(new Intent(this, AboutUsActivity_.class));
			break;
		case R.id.rl_setting_terms_condition:
			startActivity(new Intent(this, TermsConditionActivity_.class));
			break;
		case R.id.tv_setting_signout:

			// 事件统计 127 登出（设置） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c127);

			SharedPreferenceManager.getInstance().putBoolean("isAutoLogon", false);
			FocusClient.removeCookieStore();
			BuyerApplication.getInstance().setUser(null);
			SysManager.boundAccount("");
			ToastUtil.toast(this, R.string.Log_Out_Successful);
			signOutLayout.setVisibility(View.GONE);
			break;
		case R.id.rl_setting_contactus:

			// 事件统计125 点击反馈（设置） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c125);

			startActivity(new Intent(this, FeedBackActivity_.class));
			break;
		case R.id.rl_setting_update:

			// 事件统计 126 检查更新（设置） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c126);

			CheckUpdateManager.checkUpdate(this, true);
			break;
		case R.id.rl_setting_feature:

			// 事件统计 124 查看新手指导（设置） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c123);

			// 进入新手指引页．．．
			Intent guideHelpIntent = new Intent(this, GuideActivity_.class);
			guideHelpIntent.putExtra("guideTarget", "setting");
			this.startActivity(guideHelpIntent);
			break;
		case R.id.rl_setting_safe_image:

			// 事件统计 123 点击安全图片按钮（设置） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c123_1);

			if (cbSafeImage.isChecked())
			{
				cbSafeImage.setChecked(false);
				tvSafeImageStatus.setText(getString(R.string.setting_safe_image_display));
				SharedPreferenceManager.getInstance().putBoolean("isDisplaySafeImage", true);
			}
			else
			{
				cbSafeImage.setChecked(true);
				tvSafeImageStatus.setText(getString(R.string.setting_safe_image_display_off));
				SharedPreferenceManager.getInstance().putBoolean("isDisplaySafeImage", false);
			}
			break;
		}

	}

	private void showSignOut()
	{
		if (BuyerApplication.getInstance().getUser() != null)
		{
			signOutLayout.setVisibility(View.VISIBLE);
		}
	}

}
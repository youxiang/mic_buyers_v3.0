package com.micen.buyers.activity.message;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.focustech.common.mob.DisposeDataListener;
import com.focustech.common.util.ToastUtil;
import com.focustech.common.util.Utils;
import com.micen.buyers.activity.BaseActivity;
import com.micen.buyers.activity.R;
import com.micen.buyers.http.RequestCenter;
import com.micen.buyers.manager.IntentManager;
import com.micen.buyers.manager.SysManager;
import com.micen.buyers.module.user.User;
import com.micen.buyers.view.SearchListProgressBar;

/**********************************************************
 * @文件名称：MailPersonalActivity.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年4月9日 上午9:26:08
 * @文件描述：收发件人详情页
 * @修改历史：2015年4月9日创建初始版本
 **********************************************************/
@EActivity
public class MailPersonalActivity extends BaseActivity implements OnClickListener
{
	@ViewById(R.id.progressbar_layout)
	protected SearchListProgressBar progressBar;
	@ViewById(R.id.mail_personal_scroll)
	protected ScrollView personalScroll;
	@ViewById(R.id.rl_personal_title)
	protected RelativeLayout personalTitle;
	@ViewById(R.id.tv_personal_name)
	protected TextView personalName;
	@ViewById(R.id.tv_personal_company)
	protected TextView personalCompany;
	@ViewById(R.id.iv_personal_type)
	protected ImageView personalType;

	@ViewById(R.id.rl_personal_email)
	protected RelativeLayout personalEmailLayout;
	@ViewById(R.id.tv_personal_email_value)
	protected TextView tvPersonalEmail;
	@ViewById(R.id.iv_personal_email)
	protected ImageView icPersonalEmail;

	@ViewById(R.id.rl_personal_tel)
	protected RelativeLayout personalTelLayout;
	@ViewById(R.id.tv_personal_tel_value)
	protected TextView tvPersonalTel;
	@ViewById(R.id.iv_personal_tel)
	protected ImageView icPersonalTel;

	@ViewById(R.id.rl_personal_fax)
	protected RelativeLayout personalFaxLayout;
	@ViewById(R.id.tv_personal_fax_value)
	protected TextView tvPersonalFax;

	@ViewById(R.id.rl_personal_country)
	protected RelativeLayout personalCountryLayout;
	@ViewById(R.id.tv_personal_country_value)
	protected TextView tvPersonalCountry;

	@ViewById(R.id.rl_personal_website)
	protected RelativeLayout personalWebsiteLayout;
	@ViewById(R.id.tv_personal_website_value)
	protected TextView tvPersonalWebsite;
	@ViewById(R.id.iv_personal_website)
	protected ImageView icPersonalWebsite;

	private String action;
	private String companyId;
	private String mailId;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mail_personal);
	}

	@AfterViews
	protected void initView()
	{
		action = getIntent().getStringExtra("action");
		companyId = getIntent().getStringExtra("companyId");
		mailId = getIntent().getStringExtra("mailId");

		titleLeftButton.setImageResource(R.drawable.ic_title_back);
		titleRightButton1.setVisibility(View.GONE);
		titleRightButton2.setVisibility(View.GONE);
		titleRightButton3.setVisibility(View.GONE);
		titleLeftButton.setOnClickListener(this);

		if ("0".equals(action))
		{
			titleText.setText(R.string.sender);
		}
		else
		{
			titleText.setText(R.string.recipient);
		}

		Drawable drawable = getResources().getDrawable(R.drawable.bg_member_title);
		float scale = drawable.getMinimumWidth() / (float) Utils.getWindowWidthPix(this);
		personalTitle.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				(int) (drawable.getMinimumHeight() / scale)));

		tvPersonalEmail.setOnClickListener(this);
		icPersonalEmail.setOnClickListener(this);
		tvPersonalTel.setOnClickListener(this);
		icPersonalTel.setOnClickListener(this);
		tvPersonalWebsite.setOnClickListener(this);
		icPersonalWebsite.setOnClickListener(this);
		getStaffProfile();
	}

	private void getStaffProfile()
	{
		progressBar.setVisibility(View.VISIBLE);
		personalScroll.setVisibility(View.GONE);
		RequestCenter.getStaffProfile(profileListener, companyId, mailId, action);
	}

	private DisposeDataListener profileListener = new DisposeDataListener()
	{
		@Override
		public void onSuccess(Object arg0)
		{
			if (isFinishing())
				return;
			User user = (User) arg0;
			progressBar.setVisibility(View.GONE);
			personalScroll.setVisibility(View.VISIBLE);
			if (null != user.content)
			{
				String gender = user.content.userInfo.gender;
				Integer inte = Integer.parseInt(gender);
				if (inte == 0)
				{
					gender = "Mr.";
				}
				else if (inte == 1)
				{
					gender = "Mrs.";
				}
				else if (inte == 2)
				{
					gender = "Ms.";
				}
				else if (inte == 3)
				{
					gender = "Miss ";
				}
				personalName.setText(gender + user.content.userInfo.fullName);
				personalCompany.setText(user.content.companyInfo.companyName);
				if (64 == Integer.parseInt(user.content.companyInfo.memberType))
				{
					personalType.setImageResource(R.drawable.ic_member_global);
				}
				else if (128 == Integer.parseInt(user.content.companyInfo.memberType))
				{
					personalType.setImageResource(R.drawable.ic_member_pro);
				}
				else
				{
					personalType.setVisibility(View.GONE);
				}
				if ("0".equals(action)) // 1为发件箱
				{
					setViewStatus(user.content.userInfo.email, personalEmailLayout, tvPersonalEmail);
				}
				else
				{
					personalEmailLayout.setVisibility(View.GONE);
				}
				setViewStatus(user.content.companyInfo.telephone, personalTelLayout, tvPersonalTel);
				setViewStatus(user.content.companyInfo.fax, personalFaxLayout, tvPersonalFax);
				setViewStatus(user.content.companyInfo.country, personalCountryLayout, tvPersonalCountry);
				setViewStatus(user.content.companyInfo.homepage, personalWebsiteLayout, tvPersonalWebsite);
			}
		}

		@Override
		public void onFailure(Object failedReason)
		{
			ToastUtil.toast(MailPersonalActivity.this, failedReason.toString());
			finish();
		}
	};

	/**
	 * 设置View的状态，根据值控制显示或隐藏
	 * @param value
	 * @param layout
	 * @param text
	 */
	private void setViewStatus(String value, RelativeLayout layout, TextView text)
	{
		if (null != value && !"".equals(value))
		{
			layout.setVisibility(View.VISIBLE);
			text.setText(value);
		}
		else
		{
			layout.setVisibility(View.GONE);
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
		case R.id.tv_personal_email_value:
		case R.id.iv_personal_email:
			// 事件统计 49 点击发件人邮箱（发件人联系信息页） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c49);

			IntentManager.sendMail(this, tvPersonalEmail.getText().toString());
			break;
		case R.id.tv_personal_tel_value:
		case R.id.iv_personal_tel:
			// 事件统计 50 点击发件人电话（发件人联系信息页） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c50);

			IntentManager.makeCall(this, tvPersonalTel.getText().toString());
			break;
		case R.id.tv_personal_website_value:
		case R.id.iv_personal_website:
			IntentManager.startLoadUrl(this, tvPersonalWebsite.getText().toString());
			break;
		}
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		// 事件统计 $10008 发件人联系信息页 页面事件
		SysManager.analysis(R.string.a_type_page, R.string.p10008);
	}

}

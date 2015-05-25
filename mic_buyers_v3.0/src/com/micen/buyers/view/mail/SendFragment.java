package com.micen.buyers.view.mail;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

import android.app.Activity;
import android.content.Intent;

import com.focustech.common.mob.DisposeDataListener;
import com.focustech.common.util.ToastUtil;
import com.focustech.common.widget.dialog.CommonProgressDialog;
import com.micen.buyers.activity.R;
import com.micen.buyers.activity.message.VisitorRegisterActivity_;
import com.micen.buyers.constant.Constants;
import com.micen.buyers.http.RequestCenter;
import com.micen.buyers.module.MailSendTarget;
import com.micen.buyers.module.user.User;
import com.micen.buyers.util.BuyerApplication;

/**********************************************************
 * @文件名称：SendFragment.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年5月7日 上午10:07:21
 * @文件描述：发送询盘Fragment
 * @修改历史：2015年5月7日创建初始版本
 **********************************************************/
@EFragment(R.layout.mail_send)
public class SendFragment extends MailSendBaseFragment
{
	private String companyName;
	private String phoneNumber;
	/**
	 * 发邮件私有参数
	 */
	private boolean isReplyQuotation = false;
	/**
	 * 是否快速询价..
	 */
	protected boolean isQuick;
	private MailSendTarget target;

	public SendFragment()
	{
	}

	protected void initIntent()
	{
		Intent intent = getActivity().getIntent();
		bundle = intent.getExtras();
		target = MailSendTarget.getValueByTag(bundle.getString("mailSendTarget"));
		acquiesceSubject = bundle.getString("subject");
		productId = bundle.getString("productId");
		receiverId = bundle.getString("companyId");
		sentToName = bundle.getString("companyName");
		catCode = bundle.getString("catCode");
		isReplyQuotation = bundle.getBoolean("isReplayQuotation", false);
		isQuick = bundle.getBoolean("isQuick", false);
		content = bundle.getString("content");

	}

	@AfterViews
	protected void initView()
	{
		initIntent();
		super.initView();
		mailTo.setTextColor(getResources().getColor(R.color.color_333333));
		mailTo.setText(sentToName);
		if (isReplyQuotation)
		{
			subjectInput.setText(getString(R.string.replay_inquire) + acquiesceSubject);
		}
		else if (isQuick)
		{
			subjectInput.setText(getString(R.string.price_inquire) + acquiesceSubject);
		}
		else
		{
			subjectInput.setText(getString(R.string.inquire_about) + acquiesceSubject);
		}

		contentInput.setText(content);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		if (BuyerApplication.getInstance().getUser() != null)
		{
			mailFrom.setText(BuyerApplication.getInstance().getUser().content.userInfo.fullName);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == Constants.SEND_INQUIRE_CODE)
		{
			if (resultCode == Activity.RESULT_OK)
			{
				if (data != null && data.getBooleanExtra("isNeedToSend", true))
				{
					sendEmail();
				}
				getActivity().finish();
			}
		}
	}

	/**
	 * 发送询盘请求
	 */
	@Override
	public void sendEmail()
	{
		if ("".equals(subjectInput.getText().toString().trim()))
		{
			ToastUtil.toast(getActivity(), R.string.input_inquiry_subject);
			return;
		}
		if ("".equals(contentInput.getText().toString().trim()))
		{
			ToastUtil.toast(getActivity(), R.string.input_inquiry_body);
			return;
		}
		if (subjectInput.getText().toString().length() > Constants.INQUIRE_SUBJECT_LENGTH)
		{
			ToastUtil.toast(getActivity(), R.string.input_inquiry_subject_size);
			return;
		}
		if (contentInput.getText().toString().trim().length() > Constants.INQUIRE_CONTENT_LENGTH)
		{
			ToastUtil.toast(getActivity(), R.string.input_inquiry_body_size);
			return;
		}
		if (initSendEmail() == null)
		{
			/**
			 * 初始化询盘公共数据
			 */
			Intent intent = new Intent(getActivity(), VisitorRegisterActivity_.class);
			intent.putExtra("subject", acquiesceSubject);
			intent.putExtra("companyName", sentToName);
			intent.putExtra("companyId", receiverId);
			intent.putExtra("productId", productId);
			intent.putExtra("catCode", catCode);
			intent.putExtra("content", contentInput.getText().toString().trim());
			intent.putExtra("mailSendTarget", MailSendTarget.getValue(target));
			startActivityForResult(intent, Constants.SEND_INQUIRE_CODE);
			return;
		}
		else
		{
			// 发送已登陆用户询盘．．
			String mSubject = subjectInput.getText().toString().trim();
			String mContent = contentInput.getText().toString().trim();
			CommonProgressDialog.getInstance().showProgressDialog(getActivity(), getString(R.string.mic_loading));
			RequestCenter.sendMail(dataListener, mSubject, mContent, receiverId, currentEmail, fullName, gender,
					wantAS, wantMICInfo, senderCompanyId, companyName, comTelphone1, comTelphone2, comTelphone3,
					target == MailSendTarget.SendByProductId ? catCode : "",
					target == MailSendTarget.SendByProductId ? "prod" : "shrom",
					target == MailSendTarget.SendByProductId ? productId : receiverId, logonStatus, companyIdentity,
					senderRole, senderHomepage, senderCountry, memberLevel, isBeforePremium, BuyerApplication
							.getInstance().getUser() != null ? BuyerApplication.getInstance().getUser().sessionid : "",
					operatorId, isReplyQuotation);
		}

	}

	/**
	 * 发邮件回调处理
	 */
	protected DisposeDataListener dataListener = new DisposeDataListener()
	{
		@Override
		public void onSuccess(Object arg0)
		{
			CommonProgressDialog.getInstance().dismissProgressDialog();
			if (getActivity() != null)
			{
				getActivity().finish();
				ToastUtil.toast(getActivity(), R.string.Send_Successful);
			}
		}

		@Override
		public void onFailure(Object failedReason)
		{
			CommonProgressDialog.getInstance().dismissProgressDialog();
			if (getActivity() != null)
			{
				ToastUtil.toast(getActivity(), failedReason);
			}
		}

	};

	/**
	 * 根据已登陆用户信息初始化发询盘参数．．
	 * @return
	 */
	private User initSendEmail()
	{
		User user = null;
		if (BuyerApplication.getInstance().getUser() != null)
		{
			user = BuyerApplication.getInstance().getUser();
			currentEmail = user.content.userInfo.email;
			fullName = user.content.userInfo.fullName;
			transferGender(user.content.userInfo.gender);
			senderCompanyId = String.valueOf(user.content.companyInfo.companyId);
			companyName = user.content.companyInfo.companyName;
			wantAS = "false";
			wantMICInfo = "false";
			phoneNumber = user.content.companyInfo.telephone;
			sqliteTel(phoneNumber);
			logonStatus = "7";
			senderRole = user.content.userInfo.userRole;
			senderHomepage = user.content.companyInfo.homepage;
			senderCountry = user.content.companyInfo.country;
			companyIdentity = user.content.companyInfo.companyIdentity;
			memberLevel = user.content.companyInfo.memberLevel;
			isBeforePremium = user.content.companyInfo.isBeforePremium;
			if ("1".equals(isBeforePremium))
			{
				isBeforePremium = "true";
			}
			else
			{
				isBeforePremium = "false";
			}
			operatorId = user.content.userInfo.operatorId;
		}
		return user;
	}

}

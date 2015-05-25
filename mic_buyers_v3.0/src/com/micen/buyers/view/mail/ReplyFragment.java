package com.micen.buyers.view.mail;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.focustech.common.mob.DisposeDataListener;
import com.focustech.common.util.ToastUtil;
import com.focustech.common.util.Utils;
import com.focustech.common.widget.dialog.CommonProgressDialog;
import com.micen.buyers.activity.R;
import com.micen.buyers.activity.message.MailPersonalActivity_;
import com.micen.buyers.constant.Constants;
import com.micen.buyers.http.RequestCenter;
import com.micen.buyers.manager.PopupManager;
import com.micen.buyers.manager.SysManager;
import com.micen.buyers.module.user.User;
import com.micen.buyers.util.BuyerApplication;
import com.micen.buyers.util.Util;

/**********************************************************
 * @文件名称：ReplyFragment.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年5月7日 上午10:07:30
 * @文件描述：回复询盘Fragment
 * @修改历史：2015年5月7日创建初始版本
 **********************************************************/
@EFragment(R.layout.mail_send)
public class ReplyFragment extends MailSendBaseFragment
{
	private String receiverCompanyName;// 收件人公司名称
	private String receiverName;// 收件人名
	private String senderName;
	private String mailId;// 邮件编号
	private String subject;// 邮件标题
	private String mailDate;// 发件日期
	private String action;
	private String companyId;
	private String mailSource;
	private User user;
	private String mailContent;
	private String fax1;
	private String fax2;
	private String fax3;

	@AfterViews
	protected void initView()
	{
		user = BuyerApplication.getInstance().getUser();
		initIntent();
		super.initView();
		mailTo.setOnClickListener(this);
		mailTo.setText(receiverName);
		mailFrom.setText(senderName);
		subjectInput.setText("Re:" + subject);
		Utils.setEditTextCursorPosition(subjectInput);
		/**
		 * 初始化上次邮件信息
		 */
		if (mailContent != null && !mailContent.equals(""))
		{
			String originalMsg = "\r\n\r\n---------Original Message--------------\r\n";
			originalMsg = originalMsg + "From: " + receiverName + "\r\n";
			originalMsg = originalMsg + "To: " + initGender(gender) + user.content.userInfo.fullName + "\r\n";
			originalMsg = originalMsg + "Sent: " + Util.formatDateToEn(mailDate) + "\r\n";
			originalMsg = originalMsg + "Subject: " + subject + "\r\n\r\n";
			mailContent = originalMsg + mailContent;
			content = getOldMessage(mailContent);
			contentInput.setText(content);
			// le_content.setSelection(tempContent.length());
		}

		checkPostBtnStatus();
	}

	protected void initIntent()
	{
		mIntent = new Intent();
		Bundle bundle = getActivity().getIntent().getExtras();
		subject = bundle.getString("subject");
		receiverCompanyName = bundle.getString("senderCompany");
		receiverName = bundle.getString("senderFullName");
		senderName = bundle.getString("receiverFullName");
		mailId = bundle.getString("mailId");
		mailContent = bundle.getString("mailContent");
		mailDate = bundle.getString("mailDate");
		action = bundle.getString("action");
		companyId = bundle.getString("companyId");
		mailSource = "0".equals(action) ? "1" : "2";
		gender = user.content.userInfo.gender;
		transferGender(gender);
	}

	/**
	 * 初始化用户性别信息
	 * @return
	 */
	private String initGender(String gender)
	{
		String genderValue = "";
		if ("Mr.".equals(gender))
			genderValue = "0";
		else if ("Mrs.".equals(gender))
			genderValue = "1";
		else if ("Ms.".equals(gender))
			genderValue = "2";
		else if ("Miss".equals(gender))
			genderValue = "3";
		else if ("0".equals(gender))
			genderValue = "Mr.";
		else if ("1".equals(gender))
			genderValue = "Mrs.";
		else if ("2".equals(gender))
			genderValue = "Ms.";
		else if ("3".equals(gender))
			genderValue = "Miss.";
		return genderValue;
	}

	/**
	 * 获得指定长度的原文信息
	 * @param tempMailContent
	 * @return
	 */
	private String getOldMessage(String tempMailContent)
	{
		String msg = tempMailContent;
		if (mailContent.length() > Constants.REPLAY_MAIL_LENGTH)
		{
			msg = mailContent.substring(0, Constants.REPLAY_MAIL_LENGTH);
		}
		return msg;
	}

	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{
		case R.id.tv_mail_to_value:
			// 事件统计 53 查看收件人信息（回复询盘） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c53);

			Intent fromPersonalInt = new Intent(getActivity(), MailPersonalActivity_.class);
			fromPersonalInt.putExtra("action", action);
			fromPersonalInt.putExtra("companyId", companyId);
			fromPersonalInt.putExtra("mailId", mailId);
			startActivity(fromPersonalInt);
			break;
		case R.id.mail_send_subject_clear:
			subjectInput.setText("");
			break;
		case R.id.tv_mail_send_shortcut:
			// 事件统计 54 查看快捷回复模版（回复询盘） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c54);
			PopupManager.getInstance().showInquiryRecommendPopup(createPopupView(true));
			break;
		}
	}

	@Override
	public void sendEmail()
	{
		if ("".equals(subjectInput.getText().toString().trim()))
		{
			ToastUtil.toast(getActivity(), R.string.input_inquiry_subject);
		}
		else
		{
			if ("".equals(contentInput.getText().toString().trim()))
			{
				ToastUtil.toast(getActivity(), R.string.input_inquiry_body);
			}
			else
			{
				// 事件统计 52 回复询盘发送 点击事件
				SysManager.analysis(R.string.a_type_click, R.string.c52);

				String subject = subjectInput.getText().toString().trim();
				String content = contentInput.getText().toString().trim();
				CommonProgressDialog.getInstance().showProgressDialog(getActivity(), getString(R.string.mic_loading));
				sqliteTel(user.content.companyInfo.telephone);
				sqliteFax(user.content.companyInfo.fax);
				RequestCenter.replyMail(dataListener, subject, content, gender, mailSource, receiverCompanyName,
						receiverName, mailId, comTelphone1, comTelphone2, comTelphone3, fax1, fax2, fax3);
			}
		}

	}

	private void sqliteFax(String string)
	{
		String[] words = string.split("-");
		if (words.length == 3) // 如果传真全填
		{
			fax1 = words[0];
			fax2 = words[1];
			fax3 = words[2];
		}
		else if (words.length == 2)// 如果传真少填了可以为空的一个
		{
			fax1 = words[0];
			fax2 = "";
			fax3 = words[1];
		}
		else
		{
			fax1 = "";
			fax2 = "";
			fax3 = "";
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

	@Override
	public void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		// 事件统计 $10009 VO内询盘回复页 页面事件
		SysManager.analysis(R.string.a_type_page, R.string.p10009);
	}

}

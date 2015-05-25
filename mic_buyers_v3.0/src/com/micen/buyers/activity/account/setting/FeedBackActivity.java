package com.micen.buyers.activity.account.setting;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.focustech.common.mob.DisposeDataListener;
import com.focustech.common.util.ToastUtil;
import com.focustech.common.util.Utils;
import com.focustech.common.widget.associatemail.MailBoxAssociateTokenizer;
import com.focustech.common.widget.dialog.CommonProgressDialog;
import com.micen.buyers.activity.R;
import com.micen.buyers.activity.account.login.UserBaseActivity;
import com.micen.buyers.http.RequestCenter;
import com.micen.buyers.manager.SysManager;
import com.micen.buyers.module.BaseResponse;
import com.micen.buyers.module.ResponseCode;
import com.micen.buyers.module.user.User;
import com.micen.buyers.util.BuyerApplication;
import com.micen.buyers.util.Util;

/**********************************************************
 * @文件名称：FeedBackActivity.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年3月30日 下午4:33:19
 * @文件描述：意见反馈页
 * @修改历史：2015年3月30日创建初始版本
 **********************************************************/
@EActivity
public class FeedBackActivity extends UserBaseActivity
{
	@ViewById(R.id.edit_subject)
	protected EditText editSubject;
	@ViewById(R.id.edit_comment)
	protected EditText editComment;
	@ViewById(R.id.edit_name)
	protected EditText editName;
	@ViewById(R.id.message_layout)
	protected LinearLayout messageLayout;
	@ViewById(R.id.submitted_layout)
	protected RelativeLayout submitLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
	}

	@AfterViews
	protected void initView()
	{
		titleLeftButton.setImageResource(R.drawable.ic_title_back);
		titleText.setText(R.string.mic_home_meun_text10);
		titleRightButton1.setVisibility(View.GONE);
		titleRightButton2.setVisibility(View.GONE);
		titleRightButton3.setVisibility(View.GONE);

		String[] recommendMailBox = getResources().getStringArray(R.array.recommend_mailbox);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.associate_mail_list_item,
				R.id.tv_recommend_mail, recommendMailBox);
		emailInput.setAdapter(adapter);
		emailInput.setTokenizer(new MailBoxAssociateTokenizer());

		titleLeftButton.setOnClickListener(this);
		textGender.setOnClickListener(this);
		btnSend.setOnClickListener(this);
		btnSend.setEnabled(false);
		editSubject.setOnFocusChangeListener(focusListener);
		editSubject.addTextChangedListener(watcher);
		editComment.setOnFocusChangeListener(focusListener);
		editComment.addTextChangedListener(watcher);
		emailInput.setOnFocusChangeListener(focusListener);
		emailInput.addTextChangedListener(watcher);
		editName.setOnFocusChangeListener(focusListener);
		editName.addTextChangedListener(watcher);
		if (BuyerApplication.getInstance().getUser() != null)
		{
			User user = BuyerApplication.getInstance().getUser();
			emailInput.setText(user.content.userInfo.email);
			editName.setText(user.content.userInfo.fullName);
			textGender.setText(initGender(user.content.userInfo.gender));
		}
	}

	private OnFocusChangeListener focusListener = new OnFocusChangeListener()
	{
		@Override
		public void onFocusChange(View v, boolean hasFocus)
		{
			switch (v.getId())
			{
			case R.id.edit_subject:
				if (!hasFocus)
				{
					if (editSubject.getText().toString().trim().equals(""))
					{
						ToastUtil.toast(FeedBackActivity.this, R.string.subject_input);
						return;
					}
					if (Util.isChinese(editSubject.getText().toString().trim()))
					{
						ToastUtil.toast(FeedBackActivity.this, R.string.subject_in_english);
						return;
					}
				}
				break;
			case R.id.edit_comment:
				if (!hasFocus)
				{
					if (editComment.getText().toString().trim().equals(""))
					{
						ToastUtil.toast(FeedBackActivity.this, R.string.comment_input);
						return;
					}
					if (Util.isChinese(editComment.getText().toString().trim()))
					{
						ToastUtil.toast(FeedBackActivity.this, R.string.comment_in_english);
						return;
					}
				}
				break;
			case R.id.associate_email_input:
				if (!hasFocus)
				{
					if (emailInput.getText().toString().trim().equals(""))
					{
						ToastUtil.toast(FeedBackActivity.this, R.string.insert_email);
						return;
					}
					if (!Utils.checkEmail(emailInput.getText().toString().trim()))
					{
						ToastUtil.toast(FeedBackActivity.this, R.string.insert_email);
						return;
					}
					if (emailInput.getText().toString().trim().length() < 5
							|| emailInput.getText().toString().trim().length() > 160)
					{
						ToastUtil.toast(FeedBackActivity.this, R.string.vaild_email);
						return;
					}
				}
				break;
			case R.id.edit_name:
				if (!hasFocus)
				{
					if (editName.getText().toString().trim().equals(""))
					{
						ToastUtil.toast(FeedBackActivity.this, R.string.full_name_input);
						return;
					}
					if (Util.isChinese(editName.getText().toString().trim()))
					{
						ToastUtil.toast(FeedBackActivity.this, R.string.full_name_in_english);
						return;
					}
					if (editName.getText().toString().trim().length() > 50)
					{
						ToastUtil.toast(FeedBackActivity.this, R.string.full_name_length);
						return;
					}
				}
				break;
			}
		}
	};

	private TextWatcher watcher = new TextWatcher()
	{
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after)
		{
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count)
		{
		}

		@Override
		public void afterTextChanged(Editable s)
		{
			/**
			 * 判断全部填写后才可以注册…… 
			 */
			if (!"".equals(emailInput.getText().toString()) && !"".equals(editName.getText().toString())
					&& !"".equals(editSubject.getText().toString()) && !"".equals(editComment.getText().toString())
					&& !"".equals(textGender.getText().toString()) && Utils.checkEmail(emailInput.getText().toString()))
			{// 可以点击
				btnSend.setEnabled(true);
			}
			else
			{// 不可以点击
				btnSend.setEnabled(false);
			}
		}
	};

	@Override
	public void onClick(View view)
	{
		super.onClick(view);
		switch (view.getId())
		{
		case R.id.common_title_back_button:
			finish();
			break;
		case R.id.tv_gender:
			showGenderDialog();
			break;
		case R.id.send_button:
			// 事件统计 137 提交反馈 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c137);
			sendFeedback();
			break;
		}
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();

		// 事件统计 $10034 反馈 页面事件
		SysManager.analysis(R.string.a_type_page, R.string.p10034);
	}

	/**
	 * 发送请求
	 */
	private void sendFeedback()
	{
		/**
		 * 收集参数信息，准备发送
		 */
		String subject = editSubject.getText().toString().trim();
		String comment = editComment.getText().toString().trim();
		String email = emailInput.getText().toString().trim();
		String gender = initGender(textGender.getText().toString().trim());
		String name = editName.getText().toString().trim();

		CommonProgressDialog.getInstance().showProgressDialog(this, getString(R.string.mic_loading));
		RequestCenter.sendFeedBack(listener, comment, email, name, gender, subject);
	}

	private DisposeDataListener listener = new DisposeDataListener()
	{
		@Override
		public void onSuccess(Object obj)
		{
			CommonProgressDialog.getInstance().dismissProgressDialog();
			if ((BaseResponse) obj != null)
			{
				messageLayout.setVisibility(View.GONE);
				submitLayout.setVisibility(View.VISIBLE);
			}
		}

		@Override
		public void onFailure(Object failedReason)
		{
			CommonProgressDialog.getInstance().dismissProgressDialog();
			ResponseCode code = ResponseCode.getCodeValueByTag(failedReason.toString());
			switch (code)
			{
			case CODE_10002:
				ToastUtil.toast(FeedBackActivity.this, R.string.feedback_subject_error);
				break;
			case CODE_10003:
				ToastUtil.toast(FeedBackActivity.this, R.string.feedback_comment_error);
				break;
			case CODE_10004:
				ToastUtil.toast(FeedBackActivity.this, R.string.feedback_email_error);
				break;
			case CODE_10005:
				ToastUtil.toast(FeedBackActivity.this, R.string.feedback_name_error);
				break;
			default:
				ToastUtil.toast(FeedBackActivity.this, failedReason);
				break;
			}
		}
	};

}
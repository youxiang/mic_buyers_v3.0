package com.micen.buyers.activity.message;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringArrayRes;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.focustech.common.mob.DisposeDataListener;
import com.focustech.common.util.ToastUtil;
import com.focustech.common.util.Utils;
import com.focustech.common.widget.associatemail.MailBoxAssociateTokenizer;
import com.focustech.common.widget.dialog.CommonDialog;
import com.focustech.common.widget.dialog.CommonDialog.DialogClickListener;
import com.focustech.common.widget.dialog.CommonProgressDialog;
import com.micen.buyers.activity.R;
import com.micen.buyers.activity.account.login.LoginActivity_;
import com.micen.buyers.activity.account.login.RegisterActivity_;
import com.micen.buyers.activity.account.login.UserBaseActivity;
import com.micen.buyers.constant.Constants;
import com.micen.buyers.http.RequestCenter;
import com.micen.buyers.manager.RFQKeyListener;
import com.micen.buyers.module.CheckResult;
import com.micen.buyers.module.LoginTarget;
import com.micen.buyers.module.MailSendTarget;
import com.micen.buyers.module.user.TempUserInfo;
import com.micen.buyers.util.BuyerApplication;
import com.micen.buyers.util.Util;

/**********************************************************
 * @文件名称：VisitorRegisterActivity.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年5月7日 上午9:51:48
 * @文件描述：游客发询盘注册页
 * @修改历史：2015年5月7日创建初始版本
 **********************************************************/
@EActivity
public class VisitorRegisterActivity extends UserBaseActivity implements TextWatcher
{
	@ViewById(R.id.ll_register_gender)
	protected RelativeLayout genderLayout;
	@ViewById(R.id.register_fullname_input)
	protected EditText fullNameInput;
	@ViewById(R.id.register_companyname_input)
	protected EditText companyNameInput;
	@ViewById(R.id.register_countrycode_input)
	protected EditText countryCodeInput;
	@ViewById(R.id.register_areacode_input)
	protected EditText areaCodeInput;
	@ViewById(R.id.register_number_input)
	protected EditText numberInput;

	@StringArrayRes(R.array.recommend_mailbox)
	protected String[] recommendMailBox;

	private CommonDialog commonDialog;

	/**
	 * 发邮件公共参数
	 */
	private String currentEmail;
	private String gender = "0";
	private String fullName;
	private String wantAS;
	private String wantMICInfo;
	private String comTelphone1;
	private String comTelphone2;
	private String comTelphone3;
	private String catCode;
	private String logonStatus;
	private String senderRole;
	private String senderHomepage;
	private String senderCountry;
	private String companyIdentity;
	private String memberLevel;
	private String isBeforePremium;
	private String operatorId;
	private String sentToName;
	private String receiverId;
	private String productId;
	private String acquiesceSubject;
	private String senderCompanyId;
	private String content;
	private MailSendTarget target;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_visitor_register);
	}

	protected void initIntent()
	{
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		acquiesceSubject = bundle.getString("subject");
		productId = bundle.getString("productId");
		receiverId = bundle.getString("companyId");
		sentToName = bundle.getString("companyName");
		catCode = bundle.getString("catCode");
		content = bundle.getString("content");
		target = MailSendTarget.getValueByTag(getIntent().getStringExtra("mailSendTarget"));
	}

	@AfterViews
	protected void initView()
	{
		initIntent();
		titleLeftButton.setImageResource(R.drawable.ic_title_back);
		titleText.setText(R.string.send_inquiry);
		titleRightButton1.setVisibility(View.GONE);
		titleRightButton2.setVisibility(View.GONE);
		titleRightButton3.setImageResource(R.drawable.ic_post_gray);
		titleRightButton3.setOnClickListener(this);

		textGender.setText("Mr.");
		// 初始化上次用户填写的信息
		if (Constants.userInfo != null)
		{
			emailInput.setText(Constants.userInfo.tempEmail);
			companyNameInput.setText(Constants.userInfo.tempCompanyname);
			areaCodeInput.setText(Constants.userInfo.tempTelphone2);
			countryCodeInput.setText(Constants.userInfo.tempTelphone1);
			fullNameInput.setText(Constants.userInfo.tempFullname);
			numberInput.setText(Constants.userInfo.tempTelphone3);
			textGender.setText(Constants.userInfo.tempGender);
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.associate_mail_list_item,
				R.id.tv_recommend_mail, recommendMailBox);
		emailInput.setAdapter(adapter);
		emailInput.setTokenizer(new MailBoxAssociateTokenizer());
		emailInput.addTextChangedListener(this);
		emailInput.setOnFocusChangeListener(focusListener);
		fullNameInput.addTextChangedListener(this);
		fullNameInput.setOnFocusChangeListener(focusListener);
		companyNameInput.addTextChangedListener(this);
		companyNameInput.setOnFocusChangeListener(focusListener);
		countryCodeInput.addTextChangedListener(this);
		countryCodeInput.setOnFocusChangeListener(focusListener);
		numberInput.addTextChangedListener(this);
		numberInput.setOnFocusChangeListener(focusListener);
		numberInput.setKeyListener(RFQKeyListener.getInstance(Constants.numEditTextDigits));
		areaCodeInput.setOnFocusChangeListener(focusListener);
		titleLeftButton.setOnClickListener(this);
		genderLayout.setOnClickListener(this);

	}

	/**
	 * 失去焦点时就判断是否输入或输入是否正确
	 * 及时提醒
	 */
	private OnFocusChangeListener focusListener = new OnFocusChangeListener()
	{
		@Override
		public void onFocusChange(View v, boolean hasFocus)
		{
			switch (v.getId())
			{
			case R.id.associate_email_input:
				if (!hasFocus)
				{
					if (emailInput.getText().toString().trim().equals(""))
					{
						ToastUtil.toast(VisitorRegisterActivity.this, R.string.insert_email);
						return;
					}
					if (!Utils.checkEmail(emailInput.getText().toString().trim()))
					{
						ToastUtil.toast(VisitorRegisterActivity.this, R.string.vaild_email);
						return;
					}
					if (emailInput.getText().toString().trim().length() < 5
							|| emailInput.getText().toString().trim().length() > 160)
					{
						ToastUtil.toast(VisitorRegisterActivity.this, R.string.vaild_email);
						return;
					}
					checkEmail();
				}
				break;
			case R.id.register_fullname_input:
				if (!hasFocus)
				{
					if (fullNameInput.getText().toString().trim().equals(""))
					{
						ToastUtil.toast(VisitorRegisterActivity.this, R.string.full_name_input);
						return;
					}
					if (Util.isChinese(fullNameInput.getText().toString().trim()))
					{
						ToastUtil.toast(VisitorRegisterActivity.this, R.string.full_name_in_english);
						return;
					}
					if (fullNameInput.getText().toString().trim().length() > 50)
					{
						ToastUtil.toast(VisitorRegisterActivity.this, R.string.full_name_length);
						return;
					}
				}
				break;
			case R.id.register_companyname_input:
				if (!hasFocus)
				{
					if (companyNameInput.getText().toString().trim().equals(""))
					{
						ToastUtil.toast(VisitorRegisterActivity.this, R.string.insert_company_name);
						return;
					}
					if (Util.isChinese(companyNameInput.getText().toString().trim()))
					{
						ToastUtil.toast(VisitorRegisterActivity.this, R.string.company_name_in_english);
						return;
					}
					if (companyNameInput.getText().toString().trim().length() > 160)
					{
						ToastUtil.toast(VisitorRegisterActivity.this, R.string.company_name_length);
						return;
					}
				}
				break;
			case R.id.register_countrycode_input:
				if (!hasFocus)
				{
					if (countryCodeInput.getText().toString().trim().equals(""))
					{
						ToastUtil.toast(VisitorRegisterActivity.this, R.string.input_country_code);
						return;
					}
				}
				break;
			case R.id.register_number_input:
				if (!hasFocus)
				{
					if (numberInput.getText().toString().trim().equals(""))
					{
						ToastUtil.toast(VisitorRegisterActivity.this, R.string.input_phone_number);
						return;
					}
					if (numberInput.getText().toString().trim().length() > 20)
					{
						ToastUtil.toast(VisitorRegisterActivity.this, R.string.phone_number_length);
						return;
					}
					if (Util.isHasEnglish(numberInput.getText().toString().trim()))
					{
						ToastUtil.toast(VisitorRegisterActivity.this, R.string.phone_number_invalidate);
						return;
					}
				}
				break;
			case R.id.register_areacode_input:
				if (!hasFocus && areaCodeInput.isShown())
				{
					if (areaCodeInput.getText().toString().trim().length() > 10)
					{
						ToastUtil.toast(VisitorRegisterActivity.this, R.string.area_code_length);
						return;
					}
				}
				break;
			}
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			back();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == Constants.SEND_INQUIRE_CODE)
		{
			if (resultCode == RESULT_OK)
			{
				Intent resultIntent = new Intent();
				resultIntent.putExtra("isNeedToSend", true);
				setResult(RESULT_OK, resultIntent);
				finish();
			}
		}
	}

	/**
	 * 点击提交按钮时最后进行数据较验……
	 * @return
	 */
	private boolean checkFields()
	{
		if (!Utils.checkEmail(emailInput.getText().toString().trim()))
		{
			ToastUtil.toast(this, R.string.insert_email);
			return false;
		}
		if (emailInput.getText().toString().trim().length() < 5
				|| emailInput.getText().toString().trim().length() > 160)
		{
			ToastUtil.toast(this, R.string.vaild_email);
			return false;
		}
		if (Util.isChinese(fullNameInput.getText().toString().trim()))
		{
			ToastUtil.toast(this, R.string.full_name_in_english);
			return false;
		}
		if (fullNameInput.getText().toString().trim().length() > 50)
		{
			ToastUtil.toast(this, R.string.full_name_length);
			return false;
		}
		if (Util.isChinese(companyNameInput.getText().toString().trim()))
		{
			ToastUtil.toast(this, R.string.company_name_in_english);
			return false;
		}
		if (companyNameInput.getText().toString().trim().length() > 160)
		{
			ToastUtil.toast(this, R.string.company_name_length);
			return false;
		}
		if (countryCodeInput.getText().toString().trim().length() > 10)
		{
			ToastUtil.toast(this, R.string.country_code_length);
			return false;
		}
		if (areaCodeInput.getText().toString().trim().length() > 10)
		{
			ToastUtil.toast(this, R.string.area_code_length);
			return false;
		}
		if (numberInput.getText().toString().trim().length() > 20)
		{
			ToastUtil.toast(this, R.string.number_length);
			return false;
		}
		return true;
	}

	public void onClick(View view)
	{
		super.onClick(view);
		switch (view.getId())
		{
		case R.id.common_title_back_button:
			back();
			break;
		case R.id.ll_register_gender:
			showGenderDialog();
			break;
		case R.id.common_title_right_button3:
			if (checkFields())
			{
				initSendEmail();
			}
			break;
		}
	}

	/**
	 * 根据临时填写信息初始化发询盘参数．．
	 */
	private void initSendEmail()
	{
		/**
		 * 直接发送游客询盘
		 */
		currentEmail = emailInput.getText().toString().trim();
		fullName = fullNameInput.getText().toString().trim();
		/**
		 * 游客公司名
		 */
		sentToName = companyNameInput.getText().toString().trim();
		wantMICInfo = "true";
		wantAS = "true";
		senderCompanyId = "0";
		logonStatus = "0";
		companyIdentity = "";
		senderRole = "";
		senderHomepage = "";
		senderCountry = "";
		memberLevel = "0";
		isBeforePremium = "false";
		operatorId = "00";
		comTelphone1 = countryCodeInput.getText().toString();
		comTelphone2 = areaCodeInput.getText().toString();
		comTelphone3 = numberInput.getText().toString();

		CommonProgressDialog.getInstance().showProgressDialog(this, getString(R.string.mic_loading));
		RequestCenter
				.sendMail(
						dataListener,
						acquiesceSubject,
						content,
						receiverId,
						currentEmail,
						fullName,
						gender,
						wantAS,
						wantMICInfo,
						senderCompanyId,
						sentToName,
						comTelphone1,
						comTelphone2,
						comTelphone3,
						target == MailSendTarget.SendByProductId ? catCode : "",
						target == MailSendTarget.SendByProductId ? "prod" : "shrom",
						target == MailSendTarget.SendByProductId ? productId : receiverId,
						logonStatus,
						companyIdentity,
						senderRole,
						senderHomepage,
						senderCountry,
						memberLevel,
						isBeforePremium,
						BuyerApplication.getInstance().getUser() != null ? BuyerApplication.getInstance().getUser().sessionid
								: "", operatorId, false);
		/**
		 * 记录用户临时信息,在此处保存只为保存最新信息
		 */
		saveTempInfo();
	}

	private void checkEmail()
	{
		CommonProgressDialog.getInstance().showProgressDialog(this, getString(R.string.mic_loading));
		RequestCenter.checkEmail(checkEmailData, emailInput.getText().toString().trim());
	}

	private void showDialog()
	{
		CommonDialog dialog = new CommonDialog(this, getString(R.string.auto_login_msg), getString(R.string.mic_ok),
				getString(R.string.cancel), Util.dip2px(258), new DialogClickListener()
				{
					@Override
					public void onDialogClick()
					{
						Intent intent = new Intent(VisitorRegisterActivity.this, LoginActivity_.class);
						intent.putExtra("loginTarget", LoginTarget.getValue(LoginTarget.SentInquiry));
						intent.putExtra("currentEmail", emailInput.getText().toString().trim());
						startActivityForResult(intent, Constants.SEND_INQUIRE_CODE);
					}
				});
		dialog.show();
	}

	private DisposeDataListener checkEmailData = new DisposeDataListener()
	{
		@Override
		public void onSuccess(Object result)
		{
			CommonProgressDialog.getInstance().dismissProgressDialog();
			CheckResult value = (CheckResult) result;
			Constants.isRegister = value.result;
			if (value.result)
			{
				showDialog();
			}
		}

		@Override
		public void onFailure(Object failedReason)
		{
			CommonProgressDialog.getInstance().dismissProgressDialog();
			ToastUtil.toast(VisitorRegisterActivity.this, failedReason);
		}
	};

	/**
	 * 发邮件回调处理
	 */
	protected DisposeDataListener dataListener = new DisposeDataListener()
	{
		@Override
		public void onSuccess(Object arg0)
		{
			CommonProgressDialog.getInstance().dismissProgressDialog();
			if (!Constants.isRegister)
			{
				commonDialog = new CommonDialog(VisitorRegisterActivity.this, getString(R.string.go_to_register),
						getString(R.string.join_free), getString(R.string.cancel), Util.dip2px(285),
						new DialogClickListener()
						{
							@Override
							public void onDialogClick()
							{
								// 去登陆页面
								Intent intent = new Intent(VisitorRegisterActivity.this, RegisterActivity_.class);
								intent.putExtra("tempinfo", Constants.userInfo);
								startActivity(intent);
								Intent resultIntent = new Intent();
								resultIntent.putExtra("isNeedToSend", false);
								setResult(RESULT_OK, resultIntent);
								finish();
							}
						}, new DialogClickListener()
						{
							@Override
							public void onDialogClick()
							{
								Intent resultIntent = new Intent();
								resultIntent.putExtra("isNeedToSend", false);
								setResult(RESULT_OK, resultIntent);
								finish();
							}
						});
				commonDialog.show();
			}
			else
			{
				Intent resultIntent = new Intent();
				resultIntent.putExtra("isNeedToSend", false);
				setResult(RESULT_OK, resultIntent);
				finish();
			}
			ToastUtil.toast(VisitorRegisterActivity.this, R.string.Send_Successful);
		}

		@Override
		public void onFailure(Object failedReason)
		{
			CommonProgressDialog.getInstance().dismissProgressDialog();
			ToastUtil.toast(VisitorRegisterActivity.this, failedReason);
		}

	};

	private void back()
	{
		if (emailInput.getText().toString().trim().length() > 0
				|| fullNameInput.getText().toString().trim().length() > 0
				|| companyNameInput.getText().toString().trim().length() > 0
				|| numberInput.getText().toString().trim().length() > 0)
		{
			CommonDialog dialog = new CommonDialog(this, "Are you sure to cancel?", getString(R.string.mic_confirm),
					getString(R.string.cancel), Util.dip2px(258), new DialogClickListener()
					{
						@Override
						public void onDialogClick()
						{
							saveTempInfo();
							finish();
						}
					});
			dialog.show();
		}
		else
		{
			saveTempInfo();
			finish();
		}
	}

	/**
	 * 保存临时信息
	 */
	private void saveTempInfo()
	{
		Constants.userInfo = new TempUserInfo();
		Constants.userInfo.tempCompanyname = companyNameInput.getText().toString().trim();
		Constants.userInfo.tempEmail = emailInput.getText().toString().trim();
		Constants.userInfo.tempFullname = fullNameInput.getText().toString().trim();
		Constants.userInfo.tempGender = textGender.getText().toString().trim();
		Constants.userInfo.tempTelphone1 = countryCodeInput.getText().toString();
		Constants.userInfo.tempTelphone2 = areaCodeInput.getText().toString();
		Constants.userInfo.tempTelphone3 = numberInput.getText().toString();
	}

	@Override
	public void afterTextChanged(Editable s)
	{
		if (!"".equals(emailInput.getText().toString()) && !"".equals(fullNameInput.getText().toString())
				&& !"".equals(companyNameInput.getText().toString())
				&& !"".equals(countryCodeInput.getText().toString()) && !"".equals(numberInput.getText().toString())
				&& Utils.checkEmail(emailInput.getText().toString()))
		{// 可以点击
			titleRightButton3.setEnabled(true);
			titleRightButton3.setImageResource(R.drawable.ic_post);
		}
		else
		{// 不可以点击
			titleRightButton3.setEnabled(false);
			titleRightButton3.setImageResource(R.drawable.ic_post_gray);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after)
	{
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count)
	{
	}

}
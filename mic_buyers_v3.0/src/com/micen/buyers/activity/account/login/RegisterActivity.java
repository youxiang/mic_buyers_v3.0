package com.micen.buyers.activity.account.login;

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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.focustech.common.mob.DisposeDataListener;
import com.focustech.common.util.ToastUtil;
import com.focustech.common.util.Utils;
import com.focustech.common.widget.associatemail.MailBoxAssociateTokenizer;
import com.focustech.common.widget.dialog.CommonDialog;
import com.focustech.common.widget.dialog.CommonDialog.DialogClickListener;
import com.focustech.common.widget.dialog.CommonProgressDialog;
import com.micen.buyers.activity.R;
import com.micen.buyers.constant.Constants;
import com.micen.buyers.db.SharedPreferenceManager;
import com.micen.buyers.http.RequestCenter;
import com.micen.buyers.manager.RFQKeyListener;
import com.micen.buyers.manager.SysManager;
import com.micen.buyers.module.ResponseCode;
import com.micen.buyers.module.user.TempUserInfo;
import com.micen.buyers.module.user.User;
import com.micen.buyers.util.BitmapUtil;
import com.micen.buyers.util.BuyerApplication;
import com.micen.buyers.util.Util;

/**********************************************************
 * @文件名称：RegisterActivity.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年3月31日 下午2:09:17
 * @文件描述：注册页
 * @修改历史：2015年3月31日创建初始版本
 **********************************************************/
@EActivity
public class RegisterActivity extends UserBaseActivity implements TextWatcher
{
	@ViewById(R.id.rl_register_country_layout)
	protected RelativeLayout countryLayout;
	@ViewById(R.id.iv_register_country_flag)
	protected ImageView countryFlag;
	@ViewById(R.id.tv_register_country_name)
	protected TextView countryName;
	@ViewById(R.id.register_input_password)
	protected EditText passwordInput;
	@ViewById(R.id.register_input_password_confirm)
	protected EditText passwordConfirmInput;
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

	private String countryCode;
	private String countryCodeValue;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
	}

	@AfterViews
	protected void initView()
	{
		titleLeftButton.setImageResource(R.drawable.ic_title_back);
		titleText.setText(R.string.mic_joinnow);
		titleRightButton1.setVisibility(View.GONE);
		titleRightButton2.setVisibility(View.GONE);
		titleRightButton3.setVisibility(View.GONE);

		textGender.setText("Mr.");
		TempUserInfo userInfo = (TempUserInfo) getIntent().getSerializableExtra("tempinfo");
		if (userInfo != null)
		{
			emailInput.setText(userInfo.tempEmail);
			companyNameInput.setText(userInfo.tempCompanyname);
			areaCodeInput.setText(userInfo.tempTelphone2);
			countryCodeInput.setText(userInfo.tempTelphone1);
			fullNameInput.setText(userInfo.tempFullname);
			numberInput.setText(userInfo.tempTelphone3);
			textGender.setText(userInfo.tempGender);
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.associate_mail_list_item,
				R.id.tv_recommend_mail, recommendMailBox);
		emailInput.setAdapter(adapter);
		emailInput.setTokenizer(new MailBoxAssociateTokenizer());
		countryName.addTextChangedListener(this);
		emailInput.addTextChangedListener(this);
		emailInput.setOnFocusChangeListener(focusListener);
		passwordInput.addTextChangedListener(this);
		passwordInput.setOnFocusChangeListener(focusListener);
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
		countryLayout.setOnClickListener(this);
		genderLayout.setOnClickListener(this);
		btnSend.setOnClickListener(this);
		btnSend.setEnabled(false);

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
						ToastUtil.toast(RegisterActivity.this, R.string.insert_email);
						return;
					}
					if (!Utils.checkEmail(emailInput.getText().toString().trim()))
					{
						ToastUtil.toast(RegisterActivity.this, R.string.vaild_email);
						return;
					}
					if (emailInput.getText().toString().trim().length() < 5
							|| emailInput.getText().toString().trim().length() > 160)
					{
						ToastUtil.toast(RegisterActivity.this, R.string.vaild_email);
						return;
					}
				}
				break;

			case R.id.register_input_password:
				if (!hasFocus)
				{
					if (passwordInput.getText().toString().trim().equals(""))
					{
						ToastUtil.toast(RegisterActivity.this, R.string.input_password);
						return;
					}
					if (!Util.isValidKeyWord(passwordInput.getText().toString()))
					{
						ToastUtil.toast(RegisterActivity.this, R.string.valid_password);
						return;
					}
					if (isSame(passwordInput.getText().toString().trim())
							|| isOrder(passwordInput.getText().toString().trim()))
					{
						ToastUtil.toast(RegisterActivity.this, R.string.password_repeat);
						return;
					}
				}
				break;
			case R.id.register_fullname_input:
				if (!hasFocus)
				{
					if (fullNameInput.getText().toString().trim().equals(""))
					{
						ToastUtil.toast(RegisterActivity.this, R.string.full_name_input);
						return;
					}
					if (Util.isChinese(fullNameInput.getText().toString().trim()))
					{
						ToastUtil.toast(RegisterActivity.this, R.string.full_name_in_english);
						return;
					}
					if (fullNameInput.getText().toString().trim().length() > 50)
					{
						ToastUtil.toast(RegisterActivity.this, R.string.full_name_length);
						return;
					}
				}
				break;
			case R.id.register_companyname_input:
				if (!hasFocus)
				{
					if (companyNameInput.getText().toString().trim().equals(""))
					{
						ToastUtil.toast(RegisterActivity.this, R.string.insert_company_name);
						return;
					}
					if (Util.isChinese(companyNameInput.getText().toString().trim()))
					{
						ToastUtil.toast(RegisterActivity.this, R.string.company_name_in_english);
						return;
					}
					if (companyNameInput.getText().toString().trim().length() > 160)
					{
						ToastUtil.toast(RegisterActivity.this, R.string.company_name_length);
						return;
					}
				}
				break;
			case R.id.register_countrycode_input:
				if (!hasFocus)
				{
					if (countryCodeInput.getText().toString().trim().equals(""))
					{
						ToastUtil.toast(RegisterActivity.this, R.string.input_country_code);
						return;
					}
				}
				break;
			case R.id.register_number_input:
				if (!hasFocus)
				{
					if (numberInput.getText().toString().trim().equals(""))
					{
						ToastUtil.toast(RegisterActivity.this, R.string.input_phone_number);
						return;
					}
					if (numberInput.getText().toString().trim().length() > 20)
					{
						ToastUtil.toast(RegisterActivity.this, R.string.phone_number_length);
						return;
					}
					if (Util.isHasEnglish(numberInput.getText().toString().trim()))
					{
						ToastUtil.toast(RegisterActivity.this, R.string.phone_number_invalidate);
						return;
					}
				}
				break;
			case R.id.register_areacode_input:
				if (!hasFocus && areaCodeInput.isShown())
				{
					if (areaCodeInput.getText().toString().trim().length() > 10)
					{
						ToastUtil.toast(RegisterActivity.this, R.string.area_code_length);
						return;
					}
				}
				break;
			}
		}
	};

	/**
	 * 接收从国家选择页面传来的数据
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (resultCode)
		{
		case RESULT_OK:
			Bundle b = data.getExtras();
			countryName.setText(b.getString("country"));
			countryCode = b.getString("countrycode");
			countryCodeValue = b.getString("countrycodevalue");
			String countryTelNum = b.getString("countryTelNum");
			String countryAreaNum = b.getString("countryAreaNum");
			countryCodeInput.setText(countryTelNum);
			if ("-1".equals(countryAreaNum))
			{
				areaCodeInput.setVisibility(View.GONE);
			}
			else
			{
				areaCodeInput.setVisibility(View.VISIBLE);
				areaCodeInput.setText(countryAreaNum);
			}
			if (b.getByteArray("countryFlag") != null)
			{
				countryFlag.setVisibility(View.VISIBLE);
				countryFlag.setBackgroundDrawable(BitmapUtil.bytes2Drawable(b.getByteArray("countryFlag")));
			}
			break;
		}
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		// 事件统计 $10029 注册页 页面事件
		SysManager.analysis(R.string.a_type_page, R.string.p10029);
	}

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
		if (!Util.isValidKeyWord(passwordInput.getText().toString()))
		{
			ToastUtil.toast(this, R.string.valid_password);
			return false;
		}
		if (!passwordInput.getText().toString().equals(passwordConfirmInput.getText().toString()))
		{
			ToastUtil.toast(this, R.string.conform_password);
			return false;
		}
		if (isSame(passwordInput.getText().toString().trim()) || isOrder(passwordInput.getText().toString().trim()))
		{
			ToastUtil.toast(RegisterActivity.this, R.string.password_repeat);
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
		case R.id.rl_register_country_layout:
			// 事件统计120 选择国家（注册页） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c120);

			Intent intent = new Intent(RegisterActivity.this, CountryActivity_.class);
			startActivityForResult(intent, 0);
			break;
		case R.id.ll_register_gender:
			// 事件统计 122 选择称呼（注册页） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c122);

			showGenderDialog();
			break;
		case R.id.send_button:
			if (checkFields())
			{

				// 事件统计 119 点击提交注册信息 点击事件
				SysManager.analysis(R.string.a_type_click, R.string.c119);
				// 进行注册
				requestRegisterMessage();
			}
			break;
		}
	}

	private void back()
	{
		if (emailInput.getText().toString().trim().length() > 0
				|| passwordInput.getText().toString().trim().length() > 0
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
							finish();
						}
					});
			dialog.show();
		}
		else
		{
			finish();
		}
	}

	private DisposeDataListener registerData = new DisposeDataListener()
	{
		@Override
		public void onSuccess(Object result)
		{
			CommonProgressDialog.getInstance().dismissProgressDialog();
			BuyerApplication.getInstance().setUser((User) result);
			SharedPreferenceManager.getInstance().putString(getString(R.string.last_login_time),
					((User) result).content.userInfo.email);
			ToastUtil.toast(RegisterActivity.this, R.string.mic_register_success);
			setResult(RESULT_OK);
			finish();
		}

		@Override
		public void onFailure(Object failedReason)
		{
			CommonProgressDialog.getInstance().dismissProgressDialog();
			ResponseCode code = ResponseCode.getCodeValueByTag(failedReason.toString());
			switch (code)
			{
			case CODE_10002:
				ToastUtil.toast(RegisterActivity.this, R.string.regist_failed);
				break;
			case CODE_10003:
				ToastUtil.toast(RegisterActivity.this, R.string.vaild_email);
				break;
			case CODE_10004:
				ToastUtil.toast(RegisterActivity.this, R.string.used_email);
				break;
			case CODE_10007:
				ToastUtil.toast(RegisterActivity.this, R.string.valid_password);
				break;
			case CODE_10008:
				ToastUtil.toast(RegisterActivity.this, R.string.inlegal_password);
				break;
			case CODE_10009:
				ToastUtil.toast(RegisterActivity.this, R.string.full_name_in_english);
				break;
			case CODE_10011:
				ToastUtil.toast(RegisterActivity.this, R.string.country_code_input);
				break;
			default:
				ToastUtil.toast(RegisterActivity.this, failedReason);
				break;
			}
		}
	};

	/**
	 * 根据填写的信息发送注册请求
	 */
	private void requestRegisterMessage()
	{
		CommonProgressDialog.getInstance().showProgressDialog(this, getString(R.string.mic_loading));
		String genderValue = initGender(textGender.getText().toString().trim());
		RequestCenter.userRegister(countryCode, countryCodeValue, emailInput.getText().toString(), passwordInput
				.getText().toString(), genderValue, fullNameInput.getText().toString().trim(), companyNameInput
				.getText().toString(), countryCodeInput.getText().toString(), areaCodeInput.getText().toString(),
				numberInput.getText().toString(), registerData);
	}

	@Override
	public void afterTextChanged(Editable s)
	{
		if (!"".equals(emailInput.getText().toString()) && !"".equals(passwordInput.getText().toString())
				&& !"".equals(fullNameInput.getText().toString()) && !"".equals(companyNameInput.getText().toString())
				&& !"".equals(countryCodeInput.getText().toString()) && !"".equals(numberInput.getText().toString())
				&& !"".equals(countryName.getText().toString().trim())
				&& Utils.checkEmail(emailInput.getText().toString()))
		{// 可以点击
			btnSend.setEnabled(true);
		}
		else
		{// 不可以点击
			btnSend.setEnabled(false);
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

	// 顺序表
	static String orderStr = "";
	static
	{
		for (int i = 33; i < 127; i++)
		{
			orderStr += Character.toChars(i)[0];
		}
	}

	// 判断是否有顺序
	public static boolean isOrder(String str)
	{
		if (!str.matches("((\\d)|([a-z])|([A-Z]))+"))
		{
			return false;
		}
		return orderStr.contains(str);
	}

	// 判断是否相同
	public static boolean isSame(String str)
	{
		String regex = str.substring(0, 1) + "{" + str.length() + "}";
		return str.matches(regex);
	}

}
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
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.focustech.common.mob.DisposeDataListener;
import com.focustech.common.util.ToastUtil;
import com.focustech.common.util.Utils;
import com.focustech.common.widget.associatemail.MailBoxAssociateTokenizer;
import com.focustech.common.widget.dialog.CommonProgressDialog;
import com.micen.buyers.activity.R;
import com.micen.buyers.activity.account.FavoriteActivity_;
import com.micen.buyers.activity.account.MemberInfoActivity_;
import com.micen.buyers.activity.account.MessageActivity_;
import com.micen.buyers.activity.account.QuotationReceivedActivity_;
import com.micen.buyers.activity.account.SourcingRequestActivity_;
import com.micen.buyers.activity.rfq.RFQAddActivity_;
import com.micen.buyers.constant.LoginRequestCode;
import com.micen.buyers.db.SharedPreferenceManager;
import com.micen.buyers.http.RequestCenter;
import com.micen.buyers.manager.SysManager;
import com.micen.buyers.module.LoginTarget;
import com.micen.buyers.module.ResponseCode;
import com.micen.buyers.module.user.User;
import com.micen.buyers.util.BuyerApplication;
import com.micen.buyers.util.Util;

/**********************************************************
 * @文件名称：LoginActivity.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年3月31日 上午11:24:00
 * @文件描述：登录页
 * @修改历史：2015年3月31日创建初始版本
 **********************************************************/
@EActivity
public class LoginActivity extends UserBaseActivity
{
	@ViewById(R.id.associate_email_clear)
	protected ImageView btnEmailClear;
	@ViewById(R.id.password_clear)
	protected ImageView btnPasswordClear;
	@ViewById(R.id.ic_password_show)
	protected ImageView btnPasswordShow;
	@ViewById(R.id.title_register_button)
	protected TextView btnRegister;
	@ViewById(R.id.login_input_password)
	protected EditText passwordInput;
	@ViewById(R.id.btn_login)
	protected TextView btnLogin;
	@ViewById(R.id.btn_forgot_password)
	protected TextView btnForgotPassword;
	@ViewById(R.id.login_scroll)
	protected ScrollView scrollView;

	@StringArrayRes(R.array.recommend_mailbox)
	protected String[] recommendMailBox;

	private Intent mIntent;
	private LoginTarget target;
	private String currentEmail;
	private boolean isShowPassword = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	@AfterViews
	protected void initView()
	{
		mIntent = getIntent();
		titleLeftButton.setImageResource(R.drawable.ic_title_back);
		titleText.setText(R.string.mic_home_meun_text5);
		titleRightButton1.setVisibility(View.GONE);
		titleRightButton2.setVisibility(View.GONE);
		titleRightButton3.setVisibility(View.GONE);
		btnRegister.setVisibility(View.VISIBLE);
		titleLeftButton.setOnClickListener(this);
		btnRegister.setOnClickListener(this);
		btnEmailClear.setOnClickListener(this);
		btnPasswordClear.setOnClickListener(this);
		btnPasswordShow.setOnClickListener(this);

		if (mIntent != null && mIntent.hasExtra("currentEmail"))
		{
			currentEmail = mIntent.getStringExtra("currentEmail");
			if (currentEmail != null && !"".equals(currentEmail))
			{
				emailInput.setText(currentEmail);
				Utils.setEditTextCursorPosition(emailInput);
			}
		}
		else
		{
			emailInput.setText(SharedPreferenceManager.getInstance().getString("lastLoginName", ""));
			Utils.setEditTextCursorPosition(emailInput);
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.associate_mail_list_item,
				R.id.tv_recommend_mail, recommendMailBox);

		emailInput.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				// TODO Auto-generated method stub
				// 事件统计 115 点击使用邮箱联想（登录页） 点击事件
				SysManager.analysis(R.string.a_type_click, R.string.c115);
			}
		});
		emailInput.setAdapter(adapter);
		emailInput.setTokenizer(new MailBoxAssociateTokenizer());
		passwordInput.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
		passwordInput.setOnKeyListener(onKeyListener);
		btnLogin.setOnClickListener(this);
		emailInput.addTextChangedListener(watcher);
		passwordInput.addTextChangedListener(watcher);

		btnForgotPassword.setOnClickListener(this);
		checkClearBtnStatus();
	}

	private TextWatcher watcher = new TextWatcher()
	{
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count)
		{

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after)
		{

		}

		@Override
		public void afterTextChanged(Editable s)
		{
			checkClearBtnStatus();
		}
	};

	private void checkClearBtnStatus()
	{
		btnEmailClear.setVisibility("".equals(emailInput.getText().toString()) ? View.GONE : View.VISIBLE);
		btnPasswordClear.setVisibility("".equals(passwordInput.getText().toString()) ? View.GONE : View.VISIBLE);
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		// 事件统计$10028 登录页 页面事件
		SysManager.analysis(R.string.a_type_page, R.string.p10028);

		if (mIntent != null && mIntent.hasExtra("loginTarget"))
		{
			target = LoginTarget.getValueByTag(mIntent.getStringExtra("loginTarget"));
		}
	}

	private void intentToLoginTarget()
	{
		if (target == null)
			finish();
		switch (target)
		{
		case Message:
			if (BuyerApplication.getInstance().isBuyerSuspended())
			{
				ToastUtil.toast(this, R.string.mic_buyer_suspended);
			}
			else
			{
				Intent inboxIntent = new Intent(this, MessageActivity_.class);
				startActivity(inboxIntent);
			}
			finish();
			break;
		case Quotation:
			if (BuyerApplication.getInstance().isBuyerSuspended())
			{
				ToastUtil.toast(this, R.string.mic_buyer_suspended);
			}
			else
			{
				Intent quotationIntent = new Intent(this, QuotationReceivedActivity_.class);
				startActivity(quotationIntent);
			}
			finish();
			break;
		case Sourcing:
			if (BuyerApplication.getInstance().isBuyerSuspended())
			{
				ToastUtil.toast(this, R.string.mic_buyer_suspended);
			}
			else
			{
				Intent sourcingIntent = new Intent(this, SourcingRequestActivity_.class);
				startActivity(sourcingIntent);
			}
			finish();
			break;
		case Member:
			if (BuyerApplication.getInstance().isBuyerSuspended())
			{
				ToastUtil.toast(this, R.string.mic_buyer_suspended);
			}
			else
			{
				Intent memberIntent = new Intent(this, MemberInfoActivity_.class);
				startActivity(memberIntent);
			}
			finish();
			break;
		case Favorate_Product:
			Intent favProdIntent = new Intent(this, FavoriteActivity_.class);
			favProdIntent.putExtra("currentItem", 0);
			startActivity(favProdIntent);
			finish();
			break;
		case Favorate_Supplier:
			Intent favSuppIntent = new Intent(this, FavoriteActivity_.class);
			favSuppIntent.putExtra("currentItem", 1);
			startActivity(favSuppIntent);
			finish();
			break;
		case Favorate_Category:
			Intent favCateIntent = new Intent(this, FavoriteActivity_.class);
			favCateIntent.putExtra("currentItem", 2);
			startActivity(favCateIntent);
			finish();
			break;
		case PostRFQ:
			Intent postRFQIntent = new Intent();
			postRFQIntent.setClass(this, RFQAddActivity_.class);
			postRFQIntent.putExtra("loginSuccess", true);
			startActivity(postRFQIntent);
			finish();
			break;
		case SentInquiry:
		case ClickForFavorite:
			setResult(RESULT_OK);
			finish();
		default:
			finish();
			break;
		}
	}

	@Override
	protected void onNewIntent(Intent intent)
	{
		mIntent = intent;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (requestCode)
		{
		case LoginRequestCode.REGISTER_CODE:
			switch (resultCode)
			{
			case RESULT_OK:
				intentToLoginTarget();
				break;
			}
			break;
		}
	}

	private OnKeyListener onKeyListener = new OnKeyListener()
	{
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event)
		{
			if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)
			{
				Util.hideSoftInputMethod(LoginActivity.this, v);
				sendLoginRequest();
				return true;
			}
			return false;
		}
	};

	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		switch (v.getId())
		{
		case R.id.common_title_back_button:

			// 113 取消登录（登录页） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c113);

			finish();
			break;
		case R.id.btn_login:

			// 事件统计 117 点击登录（登录页） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c117);

			sendLoginRequest();
			break;
		case R.id.associate_email_clear:
			emailInput.setText("");
			break;
		case R.id.password_clear:
			passwordInput.setText("");
			break;
		case R.id.ic_password_show:

			// 事件统计116 查看密码（登录页） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c116);

			if (!isShowPassword)
			{
				isShowPassword = true;
				passwordInput.setInputType(EditorInfo.TYPE_CLASS_TEXT);
				btnPasswordShow.setBackgroundResource(R.drawable.ic_password_show_on);
			}
			else
			{
				isShowPassword = false;
				passwordInput.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
				btnPasswordShow.setBackgroundResource(R.drawable.ic_password_show_off);
			}
			Utils.setEditTextCursorPosition(passwordInput);
			break;
		case R.id.title_register_button:

			// 事件统计 114 注册（登录页） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c114);

			Intent intent = new Intent(this, RegisterActivity_.class);
			startActivityForResult(intent, LoginRequestCode.REGISTER_CODE);
			break;
		case R.id.btn_forgot_password:

			// 事件统计 118 点击找回密码（登录页） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c118);

			Intent passowrdIntent = new Intent(this, GetPasswordActivity_.class);
			passowrdIntent.putExtra("loginTarget", LoginTarget.getValue(target));
			passowrdIntent.putExtra("tempemail", emailInput.getText().toString().trim());
			startActivity(passowrdIntent);
			break;
		}
	}

	private DisposeDataListener loginListener = new DisposeDataListener()
	{
		@Override
		public void onSuccess(Object result)
		{
			CommonProgressDialog.getInstance().dismissProgressDialog();
			SharedPreferenceManager.getInstance().putBoolean("isAutoLogon", true);
			SharedPreferenceManager.getInstance().putString("lastLoginName", emailInput.getText().toString().trim());
			SharedPreferenceManager.getInstance().putString("lastLoginPassword",
					passwordInput.getText().toString().trim());
			BuyerApplication.getInstance().setUser((User) result);
			intentToLoginTarget();
			SysManager.boundAccount(((User) result).sessionid);
			ToastUtil.toast(LoginActivity.this, R.string.Login_Successful);
		}

		@Override
		public void onFailure(Object failedReason)
		{
			CommonProgressDialog.getInstance().dismissProgressDialog();
			ResponseCode code = ResponseCode.getCodeValueByTag(failedReason.toString());
			switch (code)
			{
			case CODE_10002:
				ToastUtil.toast(LoginActivity.this, R.string.mic_login_failed_10002);
				break;
			case CODE_10003:
				ToastUtil.toast(LoginActivity.this, R.string.mic_login_failed_10003);
				break;
			case CODE_10004:
				ToastUtil.toast(LoginActivity.this, R.string.mic_login_failed_10004);
				break;
			case CODE_10005:
				ToastUtil.toast(LoginActivity.this, R.string.mic_login_failed_10005);
				break;
			case CODE_10006:
				ToastUtil.toast(LoginActivity.this, R.string.mic_login_failed_10006);
				break;
			default:
				ToastUtil.toast(LoginActivity.this, failedReason);
				break;
			}
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 发送登陆请求
	 */
	private void sendLoginRequest()
	{
		if ("".equals(emailInput.getText().toString().trim()) || "".equals(passwordInput.getText().toString().trim()))
		{
			ToastUtil.toast(LoginActivity.this, R.string.mic_login_input_error);
			return;
		}
		else
		{
			CommonProgressDialog.getInstance().showProgressDialog(this, getString(R.string.mic_loading));
			String userName = emailInput.getText().toString().trim();
			String passWord = passwordInput.getText().toString().trim();
			RequestCenter.userLogin(userName, passWord, loginListener);
		}
	}

}

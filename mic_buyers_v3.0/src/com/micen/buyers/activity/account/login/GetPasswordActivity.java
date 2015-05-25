package com.micen.buyers.activity.account.login;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringArrayRes;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.focustech.common.mob.DisposeDataListener;
import com.focustech.common.util.ToastUtil;
import com.focustech.common.util.Utils;
import com.focustech.common.widget.associatemail.MailBoxAssociateTokenizer;
import com.focustech.common.widget.associatemail.MailBoxAssociateView;
import com.focustech.common.widget.dialog.CommonProgressDialog;
import com.micen.buyers.activity.BaseActivity;
import com.micen.buyers.activity.R;
import com.micen.buyers.http.RequestCenter;
import com.micen.buyers.manager.SysManager;
import com.micen.buyers.module.BaseResponse;
import com.micen.buyers.util.Util;

/**********************************************************
 * @文件名称：GetPasswordActivity.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年3月31日 上午9:24:23
 * @文件描述：找回密码页
 * @修改历史：2015年3月31日创建初始版本
 **********************************************************/
@EActivity
public class GetPasswordActivity extends BaseActivity
{
	@ViewById(R.id.password_clear)
	protected ImageView btnEmailClear;
	@ViewById(R.id.get_password_scroll)
	protected ScrollView scrollView;
	@ViewById(R.id.get_password_layout)
	protected LinearLayout getPasswordLayout;
	@ViewById(R.id.get_password_input)
	protected MailBoxAssociateView emailEditText;
	@ViewById(R.id.btn_get_password)
	protected TextView submitBtn;
	@ViewById(R.id.iv_get_password_status)
	protected ImageView getPasswordStatus;

	@StringArrayRes(R.array.recommend_mailbox)
	protected String[] recommendMailBox;

	private String email;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_password);
	}

	@AfterViews
	protected void initView()
	{
		titleLeftButton.setImageResource(R.drawable.ic_title_back);
		titleText.setText(R.string.password_reset);
		titleRightButton1.setVisibility(View.GONE);
		titleRightButton2.setVisibility(View.GONE);
		titleRightButton3.setVisibility(View.GONE);

		btnEmailClear.setOnClickListener(this);
		submitBtn.setOnClickListener(this);
		titleLeftButton.setOnClickListener(this);
		// 填充数据必须在设置联想参数之前，否则会触发联想
		emailEditText.setText(getIntent().getStringExtra("tempemail"));
		Utils.setEditTextCursorPosition(emailEditText);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.associate_mail_list_item,
				R.id.tv_recommend_mail, recommendMailBox);
		emailEditText.setAdapter(adapter);
		emailEditText.setTokenizer(new MailBoxAssociateTokenizer());
		emailEditText.addTextChangedListener(watcher);
		btnEmailClear.setVisibility("".equals(emailEditText.getText().toString()) ? View.GONE : View.VISIBLE);
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
			btnEmailClear.setVisibility("".equals(emailEditText.getText().toString()) ? View.GONE : View.VISIBLE);
		}
	};

	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		switch (v.getId())
		{
		case R.id.common_title_back_button:
			finish();
			break;
		case R.id.btn_get_password:
			// 事件统计123 提交邮箱（找回密码） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c123);

			getPasswordRequest();
			break;
		case R.id.password_clear:
			emailEditText.setText("");
			break;
		}
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		// 事件统计$10030 找回密码 页面事件
		SysManager.analysis(R.string.a_type_page, R.string.p10030);
	}

	private void getPasswordRequest()
	{
		email = emailEditText.getText().toString().trim();
		if (Utils.isEmpty(email))
		{
			ToastUtil.toast(this, R.string.empty_email);
			return;
		}
		if (!Utils.checkEmail(email))
		{
			ToastUtil.toast(this, R.string.insert_email);
			return;
		}
		CommonProgressDialog.getInstance().showProgressDialog(this, getString(R.string.mic_loading));
		RequestCenter.requestPassword(passwordListener, email);
	}

	private DisposeDataListener passwordListener = new DisposeDataListener()
	{
		@Override
		public void onSuccess(Object obj)
		{
			CommonProgressDialog.getInstance().dismissProgressDialog();
			Util.hideSoftInputMethod(GetPasswordActivity.this, emailEditText);
			if ((BaseResponse) obj != null)
			{
				getPasswordLayout.setVisibility(View.GONE);
				getPasswordStatus.setVisibility(View.VISIBLE);
				getPasswordStatus.setImageResource(R.drawable.ic_get_password_successed);
			}
		}

		@Override
		public void onFailure(Object failedReason)
		{
			CommonProgressDialog.getInstance().dismissProgressDialog();
			Util.hideSoftInputMethod(GetPasswordActivity.this, emailEditText);
			getPasswordLayout.setVisibility(View.GONE);
			getPasswordStatus.setVisibility(View.VISIBLE);
			getPasswordStatus.setImageResource(R.drawable.ic_get_password_failed);
			ToastUtil.toast(GetPasswordActivity.this, failedReason);
		}
	};
}

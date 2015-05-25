package com.micen.buyers.view.mail;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.focustech.common.util.ToastUtil;
import com.focustech.common.widget.dialog.CommonDialog;
import com.focustech.common.widget.dialog.CommonDialog.DialogClickListener;
import com.micen.buyers.activity.R;
import com.micen.buyers.activity.message.MailSendActivity;
import com.micen.buyers.adapter.InquiryRecommendAdapter;
import com.micen.buyers.constant.Constants;
import com.micen.buyers.manager.PopupManager;
import com.micen.buyers.manager.RFQKeyListener;
import com.micen.buyers.manager.SysManager;
import com.micen.buyers.util.Util;

/**********************************************************
 * @文件名称：MailSendBaseFragment.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年5月7日 上午10:07:44
 * @文件描述：发送询盘父Fragment
 * @修改历史：2015年5月7日创建初始版本
 **********************************************************/
@EFragment
public class MailSendBaseFragment extends Fragment implements OnClickListener, TextWatcher
{
	@ViewById(R.id.tv_mail_to_value)
	protected TextView mailTo;
	@ViewById(R.id.tv_mail_from_value)
	protected TextView mailFrom;
	@ViewById(R.id.mail_send_subject_input)
	protected EditText subjectInput;
	@ViewById(R.id.mail_send_subject_clear)
	protected ImageView clearImage;
	@ViewById(R.id.tv_mail_send_shortcut)
	protected TextView btnShortcut;
	@ViewById(R.id.tv_mail_send_content)
	protected EditText contentInput;

	/**
	 * 发邮件公共参数
	 */
	protected String currentEmail;
	protected String gender = "0";
	protected String fullName;
	protected String wantAS;
	protected String wantMICInfo;
	protected String comTelphone1;
	protected String comTelphone2;
	protected String comTelphone3;
	protected String catCode;
	protected String logonStatus;
	protected String senderRole;
	protected String senderHomepage;
	protected String senderCountry;
	protected String companyIdentity;
	protected String memberLevel;
	protected String isBeforePremium;
	protected String quiry_flag;
	protected String operatorId;
	protected String sentToName;
	protected String receiverId;
	protected String productId;
	protected String acquiesceSubject;
	protected String senderCompanyId;
	protected String content;

	protected Bundle bundle;
	protected Intent mIntent;
	private CommonDialog commonDialog;

	public MailSendBaseFragment()
	{

	}

	protected void initView()
	{
		btnShortcut.setOnClickListener(this);
		clearImage.setOnClickListener(this);
		subjectInput.addTextChangedListener(this);
		contentInput.addTextChangedListener(this);
		contentInput.addTextChangedListener(watcher);
		subjectInput.setKeyListener(RFQKeyListener.getInstance(Constants.rfqEditTextDigits));
		contentInput.setKeyListener(RFQKeyListener.getInstance(Constants.rfqEditTextDigits));
		setRightCompoundDrawable(R.drawable.ic_shortcut_arrow, btnShortcut);
	}

	/**
	 * 设置文本控件的右侧填充图
	 * @param drawableResId
	 * @param textView
	 */
	protected void setRightCompoundDrawable(int drawableResId, TextView textView)
	{
		Drawable drawable = getResources().getDrawable(drawableResId);
		drawable.setBounds(0, 0, Util.dip2px(10), Util.dip2px(5));
		textView.setCompoundDrawables(null, null, drawable, null);
	}

	protected void checkPostBtnStatus()
	{
		clearImage.setVisibility(!"".equals(subjectInput.getText().toString()) ? View.VISIBLE : View.GONE);
		if (!"".equals(subjectInput.getText().toString()) && !"".equals(contentInput.getText().toString()))
		{
			((MailSendActivity) getActivity()).onStatusChanged(true);
		}
		else
		{
			((MailSendActivity) getActivity()).onStatusChanged(false);
		}
	}

	/**
	 * 监听字符串是否超过指定长度
	 */
	private TextWatcher watcher = new TextWatcher()
	{
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count)
		{
			if (s.length() >= Constants.REPLAY_MAIL_LENGTH)
			{
				ToastUtil.toast(getActivity(), R.string.replay_mail_length);
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after)
		{
		}

		@Override
		public void afterTextChanged(Editable s)
		{
		}
	};

	public void sendEmail()
	{

	}

	public void back()
	{
		if (contentInput != null)
		{
			if (contentInput.getText().toString().trim().length() > 0)
			{
				commonDialog = new CommonDialog(getActivity(), getString(R.string.exit_inquire),
						this.getString(R.string.confirm), this.getString(R.string.cancel), Util.dip2px(285),
						new DialogClickListener()
						{
							@Override
							public void onDialogClick()
							{
								getActivity().finish();
							}
						});
				commonDialog.show();
			}
			else
			{
				// 事件统计 51 取消回复询盘 点击事件
				SysManager.analysis(R.string.a_type_click, R.string.c51);
				getActivity().finish();
			}
		}
		else
		{
			return;
		}
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.mail_send_subject_clear:
			subjectInput.setText("");
			break;
		case R.id.tv_mail_send_shortcut:
			PopupManager.getInstance().showInquiryRecommendPopup(createPopupView(false));
			break;
		}
	}

	protected RelativeLayout createPopupView(boolean isReply)
	{
		RelativeLayout popupLayout = new RelativeLayout(getActivity());
		popupLayout.addView(createPopupBgView());
		ListView contentView = new ListView(getActivity());
		contentView.setBackgroundResource(R.color.mic_white);
		InquiryRecommendAdapter adapter = new InquiryRecommendAdapter(getActivity(), contentInput, content, isReply);
		int height = adapter.getCount() > 5 ? Util.dip2px(47) * 5 + Util.dip2px(20) : LayoutParams.WRAP_CONTENT;
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, height);
		params.leftMargin = params.rightMargin = Util.dip2px(10);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		contentView.setLayoutParams(params);
		contentView.setAdapter(adapter);
		contentView.setCacheColorHint(0);
		contentView.setSelector(getResources().getDrawable(R.color.transparent));
		contentView.setDivider(getResources().getDrawable(R.color.color_cccccc));
		contentView.setDividerHeight(Util.dip2px(0.5f));
		popupLayout.addView(contentView);
		return popupLayout;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	protected ImageView createPopupBgView()
	{
		ImageView bgImage = new ImageView(getActivity());
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		bgImage.setLayoutParams(params);
		bgImage.setImageResource(R.color.black);
		if (Build.VERSION.SDK_INT > 10)
		{
			bgImage.setAlpha(0.6f);
		}
		else
		{
			bgImage.setAlpha(153);
		}
		bgImage.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				PopupManager.getInstance().dismissPopup();
			}
		});
		return bgImage;
	}

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
		checkPostBtnStatus();
	}

	protected void sqliteTel(String telStr)
	{
		String[] words = telStr.split("-", 3);
		if (words.length == 3) // 如果电话全填
		{
			comTelphone1 = words[0];
			comTelphone2 = words[1];
			comTelphone3 = words[2];
		}
		else
		{
			comTelphone1 = "0";
			comTelphone2 = "0";
			comTelphone3 = "0";
		}
	}

	/**
	 * 将性别转换为对应的数字．
	 * @param mGender
	 */
	protected void transferGender(String mGender)
	{
		if (mGender.equals("0") || mGender.equals("Mr."))
		{
			gender = "0";
		}
		else if (mGender.equals("1") || mGender.equals("Mrs."))
		{
			gender = "1";
		}
		else if (mGender.equals("2") || mGender.equals("Ms."))
		{
			gender = "2";
		}
		else if (mGender.equals("3") || mGender.equals("Miss"))
		{
			gender = "3";
		}
		else
		{
			gender = "0";
		}
	}

}

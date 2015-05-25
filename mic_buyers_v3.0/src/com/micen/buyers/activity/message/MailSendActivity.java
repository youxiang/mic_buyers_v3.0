package com.micen.buyers.activity.message;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.micen.buyers.activity.BaseFragmentActivity;
import com.micen.buyers.activity.R;
import com.micen.buyers.listener.CheckSendButtonStatus;
import com.micen.buyers.module.MailSendTarget;
import com.micen.buyers.view.mail.MailSendBaseFragment;
import com.micen.buyers.view.mail.ReplyFragment_;
import com.micen.buyers.view.mail.SendFragment_;

/**********************************************************
 * @文件名称：MailSendActivity.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年4月10日 下午3:02:51
 * @文件描述：询盘发送页
 * @修改历史：2015年4月10日创建初始版本
 **********************************************************/
@EActivity
public class MailSendActivity extends BaseFragmentActivity implements CheckSendButtonStatus
{
	private MailSendBaseFragment sendBaseFragment;
	private MailSendTarget target;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mail_send);
	}

	@AfterViews
	protected void initView()
	{
		if (getIntent() != null && getIntent().hasExtra("mailSendTarget"))
		{
			target = MailSendTarget.getValueByTag(getIntent().getStringExtra("mailSendTarget"));
		}
		if (findViewById(R.id.mail_send_fragment_container) != null && target != null)
		{
			switch (target)
			{
			case Reply:
				sendBaseFragment = new ReplyFragment_();
				break;
			case SendByCatcode:
			case SendByProductId:
			case SendByCompanyId:
				sendBaseFragment = new SendFragment_();
				break;
			default:
				sendBaseFragment = new SendFragment_();
				break;
			}
			getSupportFragmentManager().beginTransaction().add(R.id.mail_send_fragment_container, sendBaseFragment)
					.commit();
		}
		titleLeftButton.setImageResource(R.drawable.ic_title_back);
		if (target != null)
		{
			switch (target)
			{
			case Reply:
				titleText.setText(R.string.reply);
				break;
			default:
				titleText.setText(R.string.send_inquiry);
				break;
			}
		}
		else
		{
			titleText.setText(R.string.reply);

		}
		titleRightButton1.setVisibility(View.GONE);
		titleRightButton2.setVisibility(View.GONE);
		titleRightButton3.setImageResource(R.drawable.ic_post);

		titleLeftButton.setOnClickListener(this);
		titleRightButton3.setOnClickListener(this);
	}

	@Override
	public void onClick(View view)
	{
		super.onClick(view);
		switch (view.getId())
		{
		case R.id.common_title_back_button:
			sendBaseFragment.back();
			break;
		case R.id.common_title_right_button3:
			sendBaseFragment.sendEmail();
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			sendBaseFragment.back();
		}
		return true;
	}

	@Override
	public void onStatusChanged(boolean isEnabled)
	{
		if (isEnabled)
		{
			// 可以点击
			titleRightButton3.setEnabled(true);
			titleRightButton3.setImageResource(R.drawable.ic_post);
		}
		else
		{
			// 不可以点击
			titleRightButton3.setEnabled(false);
			titleRightButton3.setImageResource(R.drawable.ic_post_gray);
		}
	}
}

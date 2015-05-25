package com.micen.buyers.activity.message;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.focustech.common.mob.DisposeDataListener;
import com.focustech.common.util.ToastUtil;
import com.focustech.common.widget.dialog.CommonDialog;
import com.focustech.common.widget.dialog.CommonDialog.DialogClickListener;
import com.focustech.common.widget.dialog.CommonProgressDialog;
import com.micen.buyers.activity.BaseActivity;
import com.micen.buyers.activity.R;
import com.micen.buyers.http.RequestCenter;
import com.micen.buyers.manager.SysManager;
import com.micen.buyers.module.MailSendTarget;
import com.micen.buyers.module.mail.Mail;
import com.micen.buyers.module.mail.MailDetail;
import com.micen.buyers.module.mail.MailDetails;
import com.micen.buyers.util.BuyerApplication;
import com.micen.buyers.util.Util;
import com.micen.buyers.view.SearchListProgressBar;

/**********************************************************
 * @文件名称：MailDetailActivity.java
 * @文件作者：xiongjiangwei
 * @创建时间：2014年7月14日 下午2:11:41
 * @文件描述：邮件详情页
 * @修改历史：2014年7月14日创建初始版本
 **********************************************************/
@EActivity
public class MailDetailActivity extends BaseActivity
{
	@ViewById(R.id.progressbar_layout)
	protected SearchListProgressBar progressBar;
	@ViewById(R.id.tv_mail_title)
	protected TextView mailTitle;// 邮件标题
	@ViewById(R.id.tv_mail_data)
	protected TextView mailData;
	@ViewById(R.id.tv_mail_from_value)
	protected TextView mailFrom;
	@ViewById(R.id.tv_mail_to_value)
	protected TextView mailTo;
	@ViewById(R.id.tv_mail_content)
	protected TextView mailContent;
	@ViewById(R.id.mail_detail_bottom_layout)
	protected LinearLayout bottomBarLayout;
	@ViewById(R.id.mail_detail_scroll)
	protected ScrollView mailDetailScroll;
	@ViewById(R.id.ic_bottom_bar1)
	protected ImageView iv_repaly;// 回复
	@ViewById(R.id.ic_bottom_bar2)
	protected ImageView iv_delete;// 删除
	@ViewById(R.id.ic_bottom_bar3)
	protected ImageView iv_pre;// 前一页
	@ViewById(R.id.ic_bottom_bar4)
	protected ImageView iv_next;// 后一页

	private int position;
	private String mailId;
	private String mailDate;
	private String action;
	private List<Mail> mailDataList = new ArrayList<Mail>();
	private MailDetail mailDetail;
	private Mail mail;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mail_detail);
	}

	private void initIntent()
	{
		Intent intent = getIntent();
		Bundle data = intent.getExtras();
		position = Integer.parseInt(data.getString("position"));
		action = data.getString("action");
		mailDataList = BuyerApplication.getInstance().getMailDataList();
		mailId = mailDataList.get(position).mailId;
		mailDate = mailDataList.get(position).date;
		mail = mailDataList.get(position);
	}

	private DisposeDataListener mailDetailListener = new DisposeDataListener()
	{
		@Override
		public void onSuccess(Object arg0)
		{
			mailDetailScroll.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			bottomBarLayout.setVisibility(View.VISIBLE);
			MailDetails details = (MailDetails) arg0;
			if (null != details.content)
			{
				mailDetail = details.content;
				mailTitle.setText(mailDetail.subject);
				mailContent.setText(mailDetail.mailContent);
				mailData.setText(Util.formatDateToEn(mailDate));
				mailFrom.setText(mailDetail.senderFullName);
				mailTo.setText(mailDetail.receiverFullName);
			}
			else
			{
				ToastUtil.toast(MailDetailActivity.this, R.string.get_detail_failed);
				finish();
			}
		}

		@Override
		public void onFailure(Object failedReason)
		{
			progressBar.setVisibility(View.GONE);
			bottomBarLayout.setVisibility(View.VISIBLE);
			ToastUtil.toast(MailDetailActivity.this, failedReason);
		}
	};

	// 调用网络接口 根据邮件ID查询邮件详情
	private void startGetMailDetail()
	{
		mailDetailScroll.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
		bottomBarLayout.setVisibility(View.GONE);
		RequestCenter.getMailDetail(mailDetailListener, mailId, "1".equals(action));
	}

	@AfterViews
	protected void initView()
	{
		initIntent();
		titleLeftButton.setImageResource(R.drawable.ic_title_back);
		if ("0".equals(action))
		{
			titleText.setText(R.string.inbox);
			mailFrom.setTextColor(getResources().getColor(R.color.color_4383bf));
			mailTo.setTextColor(getResources().getColor(R.color.color_333333));
			mailFrom.setOnClickListener(this);
		}
		else
		{
			titleText.setText(R.string.sent);
			mailFrom.setTextColor(getResources().getColor(R.color.color_333333));
			mailTo.setTextColor(getResources().getColor(R.color.color_4383bf));
			mailTo.setOnClickListener(this);
		}
		titleRightButton1.setVisibility(View.GONE);
		titleRightButton2.setVisibility(View.GONE);
		titleRightButton3.setVisibility(View.GONE);
		titleLeftButton.setOnClickListener(this);

		iv_delete.setImageResource(R.drawable.ic_delete);
		iv_pre.setImageResource(R.drawable.ic_pre);
		iv_next.setImageResource(R.drawable.ic_next);
		iv_repaly.setImageResource(R.drawable.ic_replay_mail);
		checkImageView();

		startGetMailDetail();
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		// 事件统计$10007 收件箱询盘详情页 页面事件
		SysManager.analysis(R.string.a_type_page, R.string.p10007);
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
		case R.id.bottom_bar_layout1:
			Intent intent = new Intent(this, MailSendActivity_.class);
			intent.putExtra("mailSendTarget", MailSendTarget.getValue(MailSendTarget.Reply));
			intent.putExtra("mailId", mailId);
			intent.putExtra("sendType", 0);
			intent.putExtra("subject", mailTitle.getText());
			intent.putExtra("mailContent", mailDetail.mailContent);
			intent.putExtra("mailDate", mailDate);
			intent.putExtra("action", action);
			if ("1".equals(action)) // 发件箱
			{// 重新发送
				intent.putExtra("receiverCompanyName", mailDetail.senderCompanyName);
				intent.putExtra("receiverFullName", mailDetail.senderFullName);
				intent.putExtra("senderCompany", mailDetail.receiverCompanyName);
				intent.putExtra("senderFullName", mailDetail.receiverFullName);
				intent.putExtra("companyId", mail.receiver.comId);
			}
			else if ("0".equals(action)) // 收件箱
			{
				// 事件统计46 回复询盘（收件箱询盘详情页） 点击事件
				SysManager.analysis(R.string.a_type_click, R.string.c46);
				// 进入回复询盘页面
				intent.putExtra("receiverCompanyName", mailDetail.receiverCompanyName);
				intent.putExtra("receiverFullName", mailDetail.receiverFullName);
				intent.putExtra("senderCompany", mailDetail.senderCompanyName);
				intent.putExtra("senderFullName", mailDetail.senderFullName);
				intent.putExtra("companyId", mail.sender.comId);
			}
			startActivity(intent);
			break;
		case R.id.bottom_bar_layout2:
			CommonDialog dialog = new CommonDialog(this, "Are you sure to delete this message?",
					getString(R.string.mic_ok), getString(R.string.cancel), Util.dip2px(258), new DialogClickListener()
					{
						@Override
						public void onDialogClick()
						{

							// 事件统计47 删除询盘（收件箱询盘详情页） 点击事件
							SysManager.analysis(R.string.a_type_click, R.string.c47);

							deleteMail(mailId);
						}
					});
			dialog.show();
			break;
		case R.id.bottom_bar_layout3:
			if (position > -1)
			{
				// 事件统计 48 查看前后询盘（收件箱询盘详情页） 点击事件
				SysManager.analysis(R.string.a_type_click, R.string.c48);

				position = position - 1;
				mail = mailDataList.get(position);
				checkImageView();
				mailId = mailDataList.get(position).mailId;
				mailDate = mailDataList.get(position).date;
				startGetMailDetail();
			}
			break;
		case R.id.bottom_bar_layout4:
			if (position < mailDataList.size())
			{
				// 事件统计 48 查看前后询盘（收件箱询盘详情页） 点击事件
				SysManager.analysis(R.string.a_type_click, R.string.c48);
				position = position + 1;
				mail = mailDataList.get(position);
				checkImageView();
				/**
				 * 根据position去列表中拿到对应的ID，再去调用请求邮件接口
				 */
				mailId = mailDataList.get(position).mailId;
				mailDate = mailDataList.get(position).date;
				startGetMailDetail();
			}
			break;
		case R.id.tv_mail_from_value:
			// 事件统计45 查看发件人联系信息 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c45);

			Intent fromPersonalInt = new Intent(this, MailPersonalActivity_.class);
			fromPersonalInt.putExtra("action", action);
			fromPersonalInt.putExtra("companyId", mail.sender.comId);
			fromPersonalInt.putExtra("mailId", mailId);
			startActivity(fromPersonalInt);
			break;
		case R.id.tv_mail_to_value:
			Intent toPersonalInt = new Intent(this, MailPersonalActivity_.class);
			toPersonalInt.putExtra("action", action);
			toPersonalInt.putExtra("companyId", mail.receiver.comId);
			toPersonalInt.putExtra("mailId", mailId);
			startActivity(toPersonalInt);
			break;
		}
	}

	private void checkImageView()
	{
		if (mailDataList.size() > 1)
		{
			if (position == mailDataList.size() - 1)
			{
				((View) iv_next.getParent()).setEnabled(false);
				iv_next.setImageResource(R.drawable.ic_next_gray);
				((View) iv_pre.getParent()).setEnabled(true);
				iv_pre.setImageResource(R.drawable.ic_pre);
			}
			else if (position == 0)
			{
				((View) iv_next.getParent()).setEnabled(true);
				iv_next.setImageResource(R.drawable.ic_next);
				((View) iv_pre.getParent()).setEnabled(false);
				iv_pre.setImageResource(R.drawable.ic_pre_gray);
			}
			else
			{
				((View) iv_next.getParent()).setEnabled(true);
				iv_next.setImageResource(R.drawable.ic_next);
				((View) iv_pre.getParent()).setEnabled(true);
				iv_pre.setImageResource(R.drawable.ic_pre);
			}
		}
		else
		{
			((View) iv_next.getParent()).setEnabled(false);
			iv_next.setImageResource(R.drawable.ic_next_gray);
			((View) iv_pre.getParent()).setEnabled(false);
			iv_pre.setImageResource(R.drawable.ic_pre_gray);
		}
	}

	/**
	 * 删除收件箱或者发件箱中的邮件
	 * @param mailId	邮件ID，多个以","隔开
	 */
	public void deleteMail(String mailId)
	{
		CommonProgressDialog.getInstance().showProgressDialog(this, getString(R.string.mic_loading));
		RequestCenter.deleteMail(new DisposeDataListener()
		{
			@Override
			public void onSuccess(Object arg0)
			{
				CommonProgressDialog.getInstance().dismissProgressDialog();
				finish();
			}

			@Override
			public void onFailure(Object failedReason)
			{
				CommonProgressDialog.getInstance().dismissProgressDialog();
				ToastUtil.toast(MailDetailActivity.this, failedReason);
			}
		}, mailId, action);
	}
}
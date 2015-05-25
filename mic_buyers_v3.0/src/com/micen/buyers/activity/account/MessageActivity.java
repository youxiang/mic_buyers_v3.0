package com.micen.buyers.activity.account;

import java.util.ArrayList;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.focustech.common.widget.viewpagerindictor.UnderlinePageIndicator;
import com.focustech.common.widget.viewpagerindictor.UnderlinePageIndicator.ChangeTitle;
import com.micen.buyers.activity.BaseFragmentActivity;
import com.micen.buyers.activity.R;
import com.micen.buyers.activity.message.MailSearchActivity_;
import com.micen.buyers.adapter.CommonFragmentAdapter;
import com.micen.buyers.manager.SysManager;
import com.micen.buyers.view.mail.message.InboxFragment_;
import com.micen.buyers.view.mail.message.MailBaseFragment;
import com.micen.buyers.view.mail.message.SentFragment_;

/**********************************************************
 * @文件名称：MailActivity.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年3月17日 下午1:59:20
 * @文件描述：邮件列表页
 * @修改历史：2015年3月17日创建初始版本
 **********************************************************/
@EActivity
public class MessageActivity extends BaseFragmentActivity implements ChangeTitle
{
	@ViewById(R.id.tv_inbox)
	protected TextView tvInbox;
	@ViewById(R.id.tv_sent)
	protected TextView tvSent;
	@ViewById(R.id.vo_title_line)
	protected UnderlinePageIndicator titleLine;
	@ViewById(R.id.vp_mail)
	protected ViewPager viewPager;

	private CommonFragmentAdapter adapter;

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mail);
	}

	@AfterViews
	protected void initView()
	{
		titleLeftButton.setImageResource(R.drawable.ic_title_back);
		titleText.setText(R.string.mic_message_center);
		titleRightButton1.setImageResource(R.drawable.ic_title_search);
		titleRightButton2.setVisibility(View.GONE);
		titleRightButton3.setVisibility(View.GONE);

		ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
		fragmentList.add(new InboxFragment_());
		fragmentList.add(new SentFragment_());
		adapter = new CommonFragmentAdapter(this, getSupportFragmentManager(), fragmentList);
		viewPager.setAdapter(adapter);
		titleLine.setViewPager(viewPager);
		titleLine.setChangeTitleListener(this);
		titleLine.setFades(false);
		titleLine.setSelectedColor(getResources().getColor(R.color.page_indicator_title_selected));
		changeTitleStatus(0);

		titleLeftButton.setOnClickListener(this);
		titleRightButton1.setOnClickListener(this);

		tvInbox.setOnClickListener(this);
		tvSent.setOnClickListener(this);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		// 事件统计$10006 询盘列表页（VO） 页面事件
		SysManager.analysis(R.string.a_type_page, R.string.p10006);

	}

	@Override
	protected void onRestart()
	{
		super.onRestart();
		((MailBaseFragment) adapter.getItem(viewPager.getCurrentItem())).startRefreshMailList(false);
	}

	@Override
	public void changeTitleStatus(int position)
	{
		((MailBaseFragment) adapter.getItem(position)).cancelDeleteMode();
		switch (position)
		{
		case 0:
			tvInbox.setTextColor(getResources().getColor(R.color.page_indicator_title_selected));
			tvSent.setTextColor(getResources().getColor(R.color.page_indicator_title));
			break;
		default:
			tvInbox.setTextColor(getResources().getColor(R.color.page_indicator_title));
			tvSent.setTextColor(getResources().getColor(R.color.page_indicator_title_selected));
			break;
		}
		/**
		 *只要切换就发送更新请求
		 */
		((MailBaseFragment) adapter.getItem(position)).startRefreshMailList(false);
	}

	@Override
	public void onClick(View view)
	{
		super.onClick(view);
		switch (view.getId())
		{
		case R.id.tv_inbox:
			if (viewPager.getCurrentItem() != 0)
			{
				viewPager.setCurrentItem(0);
			}
			break;
		case R.id.tv_sent:
			// 事件统计 40 查看发件列表（VO） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c40);
			if (viewPager.getCurrentItem() != 1)
			{
				viewPager.setCurrentItem(1);
			}
			break;
		case R.id.common_title_back_button:
			finish();
			break;
		case R.id.common_title_right_button1:
			// 事件统计 38 询盘搜索（VO） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c38);
			Intent intent = new Intent(this, MailSearchActivity_.class);
			intent.putExtra("action", viewPager.getCurrentItem() == 0 ? "0" : "1");
			startActivity(intent);
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			if (adapter.getItem(viewPager.getCurrentItem()) != null)
			{
				if (((MailBaseFragment) adapter.getItem(viewPager.getCurrentItem())).cancelDeleteMode())
				{
					finish();
					return true;
				}
			}
		}
		return false;
	}
}

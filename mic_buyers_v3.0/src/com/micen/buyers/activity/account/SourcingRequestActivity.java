package com.micen.buyers.activity.account;

import java.util.ArrayList;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.focustech.common.widget.viewpagerindictor.UnderlinePageIndicator;
import com.focustech.common.widget.viewpagerindictor.UnderlinePageIndicator.ChangeTitle;
import com.micen.buyers.activity.BaseFragmentActivity;
import com.micen.buyers.activity.R;
import com.micen.buyers.activity.rfq.RFQAddActivity_;
import com.micen.buyers.adapter.CommonFragmentAdapter;
import com.micen.buyers.manager.SysManager;
import com.micen.buyers.view.rfq.ClosedFragment_;
import com.micen.buyers.view.rfq.PendingFragment_;
import com.micen.buyers.view.rfq.RFQBaseFragment;
import com.micen.buyers.view.rfq.RejectedFragment_;

/**********************************************************
 * @文件名称：SourcingRequestActivity.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年3月18日 下午3:10:57
 * @文件描述：常用RFQ列表页
 * @修改历史：2015年3月18日创建初始版本
 **********************************************************/
@EActivity
public class SourcingRequestActivity extends BaseFragmentActivity implements ChangeTitle
{
	@ViewById(R.id.tv_pending)
	protected TextView tvPending;
	@ViewById(R.id.tv_rejected)
	protected TextView tvRejected;
	@ViewById(R.id.tv_closed)
	protected TextView tvClosed;
	@ViewById(R.id.vo_title_line)
	protected UnderlinePageIndicator titleLine;
	@ViewById(R.id.vp_sourcing_request)
	protected ViewPager viewPager;

	private CommonFragmentAdapter adapter;
	private int currentFragmentIndex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sourcing_request);
	}

	@AfterViews
	protected void initView()
	{
		titleLeftButton.setImageResource(R.drawable.ic_title_back);
		titleText.setText(R.string.vo_sourcing);
		titleRightButton1.setVisibility(View.GONE);
		titleRightButton2.setVisibility(View.GONE);
		titleRightButton3.setImageResource(R.drawable.ic_add);

		ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
		fragmentList.add(new PendingFragment_());
		fragmentList.add(new RejectedFragment_());
		fragmentList.add(new ClosedFragment_());
		adapter = new CommonFragmentAdapter(this, getSupportFragmentManager(), fragmentList);
		viewPager.setAdapter(adapter);
		viewPager.setOffscreenPageLimit(2);
		titleLine.setViewPager(viewPager);
		titleLine.setChangeTitleListener(this);
		titleLine.setFades(false);
		titleLine.setSelectedColor(getResources().getColor(R.color.color_e62e2e));
		titleLine.setCurrentItem(1);

		titleLeftButton.setOnClickListener(this);
		titleRightButton3.setOnClickListener(this);

		tvPending.setOnClickListener(this);
		tvRejected.setOnClickListener(this);
		tvClosed.setOnClickListener(this);
	}

	@Override
	public void onRestart()
	{
		super.onRestart();
		if (adapter != null)
		{
			((RFQBaseFragment) adapter.getItem(currentFragmentIndex)).onLoadData();
		}
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		// 事件统计$10010 VO内Sourcing Request列表页 页面事件
		SysManager.analysis(R.string.a_type_page, R.string.p10010);
	}

	@Override
	public void changeTitleStatus(int position)
	{
		switch (position)
		{
		case 0:
			tvPending.setTextColor(getResources().getColor(R.color.page_indicator_title_selected));
			tvRejected.setTextColor(getResources().getColor(R.color.page_indicator_title));
			tvClosed.setTextColor(getResources().getColor(R.color.page_indicator_title));
			break;
		case 2:
			tvPending.setTextColor(getResources().getColor(R.color.page_indicator_title));
			tvRejected.setTextColor(getResources().getColor(R.color.page_indicator_title));
			tvClosed.setTextColor(getResources().getColor(R.color.page_indicator_title_selected));
			break;
		default:
			tvPending.setTextColor(getResources().getColor(R.color.page_indicator_title));
			tvRejected.setTextColor(getResources().getColor(R.color.page_indicator_title_selected));
			tvClosed.setTextColor(getResources().getColor(R.color.page_indicator_title));
			break;
		}
		/**
		 *只要切换就发送更新请求
		 */
		((RFQBaseFragment) adapter.getItem(position)).onLoadData();
		if (currentFragmentIndex != position)
		{
			((RFQBaseFragment) adapter.getItem(currentFragmentIndex)).onClearData();
			currentFragmentIndex = position;
		}
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
		case R.id.common_title_right_button3:
			// 事件统计 20 快速发布RFQ(Sourcing Request列表) 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c20_1);
			startActivity(new Intent(this, RFQAddActivity_.class));
			break;
		case R.id.tv_pending:
			if (viewPager.getCurrentItem() != 0)
			{
				// 事件统计56 Sourcing Request列表切换 点击事件
				SysManager.analysis(R.string.a_type_click, R.string.c56);
				viewPager.setCurrentItem(0);
			}
			break;
		case R.id.tv_rejected:
			if (viewPager.getCurrentItem() != 1)
			{
				// 事件统计56 Sourcing Request列表切换 点击事件
				SysManager.analysis(R.string.a_type_click, R.string.c56);
				viewPager.setCurrentItem(1);
			}
			break;
		case R.id.tv_closed:
			if (viewPager.getCurrentItem() != 2)
			{
				// 事件统计56 Sourcing Request列表切换 点击事件
				SysManager.analysis(R.string.a_type_click, R.string.c56);
				viewPager.setCurrentItem(2);
			}
			break;
		}
	}
}

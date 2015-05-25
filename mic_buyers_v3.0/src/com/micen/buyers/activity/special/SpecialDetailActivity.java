package com.micen.buyers.activity.special;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.focustech.common.mob.DisposeDataListener;
import com.focustech.common.util.Utils;
import com.focustech.common.widget.PagerSlidingTabStrip;
import com.micen.buyers.activity.BaseFragmentActivity;
import com.micen.buyers.activity.R;
import com.micen.buyers.activity.rfq.RFQAddActivity_;
import com.micen.buyers.adapter.SpecialDetailAdapter;
import com.micen.buyers.http.RequestCenter;
import com.micen.buyers.manager.SysManager;
import com.micen.buyers.module.special.detail.SpecialDetail;
import com.micen.buyers.util.ImageUtil;
import com.micen.buyers.view.SearchListProgressBar;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

/**********************************************************
 * @文件名称：SpecialDetailActivity.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年4月27日 下午4:10:59
 * @文件描述：专题详情页
 * @修改历史：2015年4月27日创建初始版本
 **********************************************************/
@EActivity
public class SpecialDetailActivity extends BaseFragmentActivity implements DisposeDataListener
{
	@ViewById(R.id.progressbar_layout)
	protected SearchListProgressBar progressBar;
	@ViewById(R.id.special_detail_banner)
	protected ImageView specialDetailBanner;
	@ViewById(R.id.special_detail_pager_tab)
	protected PagerSlidingTabStrip specialViewPagerTab;
	@ViewById(R.id.special_detail_pager)
	protected ViewPager viewPager;
	@ViewById(R.id.special_detail_layout)
	protected RelativeLayout specialDetailLayout;
	@ViewById(R.id.special_detail_postrfq)
	protected ImageView specialPostRFQ;

	@Extra("specialId")
	protected String specialId;

	private SpecialDetailAdapter adapter;
	private LayoutTransition mTransitioner;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_special_detail);
	}

	@AfterViews
	protected void initView()
	{
		titleLeftButton.setImageResource(R.drawable.ic_title_back);
		titleRightButton1.setVisibility(View.GONE);
		titleRightButton2.setVisibility(View.GONE);
		titleRightButton3.setVisibility(View.GONE);
		titleLeftButton.setOnClickListener(this);
		specialPostRFQ.setOnClickListener(this);

		specialViewPagerTab.setIndicatorHeight(Utils.toDip(this, 2));
		specialViewPagerTab.setIndicatorColorResource(R.color.color_e62e2e);
		specialViewPagerTab.setAllCaps(false);
		specialViewPagerTab.setDividerColorResource(R.color.transparent);
		specialViewPagerTab.setTabPaddingLeftRight(Utils.toDip(this, 15));

		progressBar.setVisibility(View.VISIBLE);
		specialDetailLayout.setVisibility(View.GONE);
		RequestCenter.getSpecialDetail(this, specialId);
	}

	@Override
	public void onSuccess(Object obj)
	{
		progressBar.setVisibility(View.GONE);
		specialDetailLayout.setVisibility(View.VISIBLE);
		SpecialDetail detail = (SpecialDetail) obj;
		if (detail.ai == null)
			return;
		titleText.setText(detail.ai.activityName);
		adapter = new SpecialDetailAdapter(getSupportFragmentManager(), detail.ai);
		viewPager.setAdapter(adapter);
		specialViewPagerTab.setViewPager(viewPager);

		if (!Utils.isEmpty(detail.ai.bannerPicUrl))
		{
			specialDetailBanner.setVisibility(View.VISIBLE);
			ImageUtil.getImageLoader().displayImage(detail.ai.bannerPicUrl, specialDetailBanner,
					ImageUtil.getRecommendImageOptions(), new ImageLoadingListener()
					{
						@Override
						public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage)
						{
							handler.sendEmptyMessageDelayed(0, 3000);
						}

						@Override
						public void onLoadingCancelled(String arg0, View arg1)
						{

						}

						@Override
						public void onLoadingFailed(String arg0, View arg1, FailReason arg2)
						{

						}

						@Override
						public void onLoadingStarted(String arg0, View arg1)
						{

						}

					});
		}
	}

	@Override
	public void onFailure(Object failedReason)
	{
		finish();
	}

	private Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			mTransitioner = new LayoutTransition();
			specialDetailLayout.setLayoutTransition(mTransitioner);
			ObjectAnimator animOut = ObjectAnimator.ofFloat(null, "translationY", 0f, -specialDetailBanner.getHeight())
					.setDuration(mTransitioner.getDuration(LayoutTransition.DISAPPEARING));
			mTransitioner.setAnimator(LayoutTransition.DISAPPEARING, animOut);
			animOut.addListener(new AnimatorListenerAdapter()
			{
				public void onAnimationEnd(Animator anim)
				{
					View view = (View) ((ObjectAnimator) anim).getTarget();
					view.setTranslationY(0f);
				}
			});
			mTransitioner.setDuration(500);
			specialDetailBanner.setVisibility(View.GONE);
		};
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
		case R.id.special_detail_postrfq:
			// 事件统计17 快速发布RFQ（专题） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c17);
			startActivity(new Intent(this, RFQAddActivity_.class));
			break;
		}
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		// 事件统计 $10003专题 专题 页面事件
		SysManager.analysis(R.string.a_type_page, R.string.p10003);
	}

}

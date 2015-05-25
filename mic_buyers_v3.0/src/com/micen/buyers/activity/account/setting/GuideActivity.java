package com.micen.buyers.activity.account.setting;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;

import com.micen.buyers.activity.BaseFragmentActivity;
import com.micen.buyers.activity.R;
import com.micen.buyers.activity.home.HomeActivity_;
import com.micen.buyers.adapter.GuidePagerAdapter;
import com.micen.buyers.db.SharedPreferenceManager;

/**********************************************************
 * @文件名称：GuideActivity.java
 * @文件作者：xiongjiangwei
 * @创建时间：2014年9月30日 上午11:22:48
 * @文件描述：新手指引页
 * @修改历史：2014年9月30日创建初始版本
 **********************************************************/
@EActivity
public class GuideActivity extends BaseFragmentActivity implements OnClickListener
{
	@ViewById(R.id.guide_viewpager)
	protected ViewPager viewPager;
	@ViewById(R.id.btn_guide_skip)
	protected Button btnSkip;

	@Extra("guideTarget")
	protected String target;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
	}

	@AfterViews
	protected void initView()
	{
		btnSkip.setOnClickListener(this);
		GuidePagerAdapter adapter = new GuidePagerAdapter(this);
		adapter.setBtStartListener(this);
		viewPager.setAdapter(adapter);
	}

	@Override
	public void onClick(View v)
	{
		start();
	}

	private void start()
	{
		SharedPreferenceManager.getInstance().putBoolean("isFirst", false);
		if (target == null || !"setting".equals(target))
		{
			startActivity(new Intent(GuideActivity.this, HomeActivity_.class));
			finish();
		}
		else
		{
			finish();
		}

	}

}

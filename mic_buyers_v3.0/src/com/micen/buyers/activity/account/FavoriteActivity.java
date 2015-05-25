package com.micen.buyers.activity.account;

import java.util.ArrayList;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.focustech.common.widget.viewpagerindictor.UnderlinePageIndicator;
import com.focustech.common.widget.viewpagerindictor.UnderlinePageIndicator.ChangeTitle;
import com.micen.buyers.activity.BaseFragmentActivity;
import com.micen.buyers.activity.R;
import com.micen.buyers.adapter.CommonFragmentAdapter;
import com.micen.buyers.manager.SysManager;
import com.micen.buyers.view.favorite.CategoryFragment_;
import com.micen.buyers.view.favorite.FavoriteBaseFragment;
import com.micen.buyers.view.favorite.ProductFragment_;
import com.micen.buyers.view.favorite.SupplierFragment_;

/**********************************************************
 * @文件名称：FavoriteActivity.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年3月24日 上午11:03:52
 * @文件描述：个人收藏页
 * @修改历史：2015年3月24日创建初始版本
 **********************************************************/
@EActivity
public class FavoriteActivity extends BaseFragmentActivity implements ChangeTitle
{
	@ViewById(R.id.tv_product)
	protected TextView tvProduct;
	@ViewById(R.id.tv_company)
	protected TextView tvCompany;
	@ViewById(R.id.tv_category)
	protected TextView tvCategory;
	@ViewById(R.id.favourite_title_line)
	protected UnderlinePageIndicator titleLine;
	@ViewById(R.id.favourite_viewpager)
	protected ViewPager viewPager;

	private CommonFragmentAdapter adapter;
	private int currentItem = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favourite);
	}

	@Override
	protected void onRestart()
	{
		super.onRestart();
		((FavoriteBaseFragment) adapter.getItem(viewPager.getCurrentItem())).onLoadData();
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		// 事件统计 $10032 收藏列表页 页面事件
		SysManager.analysis(R.string.a_type_page, R.string.p10032);
	}

	@AfterViews
	protected void initView()
	{
		currentItem = getIntent().getIntExtra("currentItem", 0);
		titleLeftButton.setImageResource(R.drawable.ic_title_back);
		titleText.setText(R.string.mic_home_favoriate);
		titleRightButton1.setVisibility(View.GONE);
		titleRightButton2.setVisibility(View.GONE);
		titleRightButton3.setVisibility(View.GONE);

		ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
		fragmentList.add(new ProductFragment_());
		fragmentList.add(new SupplierFragment_());
		fragmentList.add(new CategoryFragment_());
		adapter = new CommonFragmentAdapter(this, getSupportFragmentManager(), fragmentList);
		viewPager.setAdapter(adapter);
		titleLine.setViewPager(viewPager);
		titleLine.setChangeTitleListener(this);
		titleLine.setFades(false);
		titleLine.setSelectedColor(getResources().getColor(R.color.color_e62e2e));
		titleLine.setCurrentItem(currentItem);
		if (currentItem == 0)
		{
			changeTitleStatus(currentItem);
		}

		titleLeftButton.setOnClickListener(this);
		tvProduct.setOnClickListener(this);
		tvCompany.setOnClickListener(this);
		tvCategory.setOnClickListener(this);
	}

	@Override
	public void changeTitleStatus(int position)
	{
		switch (position)
		{
		case 0:
			tvProduct.setTextColor(getResources().getColor(R.color.page_indicator_title_selected));
			tvCompany.setTextColor(getResources().getColor(R.color.page_indicator_title));
			tvCategory.setTextColor(getResources().getColor(R.color.page_indicator_title));
			break;
		case 1:
			tvCompany.setTextColor(getResources().getColor(R.color.page_indicator_title_selected));
			tvProduct.setTextColor(getResources().getColor(R.color.page_indicator_title));
			tvCategory.setTextColor(getResources().getColor(R.color.page_indicator_title));
			break;
		case 2:
			tvCategory.setTextColor(getResources().getColor(R.color.page_indicator_title_selected));
			tvProduct.setTextColor(getResources().getColor(R.color.page_indicator_title));
			tvCompany.setTextColor(getResources().getColor(R.color.page_indicator_title));
			break;
		}
		((FavoriteBaseFragment) adapter.getItem(position)).onLoadData();
	}

	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		switch (v.getId())
		{
		case R.id.common_title_back_button:
			finish();
			break;
		case R.id.tv_product:
			if (viewPager.getCurrentItem() != 0)
			{
				viewPager.setCurrentItem(0);
			}
			break;
		case R.id.tv_company:
			if (viewPager.getCurrentItem() != 1)
			{
				viewPager.setCurrentItem(1);
			}
			break;
		case R.id.tv_category:
			if (viewPager.getCurrentItem() != 2)
			{
				viewPager.setCurrentItem(2);
			}
			break;
		}
	}
}

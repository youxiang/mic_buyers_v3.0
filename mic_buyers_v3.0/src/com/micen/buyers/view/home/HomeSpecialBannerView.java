package com.micen.buyers.view.home;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.focustech.common.widget.viewpagerindictor.UnderlinePageIndicator;
import com.micen.buyers.activity.R;
import com.micen.buyers.adapter.SpecialBannerAdapter;
import com.micen.buyers.module.special.SpecialContent;
import com.micen.buyers.view.HomeBannerViewPager;

/**********************************************************
 * @文件名称：HomeSpecialBannerView.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年5月7日 下午6:50:37
 * @文件描述：首页专题Banner布局
 * @修改历史：2015年5月7日创建初始版本
 **********************************************************/
public class HomeSpecialBannerView extends RelativeLayout
{
	private HomeBannerViewPager bannerViewPager;
	private SpecialBannerAdapter bannerAdapter;
	private UnderlinePageIndicator bannerIndicator;
	private ArrayList<SpecialContent> dataList;
	private Activity activity;

	public HomeSpecialBannerView(Activity activity, ArrayList<SpecialContent> dataList)
	{
		super(activity);
		this.activity = activity;
		this.dataList = dataList;
		initView();
	}

	private void initView()
	{
		LayoutInflater.from(getContext()).inflate(R.layout.home_special_banner_layout, this);
		bannerViewPager = (HomeBannerViewPager) findViewById(R.id.home_special_banner_viewpager);
		bannerIndicator = (UnderlinePageIndicator) findViewById(R.id.home_special_banner_point);

		bannerAdapter = new SpecialBannerAdapter(activity, dataList);
		bannerViewPager.setAdapter(bannerAdapter);
		if (dataList != null && dataList.size() > 1)
		{
			bannerIndicator.setVisibility(View.VISIBLE);
			bannerIndicator.setFades(false);
			bannerIndicator.setSelectedColor(getResources().getColor(R.color.color_e62e2e));
			bannerIndicator.setViewPager(bannerViewPager);
		}
		else
		{
			bannerIndicator.setVisibility(View.GONE);
		}

	}

}

package com.micen.buyers.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.micen.buyers.module.special.detail.SpecialDetailContent;
import com.micen.buyers.view.SpecialDetailFragment;

/**********************************************************
 * @文件名称：SpecialDetailAdapter.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年4月28日 下午4:22:17
 * @文件描述：专题详情适配器
 * @修改历史：2015年4月28日创建初始版本
 **********************************************************/
public class SpecialDetailAdapter extends FragmentPagerAdapter
{
	private SpecialDetailContent specialDetail;

	public SpecialDetailAdapter(FragmentManager fm, SpecialDetailContent specialDetail)
	{
		super(fm);
		this.specialDetail = specialDetail;
	}

	@Override
	public CharSequence getPageTitle(int position)
	{
		return specialDetail.getAllMenuName().get(position);
	}

	@Override
	public Fragment getItem(int position)
	{
		return SpecialDetailFragment.newInstance(specialDetail.activityMenuList.get(position).activityProductInfoList);
	}

	@Override
	public int getCount()
	{
		return specialDetail.getAllMenuName().size();
	}

}

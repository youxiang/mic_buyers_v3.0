package com.micen.buyers.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

/**********************************************************
 * @文件名称：CommonFragmentAdapter.java
 * @文件作者：xiongjiangwei
 * @创建时间：2014年10月15日 下午4:24:04
 * @文件描述：公共Fragment适配器
 * @修改历史：2014年10月15日创建初始版本
 **********************************************************/
public class CommonFragmentAdapter extends FragmentStatePagerAdapter
{
	private ArrayList<Fragment> fragmentList;

	public CommonFragmentAdapter(Activity activity, FragmentManager fm, ArrayList<Fragment> fragmentList)
	{
		super(fm);
		this.fragmentList = fragmentList;
	}

	@Override
	public Fragment getItem(int arg0)
	{
		return fragmentList.get(arg0);
	}

	@Override
	public int getCount()
	{
		return fragmentList.size();
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object)
	{
		super.destroyItem(container, position, object);
	}
}

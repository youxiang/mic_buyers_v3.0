package com.micen.buyers.view.refine;

import java.util.ArrayList;

import com.micen.buyers.module.sift.SearchProperty;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**********************************************************
 * @文件名称：BaseRefineFragment.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年3月23日 上午9:27:19
 * @文件描述：塞选Fragment的父Fragment
 * @修改历史：2015年3月23日创建初始版本
 **********************************************************/
public class BaseRefineFragment extends Fragment
{
	protected View contentView;
	protected Activity mContext;
	protected SiftAllListener listener;

	public BaseRefineFragment()
	{
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.mContext = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	/**
	 * 用于重置数据，通知界面变更
	 */
	public void resetData()
	{

	}

	public interface SiftAllListener
	{
		public void sift(int type, String siftString);

		/**
		 *  用于变更筛选里面的Attribute
		 * @param attribute
		 * @author xuliucheng
		 */
		public void setAttributeData(ArrayList<SearchProperty> attribute);

		/**
		 * 用于获取已经设置的值
		 * @param key
		 * @return
		 */
		public String getValue(String key);
	}
}

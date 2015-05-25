package com.focustech.common.widget;

import java.lang.reflect.Field;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.Scroller;

/**********************************************************
 * @文件名称：ControlDurationViewPager.java
 * @文件作者：xiongjiangwei
 * @创建时间：2014年7月25日 下午2:13:48
 * @文件描述：可控制滑动速度的ViewPager
 * @修改历史：2014年7月25日创建初始版本
 **********************************************************/
public class ControlDurationViewPager extends ViewPager
{
	private int mDuration = 1500;

	public ControlDurationViewPager(Context context)
	{
		super(context);
	}

	public ControlDurationViewPager(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	/**
	 * 设置滑动速度
	 * @param duration
	 */
	public void setDuration(int duration)
	{
		this.mDuration = duration;
		resetScroller();
	}

	/**
	 * 获取当前的滑动速度
	 * @return
	 */
	public int getDuration()
	{
		return mDuration;
	}

	private void resetScroller()
	{
		try
		{
			Field mScroller = ViewPager.class.getDeclaredField("mScroller");
			mScroller.setAccessible(true);
			ViewPagerScroller scroller = new ViewPagerScroller(getContext());
			mScroller.set(this, scroller);
		}
		catch (NoSuchFieldException e)
		{
			e.printStackTrace();
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}

	private final class ViewPagerScroller extends Scroller
	{
		public ViewPagerScroller(Context context)
		{
			super(context);
		}

		@Override
		public void startScroll(int startX, int startY, int dx, int dy, int duration)
		{
			// Ignore received duration, use fixed one instead
			super.startScroll(startX, startY, dx, dy, mDuration);
		}

		@Override
		public void startScroll(int startX, int startY, int dx, int dy)
		{
			// Ignore received duration, use fixed one instead
			super.startScroll(startX, startY, dx, dy, mDuration);
		}
	}

}

package com.micen.buyers.view;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.focustech.common.widget.ControlDurationViewPager;
import com.micen.buyers.util.Util;

/**********************************************************
 * @文件名称：HomeBannerViewPager.java
 * @文件作者：xiongjiangwei
 * @创建时间：2014年7月28日 上午11:42:59
 * @文件描述：首页Banner自定义控件
 * @修改历史：2014年7月28日创建初始版本
 **********************************************************/
public class HomeBannerViewPager extends ControlDurationViewPager
{
	private Timer timer;
	private int timerIndex = 0;
	private int currentPositon = 0;
	private boolean flag = true;
	private float mLastMotionY;
	private float mLastMotionX;

	public HomeBannerViewPager(Context context)
	{
		super(context);
		createTimer();
		setDuration(1000);
	}

	public HomeBannerViewPager(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		createTimer();
		setDuration(1000);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		final float x = ev.getX();
		final float y = ev.getY();
		switch (ev.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			setPullToScrollViewStatus(true);
			flag = true;
			mLastMotionX = x;
			mLastMotionY = y;
			mHandler.sendEmptyMessage(1);
			break;
		case MotionEvent.ACTION_MOVE:
			if (flag)
			{
				if (Math.abs(y - mLastMotionY) > Util.dip2px(20) && Math.abs(x - mLastMotionX) < Util.dip2px(5))
				{
					flag = false;
					setPullToScrollViewStatus(false);
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			setPullToScrollViewStatus(false);
			mHandler.sendEmptyMessage(0);
			break;
		case MotionEvent.ACTION_CANCEL:
			setPullToScrollViewStatus(false);
			mHandler.sendEmptyMessage(0);
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event)
	{
		return super.onInterceptTouchEvent(event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		return super.onTouchEvent(event);
	}

	/**
	 * 设置首页下拉ScrollView控件的状态，解决与Banner滑动事件冲突
	 * @param disallowIntercept
	 */
	private void setPullToScrollViewStatus(boolean disallowIntercept)
	{
		getParent().getParent().getParent().getParent().getParent()
				.requestDisallowInterceptTouchEvent(disallowIntercept);
	}

	/**
	 * 取消定时器
	 */
	public void cancelTimer()
	{
		if (timer != null)
		{
			timer.cancel();
			timer = null;
		}
	}

	/**
	 * 创建定时器
	 */
	private void createTimer()
	{
		if (timer == null)
		{
			timer = new Timer();
			timer.schedule(new TimerTask()
			{
				@Override
				public void run()
				{
					mHandler.sendEmptyMessage(2);
					timerIndex++;
					if (timerIndex == getAdapter().getCount())
					{
						timerIndex = 0;
					}
				}
			}, 5000, 5000);
		}
	}

	public Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case 0:// 滑动或者轮播切换
				timerIndex = getCurrentItem();
				createTimer();
				break;
			case 1:// 当手触摸后停止定时器
				cancelTimer();
				break;
			case 2:// 定时器轮播广告
				selectChildView();
				break;
			}

		};
	};

	/**
	 * 跳转到指定子元素
	 */
	public void selectChildView()
	{
		currentPositon = getCurrentItem();
		if (currentPositon == getAdapter().getCount() - 1)
		{
			setCurrentItem(0);
		}
		else
		{
			setCurrentItem(currentPositon + 1);
		}
	}
}

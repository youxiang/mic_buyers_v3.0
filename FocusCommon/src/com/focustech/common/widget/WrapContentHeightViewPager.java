package com.focustech.common.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**********************************************************
 * @文件名称：ControlDurationViewPager.java
 * @文件作者：xiongjiangwei
 * @创建时间：2014年7月25日 下午2:13:48
 * @文件描述：可控制滑动速度的ViewPager
 * @修改历史：2014年7月25日创建初始版本
 **********************************************************/
public class WrapContentHeightViewPager extends ViewPager
{

	public WrapContentHeightViewPager(Context context)
	{
		super(context);
	}

	public WrapContentHeightViewPager(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int height = 0;
		// 下面遍历所有child的高度
		for (int i = 0; i < getChildCount(); i++)
		{
			View child = getChildAt(i);
			child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			int h = child.getMeasuredHeight();
			if (h > height) // 采用最大的view的高度。
				height = h;
		}

		heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}

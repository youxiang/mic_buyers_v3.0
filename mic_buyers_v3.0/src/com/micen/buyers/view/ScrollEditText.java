package com.micen.buyers.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.ScrollView;

public class ScrollEditText extends EditText
{
	private ScrollView parentScrollView;

	public ScrollEditText(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public ScrollEditText(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public ScrollEditText(Context context)
	{
		super(context);
	}

	public void setParentScrollView(ScrollView parentScrollView)
	{
		this.parentScrollView = parentScrollView;
	}

	int currentY;

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (parentScrollView == null)
		{
			return super.onTouchEvent(event);
		}
		switch (event.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			// 将父scrollview的滚动事件拦截
			currentY = (int) event.getY();
			setParentScrollAble(false);
			return super.onTouchEvent(event);
		case MotionEvent.ACTION_UP:
			// 把滚动事件恢复给父Scrollview
			setParentScrollAble(true);
			break;
		case MotionEvent.ACTION_CANCEL:
			// 把滚动事件恢复给父Scrollview
			setParentScrollAble(true);
			break;
		}
		return super.onTouchEvent(event);
	}

	/** 
	 * 是否把滚动事件交给父scrollview 
	 * @param flag 
	 */
	private void setParentScrollAble(boolean flag)
	{
		parentScrollView.requestDisallowInterceptTouchEvent(!flag);
	}

}

package com.micen.buyers.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

public class BorderScrollView extends ScrollView
{
	private OnBorderListener onBorderListener;
	private OnTouchListener onBorderTouchListener;
	private View contentView;

	public BorderScrollView(Context context)
	{
		super(context);
		mGestureDetector = new GestureDetector(context,new YScrollDetector());
	}

	public BorderScrollView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		mGestureDetector = new GestureDetector(context,new YScrollDetector());
	}

	public BorderScrollView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		mGestureDetector = new GestureDetector(context,new YScrollDetector());
	}

	public void setOnBorderListener(final OnBorderListener onBorderListener)
	{
		this.onBorderListener = onBorderListener;
		if (onBorderListener == null)
		{
			this.onBorderTouchListener = null;
			return;
		}

		if (contentView == null)
		{
			contentView = getChildAt(0);
		}
		this.onBorderTouchListener = new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				switch (event.getAction())
				{
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_MOVE:
					doOnBorderListener();
					break;
				}
				return false;
			}

		};
		super.setOnTouchListener(onBorderTouchListener);
	}

	/**
	 * OnBorderListener, Called when scroll to top or bottom
	 */
	public static interface OnBorderListener
	{
		/**
		 * Called when scroll to bottom
		 */
		public void onBottom();

		/**
		 * Called when scroll to top
		 */
		public void onTop();

		public void onMiddle();
	}
	
	private GestureDetector mGestureDetector;
	View.OnTouchListener mGestureListener;

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
		return super.onInterceptTouchEvent(ev)
				&& mGestureDetector.onTouchEvent(ev);
	}

	class YScrollDetector extends SimpleOnGestureListener
	{
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY)
		{
			if (Math.abs(distanceY) > Math.abs(distanceX))
			{
				return true;
			}
			return false;
		}
	}

	@Override
	public void setOnTouchListener(final OnTouchListener l)
	{
		OnTouchListener onTouchListener = new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				if (onBorderTouchListener != null)
				{
					onBorderTouchListener.onTouch(v, event);
				}
				return l.onTouch(v, event);
			}
		};
		super.setOnTouchListener(onTouchListener);
	}

	private void doOnBorderListener()
	{
		if (contentView != null && contentView.getMeasuredHeight() <= getScrollY() + getHeight())
		{
			if (onBorderListener != null)
			{
				onBorderListener.onBottom();
			}
		}
		else if (getScrollY() == 0)
		{
			if (onBorderListener != null)
			{
				onBorderListener.onTop();
			}
		}
		else
		{
			if (onBorderListener != null)
			{
				onBorderListener.onMiddle();
			}
		}
	}
}

package com.micen.buyers.widget.other;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class NotifyScrollView extends ScrollView
{
	public interface OnScrollChangedListener
	{
		void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt);
	}

	private OnScrollChangedListener mOnScrollChangedListener;

	public NotifyScrollView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt)
	{
		super.onScrollChanged(l, t, oldl, oldt);
		if (mOnScrollChangedListener != null)
		{
			mOnScrollChangedListener.onScrollChanged(this, l, t, oldl, oldt);
		}
	}

	public void setOnScrollChangedListener(OnScrollChangedListener listener)
	{
		mOnScrollChangedListener = listener;
	}

}

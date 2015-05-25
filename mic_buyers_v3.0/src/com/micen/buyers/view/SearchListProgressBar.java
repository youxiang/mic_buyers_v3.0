package com.micen.buyers.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.micen.buyers.activity.R;

/**********************************************************
 * @文件名称：SearchListProgressBar.java
 * @文件作者：renzhiqiang
 * @创建时间：2014年8月14日 上午8:54:20
 * @文件描述：拉取数据列表时的进度条
 * @修改历史：2014年8月14日创建初始版本
 **********************************************************/
public class SearchListProgressBar extends RelativeLayout
{
	private RelativeLayout viewGroup;

	public SearchListProgressBar(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		initView(context);
	}

	public SearchListProgressBar(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initView(context);
	}

	public SearchListProgressBar(Context context)
	{
		super(context);
		initView(context);
	}

	private void initView(Context context)
	{
		viewGroup = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.data_loading_layout, null);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		addView(viewGroup, params);
	}
}

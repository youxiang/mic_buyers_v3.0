package com.micen.buyers.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.micen.buyers.util.Util;

public class ImageBrowserAdapter extends PagerAdapter
{
	private Context context;
	private ArrayList<String> imageList;
	private String isShowImage;

	public ImageBrowserAdapter(Context context, ArrayList<String> imageList, String isShowImage)
	{
		this.context = context;
		this.imageList = imageList;
		this.isShowImage = isShowImage;
	}

	@Override
	public void destroyItem(View arg0, int position, Object arg2)
	{
		((ViewPager) arg0).removeView((View) arg2);
	}

	@Override
	public void finishUpdate(View arg0)
	{
	}

	@Override
	public int getCount()
	{
		return imageList.size();
	}

	@Override
	public Object instantiateItem(View arg0, final int position)
	{
		ImageView iv = new ImageView(context);
		iv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		Util.showFitImage(isShowImage, imageList.get(position).trim(), iv);
		((ViewPager) arg0).addView(iv, 0);
		return iv;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1)
	{
		return arg0 == (arg1);
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1)
	{
	}

	@Override
	public Parcelable saveState()
	{
		return null;
	}

	@Override
	public void startUpdate(View arg0)
	{
	}
}

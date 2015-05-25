package com.micen.buyers.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.focustech.common.util.Utils;
import com.micen.buyers.activity.R;
import com.micen.buyers.manager.IntentManager;
import com.micen.buyers.manager.SysManager;
import com.micen.buyers.module.special.SpecialContent;
import com.micen.buyers.util.ImageUtil;
import com.micen.buyers.util.Util;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

/**********************************************************
 * @文件名称：SpecialBannerAdapter.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年4月21日 下午2:35:03
 * @文件描述：专题适配器
 * @修改历史：2015年4月21日创建初始版本
 **********************************************************/
public class SpecialBannerAdapter extends PagerAdapter
{
	private Activity activity;
	private LayoutParams params;
	private ImageView imageView;
	private ArrayList<SpecialContent> dataList;
	private boolean isViewPagerInit = false;

	private SpecialContent content;

	public SpecialBannerAdapter(Activity activity, ArrayList<SpecialContent> dataList)
	{
		this.activity = activity;
		this.dataList = dataList;
	}

	@Override
	public int getCount()
	{
		return dataList.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1)
	{
		return arg0 == ((View) arg1);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object)
	{
		((ViewPager) container).removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position)
	{
		content = dataList.get(position);
		params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		imageView = new ImageView(activity);
		imageView.setLayoutParams(params);
		ImageUtil.getImageLoader().displayImage(content.adsContent.contentPic1, imageView,
				ImageUtil.getSafeImageNoStubOptions(), new ImageLoadingListener()
				{
					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage)
					{
						// 当Banner图片加载完成，根据图片尺寸重设ViewPager的宽高，以达到图片不变形的效果
						if (!isViewPagerInit && view.getParent() != null)
						{
							final float scale = Utils.getWindowWidthPix(activity)
									/ (loadedImage.getWidth() * Util.getWindowDensity());
							final LinearLayout.LayoutParams viewPagerParams = new LinearLayout.LayoutParams(
									LayoutParams.MATCH_PARENT,
									(int) (loadedImage.getHeight() * Util.getWindowDensity() * scale));
							((View) view.getParent()).setLayoutParams(viewPagerParams);
							isViewPagerInit = true;
						}
						((ImageView) view).setImageDrawable(new BitmapDrawable(activity.getResources()));
						view.setBackgroundDrawable(new BitmapDrawable(activity.getResources(), loadedImage));
					}

					@Override
					public void onLoadingCancelled(String arg0, View arg1)
					{

					}

					@Override
					public void onLoadingFailed(String arg0, View arg1, FailReason arg2)
					{

					}

					@Override
					public void onLoadingStarted(String arg0, View arg1)
					{

					}

				});
		imageView.setOnClickListener(click);
		imageView.setTag(content);
		((ViewPager) container).addView(imageView, 0);
		return imageView;
	}

	@Override
	public void finishUpdate(View arg0)
	{
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

	@Override
	public int getItemPosition(Object object)
	{
		return POSITION_NONE;
	}

	private OnClickListener click = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			// 事件统计 3 首页广告（首页） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c3);
			IntentManager.specialTarget(activity, (SpecialContent) v.getTag());

		}
	};
}

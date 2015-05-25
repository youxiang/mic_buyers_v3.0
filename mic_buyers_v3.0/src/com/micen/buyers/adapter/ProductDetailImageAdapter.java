package com.micen.buyers.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.micen.buyers.activity.R;
import com.micen.buyers.activity.productdetail.ImageBrowserActivity_;
import com.micen.buyers.manager.SysManager;
import com.micen.buyers.util.ImageUtil;
import com.micen.buyers.util.Util;

public class ProductDetailImageAdapter extends PagerAdapter implements OnClickListener
{
	private Context mContext;

	private ArrayList<String> mImageUriList;

	private String isShowImage;

	public ProductDetailImageAdapter(Context context, ArrayList<String> imageUriList, String isShowImage)
	{
		this.mContext = context;
		this.mImageUriList = imageUriList;
		this.isShowImage = isShowImage;
	}

	@Override
	public int getCount()
	{
		return mImageUriList.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object)
	{
		return view == object;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object)
	{
		((ViewPager) container).removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position)
	{
		String uri = mImageUriList.get(position);
		View v = LayoutInflater.from(mContext).inflate(R.layout.product_detail_image_page_item, null);
		ImageView imageView = (ImageView) v.findViewById(R.id.image);
		Util.showFitImage(isShowImage, uri, imageView);
		v.setTag(position);
		v.setOnClickListener(this);
		((ViewPager) container).addView(v, 0);
		return v;
	}

	@Override
	public void onClick(View v)
	{
		// 事件统计 136 点击查看大图（产品详情页） 点击事件
		SysManager.analysis(R.string.a_type_click, R.string.c136);

		int position = (Integer) v.getTag();
		Intent intent = new Intent(mContext, ImageBrowserActivity_.class);
		intent.putExtra("position", position);
		intent.putStringArrayListExtra("imageList", mImageUriList);
		intent.putExtra("isShowImage", isShowImage);
		mContext.startActivity(intent);
	}

}

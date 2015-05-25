package com.micen.buyers.view.product;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.focustech.common.util.Utils;
import com.focustech.common.widget.viewpagerindictor.CirclePageIndicator;
import com.micen.buyers.activity.R;
import com.micen.buyers.adapter.ProductDetailImageAdapter;
import com.micen.buyers.view.HomeBannerViewPager;

public class ProductDetailImageView extends RelativeLayout
{
	private HomeBannerViewPager imageViewPager;
	private CirclePageIndicator imageIndicator;

	private ArrayList<String> mImageUriList;

	private ProductDetailImageAdapter mAdapter;
	private String isShowImage;

	public ProductDetailImageView(Context context, ArrayList<String> imageUriList, String isShowImage)
	{
		super(context);
		this.mImageUriList = imageUriList;
		this.isShowImage = isShowImage;
		init();
	}

	protected void init()
	{
		LayoutInflater.from(getContext()).inflate(R.layout.product_detail_image_layout, this);
		imageViewPager = (HomeBannerViewPager) findViewById(R.id.image_vp);
		imageIndicator = (CirclePageIndicator) findViewById(R.id.image_indicator);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, Utils.toDip(getContext(), 280));
		imageViewPager.setLayoutParams(params);
		mAdapter = new ProductDetailImageAdapter(getContext(), mImageUriList, isShowImage);
		imageViewPager.setAdapter(mAdapter);
		imageIndicator.setViewPager(imageViewPager);
	}

}

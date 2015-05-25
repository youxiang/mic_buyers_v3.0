package com.micen.buyers.view.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.micen.buyers.activity.R;
import com.micen.buyers.manager.IntentManager;
import com.micen.buyers.manager.SysManager;
import com.micen.buyers.module.special.SpecialContent;
import com.micen.buyers.module.special.SpecialObject;
import com.micen.buyers.util.ImageUtil;
import com.micen.buyers.util.Util;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

/**********************************************************
 * @文件名称：HomeSpecialItemView.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年5月7日 下午6:50:56
 * @文件描述：首页单个专题布局
 * @修改历史：2015年5月7日创建初始版本
 **********************************************************/
public class HomeSpecialItemView extends RelativeLayout implements OnClickListener
{
	private SpecialObject content;
	private LinearLayout specialLeftLayout;
	private TextView specialLeftName;
	private ImageView specialLeftImage;

	private RelativeLayout specialRightUpLayout;
	private TextView specialRightUpName;
	private ImageView specialRightUpImage;

	private RelativeLayout specialRightDownLayout;
	private TextView specialRightDownName;
	private ImageView specialRightDownImage;
	boolean isNeedTopLine = false;

	public HomeSpecialItemView(Context context, SpecialObject content, boolean isNeedTopLine)
	{
		super(context);
		this.content = content;
		this.isNeedTopLine = isNeedTopLine;
		initView();
	}

	private void displaySpecialImage(String uri, ImageView imageView)
	{
		ImageUtil.getImageLoader().displayImage(uri, imageView, ImageUtil.getSafeImageNoStubOptions(),
				new ImageLoadingListener()
				{
					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage)
					{
						((ImageView) view).setImageDrawable(new BitmapDrawable(getResources()));
						view.setBackgroundDrawable(new BitmapDrawable(getResources(), loadedImage));
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
	}

	private void initView()
	{
		LayoutInflater.from(getContext()).inflate(R.layout.home_special_item_layout, this);
		setPadding(0, isNeedTopLine ? Util.dip2px(0.5f) : 0, 0, Util.dip2px(0.5f));
		specialLeftLayout = (LinearLayout) findViewById(R.id.special_left_layout);
		specialLeftName = (TextView) findViewById(R.id.special_left_name);
		specialLeftImage = (ImageView) findViewById(R.id.special_left_image);

		if (content.left != null)
		{
			specialLeftName.setText(content.left.adsContent.contentName);
			displaySpecialImage(content.left.adsContent.contentPic1, specialLeftImage);
			specialLeftLayout.setTag(content.left);
			specialLeftLayout.setOnClickListener(this);
		}

		specialRightUpLayout = (RelativeLayout) findViewById(R.id.special_right_up_layout);
		specialRightUpName = (TextView) findViewById(R.id.special_right_up_name);
		specialRightUpImage = (ImageView) findViewById(R.id.special_right_up_image);

		if (content.rightUp != null)
		{
			specialRightUpName.setText(content.rightUp.adsContent.contentName);
			displaySpecialImage(content.rightUp.adsContent.contentPic1, specialRightUpImage);
			specialRightUpLayout.setTag(content.rightUp);
			specialRightUpLayout.setOnClickListener(this);
		}

		specialRightDownLayout = (RelativeLayout) findViewById(R.id.special_right_down_layout);
		specialRightDownName = (TextView) findViewById(R.id.special_right_down_name);
		specialRightDownImage = (ImageView) findViewById(R.id.special_right_down_image);

		if (content.rightDown != null)
		{
			specialRightDownName.setText(content.rightDown.adsContent.contentName);
			displaySpecialImage(content.rightDown.adsContent.contentPic1, specialRightDownImage);
			specialRightDownLayout.setTag(content.rightDown);
			specialRightDownLayout.setOnClickListener(this);
		}

	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.special_left_layout:
			// 事件统计 7 查看专题（首页） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c7);
			IntentManager.specialTarget(getContext(), (SpecialContent) v.getTag());
			break;
		case R.id.special_right_up_layout:
			// 事件统计 7 查看专题（首页） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c7);
			IntentManager.specialTarget(getContext(), (SpecialContent) v.getTag());
			break;
		case R.id.special_right_down_layout:
			// 事件统计 7 查看专题（首页） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c7);
			IntentManager.specialTarget(getContext(), (SpecialContent) v.getTag());
			break;
		}
	}

}

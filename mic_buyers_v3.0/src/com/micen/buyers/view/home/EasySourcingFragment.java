package com.micen.buyers.view.home;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.focustech.common.capturepicture.CameraTakePhoto;
import com.focustech.common.capturepicture.GalleryTakePhoto;
import com.focustech.common.capturepicture.TakePhoto;
import com.focustech.common.capturepicture.TakePhoto.TakePhotoFailReason;
import com.focustech.common.capturepicture.TakePhoto.TakePhotoListener;
import com.focustech.common.mob.DisposeDataListener;
import com.focustech.common.util.ToastUtil;
import com.focustech.common.widget.HorizontalListView;
import com.micen.buyers.activity.R;
import com.micen.buyers.activity.rfq.RFQAddActivity_;
import com.micen.buyers.adapter.easysourcing.RfqAndQuotationAdapter;
import com.micen.buyers.constant.Constants;
import com.micen.buyers.http.RequestCenter;
import com.micen.buyers.manager.SysManager;
import com.micen.buyers.module.easysourcing.RfqAndQuotations;
import com.micen.buyers.module.rfq.RFQContentFiles;
import com.micen.buyers.util.Util;

@EFragment(R.layout.easy_sourcing_fragment_layout)
public class EasySourcingFragment extends HomeBaseFragment implements OnClickListener
{
	@ViewById(R.id.home_recommend_list)
	protected HorizontalListView horizontalListView;

	@ViewById(R.id.common_title_name)
	protected TextView titleText;

	@ViewById(R.id.btn_easy_camera)
	protected Button btnCamera;
	@ViewById(R.id.btn_easy_gallery)
	protected Button btnGallery;
	@ViewById(R.id.btn_easy_describe)
	protected Button btnDescribe;
	@ViewById(R.id.view_flipper)
	protected ViewFlipper flipper;
	@ViewById(R.id.btn_postsourcing_now)
	protected Button btnPostSourcing;
	@ViewById(R.id.tv_todayrequests)
	protected TextView tv_todayrequests;

	private TakePhoto takePhoto;
	private Context mContext;

	private RfqAndQuotationAdapter rfqAndQuotationAdapter;

	public EasySourcingFragment()
	{

	}

	@AfterViews
	protected void initView()
	{
		titleText.setText(R.string.easy_sourcing);

		btnCamera.setOnClickListener(this);
		btnGallery.setOnClickListener(this);
		btnDescribe.setOnClickListener(this);

		btnPostSourcing.setOnClickListener(this);

		//中间一个按钮的动画
		//addAnimation();

		flipper.setInAnimation(AnimationUtils.loadAnimation(this.getActivity(), R.anim.push_up_in));
		flipper.setOutAnimation(AnimationUtils.loadAnimation(this.getActivity(), R.anim.push_up_out));
		flipper.startFlipping();

		mContext = this.getActivity();

		// 根据DP获取PX，然后设置ITEM的间距
		float scale = mContext.getResources().getDisplayMetrics().density;
		int width = (int) (5 * scale + 0.5f);
		horizontalListView.setDividerWidth(width);
	}

	@Override
	public void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		if (rfqAndQuotationAdapter == null)
		{
			RequestCenter.getEasySourcingCategory(rfqAndQuotation);
		}
		// 事件统计$10033 Easy Sourcing 页面事件
		SysManager.analysis(R.string.a_type_page, R.string.p10033);
	}

	private DisposeDataListener rfqAndQuotation = new DisposeDataListener()
	{
		@Override
		public void onSuccess(Object result)
		{
			RfqAndQuotations products = (RfqAndQuotations) result;
			if (products.content != null)
			{
				tv_todayrequests.setVisibility(View.VISIBLE);
				horizontalListView.setVisibility(View.VISIBLE);
				rfqAndQuotationAdapter = new RfqAndQuotationAdapter(getActivity(), products.content);
				horizontalListView.setAdapter(rfqAndQuotationAdapter);
				horizontalListView.setOnDragListener(new OnDragListener()
				{

					@Override
					public boolean onDrag(View v, DragEvent event)
					{
						// TODO Auto-generated method stub
						// 事件统计135 查看更多热门RFQ目录(Easy Sourcing) 点击事件
						SysManager.analysis(R.string.a_type_click, R.string.c135);
						return false;
					}
				});

			}
		}

		@Override
		public void onFailure(Object failedReason)
		{
			ToastUtil.toast(getActivity(), failedReason);
		}
	};

	@Override
	public String getChildTag()
	{
		return EasySourcingFragment.class.getName();
	}

	private void addAnimation()
	{
		Animation anim = AnimationUtils.loadAnimation(this.getActivity(), R.anim.shake);
		btnCamera.startAnimation(anim);
		btnGallery.startAnimation(anim);
		btnDescribe.startAnimation(anim);
	}

	public void setTextViewCompoundDrawable(TextView textView, int drawableResId)
	{
		Drawable drawable = getResources().getDrawable(drawableResId);
		drawable.setBounds(0, 0, Util.dip2px(46), Util.dip2px(46));
		textView.setCompoundDrawablePadding(Util.dip2px(11));
		textView.setCompoundDrawables(null, drawable, null, null);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (takePhoto != null)
		{
			takePhoto.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	public void onClick(View view)
	{
		// TODO Auto-generated method stub
		switch (view.getId())
		{
		case R.id.btn_easy_camera:
			// 事件统计 132 拍照(Easy Sourcing) 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c132);

			Constants.easySourcingBitmap = null;
			takePhoto = new CameraTakePhoto(this, null, listener);
			break;
		case R.id.btn_easy_gallery:
			// 事件统计 133 查看相册（(Easy Sourcing) 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c133);

			Constants.easySourcingBitmap = null;
			takePhoto = new GalleryTakePhoto(this, null, listener);
			break;
		case R.id.btn_easy_describe:
			// 事件统计 134 点击Describe (Easy Sourcing) 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c134);

			Intent describeIntent = new Intent(mContext, RFQAddActivity_.class);
			startActivity(describeIntent);
			break;
		case R.id.btn_postsourcing_now:
			// 事件统计131 快速发布RFQ (Easy Sourcing) 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c131);

			Intent postIntent = new Intent(mContext, RFQAddActivity_.class);
			startActivity(postIntent);

			break;
		}
	}

	protected TakePhotoListener listener = new TakePhotoListener()
	{
		@Override
		public void onSuccess(String imagePath, View displayView, Bitmap bitmap)
		{
			// 根据传入的控件ID区分
			RFQContentFiles file = new RFQContentFiles();
			file.fileLocalPath = imagePath;
			Intent cameraIntent = new Intent(mContext, RFQAddActivity_.class);
			cameraIntent.putExtra("isEasySourcing", true);
			cameraIntent.putExtra("easyImageObj", file);
			Constants.easySourcingBitmap = bitmap;
			startActivity(cameraIntent);
		}

		@Override
		public void onFail(TakePhotoFailReason failReason)
		{
			switch (failReason)
			{
			case SDCardNotFound:
				ToastUtil.toast(mContext, R.string.sd_card_undetected);
				break;
			default:
				break;
			}
		}
	};

}

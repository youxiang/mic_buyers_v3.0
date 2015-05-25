package com.micen.buyers.activity.rfq;

import java.util.ArrayList;

import org.androidannotations.annotations.EActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.focustech.common.mob.DisposeDataListener;
import com.focustech.common.util.ToastUtil;
import com.focustech.common.widget.dialog.CommonProgressDialog;
import com.micen.buyers.activity.R;
import com.micen.buyers.constant.Constants;
import com.micen.buyers.constant.UrlConstants;
import com.micen.buyers.http.RequestCenter;
import com.micen.buyers.manager.PopupManager;
import com.micen.buyers.manager.SysManager;
import com.micen.buyers.module.rfq.RFQContent;
import com.micen.buyers.module.rfq.RFQContentFiles;
import com.micen.buyers.module.rfq.RFQContentValidateTimeEnd;

/**********************************************************
 * @文件名称：RFQAddActivity.java
 * @文件作者：xiongjiangwei
 * @创建时间：2014-1-24 上午10:39:02
 * @文件描述：发布RFQ页面
 * @修改历史：2014-1-24创建初始版本
 **********************************************************/
@EActivity
public class RFQAddActivity extends RFQPostBaseActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Constants.currentActivity = this;
		mIntent = getIntent();
		initIntent();
	}

	private void initIntent()
	{
		if (mIntent != null)
		{
			boolean isEasySourcing = mIntent.getBooleanExtra("isEasySourcing", false);
			if (isEasySourcing && captureLayout != null && captureLayout.getChildCount() > 0)
			{
				ImageView captureImage = (ImageView) captureLayout.getChildAt(0).findViewById(
						R.id.mic_rfq_capture_image);
				RFQContentFiles file = (RFQContentFiles) mIntent.getSerializableExtra("easyImageObj");
				captureImage.setTag(file);
				if (Constants.easySourcingBitmap != null)
				{
					captureImage.setImageBitmap(Constants.easySourcingBitmap);
				}
				// 保存完图片路径后显示删除按钮
				captureLayout.getChildAt(0).findViewById(R.id.mic_rfq_capture_del_image).setVisibility(View.VISIBLE);
				ImageView preImage = (ImageView) captureLayout.getChildAt(captureLayout.getChildCount() - 1)
						.findViewById(R.id.mic_rfq_capture_image);
				if (captureLayout.getChildCount() < 3 && preImage.getDrawable() != null)
				{
					captureLayout.addView(createCaptureEmptyChildView());
				}
			}
		}
	}

	@Override
	protected void onResume()
	{
		super.onResume();
	}

	@Override
	protected void initChildView()
	{
		requirementsToSupplierLayout.setVisibility(View.GONE);
		supplierLayout.setVisibility(View.GONE);
		requirementsForTradingLayout.setVisibility(View.GONE);
		paymentTermsEdit.setVisibility(View.GONE);
		initExpireDate(Constants.DEFAULT_DAY_OFFSET);
		captureLayout.addView(createCaptureEmptyChildView());
		setPostRFQButtonStatus();
	}

	@Override
	protected void onNewIntent(Intent intent)
	{
		super.onNewIntent(intent);
	}

	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		switch (v.getId())
		{
		case R.id.mic_rfq_optional_layout:
			boolean isMoreMsgLayoutGone = moreMsgLayout.getVisibility() == View.GONE ? true : false;
			setRightCompoundDrawable(isMoreMsgLayoutGone ? R.drawable.mic_rfq_optional_arrow_up
					: R.drawable.mic_rfq_optional_arrow, optionalText);
			moreMsgLayout.setVisibility(isMoreMsgLayoutGone ? View.VISIBLE : View.GONE);
			if (isMoreMsgLayoutGone)
				scrollToBottom();
			break;
		case R.id.mic_rfq_pieces:
			SysManager.getInstance().dismissInputKey(this);
			PopupManager.getInstance().showRFQPopup(
					createPopupView(
							createStringListPopupView(createStringListPopupAdapter(R.array.mic_rfq_quantity_unit),
									unitPopupItemClick), popupBgClick));
			break;
		case R.id.mic_rfq_usd:
			SysManager.getInstance().dismissInputKey(this);
			PopupManager.getInstance().showRFQPopup(
					createPopupView(
							createStringListPopupView(createStringListPopupAdapter(R.array.mic_rfq_price_unit),
									usdItemClick), popupBgClick));
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void startPostRFQ()
	{
		RFQContent rfqContent = new RFQContent();
		rfqContent.subject = productNameEdit.getText().toString();
		rfqContent.estimatedQuantity = purchaseQuantityEdit.getText().toString();
		rfqContent.estimatedQuantityType = piecesEdit.getText().toString();
		rfqContent.validateTimeEnd = new RFQContentValidateTimeEnd();
		rfqContent.validateTimeEnd.time = expiredDateText.getText().toString();
		rfqContent.detailDescription = descriptionEdit.getText().toString();
		if (captureLayout.getChildCount() > 1)
		{
			rfqContent.rfqFiles = new ArrayList<RFQContentFiles>();
			RFQContentFiles files = null;
			for (int i = 0; i < captureLayout.getChildCount(); i++)
			{
				files = (RFQContentFiles) captureLayout.getChildAt(i).findViewById(R.id.mic_rfq_capture_image).getTag();
				if (files != null)
				{
					rfqContent.rfqFiles.add(files);
				}
			}
		}
		if (categoryText.getTag() != null)
		{
			rfqContent.categoryId = categoryText.getTag().toString();
		}
		if (!"".equals(shipmentTermsText.getText().toString()))
		{
			rfqContent.shipmentTerms = shipmentTermsText.getText().toString();
			if (!"".equals(targetPriceEdit.getText().toString()))
			{
				rfqContent.targetPrice = targetPriceEdit.getText().toString();
			}
			if (!"".equals(usdEdit.getText().toString()))
			{
				rfqContent.targetPriceUnit = usdEdit.getText().toString();
			}
			if (!"".equals(destinationPortEdit.getText().toString()))
			{
				rfqContent.destinationPort = destinationPortEdit.getText().toString();
			}
		}
		RequestCenter.addOrUpdateRFQ(postRFQListener, UrlConstants.ADD_RFQ, rfqContent);
	}

	private DisposeDataListener postRFQListener = new DisposeDataListener()
	{
		@Override
		public void onSuccess(Object result)
		{
			CommonProgressDialog.getInstance().dismissProgressDialog();
			ToastUtil.toast(RFQAddActivity.this, R.string.mic_rfq_post_success);
			finish();
		}

		@Override
		public void onFailure(Object failedReason)
		{
			CommonProgressDialog.getInstance().dismissProgressDialog();
			toastResponseMsg(failedReason.toString());
		}
	};
}

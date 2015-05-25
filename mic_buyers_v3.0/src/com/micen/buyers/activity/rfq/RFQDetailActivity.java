package com.micen.buyers.activity.rfq;

import java.util.ArrayList;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.focustech.common.mob.DisposeDataListener;
import com.focustech.common.util.ToastUtil;
import com.focustech.common.util.Utils;
import com.focustech.common.widget.dialog.CommonDialog;
import com.focustech.common.widget.dialog.CommonDialog.DialogClickListener;
import com.focustech.common.widget.dialog.CommonProgressDialog;
import com.micen.buyers.activity.BaseActivity;
import com.micen.buyers.activity.R;
import com.micen.buyers.constant.Constants;
import com.micen.buyers.http.RequestCenter;
import com.micen.buyers.manager.SysManager;
import com.micen.buyers.module.product.ProductKeyValuePair;
import com.micen.buyers.module.rfq.RFQ;
import com.micen.buyers.module.rfq.RFQContent;
import com.micen.buyers.module.rfq.RFQStatus;
import com.micen.buyers.util.Util;
import com.micen.buyers.view.NormalTableLayout;
import com.micen.buyers.view.SearchListProgressBar;

/**********************************************************
 * @文件名称：RFQDetailActivity.java
 * @文件作者：xiongjiangwei
 * @创建时间：2014-2-10 上午10:14:03
 * @文件描述：RFQ详情Activity
 * @修改历史：2014-2-10创建初始版本
 **********************************************************/
@EActivity
public class RFQDetailActivity extends BaseActivity
{
	@ViewById(R.id.progressbar_layout)
	protected SearchListProgressBar progressLayout;
	@ViewById(R.id.reedit_reason)
	protected RelativeLayout reasonReedit;
	@ViewById(R.id.tv_rfq_reedit_detail_reason)
	protected TextView reasonText;
	@ViewById(R.id.tv_rfq_reedit_detail_subject)
	protected TextView subjectText;
	@ViewById(R.id.tv_rfq_reedit_detail_expiredDate)
	protected TextView expiredDateText;
	@ViewById(R.id.productsource_info_table)
	protected NormalTableLayout productsourceLayout;
	@ViewById(R.id.supplier_info_table)
	protected NormalTableLayout supplierInfoLayout;
	@ViewById(R.id.shipment_info_table)
	protected NormalTableLayout shipeInfoLayout;

	private String rfqId;
	private RFQContent rfqDetail;
	private RFQStatus rfqStatus = RFQStatus.Quotation;

	/**
	 * 新添加的表格
	 */
	private int screenWidth;
	/**
	 * 界面点击事件统一处理
	 */
	private CommonDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rfq_detail);
		Constants.currentActivity = this;
		Constants.reEditDetailActivity = this;
	}

	@AfterViews
	protected void initView()
	{
		if (getIntent() != null && getIntent().hasExtra("rfqId"))
		{
			rfqId = getIntent().getStringExtra("rfqId");
			rfqStatus = RFQStatus.getValueByTag(getIntent().getStringExtra("rfqStatus"));
		}
		screenWidth = Utils.getWindowWidthPix(this);

		titleLeftButton.setImageResource(R.drawable.ic_title_back);
		titleText.setText(R.string.mic_rfq_details);
		titleRightButton1.setVisibility(View.GONE);
		titleRightButton2.setVisibility(View.GONE);
		titleLeftButton.setOnClickListener(this);
		titleRightButton3.setOnClickListener(this);

		switch (rfqStatus)
		{
		case Pending:
			titleText.setText(R.string.mic_rfq_manage_pending);
			titleRightButton3.setImageResource(R.drawable.ic_delete);
			reasonReedit.setVisibility(View.GONE);
			break;
		case Quotation:
			titleText.setText(R.string.mic_rfq_manage_quotation);
			titleRightButton3.setVisibility(View.GONE);
			reasonReedit.setVisibility(View.GONE);
			break;
		case Closed:
			titleText.setText(R.string.closed);
			titleRightButton3.setVisibility(View.GONE);
			reasonReedit.setVisibility(View.GONE);
			break;
		case Rejected:
			titleText.setText(R.string.mic_rfq_manage_rejected);
			titleRightButton3.setImageResource(R.drawable.ic_reedit);
			break;
		}
		if (rfqId != null && !"".equals(rfqId))
		{
			RequestCenter.getRFQDetail(rfqDetailListener, rfqId);
		}
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		switch (rfqStatus)
		{
		case Rejected:
			// 事件统计 $10011 Rejected 详情页（Sourcing Request） 页面事件
			SysManager.analysis(R.string.a_type_page, R.string.p10011);
			break;
		case Pending:
			// 事件统计 $10012 Pending详情页（Sourcing Request） 页面事件
			SysManager.analysis(R.string.a_type_page, R.string.p10012);
			break;
		case Closed:
			// 事件统计 $10013 Closed详情页（Sourcing Request） 页面事件
			SysManager.analysis(R.string.a_type_page, R.string.p10013);
			break;
		default:
			break;
		}

	}

	/**
	 * 创建ProductSource表格
	 */
	private void createProductSourceInfoTable()
	{
		ArrayList<ProductKeyValuePair> productSourceInfo = new ArrayList<ProductKeyValuePair>();
		productSourceInfo.add(new ProductKeyValuePair(getString(R.string.mic_rfq_hint_productName), rfqDetail.subject));
		productSourceInfo.add(new ProductKeyValuePair(getString(R.string.mic_rfq_hint_description),
				rfqDetail.detailDescription));
		productSourceInfo.add(new ProductKeyValuePair(getString(R.string.mic_rfq_hint_category), rfqDetail.category));
		productSourceInfo.add(new ProductKeyValuePair(getString(R.string.mic_rfq_hint_purchaseQuantity),
				rfqDetail.estimatedQuantity + " " + rfqDetail.estimatedQuantityType));
		productSourceInfo.add(new ProductKeyValuePair(getString(R.string.mic_rfq_hint_expiredDate), Util
				.formatDateToEn(rfqDetail.validateTimeEnd.time)));
		productsourceLayout
				.initParams(productSourceInfo, getString(R.string.mic_rfq_details_sourcing), screenWidth / 3);
		productsourceLayout.createOne2OneTable();
	}

	private void createSupplierInfoTable()
	{
		if (!Utils.isEmpty(rfqDetail.supplierLocation) || !Utils.isEmpty(rfqDetail.supplierType)
				|| !Utils.isEmpty(rfqDetail.supplierTypeOther) || !Utils.isEmpty(rfqDetail.supplierEmployeesType)
				|| !Utils.isEmpty(rfqDetail.supplierCertification) || !Utils.isEmpty(rfqDetail.exportMarket))
		{
			supplierInfoLayout.setVisibility(View.VISIBLE);
			ArrayList<ProductKeyValuePair> supplierInfo = new ArrayList<ProductKeyValuePair>();
			supplierInfo.add(new ProductKeyValuePair(getString(R.string.mic_rfq_hint_location),
					rfqDetail.supplierLocation));
			supplierInfo.add(new ProductKeyValuePair(getString(R.string.mic_rfq_hint_businessType), getTypes()));
			supplierInfo.add(new ProductKeyValuePair(getString(R.string.mic_rfq_hint_numbersOfEmployees),
					rfqDetail.supplierEmployeesType));
			supplierInfo.add(new ProductKeyValuePair(getString(R.string.mic_rfq_hint_companyCertification),
					rfqDetail.supplierCertification));
			supplierInfo.add(new ProductKeyValuePair(getString(R.string.mic_rfq_hint_exportMarkets), Util
					.getAllListValue(rfqDetail.exportMarket)));

			supplierInfoLayout.initParams(supplierInfo, getString(R.string.mic_rfq_edit_supplier_title),
					screenWidth / 3);
			supplierInfoLayout.createOne2OneTable();
		}
	}

	private void createShipeInfoTable()
	{
		if (!Utils.isEmpty(rfqDetail.shipmentTerms))
		{
			shipeInfoLayout.setVisibility(View.VISIBLE);
			ArrayList<ProductKeyValuePair> shipeInfo = new ArrayList<ProductKeyValuePair>();
			shipeInfo.add(new ProductKeyValuePair(getString(R.string.mic_rfq_hint_shipmentTerms),
					rfqDetail.shipmentTerms));
			shipeInfo.add(new ProductKeyValuePair(getString(R.string.mic_rfq_hint_targetPrice), rfqDetail.targetPrice
					+ " " + rfqDetail.targetPriceUnit));
			shipeInfo.add(new ProductKeyValuePair(getString(R.string.mic_rfq_hint_destinationPort),
					rfqDetail.destinationPort));
			shipeInfo
					.add(new ProductKeyValuePair(getString(R.string.mic_rfq_hint_paymentTerms), rfqDetail.paymentTerms));
			shipeInfoLayout.initParams(shipeInfo, getString(R.string.mic_rfq_requirementsForTrading), screenWidth / 3);
			shipeInfoLayout.createOne2OneTable();
		}

	}

	private void initHeadInfo()
	{
		subjectText.setText(getString(R.string.mic_rfq_details_subject) + rfqDetail.subject);
		expiredDateText.setText(getString(R.string.mic_rfq_details_expiredDate)
				+ Util.formatDateToEn(rfqDetail.validateTimeEnd.time));
		reasonText.setText(rfqDetail.returnAdvise);
		reasonText.setMovementMethod(new ScrollingMovementMethod());
	}

	private void updateUI()
	{
		if (rfqDetail == null)
			return;
		if ("stopped".equals(rfqDetail.status))
		{
			titleRightButton3.setVisibility(View.GONE);
		}
		/**
		 * 初始化首部信息
		 */
		initHeadInfo();
		/**
		 * 创建ProductSource表格 
		 */
		createProductSourceInfoTable();
		/**
		 * 创建Supplier表格
		 */
		createSupplierInfoTable();
		/**
		 * 创建Shipe表格 
		 */
		createShipeInfoTable();
	}

	/**
	 * RFQ详情接口请求回调，返回RFQ详情信息
	 */
	private DisposeDataListener rfqDetailListener = new DisposeDataListener()
	{
		@Override
		public void onSuccess(Object result)
		{
			progressLayout.setVisibility(View.GONE);
			rfqDetail = ((RFQ) result).content.get(0);
			updateUI();
		}

		@Override
		public void onFailure(Object failedReason)
		{
			progressLayout.setVisibility(View.GONE);
			ToastUtil.toast(RFQDetailActivity.this, failedReason);
		}
	};

	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		switch (v.getId())
		{
		case R.id.common_title_back_button:
			finish();
			break;
		case R.id.common_title_right_button3:
			if (rfqDetail != null)
			{
				switch (rfqStatus)
				{
				case Pending:
					showDelDialog();
					break;
				case Quotation:
					break;
				case Rejected:
					// 事件统计 60 点击重新编辑（Rejected 详情页） 点击事件
					SysManager.analysis(R.string.a_type_click, R.string.c60);
				case Closed:
					// 上面分支 没有break
					if (rfqStatus == RFQStatus.Closed)
					{
						// 事件统计62 点击重新编辑（Closed详情页） 点击事件
						SysManager.analysis(R.string.a_type_click, R.string.c62);
					}
					Intent editIntent = new Intent();
					editIntent.setClass(this, RFQEditActivity_.class);
					editIntent.putExtra("rfqDetail", rfqDetail);
					startActivity(editIntent);
					break;
				}
			}
			break;
		}
	}

	/**
	 * 显示是否删除RFQ对话框 
	 */
	private void showDelDialog()
	{
		if (dialog != null)
		{
			dialog.show();
			return;
		}
		dialog = new CommonDialog(this, getString(R.string.mic_rfq_delete), getString(R.string.mic_ok),
				getString(R.string.cancel), Util.dip2px(258), new DialogClickListener()
				{
					@Override
					public void onDialogClick()
					{
						// 事件统计 61 删除（Pending详情页） 点击事件
						SysManager.analysis(R.string.a_type_click, R.string.c61);
						delRFQItem();
					}
				});
		dialog.show();
	}

	/**
	 * 发送RFQ删除请求
	 */

	private void delRFQItem()
	{
		CommonProgressDialog.getInstance().showProgressDialog(this, getString(R.string.mic_loading));
		RequestCenter.delRFQByRFQId(new DisposeDataListener()
		{
			@Override
			public void onSuccess(Object result)
			{
				CommonProgressDialog.getInstance().dismissProgressDialog();
				finish();
			}

			@Override
			public void onFailure(Object failedReason)
			{
				CommonProgressDialog.getInstance().dismissProgressDialog();
				ToastUtil.toast(RFQDetailActivity.this, failedReason);
			}
		}, rfqDetail.rfqId);
	}

	/**
	 * 拼接供应商类型
	 * @return
	 */
	private String getTypes()
	{
		String types = "";
		if (!Utils.isEmpty(rfqDetail.supplierType))
		{
			types = types + Util.getAllListValue(rfqDetail.supplierType);
		}
		if (!Utils.isEmpty(rfqDetail.supplierTypeOther))
		{
			if (!Utils.isEmpty(rfqDetail.supplierType))
			{
				String[] others = rfqDetail.supplierTypeOther.split(",");
				for (int i = 0; i < others.length; i++)
				{
					if (!rfqDetail.supplierType.contains(others[i]))
					{
						types = types + "," + others[i];
					}
				}
			}
			else
			{
				types = types + rfqDetail.supplierTypeOther;
			}
		}
		return types;
	}
}

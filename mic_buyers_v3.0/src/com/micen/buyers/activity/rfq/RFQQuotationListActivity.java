package com.micen.buyers.activity.rfq;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.focustech.common.mob.DisposeDataListener;
import com.focustech.common.util.ToastUtil;
import com.focustech.common.util.Utils;
import com.focustech.common.widget.dialog.CommonDialog;
import com.focustech.common.widget.dialog.CommonDialog.DialogClickListener;
import com.focustech.common.widget.dialog.CommonProgressDialog;
import com.focustech.common.widget.pulltorefresh.PullToRefreshBase;
import com.focustech.common.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.focustech.common.widget.pulltorefresh.PullToRefreshBase.OnPullEventListener;
import com.focustech.common.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.focustech.common.widget.pulltorefresh.PullToRefreshBase.State;
import com.focustech.common.widget.pulltorefresh.PullToRefreshListView;
import com.micen.buyers.activity.BaseActivity;
import com.micen.buyers.activity.R;
import com.micen.buyers.activity.message.MailSendActivity_;
import com.micen.buyers.activity.showroom.ShowRoomActivity_;
import com.micen.buyers.adapter.rfq.RFQQuotationListAdapter;
import com.micen.buyers.db.SharedPreferenceManager;
import com.micen.buyers.http.RequestCenter;
import com.micen.buyers.manager.PopupManager;
import com.micen.buyers.manager.SysManager;
import com.micen.buyers.module.MailSendTarget;
import com.micen.buyers.module.product.ProductKeyValuePair;
import com.micen.buyers.module.quotation.QuotationList;
import com.micen.buyers.module.quotation.QuotationListContent;
import com.micen.buyers.module.quotation.QuotationListItem;
import com.micen.buyers.module.rfq.RFQStatus;
import com.micen.buyers.module.showroom.CompanyDetail;
import com.micen.buyers.util.BuyerApplication;
import com.micen.buyers.util.Util;
import com.micen.buyers.view.NormalTableLayout;
import com.micen.buyers.view.SearchListProgressBar;

/**********************************************************
 * @文件名称：RFQQuotationListActivity.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年4月14日 上午10:47:33
 * @文件描述：RFQ报价列表页
 * @修改历史：2015年4月14日创建初始版本
 **********************************************************/
@EActivity
public class RFQQuotationListActivity extends BaseActivity implements OnClickListener, OnRefreshListener2<ListView>
{
	/**
	 * RFQ详情
	 */
	@ViewById(R.id.mic_quotation_list_rfq_title_layout)
	protected LinearLayout rfqTitleLayout;
	@ViewById(R.id.mic_quotation_list_rfq_title)
	protected TextView rfqTitleText;
	@ViewById(R.id.progressbar_layout)
	protected SearchListProgressBar progressBar;
	/**
	 * 下拉刷新ListView
	 */
	@ViewById(R.id.mic_quotation_list_page_list)
	protected PullToRefreshListView pullToListView;
	/**
	 * 报价详情公司名称
	 */
	@ViewById(R.id.mic_quotation_detail_company_name)
	protected TextView companyNameTv;
	@ViewById(R.id.quotation_detail_company_layout)
	protected RelativeLayout companyNameLayout;
	@ViewById(R.id.supplier_info_table)
	protected NormalTableLayout supplierInfoLayout;
	@ViewById(R.id.quotation_all_layout)
	protected LinearLayout quotationAllLayout;
	/**
	 * 报价详情布局
	 */
	@ViewById(R.id.mic_quotation_detail_layout)
	protected RelativeLayout quotationDetailLayout;

	/**
	 * 报价详情布局 是否显示
	 */
	protected boolean quotationVisiable;

	private ListView quotationList;
	/**
	 * 屏幕高度
	 */
	private int screenHeight;
	/**
	 * RFQID
	 */
	private String rfqId;
	private String rfqSubject;
	/**
	 * 报价列表数据
	 */
	private QuotationList theQuotationListBean;
	/**
	 * 报价列表数据源
	 */
	private RFQQuotationListAdapter quotationListAdapter;
	/**
	 * 当前显示的报价详情的位置
	 */
	private int quotationPositionTag;

	private ImageView bottomBarPre;
	private ImageView bottomBarNext;

	/**
	 * 删除数据弹框
	 */
	private CommonDialog dialog;

	/**
	 * 表格控件
	 * @param savedInstanceState
	 */
	private int screenWidth;

	// private NormalTableLayout quotationLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quotation_list);
	}

	@AfterViews
	protected void initView()
	{
		screenWidth = Utils.getWindowWidthPix(this);
		screenHeight = Utils.getWindowHeightPix(this);
		rfqId = getIntent().getStringExtra("rfqID");
		rfqSubject = getIntent().getStringExtra("rfqSubject");

		titleLeftButton.setImageResource(R.drawable.ic_title_back);
		titleText.setText(R.string.mic_quotation_list);
		titleRightButton1.setVisibility(View.GONE);
		titleRightButton2.setVisibility(View.GONE);
		titleRightButton3.setImageResource(R.drawable.ic_delete);
		titleLeftButton.setOnClickListener(this);
		titleRightButton3.setOnClickListener(this);

		pullToListView.setVisibility(View.GONE);
		// 不启动任何刷新
		pullToListView.setMode(Mode.PULL_FROM_START);
		// 不显示指示器
		pullToListView.setShowIndicator(false);
		pullToListView.getLoadingLayoutProxy().setLastUpdatedLabel(
				SharedPreferenceManager.getInstance().getString(rfqId, ""));
		pullToListView.setOnPullEventListener(pullEventListener);
		pullToListView.setOnRefreshListener(this);
		quotationList = pullToListView.getRefreshableView();

		quotationDetailLayout.setVisibility(View.GONE);
		quotationVisiable = false;
		ImageView bottomBarSentMail = (ImageView) quotationDetailLayout.findViewById(R.id.ic_bottom_bar1);
		bottomBarPre = (ImageView) quotationDetailLayout.findViewById(R.id.ic_bottom_bar2);
		bottomBarNext = (ImageView) quotationDetailLayout.findViewById(R.id.ic_bottom_bar3);
		ImageView bottomBarClose = (ImageView) quotationDetailLayout.findViewById(R.id.ic_bottom_bar4);
		bottomBarSentMail.setImageResource(R.drawable.ic_replay_mail);
		bottomBarPre.setImageResource(R.drawable.ic_pre);
		bottomBarNext.setImageResource(R.drawable.ic_next);
		bottomBarClose.setImageResource(R.drawable.ic_close);
		rfqTitleLayout.setOnClickListener(this);
		rfqTitleText.setText(getString(R.string.mic_rfq_details_subject) + rfqSubject);
		companyNameLayout.setOnClickListener(this);

		// quotationLayout = (NormalTableLayout) findViewById(R.id.quotation_info_table);

		initData();
	}

	private OnPullEventListener<ListView> pullEventListener = new OnPullEventListener<ListView>()
	{
		@Override
		public void onPullEvent(PullToRefreshBase<ListView> refreshView, State state, Mode direction)
		{
			switch (direction)
			{
			case PULL_FROM_START:
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(
						SharedPreferenceManager.getInstance().getString(rfqId, ""));
				break;
			default:
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("");
				break;
			}
		}
	};

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView)
	{
		saveChildLastRefreshTime();
		requestQuotationList();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView)
	{

	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		// 事件统计$10015 单个RFQ报价列表页 页面事件
		SysManager.analysis(R.string.a_type_page, R.string.p10015);
	}

	/**
	 * 保存最后更新的时间
	 */
	private void saveChildLastRefreshTime()
	{
		// Last Updated:7/31/2013 18:30
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm", Util.getLocale());
		Date dt = new Date(System.currentTimeMillis());
		String label = getString(R.string.mic_last_updated) + sdf.format(dt);
		SharedPreferenceManager.getInstance().putString(rfqId, label);
	}

	/**
	 * 刷新完成
	 */
	private void refreshComplete()
	{
		if (pullToListView != null)
		{
			pullToListView.onRefreshComplete();
		}
	}

	/**
	 * 初始化页面数据
	 */
	private void initData()
	{
		quotationListAdapter = new RFQQuotationListAdapter(this, theQuotationListBean);
		quotationList.setAdapter(quotationListAdapter);
		quotationList.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{

				// 事件统计67 查看报价详情页 点击事件
				SysManager.analysis(R.string.a_type_click, R.string.c67);
				/**
				 * 给QuotationDetailLayout添加一个出场动画,当View执行完它拥有的动画后才会显示
				 */
				Animation translateIn = new TranslateAnimation(0, 0, screenHeight, pullToListView.getTop());
				translateIn.setDuration(400);
				quotationDetailLayout.startAnimation(translateIn);
				quotationDetailLayout.setVisibility(View.VISIBLE);
				quotationVisiable = true;

				// 事件统计 $10016 报价详情页 页面事件
				SysManager.analysis(R.string.a_type_page, R.string.p10016);

				quotationPositionTag = arg2;
				refreshQuotationUI(theQuotationListBean.content.get(quotationPositionTag));
				checkImageView();
				requestQuotationDetail();
			}
		});
		requestQuotationList();
	}

	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		switch (v.getId())
		{
		case R.id.common_title_back_button:
			finish();
			break;
		case R.id.bottom_bar_layout4:
			// 事件统计73 删除报价（报价详情页） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c73);

			Animation translateIn = new TranslateAnimation(0, 0, pullToListView.getTop(), screenHeight);
			translateIn.setDuration(400);
			quotationDetailLayout.startAnimation(translateIn);
			quotationDetailLayout.setVisibility(View.GONE);
			quotationVisiable = false;
			requestQuotationList();
			break;
		case R.id.mic_quotation_list_rfq_title_layout:
			if (quotationVisiable)
			{
				// 事件统计 69 查看RFQ(报价详情页) 点击事件
				SysManager.analysis(R.string.a_type_click, R.string.c69);
			}
			else
			{
				// 事件统计 66 点击查看RFQ（单个RFQ报价列表页） 点击事件
				SysManager.analysis(R.string.a_type_click, R.string.c66);
			}

			Intent intent = new Intent();
			intent.setClass(this, RFQDetailActivity_.class);
			intent.putExtra("rfqStatus", RFQStatus.Quotation.toString());
			intent.putExtra("rfqId", rfqId);
			startActivity(intent);
			break;
		case R.id.bottom_bar_layout1:

			if (BuyerApplication.getInstance().isBuyerSuspended())
			{
				ToastUtil.toast(this, R.string.mic_buyer_suspended);
			}
			else
			{
				// 事件统计71 回复报价 点击事件
				SysManager.analysis(R.string.a_type_click, R.string.c71);
				String comId = theQuotationListBean.content.get(quotationPositionTag).comId;
				requestCompanyDetail(comId);
			}
			break;
		case R.id.common_title_right_button3:
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
							if (quotationVisiable)
							{
								// 事件统计68 删除RFQ(报价详情页) 点击事件
								SysManager.analysis(R.string.a_type_click, R.string.c68);
							}
							else
							{
								// 事件统计 65 删除RFQ（单个RFQ报价列表页） 点击事件
								SysManager.analysis(R.string.a_type_click, R.string.c65);
							}

							delRFQItem();
						}
					});
			dialog.show();
			break;
		case R.id.bottom_bar_layout2:
			if (quotationPositionTag > 0)
			{
				quotationPositionTag--;
				// 事件统计72 查看前后报价（报价详情页） 点击事件
				SysManager.analysis(R.string.a_type_click, R.string.c72);
			}
			checkImageView();
			refreshQuotationUI(theQuotationListBean.content.get(quotationPositionTag));
			requestQuotationDetail();
			break;
		case R.id.bottom_bar_layout3:
			if (quotationPositionTag < theQuotationListBean.content.size() - 1)
			{
				quotationPositionTag++;
				// 事件统计72 查看前后报价（报价详情页） 点击事件
				SysManager.analysis(R.string.a_type_click, R.string.c72);
			}
			checkImageView();
			refreshQuotationUI(theQuotationListBean.content.get(quotationPositionTag));
			requestQuotationDetail();
			break;
		case R.id.quotation_detail_company_layout:// 报价详情页公司名称点击事件，进入showroom页

			// 事件统计70 查看报价公司展示厅（报价详情页） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c70);
			
			Intent showroomIntent = new Intent(this, ShowRoomActivity_.class);
			showroomIntent.putExtra("companyId", theQuotationListBean.content.get(quotationPositionTag).comId);
			startActivity(showroomIntent);
			break;
		default:
			break;
		}
	}

	/**
	 * 根据数据设置图标是否可点击状态
	 */
	private void checkImageView()
	{
		if (theQuotationListBean.content.size() > 1)
		{
			if (quotationPositionTag == theQuotationListBean.content.size() - 1)
			{
				((View) bottomBarNext.getParent()).setEnabled(false);
				bottomBarNext.setImageResource(R.drawable.ic_next_gray);
				((View) bottomBarPre.getParent()).setEnabled(true);
				bottomBarPre.setImageResource(R.drawable.ic_pre);
			}
			else if (quotationPositionTag == 0)
			{
				((View) bottomBarNext.getParent()).setEnabled(true);
				bottomBarNext.setImageResource(R.drawable.ic_next);
				((View) bottomBarPre.getParent()).setEnabled(false);
				bottomBarPre.setImageResource(R.drawable.ic_pre_gray);
			}
			else
			{
				((View) bottomBarNext.getParent()).setEnabled(true);
				bottomBarNext.setImageResource(R.drawable.ic_next);
				((View) bottomBarPre.getParent()).setEnabled(true);
				bottomBarPre.setImageResource(R.drawable.ic_pre);
			}
		}
		else
		{
			((View) bottomBarNext.getParent()).setEnabled(false);
			bottomBarNext.setImageResource(R.drawable.ic_next_gray);
			((View) bottomBarPre.getParent()).setEnabled(false);
			bottomBarPre.setImageResource(R.drawable.ic_pre_gray);
		}
	}

	/**
	 * 请求报价列表数据
	 */
	private void requestQuotationList()
	{
		RequestCenter.getQuotationList(quotationListCallback, rfqId);
	}

	private void requestQuotationDetail()
	{
		QuotationListContent content = theQuotationListBean.content.get(quotationPositionTag);
		if (content != null)
		{
			RequestCenter.getQuotationDetail(quotationDetailCallback, rfqId, content.id);
		}
	}

	private DisposeDataListener quotationDetailCallback = new DisposeDataListener()
	{
		@Override
		public void onSuccess(Object obj)
		{

		}

		@Override
		public void onFailure(Object failedReason)
		{

		}
	};

	/**
	 * 请求报价列表数据回调
	 */
	private DisposeDataListener quotationListCallback = new DisposeDataListener()
	{
		@Override
		public void onSuccess(Object result)
		{
			refreshComplete();
			theQuotationListBean = (QuotationList) result;
			quotationListAdapter.setTheQuotationListBean(theQuotationListBean);
			quotationListAdapter.notifyDataSetChanged();
			pullToListView.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
		}

		@Override
		public void onFailure(Object failedReason)
		{
			refreshComplete();
			ToastUtil.toast(RFQQuotationListActivity.this, failedReason);
			pullToListView.setVisibility(View.GONE);
			progressBar.setVisibility(View.GONE);
		}
	};

	/**
	 * 刷新报价详情界面，将新数据传入后更新
	 * @param quotationContent
	 */
	private void refreshQuotationUI(QuotationListContent quotationContent)
	{
		companyNameTv.setText(quotationContent.quoteCompanyName);
		createSupplierTable(quotationContent);
		createQuotationTable(quotationContent);
	}

	private void createSupplierTable(QuotationListContent quotationContent)
	{
		/**
		 *  如果此表格已经有内容，则先删除，再创建，解决表格数据叠加问题
		 */
		supplierInfoLayout.removeOldData();
		ArrayList<ProductKeyValuePair> supplierInfo = new ArrayList<ProductKeyValuePair>();
		supplierInfo.add(new ProductKeyValuePair("Contact Person", quotationContent.quoteName));
		supplierInfo.add(new ProductKeyValuePair("Email", quotationContent.quoteEmail));
		supplierInfo.add(new ProductKeyValuePair("System Certification", quotationContent.systemCertification));
		supplierInfo.add(new ProductKeyValuePair("Audited Supplier for", getSupplierTime(quotationContent)));
		supplierInfo.add(new ProductKeyValuePair("Business Type", Util.getAllListValue(quotationContent.supplierType)));
		supplierInfoLayout.initParams(supplierInfo, "Supplier Info", screenWidth / 3);
		supplierInfoLayout.createOne2OneTable();
	}

	private void createQuotationTable(QuotationListContent quotationContent)
	{
		quotationAllLayout.removeAllViews();
		// quotationLayout.removeOldData();
		// ArrayList<ProductKeyValuePair> quotationInfo = new ArrayList<ProductKeyValuePair>();
		// quotationInfo.add(new ProductKeyValuePair("Product Name", quotationContent.items.get(0).productName));
		// quotationInfo.add(new ProductKeyValuePair("Unit Price", quotationContent.items.get(0).unitPriceType + " "
		// + quotationContent.items.get(0).unitPrice + "/Piece"));
		// quotationInfo.add(new ProductKeyValuePair("Min.Order Quantity", quotationContent.items.get(0).minOrder + " "
		// + quotationContent.items.get(0).minOrderType));
		// quotationInfo.add(new ProductKeyValuePair("Lead Time", quotationContent.items.get(0).leadDays + "Day(s)"));
		// quotationInfo.add(new ProductKeyValuePair("Delivery Method",
		// quotationContent.items.get(0).deliveryMethodDis));
		// quotationInfo.add(new ProductKeyValuePair("Quality Inspection",
		// quotationContent.items.get(0).qualityInspectionDis));
		// quotationLayout.initParams(quotationInfo, "Quotation", screenWidth / 3);
		// quotationLayout.createOne2OneTable();

		for (int i = 0; i < quotationContent.items.size(); i++)
		{
			Log.e("===========", quotationContent.items.size() + "");
			if (quotationContent.items.get(i) != null)
			{
				createQuotationItem(quotationContent.items.get(i), i);
			}
		}
	}

	/**
	 * 绘制每条不同的报价，可能有多条情况
	 * @param item
	 * @param order
	 */
	private void createQuotationItem(QuotationListItem item, int order)
	{
		NormalTableLayout quotationLayout = new NormalTableLayout(this);
		TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
				TableLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(0, Util.dip2px(18), 0, 0);
		quotationLayout.setLayoutParams(params);

		ArrayList<ProductKeyValuePair> quotationInfo = new ArrayList<ProductKeyValuePair>();
		quotationInfo.add(new ProductKeyValuePair("Product Name", item.productName));
		quotationInfo.add(new ProductKeyValuePair("Unit Price", item.unitPrice + " " + item.unitPriceType));
		quotationInfo.add(new ProductKeyValuePair("Min.Order Quantity", item.minOrder + " " + item.minOrderType));
		quotationInfo.add(new ProductKeyValuePair("Lead Time", item.leadDays + "Day(s)"));
		quotationInfo.add(new ProductKeyValuePair("Delivery Method", item.deliveryMethodDis));
		quotationInfo.add(new ProductKeyValuePair("Quality Inspection", item.qualityInspectionDis));
		quotationLayout.initParams(quotationInfo, "Quotation " + (order + 1), screenWidth / 3);
		quotationLayout.createOne2OneTable();
		quotationAllLayout.addView(quotationLayout);
	}

	/**
	 * 获取公司详情
	 * @param comId
	 */
	private void requestCompanyDetail(String comId)
	{
		if (!isFinishing())
		{
			CommonProgressDialog.getInstance().showCancelableProgressDialog(this, getString(R.string.mic_loading));
		}
		RequestCenter.getCompanyDetail(companyDetailCallback, comId);
	}

	/**
	 * 获取公司信息详情数据回调
	 */
	private DisposeDataListener companyDetailCallback = new DisposeDataListener()
	{
		@Override
		public void onSuccess(Object result)
		{
			if (isFinishing())
				return;
			CommonProgressDialog.getInstance().dismissProgressDialog();
			CompanyDetail companyDetailDate = (CompanyDetail) result;
			Intent intent = new Intent(RFQQuotationListActivity.this, MailSendActivity_.class);
			intent.putExtra("companyName", companyDetailDate.content.companyName);
			intent.putExtra("mailSendTarget", MailSendTarget.getValue(MailSendTarget.SendByCompanyId));
			intent.putExtra("subject", rfqSubject);
			intent.putExtra("companyId", companyDetailDate.content.companyId);
			intent.putExtra("isReplayQuotation", true);
			startActivity(intent);
		}

		@Override
		public void onFailure(Object failedReason)
		{
			if (isFinishing())
				return;
			CommonProgressDialog.getInstance().dismissProgressDialog();
			ToastUtil.toast(RFQQuotationListActivity.this, failedReason);
		}
	};

	/**
	 * 删除列表数据
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
				ToastUtil.toast(RFQQuotationListActivity.this, failedReason);
			}
		}, rfqId);
	}

	/**
	 * 私有方法，得到供应商供应时间
	 * @param quotationContent
	 * @return
	 */
	private String getSupplierTime(QuotationListContent quotationContent)
	{
		String auditedSupplierForNTimeNo = "";
		if ("0".equals(quotationContent.asAuditTimes))
		{
			auditedSupplierForNTimeNo = quotationContent.asAuditTimes;
		}
		else if ("1".equals(quotationContent.asAuditTimes))
		{
			auditedSupplierForNTimeNo = quotationContent.asAuditTimes + "st";
		}
		else if ("2".equals(quotationContent.asAuditTimes))
		{
			auditedSupplierForNTimeNo = quotationContent.asAuditTimes + "nd";
		}
		else if ("3".equals(quotationContent.asAuditTimes))
		{
			auditedSupplierForNTimeNo = quotationContent.asAuditTimes + "rd";
		}
		else
		{
			auditedSupplierForNTimeNo = quotationContent.asAuditTimes + "th";
		}
		return auditedSupplierForNTimeNo + " Time";
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu)
	{
		if (!PopupManager.getInstance().isShowing())
		{
			// HomeMenuView.getInstance().showNewSysMenu(this, titleRightButton3);
		}
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add("menu");
		return super.onCreateOptionsMenu(menu);
	}
}

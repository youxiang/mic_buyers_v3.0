package com.micen.buyers.activity.rfq;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.focustech.common.mob.DisposeDataListener;
import com.focustech.common.util.ToastUtil;
import com.focustech.common.util.Utils;
import com.focustech.common.widget.dialog.CommonProgressDialog;
import com.micen.buyers.activity.R;
import com.micen.buyers.adapter.rfq.PostRFQBusinessTypeAdapter;
import com.micen.buyers.adapter.rfq.PostRFQExportMarketsAdapter;
import com.micen.buyers.constant.Constants;
import com.micen.buyers.constant.UrlConstants;
import com.micen.buyers.http.RequestCenter;
import com.micen.buyers.manager.PopupManager;
import com.micen.buyers.manager.RFQKeyListener;
import com.micen.buyers.manager.SysManager;
import com.micen.buyers.module.rfq.RFQContent;
import com.micen.buyers.module.rfq.RFQContentFiles;
import com.micen.buyers.module.rfq.RFQContentValidateTimeEnd;
import com.micen.buyers.util.BuyerApplication;
import com.micen.buyers.util.Util;
import com.micen.buyers.view.CustomListView;

/**********************************************************
 * @文件名称：RFQEditActivity.java
 * @文件作者：xiongjiangwei
 * @创建时间：2014-1-24 下午02:15:36
 * @文件描述：编辑RFQ页面
 * @修改历史：2014-1-24创建初始版本
 **********************************************************/
@EActivity
public class RFQEditActivity extends RFQPostBaseActivity
{
	private TextView requirementsToSupplierText;
	private TextView requirementsForTradingText;
	@ViewById(R.id.mic_rfq_requirementsToSupplier_layout_underline)
	protected View requirementsToSupplierUnderLine;
	@ViewById(R.id.mic_rfq_location)
	protected TextView locationText;
	@ViewById(R.id.mic_rfq_numbersOfEmployees)
	protected TextView numbersEmployeesText;
	@ViewById(R.id.mic_rfq_edit_companyCertification)
	protected EditText companyCerEdit;
	@ViewById(R.id.mic_rfq_businessType)
	protected TextView businessTypeText;
	@ViewById(R.id.mic_rfq_exportMarkets)
	protected TextView exportMarketsText;

	private RFQContent rfqContent;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Constants.currentActivity = this;
		if (getIntent() != null && getIntent().hasExtra("rfqDetail"))
		{
			rfqContent = (RFQContent) getIntent().getSerializableExtra("rfqDetail");
		}
		initChildData();
		setPostRFQButtonStatus();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		Constants.selectedExportMarket.clear();
		Constants.selectedBusinessType.clear();
	}

	@Override
	protected void initChildView()
	{
		requirementsToSupplierText = (TextView) requirementsToSupplierLayout.getChildAt(1);
		setRightCompoundDrawable(R.drawable.mic_rfq_optional_arrow_up, requirementsToSupplierText);
		requirementsToSupplierText.setText(R.string.mic_rfq_edit_supplier);
		requirementsToSupplierLayout.setOnClickListener(this);
		requirementsToSupplierLayout.setBackgroundDrawable(Utils.setImageButtonState(R.color.mic_rfq_expand_bg,
				R.color.mic_rfq_title_bg, this));

		requirementsForTradingText = (TextView) requirementsForTradingLayout.getChildAt(1);
		setRightCompoundDrawable(R.drawable.mic_rfq_optional_arrow_up, requirementsForTradingText);
		requirementsForTradingText.setText(R.string.mic_rfq_requirementsForTrading);
		requirementsForTradingLayout.setOnClickListener(this);
		requirementsForTradingLayout.setBackgroundDrawable(Utils.setImageButtonState(R.color.mic_rfq_expand_bg,
				R.color.mic_rfq_title_bg, this));
		paymentTermsEdit.setVisibility(View.VISIBLE);

		locationText.setOnClickListener(this);
		numbersEmployeesText.setOnClickListener(this);
		companyCerEdit.setKeyListener(RFQKeyListener.getInstance(Constants.rfqEditTextDigits));
		businessTypeText.setOnClickListener(this);
		exportMarketsText.setOnClickListener(this);

		setRightCompoundDrawable(R.drawable.mic_rfq_optional_arrow_up, optionalText);
		moreMsgLayout.setVisibility(View.VISIBLE);
	}

	private void initChildData()
	{
		if (rfqContent == null)
			return;
		productNameEdit.setText(rfqContent.subject);
		categoryText.setText(rfqContent.category);
		categoryText.setTag(rfqContent.categoryId);
		purchaseQuantityEdit.setText(rfqContent.estimatedQuantity);
		piecesEdit.setText(rfqContent.estimatedQuantityType);
		initExpireDate();
		descriptionEdit.setText(rfqContent.detailDescription);
		setEditTextCursorPosition(descriptionEdit);
		if (rfqContent.rfqFiles != null && rfqContent.rfqFiles.size() != 0)
		{
			for (int i = 0; i < rfqContent.rfqFiles.size(); i++)
			{
				captureLayout.addView(createCaptureChildView(rfqContent.rfqFiles.get(i)));
			}
			if (rfqContent.rfqFiles.size() < 3)
			{
				captureLayout.addView(createCaptureEmptyChildView());
			}
		}
		else
		{
			captureLayout.addView(createCaptureEmptyChildView());
		}
		if (Utils.isEmpty(rfqContent.supplierLocation) && Utils.isEmpty(rfqContent.supplierEmployeesType)
				&& Utils.isEmpty(rfqContent.supplierCertification) && Utils.isEmpty(rfqContent.supplierType)
				&& Utils.isEmpty(rfqContent.supplierTypeOther) && Utils.isEmpty(rfqContent.exportMarket))
		{
			setRightCompoundDrawable(R.drawable.mic_rfq_optional_arrow, requirementsToSupplierText);
			supplierLayout.setVisibility(View.GONE);
			requirementsToSupplierUnderLine.setVisibility(View.GONE);
		}
		else
		{
			supplierLayout.setVisibility(View.VISIBLE);
			locationText.setText(rfqContent.supplierLocation);
			numbersEmployeesText.setText(rfqContent.supplierEmployeesType);
			companyCerEdit.setText(rfqContent.supplierCertification);
			String types = getAllListData(rfqContent.supplierType);
			if (!"".equals(types) && !Utils.isEmpty(rfqContent.supplierTypeOther))
			{
				types = types + ",";
			}
			types = types + rfqContent.supplierTypeOther;
			businessTypeText.setText(types);
			exportMarketsText.setText(getAllListData(rfqContent.exportMarket));
			if (rfqContent.exportMarket != null && rfqContent.exportMarket.size() > 0)
			{
				for (int i = 0; i < rfqContent.exportMarket.size(); i++)
				{
					Constants.selectedExportMarket.add(rfqContent.exportMarket.get(i));
				}
			}
		}
		if (Utils.isEmpty(rfqContent.shipmentTerms) && Utils.isEmpty(rfqContent.paymentTerms))
		{
			setRightCompoundDrawable(R.drawable.mic_rfq_optional_arrow, requirementsForTradingText);
			tradingLayout.setVisibility(View.GONE);
		}
		else
		{
			tradingLayout.setVisibility(View.VISIBLE);
			shipmentTermsText.setText(rfqContent.shipmentTerms);
			shipmentTermsLayout.setVisibility(View.VISIBLE);
			fobText.setText(rfqContent.shipmentTerms);
			targetPriceEdit.setText(rfqContent.targetPrice);
			usdEdit.setText(rfqContent.targetPriceUnit);
			if (isShowDestinationPort(rfqContent.shipmentTerms))
			{
				destinationPortEdit.setText(rfqContent.destinationPort);
				destinationPortEdit.setVisibility(View.VISIBLE);
			}
			else
			{
				destinationPortEdit.setVisibility(View.GONE);
			}
			paymentTermsEdit.setText(rfqContent.paymentTerms);
		}
	}

	/**
	 * 初始化过期时间
	 */
	private void initExpireDate()
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Util.getLocale());
		try
		{
			Date dt = new Date();
			try
			{
				dt = new Date(Long.parseLong(rfqContent.validateTimeEnd.time));
			}
			catch (NumberFormatException e)
			{
				e.printStackTrace();
			}
			if (formatter.parse(formatter.format(dt)).after(Util.getMinTime()))
			{
				expiredDateText.setText(Util.formatDateToEn(rfqContent.validateTimeEnd.time));
			}
			else
			{
				initExpireDate(Constants.DAY_OFFSET);
			}
		}
		catch (ParseException e)
		{
			initExpireDate(Constants.DAY_OFFSET);
			e.printStackTrace();
		}
	}

	private String getAllListData(ArrayList<String> dataList)
	{
		String value = "";
		if (dataList == null)
			return value;
		for (int i = 0; i < dataList.size(); i++)
		{
			value = value + dataList.get(i) + (i == dataList.size() - 1 ? "" : ",");
		}
		return value;
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
			break;
		case R.id.mic_rfq_requirementsToSupplier_layout:
			boolean isSupplierLayoutGone = supplierLayout.getVisibility() == View.GONE ? true : false;
			setRightCompoundDrawable(isSupplierLayoutGone ? R.drawable.mic_rfq_optional_arrow_up
					: R.drawable.mic_rfq_optional_arrow, requirementsToSupplierText);
			requirementsToSupplierUnderLine.setVisibility(isSupplierLayoutGone ? View.VISIBLE : View.GONE);
			supplierLayout.setVisibility(isSupplierLayoutGone ? View.VISIBLE : View.GONE);
			if (isSupplierLayoutGone)
			{
				scrollToBottom();
			}
			break;
		case R.id.mic_rfq_requirementsForTrading_layout:
			boolean isTradingLayoutGone = tradingLayout.getVisibility() == View.GONE ? true : false;
			setRightCompoundDrawable(isTradingLayoutGone ? R.drawable.mic_rfq_optional_arrow_up
					: R.drawable.mic_rfq_optional_arrow, requirementsForTradingText);
			tradingLayout.setVisibility(isTradingLayoutGone ? View.VISIBLE : View.GONE);
			if (isTradingLayoutGone)
			{
				scrollToBottom();
			}
			break;
		case R.id.mic_rfq_location:
			SysManager.getInstance().dismissInputKey(this);
			PopupManager.getInstance().showRFQPopup(
					createPopupView(
							createStringListPopupView(createStringListPopupAdapter(R.array.mic_rfq_location),
									locationItemClick), popupBgClick));
			break;
		case R.id.mic_rfq_numbersOfEmployees:
			SysManager.getInstance().dismissInputKey(this);
			PopupManager.getInstance().showRFQPopup(
					createPopupView(
							createStringListPopupView(createStringListPopupAdapter(R.array.mic_rfq_number_employees),
									numberEmployeesItemClick), popupBgClick));
			break;
		case R.id.mic_rfq_pieces:
			SysManager.getInstance().dismissInputKey(this);
			PopupManager.getInstance().showRFQPopup(
					createPopupView(
							createStringListPopupView(createStringListPopupAdapter(R.array.mic_rfq_quantity_unit_edit),
									unitPopupItemClick), popupBgClick));
			break;
		case R.id.mic_rfq_usd:
			SysManager.getInstance().dismissInputKey(this);
			PopupManager.getInstance().showRFQPopup(
					createPopupView(
							createStringListPopupView(createStringListPopupAdapter(R.array.mic_rfq_price_unit_edit),
									usdItemClick), popupBgClick));
			break;
		case R.id.mic_rfq_paymentTerms:
			SysManager.getInstance().dismissInputKey(this);
			PopupManager.getInstance().showRFQPopup(
					createPopupView(
							createStringListPopupView(createStringListPopupAdapter(R.array.mic_rfq_payment_terms),
									paymentItemClick), popupBgClick));
			break;
		case R.id.mic_rfq_exportMarkets:
			SysManager.getInstance().dismissInputKey(this);
			PopupManager.getInstance().showRFQPopup(
					createPopupView(createExportMarketsPopupView(), exportMarketsPopupBgClick));
			break;
		case R.id.mic_rfq_businessType:
			SysManager.getInstance().dismissInputKey(this);
			businessTypeLayout = createPopupView(createBusinessTypePopupView(), businessTypePopupBgClick);
			baseLayout.removeView(businessTypeLayout);
			baseLayout.addView(businessTypeLayout);
			break;
		}
	}

	private RelativeLayout businessTypeLayout;

	/**
	 * 创建交易类型弹出布局
	 * @return
	 */
	protected ScrollView createBusinessTypePopupView()
	{
		ScrollView contentView = new ScrollView(this);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		contentView.setLayoutParams(params);

		LinearLayout contentLayout = new LinearLayout(this);
		contentLayout.setBackgroundResource(R.color.mic_white);
		contentLayout.setOrientation(LinearLayout.VERTICAL);

		CustomListView listView = new CustomListView(this);
		LayoutParams listViewParams = new LayoutParams(popupViewWidth, LayoutParams.WRAP_CONTENT);
		listView.setLayoutParams(listViewParams);
		listView.setCacheColorHint(0);
		listView.setSelector(getResources().getDrawable(R.color.transparent));
		listView.setDivider(getResources().getDrawable(R.color.mic_menu_line));
		listView.setDividerHeight(Util.dip2px(0.5f));
		PostRFQBusinessTypeAdapter adapter = new PostRFQBusinessTypeAdapter(this, getResources().getStringArray(
				R.array.mic_rfq_business_type), mHandler);
		listView.setAdapter(adapter);
		contentLayout.addView(listView);

		View line = new View(this);
		LayoutParams lineParams = new LayoutParams(LayoutParams.MATCH_PARENT, Util.dip2px(0.5f));
		line.setBackgroundResource(R.color.mic_menu_line);
		line.setLayoutParams(lineParams);
		contentLayout.addView(line);

		String value = initBusinessType();
		LinearLayout otherLayout = new LinearLayout(this);
		otherLayout.setOrientation(LinearLayout.VERTICAL);
		otherLayout.setVisibility("".equals(value) ? View.GONE : View.VISIBLE);

		EditText otherEdit = new EditText(this);
		otherEdit.setId(R.id.mic_rfq_business_type_edit);
		LayoutParams otherEditParams = new LayoutParams(LayoutParams.MATCH_PARENT, Util.dip2px(30));
		otherEditParams.leftMargin = otherEditParams.rightMargin = Util.dip2px(10);
		otherEditParams.topMargin = otherEditParams.bottomMargin = Util.dip2px(5);
		otherEdit.setLayoutParams(otherEditParams);
		otherEdit.setPadding(Util.dip2px(10), 0, Util.dip2px(10), 0);
		otherEdit.setBackgroundResource(R.drawable.mic_rfq_category_search_bg);
		otherEdit.setTextSize(12);
		otherEdit.setSingleLine(true);
		otherEdit.setTextColor(getResources().getColor(R.color.mic_home_menu_text));
		otherEdit.setKeyListener(RFQKeyListener.getInstance(Constants.rfqEditTextDigits));
		otherEdit.setText("".equals(value) ? "" : value);
		otherEdit.setFilters(new InputFilter[]
		{ new InputFilter.LengthFilter(50) });
		otherLayout.addView(otherEdit);

		View line1 = new View(this);
		LayoutParams lineParams1 = new LayoutParams(LayoutParams.MATCH_PARENT, Util.dip2px(0.5f));
		line1.setBackgroundResource(R.color.mic_menu_line);
		line1.setLayoutParams(lineParams1);
		otherLayout.addView(line1);

		contentLayout.addView(otherLayout);

		TextView buttonText = new TextView(this);
		LayoutParams textParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		buttonText.setLayoutParams(textParams);
		buttonText.setGravity(Gravity.CENTER);
		buttonText.setPadding(0, Util.dip2px(15), 0, Util.dip2px(15));
		buttonText.setTextColor(getResources().getColor(R.color.mic_common_title_bg));
		buttonText.setTextSize(14);
		buttonText.setText(R.string.mic_confirm);
		buttonText.setBackgroundResource(R.drawable.mic_common_menu_item_bg);
		buttonText.setOnClickListener(businessTypeSelectComplete);
		contentLayout.addView(buttonText);

		contentView.addView(contentLayout);
		return contentView;
	}

	private String initBusinessType()
	{
		if (!"".equals(businessTypeText.getText().toString()))
		{
			String[] selectValues = businessTypeText.getText().toString().split(",");
			Constants.selectedBusinessType.clear();
			for (int i = 0; i < selectValues.length; i++)
			{
				Constants.selectedBusinessType.add(selectValues[i]);
			}
		}
		String value = "";
		String[] localValues = getResources().getStringArray(R.array.mic_rfq_business_type);
		boolean isHave = false;
		for (int i = 0; i < Constants.selectedBusinessType.size(); i++)
		{
			isHave = false;
			for (int j = 0; j < localValues.length; j++)
			{
				if (Constants.selectedBusinessType.get(i).equals(localValues[j]))
				{
					isHave = true;
					break;
				}
			}
			if (!isHave)
			{
				value = value + Constants.selectedBusinessType.get(i) + ",";
			}
		}
		if (!"".equals(value))
		{
			value = value.substring(0, value.lastIndexOf(","));
			Constants.selectedBusinessType.add(getString(R.string.mic_rfq_other));
		}
		return value;
	}

	private OnClickListener businessTypeSelectComplete = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			SysManager.getInstance().dismissInputKey(RFQEditActivity.this);
			String value = "";
			ListView listView = (ListView) ((LinearLayout) v.getParent()).getChildAt(0);
			CheckBox cb = null;
			boolean isSelectOther = false;
			for (int i = 0; i < listView.getChildCount(); i++)
			{
				cb = (CheckBox) listView.getChildAt(i).findViewById(R.id.mic_rfq_checkbox);
				if (cb != null && cb.isChecked())
				{
					if (listView.getAdapter().getItem(i).toString().equals(getString(R.string.mic_rfq_other)))
					{
						isSelectOther = true;
						continue;
					}
					else
					{
						value = value + listView.getAdapter().getItem(i).toString() + ",";
					}
				}
			}
			if (isSelectOther)
			{
				EditText otherEdit = (EditText) ((LinearLayout) v.getParent())
						.findViewById(R.id.mic_rfq_business_type_edit);
				if (!"".equals(otherEdit.getText().toString().trim()))
				{
					value = value + otherEdit.getText().toString() + ",";
					Constants.selectedBusinessType.add(otherEdit.getText().toString());
				}
			}
			if (!"".equals(value))
			{
				value = value.substring(0, value.lastIndexOf(","));
			}
			businessTypeText.setText(value);
			baseLayout.removeView(businessTypeLayout);
		}
	};

	private OnClickListener businessTypePopupBgClick = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			String value = businessTypeText.getText().toString();
			Constants.selectedBusinessType.clear();
			if (!"".equals(value))
			{
				String[] selectValues = value.split(",");
				for (int i = 0; i < selectValues.length; i++)
				{
					Constants.selectedBusinessType.add(selectValues[i]);
				}
			}
			baseLayout.removeView(businessTypeLayout);
		}
	};

	/**
	 * 创建出口目的地弹出布局
	 * @return
	 */
	protected LinearLayout createExportMarketsPopupView()
	{
		LinearLayout contentLayout = new LinearLayout(this);
		contentLayout.setBackgroundResource(R.color.mic_white);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		contentLayout.setLayoutParams(params);
		contentLayout.setOrientation(LinearLayout.VERTICAL);

		ListView listView = new ListView(this);
		LayoutParams listViewParams = new LayoutParams(popupViewWidth, LayoutParams.WRAP_CONTENT);
		listView.setLayoutParams(listViewParams);
		listView.setCacheColorHint(0);
		listView.setSelector(getResources().getDrawable(R.color.transparent));
		listView.setDivider(getResources().getDrawable(R.color.mic_menu_line));
		listView.setDividerHeight(Util.dip2px(0.5f));
		PostRFQExportMarketsAdapter adapter = new PostRFQExportMarketsAdapter(this, getResources().getStringArray(
				R.array.mic_rfq_export_markets));
		listView.setAdapter(adapter);
		contentLayout.addView(listView);

		View line = new View(this);
		LayoutParams lineParams = new LayoutParams(LayoutParams.MATCH_PARENT, Util.dip2px(0.5f));
		line.setBackgroundResource(R.color.mic_menu_line);
		line.setLayoutParams(lineParams);
		contentLayout.addView(line);

		TextView buttonText = new TextView(this);
		LayoutParams textParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		buttonText.setLayoutParams(textParams);
		buttonText.setGravity(Gravity.CENTER);
		buttonText.setPadding(0, Util.dip2px(15), 0, Util.dip2px(15));
		buttonText.setTextColor(getResources().getColor(R.color.mic_common_title_bg));
		buttonText.setTextSize(14);
		buttonText.setText(R.string.mic_confirm);
		buttonText.setBackgroundResource(R.drawable.mic_common_menu_item_bg);
		buttonText.setOnClickListener(exportMarketSelectComplete);
		contentLayout.addView(buttonText);
		return contentLayout;
	}

	private OnClickListener exportMarketSelectComplete = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			String value = "";
			ListView listView = (ListView) ((LinearLayout) v.getParent()).getChildAt(0);
			CheckBox cb = null;
			for (int i = 0; i < listView.getChildCount(); i++)
			{
				cb = (CheckBox) listView.getChildAt(i).findViewById(R.id.mic_rfq_checkbox);
				if (cb != null
						&& cb.isChecked()
						&& !listView.getAdapter().getItem(i).toString()
								.equals(getString(R.string.mic_rfq_export_markets_all)))
				{
					value = value + listView.getAdapter().getItem(i).toString() + ",";
				}
			}
			if (!"".equals(value))
			{
				value = value.substring(0, value.lastIndexOf(","));
			}
			exportMarketsText.setText(value);
			PopupManager.getInstance().dismissPopup();
		}
	};

	private OnClickListener exportMarketsPopupBgClick = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			String value = exportMarketsText.getText().toString();
			Constants.selectedExportMarket.clear();
			if (!"".equals(value))
			{
				String[] selectValues = value.split(",");
				for (int i = 0; i < selectValues.length; i++)
				{
					Constants.selectedExportMarket.add(selectValues[i]);
				}
			}
			PopupManager.getInstance().dismissPopup();
		}
	};

	protected OnItemClickListener paymentItemClick = new OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
		{
			PopupManager.getInstance().dismissPopup();
			if (getString(R.string.mic_rfq_other).equals(arg0.getAdapter().getItem(arg2).toString()))
			{
				paymentTermsEdit.setText("");
				paymentTermsEdit.setFocusable(true);
				paymentTermsEdit.setFocusableInTouchMode(true);
				InputMethodManager inputManager = (InputMethodManager) paymentTermsEdit.getContext().getSystemService(
						Context.INPUT_METHOD_SERVICE);
				inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				paymentTermsEdit.requestFocus();
			}
			else
			{
				paymentTermsEdit.setText(arg0.getAdapter().getItem(arg2).toString());
				paymentTermsEdit.setFocusable(false);
				paymentTermsEdit.setFocusableInTouchMode(false);
			}
		}
	};

	private OnItemClickListener locationItemClick = new OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
		{
			locationText.setText(arg0.getAdapter().getItem(arg2).toString());
			PopupManager.getInstance().dismissPopup();
		}
	};

	private OnItemClickListener numberEmployeesItemClick = new OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
		{
			numbersEmployeesText.setText(arg0.getAdapter().getItem(arg2).toString());
			PopupManager.getInstance().dismissPopup();
		}
	};

	@Override
	protected void startPostRFQ()
	{
		RFQContent rfqContent = new RFQContent();
		rfqContent.rfqId = this.rfqContent.rfqId;
		rfqContent.operatorNo = BuyerApplication.getInstance().getUser().content.userInfo.operatorId;
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
					if (!files.fileId.contains(ADD_FILE_FLAG) && !files.fileId.contains(UPDATE_FILE_FLAG))
					{
						files.fileId = files.fileId + UPDATE_FILE_FLAG;
					}
					rfqContent.rfqFiles.add(files);
				}
			}
		}
		if (categoryText.getTag() != null)
		{
			rfqContent.categoryId = categoryText.getTag().toString();
		}
		if (!"".equals(locationText.getText().toString()))
		{
			rfqContent.supplierLocation = locationText.getText().toString();
		}
		if (!"".equals(numbersEmployeesText.getText().toString()))
		{
			rfqContent.supplierEmployeesType = numbersEmployeesText.getText().toString();
		}
		if (!"".equals(companyCerEdit.getText().toString().trim()))
		{
			rfqContent.supplierCertification = companyCerEdit.getText().toString();
		}
		if (!"".equals(businessTypeText.getText().toString()))
		{
			rfqContent.allSupplierType = businessTypeText.getText().toString();
		}
		if (!"".equals(exportMarketsText.getText().toString()))
		{
			rfqContent.allExportMarket = exportMarketsText.getText().toString();
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
		if (!"".equals(paymentTermsEdit.getText().toString()))
		{
			rfqContent.paymentTerms = paymentTermsEdit.getText().toString();
		}
		RequestCenter.addOrUpdateRFQ(postRFQListener, UrlConstants.UPDATE_RFQ, rfqContent);
	}

	private DisposeDataListener postRFQListener = new DisposeDataListener()
	{
		@Override
		public void onSuccess(Object result)
		{
			CommonProgressDialog.getInstance().dismissProgressDialog();
			if (Constants.reEditDetailActivity != null)
			{
				Constants.reEditDetailActivity.finish();
				Constants.reEditDetailActivity = null;
			}
			ToastUtil.toast(RFQEditActivity.this, R.string.mic_rfq_post_success);
			finish();
		}

		@Override
		public void onFailure(Object failedReason)
		{
			CommonProgressDialog.getInstance().dismissProgressDialog();
			toastResponseMsg(failedReason.toString());
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			if (businessTypeLayout != null && baseLayout.indexOfChild(businessTypeLayout) > -1)
			{
				baseLayout.removeView(businessTypeLayout);
			}
			else
			{
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case 0:// 显示BusinessType中的输入框
				if (businessTypeLayout != null)
				{
					View otherEditView = businessTypeLayout.findViewById(R.id.mic_rfq_business_type_edit);
					((LinearLayout) otherEditView.getParent()).setVisibility(View.VISIBLE);
					otherEditView.requestFocus();
					otherEditView.setFocusable(true);
					otherEditView.setFocusableInTouchMode(true);
					SysManager.getInstance().showInputKey(RFQEditActivity.this);
				}
				break;
			case 1:// 隐藏BusinessType中的输入框
				if (businessTypeLayout != null)
				{
					View otherEditView = businessTypeLayout.findViewById(R.id.mic_rfq_business_type_edit);
					SysManager.getInstance().dismissInputKey(RFQEditActivity.this);
					((LinearLayout) otherEditView.getParent()).setVisibility(View.GONE);
				}
				break;
			}
		};
	};
}

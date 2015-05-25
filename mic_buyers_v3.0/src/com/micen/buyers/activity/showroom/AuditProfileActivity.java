package com.micen.buyers.activity.showroom;

import java.util.ArrayList;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.focustech.common.mob.DisposeDataListener;
import com.focustech.common.util.ToastUtil;
import com.focustech.common.util.Utils;
import com.micen.buyers.activity.BaseActivity;
import com.micen.buyers.activity.R;
import com.micen.buyers.adapter.AuditProfileListAdapter;
import com.micen.buyers.constant.Constants;
import com.micen.buyers.http.RequestCenter;
import com.micen.buyers.module.auditprofile.ARDetails;
import com.micen.buyers.module.auditprofile.AuditSupplier;
import com.micen.buyers.module.product.ProductKeyValuePair;
import com.micen.buyers.view.SearchListProgressBar;

@EActivity(R.layout.activity_audit_profile)
public class AuditProfileActivity extends BaseActivity
{
	@ViewById(R.id.lv_audit_profile)
	protected ListView listView;
	
	@ViewById(R.id.progressbar_layout)
	protected SearchListProgressBar progressLayout;
	
	protected LinearLayout listHeaderView;
	protected TextView whatsAR;
	protected TextView ap_tv;
	
	private AuditProfileListAdapter adapter;
	private String copies = "0";
	private String companyId;
	private ArrayList<ProductKeyValuePair> dataList;

	private void initIntent()
	{
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		companyId = bundle.getString("companyId");
	}

	@AfterViews
	protected void initView()
	{
		titleLeftButton.setImageResource(R.drawable.ic_title_back);
		titleRightButton1.setVisibility(View.GONE);
		titleRightButton2.setVisibility(View.GONE);
		titleRightButton3.setVisibility(View.GONE);
		titleLeftButton.setOnClickListener(this);
		titleText.setText("Audit Profile");

		listHeaderView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.audit_profile_header, null);
		whatsAR = (TextView) listHeaderView.findViewById(R.id.tv_what_is_ar);
		ap_tv = (TextView) listHeaderView.findViewById(R.id.ap_title);
		whatsAR.setOnClickListener(this);
		listView.addHeaderView(listHeaderView);
		initIntent();
		startGetARDetail();
	}

	private void addDataToView(AuditSupplier ao)
	{
		if (null != copies)
		{
			if (Integer.parseInt(copies) <= 1)
			{
				ap_tv.setText(Html.fromHtml("<font color=#333333>The company has</font> " + "<font color=#c30013>"
						+ copies + "</font>"
						+ " <font color=#333333>copy of Audit Report,the following is the latest report.</font>"));
			}
			else
			{
				ap_tv.setText(Html.fromHtml("<font color=#333333>The company has</font> " + "<font color=#c30013>"
						+ copies + "</font>"
						+ " <font color=#333333>copies of Audit Report,the following is the latest report.</font>"));
			}
		}
		else
		{
			ap_tv.setText(Html.fromHtml("<font color=#333333>The company has</font> " + "<font color=#c30013>" + copies
					+ "</font>"
					+ " <font color=#333333>copy of Audit Report,the following is the latest report.</font>"));
		}

		if (dataList == null)
		{
			dataList = new ArrayList<ProductKeyValuePair>();
		}
		ProductKeyValuePair pair;
		if (!Utils.isEmpty(ao.companyName))
		{
			pair = new ProductKeyValuePair("Company", ao.companyName);
			dataList.add(pair);
		}
		if (!Utils.isEmpty(ao.sgsSerialNum))
		{
			pair = new ProductKeyValuePair("SGS serial No", ao.sgsSerialNum);
			dataList.add(pair);
		}
		if (!Utils.isEmpty(ao.reportType))
		{
			pair = new ProductKeyValuePair("Report Type", ao.reportType);
			dataList.add(pair);
		}
		if (!Utils.isEmpty(ao.auditDate))
		{
			pair = new ProductKeyValuePair("Audit Date", ao.auditDate);
			dataList.add(pair);
		}
		if (!Utils.isEmpty(ao.reportContent))
		{
			pair = new ProductKeyValuePair("Report Content", sqliteString(ao.reportContent));
			dataList.add(pair);
		}
		if (!Utils.isEmpty(ao.companyType))
		{
			pair = new ProductKeyValuePair("Company Type", ao.companyType);
			dataList.add(pair);
		}
		if (!Utils.isEmpty(ao.yearOfEstablished))
		{
			pair = new ProductKeyValuePair("Year of established", ao.yearOfEstablished);
			dataList.add(pair);
		}
		if (!Utils.isEmpty(ao.registeredCapital))
		{
			pair = new ProductKeyValuePair("Registerd Capital", ao.registeredCapital);
			dataList.add(pair);
		}
		if (!Utils.isEmpty(ao.products))
		{
			pair = new ProductKeyValuePair("Products", ao.products);
			dataList.add(pair);
		}
		if (!Utils.isEmpty(ao.numberOfEmployees))
		{
			pair = new ProductKeyValuePair("Number in Employees", ao.numberOfEmployees);
			dataList.add(pair);
		}
	}

	private String sqliteString(String str)
	{
		String string = "";
		if (null != str)
		{
			StringBuffer sb = new StringBuffer();
			String[] words = str.split(",");
			for (int i = 0; i < words.length - 1; i++)
			{
				sb.append("-" + words[i] + "\n");
			}
			sb.append("-" + words[words.length - 1]);
			string = sb.toString();
		}
		return string;
	}

	@Override
	public void onClick(View view)
	{
		super.onClick(view);
		switch (view.getId())
		{
		case R.id.common_title_back_button:
			this.finish();
			break;
		case R.id.tv_what_is_ar:
			Bundle bundle = new Bundle();
			bundle.putString(Constants.AR_AS_KEYWORD, Constants.AS_VALUES);
			Intent intent = new Intent(this, ArAsActivity_.class);
			intent.putExtras(bundle);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
			break;
		}
	}

	private DisposeDataListener dataListener = new DisposeDataListener()
	{
		@Override
		public void onSuccess(Object arg0)
		{
			progressLayout.setVisibility(View.GONE);
			ARDetails details = (ARDetails) arg0;
			if (null != details.code)
			{
				if ("10001".equals(details.code))
				{
					ToastUtil.toast(AuditProfileActivity.this, "Sorry! This audit profile is not available.");
					finish();
				}
				else
				{
					if (null != details.content)
					{
						if (null != details.content.copies)
						{
							copies = details.content.copies;
						}
						if (null != details.content.auditSupplier)
						{
							addDataToView(details.content.auditSupplier);
						}
						if (null != details.content.onsiteChecked)
						{
							addDataToView(details.content.onsiteChecked);
						}
						else if ((null == details.content.auditSupplier) && (null == details.content.onsiteChecked))
						{
							ap_tv.setText(Html
									.fromHtml("<font color=#333333>The company has</font> "
											+ "<font color=#c30013>"
											+ copies
											+ "</font>"
											+ " <font color=#333333>copy of Audit Report,the following is the latest report.</font>"));
							listView.setVisibility(View.GONE);
						}
						adapter = new AuditProfileListAdapter(AuditProfileActivity.this, dataList);
						listView.setAdapter(adapter);
					}
				}
			}
		}

		@Override
		public void onFailure(Object failedReason)
		{
			progressLayout.setVisibility(View.GONE);
			ToastUtil.toast(AuditProfileActivity.this, failedReason);
		}
	};

	private void startGetARDetail()
	{
		RequestCenter.getARDetail(dataListener, companyId);
	}
}

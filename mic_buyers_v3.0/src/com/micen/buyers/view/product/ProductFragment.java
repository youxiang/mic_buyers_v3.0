package com.micen.buyers.view.product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.focustech.common.mob.DisposeDataListener;
import com.focustech.common.util.LogUtil;
import com.focustech.common.util.ToastUtil;
import com.micen.buyers.activity.R;
import com.micen.buyers.activity.message.MailSendActivity_;
import com.micen.buyers.activity.showroom.ShowRoomActivity_;
import com.micen.buyers.constant.Constants;
import com.micen.buyers.http.RequestCenter;
import com.micen.buyers.manager.SysManager;
import com.micen.buyers.module.MailSendTarget;
import com.micen.buyers.module.product.ProductDetailContent;
import com.micen.buyers.module.product.ProductKeyValuePair;
import com.micen.buyers.module.showroom.CompanyContent;
import com.micen.buyers.module.showroom.CompanyDetail;
import com.micen.buyers.util.Util;
import com.micen.buyers.widget.other.NotifyScrollView;
import com.micen.buyers.widget.other.NotifyScrollView.OnScrollChangedListener;

public class ProductFragment extends Fragment implements OnClickListener
{
	protected ProductDetailContent detail;

	protected ProductDetailImageView productImageView;

	private NotifyScrollView notifyScrollView;

	private TextView nameText;

	private TextView minOrderText;

	private TextView getLatestPriceText;

	private TextView singlePriceText1;

	private TextView singlePriceText2;

	private LinearLayout unitPricesLayout;

	private ImageView expandImage;

	private FrameLayout imageLayout;

	private LinearLayout tradeInfoContainer;

	private LinearLayout basicInfoContainer;

	private LinearLayout additionalInfoContainer;

	private LinearLayout singlePriceLayout;

	private LinearLayout multiPriceLayout;

	private LinearLayout companyInfoLayout;

	private TextView companyNameText;

	private ImageView companyImage1, companyImage2, image3;

	private LinearLayout companyLocationLayout;

	private TextView companyAddressText;

	private TextView memberSinceText;

	private boolean isExpanded = false;

	private CompanyContent companyDetail;

	private static final float MIN_ALPHA_RATIO = 0.5F;

	private DisposeDataListener companyDataListener = new DisposeDataListener()
	{

		@Override
		public void onSuccess(Object arg0)
		{
			companyDetail = ((CompanyDetail) arg0).content;
			if (companyDetail != null && isAdded())
			{
				companyInfoLayout.setVisibility(View.VISIBLE);
				initCompanyLayout();
			}
		}

		@Override
		public void onFailure(Object failedReason)
		{
			ToastUtil.toast(getActivity(), failedReason);
		}
	};

	private View titleView;

	private OnScrollChangedListener onScrollChangedListener = new OnScrollChangedListener()
	{

		@Override
		public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt)
		{
			setTitleAlpha(t);
		}
	};

	private void setTitleAlpha(int t)
	{
		if (titleView == null)
		{
			return;
		}
		final int headerHeight = Math.abs(imageLayout.getHeight() - titleView.getHeight());
		final int minHeight = (int) (headerHeight * MIN_ALPHA_RATIO);
		final float ratio = (float) Math.min(Math.max(minHeight + t, minHeight), headerHeight) / headerHeight;
		final float finalRatio = Math.max(MIN_ALPHA_RATIO, ratio);
		final int newAlpha = (int) (finalRatio * 255);
		titleView.getBackground().setAlpha(newAlpha);
	}

	public ProductFragment()
	{
	}

	public ProductFragment(ProductDetailContent detail, View titleView)
	{
		this.detail = detail;
		this.titleView = titleView;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View root = inflater.inflate(R.layout.product_detail_layout, null);
		notifyScrollView = (NotifyScrollView) root.findViewById(R.id.notify_scroll_view);
		nameText = (TextView) root.findViewById(R.id.tv_product_detail_name);
		minOrderText = (TextView) root.findViewById(R.id.tv_min_order);
		getLatestPriceText = (TextView) root.findViewById(R.id.tv_query_price);
		unitPricesLayout = (LinearLayout) root.findViewById(R.id.unit_prices);
		expandImage = (ImageView) root.findViewById(R.id.iv_expand);
		imageLayout = (FrameLayout) root.findViewById(R.id.product_image_layout);
		tradeInfoContainer = (LinearLayout) root.findViewById(R.id.trade_info_container);
		basicInfoContainer = (LinearLayout) root.findViewById(R.id.basic_info_container);
		additionalInfoContainer = (LinearLayout) root.findViewById(R.id.additional_info_container);
		singlePriceLayout = (LinearLayout) root.findViewById(R.id.single_price_layout);
		multiPriceLayout = (LinearLayout) root.findViewById(R.id.multi_price_layout);
		singlePriceText1 = (TextView) root.findViewById(R.id.tv_single_price_1);
		singlePriceText2 = (TextView) root.findViewById(R.id.tv_single_price_2);
		companyInfoLayout = (LinearLayout) root.findViewById(R.id.company_info_layout);
		companyNameText = (TextView) root.findViewById(R.id.tv_company_name);
		companyImage1 = (ImageView) root.findViewById(R.id.image1);
		companyImage2 = (ImageView) root.findViewById(R.id.image2);
		image3 = (ImageView) root.findViewById(R.id.image3);
		companyLocationLayout = (LinearLayout) root.findViewById(R.id.company_location_layout);
		companyAddressText = (TextView) root.findViewById(R.id.company_address);
		memberSinceText = (TextView) root.findViewById(R.id.member_since);
		expandImage.setOnClickListener(this);
		companyInfoLayout.setOnClickListener(this);
		notifyScrollView.setOnScrollChangedListener(onScrollChangedListener);
		init();

		return root;
	}

	@Override
	public void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		// 事件统计$10023 产品详情页 页面事件
		SysManager.analysis(R.string.a_type_page, R.string.p10023);
	}

	private void init()
	{
		setTitleAlpha(0);
		nameText.setText(detail.name);
		initImageLayout();
		initOrderLayout();
		initBasicLayout();
		initAddtionalLayout();

		companyInfoLayout.setVisibility(View.GONE);
		RequestCenter.getCompanyDetail(companyDataListener, detail.companyId);
	}

	private void initCompanyLayout()
	{
		if (companyDetail != null)
		{
			final boolean isVip = "true".equals(companyDetail.isVIP);
			final boolean isGold = (isVip || "1".equals(companyDetail.memberType));
			final boolean isAuditSupplier = "1".equals(companyDetail.auditType);
			final boolean isOnsiteSupplier = "2".equals(companyDetail.auditType);
			final boolean isLicenseVerified = "3".equals(companyDetail.auditType);
			final boolean showAuditType = !"0".equals(companyDetail.auditType);

			companyImage1.setVisibility(isGold ? View.VISIBLE : View.GONE);
			companyImage2.setVisibility(showAuditType ? View.VISIBLE : View.GONE);
			if (isAuditSupplier)
			{
				companyImage2.setImageResource(R.drawable.ic_supplier_as);
			}
			else if (isOnsiteSupplier)
			{
				companyImage2.setImageResource(R.drawable.ic_supplier_oc);
			}
			else if (isLicenseVerified)
			{
				companyImage2.setImageResource(R.drawable.ic_supplier_lv);
			}

			companyNameText.setText(companyDetail.companyName);
			// TODO
			companyAddressText.setText((companyDetail.province.equals(companyDetail.city) ? ""
					: (companyDetail.city + ","))
					+ (companyDetail.country.equals(companyDetail.province) ? "" : (companyDetail.province + ","))
					+ companyDetail.country);
			memberSinceText.setText(getString(R.string.member_since, companyDetail.memberSince));
		}
	}

	private void initAddtionalLayout()
	{
		for (ProductKeyValuePair item : detail.additionalInfoList)
		{
			View paramLayout = LayoutInflater.from(getActivity()).inflate(R.layout.product_param_item, null);
			TextView param1Text = (TextView) paramLayout.findViewById(R.id.tv_param1);
			TextView param2Text = (TextView) paramLayout.findViewById(R.id.tv_param2);
			param1Text.setText(Html.fromHtml(item.key + ":"));
			param2Text.setText(Html.fromHtml(Util.replaceHtmlStr(String.valueOf(item.value))));
			additionalInfoContainer.addView(paramLayout);
		}
	}

	private void initBasicLayout()
	{
		for (ProductKeyValuePair item : detail.basicInfoList)
		{
			View paramLayout = LayoutInflater.from(getActivity()).inflate(R.layout.product_param_item, null);
			TextView param1Text = (TextView) paramLayout.findViewById(R.id.tv_param1);
			TextView param2Text = (TextView) paramLayout.findViewById(R.id.tv_param2);
			param1Text.setText(Html.fromHtml(item.key + ":"));
			param2Text.setText(Html.fromHtml(Util.replaceHtmlStr(String.valueOf(item.value))));
			basicInfoContainer.addView(paramLayout);
		}
	}

	private void initImageLayout()
	{
		productImageView = new ProductDetailImageView(getActivity(), Util.getHighResolutionPicture(detail.images),
				detail.catCode);
		imageLayout.addView(productImageView);
	}

	/**
	 * Initialize product order layout
	 */
	private void initOrderLayout()
	{
		minOrderText.setText(getString(R.string.product_order_unit, detail.orderUnit));
		getLatestPriceText.setOnClickListener(this);

		initPricesLayout();

		initTradeLayout();
	}

	private void initTradeLayout()
	{
		ArrayList<ProductKeyValuePair> tradeValueList = new ArrayList<ProductKeyValuePair>();
		for (ProductKeyValuePair item : detail.tradeInfoList)
		{
			if (filterInvalidKey(item.key) && !TextUtils.isEmpty(item.value.toString()))
			{
				tradeValueList.add(new ProductKeyValuePair(item.key, item.value));
			}
		}

		for (ProductKeyValuePair item : tradeValueList)
		{
			View paramLayout = LayoutInflater.from(getActivity()).inflate(R.layout.product_param_item, null);
			TextView param1Text = (TextView) paramLayout.findViewById(R.id.tv_param1);
			TextView param2Text = (TextView) paramLayout.findViewById(R.id.tv_param2);
			param1Text.setText(Html.fromHtml(item.key + ":"));
			param2Text.setText(Html.fromHtml(Util.replaceHtmlStr(String.valueOf(item.value))));
			tradeInfoContainer.addView(paramLayout);
		}
	}

	private static final String[] invalidTradeInfoKeys =
	{ "ProdPrice", "splitUnitPrice", "ProdPriceUnit", "orderUnit", "unitPrice", "Price" };

	private boolean filterInvalidKey(String key)
	{
		for (String invalidKey : invalidTradeInfoKeys)
		{
			if (invalidKey.equals(key))
				return false;
		}
		return true;
	}

	private void initPricesLayout()
	{
		singlePriceText2.setSelected(true);
		try
		{
			JSONObject tradeInfo = new JSONObject(detail.tradeInfo);
			JSONArray splitUnitPrice = tradeInfo.optJSONArray("splitUnitPrice");
			if (splitUnitPrice != null && splitUnitPrice.length() > 0)
			{
				String[] price;
				if (splitUnitPrice.length() == 1)
				{
					price = splitUnitPrice.getString(0).split(":");
					if (price.length == 2)
					{
						singlePriceLayout.setVisibility(View.VISIBLE);
						singlePriceText1.setText(price[0]);
						SpannableString builder = new SpannableString("$" + price[1]);
						builder.setSpan(new AbsoluteSizeSpan(10, true), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						singlePriceText2.setText(builder);
					}
				}
				else
				{
					List<String> pairs = new ArrayList<String>();
					for (int i = 0; i < splitUnitPrice.length(); i++)
					{
						pairs.add(splitUnitPrice.getString(i));
					}
					Collections.sort(pairs, new Comparator<String>()
					{

						@Override
						public int compare(String lhs, String rhs)
						{
							String[] lhsSplit = lhs.split(":");
							String[] rhsSplit = rhs.split(":");
							if (lhsSplit.length > 0 && rhsSplit.length > 0 && TextUtils.isDigitsOnly(lhsSplit[0])
									&& TextUtils.isDigitsOnly(rhsSplit[0]))
							{
								return Integer.parseInt(lhsSplit[0]) - Integer.parseInt(rhsSplit[0]);
							}
							return -1;
						}
					});
					LogUtil.d("yyx", Arrays.toString(pairs.toArray()));
					multiPriceLayout.setVisibility(View.VISIBLE);
					for (int i = 0; i < pairs.size(); i++)
					{
						price = pairs.get(i).split(":");
						if (price.length == 2)
						{
							UnitPriceView view = new UnitPriceView(getActivity(), price[0], price[1]);
							unitPricesLayout.addView(view);
						}
					}
				}
			}
			else
			{
				singlePriceLayout.setVisibility(View.VISIBLE);
				singlePriceText1.setText(R.string.negotiable);
				singlePriceText2.setVisibility(View.GONE);
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.tv_query_price:
			// 事件统计 95 点击快速询价 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c95);
			sendInquiry();
			break;
		case R.id.iv_expand:
			// 事件统计 97 查看隐藏产品属性 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c97);
			toggleExpandBtn();
			break;
		case R.id.company_info_layout:
			// 事件统计 98 查看公司展示厅（产品详情页） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c98);

			Intent i = new Intent(getActivity(), ShowRoomActivity_.class);
			i.putExtra("companyId", detail.companyId);
			startActivity(i);
			break;
		}
	}

	private void sendInquiry()
	{
		Intent intent = new Intent(getActivity(), MailSendActivity_.class);
		intent.putExtra("mailSendTarget", MailSendTarget.getValue(MailSendTarget.SendByProductId));
		intent.putExtra("subject", detail.name);
		intent.putExtra("companyName", detail.companyName);
		intent.putExtra("companyId", detail.companyId);
		intent.putExtra("productId", detail.productId);// 对产品发送询盘
		intent.putExtra("quiry_flag", Constants.INQ_P);// 对产品发送询盘
		intent.putExtra("isQuick", true);
		intent.putExtra("catCode", detail.catCode);
		intent.putExtra("content", getString(R.string.quick_price_head) + detail.name
				+ getString(R.string.quick_price_foot));
		startActivity(intent);
	}

	private void toggleExpandBtn()
	{
		isExpanded = !isExpanded;
		expandImage.setImageResource(isExpanded ? R.drawable.pull_expanded : R.drawable.pull_unexpanded);
		basicInfoContainer.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
		additionalInfoContainer.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
	}

}

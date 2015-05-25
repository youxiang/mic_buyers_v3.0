package com.micen.buyers.activity.showroom;

import java.util.ArrayList;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.focustech.common.mob.DisposeDataListener;
import com.micen.buyers.activity.BaseFragmentActivity;
import com.micen.buyers.activity.R;
import com.micen.buyers.activity.account.login.LoginActivity_;
import com.micen.buyers.activity.message.MailSendActivity_;
import com.micen.buyers.constant.Constants;
import com.micen.buyers.http.RequestCenter;
import com.micen.buyers.manager.FavouriteManager;
import com.micen.buyers.manager.FavouriteManager.FavouriteCallback;
import com.micen.buyers.manager.FavouriteManager.FavouriteType;
import com.micen.buyers.manager.SysManager;
import com.micen.buyers.module.LoginTarget;
import com.micen.buyers.module.MailSendTarget;
import com.micen.buyers.module.product.ProductKeyValuePair;
import com.micen.buyers.module.showroom.CompanyContent;
import com.micen.buyers.module.showroom.CompanyDetail;
import com.micen.buyers.module.showroom.CompanyProductGroup;
import com.micen.buyers.util.BuyerApplication;
import com.micen.buyers.util.ImageUtil;
import com.micen.buyers.util.Util;
import com.micen.buyers.view.product.ProductListFragment;
import com.micen.buyers.widget.other.DragLayout;
import com.micen.buyers.widget.other.DragLayout.OnStateChangeListener;
import com.micen.buyers.widget.other.DragLayout.State;

@EActivity(R.layout.activity_show_room)
public class ShowRoomActivity extends BaseFragmentActivity
{
	@ViewById(R.id.title_layout)
	protected View titleLayout;

	@ViewById(R.id.iv_logo)
	protected ImageView logo;

	@ViewById(R.id.tv_company_name)
	protected TextView companyNameText;

	@ViewById(R.id.company_address)
	protected TextView companyAddressText;

	@ViewById(R.id.member_since)
	protected TextView memberSinceText;

	@ViewById(R.id.iv_company_sence)
	protected ImageView companySenceImage;

	@ViewById(R.id.tv_company_profile)
	protected TextView companyProfileText;

	@ViewById(R.id.member_info_layout)
	protected LinearLayout memberInfoLayout;

	@ViewById(R.id.contact_details_layout)
	protected LinearLayout contactDetailLayout;

	@ViewById(R.id.top_layout)
	protected LinearLayout topLayout;

	@ViewById(R.id.image_btn)
	protected ImageView imageBtn;

	@ViewById(R.id.gm_as_layout)
	protected LinearLayout gmasLayout;

	@ViewById(R.id.audit_type_layout)
	protected LinearLayout auditTypeLayout;

	@ViewById(R.id.audit_type_text)
	protected TextView auditTypeTextView;

	@ViewById(R.id.iv_right)
	protected ImageView rightImage;

	@ViewById(R.id.btn_gold_member)
	protected TextView goldTextView;

	protected DragLayout dragLayout;

	private String companyId;

	protected CompanyContent companyDetail;

	protected int productGroupCount;

	private ProductListFragment productListFragment;

	private OnStateChangeListener dragLayoutStateChangeListener = new OnStateChangeListener()
	{

		@Override
		public void onMin()
		{
			// �¼�ͳ�� 107 �鿴��˾���� ����¼�
			SysManager.analysis(R.string.a_type_click, R.string.c107);

			// �¼�ͳ��$10026 ��˾����ҳ ҳ���¼�
			SysManager.analysis(R.string.a_type_page, R.string.p10026);
		}

		@Override
		public void onMax()
		{
			// �¼�ͳ��108 �鿴��Ʒ����ҳ��չʾ���� ����¼�
			SysManager.analysis(R.string.a_type_click, R.string.c108);

			titleLayout.setVisibility(View.GONE);
			productListFragment.showTitle(true);
			productListFragment.showToast = true;
		}

		@Override
		public void onNormal()
		{
			titleLayout.setVisibility(View.VISIBLE);

			if (productListFragment != null)
			{
				productListFragment.showTitle(false);
				productListFragment.changeGridMode();
				productListFragment.showToast = false;
			}
		}
	};

	@AfterViews
	protected void initViews()
	{
		dragLayout = (DragLayout) findViewById(R.id.root);
		dragLayout.setOnStateChangedListener(dragLayoutStateChangeListener);
		imageBtn.setOnClickListener(this);
		titleLayout.setBackgroundColor(Color.TRANSPARENT);
		titleLeftButton.setBackgroundResource(R.drawable.btn_showroom_back);
		titleRightButton2.setBackgroundResource(R.drawable.btn_showroom_favorite);
		titleRightButton3.setBackgroundResource(R.drawable.btn_showroom_mail);
		titleLeftButton.setOnClickListener(this);
		titleRightButton2.setOnClickListener(this);
		titleRightButton3.setOnClickListener(this);
		auditTypeLayout.setOnClickListener(this);
		initData();
	}

	@Override
	public void onBackPressed()
	{
		if (dragLayout.state == State.MAX)
		{
			changeToNormal();
		}
		else
		{
			super.onBackPressed();
		}
	}

	private DisposeDataListener companyDataListener = new DisposeDataListener()
	{
		@Override
		public void onSuccess(Object arg0)
		{
			if (isFinishing())
			{
				return;
			}
			companyDetail = ((CompanyDetail) arg0).content;
			if (null != companyDetail.productGroup)
			{
				if (companyDetail.productGroup.size() >= 1)
				{
					for (int i = 0; i < companyDetail.productGroup.size(); i++)
					{
						productGroupCount += Integer.parseInt(companyDetail.productGroup.get(i).num);
					}
					initProductGroup(productGroupCount);
				}
				BuyerApplication.getInstance().setProductGroup(companyDetail.productGroup);
			}
			// update ui
			updateUI();
		}

		@Override
		public void onFailure(Object failedReason)
		{
		}
	};

	private OnClickListener telephoneClickListener = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			if (v instanceof TextView)
			{
				String telephone = ((TextView) v).getText().toString();
				if (!TextUtils.isEmpty(telephone))
				{
					Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "+" + telephone));
					startActivity(intent);
				}
			}
		}
	};

	private OnClickListener webSiteClickListener = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			if (!TextUtils.isEmpty(companyDetail.homepage))
			{
				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(companyDetail.homepage));
				startActivity(i);
			}
		}
	};

	private void initProductGroup(int num)
	{
		CompanyProductGroup allGroup = new CompanyProductGroup();
		allGroup.name = "All";
		allGroup.num = String.valueOf(num);
		companyDetail.productGroup.add(0, allGroup);
	}

	protected void updateUI()
	{
		if (companyDetail != null)
		{
			// favorite
			FavouriteManager.getInstance().checkIsFavourite(this, titleRightButton2, FavouriteType.Company, companyId,
					new FavouriteCallback()
					{

						@Override
						public void onCallback(boolean result)
						{
							companyDetail.isFavorite = String.valueOf(result);
						}
					});

			// company name
			setCompanyName();

			// logo
			ImageUtil.getImageLoader().displayImage(companyDetail.logo, logo, ImageUtil.getSafeImageOptions());
			// location
			companyAddressText.setText((companyDetail.province.equals(companyDetail.city) ? ""
					: (companyDetail.city + ","))
					+ (companyDetail.country.equals(companyDetail.province) ? "" : (companyDetail.province + ","))
					+ companyDetail.country);
			// member since
			memberSinceText.setText(getString(R.string.member_since, companyDetail.memberSince));
			// member info
			ArrayList<ProductKeyValuePair> memeberInfo = new ArrayList<ProductKeyValuePair>();
			memeberInfo.add(new ProductKeyValuePair("Business Type", companyDetail.businessType));
			memeberInfo.add(new ProductKeyValuePair("Trade Mark", companyDetail.trademark));
			memeberInfo.add(new ProductKeyValuePair("Main Products", companyDetail.mainProducts));
			memeberInfo.add(new ProductKeyValuePair("Annual Turnover", companyDetail.annualTurnover));
			memeberInfo.add(new ProductKeyValuePair("Employees", companyDetail.employeeNumber));
			memeberInfo.add(new ProductKeyValuePair("Member Since", companyDetail.memberSince));
			memeberInfo.add(new ProductKeyValuePair("Last Logon Date", companyDetail.lastLoginDate));

			for (ProductKeyValuePair item : memeberInfo)
			{
				if (TextUtils.isEmpty(String.valueOf(item.value)))
				{
					continue;
				}
				View paramLayout = LayoutInflater.from(this).inflate(R.layout.product_param_item, null);
				TextView param1Text = (TextView) paramLayout.findViewById(R.id.tv_param1);
				TextView param2Text = (TextView) paramLayout.findViewById(R.id.tv_param2);
				param1Text.setTextColor(Color.parseColor(getString(R.color.color_d1d1d1)));
				param2Text.setTextColor(Color.parseColor(getString(R.color.color_f2f2f2)));
				param1Text.setText(Html.fromHtml(item.key + ":"));
				param2Text.setText(Html.fromHtml(Util.replaceHtmlStr(String.valueOf(item.value))));
				memberInfoLayout.addView(paramLayout);
			}
			// contact details

			ArrayList<ProductKeyValuePair> contactInfo = new ArrayList<ProductKeyValuePair>();
			contactInfo.add(new ProductKeyValuePair("Company Name", companyDetail.companyName));
			contactInfo.add(new ProductKeyValuePair("Contact Person", companyDetail.contactPerson));
			contactInfo.add(new ProductKeyValuePair("Position", companyDetail.position));
			contactInfo.add(new ProductKeyValuePair("Department", companyDetail.department));
			contactInfo.add(new ProductKeyValuePair("Telephone", companyDetail.telephone));
			contactInfo.add(new ProductKeyValuePair("Mobile", companyDetail.mobile));
			if (!TextUtils.isEmpty(companyDetail.fax))
			{
				contactInfo.add(new ProductKeyValuePair("Fax Number", companyDetail.fax));
			}
			contactInfo.add(new ProductKeyValuePair("Zip/Post Code", companyDetail.zipCode));
			contactInfo.add(new ProductKeyValuePair("Address", companyDetail.companyAddress));
			contactInfo
					.add(new ProductKeyValuePair("City/Province", companyDetail.city + "/" + companyDetail.province));
			contactInfo.add(new ProductKeyValuePair("Country/Region", companyDetail.country));
			if (!TextUtils.isEmpty(companyDetail.homepage))
			{
				contactInfo.add(new ProductKeyValuePair("Website", companyDetail.homepage));
			}

			for (ProductKeyValuePair item : contactInfo)
			{
				if (TextUtils.isEmpty(String.valueOf(item.value)))
				{
					continue;
				}
				View paramLayout = LayoutInflater.from(this).inflate(R.layout.product_param_item, null);
				TextView param1Text = (TextView) paramLayout.findViewById(R.id.tv_param1);
				TextView param2Text = (TextView) paramLayout.findViewById(R.id.tv_param2);
				param1Text.setTextColor(Color.parseColor(getString(R.color.color_d1d1d1)));
				if (item.key.equals("Telephone") || item.key.equals("Mobile") || item.key.equals("Website"))
				{
					param2Text.setTextColor(Color.parseColor(getString(R.color.color_86b9ff)));
					if (item.key.equals("Telephone") || item.key.equals("Mobile"))
					{
						param2Text.setOnClickListener(telephoneClickListener);
					}
					if (item.key.equals("Website"))
					{
						param2Text.setOnClickListener(webSiteClickListener);
					}
				}
				else
				{
					param2Text.setTextColor(Color.parseColor(getString(R.color.color_f2f2f2)));
				}
				param1Text.setText(Html.fromHtml(item.key + ":"));
				param2Text.setText(Html.fromHtml(Util.replaceHtmlStr(String.valueOf(item.value))));
				contactDetailLayout.addView(paramLayout);
			}

			// Company Sence
			if (!TextUtils.isEmpty(companyDetail.sence))
			{
				ImageUtil.getImageLoader().displayImage(companyDetail.sence, companySenceImage,
						ImageUtil.getSafeImageOptions());
			}
			else
			{
				companySenceImage.setVisibility(View.GONE);
			}
			// Company profile
			companyProfileText.setText(companyDetail.description);

			// init productlist fragment
			initProductListFragment();
		}
	}

	private void setCompanyName()
	{
		final boolean isVip = "true".equals(companyDetail.isVIP);
		final boolean isGold = (isVip || "1".equals(companyDetail.memberType));
		final boolean isAuditSupplier = "1".equals(companyDetail.auditType);
		final boolean isOnsiteSupplier = "2".equals(companyDetail.auditType);
		final boolean isLicenseVerified = "3".equals(companyDetail.auditType);
		final boolean showAuditType = !"0".equals(companyDetail.auditType);

		StringBuffer sb = new StringBuffer();

		ImageSpan vipSpan = new ImageSpan(this, R.drawable.icon_vip);
		ImageSpan goldSpan = new ImageSpan(this, R.drawable.ic_supplier_gold_member);

		if (isVip)
		{
			sb.append("[vip] ");
		}
		sb.append(companyDetail.companyName);

		if (!showAuditType)
		{
			gmasLayout.setVisibility(View.GONE);

			if (isGold)
			{
				sb.append("  [gold]");
			}
		}

		SpannableString builder = new SpannableString(sb.toString());
		if (isVip)
		{
			builder.setSpan(vipSpan, 0, "[vip]".length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		}
		if (!showAuditType && isGold)
		{
			builder.setSpan(goldSpan, sb.length() - "[gold]".length(), sb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		}

		companyNameText.setText(builder);

		Drawable left = getResources().getDrawable(R.drawable.ic_supplier_as);
		if (isAuditSupplier)
		{
			left = getResources().getDrawable(R.drawable.ic_supplier_as);
			auditTypeTextView.setText(R.string.audited_supplier);
		}
		else if (isOnsiteSupplier)
		{
			left = getResources().getDrawable(R.drawable.ic_supplier_oc);
			auditTypeTextView.setText(R.string.onsite_check);
		}
		else if (isLicenseVerified)
		{
			left = getResources().getDrawable(R.drawable.ic_supplier_lv);
			auditTypeTextView.setText(R.string.license_verified);
		}
		left.setBounds(0, 0, Util.dip2px(20), Util.dip2px(20));
		auditTypeTextView.setCompoundDrawables(left, null, null, null);

		left = getResources().getDrawable(R.drawable.ic_supplier_gold_member);
		left.setBounds(0, 0, Util.dip2px(20), Util.dip2px(20));
		goldTextView.setCompoundDrawables(left, null, null, null);
		rightImage.setVisibility(isAuditSupplier ? View.VISIBLE : View.GONE);
	}

	private void initProductListFragment()
	{
		productListFragment = new ProductListFragment(companyId, companyDetail.memberType,
				ProductListFragment.MODE_GRID, false, false, dragLayout);

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.layout2, productListFragment);
		transaction.commit();
	}

	private void initData()
	{
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		companyId = bundle.getString("companyId");
		RequestCenter.getCompanyDetail(companyDataListener, companyId);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.image_btn:
			SysManager.analysis(R.string.a_type_click, R.string.c109);

			changeToNormal();
			break;
		case R.id.common_title_back_button:
			finish();
			break;
		case R.id.common_title_right_button2:
			SysManager.analysis(R.string.a_type_click, R.string.c106);

			if (BuyerApplication.getInstance().getUser() == null)
			{
				Intent ins = new Intent(this, LoginActivity_.class);
				ins.putExtra("loginTarget", LoginTarget.getValue(LoginTarget.ClickForFavorite));
				startActivityForResult(ins, Constants.LOGIN_FAVOURITE);
				return;
			}
			addOrDelFavourite(v);
			break;
		case R.id.common_title_right_button3:

			SysManager.analysis(R.string.a_type_click, R.string.c105);
			sendMail();
			break;
		case R.id.audit_type_layout:
			if (companyDetail != null && !TextUtils.isEmpty(companyDetail.auditType)
					&& "1".equals(companyDetail.auditType))
			{
				Intent intent = new Intent(this, AuditProfileActivity_.class);
				intent.putExtra("companyId", companyId);
				startActivity(intent);
			}

			break;

		default:
			break;
		}
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		// �¼�ͳ��$10025 չʾ����ҳ ҳ���¼�
		SysManager.analysis(R.string.a_type_page, R.string.p10025);
	}

	private void addOrDelFavourite(View displayView)
	{
		if (companyDetail != null)
		{
			FavouriteManager.getInstance().addOrDelFavourite(this, displayView, FavouriteType.Company, companyId,
					companyDetail.companyName, companyDetail.isFavorite(), new FavouriteCallback()
					{
						@Override
						public void onCallback(boolean result)
						{
							companyDetail.isFavorite = String.valueOf(result);
						}
					});

		}
	}

	private void sendMail()
	{
		if (companyDetail != null)
		{
			Intent intent = new Intent(this, MailSendActivity_.class);
			intent.putExtra("mailSendTarget", MailSendTarget.getValue(MailSendTarget.SendByCompanyId));
			intent.putExtra("subject", companyDetail.companyName);
			intent.putExtra("companyId", companyId);
			intent.putExtra("companyName", companyDetail.companyName);
			startActivity(intent);
		}
	}

	public void changeToNormal()
	{
		dragLayout.normal();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Constants.LOGIN_FAVOURITE)
		{
			FavouriteManager.getInstance().checkIsFavourite(this, titleRightButton2, FavouriteType.Company, companyId,
					new FavouriteCallback()
					{
						@Override
						public void onCallback(boolean result)
						{
							if (result)
							{
								if (companyDetail != null)
								{
									companyDetail.isFavorite = String.valueOf(result);
								}
							}
							else
							{
								addOrDelFavourite(titleRightButton2);
							}
						}
					});
		}
	}

}

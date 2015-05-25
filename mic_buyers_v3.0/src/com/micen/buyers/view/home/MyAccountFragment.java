package com.micen.buyers.view.home;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.focustech.common.mob.DisposeDataListener;
import com.focustech.common.util.ToastUtil;
import com.focustech.common.util.Utils;
import com.micen.buyers.activity.R;
import com.micen.buyers.activity.account.FavoriteActivity_;
import com.micen.buyers.activity.account.MemberInfoActivity_;
import com.micen.buyers.activity.account.MessageActivity_;
import com.micen.buyers.activity.account.QuotationReceivedActivity_;
import com.micen.buyers.activity.account.SourcingRequestActivity_;
import com.micen.buyers.activity.account.login.LoginActivity_;
import com.micen.buyers.activity.account.setting.SettingActivity_;
import com.micen.buyers.http.RequestCenter;
import com.micen.buyers.manager.SysManager;
import com.micen.buyers.module.LoginTarget;
import com.micen.buyers.module.user.User;
import com.micen.buyers.util.BuyerApplication;
import com.micen.buyers.util.UserUtil;
import com.micen.buyers.util.Util;

/**********************************************************
 * @文件名称：MyAccountFragment.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年3月16日 下午3:02:08
 * @文件描述：个人中心Fragment
 * @修改历史：2015年3月16日创建初始版本
 **********************************************************/
@EFragment(R.layout.my_account_fragment_layout)
public class MyAccountFragment extends HomeBaseFragment implements OnClickListener
{
	@ViewById(R.id.my_account_pull_refresh_scrollview)
	protected SwipeRefreshLayout refreshLayout;
	@ViewById(R.id.bg_vo_title)
	protected ImageView bgTitle;
	@ViewById(R.id.btn_vo_setting)
	protected ImageView btnSetting;
	@ViewById(R.id.tv_vo_title)
	protected TextView title;
	@ViewById(R.id.tv_vo_unlogin)
	protected TextView btnLogin;
	@ViewById(R.id.tv_vo_login)
	protected TextView accountName;
	@ViewById(R.id.ll_vo_messages)
	protected LinearLayout btnMessage;
	@ViewById(R.id.tv_vo_message_num)
	protected TextView messageNum;
	@ViewById(R.id.ll_vo_quotations)
	protected LinearLayout btnQuotation;
	@ViewById(R.id.tv_vo_quotation_num)
	protected TextView quotationNum;
	@ViewById(R.id.tv_vo_sourcing)
	protected TextView btnSourcing;
	@ViewById(R.id.tv_vo_member)
	protected TextView btnMember;
	@ViewById(R.id.rl_vo_product)
	protected RelativeLayout btnProducts;
	@ViewById(R.id.rl_vo_supplier)
	protected RelativeLayout btnSuppliers;
	@ViewById(R.id.rl_vo_category)
	protected RelativeLayout btnCategories;
	@ViewById(R.id.tv_vo_product_num)
	protected TextView favProdNum;
	@ViewById(R.id.tv_vo_supplier_num)
	protected TextView favCompNum;
	@ViewById(R.id.tv_vo_category_num)
	protected TextView favCateNum;

	public MyAccountFragment()
	{

	}

	@AfterViews
	protected void initView()
	{
		refreshLayout.setOnRefreshListener(refreshListener);
		refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
				android.R.color.holo_orange_light, android.R.color.holo_red_light);

		Drawable drawable = getResources().getDrawable(R.drawable.bg_vo_title);
		float scale = drawable.getMinimumWidth() / (float) Utils.getWindowWidthPix(getActivity());
		bgTitle.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, (int) (drawable.getMinimumHeight() / scale)));

		setTextViewCompoundDrawable(title, R.drawable.ic_vo_title);
		btnSetting.setOnClickListener(this);
		btnLogin.setOnClickListener(this);
		btnMessage.setOnClickListener(this);
		btnQuotation.setOnClickListener(this);
		btnSourcing.setOnClickListener(this);
		btnMember.setOnClickListener(this);

		btnProducts.setOnClickListener(this);
		btnSuppliers.setOnClickListener(this);
		btnCategories.setOnClickListener(this);
	}

	private OnRefreshListener refreshListener = new OnRefreshListener()
	{

		@Override
		public void onRefresh()
		{
			if (BuyerApplication.getInstance().getUser() != null)
			{
				RequestCenter.getStaffProfile(staffProfileListener);
			}
			else
			{
				refreshLayout.setRefreshing(false);
			}
		}
	};

	private DisposeDataListener staffProfileListener = new DisposeDataListener()
	{

		@Override
		public void onSuccess(Object result)
		{
			BuyerApplication.getInstance().setUser((User) result);
			loadUserData();
			refreshLayout.setRefreshing(false);
		}

		public void onFailure(Object failedReason)
		{
			ToastUtil.toast(getActivity(), failedReason);
			refreshLayout.setRefreshing(false);
		};

	};

	@Override
	public void onResume()
	{
		super.onResume();
		// 事件统计$10005 VO首页 页面事件
		SysManager.analysis(R.string.a_type_page, R.string.p10005);
		loadUserData();
	}

	private void loadUserData()
	{
		if (BuyerApplication.getInstance().getUser() != null)
		{
			String unreadMail = BuyerApplication.getInstance().getUser().content.userInfo.unreadMail;
			if (!Utils.isEmpty(unreadMail) && !"0".equals(unreadMail))
			{
				messageNum.setVisibility(View.VISIBLE);
				messageNum.setText(unreadMail);
			}
			else
			{
				messageNum.setVisibility(View.GONE);
			}
			String unreadQuotation = BuyerApplication.getInstance().getUser().content.userInfo.unreadQuotation;
			if (!Utils.isEmpty(unreadQuotation) && !"0".equals(unreadQuotation))
			{
				quotationNum.setVisibility(View.VISIBLE);
				quotationNum.setText(unreadQuotation);
			}
			else
			{
				quotationNum.setVisibility(View.GONE);
			}
			btnLogin.setVisibility(View.GONE);
			accountName.setVisibility(View.VISIBLE);
			accountName.setText(UserUtil.transformGender()
					+ BuyerApplication.getInstance().getUser().content.userInfo.fullName);
			if (BuyerApplication.getInstance().getUser().content.favoriteInfo != null)
			{
				favProdNum.setText(BuyerApplication.getInstance().getUser().content.favoriteInfo.prodFavoriteNum);
				favCompNum.setText(BuyerApplication.getInstance().getUser().content.favoriteInfo.compFavoriteNum);
				favCateNum.setText(BuyerApplication.getInstance().getUser().content.favoriteInfo.cataFavoriteNum);
			}
		}
		else
		{
			messageNum.setVisibility(View.GONE);
			quotationNum.setVisibility(View.GONE);
			btnLogin.setVisibility(View.VISIBLE);
			accountName.setVisibility(View.GONE);
			favProdNum.setText("0");
			favCompNum.setText("0");
			favCateNum.setText("0");
		}
	}

	protected void setTextViewCompoundDrawable(TextView textView, int drawableResId)
	{
		Drawable drawable = getResources().getDrawable(drawableResId);
		drawable.setBounds(0, 0, Util.dip2px(32), Util.dip2px(30));
		textView.setCompoundDrawablePadding(Util.dip2px(8));
		textView.setCompoundDrawables(null, drawable, null, null);
	}

	@Override
	public String getChildTag()
	{
		return MyAccountFragment.class.getName();
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.btn_vo_setting:
			// 事件统计30 设置（VO） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c30);
			startActivity(new Intent(getActivity(), SettingActivity_.class));
			break;
		case R.id.tv_vo_unlogin:
			// 事件统计29 VO首页登录 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c29);
			Intent loginInt = new Intent(getActivity(), LoginActivity_.class);
			loginInt.putExtra("loginTarget", LoginTarget.getValue(LoginTarget.None));
			startActivity(loginInt);
			break;
		case R.id.ll_vo_messages:
			// 事件统计31 查看询盘（VO） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c31);
			if (BuyerApplication.getInstance().getUser() != null)
			{
				if (BuyerApplication.getInstance().isBuyerSuspended())
				{
					ToastUtil.toast(getActivity(), R.string.mic_buyer_suspended);
				}
				else
				{
					startActivity(new Intent(getActivity(), MessageActivity_.class));
				}
			}
			else
			{
				Intent messsageInt = new Intent(getActivity(), LoginActivity_.class);
				messsageInt.putExtra("loginTarget", LoginTarget.getValue(LoginTarget.Message));
				startActivity(messsageInt);
			}
			break;
		case R.id.ll_vo_quotations:
			// 事件统计32 查看报价（VO） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c32);
			if (BuyerApplication.getInstance().getUser() != null)
			{
				if (BuyerApplication.getInstance().isBuyerSuspended())
				{
					ToastUtil.toast(getActivity(), R.string.mic_buyer_suspended);
				}
				else
				{
					startActivity(new Intent(getActivity(), QuotationReceivedActivity_.class));
				}
			}
			else
			{
				Intent messsageInt = new Intent(getActivity(), LoginActivity_.class);
				messsageInt.putExtra("loginTarget", LoginTarget.getValue(LoginTarget.Quotation));
				startActivity(messsageInt);
			}
			break;
		case R.id.tv_vo_sourcing:
			// 事件统计33 查看RFQ (VO) 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c33);
			if (BuyerApplication.getInstance().getUser() != null)
			{
				if (BuyerApplication.getInstance().isBuyerSuspended())
				{
					ToastUtil.toast(getActivity(), R.string.mic_buyer_suspended);
				}
				else
				{
					startActivity(new Intent(getActivity(), SourcingRequestActivity_.class));
				}
			}
			else
			{
				Intent messsageInt = new Intent(getActivity(), LoginActivity_.class);
				messsageInt.putExtra("loginTarget", LoginTarget.getValue(LoginTarget.Sourcing));
				startActivity(messsageInt);
			}
			break;
		case R.id.tv_vo_member:
			// 事件统计34 查看会员信息（VO） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c34);
			if (BuyerApplication.getInstance().getUser() != null)
			{
				if (BuyerApplication.getInstance().isBuyerSuspended())
				{
					ToastUtil.toast(getActivity(), R.string.mic_buyer_suspended);
				}
				else
				{
					startActivity(new Intent(getActivity(), MemberInfoActivity_.class));
				}
			}
			else
			{
				Intent messsageInt = new Intent(getActivity(), LoginActivity_.class);
				messsageInt.putExtra("loginTarget", LoginTarget.getValue(LoginTarget.Member));
				startActivity(messsageInt);
			}
			break;
		case R.id.rl_vo_product:
			// 事件统计35 查看产品收藏（VO） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c35);
			if (BuyerApplication.getInstance().getUser() != null)
			{
				Intent proInt = new Intent(getActivity(), FavoriteActivity_.class);
				proInt.putExtra("currentItem", 0);
				startActivity(proInt);
			}
			else
			{
				Intent messsageInt = new Intent(getActivity(), LoginActivity_.class);
				messsageInt.putExtra("loginTarget", LoginTarget.getValue(LoginTarget.Favorate_Product));
				startActivity(messsageInt);
			}
			break;
		case R.id.rl_vo_supplier:
			// 事件统计36 查看公司收藏（VO） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c36);
			if (BuyerApplication.getInstance().getUser() != null)
			{
				Intent supInt = new Intent(getActivity(), FavoriteActivity_.class);
				supInt.putExtra("currentItem", 1);
				startActivity(supInt);
			}
			else
			{
				Intent messsageInt = new Intent(getActivity(), LoginActivity_.class);
				messsageInt.putExtra("loginTarget", LoginTarget.getValue(LoginTarget.Favorate_Supplier));
				startActivity(messsageInt);
			}
			break;
		case R.id.rl_vo_category:
			// 事件统计37 查看目录收藏（VO） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c37);
			if (BuyerApplication.getInstance().getUser() != null)
			{
				Intent cateInt = new Intent(getActivity(), FavoriteActivity_.class);
				cateInt.putExtra("currentItem", 2);
				startActivity(cateInt);
			}
			else
			{
				Intent messsageInt = new Intent(getActivity(), LoginActivity_.class);
				messsageInt.putExtra("loginTarget", LoginTarget.getValue(LoginTarget.Favorate_Category));
				startActivity(messsageInt);
			}
			break;
		}
	}
}

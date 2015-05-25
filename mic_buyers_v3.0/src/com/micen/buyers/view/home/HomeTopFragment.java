package com.micen.buyers.view.home;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.focustech.common.mob.DisposeDataListener;
import com.focustech.common.util.ToastUtil;
import com.focustech.common.util.Utils;
import com.focustech.common.widget.HorizontalListView;
import com.micen.buyers.activity.R;
import com.micen.buyers.activity.account.FavoriteActivity_;
import com.micen.buyers.activity.account.MessageActivity_;
import com.micen.buyers.activity.account.login.LoginActivity_;
import com.micen.buyers.activity.productdetail.ProductMessageActivity_;
import com.micen.buyers.activity.rfq.RFQAddActivity_;
import com.micen.buyers.adapter.RecommendProductAdapter;
import com.micen.buyers.http.RequestCenter;
import com.micen.buyers.manager.SysManager;
import com.micen.buyers.module.LoginTarget;
import com.micen.buyers.module.RecommendProduct;
import com.micen.buyers.module.RecommendProducts;
import com.micen.buyers.module.special.Special;
import com.micen.buyers.module.user.User;
import com.micen.buyers.util.BuyerApplication;
import com.micen.buyers.util.ImageUtil;

@EFragment(R.layout.home_top_layout)
public class HomeTopFragment extends HomeBaseFragment implements OnClickListener, DisposeDataListener
{
	@ViewById(R.id.home_special_upper_banner_layout)
	protected LinearLayout upperBannerLayout;

	@ViewById(R.id.mic_home_inbox)
	protected LinearLayout inboxLayout;

	@ViewById(R.id.mic_home_quotation)
	protected LinearLayout quotationLayout;

	@ViewById(R.id.tv_home_message_unread)
	protected TextView messageUnreadText;

	@ViewById(R.id.mic_home_favourite)
	protected LinearLayout favouriteLayout;

	@ViewById(R.id.mic_home_pull_refresh_scrollview)
	protected SwipeRefreshLayout refreshLayout;

	@ViewById(R.id.home_special_popular_layout)
	protected LinearLayout specialPopularLayout;

	@ViewById(R.id.home_special_dmiss_layout)
	protected LinearLayout specialDMissLayout;

	@ViewById(R.id.mic_home_recommend_layout)
	protected LinearLayout recommendLayout;

	@ViewById(R.id.home_recommend_list)
	protected HorizontalListView horizontalListView;

	private RecommendProductAdapter recommendAdapter;

	private OnItemClickListener recommendItemClick = new OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			// 事件统计 8 查看推荐产品（首页） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c8);

			RecommendProduct recommend = (RecommendProduct) parent.getAdapter().getItem(position);
			Intent intent = new Intent(getActivity(), ProductMessageActivity_.class);
			intent.putExtra("productId", recommend.productId);
			getActivity().startActivity(intent);
		}
	};

	private OnRefreshListener refreshListener = new OnRefreshListener()
	{

		@Override
		public void onRefresh()
		{
			ImageUtil.getImageLoader().clearMemoryCache();
			ImageUtil.getImageLoader().clearDiscCache();
			RequestCenter.getSpecialList(HomeTopFragment.this);
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

	@AfterViews
	protected void initView()
	{
		loadUserData();
		inboxLayout.setOnClickListener(this);
		quotationLayout.setOnClickListener(this);
		favouriteLayout.setOnClickListener(this);

		horizontalListView.setOnItemClickListener(recommendItemClick);

		refreshLayout.setOnRefreshListener(refreshListener);
		refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
				android.R.color.holo_orange_light, android.R.color.holo_red_light);

		// 如果没网，展示一组空格子
		specialPopularLayout.addView(new HomeSpecialView(getActivity(), getString(R.string.popular), null));
		specialDMissLayout.addView(new HomeSpecialView(getActivity(), getString(R.string.dmiss), null));
		RequestCenter.getSpecialList(this);
	}

	private void loadUserData()
	{
		if (BuyerApplication.getInstance().getUser() != null)
		{
			int unreadNum = 0;
			if (!Utils.isEmpty(BuyerApplication.getInstance().getUser().content.userInfo.unreadMail))
			{
				unreadNum += Integer.parseInt(BuyerApplication.getInstance().getUser().content.userInfo.unreadMail);
			}
			if (!Utils.isEmpty(BuyerApplication.getInstance().getUser().content.userInfo.unreadQuotation))
			{
				unreadNum += Integer
						.parseInt(BuyerApplication.getInstance().getUser().content.userInfo.unreadQuotation);
			}
			if (unreadNum != 0)
			{
				messageUnreadText.setText(String.valueOf(unreadNum));
				messageUnreadText.setVisibility(View.VISIBLE);
			}
			else
			{
				messageUnreadText.setVisibility(View.GONE);
			}
			RequestCenter.getRecommendProducts(recommendPros);
		}
		else
		{
			messageUnreadText.setVisibility(View.GONE);
		}
	}

	private DisposeDataListener recommendPros = new DisposeDataListener()
	{
		@Override
		public void onSuccess(Object result)
		{
			RecommendProducts products = (RecommendProducts) result;
			if (products.content != null)
			{
				recommendLayout.setVisibility(View.VISIBLE);
				recommendAdapter = new RecommendProductAdapter(getActivity(), products.content);
				horizontalListView.setAdapter(recommendAdapter);
			}
		}

		@Override
		public void onFailure(Object failedReason)
		{
			if (getActivity() != null)
			{
				ToastUtil.toast(getActivity(), String.valueOf(failedReason));
			}
		}
	};

	@Override
	public String getChildTag()
	{
		return HomeTopFragment.class.getName();
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.mic_home_inbox:
			// 事件统计 4 查看消息（首页） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c4);

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
		case R.id.mic_home_quotation:
			// 事件统计 5 快速发布RFQ（首页） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c5);
			startActivity(new Intent(getActivity(), RFQAddActivity_.class));
			break;
		case R.id.mic_home_favourite:
			// 事件统计 6 查看收藏（首页） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c6);
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
		}
	}

	@Override
	public void onSuccess(Object obj)
	{
		Special special = (Special) obj;
		if (getActivity() != null && !getActivity().isFinishing())
		{
			if (special.upper != null && special.upper.size() > 0)
			{
				for (int i = 0; i < upperBannerLayout.getChildCount(); i++)
				{
					if (upperBannerLayout.getChildAt(i) instanceof HomeSpecialBannerView)
					{
						upperBannerLayout.removeViewAt(i);
					}
				}
				upperBannerLayout.addView(new HomeSpecialBannerView(getActivity(), special.upper));
			}
			if (special.popular != null && special.popular.size() > 0)
			{
				// 请求成功，则先清空空白格子再添加真实数据
				for (int i = 0; i < specialPopularLayout.getChildCount(); i++)
				{
					if (specialPopularLayout.getChildAt(i) instanceof HomeSpecialView)
					{
						specialPopularLayout.removeViewAt(i);
					}
				}
				specialPopularLayout.addView(new HomeSpecialView(getActivity(), getString(R.string.popular),
						special.popular));
			}
			if (special.middle != null && special.middle.size() > 0)
			{
				for (int i = 0; i < specialPopularLayout.getChildCount(); i++)
				{
					if (specialPopularLayout.getChildAt(i) instanceof HomeSpecialBannerView)
					{
						specialPopularLayout.removeViewAt(i);
					}
				}
				specialPopularLayout.addView(new HomeSpecialBannerView(getActivity(), special.middle));
			}
			if (special.dMiss != null && special.dMiss.size() > 0)
			{
				// 请求成功，则先清空空白格子再添加真实数据
				for (int i = 0; i < specialDMissLayout.getChildCount(); i++)
				{
					if (specialDMissLayout.getChildAt(i) instanceof HomeSpecialView)
					{
						specialDMissLayout.removeViewAt(i);
					}
				}
				specialDMissLayout
						.addView(new HomeSpecialView(getActivity(), getString(R.string.dmiss), special.dMiss));
			}
		}
	}

	@Override
	public void onFailure(Object failedReason)
	{
		if (getActivity() != null)
		{
			ToastUtil.toast(getActivity(), String.valueOf(failedReason));
		}
	}

}

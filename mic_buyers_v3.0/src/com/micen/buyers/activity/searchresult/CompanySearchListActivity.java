package com.micen.buyers.activity.searchresult;

import java.util.ArrayList;
import java.util.HashMap;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.focustech.common.mob.DisposeDataListener;
import com.focustech.common.util.ToastUtil;
import com.focustech.common.widget.pulltorefresh.PullToRefreshBase;
import com.micen.buyers.activity.R;
import com.micen.buyers.activity.showroom.ShowRoomActivity_;
import com.micen.buyers.adapter.searchresult.CompanyListAdapter;
import com.micen.buyers.constant.Constants;
import com.micen.buyers.http.RequestCenter;
import com.micen.buyers.manager.DataManager;
import com.micen.buyers.manager.SysManager;
import com.micen.buyers.module.search.SearchCompanies;
import com.micen.buyers.module.search.SearchCompany;
import com.micen.buyers.module.search.SearchLocation;
import com.micen.buyers.util.Util;

/**********************************************************
 * @文件名称：CompanySearchListActivity.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年3月19日 上午8:47:35
 * @文件描述：公司结果列表页面
 * @修改历史：2015年3月19日创建初始版本
 **********************************************************/
@EActivity(R.layout.activity_search_list)
public class CompanySearchListActivity extends BaseSeachListActivity
{
	private CompanyListAdapter adapter;
	private SearchCompanies companies;

	private boolean isFirstGetData;
	public ArrayList<SearchLocation> mlocationsList;

	@AfterViews
	protected void initViewData()
	{
		iv_search.setVisibility(View.GONE);
		dbManager = DataManager.getInstance();
		initPullToListView();
		titleLeftButton.setImageResource(R.drawable.ic_title_back);;
		
		//titleRightButton2.setBackgroundDrawable(Utils.setImageButtonState(R.drawable.icon_refine_down,
		//		R.drawable.icon_refine_norm, this));
		
		titleRightButton2.setImageResource(R.drawable.icon_refine_norm);
		
		titleRightButton1.setVisibility(View.GONE);
		titleRightButton3.setVisibility(View.GONE);
		titleLeftButton.setOnClickListener(this);
		titleRightButton2.setOnClickListener(this);
		backTopImageView.setOnClickListener(this);
		searchLayout.setOnClickListener(this);
		searchLayout.setVisibility(View.VISIBLE);
		seachTextView.setText(keyword);

		siftMap = new HashMap<String, String>();
		siftMap.put("needGM", "0");
		siftMap.put("needAudit", "0");
		siftMap.put("location", "");
		dbManager.deleteTheUnuseRecord();
		isFirstGetData = true;
		startSearch(keyword, "", String.valueOf(pageIndex), String.valueOf(pageSize), siftMap.get("needGM"),
				siftMap.get("needAudit"), siftMap.get("location"), false);
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		// 从数据库中取数据所有有效数据．．
		searchListRecoders = dbManager.changeToSearchListRecoder(SearchType.COMPANY.toString());
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		// SysManager.analysis(this, getString(R.string.a_type_page), getString(R.string.a134));

		// 事件统计$10022 供应商搜索结果页 页面事件
		SysManager.analysis(R.string.a_type_page, R.string.p10022);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		if (arg0.getAdapter().getItem(arg2) == null)
			return;

		// 事件统计91 查看公司展示厅（供应商搜索结果页） 点击事件
		SysManager.analysis(R.string.a_type_click, R.string.c91);

		SearchCompany company = (SearchCompany) arg0.getAdapter().getItem(arg2);
		SysManager.analysis(this, getString(R.string.a_type_click), getString(R.string.a132));
		// 添加到Adapter中
		adapter.addSelectItem(company.companyId);
		// 此处进行入库操作．．
		dbManager.insertInToSearchListTable(company.companyId, SearchType.COMPANY.toString());
		Intent intent = new Intent(this, ShowRoomActivity_.class);
		intent.putExtra("companyId", ((SearchCompany) arg0.getAdapter().getItem(arg2)).companyId);
		startActivity(intent);
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
		case R.id.common_title_right_button2:
			if (companies != null)
			{
				// 事件统计 90 点击筛选（供应商搜索结果页） 点击事件
				SysManager.analysis(R.string.a_type_click, R.string.c90);

				// 去筛选
				Intent intent = new Intent(this, RefineActivity_.class);
				intent.putExtra("iscompany", true);
				intent.putExtra("siftlocations", mlocationsList);
				intent.putExtra("siftmap", this.siftMap);
				startActivityForResult(intent, Constants.RRFINE_COMPANY);
			}
			break;
		case R.id.iv_scroll_top:
			Util.dispatchCancelEvent(searchListView);
			searchListView.setSelection(0);
			break;
		case R.id.search_again_layout:
			// 事件统计89 点击搜索框（供应商搜索结果页） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c89);
			finish();
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == Constants.RRFINE_COMPANY)
		{
			if (resultCode == RESULT_OK)
			{
				pageIndex = 1;
				isRefresh = true;

				HashMap<String, String> tempSiftmap = (HashMap<String, String>) data.getSerializableExtra("siftmap");
				this.siftMap = tempSiftmap;
				if (!progressLayout.isShown())
				{
					loadingLayout.setVisibility(View.VISIBLE);
				}
				else
				{
					return;
				}
				startSearch(keyword, "", String.valueOf(pageIndex), String.valueOf(pageSize), siftMap.get("needGM"),
						siftMap.get("needAudit"), siftMap.get("location"), false);
			}
		}
	}

	private void startSearch(String keyword, String category, String pageIndex, String pageSize, String needGM,
			String needAudit, String location, boolean isLoadMore)
	{
		RequestCenter
				.searchCompanies(dataListener, keyword, category, pageIndex, pageSize, needGM, needAudit, location);
	}

	private DisposeDataListener dataListener = new DisposeDataListener()
	{
		@Override
		public void onSuccess(Object arg0)
		{
			searchListLayout.setVisibility(View.VISIBLE);
			//backTopImageView.setVisibility(View.VISIBLE);
			refreshComplete();
			companies = (SearchCompanies) arg0;
			if (companies.content != null && companies.content.size() > 0)
			{
				changeProgressView(isRefresh);

				if (!isLoadMore && !isFinishing() && !isPauseState)
				{
					showSearchMessage(companies.locations);
				}

				tvNoMatch.setVisibility(View.GONE);
				pullToListView.setVisibility(View.VISIBLE);
				// 如果适配器为空或者是刷新，则初始化列表
				if (adapter == null || isRefresh)
				{
					adapter = new CompanyListAdapter(CompanySearchListActivity.this, companies.content);
					searchListView.setAdapter(adapter);
					// if (companies.locations != null && !siftFragment.isHavaLocationData())
					// {
					// siftFragment.onLoadLocation(companies.locations);
					// }
				}
				else
				{
					adapter.addCompanies(companies.content);
				}
				// 添加浏览数据到Adapter中
				adapter.addSelectItem(searchListRecoders);
			}
			else
			{
				changeProgressView(isRefresh);

				if (!isLoadMore && !isFinishing() && !isPauseState)
				{
					showSearchMessage(new ArrayList<SearchLocation>());
				}
				// 如果不是加载更多导致的请求数据为空，则显示空数据View
				if (!isLoadMore)
				{
					// siftFragment.onLoadLocation(new ArrayList<SearchLocation>());
					pullToListView.setVisibility(View.GONE);
					tvNoMatch.setVisibility(View.VISIBLE);
					tvNoMatch.setText(R.string.no_company_matched);
				}
				else
				{
					tvNoMatch.setVisibility(View.GONE);
					pullToListView.setVisibility(View.VISIBLE);
				}
			}
			isRefresh = false;
			isLoadMore = false;
			if (isFirstGetData)
			{
				isFirstGetData = false;
				mlocationsList = companies.locations;
			}
		}

		@Override
		public void onFailure(Object arg0)
		{
			if (isFinishing())
			{
				return;
			}
			refreshComplete();
			isRefresh = false;
			isLoadMore = false;
			if (!isFinishing())
			{
				ToastUtil.toast(CompanySearchListActivity.this, arg0);
			}
			if (adapter == null)
			{
				finish();
			}
		}
	};

	@Override
	public void showSearchMessage(ArrayList<SearchLocation> locations)
	{
		//7a7a7a
		productsString = "<font color=\"#ffffff\">" + getResources().getString(R.string.mic_companies) + "</font>";
		super.showSearchMessage(locations);
	}

	/**
	 * 下拉刷新
	 * @param refreshView
	 */
	@Override
	protected void onPullUpToRefreshView(PullToRefreshBase<?> refreshView)
	{
		isLoadMore = true;
		pageIndex++;
		startSearch(keyword, "", String.valueOf(pageIndex), String.valueOf(pageSize), siftMap.get("needGM"),
				siftMap.get("needAudit"), siftMap.get("location"), true);
	}
}

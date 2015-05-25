package com.micen.buyers.activity.searchresult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout.LayoutParams;

import com.focustech.common.mob.DisposeDataListener;
import com.focustech.common.util.ToastUtil;
import com.focustech.common.util.Utils;
import com.focustech.common.widget.pulltorefresh.PullToRefreshBase;
import com.micen.buyers.activity.R;
import com.micen.buyers.activity.account.login.LoginActivity_;
import com.micen.buyers.activity.productdetail.ProductMessageActivity_;
import com.micen.buyers.adapter.searchresult.ProductBaseAdapter;
import com.micen.buyers.adapter.searchresult.ProductGridAdapter;
import com.micen.buyers.adapter.searchresult.ProductListAdapter;
import com.micen.buyers.constant.Constants;
import com.micen.buyers.constant.ProductSearchType;
import com.micen.buyers.db.DBDataHelper;
import com.micen.buyers.db.DBHelper;
import com.micen.buyers.db.SharedPreferenceManager;
import com.micen.buyers.http.RequestCenter;
import com.micen.buyers.manager.DataManager;
import com.micen.buyers.manager.FavouriteManager;
import com.micen.buyers.manager.FavouriteManager.FavouriteCallback;
import com.micen.buyers.manager.FavouriteManager.FavouriteType;
import com.micen.buyers.manager.SysManager;
import com.micen.buyers.module.LoginTarget;
import com.micen.buyers.module.db.CategoryHistory;
import com.micen.buyers.module.db.Module;
import com.micen.buyers.module.search.SearchLocation;
import com.micen.buyers.module.search.SearchProduct;
import com.micen.buyers.module.search.SearchProducts;
import com.micen.buyers.module.sift.SearchProperty;
import com.micen.buyers.module.sift.SiftCategory;
import com.micen.buyers.util.BuyerApplication;
import com.micen.buyers.util.Util;

/**********************************************************
 * @文件名称：ProductSearchListActivity.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年3月18日 下午2:18:52
 * @文件描述：搜索结果列表
 * @修改历史：2015年3月18日创建初始版本
 **********************************************************/
@EActivity(R.layout.activity_search_list)
public class ProductSearchListActivity extends BaseSeachListActivity
{
	@Extra("searchType")
	protected String searchTypeString;
	@Extra("sourceSubject")
	protected String sourceSubject;
	@Extra("category")
	protected String category;
	@Extra("companyId")
	protected String companyId;
	@Extra("isCategory")
	protected boolean isCategory;

	protected ProductBaseAdapter adapter;
	protected SearchProducts products;

	protected String property;
	protected boolean isFavorites;
	/**
	 * 返回产品实际图片
	 */
	protected ProductSearchType searchType;
	private final String GET_REAL_IMAGE = "2";

	private int gridviewpadding = 0;

	// 保存筛选的属性不变
	private boolean isFirstGetData;
	public ArrayList<SearchProduct> mContentList;
	public ArrayList<SearchLocation> mlocationsList;
	public ArrayList<SiftCategory> mCatagoryList;
	public ArrayList<SearchProperty> mpropertyList;

	@Override
	protected void onStart()
	{
		super.onStart();
		// 从数据库中取数据所有有效数据．．
		searchListRecoders = dbManager.changeToSearchListRecoder(SearchType.PRODUCT.toString());
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		if (adapter != null)
		{
			/**
			 * 再次返回到产品列表，重新根据设置去更新Adapter的显示
			 */
			adapter.updateSetting();
		}
		if (isCategory)
		{

			// 事件统计 $10021 目录搜索结果页 页面事件
			SysManager.analysis(R.string.a_type_page, R.string.p10021);
		}
		else
		{

			// 事件统计 $10019 产品关键词搜索结果页 页面事件
			SysManager.analysis(R.string.a_type_page, R.string.p10019);
		}

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus)
	{
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus)
		{
			// 非目录才去获取收藏相关
			if (!isCategory)
			{
				return;
			}
			FavouriteManager.getInstance().checkIsFavourite(this, titleRightButton1, FavouriteType.Category, category,
					new FavouriteCallback()
					{
						@Override
						public void onCallback(boolean result)
						{
							isFavorites = result;
						}
					});
		}
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		adapter = null;
	}

	@AfterViews
	protected void initData()
	{
		iv_search.setVisibility(View.GONE);
		dbManager = DataManager.getInstance();
		searchType = ProductSearchType.getSearchTypeByTag(searchTypeString);
		Constants.KEY_WORD = keyword;

		titleLeftButton.setImageResource(R.drawable.ic_title_back);
		titleLeftButton.setOnClickListener(this);
		titleRightButton1.setBackgroundDrawable(Utils.setImageButtonState(R.drawable.icon_unfavourit_down,
				R.drawable.icon_unfavourit, this));
		titleRightButton1.setOnClickListener(this);

		// titleRightButton2.setBackgroundDrawable(Utils.setImageButtonState(R.drawable.icon_refine_down,
		// R.drawable.icon_refine_norm, this));
		titleRightButton2.setImageResource(R.drawable.icon_refine_norm);

		titleRightButton2.setOnClickListener(this);
		searchLayout.setOnClickListener(this);

		titleRightButton1.setVisibility(View.GONE);
		titleRightButton3.setVisibility(View.GONE);
		searchLayout.setVisibility(View.VISIBLE);

		float scale = this.getResources().getDisplayMetrics().density;
		gridviewpadding = (int) (10 * scale + 0.5f);

		if (!isGridMode)
		{
			initPullToListView();
		}
		else
		{
			initPullToGridView();
			searchGridView.setVerticalSpacing(gridviewpadding);
			searchGridView.setHorizontalSpacing(gridviewpadding);
			searchGridView.setPadding(gridviewpadding, gridviewpadding, gridviewpadding, gridviewpadding);
		}

		seachTextView.setText(keyword);
		modeImageView.setOnClickListener(this);
		backTopImageView.setOnClickListener(this);
		if (isCategory)
		{
			/**
			 * 如果是目录就重设其间距
			 */
			titleRightButton1.setVisibility(View.VISIBLE);
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, Util.dip2px(30));
			params.setMargins(Util.dip2px(58), Util.dip2px(9), Util.dip2px(96), Util.dip2px(9));
			searchLayout.setLayoutParams(params);
		}

		siftMap = new HashMap<String, String>();
		siftMap.put("needGM", "0");
		siftMap.put("needAudit", "0");
		siftMap.put("location", "");

		dbManager.deleteTheUnuseRecord();
		isFirstGetData = true;
		startSearch(searchType, String.valueOf(pageIndex), String.valueOf(pageSize), siftMap.get("needGM"),
				siftMap.get("needAudit"), siftMap.get("location"), false, property);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		// 点击进入列表详细页面
		if (arg0.getAdapter().getItem(arg2) == null)
			return;
		// SysManager.analysis(this, getString(R.string.a_type_click), getString(R.string.a114));
		// 事件统计 78 查看产品详情页（搜索结果页） 点击事件
		SysManager.analysis(R.string.a_type_click, R.string.c78);

		SearchProduct product = (SearchProduct) arg0.getAdapter().getItem(arg2);
		// 添加到Adapter中
		adapter.addSelectItem(product.productId);
		// 此处进行入库操作．．
		dbManager.insertInToSearchListTable(product.productId, SearchType.PRODUCT.toString());
		Intent intent = new Intent(this, ProductMessageActivity_.class);
		intent.putExtra("productId", product.productId);
		intent.putExtra("category", category);
		startActivity(intent);
	}

	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		switch (v.getId())
		{
		case R.id.tv_search_suggest:
			pageIndex = 1;
			keyword = tvSuggest.getText().toString();
			seachTextView.setText(keyword);
			isRefresh = true;
			isSuggest = true;
			isFirstGetData = true;
			startSearch(searchType, String.valueOf(pageIndex), String.valueOf(pageSize), siftMap.get("needGM"),
					siftMap.get("needAudit"), siftMap.get("location"), false, property);
			break;
		case R.id.iv_search_list_mode:// 模式切换按钮
			// 事件统计 79 切换列表/橱窗（搜索结果页） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c79);

			SharedPreferenceManager.getInstance().putBoolean("isGridMode", !isGridMode);
			isGridMode = SharedPreferenceManager.getInstance().getBoolean("isGridMode", false);
			if (!isGridMode)
			{
				modeImageView.setImageResource(R.drawable.btn_search_list_mode_grid);
				initPullToListView();
				ArrayList<SearchProduct> dataList = adapter.getAdapterData();
				adapter = new ProductListAdapter(this, dataList);
				searchListView.setAdapter(adapter);
			}
			else
			{
				modeImageView.setImageResource(R.drawable.btn_search_list_mode_list);
				initPullToGridView();
				searchGridView.setVerticalSpacing(gridviewpadding);
				searchGridView.setHorizontalSpacing(gridviewpadding);
				searchGridView.setPadding(gridviewpadding, gridviewpadding, gridviewpadding, gridviewpadding);
				ArrayList<SearchProduct> dataList = adapter.getAdapterData();
				adapter = new ProductGridAdapter(this, dataList);
				searchGridView.setAdapter(adapter);
			}
			// 添加浏览数据到Adapter中
			adapter.addSelectItem(searchListRecoders);
			break;
		case R.id.common_title_back_button:
			finish();
			break;
		case R.id.common_title_right_button1:
			// 去收藏
			if (BuyerApplication.getInstance().getUser() == null)
			{
				Intent intent = new Intent(this, LoginActivity_.class);
				intent.putExtra("loginTarget", LoginTarget.getValue(LoginTarget.ClickForFavorite));
				startActivityForResult(intent, Constants.LOGIN_FAVOURITE);
				return;
			}
			addOrDelFavourite(v);
			break;
		case R.id.common_title_right_button2:
			if (products != null)
			{
				if (isCategory)
				{
					// 事件统计88 搜藏目录筛选结果 点击事件
					SysManager.analysis(R.string.a_type_click, R.string.c88);
				}
				else
				{
					// 事件统计77 查看筛选面板（搜索结果页） 点击事件
					SysManager.analysis(R.string.a_type_click, R.string.c77);
				}

				// 去筛选
				Intent intent = new Intent(this, RefineActivity_.class);

				// mContentList = products.content;
				// mlocationsList = products.locations;
				// mCatagoryList = products.catagory;
				// mpropertyList = products.property;

				intent.putExtra("siftlocations", mlocationsList);
				intent.putExtra("siftcategory", mCatagoryList);
				intent.putExtra("siftproperty", mpropertyList);

				intent.putExtra("siftmap", this.siftMap);
				/*
				 * intent.putExtra("siftlocations", products.locations); intent.putExtra("siftcategory",
				 * products.catagory); intent.putExtra("siftproperty", products.property);
				 */
				intent.putExtra("isCategory", isCategory);
				startActivityForResult(intent, Constants.RRFINE_PRODUCT);
			}
			break;
		case R.id.iv_scroll_top:
			// 事件统计 80 回到顶部（搜索结果页） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c80);

			if (isGridMode)
			{
				Util.dispatchCancelEvent(searchGridView);
				searchGridView.setSelection(0);
			}
			else
			{
				Util.dispatchCancelEvent(searchListView);
				searchListView.setSelection(0);
			}
			break;
		case R.id.search_again_layout:
			if (isCategory)
			{
				// 事件统计87 点击搜素框（目录搜索结果页） 点击事件
				SysManager.analysis(R.string.a_type_click, R.string.c87);

				Intent intent = new Intent(this, SearchActivity_.class);
				startActivity(intent);
			}
			else
			{
				// 事件统计76 点击搜索框（搜索结果页） 点击事件
				SysManager.analysis(R.string.a_type_click, R.string.c76);
			}

			finish();
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == Constants.RRFINE_PRODUCT)
		{
			if (resultCode == RESULT_OK)
			{
				// 属性回传
				mpropertyList = (ArrayList<SearchProperty>) data.getSerializableExtra("siftproperty");

				HashMap<String, String> tempSiftmap = (HashMap<String, String>) data.getSerializableExtra("siftmap");
				pageIndex = 1;
				isRefresh = true;
				// 更新条件Map为最新的
				this.siftMap = tempSiftmap;

				if (!progressLayout.isShown())
				{
					loadingLayout.setVisibility(View.VISIBLE);
				}
				else
				{
					return;
				}

				if (siftMap.get("category") != null)
				{
					category = siftMap.get("category");
					searchType = ProductSearchType.All;
				}
				if (siftMap.get("property") != null)
				{
					property = siftMap.get("property");
					searchType = ProductSearchType.All;
				}
				startSearch(searchType, String.valueOf(pageIndex), String.valueOf(pageSize), siftMap.get("needGM"),
						siftMap.get("needAudit"), siftMap.get("location"), false, property);
				// 清空上次的请求筛选条件
				property = "";
			}
		}
		else if (requestCode == Constants.LOGIN_FAVOURITE)
		{
			if (resultCode == RESULT_OK)
			{
				FavouriteManager.getInstance().checkIsFavourite(this, titleRightButton1, FavouriteType.Category,
						category, new FavouriteCallback()
						{
							@Override
							public void onCallback(boolean result)
							{
								if (result)
								{
									isFavorites = result;
								}
								else
								{
									addOrDelFavourite(titleRightButton1);
								}
							}
						});
			}
		}
	}

	private void startSearch(String keyword, String category, String companyId, String pageIndex, String pageSize,
			String needGM, String needAudit, String location, boolean isLoadMore, String property)
	{
		RequestCenter.searchProducts(dataListener, keyword, category, companyId, GET_REAL_IMAGE, pageIndex, pageSize,
				needGM, needAudit, location, property);
	}

	/**
	 * 根据不同的搜索分类开始搜索
	 * @param searchType
	 * @param pageIndex
	 * @param pageSize
	 * @param needGM
	 * @param needAudit
	 * @param location
	 * @param isLoadMore
	 */
	private void startSearch(ProductSearchType searchType, String pageIndex, String pageSize, String needGM,
			String needAudit, String location, boolean isLoadMore, String property)
	{
		switch (searchType)
		{
		case Keyword:// 关键词
			startSearch(keyword, "", "", pageIndex, pageSize, needGM, needAudit, location, isLoadMore, property);
			break;
		case Category:// 目录ID
			setFavoriteStatus(isFavorites);
			startSearch("", category, "", pageIndex, pageSize, needGM, needAudit, location, isLoadMore, property);
			break;
		case CompanyId:// 公司ID
			startSearch("", "", companyId, pageIndex, pageSize, needGM, needAudit, location, isLoadMore, property);
			break;
		case All:
			startSearch(keyword, category, companyId, pageIndex, pageSize, needGM, needAudit, location, isLoadMore,
					property);
			break;
		default:
			break;
		}
	}

	/**
	 * 设置收藏按钮状态
	 * @param isFa
	 */
	private void setFavoriteStatus(boolean isFa)
	{
		switch (searchType)
		{
		case Category:
			titleRightButton1.setVisibility(View.VISIBLE);
			titleRightButton1.setOnClickListener(this);
			if (isFa)
			{
				titleRightButton1.setBackgroundDrawable(Utils.setImageButtonState(R.drawable.icon_favourit_down,
						R.drawable.icon_favourite, this));
			}
			else
			{
				titleRightButton1.setBackgroundDrawable(Utils.setImageButtonState(R.drawable.icon_unfavourit_down,
						R.drawable.icon_unfavourit, this));
			}
			break;
		default:
			titleRightButton1.setVisibility(View.GONE);
			break;
		}
	}

	private DisposeDataListener dataListener = new DisposeDataListener()
	{
		@Override
		public void onSuccess(Object arg0)
		{
			searchListLayout.setVisibility(View.VISIBLE);

			modeImageView.setImageResource(isGridMode ? R.drawable.btn_search_list_mode_list
					: R.drawable.btn_search_list_mode_grid);
			modeImageView.setVisibility(View.VISIBLE);
			// backTopImageView.setVisibility(View.VISIBLE);
			refreshComplete();
			products = (SearchProducts) arg0;

			if (products.content != null && products.content.size() > 0)
			{
				changeProgressView(isRefresh);

				if (!isLoadMore && !isFinishing() && !isPauseState)
				{
					// 只有非加载更多才显示此进度框
					showSearchMessage(products.resultNum);
				}
				tvNoMatch.setVisibility(View.GONE);
				setPullToViewVisibility(View.VISIBLE);
				// 如果适配器为空或者是刷新，则初始化列表
				if (adapter == null || isRefresh)
				{
					if (isGridMode)
					{
						adapter = new ProductGridAdapter(ProductSearchListActivity.this, products.content);
						searchGridView.setAdapter(adapter);
					}
					else
					{
						adapter = new ProductListAdapter(ProductSearchListActivity.this, products.content);
						searchListView.setAdapter(adapter);
					}
				}
				else
				{
					adapter.addProducts(products.content);
				}

				// 添加浏览数据到Adapter中
				adapter.addSelectItem(searchListRecoders);
			}
			else
			{
				changeProgressView(isRefresh);

				if (!isLoadMore && !isFinishing() && !isPauseState)
				{
					// 只有非加载更多才显示此进度框并且Activity没有finish
					showSearchMessage("0");
				}
				// 如果不是加载更多导致的请求数据为空，则显示空数据View
				if (!isLoadMore)
				{
					modeImageView.setVisibility(View.GONE);
					backTopImageView.setVisibility(View.GONE);
					setPullToViewVisibility(View.GONE);
					tvNoMatch.setVisibility(View.VISIBLE);
					tvNoMatch.setText(R.string.no_product_matched);
				}
				else
				{
					tvNoMatch.setVisibility(View.GONE);
					setPullToViewVisibility(View.VISIBLE);
				}
			}
			if (products.suggest != null && !"".equals(products.suggest))
			{
				suggestLayout.setVisibility(View.VISIBLE);
				tvSuggest.setText(products.suggest);
				tvSuggest.setOnClickListener(ProductSearchListActivity.this);
			}
			else
			{
				suggestLayout.setVisibility(View.GONE);
			}
			addCategoryBrowseHistory();
			isRefresh = false;
			isLoadMore = false;
			isSuggest = false;

			if (isFirstGetData)
			{
				isFirstGetData = false;
				mContentList = products.content;
				mlocationsList = products.locations;
				mCatagoryList = products.catagory;
				mpropertyList = products.property;

				try
				{
					// 设置对应 Int数字
					for (int i = 0; i < mCatagoryList.size(); i++)
					{
						mCatagoryList.get(i).intnum = Integer.parseInt(mCatagoryList.get(i).num);
					}
					// 排序
					Collections.sort(mCatagoryList, new Comparator<SiftCategory>()
					{

						@Override
						public int compare(SiftCategory lhs, SiftCategory rhs)
						{
							// TODO Auto-generated method stub
							return rhs.intnum - lhs.intnum;
						}
					});
				}
				catch (Exception e)
				{

				}
			}
		}

		@Override
		public void onFailure(Object arg0)
		{
			if (isFinishing())
			{
				return;
			}

			loadingLayout.setVisibility(View.GONE);
			refreshComplete();
			isRefresh = false;
			isLoadMore = false;
			if (!isFinishing())
			{
				ToastUtil.toast(ProductSearchListActivity.this, arg0);
			}
			if (adapter == null)
			{
				finish();
			}
		}
	};

	/**
	 * 如果是按目录搜索，则加入到历史记录
	 */
	private void addCategoryBrowseHistory()
	{
		switch (searchType)
		{
		case Category:
			ArrayList<Module> modules = DBDataHelper.getInstance().select(DBHelper.CATEGORIES_BROWSE_HISTORY_, null,
					DBHelper.CATEGORY, category, null, CategoryHistory.class);
			CategoryHistory module;
			if (modules != null && modules.size() != 0)
			{
				for (int i = 0; i < modules.size(); i++)
				{
					module = (CategoryHistory) modules.get(i);
					module.visitTime = String.valueOf(System.currentTimeMillis());
					DBDataHelper.getInstance().update(DBHelper.CATEGORIES_BROWSE_HISTORY_, module);
				}
			}
			else
			{
				String categoryHistory = getIntent().getExtras().getString(Constants.CATEGORY_HISTORY_KEY);
				categoryHistory = (categoryHistory != null && !"".equals(categoryHistory)) ? categoryHistory
						: sourceSubject;
				module = new CategoryHistory();
				module.searchFlag = searchType.getValue();
				module.isFavorites = String.valueOf(isFavorites);
				module.sourceSubject = sourceSubject;
				module.categoriesHistory = categoryHistory;
				module.category = category;
				module.visitTime = String.valueOf(System.currentTimeMillis());
				DBDataHelper.getInstance().insert(DBHelper.CATEGORIES_BROWSE_HISTORY_, module);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void showSearchMessage(String resultNum)
	{
		// 7a7a7a
		productsString = "<font color=\"#ffffff\">" + getResources().getString(R.string.mic_products) + "</font>";
		super.showSearchMessage(resultNum);
	}

	@Override
	protected void onPullUpToRefreshView(PullToRefreshBase<?> refreshView)
	{
		isLoadMore = true;
		pageIndex++;
		/**
		 * 下拉刷新请求数据时也要加上全部的筛选条件,否则筛选结果不正确
		 */
		startSearch(searchType, String.valueOf(pageIndex), String.valueOf(pageSize), siftMap.get("needGM"),
				siftMap.get("needAudit"), siftMap.get("location"), true, siftMap.get("property"));
	}

	/**
	 * 添加或删除收藏
	 * @param displayView
	 */
	private void addOrDelFavourite(View displayView)
	{
		// 如果已经收藏，点击后取消收藏，否则点击后加入收藏数据
		FavouriteManager.getInstance().addOrDelFavourite(this, displayView, FavouriteType.Category, category,
				sourceSubject, isFavorites, new FavouriteCallback()
				{
					@Override
					public void onCallback(boolean result)
					{
						isFavorites = result;
					}
				});
	}
}

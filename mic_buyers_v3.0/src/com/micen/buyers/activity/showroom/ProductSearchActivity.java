package com.micen.buyers.activity.showroom;

import java.util.ArrayList;
import java.util.HashMap;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.focustech.common.mob.DisposeDataListener;
import com.focustech.common.util.ToastUtil;
import com.focustech.common.util.Utils;
import com.focustech.common.widget.pulltorefresh.PullToRefreshBase;
import com.focustech.common.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.focustech.common.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.focustech.common.widget.pulltorefresh.PullToRefreshGridView;
import com.focustech.common.widget.pulltorefresh.PullToRefreshListView;
import com.google.analytics.tracking.android.EasyTracker;
import com.micen.buyers.activity.R;
import com.micen.buyers.activity.productdetail.ProductMessageActivity_;
import com.micen.buyers.adapter.searchresult.ProductBaseAdapter;
import com.micen.buyers.adapter.searchresult.ProductGridAdapter;
import com.micen.buyers.adapter.searchresult.ProductListAdapter;
import com.micen.buyers.constant.Constants;
import com.micen.buyers.db.SharedPreferenceManager;
import com.micen.buyers.http.RequestCenter;
import com.micen.buyers.manager.ActivityManager;
import com.micen.buyers.manager.DataManager;
import com.micen.buyers.module.search.SearchLocation;
import com.micen.buyers.module.search.SearchProduct;
import com.micen.buyers.module.search.SearchProducts;
import com.micen.buyers.reveiver.ActivityFinishReceiver;
import com.micen.buyers.util.Util;

@EActivity(R.layout.activity_product_search)
public class ProductSearchActivity extends Activity implements OnClickListener, OnItemClickListener
{
	protected ActivityFinishReceiver finishReceiver = new ActivityFinishReceiver();
	private final String GET_REAL_IMAGE = "2";

	@Extra("companyId")
	protected String companyId;

	protected String property;

	@ViewById(R.id.title_back_button)
	protected ImageView titleLeftButton;

	@ViewById(R.id.keyword_edit)
	protected EditText keywordEdit;

	@ViewById(R.id.clear_keyword)
	protected ImageView clearTag;

	@ViewById(R.id.loading_bar)
	protected RelativeLayout loadingLayout;

	@ViewById(R.id.search_suggest_layout)
	protected LinearLayout suggestLayout;

	@ViewById(R.id.search_list_layout)
	protected RelativeLayout searchListLayout;

	@ViewById(R.id.tv_search_noMatch)
	protected TextView tvNoMatch;

	@ViewById(R.id.progressbar_layout)
	protected RelativeLayout progressLayout;
	@ViewById(R.id.iv_search_list_mode)
	protected ImageView modeImageView;
	@ViewById(R.id.iv_scroll_top)
	protected ImageView scrollTopImageView;

	protected PullToRefreshListView pullToListView;
	protected ListView searchListView;
	protected PullToRefreshGridView pullToGridView;
	protected GridView searchGridView;

	protected ProductBaseAdapter adapter;
	protected SearchProducts products;

	protected String keyword;

	protected HashMap<String, String> siftMap;

	protected String productsString;

	protected DataManager dbManager;

	protected boolean isGridMode = SharedPreferenceManager.getInstance().getBoolean("isGridMode", false);

	protected ArrayList<String> searchListRecoders;

	protected int pageIndex = 1;
	protected int pageSize = 20;

	/**
	 * 是否加载更多
	 */
	protected boolean isLoadMore = false;
	/**
	 * 是否刷新
	 */
	protected boolean isRefresh = false;

	/**
	 * 判断Activity是否处于隐藏状态
	 */
	protected boolean isPauseState = false;

	/**
	 * EditText文本监听器
	 */
	private TextWatcher watcher = new TextWatcher()
	{
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count)
		{
			if (!"".equals(keywordEdit.getText().toString().trim()))
			{
				clearTag.setVisibility(View.VISIBLE);
			}
			else
			{
				clearTag.setVisibility(View.GONE);
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after)
		{
		}

		@Override
		public void afterTextChanged(Editable s)
		{
		}
	};

	private OnEditorActionListener keywordOnEditorActionListener = new OnEditorActionListener()
	{

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
		{
			if (actionId == EditorInfo.IME_ACTION_SEARCH)
			{
				// 搜索时显示等待窗口
				isRefresh = true;
				tvNoMatch.setVisibility(View.VISIBLE);
				progressLayout.setVisibility(View.VISIBLE);
				searchListLayout.setVisibility(View.GONE);
				modeImageView.setVisibility(View.GONE);
				scrollTopImageView.setVisibility(View.GONE);
				// TODO
				Util.hideSoftInputMethod(ProductSearchActivity.this, keywordEdit);
				keyword = keywordEdit.getText().toString();
				pageIndex = 1;
				startSearchProducts(keyword);
				return true;
			}
			return false;
		}
	};

	private OnScrollListener scrollListener = new OnScrollListener()
	{

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState)
		{

		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
		{
			scrollTopImageView.setVisibility(firstVisibleItem == 0 ? View.INVISIBLE : View.VISIBLE);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Util.startDeveloperMode();
		Constants.currentActivity = this;
		ActivityManager.getInstance().put(this);
		registerFinishReceiver();
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		ActivityManager.getInstance().remove(this);
		unRegisterFinishReceiver();
	}

	protected void registerFinishReceiver()
	{
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.APP_FINISH_KEY);
		registerReceiver(finishReceiver, filter); // 注册
	}

	protected void unRegisterFinishReceiver()
	{
		unregisterReceiver(finishReceiver);
	}

	@Override
	public void onPause()
	{
		super.onPause();
		isPauseState = true;
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		isPauseState = false;
	}

	@AfterViews
	protected void initData()
	{
		dbManager = DataManager.getInstance();
		Constants.KEY_WORD = keyword;

		titleLeftButton.setOnClickListener(this);
		clearTag.setOnClickListener(this);

		if (!isGridMode)
		{
			initPullToListView();
		}
		else
		{
			initPullToGridView();
		}
		scrollTopImageView.setOnClickListener(this);
		modeImageView.setOnClickListener(this);

		siftMap = new HashMap<String, String>();
		siftMap.put("keyword", "");

		dbManager.deleteTheUnuseRecord();
		keywordEdit.addTextChangedListener(watcher);
		keywordEdit.setOnEditorActionListener(keywordOnEditorActionListener);

		progressLayout.setVisibility(View.VISIBLE);
		startSearchProducts(keyword);
	}

	protected void initPullToListView()
	{
		searchListLayout.removeAllViews();
		View view = LayoutInflater.from(this).inflate(R.layout.pulltorefresh_listview, null);
		pullToListView = (PullToRefreshListView) view.findViewById(R.id.lv_search_list);
		pullToListView.setOnRefreshListener(initListRefreshListener());
		pullToListView.setOnScrollListener(scrollListener);
		// 只启动下拉加载更多
		pullToListView.setMode(Mode.PULL_FROM_END);
		// 不显示指示器
		pullToListView.setShowIndicator(false);
		searchListView = pullToListView.getRefreshableView();
		searchListView.setFastScrollEnabled(true);
		searchListView.setOnItemClickListener(this);
		searchListLayout.addView(view);
	}

	protected void initPullToGridView()
	{
		searchListLayout.removeAllViews();
		View view = LayoutInflater.from(this).inflate(R.layout.pulltorefresh_gridview, null);
		pullToGridView = (PullToRefreshGridView) view.findViewById(R.id.gv_search_list);
		pullToGridView.setOnRefreshListener(initGridRefreshListener());
		pullToGridView.setOnScrollListener(scrollListener);
		LayoutParams params = (LayoutParams) pullToGridView.getLayoutParams();
		final int space = 20;
		params.setMargins(space, space, space, space);
		pullToGridView.setLayoutParams(params);
		// 只启动下拉加载更多
		pullToGridView.setMode(Mode.PULL_FROM_END);
		pullToGridView.setShowIndicator(false);
		pullToGridView.setShowGridLine(true);
		searchGridView = pullToGridView.getRefreshableView();
		searchGridView.setFastScrollEnabled(true);
		searchGridView.setHorizontalSpacing(space);
		searchGridView.setVerticalSpacing(space);
		searchGridView.setOnItemClickListener(this);
		searchListLayout.addView(view);
	}

	private OnRefreshListener2<ListView> initListRefreshListener()
	{
		OnRefreshListener2<ListView> refreshListener = new OnRefreshListener2<ListView>()
		{
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView)
			{

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView)
			{
				onPullUpToRefreshView(refreshView);
			}
		};
		return refreshListener;
	}

	private OnRefreshListener2<GridView> initGridRefreshListener()
	{
		OnRefreshListener2<GridView> refreshListener = new OnRefreshListener2<GridView>()
		{
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView)
			{

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView)
			{
				onPullUpToRefreshView(refreshView);
			}
		};
		return refreshListener;
	}

	protected void onPullUpToRefreshView(PullToRefreshBase<?> refreshView)
	{
		isLoadMore = true;
		pageIndex++;
		startSearchProducts(keyword);
	}

	private void startSearchProducts(String keyword)
	{
		RequestCenter.searchProducts(dataListener, keyword, "", companyId, GET_REAL_IMAGE, String.valueOf(pageIndex),
				String.valueOf(pageSize), "0", "0");
	}

	private DisposeDataListener dataListener = new DisposeDataListener()
	{
		@Override
		public void onSuccess(Object arg0)
		{
			searchListLayout.setVisibility(View.VISIBLE);
			modeImageView.setVisibility(View.VISIBLE);
			scrollTopImageView.setVisibility(View.VISIBLE);
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
						adapter = new ProductGridAdapter(ProductSearchActivity.this, products.content, false);
						searchGridView.setAdapter(adapter);
					}
					else
					{
						adapter = new ProductListAdapter(ProductSearchActivity.this, products.content, false);
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
					scrollTopImageView.setVisibility(View.GONE);
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
			isRefresh = false;
			isLoadMore = false;
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
				ToastUtil.toast(ProductSearchActivity.this, arg0);
			}
			if (adapter == null)
			{
				finish();
			}
		}
	};

	/**
	 * 刷新完成
	 */
	protected void refreshComplete()
	{
		if (pullToListView != null && pullToListView.isRefreshing())
		{
			pullToListView.onRefreshComplete();
		}
		if (pullToGridView != null && pullToGridView.isRefreshing())
		{
			pullToGridView.onRefreshComplete();
		}
	}

	/**
	 * 控制进度条的显示与否
	 * @param ifrefresh
	 */
	protected void changeProgressView(boolean ifrefresh)
	{
		progressLayout.setVisibility(View.GONE);
		if (isRefresh)
		{
			loadingLayout.setVisibility(View.GONE);
		}
	}

	/**
	 * 筛选数据后的提示窗口
	 */
	protected void showSearchMessage(ArrayList<SearchLocation> locations)
	{
		long size;
		size = Util.getLocationsNumber(locations);
		showSearchMessage(String.valueOf(size));
	}

	/**
	 * 筛选数据后的提示窗口
	 */
	protected void showSearchMessage(String numbers)
	{
		// 7a7a7a
		productsString = "<font color=\"#ffffff\">" + getResources().getString(R.string.mic_products) + "</font>";
		String allString = "<font color=\"#ffffff\">" + getResources().getString(R.string.search_hint_title)
				+ "</font>";
		if (numbers != null && !numbers.equals(""))
		{
			// 333333
			String number = "<font color=\"#ffffff\">" + " " + Util.getNumKb(Long.parseLong(numbers)) + " " + "</font>";
			View viewGroup = LayoutInflater.from(this).inflate(R.layout.show_search_msg, null);
			TextView tv = (TextView) viewGroup.findViewById(R.id.show_msg);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Utils.getWindowWidthPix(this),
					Util.dip2px(36));
			tv.setLayoutParams(params);
			tv.setGravity(Gravity.CENTER_VERTICAL);
			tv.setText(Html.fromHtml(allString + number + productsString));
			Toast toast = new Toast(this);
			toast.setView(viewGroup);
			toast.setGravity(Gravity.LEFT | Gravity.TOP, 0, Util.dip2px(47f));
			toast.show();
		}
	}

	protected void setPullToViewVisibility(int visibility)
	{
		if (isGridMode)
		{
			pullToGridView.setVisibility(visibility);
		}
		else
		{
			pullToListView.setVisibility(visibility);
		}
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.title_back_button:
			finish();
			break;
		case R.id.clear_keyword:
			keywordEdit.setText("");
			break;
		case R.id.iv_search_list_mode:
			changeMode();
			break;
		case R.id.iv_scroll_top:
			if (isGridMode)
			{
				Util.dispatchCancelEvent(searchGridView);
				searchGridView.smoothScrollToPosition(0);
			}
			else
			{
				Util.dispatchCancelEvent(searchListView);
				searchListView.smoothScrollToPosition(0);
			}
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id)
	{
		SearchProduct product = (SearchProduct) adapter.getItem(position);
		if (product != null)
		{
			Intent i = new Intent(this, ProductMessageActivity_.class);
			i.putExtra("productId", product.productId);
			startActivity(i);
		}
	}

	private void changeMode()
	{
		if (isGridMode)
		{
			isGridMode = false;
		}
		else
		{
			isGridMode = true;
		}
		SharedPreferenceManager.getInstance().putBoolean("isGridMode", isGridMode);
		updateChangedModeUI();
	}

	private void updateChangedModeUI()
	{
		if (isGridMode)
		{

			modeImageView.setImageResource(R.drawable.btn_search_list_mode_list);
			initPullToGridView();
			ArrayList<SearchProduct> dataList = adapter.getAdapterData();
			adapter = new ProductGridAdapter(ProductSearchActivity.this, dataList, false);
			searchGridView.setAdapter(adapter);

		}
		else
		{
			modeImageView.setImageResource(R.drawable.btn_search_list_mode_grid);
			initPullToListView();
			ArrayList<SearchProduct> dataList = adapter.getAdapterData();
			adapter = new ProductListAdapter(ProductSearchActivity.this, dataList, false);
			searchListView.setAdapter(adapter);

		}
		adapter.addSelectItem(searchListRecoders);
	}

}

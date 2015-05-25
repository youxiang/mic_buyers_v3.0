package com.micen.buyers.activity.searchresult;

import java.util.ArrayList;
import java.util.HashMap;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.focustech.common.util.Utils;
import com.focustech.common.widget.pulltorefresh.PullToRefreshBase;
import com.focustech.common.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.focustech.common.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.focustech.common.widget.pulltorefresh.PullToRefreshGridView;
import com.focustech.common.widget.pulltorefresh.PullToRefreshListView;
import com.micen.buyers.activity.BaseActivity;
import com.micen.buyers.activity.R;
import com.micen.buyers.db.SharedPreferenceManager;
import com.micen.buyers.manager.DataManager;
import com.micen.buyers.module.search.SearchLocation;
import com.micen.buyers.util.Util;

/**********************************************************
 * @文件名称：BaseSeachListActivity.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年3月18日 上午10:48:44
 * @文件描述：搜索结果列表页面的父Activity
 * @修改历史：2015年3月18日创建初始版本
 **********************************************************/
@EActivity
public class BaseSeachListActivity extends BaseActivity implements OnItemClickListener
{
	@ViewById(R.id.search_suggest_layout)
	protected LinearLayout suggestLayout;
	@ViewById(R.id.tv_search_suggest)
	protected TextView tvSuggest;
	@ViewById(R.id.search_list_layout)
	protected RelativeLayout searchListLayout;
	@ViewById(R.id.tv_search_noMatch)
	protected TextView tvNoMatch;

	protected PullToRefreshListView pullToListView;
	protected ListView searchListView;
	protected PullToRefreshGridView pullToGridView;
	protected GridView searchGridView;

	@ViewById(R.id.progressbar_layout)
	protected RelativeLayout progressLayout;
	@ViewById(R.id.loading_bar)
	protected RelativeLayout loadingLayout;
	@ViewById(R.id.iv_search_list_mode)
	protected ImageView modeImageView;
	@ViewById(R.id.iv_scroll_top)
	protected ImageView backTopImageView;
	/**
	 * 新增搜索框
	 */
	@ViewById(R.id.search_again_layout)
	protected LinearLayout searchLayout;
	@ViewById(R.id.search_name_text_view)
	protected TextView seachTextView;
	@ViewById(R.id.iv_search)
	protected ImageView iv_search;

	protected int pageIndex = 1;
	protected int pageSize = 20;
	protected int titleHeight;
	protected ArrayList<String> searchListRecoders;
	/**
	 * 判断Activity是否处于隐藏状态
	 */
	protected boolean isPauseState = false;
	protected boolean isSuggest = false;

	/**
	 * 产品入库类型,0代表产品，１代表供应商
	 */
	public enum SearchType
	{
		PRODUCT("0"), COMPANY("1");
		private String type;

		private SearchType(String type)
		{
			this.type = type;
		}

		@Override
		public String toString()
		{
			return type;
		}
	};

	/**
	 * 是否加载更多
	 */
	protected boolean isLoadMore = false;
	/**
	 * 是否刷新
	 */
	protected boolean isRefresh = false;
	/**
	 * 临时存储筛选条件的Map
	 */
	protected HashMap<String, String> siftMap;
	protected String productsString;

	protected boolean isGridMode = SharedPreferenceManager.getInstance().getBoolean("isGridMode", false);

	@Extra("keyword")
	protected String keyword;
	protected DataManager dbManager;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	private OnScrollListener scrollListener = new OnScrollListener()
	{

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState)
		{

		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
		{
			backTopImageView.setVisibility(firstVisibleItem == 0 ? View.INVISIBLE : View.VISIBLE);
		}
	};

	protected void initPullToListView()
	{
		searchListLayout.removeAllViews();
		View view = LayoutInflater.from(this).inflate(R.layout.pulltorefresh_listview, null);
		pullToListView = (PullToRefreshListView) view.findViewById(R.id.lv_search_list);
		pullToListView.setOnRefreshListener(initListRefreshListener());
		// 只启动下拉加载更多
		pullToListView.setMode(Mode.PULL_FROM_END);
		// 不显示指示器
		pullToListView.setShowIndicator(false);
		searchListView = pullToListView.getRefreshableView();
		searchListView.setFastScrollEnabled(true);
		searchListView.setOnItemClickListener(this);
		searchListLayout.addView(view);
		searchListView.setOnScrollListener(scrollListener);
	}

	protected void initPullToGridView()
	{
		searchListLayout.removeAllViews();
		View view = LayoutInflater.from(this).inflate(R.layout.pulltorefresh_gridview, null);
		pullToGridView = (PullToRefreshGridView) view.findViewById(R.id.gv_search_list);
		pullToGridView.setOnRefreshListener(initGridRefreshListener());
		// 只启动下拉加载更多
		pullToGridView.setMode(Mode.PULL_FROM_END);
		pullToGridView.setShowIndicator(false);
		pullToGridView.setShowGridLine(true);
		searchGridView = pullToGridView.getRefreshableView();
		searchGridView.setFastScrollEnabled(true);
		searchGridView.setOnItemClickListener(this);
		searchListLayout.addView(view);
		searchGridView.setOnScrollListener(scrollListener);
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

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{

	}

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
		//7a7a7a
		String allString = "<font color=\"#ffffff\">" + getResources().getString(R.string.search_hint_title)
				+ "</font>";
		if (numbers != null && !numbers.equals(""))
		{
			//String number = "<font color=\"#333333\">" + " " + Util.getNumKb(Long.parseLong(numbers)) + " " + "</font>";
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
}

package com.micen.buyers.view.home;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringArrayRes;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils.TruncateAt;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.focustech.common.http.ResponseEntityToModule;
import com.focustech.common.mob.DisposeDataListener;
import com.focustech.common.util.ToastUtil;
import com.focustech.common.widget.dialog.CommonDialog;
import com.focustech.common.widget.dialog.CommonDialog.DialogClickListener;
import com.micen.buyers.activity.R;
import com.micen.buyers.activity.category.CategorySecondActivity_;
import com.micen.buyers.activity.searchresult.CompanySearchListActivity_;
import com.micen.buyers.activity.searchresult.ProductSearchListActivity_;
import com.micen.buyers.adapter.search.AllCategoryAdapter;
import com.micen.buyers.adapter.search.RecentSearchAdapter;
import com.micen.buyers.adapter.search.SearchSuggestListAdapter;
import com.micen.buyers.constant.Constants;
import com.micen.buyers.constant.ProductSearchType;
import com.micen.buyers.db.DBHelper;
import com.micen.buyers.db.SharedPreferenceManager;
import com.micen.buyers.http.RequestCenter;
import com.micen.buyers.manager.DataManager;
import com.micen.buyers.manager.PopupManager;
import com.micen.buyers.manager.SysManager;
import com.micen.buyers.module.category.Categories;
import com.micen.buyers.module.category.Category;
import com.micen.buyers.module.db.CategoryHistory;
import com.micen.buyers.module.db.SearchRecord;
import com.micen.buyers.module.search.Association;
import com.micen.buyers.module.search.SearchSuggest;
import com.micen.buyers.util.Util;
import com.micen.buyers.widget.flowlayout.FlowLayout;
import com.micen.buyers.widget.flowlayout.FlowLayout.LayoutParams;

/**********************************************************
 * @文件名称：SearchKeywordFragment.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年3月10日 上午9:06:47
 * @文件描述：首页底部搜索Frgment
 * @修改历史：2015年3月10日创建初始版本
 **********************************************************/
@EFragment(R.layout.search_fragment_layout)
public class SearchFragment extends HomeBaseFragment implements OnClickListener, OnItemClickListener
{
	protected Activity mContext;
	protected PopupWindow searchTypePop;
	protected CommonDialog dialog;
	protected DataManager dbManager;
	/**
	 * 页面控件
	 */
	@ViewById(R.id.search_recent_list)
	protected ListView recentSearchList;

	@ViewById(R.id.keyword_edit_text)
	protected EditText searchEditText;
	@ViewById(R.id.search_btn)
	protected TextView searchButton;
	@ViewById(R.id.search_title_layout)
	protected RelativeLayout searchLayout;
	protected LinearLayout searchTypeProduct;
	protected LinearLayout searchTypeSupplier;
	@ViewById(R.id.search_type)
	protected TextView searchTypeTx;

	@ViewById(R.id.search_suggest_list)
	protected ListView suggestList;
	protected SearchSuggestListAdapter suggestAdapter;

	@ViewById(R.id.clear_keyword)
	protected ImageView clearImage;
	protected TextView clearAllBtn;
	@ViewById(R.id.search_recent_layout)
	protected LinearLayout recentLayout;
	@ViewById(R.id.search_suggest_layout)
	protected LinearLayout suggestLayout;

	@ViewById(R.id.category_layout)
	protected ScrollView categoryLayout;

	@ViewById(R.id.category_list_view)
	protected ListView allCategoryListView;

	@ViewById(R.id.delete_category_btn)
	protected ImageView deleteRecentCat;

	@ViewById(R.id.recent_layout)
	protected LinearLayout recentCategoryLayout;

	@ViewById(R.id.edit_title)
	protected RelativeLayout editTitleLayout;

	@ViewById(R.id.recent_category_flow_layout)
	protected FlowLayout flowLayout;
	/**
	 * 数据源
	 */
	protected ArrayList<SearchRecord> recentSearchDataList;
	protected RecentSearchAdapter recentSearchAdapter;
	protected AllCategoryAdapter allCategoryAdapter;
	protected String searchType;
	@StringArrayRes(R.array.search_type)
	protected String[] searchTypes;
	/**
	 * 读取本地Json文件
	 */
	private InputStream is;
	protected Categories categories;
	// 区分两种模式
	protected boolean modeFlag;

	private boolean isShowHistory = false;

	private View recentFooterView;

	protected Handler mHandler = new Handler()
	{
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case Constants.DELETE_RECENT_KEYWORDS:
				refreshRecentKeywordsList();
				break;
			case Constants.GET_SUGGEST_DATA:// 搜索联想结果列表
				if (!"".equals(searchEditText.getText().toString().trim()))
				{
					enterSuggestMode();
					suggestAdapter = new SearchSuggestListAdapter(mContext, (ArrayList<Association>) msg.obj);
					suggestList.setAdapter(suggestAdapter);
				}
				break;
			default:
				break;
			}
		};
	};

	public SearchFragment()
	{
	}

	@Override
	public void onResume()
	{
		super.onResume();
		// 事件统计$10004 Search页 页面事件
		SysManager.analysis(R.string.a_type_page, R.string.p10004);
		addDataToFlow();
	}

	@Override
	public void onHiddenChanged(boolean hidden)
	{
		super.onHiddenChanged(hidden);
		if (!hidden)
		{
			if (isShowHistory && searchEditText != null)
			{
				searchEditText.setFocusable(true);
				searchEditText.setFocusableInTouchMode(true);
				searchEditText.requestFocus();
				SysManager.getInstance().showInputKey(getActivity());
			}
		}
	}

	@AfterViews
	protected void initViewData()
	{
		mContext = getActivity();
		dbManager = DataManager.getInstance();
		searchType = searchTypes[0];
		searchTypeTx.setText(getResources().getString(R.string.mic_search_keywords_product));
		recentSearchAdapter = new RecentSearchAdapter(mContext, recentSearchDataList);
		initAllCategory();
		addDataToFlow();

		recentSearchList.setOnItemClickListener(this);
		recentSearchList.setOnItemLongClickListener(recentSearchesLongClickListeners);
		searchEditText.addTextChangedListener(watcher);
		searchEditText.setOnFocusChangeListener(focusListener);
		searchEditText.setOnEditorActionListener(editorAction);
		searchButton.setOnClickListener(this);
		suggestList.setOnItemClickListener(this);
		clearImage.setOnClickListener(this);
		deleteRecentCat.setOnClickListener(this);
		searchTypeTx.setOnClickListener(this);
		searchEditText.setOnTouchListener(touchListener);
		allCategoryListView.setOnItemClickListener(this);
		Util.setListViewHeightBasedOnChildren(allCategoryListView);

		recentFooterView = LayoutInflater.from(getActivity()).inflate(R.layout.search_recent_footer, null);
		TextView btnClear = (TextView) recentFooterView.findViewById(R.id.search_recent_clear);
		btnClear.setOnClickListener(this);

		if (isShowHistory)
		{
			searchEditText.setFocusable(true);
			searchEditText.setFocusableInTouchMode(true);
			searchEditText.requestFocus();
			SysManager.getInstance().showInputKey(getActivity());
		}
	}

	public void setShowHistory(boolean isShowHistory)
	{
		this.isShowHistory = isShowHistory;
	}

	public void clearSearchText()
	{
		if (searchEditText != null)
		{
			searchEditText.setText("");
		}
	}

	private void initAllCategory()
	{
		try
		{
			is = this.getResources().openRawResource(R.raw.category);
			byte[] buffer = new byte[is.available()];
			is.read(buffer);
			String cateJson = new String(buffer);
			categories = (Categories) ResponseEntityToModule.parseJsonToModule(cateJson, Categories.class);
			allCategoryAdapter = new AllCategoryAdapter(mContext, categories.content);
			allCategoryListView.setAdapter(allCategoryAdapter);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (is != null)
			{
				try
				{
					is.close();
					is = null;
				}
				catch (IOException e)
				{
				}
			}
		}
	}

	/**
	 * 将最近查看的目录数据添加到Flow中
	 * @param categoriesBrowseHisBean
	 */
	private void addDataToFlow()
	{
		ArrayList<CategoryHistory> recentCategoryList = dbManager.initRecentCategory();
		if (recentCategoryList.size() <= 0)
		{
			recentCategoryLayout.setVisibility(View.GONE);
		}
		else
		{
			flowLayout.removeAllViews();
			for (int i = 0; i < recentCategoryList.size(); i++)
			{
				flowLayout.addView(createButton(recentCategoryList.get(i)));
			}
		}

	}

	/**
	 * 创建流式按钮
	 * @param categoryHistory
	 * @return
	 */
	private TextView createButton(final CategoryHistory categoryHistory)
	{
		TextView recentCategoryBtn = new TextView(mContext);
		FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(LayoutParams.WRAP_CONTENT, Util.dip2px(35));
		params.leftMargin = Util.dip2px(9);
		params.topMargin = Util.dip2px(16);
		recentCategoryBtn.setMaxWidth(Util.dip2px(150));
		recentCategoryBtn.setLayoutParams(params);
		recentCategoryBtn.setEllipsize(TruncateAt.END);
		recentCategoryBtn.setSingleLine(true);
		recentCategoryBtn.setGravity(Gravity.CENTER);
		recentCategoryBtn.setBackgroundResource(R.drawable.bg_recent_category_drawable);
		recentCategoryBtn.setTextColor(Color.parseColor("#333333"));
		recentCategoryBtn.setText(Util.CutString(categoryHistory.sourceSubject, ">>"));
		recentCategoryBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				// 事件统计25 点击最近目录（目录） 点击事件
				SysManager.analysis(R.string.a_type_click, R.string.c25);
				// 直接获取数据
				Intent intent = new Intent(getActivity(), ProductSearchListActivity_.class);
				intent.putExtra("searchType", ProductSearchType.Category.getValue());
				intent.putExtra("category", categoryHistory.category);
				intent.putExtra("isFavorites", Boolean.parseBoolean(categoryHistory.isFavorites));
				intent.putExtra("sourceSubject", categoryHistory.sourceSubject);
				intent.putExtra("keyword", Util.CutString(categoryHistory.sourceSubject, ">>"));
				intent.putExtra("isCategory", true);
				startActivity(intent);
			}
		});

		return recentCategoryBtn;
	}

	/**
	 * 点击获取焦点
	 */
	private OnTouchListener touchListener = new OnTouchListener()
	{
		@Override
		public boolean onTouch(View arg0, MotionEvent arg1)
		{
			searchEditText.setFocusable(true);
			searchEditText.setFocusableInTouchMode(true);
			return false;
		}
	};
	private OnFocusChangeListener focusListener = new OnFocusChangeListener()
	{
		@Override
		public void onFocusChange(View arg0, boolean hasFocus)
		{
			if (hasFocus)
			{
				enterHistoryMode();
				refreshRecentKeywordsList();
			}
		}
	};

	/**
	 * 进入历史记录模式
	 */
	private void enterHistoryMode()
	{
		modeFlag = true;
		categoryLayout.setVisibility(View.GONE);
		suggestLayout.setVisibility(View.GONE);
		recentLayout.setVisibility(View.VISIBLE);
	}

	private void enterSuggestMode()
	{
		modeFlag = true;
		categoryLayout.setVisibility(View.GONE);
		recentLayout.setVisibility(View.GONE);
		suggestLayout.setVisibility(View.VISIBLE);
	}

	// 回到初始状态
	private boolean enterInitMode()
	{
		if (modeFlag)
		{
			isShowHistory = false;
			modeFlag = false;
			categoryLayout.setVisibility(View.VISIBLE);
			recentLayout.setVisibility(View.GONE);
			suggestLayout.setVisibility(View.GONE);
			searchEditText.setFocusable(false);
			searchEditText.setFocusableInTouchMode(false);
			return false;
		}
		else
		{
			return true;
		}
	}

	public boolean onBackPressedFragment()
	{
		// 事件统计 20 取消搜索（搜索） 点击事件
		SysManager.analysis(R.string.a_type_click, R.string.c20);
		return enterInitMode();
	}

	/**
	 * 最近搜索关键词列表长按删除
	 */
	private OnItemLongClickListener recentSearchesLongClickListeners = new OnItemLongClickListener()
	{
		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
		{
			if (arg2 == arg0.getAdapter().getCount() - 1)
				return true;
			SearchRecord recentSearch = (SearchRecord) recentSearchAdapter.getItem(arg2);
			deleteRecentSearchesDialog(recentSearch);
			return true;
		}
	};

	/**
	 * EditText文本监听器
	 */
	private TextWatcher watcher = new TextWatcher()
	{
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count)
		{
			if (!"".equals(searchEditText.getText().toString().trim()))
			{
				if ("1".equals(getSuggestType()))
				{
					RequestCenter.searchSuggest(suggestDataListener, searchEditText.getText().toString(),
							getSuggestType());
				}
				clearImage.setVisibility(View.VISIBLE);
			}
			else
			{
				enterHistoryMode();
				clearImage.setVisibility(View.GONE);
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

	/**
	 * 联想词接口回调
	 */
	private DisposeDataListener suggestDataListener = new DisposeDataListener()
	{
		@Override
		public void onSuccess(Object result)
		{
			SearchSuggest suggest = (SearchSuggest) result;
			mHandler.sendMessage(mHandler.obtainMessage(Constants.GET_SUGGEST_DATA, suggest.association));
		}

		@Override
		public void onFailure(Object failedReason)
		{
			ToastUtil.toast(mContext, failedReason);
		}
	};

	/**
	 * 键盘搜索按钮事件响应
	 */
	private OnEditorActionListener editorAction = new OnEditorActionListener()
	{
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
		{
			if (actionId == EditorInfo.IME_ACTION_SEARCH)
			{
				startSearch(searchType, searchEditText.getText().toString().trim());
				return true;
			}
			return false;
		}
	};

	/**
	 *跳转到搜索页面
	 */
	private void startSearch(String tempsearchType, String keywords)
	{
		Util.hideSoftInputMethod(mContext, searchEditText);
		if (!searchKeyWordsCheck(keywords))
		{
			return;
		}

		// 点击历史，推荐时，搜索框的文本设置成相应的内容
		searchEditText.setText(keywords);

		dbManager.insertKeyWords(keywords, searchType);
		refreshRecentKeywordsList();
		if (tempsearchType.equals(searchTypes[0]))
		{
			SharedPreferenceManager.getInstance().putString("lastSearchKeyword", keywords);
			Intent intent = new Intent(mContext, ProductSearchListActivity_.class);
			intent.putExtra("searchType", ProductSearchType.Keyword.getValue());
			intent.putExtra("keyword", keywords);
			startActivity(intent);
		}
		else if (tempsearchType.equals(searchTypes[1]))
		{
			Intent intent = new Intent(mContext, CompanySearchListActivity_.class);
			intent.putExtra("keyword", keywords);
			startActivity(intent);
		}
	}

	/**
	 * 获取联想类型
	 * @return
	 */
	private String getSuggestType()
	{
		String type = "0";
		if (searchType.equals(searchTypes[0]))
		{
			type = "1";
		}
		else if (searchType.equals(searchTypes[1]))
		{
			type = "2";
		}
		return type;
	}

	/**
	 * 刷新最近搜索关键词列表
	 */
	private void refreshRecentKeywordsList()
	{
		ArrayList<SearchRecord> dataList = dbManager.refreshRecentKeywordsList(searchType);
		if (dataList.size() > 0 && recentSearchList.getFooterViewsCount() == 0)
		{
			recentSearchList.addFooterView(recentFooterView);
		}
		recentSearchAdapter.setRecentSearchDataList(dataList);
		recentSearchList.setAdapter(recentSearchAdapter);
	}

	/**
	 * 搜索关键词验证
	 * @param keywords
	 * @return
	 */
	private boolean searchKeyWordsCheck(String keywords)
	{
		if ("".equals(keywords.trim()))
		{
			ToastUtil.toast(mContext, R.string.mic_search_key_words_page_keywords_empty);
			return false;
		}
		else if (Util.isChinese(keywords.trim()))
		{
			ToastUtil.toast(mContext, R.string.mic_search_key_words_page_keywords_contain_chinese);
			return false;
		}
		else
		{
			return true;
		}
	}

	/**
	 * 删除记录对话框
	 */
	private void deleteRecentSearchesDialog(final SearchRecord recentSearch)
	{
		dialog = new CommonDialog(mContext, mContext.getString(R.string.mic_delete),
				mContext.getString(R.string.confirm), mContext.getString(R.string.cancel), Util.dip2px(278),
				new DialogClickListener()
				{
					@Override
					public void onDialogClick()
					{
						dbManager.deleteRecentKeyWord(recentSearch.recentKeywords, recentSearch.searchType);
						mHandler.sendEmptyMessage(Constants.DELETE_RECENT_KEYWORDS);
					}
				});
		dialog.show();
	}

	@Override
	public String getChildTag()
	{
		return SearchFragment.class.getName();
	}

	/*
	 * 所有列表的点击事件
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		switch (arg0.getId())
		{
		case R.id.search_suggest_list:
			SysManager.analysis(mContext, mContext.getString(R.string.a_type_click), mContext.getString(R.string.a043));
			Association association = (Association) arg0.getAdapter().getItem(arg2);
			startSearch(searchType, association.text);
			break;
		case R.id.search_recent_list:
			if (arg2 == arg0.getAdapter().getCount() - 1)
				return;
			// 事件统计21 使用最近搜索关键词（搜索） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c21);

			SysManager.analysis(mContext, mContext.getString(R.string.a_type_click), mContext.getString(R.string.a038));
			SearchRecord recentSearchBean = (SearchRecord) recentSearchAdapter.getItem(arg2);
			startSearch(recentSearchBean.searchType, recentSearchBean.recentKeywords);
			break;
		case R.id.category_list_view:
			// 事件统计 26 浏览一级目录（目录） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c26);
			Category tempCat = (Category) allCategoryAdapter.getItem(arg2);
			Intent intent = new Intent(getActivity(), CategorySecondActivity_.class);
			intent.putExtra("Category", tempCat);
			intent.putExtra("isSearchCategory", false);
			startActivity(intent);
			break;
		}
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.delete_category_btn:
			// 事件统计 24 删除最近目录（目录） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c24);
			recentCategoryLayout.setVisibility(View.GONE);
			deleteRecentCategory();
			break;
		case R.id.clear_keyword:
			// 事件统计22 清空输入关键词（搜索） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c22);
			searchEditText.setText("");
			break;
		case R.id.search_btn:
			// 事件统计 18 跳转搜索 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c18);
			startSearch(searchType, searchEditText.getText().toString().trim());
			break;
		case R.id.search_type:
			// SysManager.analysis(mContext, getString(R.string.a_type_click), getString(R.string.a034));
			// 事件统计 19 搜索条件选项（搜索） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c19);
			initPopMenu(v);
			break;
		case R.id.mic_search_keywords_search_type_pop_product_layout:
			if (searchTypePop != null)
			{
				searchTypePop.dismiss();
			}
			searchEditText.setHint(R.string.search_products);
			searchType = searchTypes[0];
			searchTypeTx.setText(getResources().getString(R.string.mic_search_keywords_product));
			refreshRecentKeywordsList();
			break;
		case R.id.mic_search_keywords_search_type_pop_supplier_layout:
			if (searchTypePop != null)
			{
				searchTypePop.dismiss();
			}
			searchEditText.setHint(R.string.search_suppliers);
			searchType = searchTypes[1];
			searchTypeTx.setText(getResources().getString(R.string.mic_search_keywords_supplier));
			refreshRecentKeywordsList();
			break;
		case R.id.search_recent_clear:
			// 事件统计 23 清空最近搜索关键词（搜索） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c23);
			if (recentSearchAdapter.getCount() > 0)
			{
				dialog = new CommonDialog(mContext, mContext.getString(R.string.delete_all),
						mContext.getString(R.string.confirm), mContext.getString(R.string.cancel), Util.dip2px(278),
						new DialogClickListener()
						{
							@Override
							public void onDialogClick()
							{
								recentSearchList.removeFooterView(recentFooterView);
								dbManager.deleteAllRecentKeyWord();
								recentSearchAdapter.setRecentSearchDataList(new ArrayList<SearchRecord>());
								recentSearchList.setAdapter(recentSearchAdapter);
							}
						});
				dialog.show();
			}
			break;
		}
	}

	private void deleteRecentCategory()
	{
		int line = DBHelper.getInstance().delete(DBHelper.CATEGORIES_BROWSE_HISTORY_, null, null);
		if (line > 0)
		{
			addDataToFlow();
		}
	}

	/**
	 * 搜索类型pop
	 * @param v
	 */
	private void initPopMenu(View v)
	{
		searchTypePop = PopupManager.getInstance().showSearchKeywordsSearchTypePopup(mContext,
				mContext.getLayoutInflater().inflate(R.layout.search_type_pop_layout, null), searchLayout,
				Util.dip2px(151), Util.dip2px(100));
		searchTypeProduct = (LinearLayout) searchTypePop.getContentView().findViewById(
				R.id.mic_search_keywords_search_type_pop_product_layout);
		searchTypeSupplier = (LinearLayout) searchTypePop.getContentView().findViewById(
				R.id.mic_search_keywords_search_type_pop_supplier_layout);
		searchTypeProduct.setOnClickListener(this);
		searchTypeSupplier.setOnClickListener(this);
	}
}
package com.micen.buyers.activity.searchresult;

import java.util.ArrayList;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringArrayRes;

import com.focustech.common.mob.DisposeDataListener;
import com.focustech.common.util.ToastUtil;
import com.focustech.common.widget.dialog.CommonDialog;
import com.focustech.common.widget.dialog.CommonDialog.DialogClickListener;
import com.micen.buyers.activity.BaseActivity;
import com.micen.buyers.activity.R;
import com.micen.buyers.adapter.search.RecentSearchAdapter;
import com.micen.buyers.adapter.search.SearchSuggestListAdapter;
import com.micen.buyers.constant.Constants;
import com.micen.buyers.constant.ProductSearchType;
import com.micen.buyers.db.SharedPreferenceManager;
import com.micen.buyers.http.RequestCenter;
import com.micen.buyers.manager.ActivityManager;
import com.micen.buyers.manager.DataManager;
import com.micen.buyers.manager.PopupManager;
import com.micen.buyers.manager.SysManager;
import com.micen.buyers.module.db.SearchRecord;
import com.micen.buyers.module.search.Association;
import com.micen.buyers.module.search.SearchSuggest;
import com.micen.buyers.util.Util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TextView.OnEditorActionListener;

@EActivity
public class SearchActivity extends BaseActivity implements OnClickListener, OnItemClickListener
{
	protected PopupWindow searchTypePop;
	protected CommonDialog dialog;
	protected DataManager dbManager;

	@ViewById(R.id.search_recent_list)
	protected ListView recentSearchList;
	@ViewById(R.id.search_suggest_list)
	protected ListView suggestList;

	@ViewById(R.id.keyword_edit_text)
	protected EditText searchEditText;
	@ViewById(R.id.search_btn)
	protected TextView searchButton;

	@ViewById(R.id.search_recent_layout)
	protected LinearLayout recentLayout;
	@ViewById(R.id.search_suggest_layout)
	protected LinearLayout suggestLayout;

	@ViewById(R.id.search_title_layout)
	protected RelativeLayout searchLayout;

	@ViewById(R.id.search_type)
	protected TextView searchTypeTx;

	@ViewById(R.id.clear_keyword)
	protected ImageView clearImage;
	protected TextView clearAllBtn;

	@ViewById(R.id.recent_layout)
	protected LinearLayout recentCategoryLayout;

	@StringArrayRes(R.array.search_type)
	protected String[] searchTypes;

	@ViewById(R.id.title_back_button)
	protected ImageView titleLeftButton;

	protected LinearLayout searchTypeProduct;
	protected LinearLayout searchTypeSupplier;

	/**
	 * 数据源
	 */
	protected ArrayList<SearchRecord> recentSearchDataList;
	protected RecentSearchAdapter recentSearchAdapter;

	protected SearchSuggestListAdapter suggestAdapter;

	protected String searchType;

	// 区分两种模式
	protected boolean modeFlag;

	private boolean isShowHistory = true;

	private View recentFooterView;

	private Context mContext;

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
					suggestAdapter = new SearchSuggestListAdapter(SearchActivity.this, (ArrayList<Association>) msg.obj);
					suggestList.setAdapter(suggestAdapter);
				}
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_search_layout);

		Util.startDeveloperMode();
		Constants.currentActivity = this;
		ActivityManager.getInstance().put(this);
	}

	@AfterViews
	protected void initData()
	{
		mContext = this.getApplicationContext();
		dbManager = DataManager.getInstance();
		searchType = searchTypes[0];
		searchTypeTx.setText(getResources().getString(R.string.mic_search_keywords_product));
		recentSearchAdapter = new RecentSearchAdapter(this, recentSearchDataList);

		recentSearchList.setOnItemClickListener(this);
		recentSearchList.setOnItemLongClickListener(recentSearchesLongClickListeners);
		searchEditText.addTextChangedListener(watcher);
		searchEditText.setOnFocusChangeListener(focusListener);
		searchEditText.setOnEditorActionListener(editorAction);
		searchButton.setOnClickListener(this);
		suggestList.setOnItemClickListener(this);
		clearImage.setOnClickListener(this);
		searchTypeTx.setOnClickListener(this);
		searchEditText.setOnTouchListener(touchListener);
		titleLeftButton.setOnClickListener(this);
		;

		recentFooterView = LayoutInflater.from(this).inflate(R.layout.search_recent_footer, null);
		TextView btnClear = (TextView) recentFooterView.findViewById(R.id.search_recent_clear);
		btnClear.setOnClickListener(this);

		if (isShowHistory)
		{
			searchEditText.setFocusable(true);
			searchEditText.setFocusableInTouchMode(true);
			searchEditText.requestFocus();
			SysManager.getInstance().showInputKey(this);
		}
	}

	private void enterSuggestMode()
	{
		modeFlag = true;
		// categoryLayout.setVisibility(View.GONE);
		recentLayout.setVisibility(View.GONE);
		suggestLayout.setVisibility(View.VISIBLE);
	}

	/**
	 * 进入历史记录模式
	 */
	private void enterHistoryMode()
	{
		modeFlag = true;
		// categoryLayout.setVisibility(View.GONE);
		suggestLayout.setVisibility(View.GONE);
		recentLayout.setVisibility(View.VISIBLE);
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch (v.getId())
		{
		case R.id.clear_keyword:
			// 事件统计22 清空输入关键词（搜索） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c22);
			searchEditText.setText("");
			break;
		case R.id.search_btn:
			startSearch(searchType, searchEditText.getText().toString().trim());
			break;
		case R.id.search_type:
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
		case R.id.title_back_button:
			finish();
			break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		// TODO Auto-generated method stub
		switch (parent.getId())
		{
		case R.id.search_suggest_list:
			SysManager.analysis(mContext, mContext.getString(R.string.a_type_click), mContext.getString(R.string.a043));
			Association association = (Association) parent.getAdapter().getItem(position);
			startSearch(searchType, association.text);
			break;
		case R.id.search_recent_list:
			if (position == parent.getAdapter().getCount() - 1)
				return;
			// 事件统计21 使用最近搜索关键词（搜索） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c21);

			SysManager.analysis(mContext, mContext.getString(R.string.a_type_click), mContext.getString(R.string.a038));
			SearchRecord recentSearchBean = (SearchRecord) recentSearchAdapter.getItem(position);
			startSearch(recentSearchBean.searchType, recentSearchBean.recentKeywords);
			break;
		}

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

	/**
	 * 搜索类型pop
	 * @param v
	 */
	private void initPopMenu(View v)
	{
		searchTypePop = PopupManager.getInstance().showSearchKeywordsSearchTypePopup(mContext,
				this.getLayoutInflater().inflate(R.layout.search_type_pop_layout, null), searchLayout,
				Util.dip2px(151), Util.dip2px(100));
		searchTypeProduct = (LinearLayout) searchTypePop.getContentView().findViewById(
				R.id.mic_search_keywords_search_type_pop_product_layout);
		searchTypeSupplier = (LinearLayout) searchTypePop.getContentView().findViewById(
				R.id.mic_search_keywords_search_type_pop_supplier_layout);
		searchTypeProduct.setOnClickListener(this);
		searchTypeSupplier.setOnClickListener(this);
	}

}

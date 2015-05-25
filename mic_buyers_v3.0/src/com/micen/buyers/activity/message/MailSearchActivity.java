package com.micen.buyers.activity.message;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.focustech.common.mob.DisposeDataListener;
import com.focustech.common.util.ToastUtil;
import com.focustech.common.widget.pulltorefresh.PullToRefreshBase;
import com.focustech.common.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.focustech.common.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.focustech.common.widget.pulltorefresh.PullToRefreshListView;
import com.micen.buyers.activity.BaseActivity;
import com.micen.buyers.activity.R;
import com.micen.buyers.adapter.mail.MailSearchListAdapter;
import com.micen.buyers.http.RequestCenter;
import com.micen.buyers.manager.PopupManager;
import com.micen.buyers.manager.SysManager;
import com.micen.buyers.module.mail.Mails;
import com.micen.buyers.util.BuyerApplication;
import com.micen.buyers.util.Util;

/**********************************************************
 * @文件名称：MailSearchActivity.java
 * @文件作者：xiongjiangwei
 * @创建时间：2014年7月14日 上午9:57:12
 * @文件描述：邮件搜索页
 * @修改历史：2014年7月14日创建初始版本
 **********************************************************/
@EActivity
public class MailSearchActivity extends BaseActivity
{
	@ViewById(R.id.mail_search_button)
	protected TextView searchButton;
	@ViewById(R.id.mail_search_type_layout)
	protected RelativeLayout typeLayout;
	@ViewById(R.id.mail_search_type_text)
	protected TextView typeText;
	@ViewById(R.id.mail_search_keywords_edittext)
	protected EditText inputText;
	@ViewById(R.id.mail_search_keywords_clear)
	protected ImageView clearImage;
	@ViewById(R.id.mail_search_empty)
	protected TextView emptyText;
	@ViewById(R.id.progressbar_layout)
	protected RelativeLayout progressLayout;
	@ViewById(R.id.mail_search_list)
	protected PullToRefreshListView pullToListView;

	private ListView listView;
	private String action = "0";// 默认搜索收件箱
	private MailSearchListAdapter adapter;
	private int pageIndex = 1;
	private boolean isRefresh = false;

	// 增加变量，记录是否搜索过
	private boolean isSearched;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mail_search);
	}

	@Override
	protected void onRestart()
	{
		super.onRestart();
		adapter = null;
		listView.setAdapter(adapter);
		pageIndex = 1;
		startSearchMail(pageIndex);
	}

	@AfterViews
	protected void initView()
	{
		if (getIntent().getExtras() != null)
		{
			action = getIntent().getExtras().getString("action");
		}
		typeText.setTag("2"); // 默认在subject中搜索内容
		pullToListView.setMode(Mode.PULL_FROM_END);
		// 不显示指示器
		pullToListView.setShowIndicator(false);
		pullToListView.setOnRefreshListener(refreshListener);
		pullToListView.setVisibility(View.GONE);
		listView = pullToListView.getRefreshableView();
		listView.setOnItemClickListener(itemClick);

		inputText.addTextChangedListener(watcher);
		inputText.setOnEditorActionListener(editAction);
		typeLayout.setOnClickListener(this);
		searchButton.setOnClickListener(this);
		clearImage.setOnClickListener(this);
	}

	private OnEditorActionListener editAction = new OnEditorActionListener()
	{
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
		{
			if (actionId == EditorInfo.IME_ACTION_SEARCH)
			{
				searchMail();
				return true;
			}
			return false;
		}
	};

	private OnItemClickListener itemClick = new OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
		{
			BuyerApplication.getInstance().setMailDataList(
					((MailSearchListAdapter) arg0.getAdapter()).getAdapterDataList());
			Intent intent = new Intent(MailSearchActivity.this, MailDetailActivity_.class);
			intent.putExtra("action", action);
			intent.putExtra("position", String.valueOf(arg2));
			startActivity(intent);
		}
	};

	private OnRefreshListener2<ListView> refreshListener = new OnRefreshListener2<ListView>()
	{
		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView)
		{

		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView)
		{
			pageIndex++;
			isRefresh = true;
			if (!"".equals(inputText.getText().toString().trim()))
			{
				hideSoftInputMethod();
				if (!Util.isChinese(inputText.getText().toString().trim()))
				{
					startSearchMail(pageIndex);
				}
			}
			else
			{
				refreshComplete();
			}
		}
	};

	private TextWatcher watcher = new TextWatcher()
	{
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count)
		{
			if (!"".equals(inputText.getText().toString().trim()))
			{
				clearImage.setVisibility(View.VISIBLE);
			}
			else
			{
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
	 * 开始搜索
	 */
	private void searchMail()
	{
		if (!"".equals(inputText.getText().toString().trim()))
		{
			hideSoftInputMethod();
			if (!Util.isChinese(inputText.getText().toString().trim()))
			{
				adapter = null;
				listView.setAdapter(adapter);
				pageIndex = 1;
				isSearched = true;
				startSearchMail(pageIndex);
			}
			else
			{
				ToastUtil.toast(this, "Please input the information in English only");
			}
		}
		else
		{
			ToastUtil.toast(this, "Please input the keyword");
		}
	}

	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		switch (v.getId())
		{
		case R.id.mail_search_type_layout:
			popupActionList(v);
			break;
		case R.id.mail_search_button:
			searchMail();
			break;
		case R.id.mail_search_keywords_clear:
			// 事件统计42 删除搜索词（询盘搜索） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c42);
			inputText.setText("");
			break;
		case R.id.mail_search_type_pop_subject_layout: // 在subject中搜索内容
			// 事件统计 44 切换询盘搜索条件 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c44);
			PopupManager.getInstance().dismissPopup();
			typeText.setText(R.string.subject);
			typeText.setTag("2");
			break;
		case R.id.mail_search_type_pop_recipient_layout: // 在recipient中搜索内容
			// 事件统计 44 切换询盘搜索条件 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c44);
			PopupManager.getInstance().dismissPopup();
			typeText.setText(R.string.recipient);
			typeText.setTag("1");
			break;
		case R.id.mail_search_type_pop_sender_layout: // 在sender中搜索内容
			// 事件统计 44 切换询盘搜索条件 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c44);
			PopupManager.getInstance().dismissPopup();
			typeText.setText(R.string.sender);
			typeText.setTag("0");
			break;
		}
	}

	/**
	 * 发起搜索邮件请求
	 * @param pageIndex
	 */
	private void startSearchMail(int pageIndex)
	{
		/**
		 * 开始请求时要将结果都隐藏
		 */
		emptyText.setVisibility(View.GONE);
		if (isRefresh)
		{
			progressLayout.setVisibility(View.GONE);
		}
		else
		{
			progressLayout.setVisibility(View.VISIBLE);
		}
		RequestCenter.getMailList(mailListener, inputText.getText().toString().trim(), String.valueOf(pageIndex), "20",
				typeText.getTag().toString(), action);
	}

	private DisposeDataListener mailListener = new DisposeDataListener()
	{
		@Override
		public void onSuccess(Object arg0)
		{
			progressLayout.setVisibility(View.GONE);
			Mails mails = (Mails) arg0;
			refreshComplete();
			if (mails.content != null && mails.content.size() > 0)
			{
				pullToListView.setVisibility(View.VISIBLE);
				emptyText.setVisibility(View.GONE);
				if (adapter == null)
				{
					adapter = new MailSearchListAdapter(MailSearchActivity.this, mails.content, action);
					listView.setAdapter(adapter);
				}
				else
				{
					adapter.addMails(mails.content);
				}
			}
			else
			{
				progressLayout.setVisibility(View.GONE);
				if (adapter == null)
				{
					pullToListView.setVisibility(View.GONE);
					emptyText.setVisibility(View.VISIBLE);
				}
				else
				{
					pullToListView.setVisibility(View.VISIBLE);
					emptyText.setVisibility(View.GONE);
				}
			}

			isRefresh = false;
		}

		@Override
		public void onFailure(Object failedReason)
		{
			progressLayout.setVisibility(View.GONE);
			refreshComplete();
			isRefresh = false;
			ToastUtil.toast(MailSearchActivity.this, failedReason);
		}
	};

	private void hideSoftInputMethod()
	{
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(inputText.getWindowToken(), 0);
	}

	/**
	 * 弹出搜索条件列表
	 * @param v
	 */
	private void popupActionList(View targetView)
	{
		PopupManager.getInstance().showSearchKeywordsSearchTypePopup(this,
				getLayoutInflater().inflate(R.layout.mail_search_popup_layout, null), (View) targetView.getParent(),
				Util.dip2px(151), Util.dip2px(150));
	}

	/**
	 * 刷新完成
	 */
	private void refreshComplete()
	{
		if (pullToListView != null)
		{
			pullToListView.onRefreshComplete();
		}
		isRefresh = false;
	}

	@Override
	public void onBackPressed()
	{
		// TODO Auto-generated method stub
		if (!isSearched)
		{
			// 事件统计 43 取消询盘搜索 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c43);
		}
		super.onBackPressed();
	}

}

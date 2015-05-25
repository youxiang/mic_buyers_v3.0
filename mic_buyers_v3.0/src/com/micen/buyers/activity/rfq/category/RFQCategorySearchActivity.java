package com.micen.buyers.activity.rfq.category;

import java.util.ArrayList;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.focustech.common.mob.DisposeDataListener;
import com.focustech.common.util.ToastUtil;
import com.micen.buyers.activity.BaseActivity;
import com.micen.buyers.activity.R;
import com.micen.buyers.adapter.rfq.CategorySearchListAdapter;
import com.micen.buyers.constant.Constants;
import com.micen.buyers.http.RequestCenter;
import com.micen.buyers.manager.SysManager;
import com.micen.buyers.module.category.Category;
import com.micen.buyers.module.category.SearchCategory;
import com.micen.buyers.module.category.SearchCategoryContent;
import com.micen.buyers.view.SearchListProgressBar;

/**********************************************************
 * @文件名称：RFQCategorySearchActivity.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年5月7日 下午5:05:32
 * @文件描述：RFQ目录搜索页
 * @修改历史：2015年5月7日创建初始版本
 **********************************************************/
@EActivity
public class RFQCategorySearchActivity extends BaseActivity
{
	@ViewById(R.id.category_search_result_list)
	protected ListView searchResultList;
	@ViewById(R.id.keyword_edit_text)
	protected EditText inputEdit;
	@ViewById(R.id.search_btn)
	protected TextView searchButton;
	@ViewById(R.id.progressbar_layout)
	protected SearchListProgressBar progressBarLayout;
	@ViewById(R.id.clear_keyword)
	protected ImageView clearImage;

	private CategorySearchListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rfq_category_search);
	}

	@AfterViews
	protected void initView()
	{
		titleLeftButton.setImageResource(R.drawable.ic_title_back);
		titleLeftButton.setOnClickListener(this);
		titleText.setText(R.string.mic_rfq_category_search_hint);
		titleRightButton1.setVisibility(View.GONE);
		titleRightButton2.setVisibility(View.GONE);
		titleRightButton3.setVisibility(View.GONE);

		searchResultList.setOnItemClickListener(itemClick);
		inputEdit.setOnEditorActionListener(editorAction);
		inputEdit.addTextChangedListener(watcher);
		searchButton.setOnClickListener(this);
		clearImage.setOnClickListener(this);
	}

	/**
	 * EditText文本监听器
	 */
	private TextWatcher watcher = new TextWatcher()
	{
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count)
		{
			if (!"".equals(inputEdit.getText().toString().trim()))
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

	private OnEditorActionListener editorAction = new OnEditorActionListener()
	{
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
		{
			if (actionId == EditorInfo.IME_ACTION_SEARCH)
			{
				startSearch();
				return true;
			}
			return false;
		}
	};

	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		switch (v.getId())
		{
		case R.id.common_title_back_button:
			finish();
			break;
		case R.id.search_btn:
			startSearch();
			break;
		case R.id.clear_keyword:
			inputEdit.setText("");
			break;
		}
	}

	private void startSearch()
	{
		if ("".equals(inputEdit.getText().toString().trim()))
		{
			return;
		}
		SysManager.getInstance().dismissInputKey(this);
		progressBarLayout.setVisibility(View.VISIBLE);
		adapter = new CategorySearchListAdapter(RFQCategorySearchActivity.this, new ArrayList<SearchCategoryContent>());
		searchResultList.setAdapter(adapter);
		RequestCenter.getCategoryListByKeyword(categoryDataListener, inputEdit.getText().toString(), true);
	}

	private DisposeDataListener categoryDataListener = new DisposeDataListener()
	{

		@Override
		public void onSuccess(Object result)
		{
			progressBarLayout.setVisibility(View.GONE);
			SearchCategory category = (SearchCategory) result;
			if (category.content == null || (category.content != null && category.content.size() == 0))
			{
				ToastUtil.toast(RFQCategorySearchActivity.this, R.string.mic_category_search_empty);
			}
			else
			{
				adapter = new CategorySearchListAdapter(RFQCategorySearchActivity.this, category.content);
				searchResultList.setAdapter(adapter);
			}
		}

		@Override
		public void onFailure(Object failedReason)
		{
			progressBarLayout.setVisibility(View.GONE);
			ToastUtil.toast(RFQCategorySearchActivity.this, failedReason);
		}
	};

	private OnItemClickListener itemClick = new OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
		{
			SearchCategoryContent content = (SearchCategoryContent) arg0.getAdapter().getItem(arg2);
			Constants.selectedCategories = new Category();
			Constants.selectedCategories.catNameEn = content.getAllCatNames();
			Constants.selectedCategories.catCode = content.catCode;
			setResult(RESULT_OK);
			finish();
		}
	};

}

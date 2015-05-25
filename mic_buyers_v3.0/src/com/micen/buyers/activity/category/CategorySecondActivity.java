package com.micen.buyers.activity.category;

import java.io.File;
import java.util.ArrayList;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.focustech.common.mob.DisposeDataListener;
import com.focustech.common.util.ToastUtil;
import com.micen.buyers.activity.BaseActivity;
import com.micen.buyers.activity.R;
import com.micen.buyers.activity.rfq.category.RFQCategorySearchActivity_;
import com.micen.buyers.activity.searchresult.ProductSearchListActivity_;
import com.micen.buyers.adapter.category.CategoryAdapter;
import com.micen.buyers.constant.Constants;
import com.micen.buyers.constant.ProductSearchType;
import com.micen.buyers.db.SharedPreferenceManager;
import com.micen.buyers.http.RequestCenter;
import com.micen.buyers.manager.FileManager;
import com.micen.buyers.manager.SysManager;
import com.micen.buyers.module.category.Categories;
import com.micen.buyers.module.category.Category;
import com.micen.buyers.util.Util;
import com.micen.buyers.view.SearchListProgressBar;
import com.micen.buyers.widget.pinnerexpandview.PinnedHeaderExpandableListView;
import com.micen.buyers.widget.pinnerexpandview.PinnedHeaderExpandableListView.OnHeaderUpdateListener;

/**********************************************************
 * @文件名称：SecondCategoryActivity.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年3月12日 上午11:11:51
 * @文件描述：二级，三级目录页面
 * @修改历史：2015年3月12日创建初始版本
 **********************************************************/
@EActivity(R.layout.activity_category_second)
public class CategorySecondActivity extends BaseActivity implements OnClickListener, DisposeDataListener,
		OnHeaderUpdateListener, OnChildClickListener, OnGroupClickListener
{
	/**
	 * UI
	 */
	@ViewById(R.id.second_expandablelist)
	protected PinnedHeaderExpandableListView secondCategoryListView;
	@ViewById(R.id.sticky_content)
	protected LinearLayout stickyLayout;
	@ViewById(R.id.progressbar_layout)
	protected SearchListProgressBar progressLayout;
	@ViewById(R.id.search_title_layout)
	protected RelativeLayout searchLayout;

	protected CategoryAdapter categoryAapter;

	/**
	 * Data
	 */
	@Extra("Category")
	protected Category fatherCategory;
	// 二级目录列表数据
	protected ArrayList<Category> secondCategoryList;

	// 缓存目录数据的文件
	private String fileName;
	private File file;
	private int groupPosition;
	private String saveTimer;
	@Extra("isSearchCategory")
	protected boolean isSearchCategory = false;
	private boolean isFront;
	private FileManager fileManager;
	/**
	 * 目录选中后的名称
	 */
	private String selectedCategoryName;

	@AfterViews
	protected void initViewData()
	{
		titleLeftButton.setImageResource(R.drawable.ic_title_back);
		titleText.setText(fatherCategory.catNameEn);
		titleRightButton1.setVisibility(View.GONE);
		titleRightButton2.setVisibility(View.GONE);
		titleRightButton3.setVisibility(View.GONE);
		titleLeftButton.setOnClickListener(this);

		searchLayout.setVisibility(isSearchCategory ? View.VISIBLE : View.GONE);
		searchLayout.setOnClickListener(this);

		fileManager = FileManager.getInstance();
		// 初始化二级数据（从网络或文件）
		initCategoryData(fatherCategory.catCode, fatherCategory.linkCatCode, Constants.SECOND_CATEGORY);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.common_title_back_button:
			finish();
			break;
		case R.id.search_title_layout:
			startActivityForResult(new Intent(this, RFQCategorySearchActivity_.class), 0);
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == 0)
		{
			if (resultCode == RESULT_OK)
			{
				setResult(RESULT_OK);
				finish();
			}
		}
	}

	/**
	 *  判断文件是否存在,不存在则发请求，存在则读文件
	 */
	private void initCategoryData(String catCode, String linkCatcode, String categoryStatus)
	{
		saveTimer = catCode;
		fileName = fileManager.getCachePath() + catCode;
		file = new File(fileName);
		if (file == null || !file.exists())
		{
			isFront = true;
			requestCategoryData(!"0".equals(linkCatcode) ? linkCatcode : catCode, categoryStatus, isFront,
					Constants.INITIAL_TIME);
		}
		else
		{
			// 从文件读取数据,启动线程去发请求
			ArrayList<Category> categories = fileManager.readFromFile(fileName);
			if (categories != null)
			{
				isFront = false;
				if (categoryAapter == null)
				{
					createCategoryAdapter(categories);
				}
				else
				{ // 只更新对应二级目录的子目录
					categoryAapter.updateChildData(groupPosition, categories);
				}
			}
			else
			{
				isFront = true;
			}

			// 启动线程去请求，写文件，否则会一卡一卡, 防止在UI线程中写文件
			requestCategoryData(!"0".equals(linkCatcode) ? linkCatcode : catCode, categoryStatus, isFront,
					SharedPreferenceManager.getInstance().getString(saveTimer, Constants.INITIAL_TIME));
		}
	}

	/**
	 * 从网络或者文件获取当前目录数据,分为前台和后台请求
	 */
	protected void requestCategoryData(String catCode, String catLevel, boolean isShowProgress, String repTime)
	{
		if (catLevel.equals(Constants.SECOND_CATEGORY))
		{
			if (isShowProgress)
			{
				progressLayout.setVisibility(View.VISIBLE);
			}
		}
		RequestCenter.getCategoryList(this, catCode, catLevel, repTime);
	}

	@Override
	public void onFailure(Object failedReason)
	{
		ToastUtil.toast(this, failedReason);
	}

	/**
	 * 目录数据请求成功
	 */
	@Override
	public void onSuccess(Object obj)
	{
		Categories categories = (Categories) obj;
		// 只有是前台才显示
		if (isFront && categories.content != null && categories.content.size() > 0)
		{
			progressLayout.setVisibility(View.GONE);
			if (categoryAapter == null)
			{
				createCategoryAdapter(categories.content);
			}
			else
			{ // 只更新对应二级目录的子目录
				categoryAapter.updateChildData(groupPosition, categories.content);
			}
		}
		/**
		 * 保证SDCard容量至少有1M,否则不缓存文件
		 */
		if (Util.availableSDCard() > 1 && categories.content != null && categories.content.size() > 0)
		{
			if (fileManager.writeIntoFile(fileName, categories.content))
			{
				// 更新时间措,只有写入文件成功了，才更新时间措，否则一直是旧的
				SharedPreferenceManager.getInstance().putString(saveTimer, categories.repTime);
			}
		}
	}

	/**
	 * 为ListView绑定数据和事件
	 * @param categories
	 */
	private void createCategoryAdapter(ArrayList<Category> categories)
	{
		categoryAapter = new CategoryAdapter(this, categories);
		secondCategoryListView.setAdapter(categoryAapter);
		secondCategoryListView.setOnHeaderUpdateListener(this);
		secondCategoryListView.setOnChildClickListener(this);
		secondCategoryListView.setOnGroupClickListener(this, true);
		secondCategoryListView.setOnGroupExpandListener(groupExpand);
		secondCategoryListView.setOnGroupCollapseListener(groupCollapse);
	}

	@Override
	public View getPinnedHeader(int firstVisibleGroupPos)
	{
		View headerView = (ViewGroup) getLayoutInflater().inflate(R.layout.category_group_item_layout, null);
		headerView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		return headerView;
	}

	/**
	 * 进入页面默认HeaderView的点击事件
	 * @param groupPosition
	 */
	private void firstVisibilityViewClick(int groupPosition)
	{
		if (categoryAapter != null && categoryAapter.getChildrenCount(groupPosition) == 0)
		{
			Category category = (Category) categoryAapter.getGroup(groupPosition);
			if (category.childCount.equals("0") || category.childCount.equals(""))
			{
				startSearchResultActivity(category);
			}
		}
	}

	private OnGroupCollapseListener groupCollapse = new OnGroupCollapseListener()
	{
		@Override
		public void onGroupCollapse(int groupPosition)
		{
			firstVisibilityViewClick(groupPosition);
		}
	};

	private OnGroupExpandListener groupExpand = new OnGroupExpandListener()
	{
		@Override
		public void onGroupExpand(int groupPosition)
		{
			firstVisibilityViewClick(groupPosition);
		}
	};

	@Override
	public void updatePinnedHeader(View headerView, int firstVisibleGroupPos)
	{
		if (categoryAapter != null)
		{
			Category firstGroup = (Category) categoryAapter.getGroup(firstVisibleGroupPos);
			TextView textView = (TextView) headerView.findViewById(R.id.category_name_view);
			ImageView imageView = (ImageView) headerView.findViewById(R.id.indictor_view);
			textView.setText(firstGroup.catNameEn);
			if (firstGroup.childCount.equals("0"))
			{
				imageView.setVisibility(View.INVISIBLE);
			}
			else
			{
				imageView.setVisibility(View.VISIBLE);
				if (secondCategoryListView.isGroupExpanded(firstVisibleGroupPos))
				{
					imageView.setImageResource(R.drawable.icon_category_expand);
				}
				else
				{
					imageView.setImageResource(R.drawable.icon_category_unexpand);
				}
			}
		}
	}

	@Override
	public boolean onGroupClick(ExpandableListView arg0, View arg1, int groupPosition, long arg3)
	{
		// 二级目录数据
		Category category = (Category) categoryAapter.getGroup(groupPosition);
		if (category.childCount.equals("0") || category.childCount.equals(""))
		{
			if (isSearchCategory)
			{
				selectedCategoryName = fatherCategory.catNameEn + ">>" + category.catNameEn;
				Constants.selectedCategories = new Category();
				Constants.selectedCategories.catNameEn = selectedCategoryName;
				Constants.selectedCategories.catCode = !"0".equals(category.linkCatCode) ? category.linkCatCode
						: category.catCode;
				setResult(RESULT_OK);
				finish();
			}
			else
			{
				// 事件统计 27 浏览二级目录（目录） 点击事件
				SysManager.analysis(R.string.a_type_click, R.string.c27);
				startSearchResultActivity(category);
			}
			return true;
		}
		else
		{
			if (!secondCategoryListView.isGroupExpanded(groupPosition))
			{
				// 发请求更新第三级数据
				this.groupPosition = groupPosition;
				initCategoryData(category.catCode, category.linkCatCode, Constants.THIRD_CATEGORY);
				if (isSearchCategory)
				{
					selectedCategoryName = fatherCategory.catNameEn + ">>" + category.catNameEn;
				}
			}
			return false;
		}
	}

	@Override
	public boolean onChildClick(ExpandableListView arg0, View arg1, int arg2, int arg3, long arg4)
	{
		Category category = (Category) categoryAapter.getChild(arg2, arg3);
		if (isSearchCategory)
		{
			selectedCategoryName = selectedCategoryName + ">>" + category.catNameEn;
			Constants.selectedCategories = new Category();
			Constants.selectedCategories.catNameEn = selectedCategoryName;
			Constants.selectedCategories.catCode = !"0".equals(category.linkCatCode) ? category.linkCatCode
					: category.catCode;
			setResult(RESULT_OK);
			finish();
		}
		else
		{
			// 事件统计 28 浏览三级目录（目录） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c28);
			startSearchResultActivity(category);
		}
		return false;
	}

	/**
	 * 中转到结果列表页面
	 * @param category
	 */
	private void startSearchResultActivity(Category category)
	{
		Intent intent = new Intent(this, ProductSearchListActivity_.class);
		intent.putExtra("searchType", ProductSearchType.Category.getValue());
		intent.putExtra("category", !"0".equals(category.linkCatCode) ? category.linkCatCode : category.catCode);
		intent.putExtra("sourceSubject", category.catNameEn);
		intent.putExtra("isCategory", true);
		intent.putExtra("keyword", category.catNameEn);
		startActivity(intent);
	}

}

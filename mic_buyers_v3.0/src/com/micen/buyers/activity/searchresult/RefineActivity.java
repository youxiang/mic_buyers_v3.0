package com.micen.buyers.activity.searchresult;

import java.util.ArrayList;
import java.util.HashMap;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.micen.buyers.activity.BaseFragmentActivity;
import com.micen.buyers.activity.R;
import com.micen.buyers.manager.SysManager;
import com.micen.buyers.module.search.SearchLocation;
import com.micen.buyers.module.sift.SearchProperty;
import com.micen.buyers.module.sift.SiftCategory;
import com.micen.buyers.view.refine.AttributeFragment;
import com.micen.buyers.view.refine.BaseRefineFragment;
import com.micen.buyers.view.refine.BaseRefineFragment.SiftAllListener;
import com.micen.buyers.view.refine.CategoryFragment;
import com.micen.buyers.view.refine.LocationFragment;
import com.micen.buyers.view.refine.SupplierTypeFragment;

/**********************************************************
 * @文件名称：RefineActivity.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年3月20日 下午2:39:11
 * @文件描述：产品条件筛选页面
 * @修改历史：2015年3月20日创建初始版本
 **********************************************************/
@EActivity
public class RefineActivity extends BaseFragmentActivity implements SiftAllListener
{
	/**
	 * UI
	 */
	@ViewById(R.id.category_text)
	protected LinearLayout ll_category;
	@ViewById(R.id.attribute_text)
	protected LinearLayout ll_attribute;
	@ViewById(R.id.supplier_text)
	protected LinearLayout ll_supplier;
	@ViewById(R.id.location_text)
	protected LinearLayout ll_location;
	@ViewById(R.id.center_layout)
	protected LinearLayout centerLayout;

	@ViewById(R.id.iv_category)
	protected ImageView iv_category;
	@ViewById(R.id.iv_attribute)
	protected ImageView iv_attribute;
	@ViewById(R.id.iv_supplier)
	protected ImageView iv_supplier;
	@ViewById(R.id.iv_location)
	protected ImageView iv_location;

	@ViewById(R.id.tv_category)
	protected TextView tv_category;
	@ViewById(R.id.tv_attribute)
	protected TextView tv_attribute;
	@ViewById(R.id.tv_supplier)
	protected TextView tv_supplier;
	@ViewById(R.id.tv_location)
	protected TextView tv_location;

	@ViewById(R.id.reset_btn)
	protected Button resetBtn;
	@ViewById(R.id.done_btn)
	protected Button doneBtn;

	/**
	 * Fragment
	 */
	protected Fragment attributeFragment;
	protected Fragment categoryFragment;
	protected Fragment locationFragment;
	protected Fragment supplierTypeFragment;
	protected int currentFragment = 0;
	/**
	 * data
	 */
	@Extra("siftcategory")
	protected ArrayList<SiftCategory> siftCatogoriesList;
	@Extra("siftproperty")
	protected ArrayList<SearchProperty> siftPropertiesList;
	@Extra("siftlocations")
	protected ArrayList<SearchLocation> siftLocationsList;
	@Extra("iscompany")
	protected boolean isCompany;
	@Extra("isCategory")
	protected boolean isCategory;
	@Extra("siftmap")
	protected HashMap<String, String> siftMap;

	// 标识当前是否已经筛选过
	private boolean categoryFiltered;
	private boolean attributeFiltered;
	private boolean supplierFiltered;
	private boolean locationFiltered;

	@ColorRes(R.color.color_e62e2e)
	protected int color_e62e2e;

	@ColorRes(R.color.color_333333)
	protected int color_333333;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_refine_layout);
	}

	@AfterViews
	protected void initData()
	{
		// 初始化标题
		titleLeftButton.setImageResource(R.drawable.ic_title_back);
		titleText.setText(R.string.filter);
		titleRightButton1.setVisibility(View.GONE);
		titleRightButton2.setVisibility(View.GONE);
		titleRightButton3.setVisibility(View.GONE);
		// 初始化为空数据
		if (null == siftMap)
		{
			siftMap = new HashMap<String, String>();
			siftMap.put("needGM", "0");
			siftMap.put("needAudit", "0");
			siftMap.put("category", "");
			siftMap.put("location", "");
			siftMap.put("property", "");
		}

		ll_category.setOnClickListener(this);
		ll_attribute.setOnClickListener(this);
		ll_supplier.setOnClickListener(this);
		ll_location.setOnClickListener(this);
		resetBtn.setOnClickListener(this);
		doneBtn.setOnClickListener(this);
		titleLeftButton.setOnClickListener(this);

		if (!isCompany)
		{
			if (isCategory)
			{
				iv_attribute.setImageResource(R.drawable.icon_filtering);
				tv_attribute.setTextColor(color_e62e2e);

				ll_category.setVisibility(View.GONE);
				attributeFragment = new AttributeFragment(siftPropertiesList, this);
				supplierTypeFragment = new SupplierTypeFragment(this);
				locationFragment = new LocationFragment(siftLocationsList, this);
				replaceFragment(attributeFragment);
			}
			else
			{
				categoryFragment = new CategoryFragment(siftCatogoriesList, this);
				attributeFragment = new AttributeFragment(siftPropertiesList, this);
				supplierTypeFragment = new SupplierTypeFragment(this);
				locationFragment = new LocationFragment(siftLocationsList, this);

				// 需要校证下supplier的宽度
				Paint paint = tv_supplier.getPaint();
				float width = paint.measureText("Supplier");
				tv_supplier.setWidth((int) width);
				replaceFragment(categoryFragment);
			}
		}
		else
		{
			iv_supplier.setImageResource(R.drawable.icon_filtering);
			tv_supplier.setTextColor(color_e62e2e);
			
			ll_category.setVisibility(View.GONE);
			ll_attribute.setVisibility(View.GONE);
			supplierTypeFragment = new SupplierTypeFragment(this);
			locationFragment = new LocationFragment(siftLocationsList, this);
			replaceFragment(supplierTypeFragment);
		}

	}

	private void replaceFragment(Fragment targetFragment)
	{
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.center_layout, targetFragment);
		transaction.commit();
	}

	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		switch (v.getId())
		{
		case R.id.common_title_back_button:
			// 事件统计 81 取消筛选 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c81);
			finish();
			break;
		case R.id.category_text:
			iv_category.setImageResource(R.drawable.icon_filtering);
			tv_category.setTextColor(color_e62e2e);

			tv_attribute.setTextColor(color_333333);
			tv_supplier.setTextColor(color_333333);
			tv_location.setTextColor(color_333333);
			if (attributeFiltered)
			{
				iv_attribute.setImageResource(R.drawable.icon_filtered);
			}
			else
			{
				iv_attribute.setImageResource(R.drawable.icon_filter);
			}

			if (supplierFiltered)
			{
				iv_supplier.setImageResource(R.drawable.icon_filtered);
			}
			else
			{
				iv_supplier.setImageResource(R.drawable.icon_filter);
			}

			if (locationFiltered)
			{
				iv_location.setImageResource(R.drawable.icon_filtered);
			}
			else
			{
				iv_location.setImageResource(R.drawable.icon_filter);
			}

			currentFragment = 0;
			replaceFragment(categoryFragment);
			break;
		case R.id.attribute_text:

			// 事件统计 82 查看属性筛选 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c82);

			iv_attribute.setImageResource(R.drawable.icon_filtering);
			tv_attribute.setTextColor(color_e62e2e);

			tv_category.setTextColor(color_333333);
			tv_supplier.setTextColor(color_333333);
			tv_location.setTextColor(color_333333);
			if (categoryFiltered)
			{
				iv_category.setImageResource(R.drawable.icon_filtered);
			}
			else
			{
				iv_category.setImageResource(R.drawable.icon_filter);
			}

			if (supplierFiltered)
			{
				iv_supplier.setImageResource(R.drawable.icon_filtered);
			}
			else
			{
				iv_supplier.setImageResource(R.drawable.icon_filter);
			}

			if (locationFiltered)
			{
				iv_location.setImageResource(R.drawable.icon_filtered);
			}
			else
			{
				iv_location.setImageResource(R.drawable.icon_filter);
			}

			currentFragment = 1;
			replaceFragment(attributeFragment);
			break;
		case R.id.supplier_text:
			// 事件统计 83 查看供应商类型筛选 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c83);

			iv_supplier.setImageResource(R.drawable.icon_filtering);
			tv_supplier.setTextColor(color_e62e2e);

			tv_category.setTextColor(color_333333);
			tv_attribute.setTextColor(color_333333);
			tv_location.setTextColor(color_333333);
			if (categoryFiltered)
			{
				iv_category.setImageResource(R.drawable.icon_filtered);
			}
			else
			{
				iv_category.setImageResource(R.drawable.icon_filter);
			}

			if (attributeFiltered)
			{
				iv_attribute.setImageResource(R.drawable.icon_filtered);
			}
			else
			{
				iv_attribute.setImageResource(R.drawable.icon_filter);
			}

			if (locationFiltered)
			{
				iv_location.setImageResource(R.drawable.icon_filtered);
			}
			else
			{
				iv_location.setImageResource(R.drawable.icon_filter);
			}
			currentFragment = 2;
			replaceFragment(supplierTypeFragment);
			break;
		case R.id.location_text:

			// 事件统计 84 查看公司地点筛选 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c84);

			iv_location.setImageResource(R.drawable.icon_filtering);
			tv_location.setTextColor(color_e62e2e);

			tv_category.setTextColor(color_333333);
			tv_attribute.setTextColor(color_333333);
			tv_supplier.setTextColor(color_333333);
			if (categoryFiltered)
			{
				iv_category.setImageResource(R.drawable.icon_filtered);
			}
			else
			{
				iv_category.setImageResource(R.drawable.icon_filter);
			}

			if (attributeFiltered)
			{
				iv_attribute.setImageResource(R.drawable.icon_filtered);
			}
			else
			{
				iv_attribute.setImageResource(R.drawable.icon_filter);
			}

			if (supplierFiltered)
			{
				iv_supplier.setImageResource(R.drawable.icon_filtered);
			}
			else
			{
				iv_supplier.setImageResource(R.drawable.icon_filter);
			}

			currentFragment = 3;
			replaceFragment(locationFragment);
			break;
		case R.id.done_btn:
			// 事件统计86 提交筛选 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c86);
			Intent intent = getIntent().putExtra("siftmap", siftMap);
			intent.putExtra("siftproperty", siftPropertiesList);
			setResult(RESULT_OK, intent);
			finish();
		case R.id.reset_btn:

			// 事件统计 85 重置筛选条件 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c85);

			categoryFiltered = false;
			attributeFiltered = false;
			supplierFiltered = false;
			locationFiltered = false;
			siftMap.clear();
			siftMap.put("needGM", "0");
			siftMap.put("needAudit", "0");
			siftMap.put("category", "");
			siftMap.put("location", "");
			siftMap.put("property", "");
			iv_attribute.setImageResource(R.drawable.icon_filter);
			iv_supplier.setImageResource(R.drawable.icon_filter);
			iv_category.setImageResource(R.drawable.icon_filter);
			iv_location.setImageResource(R.drawable.icon_filter);

			if (currentFragment == 0)
			{
				iv_category.setImageResource(R.drawable.icon_filtering);
			}

			if (currentFragment == 1)
			{
				iv_attribute.setImageResource(R.drawable.icon_filtering);
			}

			if (currentFragment == 2)
			{
				iv_supplier.setImageResource(R.drawable.icon_filtering);
			}

			if (currentFragment == 3)
			{
				iv_location.setImageResource(R.drawable.icon_filtering);
			}

			if (categoryFragment != null)
			{
				((BaseRefineFragment) categoryFragment).resetData();
				((BaseRefineFragment) attributeFragment).resetData();
			}
			((BaseRefineFragment) supplierTypeFragment).resetData();
			((BaseRefineFragment) locationFragment).resetData();
			break;
		}
	}

	@Override
	public void sift(int type, String siftString)
	{
		switch (type)
		{
		case 0:
			siftMap.put("category", siftString);
			categoryFiltered = true;
			break;
		case 1:
			siftMap.put("property", siftString);
			attributeFiltered = true;
			break;
		case 2:
			siftMap.put("needGM", String.valueOf(siftString.charAt(0)));
			siftMap.put("needAudit", String.valueOf(siftString.charAt(1)));
			supplierFiltered = true;
			break;
		case 3:
			siftMap.put("location", siftString);
			locationFiltered = true;
			break;
		}
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();

		// 事件统计$10020 筛选面板 页面事件
		SysManager.analysis(R.string.a_type_page, R.string.p10020);
	}

	@Override
	public void setAttributeData(ArrayList<SearchProperty> attribute)
	{
		// TODO Auto-generated method stub
		if (attributeFragment != null)
		{
			// 设置属性
			siftPropertiesList = attribute;
			siftMap.put("property", "");
			((AttributeFragment) attributeFragment).resetData();
			((AttributeFragment) attributeFragment).setAttributeData(attribute);

		}

	}

	@Override
	public String getValue(String key)
	{
		// TODO Auto-generated method stub
		return siftMap.get(key);
	}
}

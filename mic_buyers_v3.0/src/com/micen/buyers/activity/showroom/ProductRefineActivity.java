package com.micen.buyers.activity.showroom;

import java.util.ArrayList;
import java.util.HashMap;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.focustech.common.util.ToastUtil;
import com.micen.buyers.activity.BaseFragmentActivity;
import com.micen.buyers.activity.R;
import com.micen.buyers.adapter.ProductRefineExpanableAdapter;
import com.micen.buyers.module.showroom.ProductGroup;
import com.micen.buyers.module.sift.SearchProperty;
import com.micen.buyers.view.refine.BaseRefineFragment.SiftAllListener;
import com.micen.buyers.widget.PasswordDialog;

@EActivity(R.layout.activity_product_refine)
public class ProductRefineActivity extends BaseFragmentActivity implements SiftAllListener, OnChildClickListener,
		OnGroupClickListener, OnGroupExpandListener
{
	protected HashMap<String, String> siftMap;

	@Extra("companyId")
	protected String companyId;

	@Extra("memberType")
	protected String memberType;

	@Extra("productGroup")
	protected ArrayList<ProductGroup> productGroup;

	@Extra("selectedGroup")
	protected ProductGroup selectedGroup;

	@ViewById(R.id.header)
	protected LinearLayout header;

	@ViewById(R.id.product_group_list)
	protected ExpandableListView listView;

	@ViewById(R.id.empty_view)
	protected ImageView emptyView;

	private Dialog dialog;

	private ProductRefineExpanableAdapter adapter;

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.common_title_back_button:
			finish();
			break;
		case R.id.common_title_right_button2:
			startProductSearchActivity();
			break;
		case R.id.common_title_right_button3:
			finish();
			break;
		}
	}

	private void startProductSearchActivity()
	{
		Intent i = new Intent(this, ProductSearchActivity_.class);
		i.putExtra("companyId", companyId);
		startActivity(i);
	}

	@AfterViews
	protected void init()
	{
		titleLeftButton.setImageResource(R.drawable.ic_up);
		titleLeftButton.setOnClickListener(this);
		titleText.setText(R.string.product_list_title);
		titleRightButton2.setImageResource(R.drawable.icon_search_normal);
		// titleRightButton2.setImageDrawable(Utils.setImageButtonState(R.drawable.icon_search_normal,
		// R.drawable.icon_search_down, this));
		titleRightButton2.setOnClickListener(this);
		titleRightButton3.setImageResource(R.drawable.ic_menu_normal);
		// titleRightButton3.setImageDrawable(Utils.setImageButtonState(R.drawable.ic_menu_selected,
		// R.drawable.ic_menu_normal, this));
		titleRightButton3.setOnClickListener(this);

		listView.setOnChildClickListener(this);
		listView.setOnGroupClickListener(this);
		listView.setOnGroupExpandListener(this);
		siftMap = new HashMap<String, String>();
		siftMap.put("needGM", "0");
		siftMap.put("needAudit", "0");
		siftMap.put("category", "");
		siftMap.put("location", "");
		siftMap.put("property", "");

		if (TextUtils.isEmpty(memberType) || "4".equals(memberType))
		{
			listView.setVisibility(View.GONE);
			emptyView.setImageResource(R.drawable.ic_product_group_free_empty);
			emptyView.setVisibility(View.VISIBLE);
		}
		else if (productGroup != null && !productGroup.isEmpty())
		{
			adapter = new ProductRefineExpanableAdapter(ProductRefineActivity.this, productGroup, selectedGroup);
			listView.setAdapter(adapter);
			expandSelectedGroup();
		}
		else
		{
			listView.setVisibility(View.GONE);
			emptyView.setImageResource(R.drawable.ic_product_group_empty);
			emptyView.setVisibility(View.VISIBLE);
		}
	}

	private void expandSelectedGroup()
	{
		if (selectedGroup != null && selectedGroup.parentGroupId != null)
		{
			for (int i = 0; i < adapter.getGroupCount(); i++)
			{
				if (selectedGroup.parentGroupId.equals(((ProductGroup) adapter.getGroup(i)).groupId))
				{
					listView.expandGroup(i);
				}
			}
		}

	}

	@Override
	public void sift(int type, String siftString)
	{
		siftMap.put("category", siftString);
	}

	@Override
	public void onGroupExpand(int groupPosition)
	{
		for (int i = 0; i < adapter.getGroupCount(); i++)
		{
			if (groupPosition != i)
			{
				listView.collapseGroup(i);
			}
		}
	}

	@Override
	public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id)
	{
		ProductGroup group = (ProductGroup) parent.getExpandableListAdapter().getGroup(groupPosition);

		if (!group.isHaveChild())
		{

			if (!TextUtils.isEmpty(group.groupPassword))
			{
				showPasswordDialog(groupPosition, group);
			}
			else
			{
				selectedGroup = group;

				siftMap.clear();
				siftMap.put("groupId", group.groupId);
				siftMap.put("groupLevel", group.groupLevel);
				adapter.onClickGroup(groupPosition, listView.isGroupExpanded(groupPosition));
				adapter.onSelectGroup(group);
				back();
			}
		}
		return false;
	}

	private void showPasswordDialog(final int groupPosition, final ProductGroup group)
	{
		dialog = new PasswordDialog(this, new PasswordDialog.PasswordDialogListener()
		{

			@Override
			public void onConfirm(String password)
			{
				if (password.equals(group.groupPassword))
				{
					adapter.onClickGroup(groupPosition, listView.isGroupExpanded(groupPosition));
					adapter.onSelectGroup(group);
					selectedGroup = group;
					adapter.onSelectGroup(group);
					siftMap.clear();
					siftMap.put("groupId", group.groupId);
					siftMap.put("groupLevel", group.groupLevel);
					back();
				}
				else
				{
					ToastUtil.toast(ProductRefineActivity.this, R.string.product_group_encrypt_error);
				}
			}

			@Override
			public void onCancel()
			{
				// TODO Auto-generated method stub

			}
		});
		dialog.show();
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
	{
		ProductGroup group = (ProductGroup) parent.getExpandableListAdapter().getChild(groupPosition, childPosition);
		if (group != null)
		{
			if (!TextUtils.isEmpty(group.groupPassword))
			{
				showPasswordDialog(groupPosition, group);
			}
			else
			{
				selectedGroup = group;
				adapter.onSelectGroup(group);
				siftMap.clear();
				siftMap.put("groupId", group.groupId);
				siftMap.put("groupLevel", group.groupLevel);
				back();
			}

		}
		return false;
	}

	private void back()
	{
		Intent data = new Intent();
		data.putExtra("siftMap", siftMap);
		data.putExtra("selectedGroup", selectedGroup);
		setResult(RESULT_OK, data);
		finish();
	}

	@Override
	public void setAttributeData(ArrayList<SearchProperty> attribute)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public String getValue(String key)
	{
		// TODO Auto-generated method stub
		return null;
	}

}

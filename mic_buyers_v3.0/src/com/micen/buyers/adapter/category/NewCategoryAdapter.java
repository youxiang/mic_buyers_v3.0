package com.micen.buyers.adapter.category;

import java.util.ArrayList;

import android.content.Context;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.micen.buyers.activity.R;
import com.micen.buyers.module.category.Category;
import com.micen.buyers.widget.PinnedHeaderExpandableListView;
import com.micen.buyers.widget.PinnedHeaderExpandableListView.HeaderAdapter;

public class NewCategoryAdapter extends BaseExpandableListAdapter implements HeaderAdapter
{
	private ArrayList<Category> groupList;
	private Context context;
	private PinnedHeaderExpandableListView listView;
	private LayoutInflater inflater;

	public NewCategoryAdapter(ArrayList<Category> groupList, Context context, PinnedHeaderExpandableListView listView)
	{
		this.groupList = groupList;
		this.context = context;
		this.listView = listView;
		inflater = LayoutInflater.from(this.context);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition)
	{
		return groupList.get(groupPosition).childCategories.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition)
	{
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent)
	{
		View view = null;
		if (convertView != null)
		{
			view = convertView;
		}
		else
		{
			view = inflater.inflate(R.layout.category_child_item_layout, null);
		}
		TextView text = (TextView) view.findViewById(R.id.category_name_view);
		text.setText(groupList.get(groupPosition).childCategories.get(childPosition).catNameEn);
		return view;
	}

	@Override
	public int getChildrenCount(int groupPosition)
	{
		return groupList.get(groupPosition).childCategories.size();
	}

	@Override
	public Object getGroup(int groupPosition)
	{
		return groupList.get(groupPosition);
	}

	@Override
	public int getGroupCount()
	{
		return groupList.size();
	}

	@Override
	public long getGroupId(int groupPosition)
	{
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
	{
		View view = null;
		if (convertView != null)
		{
			view = convertView;
		}
		else
		{
			view = inflater.inflate(R.layout.category_group_item_layout, null);
		}

		TextView text = (TextView) view.findViewById(R.id.category_name_view);
		text.setText(groupList.get(groupPosition).catNameEn);
		return view;
	}

	@Override
	public boolean hasStableIds()
	{
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition)
	{
		return true;
	}

	@Override
	public int getHeaderState(int groupPosition, int childPosition)
	{
		final int childCount = getChildrenCount(groupPosition);
		if (childPosition == childCount - 1)
		{
			return PINNED_HEADER_PUSHED_UP;
		}
		else if (childPosition == -1 && !listView.isGroupExpanded(groupPosition))
		{
			return PINNED_HEADER_GONE;
		}
		else
		{
			return PINNED_HEADER_VISIBLE;
		}
	}

	@Override
	public void configureHeader(View header, int groupPosition, int childPosition, int alpha)
	{
		String groupData = this.groupList.get(groupPosition).catNameEn;
		((TextView) header.findViewById(R.id.category_name_view)).setText(groupData);

	}

	private SparseIntArray groupStatusMap = new SparseIntArray();

	@Override
	public void setGroupClickStatus(int groupPosition, int status)
	{
		groupStatusMap.put(groupPosition, status);

	}

	@Override
	public int getGroupClickStatus(int groupPosition)
	{
		if (groupStatusMap.keyAt(groupPosition) >= 0)
		{
			return groupStatusMap.get(groupPosition);
		}
		else
		{
			return 0;
		}
	}

}

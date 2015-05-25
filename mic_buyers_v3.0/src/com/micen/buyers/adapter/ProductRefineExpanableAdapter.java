package com.micen.buyers.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.micen.buyers.activity.R;
import com.micen.buyers.module.showroom.ProductGroup;

public class ProductRefineExpanableAdapter extends BaseExpandableListAdapter
{
	private Context context;
	private ArrayList<ProductGroup> dataList;

	private ProductGroup childGroup;
	private ProductGroup group;

	private ProductGroup selectedGroup;
	private int selectedGroupPosition = -1;
	private boolean isGroupExpanded = false;

	static class ViewHolder
	{
		ImageView imageView;
		TextView groupName;
	}

	private ViewHolder childHolder;
	private ViewHolder groupHolder;

	public ProductRefineExpanableAdapter(Context context, ArrayList<ProductGroup> dataList, ProductGroup selectedGroup)
	{
		this.context = context;
		this.dataList = dataList;
		this.selectedGroup = selectedGroup;
		// FIXME
		ProductGroup all = new ProductGroup();
		all.groupId = "";
		all.groupName = "All";
		all.childGroup = new ArrayList<ProductGroup>();
		this.dataList.add(0, all);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition)
	{
		return dataList.get(groupPosition).childGroup.get(childPosition);
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
		if (convertView == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.product_group_item, null);
			childHolder = new ViewHolder();
			childHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_product_group_indicator);
			childHolder.groupName = (TextView) convertView.findViewById(R.id.tv_product_group_name);
			convertView.setTag(childHolder);
		}
		else
		{
			childHolder = (ViewHolder) convertView.getTag();
		}
		convertView.setBackgroundResource(R.drawable.bg_product_refine_list_selector1);
		childGroup = (ProductGroup) getChild(groupPosition, childPosition);
		childHolder.groupName.setText(childGroup.groupName);

		if (selectedGroup != null && selectedGroup.groupId.equals(childGroup.groupId))
		{
			childHolder.imageView.setImageResource(R.drawable.icon_selected);
			childHolder.imageView.setVisibility(View.VISIBLE);
		}
		else
		{
			childHolder.imageView.setVisibility(View.GONE);
		}
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition)
	{
		if (dataList.get(groupPosition).childGroup == null)
		{
			return 0;
		}
		return dataList.get(groupPosition).childGroup.size();
	}

	@Override
	public Object getGroup(int groupPosition)
	{
		return dataList.get(groupPosition);
	}

	@Override
	public int getGroupCount()
	{
		return dataList.size();
	}

	@Override
	public long getGroupId(int groupPosition)
	{
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
	{
		group = (ProductGroup) getGroup(groupPosition);
		if (convertView == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.product_group_item, null);
			groupHolder = new ViewHolder();
			groupHolder.groupName = (TextView) convertView.findViewById(R.id.tv_product_group_name);
			groupHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_product_group_indicator);
			convertView.setTag(groupHolder);
		}
		else
		{
			groupHolder = (ViewHolder) convertView.getTag();
		}
		convertView.setBackgroundResource(R.drawable.bg_product_refine_list_selector2);
		if (!group.isHaveChild())
		{
			groupHolder.imageView.setBackgroundResource(R.drawable.icon_selected);

			if (selectedGroup != null && selectedGroup.groupId.equals(group.groupId))
			{
				groupHolder.imageView.setVisibility(View.VISIBLE);
			}
			else
			{
				groupHolder.imageView.setVisibility(View.GONE);
			}
		}
		else
		{
			groupHolder.imageView.setVisibility(View.VISIBLE);
			if (selectedGroupPosition == groupPosition)
			{
				groupHolder.imageView.setBackgroundResource(isGroupExpanded ? R.drawable.product_group_indicator_down
						: R.drawable.product_group_indicator_up);
			}
			else
			{
				groupHolder.imageView.setBackgroundResource(R.drawable.product_group_indicator_down);
			}
		}
		groupHolder.groupName.setText(group.groupName);
		return convertView;
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

	public void onClickGroup(int groupPosition, boolean isGroupExpanded)
	{
		this.selectedGroupPosition = groupPosition;
		this.isGroupExpanded = isGroupExpanded;
		notifyDataSetChanged();
	}

	public void onSelectGroup(ProductGroup selectedGroup)
	{
		this.selectedGroup = selectedGroup;
		notifyDataSetChanged();
	}

}

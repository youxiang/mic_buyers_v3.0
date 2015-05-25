package com.micen.buyers.adapter.refine;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.micen.buyers.activity.R;
import com.micen.buyers.module.sift.Options;
import com.micen.buyers.module.sift.RecordGroup;
import com.micen.buyers.module.sift.SearchProperty;
import com.micen.buyers.module.sift.SiftSupplier;
import com.micen.buyers.module.sift.SiftSupplierGroup;
import com.micen.buyers.view.refine.BaseRefineFragment.SiftAllListener;

/**********************************************************
 * @文件名称：SupplierTypeExpandListAdapter.java
 * @文件作者：xuliucheng
 * @创建时间：2015年4月30日 上午11:00:37
 * @文件描述：
 * @修改历史：2015年4月30日创建初始版本
 **********************************************************/
public class SupplierTypeExpandListAdapter extends BaseExpandableListAdapter
{
	private Context context;
	private ArrayList<SiftSupplierGroup> dataList;
	private ViewHolder childHolder;
	private ViewHolder groupHolder;
	private SiftSupplier child;
	private SiftSupplierGroup group;
	private SiftAllListener allListener;
	public HashMap<Integer, RecordGroup> recordOptions;

	public SupplierTypeExpandListAdapter(Context context, ArrayList<SiftSupplierGroup> dataList,
			SiftAllListener propertyListener)
	{
		this.context = context;
		this.dataList = dataList;
		this.allListener = propertyListener;
		this.recordOptions = new HashMap<Integer, RecordGroup>();
	}

	@Override
	public Object getChild(int groupPosition, int childPosition)
	{
		return dataList.get(groupPosition).options.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition)
	{
		return childPosition;
	}

	@Override
	public int getChildrenCount(int groupPosition)
	{
		return dataList.get(groupPosition).options.size();
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.property_supplier_child_item, null);
			childHolder = new ViewHolder();
			childHolder.groupName = (TextView) convertView.findViewById(R.id.property_name);
			childHolder.checkBox = (CheckBox) convertView.findViewById(R.id.property_box);
			childHolder.propertyLayout = (RelativeLayout) convertView.findViewById(R.id.search_sift_property);
			childHolder.supplierIcon = (ImageView) convertView.findViewById(R.id.property_icon);
			convertView.setTag(childHolder);
		}
		else
		{
			childHolder = (ViewHolder) convertView.getTag();
		}
		childHolder.checkBox.setChecked(false);
		child = (SiftSupplier) getChild(groupPosition, childPosition);
		childHolder.supplierIcon.setImageResource(child.imageResID);
		childHolder.groupName.setText(child.name);
		childHolder.propertyLayout.setId(getID(groupPosition, childPosition).realId);
		childHolder.propertyLayout.setTag(R.id.property_one, child);
		childHolder.propertyLayout.setTag(R.id.property_two, getID(groupPosition, childPosition));
		if (recordOptions.size() != 0)
		{
			RecordGroup temp = recordOptions.get(groupPosition);
			if (temp != null)
			{
				if (temp.childId == childPosition)
				{
					childHolder.checkBox.setChecked(true);
				}
			}
		}
		return convertView;
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
		group = (SiftSupplierGroup) getGroup(groupPosition);
		if (convertView == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.property_group_item, null);
			groupHolder = new ViewHolder();
			groupHolder.groupName = (TextView) convertView.findViewById(R.id.property_name);
			groupHolder.checkBox = (CheckBox) convertView.findViewById(R.id.property_box);
			convertView.setTag(groupHolder);
		}
		else
		{
			groupHolder = (ViewHolder) convertView.getTag();
		}

		groupHolder.checkBox.setChecked(isExpanded);
		// 如果没有子类，则不显示图标
		if (getChildrenCount(groupPosition) < 1)
		{
			groupHolder.checkBox.setVisibility(View.INVISIBLE);
		}
		groupHolder.groupName.setText(group.name);
		return convertView;
	}

	class ViewHolder
	{
		TextView groupName;
		CheckBox checkBox;
		ImageView supplierIcon;
		RelativeLayout propertyLayout;
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

	public RecordGroup getID(int groupPosition, int childPosition)
	{
		String id = String.valueOf(groupPosition) + String.valueOf(childPosition);
		return new RecordGroup(id, groupPosition, childPosition);
	}

	public void changeStyle(RecordGroup record, SiftSupplier options)
	{
		if (recordOptions.containsKey(record.groupId))
		{
			RecordGroup tempRecord = recordOptions.get(record.groupId);
			if (tempRecord.childId != record.childId)
			{
				recordOptions.put(tempRecord.groupId, record);
			}
			else
			{
				recordOptions.remove(record.groupId);
			}
		}
		else
		{
			recordOptions.put(record.groupId, record);
		}
		notifyDataSetChanged();
	}

	public void resetData()
	{
		if (recordOptions != null)
		{
			recordOptions.clear();
		}
		this.notifyDataSetChanged();
	}
}
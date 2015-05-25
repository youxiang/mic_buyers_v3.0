package com.micen.buyers.adapter.refine;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.micen.buyers.activity.R;
import com.micen.buyers.module.sift.Options;
import com.micen.buyers.module.sift.RecordGroup;
import com.micen.buyers.module.sift.SearchProperty;

/**********************************************************
 * @文件名称：PropertySiftExpandListAdapter.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年3月23日 上午11:00:37
 * @文件描述：
 * @修改历史：2015年3月23日创建初始版本
 **********************************************************/
public class PropertySiftExpandListAdapter extends BaseExpandableListAdapter
{
	private Context context;
	private ArrayList<SearchProperty> dataList;
	private ViewHolder childHolder;
	private ViewHolder groupHolder;
	private Options child;
	private SearchProperty group;
	private GetPropertyListener propertyListener;
	public HashMap<Integer, RecordGroup> recordOptions;

	public PropertySiftExpandListAdapter(Context context, ArrayList<SearchProperty> dataList,
			GetPropertyListener propertyListener)
	{
		this.context = context;
		this.dataList = dataList;
		this.propertyListener = propertyListener;
		this.recordOptions = new HashMap<Integer, RecordGroup>();
	}
	
	public void setArrayList(ArrayList<SearchProperty> dataList)
	{
		this.dataList = dataList;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.property_child_item, null);
			childHolder = new ViewHolder();
			childHolder.groupName = (TextView) convertView.findViewById(R.id.property_name);
			childHolder.checkBox = (CheckBox) convertView.findViewById(R.id.property_box);
			childHolder.propertyLayout = (RelativeLayout) convertView.findViewById(R.id.search_sift_property);
			convertView.setTag(childHolder);
		}
		else
		{
			childHolder = (ViewHolder) convertView.getTag();
		}
		childHolder.checkBox.setChecked(false);
		child = (Options) getChild(groupPosition, childPosition);
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
		group = (SearchProperty) getGroup(groupPosition);
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

	public interface GetPropertyListener
	{
		public void getPropertyData(int groupId, Options property);
	}

	public void changeStyle(RecordGroup record, Options options)
	{
		if (recordOptions.containsKey(record.groupId))
		{
			RecordGroup tempRecord = recordOptions.get(record.groupId);
			if (tempRecord.childId != record.childId)
			{
				recordOptions.put(tempRecord.groupId, record);
				propertyListener.getPropertyData(tempRecord.groupId, options);
			}
			else
			{
				recordOptions.remove(record.groupId);
				propertyListener.getPropertyData(tempRecord.groupId, null);
			}
		}
		else
		{
			recordOptions.put(record.groupId, record);
			propertyListener.getPropertyData(record.groupId, options);
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
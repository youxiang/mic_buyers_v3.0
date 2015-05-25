package com.micen.buyers.view.refine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.micen.buyers.activity.R;
import com.micen.buyers.adapter.refine.PropertySiftExpandListAdapter;
import com.micen.buyers.adapter.refine.PropertySiftExpandListAdapter.GetPropertyListener;
import com.micen.buyers.module.sift.Options;
import com.micen.buyers.module.sift.RecordGroup;
import com.micen.buyers.module.sift.SearchProperty;

/**********************************************************
 * @文件名称：AttributeFragment.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年3月23日 上午10:24:26
 * @文件描述：
 * @修改历史：2015年3月23日创建初始版本
 **********************************************************/
public class AttributeFragment extends BaseRefineFragment implements GetPropertyListener
{
	protected ArrayList<SearchProperty> siftPropertiesList;
	protected ExpandableListView expandListView;
	protected PropertySiftExpandListAdapter propertyAdapter;
	private HashMap<Integer, Options> tempList;

	public AttributeFragment()
	{
	}

	public AttributeFragment(ArrayList<SearchProperty> siftPropertiesList, SiftAllListener listener)
	{
		this.siftPropertiesList = siftPropertiesList;
		this.listener = listener;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		tempList = new HashMap<Integer, Options>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		if (contentView == null)
		{
			contentView = inflater.inflate(R.layout.refine_attribute_fragment_layout, null);
			initView();
		}

		ViewGroup parent = (ViewGroup) contentView.getParent();
		if (parent != null)
		{
			parent.removeView(contentView);
		}
		return contentView;
	}

	/**
	 * 用于属性的变更
	 * @param data
	 */
	public void setAttributeData(ArrayList<SearchProperty> data)
	{
		this.siftPropertiesList = data;
		if (propertyAdapter != null)
		{
			propertyAdapter.setArrayList(data);
			propertyAdapter.notifyDataSetChanged();
		}
	}

	private void initView()
	{
		expandListView = (ExpandableListView) contentView.findViewById(R.id.attribute_group_list);
		propertyAdapter = new PropertySiftExpandListAdapter(mContext, siftPropertiesList, this);
		expandListView.setAdapter(propertyAdapter);
		expandListView.setOnChildClickListener(onChildListener);
		initChoiceData();
	}

	private void initChoiceData()
	{
		String str = listener.getValue("property");
		if (str != null && str.length() > 0)
		{
			String[] values = str.split(",");
			int groupid = -1;
			SearchProperty tempProperty;
			for (int i = 0; i < values.length; i++)
			{
				groupid = -1;
				for (int j = 0; j < siftPropertiesList.size(); j++)
				{
					tempProperty = siftPropertiesList.get(j);

					for (int k = 0; k < tempProperty.options.size(); k++)
					{

						if (tempProperty.options.get(k).key.equals(values[i]))
						{
							groupid = j;
							this.tempList.put(groupid, tempProperty.options.get(k));
							propertyAdapter.recordOptions.put(groupid, propertyAdapter.getID(groupid, k));
							expandListView.expandGroup(groupid);
							break;
						}
					}
					if (groupid > 0)
					{
						break;
					}
				}

			}

			// tempList
		}
	}

	/**
	 * 属性筛选事件响应
	 */
	private OnChildClickListener onChildListener = new OnChildClickListener()
	{
		@Override
		public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
		{
			Options option = (Options) propertyAdapter.getChild(groupPosition, childPosition);
			propertyAdapter.changeStyle(propertyAdapter.getID(groupPosition, childPosition), option);
			return true;
		}
	};

	@Override
	public void getPropertyData(int groupId, Options property)
	{
		if (property == null)
		{
			if (this.tempList.get(groupId) != null)
			{
				this.tempList.remove(groupId);
			}
		}
		else
		{
			this.tempList.put(groupId, property);
		}
		if (tempList.size() == 0)
		{
			listener.sift(1, "");
		}
		else
		{
			listener.sift(1, allOptions());
		}
	}

	@SuppressWarnings("rawtypes")
	private String allOptions()
	{
		String options = "";
		Iterator it = tempList.entrySet().iterator();
		while (it.hasNext())
		{
			Map.Entry entry = (Map.Entry) it.next();
			Options option = (Options) entry.getValue();
			options += option.key + ",";
		}
		return options.substring(0, options.length() - 1);
	}

	@Override
	public void resetData()
	{
		if (tempList != null)
		{
			tempList.clear();
		}
		if (propertyAdapter != null)
		{
			propertyAdapter.resetData();
		}
	}
	
}

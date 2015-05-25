package com.micen.buyers.adapter.refine;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.micen.buyers.activity.R;
import com.micen.buyers.module.search.SearchLocation;

/**********************************************************
 * @文件名称：RefineListAdapter.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年3月20日 下午5:59:22
 * @文件描述：筛选列表通用 Adpater
 * @修改历史：2015年3月20日创建初始版本
 **********************************************************/
public class RefineLocationAdapter extends BaseAdapter
{
	private ArrayList<SearchLocation> siftLocationsList;
	private LayoutInflater inflate;
	private ViewHolder holder;
	private SearchLocation tempLocation;

	public RefineLocationAdapter(Activity mContext, ArrayList<SearchLocation> siftLocationsList)
	{
		this.siftLocationsList = siftLocationsList;
		inflate = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount()
	{
		return siftLocationsList.size();
	}

	@Override
	public Object getItem(int position)
	{
		return siftLocationsList.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			holder = new ViewHolder();
			convertView = inflate.inflate(R.layout.refine_category_item, null);
			holder.locationCheckBox = (CheckBox) convertView.findViewById(R.id.refine_category_box);
			holder.loationNameTextView = (TextView) convertView.findViewById(R.id.refine_category_name);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		SearchLocation searchLocation = (SearchLocation) getItem(position);
		holder.loationNameTextView.setText(searchLocation.name);
		if (tempLocation != null)
		{
			if (tempLocation.key.equals(searchLocation.key))
			{
				//holder.locationCheckBox.setVisibility(View.VISIBLE);
				holder.locationCheckBox.setChecked(true);
			}
			else
			{
				//holder.locationCheckBox.setVisibility(View.GONE);
				holder.locationCheckBox.setChecked(false);
			}
		}
		else
		{
			//holder.locationCheckBox.setVisibility(View.GONE);
			holder.locationCheckBox.setChecked(false);
		}
		return convertView;
	}

	public SearchLocation choiceLocation(SearchLocation location)
	{
		if (this.tempLocation == null)
		{
			this.tempLocation = location;
		}
		else
		{
			if (this.tempLocation.key.equals(location.key))
			{
				this.tempLocation = null;
			}
			else
			{
				this.tempLocation = location;
			}
		}
		notifyDataSetChanged();
		return tempLocation;
	}
	
	public void resetData()
	{
		this.tempLocation = null;
		notifyDataSetChanged();
	}

	private static class ViewHolder
	{
		private TextView loationNameTextView;
		private CheckBox locationCheckBox;
	}
}

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
import com.micen.buyers.module.sift.SiftCategory;

/**********************************************************
 * @文件名称：RefineListAdapter.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年3月20日 下午5:59:22
 * @文件描述：筛选列表通用 Adpater
 * @修改历史：2015年3月20日创建初始版本
 **********************************************************/
public class RefineListAdapter extends BaseAdapter
{
	private ArrayList<SiftCategory> siftCatogoriesList;
	private LayoutInflater inflate;
	private ViewHolder holder;
	private SiftCategory tempCategory;

	public RefineListAdapter(Activity mContext, ArrayList<SiftCategory> siftCatogoriesList)
	{
		this.siftCatogoriesList = siftCatogoriesList;
		inflate = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount()
	{
		return siftCatogoriesList.size();
	}

	@Override
	public Object getItem(int position)
	{
		return siftCatogoriesList.get(position);
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
			holder.categoryCheckBox = (CheckBox) convertView.findViewById(R.id.refine_category_box);
			holder.categoryNameTextView = (TextView) convertView.findViewById(R.id.refine_category_name);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		SiftCategory siftCategory = (SiftCategory) getItem(position);
		holder.categoryNameTextView.setText(siftCategory.name);
		holder.categoryCheckBox.setChecked(false);
		if (tempCategory != null)
		{
			if (tempCategory.key.equals(siftCategory.key))
			{
				holder.categoryCheckBox.setChecked(true);
			}
		}
		return convertView;
	}

	public SiftCategory choiceCategory(SiftCategory category)
	{
		if (this.tempCategory == null)
		{
			this.tempCategory = category;
		}
		else
		{
			if (this.tempCategory.key.equals(category.key))
			{
				this.tempCategory = null;
			}
			else
			{
				this.tempCategory = category;
			}
		}
		notifyDataSetChanged();

		return tempCategory;
	}
	
	public void resetData()
	{
		this.tempCategory = null;
		this.notifyDataSetChanged();
	}

	private static class ViewHolder
	{
		private TextView categoryNameTextView;
		private CheckBox categoryCheckBox;
	}
}

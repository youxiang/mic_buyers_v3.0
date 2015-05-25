package com.micen.buyers.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.micen.buyers.activity.R;
import com.micen.buyers.module.db.Country;
import com.micen.buyers.util.Util;

/**********************************************************
 * @文件名称：CountryListAdapter.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年3月27日 上午11:38:04
 * @文件描述：国家选择列表适配器
 * @修改历史：2015年3月27日创建初始版本
 **********************************************************/
public class CountryListAdapter extends BaseAdapter implements SectionIndexer
{
	private Context mContext;
	private ArrayList<Country> dataList;
	private Country country;

	public CountryListAdapter(Context context, ArrayList<Country> dataList)
	{
		this.mContext = context;
		this.dataList = dataList;
	}

	@Override
	public int getCount()
	{
		return dataList.size();
	}

	@Override
	public Object getItem(int position)
	{
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		country = (Country) dataList.get(position);
		if (country.isGroup())
		{
			convertView = LayoutInflater.from(mContext).inflate(R.layout.country_list_group, null);
			TextView name = (TextView) convertView.findViewById(R.id.tv_country_group_name);
			name.setText(country.getIndexChar());
		}
		else
		{
			convertView = LayoutInflater.from(mContext).inflate(R.layout.country_list_child, null);
			ImageView countryFlag = (ImageView) convertView.findViewById(R.id.iv_country_flag);
			TextView name = (TextView) convertView.findViewById(R.id.tv_country_name);
			name.setText(country.getCountryName());
			if (country.getFlagDrawable() != null)
			{
				countryFlag.setBackgroundDrawable(country.getFlagDrawable());
			}
		}
		return convertView;
	}

	@Override
	public int getPositionForSection(int section)
	{
		for (int i = 0; i < dataList.size(); i++)
		{
			if (dataList.get(i).isGroup())
			{
				String sortStr = dataList.get(i).getIndexChar();
				if (sortStr.equals(mContext.getString(R.string.popular_countries)))
				{
					continue;
				}
				else
				{
					char firstChar = sortStr.toUpperCase(Util.getLocale()).charAt(0);
					if (firstChar == section)
					{
						return i;
					}
				}
			}
			else
			{
				continue;
			}
		}
		return -1;
	}

	@Override
	public int getSectionForPosition(int position)
	{
		return dataList.get(position).getIndexChar().charAt(0);
	}

	@Override
	public Object[] getSections()
	{
		return null;
	}

}
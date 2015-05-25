package com.micen.buyers.adapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.micen.buyers.activity.R;
import com.micen.buyers.constant.Constants;
import com.micen.buyers.manager.FavouriteManager.FavouriteType;
import com.micen.buyers.module.favorite.Favorite;
import com.micen.buyers.util.ImageUtil;
import com.micen.buyers.util.Util;

/**********************************************************
 * @文件名称：FavouriteListAdapter.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年5月4日 下午2:02:53
 * @文件描述：收藏列表适配器
 * @修改历史：2015年5月4日创建初始版本
 **********************************************************/
public class FavouriteListAdapter extends BaseAdapter
{
	private ArrayList<Favorite> dataList;
	private Context context;
	private FavouriteType type;
	private ViewHolder holder;

	public FavouriteListAdapter(ArrayList<Favorite> dataList, Context context, FavouriteType type)
	{
		this.dataList = dataList;
		this.context = context;
		this.type = type;
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
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.favourite_item, null);
			holder = new ViewHolder();
			holder.iv = (ImageView) convertView.findViewById(R.id.ivImage);
			holder.tvname = (TextView) convertView.findViewById(R.id.tvName);
			holder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		if (type.toString().equals(FavouriteType.Product.toString()))
		{
			holder.iv.setVisibility(View.VISIBLE);
			ImageUtil.getImageLoader().displayImage(dataList.get(position).picUrl, holder.iv,
					ImageUtil.getRecommendImageOptions());
		}
		else
		{
			holder.iv.setVisibility(View.GONE);
		}
		holder.tvname.setText(dataList.get(position).sourceSubject);
		holder.tvTime.setText("Date  " + formatDate(dataList.get(position).addTime.toString()));
		return convertView;
	}

	static class ViewHolder
	{
		ImageView iv;
		TextView tvname;
		TextView tvTime;
	}

	private static String formatDate(String date)
	{
		String dateValue = "";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Util.getLocale());
		DateFormat df = new SimpleDateFormat(Constants.enDateTemplate, Util.getLocale());
		try
		{
			dateValue = df.format(formatter.parse(date));
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		return dateValue;
	}

	public void addData(ArrayList<Favorite> dataList)
	{
		this.dataList.addAll(dataList);
		notifyDataSetChanged();
	}

}

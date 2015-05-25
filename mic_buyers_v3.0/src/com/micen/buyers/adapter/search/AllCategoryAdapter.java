package com.micen.buyers.adapter.search;

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
import com.micen.buyers.module.category.Category;

/**********************************************************
 * @文件名称：AllCategoryAdapter.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年3月11日 下午3:17:07
 * @文件描述：一级目录列表Adapter
 * @修改历史：2015年3月11日创建初始版本
 **********************************************************/
public class AllCategoryAdapter extends BaseAdapter
{
	private Context context;
	private ArrayList<Category> categoryList;
	private Category category;

	public AllCategoryAdapter(Context context, ArrayList<Category> categoryList)
	{
		this.context = context;
		this.categoryList = categoryList;
	}

	@Override
	public int getCount()
	{
		if (categoryList == null)
		{
			return 0;
		}
		return categoryList.size();
	}

	@Override
	public Object getItem(int arg0)
	{
		return categoryList.get(arg0);
	}

	@Override
	public long getItemId(int arg0)
	{
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2)
	{
		ViewHolder holder;
		if (arg1 == null)
		{
			holder = new ViewHolder();
			arg1 = LayoutInflater.from(context).inflate(R.layout.search_category_listview_item_layout, null);
			holder.categoryIcon = (ImageView) arg1.findViewById(R.id.catogory_icon);
			holder.categoryName = (TextView) arg1.findViewById(R.id.category_name);
			arg1.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) arg1.getTag();
		}
		category = (Category) getItem(arg0);
		holder.categoryName.setText(category.catNameEn);
		holder.categoryIcon.setBackgroundResource(Constants.FIRST_CATEGORY[category.order]);
		return arg1;
	}

	private static class ViewHolder
	{
		private ImageView categoryIcon;
		private TextView categoryName;
	}

}

package com.micen.buyers.adapter.category;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.micen.buyers.activity.R;
import com.micen.buyers.module.category.Category;

/**********************************************************
 * @文件名称：CategoryAdapter.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年3月12日 下午3:14:03
 * @文件描述：二，三级目录Adapter
 * @修改历史：2015年3月12日创建初始版本
 **********************************************************/
public class CategoryAdapter extends BaseExpandableListAdapter
{
	private Context mContext;
	private LayoutInflater inflater;
	private ArrayList<Category> groupList;
	private Category category;

	public CategoryAdapter(Context context, ArrayList<Category> groupList)
	{
		this.mContext = context;
		this.inflater = LayoutInflater.from(mContext);
		this.groupList = groupList;
	}

	@Override
	public Object getChild(int arg0, int arg1)
	{
		return groupList.get(arg0).childCategories.get(arg1);
	}

	@Override
	public long getChildId(int arg0, int arg1)
	{
		return arg1;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent)
	{
		ChildHolder childHolder = null;
		if (convertView == null)
		{
			childHolder = new ChildHolder();
			convertView = inflater.inflate(R.layout.category_child_item_layout, null);
			childHolder.categoryNameView = (TextView) convertView.findViewById(R.id.category_name_view);
			convertView.setTag(childHolder);
		}
		else
		{
			childHolder = (ChildHolder) convertView.getTag();
		}

		childHolder.categoryNameView.setText(((Category) getChild(groupPosition, childPosition)).catNameEn);
		return convertView;
	}

	@Override
	public int getChildrenCount(int arg0)
	{
		return groupList.get(arg0).childCategories.size();
	}

	@Override
	public Object getGroup(int arg0)
	{
		return groupList.get(arg0);
	}

	@Override
	public int getGroupCount()
	{
		return groupList.size();
	}

	@Override
	public long getGroupId(int arg0)
	{
		return arg0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
	{
		category = ((Category) getGroup(groupPosition));
		GroupHolder groupHolder = null;
		if (convertView == null)
		{
			groupHolder = new GroupHolder();
			convertView = inflater.inflate(R.layout.category_group_item_layout, null);
			groupHolder.categoryNameView = (TextView) convertView.findViewById(R.id.category_name_view);
			groupHolder.indictorView = (ImageView) convertView.findViewById(R.id.indictor_view);
			convertView.setTag(groupHolder);
		}
		else
		{
			groupHolder = (GroupHolder) convertView.getTag();
		}

		groupHolder.categoryNameView.setText(category.catNameEn);

		if (category.childCount.equals("0"))
		{
			groupHolder.indictorView.setVisibility(View.INVISIBLE);
		}
		else
		{
			groupHolder.indictorView.setVisibility(View.VISIBLE);
			if (isExpanded)
			{
				groupHolder.indictorView.setImageResource(R.drawable.icon_category_expand);
			}
			else
			{
				groupHolder.indictorView.setImageResource(R.drawable.icon_category_unexpand);
			}
		}
		return convertView;
	}

	@Override
	public boolean hasStableIds()
	{
		return true;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1)
	{
		return true;
	}

	public void updateChildData(int groupPos, ArrayList<Category> list)
	{
		this.groupList.get(groupPos).childCategories = list;
		notifyDataSetChanged();
	}

	private static class GroupHolder
	{
		private TextView categoryNameView;
		private ImageView indictorView;
	}

	private static class ChildHolder
	{
		private TextView categoryNameView;
	}
}

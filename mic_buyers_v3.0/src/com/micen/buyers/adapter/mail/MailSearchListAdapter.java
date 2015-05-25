package com.micen.buyers.adapter.mail;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.micen.buyers.activity.R;
import com.micen.buyers.module.mail.Mail;
import com.micen.buyers.util.Util;

/**********************************************************
 * @文件名称：MailSearchListAdapter.java
 * @文件作者：xiongjiangwei
 * @创建时间：2014年7月14日 上午10:18:45
 * @文件描述：邮件搜索列表适配器
 * @修改历史：2014年7月14日创建初始版本
 **********************************************************/
public class MailSearchListAdapter extends BaseAdapter
{
	private String fullName;
	private Context context;
	private ArrayList<Mail> dataList;
	private String action;
	private ViewHolder holder;

	public MailSearchListAdapter(Context context, ArrayList<Mail> dataList, String action)
	{
		this.dataList = dataList;
		this.context = context;
		this.action = action;
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
		if (null == convertView)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.mail_list_item, parent, false);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.tv_mail_name);
			holder.content = (TextView) convertView.findViewById(R.id.tv_mail_content);
			holder.date = (TextView) convertView.findViewById(R.id.tv_mail_date);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		if (Boolean.parseBoolean(dataList.get(position).isUnread))// true：未读
		{
			holder.name.setTextColor(context.getResources().getColor(R.color.color_333333));
			holder.content.setTextColor(context.getResources().getColor(R.color.color_333333));
			holder.date.setTextColor(context.getResources().getColor(R.color.color_333333));
		}
		else
		{
			holder.name.setTextColor(context.getResources().getColor(R.color.color_999999));
			holder.content.setTextColor(context.getResources().getColor(R.color.color_999999));
			holder.date.setTextColor(context.getResources().getColor(R.color.color_999999));
		}
		if ("0".equals(action))
		{
			fullName = dataList.get(position).sender.fullName;

		}
		else
		{
			fullName = dataList.get(position).receiver.fullName;
		}
		holder.name.setText(fullName);
		holder.date.setText(Util.formatDateToEn(dataList.get(position).date));
		holder.content.setText(dataList.get(position).subject);
		return convertView;
	}

	static class ViewHolder
	{
		TextView name;
		TextView date;
		TextView content;
	}

	public ArrayList<Mail> getAdapterDataList()
	{
		return dataList;
	}

	public void addMails(ArrayList<Mail> dataList)
	{
		this.dataList.addAll(dataList);
		notifyDataSetChanged();
	}
}

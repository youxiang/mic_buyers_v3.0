package com.micen.buyers.adapter.mail;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.micen.buyers.activity.R;
import com.micen.buyers.module.mail.Mail;
import com.micen.buyers.util.Util;

/**********************************************************
 * @文件名称：MailListAdapter.java
 * @文件作者：xiongjiangwei
 * @创建时间：2014年7月10日 下午3:40:01
 * @文件描述：邮件列表适配器
 * @修改历史：2014年7月10日创建初始版本
 **********************************************************/
public class MailListAdapter extends BaseAdapter
{
	private Context context;
	private ArrayList<Mail> dataList;
	private String flag;
	private Handler handler;
	private boolean isDeleteMode = false;
	private ViewHolder holder;
	public ArrayList<String> selectMailList = new ArrayList<String>();
	private Mail mail;

	public MailListAdapter(Context context, ArrayList<Mail> dataList, String flag, Handler handler)
	{
		this.context = context;
		this.dataList = dataList;
		this.flag = flag;
		this.handler = handler;
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
		if (convertView == null || convertView.getTag() == null)
		{
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.mail_list_item, parent, false);
			holder.name = (TextView) convertView.findViewById(R.id.tv_mail_name);
			holder.content = (TextView) convertView.findViewById(R.id.tv_mail_content);
			holder.data = (TextView) convertView.findViewById(R.id.tv_mail_date);
			holder.checkbox = (CheckBox) convertView.findViewById(R.id.cb_mail);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		mail = (Mail) getItem(position);
		holder.checkbox.setVisibility(isDeleteMode ? View.VISIBLE : View.GONE);
		holder.checkbox.setTag(position);
		holder.checkbox.setOnCheckedChangeListener(checkChange);
		if (selectMailList.contains(mail.mailId))
		{
			holder.checkbox.setChecked(true);
		}
		else
		{
			holder.checkbox.setChecked(false);
		}
		String fullName = null;
		if ("0".equals(flag))
		{
			fullName = mail.sender.fullName;
		}
		else
		{
			fullName = mail.receiver.fullName;
		}
		if (Boolean.parseBoolean(mail.isUnread))// true：未读
		{
			holder.name.setTextColor(context.getResources().getColor(R.color.color_333333));
			holder.content.setTextColor(context.getResources().getColor(R.color.color_333333));
			holder.data.setTextColor(context.getResources().getColor(R.color.color_333333));
		}
		else
		{
			holder.name.setTextColor(context.getResources().getColor(R.color.color_999999));
			holder.content.setTextColor(context.getResources().getColor(R.color.color_999999));
			holder.data.setTextColor(context.getResources().getColor(R.color.color_999999));
		}
		holder.name.setText(fullName);
		holder.data.setText(Util.formatDateToEn(mail.date));
		holder.content.setText(mail.subject);
		return convertView;
	}

	private OnCheckedChangeListener checkChange = new OnCheckedChangeListener()
	{
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		{
			int position = Integer.parseInt(buttonView.getTag().toString());
			String mailId = dataList.get(position).mailId;
			if (isChecked)
			{
				selectMailList.add(mailId);
			}
			else
			{
				selectMailList.remove(mailId);
			}
			if (selectMailList.size() == 0)
			{
				handler.sendEmptyMessage(1);
			}
			else
			{
				handler.sendEmptyMessage(2);
			}
		}
	};

	static class ViewHolder
	{
		TextView name;
		TextView data;
		TextView content;
		CheckBox checkbox;
	}

	public void setDeleteMode(boolean isDeleteMode)
	{
		this.isDeleteMode = isDeleteMode;
		notifyDataSetChanged();
	}

	public void addData(ArrayList<Mail> dataList)
	{
		this.dataList.addAll(dataList);
		notifyDataSetChanged();
	}

}

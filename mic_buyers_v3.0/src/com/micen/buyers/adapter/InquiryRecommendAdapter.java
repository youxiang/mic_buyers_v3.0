package com.micen.buyers.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.micen.buyers.activity.R;
import com.micen.buyers.manager.PopupManager;

/**********************************************************
 * @文件名称：InquiryRecommendAdapter.java
 * @文件作者：xiongjiangwei
 * @创建时间：2014年5月12日 下午3:43:32
 * @文件描述：询盘内容推荐列表适配器
 * @修改历史：2014年5月12日创建初始版本
 **********************************************************/
public class InquiryRecommendAdapter extends BaseAdapter
{
	private Context context;
	private String[] nameArray;
	private String[] valueArray;
	private EditText actv;
	private Holder holder;
	private String initContent;
	private boolean isReply;

	public InquiryRecommendAdapter(Context context, EditText actv, String initContent, boolean isReply)
	{
		this.context = context;
		nameArray = context.getResources().getStringArray(R.array.inquiry_recommend_name);
		valueArray = context.getResources().getStringArray(R.array.inquiry_recommend_value);
		this.actv = actv;
		this.initContent = initContent;
		this.isReply = isReply;
	}

	@Override
	public int getCount()
	{
		return nameArray.length;
	}

	@Override
	public Object getItem(int position)
	{
		return nameArray[position];
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.inquiry_recommend_list_item, parent, false);
			holder = new Holder();
			holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
			convertView.setTag(holder);
		}
		else
		{
			holder = (Holder) convertView.getTag();
		}
		holder.tvName.setText(nameArray[position]);
		holder.tvName.setTag(position);
		holder.tvName.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (isReply && !TextUtils.isEmpty(initContent))
				{
					actv.setText(valueArray[Integer.parseInt(v.getTag().toString())] + initContent);
				}
				else
				{
					actv.setText(valueArray[Integer.parseInt(v.getTag().toString())]);
				}
				PopupManager.getInstance().dismissPopup();
			}
		});
		return convertView;
	}

	@Override
	public long getItemId(int position)
	{
		return 0;
	}

	static class Holder
	{
		TextView tvName;
	}
}

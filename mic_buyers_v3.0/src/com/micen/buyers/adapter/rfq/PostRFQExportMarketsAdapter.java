package com.micen.buyers.adapter.rfq;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.micen.buyers.activity.R;
import com.micen.buyers.constant.Constants;

/**********************************************************
 * @文件名称：PostRFQCheckBoxAdapter.java
 * @文件作者：xiongjiangwei
 * @创建时间：2014-2-24 下午01:40:51
 * @文件描述：发布RFQ中多选框列表适配器
 * @修改历史：2014-2-24创建初始版本
 **********************************************************/
public class PostRFQExportMarketsAdapter extends BaseAdapter
{
	private String[] dataList;
	private LayoutInflater inflater;
	private String str;
	private ViewHolder holder;
	private Context context;

	public PostRFQExportMarketsAdapter(Context context, String[] dataList)
	{
		this.context = context;
		this.dataList = dataList;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount()
	{
		return dataList.length;
	}

	@Override
	public Object getItem(int arg0)
	{
		return dataList[arg0];
	}

	@Override
	public long getItemId(int arg0)
	{
		return 0;
	}

	@Override
	public View getView(int arg0, View contentView, ViewGroup arg2)
	{
		if (contentView == null)
		{
			holder = new ViewHolder();
			contentView = inflater.inflate(R.layout.mic_rfq_checkbox_popup_item, null);
			holder.tv = (TextView) contentView.findViewById(R.id.mic_rfq_checkbox_text);
			holder.cb = (CheckBox) contentView.findViewById(R.id.mic_rfq_checkbox);
			contentView.setTag(holder);
			contentView.setOnClickListener(click);
		}
		else
		{
			holder = (ViewHolder) contentView.getTag();
		}
		str = getItem(arg0).toString();
		holder.tv.setText(str);
		if (Constants.selectedExportMarket.contains(str))
		{
			holder.cb.setChecked(true);
		}
		else
		{
			holder.cb.setChecked(false);
		}
		return contentView;
	}

	private static class ViewHolder
	{
		private TextView tv;
		private CheckBox cb;
	}

	private OnClickListener click = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			String value = ((TextView) v.findViewById(R.id.mic_rfq_checkbox_text)).getText().toString();
			String selectAllText = context.getString(R.string.mic_rfq_export_markets_all);
			if (Constants.selectedExportMarket.contains(value))
			{
				Constants.selectedExportMarket.remove(value);
				Constants.selectedExportMarket.remove(selectAllText);
				if (value.equals(selectAllText))
				{
					Constants.selectedExportMarket.clear();
				}
			}
			else
			{
				if (value.equals(selectAllText))
				{
					Constants.selectedExportMarket.clear();
					for (int i = 0; i < dataList.length; i++)
					{
						Constants.selectedExportMarket.add(dataList[i]);
					}
				}
				else
				{
					Constants.selectedExportMarket.add(value);
					if (Constants.selectedExportMarket.size() == getCount() - 1)
					{
						Constants.selectedExportMarket.add(selectAllText);
					}
				}
			}
			notifyDataSetChanged();
		}
	};
}

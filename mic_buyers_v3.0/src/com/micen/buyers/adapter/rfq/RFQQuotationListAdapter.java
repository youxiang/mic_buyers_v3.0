package com.micen.buyers.adapter.rfq;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.micen.buyers.activity.R;
import com.micen.buyers.module.quotation.QuotationList;
import com.micen.buyers.module.quotation.QuotationListContent;

/**********************************************************
 * @文件名称：RFQQuotationListAdapter.java
 * @文件作者：renzhiqiang
 * @创建时间：2014年10月31日 上午9:22:47
 * @文件描述：Quotation列表适配器
 * @修改历史：2014年10月31日创建初始版本
 **********************************************************/
public class RFQQuotationListAdapter extends BaseAdapter
{
	private Context context;
	private QuotationList theQuotationListBean;
	private QuotationListContent content;
	private ViewHolder holder;

	public RFQQuotationListAdapter(Context context, QuotationList theQuotationListBean)
	{
		this.context = context;
		this.setTheQuotationListBean(theQuotationListBean);
	}

	@Override
	public int getCount()
	{
		if (getTheQuotationListBean() == null)
		{
			return 0;
		}
		return getTheQuotationListBean().content.size();
	}

	@Override
	public Object getItem(int arg0)
	{
		if (getTheQuotationListBean() == null)
		{
			return null;
		}
		return getTheQuotationListBean().content.get(arg0);
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
			contentView = LayoutInflater.from(context).inflate(R.layout.quotation_list_item, null);
			holder.productTv = (TextView) contentView.findViewById(R.id.mic_quotation_list_page_product);
			holder.quotationTv = (TextView) contentView.findViewById(R.id.mic_quotation_list_page_quotation_tx);
			holder.isGoldView = (ImageView) contentView.findViewById(R.id.mic_quotation_list_page_company_icon);
			holder.memberTypeView = (ImageView) contentView.findViewById(R.id.quotation_member_view);
			contentView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) contentView.getTag();
		}
		content = (QuotationListContent) getItem(arg0);
		if (content != null)
		{
			if (content.gold.equals("true"))
			{
				holder.isGoldView.setImageResource(R.drawable.ic_supplier_gold_member);
				if (content.AS.equals("true"))
				{
					holder.memberTypeView.setImageResource(R.drawable.ic_supplier_as);
				}
				if (content.baseAudit.equals("true"))
				{
					holder.memberTypeView.setImageResource(R.drawable.ic_supplier_oc);
				}
				if (content.fileAudit.equals("true"))
				{
					holder.memberTypeView.setImageResource(R.drawable.ic_supplier_lv);
				}
			}

			holder.productTv.setText(content.quoteCompanyName);
			if (Boolean.parseBoolean(content.buyerRead))// true：已读
			{
				holder.productTv.setTextColor(context.getResources().getColor(R.color.color_999999));
			}
			else
			{
				holder.productTv.setTextColor(context.getResources().getColor(R.color.color_333333));
			}
			holder.quotationTv
					.setText(getUnitPrice(content.items.get(0).unitPrice, content.items.get(0).unitPriceType));
		}
		return contentView;
	}

	private Spanned getUnitPrice(String unitPrice, String unitPriceType)
	{
		return Html.fromHtml("<font color='" + context.getResources().getColor(R.color.color_999999)
				+ "'>Unit Price:</font><font color='" + context.getResources().getColor(R.color.color_e62e2e) + "'>"
				+ unitPrice + "</font>&nbsp;&nbsp;<font color='"
				+ context.getResources().getColor(R.color.color_666666) + "'>" + unitPriceType + "</font>");
	}

	public QuotationList getTheQuotationListBean()
	{
		return theQuotationListBean;
	}

	public void setTheQuotationListBean(QuotationList theQuotationListBean)
	{
		this.theQuotationListBean = theQuotationListBean;
	}

	static class ViewHolder
	{
		TextView productTv;
		TextView quotationTv;
		ImageView isGoldView;
		ImageView memberTypeView;
	}
}

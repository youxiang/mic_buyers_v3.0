package com.micen.buyers.adapter.searchresult;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.focustech.common.widget.HorizontalListView;
import com.micen.buyers.activity.R;
import com.micen.buyers.activity.productdetail.ProductMessageActivity_;
import com.micen.buyers.adapter.search.CompanyProductIconAdapter;
import com.micen.buyers.module.search.SearchCompany;
import com.micen.buyers.module.search.SearchCompanyProduct;
import com.micen.buyers.util.Util;

/**********************************************************
 * @文件名称：CompanyListAdapter.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年3月19日 上午9:02:54
 * @文件描述：公司列表Adapter
 * @修改历史：2015年3月19日创建初始版本
 **********************************************************/
public class CompanyListAdapter extends BaseAdapter
{
	private Context mContext;
	private ArrayList<SearchCompany> dataList;
	private SearchCompany company;
	private ViewHolder holder;
	public ArrayList<String> mSelectItem;

	public CompanyListAdapter(Context context, ArrayList<SearchCompany> dataList)
	{
		this.mContext = context;
		this.dataList = dataList;
		this.mSelectItem = new ArrayList<String>();
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.company_list_item, null);
			holder = new ViewHolder();
			holder.companyName = (TextView) convertView.findViewById(R.id.companyname_textview);
			holder.membertype_linearlayout = (LinearLayout) convertView.findViewById(R.id.membertype_linearlayout);
			holder.audit_supplier_linearlayout = (LinearLayout) convertView
					.findViewById(R.id.audit_supplier_linearlayout);
			holder.supplier_onsidecheck = (ImageView) convertView.findViewById(R.id.supplier_onsidecheck);
			holder.tv_supplier_onsidecheck = (TextView) convertView.findViewById(R.id.tv_supplier_onsidecheck);
			holder.companyLocationText = (TextView) convertView.findViewById(R.id.company_location_textview);
			holder.companyDateText = (TextView) convertView.findViewById(R.id.company_date_textview);
			holder.listview = (HorizontalListView) convertView.findViewById(R.id.company_product_icon_list);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		company = (SearchCompany) dataList.get(position);
		holder.companyName.setText(company.companyName);
		if ("1".equals(company.memberType.trim()))
		{
			holder.membertype_linearlayout.setVisibility(View.VISIBLE);
		}
		else
		{
			holder.membertype_linearlayout.setVisibility(View.GONE);
		}
		if ("1".equals(company.auditType.trim()))
		{
			holder.audit_supplier_linearlayout.setVisibility(View.VISIBLE);
			holder.supplier_onsidecheck.setImageResource(R.drawable.ic_supplier_as);
			holder.tv_supplier_onsidecheck.setText(R.string.audited_report);
		}
		else if ("2".equals(company.auditType.trim()))
		{
			holder.audit_supplier_linearlayout.setVisibility(View.VISIBLE);
			holder.supplier_onsidecheck.setImageResource(R.drawable.ic_supplier_oc);
			holder.tv_supplier_onsidecheck.setText(R.string.onsite_check);
		}
		else if ("3".equals(company.auditType.trim()))
		{
			holder.audit_supplier_linearlayout.setVisibility(View.VISIBLE);
			holder.supplier_onsidecheck.setImageResource(R.drawable.ic_supplier_lv);
			holder.tv_supplier_onsidecheck.setText(R.string.license_verified);
		}
		else
		{
			holder.audit_supplier_linearlayout.setVisibility(View.GONE);
		}
		holder.companyLocationText.setVisibility(View.VISIBLE);
		holder.companyLocationText.setText("[" + company.province + "," + company.country + "]");
		holder.companyDateText.setText(company.updateTime);
		if (company.mainProduct != null && company.mainProduct.size() > 0)
		{
			CompanyProductIconAdapter adapter = new CompanyProductIconAdapter(mContext, company.mainProduct);
			holder.listview.setDividerWidth(Util.dip2px(5));
			holder.listview.setAdapter(adapter);
			holder.listview.setOnItemClickListener(new OnItemClickListener()
			{

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id)
				{
					SearchCompanyProduct temp = (SearchCompanyProduct) parent.getAdapter().getItem(position);
					// SearchCompanyProduct temp = company.mainProduct.get(position);
					// String productId = temp.productId;

					Intent intent = new Intent(mContext, ProductMessageActivity_.class);
					intent.putExtra("productId", temp.productId);
					// intent.putExtra("category", category);
					mContext.startActivity(intent);

					// TODO Auto-generated method stub
					// System.out.println("hellp:"+position);

				}
			});
			holder.listview.setVisibility(View.VISIBLE);
		}

		changeItemColor(convertView, holder.companyName, company);
		return convertView;
	}

	static class ViewHolder
	{
		TextView companyName;
		LinearLayout membertype_linearlayout;
		LinearLayout audit_supplier_linearlayout;
		ImageView supplier_onsidecheck;
		TextView tv_supplier_onsidecheck;
		TextView companyLocationText;
		TextView companyDateText;
		HorizontalListView listview;
	}

	public void addCompanies(ArrayList<SearchCompany> dataList)
	{
		this.dataList.addAll(dataList);
		notifyDataSetChanged();
	}

	private void changeItemColor(View convertView, TextView view, SearchCompany company)
	{
		if (mSelectItem.contains(company.companyId))
		{
			// Ҫ�滻Ϊһ��drawable
			convertView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_list_selector));
			view.setTextColor(mContext.getResources().getColor(R.color.table_line));
		}
		else
		{
			convertView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_list_selector));
			view.setTextColor(mContext.getResources().getColor(R.color.mic_home_menu_text));
		}
	}

	/**
	 * 单个数据添加到适配器
	 * @param productId
	 */
	public void addSelectItem(String productId)
	{
		if (this != null)
		{
			this.mSelectItem.add(productId);
			this.notifyDataSetChanged();
		}
	}

	/**
	 * 批量数据添加到适配器
	 * @param productIds
	 */
	public void addSelectItem(ArrayList<String> productIds)
	{
		if (productIds != null && productIds.size() != 0)
		{
			if (this != null)
			{
				this.mSelectItem.addAll(productIds);
				this.notifyDataSetChanged();
			}
		}
	}

}
package com.micen.buyers.view;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.micen.buyers.activity.R;
import com.micen.buyers.activity.productdetail.ProductMessageActivity_;
import com.micen.buyers.adapter.SpecialDetailGridAdapter;
import com.micen.buyers.manager.SysManager;
import com.micen.buyers.module.search.SearchProduct;

/**********************************************************
 * @文件名称：SpecialDetailFragment.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年4月30日 下午3:15:41
 * @文件描述：专题分类Fragment
 * @修改历史：2015年4月30日创建初始版本
 **********************************************************/
public class SpecialDetailFragment extends Fragment implements OnItemClickListener
{
	private ArrayList<SearchProduct> dataList;
	private GridView gridView;
	private SpecialDetailGridAdapter adapter;

	public SpecialDetailFragment()
	{

	}

	public SpecialDetailFragment(ArrayList<SearchProduct> dataList)
	{
		this.dataList = dataList;
	}

	public static SpecialDetailFragment newInstance(ArrayList<SearchProduct> dataList)
	{
		SpecialDetailFragment f = new SpecialDetailFragment(dataList);
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.special_detail_item, null);
		gridView = (GridView) view.findViewById(R.id.special_detail_gridview);
		adapter = new SpecialDetailGridAdapter(getActivity(), dataList);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(this);
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		// 事件统计 16 点击浏览专题产品 点击事件
		SysManager.analysis(R.string.a_type_click, R.string.c16);

		SearchProduct product = (SearchProduct) parent.getAdapter().getItem(position);
		Intent productDetailInt = new Intent(getActivity(), ProductMessageActivity_.class);
		productDetailInt.putExtra("productId", product.productId);
		startActivity(productDetailInt);
	}
}

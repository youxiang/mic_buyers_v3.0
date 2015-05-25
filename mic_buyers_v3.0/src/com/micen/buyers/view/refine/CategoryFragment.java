package com.micen.buyers.view.refine;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.focustech.common.mob.DisposeDataListener;
import com.focustech.common.widget.dialog.CommonProgressDialog;
import com.micen.buyers.activity.R;
import com.micen.buyers.adapter.refine.RefineListAdapter;
import com.micen.buyers.constant.Constants;
import com.micen.buyers.http.RequestCenter;
import com.micen.buyers.module.search.SearchProducts;
import com.micen.buyers.module.sift.SiftCategory;

/**********************************************************
 * @文件名称：CategoryFragment.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年3月20日 下午6:24:03
 * @文件描述：筛选目录Fragment
 * @修改历史：2015年3月20日创建初始版本
 **********************************************************/
public class CategoryFragment extends BaseRefineFragment implements OnItemClickListener
{
	protected ListView categoryListView;
	protected ArrayList<SiftCategory> siftCatogoriesList;
	protected RefineListAdapter categoryAdapter;

	public CategoryFragment()
	{
	}

	public CategoryFragment(ArrayList<SiftCategory> siftCatogoriesList, SiftAllListener listener)
	{
		this.siftCatogoriesList = siftCatogoriesList;
		this.listener = listener;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		if (contentView == null)
		{
			contentView = inflater.inflate(R.layout.refine_category_fragment_layout, null);
			initView();
		}

		ViewGroup parent = (ViewGroup) contentView.getParent();
		if (parent != null)
		{
			parent.removeView(contentView);
		}
		return contentView;
	}

	private void initView()
	{
		categoryListView = (ListView) contentView.findViewById(R.id.refine_category_list_view);
		categoryAdapter = new RefineListAdapter(mContext, siftCatogoriesList);
		categoryListView.setAdapter(categoryAdapter);
		categoryListView.setOnItemClickListener(this);
		initChoiceData();
	}

	private void initChoiceData()
	{
		String str = listener.getValue("category");
		if (str != null)
		{
			SiftCategory category = new SiftCategory();
			category.key = str;
			categoryAdapter.choiceCategory(category);
		}

	}

	private void getPropertyUnderCategory(String key)
	{
		RequestCenter.searchProducts(dataLisener, Constants.KEY_WORD, key, "", "2", "1", "5", "0", "0", "", "");
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		SiftCategory category = (SiftCategory) categoryAdapter.getItem(position);
		category = categoryAdapter.choiceCategory(category);
		if (category == null)
		{
			listener.sift(0, "");
		}
		else
		{
			listener.sift(0, category.key);
			// 显示等待框 by:xuliucheng
			CommonProgressDialog.getInstance().showProgressDialog(getActivity(), getString(R.string.mic_loading));
			// 重新请求数据
			getPropertyUnderCategory(category.key);
		}
		
	}
	
	private DisposeDataListener dataLisener = new DisposeDataListener()
	{

		@Override
		public void onSuccess(Object obj)
		{
			CommonProgressDialog.getInstance().dismissProgressDialog();
			SearchProducts products = (SearchProducts) obj;
			if (products.content != null && products.content.size() > 0)
			{
				listener.setAttributeData(products.property);
			}
		}

		@Override
		public void onFailure(Object failedReason)
		{
			if (getActivity() != null)
			{
				CommonProgressDialog.getInstance().dismissProgressDialog();
				getActivity().finish();
			}
		}
	};

	@Override
	public void resetData()
	{
		if (categoryAdapter != null)
		{
			categoryAdapter.resetData();
		}
	}

}

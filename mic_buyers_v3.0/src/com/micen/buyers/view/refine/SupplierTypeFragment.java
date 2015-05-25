package com.micen.buyers.view.refine;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ExpandableListView.OnChildClickListener;

import com.micen.buyers.activity.R;
import com.micen.buyers.adapter.refine.SupplierTypeExpandListAdapter;
import com.micen.buyers.module.sift.SiftSupplier;
import com.micen.buyers.module.sift.SiftSupplierGroup;

/**********************************************************
 * @文件名称：SupplierTypeFragment.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年3月23日 下午3:50:41
 * @文件描述：供应商类型Fragment
 * @修改历史：2015年3月23日创建初始版本
 **********************************************************/

public class SupplierTypeFragment extends BaseRefineFragment
{

	protected ExpandableListView listView;
	protected SupplierTypeExpandListAdapter adapter;

	protected RelativeLayout goldMemberLayout;
	protected RelativeLayout auditSupplierLayout;
	protected RelativeLayout onSiteLayout;
	protected RelativeLayout licenseLayout;
	protected ImageView goldImage;
	protected ImageView auditImage;
	protected ImageView onSiteImage;
	protected ImageView licenseImage;

	private String goldType = "0";
	private String cerType = "0";

	public SupplierTypeFragment()
	{
	}

	public SupplierTypeFragment(SiftAllListener listener)
	{
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
			contentView = inflater.inflate(R.layout.refine_supplier_type_fragment_layout, null);
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
		listView = (ExpandableListView) contentView.findViewById(R.id.supplier_group_list);
		ArrayList<SiftSupplierGroup> list = new ArrayList<SiftSupplierGroup>();
		SiftSupplierGroup membetType = new SiftSupplierGroup();
		membetType.name = "Member Type";
		membetType.options = new ArrayList<SiftSupplier>();
		membetType.options.add(new SiftSupplier(R.drawable.ic_supplier_gold_member, "Gold Member"));
		list.add(membetType);

		SiftSupplierGroup certificationType = new SiftSupplierGroup();
		certificationType.name = "Certification Type";
		certificationType.options = new ArrayList<SiftSupplier>();
		certificationType.options.add(new SiftSupplier(R.drawable.ic_supplier_as, "Aduited Supplier"));
		certificationType.options.add(new SiftSupplier(R.drawable.ic_supplier_oc, "Onsite Supplier"));
		certificationType.options.add(new SiftSupplier(R.drawable.ic_supplier_lv, "License Supplier"));

		list.add(certificationType);

		adapter = new SupplierTypeExpandListAdapter(this.getActivity(), list, listener);

		listView.setAdapter(adapter);

		listView.setOnChildClickListener(onChildListener);

		initChoiceData();
	}

	private void initChoiceData()
	{
		goldType = listener.getValue("needGM");
		cerType = listener.getValue("needAudit");

		if ("1".equals(goldType))
		{
			adapter.recordOptions.put(0, adapter.getID(0, 0));
		}

		if ("1".equals(cerType))
		{
			adapter.recordOptions.put(1, adapter.getID(1, 0));
		}
		if ("2".equals(cerType))
		{
			adapter.recordOptions.put(1, adapter.getID(1, 1));

		}
		if ("3".equals(cerType))
		{
			adapter.recordOptions.put(1, adapter.getID(1, 2));
		}

		listView.expandGroup(0);
		listView.expandGroup(1);

	}

	/**
	 * 属性筛选事件响应
	 */
	private OnChildClickListener onChildListener = new OnChildClickListener()
	{
		@Override
		public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
		{
			SiftSupplier supplier = (SiftSupplier) adapter.getChild(groupPosition, childPosition);
			adapter.changeStyle(adapter.getID(groupPosition, childPosition), supplier);

			if (groupPosition == 0)
			{
				if ("0".equals(goldType))
				{
					goldType = "1";
				}
				else
				{
					goldType = "0";
				}
			}
			else
			{
				if (childPosition == 0)
				{
					if ("1".equals(cerType))
					{
						cerType = "0";
					}
					else
					{
						cerType = "1";
					}
				}
				else if (childPosition == 1)
				{
					if ("2".equals(cerType))
					{
						cerType = "0";
					}
					else
					{
						cerType = "2";
					}
				}
				else
				{
					if ("3".equals(cerType))
					{
						cerType = "0";
					}
					else
					{
						cerType = "3";
					}
				}

			}
			listener.sift(2, goldType + cerType);
			return true;
		}
	};

	@Override
	public void resetData()
	{
		if (adapter != null)
		{
			adapter.resetData();
		}
	}

}

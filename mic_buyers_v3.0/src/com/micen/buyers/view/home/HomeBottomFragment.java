package com.micen.buyers.view.home;

import java.util.ArrayList;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;

import com.focustech.common.widget.dialog.CommonDialog;
import com.focustech.common.widget.dialog.CommonDialog.DialogClickListener;
import com.micen.buyers.activity.R;
import com.micen.buyers.activity.productdetail.ProductMessageActivity_;
import com.micen.buyers.adapter.ProductHistoryAdapter;
import com.micen.buyers.db.DBDataHelper;
import com.micen.buyers.db.DBHelper;
import com.micen.buyers.manager.SysManager;
import com.micen.buyers.module.db.Module;
import com.micen.buyers.module.db.ProductHistory;
import com.micen.buyers.util.Util;

@EFragment(R.layout.home_bottom_layout)
public class HomeBottomFragment extends HomeBaseFragment
{
	@ViewById(R.id.gv_home_history_product)
	protected GridView productHistoryGrid;

	@ViewById(R.id.home_history_empty_view)
	protected View productHistoryEmptyView;

	private ArrayList<ProductHistory> proBrowseHisBean;

	private ProductHistoryAdapter productHistoryAdapter;

	private void initData()
	{
		refreshProductHistory();
	}

	@Override
	public String getChildTag()
	{
		return HomeBottomFragment.class.getName();
	}

	@AfterViews
	protected void initView()
	{
		initData();
		productHistoryGrid.setEmptyView(productHistoryEmptyView);
		productHistoryAdapter = new ProductHistoryAdapter(getActivity(), proBrowseHisBean);
		productHistoryGrid.setAdapter(productHistoryAdapter);
		productHistoryGrid.setOnItemClickListener(historyItemClick);
		productHistoryGrid.setOnItemLongClickListener(historyItemLongClick);

	}

	protected void refreshProductHistory()
	{
		ArrayList<Module> productModules = DBDataHelper.getInstance().select(DBHelper.PRODUCT_BROWSE_HISTORY_, null,
				null, null, "vistTime DESC", ProductHistory.class);
		proBrowseHisBean = new ArrayList<ProductHistory>();
		for (int i = 0; i < productModules.size(); i++)
		{
			ProductHistory moduleBean = (ProductHistory) productModules.get(i);
			proBrowseHisBean.add(moduleBean);
		}
		if (productHistoryAdapter != null)
		{
			productHistoryAdapter.setData(proBrowseHisBean);
		}
	}

	private OnItemClickListener historyItemClick = new OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			// 事件统计 10 点击历史浏览产品（首页） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c10);

			ProductHistory history = (ProductHistory) parent.getAdapter().getItem(position);
			Intent intent = new Intent(getActivity(), ProductMessageActivity_.class);
			intent.putExtra("productId", history.productId);
			getActivity().startActivity(intent);
		}
	};

	private OnItemLongClickListener historyItemLongClick = new OnItemLongClickListener()
	{

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
		{
			ProductHistory history = (ProductHistory) parent.getAdapter().getItem(position);
			deleteProductHistoryDialog(history);
			return true;
		}
	};

	protected void deleteProductHistoryDialog(final ProductHistory history)
	{
		final CommonDialog dialog = new CommonDialog(getActivity(), getString(R.string.mic_delete),
				getString(R.string.confirm), getString(R.string.cancel), Util.dip2px(285), new DialogClickListener()
				{
					@Override
					public void onDialogClick()
					{
						int line = DBHelper.getInstance().delete(DBHelper.PRODUCT_BROWSE_HISTORY_, DBHelper.ID + "=?",
								new String[]
								{ history.id });
						if (line > 0)
						{
							refreshProductHistory();
						}
					}
				});
		dialog.show();
	}

}
package com.micen.buyers.view.product;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.focustech.common.mob.DisposeDataListener;
import com.focustech.common.util.ToastUtil;
import com.focustech.common.util.Utils;
import com.focustech.common.widget.pulltorefresh.PullToRefreshBase;
import com.focustech.common.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.focustech.common.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.focustech.common.widget.pulltorefresh.PullToRefreshGridView;
import com.focustech.common.widget.pulltorefresh.PullToRefreshListView;
import com.micen.buyers.activity.R;
import com.micen.buyers.activity.productdetail.ProductMessageActivity_;
import com.micen.buyers.activity.showroom.ProductRefineActivity_;
import com.micen.buyers.activity.showroom.ProductSearchActivity_;
import com.micen.buyers.activity.showroom.ShowRoomActivity;
import com.micen.buyers.adapter.searchresult.ProductBaseAdapter;
import com.micen.buyers.adapter.searchresult.ProductGridAdapter;
import com.micen.buyers.adapter.searchresult.ProductListAdapter;
import com.micen.buyers.constant.Constants;
import com.micen.buyers.db.SharedPreferenceManager;
import com.micen.buyers.http.RequestCenter;
import com.micen.buyers.manager.SysManager;
import com.micen.buyers.module.search.SearchProduct;
import com.micen.buyers.module.search.SearchProducts;
import com.micen.buyers.module.showroom.ProductGroup;
import com.micen.buyers.module.showroom.ProductGroups;
import com.micen.buyers.util.Util;
import com.micen.buyers.widget.other.DragLayout;

public class ProductListFragment extends Fragment implements OnClickListener, OnItemClickListener
{
	public boolean showToast;

	private static final String GET_REAL_IMAGE = "2";

	public static final int MODE_GRID = 0;
	public static final int MODE_LIST = 1;

	private int mode;

	private String companyId;

	private String memberType;

	private boolean showTitle;

	private View titleLayout;

	ImageView titleLeftBtn;

	private TextView titleNameText;

	private ImageView titleRightBtn2;

	private ImageView titleRightBtn3;

	protected LinearLayout suggestLayout;
	protected TextView tvSuggest;
	protected RelativeLayout searchListLayout;
	protected TextView tvNoMatch;

	protected PullToRefreshListView pullToListView;
	protected ListView searchListView;
	protected PullToRefreshGridView pullToGridView;
	protected GridView searchGridView;

	protected RelativeLayout progressLayout;
	protected RelativeLayout loadingLayout;

	protected ImageView modeImageView;

	protected ImageView scrollTopImageView;

	protected ProductBaseAdapter adapter;

	protected ArrayList<String> searchListRecoders;

	private HashMap<String, String> siftMap;
	protected int pageIndex = 1;
	protected int pageSize = 20;

	protected boolean isRefresh = false;
	protected boolean isLoadMore = false;
	protected boolean isPauseState = false;

	protected String productsString;

	private ProductGroups groups;

	private SearchProducts products;

	private ProductGroup selectedGroup;

	private DragLayout dragLayout;

	public ProductListFragment(String companyId, String memberType, int mode, boolean showTitle, boolean showToast,
			DragLayout dragLayout)
	{
		this.companyId = companyId;
		this.memberType = memberType;
		this.mode = mode;
		this.showTitle = showTitle;
		this.showToast = showToast;
		this.dragLayout = dragLayout;
	}

	protected void shake()
	{
		// TODO Auto-generated method stub
		dragLayout.shake();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View root = inflater.inflate(R.layout.activity_search_list, null);
		titleLeftBtn = (ImageView) root.findViewById(R.id.common_title_back_button);
		titleLeftBtn.setImageResource(R.drawable.ic_up);
		titleLeftBtn.setOnClickListener(this);
		titleNameText = (TextView) root.findViewById(R.id.common_title_name);
		titleRightBtn2 = (ImageView) root.findViewById(R.id.common_title_right_button2);
		titleRightBtn2.setImageResource(R.drawable.icon_search_normal);
		// titleRightBtn2.setImageDrawable(Utils.setImageButtonState(R.drawable.icon_search_normal,
		// R.drawable.icon_search_down, getActivity()));
		titleRightBtn2.setOnClickListener(this);
		titleRightBtn3 = (ImageView) root.findViewById(R.id.common_title_right_button3);
		titleRightBtn3.setImageResource(R.drawable.ic_menu_normal);
		// titleRightBtn3.setImageDrawable(Utils.setImageButtonState(R.drawable.ic_menu_selected,
		// R.drawable.ic_menu_normal, getActivity()));
		titleRightBtn3.setOnClickListener(this);

		titleLayout = root.findViewById(R.id.search_head_title);
		suggestLayout = (LinearLayout) root.findViewById(R.id.search_suggest_layout);
		tvSuggest = (TextView) root.findViewById(R.id.tv_search_suggest);
		searchListLayout = (RelativeLayout) root.findViewById(R.id.search_list_layout);
		tvNoMatch = (TextView) root.findViewById(R.id.tv_search_noMatch);

		progressLayout = (RelativeLayout) root.findViewById(R.id.progressbar_layout);
		loadingLayout = (RelativeLayout) root.findViewById(R.id.loading_bar);

		modeImageView = (ImageView) root.findViewById(R.id.iv_search_list_mode);
		modeImageView.setOnClickListener(this);
		scrollTopImageView = (ImageView) root.findViewById(R.id.iv_scroll_top);
		scrollTopImageView.setImageDrawable(Utils.setImageButtonState(R.drawable.ic_home_top_press,
				R.drawable.ic_home_top, getActivity()));
		scrollTopImageView.setOnClickListener(this);
		return root;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		init();
	}

	private void init()
	{
		selectedGroup = new ProductGroup();
		selectedGroup.groupId = "";
		selectedGroup.groupName = "All";

		titleNameText.setText(R.string.product_list_title);

		if (!showTitle)
		{
			titleLayout.setVisibility(View.GONE);
		}
		searchListLayout.setBackgroundColor(Color.parseColor(getString(R.color.color_f4f4f4)));
		if (mode == MODE_GRID)
		{
			initPullToGridView();
		}
		else if (mode == MODE_LIST)
		{
			initPullToListView();
		}
		dragLayout.changeMode(mode);
		scrollTopImageView.setVisibility(View.INVISIBLE);

		siftMap = new HashMap<String, String>();
		siftMap.put("keyword", "");
		RequestCenter.getCompanyGroup(productGroupListener, companyId);
	}

	private void startSearchProducts(String keyword)
	{
		RequestCenter.searchProducts(searchProductsListener, keyword, "", companyId, GET_REAL_IMAGE,
				String.valueOf(pageIndex), String.valueOf(pageSize), "0", "0");
	}

	private DisposeDataListener productGroupListener = new DisposeDataListener()
	{
		@Override
		public void onSuccess(Object obj)
		{
			if (getActivity() == null || !isAdded())
			{
				return;
			}
			groups = (ProductGroups) obj;
			startSearchProducts(siftMap.get("keyword"));
		}

		@Override
		public void onFailure(Object failedReason)
		{
			if (getActivity() == null || !isAdded())
			{
				return;
			}
			ToastUtil.toast(getActivity(), failedReason);
			shake();
		}
	};

	private DisposeDataListener searchProductsListener = new DisposeDataListener()
	{
		@Override
		public void onSuccess(Object arg0)
		{
			if (getActivity() == null || !isAdded())
			{
				return;
			}
			shake();
			searchListLayout.setVisibility(View.VISIBLE);
			scrollTopImageView.setVisibility(View.VISIBLE);
			modeImageView.setVisibility(View.VISIBLE);
			modeImageView.setImageResource(mode == MODE_GRID ? R.drawable.btn_search_list_mode_list
					: R.drawable.btn_search_list_mode_grid);
			refreshComplete();
			products = (SearchProducts) arg0;
			if (products.content != null && products.content.size() > 0)
			{
				changeProgressView(isRefresh);
				if (!isLoadMore && !getActivity().isFinishing() && !isPauseState)
				{
					showSearchMessage(products.resultNum);
				}
				tvNoMatch.setVisibility(View.GONE);
				setPullToViewVisibility(View.VISIBLE);
				if (adapter == null || isRefresh)
				{
					if (MODE_GRID == mode)
					{
						adapter = new ProductGridAdapter(getActivity(), products.content, false);
						searchGridView.setAdapter(adapter);
					}
					else
					{
						adapter = new ProductListAdapter(getActivity(), products.content, false);
						searchListView.setAdapter(adapter);
					}
				}
				else
				{
					adapter.addProducts(products.content);
				}
			}
			else
			{
				changeProgressView(isRefresh);
				if (!isLoadMore && !getActivity().isFinishing() && !isPauseState)
				{
					showSearchMessage("0");
				}
				if (!isLoadMore)
				{
					// siftFragment.onLoadLocation(new ArrayList<SearchLocation>());
					modeImageView.setVisibility(View.GONE);
					setPullToViewVisibility(View.GONE);
					tvNoMatch.setVisibility(View.VISIBLE);
					tvNoMatch.setText(R.string.no_product_matched);
				}
				else
				{
					tvNoMatch.setVisibility(View.GONE);
					setPullToViewVisibility(View.VISIBLE);
				}
			}
			isRefresh = false;
			isLoadMore = false;
		}

		@Override
		public void onFailure(Object failedReason)
		{
			if (getActivity() == null || !isAdded())
			{
				return;
			}
			shake();
			refreshComplete();
			isRefresh = false;
			isLoadMore = false;
			if (!getActivity().isFinishing())
			{
				ToastUtil.toast(getActivity(), failedReason);
			}
		}
	};

	private OnScrollListener scrollListener = new OnScrollListener()
	{

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState)
		{
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
		{
			scrollTopImageView.setVisibility(firstVisibleItem == 0 ? View.INVISIBLE : View.VISIBLE);
		}
	};

	protected void initPullToListView()
	{
		searchListLayout.removeAllViews();
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.pulltorefresh_listview, null);
		pullToListView = (PullToRefreshListView) view.findViewById(R.id.lv_search_list);
		pullToListView.setOnRefreshListener(initListRefreshListener());
		// 只启动下拉加载更多
		pullToListView.setMode(Mode.PULL_FROM_END);
		pullToListView.setOnScrollListener(scrollListener);
		// 不显示指示器
		pullToListView.setShowIndicator(false);
		searchListView = pullToListView.getRefreshableView();
		searchListView.setId(R.id.drag_layout_listview);
		searchListView.setFastScrollEnabled(true);
		searchListView.setOnItemClickListener(this);
		searchListLayout.addView(view);
	}

	protected void initPullToGridView()
	{
		searchListLayout.removeAllViews();
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.pulltorefresh_gridview, null);
		pullToGridView = (PullToRefreshGridView) view.findViewById(R.id.gv_search_list);
		pullToGridView.setOnRefreshListener(initGridRefreshListener());
		// 只启动下拉加载更多
		pullToGridView.setMode(Mode.PULL_FROM_END);
		pullToGridView.setOnScrollListener(scrollListener);
		pullToGridView.setShowIndicator(false);
		// Show grid item's border
		pullToGridView.setShowGridLine(true);
		pullToGridView.setShowAllGridLine(true);
		LayoutParams params = (LayoutParams) pullToGridView.getLayoutParams();
		final int space = 20;
		params.setMargins(space, space, space, space);
		pullToGridView.setLayoutParams(params);

		searchGridView = pullToGridView.getRefreshableView();
		searchGridView.setId(R.id.drag_layout_gridview);
		searchGridView.setFastScrollEnabled(true);
		searchGridView.setHorizontalSpacing(space);
		searchGridView.setVerticalSpacing(space);
		searchGridView.setOnItemClickListener(this);
		searchListLayout.addView(view);
	}

	private OnRefreshListener2<ListView> initListRefreshListener()
	{
		OnRefreshListener2<ListView> refreshListener = new OnRefreshListener2<ListView>()
		{
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView)
			{

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView)
			{
				onPullUpToRefreshView(refreshView);
			}
		};
		return refreshListener;
	}

	private OnRefreshListener2<GridView> initGridRefreshListener()
	{
		OnRefreshListener2<GridView> refreshListener = new OnRefreshListener2<GridView>()
		{
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView)
			{

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView)
			{
				onPullUpToRefreshView(refreshView);
			}
		};
		return refreshListener;
	}

	protected void onPullUpToRefreshView(PullToRefreshBase<?> refreshView)
	{
		isLoadMore = true;
		pageIndex++;
		if (siftMap.containsKey("groupId"))
		{
			startGetGroupedProduct(siftMap.get("groupId"), siftMap.get("groupLevel"));
		}
		else
		{
			startSearchProducts(siftMap.get("keyword"));
		}
	}

	public void showSearchMessage(String numbers)
	{
		if (!showToast)
		{
			return;
		}
		productsString = "<font color=\"#ffffff\">" + getResources().getString(R.string.mic_products) + "</font>";
		String allString = "<font color=\"#ffffff\">" + getResources().getString(R.string.search_hint_title)
				+ "</font>";
		if (numbers != null && !numbers.equals(""))
		{
			String number = "<font color=\"#ffffff\">" + " " + Util.getNumKb(Long.parseLong(numbers)) + " " + "</font>";
			View viewGroup = LayoutInflater.from(getActivity()).inflate(R.layout.show_search_msg, null);
			TextView tv = (TextView) viewGroup.findViewById(R.id.show_msg);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Utils.getWindowWidthPix(getActivity()),
					Util.dip2px(36));
			tv.setLayoutParams(params);
			tv.setGravity(Gravity.CENTER_VERTICAL);
			tv.setText(Html.fromHtml(allString + number + productsString));
			Toast toast = new Toast(getActivity());
			toast.setView(viewGroup);
			toast.setGravity(Gravity.LEFT | Gravity.TOP, 0, Util.dip2px(47f));
			toast.show();
		}
	}

	protected void refreshComplete()
	{
		if (pullToListView != null && pullToListView.isRefreshing())
		{
			pullToListView.onRefreshComplete();
		}
		if (pullToGridView != null && pullToGridView.isRefreshing())
		{
			pullToGridView.onRefreshComplete();
		}
	}

	protected void changeProgressView(boolean ifrefresh)
	{
		progressLayout.setVisibility(View.GONE);
		if (isRefresh)
		{
			loadingLayout.setVisibility(View.GONE);
		}
	}

	protected void setPullToViewVisibility(int visibility)
	{
		if (mode == MODE_GRID)
		{
			pullToGridView.setVisibility(visibility);
		}
		else
		{
			pullToListView.setVisibility(visibility);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
	{
		SearchProduct product = (SearchProduct) adapter.getItem(position);
		if (product != null)
		{
			// 事件统计 112 查看产品详情页（展示厅产品列表） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c112);

			Intent i = new Intent(getActivity(), ProductMessageActivity_.class);
			i.putExtra("productId", product.productId);
			startActivity(i);
		}
	}

	@Override
	public void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		// 事件统计$10027 产品列表页 页面事件
		SysManager.analysis(R.string.a_type_page, R.string.p10027);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.common_title_back_button:
			if (getActivity() instanceof ShowRoomActivity)
			{
				ShowRoomActivity activity = (ShowRoomActivity) getActivity();
				activity.changeToNormal();
			}
			break;
		case R.id.iv_search_list_mode:
			changeMode();
			break;
		case R.id.common_title_right_button2:

			// 事件统计 110 点击产品列表搜索（展示厅） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c110);

			// product search activity
			startProductSearchActivity();
			break;
		case R.id.common_title_right_button3:
			// 事件统计 111 查看产品列表分类（展示厅） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c111);

			// refine
			startProductRefineActivity();
			break;
		case R.id.iv_scroll_top:
			if (mode == MODE_GRID)
			{
				Util.dispatchCancelEvent(searchGridView);
				searchGridView.setSelection(0);
			}
			else
			{
				Util.dispatchCancelEvent(searchListView);
				searchListView.setSelection(0);
			}
			break;
		}
	}

	private void startProductRefineActivity()
	{
		// 去筛选
		Intent i = new Intent(getActivity(), ProductRefineActivity_.class);
		i.putExtra("companyId", companyId);
		i.putExtra("memberType", memberType);
		if (groups != null)
		{
			i.putParcelableArrayListExtra("productGroup", groups.content);
		}
		if (selectedGroup != null)
		{
			i.putExtra("selectedGroup", selectedGroup);
		}
		startActivityForResult(i, Constants.RRFINE_PRODUCT);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == Constants.RRFINE_PRODUCT && resultCode == Activity.RESULT_OK)
		{
			siftMap = (HashMap<String, String>) data.getSerializableExtra("siftMap");
			selectedGroup = data.getParcelableExtra("selectedGroup");
			isRefresh = true;
			pageIndex = 1;
			if (TextUtils.isEmpty(siftMap.get("groupId")))
			{
				siftMap.clear();
				siftMap.put("keyword", "");
				startSearchProducts(siftMap.get("keyword"));
			}
			else
			{
				startGetGroupedProduct(siftMap.get("groupId"), siftMap.get("groupLevel"));
			}
		}

	}

	private void startGetGroupedProduct(String groupId, String groupLevel)
	{
		RequestCenter.getGroupedProducts(searchProductsListener, companyId, groupId, groupLevel,
				String.valueOf(pageIndex), String.valueOf(pageSize), GET_REAL_IMAGE);
	}

	private void startProductSearchActivity()
	{
		Intent i = new Intent(getActivity(), ProductSearchActivity_.class);
		i.putExtra("companyId", companyId);
		startActivity(i);
	}

	private void changeMode()
	{
		if (mode == MODE_GRID)
		{
			mode = MODE_LIST;
		}
		else
		{
			mode = MODE_GRID;
		}

		SharedPreferenceManager.getInstance().putBoolean("isGridMode", mode == MODE_GRID);
		updateChangedModeUI();
		dragLayout.changeMode(mode);
	}

	private void updateChangedModeUI()
	{
		if (mode == MODE_LIST)
		{
			modeImageView.setImageResource(R.drawable.btn_search_list_mode_grid);
			initPullToListView();
			ArrayList<SearchProduct> dataList = adapter.getAdapterData();
			adapter = new ProductListAdapter(getActivity(), dataList, false);
			searchListView.setAdapter(adapter);
		}
		else
		{
			modeImageView.setImageResource(R.drawable.btn_search_list_mode_list);
			initPullToGridView();
			ArrayList<SearchProduct> dataList = adapter.getAdapterData();
			adapter = new ProductGridAdapter(getActivity(), dataList, false);
			searchGridView.setAdapter(adapter);
		}
		adapter.addSelectItem(searchListRecoders);
	}

	public void showTitle(boolean show)
	{
		this.showTitle = show;
		titleLayout.setVisibility(show ? View.VISIBLE : View.GONE);
	}

	public void changeGridMode()
	{
		if (mode == MODE_LIST)
		{
			mode = MODE_GRID;
			SharedPreferenceManager.getInstance().putBoolean("isGridMode", mode == MODE_GRID);
			updateChangedModeUI();
			dragLayout.changeMode(mode);
		}
		Util.dispatchCancelEvent(searchGridView);
		searchGridView.setSelection(0);
		// searchGridView.smoothScrollToPosition(0);
	}

}

package com.micen.buyers.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.micen.buyers.activity.R;
import com.micen.buyers.adapter.PriceTableAdapter;
import com.micen.buyers.module.product.ProductKeyValuePair;
import com.micen.buyers.util.Util;

/**********************************************************
 * @文件名称：MyTableLayout.java
 * @文件作者：renzhiqiang
 * @创建时间：2014年9月9日 下午3:58:54
 * @文件描述：自定义表格类
 * @修改历史：2014年9月9日创建初始版本
 **********************************************************/
public class NormalTableLayout extends TableLayout
{
	/**
	 * 布局中的控件
	 */
	private LinearLayout viewGroup;
	private View tableTitleDivider;
	private TextView tableTitle;
	public TableLayout tableContent;
	private View tableFooter;
	private Context context;

	/**
	 * 控件高度，宽度
	 */
	private static final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
	private static final int FP = ViewGroup.LayoutParams.MATCH_PARENT;

	/**
	 * 绘表数据属性, 数据单位都是dp
	 */
	private ArrayList<ProductKeyValuePair> list;
	private LinkedHashMap<String, ArrayList<ProductKeyValuePair>> map;
	private HashMap<String, Object> styleList;
	private ArrayList<String> importantList;
	private String title;
	private boolean important;
	private boolean hasFilter;
	private int leftTableWidth;
	private int rightTableWidth;
	private int dividerHeight;
	private PriceTableAdapter adapter;

	/**
	 * 绘表样式属性
	 * @param context
	 */
	private int dividerColor;
	private int forwardColor;
	private int leftTextColor;
	private int rightTextColor;
	private int importantColor;
	private int leftTextSize;
	private int rightTextSize;
	private int paddingTop;
	private int paddingBottom;
	private int leftGravity;
	private int rightGravity;
	private Drawable dividerDrawable;

	/**
	 * 绘标题样式属性
	 * @param context
	 */
	private int titleHeight;
	private int titleWeight;
	private int titleBackground;
	private int titlePaddingLeft;
	private int titleTextSize;

	/**
	 * 添加表格点击事件
	 * @param context
	 */
	private OnClickListener clickListener;

	public NormalTableLayout(Context context)
	{
		this(context, null);
	}

	public NormalTableLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initView(context);
		initData();
		initStyle();
	}

	/**
	 * 初始化样式属性,默认样式,不设置就按默认样式定义
	 */
	private void initStyle()
	{
		dividerColor = context.getResources().getColor(R.color.mic_menu_line);
		dividerDrawable = context.getResources().getDrawable(R.color.mic_menu_line);
		forwardColor = context.getResources().getColor(R.color.mic_white);
		leftTextColor = context.getResources().getColor(R.color.color_999999);
		importantColor = context.getResources().getColor(R.color.get_last_price_normal);
		rightTextColor = context.getResources().getColor(R.color.color_333333);
		leftTextSize = rightTextSize = 12;
		paddingBottom = paddingTop = Util.dip2px(12);
		leftGravity = Gravity.RIGHT;
		rightGravity = -1;
		/**
		 * 初始化标题属性
		 */
		titleBackground = context.getResources().getColor(R.color.setting_bg_color);
		titleWeight = FP;
		titleHeight = Util.dip2px(29);
		titleTextSize = 15;
		dividerHeight = Util.dip2px(0.5f);
		titlePaddingLeft = Util.dip2px(15);
	}

	/**
	 * 初始化数据属性
	 */
	private void initData()
	{
		hasFilter = true;
		list = new ArrayList<ProductKeyValuePair>();
		styleList = new HashMap<String, Object>();
		importantList = new ArrayList<String>();
	}

	/**
	 * 初始化表格界面
	 * @param context
	 */
	private void initView(Context context)
	{
		this.context = context;
		viewGroup = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.my_table_layout, null);
		tableTitleDivider = (View) viewGroup.findViewById(R.id.table_title_didiver);
		tableTitle = (TextView) viewGroup.findViewById(R.id.table_title);
		tableContent = (TableLayout) viewGroup.findViewById(R.id.table_content);
		tableFooter = (View) viewGroup.findViewById(R.id.table_footer_divider);
		addView(viewGroup);
	}

	/**
	 * 对外公开初始化数据方法，一次性初始化,仅是满足我的需求
	 * @param pairs
	 * @param title
	 * @param width
	 */
	public void initParams(ArrayList<ProductKeyValuePair> pairs, String title, int leftTableWidth)
	{
		this.list = pairs;
		this.title = title;
		this.leftTableWidth = leftTableWidth;
		this.rightTableWidth = leftTableWidth * 2;
	}

	public void initParams(LinkedHashMap<String, ArrayList<ProductKeyValuePair>> map, String title, int leftTableWidth)
	{
		this.map = map;
		this.title = title;
		this.leftTableWidth = leftTableWidth;
		this.rightTableWidth = leftTableWidth * 2;
	}

	/**
	 * 一个一个初始化
	 * @return
	 */
	public ArrayList<ProductKeyValuePair> getList()
	{
		return list;
	}

	public void setList(ArrayList<ProductKeyValuePair> list)
	{
		this.list = list;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * 向数据列表中添加数据
	 * @param collection
	 */
	public void addList(ArrayList<ProductKeyValuePair> collection)
	{
		if (this.list != null)
		{
			if (collection != null && collection.size() > 0)
			{
				this.list.addAll(collection);
			}
		}
	}

	/**
	 * 是否是需要特别标注的表格
	 * @return
	 */
	public ArrayList<String> getImportantList()
	{
		return importantList;
	}

	public void setImportantList(ArrayList<String> importantList)
	{
		this.importantList = importantList;
	}

	/**
	 * 向数据列表中添加一组重要数据
	 * @param collection
	 */
	public void addImportantList(ArrayList<String> collection)
	{
		if (this.importantList != null)
		{
			if (collection != null && collection.size() > 0)
			{
				this.importantList.addAll(collection);
			}
		}
	}

	/**
	 * 向数据列表中添加一项重要数据
	 * @param collection
	 */
	public void addImportant(String importantItem)
	{
		if (this.importantList != null)
		{
			this.importantList.add(importantItem);
		}
	}

	public void setDividerColor(int dividerColor)
	{
		this.dividerColor = dividerColor;
	}

	public void setForwardColor(int forwardColor)
	{
		this.forwardColor = forwardColor;
	}

	public void setLeftTextColor(int leftTextColor)
	{
		this.leftTextColor = leftTextColor;
	}

	public void setRightTextColor(int rightTextColor)
	{
		this.rightTextColor = rightTextColor;
	}

	/**
	 * 设置重要数据的显示颜色
	 * @param importantColor
	 */
	public void setImportantColor(int importantColor)
	{
		this.importantColor = importantColor;
	}

	public void setLeftTextSize(int leftTextSize)
	{
		this.leftTextSize = leftTextSize;
	}

	public void setRightTextSize(int rightTextSize)
	{
		this.rightTextSize = rightTextSize;
	}

	public void setPaddingTop(int paddingTop)
	{
		this.paddingTop = paddingTop;
	}

	public void setPaddingBottom(int paddingBottom)
	{
		this.paddingBottom = paddingBottom;
	}

	public void setRightTableWidth(int rightTableWidth)
	{
		this.rightTableWidth = rightTableWidth;
	}

	public void setLeftTableWidth(int leftTableWidth)
	{
		this.leftTableWidth = leftTableWidth;
	}

	public void setHasFilter(boolean hasFilter)
	{
		this.hasFilter = hasFilter;
	}

	public void setDividerHeight(int dividerHeight)
	{
		this.dividerHeight = dividerHeight;
	}

	public void setLeftGravity(int leftGravity)
	{
		this.leftGravity = leftGravity;
	}

	public void setRightGravity(int rightGravity)
	{
		this.rightGravity = rightGravity;
	}

	public void setTitleHeight(int titleHeight)
	{
		this.titleHeight = titleHeight;
	}

	public void setTitleWeight(int titleWeight)
	{
		this.titleWeight = titleWeight;
	}

	public void setTitleBackground(int titleBackground)
	{
		this.titleBackground = titleBackground;
	}

	public void setTitlePaddingLeft(int titlePaddingLeft)
	{
		this.titlePaddingLeft = titlePaddingLeft;
	}

	public void setTitleTextSize(int titleTextSize)
	{
		this.titleTextSize = titleTextSize;
	}

	public void setDividerDrawable(Drawable dividerDrawable)
	{
		this.dividerDrawable = dividerDrawable;
	}

	public void setMap(LinkedHashMap<String, ArrayList<ProductKeyValuePair>> map)
	{
		this.map = map;
	}

	/**
	 * 设置点击事件
	 */

	public void setClickListener(OnClickListener clickListener)
	{
		this.clickListener = clickListener;
	}

	/**
	 * 对外公开方法，根据初始化的数据绘制表格
	 */
	public void createOne2OneTable()
	{
		if (title != null && title.length() > 0 && title != "")
		{
			resetTitle();
		}
		else
		{
			hideTitle();
		}

		if (list != null && list.size() > 0)
		{
			for (ProductKeyValuePair pair : list)
			{
				if (pair.value.toString() != null && pair.value.toString().trim().length() > 0)
				{
					if (importantList.contains(pair.key))
					{
						important = true;
					}
					else
					{
						important = false;
					}
					tableContent.addView(createTableItem(pair.key + (hasFilter ? ":" : ""), pair.value.toString()));
				}
				else
				{
					continue;
				}
			}
		}
		else
		{
			/**
			 * 无数据则表格标题也隐藏
			 */
			tableFooter.setVisibility(View.GONE);
			hideTitle();
		}
	}

	/**
	 * 按照标题属性来设置标题样式
	 */
	private void resetTitle()
	{
		tableTitle.setText(title);
		tableTitle.setBackgroundColor(titleBackground);
		LinearLayout.LayoutParams title_params = new LinearLayout.LayoutParams(titleWeight, titleHeight);
		tableTitle.setLayoutParams(title_params);
		tableTitle.setTextSize(titleTextSize);
		LinearLayout.LayoutParams title_divider_params = new LinearLayout.LayoutParams(titleWeight, dividerHeight);
		tableTitleDivider.setLayoutParams(title_divider_params);
		tableTitle.setPadding(titlePaddingLeft, 0, 0, 0);
	}

	/**
	 * 隐藏表格标题
	 */
	public void hideTitle()
	{
		tableTitle.setVisibility(View.GONE);
		tableTitleDivider.setVisibility(View.GONE);
	}

	/**
	 * 显示表格标题
	 */
	public void showTitle()
	{
		tableTitle.setVisibility(View.VISIBLE);
		tableTitleDivider.setVisibility(View.VISIBLE);
	}

	/**
	 * 删除旧的表格数据,防止数据叠加问题
	 */
	public void removeOldData()
	{
		if (tableContent.getChildCount() > 0)
		{
			tableContent.removeAllViews();
		}
	}

	/**
	 * 表格布局，创建表格每一项
	 * @param text1
	 * @param text2
	 * @return
	 */
	private LinearLayout createTableItem(String text1, String text2)
	{
		LinearLayout layout_outer = new LinearLayout(context);
		/**
		 * 设置表格边框线
		 */
		layout_outer.setBackgroundColor(dividerColor);
		layout_outer.setPadding(0, dividerHeight, dividerHeight, 0);
		LinearLayout layout_inner = new LinearLayout(context);
		layout_inner.setBackgroundColor(forwardColor);
		layout_inner.setOrientation(LinearLayout.HORIZONTAL);

		TextView tv1 = new TextView(context);
		LinearLayout.LayoutParams tv_params = new LinearLayout.LayoutParams(leftTableWidth, WC);
		tv1.setText(Html.fromHtml(text1));
		tv1.setLayoutParams(tv_params);
		tv1.setTextSize(leftTextSize);
		tv1.setTextColor(leftTextColor);
		tv1.setGravity(leftGravity);
		tv1.setPadding(Util.dip2px(9), paddingTop, Util.dip2px(9), paddingBottom);

		ImageView view = new ImageView(context);
		LinearLayout.LayoutParams view_params = new LinearLayout.LayoutParams(dividerHeight, FP);
		view.setLayoutParams(view_params);
		view.setBackgroundColor(dividerColor);

		TextView tv2 = new TextView(context);
		LinearLayout.LayoutParams tv_params_2 = new LinearLayout.LayoutParams(rightTableWidth, WC);
		tv2.setText(Html.fromHtml(Util.replaceHtmlStr(text2)));
		tv2.setLayoutParams(tv_params_2);
		tv2.setTextSize(rightTextSize);
		tv2.setTextColor(important ? importantColor : rightTextColor);
		if (rightGravity != -1)
		{
			tv2.setGravity(rightGravity);
		}
		if (important)
		{
			tv2.setOnClickListener(clickListener);
		}
		tv2.setPadding(Util.dip2px(9), paddingTop, Util.dip2px(15), paddingBottom);

		layout_inner.addView(tv1);
		layout_inner.addView(view);
		layout_inner.addView(tv2);
		layout_outer.addView(layout_inner);

		return layout_outer;
	}

	/**
	 * 创建一对多表格
	 */
	@SuppressWarnings(
	{ "rawtypes", "unchecked" })
	public void createOne2ManyTable()
	{
		if (title != null && title.length() > 0 && title != "")
		{
			resetTitle();
		}
		else
		{
			hideTitle();
		}
		if (map != null && map.size() > 0)
		{
			Iterator iter = map.entrySet().iterator();
			while (iter.hasNext())
			{
				Map.Entry entry = (Map.Entry) iter.next();
				tableContent.addView(createTableItem(entry.getKey().toString(),
						(ArrayList<ProductKeyValuePair>) entry.getValue()));
			}
		}
		else
		{
			/**
			* 无数据则表格标题也隐藏
			*/
			hideTitle();
		}
		tableFooter.setVisibility(View.GONE);
	}

	/**
	 * 利用ListView创建嵌套表格的每一项
	 */
	public LinearLayout createTableItem(String text1, ArrayList<ProductKeyValuePair> tempList)
	{

		LinearLayout layout_outer = new LinearLayout(context);
		/**
		 * 设置表格边框线
		 */
		layout_outer.setBackgroundColor(dividerColor);
		layout_outer.setPadding(0, dividerHeight, 0, 0);
		LinearLayout layout_inner = new LinearLayout(context);
		layout_inner.setBackgroundColor(forwardColor);
		layout_inner.setOrientation(LinearLayout.HORIZONTAL);

		TextView tv1 = new TextView(context);
		LinearLayout.LayoutParams tv_params = new LinearLayout.LayoutParams(leftTableWidth, WC);
		tv1.setText(text1);
		tv1.setLayoutParams(tv_params);
		tv1.setTextSize(leftTextSize);
		tv1.setTextColor(leftTextColor);
		tv1.setGravity(leftGravity);
		tv1.setPadding(0, paddingTop, Util.dip2px(9), 0);

		ImageView view = new ImageView(context);
		LinearLayout.LayoutParams view_params = new LinearLayout.LayoutParams(dividerHeight, FP);
		view.setLayoutParams(view_params);
		view.setBackgroundColor(dividerColor);

		// 添加左侧价格列表..
		ProductDetailListView listView = new ProductDetailListView(context);
		LinearLayout.LayoutParams list_params = new LinearLayout.LayoutParams(rightTableWidth, WC);
		listView.setLayoutParams(list_params);
		listView.setClickable(false);
		listView.setCacheColorHint(R.color.transparent);
		listView.setSelector(R.color.transparent);
		listView.setDivider(dividerDrawable);
		listView.setDividerHeight(dividerHeight);
		sendParamsToAdapter();
		adapter = new PriceTableAdapter(context, tempList, styleList);
		listView.setAdapter(adapter);
		Util.setListViewHeightBasedOnChildren(listView);

		layout_inner.addView(tv1);
		layout_inner.addView(view);
		layout_inner.addView(listView);
		layout_outer.addView(layout_inner);
		return layout_outer;
	}

	/**
	 * 将用户设置的样式属性传到Adapter中
	 */
	private void sendParamsToAdapter()
	{
		styleList.put("rightTextColor", rightTextColor);
		styleList.put("dividerColor", dividerColor);
		styleList.put("rightTextSize", rightTextSize);
	}
}

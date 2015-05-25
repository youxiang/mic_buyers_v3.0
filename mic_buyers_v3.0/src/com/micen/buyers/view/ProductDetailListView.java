package com.micen.buyers.view;

import android.content.Context;
import android.widget.ListView;

/**********************************************************
 * @文件名称：ProductDetailListView.java
 * @文件作者：renzhiqiang
 * @创建时间：2014年9月4日 下午2:26:13
 * @文件描述：用于ScrollView中嵌套的ListView高度计算
 * @修改历史：2014年9月4日创建初始版本
 **********************************************************/
public class ProductDetailListView extends ListView
{
	public ProductDetailListView(Context context)
	{
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}

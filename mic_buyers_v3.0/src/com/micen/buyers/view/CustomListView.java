package com.micen.buyers.view;

import android.content.Context;
import android.widget.ListView;

/**********************************************************
 * @�ļ����ƣ�CustomListView.java
 * @�ļ����ߣ�xiongjiangwei
 * @����ʱ�䣺2014-2-25 ����11:05:48
 * @�ļ��������Զ���ListView��������ScrollView��ͻ
 * @�޸���ʷ��2014-2-25������ʼ�汾
 **********************************************************/
public class CustomListView extends ListView
{

	public CustomListView(Context context)
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

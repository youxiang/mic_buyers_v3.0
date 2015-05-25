package com.focustech.common.widget.associatemail;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.MultiAutoCompleteTextView;

/**********************************************************
 * @文件名称：MailBoxAssociateView.java
 * @文件作者：xiongjiangwei
 * @创建时间：2014年8月7日 下午3:05:13
 * @文件描述：自定义邮箱联想控件
 * @修改历史：2014年8月7日创建初始版本
 **********************************************************/
public class MailBoxAssociateView extends MultiAutoCompleteTextView
{
	public MailBoxAssociateView(Context context)
	{
		super(context);
	}

	public MailBoxAssociateView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public MailBoxAssociateView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	public boolean enoughToFilter()
	{
		// 如果字符中包含'@'并且不在第一位，则满足条件
		return getText().toString().contains("@") && getText().toString().indexOf("@") > 0;
	}
}

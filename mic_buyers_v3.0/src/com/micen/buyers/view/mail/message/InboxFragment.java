package com.micen.buyers.view.mail.message;

import org.androidannotations.annotations.EFragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.micen.buyers.activity.R;
import com.micen.buyers.activity.message.MailDetailActivity_;
import com.micen.buyers.manager.SysManager;

/**********************************************************
 * @文件名称：InboxFragment.java
 * @文件作者：xiongjiangwei
 * @创建时间：2014年7月10日 下午3:34:27
 * @文件描述：收件箱列表Fragment
 * @修改历史：2014年7月10日创建初始版本
 **********************************************************/
@EFragment(R.layout.mail_list)
public class InboxFragment extends MailBaseFragment
{
	private static final String ACTION = "0";

	public InboxFragment()
	{

	}

	@Override
	protected String getChildTag()
	{
		return InboxFragment.class.getName();
	}

	@Override
	protected String getAction()
	{
		return ACTION;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		// TODO Auto-generated method stub
		// 事件统计 39 查看询盘详情（VO） 点击事件
		SysManager.analysis(R.string.a_type_click, R.string.c39);
		Intent intent = new Intent(getActivity(), MailDetailActivity_.class);
		intent.putExtra("position", String.valueOf(arg2));
		intent.putExtra("action", getAction());
		getActivity().startActivity(intent);
	}

}

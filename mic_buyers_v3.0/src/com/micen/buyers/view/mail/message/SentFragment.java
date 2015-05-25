package com.micen.buyers.view.mail.message;

import org.androidannotations.annotations.EFragment;

import com.micen.buyers.activity.R;

/**********************************************************
 * @文件名称：SentFragment.java
 * @文件作者：xiongjiangwei
 * @创建时间：2014年7月10日 下午3:34:37
 * @文件描述：发件箱列表Fragment
 * @修改历史：2014年7月10日创建初始版本
 **********************************************************/
@EFragment(R.layout.mail_list)
public class SentFragment extends MailBaseFragment
{
	private static final String ACTION = "1";

	public SentFragment()
	{

	}

	@Override
	protected String getChildTag()
	{
		return SentFragment.class.getName();
	}

	@Override
	protected String getAction()
	{
		return ACTION;
	}

}

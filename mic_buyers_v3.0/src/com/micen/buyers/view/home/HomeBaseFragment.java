package com.micen.buyers.view.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

/**********************************************************
 * @文件名称：HomeBaseFragment.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年5月7日 下午6:51:17
 * @文件描述：首页父Fragment
 * @修改历史：2015年5月7日创建初始版本
 **********************************************************/
public abstract class HomeBaseFragment extends Fragment
{
	public HomeBaseFragment()
	{

	}

	public abstract String getChildTag();

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Log.e("========" + getChildTag() + "============", "=======onCreate=======");
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		Log.e("========" + getChildTag() + "============", "=======onDestroy=======");
	}

}

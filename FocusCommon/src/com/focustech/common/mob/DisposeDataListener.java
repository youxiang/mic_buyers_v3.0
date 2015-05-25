package com.focustech.common.mob;

/**********************************************************
 * @文件名称：DisposeDataListener.java
 * @文件作者：xiongjiangwei
 * @创建时间：2014年5月16日 下午3:24:37
 * @文件描述：数据处理回调
 * @修改历史：2014年5月16日创建初始版本
 **********************************************************/
public interface DisposeDataListener
{
	public void onSuccess(Object obj);

	public void onFailure(Object failedReason);
}

package com.focustech.common.mob.update;

/**********************************************************
 * @文件名称：UpdateListener.java
 * @文件作者：xiongjiangwei
 * @创建时间：2014年10月23日 下午1:40:17
 * @文件描述：检查更新回调
 * @修改历史：2014年10月23日创建初始版本
 **********************************************************/
public interface UpdateListener
{
	public void onSuccess(boolean isNewVersion, boolean isForceUpdate, Update update);

	public void onFailure();
}

package com.micen.buyers.listener;

/**********************************************************
 * @文件名称：CheckSendButtonStatus.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年4月14日 下午2:22:55
 * @文件描述：检查发送按钮是否有效的接口
 * @修改历史：2015年4月14日创建初始版本
 **********************************************************/
public interface CheckSendButtonStatus
{
	public void onStatusChanged(boolean isEnabled);
}

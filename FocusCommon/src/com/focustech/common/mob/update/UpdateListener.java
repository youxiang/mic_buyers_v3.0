package com.focustech.common.mob.update;

/**********************************************************
 * @�ļ����ƣ�UpdateListener.java
 * @�ļ����ߣ�xiongjiangwei
 * @����ʱ�䣺2014��10��23�� ����1:40:17
 * @�ļ������������»ص�
 * @�޸���ʷ��2014��10��23�մ�����ʼ�汾
 **********************************************************/
public interface UpdateListener
{
	public void onSuccess(boolean isNewVersion, boolean isForceUpdate, Update update);

	public void onFailure();
}

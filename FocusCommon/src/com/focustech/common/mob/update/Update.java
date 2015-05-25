package com.focustech.common.mob.update;

/**********************************************************
 * @�ļ����ƣ�Update.java
 * @�ļ����ߣ�xiongjiangwei
 * @����ʱ�䣺2014��10��23�� ����1:38:05
 * @�ļ�����������������ģ��
 * @�޸���ʷ��2014��10��23�մ�����ʼ�汾
 **********************************************************/
public class Update
{
	/**
	 * �Ƿ��и���
	 */
	public String updateOrNot;
	/**
	 * ���°汾��
	 */
	public String versionInfo;
	/**
	 * ���°汾����
	 */
	public String remarksUpdate;
	/**
	 * ��װ����С
	 */
	public String contentLength;
	/**
	 * ��װ�����ص�ַ
	 */
	public String upgradeUrl;
	/**
	 * ǿ�Ƹ��±�ʾ(1:ǿ�ƣ�2:��ǿ��)
	 */
	public String maxType;

	public boolean isNewVersion()
	{
		return "true".equals(updateOrNot);
	}

	public boolean isForceUpdate()
	{
		return "1".equals(maxType);
	}
}

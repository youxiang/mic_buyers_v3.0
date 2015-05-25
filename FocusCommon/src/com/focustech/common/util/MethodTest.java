package com.focustech.common.util;

import android.util.Log;

/**********************************************************
 * @�ļ����ƣ�MethodTest.java
 * @�ļ����ߣ�xiongjiangwei
 * @����ʱ�䣺2013-10-28 ����11:00:59
 * @�ļ�������ͳ�Ʒ���ִ�������ĵ�ʱ����ڴ棻ע�����ڲ�������ڲ��ԣ��������Ժ���ע��
 * @ʹ��ʾ��: ��Ҫ�ڷ�����ʼǰ����start(methodName)������ִ����ɺ����end()���� ����
 *       Util.MethodTest.start("my test is 'setSetting' method");
 *       setSetting(); Util.MethodTest.end();
 * @�޸���ʷ��2013-10-28������ʼ�汾
 **********************************************************/
public class MethodTest
{
	private static final String MOMORY_UNIT = "bytes";
	private static final String TIME_UNIT = "ms";
	private static final String TAG = "MethodTest";
	private static long start;
	private static long beginUsedMomery;
	private static long endUsedMomery;
	private static long end;
	private static String methodName_;// �������� ���ڲ��Դ�ӡ

	public static void start(String methodName)
	{
		methodName_ = methodName;
		Log.i(TAG, "begin method " + methodName);
		start = System.currentTimeMillis();
		beginUsedMomery = momeryUsed();
		Log.i(TAG, "use momory begin method =======" + beginUsedMomery + MOMORY_UNIT);
	}

	public static void end()
	{
		endUsedMomery = momeryUsed();
		end = System.currentTimeMillis();
		Log.i(TAG, "use momory after method =======" + endUsedMomery + MOMORY_UNIT);
		Log.i(TAG, "the method " + methodName_ + " lasts======= " + (end - start) + TIME_UNIT);
		Log.i(TAG, "the method " + methodName_ + " momory increased by ========= " + (endUsedMomery - beginUsedMomery)
				+ MOMORY_UNIT);
		Log.i(TAG, "end method " + methodName_);
		Log.i(TAG, Runtime.getRuntime().totalMemory() + "");
		Log.i(TAG, "    ");
	}

	public static long momeryUsed()
	{
		return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
	}
}
package com.focustech.common.util;

import android.util.Log;

/**********************************************************
 * @文件名称：MethodTest.java
 * @文件作者：xiongjiangwei
 * @创建时间：2013-10-28 上午11:00:59
 * @文件描述：统计方法执行所消耗的时间和内存；注：此内部类仅用于测试！！！测试后请注释
 * @使用示例: 需要在方法开始前调用start(methodName)方法，执行完成后调用end()方法 例：
 *       Util.MethodTest.start("my test is 'setSetting' method");
 *       setSetting(); Util.MethodTest.end();
 * @修改历史：2013-10-28创建初始版本
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
	private static String methodName_;// 方法名称 用于测试打印

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
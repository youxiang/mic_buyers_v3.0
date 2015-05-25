package com.focustech.common.util;

import android.graphics.BitmapFactory;

/**********************************************************
 * @文件名称：BitmapOptionUtils.java
 * @文件作者：xiongjiangwei
 * @创建时间：2014年8月19日 下午2:50:07
 * @文件描述：图片工具类
 * @修改历史：2014年8月19日创建初始版本
 **********************************************************/
public class BitmapOptionUtils
{
	/**
	 * 计算缩放比
	 * @param options
	 * @param minSideLength
	 * @param maxNumOfPixels
	 * @return
	 */
	public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels)
	{
		int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8)
		{
			roundedSize = 1;
			while (roundedSize < initialSize)
			{
				roundedSize <<= 1;
			}
		}
		else
		{
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels)
	{
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 512 : (int) Math.min(Math.floor(w / minSideLength),
				Math.floor(h / minSideLength));
		if (upperBound < lowerBound)
		{
			return lowerBound;
		}
		if ((maxNumOfPixels == -1) && (minSideLength == -1))
		{
			return 1;
		}
		else if (minSideLength == -1)
		{
			return lowerBound;
		}
		else
		{
			return upperBound;
		}
	}
}

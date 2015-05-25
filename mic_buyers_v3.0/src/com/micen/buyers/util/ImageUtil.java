package com.micen.buyers.util;

import java.io.InputStream;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.micen.buyers.activity.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**********************************************************
 * @文件名称：ImageUtil.java
 * @文件作者：xiongjiangwei
 * @创建时间：2013-4-23 上午11:17:14
 * @文件描述：网络图片加载工具类
 * @修改历史：2013-4-23创建初始版本
 **********************************************************/
public class ImageUtil
{
	private static ImageLoader imageLoader;

	public static Bitmap drawableToBitmap(Drawable drawable)
	{
		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
				drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;

	}

	public static DisplayImageOptions getImageOptions()
	{
		DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.ic_launcher)
		// .showImageForEmptyUri(R.drawable.magazine_load_image_hd)
		// .showImageOnFail(R.drawable.ic_error)
				.cacheInMemory().cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
		return options;
	}

	public static DisplayImageOptions getSimpleImageOptions()
	{
		DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.ic_launcher)
				.cacheInMemory().cacheOnDisc().bitmapConfig(Bitmap.Config.ALPHA_8).build();
		return options;
	}
	
	public static DisplayImageOptions getEasysouringImageOptions()
	{
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.mic_easysourcing_product_loading).cacheInMemory().cacheOnDisc()
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		return options;
	}

	public static DisplayImageOptions getRecommendImageOptions()
	{
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.mic_recommend_product_loading).cacheInMemory().cacheOnDisc()
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		return options;
	}

	public static DisplayImageOptions getSafeImageOptions()
	{
		DisplayImageOptions options = new DisplayImageOptions.Builder().setRequestPropertyKeys(new String[]
		{ "Referer" }).setRequestPropertyValues(new String[]
		{ " http://www.made-in-jiangsu.com" }).showStubImage(R.drawable.mic_recommend_product_loading).cacheInMemory()
				.cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
		return options;
	}

	public static DisplayImageOptions getSafeImageNoStubOptions()
	{
		DisplayImageOptions options = new DisplayImageOptions.Builder().setRequestPropertyKeys(new String[]
		{ "Referer" }).setRequestPropertyValues(new String[]
		{ " http://www.made-in-jiangsu.com" }).cacheInMemory().cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565)
				.build();
		return options;
	}

	public static ImageLoader getImageLoader()
	{
		if (imageLoader == null)
		{
			imageLoader = ImageLoader.getInstance();
		}
		return imageLoader;
	}

	/**
	 * 以最省内存的方式读取本地资源的图片
	 * @param context
	 *@param resId
	 * @return
	 */
	public static Bitmap readBitMap(Context context, int resId)
	{
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.ALPHA_8;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		opt.inSampleSize = 2;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/**
	* Utility function for decoding an image resource. The decoded bitmap will
	* be optimized for further scaling to the requested destination dimensions
	* and scaling logic.
	*
	* @param res The resources object containing the image data
	* @param resId The resource id of the image data
	* @param dstWidth Width of destination area
	* @param dstHeight Height of destination area
	* @param scalingLogic Logic to use to avoid image stretching
	* @return Decoded bitmap
	*/
	public static Bitmap decodeResource(Resources res, int resId, int dstWidth, int dstHeight, ScalingLogic scalingLogic)
	{
		Options options = new Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);
		options.inJustDecodeBounds = false;
		options.inSampleSize = calculateSampleSize(options.outWidth, options.outHeight, dstWidth, dstHeight,
				scalingLogic);
		Bitmap unscaledBitmap = BitmapFactory.decodeResource(res, resId, options);

		return unscaledBitmap;
	}

	/**
	 * Utility function for creating a scaled version of an existing bitmap
	 *
	 * @param unscaledBitmap Bitmap to scale
	 * @param dstWidth Wanted width of destination bitmap
	 * @param dstHeight Wanted height of destination bitmap
	 * @param scalingLogic Logic to use to avoid image stretching
	 * @return New scaled bitmap object
	 */
	public static Bitmap createScaledBitmap(Bitmap unscaledBitmap, int dstWidth, int dstHeight,
			ScalingLogic scalingLogic)
	{
		Rect srcRect = calculateSrcRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(), dstWidth, dstHeight,
				scalingLogic);
		Rect dstRect = calculateDstRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(), dstWidth, dstHeight,
				scalingLogic);
		Bitmap scaledBitmap = Bitmap.createBitmap(dstRect.width(), dstRect.height(), Config.ARGB_8888);
		Canvas canvas = new Canvas(scaledBitmap);
		canvas.drawBitmap(unscaledBitmap, srcRect, dstRect, new Paint(Paint.FILTER_BITMAP_FLAG));

		return scaledBitmap;
	}

	/**
	 * ScalingLogic defines how scaling should be carried out if source and
	 * destination image has different aspect ratio.
	 *
	 * CROP: Scales the image the minimum amount while making sure that at least
	 * one of the two dimensions fit inside the requested destination area.
	 * Parts of the source image will be cropped to realize this.
	 *
	 * FIT: Scales the image the minimum amount while making sure both
	 * dimensions fit inside the requested destination area. The resulting
	 * destination dimensions might be adjusted to a smaller size than
	 * requested.
	 */
	public static enum ScalingLogic
	{
		CROP, FIT
	}

	/**
	 * Calculate optimal down-sampling factor given the dimensions of a source
	 * image, the dimensions of a destination area and a scaling logic.
	 *
	 * @param srcWidth Width of source image
	 * @param srcHeight Height of source image
	 * @param dstWidth Width of destination area
	 * @param dstHeight Height of destination area
	 * @param scalingLogic Logic to use to avoid image stretching
	 * @return Optimal down scaling sample size for decoding
	 */
	public static int calculateSampleSize(int srcWidth, int srcHeight, int dstWidth, int dstHeight,
			ScalingLogic scalingLogic)
	{
		if (scalingLogic == ScalingLogic.FIT)
		{
			final float srcAspect = (float) srcWidth / (float) srcHeight;
			final float dstAspect = (float) dstWidth / (float) dstHeight;

			if (srcAspect > dstAspect)
			{
				return srcWidth / dstWidth;
			}
			else
			{
				return srcHeight / dstHeight;
			}
		}
		else
		{
			final float srcAspect = (float) srcWidth / (float) srcHeight;
			final float dstAspect = (float) dstWidth / (float) dstHeight;

			if (srcAspect > dstAspect)
			{
				return srcHeight / dstHeight;
			}
			else
			{
				return srcWidth / dstWidth;
			}
		}
	}

	/**
	 * Calculates source rectangle for scaling bitmap
	 *
	 * @param srcWidth Width of source image
	 * @param srcHeight Height of source image
	 * @param dstWidth Width of destination area
	 * @param dstHeight Height of destination area
	 * @param scalingLogic Logic to use to avoid image stretching
	 * @return Optimal source rectangle
	 */
	public static Rect calculateSrcRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight,
			ScalingLogic scalingLogic)
	{
		if (scalingLogic == ScalingLogic.CROP)
		{
			final float srcAspect = (float) srcWidth / (float) srcHeight;
			final float dstAspect = (float) dstWidth / (float) dstHeight;

			if (srcAspect > dstAspect)
			{
				final int srcRectWidth = (int) (srcHeight * dstAspect);
				final int srcRectLeft = (srcWidth - srcRectWidth) / 2;
				return new Rect(srcRectLeft, 0, srcRectLeft + srcRectWidth, srcHeight);
			}
			else
			{
				final int srcRectHeight = (int) (srcWidth / dstAspect);
				final int scrRectTop = (int) (srcHeight - srcRectHeight) / 2;
				return new Rect(0, scrRectTop, srcWidth, scrRectTop + srcRectHeight);
			}
		}
		else
		{
			return new Rect(0, 0, srcWidth, srcHeight);
		}
	}

	/**
	 * Calculates destination rectangle for scaling bitmap
	 *
	 * @param srcWidth Width of source image
	 * @param srcHeight Height of source image
	 * @param dstWidth Width of destination area
	 * @param dstHeight Height of destination area
	 * @param scalingLogic Logic to use to avoid image stretching
	 * @return Optimal destination rectangle
	 */
	public static Rect calculateDstRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight,
			ScalingLogic scalingLogic)
	{
		if (scalingLogic == ScalingLogic.FIT)
		{
			final float srcAspect = (float) srcWidth / (float) srcHeight;
			final float dstAspect = (float) dstWidth / (float) dstHeight;

			if (srcAspect > dstAspect)
			{
				return new Rect(0, 0, dstWidth, (int) (dstWidth / srcAspect));
			}
			else
			{
				return new Rect(0, 0, (int) (dstHeight * srcAspect), dstHeight);
			}
		}
		else
		{
			return new Rect(0, 0, dstWidth, dstHeight);
		}
	}
}

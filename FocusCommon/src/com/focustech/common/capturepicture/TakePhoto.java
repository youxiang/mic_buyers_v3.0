package com.focustech.common.capturepicture;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

/**********************************************************
 * @文件名称：TakePhoto.java
 * @文件作者：xiongjiangwei
 * @创建时间：2013-11-18 下午02:47:49
 * @文件描述：获取图片基类
 * @修改历史：2013-11-18创建初始版本
 **********************************************************/
public class TakePhoto
{
	protected Fragment mFragment;
	protected Activity mActivity;
	protected Intent mIntent = null;
	/**相机页面回调标示**/
	protected static final int PHOTO_REQUEST_TAKEPHOTO = 0x00000001;
	/**图片裁剪完成回调标示**/
	protected static final int PHOTO_REQUEST_CUT = 0x00000002;
	/**从本机中选取图片**/
	protected static final int PHOTO_REQUEST_GET = 0x00000003;
	/**图片本地文件的Uri**/
	protected Uri currentUri = null;
	/**图片获取成功或失败后的回调**/
	protected TakePhotoListener mTakePhotoListener;
	/**需要显示图像的控件**/
	protected View mDisplayView;
	/**sdcard缓存目录**/
	private String sdcardPath = null;
	/**图片本地存储路径**/
	private String fileBasePath = null;
	private int aspectX = 1;
	private int aspectY = 1;
	private int outputX = 480;
	private int outputY = 480;

	public TakePhoto(Activity activity, View displayView, TakePhotoListener takePhotoListener)
	{
		this.mActivity = activity;
		this.mDisplayView = displayView;
		this.mTakePhotoListener = takePhotoListener;
		initLocalFilePath(mActivity);
		startSelectImage();
	}
	
	public TakePhoto(Fragment fragment, View displayView, TakePhotoListener takePhotoListener)
	{
		this.mFragment = fragment;
		this.mActivity = fragment.getActivity();
		this.mDisplayView = displayView;
		this.mTakePhotoListener = takePhotoListener;
		initLocalFilePath(mActivity);
		startSelectImage();
	}

	public TakePhoto(Activity activity, View displayView, int aspectX, int aspectY, int outputX, int outputY,
			TakePhotoListener takePhotoListener)
	{
		this.mActivity = activity;
		this.mDisplayView = displayView;
		this.aspectX = aspectX;
		this.aspectY = aspectY;
		this.outputX = outputX;
		this.outputY = outputY;
		this.mTakePhotoListener = takePhotoListener;
		initLocalFilePath(mActivity);
		startSelectImage();
	}

	/**
	 * 初始化本地文件存储目录
	 * @param activity
	 */
	private void initLocalFilePath(Activity activity)
	{
		sdcardPath = Environment.getExternalStorageDirectory() + "/focustech/capturePicture/";
		fileBasePath = "file://" + sdcardPath;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (requestCode)
		{
		case PHOTO_REQUEST_CUT:
			try
			{
				setDisplayViewImage();
			}
			catch (FileNotFoundException e)
			{
				mTakePhotoListener.onFail(TakePhotoFailReason.FileNotFound);
			}
			catch (OutOfMemoryError e)
			{
				mTakePhotoListener.onFail(TakePhotoFailReason.OutOfMemory);
			}
			break;
		}
	}

	/**
	 * 开始选择图片
	 */
	protected void startSelectImage()
	{

	}

	/**
	 * 根据当前时间获得一个图片路径
	 * @return
	 */
	protected Uri getCurrentUri()
	{
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss", Locale.ENGLISH);
		Log.i("------currentImageUri--------", fileBasePath + dateFormat.format(date) + ".jpg");
		return Uri.parse(fileBasePath + dateFormat.format(date) + ".jpg");
	}

	/**
	 * 调用裁剪图片的系统页面
	 * @param uri
	 */
	protected void cropImageUri(Uri uri)
	{
		try
		{
			// 设置裁剪
			mIntent.putExtra("crop", "true");
			// aspectX aspectY 是宽高的比例
			mIntent.putExtra("aspectX", aspectX);
			mIntent.putExtra("aspectY", aspectY);
			// outputX outputY 是裁剪图片宽高
			mIntent.putExtra("outputX", outputX);
			mIntent.putExtra("outputY", outputY);
			mIntent.putExtra("scale", true);
			mIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			mIntent.putExtra("return-data", false);
			mIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
			mIntent.putExtra("noFaceDetection", true); // no face detection
			if (mFragment != null)
			{
				mFragment.startActivityForResult(mIntent, PHOTO_REQUEST_CUT);
			}
			else
			{
				mActivity.startActivityForResult(mIntent, PHOTO_REQUEST_CUT);
			}
			
		}
		catch (ActivityNotFoundException e)
		{
			mTakePhotoListener.onFail(TakePhotoFailReason.ActivityNotFound);
		}
	}

	/**
	 * 设置需要显示控件的图片属性
	 * @throws FileNotFoundException
	 */
	protected void setDisplayViewImage() throws FileNotFoundException
	{
		if (currentUri != null)
		{
			Bitmap bitmap = BitmapFactory.decodeFile(currentUri.getPath());
			if (bitmap == null)
			{
				throw new FileNotFoundException();
			}
			if (mDisplayView != null)
			{
				if (mDisplayView instanceof ImageView)
				{
					((ImageView) mDisplayView).setImageBitmap(bitmap);
				}
				else
				{
					Drawable drawable = new BitmapDrawable(mActivity.getResources(), bitmap);
					mDisplayView.setBackgroundDrawable(drawable);
				}
			}
			mTakePhotoListener.onSuccess(currentUri.getPath(), mDisplayView, bitmap);
		}
	}

	/**
	 * 创建存储图片的SDCard目录
	 */
	protected void createPhotoDir()
	{
		File file = new File(sdcardPath);
		if (!file.exists())
		{
			file.mkdirs();
		}
	}

	/**********************************************************
	 * @文件名称：TakePhoto.java
	 * @文件作者：xiongjiangwei
	 * @创建时间：2013-11-19 上午10:54:36
	 * @文件描述：图片选取回调
	 * @修改历史：2013-11-19创建初始版本
	 **********************************************************/
	public interface TakePhotoListener
	{
		/**
		 * 获取成功
		 * @param imagePath		当前显示的图片路径
		 * @param displayView	需要显示的控件
		 * @param bitmap		当前控件加载的图片对象
		 */
		public void onSuccess(String imagePath, View displayView, Bitmap bitmap);

		/**
		 * 获取失败
		 * @param failReason	失败原因
		 */
		public void onFail(TakePhotoFailReason failReason);
	}

	/**********************************************************
	 * @文件名称：TakePhoto.java
	 * @文件作者：xiongjiangwei
	 * @创建时间：2013-11-18 下午02:49:09
	 * @文件描述：图片选取失败原因
	 * @修改历史：2013-11-18创建初始版本
	 **********************************************************/
	public enum TakePhotoFailReason
	{
		ActivityNotFound, FileNotFound, OutOfMemory, SDCardNotFound
	}
}

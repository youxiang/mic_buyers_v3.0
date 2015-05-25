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
 * @�ļ����ƣ�TakePhoto.java
 * @�ļ����ߣ�xiongjiangwei
 * @����ʱ�䣺2013-11-18 ����02:47:49
 * @�ļ���������ȡͼƬ����
 * @�޸���ʷ��2013-11-18������ʼ�汾
 **********************************************************/
public class TakePhoto
{
	protected Fragment mFragment;
	protected Activity mActivity;
	protected Intent mIntent = null;
	/**���ҳ��ص���ʾ**/
	protected static final int PHOTO_REQUEST_TAKEPHOTO = 0x00000001;
	/**ͼƬ�ü���ɻص���ʾ**/
	protected static final int PHOTO_REQUEST_CUT = 0x00000002;
	/**�ӱ�����ѡȡͼƬ**/
	protected static final int PHOTO_REQUEST_GET = 0x00000003;
	/**ͼƬ�����ļ���Uri**/
	protected Uri currentUri = null;
	/**ͼƬ��ȡ�ɹ���ʧ�ܺ�Ļص�**/
	protected TakePhotoListener mTakePhotoListener;
	/**��Ҫ��ʾͼ��Ŀؼ�**/
	protected View mDisplayView;
	/**sdcard����Ŀ¼**/
	private String sdcardPath = null;
	/**ͼƬ���ش洢·��**/
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
	 * ��ʼ�������ļ��洢Ŀ¼
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
	 * ��ʼѡ��ͼƬ
	 */
	protected void startSelectImage()
	{

	}

	/**
	 * ���ݵ�ǰʱ����һ��ͼƬ·��
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
	 * ���òü�ͼƬ��ϵͳҳ��
	 * @param uri
	 */
	protected void cropImageUri(Uri uri)
	{
		try
		{
			// ���òü�
			mIntent.putExtra("crop", "true");
			// aspectX aspectY �ǿ�ߵı���
			mIntent.putExtra("aspectX", aspectX);
			mIntent.putExtra("aspectY", aspectY);
			// outputX outputY �ǲü�ͼƬ���
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
	 * ������Ҫ��ʾ�ؼ���ͼƬ����
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
	 * �����洢ͼƬ��SDCardĿ¼
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
	 * @�ļ����ƣ�TakePhoto.java
	 * @�ļ����ߣ�xiongjiangwei
	 * @����ʱ�䣺2013-11-19 ����10:54:36
	 * @�ļ�������ͼƬѡȡ�ص�
	 * @�޸���ʷ��2013-11-19������ʼ�汾
	 **********************************************************/
	public interface TakePhotoListener
	{
		/**
		 * ��ȡ�ɹ�
		 * @param imagePath		��ǰ��ʾ��ͼƬ·��
		 * @param displayView	��Ҫ��ʾ�Ŀؼ�
		 * @param bitmap		��ǰ�ؼ����ص�ͼƬ����
		 */
		public void onSuccess(String imagePath, View displayView, Bitmap bitmap);

		/**
		 * ��ȡʧ��
		 * @param failReason	ʧ��ԭ��
		 */
		public void onFail(TakePhotoFailReason failReason);
	}

	/**********************************************************
	 * @�ļ����ƣ�TakePhoto.java
	 * @�ļ����ߣ�xiongjiangwei
	 * @����ʱ�䣺2013-11-18 ����02:49:09
	 * @�ļ�������ͼƬѡȡʧ��ԭ��
	 * @�޸���ʷ��2013-11-18������ʼ�汾
	 **********************************************************/
	public enum TakePhotoFailReason
	{
		ActivityNotFound, FileNotFound, OutOfMemory, SDCardNotFound
	}
}

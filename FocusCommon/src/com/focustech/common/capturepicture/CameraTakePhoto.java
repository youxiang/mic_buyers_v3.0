package com.focustech.common.capturepicture;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.View;

import com.focustech.common.util.Utils;

/**********************************************************
 * @文件名称：CameraTakePhoto.java
 * @文件作者：xiongjiangwei
 * @创建时间：2013-11-18 下午02:48:13
 * @文件描述：从相机选择图片
 * @修改历史：2013-11-18创建初始版本
 **********************************************************/
public class CameraTakePhoto extends TakePhoto
{
	/**
	 * 构造方法
	 * @param activity		页面对象
	 * @param displayView	图片显示对象
	 * @param takePhotoListener	图片回调
	 */
	public CameraTakePhoto(Activity activity, View displayView, TakePhotoListener takePhotoListener)
	{
		super(activity, displayView, takePhotoListener);
	}
	
	/**
	 * 构造方法
	 * @param activity		页面对象
	 * @param displayView	图片显示对象
	 * @param takePhotoListener	图片回调
	 */
	public CameraTakePhoto(Fragment fragment, View displayView, TakePhotoListener takePhotoListener)
	{
		super(fragment, displayView, takePhotoListener);
	}

	/**
	 * 构造方法
	 * @param activity	页面对象
	 * @param displayView	图片显示对象
	 * @param aspectX	裁剪宽比例
	 * @param aspectY	裁剪高比例
	 * @param outputX	裁剪宽度
	 * @param outputY	裁剪高度
	 * @param takePhotoListener	图片回调
	 */
	public CameraTakePhoto(Activity activity, View displayView, int aspectX, int aspectY, int outputX, int outputY,
			TakePhotoListener takePhotoListener)
	{
		super(activity, displayView, aspectX, aspectY, outputX, outputY, takePhotoListener);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode != Activity.RESULT_OK)
		{
			return;
		}
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode)
		{
		case PHOTO_REQUEST_TAKEPHOTO:
			cropCurrentImage(currentUri);
			break;
		}
	}

	private void cropCurrentImage(Uri currentUri)
	{
		mIntent = new Intent("com.android.camera.action.CROP");
		Uri tmp = currentUri;
		mIntent.setDataAndType(tmp, "image/*");
		this.currentUri = getCurrentUri();
		cropImageUri(this.currentUri);
	}

	@Override
	protected void startSelectImage()
	{
		try
		{
			if (Utils.isHaveSDcard())
			{
				createPhotoDir();
				currentUri = getCurrentUri();
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, currentUri);
				if (mFragment != null)
				{
					mFragment.startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
				}
				else
				{
					mActivity.startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
				}
				
			}
			else
			{
				mTakePhotoListener.onFail(TakePhotoFailReason.SDCardNotFound);
			}
		}
		catch (ActivityNotFoundException e)
		{
			mTakePhotoListener.onFail(TakePhotoFailReason.ActivityNotFound);
		}
	}

}

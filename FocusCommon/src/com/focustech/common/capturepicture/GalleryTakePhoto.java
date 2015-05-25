package com.focustech.common.capturepicture;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.view.View;

import com.focustech.common.util.Utils;

/**********************************************************
 * @文件名称：GalleryTakePhoto.java
 * @文件作者：xiongjiangwei
 * @创建时间：2013-11-18 下午02:48:49
 * @文件描述：从相册选择图片
 * @修改历史：2013-11-18创建初始版本
 **********************************************************/
public class GalleryTakePhoto extends TakePhoto
{
	/**
	 * 构造方法
	 * @param activity		页面对象
	 * @param displayView	图片显示对象
	 * @param takePhotoListener	图片回调
	 */
	public GalleryTakePhoto(Activity activity, View displayView, TakePhotoListener takePhotoListener)
	{
		super(activity, displayView, takePhotoListener);
	}

	public GalleryTakePhoto(Fragment fragment, View displayView, TakePhotoListener takePhotoListener)
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
	public GalleryTakePhoto(Activity activity, View displayView, int aspectX, int aspectY, int outputX, int outputY,
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
		if (requestCode != PHOTO_REQUEST_GET)
		{
			super.onActivityResult(requestCode, resultCode, data);
		}
		else
		{
			Context tmp = null;
			if (mFragment != null)
			{
				tmp = mFragment.getActivity().getApplicationContext();
			}
			else
			{
				tmp = mActivity.getApplicationContext();
			}
			String thePath = Utils.getPath(tmp, data.getData());

			Uri uri = Uri.fromFile(new File(thePath));

			mIntent = new Intent("com.android.camera.action.CROP");
			mIntent.setDataAndType(uri, "image/*");
			
			cropImageUri(currentUri);
		}
	}

	@Override
	protected void startSelectImage()
	{
		if (Utils.isHaveSDcard())
		{
			createPhotoDir();
			currentUri = getCurrentUri();
			// mIntent = new Intent(Intent.ACTION_GET_CONTENT);
			if (Build.VERSION.SDK_INT < 19)
			{
				mIntent = new Intent(Intent.ACTION_GET_CONTENT);
				mIntent.setType("image/*");
				cropImageUri(currentUri);
			}
			else
			{
				mIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
				mIntent.setType("image/*");

				if (mFragment != null)
				{
					mFragment.startActivityForResult(mIntent, PHOTO_REQUEST_GET);
				}
				else
				{
					mActivity.startActivityForResult(mIntent, PHOTO_REQUEST_GET);
				}
			}

		}
		else
		{
			mTakePhotoListener.onFail(TakePhotoFailReason.SDCardNotFound);
		}
	}
}

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
 * @�ļ����ƣ�CameraTakePhoto.java
 * @�ļ����ߣ�xiongjiangwei
 * @����ʱ�䣺2013-11-18 ����02:48:13
 * @�ļ������������ѡ��ͼƬ
 * @�޸���ʷ��2013-11-18������ʼ�汾
 **********************************************************/
public class CameraTakePhoto extends TakePhoto
{
	/**
	 * ���췽��
	 * @param activity		ҳ�����
	 * @param displayView	ͼƬ��ʾ����
	 * @param takePhotoListener	ͼƬ�ص�
	 */
	public CameraTakePhoto(Activity activity, View displayView, TakePhotoListener takePhotoListener)
	{
		super(activity, displayView, takePhotoListener);
	}
	
	/**
	 * ���췽��
	 * @param activity		ҳ�����
	 * @param displayView	ͼƬ��ʾ����
	 * @param takePhotoListener	ͼƬ�ص�
	 */
	public CameraTakePhoto(Fragment fragment, View displayView, TakePhotoListener takePhotoListener)
	{
		super(fragment, displayView, takePhotoListener);
	}

	/**
	 * ���췽��
	 * @param activity	ҳ�����
	 * @param displayView	ͼƬ��ʾ����
	 * @param aspectX	�ü������
	 * @param aspectY	�ü��߱���
	 * @param outputX	�ü����
	 * @param outputY	�ü��߶�
	 * @param takePhotoListener	ͼƬ�ص�
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

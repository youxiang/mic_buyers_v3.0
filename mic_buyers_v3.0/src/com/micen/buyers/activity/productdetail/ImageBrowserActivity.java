package com.micen.buyers.activity.productdetail;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.focustech.common.util.ToastUtil;
import com.micen.buyers.activity.BaseActivity;
import com.micen.buyers.activity.R;
import com.micen.buyers.adapter.ImageBrowserAdapter;
import com.micen.buyers.util.BitmapUtil;

@EActivity
public class ImageBrowserActivity extends BaseActivity implements OnPageChangeListener
{
	@ViewById(R.id.bootom_textView)
	protected TextView bottomTextView;
	@ViewById(R.id.image_pager)
	protected ViewPager imagePager;

	private int position;
	private ArrayList<String> imageList;
	private ImageBrowserAdapter vAdapter;
	private String isShowImage;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_image_scan);
	}

	private void initIntent()
	{
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		position = bundle.getInt("position");
		imageList = bundle.getStringArrayList("imageList");
		isShowImage = bundle.getString("isShowImage");
	}

	private void initTitleView()
	{

	}

	@AfterViews
	protected void initView()
	{
		initIntent();

		titleLeftButton.setImageResource(R.drawable.ic_title_back);
		titleText.setText("Image");
		titleRightButton1.setVisibility(View.GONE);
		titleRightButton2.setVisibility(View.GONE);
		titleRightButton3.setImageResource(R.drawable.product_detail_picture_download);
		titleLeftButton.setOnClickListener(this);
		titleRightButton3.setOnClickListener(this);

		initTitleView();
		vAdapter = new ImageBrowserAdapter(this, imageList, isShowImage);
		imagePager.setAdapter(vAdapter);
		imagePager.setCurrentItem(position);
		bottomTextView.setText(position + 1 + "/" + String.valueOf(imageList.size()));

		imagePager.setOnPageChangeListener(this);
	}

	@Override
	protected void onRestart()
	{
		super.onRestart();
		if (null != vAdapter)
		{
			vAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onClick(View view)
	{
		super.onClick(view);
		switch (view.getId())
		{
		case R.id.common_title_back_button:
			this.finish();
			break;
		case R.id.common_title_right_button3:
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
			{
				new Thread(saveFileRunnable).start();
			}
			else
			{
				ToastUtil.toast(this, R.string.sd_card_undetected);
			}
			break;
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0)
	{
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2)
	{
	}

	@Override
	public void onPageSelected(int position)
	{
		bottomTextView.setText(String.valueOf(position + 1) + "/" + String.valueOf(imageList.size()));
	}

	private final static String ALBUM_PATH = Environment.getExternalStorageDirectory() + "/focustech/mic/download/";
	private Runnable saveFileRunnable = new Runnable()
	{
		@Override
		public void run()
		{
			try
			{
				BitmapUtil bt = new BitmapUtil();
				Bitmap bitmap = bt.returnBitMap(imageList.get(imagePager.getCurrentItem()));
				saveFile(bitmap, System.currentTimeMillis() + ".png");
				messageHandler.sendMessage(messageHandler.obtainMessage(0, "Save Successful"));
			}
			catch (IOException e)
			{
				messageHandler.sendMessage(messageHandler.obtainMessage(0, "Save faild"));
				e.printStackTrace();
			}
		}
	};

	public void saveFile(Bitmap bm, String fileName) throws IOException
	{
		File dirFile = new File(ALBUM_PATH);
		if (!dirFile.exists())
		{
			dirFile.mkdir();
		}
		File myCaptureFile = new File(ALBUM_PATH + fileName);
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
		bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
		bos.flush();
		bos.close();
	}

	private Handler messageHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			Log.d("ImageBrowserActivity", String.valueOf(msg.obj));
			ToastUtil.toast(ImageBrowserActivity.this, msg.obj);
		}
	};
}

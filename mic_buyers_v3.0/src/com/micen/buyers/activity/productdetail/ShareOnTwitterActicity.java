package com.micen.buyers.activity.productdetail;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.focustech.common.util.ToastUtil;
import com.micen.buyers.activity.BaseActivity;
import com.micen.buyers.activity.R;
import com.micen.twitter.android.TwitterUtil;

@EActivity(R.layout.share_twitter)
public class ShareOnTwitterActicity extends BaseActivity
{
	@ViewById(R.id.et_share_twitter)
	protected EditText et;
	@ViewById(R.id.bt_share_twitter)
	protected Button bt;
	private ProgressDialog shareingDialog;
	private final int TIMER_EXECUTE = 5;
	private final int ERROR_MESSAGE = 5;
	private TwitterUtil tt;
	private MyThread myThread;
	private String productName;
	private String productPicture;
	private String productURl;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@AfterViews
	protected void initView()
	{
		Intent intent = getIntent();
		productName = intent.getStringExtra("productName");
		productPicture = intent.getStringExtra("productPicture");
		productURl = intent.getStringExtra("productURl");
		titleLeftButton.setImageResource(R.drawable.ic_title_back);
		titleText.setText("Share on twitter");
		titleRightButton1.setVisibility(View.GONE);
		titleRightButton2.setVisibility(View.GONE);
		titleRightButton3.setVisibility(View.GONE);

		titleLeftButton.setOnClickListener(this);
		bt.setOnClickListener(this);
	}

	@Override
	public void onClick(View view)
	{
		super.onClick(view);
		switch (view.getId())
		{
		case R.id.common_title_back_button:
			finish();
			break;
		case R.id.bt_share_twitter:
			tt = new TwitterUtil(ShareOnTwitterActicity.this);
			if (null == et.getText())
			{
				ToastUtil.toast(this, "Please input the content");
			}
			else
			{
				if (null == shareingDialog)
				{
					shareingDialog = new ProgressDialog(ShareOnTwitterActicity.this);
					shareingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					// ����ProgressDialog ����
					// waitingDialog.setTitle("���Ժ�");
					shareingDialog.setCanceledOnTouchOutside(false);
					shareingDialog.setMessage(getString(R.string.mic_loading));
					shareingDialog.show();
				}
				myThread = new MyThread();
				myThread.start();
			}
			break;
		}
	}

	class MyThread extends Thread
	{
		@Override
		public void run()
		{
			Message msg = new Message();
			try
			{
				tt.sendTweet(et.getText().toString(), productName, productPicture, productURl);
				msg.what = 0;
			}
			catch (Exception e)
			{
				e.printStackTrace();
				msg.what = 1;
			}
			handler.sendMessage(msg);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id)
	{
		return new AlertDialog.Builder(this).setTitle("错误").setMessage("线程超时").create();
	}

	protected void checkThread()
	{
		Message msg = new Message();
		msg.what = TIMER_EXECUTE;
		handler.sendMessage(msg);
	}

	private Handler handler = new Handler()
	{
		@SuppressWarnings("deprecation")
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case 0:
				shareingDialog.dismiss();
				shareingDialog = null;
				ShareOnTwitterActicity.this.finish();
				ToastUtil.toast(ShareOnTwitterActicity.this, "share successful");
				break;
			case 1:
				shareingDialog.dismiss();
				shareingDialog = null;
				ShareOnTwitterActicity.this.finish();
				ToastUtil.toast(ShareOnTwitterActicity.this, "Share Failed");
				break;
			case TIMER_EXECUTE:
				shareingDialog.dismiss();
				if (myThread.getState().toString().equals("TERMINATED")
						|| myThread.getState().toString().equals("TIMED_WAITING"))
				{
					// myThread.stopThread(true);
					showDialog(ERROR_MESSAGE);
				}
				break;
			default:
				super.handleMessage(msg);
			}
		}
	};
}

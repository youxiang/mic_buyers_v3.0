package com.micen.buyers.activity.productdetail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.micen.buyers.activity.BaseActivity;
import com.micen.buyers.activity.R;
import com.micen.buyers.constant.Constants;
import com.micen.buyers.util.ImageUtil;
import com.micen.linkedin.android.DialogError;
import com.micen.linkedin.android.LinkedIn;
import com.micen.linkedin.android.LinkedInError;

public class ShareOnLinkedinActivity extends BaseActivity implements OnClickListener
{
	protected ImageView titleLeftButton;
	protected TextView titleText;
	protected ImageView titleRightButton1;
	protected ImageView titleRightButton2;
	protected ImageView titleRightButton3;
	private EditText _commentET;
	private ImageView product_image;
	private String _productURl;
	private String _name;
	private String _imageUrl;
	private String _description;
	private Button lindein;
	private LinkedIn _linkedIn;
	private SharedPreferences prefs;
	private TextView product_name;
	private TextView product_link;
	private TextView tv_product_discription;
	private static final int SHARE_SUCCESSFUL = 2;
	private static final int SHARE_ERROR = 3;
	private ProgressDialog shareingDialog;
	private CheckBox cb_include_image;
	private boolean flag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initIntent();
		_linkedIn = new LinkedIn(R.drawable.linkedin_logo);
		if (!isAuthenticated())
		{
			_linkedIn.authorize(ShareOnLinkedinActivity.this, null, getString(R.string.linkedin_api_key),
					getString(R.string.linkedin_secret_key), new LinkedIn.LinkedInDialogListener()
					{
						@Override
						public void onLinkedInError(LinkedInError e)
						{
							Log.i("tag", "LinkedInError" + e);
						}

						@Override
						public void onError(DialogError e)
						{
							Log.i("tag", "onError " + e);
						}

						@Override
						public void onComplete(Bundle values)
						{
							final Editor edit = prefs.edit();
							edit.putString("LINKEDIN_ACCESS_TOKEN", _linkedIn.getAccessToken());
							edit.putString("LINKEDIN_ACCESS_SECRETE", _linkedIn.getSecretToken());
							edit.commit();
							Log.i("tag", "onComplete");
							ShareOnLinkedinActivity.this.runOnUiThread(new Runnable()
							{
								@Override
								public void run()
								{
									setContentView(R.layout.share_linkedin);
									initView();
									setListener();
								}
							});
						}

						@Override
						public void onCancel()
						{
							Log.i("tag", "onCancel");
						}
					});
		}
		else
		{
			String token = prefs.getString("LINKEDIN_ACCESS_TOKEN", "");
			String secret = prefs.getString("LINKEDIN_ACCESS_SECRETE", "");
			_linkedIn.setAccessToken(token);
			_linkedIn.setSecretToken(secret);
			ShareOnLinkedinActivity.this.runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					setContentView(R.layout.share_linkedin);
					initView();
					setListener();
				}
			});
		}
	}

	protected void initView()
	{
		titleLeftButton = (ImageView) findViewById(R.id.common_title_back_button);
		titleText = (TextView) findViewById(R.id.common_title_name);
		titleRightButton1 = (ImageView) findViewById(R.id.common_title_right_button1);
		titleRightButton2 = (ImageView) findViewById(R.id.common_title_right_button2);
		titleRightButton3 = (ImageView) findViewById(R.id.common_title_right_button3);

		titleLeftButton.setImageResource(R.drawable.ic_title_back);
		titleText.setText("Share on Linkedin");
		titleRightButton1.setVisibility(View.GONE);
		titleRightButton2.setVisibility(View.GONE);
		titleRightButton3.setVisibility(View.GONE);

		lindein = (Button) findViewById(R.id.bt_share_linkedin);
		lindein.setOnClickListener(this);
		cb_include_image = (CheckBox) findViewById(R.id.cb_include_image);
		flag = cb_include_image.isChecked();
		_commentET = (EditText) findViewById(R.id.et_share_linkedin);
		product_image = (ImageView) findViewById(R.id.product_image);
		product_name = (TextView) findViewById(R.id.product_name);
		product_link = (TextView) findViewById(R.id.product_link);
		tv_product_discription = (TextView) findViewById(R.id.tv_product_discription);
		_description = tv_product_discription.getText().toString().trim();
		ImageUtil.getImageLoader().displayImage(_imageUrl, product_image, ImageUtil.getRecommendImageOptions());
		product_name.setText(_name);
		product_link.setText(_imageUrl);
	}

	protected void setListener()
	{
		titleLeftButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{
		case R.id.common_title_back_button:
			finish();
			break;
		case R.id.bt_share_linkedin:
			if (null == shareingDialog)
			{
				shareingDialog = new ProgressDialog(ShareOnLinkedinActivity.this);
				shareingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				// ����ProgressDialog ����
				// waitingDialog.setTitle("���Ժ�");
				shareingDialog.setCanceledOnTouchOutside(false);
				shareingDialog.setMessage(getString(R.string.mic_loading));
				shareingDialog.show();
			}
			if (cb_include_image.isChecked())
			{// ����ͼƬ
				flag = true;
			}
			else
			{
				flag = false;
			}
			new Thread()
			{
				@Override
				public void run()
				{
					Message msg = new Message();
					msg.what = 0;
					super.run();
					postToLinkedIn();
				}
			}.start();
			break;
		}
	}

	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case 0:
				break;
			case 2:
				shareingDialog.dismiss();
				shareingDialog = null;
				showDialogOk("", ShareOnLinkedinActivity.this.getString(R.string.LinkedIn_post_success),
						ShareOnLinkedinActivity.this);
				// ShareOnLinkedinActivity.this.finish();
				break;
			case 3:
				shareingDialog.dismiss();
				shareingDialog = null;
				showDialogOk("", ShareOnLinkedinActivity.this.getString(R.string.LinkedIn_post_failed),
						ShareOnLinkedinActivity.this);
				// ShareOnLinkedinActivity.this.finish();
				break;
			default:
				super.handleMessage(msg);
			}
		}
	};

	public boolean isAuthenticated()
	{
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String token = prefs.getString("LINKEDIN_ACCESS_TOKEN", "");
		if (null == token || "".equals(token.trim()))
		{
			return false;
		}
		return true;
	}

	private void initIntent()
	{
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (null != bundle)
		{
			_name = bundle.getString("productName");
			_imageUrl = bundle.getString("productPicture");
			_productURl = bundle.getString("productURl");
		}
	}

	private void postToLinkedIn()
	{
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("message", _commentET.getText().toString());
		data.put("name", _name);// ��Ʒ����
		data.put("product_url", _productURl);// ��Ʒ����
		if (flag)
		{
			data.put("picture", _imageUrl);// ͼƬ��ַ
		}
		data.put("description", _description);// ��Ʒ����
		postMessageToSession(data);
	}

	public void postMessageToSession(HashMap<String, String> data)
	{
		String xmlContent = getExtraXMLContentForLinkedIn(data);
		String message = "";
		if (!TextUtils.isEmpty(data.get("message")))
		{
			message = String.format("%s, Commented about %s", data.get("message"), data.get("name"));
		}
		String resultXML = String.format(getXMLPOSTFormatForLinkedIn(), message, xmlContent);
		Message msg = new Message();
		try
		{
			_linkedIn.shareMessage(resultXML, Constants.LN_API_KEY, Constants.LN_API_SECRET);
			msg.what = SHARE_SUCCESSFUL;
			handler.sendMessage(msg);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			msg.what = SHARE_ERROR;
			handler.sendMessage(msg);
		}
	}

	private String getXMLPOSTFormatForLinkedIn()
	{
		InputStream is;
		try
		{
			is = this.getAssets().open("format.xml");
			BufferedReader r = new BufferedReader(new InputStreamReader(is));
			StringBuilder total = new StringBuilder();
			String line;
			while ((line = r.readLine()) != null)
			{
				total.append(line);
			}
			return total.toString();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	private String getExtraXMLContentForLinkedIn(HashMap<String, String> data)
	{
		String xmlContent = null;
		if (data.containsKey("picture"))
		{
			String format = "<content><title>%s</title><submitted-url>%s</submitted-url><submitted-image-url>%s</submitted-image-url><description>%s</description></content>";
			xmlContent = String.format(format, data.get("name"),// ��Ʒ����
					data.get("product_url"),// ��Ʒ����
					data.get("picture"),// ��ƷͼƬ
					data.get("description"));// ��Ʒ����
		}
		else
		{
			String format = "<content><title>%s</title><submitted-url>%s</submitted-url><description>%s</description></content>";
			xmlContent = String.format(format, data.get("name"),// ��Ʒ����
					data.get("product_url"),// ��Ʒ����
					data.get("description"));// ��Ʒ����
		}
		return xmlContent;
	}

	public void showDialogOk(String title, String message, Context context)
	{
		if (context != null)
		{
			Dialog dlg = new AlertDialog.Builder(context).setTitle(title).setMessage(message)
					.setPositiveButton("OK", new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int whichButton)
						{
							ShareOnLinkedinActivity.this.finish();
						}
					}).create();
			dlg.setVolumeControlStream(AudioManager.STREAM_MUSIC);
			dlg.show();
		}
	}
}

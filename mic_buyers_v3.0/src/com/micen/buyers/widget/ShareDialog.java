package com.micen.buyers.widget;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.micen.buyers.activity.R;

public class ShareDialog extends Dialog implements android.view.View.OnClickListener
{
	private Activity context;

	private TextView titleText;

	private LinearLayout facebookLayout;

	private LinearLayout twitterLayout;

	private LinearLayout linkedinLayout;

	private TextView cancelBtn;

	private OnShareDialogListener listener;

	public interface OnShareDialogListener
	{
		public void onFaceBookClick();

		public void onTwitterClick();

		public void onLinkedinClick();

		public void onCancel();
	}

	public ShareDialog(Activity context, OnShareDialogListener listener)
	{
		super(context, R.style.shareDialog);
		this.context = context;
		this.listener = listener;
		init();
	}

	private void init()
	{
		View contentView = LayoutInflater.from(getContext()).inflate(R.layout.share_layout, null);
		setContentView(contentView);
		titleText = (TextView) contentView.findViewById(R.id.title);
		facebookLayout = (LinearLayout) contentView.findViewById(R.id.facebook_layout);
		twitterLayout = (LinearLayout) contentView.findViewById(R.id.twitter_layout);
		linkedinLayout = (LinearLayout) contentView.findViewById(R.id.linkedin_layout);
		cancelBtn = (TextView) contentView.findViewById(R.id.cancel);

		facebookLayout.setOnClickListener(this);
		twitterLayout.setOnClickListener(this);
		linkedinLayout.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.facebook_layout:
			dismiss();
			if (listener != null)
			{
				listener.onFaceBookClick();
			}
			break;
		case R.id.twitter_layout:
			dismiss();
			if (listener != null)
			{
				listener.onTwitterClick();
			}
			break;
		case R.id.linkedin_layout:
			dismiss();
			if (listener != null)
			{
				listener.onLinkedinClick();
			}
			break;
		case R.id.cancel:
			dismiss();
			if (listener != null)
			{
				listener.onCancel();
			}
			break;
		}
	}

}

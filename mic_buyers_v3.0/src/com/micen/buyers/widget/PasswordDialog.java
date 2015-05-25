package com.micen.buyers.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.micen.buyers.activity.R;
import com.micen.buyers.util.Util;

public class PasswordDialog extends Dialog implements android.view.View.OnClickListener
{
	private Button confirmBtn, cancelBtn;
	private EditText passwordEdit;

	public interface PasswordDialogListener
	{
		void onConfirm(String password);

		void onCancel();
	}

	private PasswordDialogListener listener;
	private Context context;

	public void setPasswordDialogListener(PasswordDialogListener listener)
	{
		this.listener = listener;
	}

	public PasswordDialog(Context context, PasswordDialogListener listener)
	{
		super(context, R.style.customDialog);
		this.context = context;
		this.listener = listener;
		init();
	}

	private void init()
	{
		View contentView = LayoutInflater.from(getContext()).inflate(R.layout.mic_dialog_content2, null);
		setContentView(contentView);
		setParams(Util.dip2px(285), Util.dip2px(160));
		confirmBtn = (Button) contentView.findViewById(R.id.btCall);
		cancelBtn = (Button) contentView.findViewById(R.id.btCancel);
		passwordEdit = (EditText) contentView.findViewById(R.id.product_group_encryptPassword);
		confirmBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);

	}
	
	public void setParams(int width, int height)
	{
		WindowManager.LayoutParams dialogParams = this.getWindow().getAttributes();
		dialogParams.width = width;
		dialogParams.height = height;
		this.getWindow().setAttributes(dialogParams);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.btCall:
			dismiss();
			if (listener != null)
			{
				listener.onConfirm(passwordEdit.getText().toString());
			}
			break;
		case R.id.btCancel:
			dismiss();
			if (listener != null)
			{
				listener.onCancel();
			}
			break;
		}
	}

}

package com.focustech.common.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.focustech.common.R;

/**********************************************************
 * @文件名称：CommonDialog.java
 * @文件作者：xiongjiangwei
 * @创建时间：2014年9月18日 下午7:02:43
 * @文件描述：公用的对话框
 * @修改历史：2014年9月18日创建初始版本
 **********************************************************/
public class CommonDialog extends Dialog
{
	private ViewGroup contentView;

	public interface DialogClickListener
	{
		public void onDialogClick();
	}

	public CommonDialog(Context context, String msg, String confirmText, int dialogWidth,
			DialogClickListener confirmClick)
	{
		this(context, msg, confirmText, null, dialogWidth, confirmClick, null);
	}

	public CommonDialog(Context context, String msg, String confirmText, int dialogWidth,
			DialogClickListener confirmClick, DialogClickListener cancelClick)
	{
		this(context, msg, confirmText, null, dialogWidth, confirmClick, cancelClick);
	}

	public CommonDialog(Context context, String msg, String confirmText, String cancelText, int dialogWidth,
			DialogClickListener confirmClick)
	{
		this(context, msg, confirmText, cancelText, dialogWidth, confirmClick, null);
	}

	public CommonDialog(Context context, String msg, String confirmText, String cancelText, int dialogWidth,
			DialogClickListener confirmClick, DialogClickListener cancelClick)
	{
		super(context, R.style.customDialog);
		initDialogStyle(msg, confirmText, cancelText, dialogWidth, confirmClick, cancelClick);
	}

	public void initDialogStyle(String msg, String confirmText, String cancelText, int dialogWidth,
			final DialogClickListener confirmClick, final DialogClickListener cancelClick)
	{
		setContentView(createDialogView(R.layout.common_dialog_content));
		setParams(dialogWidth, LayoutParams.WRAP_CONTENT);
		LinearLayout layout1 = (LinearLayout) findChildViewById(R.id.common_dialog_two_btn_layout);
		LinearLayout layout2 = (LinearLayout) findChildViewById(R.id.common_dialog_one_btn_layout);
		TextView tvMsg = (TextView) findChildViewById(R.id.common_dialog_msg);
		if (msg != null && !"".equals(msg))
		{
			tvMsg.setText(msg);
		}
		if (cancelText == null)
		{
			layout1.setVisibility(View.GONE);
			layout2.setVisibility(View.VISIBLE);
			setCanceledOnTouchOutside(false);
			setCancelable(false);
			Button btnComfirm = (Button) findChildViewById(R.id.common_dialog_btn_sure);
			btnComfirm.setText(confirmText);
			btnComfirm.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					dismiss();
					confirmClick.onDialogClick();
				}
			});
		}
		else
		{
			layout1.setVisibility(View.VISIBLE);
			layout2.setVisibility(View.GONE);
			Button btnCancel = (Button) findChildViewById(R.id.common_dialog_btn_cancel);
			Button btnComfirm = (Button) findChildViewById(R.id.common_dialog_btn_confirm);
			btnCancel.setText(cancelText);
			btnComfirm.setText(confirmText);
			btnCancel.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					dismiss();
					if (cancelClick != null)
					{
						cancelClick.onDialogClick();
					}
				}
			});
			btnComfirm.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					dismiss();
					confirmClick.onDialogClick();
				}
			});
		}
	}

	private ViewGroup createDialogView(int layoutId)
	{
		contentView = (ViewGroup) LayoutInflater.from(getContext()).inflate(layoutId, null);
		return contentView;
	}

	public void setParams(int width, int height)
	{
		WindowManager.LayoutParams dialogParams = this.getWindow().getAttributes();
		dialogParams.width = width;
		dialogParams.height = height;
		this.getWindow().setAttributes(dialogParams);
	}

	public View findChildViewById(int id)
	{
		return contentView.findViewById(id);
	}

}

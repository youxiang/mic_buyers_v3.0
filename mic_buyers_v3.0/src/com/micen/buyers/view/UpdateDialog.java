package com.micen.buyers.view;

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

import com.micen.buyers.activity.R;
import com.micen.buyers.util.Util;

/**********************************************************
 * @文件名称：UpdateDialog.java
 * @文件作者：xiongjiangwei
 * @创建时间：2014年9月18日 下午7:02:43
 * @文件描述：检查更新对话框
 * @修改历史：2014年9月18日创建初始版本
 **********************************************************/
public class UpdateDialog extends Dialog
{
	private ViewGroup contentView;
	private boolean isForceUpdate = false;
	
	public interface ConfirmDialogListener
	{
		public void onConfirmDialog();
	}

	public UpdateDialog(Context context, boolean isForceUpdate)
	{
		super(context, R.style.customDialog);
		this.isForceUpdate = isForceUpdate;
	}

	public void initDialogStyle1(String msg, final ConfirmDialogListener confirmClick)
	{
		setContentView(createDialogView(R.layout.update_dialog_content));
		setParams(Util.dip2px(285), LayoutParams.WRAP_CONTENT);
		LinearLayout layout1 = (LinearLayout) findChildViewById(R.id.update_dialog_btn_layout1);
		LinearLayout layout2 = (LinearLayout) findChildViewById(R.id.update_dialog_btn_layout2);
		TextView tvMsg = (TextView) findChildViewById(R.id.tvDialogMsg);
		if (msg != null && !"".equals(msg))
		{
			tvMsg.setText(msg);
		}
		if (isForceUpdate)
		{
			setCanceledOnTouchOutside(false);
			setCancelable(false);
			layout1.setVisibility(View.GONE);
			layout2.setVisibility(View.VISIBLE);
			Button btnComfirm = (Button) findChildViewById(R.id.btSure);
			btnComfirm.setOnClickListener(new android.view.View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					dismiss();
					confirmClick.onConfirmDialog();
				}
			});
		}
		else
		{
			layout1.setVisibility(View.VISIBLE);
			layout2.setVisibility(View.GONE);
			Button btnCancel = (Button) findChildViewById(R.id.btCancel);
			Button btnComfirm = (Button) findChildViewById(R.id.btConfirm);
			btnCancel.setOnClickListener(new android.view.View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					dismiss();
				}
			});
			btnComfirm.setOnClickListener(new android.view.View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					dismiss();
					confirmClick.onConfirmDialog();
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

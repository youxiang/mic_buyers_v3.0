package com.micen.buyers.activity.rfq;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.focustech.common.capturepicture.CameraTakePhoto;
import com.focustech.common.capturepicture.GalleryTakePhoto;
import com.focustech.common.capturepicture.TakePhoto;
import com.focustech.common.capturepicture.TakePhoto.TakePhotoFailReason;
import com.focustech.common.capturepicture.TakePhoto.TakePhotoListener;
import com.focustech.common.mob.DisposeDataListener;
import com.focustech.common.util.ToastUtil;
import com.focustech.common.util.Utils;
import com.focustech.common.widget.dialog.CommonProgressDialog;
import com.micen.buyers.activity.BaseActivity;
import com.micen.buyers.activity.R;
import com.micen.buyers.activity.account.login.LoginActivity_;
import com.micen.buyers.activity.rfq.category.RFQCategoryActivity_;
import com.micen.buyers.adapter.rfq.PostRFQCategoryAdapter;
import com.micen.buyers.adapter.rfq.PostRFQStringAdapter;
import com.micen.buyers.constant.Constants;
import com.micen.buyers.http.RequestCenter;
import com.micen.buyers.manager.PopupManager;
import com.micen.buyers.manager.RFQKeyListener;
import com.micen.buyers.manager.SysManager;
import com.micen.buyers.module.LoginTarget;
import com.micen.buyers.module.category.SearchCategory;
import com.micen.buyers.module.category.SearchCategoryContent;
import com.micen.buyers.module.rfq.RFQContentFiles;
import com.micen.buyers.module.rfq.RFQResponseCode;
import com.micen.buyers.module.rfq.UploadFile;
import com.micen.buyers.util.BuyerApplication;
import com.micen.buyers.util.ImageUtil;
import com.micen.buyers.util.Util;
import com.micen.buyers.view.ScrollEditText;

/**********************************************************
 * @文件名称：RFQBaseActivity.java
 * @文件作者：xiongjiangwei
 * @创建时间：2014-1-24 上午10:21:52
 * @文件描述：RFQ父Activity
 * @修改历史：2014-1-24创建初始版本
 **********************************************************/
@EActivity
public class RFQPostBaseActivity extends BaseActivity
{
	@ViewById(R.id.mic_rfq_layout)
	protected RelativeLayout baseLayout;
	@ViewById(R.id.mic_rfq_scrollView)
	protected ScrollView baseScrollView;
	@ViewById(R.id.mic_rfq_edit_productName)
	protected EditText productNameEdit;
	@ViewById(R.id.mic_rfq_category)
	protected TextView categoryText;
	@ViewById(R.id.mic_rfq_purchaseQuantity)
	protected EditText purchaseQuantityEdit;
	@ViewById(R.id.mic_rfq_pieces)
	protected EditText piecesEdit;
	@ViewById(R.id.mic_rfq_expiredDate)
	protected TextView expiredDateText;
	@ViewById(R.id.mic_rfq_edit_description)
	protected ScrollEditText descriptionEdit;
	@ViewById(R.id.mic_rfq_optional_layout)
	protected LinearLayout optionalLayout;
	protected TextView optionalText;
	// 以下是更多选项
	@ViewById(R.id.mic_rfq_requirementsToSupplier_layout)
	protected LinearLayout requirementsToSupplierLayout;
	@ViewById(R.id.mic_rfq_supplier_layout)
	protected LinearLayout supplierLayout;
	@ViewById(R.id.mic_rfq_requirementsForTrading_layout)
	protected LinearLayout requirementsForTradingLayout;
	@ViewById(R.id.mic_rfq_trading_layout)
	protected LinearLayout tradingLayout;
	@ViewById(R.id.mic_rfq_paymentTerms)
	protected EditText paymentTermsEdit;

	@ViewById(R.id.mic_rfq_more_layout)
	protected LinearLayout moreMsgLayout;
	@ViewById(R.id.mic_rfq_capture_layout)
	protected LinearLayout captureLayout;

	@ViewById(R.id.mic_rfq_shipmentTerms)
	protected TextView shipmentTermsText;
	@ViewById(R.id.mic_rfq_fob)
	protected TextView fobText;
	@ViewById(R.id.mic_rfq_edit_targetPrice)
	protected EditText targetPriceEdit;
	@ViewById(R.id.mic_rfq_termsDetailed_layout)
	protected LinearLayout shipmentTermsLayout;
	@ViewById(R.id.mic_rfq_usd)
	protected EditText usdEdit;
	@ViewById(R.id.mic_rfq_edit_destinationPort)
	protected EditText destinationPortEdit;

	protected TakePhoto takePhoto;
	protected int popupViewWidth = 0;
	protected Intent mIntent;

	protected final String ADD_FILE_FLAG = "_$_0";
	protected final String UPDATE_FILE_FLAG = "_$_1";
	private int currentCaptureChildIndex = 0;
	private final int UPLOAD_FILE_COMPLETE = 0;
	private final int POST_RFQ_FAILED = 1;
	/**
	 * 日期选择器当前日期
	 */
	private String pickerCurrentDate = null;
	private DatePickerDialog datePickerDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_rfq);
		Constants.currentActivity = this;

	}

	@AfterViews
	protected void initView()
	{
		popupViewWidth = Utils.getWindowWidthPix(this) / 4 * 3;
		initTitleView();
		initCommonPart();
		initMorePart();
		initChildView();
	}

	@Override
	protected void onRestart()
	{
		super.onRestart();
		if (Constants.selectedCategories != null)
		{
			categoryText.setText(Constants.selectedCategories.catNameEn);
			categoryText.setTag(Constants.selectedCategories.catCode);
			Constants.selectedCategories = null;
		}
	}

	protected void initChildView()
	{

	}

	protected void startPostRFQ()
	{

	}

	@Override
	protected void onNewIntent(Intent intent)
	{
		mIntent = intent;
		boolean loginSuccess = mIntent.getBooleanExtra("loginSuccess", false);
		if (loginSuccess)
		{
			CommonProgressDialog.getInstance().showProgressDialog(this, getString(R.string.mic_loading));
			startUploadFile();
		}
	}

	protected void initTitleView()
	{
		titleLeftButton.setImageResource(R.drawable.ic_title_back);
		titleLeftButton.setOnClickListener(this);
		titleText.setText(R.string.mic_postRFQ);
		titleRightButton1.setVisibility(View.GONE);
		titleRightButton2.setVisibility(View.GONE);
		titleRightButton3.setOnClickListener(this);
		titleRightButton3.setImageResource(R.drawable.ic_post);
	}

	protected void initCommonPart()
	{
		productNameEdit.setKeyListener(RFQKeyListener.getInstance(Constants.rfqEditTextDigits));
		productNameEdit.addTextChangedListener(emptyWatcher);
		purchaseQuantityEdit.addTextChangedListener(emptyWatcher);
		piecesEdit.setOnClickListener(this);
		piecesEdit.addTextChangedListener(emptyWatcher);
		piecesEdit.setOnFocusChangeListener(focusChange);
		expiredDateText.setOnClickListener(this);
		expiredDateText.addTextChangedListener(emptyWatcher);
		descriptionEdit.setParentScrollView(baseScrollView);
		descriptionEdit.setKeyListener(RFQKeyListener.getInstance(Constants.rfqEditTextDigits));
		descriptionEdit.addTextChangedListener(emptyWatcher);
		optionalText = (TextView) optionalLayout.getChildAt(1);
		setRightCompoundDrawable(R.drawable.mic_rfq_optional_arrow_up, optionalText);
		optionalText.setText(R.string.mic_rfq_optional);
		optionalLayout.setOnClickListener(this);
		optionalLayout.setBackgroundDrawable(Utils.setImageButtonState(R.color.mic_rfq_expand_bg,
				R.color.mic_rfq_title_bg, this));
	}

	private TextWatcher emptyWatcher = new TextWatcher()
	{
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count)
		{
			setPostRFQButtonStatus();
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after)
		{

		}

		@Override
		public void afterTextChanged(Editable s)
		{

		}
	};

	/**
	 * 设置发送按钮状态
	 */
	protected void setPostRFQButtonStatus()
	{
		if (!"".equals(productNameEdit.getText().toString().trim())
				&& !"".equals(purchaseQuantityEdit.getText().toString())
				&& !"".equals(piecesEdit.getText().toString().trim())
				&& !"".equals(expiredDateText.getText().toString())
				&& !"".equals(descriptionEdit.getText().toString().trim()))
		{
			titleRightButton3.setClickable(true);
			titleRightButton3.setImageResource(R.drawable.ic_post);
		}
		else
		{
			titleRightButton3.setClickable(false);
			titleRightButton3.setImageResource(R.drawable.ic_post_gray);
		}
	}

	protected void initMorePart()
	{
		paymentTermsEdit.setOnClickListener(this);
		paymentTermsEdit.setOnFocusChangeListener(focusChange);
		paymentTermsEdit.setKeyListener(RFQKeyListener.getInstance(Constants.rfqEditTextDigits));
		shipmentTermsText.setOnClickListener(this);
		categoryText.setOnClickListener(this);
		usdEdit.setOnClickListener(this);
		usdEdit.setOnFocusChangeListener(focusChange);
		usdEdit.setKeyListener(RFQKeyListener.getInstance(Constants.rfqEditTextDigits));
		destinationPortEdit.setKeyListener(RFQKeyListener.getInstance(Constants.rfqEditTextDigits));
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		switch (v.getId())
		{
		case R.id.common_title_back_button:
			finish();
			break;
		case R.id.common_title_right_button3:
			if (Float.parseFloat(purchaseQuantityEdit.getText().toString()) <= 0
					|| (targetPriceEdit != null && !"".equals(targetPriceEdit.getText().toString()) && Float
							.parseFloat(targetPriceEdit.getText().toString()) <= 0))
			{
				ToastUtil.toast(this, R.string.unvalid_number);
				return;
			}
			if (BuyerApplication.getInstance().getUser() != null)
			{
				CommonProgressDialog.getInstance().showProgressDialog(this, getString(R.string.mic_loading));
				startUploadFile();
			}
			else
			{
				Intent postRFQIntent = new Intent();
				postRFQIntent.setClass(this, LoginActivity_.class);
				postRFQIntent.putExtra("loginTarget", LoginTarget.getValue(LoginTarget.PostRFQ));
				startActivity(postRFQIntent);
			}
			break;
		case R.id.mic_rfq_capture_image:
			SysManager.getInstance().dismissInputKey(this);
			PopupManager.getInstance().showRFQPopup(createPopupView(createCapturePopupView(v), popupBgClick));
			break;
		case R.id.mic_rfq_capture_del_image:
			if (captureLayout != null)
			{
				if (captureLayout.getChildCount() > 1)
				{
					captureLayout.removeView((RelativeLayout) v.getParent());
					ImageView preImage = (ImageView) captureLayout.getChildAt(captureLayout.getChildCount() - 1)
							.findViewById(R.id.mic_rfq_capture_image);
					if (preImage.getDrawable() != null)
					{
						captureLayout.addView(createCaptureEmptyChildView());
					}
				}
				else
				{
					((RelativeLayout) v.getParent()).findViewById(R.id.mic_rfq_capture_image).setBackgroundResource(
							R.drawable.mic_rfq_capture_add);
					v.setVisibility(View.GONE);
				}
			}
			break;
		case R.id.mic_rfq_category:// 目录事件响应
			SysManager.getInstance().dismissInputKey(this);
			if ("".equals(productNameEdit.getText().toString().trim()))
			{
				showCategoryPopup(new ArrayList<SearchCategoryContent>());
			}
			else
			{
				CommonProgressDialog.getInstance().showProgressDialog(this, getString(R.string.mic_loading));
				RequestCenter.getCategoryListByKeyword(categoryDataListener, productNameEdit.getText().toString()
						.trim(), false);
			}
			break;
		case R.id.mic_rfq_expiredDate:// Expired Date事件响应
			Calendar cd = Calendar.getInstance();
			Date date = new Date();
			if (!"".equals(((TextView) v).getText().toString()))
			{
				SimpleDateFormat sdf = new SimpleDateFormat(Constants.enDateTemplate, Util.getLocale());
				try
				{
					date = sdf.parse(((TextView) v).getText().toString());
				}
				catch (ParseException e)
				{
					e.printStackTrace();
				}
			}
			cd.setTime(date);
			datePickerDialog = new DatePickerDialog(RFQPostBaseActivity.this, datePickerSetListener,
					cd.get(Calendar.YEAR), cd.get(Calendar.MONTH), cd.get(Calendar.DAY_OF_MONTH));
			// 如果系统版本为2.3以上则执行此方法
			if (Build.VERSION.SDK_INT > 10)
			{
				// 设置日期选择器最小日期为当前日期+偏移量
				Calendar cal = Calendar.getInstance();
				cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
						+ Constants.DAY_OFFSET);
				cal.set(Calendar.HOUR_OF_DAY, cal.getMinimum(Calendar.HOUR_OF_DAY));
				cal.set(Calendar.MINUTE, cal.getMinimum(Calendar.MINUTE));
				cal.set(Calendar.SECOND, cal.getMinimum(Calendar.SECOND));
				cal.set(Calendar.MILLISECOND, cal.getMinimum(Calendar.MILLISECOND));
				DatePicker datePicker = datePickerDialog.getDatePicker();
				datePicker.setMinDate(cal.getTimeInMillis());
			}
			datePickerDialog.setCanceledOnTouchOutside(true);
			datePickerDialog.show();
			break;
		case R.id.mic_rfq_shipmentTerms:
			SysManager.getInstance().dismissInputKey(this);
			PopupManager.getInstance().showRFQPopup(
					createPopupView(
							createStringListPopupView(createStringListPopupAdapter(R.array.mic_rfq_shipment_terms),
									shipmentTermsItemClick), popupBgClick));
			break;
		}
	}

	/**
	 * 根据数组资源ID创建列表适配器
	 * @param arrayResId
	 * @return
	 */
	protected PostRFQStringAdapter createStringListPopupAdapter(int arrayResId)
	{
		PostRFQStringAdapter adapter = new PostRFQStringAdapter(this, getResources().getStringArray(arrayResId));
		return adapter;
	}

	/**
	 * 弹出目录选择泡泡
	 * @param dataList
	 */
	private void showCategoryPopup(ArrayList<SearchCategoryContent> dataList)
	{
		dataList.add(createEmptyCategory());
		PopupManager.getInstance().showRFQPopup(createPopupView(createCategoryPopupView(dataList), popupBgClick));
	}

	private DisposeDataListener categoryDataListener = new DisposeDataListener()
	{
		@Override
		public void onSuccess(Object result)
		{
			CommonProgressDialog.getInstance().dismissProgressDialog();
			showCategoryPopup(((SearchCategory) result).content);
		}

		public void onFailure(Object failedReason)
		{
			CommonProgressDialog.getInstance().dismissProgressDialog();
			ToastUtil.toast(RFQPostBaseActivity.this, failedReason);
		};
	};

	/**
	 * 设置日期选择器日期改变监听
	 * @return
	 */
	private OnDateSetListener datePickerSetListener = new OnDateSetListener()
	{
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
		{
			pickerCurrentDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Util.getLocale());
			DateFormat df = new SimpleDateFormat(Constants.enDateTemplate, Util.getLocale());
			try
			{
				if (formatter.parse(pickerCurrentDate).after(Util.getMinTime()))
				{
					expiredDateText.setText(df.format(formatter.parse(pickerCurrentDate)));
				}
				else
				{
					ToastUtil.toast(RFQPostBaseActivity.this, R.string.time_validate);
				}
			}
			catch (ParseException e)
			{
				e.printStackTrace();
			}
		}
	};

	/**
	 * 设置文本控件的右侧填充图
	 * @param drawableResId
	 * @param textView
	 */
	protected void setRightCompoundDrawable(int drawableResId, TextView textView)
	{
		if (textView == null)
			return;
		Drawable drawable = getResources().getDrawable(drawableResId);
		drawable.setBounds(0, 0, Util.dip2px(20), Util.dip2px(11));
		textView.setCompoundDrawables(null, null, drawable, null);
	}

	/**
	 * 展开更多选项的时候控制滚动布局自动滚到底部
	 */
	protected void scrollToBottom()
	{
		if (handler != null && baseScrollView != null)
		{
			handler.post(new Runnable()
			{
				@Override
				public void run()
				{
					baseScrollView.fullScroll(ScrollView.FOCUS_DOWN);
				}
			});
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		afterTakeCaptureOperate(requestCode, resultCode, data);
	}

	/**
	 * 图片获取完成后的处理方法
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	private void afterTakeCaptureOperate(int requestCode, int resultCode, Intent data)
	{
		if (takePhoto != null)
		{
			takePhoto.onActivityResult(requestCode, resultCode, data);
		}
		if (captureLayout != null)
		{
			ImageView preImage = (ImageView) captureLayout.getChildAt(captureLayout.getChildCount() - 1).findViewById(
					R.id.mic_rfq_capture_image);
			if (captureLayout.getChildCount() < 3 && preImage.getDrawable() != null)
			{
				captureLayout.addView(createCaptureEmptyChildView());
			}
		}
	}

	/**
	 * 创建数量弹出框列表布局
	 * @param adapter
	 * @return
	 */
	protected LinearLayout createStringListPopupView(PostRFQStringAdapter adapter, OnItemClickListener itemClick)
	{
		LinearLayout contentLayout = new LinearLayout(this);
		contentLayout.setBackgroundResource(R.color.mic_white);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		contentLayout.setLayoutParams(params);
		contentLayout.setOrientation(LinearLayout.VERTICAL);

		ListView listView = new ListView(this);
		LayoutParams listViewParams = new LayoutParams(popupViewWidth, LayoutParams.WRAP_CONTENT);
		listView.setLayoutParams(listViewParams);
		listView.setCacheColorHint(0);
		listView.setSelector(getResources().getDrawable(R.color.transparent));
		listView.setDivider(getResources().getDrawable(R.color.mic_menu_line));
		listView.setDividerHeight(Util.dip2px(0.5f));
		listView.setOnItemClickListener(itemClick);
		listView.setAdapter(adapter);
		contentLayout.addView(listView);
		return contentLayout;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	protected ImageView createPopupBgView(OnClickListener click)
	{
		ImageView bgImage = new ImageView(this);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		bgImage.setLayoutParams(params);
		bgImage.setImageResource(R.color.black);
		if (Build.VERSION.SDK_INT > 10)
		{
			bgImage.setAlpha(0.6f);
		}
		else
		{
			bgImage.setAlpha(153);
		}
		bgImage.setOnClickListener(click);
		return bgImage;
	}

	protected OnClickListener popupBgClick = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			PopupManager.getInstance().dismissPopup();
		}
	};

	protected OnItemClickListener unitPopupItemClick = new OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
		{
			PopupManager.getInstance().dismissPopup();
			if (getString(R.string.mic_rfq_other).equals(arg0.getAdapter().getItem(arg2).toString()))
			{
				piecesEdit.setText("");
				piecesEdit.setFocusable(true);
				piecesEdit.setFocusableInTouchMode(true);
				InputMethodManager inputManager = (InputMethodManager) piecesEdit.getContext().getSystemService(
						Context.INPUT_METHOD_SERVICE);
				inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				piecesEdit.requestFocus();
			}
			else
			{
				piecesEdit.setText(arg0.getAdapter().getItem(arg2).toString());
				piecesEdit.setFocusable(false);
				piecesEdit.setFocusableInTouchMode(false);
			}
		}
	};

	protected OnItemClickListener shipmentTermsItemClick = new OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
		{
			PopupManager.getInstance().dismissPopup();
			shipmentTermsLayout.setVisibility(View.VISIBLE);
			String selectedText = arg0.getAdapter().getItem(arg2).toString();
			shipmentTermsText.setText(selectedText);
			fobText.setText(selectedText);
			// EXW、FOB、FCA、FAS不需要显示此项，故arg2 > 3就可以筛除这些选项
			destinationPortEdit.setVisibility(isShowDestinationPort(selectedText) ? View.VISIBLE : View.GONE);
			scrollToBottom();
		}
	};

	/**
	 * 是否显示目的地
	 * @param key
	 * @return
	 */
	protected boolean isShowDestinationPort(String key)
	{
		return !"EXW".equals(key) && !"FOB".equals(key) && !"FCA".equals(key) && !"FAS".equals(key);
	}

	protected OnItemClickListener usdItemClick = new OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
		{
			PopupManager.getInstance().dismissPopup();
			if (getString(R.string.mic_rfq_other).equals(arg0.getAdapter().getItem(arg2).toString()))
			{
				usdEdit.setText("");
				usdEdit.setFocusable(true);
				usdEdit.setFocusableInTouchMode(true);
				InputMethodManager inputManager = (InputMethodManager) usdEdit.getContext().getSystemService(
						Context.INPUT_METHOD_SERVICE);
				inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				usdEdit.requestFocus();
			}
			else
			{
				usdEdit.setText(arg0.getAdapter().getItem(arg2).toString());
				usdEdit.setFocusable(false);
				usdEdit.setFocusableInTouchMode(false);
			}
		}
	};

	protected OnFocusChangeListener focusChange = new OnFocusChangeListener()
	{
		@Override
		public void onFocusChange(View v, boolean hasFocus)
		{
			if (!hasFocus)
			{
				v.setFocusable(false);
			}
		}
	};

	/**
	 * 创建目录弹出框布局
	 * @param dataList
	 * @return
	 */
	private LinearLayout createCategoryPopupView(ArrayList<SearchCategoryContent> dataList)
	{
		LinearLayout contentLayout = new LinearLayout(this);
		contentLayout.setBackgroundResource(R.color.mic_white);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		contentLayout.setLayoutParams(params);
		contentLayout.setOrientation(LinearLayout.VERTICAL);

		ListView listView = new ListView(this);
		LayoutParams listViewParams = new LayoutParams(popupViewWidth, LayoutParams.WRAP_CONTENT);
		listView.setLayoutParams(listViewParams);
		listView.setCacheColorHint(0);
		listView.setSelector(getResources().getDrawable(R.color.transparent));
		listView.setDivider(getResources().getDrawable(R.color.mic_menu_line));
		listView.setDividerHeight(Util.dip2px(0.5f));
		listView.setOnItemClickListener(categoryPopupItemClick);
		PostRFQCategoryAdapter adapter = new PostRFQCategoryAdapter(this, dataList);
		listView.setAdapter(adapter);
		contentLayout.addView(listView);
		return contentLayout;
	}

	private OnItemClickListener categoryPopupItemClick = new OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
		{
			PopupManager.getInstance().dismissPopup();
			if (arg2 != arg0.getAdapter().getCount() - 1)
			{
				SearchCategoryContent category = (SearchCategoryContent) arg0.getAdapter().getItem(arg2);
				categoryText.setText(category.getAllCatNames());
				categoryText.setTag(category.catCode);
			}
			else
			{
				Intent intent = new Intent();
				intent.setClass(RFQPostBaseActivity.this, RFQCategoryActivity_.class);
				intent.putExtra("isSearchCategory", true);
				startActivity(intent);
			}
		}
	};

	private SearchCategoryContent createEmptyCategory()
	{
		SearchCategoryContent emptyContent = new SearchCategoryContent();
		emptyContent.catCode = "-1";
		emptyContent.catNames = new ArrayList<String>();
		emptyContent.catNames.add(getString(R.string.all_categories));
		return emptyContent;
	}

	protected RelativeLayout createPopupView(View contentView, OnClickListener bgClick)
	{
		RelativeLayout popupLayout = new RelativeLayout(this);
		popupLayout.addView(createPopupBgView(bgClick));
		popupLayout.addView(contentView);
		return popupLayout;
	}

	/**
	 * 创建图片子布局
	 * @return
	 */
	protected RelativeLayout createCaptureEmptyChildView()
	{
		RelativeLayout childItemLayout = new RelativeLayout(this);
		LayoutParams params = new LayoutParams(Util.dip2px(90), Util.dip2px(90));
		params.leftMargin = params.rightMargin = Util.dip2px(10);
		childItemLayout.setLayoutParams(params);

		ImageView image = new ImageView(this);
		image.setId(R.id.mic_rfq_capture_image);
		RelativeLayout.LayoutParams relParams = new RelativeLayout.LayoutParams(Util.dip2px(80), Util.dip2px(80));
		relParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT | RelativeLayout.ALIGN_PARENT_BOTTOM);
		image.setLayoutParams(relParams);
		image.setBackgroundResource(R.drawable.mic_rfq_capture_add);
		image.setOnClickListener(this);
		childItemLayout.addView(image);

		ImageView delImage = new ImageView(this);
		delImage.setId(R.id.mic_rfq_capture_del_image);
		relParams = new RelativeLayout.LayoutParams(Util.dip2px(22), Util.dip2px(22));
		relParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT | RelativeLayout.ALIGN_PARENT_TOP);
		delImage.setLayoutParams(relParams);
		delImage.setBackgroundResource(R.drawable.mic_rfq_capture_delete);
		delImage.setOnClickListener(this);
		delImage.setVisibility(View.GONE);
		childItemLayout.addView(delImage);
		return childItemLayout;
	}

	/**
	 * 创建图片子布局
	 * @return
	 */
	protected RelativeLayout createCaptureChildView(RFQContentFiles file)
	{
		RelativeLayout childItemLayout = new RelativeLayout(this);
		LayoutParams params = new LayoutParams(Util.dip2px(90), Util.dip2px(90));
		params.leftMargin = params.rightMargin = Util.dip2px(10);
		childItemLayout.setLayoutParams(params);

		ImageView image = new ImageView(this);
		image.setId(R.id.mic_rfq_capture_image);
		RelativeLayout.LayoutParams relParams = new RelativeLayout.LayoutParams(Util.dip2px(80), Util.dip2px(80));
		relParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT | RelativeLayout.ALIGN_PARENT_BOTTOM);
		image.setLayoutParams(relParams);
		if ("pic".equals(file.fileType))
		{
			ImageUtil.getImageLoader().displayImage(file.fileUrl, image);
		}
		else
		{
			image.setImageResource(R.drawable.mic_rfq_capture_file);
		}
		image.setBackgroundResource(R.drawable.mic_rfq_capture_add);
		image.setOnClickListener(this);
		image.setScaleType(ScaleType.FIT_XY);
		image.setTag(file);
		childItemLayout.addView(image);

		ImageView delImage = new ImageView(this);
		delImage.setId(R.id.mic_rfq_capture_del_image);
		relParams = new RelativeLayout.LayoutParams(Util.dip2px(22), Util.dip2px(22));
		relParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT | RelativeLayout.ALIGN_PARENT_TOP);
		delImage.setLayoutParams(relParams);
		delImage.setBackgroundResource(R.drawable.mic_rfq_capture_delete);
		delImage.setOnClickListener(this);
		childItemLayout.addView(delImage);
		return childItemLayout;
	}

	/**
	 * 创建图片获取方式弹出层布局
	 * @param displayView
	 * @return
	 */
	protected LinearLayout createCapturePopupView(View displayView)
	{
		LinearLayout contentLayout = new LinearLayout(this);
		contentLayout.setBackgroundResource(R.color.mic_white);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(popupViewWidth, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		contentLayout.setLayoutParams(params);
		contentLayout.setOrientation(LinearLayout.VERTICAL);

		contentLayout.addView(createCaptureText(R.id.mic_rfq_popup_gallery, R.string.mic_rfq_capture_gallery,
				displayView));

		View line = new View(this);
		LayoutParams lineParams = new LayoutParams(LayoutParams.MATCH_PARENT, Util.dip2px(0.5f));
		line.setBackgroundResource(R.color.mic_menu_line);
		line.setLayoutParams(lineParams);
		contentLayout.addView(line);

		contentLayout
				.addView(createCaptureText(R.id.mic_rfq_popup_camera, R.string.mic_rfq_capture_camera, displayView));
		return contentLayout;
	}

	/**
	 * 创建图片获取方式弹出层布局中的文本控件
	 * @param id
	 * @param textResId
	 * @param displayView
	 * @return
	 */
	private TextView createCaptureText(int id, int textResId, View displayView)
	{
		TextView textView = new TextView(this);
		textView.setId(id);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, Util.dip2px(48));
		textView.setLayoutParams(params);
		textView.setGravity(Gravity.CENTER);
		textView.setBackgroundResource(R.drawable.mic_common_menu_item_bg);
		textView.setText(textResId);
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		textView.setTextColor(getResources().getColor(R.color.mic_home_menu_text));
		textView.setOnClickListener(captureGalleryViewOnClick(displayView));
		return textView;
	}

	/**
	 * 设置图片获取方式弹出层事件
	 * @param displayView
	 * @return
	 */
	private OnClickListener captureGalleryViewOnClick(final View displayView)
	{
		OnClickListener click = new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				switch (v.getId())
				{
				case R.id.mic_rfq_popup_gallery:
					takePhoto = new GalleryTakePhoto(RFQPostBaseActivity.this, displayView, listener);
					break;
				case R.id.mic_rfq_popup_camera:
					takePhoto = new CameraTakePhoto(RFQPostBaseActivity.this, displayView, listener);
					break;
				}
				PopupManager.getInstance().dismissPopup();
			}
		};
		return click;
	}

	protected TakePhotoListener listener = new TakePhotoListener()
	{
		@Override
		public void onSuccess(String imagePath, View displayView, Bitmap bitmap)
		{
			// 根据传入的控件ID区分
			RFQContentFiles file = new RFQContentFiles();
			file.fileLocalPath = imagePath;
			if (displayView != null)
			{
				displayView.setTag(file);
				// 保存完图片路径后显示删除按钮
				((RelativeLayout) displayView.getParent()).findViewById(R.id.mic_rfq_capture_del_image).setVisibility(
						View.VISIBLE);
			}
		}

		@Override
		public void onFail(TakePhotoFailReason failReason)
		{
		}
	};

	protected Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case UPLOAD_FILE_COMPLETE:
				startPostRFQ();
				break;
			case POST_RFQ_FAILED:
				CommonProgressDialog.getInstance().dismissProgressDialog();
				ToastUtil.toast(RFQPostBaseActivity.this, R.string.mic_rfq_post_response_msg2);
				break;
			}
		};
	};

	protected void startUploadFile()
	{
		if (currentCaptureChildIndex >= captureLayout.getChildCount())
		{
			handler.sendEmptyMessage(UPLOAD_FILE_COMPLETE);
			return;
		}
		ImageView imageView = (ImageView) captureLayout.getChildAt(currentCaptureChildIndex).findViewById(
				R.id.mic_rfq_capture_image);
		if (imageView.getTag() == null)
		{
			handler.sendEmptyMessage(UPLOAD_FILE_COMPLETE);
			return;
		}
		RFQContentFiles files = (RFQContentFiles) imageView.getTag();
		if ((files.fileId != null && !"".equals(files.fileId) && files.fileId.indexOf(ADD_FILE_FLAG) > 0)
				|| files.fileLocalPath == null)
		{
			currentCaptureChildIndex++;
			startUploadFile();
		}
		if (files.fileLocalPath != null)
		{
			RequestCenter.uploadFile(uploadFileListener, files.fileLocalPath);
		}
	}

	private DisposeDataListener uploadFileListener = new DisposeDataListener()
	{
		@Override
		public void onSuccess(Object result)
		{
			ImageView imageView = (ImageView) captureLayout.getChildAt(currentCaptureChildIndex).findViewById(
					R.id.mic_rfq_capture_image);
			RFQContentFiles files = (RFQContentFiles) imageView.getTag();
			UploadFile uploadFile = (UploadFile) result;
			if (files.fileId == null || "".equals(files.fileId))
			{
				files.fileId = uploadFile.content + ADD_FILE_FLAG;
			}
			imageView.setTag(files);
			currentCaptureChildIndex++;
			startUploadFile();
		}

		@Override
		public void onFailure(Object failedReason)
		{
			handler.sendEmptyMessage(POST_RFQ_FAILED);
			ToastUtil.toast(RFQPostBaseActivity.this, failedReason);
		}
	};

	protected void toastResponseMsg(String failedReason)
	{
		switch (RFQResponseCode.getCodeValueByTag(failedReason))
		{
		case RFQ_FAIL:
			ToastUtil.toast(this, R.string.mic_rfq_post_response_msg2);
			break;
		case RFQ_NOT_AUTHORITY:
			ToastUtil.toast(this, R.string.mic_rfq_post_response_msg3);
			break;
		case RFQ_ID_CANNOT_NULL:
			ToastUtil.toast(this, R.string.mic_rfq_post_response_msg4);
			break;
		case RFQ_COM_IS_NULL:
			ToastUtil.toast(this, R.string.mic_rfq_post_response_msg5);
			break;
		case RFQ_COM_NOT_EXIST:
			ToastUtil.toast(this, R.string.mic_rfq_post_response_msg6);
			break;
		case RFQ_OPERATORNO_NOT_EXIST:
			ToastUtil.toast(this, R.string.mic_rfq_post_response_msg7);
			break;
		case RFQ_USER_NOT_EXIST:
			ToastUtil.toast(this, R.string.mic_rfq_post_response_msg8);
			break;
		case RFQ_SUBJECT_CANNOT_NULL:
			ToastUtil.toast(this, R.string.mic_rfq_post_response_msg9);
			break;
		case RFQ_SUBJECT_FLUNK:
			ToastUtil.toast(this, R.string.mic_rfq_post_response_msg10);
			break;
		case RFQ_SUBJECT_OVERDO:
			ToastUtil.toast(this, R.string.mic_rfq_post_response_msg11);
			break;
		case RFQ_CATEGORYID_NOT_EXIST:
			ToastUtil.toast(this, R.string.mic_rfq_post_response_msg12);
			break;
		case RFQ_DESCRIPTION_NOT_EXIST:
			ToastUtil.toast(this, R.string.mic_rfq_post_response_msg13);
			break;
		case RFQ_DESCRIPTION_FLUNK:
			ToastUtil.toast(this, R.string.mic_rfq_post_response_msg14);
			break;
		case RFQ_DESCRIPTION_OVERDO:
			ToastUtil.toast(this, R.string.mic_rfq_post_response_msg15);
			break;
		case RFQ_QUANTITY_NOT_EXIST:
			ToastUtil.toast(this, R.string.mic_rfq_post_response_msg16);
			break;
		case RFQ_QUANTITY_IS_NUMBER:
			ToastUtil.toast(this, R.string.mic_rfq_post_response_msg17);
			break;
		case RFQ_ENDTIME_NOT_EXIST:
			ToastUtil.toast(this, R.string.mic_rfq_post_response_msg18);
			break;
		case RFQ_UNKNOW_ERROR:
			ToastUtil.toast(this, R.string.mic_rfq_post_response_msg19);
			break;
		default:
			ToastUtil.toast(this, failedReason);
			break;
		}
	}

	/**
	 * 设置输入框光标位置
	 * @param editText
	 */
	protected void setEditTextCursorPosition(EditText editText)
	{
		CharSequence text = editText.getText();
		if (text instanceof Spannable)
		{
			Spannable spanText = (Spannable) text;
			Selection.setSelection(spanText, text.length());
		}
	}

	/**
	 * 设置默认时间为当天偏移四十天
	 */
	protected void initExpireDate(int dayOffset)
	{
		// 设置日期选择器最小日期为当前日期+偏移量
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + dayOffset);
		cal.set(Calendar.HOUR_OF_DAY, cal.getMinimum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getMinimum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getMinimum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getMinimum(Calendar.MILLISECOND));
		expiredDateText.setText(new SimpleDateFormat(Constants.enDateTemplate, Util.getLocale()).format(new Date(cal
				.getTimeInMillis())));
	}
}

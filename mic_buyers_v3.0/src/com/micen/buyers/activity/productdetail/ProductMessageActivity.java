package com.micen.buyers.activity.productdetail;

import java.util.ArrayList;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.Sharer.Result;
import com.facebook.share.model.ShareLinkContent;
import com.focustech.common.mob.DisposeDataListener;
import com.focustech.common.util.LogUtil;
import com.focustech.common.util.ToastUtil;
import com.micen.buyers.activity.BaseFragmentActivity;
import com.micen.buyers.activity.R;
import com.micen.buyers.activity.account.login.LoginActivity_;
import com.micen.buyers.activity.message.MailSendActivity_;
import com.micen.buyers.constant.Constants;
import com.micen.buyers.db.DBDataHelper;
import com.micen.buyers.db.DBHelper;
import com.micen.buyers.http.RequestCenter;
import com.micen.buyers.manager.FavouriteManager;
import com.micen.buyers.manager.FavouriteManager.FavouriteCallback;
import com.micen.buyers.manager.FavouriteManager.FavouriteType;
import com.micen.buyers.manager.SysManager;
import com.micen.buyers.module.LoginTarget;
import com.micen.buyers.module.MailSendTarget;
import com.micen.buyers.module.db.Module;
import com.micen.buyers.module.db.ProductHistory;
import com.micen.buyers.module.product.ProductDetail;
import com.micen.buyers.module.product.ProductDetailContent;
import com.micen.buyers.module.product.ProductKeyValuePair;
import com.micen.buyers.util.BuyerApplication;
import com.micen.buyers.util.Util;
import com.micen.buyers.view.SearchListProgressBar;
import com.micen.buyers.view.product.ProductDescriptionFragment;
import com.micen.buyers.view.product.ProductFragment;
import com.micen.buyers.widget.ShareDialog;
import com.micen.buyers.widget.ShareDialog.OnShareDialogListener;
import com.micen.buyers.widget.other.PullToNextAdapter;
import com.micen.buyers.widget.other.PullToNextLayout;
import com.micen.buyers.widget.other.PullToNextLayout.LayoutType;
import com.micen.twitter.android.TwitterUtil;

/**********************************************************
 * @文件名称：ProductMessageActivity.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年3月30日 上午10:39:40
 * @文件描述：
 * @修改历史：2015年3月30日创建初始版本
 **********************************************************/
@EActivity(R.layout.activity_productdetail_layout)
public class ProductMessageActivity extends BaseFragmentActivity implements OnShareDialogListener
{
	@ViewById(R.id.root)
	protected View rootLayout;
	@ViewById(R.id.product_title_layout)
	protected View titleLayout;
	@ViewById(R.id.pull_to_next_layout)
	protected PullToNextLayout pullToNextLayout;
	@ViewById(R.id.btn_send_inquiry)
	protected View sendInquiryBtn;
	@ViewById(R.id.progressbar_layout)
	protected SearchListProgressBar progressBar;

	private String productId;
	private ProductDetailContent detail;

	private Fragment prudctFragment;

	private Fragment productDescFragment;

	private ArrayList<Fragment> list;

	private com.facebook.share.widget.ShareDialog facebookDialog;

	CallbackManager callbackManager;
	private FacebookCallback<Sharer.Result> facebookCallback = new FacebookCallback<Sharer.Result>()
	{

		@Override
		public void onCancel()
		{
			LogUtil.d("facebook", "onCancel");
			ToastUtil.toast(ProductMessageActivity.this, R.string.Share_Canceled);
		}

		@Override
		public void onError(FacebookException ex)
		{
			LogUtil.d("facebook", "onError " + ex);
			ToastUtil.toast(ProductMessageActivity.this, R.string.Share_Failed);
		}

		@Override
		public void onSuccess(Result result)
		{
			LogUtil.d("facebook", "onSuccess " + result);
			ToastUtil.toast(ProductMessageActivity.this, R.string.Share_Successful);
		}
	};

	@AfterViews
	protected void initView()
	{
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		productId = bundle.getString("productId");
		titleLeftButton.setImageResource(R.drawable.ic_title_back);
		titleRightButton1.setImageResource(R.drawable.icon_unfavourit);
		titleRightButton2.setImageResource(R.drawable.icon_share_normal);
		titleRightButton3.setVisibility(View.GONE);
		titleLeftButton.setOnClickListener(this);
		titleRightButton1.setOnClickListener(this);
		titleRightButton2.setOnClickListener(this);
		sendInquiryBtn.setOnClickListener(this);
		list = new ArrayList<Fragment>();
		stratGetProductDetail();
		FacebookSdk.sdkInitialize(this);
		callbackManager = CallbackManager.Factory.create();
		facebookDialog = new com.facebook.share.widget.ShareDialog(this);
		facebookDialog.registerCallback(callbackManager, facebookCallback);
	}

	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		switch (v.getId())
		{
		case R.id.common_title_back_button:
			onBackPressed();
			break;
		case R.id.common_title_right_button1:
			// add/remove favorite
			if (BuyerApplication.getInstance().getUser() == null)
			{
				Intent ins = new Intent(this, LoginActivity_.class);
				ins.putExtra("loginTarget", LoginTarget.getValue(LoginTarget.ClickForFavorite));
				startActivityForResult(ins, Constants.LOGIN_FAVOURITE);
				return;
			}

			// 事件统计 93 收藏产品 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c93);

			addOrDelFavourite(v);
			break;
		case R.id.common_title_right_button2:

			// 事件统计94 分享产品 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c94);
			// share
			shareDialog();
			// if (detail != null)
			// {
			// showShare();
			// }
			break;
		case R.id.btn_send_inquiry:
			if (pullToNextLayout.isPrevious)
			{
				// 事件统计 100 点击发送询盘（产品详情页） 点击事件
				SysManager.analysis(R.string.a_type_click, R.string.c100);
			}
			else
			{
				// 事件统计101 点击发送询盘（产品属性页） 点击事件
				SysManager.analysis(R.string.a_type_click, R.string.c101);

			}
			if (detail != null)
			{
				sendInquiry();
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus)
	{
		if (hasFocus)
		{
			FavouriteManager.getInstance().checkIsFavourite(this, titleRightButton1, FavouriteType.Product, productId,
					new FavouriteCallback()
					{
						@Override
						public void onCallback(boolean result)
						{
							if (detail != null)
							{
								detail.isFavorite = String.valueOf(result);
							}
						}
					});
		}
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}

	private void sendInquiry()
	{
		Intent intent = new Intent(this, MailSendActivity_.class);
		intent.putExtra("mailSendTarget", MailSendTarget.getValue(MailSendTarget.SendByProductId));
		intent.putExtra("subject", detail.name);
		intent.putExtra("companyName", detail.companyName);
		intent.putExtra("companyId", detail.companyId);
		intent.putExtra("productId", productId);// 对产品发送询盘
		intent.putExtra("quiry_flag", Constants.INQ_P);// 对产品发送询盘
		intent.putExtra("catCode", detail.catCode);
		startActivity(intent);
	}

	private ShareDialog dialog;

	@SuppressWarnings(
	{ "deprecation" })
	private void shareDialog()
	{
		if (dialog == null)
		{
			dialog = new ShareDialog(this, this);

			WindowManager manager = getWindowManager();
			Display display = manager.getDefaultDisplay();
			WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
			p.height = WindowManager.LayoutParams.WRAP_CONTENT;
			p.width = display.getWidth();
			dialog.getWindow().setAttributes(p);
			dialog.getWindow().setGravity(Gravity.BOTTOM);
		}

		dialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		// handle facebook response
		callbackManager.onActivityResult(requestCode, resultCode, data);
		if (Constants.LOGIN_FAVOURITE == requestCode && RESULT_OK == resultCode)
		{
			// favorite
			addOrDelFavourite(titleRightButton1);
		}
	}

	/**
	 * 添加或删除收藏
	 * @param displayView
	 */
	private void addOrDelFavourite(View displayView)
	{
		if (detail != null)
		{
			FavouriteManager.getInstance().addOrDelFavourite(this, displayView, FavouriteType.Product, productId,
					detail.name, detail.isFavorite(), new FavouriteCallback()
					{
						@Override
						public void onCallback(boolean result)
						{
							detail.isFavorite = String.valueOf(result);
						}
					});

		}
	}

	/**
	 * 请求产品详情数据...
	 */
	private void stratGetProductDetail()
	{
		RequestCenter.getProductDetail(productDetailListener, productId);
	}

	/**
	 * 请求产品详情响应事件
	 */
	private DisposeDataListener productDetailListener = new DisposeDataListener()
	{
		@Override
		public void onSuccess(Object arg0)
		{
			if (isFinishing())
			{
				return;
			}
			progressBar.setVisibility(View.GONE);
			pullToNextLayout.setVisibility(View.VISIBLE);
			detail = ((ProductDetail) arg0).content;
			try
			{
				if (detail != null)
				{
					JSONObject tradeInfoObj = new JSONObject(detail.tradeInfo);
					/**
					 * 初始化产品详情页面的交易信息
					 */
					detail.tradeInfoList = new ArrayList<ProductKeyValuePair>();
					Util.setJsonValueToPairList(tradeInfoObj, detail.tradeInfoList);
					if (tradeInfoObj.has("Price"))
					{
						detail.pricevalue = tradeInfoObj.getString("Price");
					}
					if (tradeInfoObj.has("orderUnit"))
					{
						detail.orderUnit = tradeInfoObj.getString("orderUnit");
					}

					if (tradeInfoObj.has("Trade Terms"))
					{
						detail.tradeTerms = tradeInfoObj.getString("Trade Terms");
					}
					/**
					 * 初始化产品详情列表页面的basicinfo
					 */
					JSONObject basicInfoObj = new JSONObject(detail.basicInfo);
					detail.basicInfoList = new ArrayList<ProductKeyValuePair>();
					Util.setJsonValueToPairList(basicInfoObj, detail.basicInfoList);
					/**
					 * 初始化产品详情列表页面的addationInfo
					 */
					JSONObject additionalInfoObj = new JSONObject(detail.additionalInfo);
					detail.additionalInfoList = new ArrayList<ProductKeyValuePair>();
					Util.setJsonValueToPairList(additionalInfoObj, detail.additionalInfoList);

					/**
					 * 浏览记录查入到数据库表
					 */
					addDatabase(detail);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			prudctFragment = new ProductFragment(detail, titleLayout);
			productDescFragment = new ProductDescriptionFragment(detail);
			list.add(prudctFragment);
			list.add(productDescFragment);
			pullToNextLayout.setLayoutType(LayoutType.PRODUCT_DETAIL);
			pullToNextLayout.setAdapter(new PullToNextAdapter(getSupportFragmentManager(), list));
		}

		@Override
		public void onFailure(Object failedReason)
		{
			ToastUtil.toast(ProductMessageActivity.this, failedReason);
			finish();
		}
	};

	/**
	 * 更新产品浏览历史数据
	 */
	private void addDatabase(ProductDetailContent detail)
	{
		ProductHistory productBrowseVisitHisBean = new ProductHistory();
		ArrayList<Module> modules = DBDataHelper.getInstance().select(DBHelper.PRODUCT_BROWSE_HISTORY_,
				DBHelper.CATEGORY_PRODUCT_ID_, DBHelper.CATEGORY_PRODUCT_ID_, detail.productId, null,
				ProductHistory.class);
		if (modules != null && modules.size() != 0)
		{
			DBDataHelper.getInstance().delete(DBHelper.PRODUCT_BROWSE_HISTORY_,
					DBHelper.CATEGORY_PRODUCT_ID_ + "=" + detail.productId, null);
		}
		productBrowseVisitHisBean.productId = detail.productId;
		productBrowseVisitHisBean.productName = detail.name;
		productBrowseVisitHisBean.companyName = detail.companyName;
		productBrowseVisitHisBean.productImageUrl = detail.images.get(0);
		productBrowseVisitHisBean.unitPrice = detail.pricevalue;
		productBrowseVisitHisBean.unitType = detail.pricevalue;
		productBrowseVisitHisBean.minOrder = detail.orderUnit;
		productBrowseVisitHisBean.tradeTerms = detail.tradeTerms;
		productBrowseVisitHisBean.goldMember = detail.memberType;
		productBrowseVisitHisBean.fProduct = detail.isFeature;
		productBrowseVisitHisBean.as_ = detail.auditType;
		productBrowseVisitHisBean.categoryId = detail.catCode;
		productBrowseVisitHisBean.vistTime = String.valueOf(System.currentTimeMillis());
		DBDataHelper.getInstance().insert(DBHelper.PRODUCT_BROWSE_HISTORY_, productBrowseVisitHisBean);
	}

	@Override
	public void onFaceBookClick()
	{
		// 事件统计102 选择Facebook分享 点击事件
		SysManager.analysis(R.string.a_type_click, R.string.c102);

		if (com.facebook.share.widget.ShareDialog.canShow(ShareLinkContent.class))
		{
			ShareLinkContent linkContent = new ShareLinkContent.Builder()
					.setContentTitle(getString(R.string.share_content1, detail.name))
					.setContentDescription(getString(R.string.share_content2))
					.setImageUrl(Uri.parse(detail.images.get(0))).setContentUrl(Uri.parse(detail.webAddress)).build();

			facebookDialog.show(linkContent);
		}
	}

	@Override
	public void onTwitterClick()
	{
		// 事件统计103 选择Twitter分享 点击事件
		SysManager.analysis(R.string.a_type_click, R.string.c103);

		TwitterUtil t = new TwitterUtil(this);
		if (!t.isAuthenticated())
		{
			t.login();
		}
		else
		{
			try
			{
				Intent intent = new Intent(this, ShareOnTwitterActicity_.class);
				intent.putExtra("productName", detail.name);
				intent.putExtra("productPicture", detail.images.get(0));
				intent.putExtra("productURl", detail.webAddress);
				startActivity(intent);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onLinkedinClick()
	{
		// 事件统计104 选择LinkedIn分享 点击事件
		SysManager.analysis(R.string.a_type_click, R.string.c104);
		Intent intent = new Intent(this, ShareOnLinkedinActivity.class);
		if (null != detail)
		{
			intent.putExtra("productName", detail.name);
			intent.putExtra("productPicture", detail.images.get(0));
			intent.putExtra("productURl", detail.webAddress);
		}
		startActivity(intent);
	}

	@Override
	public void onCancel()
	{
		// TODO Auto-generated method stub
	}

}

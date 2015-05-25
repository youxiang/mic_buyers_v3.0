package com.micen.buyers.manager;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.micen.buyers.activity.productdetail.ProductMessageActivity_;
import com.micen.buyers.activity.showroom.ShowRoomActivity_;
import com.micen.buyers.activity.special.SpecialDetailActivity_;
import com.micen.buyers.module.SpecialTarget;
import com.micen.buyers.module.special.SpecialContent;

/**********************************************************
 * @文件名称：IntentManager.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年4月9日 上午9:32:41
 * @文件描述：跳转系统页面管理器
 * @修改历史：2015年4月9日创建初始版本
 **********************************************************/
public class IntentManager
{
	/**
	 * 调用系统方法打开浏览器
	 * @param activity
	 * @param url
	 */
	public static void startLoadUrl(Context context, String url)
	{
		try
		{
			Uri uri = Uri.parse(url);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			context.startActivity(intent);
		}
		catch (ActivityNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 调用系统方法拨号
	 * @param activity
	 * @param number
	 */
	public static void makeCall(Context context, String number)
	{
		try
		{
			// 用intent启动拨打电话
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
			context.startActivity(intent);
		}
		catch (ActivityNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 调用系统方法发送邮件
	 * @param activity
	 * @param email
	 */
	public static void sendMail(Context context, String email)
	{
		try
		{
			Intent intent = new Intent(Intent.ACTION_SENDTO);
			intent.setData(Uri.parse("mailto:" + email));
			context.startActivity(intent);
		}
		catch (ActivityNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 专题跳转目的地
	 * @param context
	 * @param specialContent
	 */
	public static void specialTarget(Context context, SpecialContent specialContent)
	{
		if (context == null || specialContent == null)
			return;
		switch (SpecialTarget.getValueByTag(specialContent.targetType))
		{
		case SpecialDetail:// 专题详情
			Intent specialDetailInt = new Intent(context, SpecialDetailActivity_.class);
			specialDetailInt.putExtra("specialId", specialContent.target);
			context.startActivity(specialDetailInt);
			break;
		case WebAddress:// 跳转网页
			startLoadUrl(context, specialContent.target);
			break;
		case Showroom:// 跳转展示厅
			Intent showroomInt = new Intent(context, ShowRoomActivity_.class);
			showroomInt.putExtra("companyId", specialContent.target);
			context.startActivity(showroomInt);
			break;
		case ProductDetail:// 跳转产品详情
			Intent productDetailInt = new Intent(context, ProductMessageActivity_.class);
			productDetailInt.putExtra("productId", specialContent.target);
			context.startActivity(productDetailInt);
			break;
		default:
			break;
		}
	}

}

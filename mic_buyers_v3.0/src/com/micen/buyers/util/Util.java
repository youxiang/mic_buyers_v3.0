package com.micen.buyers.util;

import java.io.File;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.os.StrictMode.VmPolicy;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

import com.micen.buyers.activity.R;
import com.micen.buyers.constant.Constants;
import com.micen.buyers.db.SharedPreferenceManager;
import com.micen.buyers.module.product.ProductKeyValuePair;
import com.micen.buyers.module.search.SearchLocation;

/**********************************************************
 * @文件名称：Util.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年3月19日 上午9:17:37
 * @文件描述：
 * @修改历史：2015年3月19日创建初始版本
 **********************************************************/
public class Util
{
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(float dpValue)
	{
		return (int) (dpValue * Constants.density + 0.5f);
	}

	public static String getPrice(List<String> values)
	{
		String result = null;
		if (values != null)
		{
			if (values.size() == 1)
			{
				int index = values.get(0).indexOf(":");
				index = index + 1;
				if (index > 0 && index < values.get(0).length())
				{
					result = values.get(0).substring(index);
					if (result.startsWith("-1"))
					{
						result = null;
					}
					else
					{
						result = "$" + result;
					}
				}
			}
			else if (values.size() > 1)
			{
				int index = 0;
				double max = 0.0;
				double min = -1.0;
				double tmp = 0.0;

				String strLow = null;
				String strHigh = null;

				for (String str : values)
				{
					index = str.indexOf(":");

					index = index + 1;
					if (index > 0 && index < str.length())
					{
						result = str.substring(index);
						if (result.startsWith("-1"))
						{
							result = null;
						}
						else
						{
							try
							{
								tmp = Double.parseDouble(result);
							}
							catch (Exception e)
							{

							}

							if (tmp > max)
							{
								max = tmp;
								strHigh = result;
							}

							if (min < 0 || tmp < min)
							{
								min = tmp;
								strLow = result;
							}

							if (strLow == null)
							{
								strLow = result;
							}
						}
					}
				}

				result = "$" + strLow;
				if (strHigh != null)
				{
					result = result + "-$" + strHigh;
				}

			}
		}
		return result;
	}

	/**
	 * 获取设备屏幕密度
	 * @return
	 */
	public static float getWindowDensity()
	{
		DisplayMetrics dm = new DisplayMetrics();
		Constants.currentActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.density;
	}

	/**
	 * 格式化日期
	 * @param date
	 * @return
	 */
	public static String formatDate(String date)
	{
		if ("".equals(date) || date == null)
		{
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", getLocale());
		Date dt = new Date();
		try
		{
			dt = new Date(Long.parseLong(date));
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
		}
		return sdf.format(dt);
	}

	/**
	 * 格式化日期
	 * @param date
	 * @return
	 */
	public static String formatDateToEn(String date)
	{
		if ("".equals(date) || date == null)
		{
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.enDateTemplate, getLocale());
		Date dt = new Date();
		try
		{
			dt = new Date(Long.parseLong(date));
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
		}
		return sdf.format(dt);
	}

	/**
	 * 转义HTML特殊字符
	 * @param content
	 * @return
	 */
	public static String transformHtml(String content)
	{
		if (content == null)
			return "";
		String html = content;
		html = html.replaceAll("&", "&amp;");
		html = html.replaceAll("\"", "&quot;"); // "
		// html = html.replaceAll("\t", "&nbsp;&nbsp;");// 替换跳格
		// html = html.replaceAll(" ", "&nbsp;");// 替换空格
		html = html.replaceAll("<", "&lt;");
		html = html.replaceAll(">", "&gt;");
		return html;
	}

	/**
	 * 转义HTML特殊字符
	 * @param content
	 * @return
	 */
	public static String transToHtml(String content)
	{
		if (content == null)
			return "";
		String html = content;
		// html = html.replaceAll("&amp;", "&");
		// html = html.replaceAll("&quot;", "\""); // "
		// html = html.replaceAll("\t", "&nbsp;&nbsp;");// 替换跳格
		// html = html.replaceAll("&nbsp;", " ");// 替换空格
		html = html.replaceAll("&lt;", "<");
		html = html.replaceAll("&gt;", ">");
		return html;
	}

	/**
	 * 获得刷新时间 formart 格式 “MM/dd/yyyy  HH:mm”
	 * @return
	 */
	public static String getFormartRefershTime()
	{
		SimpleDateFormat formart = new SimpleDateFormat("MM/dd/yyyy  HH:mm", getLocale());

		return formart.format(new Date());
	}

	public static String getFormartTime(String strFormart)
	{
		SimpleDateFormat formart = new SimpleDateFormat(strFormart, getLocale());
		return formart.format(new Date());
	}

	/**
	 * 给数字添加千位分隔符
	 * @param number
	 * @return
	 */
	public static String getNumKb(long number)
	{
		NumberFormat formatter = new DecimalFormat("###,###");
		return formatter.format(number) + "";
	}

	/**
	 * 根据ListView中子项的总高度，重置ListView的高度
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView)
	{
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null)
		{
			// pre-condition
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++)
		{
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	public static void setScrollViewHeightBasedOnChild(ScrollView scrollView)
	{
		View child = scrollView.getChildAt(0);
		ViewGroup.LayoutParams params = (LayoutParams) scrollView.getLayoutParams();
		params.height = child.getHeight();
		scrollView.setLayoutParams(params);
	}

	private static final boolean isChinese(char c)
	{
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS)
		{
			return true;
		}
		return false;
	}

	/**
	 * 是否包含中文
	 * @param strName
	 * @return	true:包含
	 */
	public static boolean isChinese(String strName)
	{
		char[] ch = strName.toCharArray();
		for (int i = 0; i < ch.length; i++)
		{
			char c = ch[i];
			if (isChinese(c))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * 计算两个时间点的时长是否超过24小时
	 */
	public static String calculateTime()
	{
		String timeout = String.valueOf(System.currentTimeMillis() - Constants.DAY_TIME);
		return timeout;
	}

	public static List<String> getMidResolutionPicture(List<String> list)
	{
		List<String> lt = new ArrayList<String>();
		if (list.size() == 1)
		{
			lt = list;
		}
		else
		{
			for (int i = 0; i < list.size() / 3; i++)
			{
				lt.add(list.get(3 * i + 1));
			}
		}
		return lt;
	}

	public static ArrayList<String> getHighResolutionPicture(List<String> list)
	{
		ArrayList<String> lt = new ArrayList<String>();
		if (list.size() == 1)
		{
			lt = (ArrayList<String>) list;
		}
		else
		{
			for (int i = 0; i < list.size() / 3; i++)
			{
				lt.add(list.get(3 * i));
			}
		}
		return lt;
	}

	/**
	 * 将JSON列表数据转化为ArrayList
	 * @param jsonObj
	 * @param pairList
	 * @throws JSONException
	 */
	public static void setJsonValueToPairList(JSONObject jsonObj, ArrayList<ProductKeyValuePair> pairList)
			throws JSONException
	{
		Iterator<?> it = jsonObj.keys();
		ProductKeyValuePair pair;
		while (it.hasNext())
		{
			pair = new ProductKeyValuePair();
			pair.key = it.next().toString();
			if (jsonObj.get(pair.key) instanceof JSONObject)
			{
				ArrayList<ProductKeyValuePair> tempPairList = new ArrayList<ProductKeyValuePair>();
				ProductKeyValuePair tempPair;
				Iterator<?> tempIt = jsonObj.getJSONObject(pair.key).keys();
				while (tempIt.hasNext())
				{
					tempPair = new ProductKeyValuePair();
					tempPair.key = tempIt.next().toString();
					tempPair.value = jsonObj.getJSONObject(pair.key).getJSONArray(tempPair.key).get(0);
					tempPairList.add(tempPair);
				}
				pair.value = tempPairList;
			}
			else
			{
				pair.value = jsonObj.get(pair.key).toString();
			}
			pairList.add(pair);
		}
	}

	/**
	 * 根据设定好的符验证输入字符串是否满足条件
	 * @param value
	 * @return
	 */
	public static boolean isValidKeyWord(String value)
	{
		String regex = "^[a-zA-Z0-9-]{6,20}+$";
		return Pattern.matches(regex, value);
	}

	/**
	 * 隐藏系统软件盘
	 */
	public static void hideSoftInputMethod(Context context, View v)
	{
		/* 隐藏软键盘 */
		InputMethodManager inputMethodManager = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (inputMethodManager.isActive())
		{
			inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
		}
	}

	/**
	 * 获取格式转换的语言
	 * @return
	 */
	public static Locale getLocale()
	{
		return Locale.ENGLISH;
	}

	/**
	 * 取出数组中的所有值组装成一个字符串
	 * @param data
	 * @return
	 */
	public static String getAllListValue(ArrayList<String> data)
	{
		String value = "";
		if (data == null)
			return value;
		for (int i = 0; i < data.size(); i++)
		{
			value = value + data.get(i) + (i == data.size() - 1 ? "" : ",");
		}
		return value;
	}

	/** 
	 * 替换字符串中特殊字符 
	 */
	public static String replaceHtmlStr(String strData)
	{
		if (strData == null)
		{
			return "";
		}
		strData = strData.replace("<", "&lt;");
		strData = strData.replace(">", "&gt;");
		strData = strData.replace("\"", "&quot;");
		return strData;
	}

	/**
	 * 按照APP设置去决定是否显示图片
	 */
	public static void showFitImage(String isShowImage, String imagePath, ImageView imageView)
	{
		/**
		 * 判断图片是否显示
		 */
		if ("2201000000".equals(isShowImage) || "1713000000".equals(isShowImage))
		{// 是限制级
			// 判断 是否开启浏览图片
			if (!SharedPreferenceManager.getInstance().getBoolean("isDisplaySafeImage", false))
			{// 默认关闭
				imageView.setImageResource(R.drawable.product_fort_adult);
			}
			else
			{
				ImageUtil.getImageLoader().displayImage(imagePath, imageView, ImageUtil.getSafeImageOptions());
			}
		}
		else
		{
			ImageUtil.getImageLoader().displayImage(imagePath, imageView, ImageUtil.getSafeImageOptions());
		}
	}

	/**
	 * 收集设备参数信息
	 * @param ctx
	 */
	public static void collectDeviceInfo(Context ctx, HashMap<String, String> infos)
	{
		try
		{
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
			if (pi != null)
			{
				String versionName = pi.versionName == null ? "null" : pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
		}
		catch (NameNotFoundException e)
		{
			e.getMessage();
		}
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields)
		{
			try
			{
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
			}
			catch (Exception e)
			{
				e.getMessage();
			}
		}
	}

	public static long availableSDCard()
	{
		File path = Environment.getExternalStorageDirectory();
		long availableSize = 0;
		if (path != null)
		{
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long availableBlock = stat.getAvailableBlocks();
			availableSize = availableBlock * blockSize;
		}

		return availableSize / (1024 * 1024);
	}

	/**
	 * RFQ默认最小时间（毫秒）
	 * 选择的日期如果大大于等于当前日期加上偏移值之后的日期，则验证通过
	 * @return
	 */
	public static Date getMinTime()
	{
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + Constants.DAY_OFFSET
				- 1);
		return cal.getTime();
	}

	/**
	 * 检测一个字符串中是否包含英文字符
	 * @return
	 */
	public static boolean isHasEnglish(String source)
	{
		if (source != null && !"".equals(source))
		{
			for (int i = 0; i < source.length(); i++)
			{
				if (Character.isLetter(source.charAt(i)))
				{ // 用char包装类中的判断字母的方法判断每一个字符
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 根据输入的分隔符接取字符串
	 */
	public static String CutString(String source, String format)
	{
		String[] des = source.split(format);
		return des[des.length - 1];
	}

	/**
	 * 得到不同产品的总数
	 * @param locations
	 * @return
	 */
	public static long getLocationsNumber(ArrayList<SearchLocation> locations)
	{
		long size = 0;
		if (locations == null || locations.size() == 0)
		{
			size = 0;
		}
		else
		{
			for (SearchLocation location : locations)
			{
				size += Integer.parseInt(location.num);
			}
		}
		return size;
	}

	public static void startDeveloperMode()
	{
		if (Constants.DEV_MODE)
		{
			StrictMode.setThreadPolicy(new ThreadPolicy.Builder().detectAll().penaltyLog().build());
			StrictMode.setVmPolicy(new VmPolicy.Builder().detectAll().penaltyLog().build());
		}
	}

	public static void dispatchCancelEvent(View view)
	{
		view.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
				MotionEvent.ACTION_CANCEL, 0, 0, 0));
	}
}

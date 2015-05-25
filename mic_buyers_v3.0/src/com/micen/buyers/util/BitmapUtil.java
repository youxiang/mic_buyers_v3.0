package com.micen.buyers.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;

import com.micen.buyers.db.SharedPreferenceManager;

public class BitmapUtil
{
	public static Bitmap createTxtImage(String txt, int txtSize, int screenW, int screenH)
	{
		Bitmap mbmpTest = Bitmap.createBitmap(screenW / 2, txtSize + 4, Config.ARGB_8888);
		Canvas canvasTemp = new Canvas(mbmpTest);
		Paint p = new Paint();
		p.setAntiAlias(true);
		p.setColor(0xff404040);
		p.setTextSize(txtSize);
		canvasTemp.drawText(txt, screenW / 4 - p.measureText(txt) / 2, txtSize - 2, p);
		return mbmpTest;
	}

	public Bitmap returnBitMap(String url)
	{
		URL myFileUrl = null;
		Bitmap bitmap = null;
		try
		{
			myFileUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
			conn.setRequestProperty("Referer", " http://www.made-in-jiangsu.com");
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

	public Bitmap returnAllBitMap(String url)
	{
		URL myFileUrl = null;
		Bitmap bitmap = null;
		try
		{
			myFileUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
			if (SharedPreferenceManager.getInstance().getBoolean("isDisplaySafeImage", false))
			{// ����
				conn.setRequestProperty("Referer", " http://www.made-in-jiangsu.com");
			}
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

	/**
	 * 将字节数组转化为Drawable对象
	 * @param b
	 * @return
	 */
	public static BitmapDrawable bytes2Drawable(byte[] b)
	{
		Bitmap bitmap = Bytes2Bitmap(b);
		return bitmap2Drawable(bitmap);
	}

	// byte[]转换成Bitmap
	public static Bitmap Bytes2Bitmap(byte[] b)
	{
		if (b.length != 0)
		{
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		}
		return null;
	}

	// Bitmap转换成Drawable
	public static BitmapDrawable bitmap2Drawable(Bitmap bitmap)
	{
		BitmapDrawable bd = new BitmapDrawable(BuyerApplication.getInstance().getResources(), bitmap);
		return bd;
	}

	public static byte[] bitmap2Bytes(Bitmap bm)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

}

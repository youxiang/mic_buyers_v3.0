package com.micen.buyers.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.micen.buyers.activity.R;
import com.micen.buyers.module.db.Country;
import com.micen.buyers.util.BitmapUtil;

/**********************************************************
 * @文件名称：CountryDBHelper.java
 * @文件作者：xiongjiangwei
 * @创建时间：2014年7月22日 上午11:28:52
 * @文件描述：国家数据库帮助类
 * @修改历史：2014年7月22日创建初始版本
 **********************************************************/
public class CountryDBHelper
{
	private static final String DATABASE_NAME = "/country.db";

	/**
	 * 获取国家数据列表
	 * @param context
	 * @return
	 */
	public static LinkedList<Country> getCountryList(Context context)
	{
		LinkedList<Country> list = new LinkedList<Country>();
		initCountryDB(context);
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(context.getFilesDir() + DATABASE_NAME, null);
		Cursor cursor = db.query("COUNTRY", null, null, null, null, null, "name");
		char Char = 'A';
		while (cursor.moveToNext())
		{
			Country country = new Country();
			Char = Character.toUpperCase(cursor.getString(1).charAt(0));
			country.setCountryCode(cursor.getString(0));
			country.setCountryCodeValue(cursor.getString(2));
			country.setIndexChar(String.valueOf(Char));
			country.setCountryName(cursor.getString(1));
			country.setCountryTelNum(cursor.getString(3));
			country.setCountryAreaNum(cursor.getString(4));
			if (cursor.getBlob(5) != null)
			{
				country.setFlagDrawable(BitmapUtil.bytes2Drawable(cursor.getBlob(5)));
			}
			list.add(country);
		}
		cursor.close();
		db.close();
		return list;
	}

	private static void initCountryDB(Context context)
	{
		InputStream is = null;
		FileOutputStream fos = null;
		String dbPath = context.getFilesDir() + DATABASE_NAME;
		try
		{
			File file = new File(context.getFilesDir().toString());
			if (!file.exists())
			{
				file.mkdirs();
			}
			File file1 = new File(dbPath);
			if (file1.exists())
			{
				file1.delete();
			}
			file1.createNewFile();
			is = context.getResources().openRawResource(R.raw.country);
			fos = new FileOutputStream(dbPath);
			byte[] buffer = new byte[4096];
			int count = 0;
			while ((count = is.read(buffer)) > 0)
			{
				fos.write(buffer, 0, count);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (fos != null)
				{
					fos.close();
				}
				if (is != null)
				{
					is.close();
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

}

package com.micen.buyers.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import android.os.Environment;

import com.micen.buyers.module.category.Category;

public class FileManager
{
	private File dirFile;

	private static FileManager fm = null;
	static
	{
		fm = new FileManager();
	}

	public static FileManager getInstance()
	{
		return fm;
	}

	/**
	 * 保存目录数据的文件
	 */
	private final String path = Environment.getExternalStorageDirectory() + "/focustech/mic/category/";

	/**
	 * 将目录内容写入文件,互斥锁防止多个请求同时写文件
	 * @param fileName
	 * @param content
	 */
	public synchronized boolean writeIntoFile(String fileName, ArrayList<Category> content)
	{
		initDir();
		try
		{
			ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(new File(fileName)));
			oo.writeObject(content);
			oo.close();
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public ArrayList<Category> readFromFile(String fileName)
	{
		initDir();
		try
		{
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(fileName)));
			ArrayList<Category> content = (ArrayList<Category>) ois.readObject();
			ois.close();
			return content;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	private void initDir()
	{
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			dirFile = new File(path);
			if (!dirFile.exists())
			{
				dirFile.mkdirs();
			}
		}
	}

	public String getCachePath()
	{
		return path;
	}
}

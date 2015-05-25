package com.micen.buyers.module.db;

import android.graphics.drawable.BitmapDrawable;

/**********************************************************
 * @文件名称：Country.java
 * @文件作者：xiongjiangwei
 * @创建时间：2014年7月22日 上午11:33:46
 * @文件描述：国家数据模型
 * @修改历史：2014年7月22日创建初始版本
 **********************************************************/
public class Country implements Comparable<Country>
{
	/**
	 * 是否分组名称
	 */
	private boolean isGroup = false;
	/**
	 * 国家Code(注册用)
	 */
	private String countryCode;
	/**
	 * 国家Code对应的值
	 */
	private String countryCodeValue;
	/**
	 * 国家名称首字母
	 */
	private String indexChar;
	/**
	 * 国家名称
	 */
	private String countryName;
	/**
	 * 电话中的国家码
	 */
	private String countryTelNum;
	/**
	 * 电话中的国家地区码
	 */
	private String countryAreaNum;

	/**
	 * 国家国旗
	 */
	private byte[] countryFlag;

	/**
	 * 常用国家国旗
	 * @return
	 */
	private BitmapDrawable flagDrawable = null;

	public BitmapDrawable getFlagDrawable()
	{
		return flagDrawable;
	}

	public void setFlagDrawable(BitmapDrawable flagDrawable)
	{
		this.flagDrawable = flagDrawable;
	}

	public byte[] getCountryFlag()
	{
		return countryFlag;
	}

	public void setCountryFlag(byte[] countryFlag)
	{
		this.countryFlag = countryFlag;
	}

	public String getCountryTelNum()
	{
		return countryTelNum;
	}

	public void setCountryTelNum(String countryTelNum)
	{
		this.countryTelNum = countryTelNum;
	}

	public String getCountryAreaNum()
	{
		return countryAreaNum;
	}

	public void setCountryAreaNum(String countryAreaNum)
	{
		this.countryAreaNum = countryAreaNum;
	}

	public boolean isGroup()
	{
		return isGroup;
	}

	public void setGroup(boolean isGroup)
	{
		this.isGroup = isGroup;
	}

	public String getCountryCode()
	{
		return countryCode;
	}

	public void setCountryCode(String countryCode)
	{
		this.countryCode = countryCode;
	}

	public String getCountryCodeValue()
	{
		return countryCodeValue;
	}

	public void setCountryCodeValue(String countryCodeValue)
	{
		this.countryCodeValue = countryCodeValue;
	}

	public String getIndexChar()
	{
		return indexChar;
	}

	public void setIndexChar(String indexChar)
	{
		this.indexChar = indexChar;
	}

	public String getCountryName()
	{
		return countryName;
	}

	public void setCountryName(String countryName)
	{
		this.countryName = countryName;
	}

	@Override
	public int compareTo(Country another)
	{
		return this.getCountryName().compareTo(another.getCountryName());
	}

}

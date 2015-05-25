package com.micen.buyers.module;

import android.text.TextUtils;

public enum ResponseCode
{
	CODE_0("0"),
	CODE_10001("10001"),
	CODE_10002("10002"),
	CODE_10003("10003"),
	CODE_10004("10004"),
	CODE_10005("10005"),
	CODE_10006("10006"),
	CODE_10007("10007"),
	CODE_10008("10008"),
	CODE_10009("10009"),
	CODE_10010("10010"),
	CODE_10011("10011"),
	CODE_11000("11000"),
	CODE_11001("11001"),
	CODE_110013("110013");

	private String value;

	private ResponseCode(String value)
	{
		this.value = value;
	}

	public static ResponseCode getCodeValueByTag(String type)
	{
		if (TextUtils.isEmpty(type))
		{
			return CODE_10001;
		}
		for (ResponseCode code : ResponseCode.values())
		{
			if (code.value.equals(type))
			{
				return code;
			}
		}
		return CODE_10001;
	}
	
	@Override
	public String toString()
	{
		return value;
	}
}

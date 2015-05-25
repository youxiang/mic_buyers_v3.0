package com.micen.buyers.module.sift;

import java.io.Serializable;

public class SiftSupplier implements Serializable
{
	public int imageResID;
	public String name;

	public SiftSupplier(int resid, String name)
	{
		this.imageResID = resid;
		this.name = name;
	}
}

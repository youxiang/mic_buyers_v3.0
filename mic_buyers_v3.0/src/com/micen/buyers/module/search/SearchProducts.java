package com.micen.buyers.module.search;

import java.util.ArrayList;

import com.micen.buyers.module.BaseResponse;
import com.micen.buyers.module.sift.SearchProperty;
import com.micen.buyers.module.sift.SiftCategory;

public class SearchProducts extends BaseResponse
{
	public String suggest;
	public String resultNum;
	public ArrayList<SearchProduct> content;
	public ArrayList<SearchLocation> locations;
	/**
	 * 返回目录 
	 */
	public ArrayList<SiftCategory> catagory;
	/**
	 * 返回属性
	 */
	public ArrayList<SearchProperty> property;

}

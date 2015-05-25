package com.micen.buyers.module.search;

import java.util.ArrayList;

import com.micen.buyers.module.BaseResponse;

public class SearchCompanies extends BaseResponse
{
	public String map;
	public ArrayList<SearchCompany> content;
	public ArrayList<SearchLocation> locations;
}

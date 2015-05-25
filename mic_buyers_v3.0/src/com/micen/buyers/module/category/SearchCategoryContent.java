package com.micen.buyers.module.category;

import java.util.ArrayList;

public class SearchCategoryContent
{
	public String catCode;
	public ArrayList<String> catNames;
	public String count;

	public String getAllCatNames()
	{
		if (catNames == null || (catNames != null && catNames.size() == 0))
			return "";
		String names = "";
		for (int i = 0; i < catNames.size(); i++)
		{
			names = names + catNames.get(i) + (i == catNames.size() - 1 ? "" : ">>");
		}
		return names;
	}
}

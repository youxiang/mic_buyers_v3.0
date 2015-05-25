package com.micen.buyers.module.special.detail;

import java.util.ArrayList;

public class SpecialDetailContent
{
	public String activityId;
	public ArrayList<SpecialDetailMenu> activityMenuList;
	public String activityName;
	public String activityUrl;
	public String bannerAlt;
	public String bannerAltAlias;
	public String bannerPicHeight;
	public String bannerPicUrl;
	public String bannerPicWidth;
	public String bannerUrl;
	public String bottonModelShowType;
	public String descriptionInfo;
	public String menuShowFlag;
	public String menuShowType;
	public String navigationShowType;
	public String status;
	public String templateId;

	public ArrayList<String> getAllMenuName()
	{
		ArrayList<String> nameList = new ArrayList<String>();
		if (activityMenuList == null || (activityMenuList != null && activityMenuList.size() == 0))
			return nameList;
		for (int i = 0; i < activityMenuList.size(); i++)
		{
			nameList.add(activityMenuList.get(i).menuName);
		}
		return nameList;
	}
}

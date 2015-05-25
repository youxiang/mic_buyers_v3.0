package com.micen.buyers.module.user;

public class UserContent
{
	public UserInfo userInfo;
	public CompanyInfo companyInfo;
	public FavoriteInfo favoriteInfo;

	public void resetValue(UserContent content)
	{
		userInfo.resetValue(content.userInfo);
		companyInfo.resetValue(content.companyInfo);
		favoriteInfo.resetValue(content.favoriteInfo);
	}
}

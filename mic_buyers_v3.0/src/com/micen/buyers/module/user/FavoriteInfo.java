package com.micen.buyers.module.user;

/**********************************************************
 * @文件名称：FavoriteInfo.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年4月27日 上午9:25:42
 * @文件描述：用户收藏信息
 * @修改历史：2015年4月27日创建初始版本
 **********************************************************/
public class FavoriteInfo
{
	/**
	 * 收藏产品总数
	 */
	public String prodFavoriteNum;
	/**
	 * 收藏公司总数
	 */
	public String compFavoriteNum;
	/**
	 * 收藏目录总数
	 */
	public String cataFavoriteNum;

	public void resetValue(FavoriteInfo favoriteInfo)
	{
		this.prodFavoriteNum = favoriteInfo.prodFavoriteNum;
		this.prodFavoriteNum = favoriteInfo.prodFavoriteNum;
		this.prodFavoriteNum = favoriteInfo.prodFavoriteNum;
	}
}

package com.micen.buyers.util;

/**********************************************************
 * @文件名称：UserUtil.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年4月16日 下午5:39:02
 * @文件描述：用户数据工具类
 * @修改历史：2015年4月16日创建初始版本
 **********************************************************/
public class UserUtil
{
	/**
	 * 转换姓名称呼代码为字符串
	 * @return
	 */
	public static String transformGender()
	{
		if (BuyerApplication.getInstance().getUser() == null)
			return "";
		String gender = BuyerApplication.getInstance().getUser().content.userInfo.gender;
		switch (Integer.parseInt(gender))
		{
		case 0:
			gender = "Mr.";
			break;
		case 1:
			gender = "Mrs.";
			break;
		case 2:
			gender = "Ms.";
			break;
		case 3:
			gender = "Miss ";
			break;
		default:
			gender = "";
			break;
		}
		return gender;
	}
}

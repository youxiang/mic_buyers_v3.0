package com.micen.buyers.module.user;

import com.micen.buyers.module.BaseResponse;

/**********************************************************
 * @文件名称：User.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年4月27日 上午9:26:27
 * @文件描述：用户信息
 * @修改历史：2015年4月27日创建初始版本
 **********************************************************/
public class User extends BaseResponse
{
	public String sessionid;
	public UserContent content;

	public User copy(User user)
	{
		content.resetValue(user.content);
		return this;
	}
}

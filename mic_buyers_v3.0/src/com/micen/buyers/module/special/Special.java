package com.micen.buyers.module.special;

import java.util.ArrayList;

import com.micen.buyers.module.BaseResponse;

/**********************************************************
 * @文件名称：Special.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年4月21日 上午9:55:24
 * @文件描述：专题根数据模型
 * @修改历史：2015年4月21日创建初始版本
 **********************************************************/
public class Special extends BaseResponse
{
	public ArrayList<SpecialObject> dMiss;
	public ArrayList<SpecialObject> popular;
	public ArrayList<SpecialContent> upper;
	public ArrayList<SpecialContent> middle;
	public ArrayList<SpecialContent> lower;
}

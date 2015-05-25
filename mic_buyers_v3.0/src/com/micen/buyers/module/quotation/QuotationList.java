package com.micen.buyers.module.quotation;

import java.util.ArrayList;

import com.micen.buyers.module.BaseResponse;

/**********************************************************
 * @文件名称：QuotationList.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年5月4日 下午2:22:15
 * @文件描述：报价列表数据模型
 * @修改历史：2015年5月4日创建初始版本
 **********************************************************/
public class QuotationList extends BaseResponse
{
	public ArrayList<QuotationListContent> content;
}

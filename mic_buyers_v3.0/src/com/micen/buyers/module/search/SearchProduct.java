package com.micen.buyers.module.search;

import java.util.List;
import java.util.Map;

public class SearchProduct
{
	public String productId;
	public String image;
	public String name;
	public String description;
	public String memberType;
	public String auditType;
	public String isFeature;
	public String minOrder;
	public String unitPrice;
	public String tradeTerm;
	public String catCode;
	public String companyName;

	// 下面是新增加了字段
	public List<String> splitUnitPrice;
	public String orderUnit;
	public String prodPriceUnit;
	public boolean isFavorite;

	// 下面是专题产品新增加的字段
	public Map<String, Object> prodProps;
}

package com.focustech.common.mob;
public class BaseNetParams
{	
	/**����url*/
	private String url;
	/**���󷽷�*/
	private String method;
	public BaseNetParams(String url, String method)
	{
		super();
		this.url= url;
		this.method= method;
	}
	public BaseNetParams()
	{
		super();
	}
	public String getUrl()
	{
		return url;
	}
	public void setUrl(String url)
	{
		this.url= url;
	}
	public String getMethod()
	{
		return method;
	}
	public void setMethod(String method)
	{
		this.method= method;
	}
}

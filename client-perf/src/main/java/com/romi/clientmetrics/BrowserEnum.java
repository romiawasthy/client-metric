package com.romi.clientmetrics;

public enum BrowserEnum {
	
	firefox("firefox", "Firefox"),
	chrome("chrome","Chrome"),
	
	iexplore("iexplore","MSIE");
	
	private String name;
	private String agentString;
	
	private BrowserEnum(String name, String agentString)
	{
		this.name = name;
		this.agentString= agentString;
	}
	
	public String getName()
	{
		return this.name;
	}
	public String getAgent()
	{
		return this.agentString;
	}

}

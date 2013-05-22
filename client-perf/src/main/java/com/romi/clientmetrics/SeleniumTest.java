package com.romi.clientmetrics;

import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

public interface SeleniumTest {
	
	public JSONObject mainTest(WebDriver driver, String browserType);
	

}

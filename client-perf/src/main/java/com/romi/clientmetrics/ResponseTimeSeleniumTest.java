package com.romi.clientmetrics;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ResponseTimeSeleniumTest implements SeleniumTest {
	

	public void loginToApplication(EventFiringWebDriver eventFiringWebDriver,
			String username, String password) {

		eventFiringWebDriver.manage().window().maximize();

		WebDriverWait wait = new WebDriverWait(eventFiringWebDriver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By
				.cssSelector("input.submitButton")));
		eventFiringWebDriver.findElement(By.cssSelector("input.tdClass"))
				.clear();
		eventFiringWebDriver.findElement(By.cssSelector("input.tdClass"))
				.sendKeys(username);
		eventFiringWebDriver.findElement(By.cssSelector("#password")).sendKeys(
				password);

		eventFiringWebDriver.findElement(By.cssSelector("input.submitButton"))
				.click();

	}

	public void waitforHomePageLoad(EventFiringWebDriver eventFiringWebDriver) {
		WebDriverWait wait = new WebDriverWait(eventFiringWebDriver, 60);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By
				.cssSelector(".auto-test-whatsnewportlet")));

	}

	public void logoutFromApplication(
			EventFiringWebDriver eventFiringWebDriver, String browser) {
		WebDriverWait wait = new WebDriverWait(eventFiringWebDriver, 10);
		if (browser.equalsIgnoreCase("iexplore")) {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By
					.cssSelector(".auto-test-btnLogout>table>tbody>tr>td>em>button")));

			eventFiringWebDriver
					.findElement(
							By.cssSelector(".auto-test-btnLogout>table>tbody>tr>td>em>button"))
					.click();

		} else {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By
					.cssSelector(".auto-test-btnLogout>em>button")));
			eventFiringWebDriver.findElement(
					By.cssSelector(".auto-test-btnLogout>em>button")).click();
		}
	}

	@Override
	public JSONObject mainTest(WebDriver driver, String browserType) {
		
		JSONObject loadTimes = new JSONObject();
		driver.get("http://dev.sencha.com/deploy/ext-4.0.0/examples/sandbox/sandbox.html");
		EventFiringWebDriver eventFiringWebDriver = new EventFiringWebDriver(
				driver);

		WebDriverWait wait = new WebDriverWait(eventFiringWebDriver, 20);
		long startTime = Calendar.getInstance().getTimeInMillis();
		wait.until(ExpectedConditions.visibilityOfElementLocated( By.id("ext-gen12")));
					
		long loadTimeSecs = (Calendar.getInstance().getTimeInMillis() - startTime);
		
		try {
			loadTimes
					.put("homepageload", String.valueOf(loadTimeSecs) + "ms");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		eventFiringWebDriver.manage().deleteAllCookies();
		eventFiringWebDriver.close();
		return loadTimes;
		
	}

}

package com.romi.clientmetrics;

import java.io.IOException;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Servlet implementation class ResponseTimeServlet
 */
@WebServlet("/ResponseTimeServlet/*")
public class ResponseTimeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	SeleniumTest selenium = new ResponseTimeSeleniumTest();
	

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ResponseTimeServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		service(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		service(request, response);
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		JSONObject metrics = new JSONObject();
		JSONObject cookies = new JSONObject();
		JSONObject headers = new JSONObject();			
		ResponsetimeDataAccess dataaccess = new ResponsetimeDataAccess();
		String url = (String) req.getParameter("testURL");
		try {
			metrics.put("url", url);
			metrics.put("type", "testapp");
			// metrics.put("headers",headers);
			Enumeration<String> headerNames = req.getHeaderNames();
			String headerName;

			while (headerNames.hasMoreElements()) {
				headerName = (String) headerNames.nextElement();
				headers.put(headerName, req.getHeader(headerName));

			}
			Cookie[] cookie = req.getCookies();
			/**
			for (int i = 0; i < cookie.length; i++) {
				cookies.put(cookie[i].getName(), cookie[i].getValue()
						.toString());

			}**/

			// metrics.put("cookies", cookies);

			String browserType = getBrowser(req.getHeader("user-agent"));
			metrics.put("browser", browserType);
			System.out.print("browser:" + browserType);
			WebDriver driver = getDriver(browserType);			
		
			metrics.put("seleniumTestResults", selenium.mainTest(driver, browserType));
			dataaccess.post(metrics.toString());
			System.out.print(metrics.toString());
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
	}

	public String getBrowser(String userAgent) {

		for (BrowserEnum browser : BrowserEnum.values()) {
			if (StringUtils.containsIgnoreCase(userAgent, browser.getAgent())) {

				return browser.getName();

			}
		}
		return "default";
	}

	public WebDriver getDriver(String browser) {
		WebDriver driver;
		if (browser.equalsIgnoreCase("iexplore")) {
			DesiredCapabilities capabilities = DesiredCapabilities
					.internetExplorer();
			capabilities
					.setCapability(
							InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
							true);
			System.setProperty("webdriver.ie.driver",
					"C:\\browserdriver\\IEDriverServer.exe");
			driver = new InternetExplorerDriver(capabilities);
		} else if (browser.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver",
					"C:\\browserdriver\\chromedriver.exe");
			driver = new ChromeDriver();

		} else {
			driver = new FirefoxDriver();
		}
		return driver;
	}

	public String createRequestBody(HttpServletRequest req) {

		HashMap<String, String> map = new HashMap<String, String>();

		map.put("browser", req.getHeader("user-agent"));
		map.put("url", req.getRequestURI());
		map.put("ip", req.getLocalName());
		return map.toString();

	}

}

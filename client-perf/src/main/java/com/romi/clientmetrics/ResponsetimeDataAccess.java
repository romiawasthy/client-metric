package com.romi.clientmetrics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class ResponsetimeDataAccess {
	String urlToDB = "";


	public void logResponsetime(String url, String browser, String username,
			String userType, String lobs, String reportGroups, String loadTime,
			String ip) {
		post(createRequestBody(url, browser, username, userType, lobs,
				reportGroups, loadTime, ip));
	}

	public String createRequestBody(String url, String browser,
			String username, String userType, String lobs, String reportGroups,
			String loadTime, String ip) {

		StringBuffer request = new StringBuffer();
		request.append("{").append("\"URL\":\"").append(url).append("\",")
				.append( "\"type\":\"").append("testpage").append("\",")
				.append("\"BROWSER\":\"").append(browser).append("\",")
				.append("\"USER_NAME\":\"").append(username).append("\",")
				.append("\"USER_TYPE\":\"").append(userType).append("\",")
				.append("\"LOBS\":\"").append(lobs).append("\",")
				.append("\"REPORT_GROUPS\":\"").append(reportGroups).append("\",")
				.append("\"LOAD_TIME\":\"").append(loadTime).append("\",")
				.append("\"PC_IP_ADDRESS\":\"").append(ip).append("\"}");

		return request.toString();

	}

	public void post(String request) {
		try {

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost postRequest = new HttpPost(
					"http://resprepo:5984/imcresponse");

			StringEntity input = new StringEntity(request);
			input.setContentType("application/json");
			System.out.println(request);
			postRequest.setEntity(input);

			HttpResponse response = httpClient.execute(postRequest);

			if (response.getStatusLine().getStatusCode() != 201) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatusLine().getStatusCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(response.getEntity().getContent())));

			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {

				System.out.println(output);
			}

			httpClient.getConnectionManager().shutdown();

		} catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();

		}

	}

	
}

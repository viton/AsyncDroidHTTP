package br.com.droidhttp;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

public class DroidHTTPRequest {
	public String mainURL;

	public DroidHTTPRequest(String mainURL) {
		this.mainURL = mainURL;
	}

	public DroidHTTPResponse put(JSONObject params) throws MalformedURLException, IOException {
		return via(HTTPMethod.PUT, params);
	}

	public DroidHTTPResponse post(JSONObject params) throws MalformedURLException, IOException {
		return via(HTTPMethod.POST, params);
	}

	public DroidHTTPResponse get() throws MalformedURLException, IOException {
		return via(HTTPMethod.GET, null);
	}

	private HttpURLConnection connectFor(HTTPMethod httpMethod, JSONObject params) throws MalformedURLException, IOException {
		URL url = new URL(mainURL);
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		urlConnection.setDoOutput(false);
		urlConnection.setRequestMethod(httpMethod.getValue());
		urlConnection.setUseCaches(false);
		urlConnection.setConnectTimeout(10000);
		urlConnection.setReadTimeout(10000);
		urlConnection.setRequestProperty("Content-Type", "application/json");

		if (params != null) {
			OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
			out.write(params.toString());
			out.close();
		}

		urlConnection.connect();

		return urlConnection;
	}

	private DroidHTTPResponse via(HTTPMethod httpMethod, JSONObject params) throws IOException, UnsupportedEncodingException {
		HttpURLConnection urlConnection = connectFor(httpMethod, params);
		int responseCode = urlConnection.getResponseCode();
		return new DroidHTTPResponse(responseCode, urlConnection.getInputStream());
	}
}

enum HTTPMethod {
	GET("GET"), POST("POST"), PUT("PUT");

	private String value;

	private HTTPMethod(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}

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

	public DroidHTTPResponse delete(JSONObject params) throws MalformedURLException, IOException {
		return via(HTTPMethod.DELETE, params);
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
		urlConnection.setRequestMethod(httpMethod.getValue());
		urlConnection.setUseCaches(false);
		urlConnection.setConnectTimeout(10000);
		urlConnection.setReadTimeout(10000);
		urlConnection.setRequestProperty("Content-Type", "application/json");

		if (params != null) {
			urlConnection.setDoOutput(true);
			OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
			out.write(params.toString());
			out.close();
		} else {
			urlConnection.setDoOutput(false);
		}

		urlConnection.connect();

		return urlConnection;
	}

	private DroidHTTPResponse via(HTTPMethod httpMethod, JSONObject params) throws IOException, UnsupportedEncodingException {
		int responseCode = -1;
		HttpURLConnection urlConnection = null;
		try {
			urlConnection = connectFor(httpMethod, params);
			responseCode = urlConnection.getResponseCode();
			if (urlConnection.getContent() != null) {
				return new DroidHTTPResponse(responseCode, urlConnection.getInputStream());
			} else {
				return new DroidHTTPResponse(responseCode);
			}
		} catch (IOException e) {
			if (responseCode == 422 && urlConnection != null) {
				return new DroidHTTPResponse(responseCode, urlConnection.getErrorStream());
			}
			
			if (e.getMessage().contains("authentication challenge")) {
				return new DroidHTTPResponse(HttpURLConnection.HTTP_UNAUTHORIZED);
		    } else { throw e; }
		}			
	}
}

enum HTTPMethod {
	GET("GET"), POST("POST"), PUT("PUT"), DELETE("DELETE");

	private String value;

	private HTTPMethod(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}

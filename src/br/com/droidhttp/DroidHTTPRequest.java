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
	private DroidHTTPParams droidParams;

	public DroidHTTPRequest(String mainURL, DroidHTTPParams droidParams) {
		this.mainURL = mainURL;
		this.droidParams = droidParams;
	}

	public DroidHTTPResponse put(JSONObject params) throws MalformedURLException, IOException {
		return via(HTTPMethod.PUT, params, droidParams);
	}

	public DroidHTTPResponse delete(JSONObject params) throws MalformedURLException, IOException {
		return via(HTTPMethod.DELETE, params, droidParams);
	}
	
	public DroidHTTPResponse delete() throws MalformedURLException, IOException {
		return via(HTTPMethod.DELETE, null, droidParams);
	}

	public DroidHTTPResponse post(JSONObject params) throws MalformedURLException, IOException {
		return via(HTTPMethod.POST, params, droidParams);
	}

	public DroidHTTPResponse get() throws MalformedURLException, IOException {
		return via(HTTPMethod.GET, null, droidParams);
	}

	private HttpURLConnection connectFor(HTTPMethod httpMethod, JSONObject params, DroidHTTPParams droidParams) throws MalformedURLException, IOException {
		URL url = new URL(mainURL);
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		urlConnection.setRequestMethod(httpMethod.getValue());
		urlConnection.setUseCaches(droidParams != null? droidParams.isUsesCache():false);
		urlConnection.setConnectTimeout(droidParams != null? droidParams.getConnectTimeOut():10000);
		urlConnection.setReadTimeout(droidParams != null? droidParams.getReadTimeOut():10000);
		urlConnection.setRequestProperty("Content-Type", droidParams != null? droidParams.getContentType():"application/json");

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

	private DroidHTTPResponse via(HTTPMethod httpMethod, JSONObject params, DroidHTTPParams droidParams) throws IOException, UnsupportedEncodingException {
		int responseCode = -1;
		HttpURLConnection urlConnection = null;
		try {
			urlConnection = connectFor(httpMethod, params, droidParams);
			responseCode = urlConnection.getResponseCode();
			if (urlConnection.getContent() != null) {
				return new DroidHTTPResponse(responseCode, urlConnection.getInputStream());
			} else {
				return new DroidHTTPResponse(responseCode);
			}
		} catch (IOException e) {
			if ((responseCode == 422  || responseCode == 424)&& urlConnection != null) {
				return new DroidHTTPResponse(responseCode, urlConnection.getErrorStream());
			}

			try{
				if (e.getMessage().contains("authentication challenge")) {
					return new DroidHTTPResponse(HttpURLConnection.HTTP_UNAUTHORIZED);
				} else {
					throw e;
				}
			}catch(Exception e1){
				throw e;
			}
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

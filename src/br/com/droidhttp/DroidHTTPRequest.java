package br.com.droidhttp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.JSONObject;

import android.os.Build;

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
		if (android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.FROYO ) {
			trustAllHosts();
		}
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		urlConnection.setRequestMethod(httpMethod.getValue());
		urlConnection.setUseCaches(false);
		urlConnection.setConnectTimeout(40000);
		urlConnection.setReadTimeout(40000);
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

	/**
	 * Trust every server - dont check for any certificate
	 */
	private static void trustAllHosts() {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[] {};
			}

			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		} catch (FileNotFoundException e) {
			return new DroidHTTPResponse(responseCode, urlConnection.getErrorStream());
		} catch (IOException e) {
			if (responseCode == 422 && urlConnection != null) {
				return new DroidHTTPResponse(responseCode, urlConnection.getErrorStream());
			}

			if (e.getMessage() != null && e.getMessage().contains("authentication challenge")) {
				return new DroidHTTPResponse(HttpURLConnection.HTTP_UNAUTHORIZED);
			} else {
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

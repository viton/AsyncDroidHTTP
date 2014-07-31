package br.com.asyncdroidhttp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Build;

public class AsyncDroidHTTPRequest {
	
	private String url;

	public AsyncDroidHTTPRequest(String url, AsyncDroidHTTPParams droidParams) {
		this.url = url;
	}

	

	private HttpURLConnection connectFor(String httpMethod, JSONObject params, AsyncDroidHTTPParams droidParams) throws MalformedURLException, IOException {
		if (android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.FROYO ) {
			trustAllHosts();
		}
		URL url = new URL(this.url);
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		urlConnection.setRequestMethod(httpMethod);

		urlConnection.setUseCaches(droidParams != null? droidParams.isUsesCache():false);
		urlConnection.setConnectTimeout(droidParams != null? droidParams.getConnectTimeOut():40000);
		urlConnection.setReadTimeout(droidParams != null? droidParams.getReadTimeOut():40000);
		urlConnection.setRequestProperty("Content-Type", droidParams != null? droidParams.getContentType():"application/json");

		if (params != null) {
			try {
				params.putOpt("locale", Locale.getDefault().getLanguage());
			} catch (JSONException e) {}
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

	public AsyncDroidHTTPResponse via(String httpMethod, JSONObject params, AsyncDroidHTTPParams droidParams) {
		int responseCode = -1;
		HttpURLConnection urlConnection = null;
		try {
			urlConnection = connectFor(httpMethod, params, droidParams);
			responseCode = urlConnection.getResponseCode();
			if (urlConnection.getContent() != null) {
				return new AsyncDroidHTTPResponse(responseCode, urlConnection.getInputStream(), null);
			} else {
				return new AsyncDroidHTTPResponse(responseCode);
			}
		} catch (FileNotFoundException e) {
			return new AsyncDroidHTTPResponse(responseCode, urlConnection.getErrorStream(), e);
		} catch (IOException e) {
			if ((responseCode == 422  || responseCode == 424)&& urlConnection != null) {
				return new AsyncDroidHTTPResponse(responseCode, urlConnection.getErrorStream(), e);
			}
			return new AsyncDroidHTTPResponse(HttpURLConnection.HTTP_UNAUTHORIZED);
//			if (e.getMessage() != null && e.getMessage().contains("authentication challenge")) {
//				return new DroidHTTPResponse(HttpURLConnection.HTTP_UNAUTHORIZED);
//			} else {
//				throw e;
//			}
		}
	}

}


package br.com.asyncdroidhttp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class AsyncDroidHTTPResponse {
	private int responseCode;
	private HALObject json;
	private Exception exception;
	
	public AsyncDroidHTTPResponse(int responseCode) {
		super();
		this.responseCode = responseCode;
	}
	
	public AsyncDroidHTTPResponse(int responseCode, InputStream is, Exception exception) {
		this(responseCode);
		this.exception = exception;
		try {
			String jsonStr = streamToString(is);
			json = new HALObject(jsonStr);
		} catch (Exception e) {
			e.printStackTrace();
			this.exception = e;
		}
	}

	public int getResponseCode() {
		return responseCode;
	}

	public HALObject getJson() {
		return json;
	}
	
	public boolean hasJson() {
		return json != null;
	}

	private String streamToString(InputStream is) throws UnsupportedEncodingException, IOException {
		StringBuilder sb = new StringBuilder();

		BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
		String line = null;
		while ((line = br.readLine()) != null) {
			sb.append(line + "\n");
		}
		br.close();

		return sb.toString();

	}

	public Exception getException() {
		return exception;
	}

}
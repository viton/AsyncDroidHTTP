package br.com.droidhttp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("rawtypes")
public class DroidHTTPResponse {
	private int responseCode;
	private DroidHTTPJSON json;
	
	public DroidHTTPResponse(int responseCode) {
		super();
		this.responseCode = responseCode;
	}
	
	public DroidHTTPResponse(int responseCode, InputStream is) throws UnsupportedEncodingException, IOException {
		this(responseCode);
		String jsonStr = streamToString(is);
		try {
			if (jsonStr.charAt(0) == '[') {
				json = new DroidHTTPJSON<JSONArray>(jsonStr, JSONArray.class);
			} else {
				json = new DroidHTTPJSON<JSONObject>(jsonStr, JSONObject.class);

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public int getResponseCode() {
		return responseCode;
	}

	public Object getJson() {
		return json.getJSON();
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
}
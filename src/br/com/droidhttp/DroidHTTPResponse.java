package br.com.droidhttp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class DroidHTTPResponse {
	private int responseCode;
	private JSONObject json;

	public DroidHTTPResponse(int responseCode, InputStream is) throws UnsupportedEncodingException, IOException {
		super();
		this.responseCode = responseCode;
		this.json = streamToJSON(is);

	}

	public int getResponseCode() {
		return responseCode;
	}

	public JSONObject getJson() {
		return json;
	}

	private JSONObject streamToJSON(InputStream is) throws UnsupportedEncodingException, IOException {
		StringBuilder sb = new StringBuilder();

		BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
		String line = null;
		while ((line = br.readLine()) != null) {
			sb.append(line + "\n");
		}
		br.close();

		try {
			return new JSONObject(sb.toString());
		} catch (JSONException e) {
			Log.e("DroidHTTP", "Fail to parse JSON", e);
		}

		return null;
	}
}
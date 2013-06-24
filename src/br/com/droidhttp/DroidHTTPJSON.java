package br.com.droidhttp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("unchecked")
public class DroidHTTPJSON<T> {
	private T json;

	public DroidHTTPJSON(String jsonStr, Class<T> jsonClass) throws JSONException {
		if (jsonClass == JSONArray.class) {
			json = (T) new JSONArray(jsonStr);
		} else if (jsonClass == JSONObject.class) {
			json = (T) new JSONObject(jsonStr);
		} else {
			throw new IllegalArgumentException("Only use a JSONArray or a JSONObject");
		}
	}

	public T getJSON() {
		return json;
	}
}
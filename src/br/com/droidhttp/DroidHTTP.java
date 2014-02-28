package br.com.droidhttp;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import org.json.JSONObject;

public class DroidHTTP implements DroidHTTPMethods {
	
	private String mainURL;
	private DroidHTTPParams droidParams;
	
	public DroidHTTP(String mainURL) {
		this.mainURL = mainURL;
	}
	
	public DroidHTTP(String mainURL, DroidHTTPParams params) {
		this.mainURL = mainURL;
		this.droidParams = params;
	}

	public DroidHTTPResponse get(String path) throws MalformedURLException, IOException {
		DroidHTTPRequest droidHTTPRequest = new DroidHTTPRequest(mainURL.concat(path), droidParams);
		return droidHTTPRequest.get();
	}

	public void get(String path, Map<String, String> params) {

	}

	public DroidHTTPResponse post(String path) throws MalformedURLException, IOException {
		return null;
	}

	public DroidHTTPResponse post(String path, JSONObject params) throws MalformedURLException, IOException {
		if (params == null) {
			throw new IllegalArgumentException("params can't be null");
		}

		DroidHTTPRequest droidHTTPRequest = new DroidHTTPRequest(mainURL.concat(path), droidParams);
		return droidHTTPRequest.post(params);
	}

	public DroidHTTPResponse put(String path) throws MalformedURLException, IOException {
		DroidHTTPRequest droidHTTPRequest = new DroidHTTPRequest(mainURL.concat(path), droidParams);
		return droidHTTPRequest.put(null);
	}

	public DroidHTTPResponse put(String path, JSONObject params) throws MalformedURLException, IOException {
		if (params == null) {
			throw new IllegalArgumentException("params can't be null");
		}

		DroidHTTPRequest droidHTTPRequest = new DroidHTTPRequest(mainURL.concat(path), droidParams);
		return droidHTTPRequest.put(params);
	}

	public DroidHTTPResponse delete(String path, JSONObject params) throws MalformedURLException, IOException {
		if (params == null) {
			throw new IllegalArgumentException("params can't be null");
		}

		DroidHTTPRequest droidHTTPRequest = new DroidHTTPRequest(mainURL.concat(path), droidParams);
		return droidHTTPRequest.delete(params);
	}

	@Override
	public DroidHTTPResponse delete(String path) throws MalformedURLException,
			IOException {
		DroidHTTPRequest droidHTTPRequest = new DroidHTTPRequest(mainURL.concat(path), droidParams);
		return droidHTTPRequest.delete();
	}
}
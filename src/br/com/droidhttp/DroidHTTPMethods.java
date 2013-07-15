package br.com.droidhttp;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONObject;

public interface DroidHTTPMethods {
	DroidHTTPResponse get(String path) throws MalformedURLException, IOException;

	// void get(String path, Map<String, String> params);

	DroidHTTPResponse post(String path) throws MalformedURLException, IOException;

	DroidHTTPResponse post(String path, JSONObject params) throws MalformedURLException, IOException;
	
	DroidHTTPResponse put(String path) throws MalformedURLException, IOException;
	
	DroidHTTPResponse put(String path, JSONObject params) throws MalformedURLException, IOException;
	
	DroidHTTPResponse delete(String path, JSONObject params) throws MalformedURLException, IOException;
}
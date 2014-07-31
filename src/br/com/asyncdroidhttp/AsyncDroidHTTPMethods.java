package br.com.asyncdroidhttp;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONObject;

import br.com.asyncdroidhttp.AsyncDroidHTTP.AsyncDroidHTTPCallback;

public interface AsyncDroidHTTPMethods {
	
	void execute(String method, String url, JSONObject params, final AsyncDroidHTTPCallback callback) throws MalformedURLException, IOException;
	
}
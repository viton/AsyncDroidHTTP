package br.com.asyncdroidhttp;

import org.json.JSONObject;

import android.os.AsyncTask;

public class AsyncDroidHTTP implements AsyncDroidHTTPMethods {
	
	private AsyncDroidHTTPParams droidParams;
	
	public AsyncDroidHTTP() {
	}
	
	public AsyncDroidHTTP(AsyncDroidHTTPParams params) {
		this.droidParams = params;
	}

	@Override
	public void execute(final String method, final String url,
			final JSONObject jsonParam, final AsyncDroidHTTPCallback callback){
		new AsyncTask<Void, Void, AsyncDroidHTTPResponse>() {
			
			@Override
			protected AsyncDroidHTTPResponse doInBackground(Void... params) {
				AsyncDroidHTTPRequest droidHTTPRequest = new AsyncDroidHTTPRequest(url, droidParams);
				AsyncDroidHTTPResponse response = droidHTTPRequest.via(method, jsonParam, droidParams);
				return response;
			}

			@Override
			protected void onPostExecute(AsyncDroidHTTPResponse result) {
				super.onPostExecute(result);
				if(result.getException() != null){
					callback.httpError(result.getResponseCode(), result.getException());
				}else{
					callback.httpSuccess(result.getResponseCode(), result.getJson());
				}
			}
		}.execute(new Void[0]);
	}
	
	public interface AsyncDroidHTTPCallback{
		
		public void httpSuccess(int httpResponseCode, Object object);
		
		public void httpError(int httpResponseCode, Exception exception);
		
	}
	
}
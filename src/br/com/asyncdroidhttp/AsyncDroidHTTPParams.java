package br.com.asyncdroidhttp;

public class AsyncDroidHTTPParams {

	
	private int readTimeOut;
	private int connectTimeOut;
	private boolean usesCache;
	private String contentType;
	
	public AsyncDroidHTTPParams(int readTimeOut, int connectTimeOut, boolean usesCache, String contentType){
		this.readTimeOut = readTimeOut;
		this.connectTimeOut = connectTimeOut;
		this.usesCache = usesCache;
		this.contentType = contentType;
	}
	
	public int getReadTimeOut() {
		return readTimeOut;
	}
	public int getConnectTimeOut() {
		return connectTimeOut;
	}
	public boolean isUsesCache() {
		return usesCache;
	}
	public String getContentType() {
		return contentType;
	}
	
}

package br.com.asyncdroidhttp;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class HALLinkObject implements Parcelable{

	private JSONObject json;
	private String href;
	private String method;
	
	public HALLinkObject(JSONObject json){
		this.json = json;
		if(this.json.has("href")){
			this.href = this.json.optString("href");
			this.json.remove("href");
		}
		if(this.json.has("method")){
			this.method = this.json.optString("method");
			this.json.remove("method");
		}
		
	}

	public JSONObject getJson() {
		return json;
	}

	public String getHref() {
		return href;
	}

	public String getMethod() {
		return method;
	}


	
	protected HALLinkObject(Parcel in){
		try {
			json = new JSONObject(in.readString());
			href = in.readString();
			method = in.readString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(json.toString());
		dest.writeString(href);
		dest.writeString(method);
	}
	
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<HALLinkObject> CREATOR = new Parcelable.Creator<HALLinkObject>() {
		public HALLinkObject createFromParcel(Parcel in) {
			return new HALLinkObject(in);
		}

		public HALLinkObject[] newArray(int size) {
			return new HALLinkObject[size];
		}
	};
	
	
}

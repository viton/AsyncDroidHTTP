package br.com.asyncdroidhttp;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class HALObject implements Parcelable{
	
	private JSONObject json;
	private List<HALLinkObject> links;
	private HALObject embeded;
	
	public HALObject(String jsonStr) throws JSONException {
		this.json = new JSONObject(jsonStr);
		if(this.json.has("_links")){
			links = new ArrayList<HALLinkObject>();
			JSONArray array = json.optJSONArray("_links"); 
			json.remove("_links");
			for(int i=0; i<array.length(); i++){
				links.add(new HALLinkObject(array.optJSONObject(i)));
			}
		}
		if(this.json != null && this.json.has("_embeded")){
			this.embeded= new HALObject(json.optJSONObject("_links").toString());
			json.remove("_embeded");
		}
		
	}

	public JSONObject getJSON(){
		return json;
	}
	
	public List<HALLinkObject> getLinks(){
		return links;
	}
	
	public HALObject getEmbeded(){
		return embeded;
	}
	
	public boolean hasLinks(){
		return (links != null && links.size() != 0);
	}
	
	public boolean hasEmbeded(){
		return (embeded != null);
	}
	
	protected HALObject(Parcel in){
		try {
			json = new JSONObject(in.readString());
			embeded = in.readParcelable(HALLinkObject.class.getClassLoader());
			in.readTypedList(links, HALLinkObject.CREATOR);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(json.toString());
		dest.writeParcelable(embeded, flags);
		dest.writeTypedList(links);
	}
	
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<HALObject> CREATOR = new Parcelable.Creator<HALObject>() {
		public HALObject createFromParcel(Parcel in) {
			return new HALObject(in);
		}

		public HALObject[] newArray(int size) {
			return new HALObject[size];
		}
	};
	
}
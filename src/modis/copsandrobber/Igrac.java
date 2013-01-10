package modis.copsandrobber;

import java.io.Serializable;

import android.content.Context;
import android.telephony.TelephonyManager;

public class Igrac implements Serializable {
	
	private String longitude, latitude;
	private String uloga;
	private String reg_id;
	
	public Igrac()
	{}
	
	public Igrac(String uloga, String lat, String longi, String id)
	{
		this.reg_id = id;
		this.uloga = uloga;
		this.latitude=lat;
		this.longitude=longi;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getUloga() {
		return uloga;
	}
	public void setUloga(String uloga) {
		this.uloga = uloga;
	}
	public String getImei() {
		return reg_id;
	}
	public void setImei(String id) {
		this.reg_id = id;
	}

}

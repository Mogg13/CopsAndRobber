package modis.copsandrobber;



public class Igrac {
	
	private String longitude, latitude;
	private String uloga;
	private String regId;
	private JedanOverlay overlay;
	
	public Igrac()
	{}
	
	public Igrac(String uloga, String lat, String longi, String id)
	{
		this.regId = id;
		this.uloga = uloga;
		this.latitude=lat;
		this.longitude=longi;
		if(uloga.equals("Cop"))
			this.overlay = new JedanOverlay(R.drawable.cop,lat,longi, "Cop");
		else
			this.overlay = new JedanOverlay(R.drawable.robber,lat,longi, "Robber");
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitudeAndOverlay(String longitude) {
		this.longitude = longitude;
		this.overlay.setLon(longitude);
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitudeAndOveray(String latitude) {
		this.latitude = latitude;
		this.overlay.setLat(latitude);
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
	public String getRegId() {
		return regId;
	}
	public void setRegId(String reg_id) {
		this.regId = reg_id;
	}

	public JedanOverlay getOverlay() {
		return overlay;
	}

	public void setOverlay(JedanOverlay overlay) {
		this.overlay = overlay;
	}
	

}

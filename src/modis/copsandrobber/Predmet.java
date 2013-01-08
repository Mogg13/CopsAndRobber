package modis.copsandrobber;

public class Predmet {
	
	private String ime;
	private String latitude, longitude;
	private int id;
	private int status;

	public Predmet()
	{
		
	}
	public Predmet(String ime, String latitude, String longitude, int id,
			int status) {
		super();
		this.ime = ime;
		this.latitude = latitude;
		this.longitude = longitude;
		this.id = id;
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public String getIme() {
		return ime;
	}
	public void setIme(String ime) {
		this.ime = ime;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

}

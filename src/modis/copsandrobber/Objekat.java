package modis.copsandrobber;

import java.util.List;

public class Objekat {
	
	private String ime;
	private String latitude, longitude;
	private List<String> predmeti;
	private int id;	
	private int status;
	
	public Objekat()
	{		
	}

	public Objekat(String ime, String latitude, String longitude,
			List<String> predmeti, int id, int status) {
		super();
		this.ime = ime;
		this.latitude = latitude;
		this.longitude = longitude;
		this.predmeti = predmeti;
		this.id = id;
		this.status = status;
	}
	
	public void addPredmet(String p)
	{
		predmeti.add(p);
	}
	
	public String getPredmetAt(int i)
	{
		return predmeti.get(i);
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
	public List<String> getPredmeti() {
		return predmeti;
	}
	public void setPredmeti(List<String> predmeti) {
		this.predmeti = predmeti;
	}

}

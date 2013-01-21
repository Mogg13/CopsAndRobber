package modis.copsandrobber;

import java.util.List;

public class Objekat {
	
	private String ime;
	private String latitude, longitude;
	private List<Predmet> predmeti;
	private int id;	
	private int status;
	private String cena;

	public Objekat()
	{		
	}

	public Objekat(String ime, String latitude, String longitude,
			List<Predmet> predmeti, int id, int status, String cena) {
		super();
		this.ime = ime;
		this.latitude = latitude;
		this.longitude = longitude;
		this.predmeti = predmeti;
		this.id = id;
		this.status = status;
		this.cena = cena;
	}

	public void addPredmet(Predmet p)
	{
		predmeti.add(p);
	}
	
	public Predmet getPredmetAt(int i)
	{
		return predmeti.get(i);
	}

	public List<Predmet> getPredmeti() {
		return predmeti;
	}

	public void setPredmeti(List<Predmet> predmeti) {
		this.predmeti = predmeti;
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
	public String getCena() {
		return cena;
	}
	public void setCena(String cena) {
		this.cena = cena;
	}

}

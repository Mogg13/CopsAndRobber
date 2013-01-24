package modis.copsandrobber;

import java.util.ArrayList;
import java.util.List;

public class Igra {

	private int id;
	private List<Igrac> igraci;
	private String latitude1, longitude1, latitude2, longitude2, latitude3, longitude3, latitude4, longitude4;	
	private String ime;
	private List<Objekat> objekti;
	private List<Predmet> predmeti;
	private int status;
	
	public Igra(){
		igraci = new ArrayList<Igrac>();
		objekti = new ArrayList<Objekat>();
		predmeti = new ArrayList<Predmet>();
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

	public void addObjekat(Objekat o){
		objekti.add(o);
	}
	
	public void addPredmet(Predmet p){
		predmeti.add(p);
	}
	
	public void addIgrac(Igrac i){
		igraci.add(i);
	}
	
	public Igrac getIgracAt(int i){
		return igraci.get(i);
	}
	
	public Objekat getObjekatAt(int i){
		return objekti.get(i);
	}
	
	public Predmet getPredmetAt(int i){
		return predmeti.get(i);
	}
	
	public List<Objekat> getObjekti(){
		return objekti;
	}
	
	public void setObjekti(List<Objekat> objekti){
		this.objekti = objekti;
	}
	
	public List<Predmet> getPredmeti(){
		return predmeti;
	}
	
	public void setPredmeti(List<Predmet> predmeti){
		this.predmeti = predmeti;
	}
	public String getLatitude1(){
		return latitude1;
	}
	
	public void setLatitude1(String latitude1){
		this.latitude1 = latitude1;
	}
	public String getLongitude1(){
		return longitude1;
	}
	
	public void setLongitude1(String longitude1){
		this.longitude1 = longitude1;
	}
	
	public String getLatitude2(){
		return latitude2;
	}
	
	public void setLatitude2(String latitude2){
		this.latitude2 = latitude2;
	}
	
	public String getLongitude2(){		
		return longitude2;
	}
	
	public void setLongitude2(String longitude2){
		this.longitude2 = longitude2;
	}
	
	public String getLatitude3(){
		return latitude3;
	}
	
	public void setLatitude3(String latitude3){
		this.latitude3 = latitude3;
	}
	
	public String getLongitude3() {
		return longitude3;
	}
	
	public void setLongitude3(String longitude3) {
		this.longitude3 = longitude3;
	}
	public String getLatitude4() {
		return latitude4;
	}
	
	public void setLatitude4(String latitude4) {
		this.latitude4 = latitude4;
	}
	public String getLongitude4() {
		return longitude4;
	}
	public void setLongitude4(String longitude4) {
		this.longitude4 = longitude4;
	}
	public String getIme() {
		return ime;
	}
	public void setIme(String ime) {
		this.ime = ime;
	}
	public List<Igrac> getIgraci() {
		return igraci;
	}
	public void setIgraci(List<Igrac> igraci) {
		this.igraci = igraci;
	}
	
	public Predmet getPredmetWithId(int id){
		int k=0;
		while(k<this.predmeti.size() && this.predmeti.get(k).getId() != id)
		{
			k++;
		}
		return this.predmeti.get(k);
	}
	
	public void editIgrac(String idIgraca, String latitude1, String longitude1){
		
		Igrac ig = getIgracById(idIgraca);
		ig.setLatitude(latitude1);
		ig.setLongitude(longitude1);
		/*for(int i = 0; i<igraci.size(); i++){
			if(igraci.get(i).getRegId().equals(idIgraca))
			{
				igraci.get(i).setLatitude(latitude1);
				igraci.get(i).setLongitude(longitude1);
			}
		}*/
	}
	
	public void editIgracWithOverlay(String idIgraca, String latitude1, String longitude1){
		
		Igrac ig = getIgracById(idIgraca);
		ig.setLatitudeAndOveray(latitude1);
		ig.setLongitudeAndOverlay(longitude1);
		/*for(int i = 0; i<igraci.size(); i++){
			if(igraci.get(i).getRegId().equals(idIgraca))
			{
				igraci.get(i).setLatitudeAndOveray(latitude1);
				igraci.get(i).setLongitudeAndOverlay(longitude1);
			}
		}*/
	}
	
	public Igrac getIgracById(String id){	
		Igrac res = new Igrac();
		boolean nadjen = false;
		int i = 0;
		while(i<igraci.size() && !nadjen)
		{
			if(igraci.get(i).getRegId().equals(id))
			{
				res = igraci.get(i);
				nadjen = true;
			}
			i++;
		}
		/*for(int i = 0; i<igraci.size(); i++){
			if(igraci.get(i).getRegId().equals(id))
			{
				res = igraci.get(i);
			}
		}
		*/
		return res;
	}
	
	public Igrac getLopov(){
		Igrac res = null;
		boolean nadjen = false;
		int i = 0;
		while(i<igraci.size() && !nadjen)
		{
			if(igraci.get(i).getUloga().equals("Lopov"))
			{
				res = igraci.get(i);
				nadjen = true;
			}
			i++;
		}
		/*
		for(int i = 0; i<igraci.size(); i++){
			if(igraci.get(i).getUloga().equals("Lopov"))
			{
				res = igraci.get(i);
			}
		}		
		*/
		return res;
	}
	
	public Objekat getObjekatByName(String name){
		Objekat res = null;
		boolean nadjen = false;
		int i = 0;
		while(i<objekti.size() && !nadjen)
		{
			if(objekti.get(i).getIme().equals(name))
			{
				res = objekti.get(i);
				nadjen = true;
			}
			i++;
		}
		/*for(int i=0; i<objekti.size(); i++)
		{
			if(objekti.get(i).getIme().equals(name))
			{
				res = objekti.get(i);
			}
		}*/
		
		return res;
	}
	
	public Objekat getObjekatWithId(int id){

		int k = 0;
		while(k < this.objekti.size() && this.objekti.get(k).getId() != id){
			k++;
		}
		return this.objekti.get(k);
	}
}

package modis.copsandrobber;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CopsandrobberHTTPHelper {
	
	//private static final String url = "http://uhvatilopova.site11.com";
	private static final String url = "http://copsandrobber.freetzi.com/";
	
	public static String napraviNovuIgru(Igrac igrac, String imeIgre)
	{
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url + "/create_new_game.php");
		
		String retStr;
		retStr = imeIgre;
		
		try {
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
			nameValuePairs.add(new BasicNameValuePair("ime",imeIgre));
			nameValuePairs.add(new BasicNameValuePair("pozicija", igrac.getUloga()));
			nameValuePairs.add(new BasicNameValuePair("latitude", igrac.getLatitude()));
			nameValuePairs.add(new BasicNameValuePair("longitude", igrac.getLongitude()));
			nameValuePairs.add(new BasicNameValuePair("idIgraca", igrac.getRegId()));
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
			
			HttpResponse response = httpClient.execute(httpPost);
			String str = inputStreamToString(response.getEntity().getContent()).toString();
			JSONObject jsonObject = new JSONObject(str);
		
			retStr = jsonObject.getString("greska");
						
		} catch (IOException e) {

			e.printStackTrace();
			retStr = "Error during upload!";
		} catch (JSONException e) {
			e.printStackTrace();
			retStr = "Error in JSON!";
		}
		return retStr;
	}
	
	public static String pridruziSeIgri(Igrac igrac, String imeIgre)
	{
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url + "/join_the_game.php");
		
		
		String retStr;
		retStr = imeIgre;
		
		try {
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
			nameValuePairs.add(new BasicNameValuePair("ime",imeIgre));
			nameValuePairs.add(new BasicNameValuePair("pozicija", igrac.getUloga()));
			nameValuePairs.add(new BasicNameValuePair("latitude", igrac.getLatitude()));
			nameValuePairs.add(new BasicNameValuePair("longitude", igrac.getLongitude()));
			nameValuePairs.add(new BasicNameValuePair("idIgraca", igrac.getRegId()));
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
			HttpResponse response = httpClient.execute(httpPost);
			String str = inputStreamToString(response.getEntity().getContent()).toString();
			JSONObject jsonObject = new JSONObject(str);			
			retStr = jsonObject.getString("pozicija");
						
		} catch (IOException e) {

			e.printStackTrace();
			retStr = "Error during upload!";
		} catch (JSONException e) {
			e.printStackTrace();
			retStr = "Error in JSON!";
		}
		return retStr;
		
	}
	
	public static List<String> getAllGames()
	{
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url+"/all_games.php");
		List<String> names = new ArrayList<String>();
		try{
			
			HttpResponse response = httpClient.execute(httpPost);
			String str = inputStreamToString(response.getEntity().getContent()).toString();
			JSONObject jsonObject = new JSONObject(str);
			JSONArray jsonArray = jsonObject.getJSONArray("imena");
			for(int i = 0; i<jsonArray.length(); i++){
				String name = jsonArray.getString(i);
				names.add(name);
			}
		} catch (Exception e){
			e.printStackTrace();
			names.add(e.getMessage());
		}
		return names;

	}
	
	public static String getGameSetup(String imeIgre) {
		// TODO Auto-generated method stub
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url + "/set_up_game.php");
				
		String retStr;
		retStr = "";
		
		try {
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("ime",imeIgre));
			
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
			
			HttpResponse response = httpClient.execute(httpPost);
			retStr = inputStreamToString(response.getEntity().getContent()).toString();
			
						
		} catch (IOException e) {

			e.printStackTrace();
			retStr = "Error during upload!";
		}
		return retStr;
	}

	
	private static StringBuilder inputStreamToString(InputStream is){
		String line = "";
		StringBuilder total = new StringBuilder();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		try {
			while ((line = rd.readLine()) != null) {
				total.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return total;
	}

	public static String getLocationUpdate(int id, String imei, String latitude, String longitude) {
		// TODO Auto-generated method stub
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url + "/update_location.php");
		
		String retStr;
		retStr = "";
		
		try {
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
			nameValuePairs.add(new BasicNameValuePair("idIgre", Integer.toString(id)));
			nameValuePairs.add(new BasicNameValuePair("longitude", longitude));
			nameValuePairs.add(new BasicNameValuePair("latitude", latitude));
			nameValuePairs.add(new BasicNameValuePair("idIgraca", imei));
			
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));			
			
			HttpResponse response = httpClient.execute(httpPost);
			retStr = inputStreamToString(response.getEntity().getContent()).toString();			
						
		} catch (IOException e) {

			e.printStackTrace();
			retStr = "Error during upload!";
		}
		return retStr;
	}

	public static void onPosition(String regId, String latitude, String longitude, int id, String en) {
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url + "/at_position.php");			
		try {			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
			nameValuePairs.add(new BasicNameValuePair("idIgre", Integer.toString(id)));
			nameValuePairs.add(new BasicNameValuePair("longitude", longitude));
			nameValuePairs.add(new BasicNameValuePair("latitude", latitude));
			nameValuePairs.add(new BasicNameValuePair("idIgraca", regId));
			nameValuePairs.add(new BasicNameValuePair("entering", en));
			
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));	
			httpClient.execute(httpPost);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void PredmetRobbed(int id, int id2) {

		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url + "/predmet_robbed.php");			
		try {			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("idIgre", Integer.toString(id)));
			nameValuePairs.add(new BasicNameValuePair("idPredmeta", Integer.toString(id2)));
			
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			httpClient.execute(httpPost);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void ObjectRobbed(int id, int id2, String regId) {
		// TODO Auto-generated method stub
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url + "/object_robbed.php");			
		try {			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("idIgre", Integer.toString(id)));
			nameValuePairs.add(new BasicNameValuePair("idObjekta", Integer.toString(id2)));
			nameValuePairs.add(new BasicNameValuePair("idIgraca", regId));
			
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));	
			httpClient.execute(httpPost);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void EndGame(int id, String poruka) {
		// TODO Auto-generated method stub
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url + "/end_game.php");			
		try {			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("idIgre", Integer.toString(id)));
			nameValuePairs.add(new BasicNameValuePair("poruka", poruka));
			
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));	
			httpClient.execute(httpPost);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void AktivirajPancir(int id, String regId) {
		// TODO Auto-generated method stub
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url + "/gcm_armor_activated.php");			
		try {			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("idIgre", Integer.toString(id)));
			nameValuePairs.add(new BasicNameValuePair("idIgraca", regId));
			
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));	
			httpClient.execute(httpPost);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void AktivirajOmetac(int id, String regId) {
		// TODO Auto-generated method stub
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url + "/gcm_ometac_activiran.php");			
		try {			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("idIgre", Integer.toString(id)));
			nameValuePairs.add(new BasicNameValuePair("idIgaca", regId));
			
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));	
			httpClient.execute(httpPost);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

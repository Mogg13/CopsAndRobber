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
	
private static final String url = "http://uhvatilopova.site11.com";
	
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
			nameValuePairs.add(new BasicNameValuePair("idIgraca", igrac.getImei()));
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
			nameValuePairs.add(new BasicNameValuePair("idIgraca", igrac.getImei()));
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

}

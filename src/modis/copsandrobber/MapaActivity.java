package modis.copsandrobber;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONObject;

import modis.copsandrobber.R;
import android.R.drawable;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class MapaActivity extends MapActivity{

	private Igra igra;
	private Igrac igrac;
	private String uloga;
	private MapView map;
	private MapController controller;
	private ImageView [] radar = new ImageView[5];
	private Handler guiThread;
	//Context context;
	private ExecutorService transThread;
	private ProgressDialog progressDialog;
	private List<Overlay> mapOverlays;
	private Projection projection;
	TextView timerIgre;
	private int brojac;
	
	
	@Override
	protected boolean isRouteDisplayed() {
		
		return false;
	}
	
	public void onCreate(Bundle savedInstanceState)	{
		
		super.onCreate(savedInstanceState);
		igra = new Igra();
		try {
			Intent mapIntent = getIntent();
			igrac = (Igrac)mapIntent.getSerializableExtra("igrac");
			Log.i("Igrac iz mape",igrac.getUloga());
			Bundle mapBundle = mapIntent.getExtras();
			if(mapBundle !=null)
			{
				String imeIgre = mapBundle.getString("imeIgre");
				igra.setIme(imeIgre);
			}
			
		} catch (Exception e) {
		}
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
        if(igrac.getUloga().equals("Policajac"))
        {
        	setContentView(R.layout.map_policajac);
        }
        else
        {
        	setContentView(R.layout.map_lopov);
        }
		//Inicijalizacija mape
		initMapView();
		initMyLocation();
		
		//radar
		radar[0] = (ImageView) findViewById(R.id.radarLinija0);
		radar[1] = (ImageView) findViewById(R.id.radarLinija1);
		radar[2] = (ImageView) findViewById(R.id.radarLinija2);
		radar[3] = (ImageView) findViewById(R.id.radarLinija3);
		radar[4] = (ImageView) findViewById(R.id.radarLinija4);
		
		radar[0].setImageResource(drawable.button_onoff_indicator_on);
		
		progressDialog = new ProgressDialog(this);
		ucitajPodatke();
		
		//tajmer
		timerIgre = (TextView) findViewById(R.id.timerIgre);
		brojac = 0;
		
		new CountDownTimer(7200000, 1000) {

		     public void onTick(long millisUntilFinished) {
		    	 
		    	 brojac++;
		    	 millisUntilFinished = millisUntilFinished/1000;
				 int sati = (int) (millisUntilFinished/3600);
				 int minuti = (int) ((millisUntilFinished % 3600) / 60);
				 int sekundi = (int) ((millisUntilFinished % 3600) % 60);
				  
				 String minString = "";
				 String secString = "";
				 if(minuti<10)
					 minString = "0" + Integer.toString(minuti);
				 else
					 minString = Integer.toString(minuti);
				 if(sekundi<10)
					 secString = "0" + Integer.toString(sekundi);
				 else
					 secString = Integer.toString(sekundi);
				  
		         timerIgre.setText( sati + ":" + minString + ":" + secString);
		         
		         if(brojac%10 == 0)	//10s refresh
		         {
		        	  ucitajPromeneDeset();
		         }
		         
		     }

			public void onFinish() {
		    	 timerIgre.setText("Kraj igre!");
		     }
		  }.start();
	}
	
	private void ucitajPodatke() {
		guiThread = new Handler();
		transThread = Executors.newSingleThreadExecutor();
		transThread.submit(new Runnable() {
			
			public void run() {
				guiProgressDialog(true);
				try{
					final String info = CopsandrobberHTTPHelper.getGameSetup(igra.getIme());
					guiParseInfo(info);
				} catch (Exception e){
					e.printStackTrace();
				}
				guiProgressDialog(false);
			}
		});
	}
	
    private void ucitajPromeneDeset() {
		// TODO Auto-generated method stub
    	guiThread = new Handler();
		transThread = Executors.newSingleThreadExecutor();
		transThread.submit(new Runnable() {
			
			public void run() {
				
				try{
					final String info = CopsandrobberHTTPHelper.getLocationUpdate(igra.getIme());
					
				} catch (Exception e){
					e.printStackTrace();
				}
				
			}
		});
		
	}
    
	private void guiProgressDialog(final boolean start){
		guiThread.post(new Runnable() {
			
			public void run() {
				if(start)
				{
					progressDialog.setMessage("Ucitavanje mape u toku...");
					progressDialog.show();
				}
				else
					progressDialog.dismiss();
				
			}
		});
	}
	
	private void guiParseInfo(final String info) {
		guiThread.post(new Runnable() {
			
			public void run() {
				
				try{
					int id;
					igra.setStatus(0);
					JSONObject jsonObject = new JSONObject(info);
					String pom = jsonObject.getString("latitude1");
					//Log.e("lat1",lat1 );
					igra.setLatitude1(pom);
					//Toast.makeText(UhvatiLopovaApplication.getContext(), pom, Toast.LENGTH_SHORT).show();
					pom = jsonObject.getString("latitude2");
					igra.setLatitude2(pom);
					
					pom = jsonObject.getString("latitude3");
					igra.setLatitude3(pom);
					
					pom = jsonObject.getString("latitude4");
					igra.setLatitude4(pom);
					
					pom = jsonObject.getString("longitude1");
					igra.setLongitude1(pom);
					
					pom = jsonObject.getString("longitude2");
					igra.setLongitude2(pom);
					
					pom = jsonObject.getString("longitude3");
					igra.setLongitude3(pom);
					
					pom = jsonObject.getString("longitude4");
					igra.setLongitude4(pom);
					
					id = jsonObject.getInt("idIgre");
					igra.setId(id);
					
					mapOverlays = map.getOverlays();
					projection = map.getProjection();
				    mapOverlays.add(new OkvirMape(igra));
				    
				    String imeObj, latObj, lonObj;
				    
				    List<String> predObj = new ArrayList<String>();
				    JSONObject obj;
				    JSONArray jsonArray = jsonObject.getJSONArray("objekti");
					for(int i = 0; i<jsonArray.length(); i++){
						obj = (JSONObject) jsonArray.get(i);
						imeObj = obj.getString("naziv");
						latObj = obj.getString("latitude");
						lonObj = obj.getString("longitude");
						id = obj.getInt("id");
						// treba da se preuzme lista predmeta
						igra.addObjekat(new Objekat(imeObj,latObj,lonObj, predObj, id, 0));
						Log.i("Ubacujem...", imeObj);
						mapOverlays.add(new JedanOverlay(vratiKodSlicice(imeObj), latObj, lonObj));
						Log.i("Ubaceno...", imeObj);
					}
					
					jsonArray = jsonObject.getJSONArray("predmeti");
					for(int i = 0; i<jsonArray.length(); i++){
						obj = (JSONObject) jsonArray.get(i);
						imeObj = obj.getString("naziv");
						latObj = obj.getString("latitude");
						lonObj = obj.getString("longitude");
						id = obj.getInt("id");
						igra.addPredmet(new Predmet(imeObj,latObj,lonObj, id, 0));
						Log.i("Ubacujem...", imeObj);
						mapOverlays.add(new JedanOverlay(vratiKodSlicice(imeObj), latObj, lonObj));
						Log.i("Ubaceno...", imeObj);
					}
				    
				    //mapOverlays.add(new JedanOverlay(R.drawable.cabin, "43.31452941894531", "21.888486862182617"));
					
				} catch (Exception e){
					e.printStackTrace();
					
				}
			}
		});
	}
	
	public int vratiKodSlicice(String ime)
	{
		int kod = 0;
		
		if(ime.equals("banka1"))
			kod = R.drawable.bank;
		if(ime.equals("banka2"))
			kod = R.drawable.bank;	
		if(ime.equals("menjacnica1"))
			kod = R.drawable.bankeuro;
		if(ime.equals("menjacnica2"))
			kod = R.drawable.bankeuro;
		if(ime.equals("zlatara"))
			kod = R.drawable.hotel1star;
		if(ime.equals("sigurna kuca"))
			kod = R.drawable.cabin;
		if(ime.equals("policija"))
			kod = R.drawable.police;
		
		if(ime.equals("luk i strela"))
			kod = R.drawable.archery;
		if(ime.equals("kljuc"))
			kod = R.drawable.carrental;
		if(ime.equals("alat"))
			kod = R.drawable.carrepair;
		if(ime.equals("lupa"))
			kod = R.drawable.cluster;
		if(ime.equals("okular"))
			kod = R.drawable.cluster3;
		if(ime.equals("kompjuter"))
			kod = R.drawable.computer;
		if(ime.equals("sedativi"))
			kod = R.drawable.drugs;
		if(ime.equals("aparat za gasenje"))
			kod = R.drawable.fire_extinguisher;
		if(ime.equals("pistolj"))
			kod = R.drawable.gun;
		if(ime.equals("puska"))
			kod = R.drawable.hunting;
		if(ime.equals("night vision"))
			kod = R.drawable.ophthalmologist;
		if(ime.equals("cekic"))
			kod = R.drawable.mine;
		if(ime.equals("mobilni"))
			kod = R.drawable.phones;
		if(ime.equals("fotoaparat"))
			kod = R.drawable.photo;
		if(ime.equals("dokument"))
			kod = R.drawable.postal;
		
		if(ime.equals("pancir"))
			kod = R.drawable.clothes_male;
		if(ime.equals("ometac"))
			kod = R.drawable.mobilephonetower;
		
		return kod;
	}

	private void initMapView() {
		
		map = (MapView) findViewById(R.id.mapView);
		controller = map.getController();
		map.setSatellite(true);
		map.setBuiltInZoomControls(true);

	}
	
	private void initMyLocation() {
		
		final MyLocationOverlay overlay = new MyLocationOverlay(this, map);
		overlay.enableMyLocation();
		overlay.runOnFirstFix(new Runnable(){
			public void run() {
				controller.setZoom(15);
				controller.animateTo(overlay.getMyLocation());
			}
		});
		map.getOverlays().add(overlay);
	}
	

}

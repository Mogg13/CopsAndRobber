package modis.copsandrobber;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONObject;

import modis.copsandrobber.R;
import android.R.drawable;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gcm.GCMRegistrar;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class MapaActivity extends MapActivity implements OnClickListener{

	private Igra igra;
	private Igrac igrac;
	//private String uloga;
	private MapView map;
	private MapController controller;
	private ImageView [] radar = new ImageView[5];
	private Handler guiThread;
	//Context context;
	private ExecutorService transThread;
	private ProgressDialog progressDialog;
	private List<Overlay> mapOverlays;
	private Projection projection;
	private TextView timerIgre;
	private int brojac10s;
	private int brojac6min;
	//private JedanOverlay jo;
	//double lat1proba, lat2proba, lat3proba, lon1proba, lon2proba, lon3proba;
	private String latLopova, lonLopova;
	private String [] latPolicajca = new String[3];
	private String [] lonPolicajca = new String[3];
	private String [] idPolicajca = new String [3];
	Context context;
	Intent intentMyService;
	ComponentName service;
	LocationManager lm;
	GPSListener myLocationListener;
	
	@Override
	protected boolean isRouteDisplayed() {
		
		return false;
	}
	
	public void onCreate(Bundle savedInstanceState)	{
		
		super.onCreate(savedInstanceState);
	    LocalBroadcastManager.getInstance(this).registerReceiver(
	    		mMessageReceiverGameStart, new IntentFilter("start_the_game"));
	    
		igra = new Igra();
		try {
			Intent mapIntent = getIntent();
			//igrac = (Igrac)mapIntent.getSerializableExtra("igrac");
			//Log.i("Igrac iz mape",igrac.getUloga());
			Bundle mapBundle = mapIntent.getExtras();
			if(mapBundle !=null)
			{
				String imeIgre = mapBundle.getString("imeIgre");
				igra.setIme(imeIgre);
				
				String uloga = mapBundle.getString("uloga");
				String lat = mapBundle.getString("lat");
				String lon = mapBundle.getString("lon");
				String reg_id = mapBundle.getString("reg_id");
				
				igrac = new Igrac(uloga,lat,lon,reg_id);
			}
			
		} catch (Exception e) {
		}
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
        if(igrac.getUloga().equals("Policajac"))
        {
        	setContentView(R.layout.map_policajac);
        	
        	View dugmePucaj = findViewById(R.id.dugmePucaj);
        	dugmePucaj.setOnClickListener(this);
        }
        else
        {
        	setContentView(R.layout.map_lopov);
        	
        	View dugmePancir = findViewById(R.id.dugmePancir);
        	dugmePancir.setOnClickListener(this);
        	
        	View dugmeOmetac = findViewById(R.id.dugmeOmetac);
        	dugmeOmetac.setOnClickListener(this);
        	
        	dugmeOmetac.setEnabled(false);
        	dugmePancir.setEnabled(false);
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
		
		//jo = new JedanOverlay(R.drawable.cop, "43.35689985", "21.88989982");
		/////////////////////////////////////////////////////////// PROBA
		/*lat1proba = 43.356885;
		lat2proba = 43.456986;
		lat3proba = 43.559869;
		lon1proba = 21.886598;
		lon2proba = 21.986656;
		lon3proba = 21.786525;
		
		igra.addIgrac(new Igrac("Lopov",Double.toString(lat1proba), Double.toString(lon1proba), "00000"));
		igra.addIgrac(new Igrac("Policajac",Double.toString(lat2proba), Double.toString(lon2proba), "11111"));
		igra.addIgrac(new Igrac("Policajac",Double.toString(lat3proba), Double.toString(lon3proba), "22222"));*/
		//////////////////////////////////////////////////////////////
		//tajmer
		timerIgre = (TextView) findViewById(R.id.timerIgre);

		brojac10s = 1;
		brojac6min = 1;
		
		//GSP LOKACIJA
		lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE); 
		myLocationListener = new GPSListener();
		long minTime=1000;
		float minDistance = 1;
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, myLocationListener);
		
		//PROHIMITY ALERTI
		if(igrac.getUloga().equals("Policajac"))
		{
			Log.i("PROXIMITY", "uso u petlju uu mapactivity");
			for(int i = 0;i<igra.getObjekti().size();i++)
			{
				if ( igra.getObjekatAt(i).getIme() == "policija")
				{
					Intent intent = new Intent("proximity_intent");
					PendingIntent proximityIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
					intent.putExtra("tip", "policija");
					lm.addProximityAlert(Double.parseDouble(igra.getObjekatAt(i).getLatitude()), Double.parseDouble(igra.getObjekatAt(i).getLongitude()), 30, -1, proximityIntent);
				}
			}
		    LocalBroadcastManager.getInstance(this).registerReceiver(
		    		mMessageProxReceiverPolicija, new IntentFilter("u_policiji"));
		}
		else
		{
			String imeIntenta;
			for(int i = 0;i<igra.getObjekti().size();i++)
			{
				//imeIntenta="Lokacija_objekta"+i;
				Intent intent = new Intent("proximity_intent");
				intent.putExtra("tip", "objekat");
				intent.putExtra("vrednost", igra.getObjekatAt(i).getIme());
				PendingIntent proximityIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
				
				lm.addProximityAlert(Double.parseDouble(igra.getObjekatAt(i).getLatitude()), Double.parseDouble(igra.getObjekatAt(i).getLongitude()), 10, -1, proximityIntent);
				
				
			}
			for(int i = 0;i<igra.getPredmeti().size();i++)
			{
				//imeIntenta="Lokacija_predmeta"+i;
				Intent intent = new Intent("proximity_intent");
				intent.putExtra("tip", "predmet");
				intent.putExtra("vrednost", igra.getPredmetAt(i).getIme());
				PendingIntent proximityIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
				
				lm.addProximityAlert(Double.parseDouble(igra.getPredmetAt(i).getLatitude()), Double.parseDouble(igra.getPredmetAt(i).getLongitude()), 10, -1, proximityIntent);
				
			}
		    LocalBroadcastManager.getInstance(this).registerReceiver(
		    		mMessageProxReceiverObjekat, new IntentFilter("u_objektu"));
		    LocalBroadcastManager.getInstance(this).registerReceiver(
		    		mMessageProxReceiverPredmet, new IntentFilter("u_predmetu"));
		}
		IntentFilter filter = new IntentFilter("proximity_intent");  
	    registerReceiver(new ProximityIntentReceiver(), filter);
	}
	
	private class GPSListener implements LocationListener{
		
		public GPSListener() {
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			 igrac.setLongitude(Double.toString(location.getLongitude()));
		     igrac.setLatitude(Double.toString(location.getLatitude()));
		     
		     Log.i("LOKACIJA", "primljen gps" + igrac.getLatitude() + " " + igrac.getLongitude());

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub			
		}
	};
	
	public void onClick(View v) {	
		
    	
    	switch(v.getId())
    	{
    		case R.id.dugmePancir:    		
    			break;    			
    		case R.id.dugmeOmetac:        		
    			break;    			
    		case R.id.dugmePucaj:        		
    			break;
    	}
	}
	
	protected void onDestroy() { 
		 
        try { 
           //stopService(intentMyService);
        } catch (Exception e) { 
            Log.e("Gasenje servisa - error", "> " + e.getMessage()); 
        } 
        super.onDestroy(); 
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
					final String info = CopsandrobberHTTPHelper.getLocationUpdate(igra.getId(), igrac.getImei(), igrac.getLatitude(), igrac.getLongitude());
					JSONObject jsonObject = new JSONObject(info);
					JSONObject obj;
				    JSONArray jsonArray = jsonObject.getJSONArray("igraci");
				    for(int i = 0; i<jsonArray.length(); i++){
				    	obj = (JSONObject) jsonArray.get(i);
						String idIgraca = obj.getString("idIgraca");
						String latIgraca = obj.getString("latitude");
						String lonIgraca = obj.getString("longitude");
						//igra.EditIgraci(idIgraca, latIgraca, lonIgraca);
						if(igrac.getUloga().equals("Policajac"))
						{
							if(igra.getIgracById(idIgraca).equals("Lopov"))
							{
								latLopova = latIgraca;
								lonLopova = lonIgraca;
							}
							else
							{
								igra.EditIgraci(idIgraca, latIgraca, lonIgraca);
							}
						}
						else
						{
							latPolicajca[i] = latIgraca;
							lonPolicajca[i] = lonIgraca;
							idPolicajca[i] = idIgraca;
						}
						//TO-DO
						//updateRadar();
				    }
				} catch (Exception e){
					e.printStackTrace();
				}
				
			}
		});
	
    	/*
    	igra.getIgracAt(0).setLatitude(Double.toString(lat1proba));
 		igra.getIgracAt(0).setLongitude(Double.toString(lon1proba));
 		igra.getIgracAt(1).setLatitude(Double.toString(lat2proba));
 		igra.getIgracAt(1).setLongitude(Double.toString(lon2proba));
 		igra.getIgracAt(2).setLatitude(Double.toString(lat3proba));
 		igra.getIgracAt(2).setLongitude(Double.toString(lon3proba));
    	//Log.i("ucitajPromeneDeset", "da");
    	/*
    	if(mapOverlays.contains(igra.getIgracAt(0).getOverlay()))
    	{
    		//Log.i("SADRZI", "da");
    		mapOverlays.remove(igra.getIgracAt(0).getOverlay());
    		
    		mapOverlays.add(igra.getIgracAt(0).getOverlay());
    	}
    	else
    	{
    		mapOverlays.add(igra.getIgracAt(0).getOverlay());
    	}
    	*-/
 		if(!mapOverlays.contains(igra.getIgracAt(0).getOverlay()))
    	{
    		mapOverlays.add(igra.getIgracAt(0).getOverlay());
    	}
    	//////////////////////////////////////////////////////////
    	if(!mapOverlays.contains(igra.getIgracAt(1).getOverlay()))
    	{
    		mapOverlays.add(igra.getIgracAt(1).getOverlay());
    	}    	
		//////////////////////////////////////////////////////////
		if(!mapOverlays.contains(igra.getIgracAt(2).getOverlay()))
		{
			mapOverlays.add(igra.getIgracAt(2).getOverlay());
		}

		
    	*/

	}
    
    private void ucitajPromeneSestMin() {
		// TODO Auto-generated method stub
    	guiThread = new Handler();
		transThread = Executors.newSingleThreadExecutor();
		transThread.submit(new Runnable() {
			
			public void run() {
				
				try{
					final String info = CopsandrobberHTTPHelper.getLocationUpdate(igra.getId(), igrac.getImei(), igrac.getLatitude(), igrac.getLongitude());
					JSONObject jsonObject = new JSONObject(info);
					JSONObject obj;
				    JSONArray jsonArray = jsonObject.getJSONArray("igraci");
				    for(int i = 0; i<jsonArray.length(); i++){
				    	obj = (JSONObject) jsonArray.get(i);
						String idIgraca = obj.getString("idIgraca");
						String latIgraca = obj.getString("latitude");
						String lonIgraca = obj.getString("longitude");
						igra.EditIgraci(idIgraca, latIgraca, lonIgraca);
						
						//TO-DO
						//updateRadar();
						
						if(!mapOverlays.contains(igra.getIgracAt(0).getOverlay()))
				    	{
				    		mapOverlays.add(igra.getIgracAt(0).getOverlay());
				    	}
				    	//////////////////////////////////////////////////////////
				    	if(!mapOverlays.contains(igra.getIgracAt(1).getOverlay()))
				    	{
				    		mapOverlays.add(igra.getIgracAt(1).getOverlay());
				    	}    	
						//////////////////////////////////////////////////////////
						if(!mapOverlays.contains(igra.getIgracAt(2).getOverlay()))
						{
							mapOverlays.add(igra.getIgracAt(2).getOverlay());
						}
				    }
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

				    JSONObject obj;
				    JSONArray jsonArray = jsonObject.getJSONArray("predmeti");
					for(int i = 0; i<jsonArray.length(); i++){
						obj = (JSONObject) jsonArray.get(i);
						imeObj = obj.getString("naziv");
						latObj = obj.getString("latitude");
						lonObj = obj.getString("longitude");
						id = obj.getInt("id");
						igra.addPredmet(new Predmet(imeObj,latObj,lonObj, id, 0));
						//Log.i("Ubacujem...", imeObj);
						if(igrac.getUloga().equals("Lopov"))
						{
							mapOverlays.add(new JedanOverlay(vratiKodSlicice(imeObj), latObj, lonObj));
						}
						//Log.i("Ubaceno...", imeObj);
					}
					
					jsonArray = jsonObject.getJSONArray("objekti");
					JSONArray jArrayUslov;
					JSONObject objUslov;
					int uslovId;
					List<Predmet> predObj = new ArrayList<Predmet>();
					for(int i = 0; i<jsonArray.length(); i++){
						obj = (JSONObject) jsonArray.get(i);
						imeObj = obj.getString("naziv");
						latObj = obj.getString("latitude");
						lonObj = obj.getString("longitude");
						id = obj.getInt("id");

						jArrayUslov = obj.getJSONArray("uslovi");
						for(int j = 0; j<jArrayUslov.length(); j++)
						{
							objUslov = (JSONObject) jArrayUslov.get(j);
							uslovId = objUslov.getInt("idpUslova");
							//predmetUslov = igra.getPredmetWithId(uslovId);
							predObj.add(igra.getPredmetWithId(uslovId));
						}
						
						igra.addObjekat(new Objekat(imeObj,latObj,lonObj, predObj, id, 0));
						predObj = new ArrayList<Predmet>();
						//Log.i("Ubacujem...", imeObj);
						
						if(imeObj.equals("sigurna kuca") && igrac.getUloga().equals("Policajac"))
						{
							//do nothing
						}
						else
						{
							mapOverlays.add(new JedanOverlay(vratiKodSlicice(imeObj), latObj, lonObj));
						}
					}
					
					//samo za test
					/*
					for(int i=0; i<igra.getObjekti().size(); i++)
					{
						for(int j = 0; j<igra.getObjekatAt(i).getPredmeti().size(); j++)
						{
							Log.i("Objekat " + igra.getObjekatAt(i).getIme() + " " + igra.getObjekatAt(i).getId(), igra.getObjekatAt(i).getPredmetAt(j).getIme() + " " + igra.getObjekatAt(i).getPredmetAt(j).getId() );
						}
					}
				    */
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
	
    private BroadcastReceiver mMessageReceiverGameStart = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {

        	new CountDownTimer(7200000, 1000) {

   		     public void onTick(long millisUntilFinished) {
   		    	 
   				  		    	 
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
   		         
   		         if(brojac10s>=10)	//10s refresh
   		         {
   		        	 brojac10s = 0;
   		        	 if(brojac6min >= 36) // 6min refresh
   		        	 {
   		        		 ucitajPromeneSestMin();
   		        		 brojac6min = 0;
   		        	 }
   		        	 else
   		        	 {
   		        		ucitajPromeneDeset();
   		        	 }
   		        	 brojac6min++;
   		         }
   		         
   		         brojac10s++;
   		         
   		         //////// proba
   		        /* lat1proba += 0.01;
   		 		lat2proba += 0.01;
   		 		lat3proba += 0.01;
   		 		lon1proba += 0.01;
   		 		lon2proba += 0.01;
   		 		lon3proba += 0.01;*/
   		 		
   		 		
   		     }

   			public void onFinish() {
   		    	 timerIgre.setText("Kraj igre!");
   		     }
   		  }.start();
        }
    };
    
    private BroadcastReceiver mMessageProxReceiverPolicija = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			
		}
    	
    };
    private BroadcastReceiver mMessageProxReceiverObjekat = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			
		}
    	
    };
    private BroadcastReceiver mMessageProxReceiverPredmet= new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			
		}
    	
    };

}

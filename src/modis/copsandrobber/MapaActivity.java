package modis.copsandrobber;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONObject;

import android.R.drawable;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

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
	private static Context context;
	private Intent intentMyService;
	private ComponentName service;
	private LocationManager lm;
	private GPSListener myLocationListener;
	private ProximityIntentReceiver proxReciever;
	private Boolean statusIgre;
	//private String entering;
	private Intent pomocniIntent;
	//private boolean inicijalizovano;
	
	private TextView brmetaka;
	private int brojMetaka;
	private View dugmePucaj;
	private View dugmePancir;
	private View dugmeOmetac;
	
	@Override
	protected boolean isRouteDisplayed() {
		
		return false;
	}
	
	public void onCreate(Bundle savedInstanceState)	{
		
		super.onCreate(savedInstanceState);
	    LocalBroadcastManager.getInstance(this).registerReceiver(
	    		mMessageReceiverGameStart, new IntentFilter("start_the_game"));
	    
		igra = new Igra();
		proxReciever = new ProximityIntentReceiver();
		context = this;
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
        	
        	dugmePucaj = findViewById(R.id.dugmePucaj);
        	dugmePucaj.setOnClickListener(this);
        	
        	brojMetaka = 3;
        }
        else
        {
        	setContentView(R.layout.map_lopov);
        	
        	dugmePancir = findViewById(R.id.dugmePancir);
        	dugmePancir.setOnClickListener(this);
        	
        	dugmeOmetac = findViewById(R.id.dugmeOmetac);
        	dugmeOmetac.setOnClickListener(this);
        	
        	// da se onemoguce dugmici
        	//dugmeOmetac.setEnabled(false);
        	//dugmePancir.setEnabled(false);
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

		brojac10s = 1;
		brojac6min = 1;
		
		//GSP LOKACIJA
		lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE); 
		myLocationListener = new GPSListener();
		long minTime=1000;
		float minDistance = 1;
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, myLocationListener);
		
		statusIgre = false;		
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
    			inicijalizujIgrace();
            	
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
       		     }
       			public void onFinish() {
       		    	 timerIgre.setText("Kraj igre!");
       		     }
       		  }.start();
       		  
       		  statusIgre = true;
    			break;    			
    		case R.id.dugmeOmetac:        		
    			break;    			
    		case R.id.dugmePucaj: 	
    			inicijalizujIgrace();
            	
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
       		     }
       			public void onFinish() {
       		    	 timerIgre.setText("Kraj igre!");
       		     }
       		  }.start();
       		  
       		  statusIgre = true;
    			break;
    	}
	}
	
	protected void onDestroy() { 
		 
        try { 
           //stopService(intentMyService);
        	this.unregisterReceiver(proxReciever);
        } catch (Exception e) { 
            Log.e("Gasenje servisa - error", "> " + e.getMessage()); 
        } 
        super.onDestroy(); 
    } 
	
	private void ucitajProximityPodesavanja()
	{
		//PROHIMITY ALERTI
		if(igrac.getUloga().equals("Policajac"))
		{
			//Log.i("PROXIMITY: OBJEKAT", Integer.toString(igra.getObjekti().size()));
			for(int i = 0;i<igra.getObjekti().size();i++)
			{
				//Log.i("PROXIMITY: OBJEKAT", igra.getObjekatAt(i).getIme());
				if ( igra.getObjekatAt(i).getIme().equals("policija"))
				{
					Log.i("PROXIMITY", "uso u petlju uu mapactivity");
					Intent intent = new Intent("modis.copsandrobber.proximity_intent");
					intent.putExtra("tip", "policija");
					PendingIntent proximityIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
					
					lm.addProximityAlert(Double.parseDouble(igra.getObjekatAt(i).getLatitude()), Double.parseDouble(igra.getObjekatAt(i).getLongitude()), 30, -1, proximityIntent);
					//Log.i("PROXIMITY: ALARM", igra.getObjekatAt(i).getIme());
				}
			}
		    LocalBroadcastManager.getInstance(this).registerReceiver(
		    		mMessageProxReceiverPolicija, new IntentFilter("u_policiji"));
			IntentFilter filter = new IntentFilter("modis.copsandrobber.proximity_intent");  
		    registerReceiver(new ProximityIntentReceiver(), filter);
		}
		else
		{
			String imeIntenta;
			for(int i = 0;i<igra.getObjekti().size();i++)
			{
				if ( !igra.getObjekatAt(i).getIme().equals("policija"))
				{
					Log.i("OOOOOOO", igra.getObjekatAt(i).getIme());
					imeIntenta="modis.copsandrobber.proximity_intent_o"+Integer.toString(i);
					Intent intent = new Intent(imeIntenta);
					intent.putExtra("tip", "objekat");
					intent.putExtra("vrednost", i);
					PendingIntent proximityIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
					
					lm.addProximityAlert(Double.parseDouble(igra.getObjekatAt(i).getLatitude()), Double.parseDouble(igra.getObjekatAt(i).getLongitude()), 10, -1, proximityIntent);
					
					IntentFilter filter = new IntentFilter(imeIntenta);  
				    registerReceiver(proxReciever, filter);
				}
				
			}
			for(int i = 0;i<igra.getPredmeti().size();i++)
			{
				Log.i("PPPPPPPP", igra.getPredmetAt(i).getIme());
				imeIntenta="modis.copsandrobber.proximity_intent_p"+Integer.toString(i);
				Intent intent = new Intent(imeIntenta);
				intent.putExtra("tip", "predmet");
				intent.putExtra("vrednost", i);
				PendingIntent proximityIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
				
				lm.addProximityAlert(Double.parseDouble(igra.getPredmetAt(i).getLatitude()), Double.parseDouble(igra.getPredmetAt(i).getLongitude()), 10, -1, proximityIntent);
				
				IntentFilter filter = new IntentFilter(imeIntenta);  
			    registerReceiver(proxReciever, filter);
			}
			
		    LocalBroadcastManager.getInstance(this).registerReceiver(
		    		mMessageProxReceiverObjekat, new IntentFilter("u_objektu"));
		    LocalBroadcastManager.getInstance(this).registerReceiver(
		    		mMessageProxReceiverPredmet, new IntentFilter("u_predmetu"));
		}
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
				//notify();				
			}
		});
		/*
		try {
			guiThread.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
    private void ucitajPromeneDeset() {
		// TODO Auto-generated method stub
    	
    	/*guiThread = new Handler();
		transThread = Executors.newSingleThreadExecutor();
		transThread.submit(new Runnable() {
			
			public void run() {*/
				
				try{
					final String info = CopsandrobberHTTPHelper.getLocationUpdate(igra.getId(), igrac.getRegId(), igrac.getLatitude(), igrac.getLongitude());
					JSONObject jsonObject = new JSONObject(info);
					JSONObject obj;
					String str = info;
					Log.i("JSONNN_"+igrac.getUloga(), str);
				    JSONArray jsonArray = jsonObject.getJSONArray("igraci");
				    for(int i = 0; i<jsonArray.length(); i++){
				    	obj = (JSONObject) jsonArray.get(i);
						String idIgraca = obj.getString("idIgraca");
						String latIgraca = obj.getString("latitude");
						String lonIgraca = obj.getString("longitude");
						//igra.EditIgraci(idIgraca, latIgraca, lonIgraca);
						if(igrac.getUloga().equals("Policajac"))
						{
							if(igra.getIgracById(idIgraca).getUloga().equals("Lopov"))
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
						
						
				    }
				    
				    updateRadar();
				    
				} catch (Exception e){
					e.printStackTrace();
				}
				/*
			}
		});
	*/
	}
    
    private void ucitajPromeneSestMin() {
		// TODO Auto-generated method stub
    	/*guiThread = new Handler();
		transThread = Executors.newSingleThreadExecutor();
		transThread.submit(new Runnable() {
			
			public void run() {*/
				
				try{
					final String info = CopsandrobberHTTPHelper.getLocationUpdate(igra.getId(), igrac.getRegId(), igrac.getLatitude(), igrac.getLongitude());
					JSONObject jsonObject = new JSONObject(info);
					String str = info;
					Log.i("JSONNN_"+igrac.getUloga(), str);
					JSONObject obj;
				    JSONArray jsonArray = jsonObject.getJSONArray("igraci");
				    for(int i = 0; i<jsonArray.length(); i++){
				    	obj = (JSONObject) jsonArray.get(i);
						String idIgraca = obj.getString("idIgraca");
						String latIgraca = obj.getString("latitude");
						String lonIgraca = obj.getString("longitude");
						igra.EditIgraci(idIgraca, latIgraca, lonIgraca);
						
						if(igrac.getUloga().equals("Policajac"))
						{
							if(igra.getIgracById(idIgraca).getUloga().equals("Lopov"))
							{
								latLopova = latIgraca;
								lonLopova = lonIgraca;
							}
						}
						else
						{
							latPolicajca[i] = latIgraca;
							lonPolicajca[i] = lonIgraca;
							idPolicajca[i] = idIgraca;
						}
				    }
				    
				  
					if(igra.getIgraci().size()>0)
						if(!mapOverlays.contains(igra.getIgracAt(0).getOverlay()))
				    	{
				    		mapOverlays.add(igra.getIgracAt(0).getOverlay());
				    	}
			    	//////////////////////////////////////////////////////////
					if(igra.getIgraci().size()>1)
				    	if(!mapOverlays.contains(igra.getIgracAt(1).getOverlay()))
				    	{
				    		mapOverlays.add(igra.getIgracAt(1).getOverlay());
				    	}    	
					//////////////////////////////////////////////////////////
					if(igra.getIgraci().size()>2)
						if(!mapOverlays.contains(igra.getIgracAt(2).getOverlay()))
						{
							mapOverlays.add(igra.getIgracAt(2).getOverlay());
						}
					
					
					updateRadar();
					
				} catch (Exception e){
					e.printStackTrace();
				}
				/*
			}
		});
		*/
	}
    
    public void updateRadar()
    {
    	//ako je kod 1 - onda je pozvana iz ucitajPromeneDeset
    	// ako je kod 2 - onda je pozvana iz ucitajPromeneSestMin
    	float [] results = new float[1];
    	float distance = 0;
    	if(igrac.getUloga().equals("Policajac"))
		{
			Location.distanceBetween(Double.parseDouble(igrac.getLatitude()), Double.parseDouble(igrac.getLongitude()), Double.parseDouble(latLopova), Double.parseDouble(lonLopova), results);
			distance = results[0];
			
		}
		else
		{
			float pom;
			distance = 999999999;
			for(int i=0;i<3;i++)
			{
				Location.distanceBetween(Double.parseDouble(igrac.getLatitude()), Double.parseDouble(igrac.getLongitude()), Double.parseDouble(latPolicajca[i]), Double.parseDouble(lonPolicajca[i]), results);
				pom = results[0];
				if (pom < distance)
				{
					distance = pom;
				}
			}					
		}
    	
    	
    	Log.i("DISTANCE", Float.toString(distance));
    }
    
    public void inicijalizujIgrace()//poziva se kad dodje start signal
    {
    	
    	try{
			final String info = CopsandrobberHTTPHelper.getLocationUpdate(igra.getId(), igrac.getRegId(), igrac.getLatitude(), igrac.getLongitude());
			JSONObject jsonObject = new JSONObject(info);
			String str = info;
			Log.i("JSONNN", str);
			JSONObject obj;
		    JSONArray jsonArray = jsonObject.getJSONArray("igraci");
		    for(int i = 0; i<jsonArray.length(); i++){
		    	obj = (JSONObject) jsonArray.get(i);
				String idIgraca = obj.getString("idIgraca");
				String latIgraca = obj.getString("latitude");
				String lonIgraca = obj.getString("longitude");
				String uloga = obj.getString("uloga");
				igra.addIgrac(new Igrac(uloga, latIgraca, lonIgraca, idIgraca));				
		    }
		} catch (Exception e){
			e.printStackTrace();
		}
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
				    
				    String imeObj, latObj, lonObj, cenaObj;

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
							mapOverlays.add(new JedanOverlay(vratiKodSlicice(imeObj), latObj, lonObj, imeObj));
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
						cenaObj = obj.getString("cena");
						id = obj.getInt("id");
						jArrayUslov = obj.getJSONArray("uslovi");
						for(int j = 0; j<jArrayUslov.length(); j++)
						{
							objUslov = (JSONObject) jArrayUslov.get(j);
							uslovId = objUslov.getInt("idpUslova");
							//predmetUslov = igra.getPredmetWithId(uslovId);
							predObj.add(igra.getPredmetWithId(uslovId));
						}
						Objekat oTemp = new Objekat(imeObj,latObj,lonObj, predObj, id, 0, cenaObj);
						igra.addObjekat(oTemp);
						predObj = new ArrayList<Predmet>();
						//Log.i("Ubacujem...", imeObj);						
						if(imeObj.equals("sigurna kuca") && igrac.getUloga().equals("Policajac"))
						{
							//do nothing
						}
						else
						{
							mapOverlays.add(new JedanOverlay(vratiKodSlicice(imeObj), latObj, lonObj, oTemp, igrac.getUloga(), imeObj));
						}

					}
					// kod za menjanje overlay ikonice
					/*for(int i=0;i<mapOverlays.size();i++)
					{
						if(mapOverlays.get(i) instanceof JedanOverlay)
							if(((JedanOverlay)mapOverlays.get(i)).getIme().equals("luk i strela"))
							{
								((JedanOverlay)mapOverlays.get(i)).setBitmap(vratiKodXSlicice("luk i strela"));
							}
					}*/
					ucitajProximityPodesavanja();
										
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
	
	public int vratiKodXSlicice(String ime)
	{
		int kod = 0;
		
		if(ime.equals("banka1"))
			kod = R.drawable.xbank;
		if(ime.equals("banka2"))
			kod = R.drawable.xbank;	
		if(ime.equals("menjacnica1"))
			kod = R.drawable.xbankeuro;
		if(ime.equals("menjacnica2"))
			kod = R.drawable.xbankeuro;
		if(ime.equals("zlatara"))
			kod = R.drawable.xhotel1star;
		
		
		if(ime.equals("luk i strela"))
			kod = R.drawable.xarchery;
		if(ime.equals("kljuc"))
			kod = R.drawable.xcarrental;
		if(ime.equals("alat"))
			kod = R.drawable.xcarrepair;
		if(ime.equals("lupa"))
			kod = R.drawable.xcluster;
		if(ime.equals("okular"))
			kod = R.drawable.xcluster3;
		if(ime.equals("kompjuter"))
			kod = R.drawable.xcomputer;
		if(ime.equals("sedativi"))
			kod = R.drawable.xdrugs;
		if(ime.equals("aparat za gasenje"))
			kod = R.drawable.xfire_extinguisher;
		if(ime.equals("pistolj"))
			kod = R.drawable.xgun;
		if(ime.equals("puska"))
			kod = R.drawable.xhunting;
		if(ime.equals("night vision"))
			kod = R.drawable.xophthalmologist;
		if(ime.equals("cekic"))
			kod = R.drawable.xmine;
		if(ime.equals("mobilni"))
			kod = R.drawable.xphones;
		if(ime.equals("fotoaparat"))
			kod = R.drawable.xphoto;
		if(ime.equals("dokument"))
			kod = R.drawable.xpostal;
		
		if(ime.equals("pancir"))
			kod = R.drawable.xclothes_male;
		if(ime.equals("ometac"))
			kod = R.drawable.xmobilephonetower;
		
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
        	
        	inicijalizujIgrace();
        	
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
   		     }
   			public void onFinish() {
   		    	 timerIgre.setText("Kraj igre!");
   		     }
   		  }.start();
   		  
   		  statusIgre = true;
        }
    };
    private BroadcastReceiver mMessageProxReceiverPolicija = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			
			pomocniIntent = arg1;
			//entering = arg1.getStringExtra("entering");
			if(statusIgre == true)
			{	//
				brojMetaka = 3;
				brmetaka = (TextView) findViewById(R.id.textBrMetaka);
				brmetaka.setText("3");
			}
			else
			{
				guiThread = new Handler();
				transThread = Executors.newSingleThreadExecutor();
				transThread.submit(new Runnable() {
					
					public void run() {
						try{
							String entering = pomocniIntent.getStringExtra("entering");
							CopsandrobberHTTPHelper.onPosition(igrac.getRegId(), igrac.getLatitude(), igrac.getLongitude(), igra.getId(), entering);
						} catch (Exception e){
							e.printStackTrace();
						}
					}
				});
			}			
		}
    	
    };
    private BroadcastReceiver mMessageProxReceiverObjekat = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			
			pomocniIntent = arg1; 
			Log.i("PROXI", "pre sigurna kuca");
			guiThread = new Handler();
			transThread = Executors.newSingleThreadExecutor();
			transThread.submit(new Runnable() {
				
				public void run() {
					try{
						Bundle b = pomocniIntent.getExtras();
						int i = b.getInt("vrednost");
						String entering = b.getString("entering");
						if(statusIgre == false)
						{
							Log.i("PROXI", igra.getObjekatAt(i).getIme());
							if(igra.getObjekatAt(i).getIme().equals("sigurna kuca"))
							{
								Log.i("PROXI", entering);
								CopsandrobberHTTPHelper.onPosition(igrac.getRegId(), igrac.getLatitude(), igrac.getLongitude(), igra.getId(), entering);
							}
						}
						else
						{
							Log.i("PROXI", "igra u toku");
							if(igra.getObjekatAt(i).getStatus() == 0 && entering.equals("true"))
							{
								/*if(igra.getObjekatAt(i).getDostupan() == igra.getObjekatAt(i).getPredmeti().size())
								{
									igra.getObjekatAt(i).setStatus(1);
									CopsandrobberHTTPHelper.ObjectRobbed(igra.getId(), igra.getObjekatAt(i).getId(), igrac.getRegId());
								}*/
								int j = 0;
								while(igra.getObjekatAt(i).getPredmetAt(j).getStatus() == 1 && (j+1) != igra.getObjekatAt(i).getPredmeti().size())
								{
									j++;
								}
								if((j+1) == igra.getObjekatAt(i).getPredmeti().size())
								{
									igra.getObjekatAt(i).setStatus(1);
									CopsandrobberHTTPHelper.ObjectRobbed(igra.getId(), igra.getObjekatAt(i).getId(), igrac.getRegId());
								}
							}
						}
					} catch (Exception e){
						e.printStackTrace();
					}
				}
			});
		}
    	
    };
    private BroadcastReceiver mMessageProxReceiverPredmet= new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			pomocniIntent = arg1; 
			guiThread = new Handler();
			transThread = Executors.newSingleThreadExecutor();
			transThread.submit(new Runnable() {
				
				public void run() {
					try{
						if(statusIgre == true)
						{		
							Bundle b = pomocniIntent.getExtras();
							int i = b.getInt("vrednost");
							CopsandrobberHTTPHelper.PredmetRobbed(igra.getId(), igra.getPredmetAt(i).getId());
						}
					} catch (Exception e){
						e.printStackTrace();
					}
				}
			});
			Bundle b = pomocniIntent.getExtras();
			int i = b.getInt("vrednost");
			igra.getPredmetAt(i).setStatus(1);
			if(igra.getPredmetAt(i).getIme().equals("pancir"))
			{															
	        	dugmePancir.setEnabled(true);
			}
			else if(igra.getPredmetAt(i).getIme().equals("ometac"))
			{
				dugmeOmetac.setEnabled(true);
			}
		}
    	
    };

    
    /*private void UpdateStatusObjekta(int id)
    {
    	for(int i=0; i<igra.getObjekti().size();i++)
    	{
    		for(int j=0;j<igra.getObjekatAt(i).getPredmeti().size();j++)
    		{
    			if(id == igra.getObjekatAt(i).getPredmetAt(j).getId())
    			{
    				igra.getObjekatAt(i).setDostupan(igra.getObjekatAt(i).getDostupan() + 1);
    			}
    			
    		}
    	}

    }*/
    
    public static void napraviDialogZaObjekat(Objekat obj, String uloga)
    {
    	String msg = "";
    	//List<String> list = new ArrayList<String>();
    	if(!obj.getIme().equals("sigurna kuca") && !obj.getIme().equals("policija"))
    	{
	    	msg = "Vrednost objekta: " + obj.getCena() +"\n";
	    	if( uloga.equals("Lopov"))    		
	    	{
	        	msg += "Potrebni predmeti:" +"\n";
	        	for (int i=0; i<obj.getPredmeti().size(); i++)
	        	{
	        		msg += obj.getPredmetAt(i).getIme() + "		"+Integer.toString(obj.getPredmetAt(i).getStatus())+"\n";
	        	}
	    	}
    	}
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
			// set title
			alertDialogBuilder.setTitle(obj.getIme());
			// set dialog message
			alertDialogBuilder
				.setMessage(msg)
				.setCancelable(false)
				.setPositiveButton("OK",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, close
						// current activity
						dialog.cancel();
					}
				  });
				
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
			
    }
    

}

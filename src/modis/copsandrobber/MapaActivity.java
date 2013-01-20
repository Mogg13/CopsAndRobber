package modis.copsandrobber;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONObject;

import modis.copsandrobber.R;
import android.R.drawable;
import android.app.AlertDialog;
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
	private Context context;
	private Intent intentMyService;
	private ComponentName service;
	private LocationManager lm;
	private GPSListener myLocationListener;
	private ProximityIntentReceiver proxReciever;
	private Boolean statusIgre;
	//private String entering;
	private Intent pomocniIntent;
	//private boolean inicijalizovano;
	
	//broj metaka koje ima policajac
	private int brojMetaka;
	
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
		//inicijalizovano = false;
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
        	
        	brojMetaka = 3;
        }
        else
        {
        	setContentView(R.layout.map_lopov);
        	
        	View dugmePancir = findViewById(R.id.dugmePancir);
        	dugmePancir.setOnClickListener(this);
        	
        	View dugmeOmetac = findViewById(R.id.dugmeOmetac);
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
		
		
		//IntentFilter filter = new IntentFilter("modis.copsandrobber.proximity_intent");  
	    //registerReceiver(new ProximityIntentReceiver(), filter);
		
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
				////////////////////////////////////////SAMO PRIVREMENO
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
				
				if(brojac10s>=20)	//10s refresh
				{
				brojac10s = 0;
				if(brojac6min >= 2) // 6min refresh
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
				////////////////////////////////////////////  DO OVDE
    			break;    			
    		case R.id.dugmeOmetac:        		
    			break;    			
    		case R.id.dugmePucaj: 
			////////////////////////////////////////SAMO PRIVREMENO
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
			
			if(brojac10s>=20)	//10s refresh
			{
			brojac10s = 0;
			if(brojac6min >= 2) // 6min refresh
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
			////////////////////////////////////////////  DO OVDE
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
					imeIntenta="modis.copsandrobber.proximity_intent_o"+Integer.toString(i);
					Intent intent = new Intent(imeIntenta);
					intent.putExtra("tip", "objekat");
					intent.putExtra("vrednost", igra.getObjekatAt(i).getIme());
					PendingIntent proximityIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
					
					lm.addProximityAlert(Double.parseDouble(igra.getObjekatAt(i).getLatitude()), Double.parseDouble(igra.getObjekatAt(i).getLongitude()), 10, -1, proximityIntent);
					
					IntentFilter filter = new IntentFilter(imeIntenta);  
				    registerReceiver(proxReciever, filter);
				}
				
			}
			for(int i = 0;i<igra.getPredmeti().size();i++)
			{
				imeIntenta="modis.copsandrobber.proximity_intent_p"+Integer.toString(i);
				Intent intent = new Intent(imeIntenta);
				intent.putExtra("tip", "predmet");
				intent.putExtra("vrednost", igra.getPredmetAt(i).getIme());
				PendingIntent proximityIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
				
				lm.addProximityAlert(Double.parseDouble(igra.getPredmetAt(i).getLatitude()), Double.parseDouble(igra.getPredmetAt(i).getLongitude()), 10, -1, proximityIntent);
				
				IntentFilter filter = new IntentFilter(imeIntenta);  
			    registerReceiver(proxReciever, filter);
			}
			/*IntentFilter filter = new IntentFilter("modis.copsandrobber.proximity_intent");  
		    registerReceiver(new ProximityIntentReceiver(), filter);
			*/
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
			}
		});
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
						//TO-DO
						//updateRadar();
				    }
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
						
						//TO-DO
						//updateRadar();
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
				    }
				} catch (Exception e){
					e.printStackTrace();
				}
				/*
			}
		});
		*/
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
						Objekat oTemp = new Objekat(imeObj,latObj,lonObj, predObj, id, 0);
						igra.addObjekat(oTemp);
						predObj = new ArrayList<Predmet>();
						//Log.i("Ubacujem...", imeObj);						
						if(imeObj.equals("sigurna kuca") && igrac.getUloga().equals("Policajac"))
						{
							//do nothing
						}
						else
						{
							mapOverlays.add(new JedanOverlay(vratiKodSlicice(imeObj), latObj, lonObj, oTemp));
						}
					}					
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
			// TODO Auto-generated method stub
			
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
						int i = Integer.parseInt(pomocniIntent.getStringExtra("vrednost"));
						CopsandrobberHTTPHelper.PredmetRobed(igra.getId(), igra.getPredmetAt(i).getId());
					} catch (Exception e){
						e.printStackTrace();
					}

				}
			});
		}
    	
    };
    
    public static void napraviDialogZaObjekat(Objekat obj)
    {
    	
    }

}

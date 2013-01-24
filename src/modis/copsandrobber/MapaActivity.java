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
import android.widget.Toast;

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
	//private String latLopova, lonLopova;
	//private String [] latPolicajca = new String[3];
	//private String [] lonPolicajca = new String[3];
	//private String [] idPolicajca = new String [3];
	private static Context context;
	private Intent intentMyService;
	private ComponentName service;
	private LocationManager lm;
	private GPSListener myLocationListener;
	private ProximityIntentReceiver proxReciever;
	private boolean statusIgre;
	//private String entering;
	private Intent pomocniIntent;
	//private boolean inicijalizovano;
	private int ulov;
	
	private TextView brmetaka;
	private int brojMetaka;
	private View dugmePucaj;
	private View dugmePancir;
	private View dugmeOmetac;
	private boolean aktPancir;
	private boolean aktOmetac;
	private CountDownTimer timer;
	
	
	@Override
	protected boolean isRouteDisplayed() {
		
		return false;
	}
	
	public void onCreate(Bundle savedInstanceState)	{
		
		super.onCreate(savedInstanceState);
	    LocalBroadcastManager.getInstance(this).registerReceiver(
	    		mMessageReceiverGameStart, new IntentFilter("start_the_game"));
	    LocalBroadcastManager.getInstance(this).registerReceiver(
	    		mMessageReceiverGameEnd, new IntentFilter("end_the_game"));
		igra = new Igra();
		proxReciever = new ProximityIntentReceiver();
		context = this;
		guiThread = new Handler();
		try {
			Intent mapIntent = getIntent();
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
        	dugmePucaj.setEnabled(false);
        	
        	brojMetaka = 3;
			brmetaka = (TextView) findViewById(R.id.metkoviText);
        	LocalBroadcastManager.getInstance(this).registerReceiver(
    	    		mMessageReceiverObjectRobbed, new IntentFilter("object_robbed_intent"));
    	    LocalBroadcastManager.getInstance(this).registerReceiver(
    	    		mMessageReceiverOmetacAktiviran, new IntentFilter("ometac_aktiviran"));
    	    LocalBroadcastManager.getInstance(this).registerReceiver(
    	    		mMessageReceiverPancirAktiviran, new IntentFilter("pancir_akticiran"));
        }
        else
        {
        	setContentView(R.layout.map_lopov);
        	
        	dugmePancir = findViewById(R.id.dugmePancir);
        	dugmePancir.setOnClickListener(this);
        	
        	dugmeOmetac = findViewById(R.id.dugmeOmetac);
        	dugmeOmetac.setOnClickListener(this);
        	
        	// da se onemoguce dugmici
        	dugmeOmetac.setEnabled(false);
        	dugmePancir.setEnabled(false);
        }
        ulov = 0;
		
		//Inicijalizacija mape
		initMapView();
		initMyLocation();
		
		//radar
		radar[0] = (ImageView) findViewById(R.id.radarLinija0);
		radar[1] = (ImageView) findViewById(R.id.radarLinija1);
		radar[2] = (ImageView) findViewById(R.id.radarLinija2);
		radar[3] = (ImageView) findViewById(R.id.radarLinija3);
		radar[4] = (ImageView) findViewById(R.id.radarLinija4);
		
		
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
		
		progressDialog = new ProgressDialog(this);
		ucitajPodatke();
		
		statusIgre = false;			
		aktPancir = false;
		aktOmetac = false;
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
    			Log.i("TAG", "Aktiviran pancir");
    			
    			transThread = Executors.newSingleThreadExecutor();
    			transThread.submit(new Runnable() {
    				
    				public void run() {
    					try{
    						CopsandrobberHTTPHelper.AktivirajPancir(igra.getId(), igrac.getRegId());
    					} catch (Exception e){
    						e.printStackTrace();
    					}
    				}
    			});
    			dugmePancir.setEnabled(false);
    			break;    			
    		case R.id.dugmeOmetac: 
    			Log.i("TAG", "Aktiviran ometac");
    			//guiThread = new Handler();
    			transThread = Executors.newSingleThreadExecutor();
    			transThread.submit(new Runnable() {
    				
    				public void run() {
    					try{
    						CopsandrobberHTTPHelper.AktivirajOmetac(igra.getId(), igrac.getRegId());
    					} catch (Exception e){
    						e.printStackTrace();
    					}
    				}
    			});
    			dugmeOmetac.setEnabled(false);
    			break;    			
    		case R.id.dugmePucaj: 
    			int daljina = 100;
    			float[] results = new float[1];	
    			if(!aktPancir)
    			{		
	    			if(igra.getLopov() != null){
	    				Location.distanceBetween(Double.parseDouble(igrac.getLatitude()), Double.parseDouble(igrac.getLongitude()), Double.parseDouble(igra.getLopov().getLatitude()), Double.parseDouble(igra.getLopov().getLongitude()), results);
	    				daljina = (int) results[0];
	    			}
	    			if(daljina <= 30)
	    			{
	    				Log.i("TAG", "Lopov upucan");
	    				UhvacenLopov();
	    			}	
    			}
    			brojMetaka--;
    			if(igra.getObjekatByName("policija") != null){
    				Location.distanceBetween(Double.parseDouble(igrac.getLatitude()), Double.parseDouble(igrac.getLongitude()), Double.parseDouble(igra.getObjekatByName("policija").getLatitude()), Double.parseDouble(igra.getObjekatByName("policija").getLongitude()), results);
					daljina = (int) results[0];
    			}
				if(daljina <= 30)
					brojMetaka = 3;

				brmetaka.setText(Integer.toString(brojMetaka));
    			if(brojMetaka == 0)
    			{
    				dugmePucaj.setEnabled(false);
    			}	 
    			break;
    	}
	}
	
	private void UhvacenLopov() {
		//guiThread = new Handler();
		transThread = Executors.newSingleThreadExecutor();
		transThread.submit(new Runnable() {
			
			public void run() {
				try{
					CopsandrobberHTTPHelper.EndGame(igra.getId(), "uhvacen lopov");
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		});
	}

	protected void onDestroy() { 
		 
        try { 
           //stopService(intentMyService);
        	if(timer != null)
        	{
        		timer.cancel();
        		timer = null;
        	}
        	this.unregisterReceiver(proxReciever);
        } catch (Exception e) { 
            Log.e("Gasenje servisa - error", "> " + e.getMessage()); 
        } 
        super.onDestroy(); 
    } 
	
	private void ucitajProximityPodesavanja()
	{
		if(igrac.getUloga().equals("Policajac"))
		{
			for(int i = 0;i<igra.getObjekti().size();i++)
			{
				if ( igra.getObjekatAt(i).getIme().equals("policija"))
				{
					Intent intent = new Intent("modis.copsandrobber.proximity_intent");
					intent.putExtra("tip", "policija");
					PendingIntent proximityIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
					lm.addProximityAlert(Double.parseDouble(igra.getObjekatAt(i).getLatitude()), Double.parseDouble(igra.getObjekatAt(i).getLongitude()), 30, -1, proximityIntent);
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
					intent.putExtra("vrednost", i);
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
    	
    	guiThread = new Handler();
		transThread = Executors.newSingleThreadExecutor();
		transThread.submit(new Runnable() {
			
			public void run() {
				
				try{
					final String info = CopsandrobberHTTPHelper.getLocationUpdate(igra.getId(), igrac.getRegId(), igrac.getLatitude(), igrac.getLongitude());
					JSONObject jsonObject = new JSONObject(info);
					JSONObject obj;
					String str = info;
					Log.i("JSONNN_"+igrac.getUloga(), str);
				    JSONArray jsonArray = jsonObject.getJSONArray("igraci");
				    for(int i = 0; i<jsonArray.length(); i++){
				    	obj = (JSONObject) jsonArray.get(i);
						final String idIgraca = obj.getString("idIgraca");
						final String latIgraca = obj.getString("latitude");
						final String lonIgraca = obj.getString("longitude");
						//igra.EditIgraci(idIgraca, latIgraca, lonIgraca);
						if(igrac.getUloga().equals("Policajac"))
						{
							if(igra.getIgracById(idIgraca).getUloga().equals("Lopov"))
							{
								igra.editIgrac(idIgraca, latIgraca, lonIgraca);
							}
							else
							{
								guiPromeneDeset(idIgraca, latIgraca, lonIgraca);
								/*igra.editIgracWithOverlay(idIgraca, latIgraca, lonIgraca);
								if(!mapOverlays.contains(igra.getIgracById(idIgraca).getOverlay()) && igra.getIgracById(idIgraca).getLatitude() != null)
						    	{
						    		mapOverlays.add(igra.getIgracById(idIgraca).getOverlay());
						    	}*/
							}
						}
						else
						{
							igra.editIgrac(idIgraca, latIgraca, lonIgraca);
						}
						
						
				    }
				    
				    guiThread.post(new Runnable() {
						
						public void run() {
							updateRadar();
						}
					});
				    
				} catch (Exception e){
					e.printStackTrace();
				}
				
			}
		});
	
	}
    
    private void guiPromeneDeset(final String idIgraca, final String latIgraca, final String lonIgraca)
    {
    	guiThread.post(new Runnable() {
			
			public void run() {
				igra.editIgracWithOverlay(idIgraca, latIgraca, lonIgraca);
				if(!mapOverlays.contains(igra.getIgracById(idIgraca).getOverlay()) && igra.getIgracById(idIgraca).getLatitude() != null)
		    	{
		    		mapOverlays.add(igra.getIgracById(idIgraca).getOverlay());
		    	}
			}
		});
    }
    
    private void ucitajPromeneSestMin() {
    	guiThread = new Handler();
		transThread = Executors.newSingleThreadExecutor();
		transThread.submit(new Runnable() {
			
			public void run() {
				
				try{
					final String info = CopsandrobberHTTPHelper.getLocationUpdate(igra.getId(), igrac.getRegId(), igrac.getLatitude(), igrac.getLongitude());
					JSONObject jsonObject = new JSONObject(info);
					String str = info;
					Log.i("JSONNN_"+igrac.getUloga(), str);
					
				    final JSONArray jsonArray = jsonObject.getJSONArray("igraci");
				    guiPromeneSestMin(jsonArray);
				    /*for(int i = 0; i<jsonArray.length(); i++){
				    	obj = (JSONObject) jsonArray.get(i);
						String idIgraca = obj.getString("idIgraca");
						String latIgraca = obj.getString("latitude");
						String lonIgraca = obj.getString("longitude");
						if(aktOmetac && igrac.getUloga().equals("Policajac"))
						{
							aktOmetac = false;
							if(igra.getIgracById(idIgraca).getUloga().equals("Lopov"))
							{
								igra.editIgrac(idIgraca, latIgraca, lonIgraca);
							}
							else
							{
								igra.editIgracWithOverlay(idIgraca, latIgraca, lonIgraca);
							}
						}
						else
						{
							igra.editIgracWithOverlay(idIgraca, latIgraca, lonIgraca);
						}
				    }
				    
				    for(int i=0;i<igra.getIgraci().size(); i++)
				    {
				    	if(!mapOverlays.contains(igra.getIgracAt(i).getOverlay()))
				    	{
				    		mapOverlays.add(igra.getIgracAt(i).getOverlay());
				    	}
				    }
										
					updateRadar();*/
					
				} catch (Exception e){
					e.printStackTrace();
				}
				
			}
		});
		
	}
    
    private void guiPromeneSestMin(final JSONArray jsonArray)
    {
    	guiThread.post(new Runnable() {
			
			public void run() {
				try
				{
					JSONObject obj;
					for(int i = 0; i<jsonArray.length(); i++){
				    	obj = (JSONObject) jsonArray.get(i);
						String idIgraca = obj.getString("idIgraca");
						String latIgraca = obj.getString("latitude");
						String lonIgraca = obj.getString("longitude");
						if(aktOmetac && igrac.getUloga().equals("Policajac"))
						{
							aktOmetac = false;
							if(igra.getIgracById(idIgraca).getUloga().equals("Lopov"))
							{
								igra.editIgrac(idIgraca, latIgraca, lonIgraca);
							}
							else
							{
								igra.editIgracWithOverlay(idIgraca, latIgraca, lonIgraca);
							}
						}
						else
						{
							igra.editIgracWithOverlay(idIgraca, latIgraca, lonIgraca);
						}
				    }
				    
					//dodavanje overleja
				    for(int i=0;i<igra.getIgraci().size(); i++)
				    {
				    	if(!mapOverlays.contains(igra.getIgracAt(i).getOverlay()))
				    	{
				    		mapOverlays.add(igra.getIgracAt(i).getOverlay());
				    	}
				    }
										
					updateRadar();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		});
    }
    
    
    public void updateRadar()
    {
    	float [] results = new float[1];
    	float distance = 999999999;
    	
    	if(igrac.getUloga().equals("Policajac"))
		{
    		Igrac igracZaRadar;
    		igracZaRadar = igra.getLopov();
    		if(igracZaRadar != null)
    		{
    			Location.distanceBetween(Double.parseDouble(igrac.getLatitude()), Double.parseDouble(igrac.getLongitude()), 
					Double.parseDouble(igracZaRadar.getLatitude()), Double.parseDouble(igracZaRadar.getLongitude()), results);
    			distance = results[0];
    		}
			
		}
		else
		{
			float pom;
			//distance = 999999999;
			for(int i=0;i<igra.getIgraci().size();i++)
			{
				Location.distanceBetween(Double.parseDouble(igrac.getLatitude()), Double.parseDouble(igrac.getLongitude()), 
						Double.parseDouble(igra.getIgracAt(i).getLatitude()), Double.parseDouble(igra.getIgracAt(i).getLongitude()), results);
				pom = results[0];
				if (pom < distance)
				{
					distance = pom;
				}
				
			}					
		}
    	float faktor = 40;
    	if(distance>5*faktor)
    	{
    		radar[0].setImageResource(drawable.button_onoff_indicator_off);
    		radar[1].setImageResource(drawable.button_onoff_indicator_off);
    		radar[2].setImageResource(drawable.button_onoff_indicator_off);
    		radar[3].setImageResource(drawable.button_onoff_indicator_off);
    		radar[4].setImageResource(drawable.button_onoff_indicator_off);
    	}
    	else if(distance>4*faktor)
    	{
    		radar[0].setImageResource(drawable.button_onoff_indicator_on);
    		radar[1].setImageResource(drawable.button_onoff_indicator_off);
    		radar[2].setImageResource(drawable.button_onoff_indicator_off);
    		radar[3].setImageResource(drawable.button_onoff_indicator_off);
    		radar[4].setImageResource(drawable.button_onoff_indicator_off);
    	}
    	else if(distance>3*faktor)
    	{
    		radar[0].setImageResource(drawable.button_onoff_indicator_on);
    		radar[1].setImageResource(drawable.button_onoff_indicator_on);
    		radar[2].setImageResource(drawable.button_onoff_indicator_off);
    		radar[3].setImageResource(drawable.button_onoff_indicator_off);
    		radar[4].setImageResource(drawable.button_onoff_indicator_off);
    	}
    	else if(distance>2*faktor)
    	{
    		radar[0].setImageResource(drawable.button_onoff_indicator_on);
    		radar[1].setImageResource(drawable.button_onoff_indicator_on);
    		radar[2].setImageResource(drawable.button_onoff_indicator_on);
    		radar[3].setImageResource(drawable.button_onoff_indicator_off);
    		radar[4].setImageResource(drawable.button_onoff_indicator_off);
    	}
    	else if(distance > faktor)
    	{
    		radar[0].setImageResource(drawable.button_onoff_indicator_on);
    		radar[1].setImageResource(drawable.button_onoff_indicator_on);
    		radar[2].setImageResource(drawable.button_onoff_indicator_on);
    		radar[3].setImageResource(drawable.button_onoff_indicator_on);
    		radar[4].setImageResource(drawable.button_onoff_indicator_off);
    	}
    	else if(distance <= faktor)
    	{
    		radar[0].setImageResource(drawable.button_onoff_indicator_on);
    		radar[1].setImageResource(drawable.button_onoff_indicator_on);
    		radar[2].setImageResource(drawable.button_onoff_indicator_on);
    		radar[3].setImageResource(drawable.button_onoff_indicator_on);
    		radar[4].setImageResource(drawable.button_onoff_indicator_on);
    	}
    	
    	
    	
    	Log.i("DISTANCE", Float.toString(distance));
    }
    
    public void inicijalizujIgrace()//poziva se kad dodje start signal
    {
    	//guiThread = new Handler();
		transThread = Executors.newSingleThreadExecutor();
		transThread.submit(new Runnable() {
			
			public void run() {
    	
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
					Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
					if(location != null)
					{
						double longitude = location.getLongitude();
						double latitude = location.getLatitude();
					
						igrac.setLatitude(Double.toString(latitude));
						igrac.setLongitude(Double.toString(longitude));
						Objekat o;
						float [] results = new float[1];
						float res;
						if(igrac.getUloga().equals("Policajac"))
						{
							o=igra.getObjekatByName("policija");
						}
						else
						{
							o=igra.getObjekatByName("sigurna kuca");
						}
						
						if(o != null)
						{
							Location.distanceBetween(latitude, longitude, Double.parseDouble(o.getLatitude()), Double.parseDouble(o.getLongitude()), results);
							res = results[0];
							if(res <= 30)
							{
								transThread = Executors.newSingleThreadExecutor();
								transThread.submit(new Runnable() {
									
									public void run() {
										try{
											CopsandrobberHTTPHelper.onPosition(igrac.getRegId(), igrac.getLatitude(), igrac.getLongitude(), igra.getId(), "true");
										} catch (Exception e){
											e.printStackTrace();
										}
									}
								});
							}
							
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
        	
        	timer = new CountDownTimer(7200000, 1000) {

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
   		         
   		         if(brojac10s >= 10)	//10s refresh - 20
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
   		  if(igrac.getUloga().equals("Policajac"))
   			  dugmePucaj.setEnabled(true);
        }
    };
    private BroadcastReceiver mMessageProxReceiverPolicija = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			
			pomocniIntent = arg1;
			if(statusIgre == true)
			{	
				brojMetaka = 3;
				brmetaka.setText("3");
				dugmePucaj.setEnabled(true);
			}
			else
			{
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
						final int i = b.getInt("vrednost");
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
								int j = 0;
								boolean pom = true;
								while( j < igra.getObjekatAt(i).getPredmeti().size() && pom == true)
								{
									if(igra.getObjekatAt(i).getPredmetAt(j).getStatus() != 1)
										pom = false;
									j++;
								}
								if(pom)
								{
									igra.getObjekatAt(i).setStatus(1);
									ulov += Integer.parseInt(igra.getObjekatAt(i).getCena());
									TextView u = (TextView) findViewById(R.id.ulovText);
									u.setText(Integer.toString(ulov)+" din.");
									CopsandrobberHTTPHelper.ObjectRobbed(igra.getId(), igra.getObjekatAt(i).getId(), igrac.getRegId());
									
									guiThread.post(new Runnable() {
										
										public void run() {
											for(int k=0;k<mapOverlays.size();k++)
											{
												if(mapOverlays.get(k) instanceof JedanOverlay)
													if(((JedanOverlay)mapOverlays.get(k)).getIme().equals(igra.getObjekatAt(i).getIme()))
													{
														((JedanOverlay)mapOverlays.get(k)).setBitmap(vratiKodXSlicice(igra.getObjekatAt(i).getIme()));
													}
											}
										}
									});
									
									Intent in = new Intent("modis.copsandrobber.proximity_intent_o"+Integer.toString(i));
									LocationManager locManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
									PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, in, PendingIntent.FLAG_UPDATE_CURRENT);
									locManager.removeProximityAlert(pendingIntent);	
									
									int br = 0;
									boolean pom2 = true;
									while( br < igra.getObjekti().size() && pom2 == true)
									{										
										if(igra.getObjekatAt(br).getStatus() != 1)
											pom2 = false;
										br++;
									}										
									if(pom2)
										CopsandrobberHTTPHelper.EndGame(igra.getId(), "Lopov je pobednik");											
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
			if(statusIgre == true)
			{
				pomocniIntent = arg1; 
				guiThread = new Handler();
				transThread = Executors.newSingleThreadExecutor();
				transThread.submit(new Runnable() {
					
					public void run() {
						try{
							Bundle b = pomocniIntent.getExtras();
							final int i = b.getInt("vrednost");
							CopsandrobberHTTPHelper.PredmetRobbed(igra.getId(), igra.getPredmetAt(i).getId());		
							igra.getPredmetAt(i).setStatus(1);
							guiThread.post(new Runnable() {
								
								public void run() {
									for(int k=0;k<mapOverlays.size();k++)
									{
										if(mapOverlays.get(k) instanceof JedanOverlay)
											if(((JedanOverlay)mapOverlays.get(k)).getIme().equals(igra.getPredmetAt(i).getIme()))
											{
												((JedanOverlay)mapOverlays.get(k)).setBitmap(vratiKodXSlicice(igra.getPredmetAt(i).getIme()));
											}
									}
									if(igra.getPredmetAt(i).getIme().equals("pancir"))
									{															
							        	dugmePancir.setEnabled(true);
									}
									else if(igra.getPredmetAt(i).getIme().equals("ometac"))
									{
										dugmeOmetac.setEnabled(true);
									}
								}
							});
							
							Intent in = new Intent("modis.copsandrobber.proximity_intent_p"+Integer.toString(i));
							LocationManager locManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
						    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, in, PendingIntent.FLAG_UPDATE_CURRENT);
						    locManager.removeProximityAlert(pendingIntent);
							
						} catch (Exception e){
							e.printStackTrace();
						}
					}
				});
			}
		}
    	
    };
 
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
		alertDialogBuilder.setTitle(obj.getIme());
		alertDialogBuilder
			.setMessage(msg)
			.setCancelable(false)
			.setPositiveButton("OK",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
				}
			  });
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
    }
    private BroadcastReceiver mMessageReceiverObjectRobbed = new BroadcastReceiver() {

		public void onReceive(Context arg0, Intent arg1) {
			
			Log.i("OBJEKAT", "ma");
			Bundle b = arg1.getExtras();
			int idObj = b.getInt("idObjekta");
			Objekat o = igra.getObjekatWithId(idObj);
			for(int k=0;k<mapOverlays.size();k++)
			{
				if(mapOverlays.get(k) instanceof JedanOverlay)
					if(((JedanOverlay)mapOverlays.get(k)).getIme().equals(o.getIme()))
					{
						((JedanOverlay)mapOverlays.get(k)).setBitmap(vratiKodXSlicice(o.getIme()));
					}
			}
		}
    	
    };
    private BroadcastReceiver mMessageReceiverPancirAktiviran = new BroadcastReceiver() {

		public void onReceive(Context arg0, Intent arg1) {
			
			aktPancir = true;
			new CountDownTimer(900000, 1000) {

	   		    public void onTick(long millisUntilFinished) {   		    	 
	   		    	//do nothing
	   		    }
	   			public void onFinish() {
	   		    	aktPancir = false;
	   		    }
			}.start();
		}
    };
    private BroadcastReceiver mMessageReceiverOmetacAktiviran = new BroadcastReceiver() {

		public void onReceive(Context arg0, Intent arg1) {			
			aktOmetac = true;
		}    	
    };

    private BroadcastReceiver mMessageReceiverGameEnd = new BroadcastReceiver() {

		public void onReceive(Context arg0, Intent arg1) {			
			// poslati toast
			Bundle b = arg1.getExtras();
			String poruka = b.getString("poruka");
			//Toast.makeText(CopsAndRobberApplication.getContext(), poruka+ " KRAJ IGRE!", Toast.LENGTH_LONG).show();
			napraviDialogZaKrajIgre(poruka);
			// iskljuciti tajmer
			if(timer != null)
			{
				timer.cancel();
				timer = null;
			}
			// prebaciti na novi aktiviti
			// 
		}    	
    };
    public void napraviDialogZaKrajIgre(String poruka)
    {
    	String msg = poruka + "\n\n";
    	msg += "Ukoliko zelite ponovo da igrade idite na svoja pocetna mesta! \n";
    	msg += "Za izlazak iz igre pritisnite exit";
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setTitle("Kraj igre!");
		alertDialogBuilder
			.setMessage(msg)
			.setCancelable(false)
			.setPositiveButton("OK",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
				}
			  })
			.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					MapaActivity.this.finish();
				}
			});
			
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
    }
}

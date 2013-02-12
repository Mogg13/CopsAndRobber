package modis.copsandrobber;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;

import android.R.drawable;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
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
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Menu;
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

	//protected static final TimeUnit SECONDS = null;
	private Igra igra;
	private Igrac igrac;
	private MapView map;
	private MapController controller;
	private ImageView [] radar = new ImageView[5];
	private Handler guiThread;
	private ExecutorService transThread;
	private final ScheduledExecutorService periodicThread = Executors.newSingleThreadScheduledExecutor();
	private ProgressDialog progressDialog;
	private List<Overlay> mapOverlays;
	private Projection projection;
	private TextView timerIgre;
	private int brojac10s;
	private int brojac6min;
	private static Context context;
	private LocationManager lm;
	private GPSListener myLocationListener;
	private ProximityIntentReceiver proxReciever;
	private int ulov;
	private Intent pomocniIntent;	
	private TextView brmetaka;
	private int brojMetaka;
	private View dugmePucaj;
	private View dugmePancir;
	private View dugmeOmetac;
	private boolean aktPancir;
	private boolean aktOmetac;
	private CountDownTimer timer;
	private boolean upucan;

	private String pomString;
	private final Runnable tenSecTask= new Runnable() {

        public void run() 
        { 
        	Log.i("SCHEDULE",Integer.toString(brojac6min));
        	if(brojac6min != 36)
        	{
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
						if(igrac.getUloga().equals("Cop"))
						{
							if(igra.getIgracById(idIgraca).getUloga().equals("Robber"))
							{
								igra.editIgrac(idIgraca, latIgraca, lonIgraca);
							}
							else
							{
								guiPromeneDeset(idIgraca, latIgraca, lonIgraca);								
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
        	else
        	{
        		brojac6min = 0;
        		try{
					final String info = CopsandrobberHTTPHelper.getLocationUpdate(igra.getId(), igrac.getRegId(), igrac.getLatitude(), igrac.getLongitude());
					JSONObject jsonObject = new JSONObject(info);
					String str = info;
					Log.i("JSONNN_"+igrac.getUloga(), str);
					
				    final JSONArray jsonArray = jsonObject.getJSONArray("igraci");
				    guiPromeneSestMin(jsonArray);
				    
					
				} catch (Exception e){
					e.printStackTrace();
				}
        	}
        	brojac6min++;
        }
    };
	private ScheduledFuture<?> tenSecTaskHandle;
	
	protected boolean isRouteDisplayed() {		
		return false;
	}
	
	public void onCreate(Bundle savedInstanceState)	{
		
		Log.i("LIFECYCLE","MAPAActivity - onCreate");
		super.onCreate(savedInstanceState);
	    LocalBroadcastManager.getInstance(this).registerReceiver(
	    		mMessageReceiverGameStart, new IntentFilter("start_the_game"));
	    LocalBroadcastManager.getInstance(this).registerReceiver(
	    		mMessageReceiverGameEnd, new IntentFilter("end_the_game"));
        LocalBroadcastManager.getInstance(this).registerReceiver(
	    		mMessageReceiverObjectRobbed, new IntentFilter("object_robbed_intent"));
        LocalBroadcastManager.getInstance(this).registerReceiver(
	    		mMessageReceiver, new IntentFilter("googleservice_registration"));
        
		igra = new Igra();
		proxReciever = new ProximityIntentReceiver();
		context = this;
		guiThread = new Handler();
		transThread = Executors.newSingleThreadExecutor();
		
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
		
        if(igrac.getUloga().equals("Cop"))
        {
        	setContentView(R.layout.map_policajac);
        	
        	dugmePucaj = findViewById(R.id.dugmePucaj);
        	dugmePucaj.setOnClickListener(this);
        	dugmePucaj.setEnabled(false);
        	
        	brojMetaka = 3;
			brmetaka = (TextView) findViewById(R.id.metkoviText);
        	
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
        	
        	dugmeOmetac.setEnabled(false);
        	dugmePancir.setEnabled(false);
        	ulov = 0;
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
		
		timer = new CountDownTimer(7200000, 1000) {
		     public void onTick(long millisUntilFinished) {   		    	 
				 
		    	 //Log.i("TICK", "napravio tik " + Integer.toString(brojac10s));

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
		         
		     }
			public void onFinish() {
		    	 //napraviDialogZaKrajIgre("Vreme je isteklo!");
				if(igrac.getUloga().equals("Robber"))
					zavrsiIgru("Time is up!");
				else
					Toast.makeText(CopsAndRobberApplication.getContext(), "Please wait, the game will finish any moment!", Toast.LENGTH_SHORT).show();
		     }
		  };
					
		aktPancir = false;
		aktOmetac = false;
		upucan = false;
		
	}
	
	private class GPSListener implements LocationListener{
		
		public GPSListener() {
			// TODO Auto-generated constructor stub
		}
		
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			 igrac.setLongitude(Double.toString(location.getLongitude()));
		     igrac.setLatitude(Double.toString(location.getLatitude()));		     
		     Log.i("LOKACIJA", "primljen gps" + igrac.getLatitude() + " " + igrac.getLongitude());
		}
		
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub			
		}
		
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub			
		}
		
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub			
		}
	};
	
	public void onClick(View v) {
    	
    	switch(v.getId())
    	{
    		case R.id.dugmePancir: 
    			Log.i("TAG", "Aktiviran pancir");    			
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
	    			if(daljina <= 30 && !upucan)
	    			{
	    				Log.i("TAG", "Lopov upucan");
	    				upucan = true;
	    				zavrsiIgru("Robber caught!");
	    			}	
    			}
    			brojMetaka--;
    			if(igra.getObjekatByName("Police") != null){
    				Location.distanceBetween(Double.parseDouble(igrac.getLatitude()), Double.parseDouble(igrac.getLongitude()), Double.parseDouble(igra.getObjekatByName("Police").getLatitude()), Double.parseDouble(igra.getObjekatByName("Police").getLongitude()), results);
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
	
	private void zavrsiIgru(String poruka) {

		pomString = poruka;
		transThread.submit(new Runnable() {
			
			public void run() {
				try{
					CopsandrobberHTTPHelper.EndGame(igra.getId(), pomString);
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		});
	}

	protected void onDestroy() { 
		 
		Log.i("LIFECYCLE","MAPAActivity - onDestroy");
		
		if(igra.getStatus() == 1 && igrac.getUloga().equals("Robber"))
		{
			deregistracijaSaBaze("yes");
			Log.i("ODUSTAJANJE", "DA");
		}
		else
			deregistracijaSaBaze("no");
		
		if(timer != null)
    	{
    		Log.i("TIMER","Gasim tajmer.");
    		timer.cancel();
    		timer = null;    		
    	}

    	//transThread.shutdownNow();
    	if(tenSecTaskHandle != null)
    	{
        	tenSecTaskHandle.cancel(true);
        	Log.i("CANCEL", Boolean.toString(tenSecTaskHandle.isCancelled()) + " iz onDestroy");
        	tenSecTaskHandle = null;
    	}
    	//periodicThread.shutdownNow();
    	
    	UnregisterAllProxAlerts();
    	lm.removeUpdates(myLocationListener);
    	
        try {         	
        	
        	LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiverGameStart);
        	LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiverGameEnd);        
        	LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        	LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiverObjectRobbed);

        	if(igrac.getUloga().equals("Cop"))
        	{
        		LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiverPancirAktiviran);
        		LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiverOmetacAktiviran);        		
        		LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageProxReceiverPolicija);
        	}
        	else
        	{
        		LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageProxReceiverObjekat);
        		LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageProxReceiverPredmet);
        	}
        	
        } catch (Exception e) { 

        	e.printStackTrace();
        } 
        super.onDestroy(); 
    } 
	
	private void deregistracijaSaBaze(String s) {

		pomString = s;
		transThread.submit(new Runnable() {
			
			public void run() {

				try{
					CopsandrobberHTTPHelper.unregiseterFromDatabase(igra.getId(), igrac.getRegId(), pomString);
				} catch (Exception e){
					e.printStackTrace();
				}			
			}
		});
	}

	private void ucitajProximityPodesavanja()
	{
		if(igrac.getUloga().equals("Cop"))
		{
			Objekat o = igra.getObjekatByName("Police");
			
			Intent intent = new Intent("modis.copsandrobber.proximity_intent");
			intent.putExtra("tip", "policija");
			PendingIntent proximityIntent = PendingIntent.getBroadcast(CopsAndRobberApplication.getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			
			lm.addProximityAlert(Double.parseDouble(o.getLatitude()), Double.parseDouble(o.getLongitude()), 10, -1, proximityIntent);
			
		    LocalBroadcastManager.getInstance(this).registerReceiver(
		    		mMessageProxReceiverPolicija, new IntentFilter("u_policiji"));
		    
			//IntentFilter filter = new IntentFilter("policija_intent");  
		    //registerReceiver(proxReciever, filter);

		}
		else
		{
			String imeIntenta;
			for(int i = 0;i<igra.getObjekti().size();i++)
			{
				if ( !igra.getObjekatAt(i).getIme().equals("Police"))
				{
					imeIntenta="modis.copsandrobber.proximity_intent_o"+Integer.toString(i);
					Intent intent = new Intent(imeIntenta);
					intent.putExtra("tip", "objekat");
					intent.putExtra("vrednost", i);
					PendingIntent proximityIntent = PendingIntent.getBroadcast(CopsAndRobberApplication.getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
					
					lm.addProximityAlert(Double.parseDouble(igra.getObjekatAt(i).getLatitude()), Double.parseDouble(igra.getObjekatAt(i).getLongitude()), 10, -1, proximityIntent);
					
					//IntentFilter filter = new IntentFilter("objekat_intent");  
				    //registerReceiver(proxReciever, filter);

				}
				
			}
			for(int i = 0;i<igra.getPredmeti().size();i++)
			{
				imeIntenta="modis.copsandrobber.proximity_intent_p"+Integer.toString(i);
				Intent intent = new Intent(imeIntenta);
				intent.putExtra("tip", "predmet");
				intent.putExtra("vrednost", i);
				PendingIntent proximityIntent = PendingIntent.getBroadcast(CopsAndRobberApplication.getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
				
				lm.addProximityAlert(Double.parseDouble(igra.getPredmetAt(i).getLatitude()), Double.parseDouble(igra.getPredmetAt(i).getLongitude()), 10, -1, proximityIntent);
				
				//IntentFilter filter = new IntentFilter("predmet_intent");  
			   // registerReceiver(proxReciever, filter);

			}
			
		    LocalBroadcastManager.getInstance(this).registerReceiver(
		    		mMessageProxReceiverObjekat, new IntentFilter("u_objektu"));
		    LocalBroadcastManager.getInstance(this).registerReceiver(
		    		mMessageProxReceiverPredmet, new IntentFilter("u_predmetu"));
		}
	}
	
	private void ucitajPodatke() {

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
						if(aktOmetac)
						{							
							if(igra.getIgracById(idIgraca).getUloga().equals("Robber"))
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
					aktOmetac = false;
				    
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
    	
    	if(igrac.getUloga().equals("Cop"))
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
    
    public void inicijalizujIgrace()
    {
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
					progressDialog.setMessage("Loading map...");
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
				    int cenaObj;

				    JSONObject obj;
				    JSONArray jsonArray = jsonObject.getJSONArray("predmeti");
					for(int i = 0; i<jsonArray.length(); i++){
						obj = (JSONObject) jsonArray.get(i);
						imeObj = obj.getString("naziv");
						latObj = obj.getString("latitude");
						lonObj = obj.getString("longitude");
						id = obj.getInt("id");
						igra.addPredmet(new Predmet(imeObj,latObj,lonObj, id, 0));

						if(igrac.getUloga().equals("Robber"))
						{
							mapOverlays.add(new JedanOverlay(vratiKodSlicice(imeObj), latObj, lonObj, imeObj));
						}
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
						cenaObj = obj.getInt("cena");
						id = obj.getInt("id");
						jArrayUslov = obj.getJSONArray("uslovi");
						for(int j = 0; j<jArrayUslov.length(); j++)
						{
							objUslov = (JSONObject) jArrayUslov.get(j);
							uslovId = objUslov.getInt("idpUslova");
							predObj.add(igra.getPredmetWithId(uslovId));
						}
						Objekat oTemp = new Objekat(imeObj,latObj,lonObj, predObj, id, 0, cenaObj);
						igra.addObjekat(oTemp);
						predObj = new ArrayList<Predmet>();					
						if(imeObj.equals("Safe House") && igrac.getUloga().equals("Cop"))
						{
							//do nothing
						}
						else
						{
							mapOverlays.add(new JedanOverlay(vratiKodSlicice(imeObj), latObj, lonObj, oTemp, igrac.getUloga(), imeObj));
						}

					}
					
					proveriPozicijuIgraca();
					
					ucitajProximityPodesavanja();

										
				} catch (Exception e){
					e.printStackTrace();					
				}
			}
		});
	}
	
	public void proveriPozicijuIgraca()
	{
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
			if(igrac.getUloga().equals("Cop"))
			{
				o=igra.getObjekatByName("Police");
			}
			else
			{
				o=igra.getObjekatByName("Safe House");
			}
			
			if(o != null)
			{
				Location.distanceBetween(latitude, longitude, Double.parseDouble(o.getLatitude()), Double.parseDouble(o.getLongitude()), results);
				res = results[0];
				if(res <= 30)
				{
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
	}
	
	public int vratiKodSlicice(String ime)
	{
		int kod = 0;
		
		if(ime.equals("Bank One"))
			kod = R.drawable.bank;
		if(ime.equals("Bank Two"))
			kod = R.drawable.bank;	
		if(ime.equals("Exchange Office One"))
			kod = R.drawable.bankeuro;
		if(ime.equals("Exchange Office Two"))
			kod = R.drawable.bankeuro;
		if(ime.equals("Jewelry Store"))
			kod = R.drawable.hotel1star;
		if(ime.equals("Safe House"))
			kod = R.drawable.cabin;
		if(ime.equals("Police"))
			kod = R.drawable.police;
		
		if(ime.equals("Bow and arrow"))
			kod = R.drawable.archery;
		if(ime.equals("Key"))
			kod = R.drawable.carrental;
		if(ime.equals("Tools"))
			kod = R.drawable.carrepair;
		if(ime.equals("Magnifier"))
			kod = R.drawable.cluster;
		if(ime.equals("Ocular"))
			kod = R.drawable.cluster3;
		if(ime.equals("Computer"))
			kod = R.drawable.computer;
		if(ime.equals("Sedatives"))
			kod = R.drawable.drugs;
		if(ime.equals("Fire extinguisher"))
			kod = R.drawable.fire_extinguisher;
		if(ime.equals("Gun"))
			kod = R.drawable.gun;
		if(ime.equals("Rifle"))
			kod = R.drawable.hunting;
		if(ime.equals("Night vision"))
			kod = R.drawable.ophthalmologist;
		if(ime.equals("Hammer"))
			kod = R.drawable.mine;
		if(ime.equals("Cell phone"))
			kod = R.drawable.phones;
		if(ime.equals("Camera"))
			kod = R.drawable.photo;
		if(ime.equals("Document"))
			kod = R.drawable.postal;
		
		if(ime.equals("Armor"))
			kod = R.drawable.clothes_male;
		if(ime.equals("Jammer"))
			kod = R.drawable.mobilephonetower;		
		return kod;
	}
	
	public int vratiKodXSlicice(String ime)
	{
		int kod = 0;
		
		if(ime.equals("Bank One"))
			kod = R.drawable.xbank;
		if(ime.equals("Bank Two"))
			kod = R.drawable.xbank;	
		if(ime.equals("Exchange Office One"))
			kod = R.drawable.xbankeuro;
		if(ime.equals("Exchange Office Two"))
			kod = R.drawable.xbankeuro;
		if(ime.equals("Jewelry Store"))
			kod = R.drawable.xhotel1star;
		
		
		if(ime.equals("Bow and arrow"))
			kod = R.drawable.xarchery;
		if(ime.equals("Key"))
			kod = R.drawable.xcarrental;
		if(ime.equals("Tools"))
			kod = R.drawable.xcarrepair;
		if(ime.equals("Magnifier"))
			kod = R.drawable.xcluster;
		if(ime.equals("Ocular"))
			kod = R.drawable.xcluster3;
		if(ime.equals("Computer"))
			kod = R.drawable.xcomputer;
		if(ime.equals("Sedatives"))
			kod = R.drawable.xdrugs;
		if(ime.equals("Fire extinguisher"))
			kod = R.drawable.xfire_extinguisher;
		if(ime.equals("Gun"))
			kod = R.drawable.xgun;
		if(ime.equals("Rifle"))
			kod = R.drawable.xhunting;
		if(ime.equals("Night vision"))
			kod = R.drawable.xophthalmologist;
		if(ime.equals("Hammer"))
			kod = R.drawable.xmine;
		if(ime.equals("Cell phone"))
			kod = R.drawable.xphones;
		if(ime.equals("Camera"))
			kod = R.drawable.xphoto;
		if(ime.equals("Document"))
			kod = R.drawable.xpostal;
		
		if(ime.equals("Armor"))
			kod = R.drawable.xclothes_male;
		if(ime.equals("Jammer"))
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

        	if(igra.getStatus() == 0)
        	{
	        	inicijalizujIgrace();
	        		        	
	        	tenSecTaskHandle = periodicThread.scheduleAtFixedRate(tenSecTask, 10, 10, TimeUnit.SECONDS);
	        		        	
	        	timer.start();
	   		  
	   		  	igra.setStatus(1);
	   		  	
	   		  	if(igrac.getUloga().equals("Cop"))
	   		  		dugmePucaj.setEnabled(true);
	        }

        }
    };
    private BroadcastReceiver mMessageProxReceiverPolicija = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			
			pomocniIntent = arg1;
			if(igra.getStatus() == 1)
			{	
				brojMetaka = 3;
				brmetaka.setText("3");
				dugmePucaj.setEnabled(true);
			}
			else
			{
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
			transThread.submit(new Runnable() {
				
				public void run() {
					try{
						Bundle b = pomocniIntent.getExtras();
						final int i = b.getInt("vrednost");
						String entering = b.getString("entering");
						
						if(igra.getStatus() == 0)
						{
							Log.i("PROXI", igra.getObjekatAt(i).getIme());
							if(igra.getObjekatAt(i).getIme().equals("Safe House"))
							{
								Log.i("PROXI", entering);
								CopsandrobberHTTPHelper.onPosition(igrac.getRegId(), igrac.getLatitude(), igrac.getLongitude(), igra.getId(), entering);
							}
						}
						else if(!igra.getObjekatAt(i).getIme().equals("Safe House"))
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

			if(igra.getStatus() == 1)
			{
				pomocniIntent = arg1; 
				transThread.submit(new Runnable() {
					
					public void run() {
						try{
							Bundle b = pomocniIntent.getExtras();
							final int i = b.getInt("vrednost");
							if(igra.getPredmetAt(i).getStatus() == 0)
							{
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
										if(igra.getPredmetAt(i).getIme().equals("Armor"))
										{															
								        	dugmePancir.setEnabled(true);
										}
										else if(igra.getPredmetAt(i).getIme().equals("Jammer"))
										{
											dugmeOmetac.setEnabled(true);
										}
									}
								});
							}
							
						} catch (Exception e){
							e.printStackTrace();
						}
					}
				});
			}
		}
    	
    };   
    private BroadcastReceiver mMessageReceiverObjectRobbed = new BroadcastReceiver() {

		public void onReceive(Context arg0, Intent arg1) {
			
			Log.i("OBJEKAT", "ma");
			Bundle b = arg1.getExtras();
			int idObj = b.getInt("idObjekta");
			Objekat o = igra.getObjekatWithId(idObj);
			o.setStatus(1);
			for(int k=0;k<mapOverlays.size();k++)
			{
				if(mapOverlays.get(k) instanceof JedanOverlay)
					if(((JedanOverlay)mapOverlays.get(k)).getIme().equals(o.getIme()))
					{
						((JedanOverlay)mapOverlays.get(k)).setBitmap(vratiKodXSlicice(o.getIme()));
					}
			}
			
			if(igrac.getUloga().equals("Robber"))
			{
				ulov += o.getCena();
				TextView u = (TextView) findViewById(R.id.ulovText);
				u.setText(Integer.toString(ulov)+" $");
			}
			napraviDialogZaOpljackanObjekat(o.getIme());
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

			Bundle b = arg1.getExtras();
			String poruka = b.getString("poruka");
			String odustajanje = b.getString("odustajanje");

			if(tenSecTaskHandle != null)
	    	{
	        	tenSecTaskHandle.cancel(true);
	        	Log.i("CANCEL", Boolean.toString(tenSecTaskHandle.isCancelled()) + " iz GameEnd");
	        	tenSecTaskHandle = null;
	    	}
			if(timer != null)
			{
				timer.cancel();
				Log.i("TIMER", "timer je stao");
			}
	    	igra.setStatus(0);
			if(odustajanje.equals("no"))
				napraviDialogZaKrajIgre(poruka);
			else
				napraviDijalogZaOdustajanje(poruka);

		}

    	
    };
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
          
        	pomocniIntent = intent;
			transThread.submit(new Runnable() {
				
				public void run() {
					try{
						Bundle igraBundle = pomocniIntent.getExtras();
						if(igraBundle != null)
						{
							String stariReg = igrac.getRegId();
							igrac.setRegId(igraBundle.getString("googleservice_num"));
							CopsandrobberHTTPHelper.UpdateRegistrationId(igra.getId(), igrac.getRegId(), stariReg);
						}
					} catch (Exception e){
						e.printStackTrace();
					}
				}
			});
			
			
        }
    };
    public static void napraviDialogZaObjekat(Objekat obj, String uloga)
    {
    	String msg = "";
    	if(!obj.getIme().equals("Safe House") && !obj.getIme().equals("Police"))
    	{
	    	msg = "Object value: " + obj.getCena() +"\n";
	    	if( uloga.equals("Robber"))    		
	    	{
	        	msg += "Necessary tools:" +"\n";
	        	for (int i=0; i<obj.getPredmeti().size(); i++)
	        	{
	        		if(obj.getPredmetAt(i).getStatus() == 0)
	        			msg += obj.getPredmetAt(i).getIme() + "		Not taken"+"\n";
	        		else
	        			msg += obj.getPredmetAt(i).getIme() + "		Taken"+"\n";
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
    public void napraviDialogZaKrajIgre(String poruka)
    {
    	String msg = poruka + "\n\n";
    	msg += "If you want to play again, go to the starting position! \n";
    	msg += "Otherwise click exit.";
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setTitle("Game end!");
		alertDialogBuilder
			.setMessage(msg)
			.setCancelable(false)
			.setPositiveButton("OK",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					RestartGame();
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
    
	private void napraviDijalogZaOdustajanje(String poruka) {

		String msg = poruka + "\n\n";
    	msg += "Exit the game!";
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setTitle("Game end!");
		alertDialogBuilder
			.setMessage(msg)
			.setCancelable(false)
			.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					MapaActivity.this.finish();
				}
			});
			
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
	}

    public void napraviDialogZaOpljackanObjekat(String imeObjekta)

    {
    	String msg = "";
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
    	if(igrac.getUloga().equals("Cop"))
    	{
    		msg += "Object " + imeObjekta;
        	msg += " is robbed!";        	
    		alertDialogBuilder.setTitle("There was a robbery!");
    	}
    	else
    	{
    		msg += "You earned: ";
        	msg += igra.getObjekatByName(imeObjekta).getCena();        	
    		alertDialogBuilder.setTitle( imeObjekta +" is successfully robbed!");
    	}
    	
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
    
    public void napraviDialogZaExit()
    {
    	String msg = "Are you sure that you want to exit the game?";

    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setTitle("Exit the game?");
		alertDialogBuilder
			.setMessage(msg)
			.setCancelable(false)
			.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {	
					MapaActivity.this.finish();
				}
			  })
			.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
				}
			});
			
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
    }
    
    public void napraviDialogZaInfo()
    {
    	String msg;
    	if(igrac.getUloga().equals("Cop"))
    	{
    		msg = "In order for the game to start, everybody needs to go to their starting position. " +
    				"Your starting position is Police station. Robber's goal in the game is to obtain 10 000 $ by robbing different object. " +
    				"You need to stop him by shooting him! But in order to do that you need to be at a distance of 30m or less from the robber. " +
    				"You can reload by going back to Police station." +
    				"Robber's position will be updated every 6 minutes.";
    	}
    	else
    	{
    		msg = "In order for the game to start, everybody needs to go to their starting position. " +
    				"Your starting position is Safe House. Your goal in the game is to rob different objects in total value of 10 000 $ or more. " +
    				"But be careful! Cops will try to stop you by shooting you! They can shoot you only if they are at distance of 30m or less. " +
    				"You can protect yourself by obtaining and activating armor. When armor is activated, cops can not shoot you. Armor expires after 15 minutes. " +
    				"Cops' positions will be updated every 6 minutes. If you activate jammer cops won't get your position in the next update.";
    	}
    	
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setTitle("Instructions");
		alertDialogBuilder
			.setMessage(msg)
			.setCancelable(false)
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
				}
			});
			
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
    }
    
    public void RestartGame()
    {

    	timerIgre.setText("0:00:00");   

    	
    	if(igrac.getUloga().equals("Cop"))
    	{
    		dugmePucaj.setEnabled(false);
    		brojMetaka = 3;
    		brmetaka.setText("3");			
    	}
    	else
    	{
    		dugmePancir.setEnabled(false);
    		dugmeOmetac.setEnabled(false);
    		ulov = 0;    		
    		TextView u = (TextView) findViewById(R.id.ulovText);
			u.setText(Integer.toString(ulov)+" $");
    		
    		for(int i=0;i<igra.getPredmeti().size();i++)
    		{
    			if(igra.getPredmetAt(i).getStatus() == 1)
    			{
    				igra.getPredmetAt(i).setStatus(0);
    				String ime = igra.getPredmetAt(i).getIme();
    				for(int j=0;j<mapOverlays.size();j++)    				
    					if(mapOverlays.get(j) instanceof JedanOverlay)
    						if(((JedanOverlay)mapOverlays.get(j)).getIme().equals(ime))    						
    							((JedanOverlay)mapOverlays.get(j)).setBitmap(vratiKodSlicice(ime));   						
    			}
    		}
    	}
    	aktPancir = false;
		aktOmetac = false;
		upucan = false;
		
		brojac10s = 1;
		brojac6min = 1;
		
		for(int i=0;i<igra.getObjekti().size();i++)
		{
			if(igra.getObjekatAt(i).getStatus() == 1)
			{
				igra.getObjekatAt(i).setStatus(0);
				String ime = igra.getObjekatAt(i).getIme();
				for(int j=0;j<mapOverlays.size();j++)				
					if(mapOverlays.get(j) instanceof JedanOverlay)
						if(((JedanOverlay)mapOverlays.get(j)).getIme().equals(ime))						
							((JedanOverlay)mapOverlays.get(j)).setBitmap(vratiKodSlicice(ime));
			}
		}

		for(int i=0;i<igra.getIgraci().size(); i++)	    
	    	if(mapOverlays.contains(igra.getIgracAt(i).getOverlay()))	    	
	    		mapOverlays.remove(igra.getIgracAt(i).getOverlay());
		
		radar[0].setImageResource(drawable.button_onoff_indicator_off);
		radar[1].setImageResource(drawable.button_onoff_indicator_off);
		radar[2].setImageResource(drawable.button_onoff_indicator_off);
		radar[3].setImageResource(drawable.button_onoff_indicator_off);
		radar[4].setImageResource(drawable.button_onoff_indicator_off);
		
		igra.setIgraci(new ArrayList<Igrac>());		
		proveriPozicijuIgraca();				
    }
    public void UnregisterAllProxAlerts()
    {
    	if(igrac.getUloga().equals("Cop"))
    	{
			Intent in = new Intent("modis.copsandrobber.proximity_intent");
			PendingIntent proximityIntent = PendingIntent.getBroadcast(CopsAndRobberApplication.getContext(), 0, in, PendingIntent.FLAG_UPDATE_CURRENT);
			lm.removeProximityAlert(proximityIntent);
    	}
    	else
    	{
    		String imeIntenta;
			for(int i = 0;i<igra.getObjekti().size();i++)
			{
				if(!igra.getObjekatAt(i).getIme().equals("Police"))
				{
					imeIntenta="modis.copsandrobber.proximity_intent_o"+Integer.toString(i);
					Intent intent = new Intent(imeIntenta);
					PendingIntent proximityIntent = PendingIntent.getBroadcast(CopsAndRobberApplication.getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);					
					lm.removeProximityAlert(proximityIntent);
				}
				
			}
			for(int i = 0;i<igra.getPredmeti().size();i++)
			{
				imeIntenta="modis.copsandrobber.proximity_intent_p"+Integer.toString(i);
				Intent intent = new Intent(imeIntenta);
				PendingIntent proximityIntent = PendingIntent.getBroadcast(CopsAndRobberApplication.getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);				
				lm.removeProximityAlert(proximityIntent);

			}
    	}
    	
    }

    /*protected void onRestart()
    {
    	super.onRestart();
    	
    }
*/
  /*  protected void onPause()
    {
    	super.onPause();
    	Log.i("LIFECYCLE","MAPAActivity - onPause");
    	try{    		
    		this.unregisterReceiver(proxReciever);    	
	    } catch (Exception e) { 
	        Log.e("Gasenje servisa - error", "> " + e.getMessage()); 
	    } 
    	
    }*/
    protected void onResume()
    {
    	Log.i("LIFECYCLE","MAPAActivity - onResume");
    	super.onResume();
    	
    	if(igrac.getUloga().equals("Cop"))
		{
			IntentFilter filter = new IntentFilter("modis.copsandrobber.proximity_intent");  
		    registerReceiver(proxReciever, filter);
		}
		else
		{
			String imeIntenta;
			for(int i = 0;i<7;i++)
			{
				//if ( !igra.getObjekatAt(i).getIme().equals("Police"))
				{
					imeIntenta="modis.copsandrobber.proximity_intent_o"+Integer.toString(i);
										
					IntentFilter filter = new IntentFilter(imeIntenta);  
				    registerReceiver(proxReciever, filter);
				}
				
			}
			for(int i = 0;i<17;i++)
			{
				imeIntenta="modis.copsandrobber.proximity_intent_p"+Integer.toString(i);
								
				IntentFilter filter = new IntentFilter(imeIntenta); 
				filter = new IntentFilter(imeIntenta);
			    registerReceiver(proxReciever, filter);
			}
			
		}
    }
    
    public void onBackPressed() {
    	
    	napraviDialogZaExit();
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}
    
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.map_exit:
        	napraviDialogZaExit();
          break;          
        case R.id.map_info:
        	napraviDialogZaInfo();
          break;
        }
        return true;
      }
    /*

    protected void onStart()
    {
    	Log.i("LIFECYCLE","MAPAActivity - onStart");
    	super.onStart();
    }
    protected void onStop()
    {
    	Log.i("LIFECYCLE","MAPAActivity - onStop");
    	super.onStop();
    }

    */

}

package modis.copsandrobber;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class CopsAndRobberGPSService extends Service{

	Thread triggerService;
	LocationManager lm;
	GPSListener myLocationListener;
	Context context;
	double latitude;
	double longitude;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public void onCreate() { 
		super.onCreate();
		context = this;
	}
	
	public void onStart (Intent intent, int startId){
		super.onStart(intent, startId);
		Log.e("<<MyGpsService-onStart>>", "I am alive-GPS!");
	
	
		triggerService = new Thread (new Runnable() {
			
			public void run(){
				
				try{
					Looper.prepare();
					lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE); 
					myLocationListener = new GPSListener(context);
					long minTime=5000;
					float minDistance = 1;
					lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, myLocationListener);
					Looper.loop();
				}catch(Exception e){
					Log.e("MYGPS", e.getMessage() );
				}
			}
		});
		triggerService.start();
		
		LocalBroadcastManager.getInstance(this).registerReceiver(
	    		mMessageReceiver, new IntentFilter("gpsLokacija_filter_poslati"));
	}
	
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
        	sendGpsLokacijaIntent();
        	Log.i("LOKACIJA 2", "primljen gps" + latitude + " " + longitude);
        }
    };
    
	public void sendGpsLokacijaIntent()
	{
		Intent myFilteredResponse= new Intent("gpsLokacija_filter");
	    myFilteredResponse.putExtra("latitude", Double.toString(latitude));
	    myFilteredResponse.putExtra("longitude", Double.toString(longitude));
	    LocalBroadcastManager.getInstance(context).sendBroadcast(myFilteredResponse);
	}
	
	private class GPSListener implements LocationListener{
		
		Context context;
		
		public GPSListener(Context c) {
			// TODO Auto-generated constructor stub
			context =c;
		}

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			 longitude = location.getLongitude();
		     latitude = location.getLatitude();
		     
		     sendGpsLokacijaIntent();
			Log.i("LOKACIJA", "primljen gps" + latitude + " " + longitude);

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
}

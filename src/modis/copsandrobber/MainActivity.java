package modis.copsandrobber;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.android.gcm.GCMRegistrar;

import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Toast;

import static modis.copsandrobber.CommonUtilities.SENDER_ID;

public class MainActivity extends Activity implements OnClickListener {
	
	private String reg_number;
	private ExecutorService transThread;
	private ProgressDialog progressDialog;
	private Handler guiThread;
	Context context;
	Intent intentMyService;
	LocationManager locationManager;
	//ComponentName service;
	//BroadcastReceiver receiver;
	
	protected void onCreate(Bundle savedInstanceState) {
		
		Log.i("LIFECYCLE","MainActivity - onCreate");
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
		setContentView(R.layout.activity_main);
	    LocalBroadcastManager.getInstance(this).registerReceiver(
	    		mMessageReceiver, new IntentFilter("googleservice_registration"));
	
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		final String regId = GCMRegistrar.getRegistrationId(this); 
		if (regId.equals("")) { 
            GCMRegistrar.register(this, SENDER_ID); 
		}
		else 
		{
			Log.i("InfoLog", "vec je logovano" + regId);  
		}
		
		reg_number = regId;
		this.context = this;
		
		View novaIgraDugme = findViewById(R.id.dugme_nova_igra);
        novaIgraDugme.setOnClickListener(this);
        
        View postojeceIgreDugme = findViewById(R.id.dugme_postojece_igre);
        postojeceIgreDugme.setOnClickListener(this);  
        
        View exitDugme = findViewById(R.id.dugme_exit);
        exitDugme.setOnClickListener(this);
        
        progressDialog = new ProgressDialog(this);
        
        guiThread = new Handler();
		transThread = Executors.newSingleThreadExecutor();
		
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		//locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

	}

	public void onClick(View v) {	
		
    	
    	switch(v.getId())
    	{
    	case R.id.dugme_nova_igra:
    		
    		if(!isConnectingToInternet())
    		{
    			showAlertDialog("No internet access", "You can not play this game without internet. Establish internet connection and try again.");
    		}
    		else if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
    		{
    			Log.i("GPS","Nije ukljucen");
    			showGPSDisabledAlertToUser();
    		}
    		else
    		{
    			if(reg_number.equals(""))
	    		{
	    			transThread.submit(new Runnable(){
	    				public void run(){
	    					guiProgressDialog(true);
	
	    					while(reg_number.equals(""))
	    					{ }
	    					guiNotifyUser("nova");
	    					guiProgressDialog(false);
	    				}    				
	    			});    			
	    		}    		    		
	    		else
	    		{
	    			startNovaActivity();
	    		}
    		}

    		break;
    	case R.id.dugme_postojece_igre:    		
    		
    		if(!isConnectingToInternet())
    		{
    			showAlertDialog("No internet access", "You can not play this game without internet. Establish internet connection and try again.");
    		}
    		else if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
    		{
    			showGPSDisabledAlertToUser();
    		}
    		else
    		{
    			if(reg_number.equals(""))
	    		{
	    			transThread = Executors.newSingleThreadExecutor();
	    			transThread.submit(new Runnable(){
	    				public void run(){
	    					guiProgressDialog(true);
	
	    					while(reg_number.equals(""))
	    					{
	    						//Log.i("registracija","vrti se petlja");
	    					}
	    					guiNotifyUser("postojeca");
	    					guiProgressDialog(false);
	    				}    				
	    			});    			
	    		}
	    		else
	    		{
	    			startPostojeceActivity();
	    		}
    		}
	    		
    		break;
    	case R.id.dugme_exit:
    		//GCMRegistrar.unregister(this);
    		finish();
    		break;
    	}	
  	}

	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	protected void onDestroy() { 
		
		Log.i("LIFECYCLE","MainActivity - onDestroy");
 
        try { 
    		GCMRegistrar.unregister(this);
            //GCMRegistrar.onDestroy(this); 
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
            
           // stopService(intentMyService);
           // unregisterReceiver(receiver);
        } catch (Exception e) { 
            //Log.e("UnRegister Receiver Error", "> " + e.getMessage()); 
        	e.printStackTrace();
        } 
        super.onDestroy(); 
    } 
	
    private void guiProgressDialog(final boolean start){
		guiThread.post(new Runnable() {
			
			public void run() {
				if(start)
				{
					progressDialog.setMessage("Prijavljivanje na servis je u toku...");
					progressDialog.show();
				}
				else
					progressDialog.dismiss();
				
			}
		});
	}
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
          
            Bundle igraBundle = intent.getExtras();
			if(igraBundle !=null)
				reg_number = intent.getStringExtra("googleservice_num");
			Log.i("InfoLog", "primljen brodkast" + reg_number);
        }
    };

    public void guiNotifyUser(final String act){
		
		guiThread.post(new Runnable() {			
			public void run() {				
				if(act.equals("nova")){
					startNovaActivity();
				}
				else{
					startPostojeceActivity();
				}
    				Toast.makeText(context, "prijavljen na google servis"  , Toast.LENGTH_SHORT).show();
			}
		});
	}
    
    public void startNovaActivity()
    {
    	Intent i;
    	i = new Intent(context, NovaIgraActivity.class);
    	i.putExtra("googleservice_num", reg_number);
    	startActivity(i);
    	//finish();
    }
    
    public void startPostojeceActivity()
    {
    	Intent i;
		i = new Intent(context, PostojeceIgreActivity.class);
		i.putExtra("googleservice_num", reg_number);
		startActivity(i);
		//finish();
    }
    
    public boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
          if (connectivity != null)
          {
              NetworkInfo[] info = connectivity.getAllNetworkInfo();
              if (info != null)
                  for (int i = 0; i < info.length; i++)
                      if (info[i].getState() == NetworkInfo.State.CONNECTED)
                      {
                          return true;
                      }
 
          }
          return false;
    }
    
    public void showAlertDialog(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
 
        // Setting Dialog Title
        alertDialog.setTitle(title);
 
        // Setting Dialog Message
        alertDialog.setMessage(message);
 
        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	dialog.cancel();
            }
        });
 
        // Showing Alert Message
        alertDialog.show();
    }
    
    private void showGPSDisabledAlertToUser(){
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
    	alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
    	.setCancelable(false)
    	.setPositiveButton("Goto Settings Page To Enable GPS",
    	new DialogInterface.OnClickListener(){
    	public void onClick(DialogInterface dialog, int id){
    	Intent callGPSSettingIntent = new Intent(
    	android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
    	startActivity(callGPSSettingIntent);
    	}
    	});
    	alertDialogBuilder.setNegativeButton("Cancel",
    	new DialogInterface.OnClickListener(){
    	public void onClick(DialogInterface dialog, int id){
    	dialog.cancel();
    	}
    	});
    	AlertDialog alert = alertDialogBuilder.create();
    	alert.show();
    	}
    
    protected void onStart()
    {
    	Log.i("LIFECYCLE","MainActivity - onStart");
    	super.onStart();
    }
    protected void onStop()
    {
    	Log.i("LIFECYCLE","MainActivity - onStop");
    	super.onStop();
    }
    protected void onPause()
    {
    	Log.i("LIFECYCLE","MainActivity - onPause");
    	super.onPause();
    }
    protected void onResume()
    {
    	Log.i("LIFECYCLE","MainActivity - onResume");
    	super.onResume();
    }
    

}


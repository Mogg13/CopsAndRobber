package modis.copsandrobber;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.google.android.gcm.GCMRegistrar;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
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
	
	protected void onCreate(Bundle savedInstanceState) {

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
	    			transThread.submit(new Runnable(){
	    				public void run(){
	    					guiProgressDialog(true);
	
	    					while(reg_number.equals(""))
	    					{
	    						
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
    		finish();
    		break;
    	}	
  	}

	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	protected void onDestroy() { 
 
        try { 
    		GCMRegistrar.unregister(this); 
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
            
        } catch (Exception e) {
        	e.printStackTrace();
        } 
        super.onDestroy(); 
    } 
	
    private void guiProgressDialog(final boolean start){
		guiThread.post(new Runnable() {
			
			public void run() {
				if(start)
				{
					progressDialog.setMessage("Connecting to service...");
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
    				Toast.makeText(context, "Connected to service."  , Toast.LENGTH_SHORT).show();
			}
		});
	}
    
    public void startNovaActivity()
    {
    	Intent i;
    	i = new Intent(context, NovaIgraActivity.class);
    	i.putExtra("googleservice_num", reg_number);
    	startActivity(i);	
    }
    
    public void startPostojeceActivity()
    {
    	Intent i;
		i = new Intent(context, PostojeceIgreActivity.class);
		i.putExtra("googleservice_num", reg_number);
		startActivity(i);
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
    
    public void showAlertDialog(String title, String message) 
    {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        alertDialog.setTitle(title);

        alertDialog.setMessage(message);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	dialog.cancel();
            }
        });

        alertDialog.show();
    }
    
    private void showGPSDisabledAlertToUser()
    {
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

}


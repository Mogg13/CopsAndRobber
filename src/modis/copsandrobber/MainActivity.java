package modis.copsandrobber;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.android.gcm.GCMRegistrar;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
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
	ComponentName service;
	BroadcastReceiver receiver;
	
	protected void onCreate(Bundle savedInstanceState) {
		
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
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		View novaIgraDugme = findViewById(R.id.dugme_nova_igra);
        novaIgraDugme.setOnClickListener(this);
        
        View postojeceIgreDugme = findViewById(R.id.dugme_postojece_igre);
        postojeceIgreDugme.setOnClickListener(this);  
        
        View exitDugme = findViewById(R.id.dugme_exit);
        exitDugme.setOnClickListener(this);
        
        progressDialog = new ProgressDialog(this);
        
        guiThread = new Handler();
		transThread = Executors.newSingleThreadExecutor();
		
		intentMyService= new Intent(this, CopsAndRobberGPSService.class);
		service = startService(intentMyService);
		
		/*IntentFilter mainFilter = newIntentFilter(GPS_FILTER);
		receiver = new MyMainLocalReceiver();
		registerReceiver(receiver, mainFilter);*/
	}

	public void onClick(View v) {	
		
    	
    	switch(v.getId())
    	{
    	case R.id.dugme_nova_igra:
    		
    		if(reg_number.equals(""))
    		{
    			//transThread = Executors.newSingleThreadExecutor();
    			transThread.submit(new Runnable(){
    				public void run(){
    					guiProgressDialog(true);

    					while(reg_number.equals(""))
    					{
    						//Log.i("registracija","vrti se petlja");
    					}
    					guiNotifyUser("nova");
    					guiProgressDialog(false);
    				}    				
    			});    			
    		}    		    		
    		else
    		{
    			startNovaActivity();
    		}

    		break;
    	case R.id.dugme_postojece_igre:
    		
    		if(reg_number.equals(""))
    		{
    			transThread = Executors.newSingleThreadExecutor();
    			transThread.submit(new Runnable(){
    				public void run(){
    					guiProgressDialog(true);

    					while(reg_number.equals(""))
    					{
    						Log.i("registracija","vrti se petlja");
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
    		

    		break;
    		case R.id.dugme_exit:
    		GCMRegistrar.unregister(this);
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
            GCMRegistrar.onDestroy(this); 
            stopService(intentMyService);
            unregisterReceiver(receiver);
        } catch (Exception e) { 
            Log.e("UnRegister Receiver Error", "> " + e.getMessage()); 
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
				
				if(act.equals("nova"))
				{
					startNovaActivity();
				}
				else
				{
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
    }
    
    public void startPostojeceActivity()
    {
    	Intent i;
		i = new Intent(context, PostojeceIgreActivity.class);
		i.putExtra("googleservice_num", reg_number);
		startActivity(i);
    }

}


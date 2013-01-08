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
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

import static modis.copsandrobber.CommonUtilities.SENDER_ID;

public class MainActivity extends Activity implements OnClickListener {
	
	private String reg_number;
	private ExecutorService transThread;
	private ProgressDialog progressDialog;
	private Handler guiThread;
	
	protected void onCreate(Bundle savedInstanceState) {
		
		
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		final String regId = GCMRegistrar.getRegistrationId(this); 
		if (regId.equals("")) { 
            // Registration is not present, register now with GCM 
            GCMRegistrar.register(this, SENDER_ID); 
		}
		else 
		{
			Log.i("InfoLog", "vec je logovano" + regId);  
		}
		
		reg_number = regId;
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		View novaIgraDugme = findViewById(R.id.dugme_nova_igra);
        novaIgraDugme.setOnClickListener(this);
        
        View postojeceIgreDugme = findViewById(R.id.dugme_postojece_igre);
        postojeceIgreDugme.setOnClickListener(this);  
        
        View exitDugme = findViewById(R.id.dugme_exit);
        exitDugme.setOnClickListener(this);
	}

	public void onClick(View v) {	
		
    	Intent i;
    	switch(v.getId())
    	{
    	case R.id.dugme_nova_igra:
    		i = new Intent(this, NovaIgraActivity.class);
    		i.putExtra("googleservice_num", reg_number);
    		startActivity(i);
    		break;
    	case R.id.dugme_postojece_igre:
    		i = new Intent(this, PostojeceIgreActivity.class);
    		i.putExtra("googleservice_num", reg_number);
    		startActivity(i);
    		break;
    	case R.id.dugme_exit:
    		/*Intent unregIntent = new Intent("com.google.android.c2dm.intent.UNREGISTER");
    		unregIntent.putExtra("app", PendingIntent.getBroadcast(this, 0, new Intent(), 0));
    		startService(unregIntent);*/
    		GCMRegistrar.unregister(this);
    		finish();
    		break;
    	}	
  	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	protected void onDestroy() { 
 
        try { 
        	/*Intent unregIntent = new Intent("com.google.android.c2dm.intent.UNREGISTER");
    		unregIntent.putExtra("app", PendingIntent.getBroadcast(this, 0, new Intent(), 0));
    		startService(unregIntent);*/
    		GCMRegistrar.unregister(this);
            GCMRegistrar.onDestroy(this); 
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
    
	
}

package modis.copsandrobber;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import static modis.copsandrobber.CommonUtilities.SENDER_ID;
import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

    private static final String TAG = "GCMIntentService"; 
    
    public GCMIntentService() { 
        super(SENDER_ID); 
    }

	protected void onError(Context arg0, String arg1) {

		Log.i(TAG, "Received error: "); 

	}

	protected void onMessage(Context arg0, Intent arg1) {

		Bundle b = arg1.getExtras();
        String message = b.getString("kod_poruke");
        
        if(message.equals("objekat_opljackan"))        	
        {
        	Log.i(TAG, "OBJEKAT OPLJACKAN: " + message);
        	int id = Integer.parseInt(b.getString("idObjekta"));
        	Intent intent = new Intent("object_robbed_intent");
        	intent.putExtra("idObjekta", id);
    	    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
        else if(message.equals("pocetak_igre"))
        {
        	Log.i(TAG, "POCINJE IGRA"); 
    	    Intent intent = new Intent("start_the_game");
    	    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
        else if(message.equals("ometac_aktiviran"))
        {
        	Log.i(TAG, "Received message: " + message);
        	Intent intent = new Intent("ometac_aktiviran");
    	    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
        else if(message.equals("pancir_akticiran"))
        {
        	Log.i(TAG, "Received message: " + message);
        	Intent intent = new Intent("pancir_akticiran");
    	    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
        else if(message.equals("kraj_igre"))
        {
        	Log.i(TAG, "Received message: " + message);
        	String poruka = b.getString("poruka");
        	Intent intent = new Intent("end_the_game");
        	intent.putExtra("poruka", poruka);
        	intent.putExtra("odustajanje", "no");
    	    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
        else if(message.equals("odustajanje"))
        {
        	Log.i(TAG, "Received message: " + message);
        	String poruka = b.getString("poruka");
        	Intent intent = new Intent("end_the_game");
        	intent.putExtra("poruka", poruka);
        	intent.putExtra("odustajanje", "yes");
    	    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
	}

	protected void onRegistered(Context arg0, String arg1) {

		Log.i(TAG, "Device registered: regId = " + arg1); 
	    Intent intent = new Intent("googleservice_registration");
	    intent.putExtra("googleservice_num", arg1);
	    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}

	protected void onUnregistered(Context arg0, String arg1) {

		Log.i(TAG, "Device unregistered"); 
	}
	

}

package modis.copsandrobber;
//import com.google.android.gcm.GCMBaseIntentService; 
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;
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

		Log.i(TAG, "Received message"); 
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

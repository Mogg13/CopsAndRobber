package modis.copsandrobber;
//import com.google.android.gcm.GCMBaseIntentService; 
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import static modis.copsandrobber.CommonUtilities.SENDER_ID;
import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

    private static final String TAG = "GCMIntentService"; 
    
    public GCMIntentService() { 
        super(SENDER_ID); 
    }
	@Override
	protected void onError(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		Log.i(TAG, "Received error: "); 

	}

	@Override
	protected void onMessage(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		Log.i(TAG, "Received message"); 
	}

	@Override
	protected void onRegistered(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		Log.i(TAG, "Device registered: regId = " + arg1); 
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		Log.i(TAG, "Device unregistered"); 
	}
	

}

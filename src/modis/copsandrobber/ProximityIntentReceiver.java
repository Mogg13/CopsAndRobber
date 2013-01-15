package modis.copsandrobber;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

public class ProximityIntentReceiver extends BroadcastReceiver  {


			
	@Override
	public void onReceive(Context arg0, Intent intent) {
		// TODO Auto-generated method stub
		String key = LocationManager.KEY_PROXIMITY_ENTERING;
		Boolean entering = intent.getBooleanExtra(key, false);
		if (entering)
		{
			
		}
		else
		{
			
		}
		
	}

}

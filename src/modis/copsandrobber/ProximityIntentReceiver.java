package modis.copsandrobber;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class ProximityIntentReceiver extends BroadcastReceiver  {
			
	@Override
	public void onReceive(Context arg0, Intent intent) {
		
		Log.i("PROXIMITY", "uhvaceno nesto");
		String key = LocationManager.KEY_PROXIMITY_ENTERING;
		Boolean entering = intent.getBooleanExtra(key, false);         
		String tip_intenta = intent.getStringExtra("tip");
		Log.i("PROXIMITY", tip_intenta);
		
		if(tip_intenta.equals("policija"))
		{
			if (entering) {
				Intent in = new Intent("u_policiji");
				LocalBroadcastManager.getInstance(arg0).sendBroadcast(in);
			}
		}
		else if(tip_intenta.equals("objekat"))
		{
			String kod = intent.getStringExtra("vrednost");
			if (entering) {
				Intent in = new Intent("u_objektu");
				in.putExtra("vrednost", kod);
				LocalBroadcastManager.getInstance(arg0).sendBroadcast(in);
			}
		}
		else
		{
			String kod = intent.getStringExtra("vrednost");
			if (entering) {
	    	    Intent in = new Intent("u_predmetu");
	    	    in.putExtra("vrednost", kod);
	    	    LocalBroadcastManager.getInstance(arg0).sendBroadcast(in);
			}
		}
		
	}

}

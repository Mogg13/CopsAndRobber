package modis.copsandrobber;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class ProximityIntentReceiver extends BroadcastReceiver  {
			
	@Override
	public void onReceive(Context arg0, Intent intent) {
		
		Log.i("PROXIMITY", "uhvaceno nesto");
		String key = LocationManager.KEY_PROXIMITY_ENTERING;
		 		
		Bundle extras = intent.getExtras();
		Boolean entering = intent.getBooleanExtra(key, false); 

		String tip_intenta = extras.getString("tip");
		Log.i("PROXII", tip_intenta);
		if(tip_intenta.equals("policija"))
		{
			if (entering) {
				Intent in = new Intent("u_policiji");
				in.putExtra("entering", "true");
				LocalBroadcastManager.getInstance(arg0).sendBroadcast(in);
			}
			else
			{
				Intent in = new Intent("u_policiji");
				in.putExtra("entering", "false");
				LocalBroadcastManager.getInstance(arg0).sendBroadcast(in);
			}
		}
		else if(tip_intenta.equals("objekat"))
		{
			int kod = extras.getInt("vrednost");
			if (entering) {
				Intent in = new Intent("u_objektu");
				Log.i("PROXI", Integer.toString(kod));
				in.putExtra("vrednost", kod);
				in.putExtra("entering", "true");
				LocalBroadcastManager.getInstance(arg0).sendBroadcast(in);
			}
			else
			{
				Intent in = new Intent("u_objektu");
				Log.i("PROXI 2", Integer.toString(kod));
				in.putExtra("vrednost", kod);
				in.putExtra("entering", "false");
				LocalBroadcastManager.getInstance(arg0).sendBroadcast(in);
			}
		}
		else
		{
			int kod = extras.getInt("vrednost");
			if (entering) 
			{
	    	    Intent in = new Intent("u_predmetu");
	    	    in.putExtra("vrednost", kod);
	    	    Log.i("PROXI P", Integer.toString(kod));
	    	    LocalBroadcastManager.getInstance(arg0).sendBroadcast(in);

			}

		}		
	}
}

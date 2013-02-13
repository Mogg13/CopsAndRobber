package modis.copsandrobber;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

public class ProximityIntentReceiver extends BroadcastReceiver  {
			
	@Override
	public void onReceive(Context arg0, Intent intent) {
		
		String key = LocationManager.KEY_PROXIMITY_ENTERING;
		 		
		Bundle extras = intent.getExtras();
		Boolean entering = intent.getBooleanExtra(key, false); 

		String tip_intenta = extras.getString("tip");
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
				in.putExtra("vrednost", kod);
				in.putExtra("entering", "true");
				LocalBroadcastManager.getInstance(arg0).sendBroadcast(in);
			}
			else
			{
				Intent in = new Intent("u_objektu");
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
	    	    LocalBroadcastManager.getInstance(arg0).sendBroadcast(in);

			}

		}		
	}
}

package modis.copsandrobber;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.maps.MapActivity;

public class MapaActivity extends MapActivity{

	Igra igra;
	
	@Override
	protected boolean isRouteDisplayed() {
		
		return false;
	}
	
	public void onCreate(Bundle savedInstanceState)	{
		
		super.onCreate(savedInstanceState);
		igra = new Igra();
		try {
			Intent mapIntent = getIntent();
			Bundle mapBundle = mapIntent.getExtras();
			if(mapBundle !=null)
			{
				String imeIgre = mapBundle.getString("imeIgre");
				igra.setIme(imeIgre);
			}
		} catch (Exception e) {
		}
		
	}
	

}

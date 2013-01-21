package modis.copsandrobber;
import java.io.Serializable;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;



public class JedanOverlay extends Overlay implements Serializable{
	
	private Bitmap bmp;
	private Context context;
	private String lat;
	private String lon;
	
	// vezano za touch event
	private Objekat objekat;
	private String uloga;
	
	public JedanOverlay(int kod, String lat, String lon)
	{
		bmp = BitmapFactory.decodeResource(CopsAndRobberApplication.getContext().getResources(), kod);
		this.lat = lat;
		this.lon = lon;
		this.objekat = null;
	}
	
	public JedanOverlay(int kod, String lat, String lon, Objekat obj, String ulo)
	{
		bmp = BitmapFactory.decodeResource(CopsAndRobberApplication.getContext().getResources(), kod);
		this.lat = lat;
		this.lon = lon;
		this.objekat = obj;
		this.uloga = ulo;
	}
	
	public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when)
	{
		super.draw(canvas, mapView, shadow);
		
		Point screenPts = new Point();
		
		int latE6 = (int)(Double.parseDouble(lat)*1e6);
		int lonE6 = (int)(Double.parseDouble(lon)*1e6);
		GeoPoint p = new GeoPoint(latE6, lonE6);
		mapView.getProjection().toPixels(p, screenPts);
		canvas.drawBitmap(bmp, screenPts.x - bmp.getWidth()/2, screenPts.y - bmp.getHeight(), null);
		
		return true;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}
	
	
	
	public boolean onTouchEvent(MotionEvent event, MapView mapView)
	{
		if(event.getAction() == MotionEvent.ACTION_UP){
			if(objekat != null)
			{
				//if(uloga.equals("Lopov"))
				//{
				//	if(!objekat.getIme().equals("sigurna kuca") && !objekat.getIme().equals("policija"))
				//	{
						Point screenPts = new Point();
						
						String lat = objekat.getLatitude();
						String lon = objekat.getLongitude();
						int latE6 = (int)(Double.parseDouble(lat)*1E6);
						int lonE6 = (int)(Double.parseDouble(lon)*1E6);
						GeoPoint p = new GeoPoint(latE6, lonE6);
						mapView.getProjection().toPixels(p, screenPts);
						if(Math.abs(screenPts.x - (int)event.getX())<bmp.getWidth()/2 && Math.abs(screenPts.y - (int)event.getY()) < bmp.getHeight())
						{
							//Toast.makeText(CopsAndRobberApplication.getContext(), "Overay", Toast.LENGTH_SHORT).show();
							Log.i("TOUCH", objekat.getIme());
							MapaActivity.napraviDialogZaObjekat(objekat, uloga);
						}
				//	}
				//}
			}
		}
		return false;
	}

}

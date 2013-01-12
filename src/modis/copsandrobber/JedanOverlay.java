package modis.copsandrobber;
import java.io.Serializable;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;



public class JedanOverlay extends Overlay implements Serializable{
	
	Bitmap bmp;
	Context context;
	String lat;
	String lon;
	
	public JedanOverlay(int kod, String lat, String lon)
	{
		bmp = BitmapFactory.decodeResource(CopsAndRobberApplication.getContext().getResources(), kod);
		this.lat = lat;
		this.lon = lon;
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
	
	
	
	/*public boolean onTouchEvent(MotionEvent event, MapView mapView)
	{
		if(event.getAction() == MotionEvent.ACTION_UP){
			Point screenPts = new Point();
			ArrayList<MyPlace> places = MyPlacesData.getInstance().getMyPlaces();
			MyPlace p1 = null;
			int i = 0;
			while(p1 == null && i<places.size())
			{
				MyPlace place =places.get(i);
				String lat = place.getLatitude();
				String lon = place.getLongitude();
				int latE6 = (int)(Double.parseDouble(lat)*1E6);
				int lonE6 = (int)(Double.parseDouble(lon)*1E6);
				GeoPoint p = new GeoPoint(latE6, lonE6);
				mapView.getProjection().toPixels(p, screenPts);
				if(Math.abs(screenPts.x - (int)event.getX())<bmp.getWidth()/2 && screenPts.y - (int)event.getY() < bmp.getHeight())
				{
					p1 = place;
				}
				else
					i++;
			}
			if(p1!=null)
			{
				Intent intent = new Intent(mapView.getContext(),ViewMyPlaceActivity.class);
				intent.putExtra("position", i);
				mapView.getContext().startActivity(intent);
			}
			
		}
		return false;
	}*/

}

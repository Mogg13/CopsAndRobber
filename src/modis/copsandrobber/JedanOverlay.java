package modis.copsandrobber;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class JedanOverlay extends Overlay{
	
	private Bitmap bmp;
	private String lat;
	private String lon;
	private String ime;
	private Objekat objekat;
	private String uloga;
	
	public JedanOverlay(int kod, String lat, String lon, String ime)
	{
		this.bmp = BitmapFactory.decodeResource(CopsAndRobberApplication.getContext().getResources(), kod);
		this.lat = lat;
		this.lon = lon;
		this.objekat = null;
		this.ime = ime;
	}
	
	public JedanOverlay(int kod, String lat, String lon, Objekat obj, String ulo, String ime)
	{
		this.bmp = BitmapFactory.decodeResource(CopsAndRobberApplication.getContext().getResources(), kod);
		this.lat = lat;
		this.lon = lon;
		this.objekat = obj;
		this.uloga = ulo;
		this.ime = ime;
	}
	
	public void setBitmap(int kod)
	{
		this.bmp = BitmapFactory.decodeResource(CopsAndRobberApplication.getContext().getResources(), kod);
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
	
	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public boolean onTouchEvent(MotionEvent event, MapView mapView)
	{
		if(event.getAction() == MotionEvent.ACTION_UP){
			if(objekat != null)
			{
				Point screenPts = new Point();
				
				String lat = objekat.getLatitude();
				String lon = objekat.getLongitude();
				int latE6 = (int)(Double.parseDouble(lat)*1E6);
				int lonE6 = (int)(Double.parseDouble(lon)*1E6);
				GeoPoint p = new GeoPoint(latE6, lonE6);
				mapView.getProjection().toPixels(p, screenPts);
				int x = Math.abs(screenPts.x - (int)event.getX());
				int y = screenPts.y - (int)event.getY();
				if(x < bmp.getWidth()/2 &&  y < bmp.getHeight() && y > 0)
				{
					MapaActivity.napraviDialogZaObjekat(objekat, uloga);
				}
			}
			else
			{
				Point screenPts = new Point();				
				int latE6 = (int)(Double.parseDouble(lat)*1E6);
				int lonE6 = (int)(Double.parseDouble(lon)*1E6);
				GeoPoint p = new GeoPoint(latE6, lonE6);
				mapView.getProjection().toPixels(p, screenPts);
				int x = Math.abs(screenPts.x - (int)event.getX());
				int y = screenPts.y - (int)event.getY();
				if(x < bmp.getWidth()/2 &&  y < bmp.getHeight() && y > 0)
				{
					Toast.makeText(CopsAndRobberApplication.getContext(), ime, Toast.LENGTH_LONG).show();	
				}
			}
		}
		return false;
	}

}

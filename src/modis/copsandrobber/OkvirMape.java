package modis.copsandrobber;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

class OkvirMape extends Overlay{

	private Igra igra;
	 
	
	
    public OkvirMape(Igra igra) {
		this.igra = igra;
	}

	public void draw(Canvas canvas, MapView mapv, boolean shadow){
        super.draw(canvas, mapv, shadow);

        Paint   mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(2);

        nacrtajLiniju(igra.getLatitude1(), igra.getLongitude1(), igra.getLatitude2(), igra.getLongitude2(), canvas, mPaint, mapv);
        nacrtajLiniju(igra.getLatitude1(), igra.getLongitude1(), igra.getLatitude3(), igra.getLongitude3(), canvas, mPaint, mapv);
        nacrtajLiniju(igra.getLatitude4(), igra.getLongitude4(), igra.getLatitude2(), igra.getLongitude2(), canvas, mPaint, mapv);
        nacrtajLiniju(igra.getLatitude4(), igra.getLongitude4(), igra.getLatitude3(), igra.getLongitude3(), canvas, mPaint, mapv);
    }
    
    public void nacrtajLiniju(String coordX1, String coordY1, String coordX2, String coordY2, Canvas canvas, Paint mPaint, MapView mapv)
    {
    	
    	GeoPoint gP1 = new GeoPoint((int)(Double.parseDouble(coordX1)*1e6), (int)(Double.parseDouble(coordY1)*1e6));
        GeoPoint gP2 = new GeoPoint((int)(Double.parseDouble(coordX2)*1e6), (int)(Double.parseDouble(coordY2)*1e6));
        
        Point p1 = new Point();
        Point p2 = new Point();
        Path path = new Path();

        mapv.getProjection().toPixels(gP1, p1);
        mapv.getProjection().toPixels(gP2, p2);

        path.moveTo(p2.x, p2.y);
        path.lineTo(p1.x,p1.y);

        canvas.drawPath(path, mPaint);
    }
}


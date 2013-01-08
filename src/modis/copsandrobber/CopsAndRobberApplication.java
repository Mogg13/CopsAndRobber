package modis.copsandrobber;

import android.app.Application;
import android.content.Context;


public class CopsAndRobberApplication extends Application{

	private static CopsAndRobberApplication instance;
	
	public CopsAndRobberApplication() {
		instance = this;
	}
	
	public static Context getContext(){
		return instance;
	}
}

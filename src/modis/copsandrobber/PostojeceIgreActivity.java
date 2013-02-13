package modis.copsandrobber;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class PostojeceIgreActivity extends Activity implements OnItemSelectedListener, OnClickListener{

	private Spinner spinner;
	private Spinner spinnerPL;
	private int pozicijaUloge;
	private String uloga;
	private String igra;
	private Handler guiThread;
	Context context;
	private ExecutorService transThread;
	private ProgressDialog progressDialog;
	private int pozicijaIgre;
	private String googleservice_num;
	private String longitude;
	private String latitude;
	private Igrac igrac;

	public void onCreate(Bundle savedInstanceState) {
		
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.postojece_igre);
        
        longitude = "0.0";
        latitude = "0.0";
	    
        try {
			Intent igraIntent = getIntent();
			Bundle igraBundle = igraIntent.getExtras();
			if(igraBundle !=null)
			{
				googleservice_num = igraBundle.getString("googleservice_num");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        View pridruziSeDugme = findViewById(R.id.dugme_pridruzi_se);
        pridruziSeDugme.setOnClickListener(this);
        
        progressDialog = new ProgressDialog(this);
        
        this.context = this;
        
        guiThread = new Handler();
		transThread = Executors.newSingleThreadExecutor();
		transThread.submit(new Runnable() {
			
			public void run() {
				guiProgressDialog(true);
				try{
					final List<String> names = CopsandrobberHTTPHelper.getAllGames();
					guiSetAdapter(names);
				} catch (Exception e){
					e.printStackTrace();
				}
				guiProgressDialog(false);
			}
		});
        
        spinner = (Spinner) findViewById(R.id.spinner_postojece_igre);
        
        spinner.setOnItemSelectedListener(this);
        
        spinnerPL = (Spinner) findViewById(R.id.spinner_policajac_lopov);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.uloge, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPL.setAdapter(adapter);
        
        spinnerPL.setOnItemSelectedListener(this);

    }

	public void onClick(View v) {	
    	switch(v.getId())
    	{
    	case R.id.dugme_pridruzi_se:
    		
    		if (spinner.getAdapter().getCount() > 0)
    		{
    			igra = (String) spinner.getItemAtPosition(pozicijaIgre);
    			uloga = (String) spinnerPL.getItemAtPosition(pozicijaUloge);
    			
				transThread.submit(new Runnable(){
					public void run(){
						guiProgressDialog(true);
						try{
							igrac = new Igrac(uloga, latitude, longitude, googleservice_num);
							final String message =CopsandrobberHTTPHelper.pridruziSeIgri(igrac, igra);
							guiNotifyUser(message, uloga);
						} catch(Exception e){e.printStackTrace();}
						
						guiProgressDialog(false);
					}
					
				});				
    		}
    		else
    		{
    			Toast.makeText(context, "No available games. Please create a new one.", Toast.LENGTH_SHORT).show();
    		}
    		break;
   	
    	}	
  	}

	public void guiNotifyUser(final String message, final String uloga){
		
		guiThread.post(new Runnable() {
			
			public void run() {
				if(message.equals(uloga))
				{
					Toast.makeText(context, "Registration successful!", Toast.LENGTH_SHORT).show();
					startMapActivity();
				}
				else
				{
					Toast.makeText(context, "Desired role was already taken, you are assigned with opposite role.", Toast.LENGTH_LONG).show();				
					igrac.setUloga(message);
					startMapActivity();
				}
			}
		});
	}
	
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
	{
		switch(parent.getId())
		{
		case R.id.spinner_postojece_igre:
			pozicijaIgre = pos;
			break;
		case R.id.spinner_policajac_lopov:
			pozicijaUloge = pos;
			break;
		}		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

    private void guiProgressDialog(final boolean start){
		guiThread.post(new Runnable() {
			
			public void run() {
				if(start)
				{
					progressDialog.setMessage("Loading game...");
					progressDialog.show();
				}
				else
					progressDialog.dismiss();
				
			}
		});
	}
    
    private void guiSetAdapter(final List<String> names) {
		guiThread.post(new Runnable() {
			
			public void run(){
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(CopsAndRobberApplication.getContext(), android.R.layout.simple_spinner_item, names);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner.setAdapter(adapter);
			}
		});
	}

    public void startMapActivity()
    {
    	Intent i = new Intent (this, MapaActivity.class);
		i.putExtra("imeIgre", igra);
		i.putExtra("uloga", igrac.getUloga());
		i.putExtra("lat", igrac.getLatitude());
		i.putExtra("lon", igrac.getLongitude());
		i.putExtra("reg_id", igrac.getRegId());
		startActivity(i);
		finish();
    }
    
    protected void onDestroy()
    {
    	try{
    		transThread.shutdown();   
    	}catch (Exception e) { 
    		e.printStackTrace();
        } 
    	super.onDestroy();
    }
    
}

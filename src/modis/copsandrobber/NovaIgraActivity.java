package modis.copsandrobber;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class NovaIgraActivity extends Activity implements OnItemSelectedListener, OnClickListener, OnKeyListener{

	private Spinner spinnerPL;
	private int pozicijaUloge;
	private EditText imeIgre;
	private Handler guiThread;
	Context context;
	private ExecutorService transThread;
	private ProgressDialog progressDialog;
	private String uloga;
	private String ime;
	private String googleservice_num;
	private Igrac igrac;
	private String longitude;
	private String latitude;
	
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.nova_igra);
        
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
        
        progressDialog = new ProgressDialog(this);
        
        this.context = this;
        
        guiThread = new Handler();
		transThread = Executors.newSingleThreadExecutor();
        
        View kreirajIgru = findViewById(R.id.kreiraj_igru_dugme);
        kreirajIgru.setOnClickListener(this);
        
        imeIgre = (EditText) findViewById(R.id.ime_nove_igre_edit); 
        imeIgre.setOnKeyListener(this);
        
        spinnerPL = (Spinner) findViewById(R.id.spinner_policajac_lopov);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.uloge, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPL.setAdapter(adapter);
        
        spinnerPL.setOnItemSelectedListener(this);
    }
	
	@Override
	public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onClick(View v) {
    	switch(v.getId())
    	{
    	case R.id.kreiraj_igru_dugme:
    		
    		ime = imeIgre.getText().toString();
    		
    		uloga = (String) spinnerPL.getItemAtPosition(pozicijaUloge);
    		if(!ime.equals(""))
    		{
    			transThread.submit(new Runnable(){
    				public void run(){
    					guiProgressDialog(true);
    					try{
    						igrac = new Igrac(uloga, latitude, longitude, googleservice_num);
    						final String message = CopsandrobberHTTPHelper.napraviNovuIgru(igrac, ime);
    						final String igra = ime;
    						guiNotifyUser(message, igra);
    					} catch(Exception e){e.printStackTrace();}
    					
    					guiProgressDialog(false);
    				}    				
    			});    			
    		}
    		else
    			Toast.makeText(this, "Name field can not be empty!", Toast.LENGTH_SHORT).show();    			
    		break;    	
    	}	
		
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
	{
		pozicijaUloge = pos;
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

    public void guiNotifyUser(final String message, final String igra){
		
		guiThread.post(new Runnable() {
			
			public void run() {
				
				if(message.equals("1"))
    				Toast.makeText(context, "Sorry, that name already exists!", Toast.LENGTH_SHORT).show();
    			else
    			{
    				Intent i;
    				i = new Intent (context, MapaActivity.class);
    				i.putExtra("imeIgre", igra);
    				i.putExtra("uloga", igrac.getUloga());
    				i.putExtra("lat", igrac.getLatitude());
    				i.putExtra("lon", igrac.getLongitude());
    				i.putExtra("reg_id", igrac.getRegId());
        			startActivity(i);
    				Toast.makeText(context, "Created game: " + ime , Toast.LENGTH_SHORT).show();
    				finish();
    			}
			}
		});
	}
    
    private void guiProgressDialog(final boolean start){
		guiThread.post(new Runnable() {
			
			public void run() {
				if(start)
				{
					progressDialog.setMessage("Communicating with server...");
					progressDialog.show();
				}
				else
					progressDialog.dismiss();				
			}
		});
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

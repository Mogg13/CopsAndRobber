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
        setContentView(R.layout.postojece_igre);
        
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
		
    	Intent i;
    	int result = 1;
    	switch(v.getId())
    	{
    	case R.id.dugme_pridruzi_se:
    		
    		if (spinner.getAdapter().getCount() > 0)
    		{
    			igra = (String) spinner.getItemAtPosition(pozicijaIgre);
    			uloga = (String) spinnerPL.getItemAtPosition(pozicijaUloge);
    			
    			ExecutorService transThread = Executors.newSingleThreadExecutor();
				transThread.submit(new Runnable(){
					public void run(){
						guiProgressDialog(true);
						try{
							igrac = new Igrac(uloga, "999999999","8888888888", googleservice_num);
							final String message =CopsandrobberHTTPHelper.pridruziSeIgri(igrac, igra);
							guiNotifyUser(message, uloga);
							//greska = message;
						} catch(Exception e){e.printStackTrace();}
						
						guiProgressDialog(false);
					}
					
				});
				
				
    		}
    		else
    		{
    			Toast.makeText(context, "Ne postoje slobodne igre. Napravite novu.", Toast.LENGTH_SHORT).show();
    		}
    		break;
   	
    	}	
  	}

	public void guiNotifyUser(final String message, final String uloga){
		
		guiThread.post(new Runnable() {
			
			public void run() {
				if(message.equals(uloga))
				{
					Toast.makeText(context, "Uspesno ste se prijavili!", Toast.LENGTH_SHORT).show();
					startMapActivity();
				}
				else
				{
					Toast.makeText(context, "Posto je uloga koju ste izabrali zauzeta, prijavljeni ste kao " + message , Toast.LENGTH_SHORT).show();				
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
			//Toast.makeText(parent.getContext(), "Odabrali ste " + parent.getItemAtPosition(pos).toString(), Toast.LENGTH_SHORT).show();
			break;
		case R.id.spinner_policajac_lopov:
			pozicijaUloge = pos;
			//Toast.makeText(parent.getContext(), "Odabrali ste " + parent.getItemAtPosition(pos).toString(), Toast.LENGTH_SHORT).show();
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
					progressDialog.setMessage("Ucitavanje igara u toku...");
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
		i.putExtra("igrac", igrac);
		startActivity(i);
    }
    
	
    
}

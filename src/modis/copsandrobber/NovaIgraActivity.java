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
	private String greska;
	private String googleservice_num;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nova_igra);
        
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
		Intent i;
    	int result = 1;
    	switch(v.getId())
    	{
    	case R.id.kreiraj_igru_dugme:
    		
    		ime = imeIgre.getText().toString();
    		
    		uloga = (String) spinnerPL.getItemAtPosition(pozicijaUloge);
    		if(!ime.equals(""))
    		{
    			ExecutorService transThread = Executors.newSingleThreadExecutor();
    			transThread.submit(new Runnable(){
    				public void run(){
    					guiProgressDialog(true);
    					try{
    						Igrac igrac = new Igrac(uloga, "44444444444444","444444444444", googleservice_num);
    						final String message = CopsandrobberHTTPHelper.napraviNovuIgru(igrac, ime);
    						final String igra = ime;
    						guiNotifyUser(message, igra);
    					} catch(Exception e){e.printStackTrace();}
    					
    					guiProgressDialog(false);
    				}    				
    			});    			
    		}
    		else
    			Toast.makeText(this, "Polje za ime je prazno!", Toast.LENGTH_SHORT).show();    			
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
    				Toast.makeText(context, "Takvo ime za igru vec postoji!", Toast.LENGTH_SHORT).show();
    			else
    			{
    				/*Intent i;
    				i = new Intent (context, MapaActivity.class);
    				i.putExtra("imeIgre", igra);
        			startActivity(i);*/
    				Toast.makeText(context, "Kreirana igra: " + ime , Toast.LENGTH_SHORT).show();
    			}
			}
		});
	}
    
    private void guiProgressDialog(final boolean start){
		guiThread.post(new Runnable() {
			
			public void run() {
				if(start)
				{
					progressDialog.setMessage("Komunikacija sa serverom...");
					progressDialog.show();
				}
				else
					progressDialog.dismiss();				
			}
		});
	}
}

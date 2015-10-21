package com.durox.app.Presupuestos;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.durox_app.R;
import com.durox.app.Config_durox;
import com.durox.app.MenuActivity;
import com.durox.app.Models.Estados_presupuesto_model;
import com.durox.app.Models.Lineas_Presupuestos_model;
import com.durox.app.Models.Presupuestos_model;
import com.durox.app.Models.Vendedores_model;


import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
 
public class Presupuestos_Lista extends MenuActivity {
	
	// Declare Variables
	ListView list;
	Presupuestos_ListView adapter;
	EditText editsearch;
	
	String[] c_nombre;
	String[] p_nombre;
	String[] total;
	String[] estado;
	String[] fecha;
	int[] foto;
	String[] id_presupuesto;
	String[] id_back;
	String[] id_pback;
	int[] imagen;
	ArrayList<Presupuesto> arraylist = new ArrayList<Presupuesto>();
	SQLiteDatabase db;
	
	String truncate;
	String sql;
	Cursor c;
	int j;	
		
	private String jsonResult;
	
	TextView content;
	Config_durox config;
	
	Presupuestos_model mPresupuesto;
	Lineas_Presupuestos_model mLineas;
	Estados_presupuesto_model mEstados;
	ProgressDialog pDialog;
	Vendedores_model mVendedor;
	String id_vendedor;
   
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.presupuestos_listview);
        
        setTitle("Presupuestos - Lista");
        getActionBar().setIcon(R.drawable.menupresupuesto);
        
        config = new Config_durox();
        db = openOrCreateDatabase(config.getDatabase(), Context.MODE_PRIVATE, null);
        
        mVendedor = new Vendedores_model(db);
        id_vendedor = mVendedor.getID();        
        
        presupuestos_lista();
   }
    
    
   public void presupuestos_lista(){
 		setContentView(R.layout.presupuestos_listview);
 		db = openOrCreateDatabase(config.getDatabase(), Context.MODE_PRIVATE, null);
    	
    	Presupuestos_model mPresupuestos = new Presupuestos_model(db);
 		c = mPresupuestos.getRegistros();
		
		int cantidad_presupuestos = c.getCount();
		
		id_back = new String[cantidad_presupuestos];
		id_presupuesto = new String[cantidad_presupuestos];
		c_nombre = new String[cantidad_presupuestos];
		total = new String[cantidad_presupuestos];
		estado = new String[cantidad_presupuestos];
		fecha = new String[cantidad_presupuestos];
		foto = new int[cantidad_presupuestos];
		
		int j = 0;
				
		if(c.getCount() > 0){
			while(c.moveToNext()){
				id_back[j] = c.getString(0);
				c_nombre[j] = c.getString(2);
				id_presupuesto[j] = c.getString(5);
    			total[j] = c.getString(3);
    			estado[j] = c.getString(6);
    			fecha[j] = c.getString(4);
    			foto[j] = R.drawable.presupuesto; 
    		
    			j = j + 1;
    		}
			
			list = (ListView) findViewById(R.id.lvPresupuestos);
    		arraylist.clear();

    		for (int i = 0; i < c_nombre.length; i++) {
    			Presupuesto wp = new Presupuesto(
    					id_back[i],
    					c_nombre[i],
    					total[i],
    					estado[i], 
    					fecha[i],
    					foto[i],
    					id_presupuesto[i]
    			);
    			
    			arraylist.add(wp);
    		}
    		
    		adapter = new Presupuestos_ListView(this, arraylist);
    		list.setAdapter(adapter);
    		editsearch = (EditText) findViewById(R.id.search);
    		
    		editsearch.addTextChangedListener(new TextWatcher() {
	   			public void afterTextChanged(Editable arg0) {
	   				String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
	    			adapter.filter(text);
	    		}
	
	    		public void beforeTextChanged(
	    			CharSequence arg0, 
	    			int arg1,
	    			int arg2, 
	    			int arg3) {
	    			}
	
	    			@Override
	    			public void onTextChanged(
	    					CharSequence arg0, 
	    					int arg1, 
	    					int arg2,
	    					int arg3) {
	    			}
    		});
		}
 	}	
    
    
    public void actualizar_presupuestos(View view) {
		pDialog = new ProgressDialog(this);
        pDialog.setMessage("Actualizando....");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
        
        Presupuestos_Update presupuestos = new Presupuestos_Update(db, this);
        presupuestos.actualizar_registros();
        
        pDialog.dismiss();
        presupuestos_lista();
    	
    }	
}
  
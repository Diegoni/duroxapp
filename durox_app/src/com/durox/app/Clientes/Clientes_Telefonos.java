package com.durox.app.Clientes;

import com.example.durox_app.R;
import com.durox.app.Models.Documentos_model;
import com.durox.app.Models.Telefonos_clientes_model;

import java.util.ArrayList;
import java.util.Locale;

import com.durox.app.Config_durox;
import com.durox.app.MenuActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


public class Clientes_Telefonos extends MenuActivity {
	// Declare Variables
	ListView list;
	Telefonos_ListView adapter;
	EditText editsearch;
		
	String[] telefono;
	String[] cod_area;
	String[] tipo;
	int[] foto;
	String[] id_back;
		
	int[] imagen;
	ArrayList<Telefonos> arraylist = new ArrayList<Telefonos>();
	SQLiteDatabase db;
			
	String truncate;
	String sql;
	Cursor c;
	int j;	
			
	Telefonos_clientes_model mTelefono;
	TextView content;
			
	Documentos_model mDocumentos;
	Cursor cDocumentos;
			
	Config_durox config;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        config = new Config_durox();
        db = openOrCreateDatabase(config.getDatabase(), Context.MODE_PRIVATE, null);
		
		clientes_lista();
	}
	
	
	public void clientes_lista(){
		setContentView(R.layout.perfil_listview);
 		
		mTelefono = new Telefonos_clientes_model(db);
		
		Intent intent = getIntent();
		
		// Traigo los resultados 
		String id = intent.getStringExtra("id");
    	 	
		c = mTelefono.getTelefonosCliente(id);
		
		int cantidad_clientes = c.getCount();
		
		id_back = new String[cantidad_clientes];
		telefono = new String[cantidad_clientes];
		cod_area = new String[cantidad_clientes];
		tipo = new String[cantidad_clientes];
		foto = new int[cantidad_clientes];
	    
		int j = 0;
		
		if(c.getCount() > 0){
			while(c.moveToNext()){
				id_back[j] = c.getString(1);
				telefono[j] = c.getString(2);
				cod_area[j] = c.getString(3);
				tipo[j] = c.getString(5);
				foto[j] = R.drawable.phonecell; 
    		
    			j = j + 1;
    		}	
			
			list = (ListView) findViewById(R.id.lv_Perfiles);
    		arraylist.clear();

    		for (int i = 0; i < telefono.length; i++) {
    			Telefonos wp = new Telefonos(
    					id_back[i],
    					telefono[i],
    					cod_area[i], 
    					tipo[i],
    					id,
    					foto[i]
    			);
    			arraylist.add(wp);
    		}

    		adapter = new Telefonos_ListView(this, arraylist);
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

    			public void onTextChanged(
    					CharSequence arg0, 
    					int arg1, 
    					int arg2,
    					int arg3) {
    			}
    		});
		}
 	}
}
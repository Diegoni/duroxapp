package com.durox.app.Clientes;

import com.example.durox_app.R;
import com.durox.app.Models.Direcciones_clientes_model;
import com.durox.app.Models.Documentos_model;

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


public class Clientes_Direcciones extends MenuActivity {
	// Declare Variables
	ListView list;
	Direcciones_ListView adapter;
	EditText editsearch;
		
	String[] direccion;
	String[] id_departamento;
	String[] tipo;
	int[] foto;
	String[] id_back;
		
	int[] imagen;
	ArrayList<Direcciones> arraylist = new ArrayList<Direcciones>();
	SQLiteDatabase db;
			
	String truncate;
	String sql;
	Cursor c;
	int j;	
			
	Direcciones_clientes_model mDireccion;
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
 		
		mDireccion = new Direcciones_clientes_model(db);
		
		Intent intent = getIntent();
		
		// Traigo los resultados 
		String id = intent.getStringExtra("id");
    	 	
		c = mDireccion.getDireccionesCliente(id);
		
		int cantidad_clientes = c.getCount();
		
		id_back = new String[cantidad_clientes];
		direccion = new String[cantidad_clientes];
		id_departamento = new String[cantidad_clientes];
		tipo = new String[cantidad_clientes];
		foto = new int[cantidad_clientes];
	    
		int j = 0;
		
		if(c.getCount() > 0){
			while(c.moveToNext()){
				id_back[j] = c.getString(1);
				direccion[j] = c.getString(2);
				id_departamento[j] = c.getString(3);
				tipo[j] = c.getString(4);
				foto[j] = R.drawable.address; 
				
				j = j + 1;
    		}	
			
			list = (ListView) findViewById(R.id.lv_Perfiles);
    		arraylist.clear();

    		for (int i = 0; i < direccion.length; i++) {
    			Direcciones wp = new Direcciones(
    					id_back[i],
    					direccion[i],
    					id_departamento[i], 
    					tipo[i],
    					id,
    					foto[i]
    			);
    			arraylist.add(wp);
    		}

    		adapter = new  Direcciones_ListView(this, arraylist);
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
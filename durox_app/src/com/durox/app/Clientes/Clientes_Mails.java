package com.durox.app.Clientes;

import com.example.durox_app.R;
import com.durox.app.Models.Documentos_model;
import com.durox.app.Models.Mails_clientes_model;

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

public class Clientes_Mails extends MenuActivity {
	// Declare Variables
	ListView list;
	Mails_ListView adapter;
	EditText editsearch;
		
	String[] c_nombre;
	String[] direccion;
	int[] foto;
	String[] id_back;
		
	int[] imagen;
	ArrayList<Clientes> arraylist = new ArrayList<Clientes>();
	SQLiteDatabase db;
			
	String truncate;
	String sql;
	Cursor c;
	int j;	
			
	Mails_clientes_model mMail;
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
 		
		mMail = new Mails_clientes_model(db);
		
		Intent intent = getIntent();
		
		// Traigo los resultados 
		String id = intent.getStringExtra("id");
    	 	
		c = mMail.getRegistro(id);
		
		int cantidad_clientes = c.getCount();
		
		id_back = new String[cantidad_clientes];
		c_nombre = new String[cantidad_clientes];
		direccion = new String[cantidad_clientes];
		foto = new int[cantidad_clientes];
	    
		int j = 0;
		
		if(c.getCount() > 0)
		{
			while(c.moveToNext())
    		{
				id_back[j] = c.getString(1);
				c_nombre[j] = c.getString(3);
    			direccion[j] = c.getString(2);
    			foto[j] = R.drawable.email; 
    		
    			j = j + 1;
    		}	
			
			list = (ListView) findViewById(R.id.lv_Perfiles);
    		arraylist.clear();

    		for (int i = 0; i < c_nombre.length; i++) 
    		{
    			Clientes wp = new Clientes(
    					id_back[i],
    					c_nombre[i],
    					direccion[i], 
    					foto[i]
    			);
    			arraylist.add(wp);
    		}

    		adapter = new Mails_ListView(this, arraylist);
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
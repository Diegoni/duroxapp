package com.durox.app.Clientes;

import java.util.ArrayList;
import java.util.Locale;

import com.durox.app.Config_durox;
import com.durox.app.MenuActivity;
import com.durox.app.Models.Clientes_model;
import com.durox.app.Models.Vendedores_model;
import com.example.durox_app.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class Clientes_Main extends MenuActivity {

	ListView list;
	Clientes_ListView adapter;
	EditText editsearch;
	
	String[] id_back;
	String[] razon_social;
	String[] nombre;
	int[] imagen;
	
	ArrayList<Clientes> arraylist = new ArrayList<Clientes>();
	SQLiteDatabase db;
		
	String truncate;
	String sql;
	Cursor c;
	int j;	
		
	Clientes_model mCliente;
	TextView content;
	
	Vendedores_model mVendedor;
	String id_vendedor;
		
	Config_durox config;
	private ProgressDialog pDialog;
	
	private Button btn_orden;
	private Button btn_filtro;
	
	CharSequence orden;
	CharSequence filtro;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        config = new Config_durox();
        db = openOrCreateDatabase(config.getDatabase(), Context.MODE_PRIVATE, null);
        
        mVendedor = new Vendedores_model(db);
        id_vendedor = mVendedor.getID();
        
        setTitle("Clientes - Lista");
        getActionBar().setIcon(R.drawable.menuclientes);
        
        setContentView(R.layout.clientes_listview);
        
        orden = "";
        filtro = "razon social";
        
		clientes_lista(orden, filtro);
		
		btn_orden = (Button) findViewById(R.id.btn_orden);
	    btn_orden.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				PopupMenu popup = new PopupMenu(Clientes_Main.this, btn_orden);
				popup.getMenuInflater().inflate(R.menu.popup_cliente, popup.getMenu());

				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					public boolean onMenuItemClick(MenuItem item) {
						orden = item.getTitle();
							
						clientes_lista(orden, filtro);
							
						Toast.makeText(Clientes_Main.this, "Orden : " + item.getTitle(), Toast.LENGTH_SHORT).show();
		                        return true;
					}
				});
			        
				popup.show();
			}
		});
	        
	        
	    btn_filtro = (Button) findViewById(R.id.btn_filtro);
	    btn_filtro.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					PopupMenu popup = new PopupMenu(Clientes_Main.this, btn_filtro);
					popup.getMenuInflater().inflate(R.menu.popup_cliente, popup.getMenu());

					popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
						public boolean onMenuItemClick(MenuItem item) {
							filtro = item.getTitle();
							
							clientes_lista(orden, filtro);
							
							Toast.makeText(Clientes_Main.this, "Filtro : " + item.getTitle(), Toast.LENGTH_SHORT).show();
			                        return true;
						}
					});
			        
					popup.show();
				}
			});
	}
	
	
	public void clientes_lista(CharSequence orden, final CharSequence filtro){
		mCliente = new Clientes_model(db);
    	
    	c = mCliente.getRegistros(orden);
		
		int cantidad_clientes = c.getCount();
		
		id_back = new String[cantidad_clientes];
		razon_social = new String[cantidad_clientes];
		nombre = new String[cantidad_clientes];
		imagen = new int[cantidad_clientes];
	    
		int j = 0;
		
		if(c.getCount() > 0){
			while(c.moveToNext()){
				id_back[j] = c.getString(1);
				razon_social[j] = c.getString(10);
    			nombre[j] = c.getString(3)+" "+c.getString(2);
    			imagen[j] = R.drawable.clientes; 
    			
    			Log.e("Cliente ", id_back[j]+" "+razon_social[j]+" "+c.getString(3)+" "+c.getString(2));
    		
    			j = j + 1;
    		}	
			
			list = (ListView) findViewById(R.id.lvClientes);
    		arraylist.clear();

    		for (int i = 0; i < razon_social.length; i++) {
    			Clientes wp = new Clientes(
    					id_back[i],
    					razon_social[i],
    					nombre[i], 
    					imagen[i]
    			);
    			arraylist.add(wp);
    		}

    		adapter = new Clientes_ListView(this, arraylist);
    		list.setAdapter(adapter);
    		
    		editsearch = (EditText) findViewById(R.id.search);
    		editsearch.setHint(filtro);
    		
    		editsearch.addTextChangedListener(new TextWatcher() {
    			public void afterTextChanged(Editable arg0) {
    				String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
    				adapter.filter(text, filtro);
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
	
	
	/*---------------------------------------------------------------------------------
		Funciones con los botones Actualizar
	---------------------------------------------------------------------------------*/    	

	public void actualizar_clientes(View view) {
		 pDialog = new ProgressDialog(Clientes_Main.this);
         pDialog.setMessage("Actualizando....");
         pDialog.setIndeterminate(false);
         pDialog.setCancelable(false);
         pDialog.show();
         
         Clientes_Update clientes = new Clientes_Update(db, this);
         clientes.actualizar_registros();
         
         pDialog.dismiss();
	}
	
	
	public void agregar(View view) {
		Intent intentClientes = new Intent(this, Clientes_Edit.class);
		intentClientes.putExtra("id", "0");
		startActivity(intentClientes);
	}
	 
}

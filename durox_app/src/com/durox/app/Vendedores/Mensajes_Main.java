package com.durox.app.Vendedores;

import java.util.ArrayList;
import java.util.Locale;

import com.durox.app.Config_durox;
import com.durox.app.MenuActivity;
import com.durox.app.Models.Mensajes_model;
import com.durox.app.Models.Vendedores_model;
import com.example.durox_app.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class Mensajes_Main extends MenuActivity {
	String[] asunto;
	String[] id_back;
	String[] mensaje;
	String[] date_add;
	
	int[] imagen;
	
	ListView list;
	Mensajes_ListView adapter;
	
	EditText editsearch;

	ArrayList<Mensajes> arraylist = new ArrayList<Mensajes>();
	SQLiteDatabase db;
		
	String truncate;
	String sql;
	String id_vendedor;
	Cursor c;
	int j;	
		
	TextView content;
	
	Config_durox config;
	Mensajes_model mMensajes;
	Vendedores_model mVendedor;
	
	ProgressDialog pDialog;
	Button btn_entrada;
	Button btn_enviados;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        config = new Config_durox();
        db = openOrCreateDatabase(config.getDatabase(), Context.MODE_PRIVATE, null);
        
        mVendedor = new Vendedores_model(db);
        id_vendedor = mVendedor.getID();
        
        setTitle("Mensajes - Lista");
        getActionBar().setIcon(R.drawable.menuproductos);
       
		mensajes_lista("entrada");
		
	}
	
	public void actualizar_mensajes(View view) {
		pDialog = new ProgressDialog(this);
        pDialog.setMessage("Actualizando....");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
        
        Mensajes_Update mensajes = new Mensajes_Update(db, this);
        mensajes.actualizar_registros();
        
        pDialog.dismiss();
    	mensajes_lista("entrada");   
        
	}
	
	
	public void entrada_mensajes(View view) {
		mensajes_lista("entrada");
	}
	
	public void enviados_mensajes(View view) {
		mensajes_lista("enviados");
	}
	
	
	public void mensajes_lista(String e){
		setContentView(R.layout.mensajes_listview);
		btn_entrada = (Button) findViewById(R.id.btn_entrada);
	    btn_enviados = (Button) findViewById(R.id.btn_enviados);
	    String id_origen;
		
		if(e.equals("entrada")){
			btn_entrada.setEnabled(false);
			id_origen = "2";
		}else{
			btn_enviados.setEnabled(false);
			id_origen = "1";
		}
		
		mMensajes = new Mensajes_model(db);
		mMensajes.createTable();
		c = mMensajes.getMensajes(e);
		
		int cantidad_mensajes = c.getCount();
		
		asunto = new String[cantidad_mensajes];
		id_back = new String[cantidad_mensajes];
		mensaje = new String[cantidad_mensajes];
		date_add = new String[cantidad_mensajes];
		imagen = new int[cantidad_mensajes];
		
		int j = 0;
			
		if(c.getCount() > 0){
			while(c.moveToNext()){
				id_back[j] = c.getString(0);
				asunto[j] = c.getString(1);
				mensaje[j] = c.getString(2);
				date_add[j] = c.getString(3);
				imagen[j] = R.drawable.productos; 
				j = j + 1;
			}	
			
			list = (ListView) findViewById(R.id.listview);
			arraylist.clear();
			
			for (int i = 0; i < asunto.length; i++) {
				Mensajes wp = new Mensajes(
						id_back[i],
						asunto[i],
						mensaje[i], 
						date_add[i],
						id_origen,
						imagen[i]
				);
				
				arraylist.add(wp);
			}
			
			adapter = new Mensajes_ListView(this, arraylist);
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
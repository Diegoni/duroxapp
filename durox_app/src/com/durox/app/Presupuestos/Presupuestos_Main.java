package com.durox.app.Presupuestos;

import java.util.ArrayList;
import java.util.Locale;

import com.example.durox_app.R;
import com.durox.app.Config_durox;
import com.durox.app.MenuActivity;
import com.durox.app.Models.Vendedores_model;
import com.durox.app.Models.Visitas_model;
import com.durox.app.Visitas.Epocas_Update;
import com.durox.app.Visitas.Visitas;
import com.durox.app.Visitas.Visitas_ListView;
import com.durox.app.Visitas.Visitas_Update;

import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

public class Presupuestos_Main extends MenuActivity {

	// Declare Variables
	ListView list;
	Visitas_ListView adapter;
	EditText editsearch;
	
	ArrayList<Visitas> arraylist = new ArrayList<Visitas>();
	SQLiteDatabase db;
	
	Visitas_model mVisitas;
	Cursor cVisitas;
	Vendedores_model mVendedor;
	String id_vendedor;
	
	Config_durox config;
	ProgressDialog pDialog;
	
	Button btn_orden;
	Button btn_filtro;
	CharSequence orden;
	CharSequence filtro;
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.visitas_listview);
		config = new Config_durox();
		db = openOrCreateDatabase(config.getDatabase(), Context.MODE_PRIVATE, null);
		
		setTitle("Presupuestos - Selección de la visita");
        getActionBar().setIcon(R.drawable.menupresupuesto);
        		
		mVendedor = new Vendedores_model(db);
		id_vendedor = mVendedor.getID();
		
		orden = "";
	    filtro = "Razón Social";
	    visitas_lista(orden, filtro);
	   
	    btn_orden = (Button) findViewById(R.id.btn_ordenar);
	    btn_orden.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				PopupMenu popup = new PopupMenu(Presupuestos_Main.this, btn_orden);
				popup.getMenuInflater().inflate(R.menu.popup_visita, popup.getMenu());

				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					public boolean onMenuItemClick(MenuItem item) {
						orden = item.getTitle();
						visitas_lista(orden, filtro);
						Toast.makeText(Presupuestos_Main.this, "Orden : " + item.getTitle(), Toast.LENGTH_SHORT).show();
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
				PopupMenu popup = new PopupMenu(Presupuestos_Main.this, btn_filtro);
				popup.getMenuInflater().inflate(R.menu.popup_visita, popup.getMenu());

				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					public boolean onMenuItemClick(MenuItem item) {
						filtro = item.getTitle();
						visitas_lista(orden, filtro);
						Toast.makeText(Presupuestos_Main.this, "Filtro : " + item.getTitle(), Toast.LENGTH_SHORT).show();
		                        return true;
					}
				});
				popup.show();
			}
	    }); 
	}
	
	
	
	public void actualizar_visitas(View view) {
		pDialog = new ProgressDialog(this);
        pDialog.setMessage("Actualizando....");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
        
        Epocas_Update epocas = new Epocas_Update(db, this);
        epocas.actualizar_registros();
        
    	Visitas_Update visitas = new Visitas_Update(db, this);
    	visitas.actualizar_registros();
    	
    	Presupuestos_Update presupuestos = new Presupuestos_Update(db, this);
    	presupuestos.actualizar_registros();
    	
    	pDialog.dismiss();
    	visitas_lista(orden, filtro);
	}

	
	
	public void visitas_lista(CharSequence orden, final CharSequence filtro){
		mVisitas = new Visitas_model(db);
		cVisitas = mVisitas.getVisitas(orden);
			
		String[] id_visita = new String[cVisitas.getCount()];
		String[] nombre = new String[cVisitas.getCount()];
		String[] epoca = new String[cVisitas.getCount()];
		String[] fecha = new String[cVisitas.getCount()];
		String[] comentario = new String[cVisitas.getCount()];
		int[] foto = new int[cVisitas.getCount()];
		String[] id_front = new String[cVisitas.getCount()];
		int j = 0;
					
		if(cVisitas.getCount() > 0){
			while(cVisitas.moveToNext()){
				id_visita[j]	= cVisitas.getString(0);
				nombre[j]		= cVisitas.getString(1);
				epoca[j]		= cVisitas.getString(2);
				fecha[j]		= cVisitas.getString(3);
				id_front[j]		= cVisitas.getString(4);
				comentario[j]	= cVisitas.getString(5);
				foto[j]			= R.drawable.visitas; 
				j = j + 1;
			}				
			
			list = (ListView) findViewById(R.id.lvVisitas);
			arraylist.clear();

			for (int i = 0; i < nombre.length; i++) {
				Visitas wp = new Visitas(
						id_visita[i],
						nombre[i],
						epoca[i], 
						fecha[i],
						comentario[i], 
						foto[i],
						id_front[i]
				);
				
				arraylist.add(wp);
			}
			adapter = new Visitas_ListView(this, arraylist);
			list.setAdapter(adapter);
			
			editsearch = (EditText) findViewById(R.id.et_pBuscar);
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
	
	
	public void presupuestos_lista(View view){
		Intent intent = new Intent(Presupuestos_Main.this, Presupuestos_Lista.class);
		startActivity(intent);
	}
}
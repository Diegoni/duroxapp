package com.durox.app.Documentos;

import java.util.ArrayList;
import java.util.Locale;

import com.example.durox_app.R;
import com.durox.app.Config_durox;
import com.durox.app.MenuActivity;
import com.durox.app.Models.Documentos_model;

import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class Documentos_Main extends MenuActivity {

	Config_durox config;
	SQLiteDatabase db;
	Documentos_model mDocumentos;
	
	ProgressDialog pDialog;
	Cursor cDocumentos;
	
	ListView list;
	Documentos_ListView adapterd;
	EditText editsearch;
	
	String[] nombre;
	String[] direccion;
	int[] imagen;
	ArrayList<Documentos> arraylistd = new ArrayList<Documentos>();
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setTitle("Documentos - Lista");
        getActionBar().setIcon(R.drawable.menudocumentos);
		
		Intent intent = getIntent();
		String dNombre = intent.getStringExtra("nombre");
		
		if(dNombre != null){
			Toast.makeText(this, "Descargando "+dNombre, Toast.LENGTH_LONG).show();
		}
		
		config = new Config_durox();
		
		documentos_lista();
	}
	
	
	public void actualizar_documentos(View view) {
		pDialog = new ProgressDialog(this);
        pDialog.setMessage("Actualizando....");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
        
        Documentos_Update update = new Documentos_Update(db, this);
		update.actualizar_registros();
		
		pDialog.dismiss();
        
     	documentos_lista();
	}
	
	
	public void documentos_lista(){
		setContentView(R.layout.documentos_listview);
		db = openOrCreateDatabase(config.getDatabase(), Context.MODE_PRIVATE, null);
			
		mDocumentos = new Documentos_model(db);
		cDocumentos = mDocumentos.getRegistros();
			
		int j = 0;
					
		if(cDocumentos.getCount() > 0){
			String[] id_documento = new String[cDocumentos.getCount()];
			String[] nombre = new String[cDocumentos.getCount()];
			String[] epoca = new String[cDocumentos.getCount()];
			String[] fecha = new String[cDocumentos.getCount()];
			int[] foto = new int[cDocumentos.getCount()];
			
			while(cDocumentos.moveToNext()){
				id_documento[j] = cDocumentos.getString(0);
				nombre[j] = cDocumentos.getString(1);
				epoca[j] = cDocumentos.getString(2);
				fecha[j] = cDocumentos.getString(3);
				foto[j] = R.drawable.documentos; 
				j = j + 1;
			}				
			
			list = (ListView) findViewById(R.id.lvDocumentos);
			arraylistd.clear();

			for (int i = 0; i < nombre.length; i++) {
				Documentos wp = new Documentos(
						id_documento[i],
						nombre[i],
						epoca[i], 
						foto[i]
				);
					
				arraylistd.add(wp);
			}
			
			adapterd = new Documentos_ListView(this, arraylistd);
			list.setAdapter(adapterd);
			
			editsearch = (EditText) findViewById(R.id.search);
			editsearch.addTextChangedListener(new TextWatcher() {
				public void afterTextChanged(Editable arg0) {
					String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
					adapterd.filter(text);
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

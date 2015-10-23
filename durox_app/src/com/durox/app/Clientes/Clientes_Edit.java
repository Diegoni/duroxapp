package com.durox.app.Clientes;

import com.example.durox_app.R;

import java.util.ArrayList;
import java.util.List;

import com.durox.app.Config_durox;
import com.durox.app.MenuActivity;
import com.durox.app.Models.Clientes_model;
import com.durox.app.Models.Grupos_model;
import com.durox.app.Models.Iva_model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;



@SuppressLint("ShowToast")
public class Clientes_Edit extends MenuActivity {
	String id;
	Clientes_model mCliente;
	Config_durox config;
	SQLiteDatabase db;
	Cursor cursor;
		
	EditText et_razon_social;
	EditText et_nombre_fantasia;
	EditText et_nombre;
	EditText et_apellido;
	Spinner ac_grupo;
	Spinner ac_iva;
	EditText et_web;
	EditText et_cuit;
	Button btn_editar;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clientes_edit);
		
		et_razon_social = (EditText) findViewById(R.id.et_razon_social);
		et_nombre_fantasia = (EditText) findViewById(R.id.et_nombre_fantasia);
		et_nombre = (EditText) findViewById(R.id.et_nombre);
		et_apellido = (EditText) findViewById(R.id.et_apellido);
		et_web = (EditText) findViewById(R.id.et_web);
		et_cuit = (EditText) findViewById(R.id.et_cuit);
		btn_editar = (Button) findViewById(R.id.btn_editar);
		
		config = new Config_durox();
	    db = openOrCreateDatabase(config.getDatabase(), Context.MODE_PRIVATE, null);
		
		// Autocomplete Grupos
		ac_grupo = (Spinner) findViewById(R.id.ac_grupo);
		List<String> listGrupos = new ArrayList<String>();
		Grupos_model mGrupos = new Grupos_model(db);
		Cursor cursor_grupos = mGrupos.getRegistros();
		if(cursor_grupos.getCount() > 0){
			while(cursor_grupos.moveToNext()){
				listGrupos.add(cursor_grupos.getString(2));
			}	
		
		}
		ArrayAdapter<String> adapterGrupos = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,listGrupos);
		adapterGrupos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		ac_grupo.setAdapter(adapterGrupos);
		
		// Autocomplete Iva		
		ac_iva = (Spinner) findViewById(R.id.ac_iva);
		List<String> listIva = new ArrayList<String>();
		Iva_model mIva = new Iva_model(db);
		Cursor cursor_iva = mIva.getRegistros();
		if(cursor_iva.getCount() > 0){
			while(cursor_iva.moveToNext()){
				listIva.add(cursor_iva.getString(2));
			}	
		
		}
		ArrayAdapter<String> adapterIva = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,listIva);
		adapterIva.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		ac_iva.setAdapter(adapterIva);
		
		Intent i = getIntent();
		id = i.getStringExtra("id");
		
		if(id.equals("0")){
			btn_editar.setText("Agregar");
		} else {
			mCliente = new Clientes_model(db);
			cursor = mCliente.getRegistro(id);
			
			if(cursor.getCount() > 0){
				while(cursor.moveToNext()){
					et_razon_social.setText(cursor.getString(10));
					et_nombre_fantasia.setText(cursor.getString(9));
					et_nombre.setText(cursor.getString(2));
					et_apellido.setText(cursor.getString(3));
					ac_iva.setPrompt(cursor.getString(7));
					et_web.setText(cursor.getString(11));
					et_cuit.setText(cursor.getString(5));
					
					String compareValue = cursor.getString(6);
					if (!compareValue.equals(null)) {
					    int spinnerPosition = adapterGrupos.getPosition(compareValue);
					    ac_grupo.setSelection(spinnerPosition);
					}
					compareValue = cursor.getString(7);
					if (!compareValue.equals(null)) {
					    int spinnerPosition = adapterIva.getPosition(compareValue);
					    ac_iva.setSelection(spinnerPosition);
					}
				}
			}
		}
	}
	
	
	
	public void clientes_editar(View view) {
		String razon_social = et_razon_social.getText().toString();
		String nombre_fantasia = et_nombre_fantasia.getText().toString();
		String nombre = et_nombre.getText().toString();
		String apellido = et_apellido.getText().toString();
		String grupo = ((Spinner)findViewById(R.id.ac_grupo)).getSelectedItem().toString();
		String iva = ((Spinner)findViewById(R.id.ac_iva)).getSelectedItem().toString();
		String web = et_web.getText().toString();
		String cuit = et_cuit.getText().toString();
		String id_grupo = "0";
		String id_iva = "0";
				
		Grupos_model mGrupos = new Grupos_model(db);
		Cursor cursor_grupos = mGrupos.getID(grupo);
		int j = 0;
		if(cursor_grupos.getCount() > 0){
			while(cursor_grupos.moveToNext()){
				id_grupo = cursor_grupos.getString(0);
				j = j + 1;
    		}		
		} 
		
		Iva_model mIva = new Iva_model(db);
		Cursor cursor_iva = mIva.getID(iva);
		j = 0;
		if(cursor_iva.getCount() > 0){
			while(cursor_iva.moveToNext()){
				id_iva = cursor_iva.getString(0);
				j = j + 1;
    		}		
		}
		
		if(id_grupo.equals("0")){
			Toast.makeText(this, config.msjNoRegistro("grupo"), Toast.LENGTH_SHORT).show();
			//ac_grupo.setText("");
			ac_grupo.setFocusable(true);			
		}else{
			if(id_iva.equals("0")){
				Toast.makeText(this, config.msjNoRegistro("iva"), Toast.LENGTH_SHORT).show();
				//ac_iva.setText("");
				ac_iva.setFocusable(true);
			}else{
				mCliente = new Clientes_model(db);
				if(id.equals("0")){
					Log.e("Paso ", "insert cliente");
					
					Cursor cRazon = mCliente.getID(razon_social);
					
					if(cRazon.getCount() > 0){
						et_razon_social.setFocusable(true);
						Toast.makeText(this, config.msjDuplicado(), Toast.LENGTH_SHORT).show();
					}else{
						mCliente.insert(
							"0",			// id_back, 
							nombre, 
							apellido, 
							"1",			// modificado, 
							cuit, 
							id_grupo,		// id_grupo_cliente, 
							id_iva, 
							"0",			// imagen, 
							nombre_fantasia, 
							razon_social, 
							web, 
							"0",			// date_add, 
							"0",			// date_upd, 
							"0",			// eliminado, 
							"0",			// user_add, 
							"0");			// user_upd);
						
						Toast.makeText(this, config.msjOkInsert(), Toast.LENGTH_SHORT).show();
				    	Intent intentClientes = new Intent(this, Clientes_Main.class);
						startActivity(intentClientes);
					}
					
				}else{	
					mCliente.edit(
						id, 
						razon_social, 
						nombre_fantasia, 
						nombre, 
						apellido, 
						id_grupo, 
						id_iva, 
						web, 
						cuit);
					
					Toast.makeText(this, config.msjOkUpdate(), Toast.LENGTH_SHORT).show();
			    	Intent intentClientes = new Intent(this, Clientes_Main.class);
					startActivity(intentClientes);
				}
			}
		}		
	}
	
	
	
	public void cancelar(View view) {
		Intent intentClientes = new Intent(this, Clientes_Main.class);
		startActivity(intentClientes);
	}
}
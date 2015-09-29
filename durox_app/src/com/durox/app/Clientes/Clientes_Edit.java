package com.durox.app.Clientes;

import com.example.durox_app.R;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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
	AutoCompleteTextView ac_grupo;
	AutoCompleteTextView ac_iva;
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
		ac_grupo = (AutoCompleteTextView) findViewById(R.id.ac_grupo);
		ac_iva = (AutoCompleteTextView) findViewById(R.id.ac_iva);
		et_web = (EditText) findViewById(R.id.et_web);
		et_cuit = (EditText) findViewById(R.id.et_cuit);
		btn_editar = (Button) findViewById(R.id.btn_editar);
		
		config = new Config_durox();
	    db = openOrCreateDatabase(config.getDatabase(), Context.MODE_PRIVATE, null);
		
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
					ac_grupo.setText(cursor.getString(6));
					ac_iva.setText(cursor.getString(7));
					et_web.setText(cursor.getString(11));
					et_cuit.setText(cursor.getString(5));
				}
			}
		}
				
		// Autocomplete Grupos
		Grupos_model mGrupos = new Grupos_model(db);
		Cursor cursor_grupos = mGrupos.getRegistros();
		int cantidad = cursor_grupos.getCount();
		
		String[] grupos_array;
		grupos_array = new String[cantidad];
		
		int j = 0;
				
		if(cursor_grupos.getCount() > 0){
			while(cursor_grupos.moveToNext()){
				grupos_array[j] = cursor_grupos.getString(2);
				
    			j = j + 1;
    		}		
		} else {
			Toast.makeText(this, config.msjNoRegistro("grupos"), Toast.LENGTH_LONG).show();
		}
		
		ArrayAdapter<String> adapterGrupos = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, grupos_array);
		ac_grupo.setThreshold(1);
		ac_grupo.setAdapter(adapterGrupos);
		
		// Autocomplete Iva		
		Iva_model mIva = new Iva_model(db);
		Cursor cursor_iva = mIva.getRegistros();
		int cantidad_iva = cursor_iva.getCount();
		
		String[] iva_array;
		iva_array = new String[cantidad_iva];
		
		int k = 0;
				
		if(cursor_iva.getCount() > 0){
			while(cursor_iva.moveToNext()){
				iva_array[k] = cursor_iva.getString(2);
				
    			k = k + 1;
    		}		
		} else {
			Toast.makeText(this, config.msjNoRegistro("iva"), Toast.LENGTH_LONG).show();
		}
		
		ArrayAdapter<String> adapterIva = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, iva_array);
		ac_iva.setThreshold(1);
		ac_iva.setAdapter(adapterIva);
	}
	
	
	
	public void clientes_editar(View view) {
		String razon_social = et_razon_social.getText().toString();
		String nombre_fantasia = et_nombre_fantasia.getText().toString();
		String nombre = et_nombre.getText().toString();
		String apellido = et_apellido.getText().toString();
		String grupo = ac_grupo.getText().toString();
		String iva = ac_iva.getText().toString();
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
			ac_grupo.setText("");
			ac_grupo.setFocusable(true);			
		}else{
			if(id_iva.equals("0")){
				Toast.makeText(this, config.msjNoRegistro("iva"), Toast.LENGTH_SHORT).show();
				ac_iva.setText("");
				ac_iva.setFocusable(true);
			}else{
				mCliente = new Clientes_model(db);
				if(id.equals("0")){
					Log.e("Paso ", "insert cliente");
					
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
				}
				
				Toast.makeText(this, config.msjOkUpdate(), Toast.LENGTH_SHORT).show();
		    	Intent intentClientes = new Intent(this, Clientes_Main.class);
				startActivity(intentClientes);
			}
		}		
	}
	
	
	
	public void cancelar(View view) {
		Intent intentClientes = new Intent(this, Clientes_Main.class);
		startActivity(intentClientes);
	}
}
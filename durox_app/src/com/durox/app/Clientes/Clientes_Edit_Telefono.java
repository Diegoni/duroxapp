package com.durox.app.Clientes;

import com.example.durox_app.R;
import com.durox.app.Config_durox;
import com.durox.app.MenuActivity;
import com.durox.app.Models.Clientes_model;
import com.durox.app.Models.Telefonos_clientes_model;
import com.durox.app.Models.Tipos_model;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



@SuppressLint("ShowToast")
public class Clientes_Edit_Telefono extends MenuActivity {
	String id;
	Telefonos_clientes_model mTelefono;
	Config_durox config;
	SQLiteDatabase db;
	Cursor cursor;
	
	EditText et_telefono;
	EditText et_cod_area;
	AutoCompleteTextView ac_tipo;
	TextView razon_social;
	
	String tipo;
	String telefono;
	String cod_area;
	String id_cliente;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);
		
		config = new Config_durox();
		db = openOrCreateDatabase(config.getDatabase(), Context.MODE_PRIVATE, null);
		
		Intent i = getIntent();
		
		id = i.getStringExtra("id");
		telefono = i.getStringExtra("telefono");
		cod_area = i.getStringExtra("cod_area");
		tipo = i.getStringExtra("tipo");
		id_cliente = i.getStringExtra("id_cliente");
		
		et_telefono = (EditText) findViewById(R.id.et_telefono);
		et_cod_area = (EditText) findViewById(R.id.et_cod_area);
		ac_tipo = (AutoCompleteTextView) findViewById(R.id.ac_tipo);
		razon_social = (TextView) findViewById(R.id.txt_razon_social);
		
		et_telefono.setText(telefono);
		et_cod_area.setText(cod_area);
		ac_tipo.setText(tipo);
		
		// Autocomplete Grupos
		Tipos_model mTipos = new Tipos_model(db);
		Cursor cursor_tipos = mTipos.getRegistros();
		int cantidad = cursor_tipos.getCount();
				
		String[] tipos_array;
		tipos_array = new String[cantidad];
				
		int j = 0;
						
		if(cursor_tipos.getCount() > 0){
			while(cursor_tipos.moveToNext()){
				tipos_array[j] = cursor_tipos.getString(1);
					
		    	j = j + 1;
		    }		
		} else {
			Toast.makeText(this, config.msjNoRegistro("tipos"), Toast.LENGTH_LONG).show();
		}
				
		ArrayAdapter<String> adapterTipos = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tipos_array);
		ac_tipo.setThreshold(1);
		ac_tipo.setAdapter(adapterTipos);
		
		//Completar cliente
		
		Clientes_model mCliente = new Clientes_model(db);
		Cursor cliente_cursor = mCliente.getRegistro(id_cliente);
		
		if(cliente_cursor.getCount() > 0){
			while(cliente_cursor.moveToNext()){
				razon_social.setText(cliente_cursor.getString(10));
			}
		}
	}
	
	
	public void telefonos_editar(View view) {
		String txt_telefono = et_telefono.getText().toString();
		String txt_cod_area = et_cod_area.getText().toString();
		String txt_id_tipo = ac_tipo.getText().toString();
		String id_tipo = "0";
		
		Log.e("Paso ", "id_tipo "+txt_id_tipo);
				
		Tipos_model mTipo = new Tipos_model(db);
		Cursor cursor_tipos = mTipo.getID(txt_id_tipo);
		int j = 0;
		
		
		if(cursor_tipos.getCount() > 0){
			while(cursor_tipos.moveToNext()){
				id_tipo = cursor_tipos.getString(0);
				j = j + 1;
    		}		
		} 
		
		if(id_tipo.equals("0")){
			Toast.makeText(this, config.msjNoRegistro("tipo"), Toast.LENGTH_SHORT).show();
			ac_tipo.setText("");
			ac_tipo.setFocusable(true);			
		}else{
			mTelefono = new Telefonos_clientes_model(db);
			mTelefono.edit(
				id, 
				txt_telefono, 
				txt_cod_area, 
				id_tipo
			);
				
			Toast.makeText(this, config.msjOkUpdate(), Toast.LENGTH_SHORT).show();
		    Intent intentClientes = new Intent(this, Clientes_Main.class);
			startActivity(intentClientes);
		}
			
	}
	
	
	public void cancelar(View view) {
		Intent intentClientes = new Intent(this, Clientes_Main.class);
		startActivity(intentClientes);
	}
	
}
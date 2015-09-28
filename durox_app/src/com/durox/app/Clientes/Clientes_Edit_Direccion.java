package com.durox.app.Clientes;

import com.example.durox_app.R;
import com.durox.app.Config_durox;
import com.durox.app.MenuActivity;
import com.durox.app.Models.Clientes_model;
import com.durox.app.Models.Departamentos_model;
import com.durox.app.Models.Direcciones_clientes_model;
import com.durox.app.Models.Provincias_model;
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
public class Clientes_Edit_Direccion extends MenuActivity {
	protected static final Context Context = null;
	String id;
	Direcciones_clientes_model mDireccion;
	Config_durox config;
	SQLiteDatabase db;
	Cursor cursor;
	
	EditText et_direccion;
	AutoCompleteTextView et_departamento;
	AutoCompleteTextView ac_provincia;
	AutoCompleteTextView ac_tipo;
	TextView razon_social;
	
	String tipo;
	String direccion;
	String departamento;
	String id_cliente;
	String id_provincia;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main3);
		
		config = new Config_durox();
		db = openOrCreateDatabase(config.getDatabase(), android.content.Context.MODE_PRIVATE, null);
		
		Intent i = getIntent();
		
		id = i.getStringExtra("id");
		direccion = i.getStringExtra("direccion");
		departamento = i.getStringExtra("departamento");
		tipo = i.getStringExtra("tipo");
		id_cliente = i.getStringExtra("id_cliente");
		
		et_direccion = (EditText) findViewById(R.id.et_direccion);
		et_departamento = (AutoCompleteTextView) findViewById(R.id.et_departamento);
		ac_provincia = (AutoCompleteTextView) findViewById(R.id.ac_provincia);
		ac_tipo = (AutoCompleteTextView) findViewById(R.id.ac_tipo);
		razon_social = (TextView) findViewById(R.id.txt_razon_social);
		
		et_direccion.setText(direccion);
		et_departamento.setText(departamento);
		ac_tipo.setText(tipo);
		
		id_provincia = "0";
		Direcciones_clientes_model mDirecciones = new Direcciones_clientes_model(db);
		Cursor cDirecciones = mDirecciones.getProvincia(id);
		int j = 0;
		if(cDirecciones.getCount() > 0){
			while(cDirecciones.moveToNext()){
				ac_provincia.setText(cDirecciones.getString(1));
				id_provincia = cDirecciones.getString(0);
				j = j + 1;
		    }		
		} 
		
		autoDepartamentos(id_provincia);
				
		// Autocomplete Grupos
		Tipos_model mTipos = new Tipos_model(db);
		Cursor cursor_tipos = mTipos.getRegistros();
		int cantidad = cursor_tipos.getCount();
				
		String[] tipos_array;
		tipos_array = new String[cantidad];
				
		j = 0;
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
		
		
		
		// Autocomplete Departamentos
		Provincias_model mProvincias = new Provincias_model(db);
		Cursor cursor_provincias = mProvincias.getRegistros();
		cantidad = cursor_provincias.getCount();
						
		String[] provincias_array;
		provincias_array = new String[cantidad];
								
		j = 0;
		if(cursor_provincias.getCount() > 0){
			while(cursor_provincias.moveToNext()){
				provincias_array[j] = cursor_provincias.getString(2);
								
				j = j + 1;
			}		
		} else {
			Toast.makeText(this, config.msjNoRegistro("provincias"), Toast.LENGTH_LONG).show();
		}
								
		ArrayAdapter<String> adapterProvincias = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, provincias_array);
		ac_provincia.setThreshold(1);
		ac_provincia.setAdapter(adapterProvincias);
		
		//Completar cliente
		
		Clientes_model mCliente = new Clientes_model(db);
		Cursor cliente_cursor = mCliente.getRegistro(id_cliente);
		
		if(cliente_cursor.getCount() > 0){
			while(cliente_cursor.moveToNext()){
				razon_social.setText(cliente_cursor.getString(10));
			}
		}
		
		
		
		ac_provincia.setOnFocusChangeListener( new View.OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				String text = ac_provincia.getText().toString();
				Provincias_model mProvincias = new Provincias_model(db);
				Cursor cursor_nuevo = mProvincias.getID(text);
				String id_nuevo = "0";
				
				int j = 0;
				if(cursor_nuevo.getCount() > 0){
					while(cursor_nuevo.moveToNext()){
						id_nuevo = cursor_nuevo.getString(0);
								
				    	j = j + 1;
				    }		
				}
				
				if(id_nuevo.equals("0")){
					ac_provincia.setText("");
					ac_provincia.setFocusable(true);
				}else if(id_nuevo.equals(id_provincia)){
					Log.e("Paso ", "son iguales ");					
				}else{
					et_departamento.setText("");
					et_departamento.setFocusable(true);
					id_provincia = id_nuevo;
					
					autoDepartamentos(id_nuevo);
				}
			}
		});
		
	}
	
	public void autoDepartamentos(String id_provincia){
		// Autocomplete Departamentos
		Departamentos_model mDepartamentos = new Departamentos_model(db);
		Cursor cursor_dep = mDepartamentos.getRegistros(id_provincia);
		int cantidad = cursor_dep.getCount();
						
		String[] dep_array;
		dep_array = new String[cantidad];
								
		int	j = 0;
		if(cursor_dep.getCount() > 0){
			while(cursor_dep.moveToNext()){
				dep_array[j] = cursor_dep.getString(2);
							
		    	j = j + 1;
		    }		
		} else {
			Toast.makeText(this, config.msjNoRegistro("departamentos"), Toast.LENGTH_LONG).show();
		}
								
		ArrayAdapter<String> adapterDep = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dep_array);
		et_departamento.setThreshold(1);
		et_departamento.setAdapter(adapterDep);
	}
	
	
	public void direcciones_editar(View view) {
		String txt_direccion = et_direccion.getText().toString();
		String txt_departamento = et_departamento.getText().toString();
		String txt_provincia = ac_provincia.getText().toString();
		String txt_id_tipo = ac_tipo.getText().toString();
		String id_departamento = "0";
		String id_provincia = "0";
		String id_tipo = "0";
		
		Departamentos_model mDepartamento = new Departamentos_model(db);
		Cursor cursor_dep = mDepartamento.getID(txt_departamento);
		int j = 0;
				
		if(cursor_dep.getCount() > 0){
			while(cursor_dep.moveToNext()){
				id_departamento = cursor_dep.getString(0);
				j = j + 1;
    		}		
		} 
		
		
		Provincias_model mProvincia = new Provincias_model(db);
		Cursor cursor_pro = mProvincia.getID(txt_provincia);
		 j = 0;
				
		if(cursor_pro.getCount() > 0){
			while(cursor_pro.moveToNext()){
				id_provincia = cursor_pro.getString(0);
				j = j + 1;
    		}		
		} 
				
		
		Tipos_model mTipo = new Tipos_model(db);
		Cursor cursor_tipos = mTipo.getID(txt_id_tipo);
		j = 0;
				
		if(cursor_tipos.getCount() > 0){
			while(cursor_tipos.moveToNext()){
				id_tipo = cursor_tipos.getString(0);
				j = j + 1;
    		}		
		} 
		
		if(id_tipo.equals("0") || id_departamento.equals("0") || id_provincia.equals("0")){
			if(id_tipo.equals("0")){
				Toast.makeText(this, config.msjNoRegistro("tipo"), Toast.LENGTH_SHORT).show();
				ac_tipo.setText("");
				ac_tipo.setFocusable(true);
			}else if(id_departamento.equals("0")){
				Toast.makeText(this, config.msjNoRegistro("departamento"), Toast.LENGTH_SHORT).show();
				et_departamento.setText("");
				et_departamento.setFocusable(true);
			}else{
				Toast.makeText(this, config.msjNoRegistro("provincia"), Toast.LENGTH_SHORT).show();
				ac_provincia.setText("");
				ac_provincia.setFocusable(true);
			}
		}else{
			mDireccion = new Direcciones_clientes_model(db);
			mDireccion.edit(
				id, 
				txt_direccion, 
				id_departamento, 
				id_provincia,
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
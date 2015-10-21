package com.durox.app.Visitas;

import com.example.durox_app.R;

import java.util.ArrayList;
import java.util.List;

import com.durox.app.Config_durox;
import com.durox.app.MainActivity;
import com.durox.app.MenuActivity;
import com.durox.app.Models.Clientes_model;
import com.durox.app.Models.Epocas_model;
import com.durox.app.Models.Vendedores_model;
import com.durox.app.Models.Visitas_model;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

public class Visitas_Main extends MenuActivity {
	AutoCompleteTextView auto_cliente;
	Spinner auto_epoca;
	RatingBar ebValoracion;
	DatePicker etfecha;
	EditText etComentario;
	String[] c_nombre;
	String[] epocas;
	SQLiteDatabase db;
	
	String truncate;
	String sql;
	Cursor c;
	int cantidad;
	int j;
	Clientes_model mClientes;
	Visitas_model mVisitas;
	Epocas_model mEpocas;
	
	Config_durox config;


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.visitas_main);
		
		setTitle("Visitas - Carga de nueva visita");
        getActionBar().setIcon(R.drawable.menuvisitas);
		
		cargar_vista();
	}

	
	public void cargar_vista(){
		auto_cliente = (AutoCompleteTextView) findViewById(R.id.autoClientes);
		etfecha = (DatePicker) findViewById(R.id.dp_Fecha);
		etComentario = (EditText) findViewById(R.id.txtcomentario);
		ebValoracion = (RatingBar) findViewById(R.id.ebValoracion);
		
		config = new Config_durox();
		
		db = openOrCreateDatabase(config.getDatabase(), Context.MODE_PRIVATE, null);
		
		mClientes = new Clientes_model(db);
		c = mClientes.getClientesVisitas();
		cantidad = c.getCount();
		c_nombre = new String[cantidad];
		j = 0;
		if(c.getCount() > 0){
			while(c.moveToNext()){
				c_nombre[j] = c.getString(10);
				j = j + 1;
    		}		
		} else {
			Toast.makeText(this, config.msjNoRegistros("clientes"), Toast.LENGTH_SHORT).show();
		}
				
		
		
		Spinner auto_epoca = (Spinner) findViewById(R.id.autoEpocas);
		List<String> listEpocas = new ArrayList<String>();
		mEpocas = new Epocas_model(db);
		Cursor cEpocas = mEpocas.getRegistros();
		if(cEpocas.getCount() > 0){
			while(cEpocas.moveToNext()){
				listEpocas.add(cEpocas.getString(2));
			}	
		
		}
		ArrayAdapter<String> adapterEpocas = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,listEpocas);
		adapterEpocas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		auto_epoca.setAdapter(adapterEpocas);
		
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, c_nombre);
		auto_cliente.setThreshold(1);
		auto_cliente.setAdapter(adapter);
		
		
		Intent i = getIntent();
		String nombre = i.getStringExtra("nombre");
		
		if(nombre != ""){
			auto_cliente.setText(nombre);
		}	
		
	}
	
	
	public void guardar(View view){
		Log.e("PASO", "1");
		
		String razon_social = auto_cliente.getText().toString();
		//String epoca = auto_epoca.getSelectedItem().toString();
		
		String epoca = ((Spinner)findViewById(R.id.autoEpocas)).getSelectedItem().toString();
		
		Log.e("PASO", "2");
		
		int mes = etfecha.getMonth();
		mes = mes + 1;
		String fecha = etfecha.getYear()+"/"+mes+"/"+etfecha.getDayOfMonth();
		String comentario = etComentario.getText().toString();
		float valoracion2 = ebValoracion.getRating();
		
		Log.e("PASO", "3");
		
		Log.e("Fecha ","dia "+etfecha.getDayOfMonth());
		Log.e("Fecha ","mes "+etfecha.getMonth());
		Log.e("Fecha ","año "+etfecha.getYear());
		
		db = openOrCreateDatabase(config.getDatabase(), Context.MODE_PRIVATE, null);
		
		Cursor cCliente = mClientes.getID(razon_social);
		
		if(cCliente.getCount() > 0){
			
			while(cCliente.moveToNext()){
				
				mEpocas = new Epocas_model(db);
				Cursor cEpoca = mEpocas.getID(epoca);
				Vendedores_model mVendedor = new Vendedores_model(db);
				Cursor cVendedor = mVendedor.getRegistros();
				String IDvendedor = "0";
				
				if(cVendedor.getCount() > 0){
					while(cVendedor.moveToNext()){
						IDvendedor = cVendedor.getString(1);
					}
				}
				
				if(cEpoca.getCount() > 0){
					while(cEpoca.moveToNext()){
						String id_visita = "0";
						String id_cliente = cCliente.getString(0);
						String descripcion = comentario;
						String id_vendedor = IDvendedor;
						String id_epoca_visita = cEpoca.getString(0);
						String valoracion = Float.toString(valoracion2);
						mVisitas = new Visitas_model(db);
						
						mVisitas.insert(
					 			id_visita,
					 			id_vendedor,
					 			id_cliente,
					 			descripcion,
					 			id_epoca_visita,
					 			valoracion,
					 			fecha,
					 			null,
					 			null,
					 			null,
					 			null,
					 			null,
					 			null,
					 			null
					 		);
						
						Toast.makeText(this, config.msjOkInsert(), Toast.LENGTH_SHORT).show();
						
						Intent intent = new Intent(Visitas_Main.this, MainActivity.class);
						startActivity(intent);
					}
				}else{
					Toast.makeText(this, config.msjNoRegistro("epoca"), Toast.LENGTH_LONG).show();
				}
			}	
		} else {
			Toast.makeText(this, config.msjNoRegistro("cliente"), Toast.LENGTH_LONG).show();
		}
	}
	
	public void actualizar_visitas(View view){
		Intent intent = new Intent(Visitas_Main.this, Visitas_Enviar.class);
		startActivity(intent);
	}
	
	
	public void actualizar_epocas(View view) {
 		Epocas_Update epocas = new Epocas_Update(db, this);
 		epocas.actualizar_registros();
 		cargar_vista();
 	}
}

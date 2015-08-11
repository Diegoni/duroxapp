package com.durox.app.Visitas;

import com.androidbegin.filterlistviewimg.R;
import com.androidbegin.filterlistviewimg.R.id;
import com.androidbegin.filterlistviewimg.R.layout;
import com.durox.app.MainActivity;
import com.durox.app.Models.Clientes_model;
import com.durox.app.Models.Epocas_model;
import com.durox.app.Models.Visitas_model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

public class Visitas_Main extends Activity {
	AutoCompleteTextView auto_cliente;
	AutoCompleteTextView auto_epoca;
	RatingBar ebValoracion;
	EditText etfecha;
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


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);
		
		setContentView(R.layout.visitas_main);
		
		auto_cliente = (AutoCompleteTextView) findViewById(R.id.autoClientes);
		auto_epoca = (AutoCompleteTextView) findViewById(R.id.autoEpocas);
		etfecha = (EditText) findViewById(R.id.etFecha);
		ebValoracion = (RatingBar) findViewById(R.id.ebValoracion);
		
		db = openOrCreateDatabase("Duroxapp", Context.MODE_PRIVATE, null);
		
		mClientes = new Clientes_model(db);
		
		c = mClientes.getRegistros();
		
		cantidad = c.getCount();
		
		c_nombre = new String[cantidad];
		
		j = 0;
				
		if(c.getCount() > 0){
			while(c.moveToNext()){
				c_nombre[j] = c.getString(1);
				j = j + 1;
    		}		
		}
		else{
			Toast.makeText(this, "No hay clientes cargados", Toast.LENGTH_SHORT).show();
		}
		
		/*
		sql = "INSERT INTO `epocas_visitas` (`epoca`) VALUES"
		+ "('Verano'),"
		+ "('Pre Cosecha');";
		*/
				
		mEpocas = new Epocas_model(db);
		
		c = mEpocas.getRegistros();
		
		cantidad = c.getCount();
		
		epocas = new String[cantidad];
		
		j = 0;
				
		if(c.getCount() > 0){
			while(c.moveToNext()){
				epocas[j] = c.getString(0);
    			j = j + 1;
    		}		
		}
		
		ArrayAdapter<String> adapterProductos = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, epocas);
		auto_epoca.setThreshold(1);
		auto_epoca.setAdapter(adapterProductos);
		
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, c_nombre);
		auto_cliente.setThreshold(1);
		auto_cliente.setAdapter(adapter);
		
		
		Intent i = getIntent();
		String nombre = i.getStringExtra("nombre");
		
		if(nombre != ""){
			auto_cliente.setText(nombre);
		}	
	}

	
	public void agregar_comentarios(View view) {
		String nombre = auto_cliente.getText().toString();
		String epoca = auto_epoca.getText().toString();
		String fecha = etfecha.getText().toString();
		float valoracion = ebValoracion.getRating();
		
		Intent intent = new Intent(Visitas_Main.this, Visitas_Detalle.class);
		
		intent.putExtra("nombre", nombre);
		intent.putExtra("epoca", epoca);
		intent.putExtra("fecha", fecha);
		intent.putExtra("valoracion", valoracion);
		
		startActivity(intent);
	}
	
	
	public void guardar(View view){
		String nombre = auto_cliente.getText().toString();
		String epoca = auto_epoca.getText().toString();
		String fecha = etfecha.getText().toString();
		float valoracion = ebValoracion.getRating();
		
		db = openOrCreateDatabase("Duroxapp", Context.MODE_PRIVATE, null);
		
		mVisitas = new Visitas_model(db);
		
		mVisitas.insert(nombre, epoca, fecha, valoracion, null);
		
		Toast.makeText(this, "El registro se ha guardado con éxito", Toast.LENGTH_SHORT).show();
		
		Intent intent = new Intent(Visitas_Main.this, MainActivity.class);
		startActivity(intent);
	}
	
	public void actualizar_visitas(View view){
		Intent intent = new Intent(Visitas_Main.this, Visitas_Enviar.class);
		startActivity(intent);
	}

}

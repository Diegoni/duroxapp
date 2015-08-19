package com.durox.app.Visitas;

import com.example.durox_app.R;
import com.example.durox_app.R.id;
import com.example.durox_app.R.layout;
import com.durox.app.Config_durox;
import com.durox.app.MainActivity;
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
import android.widget.TextView;
import android.widget.Toast;

public class Visitas_Detalle extends Activity {
	
	SQLiteDatabase db;
	
	String epoca;
	String nombre;
	String fecha;
	String comentario;
	float valoracion;
	EditText etComentario;
	
	String sql;
	
	Visitas_model mVisitas;
	
	Config_durox config;

	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.visitas_detalle);
		
		Intent intent = getIntent();
		nombre = intent.getStringExtra("nombre");
		epoca = intent.getStringExtra("epoca");
		fecha = intent.getStringExtra("fecha");
		valoracion = intent.getFloatExtra("valoracion", 3);
		
		final TextView txtNombre = (TextView) findViewById(R.id.txtNombre);
		final TextView txtEpoca = (TextView) findViewById(R.id.txtEpoca);
		final TextView txtFecha = (TextView) findViewById(R.id.txtFecha);
		
		txtNombre.setText(nombre);
		txtEpoca.setText(epoca);
		txtFecha.setText(fecha);
	}
	
	
	public void guardar(View view){
		EditText etComentario = (EditText) findViewById(R.id.etComentario);
		comentario = etComentario.getText().toString();	
		
		config = new Config_durox();
		
		db = openOrCreateDatabase(config.getDatabase(), Context.MODE_PRIVATE, null);
		
		mVisitas = new Visitas_model(db);
		
		String id_visita = nombre;
		String id_vendedor = nombre;
		String id_cliente = nombre;
		String descripcion = nombre;
		String id_epoca_visita = nombre;
		String valoracion = nombre;
		String fecha = nombre;
		String id_origen = nombre;
		String visto = nombre;
		String date_add = nombre;
		String date_upd = nombre;
		String eliminado = nombre;
		String user_add = nombre;
		String user_upd = nombre;
		
		mVisitas.insert(
 				id_visita,
 				id_vendedor,
 				id_cliente,
 				descripcion,
 				id_epoca_visita,
 				valoracion,
 				fecha,
 				id_origen,
 				visto,
 				date_add,
 				date_upd,
 				eliminado,
 				user_add,
 				user_upd
 			);
		
		Toast.makeText(this, config.msjOkInsert(), Toast.LENGTH_LONG).show();
		
		Intent intent = new Intent(Visitas_Detalle.this, MainActivity.class);
		startActivity(intent);
	}
}

package com.durox.app.Visitas;

import com.androidbegin.filterlistviewimg.R;
import com.androidbegin.filterlistviewimg.R.id;
import com.androidbegin.filterlistviewimg.R.layout;
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
		
		db = openOrCreateDatabase("Duroxapp", Context.MODE_PRIVATE, null);
		
		mVisitas = new Visitas_model(db);
		
		mVisitas.insert(nombre, epoca, fecha, valoracion, comentario);
		
		Toast.makeText(this, "El registro fue guardado con éxito", Toast.LENGTH_LONG).show();
		
		Intent intent = new Intent(Visitas_Detalle.this, MainActivity.class);
		startActivity(intent);
	}
}

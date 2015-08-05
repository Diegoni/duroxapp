package com.durox.app.Visitas;

import com.androidbegin.filterlistviewimg.R;
import com.androidbegin.filterlistviewimg.R.id;
import com.androidbegin.filterlistviewimg.R.layout;
import com.durox.app.MainActivity;

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
	EditText etComentario;
	
	String sql;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.visitas_detalle);
		
		Intent intent = getIntent();
		nombre = intent.getStringExtra("nombre");
		epoca = intent.getStringExtra("epoca");
		fecha = intent.getStringExtra("fecha");
		EditText etComentario = (EditText) findViewById(R.id.etCantidad);
		
		final TextView txtNombre = (TextView) findViewById(R.id.txtNombre);
		final TextView txtEpoca = (TextView) findViewById(R.id.txtEpoca);
		final TextView txtFecha = (TextView) findViewById(R.id.txtFecha);
		
		txtNombre.setText(nombre);
		txtEpoca.setText(epoca);
		txtFecha.setText(fecha);
		
		comentario = etComentario.getText().toString();			
	}
	
	public void guardar(View view){
		
		db = openOrCreateDatabase("Duroxapp", Context.MODE_PRIVATE, null);
		
		db.execSQL("CREATE TABLE IF NOT EXISTS visitas2("
				+ "nombre VARCHAR, "
				+ "epoca VARCHAR, "
				+ "fecha VARCHAR, "
				+ "comentario VARCHAR "
				+ ");");

		
		sql = "INSERT INTO `visitas2` (`nombre`, `epoca`, `fecha`, `comentario`) VALUES"
	    		+ "('" + nombre + "', '" + epoca + "', '" + fecha + "', '" + comentario + "');";
				
		db.execSQL(sql);
		
		Toast.makeText(this, "El registro se ha guardado con éxito", Toast.LENGTH_SHORT).show();
		
		Intent intent = new Intent(Visitas_Detalle.this, MainActivity.class);
		startActivity(intent);
	}



	
}

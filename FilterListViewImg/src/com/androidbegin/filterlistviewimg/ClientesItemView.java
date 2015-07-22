package com.androidbegin.filterlistviewimg;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ClientesItemView extends Activity {
	
	// Declare Variables
	ListView list;
	ArrayList<Productos> arraylist = new ArrayList<Productos>();
	
	// Declare Variables
	TextView txtnombre;
	TextView txtdireccion;
	ImageView imgimagen;
	
	String nombre;
	String direccion;
	int imagen;
	
	int[] imagen_array;
	
	SQLiteDatabase db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clientesitemview);
		// Get the intent from ListViewAdapter
		Intent i = getIntent();
		
		// Get the results of nombre
		nombre = i.getStringExtra("nombre");
		
		// Get the results of direccion
		direccion = i.getStringExtra("direccion");
		
		// Get the results of flag
		imagen = i.getIntExtra("imagen", imagen);

		// Locate the TextViews in singleitemview.xml
		txtnombre = (TextView) findViewById(R.id.nombre);
		txtdireccion = (TextView) findViewById(R.id.direccion);
		

		// Locate the ImageView in singleitemview.xml
		imgimagen = (ImageView) findViewById(R.id.imagen);
		

		// Load the results into the TextViews
		txtnombre.setText(nombre);
		txtdireccion.setText(direccion);

		// Load the image into the ImageView
		imgimagen.setImageResource(imagen);

	}
}
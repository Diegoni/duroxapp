package com.androidbegin.filterlistviewimg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Clientes_ItemView extends Activity 
{
	// Declare Variables
	TextView txtnombre;
	TextView txtdireccion;
	ImageView imgimagen;
	
	String nombre;
	String direccion;
	int imagen;
	
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clientes_itemview);
		// Intent para ClientesListView
		Intent i = getIntent();
		
		// Traigo los resultados 
		nombre = i.getStringExtra("nombre");
		direccion = i.getStringExtra("direccion");
		imagen = i.getIntExtra("imagen", imagen);
		
		// Locate the TextViews in singleitemview.xml
		txtnombre = (TextView) findViewById(R.id.nombre);
		txtdireccion = (TextView) findViewById(R.id.direccion);
		imgimagen = (ImageView) findViewById(R.id.imagen);
		
		// Load the results into the TextViews
		txtnombre.setText(nombre);
		txtdireccion.setText(direccion);
		imgimagen.setImageResource(imagen);
	}
}
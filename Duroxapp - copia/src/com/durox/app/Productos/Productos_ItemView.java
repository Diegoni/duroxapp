package com.durox.app.Productos;

import com.androidbegin.filterlistviewimg.R;
import com.androidbegin.filterlistviewimg.R.id;
import com.androidbegin.filterlistviewimg.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Productos_ItemView extends Activity 
{
	// Declare Variables
	TextView txtnombre;
	TextView txtdetalle;
	ImageView imgimagen;
	
	String nombre;
	String detalle;
	int imagen;
	
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.productos_itemview);
		// Intent para ClientesListView
		Intent i = getIntent();
		
		// Traigo los resultados 
		nombre = i.getStringExtra("nombre");
		detalle = i.getStringExtra("detalle");
		imagen = i.getIntExtra("imagen", imagen);
		
		// Locate the TextViews in singleitemview.xml
		txtnombre = (TextView) findViewById(R.id.nombre);
		txtdetalle = (TextView) findViewById(R.id.detalle);
		imgimagen = (ImageView) findViewById(R.id.imagen);
		
		// Load the results into the TextViews
		txtnombre.setText(nombre);
		txtdetalle.setText(detalle);
		imgimagen.setImageResource(imagen);
	}
}
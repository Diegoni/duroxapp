package com.durox.app.Presupuestos;

import com.example.durox_app.R;
import com.example.durox_app.R.id;
import com.example.durox_app.R.layout;
import com.durox.app.MenuActivity;
import com.durox.app.Visitas.Visitas_Main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class Linea_Presupuestos_ItemView extends MenuActivity {
	// Declare Variables
	TextView txtproducto;
	TextView txtcantidad;
	TextView txtcomentario;
	
	Context mContext;
	
	String producto;
	String cantidad;
	String comentario;
	
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.linea_presupuestos_itemview);
		// Intent para ClientesListView
		Intent i = getIntent();
		
		// Traigo los resultados 
		producto = i.getStringExtra("producto");
		cantidad = i.getStringExtra("cantidad");
		comentario = i.getStringExtra("comentario");
		
		// Locate the TextViews in singleitemview.xml
		txtproducto = (TextView) findViewById(R.id.producto);
		txtcantidad = (TextView) findViewById(R.id.cantidad);
		txtcomentario = (TextView) findViewById(R.id.comentario);
	
		
		// Load the results into the TextViews
		txtproducto.setText(producto);
		txtcantidad.setText(cantidad);
		txtcomentario.setText(comentario);
		
	}
	
}
package com.durox.app.Clientes;

import com.androidbegin.filterlistviewimg.R;
import com.androidbegin.filterlistviewimg.R.id;
import com.androidbegin.filterlistviewimg.R.layout;
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

@SuppressLint("ShowToast")
public class Clientes_ItemView extends Activity 
{
	// Declare Variables
	TextView txtnombre;
	TextView txtdireccion;
	Button btnvisita;
	ImageView imgimagen;
	
	Context mContext;
	
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
		btnvisita = (Button) findViewById(R.id.visita);
		
		// Load the results into the TextViews
		txtnombre.setText(nombre);
		txtdireccion.setText(direccion);
		imgimagen.setImageResource(imagen);
		
	}
	
	
	public void visitas_crear(View view) {
		Intent i = getIntent();
		nombre = i.getStringExtra("nombre");
		
		Intent intent = new Intent(this, Visitas_Main.class);
		
		intent.putExtra("nombre", nombre);
		
		startActivity(intent);
	}
}
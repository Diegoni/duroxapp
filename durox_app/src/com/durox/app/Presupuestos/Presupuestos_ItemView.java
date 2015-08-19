package com.durox.app.Presupuestos;

import com.example.durox_app.R;
import com.example.durox_app.R.id;
import com.example.durox_app.R.layout;
import com.durox.app.Config_durox;
import com.durox.app.Models.Clientes_model;
import com.durox.app.Models.Lineas_Presupuestos_model;
import com.durox.app.Models.Presupuestos_model;
import com.durox.app.Visitas.Visitas_Main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class Presupuestos_ItemView extends Activity 
{
	// Declare Variables
	
	ImageView imgimagen;
	
	Context mContext;
	
	String nombre;
	String direccion;
	int imagen;
	
	SQLiteDatabase db;
	Presupuestos_model mPresupuesto;
	Cursor c;
	Config_durox config;
	
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.presupuestos_itemview);
		
		config = new Config_durox();
		
		db = openOrCreateDatabase(config.getDatabase(), Context.MODE_PRIVATE, null);
		
		TextView txtpreIdPresupuesto = (TextView) findViewById(R.id.txt_preIdPresupuesto);
		TextView txtpreIdVisita = (TextView) findViewById(R.id.txt_preIdVisita);
		TextView txtpreTotal = (TextView) findViewById(R.id.txt_preTotal);
		TextView txtpreCliente = (TextView) findViewById(R.id.txt_preCliente);
		TextView txtpreNombre = (TextView) findViewById(R.id.txt_preNombre);
		TextView txtpreCuit = (TextView) findViewById(R.id.txt_preCuit);
		
		imgimagen = (ImageView) findViewById(R.id.imagen);
		
		
		// Intent para ClientesListView
		Intent i = getIntent();
				
		// Traigo los resultados 
		String id = i.getStringExtra("id");
		imagen = i.getIntExtra("imagen", imagen);
		
		mPresupuesto = new Presupuestos_model(db);
		
		c = mPresupuesto.getRegistro(id);
				
		if(c.getCount() > 0)
		{
			while(c.moveToNext())
    		{
				txtpreIdPresupuesto.setText(c.getString(0));
				txtpreIdVisita.setText(c.getString(1));
				txtpreTotal.setText(c.getString(2));
				txtpreCliente.setText(c.getString(3));
				txtpreNombre.setText(c.getString(5)+" "+c.getString(4));
				txtpreCuit.setText(c.getString(6));
			}
		}
		
		imgimagen.setImageResource(imagen);
		
		
		Lineas_Presupuestos_model mLinea = new Lineas_Presupuestos_model(db);
		
		Cursor cLinea = mLinea.getRegistro(id);
		
		if(cLinea.getCount() > 0)
		{
			while(cLinea.moveToNext())
    		{
				//Toast.makeText(this, "Clientes :"+cLinea.getString(0), Toast.LENGTH_SHORT).show(); 
			}
		}
		
		
	}	
}
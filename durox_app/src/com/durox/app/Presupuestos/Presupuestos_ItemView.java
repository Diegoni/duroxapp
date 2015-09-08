package com.durox.app.Presupuestos;

import com.example.durox_app.R;

import java.util.ArrayList;
import com.durox.app.Config_durox;
import com.durox.app.MenuActivity;
import com.durox.app.Models.Lineas_Presupuestos_model;
import com.durox.app.Models.Presupuestos_model;
import com.durox.app.Presupuestos.Presupuestos_Create;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class Presupuestos_ItemView extends MenuActivity {
	// Declare Variables
	
	ImageView imgimagen;
	
	Context mContext;
	
	String nombre;
	String direccion;
	int imagen;
	
	SQLiteDatabase db;
	Presupuestos_model mPresupuesto;
	Cursor c;
	Cursor cursorLinea;
	Config_durox config;
	
	ListView list;
	ArrayList<Linea_Presupuestos> arraylist = new ArrayList<Linea_Presupuestos>();
	Linea_Presupuestos_ListView adapter_listView;
	
	String id;
	String cIdVisita;
	String cNombre;
	String id_presupuesto;
	
	public void onCreate(Bundle savedInstanceState) {
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
		TextView txtpreNombreFantasia = (TextView) findViewById(R.id.txt_preNombreFantasia);
		TextView txtpreFecha = (TextView) findViewById(R.id.txt_preFecha);
		Button btnAprobar = (Button) findViewById(R.id.btn_preAprobar);
		
		
		imgimagen = (ImageView) findViewById(R.id.imagen);
		
		
		// Intent para ClientesListView
		Intent i = getIntent();
				
		// Traigo los resultados 
		id = i.getStringExtra("id");
		id_presupuesto = i.getStringExtra("id_presupuesto");
		imagen = i.getIntExtra("imagen", imagen);
		
		mPresupuesto = new Presupuestos_model(db);
		
		if (id.equals("0")){
			c = mPresupuesto.getIDRegistro(id_presupuesto);
		} else {
			btnAprobar.setEnabled(false);
			c = mPresupuesto.getRegistro(id);
		}
				
		if(c.getCount() > 0){
			while(c.moveToNext()){
				txtpreIdPresupuesto.setText(c.getString(0));
				txtpreIdVisita.setText(c.getString(1));
				txtpreTotal.setText(c.getString(2));
				txtpreCliente.setText(c.getString(3));
				txtpreNombre.setText(c.getString(5)+" "+c.getString(4));
				txtpreCuit.setText(c.getString(6));
				txtpreNombreFantasia.setText(c.getString(7));
				txtpreFecha.setText(c.getString(8));
				
				cIdVisita = c.getString(1);
				cNombre = c.getString(3);
			}
		}
		
		imgimagen.setImageResource(imagen);
		
		// Detalle del Presupuesto
		
		Lineas_Presupuestos_model mLineas = new Lineas_Presupuestos_model(db);
		
		if (id.equals("0")){
			cursorLinea = mLineas.getIDRegistro(id_presupuesto);
		} else {
			cursorLinea = mLineas.getRegistro(id);
		}		
			
		int cantidad_lineas = cursorLinea.getCount();
			
		String[] producto_linea = new String[cantidad_lineas];
		String[] cantidad_linea = new String[cantidad_lineas];
		String[] precio_linea = new String[cantidad_lineas];
		String[] total_linea = new String[cantidad_lineas];
			
		int x = 0;
					
		if(cursorLinea.getCount() > 0){
			while(cursorLinea.moveToNext()){
				producto_linea[x] = cursorLinea.getString(0);
				cantidad_linea[x] = cursorLinea.getString(1);
				precio_linea[x] = cursorLinea.getString(2);
				total_linea[x] = cursorLinea.getString(3);
				x = x + 1;
			}	
				
			list = (ListView) findViewById(R.id.detallePresupuesto);

			for (int j = 0; j < producto_linea.length; j++) {
				
				Linea_Presupuestos wp = new Linea_Presupuestos(
					cantidad_linea[j],
					producto_linea[j],
					precio_linea[j],
					total_linea[j]
				);
					
				arraylist.add(wp);
			}
				
			adapter_listView = new Linea_Presupuestos_ListView(this, arraylist);
			list.setAdapter(adapter_listView);
		}
	}
	
	
	
	public void aprobar_presupuesto(View view) {
		Intent i = getIntent();
		
		id_presupuesto = i.getStringExtra("id_presupuesto");
		db = openOrCreateDatabase(config.getDatabase(), Context.MODE_PRIVATE, null);
		Presupuestos_model mPresupuestoUpdate = new Presupuestos_model(db);
		mPresupuestoUpdate.aprobar(id_presupuesto);
		
		Toast.makeText(this, config.msjOkUpdate(), Toast.LENGTH_SHORT).show();
	}
	
	
	
	public void editar_presupuesto(View view) {
		/*//Intent intent = new Intent(Presupuestos_ItemView.this, Presupuestos_Create.class);
		Intent intent = new Intent(Presupuestos_ItemView.this, Presupuestos_ItemView.class);
		Log.e("Paso ", "intent ");
		
		intent.putExtra("cNombre", cNombre);
		Log.e("Paso ", "cNombre "+cNombre);
		intent.putExtra("cIdVisita", cIdVisita);
		Log.e("Paso ", "cIdVisita "+cIdVisita);
		intent.putExtra("id_presupuesto", id_presupuesto);
		Log.e("Paso ", "id_presupuesto "+id_presupuesto);
		startActivity(intent);
		Log.e("Paso ", "startActivity ");
		*/
		
		Intent intent = new Intent(Presupuestos_ItemView.this, Presupuestos_Create.class);
		/*
		intent.putExtra("nombre", cNombre);
		intent.putExtra("id_visita", cIdVisita);
		intent.putExtra("truncate", "truncate");
		*/
		startActivity(intent);
	}
}
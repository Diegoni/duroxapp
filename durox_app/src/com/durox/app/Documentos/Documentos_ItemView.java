package com.durox.app.Documentos;

import com.example.durox_app.R;
import com.example.durox_app.R.id;
import com.example.durox_app.R.layout;
import com.durox.app.Config_durox;
import com.durox.app.Models.Clientes_model;
import com.durox.app.Models.Productos_model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Documentos_ItemView extends Activity 
{
	// Declare Variables
	ImageView imgimagen;
	
	String nombre;
	String detalle;
	String id;
	int imagen;
	
	SQLiteDatabase db;
	Productos_model mProducto;
	Cursor c;
	Config_durox config;
	
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.productos_itemview);
		
		config = new Config_durox();
		
		db = openOrCreateDatabase(config.getDatabase(), Context.MODE_PRIVATE, null);
		
		// Locate the TextViews in singleitemview.xml
		//TextView id_back = (TextView) findViewById(R.id.txt_c);
		
		TextView txt_pNombre = (TextView) findViewById(R.id.txt_pNombre);
		TextView txt_pCodigo = (TextView) findViewById(R.id.txt_pCodigo);
		TextView txt_pCodigoLote = (TextView) findViewById(R.id.txt_pCodigoLote);
		TextView txt_pPrecio = (TextView) findViewById(R.id.txt_pPrecio);
		TextView txt_pPrecioIva = (TextView) findViewById(R.id.txt_pPrecioIva);
		TextView txt_pPrecioMin = (TextView) findViewById(R.id.txt_pPrecioMin);
		TextView txt_pPrecioMinIva = (TextView) findViewById(R.id.txt_pPrecioMinIva);
		TextView txt_pIva = (TextView) findViewById(R.id.txt_pIva);
		TextView txt_pFichaTecnica = (TextView) findViewById(R.id.txt_pFichaTecnica);
		TextView txt_pDateAdd = (TextView) findViewById(R.id.txt_pDateAdd);
		TextView txt_pDateUpd = (TextView) findViewById(R.id.txt_pDateUpd);
		
		imgimagen = (ImageView) findViewById(R.id.imagen);
		
		
		// Intent para ClientesListView
		Intent i = getIntent();
		
		// Traigo los resultados 
		id = i.getStringExtra("id");
		nombre = i.getStringExtra("nombre");
		detalle = i.getStringExtra("detalle");
		imagen = i.getIntExtra("imagen", imagen);
		
		mProducto = new Productos_model(db);
		
		c = mProducto.getRegistro(id);
				
		if(c.getCount() > 0)
		{
			while(c.moveToNext())
    		{
				txt_pNombre.setText(c.getString(4));
				txt_pCodigo.setText(c.getString(2));
				txt_pCodigoLote.setText(c.getString(3));
				txt_pPrecio.setText(c.getString(5));
				txt_pPrecioIva.setText(c.getString(6));
				txt_pPrecioMin.setText(c.getString(7));
				txt_pPrecioMinIva.setText(c.getString(8));
				txt_pIva.setText(c.getString(9));
				txt_pFichaTecnica.setText(c.getString(10));
				txt_pDateAdd.setText(c.getString(11));
				txt_pDateUpd.setText(c.getString(12));
			}
		}
		
		
		imgimagen.setImageResource(imagen);
		
	}
}
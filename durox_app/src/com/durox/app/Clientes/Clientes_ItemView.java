package com.durox.app.Clientes;

import com.example.durox_app.R;
import com.example.durox_app.R.id;
import com.example.durox_app.R.layout;
import com.durox.app.Models.Clientes_model;
import com.durox.app.Models.Telefonos_clientes_model;
import com.durox.app.Visitas.Visitas_Main;
import com.durox.app.Config_durox;
import com.durox.app.MenuActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class Clientes_ItemView extends MenuActivity {
	// Declare Variables
	
	Button btnvisita;
	ImageView imgimagen;
	
	Context mContext;
	
	String nombre;
	String direccion;
	int imagen;
	
	SQLiteDatabase db;
	Clientes_model mCliente;
	Cursor c;
	Config_durox config;
	
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clientes_itemview);
		
		config = new Config_durox();
		
		db = openOrCreateDatabase(config.getDatabase(), Context.MODE_PRIVATE, null);
		
		// Locate the TextViews in singleitemview.xml
		//TextView id_back = (TextView) findViewById(R.id.txt_c);
		TextView txtnombre = (TextView) findViewById(R.id.txt_cNombre);
		TextView txtcuit = (TextView) findViewById(R.id.txt_cCuit);
		TextView txtgrupo = (TextView) findViewById(R.id.txt_cGrupo);
		TextView txtiva = (TextView) findViewById(R.id.txt_cIva);
		TextView txtnombre_fantasia = (TextView) findViewById(R.id.txt_cNombreFantasia);
		TextView txtrazon_social = (TextView) findViewById(R.id.txt_cRazon_social);
		TextView txtweb = (TextView) findViewById(R.id.txt_cWeb);
		TextView txtdate_add = (TextView) findViewById(R.id.txt_cDateadd);
		TextView txtdate_upd = (TextView) findViewById(R.id.txt_cDateupd);
		
		imgimagen = (ImageView) findViewById(R.id.imagen);
		btnvisita = (Button) findViewById(R.id.visita);
		
		// Intent para ClientesListView
		Intent i = getIntent();
				
		// Traigo los resultados 
		String id = i.getStringExtra("id");
		imagen = i.getIntExtra("imagen", imagen);
		
		mCliente = new Clientes_model(db);
		
		c = mCliente.getRegistro(id);
				
		if(c.getCount() > 0)
		{
			while(c.moveToNext())
    		{
				txtnombre.setText(c.getString(3)+" "+c.getString(2));
				txtcuit.setText(c.getString(5));
				txtgrupo.setText(c.getString(6));
				txtiva.setText(c.getString(7));
				txtnombre_fantasia.setText(c.getString(9));
				txtrazon_social.setText(c.getString(10));
				txtweb.setText(c.getString(11));
				txtdate_add.setText(c.getString(12));
				txtdate_upd.setText(c.getString(13));	
    		}
		}
		
		Telefonos_clientes_model mTelefonos = new Telefonos_clientes_model(db);
		
		Cursor cTelefonos = mTelefonos.getRegistro(id);
		
		Log.e("Resultado  ", ""+cTelefonos.getCount());
		
		if(cTelefonos.getCount() > 0){
			
		}else{
			//btnTelefono.setEnabled(false);
		}
		
		
		
		// Load the results into the TextViews
		
		
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
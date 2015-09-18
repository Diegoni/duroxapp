package com.durox.app.Vendedores;

import com.example.durox_app.R;
import com.durox.app.Config_durox;
import com.durox.app.MenuActivity;
import com.durox.app.Models.Mensajes_model;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Mensajes_ItemView extends MenuActivity {
	// Declare Variables
	ImageView imgimagen;
	
	String id;
	String asunto;
	String mensaje;
	String date_add;
	String id_origen;
	int imagen;
	
	SQLiteDatabase db;
	Mensajes_model mMensaje;
	Cursor c;
	
	Config_durox config;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mensajes_itemview);
		
		setTitle("Mensajes - Registro");
        getActionBar().setIcon(R.drawable.menuproductos);
		
		config = new Config_durox();
		
		db = openOrCreateDatabase(config.getDatabase(), Context.MODE_PRIVATE, null);
		
		TextView txt_asunto = (TextView) findViewById(R.id.txt_asunto);
		TextView txt_mensaje = (TextView) findViewById(R.id.txt_mensaje);
		TextView txt_date = (TextView) findViewById(R.id.txt_date);
		EditText et_asunto = (EditText) findViewById(R.id.et_asunto);
		EditText et_mensaje = (EditText) findViewById(R.id.et_mensaje);
		Button btn_enviar = (Button) findViewById(R.id.btn_enviar);
		
		imgimagen = (ImageView) findViewById(R.id.imagen);
		 
		Intent i = getIntent();
		
		id = i.getStringExtra("id");
		asunto = i.getStringExtra("asunto");
		mensaje = i.getStringExtra("mensaje");
		date_add = i.getStringExtra("date_add");
		id_origen = i.getStringExtra("id_origen");
		imagen = i.getIntExtra("imagen", imagen);
		
		mMensaje = new Mensajes_model(db);
		
		c = mMensaje.getRegistro(id);
				
		if(c.getCount() > 0){
			while(c.moveToNext()){
				txt_asunto.setText(c.getString(1));
				txt_mensaje.setText(c.getString(2));
				txt_date.setText(c.getString(3));
				et_asunto.setText("Re: "+c.getString(1));
				
			}
		}
		
		imgimagen.setImageResource(imagen);
		
		if(id_origen.equals("1")){
			et_asunto.setEnabled(false);
			et_mensaje.setEnabled(false);
			btn_enviar.setEnabled(false);
			
		}
	}
	
	
	public void enviar_mensaje(View view) {
		EditText et_asunto = (EditText) findViewById(R.id.et_asunto);
		EditText et_mensaje = (EditText) findViewById(R.id.et_mensaje);
		
		String asunto = ""; 
		asunto = et_asunto.getText().toString();
		String mensaje = et_mensaje.getText().toString();
		
		Mensajes_model mMensaje = new Mensajes_model(db);
		
		mMensaje.insert(
			"0", 		// id_back, 
			asunto, 
			mensaje, 
			"1",		// id_origen, 
			id, 		// id_mensaje_padre, arreglar 
			"0", 		// visto,
			"",			// date_add, 
			"",			// date_upd, 
			"",			// eliminado, 
			"", 		// user_add, 
			""
		);
		
		Intent intentVendedores = new Intent(this, Mensajes_Main.class);
		startActivity(intentVendedores);
	}
}
package com.durox.app.Presupuestos;

import com.example.durox_app.R;

import java.util.ArrayList;
import java.util.List;

import com.durox.app.Config_durox;
import com.durox.app.MenuActivity;
import com.durox.app.Models.Clientes_model;
import com.durox.app.Models.Condiciones_pago_model;
import com.durox.app.Models.Epocas_model;
import com.durox.app.Models.Lineas_Presupuestos_model;
import com.durox.app.Models.Modos_pago_model;
import com.durox.app.Models.Monedas_model;
import com.durox.app.Models.Presupuestos_model;
import com.durox.app.Models.Tiempos_entrega_model;
import com.durox.app.Presupuestos.Presupuestos_Create;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class Presupuestos_Datos extends MenuActivity {
	// Declare Variables
	
	SQLiteDatabase db;
	Config_durox config;
	
	private Spinner sp_condiciones_pago;
	private Spinner sp_modos_pago;
	private Spinner sp_tiempo_entrega;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.presupuestos_datos);
		
		config = new Config_durox();
        db = openOrCreateDatabase(config.getDatabase(), Context.MODE_PRIVATE, null);
		
		sp_condiciones_pago = (Spinner) findViewById(R.id.sp_condiciones_pago);
		List<String> listCondiciones = new ArrayList<String>();
		Condiciones_pago_model mCondiciones = new Condiciones_pago_model(db);
		Cursor cCondiciones = mCondiciones.getRegistros();
		if(cCondiciones.getCount() > 0){
			while(cCondiciones.moveToNext()){
				listCondiciones.add(cCondiciones.getString(1));
			}	
		
		}
		ArrayAdapter<String> adapterCondiciones = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,listCondiciones);
		adapterCondiciones.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_condiciones_pago.setAdapter(adapterCondiciones);
		
		
		sp_modos_pago = (Spinner) findViewById(R.id.sp_modos_pago);
		List<String> listModos = new ArrayList<String>();
		Modos_pago_model mModos = new Modos_pago_model(db);
		Cursor cModos = mModos.getRegistros();
		if(cModos.getCount() > 0){
			while(cModos.moveToNext()){
				listModos.add(cModos.getString(1));
			}	
		
		}
		ArrayAdapter<String> adapterModos = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,listModos);
		adapterModos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_modos_pago.setAdapter(adapterModos);
		
		
		sp_tiempo_entrega = (Spinner) findViewById(R.id.sp_tiempo_entrega);
		List<String> listTiempos = new ArrayList<String>();
		Tiempos_entrega_model mTiempos = new Tiempos_entrega_model(db);
		Cursor cTiempos = mTiempos.getRegistros();
		if(cTiempos.getCount() > 0){
			while(cTiempos.moveToNext()){
				listTiempos.add(cTiempos.getString(1));
			}	
		
		}
		ArrayAdapter<String> adapterTiempos = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,listTiempos);
		adapterTiempos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_tiempo_entrega.setAdapter(adapterTiempos);
		
	}
}
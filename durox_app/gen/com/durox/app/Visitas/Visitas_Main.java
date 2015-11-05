package com.durox.app.Visitas;

import com.example.durox_app.R;

import java.util.ArrayList;
import java.util.List;

import com.durox.app.Config_durox;
import com.durox.app.MainActivity;
import com.durox.app.MenuActivity;
import com.durox.app.Models.Clientes_model;
import com.durox.app.Models.Epocas_model;
import com.durox.app.Models.Vendedores_model;
import com.durox.app.Models.Visitas_model;
import com.durox.app.Presupuestos.Presupuestos_Main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

public class Visitas_Main extends MenuActivity {
	AutoCompleteTextView auto_cliente;
	Spinner auto_epoca;
	RatingBar ebValoracion;
	DatePicker etfecha;
	EditText etComentario;
	String[] c_nombre;
	String[] epocas;
	SQLiteDatabase db;
	
	String truncate;
	String sql;
	Cursor c;
	int cantidad;
	int j;
	Clientes_model mClientes;
	Visitas_model mVisitas;
	Epocas_model mEpocas;
	ProgressDialog pDialog;
	
	String id_front;
	
	Config_durox config;


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.visitas_main);
		
		setTitle("Visitas - Carga de nueva visita");
        getActionBar().setIcon(R.drawable.menuvisitas);
		
		cargar_vista();
	}

	
	public void cargar_vista(){
		auto_cliente = (AutoCompleteTextView) findViewById(R.id.autoClientes);
		etfecha = (DatePicker) findViewById(R.id.dp_Fecha);
		etComentario = (EditText) findViewById(R.id.txtcomentario);
		ebValoracion = (RatingBar) findViewById(R.id.ebValoracion);
		
		config = new Config_durox();
		db = openOrCreateDatabase(config.getDatabase(), Context.MODE_PRIVATE, null);
		
		mClientes = new Clientes_model(db);
		c = mClientes.getClientesVisitas();
		cantidad = c.getCount();
		c_nombre = new String[cantidad];
		j = 0;
		if(c.getCount() > 0){
			while(c.moveToNext()){
				c_nombre[j] = c.getString(10);
				j = j + 1;
    		}		
		} else {
			Toast.makeText(this, config.msjNoRegistros("clientes"), Toast.LENGTH_SHORT).show();
		}
			
		Spinner auto_epoca = (Spinner) findViewById(R.id.autoEpocas);
		List<String> listEpocas = new ArrayList<String>();
		mEpocas = new Epocas_model(db);
		Cursor cEpocas = mEpocas.getRegistros();
		if(cEpocas.getCount() > 0){
			while(cEpocas.moveToNext()){
				listEpocas.add(cEpocas.getString(2));
			}	
		
		}
		ArrayAdapter<String> adapterEpocas = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,listEpocas);
		adapterEpocas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		auto_epoca.setAdapter(adapterEpocas);
		
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, c_nombre);
		auto_cliente.setThreshold(1);
		auto_cliente.setAdapter(adapter);
		
		
		Intent i = getIntent();
		String nombre = i.getStringExtra("nombre");
		id_front = i.getStringExtra("id_front");
		
		if(id_front !=null  &&  !id_front.isEmpty()) {
			Visitas_model mVisitas = new Visitas_model(db);
			Cursor cVisitas = mVisitas.getVisita(id_front);
			

			if(cVisitas.getCount() > 0){
				while(cVisitas.moveToNext()){
					String compareValue = cVisitas.getString(2);
					if (!compareValue.equals(null)) {
					    int spinnerPosition = adapterEpocas.getPosition(compareValue);
					    auto_epoca.setSelection(spinnerPosition);
					}
					auto_cliente.setText(cVisitas.getString(1));
					etComentario.setText(cVisitas.getString(5));
					ebValoracion.setRating(Float.parseFloat(cVisitas.getString(7)));
					
					String fecha = cVisitas.getString(3);
					String[] fecha_separada = fecha.split("/");
					etfecha.updateDate(
							Integer.parseInt(fecha_separada[0]), 
							Integer.parseInt(fecha_separada[1]), 
							Integer.parseInt(fecha_separada[2]));
				}
			}
		}else if(nombre != ""){
			auto_cliente.setText(nombre);
		}	
		
	}
	
	
	
	public void guardar_bajar(View view){
		String textReturn = guardar_visita();
		if(textReturn.equals("ok")){
			Intent intent = new Intent(Visitas_Main.this, Presupuestos_Main.class);
			intent.putExtra("actualizar", "ok");
			startActivity(intent);
		}else{
			Toast.makeText(this, textReturn, Toast.LENGTH_LONG).show();
		}
	}
	
	
	public void guardar(View view){
		String textReturn = guardar_visita();
		if(textReturn.equals("ok")){
			Intent intent = new Intent(Visitas_Main.this, MainActivity.class);
			startActivity(intent);
		}else{
			Toast.makeText(this, textReturn, Toast.LENGTH_LONG).show();
		}
	}
	
	
	public String guardar_visita(){
		String razon_social = auto_cliente.getText().toString();
		String epoca = ((Spinner)findViewById(R.id.autoEpocas)).getSelectedItem().toString();
		String textReturn = "";
		
		int mes = etfecha.getMonth();
		mes = mes + 1;
		String fecha = etfecha.getYear()+"/"+mes+"/"+etfecha.getDayOfMonth();
		String comentario = etComentario.getText().toString();
		float valoracion2 = ebValoracion.getRating();
		db = openOrCreateDatabase(config.getDatabase(), Context.MODE_PRIVATE, null);
		
		Cursor cCliente = mClientes.getID(razon_social);
		
		if(cCliente.getCount() > 0){
			while(cCliente.moveToNext()){
				mEpocas = new Epocas_model(db);
				Cursor cEpoca = mEpocas.getID(epoca);
				Vendedores_model mVendedor = new Vendedores_model(db);
				Cursor cVendedor = mVendedor.getRegistros();
				String IDvendedor = "0";
				
				if(cVendedor.getCount() > 0){
					while(cVendedor.moveToNext()){
						IDvendedor = cVendedor.getString(1);
					}
				}
				
				if(cEpoca.getCount() > 0){
					while(cEpoca.moveToNext()){
						String id_visita = "0";
						String id_cliente = cCliente.getString(0);
						String descripcion = comentario;
						String id_vendedor = IDvendedor;
						String id_epoca_visita = cEpoca.getString(0);
						String valoracion = Float.toString(valoracion2);
						mVisitas = new Visitas_model(db);
						
						
						if(id_front !=null  &&  !id_front.isEmpty()) {
							mVisitas.updateVisita(
								id_front, 
								id_cliente, 
								descripcion, 
								id_epoca_visita, 
								valoracion, 
								fecha);
						
							Toast.makeText(this, config.msjOkUpdate(), Toast.LENGTH_SHORT).show();
							textReturn = "ok";
							
						}else{
							mVisitas.insert(
						 		id_visita,
						 		id_vendedor,
						 		id_cliente,
						 		descripcion,
						 		id_epoca_visita,
						 		valoracion,
						 		fecha,
						 		null,
						 		null,
						 		null,
						 		null,
						 		null,
						 		null,
						 		null);
							
							Toast.makeText(this, config.msjOkInsert(), Toast.LENGTH_SHORT).show();
							textReturn = "ok";
						}
					}
				}else{
					textReturn = config.msjNoRegistro("epoca");
				}
			}	
		} else {
			textReturn = config.msjNoRegistro("cliente");
		}
		return textReturn;
	}
	
	
	
	public void actualizar_visitas(View view){
		Intent intent = new Intent(Visitas_Main.this, Visitas_Enviar.class);
		startActivity(intent);
	}
	
	
	
	public void ver_visitas(View view){
		Intent intent = new Intent(Visitas_Main.this, Presupuestos_Main.class);
		startActivity(intent);
	}
	
	
	
	public void actualizar_epocas(View view) {
		pDialog = new ProgressDialog(this);
        pDialog.setMessage("Actualizando....");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
        
 		Epocas_Update epocas = new Epocas_Update(db, this);
 		epocas.actualizar_registros();
 		
 		Visitas_Update visitas = new Visitas_Update(db, this);
 		visitas.actualizar_registros();
 		
 		pDialog.dismiss();
 		cargar_vista();
 	}
}

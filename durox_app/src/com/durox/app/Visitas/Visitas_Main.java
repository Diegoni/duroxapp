package com.durox.app.Visitas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.durox_app.R;
import com.example.durox_app.R.id;
import com.example.durox_app.R.layout;
import com.durox.app.Config_durox;
import com.durox.app.MainActivity;
import com.durox.app.MenuActivity;
import com.durox.app.Models.Clientes_model;
import com.durox.app.Models.Epocas_model;
import com.durox.app.Models.Vendedores_model;
import com.durox.app.Models.Visitas_model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

public class Visitas_Main extends MenuActivity {
	AutoCompleteTextView auto_cliente;
	AutoCompleteTextView auto_epoca;
	RatingBar ebValoracion;
	EditText etfecha;
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
	
	private String jsonResult;
	
	Config_durox config;


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.visitas_main);
		
		cargar_vista();
	}

	
	public void cargar_vista(){
		auto_cliente = (AutoCompleteTextView) findViewById(R.id.autoClientes);
		auto_epoca = (AutoCompleteTextView) findViewById(R.id.autoEpocas);
		etfecha = (EditText) findViewById(R.id.etFecha);
		etComentario = (EditText) findViewById(R.id.txtcomentario);
		ebValoracion = (RatingBar) findViewById(R.id.ebValoracion);
		
		config = new Config_durox();
		
		db = openOrCreateDatabase(config.getDatabase(), Context.MODE_PRIVATE, null);
		
		mClientes = new Clientes_model(db);
		
		c = mClientes.getRegistros();
		
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
				
		mEpocas = new Epocas_model(db);
		
		c = mEpocas.getRegistros();
		
		cantidad = c.getCount();
		
		epocas = new String[cantidad];
		
		j = 0;
				
		if(c.getCount() > 0){
			while(c.moveToNext()){
				epocas[j] = c.getString(2);
				
    			j = j + 1;
    		}		
		} else {
			Toast.makeText(this, config.msjNoRegistro("epocas"), Toast.LENGTH_LONG).show();
		}
		
		ArrayAdapter<String> adapterProductos = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, epocas);
		auto_epoca.setThreshold(1);
		auto_epoca.setAdapter(adapterProductos);
		
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, c_nombre);
		auto_cliente.setThreshold(1);
		auto_cliente.setAdapter(adapter);
		
		
		Intent i = getIntent();
		String nombre = i.getStringExtra("nombre");
		
		if(nombre != ""){
			auto_cliente.setText(nombre);
		}	
		
	}
	
	public void agregar_comentarios(View view) {
		String nombre = auto_cliente.getText().toString();
		String epoca = auto_epoca.getText().toString();
		String fecha = etfecha.getText().toString();
		float valoracion = ebValoracion.getRating();
		
		Intent intent = new Intent(Visitas_Main.this, Visitas_Detalle.class);
		
		intent.putExtra("nombre", nombre);
		intent.putExtra("epoca", epoca);
		intent.putExtra("fecha", fecha);
		intent.putExtra("valoracion", valoracion);
		
		startActivity(intent);
	}
	
	
	public void guardar(View view){
		String razon_social = auto_cliente.getText().toString();
		String epoca = auto_epoca.getText().toString();
		String fecha = etfecha.getText().toString();
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
						String id_origen = "1";
						String visto = "0";
						
						
						mVisitas = new Visitas_model(db);
						
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
					 			null
					 		);
						
						Toast.makeText(this, config.msjOkInsert(), Toast.LENGTH_SHORT).show();
						
						Intent intent = new Intent(Visitas_Main.this, MainActivity.class);
						startActivity(intent);
					}
				}else{
					Toast.makeText(this, config.msjNoRegistro("epoca"), Toast.LENGTH_LONG).show();
				}
			}	
		} else {
			Toast.makeText(this, config.msjNoRegistro("cliente"), Toast.LENGTH_LONG).show();
		}
	}
	
	public void actualizar_visitas(View view){
		Intent intent = new Intent(Visitas_Main.this, Visitas_Enviar.class);
		startActivity(intent);
	}
	
	
	public void actualizar_epocas(View view) {
 		JsonReadTask taskclientes = new JsonReadTask();
 		String url = config.getIp()+"/actualizaciones/getEpocas/";
 		taskclientes.execute(new String[] { url });
 	}
	
	
	private class JsonReadTask extends AsyncTask<String, Void, String> {
 		
 		protected String doInBackground(String... params) {
 			HttpClient httpclient = new DefaultHttpClient();
 			HttpPost httppost = new HttpPost(params[0]);
 				
 			try { 				
 				HttpResponse response = httpclient.execute(httppost);
 				jsonResult = inputStreamToString(
 				response.getEntity().getContent()).toString();
 			} catch (ClientProtocolException e) {
 				e.printStackTrace();
 			} catch (IOException e) {
 				e.printStackTrace();
 			}
 			
 			return null;
 		}
  
 		private StringBuilder inputStreamToString(InputStream is) {
 			String rLine = "";
 			StringBuilder answer = new StringBuilder();
 			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
 	 
 			try {
 				while ((rLine = rd.readLine()) != null) {
 					answer.append(rLine);
 				}
 			} catch (IOException e) {
 				// e.printStackTrace();
 				Toast.makeText(getApplicationContext(), 
 				config.msjError(e.toString()) , Toast.LENGTH_LONG).show();
 			}
 			
 			return answer;
 		}
  
 		protected void onPostExecute(String result) {
 			CargarEpocas();
 		}
 	}
 	
 	
 
  	public void CargarEpocas() {
  		try {
  			JSONObject jsonResponse = new JSONObject(jsonResult);
  			JSONArray jsonMainNode = jsonResponse.optJSONArray("epocas");
  			
  			db = openOrCreateDatabase(config.getDatabase(), Context.MODE_PRIVATE, null);
  			
  			mEpocas = new Epocas_model(db);
  			
  			mEpocas.getRegistros();
  			  			
  			if(jsonMainNode.length() > 0){
  			
  				mEpocas.truncate();
  				Toast.makeText(getApplicationContext(), 
  			 			config.msjActualizandoRegistros() , Toast.LENGTH_SHORT).show();
  			}
  			  
  			for (int i = 0; i < jsonMainNode.length(); i++) {
  				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
  				  				
  				String id_back = jsonChildNode.optString("id_epoca_visita");
  				String epoca = jsonChildNode.optString("epoca");
  				String date_add = jsonChildNode.optString("date_add");
  				String date_upd = jsonChildNode.optString("date_upd");
  				String eliminado = jsonChildNode.optString("eliminado");
  				String user_add = jsonChildNode.optString("user_add");
  				String user_upd = jsonChildNode.optString("user_upd");
  				 				
  				mEpocas.insert(
  					id_back,
  					epoca,
  					date_add,
  					date_upd,
  					eliminado,
  					user_add,
  					user_upd
  				);
  			}
  			
  			Toast.makeText(getApplicationContext(), 
  	  	 			config.msjRegistrosActualizados(" epocas "+jsonMainNode.length()), Toast.LENGTH_SHORT).show();
  			
  		} catch (JSONException e) {
  			Toast.makeText(getApplicationContext(), 
  			config.msjError(e.toString()) , Toast.LENGTH_SHORT).show();
  		}
  		
  		cargar_vista();
   	}
  	

}

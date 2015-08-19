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

import com.androidbegin.filterlistviewimg.R;
import com.androidbegin.filterlistviewimg.R.id;
import com.androidbegin.filterlistviewimg.R.layout;
import com.durox.app.MainActivity;
import com.durox.app.Models.Clientes_model;
import com.durox.app.Models.Epocas_model;
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

public class Visitas_Main extends Activity {
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


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.visitas_main2);
		
		//cargar_vista();
	}

	
	public void cargar_vista(){
		auto_cliente = (AutoCompleteTextView) findViewById(R.id.autoClientes);
		auto_epoca = (AutoCompleteTextView) findViewById(R.id.autoEpocas);
		etfecha = (EditText) findViewById(R.id.etFecha);
		etComentario = (EditText) findViewById(R.id.txtcomentario);
		ebValoracion = (RatingBar) findViewById(R.id.ebValoracion);
		
		
		db = openOrCreateDatabase("Durox_app", Context.MODE_PRIVATE, null);
		
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
			Toast.makeText(this, "No hay clientes cargados", Toast.LENGTH_SHORT).show();
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
			Toast.makeText(this, "No hay epocas cargadas", Toast.LENGTH_LONG).show();
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
		String nombre = auto_cliente.getText().toString();
		String epoca = auto_epoca.getText().toString();
		String fecha = etfecha.getText().toString();
		String comentario = etComentario.getText().toString();
		float valoracion2 = ebValoracion.getRating();
		
		db = openOrCreateDatabase("Durox_app", Context.MODE_PRIVATE, null);
		
		sql = "SELECT "
				+ "id_back"
				+ " FROM clientes"
				+ " WHERE razon_social = '"+nombre+"'";
		
		c = db.rawQuery(sql, null);
		
		if(c.getCount() > 0){
			while(c.moveToNext()){
				sql = "SELECT "
						+ "id_back"
						+ " FROM epocas_visitas"
						+ " WHERE epoca = '"+epoca+"'";
				
				Cursor cursor = db.rawQuery(sql, null);
				
				if(cursor.getCount() > 0){
					while(cursor.moveToNext()){
						

						String id_visita = "0";
						String id_cliente = c.getString(0);
						String descripcion = comentario;
						String id_vendedor = "1";
						String id_epoca_visita = cursor.getString(0);
						String valoracion = Float.toString(valoracion2);
						String id_origen = "1";
						String visto = "0";
						String date_add = nombre;
						String date_upd = nombre;
						String eliminado = nombre;
						String user_add = nombre;
						String user_upd = nombre;
						
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
						
						Toast.makeText(this, "El registro se ha guardado con éxito", Toast.LENGTH_SHORT).show();
						
						Intent intent = new Intent(Visitas_Main.this, MainActivity.class);
						startActivity(intent);
						
					}
				}else{
					Toast.makeText(this, "La epoca no se encuentra en la base de datos", Toast.LENGTH_LONG).show();
				}
				
    		}		
		} else {
			Toast.makeText(this, "El cliente no se encuentra en la base de datos", Toast.LENGTH_LONG).show();
		}
	}
	
	public void actualizar_visitas(View view){
		Intent intent = new Intent(Visitas_Main.this, Visitas_Enviar.class);
		startActivity(intent);
	}
	
	
	public void actualizar_epocas(View view) {
 		JsonReadTask taskclientes = new JsonReadTask();
 		String url = "http://10.0.2.2/durox/index.php/actualizaciones/getEpocas/";
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
 				"Error..." + e.toString(), Toast.LENGTH_LONG).show();
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
  			
  			if(jsonMainNode.length() > 0){
  				mEpocas.truncate();
  				Toast.makeText(getApplicationContext(), 
  			 			"Registros registros "+jsonMainNode.length() , Toast.LENGTH_SHORT).show();
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
  		} catch (JSONException e) {
  			Toast.makeText(getApplicationContext(), 
  			"Error" + e.toString(), Toast.LENGTH_SHORT).show();
  		}
  		
  		Toast.makeText(getApplicationContext(), 
  	 			"Registros actualizados", Toast.LENGTH_SHORT).show();
  		
  		cargar_vista();
   	}
  	

}

package com.durox.app.Visitas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.durox_app.R;
import com.example.durox_app.R.id;
import com.example.durox_app.R.layout;
import com.durox.app.Config_durox;
import com.durox.app.MainActivity;
import com.durox.app.MenuActivity;
import com.durox.app.Clientes.Clientes;
import com.durox.app.Models.Clientes_model;
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
import android.widget.TextView;
import android.widget.Toast;

public class Visitas_Enviar extends MenuActivity {
	
	private String jsonResult;
	Clientes_model mCliente;
	SQLiteDatabase db;
	Visitas_model mVisitas;
	Cursor c;
	Config_durox config;

	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.visitas_enviar);
		
		config = new Config_durox();
		
		db = openOrCreateDatabase(config.getDatabase(), Context.MODE_PRIVATE, null);
		
		mVisitas = new Visitas_model(db);
		
		//c = mVisitas.getNuevos();
		c = mVisitas.getRegistros();
		
		int cantidad = c.getCount();
		
		if(c.getCount() > 0){
			while(c.moveToNext()){
				
				JsonReadTask task = new JsonReadTask(
						c.getString(2),
						c.getString(3),
						c.getString(4),
						c.getString(5),
						c.getString(6),
						c.getString(7)
				);
				
				String url = config.getIp(db)+"/actualizaciones/setVisita/";
		 		task.execute(new String[] { url });
    		}		
		}
		else{
			Toast.makeText(this, config.msjNoRegistro("visitas"), Toast.LENGTH_SHORT).show();
		}
 			
		
		
	}
	
	
	  // Async Task to access the web
	private class JsonReadTask extends AsyncTask<String, Void, String> {
		String id_vendedor;
		String id_cliente;
		String descripcion;
		String id_epoca_visita;
		String valoracion;
		String fecha;
		
 		public JsonReadTask(
 				String id_vendedor,
 				String id_cliente,
 				String descripcion,
 				String id_epoca_visita,
 				String valoracion,
 				String fecha) {
			this.id_vendedor  = id_vendedor;
 			this.id_cliente  = id_cliente;
 			this.descripcion  = descripcion;
 			this.id_epoca_visita  = id_epoca_visita;
 			this.valoracion  = valoracion;
 			this.fecha  = fecha;	
 		}
 		
	
		protected String doInBackground(String... params) {
	 		HttpClient httpclient = new DefaultHttpClient();
	 		HttpPost httppost = new HttpPost(params[0]);
	 		
	 		
	 		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
	 		pairs.add(new BasicNameValuePair("id_vendedor", id_vendedor));
 			pairs.add(new BasicNameValuePair("id_cliente", id_cliente));
 			pairs.add(new BasicNameValuePair("descripcion", descripcion));
 			pairs.add(new BasicNameValuePair("id_epoca_visita", id_epoca_visita));
 			pairs.add(new BasicNameValuePair("valoracion", valoracion));
 			pairs.add(new BasicNameValuePair("fecha", fecha));
 			
	 			
	 		try { 				
	 			httppost.setEntity(new UrlEncodedFormEntity(pairs));
	 				
	 			HttpResponse response = httpclient.execute(httppost);
	 		} catch (ClientProtocolException e) {
	 			Toast.makeText(getApplicationContext(), 
	 		 			config.msjError(e.toString())  , Toast.LENGTH_SHORT).show();
	 			
	 		} catch (IOException e) {
	 			Toast.makeText(getApplicationContext(), 
	 					config.msjError(e.toString()) , Toast.LENGTH_SHORT).show();
	 		}
	 			
	 		return null;
	 	}
	  
		protected void onPostExecute(String result) {
	 	}
	}
}

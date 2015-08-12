package com.durox.app.Presupuestos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

import com.androidbegin.filterlistviewimg.R;
import com.durox.app.Models.Visitas_model;
import com.durox.app.Visitas.Visitas;
import com.durox.app.Visitas.Visitas_ListView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class Presupuestos_Main extends Activity 
{

	// Declare Variables
	ListView list;
	Visitas_ListView adapter;
	EditText editsearch;
	
	String[] nombre;
	String[] direccion;
	int[] imagen;
	ArrayList<Visitas> arraylist = new ArrayList<Visitas>();
	SQLiteDatabase db;
	
	Visitas_model mVisitas;
	Cursor cVisitas;
	private String jsonResult;
	
	
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.visitas_listview);
		
		visitas_lista();
	}
	
	
    // Async Task to access the web
	private class JsonReadTask extends AsyncTask<String, Void, String> {
		
		protected String doInBackground(String... params) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(params[0]);
			
			try {
				HttpResponse response = httpclient.execute(httppost);
				jsonResult = inputStreamToString(
				response.getEntity().getContent()).toString();
			}
	 
			catch (ClientProtocolException e) {
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
			CargarClientes();
		}
	}// end async task
	
	
	
	
	public void actualizar_visitas(View view) {
		JsonReadTask taskclientes = new JsonReadTask();
		String url = "http://10.0.2.2/durox/index.php/actualizaciones/getVisitas/";
		taskclientes.execute(new String[] { url });
		
		mVisitas = new Visitas_model(db);
		
		Cursor c = mVisitas.getNuevos();
		
		if(c.getCount() > 0) {
			while(c.moveToNext()){
				
				JsonSetTask task = new JsonSetTask(
					c.getString(2),
					c.getString(3),
					c.getString(4),
					c.getString(5),
					c.getString(6),
					c.getString(7)
				);
				
				String url2 = "http://10.0.2.2/durox/index.php/actualizaciones/setVisita/";
		 		task.execute(new String[] { url2 });
    		}		
		} else {
			Toast.makeText(this, "No hay visitas cargadas", Toast.LENGTH_SHORT).show();
		}
	}
	
	
	
	
	private class JsonSetTask extends AsyncTask<String, Void, String> {
		String id_vendedor;
		String id_cliente;
		String descripcion;
		String id_epoca_visita;
		String valoracion;
		String fecha;
		
 		public JsonSetTask(
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
	 		 			"Error" + e.toString(), Toast.LENGTH_SHORT).show();
	 			
	 		} catch (IOException e) {
	 			Toast.makeText(getApplicationContext(), 
	 		 			"Error" + e.toString(), Toast.LENGTH_SHORT).show();
	 		}
	 			
	 		return null;
	 	}
	  
		protected void onPostExecute(String result) {
	 	}
	}
	
	
	
	// build hash set for list view
	public void CargarClientes() {
		try {
			JSONObject jsonResponse = new JSONObject(jsonResult);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("visitas");
	 			
			if(jsonMainNode.length() > 0){
	 			mVisitas.truncate();
	 			Toast.makeText(getApplicationContext(), 
	 					"Registros registros "+jsonMainNode.length() , Toast.LENGTH_SHORT).show();
	 		}
	 			  
	 		for (int i = 0; i < jsonMainNode.length(); i++) {
	 			JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
	 			
	 			String id_visita = jsonChildNode.optString("id_visita");
				String id_vendedor = jsonChildNode.optString("id_vendedor");
				String id_cliente = jsonChildNode.optString("id_cliente");
				String descripcion = jsonChildNode.optString("descripcion");
				String id_epoca_visita = jsonChildNode.optString("id_epoca_visita");
				String valoracion = jsonChildNode.optString("valoracion");
				String fecha = jsonChildNode.optString("fecha");
				String id_origen = jsonChildNode.optString("id_origen");
				String visto = jsonChildNode.optString("visto");
				String date_add = jsonChildNode.optString("date_add");
				String date_upd = jsonChildNode.optString("date_upd");
				String eliminado = jsonChildNode.optString("eliminado");
				String user_add = jsonChildNode.optString("user_add");
				String user_upd = jsonChildNode.optString("user_upd");
	 				
	 			mVisitas.insert(
	 				id_visita,
	 				id_vendedor,
	 				id_cliente,
	 				descripcion,
	 				id_epoca_visita,
	 				valoracion,
	 				fecha,
	 				id_origen,
	 				visto,
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
	 		
	 	visitas_lista();
	}
	 	
	
	
	public void visitas_lista(){
		db = openOrCreateDatabase("Durox_app", Context.MODE_PRIVATE, null);
			
		mVisitas = new Visitas_model(db);
			
		cVisitas = mVisitas.getVisitas();
			
		String[] nombre = new String[cVisitas.getCount()];
		String[] epoca = new String[cVisitas.getCount()];
		String[] fecha = new String[cVisitas.getCount()];
			
		int j = 0;
					
		if(cVisitas.getCount() > 0)
		{
			while(cVisitas.moveToNext())
			{
				nombre[j] = cVisitas.getString(1);
				epoca[j] = cVisitas.getString(2);
				fecha[j] = cVisitas.getString(3);
				j = j + 1;
			}				
			
			// Locate the ListView in listview_main.xml
			list = (ListView) findViewById(R.id.lvVisitas);
			arraylist.clear();

			for (int i = 0; i < nombre.length; i++) 
			{
				Visitas wp = new Visitas(
						nombre[i],
						epoca[i], 
						fecha[i]
				);
					
				// Binds all strings into an array
				arraylist.add(wp);
			}
			
			// Pass results to ListViewAdapter Class
			adapter = new Visitas_ListView(this, arraylist);
			
			// Binds the Adapter to the ListView
			list.setAdapter(adapter);
			
			// Locate the EditText in listview_main.xml
			editsearch = (EditText) findViewById(R.id.et_pBuscar);
			

			// Capture Text in EditText
			editsearch.addTextChangedListener(new TextWatcher() {
				public void afterTextChanged(Editable arg0) {
					String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
					adapter.filter(text);
				}
		
				public void beforeTextChanged(
					CharSequence arg0, 
					int arg1,
					int arg2, 
					int arg3) {
				}
		
				public void onTextChanged(
					CharSequence arg0, 
					int arg1, 
					int arg2,
					int arg3) {
				}
			});	
		}
	 }	
}

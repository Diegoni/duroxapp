package com.durox.app.Presupuestos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
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
	}
	
	// build hash set for list view
	public void CargarClientes() {
		try {
			JSONObject jsonResponse = new JSONObject(jsonResult);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("visitas");
	 			
			String id_cliente;
			String id_epoca_visita;
			String fecha;
	 		String descripcion;
	 		float valoracion;
	 			
	 		if(jsonMainNode.length() > 0){
	 			mVisitas.truncate();
	 			Toast.makeText(getApplicationContext(), 
	 					"Registros registros "+jsonMainNode.length() , Toast.LENGTH_SHORT).show();
	 		}
	 			  
	 		for (int i = 0; i < jsonMainNode.length(); i++) {
	 			JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
	 			id_cliente = jsonChildNode.optString("id_cliente");
	 			id_epoca_visita = jsonChildNode.optString("id_epoca_visita");
	 			fecha = jsonChildNode.optString("fecha");
	 			descripcion = jsonChildNode.optString("descripcion");
	 			valoracion = (float) jsonChildNode.optDouble("valoracion");
	 				
	 			mVisitas.insert(id_cliente, id_epoca_visita, fecha, valoracion, descripcion);
	 			
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
		db = openOrCreateDatabase("Duroxapp", Context.MODE_PRIVATE, null);
			
		mVisitas = new Visitas_model(db);
			
		cVisitas = mVisitas.getRegistros();
			
		String[] nombre = new String[cVisitas.getCount()];
		String[] epoca = new String[cVisitas.getCount()];
		String[] fecha = new String[cVisitas.getCount()];
			
		int j = 0;
					
		if(cVisitas.getCount() == 0)
		{
			//showMessage("Error", "No records found");
			return;
		}
			
		while(cVisitas.moveToNext())
		{
			nombre[j] = cVisitas.getString(0);
			epoca[j] = cVisitas.getString(1);
			fecha[j] = cVisitas.getString(2);
			j = j + 1;
		}				
			
		// Locate the ListView in listview_main.xml
		list = (ListView) findViewById(R.id.listview);

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
		editsearch = (EditText) findViewById(R.id.search);

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

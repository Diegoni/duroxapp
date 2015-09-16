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

import com.example.durox_app.R;
import com.durox.app.Config_durox;
import com.durox.app.MenuActivity;
import com.durox.app.Models.Vendedores_model;
import com.durox.app.Models.Visitas_model;
import com.durox.app.Visitas.Visitas;
import com.durox.app.Visitas.Visitas_ListView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class Presupuestos_Main extends MenuActivity {

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
	Vendedores_model mVendedor;
	String id_vendedor;
	private String jsonResult;
	
	Config_durox config;
	ProgressDialog pDialog;
	
	
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.visitas_listview);
		config = new Config_durox();
		db = openOrCreateDatabase(config.getDatabase(), Context.MODE_PRIVATE, null);
		
		setTitle("Presupuestos - Selección de la visita");
        getActionBar().setIcon(R.drawable.menupresupuesto);
		
		mVendedor = new Vendedores_model(db);
		id_vendedor = mVendedor.getID();
		
		visitas_lista();
	}
	
	
	public void actualizar_visitas(View view) {
		pDialog = new ProgressDialog(this);
        pDialog.setMessage("Actualizando....");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
        
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
				
				String url2 = config.getIp(db)+"/actualizaciones/setVisita/";
		 		task.execute(new String[] { url2 });
    		}		
		} else {
			Toast.makeText(this, config.msjNoRegistros("visitas"), Toast.LENGTH_SHORT).show();
		}
		
		
		JsonReadTask taskVisitas = new JsonReadTask();
		String url = config.getIp(db)+"/actualizaciones/getVisitas/";
		taskVisitas.execute(new String[] { url });
		
		
		visitas_lista();
	}
	
	
	private class JsonReadTask extends AsyncTask<String, Void, String> {
			
		protected String doInBackground(String... params) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(params[0]);
			
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
	 		pairs.add(new BasicNameValuePair("id_vendedor", id_vendedor));
				
			try { 		
				httppost.setEntity(new UrlEncodedFormEntity(pairs));
 				
	 			HttpResponse response = httpclient.execute(httppost);
				jsonResult = inputStreamToString(
				response.getEntity().getContent()).toString();
			} catch (ClientProtocolException e) {
				Log.e("Error ClientProtocolException", e.toString());
				return "ClientProtocolException "+e.toString();
			} catch (IOException e) {
				Log.e("Error IOException", e.toString());
				return "IOException "+e.toString();
			}
				
			return "ok";
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
				config.msjError(e.toString()), Toast.LENGTH_LONG).show();
			}
				
			return answer;
		}
	 
		protected void onPostExecute(String result) {
			pDialog.dismiss();
				
			if(result.equals("ok")){
				CargarVisitas();
			}else{
				Toast.makeText(getApplicationContext(), 
						result, Toast.LENGTH_LONG).show();
			}
		}
	}// end async task
	
	
	
	
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
	 				
	 			//HttpResponse response; 
	 			httpclient.execute(httppost);
	 		} catch (ClientProtocolException e) {
	 			Toast.makeText(getApplicationContext(), 
	 		 			config.msjError(e.toString()) , Toast.LENGTH_SHORT).show();
	 			
	 		} catch (IOException e) {
	 			Toast.makeText(getApplicationContext(), 
	 					config.msjError(e.toString()), Toast.LENGTH_SHORT).show();
	 		}
	 			
	 		return null;
	 	}
	  
		protected void onPostExecute(String result) {
	 	}
	}
	
	
	
	// build hash set for list view
	public void CargarVisitas() {
		try {
			JSONObject jsonResponse = new JSONObject(jsonResult);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("visitas");
	 			
			if(jsonMainNode.length() > 0){
	 			mVisitas.truncate();
	 			Toast.makeText(getApplicationContext(), 
	 					config.msjActualizandoRegistros() , Toast.LENGTH_SHORT).show();
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
	 		
	 		Toast.makeText(getApplicationContext(), 
	 		 		config.msjRegistrosActualizados(" visitas "+jsonMainNode.length()), Toast.LENGTH_SHORT).show();
	 	} catch (JSONException e) {
	 		Toast.makeText(getApplicationContext(), 
	 			config.msjError(e.toString()) , Toast.LENGTH_SHORT).show();
	 	}
	 		
	 	visitas_lista();
	}
	 	
	
	
	public void visitas_lista(){
		mVisitas = new Visitas_model(db);
			
		cVisitas = mVisitas.getVisitas();
			
		String[] id_visita = new String[cVisitas.getCount()];
		String[] nombre = new String[cVisitas.getCount()];
		String[] epoca = new String[cVisitas.getCount()];
		String[] fecha = new String[cVisitas.getCount()];
		int[] foto = new int[cVisitas.getCount()];
			
		int j = 0;
					
		if(cVisitas.getCount() > 0)
		{
			while(cVisitas.moveToNext())
			{
				id_visita[j] = cVisitas.getString(0);
				nombre[j] = cVisitas.getString(1);
				epoca[j] = cVisitas.getString(2);
				fecha[j] = cVisitas.getString(3);
				foto[j] = R.drawable.visitas; 
				j = j + 1;
			}				
			
			// Locate the ListView in listview_main.xml
			list = (ListView) findViewById(R.id.lvVisitas);
			arraylist.clear();

			for (int i = 0; i < nombre.length; i++) 
			{
				Visitas wp = new Visitas(
						id_visita[i],
						nombre[i],
						epoca[i], 
						fecha[i], 
						foto[i]
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
	
	
	public void presupuestos_lista(View view){
		Intent intent = new Intent(Presupuestos_Main.this, Presupuestos_Lista.class);
		startActivity(intent);
	}
	
	
}

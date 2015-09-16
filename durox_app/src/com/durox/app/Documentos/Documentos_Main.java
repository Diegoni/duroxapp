package com.durox.app.Documentos;

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

import com.example.durox_app.R;
import com.durox.app.Config_durox;
import com.durox.app.MenuActivity;
import com.durox.app.Models.Documentos_model;


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

public class Documentos_Main extends MenuActivity {

	// Declare Variables
	ListView list;
	Documentos_ListView adapterd;
	EditText editsearch;
	
	String[] nombre;
	String[] direccion;
	int[] imagen;
	ArrayList<Documentos> arraylistd = new ArrayList<Documentos>();
	SQLiteDatabase db;
	
	Documentos_model mDocumentos;
	Cursor cDocumentos;
	private String jsonResult;
	Config_durox config;
	ProgressDialog pDialog;
	
	
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		setTitle("Documentos - Lista");
        getActionBar().setIcon(R.drawable.menudocumentos);
		
		Intent intent = getIntent();
		String dNombre = intent.getStringExtra("nombre");
		
		if(dNombre != null){
			Toast.makeText(this, "Descargando "+dNombre, Toast.LENGTH_LONG).show();
		}
		
		config = new Config_durox();
		
		documentos_lista();
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
				CargarDocumentos();
			}else{
				Toast.makeText(getApplicationContext(), 
						result, Toast.LENGTH_LONG).show();
			}
		}
	}// end async task
	
	
	public void actualizar_documentos(View view) {
		pDialog = new ProgressDialog(this);
        pDialog.setMessage("Actualizando....");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
        
		JsonReadTask taskclientes = new JsonReadTask();
		String url = config.getIp(db)+"/actualizaciones/getDocumentos/";
		taskclientes.execute(new String[] { url });
	}
	
	
	
	
	// build hash set for list view
	public void CargarDocumentos() {
		try {
			JSONObject jsonResponse = new JSONObject(jsonResult);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("documentos");
	 			
			if(jsonMainNode.length() > 0){
				mDocumentos.createTable();
				mDocumentos.truncate();
	 			Toast.makeText(getApplicationContext(), 
	 					config.msjActualizandoRegistros() , Toast.LENGTH_SHORT).show();
	 		}
	 			  
	 		for (int i = 0; i < jsonMainNode.length(); i++) {
	 			JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
	 			
	 			String id_back = jsonChildNode.optString("id_documento");
	 			String nombre = jsonChildNode.optString("nombre");
				String documento = jsonChildNode.optString("documento");
				String date_add = jsonChildNode.optString("date_add");
				String date_upd = jsonChildNode.optString("date_upd");
				String eliminado = jsonChildNode.optString("eliminado");
				String user_add = jsonChildNode.optString("user_add");
				String user_upd = jsonChildNode.optString("user_upd");
	 				
				mDocumentos.insert(
					id_back,
					nombre,
					documento,
	 				date_add,
	 				date_upd,
	 				eliminado,
	 				user_add,
	 				user_upd
	 			);
	 			
	 		}
	 		
	 		Toast.makeText(getApplicationContext(), 
	 		 		config.msjRegistrosActualizados(" documentos "+jsonMainNode.length()), Toast.LENGTH_SHORT).show();
	 		
	 	} catch (JSONException e) {
	 		Toast.makeText(getApplicationContext(), 
	 			"Error" + e.toString(), Toast.LENGTH_SHORT).show();
	 	}
	 		
	 	
	 		
	 	documentos_lista();
	}
	 	
	
	
	public void documentos_lista(){
		setContentView(R.layout.documentos_listview);
		db = openOrCreateDatabase(config.getDatabase(), Context.MODE_PRIVATE, null);
			
		mDocumentos = new Documentos_model(db);
			
		cDocumentos = mDocumentos.getRegistros();
			
		int j = 0;
					
		if(cDocumentos.getCount() > 0)
		{
			String[] id_documento = new String[cDocumentos.getCount()];
			String[] nombre = new String[cDocumentos.getCount()];
			String[] epoca = new String[cDocumentos.getCount()];
			String[] fecha = new String[cDocumentos.getCount()];
			int[] foto = new int[cDocumentos.getCount()];
			
			while(cDocumentos.moveToNext())
			{
				id_documento[j] = cDocumentos.getString(0);
				nombre[j] = cDocumentos.getString(1);
				epoca[j] = cDocumentos.getString(2);
				fecha[j] = cDocumentos.getString(3);
				foto[j] = R.drawable.documentos; 
				j = j + 1;
			}				
			
			// Locate the ListView in listview_main.xml
			list = (ListView) findViewById(R.id.lvDocumentos);
			arraylistd.clear();

			for (int i = 0; i < nombre.length; i++) 
			{
				Documentos wp = new Documentos(
						id_documento[i],
						nombre[i],
						epoca[i], 
						foto[i]
				);
					
				// Binds all strings into an array
				arraylistd.add(wp);
			}
			
			// Pass results to ListViewAdapter Class
			adapterd = new Documentos_ListView(this, arraylistd);
			
			// Binds the Adapter to the ListView
			list.setAdapter(adapterd);
			
			// Locate the EditText in listview_main.xml
			editsearch = (EditText) findViewById(R.id.search);
			

			// Capture Text in EditText
			editsearch.addTextChangedListener(new TextWatcher() {
				public void afterTextChanged(Editable arg0) {
					String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
					adapterd.filter(text);
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

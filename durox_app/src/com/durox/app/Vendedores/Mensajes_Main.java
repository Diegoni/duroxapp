package com.durox.app.Vendedores;

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

import com.durox.app.Config_durox;
import com.durox.app.MenuActivity;
import com.durox.app.Models.Mensajes_model;
import com.durox.app.Models.Vendedores_model;
import com.example.durox_app.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Mensajes_Main extends MenuActivity {
	String[] asunto;
	String[] id_back;
	String[] mensaje;
	String[] date_add;
	
	int[] imagen;
	
	ListView list;
	Mensajes_ListView adapter;
	
	EditText editsearch;

	ArrayList<Mensajes> arraylist = new ArrayList<Mensajes>();
	SQLiteDatabase db;
		
	String truncate;
	String sql;
	String id_vendedor;
	Cursor c;
	int j;	
			
	private String jsonResult;
		
	TextView content;
	
	Config_durox config;
	Mensajes_model mMensajes;
	Vendedores_model mVendedor;
	
	ProgressDialog pDialog;
	Button btn_entrada;
	Button btn_enviados;
	
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
        config = new Config_durox();
        db = openOrCreateDatabase(config.getDatabase(), Context.MODE_PRIVATE, null);
        
        mVendedor = new Vendedores_model(db);
        id_vendedor = mVendedor.getID();
        
        setTitle("Mensajes - Lista");
        getActionBar().setIcon(R.drawable.menuproductos);
       
		mensajes_lista("entrada");
		
	}
	
	
	/*---------------------------------------------------------------------------------
	Clases Para Leer el Json y actualizar tablas
	---------------------------------------------------------------------------------*/    
	
	// Async Task to access the web
	private class JsonReadTask extends AsyncTask<String, Void, String> {
		String carga;
		
		public JsonReadTask(String carga) {
			this.carga = carga;
		}
		
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
				if(carga == "mensajes"){
					CargarMensajes();
				}
			}else{
				Toast.makeText(getApplicationContext(), 
						result, Toast.LENGTH_LONG).show();
			}
			
		}
	}
	
	
	public void actualizar_mensajes(View view) {
		pDialog = new ProgressDialog(this);
        pDialog.setMessage("Actualizando....");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
        
        Mensajes_model mMensaje = new Mensajes_model(db);
        Cursor cMensaje = mMensaje.getNuevos();
    	
		if(cMensaje.getCount() > 0) {
			while(cMensaje.moveToNext()){
				JsonSetTask task = new JsonSetTask(
					cMensaje.getString(0),		//+ 0"id_mensaje VARCHAR, "
					cMensaje.getString(2),		//+ 2"asunto VARCHAR, "
					cMensaje.getString(3),		//+ 3"mensaje VARCHAR, "
					cMensaje.getString(4),		//+ 4"id_origen VARCHAR, "
					cMensaje.getString(5),		//+ 5"id_mensaje_padre VARCHAR, "
					cMensaje.getString(6)		//+ 6"visto VARCHAR, "
				);
				
				String url2 = config.getIp(db)+"/actualizaciones/setMensajes/";
		 		task.execute(new String[] { url2 });
    		}		
		} else {
			Toast.makeText(this, config.msjNoRegistros("mensajes"), Toast.LENGTH_SHORT).show();
		}
		
		JsonReadTask taskmensajes = new JsonReadTask("mensajes");
		String url = config.getIp(db)+"/actualizaciones/getMensajes/";
		taskmensajes.execute(new String[] { url });
	}
	
	
	
	private class JsonSetTask extends AsyncTask<String, Void, String> {
		String id_mensaje;
		String asunto;
		String mensaje;
		String id_origen;
		String id_mensaje_padre;
		String visto;
		
 		public JsonSetTask(
 				String id_mensaje,
 				String asunto,
 				String mensaje,
 				String id_origen,
 				String id_mensaje_padre,
 				String visto) {
 			this.id_mensaje = id_mensaje;
 			this.asunto = asunto;
 			this.mensaje = mensaje;
 			this.id_origen = id_origen;
 			this.id_mensaje_padre = id_mensaje_padre;
 			this.visto = visto;
 		}
 		
	
 		protected String doInBackground(String... params) {
	 		HttpClient httpclient = new DefaultHttpClient();
	 		HttpPost httppost = new HttpPost(params[0]);
	 			 		
	 		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
	 		pairs.add(new BasicNameValuePair("id_mensaje", id_mensaje));
	 		pairs.add(new BasicNameValuePair("asunto", asunto));
	 		pairs.add(new BasicNameValuePair("mensaje", mensaje));
	 		pairs.add(new BasicNameValuePair("id_origen", id_origen));
	 		pairs.add(new BasicNameValuePair("id_mensaje_padre", id_mensaje_padre));
	 		pairs.add(new BasicNameValuePair("visto", visto));
	 		pairs.add(new BasicNameValuePair("id_vendedor", id_vendedor));
	 		 				 			
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
		
		
	public void CargarMensajes() {
		try {
			JSONObject jsonResponse = new JSONObject(jsonResult);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("mensajes");
			 			
			if(jsonMainNode.length() > 0){
				mMensajes.truncate();
			}
			  
			for (int i = 0; i < jsonMainNode.length(); i++) {
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
				
				String id_back = jsonChildNode.optString("id_mensaje");
				String asunto = jsonChildNode.optString("asunto");
				String mensaje = jsonChildNode.optString("mensaje");
				String id_origen = jsonChildNode.optString("id_origen");
				String id_mensaje_padre = jsonChildNode.optString("id_mensaje_padre");
				String visto = jsonChildNode.optString("visto");
				String date_add = jsonChildNode.optString("date_add");
				String date_upd = jsonChildNode.optString("date_upd");
				String eliminado = jsonChildNode.optString("eliminado");
				String user_add = jsonChildNode.optString("user_add");
				String user_upd = jsonChildNode.optString("user_upd");
				
				mMensajes.insert(
						id_back, 
						asunto, 
						mensaje, 
						id_origen, 
						id_mensaje_padre, 
						visto, 
						date_add, 
						date_upd, 
						eliminado, 
						user_add, 
						user_upd
				);
				
			}
			
			Toast.makeText(getApplicationContext(), 
					config.msjRegistrosActualizados(" mensajes "+jsonMainNode.length()), Toast.LENGTH_SHORT).show();
			
		} catch (JSONException e) {
			Toast.makeText(getApplicationContext(), 
			config.msjError(e.toString()), Toast.LENGTH_SHORT).show();
		}
		mensajes_lista("entrada");   
	}
	
	
	public void entrada_mensajes(View view) {
		mensajes_lista("entrada");
	}
	
	public void enviados_mensajes(View view) {
		mensajes_lista("enviados");
	}
	
	
	
	public void mensajes_lista(String e){
		setContentView(R.layout.mensajes_listview);
		btn_entrada = (Button) findViewById(R.id.btn_entrada);
	    btn_enviados = (Button) findViewById(R.id.btn_enviados);
	    String id_origen;
		
		if(e.equals("entrada")){
			btn_entrada.setEnabled(false);
			id_origen = "2";
		}else{
			btn_enviados.setEnabled(false);
			id_origen = "1";
		}
		
		mMensajes = new Mensajes_model(db);
		mMensajes.createTable();
		c = mMensajes.getMensajes(e);
		
		int cantidad_mensajes = c.getCount();
		
		asunto = new String[cantidad_mensajes];
		id_back = new String[cantidad_mensajes];
		mensaje = new String[cantidad_mensajes];
		date_add = new String[cantidad_mensajes];
		imagen = new int[cantidad_mensajes];
		
		int j = 0;
			
		if(c.getCount() > 0){
			while(c.moveToNext()){
				id_back[j] = c.getString(0);
				asunto[j] = c.getString(1);
				mensaje[j] = c.getString(2);
				date_add[j] = c.getString(3);
				imagen[j] = R.drawable.productos; 
				j = j + 1;
			}	
			
			// Locate the ListView in listview_main.xml
			list = (ListView) findViewById(R.id.listview);
			arraylist.clear();
			
			for (int i = 0; i < asunto.length; i++) {
				Mensajes wp = new Mensajes(
						id_back[i],
						asunto[i],
						mensaje[i], 
						date_add[i],
						id_origen,
						imagen[i]
				);
				
				arraylist.add(wp);
			}
			
			adapter = new Mensajes_ListView(this, arraylist);
			list.setAdapter(adapter);
			
			editsearch = (EditText) findViewById(R.id.search);
			
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

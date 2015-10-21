package com.durox.app.Vendedores;

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

import com.durox.app.Config_durox;
import com.durox.app.MenuActivity;
import com.durox.app.Models.Mensajes_model;
import com.durox.app.Models.Vendedores_model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class Mensajes_Update extends MenuActivity {
	Config_durox config;
	Context mContext;
	SQLiteDatabase db;
	
	private String jsonResult;
	
	Mensajes_model mMensajes;
	Vendedores_model mVendedor;
	String id_vendedor;
	
	public Mensajes_Update(SQLiteDatabase db_enviada, Context context) {
		config = new Config_durox();
		db = db_enviada;
		mContext = context;
		
		mVendedor = new Vendedores_model(db);
		id_vendedor = mVendedor.getID();
	}
	
	
	
	public void actualizar_registros() {
		Mensajes_model mMensaje = new Mensajes_model(db);
        Cursor cMensaje = mMensaje.getNuevos();
        
		if(cMensaje.getCount() > 0) {
			while(cMensaje.moveToNext()){
				JsonSetTask task = new JsonSetTask(
					cMensaje.getString(0),		// 0 "id_mensaje VARCHAR, "
					cMensaje.getString(2),		// 2 "asunto VARCHAR, "
					cMensaje.getString(3),		// 3 "mensaje VARCHAR, "
					cMensaje.getString(4),		// 4 "id_origen VARCHAR, "
					cMensaje.getString(5),		// 5 "id_mensaje_padre VARCHAR, "
					cMensaje.getString(6)		// 6 "visto VARCHAR, "
				);
				
				String url2 = config.getIp(db)+"/actualizaciones/setMensajes/";
		 		task.execute(new String[] { url2 });
    		}		
		} else {
			Toast.makeText(mContext, config.msjNoRegistros("mensajes"), Toast.LENGTH_SHORT).show();
		}
		
		JsonReadTask taskmensajes = new JsonReadTask();
		String url = config.getIp(db)+"/actualizaciones/getMensajes/";
		taskmensajes.execute(new String[] { url });
		
	}
	
	
	/*---------------------------------------------------------------------------------
	Clases Para Leer el Json y actualizar tablas
	---------------------------------------------------------------------------------*/    
	
	
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
				Toast.makeText(mContext, 
				config.msjError(e.toString()), Toast.LENGTH_LONG).show();
			}
			
			return answer;
		}
		
		protected void onPostExecute(String result) {
			if(result.equals("ok")){
				CargarMensajes();
			}else{
				Toast.makeText(mContext, 
						result, Toast.LENGTH_LONG).show();
			}
		}
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
	 			httpclient.execute(httppost);
	 		} catch (ClientProtocolException e) {
	 			Toast.makeText(mContext, 
	 		 			config.msjError(e.toString()) , Toast.LENGTH_SHORT).show();
	 			
	 		} catch (IOException e) {
	 			Toast.makeText(mContext, 
	 					config.msjError(e.toString()), Toast.LENGTH_SHORT).show();
	 		}
	 			
	 		return null;
	 	}
	  
		protected void onPostExecute(String result) {
	 	}
	}
		
		
	public void CargarMensajes() {
		String subjet;
		subjet = "mensajes";
		mMensajes = new Mensajes_model(db);
		
		try {
			JSONObject jsonResponse = new JSONObject(jsonResult);
			JSONArray jsonMainNode = jsonResponse.optJSONArray(subjet);
			 			
			if(jsonMainNode.length() > 0){
				mMensajes.truncate();
			}
			  
			for (int i = 0; i < jsonMainNode.length(); i++) {
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
					
				mMensajes.insert(
					jsonChildNode.optString("id_mensaje"),
					jsonChildNode.optString("asunto"),
					jsonChildNode.optString("mensaje"),
					jsonChildNode.optString("id_origen"),
					jsonChildNode.optString("id_mensaje_padre"),
					jsonChildNode.optString("visto"),
					jsonChildNode.optString("date_add"),
					jsonChildNode.optString("date_upd"),
					jsonChildNode.optString("eliminado"),
					jsonChildNode.optString("user_add"),
					jsonChildNode.optString("user_upd")
				);
				
			}
			
			Toast.makeText(mContext, 
				config.msjRegistrosActualizados(subjet+" "+jsonMainNode.length()), Toast.LENGTH_SHORT).show();
			
		} catch (JSONException e) {
			Toast.makeText(mContext, 
				config.msjError(e.toString()), Toast.LENGTH_SHORT).show();
		}
	}
}
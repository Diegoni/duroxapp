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

import com.durox.app.Config_durox;
import com.durox.app.MenuActivity;
import com.durox.app.Models.Vendedores_model;
import com.durox.app.Models.Visitas_model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class Visitas_Update extends MenuActivity {
	
	Config_durox config;
	Context mContext;
	SQLiteDatabase db;
	
	private String jsonResult;
	
	Visitas_model mVisitas;
	Vendedores_model mVendedor;
	String id_vendedor;
	
	public Visitas_Update(SQLiteDatabase db_enviada, Context context) {
		config = new Config_durox();
		db = db_enviada;
		mContext = context;
		
		mVendedor = new Vendedores_model(db);
	    id_vendedor = mVendedor.getID();
	}
	
	
	
	public void actualizar_registros() {
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
			Toast.makeText(mContext, config.msjNoRegistros("visitas"), Toast.LENGTH_SHORT).show();
		}
		
		
		JsonReadTask taskVisitas = new JsonReadTask();
		String url = config.getIp(db)+"/actualizaciones/getVisitas/";
		taskVisitas.execute(new String[] { url });
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
				Toast.makeText(mContext, 
				config.msjError(e.toString()), Toast.LENGTH_LONG).show();
			}
				
			return answer;
		}
	 
		protected void onPostExecute(String result) {
			if(result.equals("ok")){
				CargarVisitas();
			}else{
				Toast.makeText(mContext, 
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
	
	
	
	// build hash set for list view
	public void CargarVisitas() {
		
		String subjet;
		
		subjet = "visitas";
		try {
			JSONObject jsonResponse = new JSONObject(jsonResult);
			JSONArray jsonMainNode = jsonResponse.optJSONArray(subjet);
	 		
			if(jsonMainNode != null){
				if(jsonMainNode.length() > 0){
		 			mVisitas.truncate();
		 		}
		 			  
		 		for (int i = 0; i < jsonMainNode.length(); i++) {
		 			JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
		 				
		 			mVisitas.insert(
		 				jsonChildNode.optString("id_visita"),
		 				jsonChildNode.optString("id_vendedor"),
		 				jsonChildNode.optString("id_cliente"),
		 				jsonChildNode.optString("descripcion"),
		 				jsonChildNode.optString("id_epoca_visita"),
		 				jsonChildNode.optString("valoracion"),
		 				jsonChildNode.optString("fecha"),
		 				jsonChildNode.optString("id_origen"),
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
			}
	 	} catch (JSONException e) {
	 		Toast.makeText(mContext, 
	 			config.msjError(e.toString()) , Toast.LENGTH_SHORT).show();
	 	}
	}
}

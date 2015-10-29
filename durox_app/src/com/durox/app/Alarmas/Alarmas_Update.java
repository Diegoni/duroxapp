package com.durox.app.Alarmas;

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

import com.durox.app.Config_durox;
import com.durox.app.MenuActivity;
import com.durox.app.Models.Alarmas_model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;
import android.util.Log;

public class Alarmas_Update extends MenuActivity {
	Config_durox config;
	Context mContext;
	SQLiteDatabase db;
	
	private String jsonResult;
	
	
	public Alarmas_Update(SQLiteDatabase db_enviada, Context context) {
		config = new Config_durox();
		db = db_enviada;
		mContext = context;
	}
	
	
	
	public void actualizar_registros() {
		JsonReadTask taskalarmas = new JsonReadTask();
		String url = config.getIp(db)+"/actualizaciones/getAlarmas/";
		taskalarmas.execute(new String[] { url });
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
				 e.printStackTrace();
				Toast.makeText(mContext, 
				config.msjError(e.toString()), Toast.LENGTH_LONG).show();
			}
			
			return answer;
		}
		
		protected void onPostExecute(String result) {

			if(result.equals("ok")){
				CargarAlarmas();
			}else{
				Toast.makeText(mContext, 
						result, Toast.LENGTH_LONG).show();
			}
		}
	}
		
	
		
	public void CargarAlarmas() {
		
		String subjet;
		subjet = "alarmas";
		Alarmas_model mAlarmas = new Alarmas_model(db);
		mAlarmas.createTable();
		mAlarmas.truncate();
		
		try {
			JSONObject jsonResponse = new JSONObject(jsonResult);
			JSONArray jsonMainNode = jsonResponse.optJSONArray(subjet);
			  
			for (int i = 0; i < jsonMainNode.length(); i++) {
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
				mAlarmas.insert(
					jsonChildNode.optString("id_alarma"), 
					jsonChildNode.optString("id_tipo_alarma"), 
					jsonChildNode.optString("mensaje"), 
					jsonChildNode.optString("id_creador"), 
					jsonChildNode.optString("id_origen"), 
					jsonChildNode.optString("visto_back"), 
					jsonChildNode.optString("visto_front"), 
					jsonChildNode.optString("id_front"),
					jsonChildNode.optString("date_add"), 
					jsonChildNode.optString("date_upd"), 
					jsonChildNode.optString("eliminado"), 
					jsonChildNode.optString("user_add"), 
					jsonChildNode.optString("user_upd"));
				Log.e("PASO", "10");
			}
			Log.e("PASO", "11");
			Toast.makeText(mContext, 
				config.msjRegistrosActualizados(subjet+" "+jsonMainNode.length()), Toast.LENGTH_SHORT).show();
			Log.e("PASO", "12");
			
		} catch (JSONException e) {
			Toast.makeText(mContext, 
				config.msjError(e.toString()), Toast.LENGTH_SHORT).show();
		}
		
		
		subjet = "tipos_alarmas";
		try {
			JSONObject jsonResponse = new JSONObject(jsonResult);
			JSONArray jsonMainNode = jsonResponse.optJSONArray(subjet);
			  
			for (int i = 0; i < jsonMainNode.length(); i++) {
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
				
				mAlarmas.insertTipos(
					jsonChildNode.optString("id_tipo_alarma"), 
					jsonChildNode.optString("nombre"), 
					jsonChildNode.optString("tipo_alarma"), 
					jsonChildNode.optString("color"), 
					jsonChildNode.optString("date_add"), 
					jsonChildNode.optString("date_upd"), 
					jsonChildNode.optString("eliminado"), 
					jsonChildNode.optString("user_add"), 
					jsonChildNode.optString("user_upd"));
			}
			
			Toast.makeText(mContext, 
					config.msjRegistrosActualizados(subjet+" "+jsonMainNode.length()), Toast.LENGTH_SHORT).show();
			
		} catch (JSONException e) {
			Toast.makeText(mContext, 
					config.msjError(e.toString()), Toast.LENGTH_SHORT).show();
		}
		
		/*
			Tablas de sincronización	
		*/
		
		subjet = "sin_alarmas_clientes";
		try {
			JSONObject jsonResponse = new JSONObject(jsonResult);
			JSONArray jsonMainNode = jsonResponse.optJSONArray(subjet);
			  
			for (int i = 0; i < jsonMainNode.length(); i++) {
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
				
				mAlarmas.insertSin(
					jsonChildNode.optString("id_sin_alarma_cliente"), 
					jsonChildNode.optString("id_alarma"), 
					jsonChildNode.optInt("id_front_alarma"),
					jsonChildNode.optString("id_cliente"),
					jsonChildNode.optInt("id_front_tabla"),
					jsonChildNode.optString("date_add"), 
					jsonChildNode.optString("date_upd"), 
					jsonChildNode.optString("eliminado"), 
					jsonChildNode.optString("user_add"), 
					jsonChildNode.optString("user_upd"), 
					subjet);
			}
			
			Toast.makeText(mContext, 
					config.msjRegistrosActualizados(subjet+" "+jsonMainNode.length()), Toast.LENGTH_SHORT).show();
			
		} catch (JSONException e) {
			Toast.makeText(mContext, 
					config.msjError(e.toString()), Toast.LENGTH_SHORT).show();
		}
		
		
		subjet = "sin_alarmas_pedidos";
		try {
			JSONObject jsonResponse = new JSONObject(jsonResult);
			JSONArray jsonMainNode = jsonResponse.optJSONArray(subjet);
			  
			for (int i = 0; i < jsonMainNode.length(); i++) {
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
				
				mAlarmas.insertSin(
					jsonChildNode.optString("id_sin_alarma_cliente"), 
					jsonChildNode.optString("id_alarma"),
					jsonChildNode.optInt("id_front_alarma"),
					jsonChildNode.optString("id_pedido"),
					jsonChildNode.optInt("id_front_tabla"),
					jsonChildNode.optString("date_add"), 
					jsonChildNode.optString("date_upd"), 
					jsonChildNode.optString("eliminado"), 
					jsonChildNode.optString("user_add"), 
					jsonChildNode.optString("user_upd"), 
					subjet);
			}
			
			Toast.makeText(mContext, 
					config.msjRegistrosActualizados(subjet+" "+jsonMainNode.length()), Toast.LENGTH_SHORT).show();
			
		} catch (JSONException e) {
			Toast.makeText(mContext, 
					config.msjError(e.toString()), Toast.LENGTH_SHORT).show();
		}
		
		
		
		subjet = "sin_alarmas_productos";
		try {
			JSONObject jsonResponse = new JSONObject(jsonResult);
			JSONArray jsonMainNode = jsonResponse.optJSONArray(subjet);
			  
			for (int i = 0; i < jsonMainNode.length(); i++) {
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
				
				mAlarmas.insertSin(
					jsonChildNode.optString("id_sin_alarma_cliente"), 
					jsonChildNode.optString("id_alarma"), 
					jsonChildNode.optInt("id_front_alarma"),
					jsonChildNode.optString("id_producto"),
					jsonChildNode.optInt("id_front_tabla"),
					jsonChildNode.optString("date_add"), 
					jsonChildNode.optString("date_upd"), 
					jsonChildNode.optString("eliminado"), 
					jsonChildNode.optString("user_add"), 
					jsonChildNode.optString("user_upd"), 
					subjet);
			}
			
			Toast.makeText(mContext, 
					config.msjRegistrosActualizados(subjet+" "+jsonMainNode.length()), Toast.LENGTH_SHORT).show();
			
		} catch (JSONException e) {
			Toast.makeText(mContext, 
					config.msjError(e.toString()), Toast.LENGTH_SHORT).show();
		}
		
		
		subjet = "sin_alarmas_presupuestos";
		try {
			JSONObject jsonResponse = new JSONObject(jsonResult);
			JSONArray jsonMainNode = jsonResponse.optJSONArray(subjet);
			  
			for (int i = 0; i < jsonMainNode.length(); i++) {
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
				
				mAlarmas.insertSin(
					jsonChildNode.optString("id_sin_alarma_cliente"), 
					jsonChildNode.optString("id_alarma"), 
					jsonChildNode.optInt("id_front_alarma"),
					jsonChildNode.optString("id_presupuesto"),
					jsonChildNode.optInt("id_front_tabla"),
					jsonChildNode.optString("date_add"), 
					jsonChildNode.optString("date_upd"), 
					jsonChildNode.optString("eliminado"), 
					jsonChildNode.optString("user_add"), 
					jsonChildNode.optString("user_upd"), 
					subjet);
			}
			
			Toast.makeText(mContext, 
					config.msjRegistrosActualizados(subjet+" "+jsonMainNode.length()), Toast.LENGTH_SHORT).show();
			
		} catch (JSONException e) {
			Toast.makeText(mContext, 
					config.msjError(e.toString()), Toast.LENGTH_SHORT).show();
		}
		
		
		subjet = "sin_alarmas_visitas";
		try {
			JSONObject jsonResponse = new JSONObject(jsonResult);
			JSONArray jsonMainNode = jsonResponse.optJSONArray(subjet);
			  
			for (int i = 0; i < jsonMainNode.length(); i++) {
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
				
				mAlarmas.insertSin(
					jsonChildNode.optString("id_sin_alarma_cliente"), 
					jsonChildNode.optString("id_alarma"), 
					jsonChildNode.optInt("id_front_alarma"),
					jsonChildNode.optString("id_visita"),
					jsonChildNode.optInt("id_front_tabla"),
					jsonChildNode.optString("date_add"), 
					jsonChildNode.optString("date_upd"), 
					jsonChildNode.optString("eliminado"), 
					jsonChildNode.optString("user_add"), 
					jsonChildNode.optString("user_upd"), 
					subjet);
			}
			
			Toast.makeText(mContext, 
					config.msjRegistrosActualizados(subjet+" "+jsonMainNode.length()), Toast.LENGTH_SHORT).show();
			
		} catch (JSONException e) {
			Toast.makeText(mContext, 
					config.msjError(e.toString()), Toast.LENGTH_SHORT).show();
		}
		
		
		subjet = "sin_alarmas_vendedores";
		try {
			JSONObject jsonResponse = new JSONObject(jsonResult);
			JSONArray jsonMainNode = jsonResponse.optJSONArray(subjet);
			  
			for (int i = 0; i < jsonMainNode.length(); i++) {
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
				
				mAlarmas.insertSin(
					jsonChildNode.optString("id_sin_alarma_cliente"), 
					jsonChildNode.optString("id_alarma"), 
					jsonChildNode.optInt("id_front_alarma"),
					jsonChildNode.optString("id_vendedor"),
					jsonChildNode.optInt("id_front_tabla"),
					jsonChildNode.optString("date_add"), 
					jsonChildNode.optString("date_upd"), 
					jsonChildNode.optString("eliminado"), 
					jsonChildNode.optString("user_add"), 
					jsonChildNode.optString("user_upd"), 
					subjet);
			}
			
			Toast.makeText(mContext, 
					config.msjRegistrosActualizados(subjet+" "+jsonMainNode.length()), Toast.LENGTH_SHORT).show();
			
		} catch (JSONException e) {
			Toast.makeText(mContext, 
					config.msjError(e.toString()), Toast.LENGTH_SHORT).show();
		}
	   
	}
}

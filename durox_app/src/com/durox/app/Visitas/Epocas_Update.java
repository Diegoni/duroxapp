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

import com.durox.app.Config_durox;
import com.durox.app.MenuActivity;
import com.durox.app.Models.Epocas_model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class Epocas_Update extends MenuActivity {
	Config_durox config;
	Context mContext;
	SQLiteDatabase db;
	
	private String jsonResult;
	
	public Epocas_Update(SQLiteDatabase db_enviada, Context context) {
		config = new Config_durox();
		db = db_enviada;
		mContext = context;
	}
	
	
	
	public void actualizar_registros() {
		JsonReadTask taskclientes = new JsonReadTask();
 		String url = config.getIp(db)+"/actualizaciones/getEpocas/";
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
 				config.msjError(e.toString()) , Toast.LENGTH_LONG).show();
 			}
 			
 			return answer;
 		}
  
 		protected void onPostExecute(String result) {
 			if(result.equals("ok")){
 				CargarEpocas();
 			}else{
				Toast.makeText(mContext, 
						result, Toast.LENGTH_LONG).show();
			}
 		}
 	}
 	
 	
 
  	public void CargarEpocas() {
  		String subjet;
  		subjet = "epocas";
  		
  		try {
  			JSONObject jsonResponse = new JSONObject(jsonResult);
  			JSONArray jsonMainNode = jsonResponse.optJSONArray(subjet);
  			
  			if(jsonMainNode != null){
	  			Epocas_model mEpocas = new Epocas_model(db);
	  			mEpocas.getRegistros();
	  			
	  			if(jsonMainNode.length() > 0){
	  				mEpocas.truncate();
	  			}
	  			  
	  			for (int i = 0; i < jsonMainNode.length(); i++) {
	  				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
	  				mEpocas.insert(
	  					jsonChildNode.optString("id_epoca_visita"),
	  					jsonChildNode.optString("epoca"),
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

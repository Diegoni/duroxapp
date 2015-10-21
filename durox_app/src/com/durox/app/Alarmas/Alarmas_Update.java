package com.durox.app.Alarmas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
import com.durox.app.Documentos.Documentos;
import com.durox.app.Documentos.Documentos_ListView;
import com.durox.app.Models.Alarmas_model;
import com.durox.app.Models.Clientes_model;
import com.durox.app.Models.Documentos_model;
import com.durox.app.Models.Monedas_model;
import com.durox.app.Models.Productos_model;
import com.durox.app.Productos.Productos;
import com.durox.app.Productos.Productos_ListView;
import com.example.durox_app.R;

import android.R.array;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class Alarmas_Update extends MenuActivity {
	
	// Declare Variables
	ListView list;
	Productos_ListView adapterp;
	Documentos_ListView adapterd;
	EditText editsearch;
	
	String[] nombre;
	String[] id_back;
	String[] precio;
	String[] moneda;
	String[] codigo;
	int[] imagen;
	ArrayList<Productos> arraylistp = new ArrayList<Productos>();
	ArrayList<Documentos> arraylistd = new ArrayList<Documentos>();
	SQLiteDatabase db;
		
	String truncate;
	String sql;
	Cursor c;
	int j;	
			
	private String jsonResult;
		
	Clientes_model mCliente;
	Productos_model mProductos;
		
	TextView content;
		
	Documentos_model mDocumentos;
	Cursor cDocumentos;
		
	Config_durox config;
	ProgressDialog pDialog;
	
	CharSequence orden;
	CharSequence filtro;
	
	String subjet;
	
	Context mContext;
	
	public Alarmas_Update(SQLiteDatabase db_enviada, Context context) {
		config = new Config_durox();
		db = db_enviada;
		mContext = context;
	}
	
	
	
	public void actualizar_productos() {
		JsonReadTask taskalarmas = new JsonReadTask("alarmas");
		String url = config.getIp(db)+"/actualizaciones/getAlarmas/";
		taskalarmas.execute(new String[] { url });
	}
	
	

	private class JsonReadTask extends AsyncTask<String, Void, String> {
		String carga;
		
		public JsonReadTask(String carga) {
			this.carga = carga;
		}
		
		protected String doInBackground(String... params) {
			Log.e("PASO", "1");
			HttpClient httpclient = new DefaultHttpClient();
			Log.e("PASO", "2");
			HttpPost httppost = new HttpPost(params[0]);
			Log.e("PASO", "3");
				
			try { 				
				Log.e("PASO", "4");
				HttpResponse response = httpclient.execute(httppost);
				Log.e("PASO", "5");
				jsonResult = inputStreamToString(
				response.getEntity().getContent()).toString();
				Log.e("PASO", "6");
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
			Log.e("PASO", "7a");
			String rLine = "";
			Log.e("PASO", "7b");
			StringBuilder answer = new StringBuilder();
			Log.e("PASO", "7c");
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			Log.e("PASO", "7d");
		
			try {
				Log.e("PASO", "7e");
				while ((rLine = rd.readLine()) != null) {
					Log.e("PASO", "7f");
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
				Log.e("PASO", "7");
				CargarAlarmas();
			}else{
				Toast.makeText(mContext, 
						result, Toast.LENGTH_LONG).show();
			}
		}
	}
		
	
		
	public void CargarAlarmas() {
		
		subjet = "alarmas";
		Log.e("PASO", "8");
		Alarmas_model mAlarmas = new Alarmas_model(db);
		Log.e("PASO", "9");
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
					jsonChildNode.optString("id_cliente"), 
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
					jsonChildNode.optString("id_pedido"), 
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
					jsonChildNode.optString("id_producto"), 
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
					jsonChildNode.optString("id_presupuesto"), 
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
					jsonChildNode.optString("id_visita"), 
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
					jsonChildNode.optString("id_vendedor"), 
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

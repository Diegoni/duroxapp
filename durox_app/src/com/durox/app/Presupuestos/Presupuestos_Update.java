package com.durox.app.Presupuestos;

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
import com.durox.app.Models.Alarmas_model;
import com.durox.app.Models.Condiciones_pago_model;
import com.durox.app.Models.Estados_presupuesto_model;
import com.durox.app.Models.Lineas_Presupuestos_model;
import com.durox.app.Models.Modos_pago_model;
import com.durox.app.Models.Presupuestos_model;
import com.durox.app.Models.Tiempos_entrega_model;
import com.durox.app.Models.Vendedores_model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class Presupuestos_Update extends MenuActivity {
	
	Presupuestos_model mPresupuesto;
	Estados_presupuesto_model mEstados;
	Lineas_Presupuestos_model mLineas;
	Alarmas_model mAlarmas;
	Modos_pago_model mModos;
	
	Config_durox config;
	Context mContext;
	SQLiteDatabase db;
	
	private String jsonResult;
	
	Vendedores_model mVendedor;
	String id_vendedor;
	
	public Presupuestos_Update(SQLiteDatabase db_enviada, Context context) {
		config = new Config_durox();
		db = db_enviada;
		mContext = context;
		
		mVendedor = new Vendedores_model(db);
	    id_vendedor = mVendedor.getID();
	}
	
	
	
	public void actualizar_registros() {
		mPresupuesto = new Presupuestos_model(db);
    	mEstados = new Estados_presupuesto_model(db);
    	mLineas = new Lineas_Presupuestos_model(db);
    	mAlarmas = new Alarmas_model(db);
    	mModos = new Modos_pago_model(db);
		
    	Cursor cLineas = mLineas.getNuevos();
    	Cursor cPresupuestos = mPresupuesto.getNuevos();
    	Cursor cAlarmas = mAlarmas.getNuevos();
    	Cursor cAlarmasSin = mAlarmas.getNuevosSin("presupuestos");
    	Cursor cModos = mModos.getNuevos();
    	
    	// Exportamos los presupuestos		
		
		if(cPresupuestos.getCount() > 0) {
			while(cPresupuestos.moveToNext()){
				JsonSetTask task = new JsonSetTask(
					cPresupuestos.getString(0),		//+ 0"id_presupuesto VARCHAR, "
					cPresupuestos.getString(2),		//+ 2"id_visita VARCHAR, "
					cPresupuestos.getString(3),		//+ 3"id_cliente VARCHAR, "
					cPresupuestos.getString(4),		//+ 4"id_vendedor VARCHAR, "
					cPresupuestos.getString(5),		//+ 5"id_estado_presupuesto VARCHAR, "
					cPresupuestos.getString(6),		// 6+ "`id_condicion_pago` VARCHAR, "
					cPresupuestos.getString(7),		//7+ "`id_tiempo_entrega` VARCHAR, "
					cPresupuestos.getString(8),		//8+ "`nota_publica` VARCHAR, "
					cPresupuestos.getString(9),		//+ 9"total VARCHAR, "
					cPresupuestos.getString(11),	//+ 8"id_origen VARCHAR, "
					cPresupuestos.getString(12),	//+ 9" aprobado_back VARCHAR, "
					cPresupuestos.getString(13),	//+ 10" aprobado_front VARCHAR, "
					cPresupuestos.getString(14),	//+ 11" visto_back VARCHAR, "
					cPresupuestos.getString(15),	//+ 12" visto_front VARCHAR, "
					cPresupuestos.getString(18)		//+ 15"eliminado VARCHAR, "
				);
				
			
				
				
				String url2 = config.getIp(db)+"/actualizaciones/setPresupuestos/";
		 		task.execute(new String[] { url2 });
    		}		
		} else {
			Toast.makeText(mContext, config.msjNoRegistros("presupuestos"), Toast.LENGTH_SHORT).show();
		}
		
		// Exportamos las lineas de los presupuestos
		
		if(cLineas.getCount() > 0) {
			while(cLineas.moveToNext()){
				JsonSetTaskLineas task = new JsonSetTaskLineas(
					cLineas.getString(2), 	//2+ "id_temporario VARCHAR, "	
					cLineas.getString(4), 	//4+ "id_producto VARCHAR, "
					cLineas.getString(5), 	//5+ "precio VARCHAR, "
					cLineas.getString(6), 	//6+ "id_moneda VARCHAR, "
					cLineas.getString(7), 	//7+ "valor_moneda VARCHAR, "
					cLineas.getString(8), 	//8+ "cantidad VARCHAR, "
					cLineas.getString(9), 	//9+ "subtotal VARCHAR, "
					cLineas.getString(10), 	//10+ "id_estado_producto_presupuesto VARCHAR, "
					cLineas.getString(11), 	//11+ "comentario VARCHAR, "
					cLineas.getString(14) 	//14+ "eliminado VARCHAR, "
				);
				
				String url2 = config.getIp(db)+"/actualizaciones/setLineasPresupuestos/";
		 		task.execute(new String[] { url2 });
    		}		
		} 
		
		
		// Exportamos las alarmas
		
		if(cAlarmas.getCount() > 0) {
			while(cAlarmas.moveToNext()){
				JsonSetTaskAlarmas task = new JsonSetTaskAlarmas(
					cAlarmas.getString(0),	//	2+ "`id_alarma` VARCHAR,"
					cAlarmas.getString(2),	//	2+ "`id_tipo_alarma` VARCHAR,"
					cAlarmas.getString(3),	//	3+ "`mensaje` VARCHAR,"
					cAlarmas.getString(4),	//	4+ "`id_creador` VARCHAR,"
					cAlarmas.getString(5),	//	5+ "`id_origen` VARCHAR,"
					cAlarmas.getString(6),	//	6+ "`visto_back` VARCHAR,"
					cAlarmas.getString(7)	//	7+ "`visto_front` VARCHAR,"
				);
				
				String url2 = config.getIp(db)+"/actualizaciones/setAlarmas/";
		 		task.execute(new String[] { url2 });
    		}		
		} 
		
		
		// Exportamos las alarmas sin 
		
		if(cAlarmasSin.getCount() > 0) {
			while(cAlarmasSin.moveToNext()){
				JsonSetTaskAlarmasSin task = new JsonSetTaskAlarmasSin(
					cAlarmasSin.getString(2),	//2 + "`id_alarma` VARCHAR,"
					cAlarmasSin.getString(3),	//3 + "`id_front_alarma` VARCHAR,"
					cAlarmasSin.getString(4),	//4 + "`id_presupuesto` VARCHAR,"
					cAlarmasSin.getString(5)	//5 + "`id_front_tabla` VARCHAR,"
				);
					
				String url2 = config.getIp(db)+"/actualizaciones/setAlarmasSin/";
		 		task.execute(new String[] { url2 });
			}		
		} 
		
		
		// Exportamos las Modos
		
		if(cModos.getCount() > 0) {
			while(cModos.moveToNext()){
				JsonSetTaskModosSin task = new JsonSetTaskModosSin(
					cModos.getString(2),	//2 + " `id_presupuesto` VARCHAR,"
					cModos.getString(2),	//3 + " `id_presupuesto_front` VARCHAR,"
					cModos.getString(2)	//4 + " `id_modo_pago` VARCHAR,"
				);
							
				String url2 = config.getIp(db)+"/actualizaciones/setModosSin/";
				task.execute(new String[] { url2 });
			}		
		} 
		
		
		
		// Importamos los presupuestos
		JsonReadTask taskpresupuestos = new JsonReadTask();
		String urlPresupuestos = config.getIp(db)+"/actualizaciones/getPresupuestos/";
		taskpresupuestos.execute(new String[] { urlPresupuestos });
	}
    
    
    
    // Clase para leer presupuestos
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
				Toast.makeText(mContext, 
				config.msjError(e.toString()), Toast.LENGTH_LONG).show();
			}
			
			return answer;
		}
 
		protected void onPostExecute(String result) {
			if(result.equals("ok")){
				CargarPresupuestos();
			}else{
				Toast.makeText(mContext, 
						result, Toast.LENGTH_LONG).show();
			}
		}
	}
	
	
	
	private class JsonSetTask extends AsyncTask<String, Void, String> {
		String id_presupuesto;
		String id_visita;
		String id_cliente;
		String id_vendedor;
		String id_estado_presupuesto;
		String id_condicion_pago;
		String id_tiempo_entrega;
		String nota_publica;
		String total;
		String id_origen;
		String aprobado_back;
		String aprobado_front;
		String visto_back;
		String visto_front;
		String eliminado;
		
 		public JsonSetTask(
 				String id_presupuesto,
 				String id_visita,
				String id_cliente,
				String id_vendedor,
				String id_estado_presupuesto,
				String id_condicion_pago,
				String id_tiempo_entrega,
				String nota_publica,
				String total,
				String id_origen,
				String aprobado_back,
				String aprobado_front,
				String visto_back,
				String visto_front,
				String eliminado) {
 			this.id_presupuesto = id_presupuesto;
 			this.id_visita = id_visita;
 			this.id_cliente = id_cliente;
 			this.id_vendedor = id_vendedor;
 			this.id_estado_presupuesto = id_estado_presupuesto;
 			this.id_condicion_pago = id_condicion_pago;
 			this.id_tiempo_entrega = id_tiempo_entrega;
 			this.nota_publica = nota_publica;
 			this.total = total;
 			this.id_origen = id_origen;
 			this.aprobado_back = aprobado_back;
 			this.aprobado_front = aprobado_front;
 			this.visto_back = visto_back;
 			this.visto_front = visto_front;
 			this.eliminado = eliminado;	
 		}
 		
	
 		protected String doInBackground(String... params) {
	 		HttpClient httpclient = new DefaultHttpClient();
	 		HttpPost httppost = new HttpPost(params[0]);
	 		
	 		
	 		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
	 		pairs.add(new BasicNameValuePair("id_front", id_presupuesto));
	 		pairs.add(new BasicNameValuePair("id_visita", id_visita));
	 		pairs.add(new BasicNameValuePair("id_cliente", id_cliente));
	 		pairs.add(new BasicNameValuePair("id_vendedor", id_vendedor));
	 		pairs.add(new BasicNameValuePair("id_estado_presupuesto", id_estado_presupuesto));
	 		pairs.add(new BasicNameValuePair("id_condicion_pago", id_condicion_pago));
	 		pairs.add(new BasicNameValuePair("id_tiempo_entrega", id_tiempo_entrega));
	 		pairs.add(new BasicNameValuePair("nota_publica", nota_publica));
	 		pairs.add(new BasicNameValuePair("total", total));
	 		pairs.add(new BasicNameValuePair("id_origen", id_origen));
	 		pairs.add(new BasicNameValuePair("aprobado_back", aprobado_back));
	 		pairs.add(new BasicNameValuePair("aprobado_front", aprobado_front));
	 		pairs.add(new BasicNameValuePair("visto_back", visto_back));
	 		pairs.add(new BasicNameValuePair("visto_front", visto_front));
	 		pairs.add(new BasicNameValuePair("eliminado", eliminado));	
 				 			
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
	
	
	
	
	private class JsonSetTaskLineas extends AsyncTask<String, Void, String> {
		String id_temporario;		
		String id_producto;
		String precio;
		String id_moneda;		
		String valor_moneda;
		String cantidad;
		String subtotal;
		String id_estado_producto_presupuesto;
		String comentario;
		String eliminado;
		
 		public JsonSetTaskLineas(
 				String id_temporario,		
				String id_producto,
				String precio,
				String id_moneda,
				String valor_moneda,
				String cantidad,
				String subtotal,
				String id_estado_producto_presupuesto,
				String comentario,
				String eliminado
				) {
 			this.id_temporario = id_temporario;		
			this.id_producto = id_producto;
			this.precio = precio;
			this.id_moneda = id_moneda;
			this.valor_moneda = valor_moneda;
			this.cantidad = cantidad;
			this.subtotal = subtotal;
			this.id_estado_producto_presupuesto = id_estado_producto_presupuesto;
			this.comentario = comentario;
			this.eliminado = eliminado;
 		}
 		
	
		protected String doInBackground(String... params) {
	 		HttpClient httpclient = new DefaultHttpClient();
	 		HttpPost httppost = new HttpPost(params[0]);
	 			 		
	 		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
	 		pairs.add(new BasicNameValuePair("id_temporario", id_temporario));		
	 		pairs.add(new BasicNameValuePair("id_producto", id_producto));
	 		pairs.add(new BasicNameValuePair("precio", precio));
	 		pairs.add(new BasicNameValuePair("id_moneda", id_moneda));
	 		pairs.add(new BasicNameValuePair("valor_moneda", valor_moneda));
	 		pairs.add(new BasicNameValuePair("cantidad", cantidad));
	 		pairs.add(new BasicNameValuePair("subtotal", subtotal));
	 		pairs.add(new BasicNameValuePair("id_estado_producto_presupuesto", id_estado_producto_presupuesto));
	 		pairs.add(new BasicNameValuePair("comentario", comentario));
	 		pairs.add(new BasicNameValuePair("eliminado", eliminado));	
 				 			
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
	
	
	
	
	private class JsonSetTaskModosSin extends AsyncTask<String, Void, String> {
		String id_presupuesto;
		String id_presupuesto_front;
		String id_modo_pago;
		
 		public JsonSetTaskModosSin(
 				String id_presupuesto,
 				String id_presupuesto_front,
 				String id_modo_pago
				) {
 			this.id_presupuesto = id_presupuesto;		
			this.id_presupuesto_front = id_presupuesto_front;
			this.id_modo_pago = id_modo_pago;
 		}
 		protected String doInBackground(String... params) {
	 		HttpClient httpclient = new DefaultHttpClient();
	 		HttpPost httppost = new HttpPost(params[0]);
	 		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
	 		pairs.add(new BasicNameValuePair("id_presupuesto", id_presupuesto));		
	 		pairs.add(new BasicNameValuePair("id_presupuesto_front", id_presupuesto_front));
	 		pairs.add(new BasicNameValuePair("id_modo_pago", id_modo_pago));	
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
	
	
	
	private class JsonSetTaskAlarmas extends AsyncTask<String, Void, String> {
		String id_alarma;
		String id_tipo_alarma;
		String mensaje;
		String id_creador;
		String id_origen;
		String visto_back;
		String visto_front;
		
 		public JsonSetTaskAlarmas(
 				String id_alarma,
 				String id_tipo_alarma,
 				String mensaje,
 				String id_creador,
 				String id_origen,
 				String visto_back,
 				String visto_front
				) {
 			this.id_alarma = id_alarma;
 			this.id_tipo_alarma = id_tipo_alarma;		
			this.mensaje = mensaje;
			this.id_creador = id_creador;
			this.id_origen = id_origen;
			this.visto_back = visto_back;
			this.visto_front = visto_front;
 		}
 		protected String doInBackground(String... params) {
	 		HttpClient httpclient = new DefaultHttpClient();
	 		HttpPost httppost = new HttpPost(params[0]);
	 		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
	 		pairs.add(new BasicNameValuePair("id_alarma", id_alarma));
	 		pairs.add(new BasicNameValuePair("id_tipo_alarma", id_tipo_alarma));		
	 		pairs.add(new BasicNameValuePair("mensaje", mensaje));
	 		pairs.add(new BasicNameValuePair("id_creador", id_creador));
	 		pairs.add(new BasicNameValuePair("id_origen", id_origen));
	 		pairs.add(new BasicNameValuePair("visto_back", visto_back));
	 		pairs.add(new BasicNameValuePair("visto_front", visto_front));
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
	
	
	
	
	private class JsonSetTaskAlarmasSin extends AsyncTask<String, Void, String> {
		String id_alarma;
		String id_front_alarma;
		String id_presupuesto;
		String id_front_tabla;
		
 		public JsonSetTaskAlarmasSin(
 				String id_alarma,
 				String id_front_alarma,
 				String id_presupuesto,
 				String id_front_tabla
				) {
 			this.id_alarma = id_alarma;		
			this.id_front_alarma = id_front_alarma;
			this.id_presupuesto = id_presupuesto;
			this.id_front_tabla = id_front_tabla;
 		}
		protected String doInBackground(String... params) {
	 		HttpClient httpclient = new DefaultHttpClient();
	 		HttpPost httppost = new HttpPost(params[0]);
	 		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
	 		pairs.add(new BasicNameValuePair("id_alarma", id_alarma));		
	 		pairs.add(new BasicNameValuePair("id_front_alarma", id_front_alarma));
	 		pairs.add(new BasicNameValuePair("id_presupuesto", id_presupuesto));
	 		pairs.add(new BasicNameValuePair("id_front_tabla", id_front_tabla));
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
	
	
	
	
	// build hash set for list view
	public void CargarPresupuestos() {
		// Carga de presupuestos
		
		String subjet;
		
		subjet = "presupuestos";
		try {
			JSONObject jsonResponse = new JSONObject(jsonResult);
			JSONArray jsonMainNode = jsonResponse.optJSONArray(subjet);
				 			
			if(jsonMainNode.length() > 0){
				mPresupuesto.truncate();
	 		}
	 			  
	 		for (int i = 0; i < jsonMainNode.length(); i++) {
	 			JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
	 			
		 		mPresupuesto.insert(
		 			jsonChildNode.optString("id_presupuesto"),
				 	jsonChildNode.optString("id_visita"),
				 	jsonChildNode.optString("id_cliente"),
				 	jsonChildNode.optString("id_vendedor"),
				 	jsonChildNode.optString("id_estado_presupuesto"),
				 	jsonChildNode.optString("id_condicion_pago"),
				 	jsonChildNode.optString("id_tiempo_entrega"),
				 	jsonChildNode.optString("nota_publica"),
				 	jsonChildNode.optString("total"),
				 	jsonChildNode.optString("fecha"),
				 	jsonChildNode.optString("id_origen"),
				 	jsonChildNode.optString("aprobado_back"),
				 	jsonChildNode.optString("aprobado_front"),
				 	jsonChildNode.optString("visto_back"),
				 	jsonChildNode.optString("visto_front"),
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
	 			config.msjError(e.toString()) , Toast.LENGTH_SHORT).show();
	 	}
		
		
		// Carga de lineas de presupuestos
			
		subjet = "lineas_presupuestos";	
		try {
			JSONObject jsonResponse = new JSONObject(jsonResult);
			JSONArray jsonMainNode = jsonResponse.optJSONArray(subjet);
			
			mLineas.delete();
					
			if(jsonMainNode.length() > 0){
				mLineas.delete();
			}
		 	
	 		for (int i = 0; i < jsonMainNode.length(); i++) {
	 			JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
		 	
	 			mLineas.insert(
	 				jsonChildNode.optString("id_linea_producto_presupuesto"),
	 		 		jsonChildNode.optString("id_presupuesto"),
	 		 		jsonChildNode.optString("id_producto"),
	 		 		jsonChildNode.optString("precio"),
	 		 		jsonChildNode.optString("id_moneda"),
	 		 		jsonChildNode.optString("valor_moneda"),
	 		 		jsonChildNode.optString("cantidad"),
	 		 		jsonChildNode.optString("subtotal"),
	 		 		jsonChildNode.optString("id_estado_producto_presupuesto"),
	 		 		jsonChildNode.optString("id_front"),
	 		 		jsonChildNode.optString("comentario"),
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
	 			config.msjError(e.toString()) , Toast.LENGTH_SHORT).show();
	 	}
		
		
		// Carga de estados presupuestos
			
		subjet = "estados_presupuestos";
		try {
			JSONObject jsonResponse = new JSONObject(jsonResult);
			JSONArray jsonMainNode = jsonResponse.optJSONArray(subjet);
		 			
			if(jsonMainNode.length() > 0){
				mEstados.delete();
	 		}
		 			  
	 		for (int i = 0; i < jsonMainNode.length(); i++) {
	 			JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
	 			
	 			mEstados.insert(
		 			jsonChildNode.optString("id_estado_presupuesto"),
			 		jsonChildNode.optString("estado"),
			 		jsonChildNode.optString("text"),
			 		jsonChildNode.optString("eliminar_cliente"),
			 		jsonChildNode.optString("eliminar_vendedor"),
			 		jsonChildNode.optString("eliminar_visita"),
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
	 			config.msjError(e.toString()) , Toast.LENGTH_SHORT).show();
	 	}	
		
		
		
		subjet = "condiciones_pago";
		Condiciones_pago_model mCondiciones = new Condiciones_pago_model(db);
		try {
			JSONObject jsonResponse = new JSONObject(jsonResult);
			JSONArray jsonMainNode = jsonResponse.optJSONArray(subjet);
		 			
			if(jsonMainNode.length() > 0){
				mCondiciones.truncate();
	 		}
		 			  
	 		for (int i = 0; i < jsonMainNode.length(); i++) {
	 			JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
	 			
	 			mCondiciones.insert(
		 			jsonChildNode.optString("id_condicion_pago"),
			 		jsonChildNode.optString("condicion_pago"),
			 		jsonChildNode.optString("dias"),
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
	 			config.msjError(e.toString()) , Toast.LENGTH_SHORT).show();
	 	}
		
		
		subjet = "modos_pago";
		Modos_pago_model mModos = new Modos_pago_model(db);
		try {
			JSONObject jsonResponse = new JSONObject(jsonResult);
			JSONArray jsonMainNode = jsonResponse.optJSONArray(subjet);
		 			
			if(jsonMainNode.length() > 0){
				mModos.truncate();
	 		}
		 			  
	 		for (int i = 0; i < jsonMainNode.length(); i++) {
	 			JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
	 			
	 			mModos.insert(
		 			jsonChildNode.optString("id_modo_pago"),
			 		jsonChildNode.optString("modo_pago"),
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
	 			config.msjError(e.toString()) , Toast.LENGTH_SHORT).show();
	 	}
		
		
		subjet = "sin_presupuestos_modos";
		try {
			JSONObject jsonResponse = new JSONObject(jsonResult);
			JSONArray jsonMainNode = jsonResponse.optJSONArray(subjet);
		 			  
	 		for (int i = 0; i < jsonMainNode.length(); i++) {
	 			JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
	 			
	 			mModos.insertSin(
	 				jsonChildNode.optString("id_sin_presupuesto_modo"),
	 				jsonChildNode.optString("id_presupuesto"), 
	 				jsonChildNode.optInt("id_presupuesto_front"),
	 				jsonChildNode.optString("id_modo_pago"), 
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
	 			config.msjError(e.toString()) , Toast.LENGTH_SHORT).show();
	 	}
				

		subjet = "tiempos_entrega";
		Tiempos_entrega_model mTiempos = new Tiempos_entrega_model(db);
		try {
			JSONObject jsonResponse = new JSONObject(jsonResult);
			JSONArray jsonMainNode = jsonResponse.optJSONArray(subjet);
		 			
			if(jsonMainNode.length() > 0){
				mTiempos.truncate();
	 		}
		 			  
	 		for (int i = 0; i < jsonMainNode.length(); i++) {
	 			JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
	 			
	 			mTiempos.insert(
		 			jsonChildNode.optString("id_tiempo_entrega"),
			 		jsonChildNode.optString("tiempo_entrega"),
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
	 			config.msjError(e.toString()) , Toast.LENGTH_SHORT).show();
	 	}
	}
}

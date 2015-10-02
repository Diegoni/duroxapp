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
import com.durox.app.Models.Estados_presupuesto_model;
import com.durox.app.Models.Lineas_Presupuestos_model;
import com.durox.app.Models.Presupuestos_model;
import com.durox.app.Models.Vendedores_model;


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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
 
public class Presupuestos_Lista extends MenuActivity {
	
	// Declare Variables
	ListView list;
	Presupuestos_ListView adapter;
	EditText editsearch;
	
	String[] c_nombre;
	String[] p_nombre;
	String[] total;
	String[] estado;
	String[] fecha;
	int[] foto;
	String[] id_presupuesto;
	String[] id_back;
	String[] id_pback;
	int[] imagen;
	ArrayList<Presupuesto> arraylist = new ArrayList<Presupuesto>();
	SQLiteDatabase db;
	
	String truncate;
	String sql;
	Cursor c;
	int j;	
		
	private String jsonResult;
	
	TextView content;
	Config_durox config;
	
	Presupuestos_model mPresupuesto;
	Lineas_Presupuestos_model mLineas;
	Estados_presupuesto_model mEstados;
	ProgressDialog pDialog;
	Vendedores_model mVendedor;
	String id_vendedor;
   
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.presupuestos_listview);
        
        setTitle("Presupuestos - Lista");
        getActionBar().setIcon(R.drawable.menupresupuesto);
        
        config = new Config_durox();
        db = openOrCreateDatabase(config.getDatabase(), Context.MODE_PRIVATE, null);
        
        mVendedor = new Vendedores_model(db);
        id_vendedor = mVendedor.getID();        
        
        presupuestos_lista();
   }
    
    
   public void presupuestos_lista(){
 		setContentView(R.layout.presupuestos_listview);
 		db = openOrCreateDatabase(config.getDatabase(), Context.MODE_PRIVATE, null);
    	
    	Presupuestos_model mPresupuestos = new Presupuestos_model(db);
 		c = mPresupuestos.getRegistros();
		
		int cantidad_presupuestos = c.getCount();
		
		id_back = new String[cantidad_presupuestos];
		id_presupuesto = new String[cantidad_presupuestos];
		c_nombre = new String[cantidad_presupuestos];
		total = new String[cantidad_presupuestos];
		estado = new String[cantidad_presupuestos];
		fecha = new String[cantidad_presupuestos];
		foto = new int[cantidad_presupuestos];
		
		int j = 0;
				
		if(c.getCount() > 0){
			while(c.moveToNext()){
				id_back[j] = c.getString(0);
				c_nombre[j] = c.getString(2);
				id_presupuesto[j] = c.getString(5);
    			total[j] = c.getString(3);
    			estado[j] = c.getString(6);
    			fecha[j] = c.getString(4);
    			foto[j] = R.drawable.presupuesto; 
    		
    			j = j + 1;
    		}
			
			list = (ListView) findViewById(R.id.lvPresupuestos);
    		arraylist.clear();

    		for (int i = 0; i < c_nombre.length; i++) {
    			Presupuesto wp = new Presupuesto(
    					id_back[i],
    					c_nombre[i],
    					total[i],
    					estado[i], 
    					fecha[i],
    					foto[i],
    					id_presupuesto[i]
    			);
    			
    			arraylist.add(wp);
    		}
    		
    		adapter = new Presupuestos_ListView(this, arraylist);
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
	
	    			@Override
	    			public void onTextChanged(
	    					CharSequence arg0, 
	    					int arg1, 
	    					int arg2,
	    					int arg3) {
	    			}
    		});
		}
 	}	
    
    
    public void actualizar_presupuestos(View view) {
		pDialog = new ProgressDialog(this);
        pDialog.setMessage("Actualizando....");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    	
    	mPresupuesto = new Presupuestos_model(db);
    	mEstados = new Estados_presupuesto_model(db);
    	mLineas = new Lineas_Presupuestos_model(db);
		
    	Cursor cLineas = mLineas.getNuevos();
    	Cursor cPresupuestos = mPresupuesto.getNuevos();
    	
    	// Exportamos los presupuestos		
		
		if(cPresupuestos.getCount() > 0) {
			while(cPresupuestos.moveToNext()){
				JsonSetTask task = new JsonSetTask(
					cPresupuestos.getString(0),		//+ 0"id_presupuesto VARCHAR, "
					cPresupuestos.getString(2),		//+ 2"id_visita VARCHAR, "
					cPresupuestos.getString(3),		//+ 3"id_cliente VARCHAR, "
					cPresupuestos.getString(4),		//+ 4"id_vendedor VARCHAR, "
					cPresupuestos.getString(5),		//+ 5"id_estado_presupuesto VARCHAR, "
					cPresupuestos.getString(6),		//+ 6"total VARCHAR, "
					cPresupuestos.getString(8),		//+ 8"id_origen VARCHAR, "
					cPresupuestos.getString(9),		//+ 9" aprobado_back VARCHAR, "
					cPresupuestos.getString(10),	//+ 10" aprobado_front VARCHAR, "
					cPresupuestos.getString(11),	//+ 11" visto_back VARCHAR, "
					cPresupuestos.getString(12),	//+ 12" visto_front VARCHAR, "
					cPresupuestos.getString(15)		//+ 15"eliminado VARCHAR, "
				);
				
				String url2 = config.getIp(db)+"/actualizaciones/setPresupuestos/";
		 		task.execute(new String[] { url2 });
    		}		
		} else {
			Toast.makeText(this, config.msjNoRegistros("presupuestos"), Toast.LENGTH_SHORT).show();
		}
		
		// Exportamos las lineas de los presupuestos
		
		if(cLineas.getCount() > 0) {
			while(cLineas.moveToNext()){
				JsonSetTaskLineas task = new JsonSetTaskLineas(
					cLineas.getString(2), 	//2+ "id_temporario VARCHAR, "		
					cLineas.getString(4), 	//4+ "id_producto VARCHAR, "
					cLineas.getString(5), 	//5+ "precio VARCHAR, "
					cLineas.getString(6), 	//6+ "cantidad VARCHAR, "
					cLineas.getString(7), 	//7+ "subtotal VARCHAR, "
					cLineas.getString(8), 	//8+ "id_estado_producto_presupuesto VARCHAR, "
					cLineas.getString(9), 	//9+ "comentario VARCHAR, "
					cLineas.getString(12) 	//12+ "eliminado VARCHAR, "
				);
				
				String url2 = config.getIp(db)+"/actualizaciones/setLineasPresupuestos/";
		 		task.execute(new String[] { url2 });
    		}		
		} else {
			Toast.makeText(this, config.msjNoRegistros("lineas presupuestos"), Toast.LENGTH_SHORT).show();
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
				Toast.makeText(getApplicationContext(), 
				config.msjError(e.toString()), Toast.LENGTH_LONG).show();
			}
			
			return answer;
		}
 
		protected void onPostExecute(String result) {
			if(result.equals("ok")){
				CargarPresupuestos();
				pDialog.dismiss();
			}else{
				Toast.makeText(getApplicationContext(), 
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
	
	
	
	
	private class JsonSetTaskLineas extends AsyncTask<String, Void, String> {
		String id_temporario;		
		String id_producto;
		String precio;
		String cantidad;
		String subtotal;
		String id_estado_producto_presupuesto;
		String comentario;
		String eliminado;
		
 		public JsonSetTaskLineas(
 				String id_temporario,		
				String id_producto,
				String precio,
				String cantidad,
				String subtotal,
				String id_estado_producto_presupuesto,
				String comentario,
				String eliminado
				) {
 			this.id_temporario = id_temporario;		
			this.id_producto = id_producto;
			this.precio = precio;
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
	public void CargarPresupuestos() {
		
		
		// Carga de presupuestos
		
		
		try {
			JSONObject jsonResponse = new JSONObject(jsonResult);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("presupuestos");
				 			
			if(jsonMainNode.length() > 0){
				mPresupuesto.truncate();
	 		}
	 			  
	 		for (int i = 0; i < jsonMainNode.length(); i++) {
	 			JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
	 			
	 			String id_back = jsonChildNode.optString("id_presupuesto");
		 		String id_visita = jsonChildNode.optString("id_visita");
		 		String id_cliente = jsonChildNode.optString("id_cliente");
		 		String id_vendedor = jsonChildNode.optString("id_vendedor");
		 		String id_estado_presupuesto = jsonChildNode.optString("id_estado_presupuesto");
		 		String total = jsonChildNode.optString("total");
		 		String fecha = jsonChildNode.optString("fecha");
		 		String id_origen = jsonChildNode.optString("id_origen");
		 		String aprobado_back = jsonChildNode.optString("aprobado_back");
		 		String aprobado_front = jsonChildNode.optString("aprobado_front");
		 		String visto_back = jsonChildNode.optString("visto_back");
		 		String visto_front = jsonChildNode.optString("visto_front");
		 		String date_add = jsonChildNode.optString("date_add");
		 		String date_upd = jsonChildNode.optString("date_upd");
		 		String eliminado = jsonChildNode.optString("eliminado");
		 		String user_add = jsonChildNode.optString("user_add");
		 		String user_upd = jsonChildNode.optString("user_upd");
		 		
		 		mPresupuesto.insert(
		 				id_back, 
		 				id_visita, 
		 				id_cliente, 
		 				id_vendedor, 
		 				id_estado_presupuesto, 
		 				total, 
		 				fecha, 
		 				id_origen, 
		 				aprobado_back, 
		 				aprobado_front, 
		 				visto_back, 
		 				visto_front, 
		 				date_add,
		 				date_upd, 
		 				eliminado, 
		 				user_add, 
		 				user_upd
		 		);
	 			
	 		}
	 		
	 		Toast.makeText(getApplicationContext(), 
	 		 		config.msjRegistrosActualizados(" presupuestos "+jsonMainNode.length()), Toast.LENGTH_SHORT).show();
	 	} catch (JSONException e) {
	 		Toast.makeText(getApplicationContext(), 
	 			config.msjError(e.toString()) , Toast.LENGTH_SHORT).show();
	 	}
		
		
		// Carga de lineas de presupuestos
			
			
		try {
			JSONObject jsonResponse = new JSONObject(jsonResult);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("lineas_presupuestos");
		 			
			if(jsonMainNode.length() > 0){
				mLineas.delete();
	 		}
		 			  
	 		for (int i = 0; i < jsonMainNode.length(); i++) {
	 			JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
		 			
	 			String id_back = jsonChildNode.optString("id_linea_producto_presupuesto");
	 			String id_presupuesto = jsonChildNode.optString("id_presupuesto");
	 			String id_producto = jsonChildNode.optString("id_producto");
	 			String precio = jsonChildNode.optString("precio");
	 			String cantidad = jsonChildNode.optString("cantidad");
	 			String subtotal = jsonChildNode.optString("subtotal");
	 			String id_estado_producto_presupuesto = jsonChildNode.optString("id_estado_producto_presupuesto");
	 			String id_temporario = jsonChildNode.optString("id_front");
	 			String comentario = jsonChildNode.optString("comentario");
		 		String date_add = jsonChildNode.optString("date_add");
		 		String date_upd = jsonChildNode.optString("date_upd");
		 		String eliminado = jsonChildNode.optString("eliminado");
		 		String user_add = jsonChildNode.optString("user_add");
		 		String user_upd = jsonChildNode.optString("user_upd");
		 		
		 		mLineas.insert(
		 			id_back, 
		 			id_temporario, 
		 			id_presupuesto, 
		 			id_producto, 
		 			precio, 
		 			cantidad, 
		 			subtotal, 
		 			id_estado_producto_presupuesto, 
		 			comentario, 
		 			date_add, 
		 			date_upd, 
		 			eliminado, 
		 			user_add, 
		 			user_upd
		 		);
		 			
	 		}
		 		
	 		Toast.makeText(getApplicationContext(), 
	 		 		config.msjRegistrosActualizados(" lineas presupuestos "+jsonMainNode.length()), Toast.LENGTH_SHORT).show();
	 	} catch (JSONException e) {
	 		Toast.makeText(getApplicationContext(), 
	 			config.msjError(e.toString()) , Toast.LENGTH_SHORT).show();
	 	}
		
		
		// Carga de estados presupuestos
			
			
		try {
			JSONObject jsonResponse = new JSONObject(jsonResult);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("estados_presupuestos");
		 			
			if(jsonMainNode.length() > 0){
				mEstados.delete();
	 		}
		 			  
	 		for (int i = 0; i < jsonMainNode.length(); i++) {
	 			JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
	 			
		 			
	 			String id_back = jsonChildNode.optString("id_estado_presupuesto");
	 			String estado = jsonChildNode.optString("estado");
	 			String text = jsonChildNode.optString("text");
	 			String eliminar_cliente = jsonChildNode.optString("eliminar_cliente");
	 			String eliminar_vendedor = jsonChildNode.optString("eliminar_vendedor");
	 			String eliminar_visita = jsonChildNode.optString("eliminar_visita");
	 			String date_add = jsonChildNode.optString("date_add");
		 		String date_upd = jsonChildNode.optString("date_upd");
		 		String eliminado = jsonChildNode.optString("eliminado");
		 		String user_add = jsonChildNode.optString("user_add");
		 		String user_upd = jsonChildNode.optString("user_upd");
		 		
		 		mEstados.insert(
		 			id_back, 
		 			estado, 
		 			text, 
		 			eliminar_cliente, 
		 			eliminar_vendedor, 
		 			eliminar_visita, 
		 			date_add, 
		 			date_upd, 
		 			eliminado, 
		 			user_add, 
		 			user_upd
		 		);
		 			
	 		}
		 		
	 		Toast.makeText(getApplicationContext(), 
	 		 		config.msjRegistrosActualizados(" estados presupuestos "+jsonMainNode.length()), Toast.LENGTH_SHORT).show();
	 	} catch (JSONException e) {
	 		Toast.makeText(getApplicationContext(), 
	 			config.msjError(e.toString()) , Toast.LENGTH_SHORT).show();
	 	}
		 		
	 	presupuestos_lista();
	}
	
}
  
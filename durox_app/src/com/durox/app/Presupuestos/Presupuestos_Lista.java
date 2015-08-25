package com.durox.app.Presupuestos;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.durox_app.R;
import com.durox.app.Config_durox;
import com.durox.app.Clientes.Clientes;
import com.durox.app.Clientes.Clientes_ListView;
import com.durox.app.Models.Clientes_model;
import com.durox.app.Models.Grupos_model;
import com.durox.app.Models.Iva_model;
import com.durox.app.Models.Lineas_Presupuestos_model;
import com.durox.app.Models.Presupuestos_model;
import com.durox.app.Models.Productos_model;
import com.durox.app.Models.Visitas_model;
import com.durox.app.Presupuestos.Presupuestos_Main;
import com.durox.app.Productos.Productos;
import com.durox.app.Productos.Productos_ListView;
import com.durox.app.Visitas.Visitas_Main;

import android.app.Activity;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
 
public class Presupuestos_Lista extends Activity {
	
	// Declare Variables
	ListView list;
	Presupuestos_ListView adapter;
	EditText editsearch;
	
	String[] c_nombre;
	String[] p_nombre;
	String[] direccion;
	String[] detalle;
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
	private ListView listView;
	
	TextView content;
	
	Config_durox config;
	
	Presupuestos_model mPresupuesto;
	Lineas_Presupuestos_model mLineas;
 
   
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        config = new Config_durox();
        
        db = openOrCreateDatabase(config.getDatabase(), Context.MODE_PRIVATE, null);
        
        presupuestos_lista();
   }
    
    
    public void presupuestos_lista(){
 		setContentView(R.layout.presupuestos_listview);
 		
 		db = openOrCreateDatabase(config.getDatabase(), Context.MODE_PRIVATE, null);
    	
    	Presupuestos_model mPresupuestos = new Presupuestos_model(db);
 		
		c = mPresupuestos.getRegistros();
		
		int cantidad_clientes = c.getCount();
		
		id_back = new String[cantidad_clientes];
		id_presupuesto = new String[cantidad_clientes];
		c_nombre = new String[cantidad_clientes];
		direccion = new String[cantidad_clientes];
		foto = new int[cantidad_clientes];
		
		int j = 0;
				
		if(c.getCount() > 0)
		{
			while(c.moveToNext())
    		{
				id_back[j] = c.getString(0);
				c_nombre[j] = c.getString(2);
				id_presupuesto[j] = c.getString(5);
    			direccion[j] = c.getString(3);
    			foto[j] = R.drawable.presupuesto; 
    		
    			j = j + 1;
    		}
			
			// Locate the ListView in listview_main.xml
    		list = (ListView) findViewById(R.id.lvPresupuestos);
    		arraylist.clear();

    		for (int i = 0; i < c_nombre.length; i++) 
    		{
    			//WorldPopulation wp = new WorldPopulation(rank[i], country[i],
    			Presupuesto wp = new Presupuesto(
    					id_back[i],
    					c_nombre[i],
    					direccion[i], 
    					foto[i],
    					id_presupuesto[i]
    			);
    			
    			// Binds all strings into an array
    			arraylist.add(wp);
    		}
    		
    		// Pass results to ListViewAdapter Class
    		adapter = new Presupuestos_ListView(this, arraylist);
    		
    		// Binds the Adapter to the ListView
    		list.setAdapter(adapter);
    		
    		// Locate the EditText in listview_main.xml
    		editsearch = (EditText) findViewById(R.id.search);

    		// Capture Text in EditText
    		editsearch.addTextChangedListener(new TextWatcher() {

    			@Override
    			public void afterTextChanged(Editable arg0) 
    			{
    				String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
    				adapter.filter(text);
    			}

    			@Override
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
		// Exportamos los presupuestos 
    	
    	mPresupuesto = new Presupuestos_model(db);
		
		Cursor c = mPresupuesto.getNuevos();
		
		if(c.getCount() > 0) {
			while(c.moveToNext()){
				
				JsonSetTask task = new JsonSetTask(
					c.getString(0),		//+ 0"id_presupuesto VARCHAR, "
					c.getString(2),		//+ 2"id_visita VARCHAR, "
					c.getString(3),		//+ 3"id_cliente VARCHAR, "
					c.getString(4),		//+ 4"id_vendedor VARCHAR, "
					c.getString(5),		//+ 5"id_estado_presupuesto VARCHAR, "
					c.getString(6),		//+ 6"total VARCHAR, "
					c.getString(8),		//+ 8"id_origen VARCHAR, "
					c.getString(9),		//+ 9" aprobado_back VARCHAR, "
					c.getString(10),	//+ 10" aprobado_front VARCHAR, "
					c.getString(11),	//+ 11" visto_back VARCHAR, "
					c.getString(12),	//+ 12" visto_front VARCHAR, "
					c.getString(15)		//+ 15"eliminado VARCHAR, "
				);
				
				String url2 = config.getIp()+"/actualizaciones/setPresupuestos/";
		 		task.execute(new String[] { url2 });
    		}		
		} else {
			Toast.makeText(this, config.msjNoRegistros("presupuestos"), Toast.LENGTH_SHORT).show();
		}
		
		// Importamos los presupuestos
		
		JsonReadTask taskpresupuestos = new JsonReadTask();
		String url = config.getIp()+"/actualizaciones/getPresupuestos/";
		taskpresupuestos.execute(new String[] { url });
		
		// Exportamos las lineas de los presupuestos
		
		mLineas = new Lineas_Presupuestos_model(db);
		
		Cursor cLineas = mLineas.getNuevos();
		
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
				
				String url2 = config.getIp()+"/actualizaciones/setLineasPresupuestos/";
		 		task.execute(new String[] { url2 });
    		}		
		} else {
			Toast.makeText(this, config.msjNoRegistros("lineas presupuestos"), Toast.LENGTH_SHORT).show();
		}
		
		// Importamos los presupuestos
		
		JsonReadTaskLineas tasklineas = new JsonReadTaskLineas();
		String urlLineas = config.getIp()+"/actualizaciones/getLineasPresupuestos/";
		tasklineas.execute(new String[] { urlLineas });
		
		presupuestos_lista();
	}
    
    
    // Clase para leer presupuestos
	private class JsonReadTask extends AsyncTask<String, Void, String> {
		
		protected String doInBackground(String... params) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(params[0]);
			
			try {
				HttpResponse response = httpclient.execute(httppost);
				jsonResult = inputStreamToString(
				response.getEntity().getContent()).toString();
			}
	 
			catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return null;
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
			CargarPresupuestos();
		}
	}
	
	
	 // Clase para leer presupuestos
	 private class JsonReadTaskLineas extends AsyncTask<String, Void, String> {
			
			protected String doInBackground(String... params) {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(params[0]);
				
				try {
					HttpResponse response = httpclient.execute(httppost);
					jsonResult = inputStreamToString(
					response.getEntity().getContent()).toString();
				}
		 
				catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				return null;
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
				CargarLineasPresupuestos();
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
	 				
	 			HttpResponse response = httpclient.execute(httppost);
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
	 				
	 			HttpResponse response = httpclient.execute(httppost);
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
		try {
			JSONObject jsonResponse = new JSONObject(jsonResult);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("presupuestos");
	 			
			if(jsonMainNode.length() > 0){
				mPresupuesto.truncate();
	 			Toast.makeText(getApplicationContext(), 
	 					config.msjActualizandoRegistros() , Toast.LENGTH_SHORT).show();
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
	 		
	 	presupuestos_lista();
	}
	
	
	
	// build hash set for list view
		public void CargarLineasPresupuestos() {
			try {
				JSONObject jsonResponse = new JSONObject(jsonResult);
				JSONArray jsonMainNode = jsonResponse.optJSONArray("lineas_presupuestos");
		 			
				if(jsonMainNode.length() > 0){
					mLineas.delete();
		 			Toast.makeText(getApplicationContext(), 
		 					config.msjActualizandoRegistros() , Toast.LENGTH_SHORT).show();
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
		 		
		 	presupuestos_lista();
		}
}
  
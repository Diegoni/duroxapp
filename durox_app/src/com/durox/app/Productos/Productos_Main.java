package com.durox.app.Productos;

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

import com.durox.app.Config_durox;
import com.durox.app.MenuActivity;
import com.durox.app.Documentos.Documentos;
import com.durox.app.Documentos.Documentos_ListView;
import com.durox.app.Models.Clientes_model;
import com.durox.app.Models.Documentos_model;
import com.durox.app.Models.Productos_model;
import com.durox.app.Productos.Productos;
import com.durox.app.Productos.Productos_ListView;
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
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class Productos_Main extends MenuActivity {
	
	// Declare Variables
	ListView list;
	Productos_ListView adapterp;
	Documentos_ListView adapterd;
	EditText editsearch;
	
	String[] nombre;
	String[] id_back;
	String[] precio;
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
	
	private Button btn_orden;
	private Button btn_filtro; 
	
	
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
        config = new Config_durox();
        db = openOrCreateDatabase(config.getDatabase(), Context.MODE_PRIVATE, null);
        
        setTitle("Productos - Lista");
        getActionBar().setIcon(R.drawable.menuproductos);
        
        setContentView(R.layout.productos_listview);
        
        orden = "";
        filtro = "nombre";
        
        productos_lista(orden, filtro);
		
        btn_orden = (Button) findViewById(R.id.btn_orden);
        btn_orden.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				PopupMenu popup = new PopupMenu(Productos_Main.this, btn_orden);
				popup.getMenuInflater().inflate(R.menu.popup_producto, popup.getMenu());

				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					public boolean onMenuItemClick(MenuItem item) {
						orden = item.getTitle();
						
						productos_lista(orden, filtro);
						
						Toast.makeText(Productos_Main.this, "Orden : " + item.getTitle(), Toast.LENGTH_SHORT).show();
		                        return true;
					}
				});
		        
				popup.show();
			}
		});
        
        
        btn_filtro = (Button) findViewById(R.id.btn_filtro);
        btn_filtro.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				PopupMenu popup = new PopupMenu(Productos_Main.this, btn_filtro);
				popup.getMenuInflater().inflate(R.menu.popup_producto, popup.getMenu());

				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					public boolean onMenuItemClick(MenuItem item) {
						filtro = item.getTitle();
						
						productos_lista(orden, filtro);
						
						Toast.makeText(Productos_Main.this, "Filtro : " + item.getTitle(), Toast.LENGTH_SHORT).show();
		                        return true;
					}
				});
		        
				popup.show();
			}
		});
		
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
				if(carga == "productos"){
					CargarProductos();
				}
			}else{
				Toast.makeText(getApplicationContext(), 
						result, Toast.LENGTH_LONG).show();
			}
			
		}
	}
	
	
	public void actualizar_productos(View view) {
		pDialog = new ProgressDialog(this);
        pDialog.setMessage("Actualizando....");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
		
		JsonReadTask taskproductos = new JsonReadTask("productos");
		String url = config.getIp(db)+"/actualizaciones/getProductos/";
		taskproductos.execute(new String[] { url });
	}
		
		
	public void CargarProductos() {
		try {
			JSONObject jsonResponse = new JSONObject(jsonResult);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("productos");
			 			
			if(jsonMainNode.length() > 0){
				mProductos.truncate();
				Toast.makeText(getApplicationContext(), 
			 			config.msjActualizandoRegistros(), Toast.LENGTH_SHORT).show();
			}
			  
			for (int i = 0; i < jsonMainNode.length(); i++) {
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
				
				String id_back = jsonChildNode.optString("id_producto");
				String codigo = jsonChildNode.optString("codigo");
				String codigo_lote = jsonChildNode.optString("codigo_lote");
				String nombre = jsonChildNode.optString("nombre");
				String precio = jsonChildNode.optString("precio");
				String precio_iva = jsonChildNode.optString("precio_iva");
				String precio_min = jsonChildNode.optString("precio_min");
				String precio_min_iva = jsonChildNode.optString("precio_min_iva");
				String id_iva = jsonChildNode.optString("id_iva");
				String ficha_tecnica = jsonChildNode.optString("ficha_tecnica");
				String date_add = jsonChildNode.optString("date_add");
				String date_upd = jsonChildNode.optString("date_upd");
				String eliminado = jsonChildNode.optString("eliminado");
				String user_add = jsonChildNode.optString("user_add");
				String user_upd = jsonChildNode.optString("user_upd");
				
				Cursor cUnico = mProductos.getID(nombre);
				
				if(cUnico.getCount() > 0){
					//Toast.makeText(this, config.msjDuplicado(), Toast.LENGTH_SHORT).show();
				}else{
					mProductos.insert(
						id_back,
						codigo,
						codigo_lote,
						nombre,
						precio,
						precio_iva,
						precio_min,
						precio_min_iva,
						id_iva,
						ficha_tecnica,
						date_add,
						date_upd,
						eliminado,
						user_add,
						user_upd
					);
				}
			}
			
			Toast.makeText(getApplicationContext(), 
					config.msjRegistrosActualizados(" productos "+jsonMainNode.length()), Toast.LENGTH_SHORT).show();
			
		} catch (JSONException e) {
			Toast.makeText(getApplicationContext(), 
			config.msjError(e.toString()), Toast.LENGTH_SHORT).show();
		}
		productos_lista(orden, filtro);   
	}
	
	
	
	public void productos_lista(CharSequence order, final CharSequence filtro){
		
		mProductos = new Productos_model(db);
		mProductos.createTable();
		c = mProductos.getRegistros(order);
		
		int cantidad_productos = c.getCount();
		
		nombre = new String[cantidad_productos];
		id_back = new String[cantidad_productos];
		precio = new String[cantidad_productos];
		imagen = new int[cantidad_productos];
		
		j = 0;
			
		if(c.getCount() > 0){
			while(c.moveToNext()){
				id_back[j] = c.getString(1);
				nombre[j] = c.getString(4);
				precio[j] = c.getString(5);
				imagen[j] = R.drawable.productos; 
				j = j + 1;
			}	
			
			// Locate the ListView in listview_main.xml
			list = (ListView) findViewById(R.id.listview);
			arraylistp.clear();
			
			for (int i = 0; i < nombre.length; i++) {
				Productos wp = new Productos(
						id_back[i],
						nombre[i],
						precio[i], 
						imagen[i]
				);
				
				// Binds all strings into an array
				arraylistp.add(wp);
			}
			
			// Pass results to ListViewAdapter Class
			adapterp = new Productos_ListView(this, arraylistp);
			
			// Binds the Adapter to the ListView
			list.setAdapter(adapterp);
			
			// Locate the EditText in listview_main.xml
			editsearch = (EditText) findViewById(R.id.search);
			editsearch.setHint(filtro);
			
			// Capture Text in EditText
			editsearch.addTextChangedListener(new TextWatcher() {
			
				@Override
				public void afterTextChanged(Editable arg0) {
					String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
					adapterp.filter(text, filtro);
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


}

package com.durox.app;


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
import com.durox.app.Clientes.Clientes;
import com.durox.app.Clientes.Clientes_ListView;
import com.durox.app.Documentos.Documentos;
import com.durox.app.Documentos.Documentos_ListView;
import com.durox.app.Documentos.Documentos_Main;
import com.durox.app.Models.Clientes_model;
import com.durox.app.Models.Documentos_model;
import com.durox.app.Models.Grupos_model;
import com.durox.app.Models.Iva_model;
import com.durox.app.Models.Productos_model;
import com.durox.app.Models.Vendedores_model;
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
 
public class MainActivity extends Activity {
	
	// Declare Variables
	ListView list;
	Clientes_ListView adapter;
	Productos_ListView adapterp;
	Documentos_ListView adapterd;
	EditText editsearch;
	
	String[] c_nombre;
	String[] p_nombre;
	String[] direccion;
	String[] detalle;
	int[] foto;
	String[] id_back;
	String[] id_pback;
	int[] imagen;
	ArrayList<Clientes> arraylist = new ArrayList<Clientes>();
	ArrayList<Productos> arraylistp = new ArrayList<Productos>();
	ArrayList<Documentos> arraylistd = new ArrayList<Documentos>();
	SQLiteDatabase db;
	
	String truncate;
	String sql;
	Cursor c;
	int j;	
		
	private String jsonResult;
	private ListView listView;
	
	Clientes_model mCliente;
	Productos_model mProductos;
	
	TextView content;
	
	
	Documentos_model mDocumentos;
	Cursor cDocumentos;
	
	Config_durox config;
	
 
   
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        config = new Config_durox();
        
        db = openOrCreateDatabase(config.getDatabase(), Context.MODE_PRIVATE, null);
        
        Intent i = getIntent();
		 
		String user = i.getStringExtra("user");
		String pass = i.getStringExtra("pass");
		String id_vendedor = i.getStringExtra("id_vendedor");

		if(user != null){
			Vendedores_model mVendedor = new Vendedores_model(db);
			
			mVendedor.insert(id_vendedor, user, pass);
		}
   } 
    
   
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.item1:
        	clientes_lista();
        	
    		Toast.makeText(this, "Clientes :"+c.getCount(), Toast.LENGTH_SHORT).show(); 
            return true;
    		
        case R.id.item2: 
        	productos_lista();        	
        	    		
            Toast.makeText(this, "Productos "+c.getCount(), Toast.LENGTH_SHORT).show(); 
            return true;
        case R.id.item3: 
        	Toast.makeText(this, "Visitas", Toast.LENGTH_SHORT).show();
        	
        	Intent intentVisitas = new Intent(this, Visitas_Main.class);
    		startActivity(intentVisitas);
        	 
            return true;            
        case R.id.item4: 
            Toast.makeText(this, "Presupuestos", Toast.LENGTH_SHORT).show();
           
            Intent intentPresupuestos = new Intent(this, Presupuestos_Main.class);
    		startActivity(intentPresupuestos);
            
            return true;
        case R.id.item5: 
        	
        	Toast.makeText(this, "Documentos", Toast.LENGTH_SHORT).show();
        	
        	Intent intentDocumentos = new Intent(this, Documentos_Main.class);
     		startActivity(intentDocumentos);
        	  
            return true;
        case R.id.item6: 
            Toast.makeText(this, "Actualizar", Toast.LENGTH_SHORT).show(); 
            
            Intent intentLogin = new Intent(this, Login.class);
     		startActivity(intentLogin);
    		
            return true;            
        default:
            return super.onOptionsItemSelected(item);
        }
    }
         
     
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
 			
 			if(carga == "clientes"){
 				CargarClientes();
 			}else if(carga == "productos"){
 				CargarProductos();
 			}else if(carga == "grupos"){
 				CargarGrupos();
	 		}else if(carga == "iva"){
				CargarIva();
			}
 			
 		}
 	}// end async task
 	
 	public void actualizar_clientes(View view) {
 		JsonReadTask taskgrupos = new JsonReadTask("grupos");
 		String url = config.getIp()+"/actualizaciones/getGrupos/";
 		taskgrupos.execute(new String[] { url });
 		
 		JsonReadTask taskiva = new JsonReadTask("iva");
 		url = config.getIp()+"/actualizaciones/getIva/";
 		taskiva.execute(new String[] { url });
 		
 		JsonReadTask taskclientes = new JsonReadTask("clientes");
 		url = config.getIp()+"/actualizaciones/getClientes/";
 		taskclientes.execute(new String[] { url });
 	}
 	
 	public void actualizar_productos(View view) {
 		JsonReadTask taskproductos = new JsonReadTask("productos");
 		String url = config.getIp()+"/actualizaciones/getProductos/";
 		taskproductos.execute(new String[] { url });
 	}
  
 	
 	// build hash set for list view
 	public void CargarClientes() {
 		try {
 			JSONObject jsonResponse = new JSONObject(jsonResult);
 			JSONArray jsonMainNode = jsonResponse.optJSONArray("clientes");
 			
 			if(jsonMainNode.length() > 0){
 				mCliente.truncate();
 				Toast.makeText(getApplicationContext(), 
 			 			config.msjActualizandoRegistros() , Toast.LENGTH_SHORT).show();
 			}
 			  
 			for (int i = 0; i < jsonMainNode.length(); i++) {
 				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
 				
 				int id_back = jsonChildNode.optInt("id_cliente");
 				String nombre = jsonChildNode.optString("nombre");
 				String apellido = jsonChildNode.optString("apellido");
 				String domicilio = jsonChildNode.optString("domicilio");
 				String cuit = jsonChildNode.optString("cuit");
 				int id_grupo_cliente = jsonChildNode.optInt("id_grupo_cliente");
 				int id_iva = jsonChildNode.optInt("id_iva"); 
 				String imagen = jsonChildNode.optString("imagen");
 				String nombre_fantasia = jsonChildNode.optString("nombre_fantasia");
 				String razon_social = jsonChildNode.optString("razon_social");
 				String web = jsonChildNode.optString("web");
 				String date_add = jsonChildNode.optString("date_add");
 				String date_upd = jsonChildNode.optString("date_upd");
 				String eliminado = jsonChildNode.optString("eliminado");
 				int  user_add = jsonChildNode.optInt("user_add");
 				int user_upd = jsonChildNode.optInt("user_upd");
 				 				
 				mCliente.insert(
 					id_back,
 					nombre,
 					apellido,
 					domicilio,
 					cuit,
 					id_grupo_cliente,
 					id_iva,
 					imagen,
 					nombre_fantasia,
 					razon_social,
 					web,
 					date_add,
 					date_upd,
 					eliminado,
 					user_add,
 					user_upd
 				);
 			}
 			
 			Toast.makeText(getApplicationContext(), 
 	 	 			config.msjRegistrosActualizados(" clientes "+jsonMainNode.length()), Toast.LENGTH_SHORT).show();
 		} catch (JSONException e) {
 			Toast.makeText(getApplicationContext(), 
 			config.msjError(e.toString()), Toast.LENGTH_SHORT).show();
 		}
 		
 		
 		
 		clientes_lista();
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
 			
 			Toast.makeText(getApplicationContext(), 
 	 	 			config.msjRegistrosActualizados(" productos "+jsonMainNode.length()), Toast.LENGTH_SHORT).show();
 			
 		} catch (JSONException e) {
 			Toast.makeText(getApplicationContext(), 
 			config.msjError(e.toString()), Toast.LENGTH_SHORT).show();
 		}
 		
 		
 		
 		productos_lista();   
  	}
 	
 	
 	public void CargarGrupos() {
  		try {
  			JSONObject jsonResponse = new JSONObject(jsonResult);
  			JSONArray jsonMainNode = jsonResponse.optJSONArray("grupos");
  			
  			Grupos_model mGrupos = new Grupos_model(db);
				
			Cursor m = mGrupos.getRegistros();
  			
  			if(jsonMainNode.length() > 0){
  				
  				mGrupos.truncate();
  				
  				Toast.makeText(getApplicationContext(), 
  			 			config.msjActualizandoRegistros() , Toast.LENGTH_SHORT).show();
  			}
  			  
  			for (int i = 0; i < jsonMainNode.length(); i++) {
  				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
  				  				
  				String id_back = jsonChildNode.optString("id_grupo_cliente");
  				String grupo = jsonChildNode.optString("grupo_nombre");
  				String date_add = jsonChildNode.optString("date_add");
  				String date_upd = jsonChildNode.optString("date_upd");
  				String eliminado = jsonChildNode.optString("eliminado");
  				String user_add = jsonChildNode.optString("user_add");
  				String user_upd = jsonChildNode.optString("user_upd");
  				 				
  				mGrupos.insert(
  					id_back,
  					grupo,
  					date_add,
  					date_upd,
  					eliminado,
  					user_add,
  					user_upd
  				);
  			}
  			
  			Toast.makeText(getApplicationContext(), 
  	  	 			config.msjRegistrosActualizados(" grupos "+jsonMainNode.length()), Toast.LENGTH_SHORT).show();
  			
  		} catch (JSONException e) {
  			Toast.makeText(getApplicationContext(), 
  			config.msjError(e.toString()), Toast.LENGTH_SHORT).show();
  		}
  		
  		
   	}
 	
 	
 	public void CargarIva() {
  		try {
  			JSONObject jsonResponse = new JSONObject(jsonResult);
  			JSONArray jsonMainNode = jsonResponse.optJSONArray("iva");
  			
  			Iva_model mIva = new Iva_model(db);
				
			Cursor m = mIva.getRegistros();
  			
  			if(jsonMainNode.length() > 0){
  				
  				mIva.truncate();
  				
  				Toast.makeText(getApplicationContext(), 
  			 			config.msjActualizandoRegistros() , Toast.LENGTH_SHORT).show();
  			}
  			  
  			for (int i = 0; i < jsonMainNode.length(); i++) {
  				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
  				  				
  				String id_back = jsonChildNode.optString("id_iva");
  				String iva = jsonChildNode.optString("iva");
  				String date_add = jsonChildNode.optString("date_add");
  				String date_upd = jsonChildNode.optString("date_upd");
  				String eliminado = jsonChildNode.optString("eliminado");
  				String user_add = jsonChildNode.optString("user_add");
  				String user_upd = jsonChildNode.optString("user_upd");
  				 				
  				mIva.insert(
  					id_back,
  					iva,
  					date_add,
  					date_upd,
  					eliminado,
  					user_add,
  					user_upd
  				);
  			}
  			
  			Toast.makeText(getApplicationContext(), 
  	  	 			config.msjRegistrosActualizados(" iva "+jsonMainNode.length()), Toast.LENGTH_SHORT).show();
  			
  			
  		} catch (JSONException e) {
  			Toast.makeText(getApplicationContext(), 
  			"Error" + e.toString(), Toast.LENGTH_SHORT).show();
  		}
  		
  		
  		
   	}
 	
 	
 	public void clientes_lista(){
 		setContentView(R.layout.clientes_listview);
    	
    	mCliente = new Clientes_model(db);
		
		c = mCliente.getRegistros();
		
		int cantidad_clientes = c.getCount();
		
		id_back = new String[cantidad_clientes];
		c_nombre = new String[cantidad_clientes];
		direccion = new String[cantidad_clientes];
		foto = new int[cantidad_clientes];
	    
		int j = 0;
				
		if(c.getCount() > 0)
		{
			while(c.moveToNext())
    		{
				id_back[j] = c.getString(1);
				c_nombre[j] = c.getString(10);
    			direccion[j] = c.getString(3)+" "+c.getString(2);
    			foto[j] = R.drawable.clientes; 
    		
    			j = j + 1;
    		}	
			
			// Locate the ListView in listview_main.xml
    		list = (ListView) findViewById(R.id.lvClientes);
    		arraylist.clear();

    		for (int i = 0; i < c_nombre.length; i++) 
    		{
    			//WorldPopulation wp = new WorldPopulation(rank[i], country[i],
    			Clientes wp = new Clientes(
    					id_back[i],
    					c_nombre[i],
    					direccion[i], 
    					foto[i]
    			);
    			
    			// Binds all strings into an array
    			arraylist.add(wp);
    		}

    		// Pass results to ListViewAdapter Class
    		adapter = new Clientes_ListView(this, arraylist);
    		
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
 	
 	
 	public void productos_lista(){
 		setContentView(R.layout.productos_listview);
		
		mProductos = new Productos_model(db);
		
		mProductos.createTable();
		    		
		c = mProductos.getRegistros();
		
		int cantidad_productos = c.getCount();
		
		p_nombre = new String[cantidad_productos];
		id_pback = new String[cantidad_productos];
		detalle = new String[cantidad_productos];
		imagen = new int[cantidad_productos];
		
		j = 0;
				
		if(c.getCount() > 0)
		{
			while(c.moveToNext())
    		{
				id_pback[j] = c.getString(1);
    			p_nombre[j] = c.getString(4);
    			detalle[j] = c.getString(5);
    			imagen[j] = R.drawable.articulo; 
    			j = j + 1;
    		}	
			
			// Locate the ListView in listview_main.xml
    		list = (ListView) findViewById(R.id.listview);
    		arraylistp.clear();

    		for (int i = 0; i < p_nombre.length; i++) 
    		{
    			//WorldPopulation wp = new WorldPopulation(rank[i], country[i],
    			Productos wp = new Productos(
    					id_pback[i],
    					p_nombre[i],
    					detalle[i], 
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

    		// Capture Text in EditText
    		editsearch.addTextChangedListener(new TextWatcher() {

    			@Override
    			public void afterTextChanged(Editable arg0) 
    			{
    				String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
    				adapterp.filter(text);
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
  
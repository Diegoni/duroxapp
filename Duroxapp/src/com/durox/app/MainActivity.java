package com.durox.app;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.androidbegin.filterlistviewimg.R;
import com.durox.app.Clientes.Clientes;
import com.durox.app.Clientes.Clientes_ListView;
import com.durox.app.Models.Clientes_model;
import com.durox.app.Models.Productos_model;
import com.durox.app.Presupuestos.Presupuestos_Main;
import com.durox.app.Productos.Productos;
import com.durox.app.Productos.Productos_ListView;
import com.durox.app.Visitas.Visitas_Main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
 
public class MainActivity extends Activity {
	
	// Declare Variables
	ListView list;
	Clientes_ListView adapter;
	Productos_ListView adapterp;
	EditText editsearch;
	
	String[] c_nombre;
	String[] p_nombre;
	String[] direccion;
	String[] detalle;
	int[] foto;
	int[] imagen;
	ArrayList<Clientes> arraylist = new ArrayList<Clientes>();
	ArrayList<Productos> arraylistp = new ArrayList<Productos>();
	SQLiteDatabase db;
	
	String truncate;
	String sql;
	Cursor c;
	int j;	
		
	private String jsonResult;
	private ListView listView;
	
	Clientes_model mCliente;
	Productos_model mProductos;
 
   
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
   } 
    
   
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
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
            Toast.makeText(this, "Presupestos", Toast.LENGTH_SHORT).show();
           
            Intent intentPresupuestos = new Intent(this, Presupuestos_Main.class);
    		startActivity(intentPresupuestos);
            
            return true;
        case R.id.item5: 
            Toast.makeText(this, "Pedidos", Toast.LENGTH_SHORT).show(); 
            return true;
        case R.id.item6: 
            Toast.makeText(this, "Actualizar", Toast.LENGTH_SHORT).show(); 
            
            setContentView(R.layout.actualizar);
    		listView = (ListView) findViewById(R.id.listView1);
    		
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
 				"Error..." + e.toString(), Toast.LENGTH_LONG).show();
 			}
 			
 			return answer;
 		}
  
 		protected void onPostExecute(String result) {
 			
 			if(carga == "clientes"){
 				CargarClientes();
 			}else
 			if(carga == "productos"){
 				CargarProductos();
 			}
 		}
 	}// end async task
 	
 	public void actualizar_clientes(View view) {
 		JsonReadTask taskclientes = new JsonReadTask("clientes");
 		String url = "http://10.0.2.2/durox/index.php/actualizaciones/getClientes/";
 		taskclientes.execute(new String[] { url });
 	}
 	
 	public void actualizar_productos(View view) {
 		JsonReadTask taskproductos = new JsonReadTask("productos");
 		String url = "http://10.0.2.2/durox/index.php/actualizaciones/getProductos/";
 		taskproductos.execute(new String[] { url });
 	}
  
 	
 	// build hash set for list view
 	public void CargarClientes() {
 		try {
 			JSONObject jsonResponse = new JSONObject(jsonResult);
 			JSONArray jsonMainNode = jsonResponse.optJSONArray("clientes");
 			
 			String razon_social;
 			String nombre;
 			String apellido;
 			String number;
 			
 			if(jsonMainNode.length() > 0){
 				mCliente.truncate();
 				Toast.makeText(getApplicationContext(), 
 			 			"Registros registros "+jsonMainNode.length() , Toast.LENGTH_SHORT).show();
 			}
 			  
 			for (int i = 0; i < jsonMainNode.length(); i++) {
 				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
 				razon_social = jsonChildNode.optString("razon_social");
 				nombre = jsonChildNode.optString("nombre");
 				apellido = jsonChildNode.optString("apellido");
 				number = jsonChildNode.optString("cuit");
 				
 				mCliente.insert(razon_social, apellido+" "+nombre , number);
 			}
 		} catch (JSONException e) {
 			Toast.makeText(getApplicationContext(), 
 			"Error" + e.toString(), Toast.LENGTH_SHORT).show();
 		}
 		
 		Toast.makeText(getApplicationContext(), 
 	 			"Registros actualizados", Toast.LENGTH_SHORT).show();
 		
 		clientes_lista();
  	}
 	
 	
 	public void CargarProductos() {
 		try {
 			JSONObject jsonResponse = new JSONObject(jsonResult);
 			JSONArray jsonMainNode = jsonResponse.optJSONArray("productos");
 			
 			String nombre;
 			String precio;
 			String ficha_tecnica;
 			
 			if(jsonMainNode.length() > 0){
 				mProductos.truncate();
 				Toast.makeText(getApplicationContext(), 
 			 			"Buscando registros "+jsonMainNode.length() , Toast.LENGTH_SHORT).show();
 			}
 			  
 			for (int i = 0; i < jsonMainNode.length(); i++) {
 				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
 				
 				nombre = jsonChildNode.optString("nombre");
 				precio = jsonChildNode.optString("precio");
 				ficha_tecnica = jsonChildNode.optString("ficha_tecnica");
 				
 				mProductos.insert(nombre, precio , ficha_tecnica);
 			}
 		} catch (JSONException e) {
 			Toast.makeText(getApplicationContext(), 
 			"Error" + e.toString(), Toast.LENGTH_SHORT).show();
 		}
 		
 		Toast.makeText(getApplicationContext(), 
 	 			"Registros actualizados", Toast.LENGTH_SHORT).show();
 		
 		productos_lista();   
  	}
 	
 	
 	public void clientes_lista(){
 		setContentView(R.layout.clientes_listview);
    	
    	db = openOrCreateDatabase("Duroxapp", Context.MODE_PRIVATE, null);
    	
    	mCliente = new Clientes_model(db);
		
		mCliente.createTable();
		
		c = mCliente.getRegistros();
		
		int cantidad_clientes = c.getCount();
		
		c_nombre = new String[cantidad_clientes];
		direccion = new String[cantidad_clientes];
		foto = new int[cantidad_clientes];
	    
		int j = 0;
				
		if(c.getCount() > 0)
		{
			while(c.moveToNext())
    		{
				c_nombre[j] = c.getString(1);
    			direccion[j] = c.getString(2);
    			foto[j] = R.drawable.clientes; 
    			j = j + 1;
    		}	
			
			// Locate the ListView in listview_main.xml
    		list = (ListView) findViewById(R.id.listview);
    		arraylist.clear();

    		for (int i = 0; i < c_nombre.length; i++) 
    		{
    			//WorldPopulation wp = new WorldPopulation(rank[i], country[i],
    			Clientes wp = new Clientes(
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
		
		db = openOrCreateDatabase("Duroxapp", Context.MODE_PRIVATE, null);
		
		mProductos = new Productos_model(db);
		
		mProductos.createTable();
		    		
		c = mProductos.getRegistros();
		
		int cantidad_productos = c.getCount();
		
		p_nombre = new String[cantidad_productos];
		detalle = new String[cantidad_productos];
		imagen = new int[cantidad_productos];
		
		j = 0;
				
		if(c.getCount() > 0)
		{
			while(c.moveToNext())
    		{
    			p_nombre[j] = c.getString(1);
    			detalle[j] = c.getString(2);
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
  
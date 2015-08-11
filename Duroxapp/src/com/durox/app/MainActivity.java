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
	EditText editsearch;
	
	String[] c_nombre;
	String[] p_nombre;
	String[] direccion;
	String[] detalle;
	int[] foto;
	String[] id_back;
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
	
	TextView content;
 
   
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        db = openOrCreateDatabase("Durox_app", Context.MODE_PRIVATE, null);
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
 			
 			if(jsonMainNode.length() > 0){
 				mCliente.truncate();
 				Toast.makeText(getApplicationContext(), 
 			 			"Registros registros "+jsonMainNode.length() , Toast.LENGTH_SHORT).show();
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
  
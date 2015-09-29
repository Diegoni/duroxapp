package com.durox.app.Clientes;

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

import com.durox.app.Config_durox;
import com.durox.app.MenuActivity;
import com.durox.app.Models.Clientes_model;
import com.durox.app.Models.Departamentos_model;
import com.durox.app.Models.Direcciones_clientes_model;
import com.durox.app.Models.Documentos_model;
import com.durox.app.Models.Grupos_model;
import com.durox.app.Models.Iva_model;
import com.durox.app.Models.Mails_clientes_model;
import com.durox.app.Models.Provincias_model;
import com.durox.app.Models.Telefonos_clientes_model;
import com.durox.app.Models.Tipos_model;
import com.durox.app.Models.Vendedores_model;
import com.example.durox_app.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

public class Clientes_Main extends MenuActivity {
	
	// Declare Variables
	ListView list;
	Clientes_ListView adapter;
	EditText editsearch;
	
	String[] c_nombre;
	String[] direccion;
	int[] foto;
	String[] id_back;
	
	int[] imagen;
	ArrayList<Clientes> arraylist = new ArrayList<Clientes>();
	SQLiteDatabase db;
		
	String truncate;
	String sql;
	Cursor c;
	int j;	
			
	private String jsonResult;
		
	Clientes_model mCliente;
	TextView content;
		
	Documentos_model mDocumentos;
	Cursor cDocumentos;
	
	Vendedores_model mVendedor;
	String id_vendedor;
		
	Config_durox config;
	private ProgressDialog pDialog;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        config = new Config_durox();
        db = openOrCreateDatabase(config.getDatabase(), Context.MODE_PRIVATE, null);
        
        mVendedor = new Vendedores_model(db);
        id_vendedor = mVendedor.getID();
        
        setTitle("Clientes - Lista");
        getActionBar().setIcon(R.drawable.menuclientes);
        
		clientes_lista();
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
		
		if(c.getCount() > 0){
			while(c.moveToNext()){
				id_back[j] = c.getString(1);
				c_nombre[j] = c.getString(10);
    			direccion[j] = c.getString(3)+" "+c.getString(2);
    			foto[j] = R.drawable.clientes; 
    		
    			j = j + 1;
    		}	
			
			list = (ListView) findViewById(R.id.lvClientes);
    		arraylist.clear();

    		for (int i = 0; i < c_nombre.length; i++) 
    		{
    			Clientes wp = new Clientes(
    					id_back[i],
    					c_nombre[i],
    					direccion[i], 
    					foto[i]
    			);
    			arraylist.add(wp);
    		}

    		adapter = new Clientes_ListView(this, arraylist);
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

    			public void onTextChanged(
    					CharSequence arg0, 
    					int arg1, 
    					int arg2,
    					int arg3) {
    			}
    		});
		}
	}
	
	
	/*---------------------------------------------------------------------------------
		Funciones con los botones Actualizar
	---------------------------------------------------------------------------------*/    	

	public void actualizar_clientes(View view) {
		Clientes_model mClientes = new Clientes_model(db);
		Cursor cursorCliente = mClientes.getNuevos();
		
		if(cursorCliente.getCount() > 0) {
			while(cursorCliente.moveToNext()){
				JsonSetTask task = new JsonSetTask(
					cursorCliente.getString(0), //+ "id_back, "
					cursorCliente.getString(1), //+ "nombre, "
					cursorCliente.getString(2), //+ "apellido, "
					cursorCliente.getString(3), //+ "cuit, "
					cursorCliente.getString(4), //+ "id_grupo_cliente, "
					cursorCliente.getString(5), //+ "id_iva, "
					cursorCliente.getString(6), //+ "nombre_fantasia, "
					cursorCliente.getString(7), //+ "razon_social, "
					cursorCliente.getString(8) //+ "web "
				);
				
				String url2 = config.getIp(db)+"/actualizaciones/setClientes/";
		 		task.execute(new String[] { url2 });
    		}
		} 
		
		Telefonos_clientes_model mTelefonos = new Telefonos_clientes_model(db);
		Cursor cursorTelefonos = mTelefonos.getNuevos();
		
		if(cursorTelefonos.getCount() > 0){
			while(cursorTelefonos.moveToNext()){
				JsonSetTelefonos tasktel = new JsonSetTelefonos(
						cursorTelefonos.getString(0),
						cursorTelefonos.getString(1),
						cursorTelefonos.getString(2),
						cursorTelefonos.getString(3)
				);
				
				String urltel = config.getIp(db)+"/actualizaciones/setTelefonos/";
				tasktel.execute(new String[] { urltel });
			}
		}
		
		Direcciones_clientes_model mDireccion = new Direcciones_clientes_model(db);
		Cursor cursorDireccion = mDireccion.getNuevos();
		
		if(cursorDireccion.getCount() > 0){
			while(cursorDireccion.moveToNext()){
				JsonSetDireccion tasktel = new JsonSetDireccion(
						cursorDireccion.getString(0),
						cursorDireccion.getString(1),
						cursorDireccion.getString(2),
						cursorDireccion.getString(3),
						cursorDireccion.getString(4)
				);
				
				String urltel = config.getIp(db)+"/actualizaciones/setDirecciones/";
				tasktel.execute(new String[] { urltel });
			}
		}
		
		Mails_clientes_model mMails = new Mails_clientes_model(db);
		Cursor cursorMails = mMails.getNuevos();
		
		if(cursorMails.getCount() > 0){
			while(cursorMails.moveToNext()){
				JsonSetMails tasktel = new JsonSetMails(
						cursorMails.getString(0),
						cursorMails.getString(1),
						cursorMails.getString(2)
				);
				
				String urltel = config.getIp(db)+"/actualizaciones/setMails/";
				tasktel.execute(new String[] { urltel });
			}
		}
		
		
		new asyncciente().execute();
	}
	
	
	class asyncciente extends AsyncTask< String, String, String > {
   	 
    	String user,pass;
    	protected void onPreExecute() {
        	//para el progress dialog
            pDialog = new ProgressDialog(Clientes_Main.this);
            pDialog.setMessage("Actualizando....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
		protected String doInBackground(String... params) {
			String url;
			JsonReadTask taskclientes = new JsonReadTask("clientes");
			url = config.getIp(db)+"/actualizaciones/getClientes/";
			taskclientes.execute(new String[] { url });
			
			return "ok";
        }
       
        protected void onPostExecute(String result) {
        	clientes_lista();
        }
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
			pDialog.dismiss();
			
			if(result.equals("ok")){
				if(carga == "clientes"){
					CargarClientes();
				}
			}else{
				Toast.makeText(getApplicationContext(), 
						result, Toast.LENGTH_LONG).show();
			}
		}
	}
	
	
	/*---------------------------------------------------------------------------------
		Carga de los Clientes en base de datos
	---------------------------------------------------------------------------------*/    

	 public void CargarClientes() {
		String subjet = "clientes";
		Clientes_model mModel = new Clientes_model(db);
		 
	 	try {
	 		JSONObject jsonResponse = new JSONObject(jsonResult);
	 		JSONArray jsonMainNode = jsonResponse.optJSONArray(subjet);
	 		
	 		Log.e("Paso ", "Clientes total "+ jsonMainNode.length());
	 		
	 		mModel.createTable();
	 			
	 		if(jsonMainNode.length() > 0){
	 			mModel.truncate();
	 		}
	 			  
	 		for (int i = 0; i < jsonMainNode.length(); i++) {
	 			JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
	 				
	 			String id_back = jsonChildNode.optString("id_cliente");
	 			String nombre = jsonChildNode.optString("nombre");
	 			String apellido = jsonChildNode.optString("apellido");
	 			String cuit = jsonChildNode.optString("cuit");
	 			String id_grupo_cliente = jsonChildNode.optString("id_grupo_cliente");
	 			String id_iva = jsonChildNode.optString("id_iva"); 
	 			String imagen = jsonChildNode.optString("imagen");
	 			String nombre_fantasia = jsonChildNode.optString("nombre_fantasia");
	 			String razon_social = jsonChildNode.optString("razon_social");
	 			String web = jsonChildNode.optString("web");
	 			String date_add = jsonChildNode.optString("date_add");
	 			String date_upd = jsonChildNode.optString("date_upd");
	 			String eliminado = jsonChildNode.optString("eliminado");
	 			String  user_add = jsonChildNode.optString("user_add");
	 			String user_upd = jsonChildNode.optString("user_upd");
	 			
	 			Cursor cUnico = mModel.getID(razon_social);
				
				if(cUnico.getCount() > 0){
					//Toast.makeText(this, config.msjDuplicado(), Toast.LENGTH_SHORT).show();
				}else{
	 			 	mModel.insert(
		 				id_back,
		 				nombre,
		 				apellido,
		 				"0",				// modificado
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
	 		}
	 		
	 		Toast.makeText(getApplicationContext(), 
		  	  		config.msjRegistrosActualizados(subjet+" "+jsonMainNode.length()), Toast.LENGTH_SHORT).show();
		} catch (JSONException e) {
	 		Toast.makeText(getApplicationContext(), 
	 			config.msjError(e.toString()), Toast.LENGTH_SHORT).show();
	 	}
	 	
	 	
	 	
	 	
	 	
	 	
	 	subjet = "grupos";
		Grupos_model mGrupos = new Grupos_model(db); 
	 	
		try {
	 		JSONObject jsonResponse = new JSONObject(jsonResult);
	 		JSONArray jsonMainNode = jsonResponse.optJSONArray("grupos");
	 		
	 		Log.e("Paso ", "Grupos total "+ jsonMainNode.length());
	 			
	 		mGrupos.createTable();
	  			
	  		if(jsonMainNode.length() > 0){
	  			mGrupos.truncate();
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
	  			
	  			Cursor cUnico = mModel.getID(grupo);
				
				if(cUnico.getCount() > 0){
					//Toast.makeText(this, config.msjDuplicado(), Toast.LENGTH_SHORT).show();
				}else{
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
	  		}
	  			
	  		Toast.makeText(getApplicationContext(), 
	  	  		config.msjRegistrosActualizados(subjet+" "+jsonMainNode.length()), Toast.LENGTH_SHORT).show();
	  			
	  	} catch (JSONException e) {
	  		Toast.makeText(getApplicationContext(), 
	  			config.msjError(e.toString()), Toast.LENGTH_SHORT).show();
	  	}
		
		
		
		
		
		
		
		subjet = "iva";
		Iva_model mIva = new Iva_model(db);
		
	 	try {
	  		JSONObject jsonResponse = new JSONObject(jsonResult);
	  		JSONArray jsonMainNode = jsonResponse.optJSONArray(subjet);
	  		
	  		Log.e("Paso ", "iva total "+ jsonMainNode.length());
	  			
	  		mIva.createTable();
	  		
	  		if(jsonMainNode.length() > 0){
	  			mIva.truncate();
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
	  			
	  			Cursor cUnico = mModel.getID(iva);
				
				if(cUnico.getCount() > 0){
					//Toast.makeText(this, config.msjDuplicado(), Toast.LENGTH_SHORT).show();
				}else{
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
	  		}
	  		
	  		Toast.makeText(getApplicationContext(), 
	  	 	 	config.msjRegistrosActualizados(subjet+" "+jsonMainNode.length()), Toast.LENGTH_SHORT).show();
	  		
	  	} catch (JSONException e) {
	  		Toast.makeText(getApplicationContext(), 
	  		config.msjError(e.toString()), Toast.LENGTH_SHORT).show();
	  	}
		
	 	
	 	
	 	
	 	
	 	subjet = "tipos";
		Tipos_model mTipos = new Tipos_model(db);
		 
		try {
		 	JSONObject jsonResponse = new JSONObject(jsonResult);
		 	JSONArray jsonMainNode = jsonResponse.optJSONArray(subjet);
		 			
		 	mTipos.createTable();
		  			
		  	if(jsonMainNode.length() > 0){
		  		mTipos.truncate();
		   	}
		  		
		  	for (int i = 0; i < jsonMainNode.length(); i++) {
		  		JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
		  			  				
		  		String id_back = jsonChildNode.optString("id_tipo");
		  		String tipo = jsonChildNode.optString("tipo");
		  		String date_add = jsonChildNode.optString("date_add");
		  		String date_upd = jsonChildNode.optString("date_upd");
		  		String eliminado = jsonChildNode.optString("eliminado");
		  		String user_add = jsonChildNode.optString("user_add");
		  		String user_upd = jsonChildNode.optString("user_upd");
		  		
		  		Cursor cUnico = mModel.getID(tipo);
				
				if(cUnico.getCount() > 0){
					//Toast.makeText(this, config.msjDuplicado(), Toast.LENGTH_SHORT).show();
				}else{
		  			mTipos.insert(
			  			id_back, 
			  			tipo, 
			  			date_add, 
			  			date_upd, 
			  			eliminado, 
			  			user_add, 
			  			user_upd
			  		);
				}
		  	}
		  			
		  	Toast.makeText(getApplicationContext(), 
		  		config.msjRegistrosActualizados(subjet+" "+jsonMainNode.length()), Toast.LENGTH_SHORT).show();
		  			
		} catch (JSONException e) {
		 	Toast.makeText(getApplicationContext(), 
		 		config.msjError(e.toString()), Toast.LENGTH_SHORT).show();
		}
		
		
		
		
		
		
		subjet = "telefonos";
		Telefonos_clientes_model mTelefonos = new Telefonos_clientes_model(db);
		
		try {
		 	JSONObject jsonResponse = new JSONObject(jsonResult);
		 	JSONArray jsonMainNode = jsonResponse.optJSONArray(subjet);
		 			
		 	mTelefonos.createTable();
		  			
		  	if(jsonMainNode.length() > 0){
		  		mTelefonos.truncate();
		  	}
		  		
		  	for (int i = 0; i < jsonMainNode.length(); i++) {
		  		JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
		  			  				
		  		String id_back = jsonChildNode.optString("id_telefono");
		  		String id_tipo = jsonChildNode.optString("id_tipo");
		  		String telefono = jsonChildNode.optString("telefono");
		  		String cod_area = jsonChildNode.optString("cod_area");
		  		String fax = jsonChildNode.optString("fax");
		  		String date_add = jsonChildNode.optString("date_add");
		  		String date_upd = jsonChildNode.optString("date_upd");
		  		String eliminado = jsonChildNode.optString("eliminado");
		  		String user_add = jsonChildNode.optString("user_add");
		  		String user_upd = jsonChildNode.optString("user_upd");
		  		
		  		mTelefonos.insert(
		  			id_back, 
		  			id_tipo, 
		  			telefono, 
		  			cod_area, 
		  			fax, 
		  			date_add, 
		  			date_upd, 
		  			eliminado, 
		  			user_add, 
		  			user_upd
		  		);
		  		
		  	}
		  			
		  	Toast.makeText(getApplicationContext(), 
		  		config.msjRegistrosActualizados(subjet+" "+jsonMainNode.length()), Toast.LENGTH_SHORT).show();
		  			
		} catch (JSONException e) {
		 	Toast.makeText(getApplicationContext(), 
		 		config.msjError(e.toString()), Toast.LENGTH_SHORT).show();
		}
		
		
		
		
		
		
		
		
		
		
		subjet = "sin_clientes_telefonos";
		Telefonos_clientes_model mSinTelefonos = new Telefonos_clientes_model(db);
		 
		try {
		 	JSONObject jsonResponse = new JSONObject(jsonResult);
		 	JSONArray jsonMainNode = jsonResponse.optJSONArray(subjet);
		 			
		 	mSinTelefonos.createTable();
		  			
		  	if(jsonMainNode.length() > 0){
		  		mSinTelefonos.truncateSin();
		  	}
		  		
		  	for (int i = 0; i < jsonMainNode.length(); i++) {
		  		JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
		  			  				
		  		String id_back = jsonChildNode.optString("id_sin_cliente_telefono");
		  		String id_cliente = jsonChildNode.optString("id_cliente");
		  		String id_telefono = jsonChildNode.optString("id_telefono");
		  		String date_add = jsonChildNode.optString("date_add");
		  		String date_upd = jsonChildNode.optString("date_upd");
		  		String eliminado = jsonChildNode.optString("eliminado");
		  		String user_add = jsonChildNode.optString("user_add");
		  		String user_upd = jsonChildNode.optString("user_upd");
		  		
		  		mSinTelefonos.insertSin(
		  			id_back, 
		  			id_cliente, 
		  			id_telefono, 
		  			date_add, 
		  			date_upd, 
		  			eliminado, 
		  			user_add, 
		  			user_upd
		  		);
		  		
		  	}
		  			
		  	Toast.makeText(getApplicationContext(), 
		  		config.msjRegistrosActualizados(subjet+" "+jsonMainNode.length()), Toast.LENGTH_SHORT).show();
		  			
		} catch (JSONException e) {
		 	Toast.makeText(getApplicationContext(), 
		 		config.msjError(e.toString()), Toast.LENGTH_SHORT).show();
		}
		
		
		
		
		
		
		
		
		subjet = "mails";
		Mails_clientes_model mMails = new Mails_clientes_model(db);
		 
		try {
		 	JSONObject jsonResponse = new JSONObject(jsonResult);
		 	JSONArray jsonMainNode = jsonResponse.optJSONArray(subjet);
		 			
		 	mMails.createTable();
		  			
		  	if(jsonMainNode.length() > 0){
		  		mMails.truncate();
		  	}
		  		
		  	for (int i = 0; i < jsonMainNode.length(); i++) {
		  		JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
		  			  				
		  		String id_back = jsonChildNode.optString("id_mail");
		  		String id_tipo = jsonChildNode.optString("id_tipo");
		  		String mail = jsonChildNode.optString("mail");
		  		String date_add = jsonChildNode.optString("date_add");
		  		String date_upd = jsonChildNode.optString("date_upd");
		  		String eliminado = jsonChildNode.optString("eliminado");
		  		String user_add = jsonChildNode.optString("user_add");
		  		String user_upd = jsonChildNode.optString("user_upd");
		  		
		  		mMails.insert(
		  			id_back, 
		  			id_tipo, 
		  			mail, 
		  			date_add, 
		  			date_upd, 
		  			eliminado, 
		  			user_add, 
		  			user_upd
		  		);
		  		
		  	}
		  			
		  	Toast.makeText(getApplicationContext(), 
		  		config.msjRegistrosActualizados(subjet+" "+jsonMainNode.length()), Toast.LENGTH_SHORT).show();
		  			
		} catch (JSONException e) {
		 	Toast.makeText(getApplicationContext(), 
		 		config.msjError(e.toString()), Toast.LENGTH_SHORT).show();
		}
		
		
		
		
		subjet = "sin_clientes_mails";
		Mails_clientes_model mSinMails = new Mails_clientes_model(db);
		 
		try {
		 	JSONObject jsonResponse = new JSONObject(jsonResult);
		 	JSONArray jsonMainNode = jsonResponse.optJSONArray(subjet);
		 			
		 	mSinMails.createTable();
		  			
		  	if(jsonMainNode.length() > 0){
		  		mSinMails.truncateSin();
		  	}
		  		
		  	for (int i = 0; i < jsonMainNode.length(); i++) {
		  		JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
		  			  				
		  		String id_back = jsonChildNode.optString("id_sin_cliente_mail");
		  		String id_cliente = jsonChildNode.optString("id_cliente");
		  		String id_mail = jsonChildNode.optString("id_mail");
		  		String date_add = jsonChildNode.optString("date_add");
		  		String date_upd = jsonChildNode.optString("date_upd");
		  		String eliminado = jsonChildNode.optString("eliminado");
		  		String user_add = jsonChildNode.optString("user_add");
		  		String user_upd = jsonChildNode.optString("user_upd");
		  		
		  		mSinMails.insertSin(
		  			id_back, 
		  			id_cliente, 
		  			id_mail, 
		  			date_add, 
		  			date_upd, 
		  			eliminado, 
		  			user_add, 
		  			user_upd
		  		);
		  		
		  	}
		  			
		  	Toast.makeText(getApplicationContext(), 
		  		config.msjRegistrosActualizados(subjet+" "+jsonMainNode.length()), Toast.LENGTH_SHORT).show();
		  			
		} catch (JSONException e) {
		 	Toast.makeText(getApplicationContext(), 
		 		config.msjError(e.toString()), Toast.LENGTH_SHORT).show();
		}
		
		
		
		
		
		subjet = "direcciones";
		Direcciones_clientes_model mDirecciones = new Direcciones_clientes_model(db);
		 
		try {
		 	JSONObject jsonResponse = new JSONObject(jsonResult);
		 	JSONArray jsonMainNode = jsonResponse.optJSONArray(subjet);
		 			
		 	mDirecciones.createTable();
		  			
		  	if(jsonMainNode.length() > 0){
		  		mDirecciones.truncate();
		  	}
		  			  		
		  	for (int i = 0; i < jsonMainNode.length(); i++) {
		  		JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
		  			  				
		  		String id_back = jsonChildNode.optString("id_direccion");
		  		String id_tipo = jsonChildNode.optString("id_tipo");
		  		String id_departamento = jsonChildNode.optString("id_departamento");
		  		String id_provincia = jsonChildNode.optString("id_provincia");
		  		String id_pais = jsonChildNode.optString("id_pais");
		  		String direccion = jsonChildNode.optString("direccion");
		  		String cod_postal = jsonChildNode.optString("cod_postal");
		  		String date_add = jsonChildNode.optString("date_add");
		  		String date_upd = jsonChildNode.optString("date_upd");
		  		String eliminado = jsonChildNode.optString("eliminado");
		  		String user_add = jsonChildNode.optString("user_add");
		  		String user_upd = jsonChildNode.optString("user_upd");
		  		
		  		mDirecciones.insert( 
		  				id_back, 
		  				id_tipo,
		  				direccion,
		  				id_departamento, 
		  				id_provincia, 
		  				id_pais, 
		  			 	cod_postal, 
		  				date_add, 
		  				date_upd, 
		  				eliminado, 
		  				user_add, 
		  				user_upd);
		  		
		  	}
		  			
		  	Toast.makeText(getApplicationContext(), 
		  		config.msjRegistrosActualizados(subjet+" "+jsonMainNode.length()), Toast.LENGTH_SHORT).show();
		  			
		} catch (JSONException e) {
		 	Toast.makeText(getApplicationContext(), 
		 		config.msjError(e.toString()), Toast.LENGTH_SHORT).show();
		}
		
		
		
		
		
		
		
		
		
		subjet = "sin_clientes_direcciones";
		Direcciones_clientes_model mSinDirecciones = new Direcciones_clientes_model(db);
		 
		try {
		 	JSONObject jsonResponse = new JSONObject(jsonResult);
		 	JSONArray jsonMainNode = jsonResponse.optJSONArray(subjet);
		 			
		 	mSinDirecciones.createTable();
		  			
		  	if(jsonMainNode.length() > 0){
		  		mSinDirecciones.truncateSin();
		  	}
		  		
		  	for (int i = 0; i < jsonMainNode.length(); i++) {
		  		JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
		  			  				
		  		String id_back = jsonChildNode.optString("id_sin_cliente_direccion");
		  		String id_cliente = jsonChildNode.optString("id_cliente");
		  		String id_direccion = jsonChildNode.optString("id_direccion");
		  		String date_add = jsonChildNode.optString("date_add");
		  		String date_upd = jsonChildNode.optString("date_upd");
		  		String eliminado = jsonChildNode.optString("eliminado");
		  		String user_add = jsonChildNode.optString("user_add");
		  		String user_upd = jsonChildNode.optString("user_upd");
		  		
		  		mSinDirecciones.insertSin(
		  			id_back, 
		  			id_cliente, 
		  			id_direccion, 
		  			date_add, 
		  			date_upd, 
		  			eliminado, 
		  			user_add, 
		  			user_upd
		  		);
		  		
		  	}
		  			
		  	Toast.makeText(getApplicationContext(), 
		  		config.msjRegistrosActualizados(subjet+" "+jsonMainNode.length()), Toast.LENGTH_SHORT).show();
		  			
		} catch (JSONException e) {
		 	Toast.makeText(getApplicationContext(), 
		 		config.msjError(e.toString()), Toast.LENGTH_SHORT).show();
		}
		
		
		
		subjet = "departamentos";
		Departamentos_model mDepartamentos = new Departamentos_model(db); 
	 	
		try {
	 		JSONObject jsonResponse = new JSONObject(jsonResult);
	 		JSONArray jsonMainNode = jsonResponse.optJSONArray("departamentos");
	 			
	 		mDepartamentos.createTable();
	  			
	  		if(jsonMainNode.length() > 0){
	  			mDepartamentos.truncate();
	  		}
	  		
	  		for (int i = 0; i < jsonMainNode.length(); i++) {
	  			JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
	  			  				
	  			String id_back = jsonChildNode.optString("id_departamento");
	  			String id_provincia = jsonChildNode.optString("id_provincia");
	  			String nombre_departamento = jsonChildNode.optString("nombre_departamento");
	  				 				
	  			mDepartamentos.insert(
	  				id_back, 
	  				id_provincia, 
	  				nombre_departamento);
	  		}
	  			
	  		Toast.makeText(getApplicationContext(), 
	  	  		config.msjRegistrosActualizados(subjet+" "+jsonMainNode.length()), Toast.LENGTH_SHORT).show();
	  			
	  	} catch (JSONException e) {
	  		Toast.makeText(getApplicationContext(), 
	  			config.msjError(e.toString()), Toast.LENGTH_SHORT).show();
	  	}
		
		
		
		
		subjet = "provincias";
		Provincias_model mProvincias = new Provincias_model(db); 
	 	
		try {
	 		JSONObject jsonResponse = new JSONObject(jsonResult);
	 		JSONArray jsonMainNode = jsonResponse.optJSONArray("provincias");
	 			
	 		mProvincias.createTable();
	  			
	  		if(jsonMainNode.length() > 0){
	  			mProvincias.truncate();
	  		}
	  		
	  		Log.e("Paso ", "Provincias total "+ jsonMainNode.length());
	  			  		
	  		for (int i = 0; i < jsonMainNode.length(); i++) {
	  			JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
	  			  				
	  			String id_back = jsonChildNode.optString("id_provincia");
	  			String id_pais = jsonChildNode.optString("id_pais");
	  			String nombre_provincia = jsonChildNode.optString("nombre_provincia");
	  				 				
	  			mProvincias.insert(
	  				id_back, 
	  				id_pais, 
	  				nombre_provincia);
	  		}
	  			
	  		Toast.makeText(getApplicationContext(), 
	  	  		config.msjRegistrosActualizados(subjet+" "+jsonMainNode.length()), Toast.LENGTH_SHORT).show();
	  			
	  	} catch (JSONException e) {
	  		Toast.makeText(getApplicationContext(), 
	  			config.msjError(e.toString()), Toast.LENGTH_SHORT).show();
	  	}
		
	}
	 
	 
	 

	private class JsonSetTask extends AsyncTask<String, Void, String> {
		String id_back;
		String nombre;
		String apellido; 
		String cuit;
		String id_grupo_cliente; 
		String id_iva;
		String nombre_fantasia; 
		String razon_social;
		String web;
		
 		public JsonSetTask(
 				String id_back,
				String nombre, 
				String apellido, 
				String cuit,
				String id_grupo_cliente, 
				String id_iva, 
		 		String nombre_fantasia, 
				String razon_social,
				String web) {
 			this.id_back = id_back;
 			this.nombre = nombre;
 			this.apellido = apellido; 
 			this.cuit = cuit;
 			this.id_grupo_cliente = id_grupo_cliente; 
			this.id_iva = id_iva;
			this.nombre_fantasia = nombre_fantasia; 
			this.razon_social = razon_social;
			this.web = web;
		}
 		
		protected String doInBackground(String... params) {
	 		HttpClient httpclient = new DefaultHttpClient();
	 		HttpPost httppost = new HttpPost(params[0]);
	 			 		
	 		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
	 		pairs.add(new BasicNameValuePair("id_back", id_back));
	 		pairs.add(new BasicNameValuePair("nombre", nombre));
	 		pairs.add(new BasicNameValuePair("apellido", apellido)); 
	 		pairs.add(new BasicNameValuePair("cuit", cuit));
	 		pairs.add(new BasicNameValuePair("id_grupo_cliente", id_grupo_cliente)); 
	 		pairs.add(new BasicNameValuePair("id_iva", id_iva));
	 		pairs.add(new BasicNameValuePair("nombre_fantasia", nombre_fantasia)); 
	 		pairs.add(new BasicNameValuePair("razon_social", razon_social));
	 		pairs.add(new BasicNameValuePair("web", web));
	 		pairs.add(new BasicNameValuePair("id_vendedor", id_vendedor));
	 			
	 		try { 				
	 			httppost.setEntity(new UrlEncodedFormEntity(pairs));
	 				
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
	
	
	
	private class JsonSetTelefonos extends AsyncTask<String, Void, String> {
		String id_back;
		String telefono;
		String cod_area; 
		String id_tipo;
		
 		public JsonSetTelefonos(
 				String id_back,
				String telefono, 
				String cod_area, 
				String id_tipo) {
 			this.id_back = id_back;
 			this.telefono = telefono;
 			this.cod_area = cod_area; 
 			this.id_tipo = id_tipo;
		}
 		
		protected String doInBackground(String... params) {
	 		HttpClient httpclient = new DefaultHttpClient();
	 		HttpPost httppost = new HttpPost(params[0]);
	 			 		
	 		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
	 		pairs.add(new BasicNameValuePair("id_back", id_back));
	 		pairs.add(new BasicNameValuePair("telefono", telefono));
	 		pairs.add(new BasicNameValuePair("cod_area", cod_area)); 
	 		pairs.add(new BasicNameValuePair("id_tipo", id_tipo));
	 		pairs.add(new BasicNameValuePair("id_vendedor", id_vendedor));
	 			
	 		try { 				
	 			httppost.setEntity(new UrlEncodedFormEntity(pairs));
	 				
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
	
	
	
	
	private class JsonSetDireccion extends AsyncTask<String, Void, String> {
		String id_back;
		String direccion;
		String id_departamento; 
		String id_tipo;
		String id_provincia;
		
 		public JsonSetDireccion(
 				String id_back,
				String direccion, 
				String id_departamento, 
				String id_tipo,
				String id_provincia) {
 			this.id_back = id_back;
 			this.direccion = direccion;
 			this.id_departamento = id_departamento; 
 			this.id_tipo = id_tipo;
 			this.id_provincia = id_provincia;
		}
 		
		protected String doInBackground(String... params) {
	 		HttpClient httpclient = new DefaultHttpClient();
	 		HttpPost httppost = new HttpPost(params[0]);
	 			 		
	 		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
	 		pairs.add(new BasicNameValuePair("id_back", id_back));
	 		pairs.add(new BasicNameValuePair("direccion", direccion));
	 		pairs.add(new BasicNameValuePair("id_departamento", id_departamento)); 
	 		pairs.add(new BasicNameValuePair("id_tipo", id_tipo));
	 		pairs.add(new BasicNameValuePair("id_provincia", id_provincia));
	 		pairs.add(new BasicNameValuePair("id_vendedor", id_vendedor));
	 			
	 		try { 				
	 			httppost.setEntity(new UrlEncodedFormEntity(pairs));
	 				
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
	
	
	
	private class JsonSetMails extends AsyncTask<String, Void, String> {
		String id_back;
		String mail;
		String id_tipo;
		
 		public JsonSetMails(
 				String id_back,
				String mail, 
				String id_tipo) {
 			this.id_back = id_back;
 			this.mail = mail; 
 			this.id_tipo = id_tipo;
 		}
 		
		protected String doInBackground(String... params) {
	 		HttpClient httpclient = new DefaultHttpClient();
	 		HttpPost httppost = new HttpPost(params[0]);
	 			 		
	 		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
	 		pairs.add(new BasicNameValuePair("id_back", id_back));
	 		pairs.add(new BasicNameValuePair("mail", mail));
	 		pairs.add(new BasicNameValuePair("id_tipo", id_tipo));
	 		pairs.add(new BasicNameValuePair("id_vendedor", id_vendedor));
	 			
	 		try { 				
	 			httppost.setEntity(new UrlEncodedFormEntity(pairs));
	 				
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
	
	
	public void agregar(View view) {
		Intent intentClientes = new Intent(this, Clientes_Edit.class);
		intentClientes.putExtra("id", "0");
		startActivity(intentClientes);
	}
	 
}

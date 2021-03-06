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
import com.durox.app.Models.Direcciones_clientes_model;
import com.durox.app.Models.Documentos_model;
import com.durox.app.Models.Grupos_model;
import com.durox.app.Models.Iva_model;
import com.durox.app.Models.Mails_clientes_model;
import com.durox.app.Models.Telefonos_clientes_model;
import com.durox.app.Models.Tipos_model;
import com.durox.app.Models.Vendedores_model;
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
				}else if(carga == "grupos"){
					CargarGrupos();
				}else if(carga == "tipos"){
					CargarTipos();	
				}else if(carga == "telefonos"){
					CargarTelefonos();	
				}else if(carga == "sin_telefonos"){
					CargarSinTelefonos();
				}else if(carga == "mails"){
					CargarMails();	
				}else if(carga == "sin_mails"){
					CargarSinMails();
				}else if(carga == "direcciones"){
					CargarDirecciones();	
				}else if(carga == "sin_direcciones"){
					CargarSinDirecciones();	
				}else if(carga == "iva"){
					CargarIva();
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
	 		
	 		mModel.createTable();
	 			
	 		if(jsonMainNode.length() > 0){
	 			mModel.truncate();
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
	 			 				
	 			mModel.insert(
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
		
	}
	 
	 
	 
	public void CargarGrupos() {
		
	}
	 	
	 	
	public void CargarIva() {
		
	}
	
	
	public void CargarTipos() {
		
	}
	
	
	public void CargarTelefonos() {
		
	}
	
	
	
	public void CargarSinTelefonos() {
		
	}
	
	
	public void CargarMails() {
		
	}
	
	
	public void CargarSinMails() {
		
	}
	
	
	public void CargarDirecciones() {
		
	}
	
	
	public void CargarSinDirecciones() {
		
	}
	 
}

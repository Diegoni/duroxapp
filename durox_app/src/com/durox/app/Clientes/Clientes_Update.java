package com.durox.app.Clientes;

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
import com.durox.app.Models.Clientes_model;
import com.durox.app.Models.Departamentos_model;
import com.durox.app.Models.Direcciones_clientes_model;
import com.durox.app.Models.Grupos_model;
import com.durox.app.Models.Iva_model;
import com.durox.app.Models.Mails_clientes_model;
import com.durox.app.Models.Provincias_model;
import com.durox.app.Models.Telefonos_clientes_model;
import com.durox.app.Models.Tipos_model;
import com.durox.app.Models.Vendedores_model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class Clientes_Update extends MenuActivity {
	Config_durox config;
	Context mContext;
	SQLiteDatabase db;
	
	private String jsonResult;
	
	Vendedores_model mVendedor;
	String id_vendedor;
	
	public Clientes_Update(SQLiteDatabase db_enviada, Context context) {
		config = new Config_durox();
		db = db_enviada;
		mContext = context;
		
		mVendedor = new Vendedores_model(db);
	    id_vendedor = mVendedor.getID();
	}
	
	public void addNew() {
		String url;
		JsonReadTask taskclientes = new JsonReadTask();
		url = config.getIp(db)+"/actualizaciones/getClientes/";
		taskclientes.execute(new String[] { url });
	}
	
	
	
	public void actualizar_registros() {
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
		
		addNew();
	}
	
	
	/*---------------------------------------------------------------------------------
		Clases Para Leer el Json y actualizar tablas
	---------------------------------------------------------------------------------*/    

	// Async Task to access the web
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
				CargarClientes();
			}else{
				Toast.makeText(mContext, 
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
	 		
	 		if(jsonMainNode != null){
		 		if(jsonMainNode.length() > 0){
		 			mModel.truncate();
		 		}
		 		for (int i = 0; i < jsonMainNode.length(); i++) {
		 			JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
		 			
		 			String razon_social = jsonChildNode.optString("razon_social");
		 			Cursor cUnico = mModel.getID(razon_social);
					
					if(cUnico.getCount() > 0){
						//Toast.makeText(this, config.msjDuplicado(), Toast.LENGTH_SHORT).show();
					}else{
		 			 	mModel.insert(
		 			 		jsonChildNode.optString("id_cliente"),
		 			 		jsonChildNode.optString("nombre"),
		 			 		jsonChildNode.optString("apellido"),
			 				"0",				// modificado
			 				jsonChildNode.optString("cuit"),
				 			jsonChildNode.optString("id_grupo_cliente"),
				 			jsonChildNode.optString("id_iva"),
				 			jsonChildNode.optString("imagen"),
				 			jsonChildNode.optString("nombre_fantasia"),
			 				razon_social,
			 				jsonChildNode.optString("web"),
		 		 			jsonChildNode.optString("date_add"),
		 		 			jsonChildNode.optString("date_upd"),
		 		 			jsonChildNode.optString("eliminado"),
		 		 			jsonChildNode.optString("user_add"),
		 		 			jsonChildNode.optString("user_upd")
			 			);
					}
		 		}
	 			Toast.makeText(mContext, 
		  	  		config.msjRegistrosActualizados(subjet+" "+jsonMainNode.length()), Toast.LENGTH_SHORT).show();
	 		}
		} catch (JSONException e) {
	 		Toast.makeText(mContext, 
	 			config.msjError(e.toString()), Toast.LENGTH_SHORT).show();
	 	}
	 	
	 	
	 	
	 	
	 	
	 	
	 	subjet = "grupos";
		Grupos_model mGrupos = new Grupos_model(db); 
	 	
		try {
	 		JSONObject jsonResponse = new JSONObject(jsonResult);
	 		JSONArray jsonMainNode = jsonResponse.optJSONArray("grupos");
	 		
	 		if(jsonMainNode != null){
	 			if(jsonMainNode.length() > 0){
		  			mGrupos.truncate();
		  		}
		  		for (int i = 0; i < jsonMainNode.length(); i++) {
		  			JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
		  			
		  			String grupo = jsonChildNode.optString("grupo_nombre");
		  			Cursor cUnico = mModel.getID(grupo);
					
					if(cUnico.getCount() > 0){
						//Toast.makeText(this, config.msjDuplicado(), Toast.LENGTH_SHORT).show();
					}else{
		  				mGrupos.insert(
		  					jsonChildNode.optString("id_grupo_cliente"),
			  				grupo,
			  				jsonChildNode.optString("date_add"),
				  			jsonChildNode.optString("date_upd"),
				  			jsonChildNode.optString("eliminado"),
				  			jsonChildNode.optString("user_add"),
				  			jsonChildNode.optString("user_upd")
			  			);
					}
		  		}
		  			
		  		Toast.makeText(mContext, 
		  	  		config.msjRegistrosActualizados(subjet+" "+jsonMainNode.length()), Toast.LENGTH_SHORT).show();
	 		}
	  			
	  	} catch (JSONException e) {
	  		Toast.makeText(mContext, 
	  			config.msjError(e.toString()), Toast.LENGTH_SHORT).show();
	  	}
		
		
		
		
		
		
		
		subjet = "iva";
		Iva_model mIva = new Iva_model(db);
		
	 	try {
	  		JSONObject jsonResponse = new JSONObject(jsonResult);
	  		JSONArray jsonMainNode = jsonResponse.optJSONArray(subjet);
	  		
	  		if(jsonMainNode != null){
		  		if(jsonMainNode.length() > 0){
		  			mIva.truncate();
		  		}
		  		  
		  		for (int i = 0; i < jsonMainNode.length(); i++) {
		  			JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
		  			
		  			String iva = jsonChildNode.optString("iva");
		  			Cursor cUnico = mModel.getID(iva);
					
					if(cUnico.getCount() > 0){
						//Toast.makeText(this, config.msjDuplicado(), Toast.LENGTH_SHORT).show();
					}else{
		  			 	mIva.insert(
		  			 		jsonChildNode.optString("id_iva"),
			  				iva,
			  				jsonChildNode.optString("date_add"),
				  			jsonChildNode.optString("date_upd"),
				  			jsonChildNode.optString("eliminado"),
				  			jsonChildNode.optString("user_add"),
				  			jsonChildNode.optString("user_upd")
			  			);
					}
		  		}
		  		
		  		Toast.makeText(mContext, 
		  	 	 	config.msjRegistrosActualizados(subjet+" "+jsonMainNode.length()), Toast.LENGTH_SHORT).show();
	  		}	
	  	} catch (JSONException e) {
	  		Toast.makeText(mContext, 
	  		config.msjError(e.toString()), Toast.LENGTH_SHORT).show();
	  	}
		
	 	
	 	
	 	
	 	
	 	subjet = "tipos";
		Tipos_model mTipos = new Tipos_model(db);
		 
		try {
		 	JSONObject jsonResponse = new JSONObject(jsonResult);
		 	JSONArray jsonMainNode = jsonResponse.optJSONArray(subjet);
		 			
		 	if(jsonMainNode != null){		
			  	if(jsonMainNode.length() > 0){
			  		mTipos.truncate();
			   	}
			  		
			  	for (int i = 0; i < jsonMainNode.length(); i++) {
			  		JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
			  		
			  		String tipo = jsonChildNode.optString("tipo");
			  		Cursor cUnico = mModel.getID(tipo);
					
					if(cUnico.getCount() > 0){
						//Toast.makeText(this, config.msjDuplicado(), Toast.LENGTH_SHORT).show();
					}else{
			  			mTipos.insert(
			  				jsonChildNode.optString("id_tipo"),
				  			tipo, 
				  			jsonChildNode.optString("date_add"),
					  		jsonChildNode.optString("date_upd"),
					  		jsonChildNode.optString("eliminado"),
					  		jsonChildNode.optString("user_add"),
					  		jsonChildNode.optString("user_upd")
				  		);
					}
			  	}
			  			
			  	Toast.makeText(mContext, 
			  		config.msjRegistrosActualizados(subjet+" "+jsonMainNode.length()), Toast.LENGTH_SHORT).show();
		 	}  			
		} catch (JSONException e) {
		 	Toast.makeText(mContext, 
		 		config.msjError(e.toString()), Toast.LENGTH_SHORT).show();
		}
		
		
		
		
		
		
		subjet = "telefonos";
		Telefonos_clientes_model mTelefonos = new Telefonos_clientes_model(db);
		
		try {
		 	JSONObject jsonResponse = new JSONObject(jsonResult);
		 	JSONArray jsonMainNode = jsonResponse.optJSONArray(subjet);
		 			
		 	if(jsonMainNode != null){		
			  	if(jsonMainNode.length() > 0){
			  		mTelefonos.truncate();
			  	}
			  		
			  	for (int i = 0; i < jsonMainNode.length(); i++) {
			  		JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
			  		
			  		mTelefonos.insert(
			  			jsonChildNode.optString("id_telefono"),
					  	jsonChildNode.optString("id_tipo"),
					  	jsonChildNode.optString("telefono"),
					  	jsonChildNode.optString("cod_area"),
					  	jsonChildNode.optString("fax"),
					  	jsonChildNode.optString("date_add"),
					  	jsonChildNode.optString("date_upd"),
					  	jsonChildNode.optString("eliminado"),
					  	jsonChildNode.optString("user_add"),
					  	jsonChildNode.optString("user_upd")
			  		);
			  		
			  	}
			  			
			  	Toast.makeText(mContext, 
			  		config.msjRegistrosActualizados(subjet+" "+jsonMainNode.length()), Toast.LENGTH_SHORT).show();
		 	}  			
		} catch (JSONException e) {
		 	Toast.makeText(mContext, 
		 		config.msjError(e.toString()), Toast.LENGTH_SHORT).show();
		}
		
		
		
		
		
		
		
		
		
		
		subjet = "sin_clientes_telefonos";
		Telefonos_clientes_model mSinTelefonos = new Telefonos_clientes_model(db);
		 
		try {
		 	JSONObject jsonResponse = new JSONObject(jsonResult);
		 	JSONArray jsonMainNode = jsonResponse.optJSONArray(subjet);
		 	if(jsonMainNode != null){		
			  	if(jsonMainNode.length() > 0){
			  		mSinTelefonos.truncateSin();
			  	}
			  		
			  	for (int i = 0; i < jsonMainNode.length(); i++) {
			  		JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
			  		
			  		mSinTelefonos.insertSin(
			  			jsonChildNode.optString("id_sin_cliente_telefono"),
					  	jsonChildNode.optString("id_cliente"),
					  	jsonChildNode.optString("id_telefono"),
					  	jsonChildNode.optString("date_add"),
					  	jsonChildNode.optString("date_upd"),
					  	jsonChildNode.optString("eliminado"),
					  	jsonChildNode.optString("user_add"),
					  	jsonChildNode.optString("user_upd")
			  		);
			  		
			  	}
			  			
			  	Toast.makeText(mContext, 
			  		config.msjRegistrosActualizados(subjet+" "+jsonMainNode.length()), Toast.LENGTH_SHORT).show();
		 	}  			
		} catch (JSONException e) {
		 	Toast.makeText(mContext, 
		 		config.msjError(e.toString()), Toast.LENGTH_SHORT).show();
		}
		
		
		
		
		
		
		
		
		subjet = "mails";
		Mails_clientes_model mMails = new Mails_clientes_model(db);
		 
		try {
		 	JSONObject jsonResponse = new JSONObject(jsonResult);
		 	JSONArray jsonMainNode = jsonResponse.optJSONArray(subjet);
		 	if(jsonMainNode != null){		
			  	if(jsonMainNode.length() > 0){
			  		mMails.truncate();
			  	}
			  		
			  	for (int i = 0; i < jsonMainNode.length(); i++) {
			  		JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
			  		
			  		mMails.insert(
			  			jsonChildNode.optString("id_mail"),
					  	jsonChildNode.optString("id_tipo"),
					  	jsonChildNode.optString("mail"),
					  	jsonChildNode.optString("date_add"),
					  	jsonChildNode.optString("date_upd"),
					  	jsonChildNode.optString("eliminado"),
					  	jsonChildNode.optString("user_add"),
					  	jsonChildNode.optString("user_upd")
			  		);
			  		
			  	}
			  			
			  	Toast.makeText(mContext, 
			  		config.msjRegistrosActualizados(subjet+" "+jsonMainNode.length()), Toast.LENGTH_SHORT).show();
		 	}		
		} catch (JSONException e) {
		 	Toast.makeText(mContext, 
		 		config.msjError(e.toString()), Toast.LENGTH_SHORT).show();
		}
		
		
		
		
		subjet = "sin_clientes_mails";
		Mails_clientes_model mSinMails = new Mails_clientes_model(db);
		 
		try {
		 	JSONObject jsonResponse = new JSONObject(jsonResult);
		 	JSONArray jsonMainNode = jsonResponse.optJSONArray(subjet);
		 	if(jsonMainNode != null){		
			  	if(jsonMainNode.length() > 0){
			  		mSinMails.truncateSin();
			  	}
			  		
			  	for (int i = 0; i < jsonMainNode.length(); i++) {
			  		JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
			  		
			  		mSinMails.insertSin(
			  			jsonChildNode.optString("id_sin_cliente_mail"),
					  	jsonChildNode.optString("id_cliente"),
					  	jsonChildNode.optString("id_mail"),
					  	jsonChildNode.optString("date_add"),
					  	jsonChildNode.optString("date_upd"),
					  	jsonChildNode.optString("eliminado"),
					  	jsonChildNode.optString("user_add"),
					  	jsonChildNode.optString("user_upd")
			  		);
			  		
			  	}
			  			
			  	Toast.makeText(mContext, 
			  		config.msjRegistrosActualizados(subjet+" "+jsonMainNode.length()), Toast.LENGTH_SHORT).show();
		 	}		
		} catch (JSONException e) {
		 	Toast.makeText(mContext, 
		 		config.msjError(e.toString()), Toast.LENGTH_SHORT).show();
		}
		
		
		
		
		
		subjet = "direcciones";
		Direcciones_clientes_model mDirecciones = new Direcciones_clientes_model(db);
		 
		try {
		 	JSONObject jsonResponse = new JSONObject(jsonResult);
		 	JSONArray jsonMainNode = jsonResponse.optJSONArray(subjet);
		 	if(jsonMainNode != null){		
			  	if(jsonMainNode.length() > 0){
			  		mDirecciones.truncate();
			  	}
			  			  		
			  	for (int i = 0; i < jsonMainNode.length(); i++) {
			  		JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
			  		
			  		mDirecciones.insert( 
			  			jsonChildNode.optString("id_direccion"),
					  	jsonChildNode.optString("id_tipo"),
					  	jsonChildNode.optString("id_departamento"),
					  	jsonChildNode.optString("id_provincia"),
					  	jsonChildNode.optString("id_pais"),
					  	jsonChildNode.optString("direccion"),
					  	jsonChildNode.optString("cod_postal"),
					  	jsonChildNode.optString("date_add"),
					  	jsonChildNode.optString("date_upd"),
					  	jsonChildNode.optString("eliminado"),
					  	jsonChildNode.optString("user_add"),
					  	jsonChildNode.optString("user_upd")
			  		);
			  		
			  	}
			  			
			  	Toast.makeText(mContext, 
			  		config.msjRegistrosActualizados(subjet+" "+jsonMainNode.length()), Toast.LENGTH_SHORT).show();
		 	}		
		} catch (JSONException e) {
		 	Toast.makeText(mContext, 
		 		config.msjError(e.toString()), Toast.LENGTH_SHORT).show();
		}
		
		
		
		
		
		
		
		
		
		subjet = "sin_clientes_direcciones";
		Direcciones_clientes_model mSinDirecciones = new Direcciones_clientes_model(db);
		 
		try {
		 	JSONObject jsonResponse = new JSONObject(jsonResult);
		 	JSONArray jsonMainNode = jsonResponse.optJSONArray(subjet);
		 	if(jsonMainNode != null){		
			  	if(jsonMainNode.length() > 0){
			  		mSinDirecciones.truncateSin();
			  	}
			  		
			  	for (int i = 0; i < jsonMainNode.length(); i++) {
			  		JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
			  		
			  		mSinDirecciones.insertSin(
			  			jsonChildNode.optString("id_sin_cliente_direccion"),
					  	jsonChildNode.optString("id_cliente"),
					  	jsonChildNode.optString("id_direccion"),
					  	jsonChildNode.optString("date_add"),
					  	jsonChildNode.optString("date_upd"),
					  	jsonChildNode.optString("eliminado"),
					  	jsonChildNode.optString("user_add"),
					  	jsonChildNode.optString("user_upd")
			  		);
			  		
			  	}
			  			
			  	Toast.makeText(mContext, 
			  		config.msjRegistrosActualizados(subjet+" "+jsonMainNode.length()), Toast.LENGTH_SHORT).show();
		 	}  			
		} catch (JSONException e) {
		 	Toast.makeText(mContext, 
		 		config.msjError(e.toString()), Toast.LENGTH_SHORT).show();
		}
		
		
		
		subjet = "departamentos";
		Departamentos_model mDepartamentos = new Departamentos_model(db); 
	 	
		try {
	 		JSONObject jsonResponse = new JSONObject(jsonResult);
	 		JSONArray jsonMainNode = jsonResponse.optJSONArray("departamentos");
	 		if(jsonMainNode != null){	
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
		  			
		  		Toast.makeText(mContext, 
		  	  		config.msjRegistrosActualizados(subjet+" "+jsonMainNode.length()), Toast.LENGTH_SHORT).show();
	 		}		
	  	} catch (JSONException e) {
	  		Toast.makeText(mContext, 
	  			config.msjError(e.toString()), Toast.LENGTH_SHORT).show();
	  	}
		
		
		
		
		subjet = "provincias";
		Provincias_model mProvincias = new Provincias_model(db); 
	 	
		try {
	 		JSONObject jsonResponse = new JSONObject(jsonResult);
	 		JSONArray jsonMainNode = jsonResponse.optJSONArray("provincias");
	 		if(jsonMainNode != null){	
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
		  			
		  		Toast.makeText(mContext, 
		  	  		config.msjRegistrosActualizados(subjet+" "+jsonMainNode.length()), Toast.LENGTH_SHORT).show();
	 		}
	  	} catch (JSONException e) {
	  		Toast.makeText(mContext, 
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
}
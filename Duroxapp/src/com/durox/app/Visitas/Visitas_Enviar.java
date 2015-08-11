package com.durox.app.Visitas;

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

import com.androidbegin.filterlistviewimg.R;
import com.androidbegin.filterlistviewimg.R.id;
import com.androidbegin.filterlistviewimg.R.layout;
import com.durox.app.MainActivity;
import com.durox.app.Clientes.Clientes;
import com.durox.app.Models.Clientes_model;
import com.durox.app.Models.Visitas_model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Visitas_Enviar extends Activity {
	
	private String jsonResult;
	Clientes_model mCliente;
	SQLiteDatabase db;
	Visitas_model mVisitas;
	Cursor c;

	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.visitas_enviar);
		
		db = openOrCreateDatabase("Duroxapp", Context.MODE_PRIVATE, null);
		
		mVisitas = new Visitas_model(db);
		
		c = mVisitas.getRegistros();
		
		int cantidad = c.getCount();
		
		if(c.getCount() > 0){
			while(c.moveToNext()){
				String cadena = c.getString(0) +" "+ c.getString(2) +" "+ c.getString(3);
				
				JsonReadTask task = new JsonReadTask(
						c.getString(0),
						c.getString(1),
						c.getString(2),
						c.getString(3),
						cadena
				);
		 		String url = "http://10.0.2.2/durox/index.php/actualizaciones/setVisita/";
		 		task.execute(new String[] { url });
    		}		
		}
		else{
			Toast.makeText(this, "No hay clientes cargados", Toast.LENGTH_SHORT).show();
		}
 			
		
		
	}
	
	
	  // Async Task to access the web
	private class JsonReadTask extends AsyncTask<String, Void, String> {
		String id_cliente;
		String id_epoca_visita;
		String fecha;
		String valoracion;
		String descripcion;
		
 		public JsonReadTask(
 				String id_cliente,
 				String id_epoca_visita,
 				String fecha,
 				String valoracion,
 				String descripcion) {
 			this.id_cliente = id_cliente;
 			this.id_epoca_visita = id_epoca_visita;
 			this.fecha = fecha;
 			this.valoracion = valoracion;
 			this.descripcion = descripcion;
 		}
 		
	
		protected String doInBackground(String... params) {
	 		HttpClient httpclient = new DefaultHttpClient();
	 		HttpPost httppost = new HttpPost(params[0]);
	 		
	 		
	 		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
	 		pairs.add(new BasicNameValuePair("id_cliente", id_cliente));
	 		pairs.add(new BasicNameValuePair("id_epoca_visita", id_epoca_visita));
	 		pairs.add(new BasicNameValuePair("fecha", fecha));
	 		pairs.add(new BasicNameValuePair("valoracion", valoracion));
	 		pairs.add(new BasicNameValuePair("descripcion", descripcion));
	 			
	 		try { 				
	 			httppost.setEntity(new UrlEncodedFormEntity(pairs));
	 				
	 			HttpResponse response = httpclient.execute(httppost);
	 		}
	 	 
	 		catch (ClientProtocolException e) {
	 			e.printStackTrace();
	 		} catch (IOException e) {
	 			e.printStackTrace();
	 		}
	 			
	 		return null;
	 	}
	  
		protected void onPostExecute(String result) {
	 		CargarClientes();
	 			
	 	}
	}// end async task
	 	
	 	
	 public void CargarClientes() {
	 		try {
	 			JSONObject jsonResponse = new JSONObject(jsonResult);
	 			JSONArray jsonMainNode = jsonResponse.optJSONArray("visitas");
	 			
	 			if(jsonMainNode.length() > 0){
	 				mCliente.truncate();
	 				Toast.makeText(getApplicationContext(), 
	 			 			"Registros registros "+jsonMainNode.length() , Toast.LENGTH_SHORT).show();
	 			}
	 			  
	 			for (int i = 0; i < jsonMainNode.length(); i++) {
	 				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
	 				int id_back = jsonChildNode.optInt("id_back");
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
	 		
	 		//clientes_lista();
	  	}
}

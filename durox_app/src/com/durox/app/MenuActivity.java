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
import com.durox.app.Clientes.Clientes_Main;
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
import com.durox.app.Productos.Productos_Main;
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
 
public class MenuActivity extends Activity {
	
	public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
        case R.id.item1:
        	Toast.makeText(this, "Clientes", Toast.LENGTH_SHORT).show();
        	Intent intentClientes = new Intent(this, Clientes_Main.class);
    		startActivity(intentClientes);
    		return true;
    		
        case R.id.item2: 
        	Toast.makeText(this, "Productos", Toast.LENGTH_SHORT).show(); 
        	Intent intentProductos = new Intent(this, Productos_Main.class);
    		startActivity(intentProductos);
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
}
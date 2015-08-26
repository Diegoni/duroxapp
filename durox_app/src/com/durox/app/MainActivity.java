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
 
public class MainActivity extends MenuActivity {
 
	Config_durox config;
	SQLiteDatabase db;
	
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
    

}
  
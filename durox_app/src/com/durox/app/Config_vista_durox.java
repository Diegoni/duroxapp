package com.durox.app;

import com.example.durox_app.R;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Config_vista_durox extends MenuActivity {
	 
	Config_durox config;
	SQLiteDatabase db;
	
	EditText etIp;
	EditText etDocumentos;
	EditText etProductos;
	
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config);
        
        setTitle("Configuración - Parámetros ");
        getActionBar().setIcon(R.drawable.menuconfig);
        
        etIp = (EditText) findViewById(R.id.et_config_ip);
        /*
        etDocumentos = (EditText) findViewById(R.id.et_config_documento);
        etProductos = (EditText) findViewById(R.id.et_config_productos);
        */
        config = new Config_durox();
        
        db = openOrCreateDatabase(config.getDatabase(), Context.MODE_PRIVATE, null);
        
        etIp.setText(config.getRealIp(db));
        /*
        etDocumentos.setText(config.getDocumentos(db));
        etProductos.setText(config.getFichaProductos(db));
        */
    }
    
    
    public void guardar(View view){
    	config.setIP(etIp.getText().toString(), db);
    	/*
    	config.setDocumentos(etDocumentos.getText().toString(), db);
    	config.setFichaProductos(etProductos.getText().toString(), db);
    	*/
    	Toast.makeText(this, config.msjOkUpdate(), Toast.LENGTH_SHORT).show();
    	
    	Intent intentLogin = new Intent(this, Config_vista_durox.class);
  		startActivity(intentLogin);
    }
    
}
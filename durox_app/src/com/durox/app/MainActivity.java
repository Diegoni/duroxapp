package com.durox.app;



import com.example.durox_app.R;
import com.durox.app.Models.Clientes_model;
import com.durox.app.Models.Direcciones_clientes_model;
import com.durox.app.Models.Documentos_model;
import com.durox.app.Models.Epocas_model;
import com.durox.app.Models.Estados_presupuesto_model;
import com.durox.app.Models.Grupos_model;
import com.durox.app.Models.Iva_model;
import com.durox.app.Models.Lineas_Presupuestos_model;
import com.durox.app.Models.Mails_clientes_model;
import com.durox.app.Models.Pedidos_model;
import com.durox.app.Models.Presupuestos_model;
import com.durox.app.Models.Productos_model;
import com.durox.app.Models.Telefonos_clientes_model;
import com.durox.app.Models.Tipos_model;
import com.durox.app.Models.Vendedores_model;
import com.durox.app.Models.Visitas_model;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

 
public class MainActivity extends MenuActivity {
 
	Config_durox config;
	SQLiteDatabase db;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        setTitle("Durox");
        getActionBar().setIcon(R.drawable.ic_launcher);
        
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
		
		crearTablas();
   } 
    
   public void crearTablas(){
	   db = openOrCreateDatabase(config.getDatabase(), Context.MODE_PRIVATE, null);
	   
	   Clientes_model mCliente = new Clientes_model(db);
	   		mCliente.createTable();
	   Direcciones_clientes_model mDirecciones = new Direcciones_clientes_model(db);
	   		mDirecciones.createTable();
	   Documentos_model mDocumentos = new Documentos_model(db);
	   		mDocumentos.createTable();
	   Epocas_model mEpocas = new Epocas_model(db);
	   		mEpocas.createTable();
	   Grupos_model mGrupos = new Grupos_model(db);
	   		mGrupos.createTable();
	   Iva_model mIva = new Iva_model(db);
	   		mIva.createTable();
	   Lineas_Presupuestos_model mLineas = new Lineas_Presupuestos_model(db);
	   		mLineas.createTable();
	   Mails_clientes_model mMails = new Mails_clientes_model(db);
	   		mMails.createTable();
	   Pedidos_model mPedidos = new Pedidos_model(db);
	   		mPedidos.createTable();
	   Presupuestos_model mPresupuestos = new Presupuestos_model(db);
	   		mPresupuestos.createTable();
	   Productos_model mProductos = new Productos_model(db);
	   		mProductos.createTable();
	   Telefonos_clientes_model mTelefonos = new Telefonos_clientes_model(db);
	   		mTelefonos.createTable();
	   Tipos_model mTipos = new Tipos_model(db);
	   		mTipos.createTable();
	   Vendedores_model mVendedores = new Vendedores_model(db);
	   		mVendedores.createTable();
	   Visitas_model mVisitas = new Visitas_model(db);
	   		mVisitas.createTable(); 
	   Estados_presupuesto_model mEstados = new Estados_presupuesto_model(db);
	   		mEstados.createTable();		   		
   } 

}
  
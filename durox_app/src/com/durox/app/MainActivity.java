package com.durox.app;



import com.example.durox_app.R;
import com.durox.app.Alarmas.Alarmas_Update;
import com.durox.app.Clientes.Clientes_Update;
import com.durox.app.Documentos.Documentos_Update;
import com.durox.app.Models.Alarmas_model;
import com.durox.app.Models.Clientes_model;
import com.durox.app.Models.Condiciones_pago_model;
import com.durox.app.Models.Departamentos_model;
import com.durox.app.Models.Direcciones_clientes_model;
import com.durox.app.Models.Documentos_model;
import com.durox.app.Models.Epocas_model;
import com.durox.app.Models.Estados_presupuesto_model;
import com.durox.app.Models.Grupos_model;
import com.durox.app.Models.Iva_model;
import com.durox.app.Models.Lineas_Presupuestos_model;
import com.durox.app.Models.Mails_clientes_model;
import com.durox.app.Models.Mensajes_model;
import com.durox.app.Models.Modos_pago_model;
import com.durox.app.Models.Monedas_model;
import com.durox.app.Models.Pedidos_model;
import com.durox.app.Models.Presupuestos_model;
import com.durox.app.Models.Productos_model;
import com.durox.app.Models.Provincias_model;
import com.durox.app.Models.Telefonos_clientes_model;
import com.durox.app.Models.Tiempos_entrega_model;
import com.durox.app.Models.Tipos_model;
import com.durox.app.Models.Vendedores_model;
import com.durox.app.Models.Visitas_model;
import com.durox.app.Presupuestos.Presupuestos_Update;
import com.durox.app.Productos.Productos_Update;
import com.durox.app.Vendedores.Mensajes_Update;
import com.durox.app.Visitas.Epocas_Update;
import com.durox.app.Visitas.Visitas_Update;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

 
public class MainActivity extends MenuActivity {
 
	Config_durox config;
	SQLiteDatabase db;
	private ProgressDialog pDialog;
	
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
		String actualizar = i.getStringExtra("actualizar");

		if(user != null){
			Vendedores_model mVendedor = new Vendedores_model(db);
			
			mVendedor.insert(
				id_vendedor, 	// id_back, 
				user, 			// nombre, 
				"",				// apellido, 
				pass, 
				"", 			// imagen, 
				"", 			// id_origen, 
				"", 			// id_db, 
				"", 			// visto, 
				"", 			// date_add, 
				"", 			// date_upd, 
				"", 			// eliminado, 
				"", 			// user_add, 
				"" 			// user_upd
			);
			
			crearTablas();
		}
		
		if(actualizar != null){
			bigInsert();
		}
		
		
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
	   Departamentos_model mDepartamentos = new Departamentos_model(db);
	   		mDepartamentos.createTable();
	   Provincias_model mProvincias = new Provincias_model(db);
	   		mProvincias.createTable();	
	   Monedas_model mMonedas = new Monedas_model(db);
	   		mMonedas.createTable();
	   Mensajes_model mMensajes = new Mensajes_model(db);
	   		mMensajes.createTable();	
	   Alarmas_model mAlarmas = new Alarmas_model(db);
	   		mAlarmas.createTable();
	   Condiciones_pago_model mCondiciones = new Condiciones_pago_model(db);
	   		mCondiciones.createTable();		
	   Modos_pago_model mModos = new Modos_pago_model(db);
	   		mModos.createTable();
	   Tiempos_entrega_model mTiempos = new Tiempos_entrega_model(db);
	   		mTiempos.createTable();				
	   
	   bigInsert();
   } 
   
   public void bigInsert(){
	   pDialog = new ProgressDialog(MainActivity.this);
	   pDialog.setMessage("Actualizando....");
       pDialog.setIndeterminate(false);
       pDialog.setCancelable(false);
       pDialog.show();
       
        Alarmas_Update uAlarmas = new Alarmas_Update(db, this);	
			uAlarmas.actualizar_registros();
		Clientes_Update uClientes = new Clientes_Update(db, this);	
			uClientes.actualizar_registros();
		Documentos_Update uDocumentos = new Documentos_Update(db, this);
			uDocumentos.actualizar_registros();
		Epocas_Update uEpocas = new Epocas_Update(db, this);
			uEpocas.actualizar_registros();
		Mensajes_Update uMensajes = new Mensajes_Update(db, this);
			uMensajes.actualizar_registros();
		Presupuestos_Update uPresupuestos = new Presupuestos_Update(db, this);
			uPresupuestos.actualizar_registros();
		Productos_Update uProductos = new Productos_Update(db, this);
			uProductos.actualizar_registros();
		Visitas_Update uVisitas = new Visitas_Update(db, this);
			uVisitas.actualizar_registros();
		
		pDialog.dismiss();
   } 
}


  
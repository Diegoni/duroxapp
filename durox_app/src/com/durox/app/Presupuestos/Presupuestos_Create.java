package com.durox.app.Presupuestos;

import java.util.ArrayList;

import com.example.durox_app.R;
import com.durox.app.Config_durox;
import com.durox.app.MainActivity;
import com.durox.app.MenuActivity;
import com.durox.app.Models.Clientes_model;
import com.durox.app.Models.Lineas_Presupuestos_model;
import com.durox.app.Models.Presupuestos_model;
import com.durox.app.Models.Productos_model;
import com.durox.app.Models.Vendedores_model;

import android.os.Bundle;
import android.util.Log;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Presupuestos_Create extends MenuActivity {
	AutoCompleteTextView auto_producto;
	EditText etCantidad;
	EditText etComentario;
	
	SQLiteDatabase db;
	Cursor c;
	int cantidad;
	String[] p_nombre;
	
	ListView list;
	ArrayList<Linea_Presupuestos> arraylist = new ArrayList<Linea_Presupuestos>();
	Linea_Presupuestos_ListView adapter_listView;
	EditText editsearch;
	
	Productos_model mProductos;
	Lineas_Presupuestos_model mLineas;
	
	String cNombre;
	String cIdVisita;
	String truncate;
	String id_presupuesto;
	String id_linea;
	String id;
	
	Config_durox config;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.presupuestos_create);
		
		Intent intent = getIntent();
		cNombre = intent.getStringExtra("nombre");
		cIdVisita = intent.getStringExtra("id_visita");
		id_presupuesto = intent.getStringExtra("id_presupuesto");
		id = intent.getStringExtra("id");
		id_linea = intent.getStringExtra("id_linea");
		String truncate = intent.getStringExtra("truncate");
		
		if(truncate == null){
			truncate = "no"; 
		}
		
		Log.e("Presupuestos_Create", "cNombre "+cNombre);
		Log.e("Presupuestos_Create", "cIdVisita "+cIdVisita);
		Log.e("Presupuestos_Create", "id_presupuesto "+id_presupuesto);
		Log.e("Presupuestos_Create", "id "+id);
		Log.e("Presupuestos_Create ", "truncate "+truncate);
		
		Button btnGuardar = (Button) findViewById(R.id.guardar_presupuesto);
		Button btnAprobar = (Button) findViewById(R.id.btnAprobarPresupuesto);
		TextView txtNombre = (TextView) findViewById(R.id.txtNombre);
		TextView txtIDVistia = (TextView) findViewById(R.id.txt_idVisita);
		txtNombre.setText(cNombre);
		txtIDVistia.setText(cIdVisita);
				
		auto_producto = (AutoCompleteTextView) findViewById(R.id.autoProducto);
		etCantidad = (EditText) findViewById(R.id.etCantidad);
		etComentario = (EditText) findViewById(R.id.etComentario);
		
		config = new Config_durox();
		db = openOrCreateDatabase(config.getDatabase(), Context.MODE_PRIVATE, null);
		
		mProductos = new Productos_model(db);
		c = mProductos.getRegistros();
		cantidad = c.getCount();
		p_nombre = new String[cantidad];
		
		int j = 0;
				
		if(c.getCount() > 0){
			while(c.moveToNext()){
				p_nombre[j] = c.getString(4);
    			j = j + 1;
    		}		
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, p_nombre);
		auto_producto.setThreshold(1);
		auto_producto.setAdapter(adapter);
		
		mLineas = new Lineas_Presupuestos_model(db);
		
		if(id_linea != null){
			mLineas.deleteLinea(id_linea);
		}
		
		if(truncate.equals("truncate")){
			mLineas.truncate();
			btnGuardar.setEnabled(false);
			btnAprobar.setEnabled(false);
		}else{
			
			Cursor cursor;
			
			if(id_presupuesto == null || id_presupuesto.equals(" ") || id_presupuesto == ""+null){
				cursor = mLineas.getRegistros();
			} else {
				if (id.equals("0")){
					cursor = mLineas.getIDRegistro(id_presupuesto);
				} else {
					cursor = mLineas.getRegistro(id);
				}	
			}
			
			int cantidad_lineas = cursor.getCount();
			
			String[] producto_linea = new String[cantidad_lineas];
			String[] cantidad_linea = new String[cantidad_lineas];
			String[] precio_linea = new String[cantidad_lineas];
			String[] total_linea = new String[cantidad_lineas];
			String[] id_linea = new String[cantidad_lineas];
			
			int x = 0;
					
			if(cursor.getCount() > 0){
				while(cursor.moveToNext()){
					producto_linea[x] = cursor.getString(0);
					cantidad_linea[x] = cursor.getString(1);
					precio_linea[x] = cursor.getString(2);
					total_linea[x] = cursor.getString(3);
					id_linea[x] = cursor.getString(6);
					x = x + 1;
				}	
				
				list = (ListView) findViewById(R.id.list_linea);

				for (int i = 0; i < producto_linea.length; i++) {
					Linea_Presupuestos wp = new Linea_Presupuestos(
							id_linea[i],
							cantidad_linea[i],
							producto_linea[i],
							precio_linea[i],
							total_linea[i],
							cNombre,
							cIdVisita,
							id_presupuesto,
							id
					);
					
					arraylist.add(wp);
				}
				
				adapter_listView = new Linea_Presupuestos_ListView(this, arraylist);
				list.setAdapter(adapter_listView);
			}
						
			Cursor cursorLinea;	
			
			if(id_presupuesto == null || id_presupuesto.equals(" ") || id_presupuesto == ""+null){
				cursorLinea = mLineas.getRegistros();
			} else {
				if (id.equals("0")){
					cursorLinea = mLineas.getIDRegistro(id_presupuesto);
				} else {
					cursorLinea = mLineas.getRegistro(id);
				}	
			}
			
			if(cursorLinea.getCount() > 0){
				Float total = (float) 0;
				Float subtotal = (float) 0;
				
				while(cursorLinea.moveToNext()){
					subtotal = Float.parseFloat(cursorLinea.getString(3)); 
					total = total + subtotal;
				}
				
				TextView txtTotal = (TextView) findViewById(R.id.txt_pTotal);
				txtTotal.setText(""+total+"");
				
				btnGuardar.setEnabled(true);
				btnAprobar.setEnabled(true);
			} else {
				
				btnGuardar.setEnabled(false);
				btnAprobar.setEnabled(false);
			}
		}
	}
	
	
	
	public void guardar_detalle(View view){
		String producto = auto_producto.getText().toString();
		String cantidad = etCantidad.getText().toString();
		String comentario = etComentario.getText().toString();
		
		db = openOrCreateDatabase(config.getDatabase(), Context.MODE_PRIVATE, null);
		
		mLineas = new Lineas_Presupuestos_model(db);
		
		Cursor cursor = mProductos.getAutocomplete(producto);
		
		if(cursor.getCount() > 0){
			while(cursor.moveToNext()){
				
				Float precio = Float.parseFloat(cursor.getString(2)); 
				Float row_cant = Float.parseFloat(cantidad);
				Float subtotal = precio * row_cant;
				String subt = String.valueOf(subtotal);
				
				String linea_id_back = "0";
				String linea_id_presupuesto = "0";
				String linea_id_temporario = "0";
				
				
				if(id_presupuesto == null){
				}else{
					Cursor cursorLinea;
					
					if (id.equals("0")){
						cursorLinea = mLineas.getIDRegistro(id_presupuesto);
					} else {
						cursorLinea = mLineas.getRegistro(id);
					}	
					
					if(cursorLinea.getCount() > 0){
						while(cursorLinea.moveToNext()){
							linea_id_presupuesto = cursorLinea.getString(4);
							linea_id_temporario = cursorLinea.getString(5);
						}
					}
				}
				
				mLineas.insert(
						linea_id_back,			//id_back
						linea_id_temporario,	//id_temporario
						linea_id_presupuesto,	//id_presupuesto
						cursor.getString(0),	//id_producto
						cursor.getString(2), 	//precio
						cantidad,				//cantidad
						subt, 					//subtotal
						"1", 					//id_estado_producto_presupuesto
						comentario, 			//comentario
						null, 					//date_add
						null, 					//date_upd
						null,					//eliminado 
						null,					//user_add
						null 					//user_upd
				);
				Toast.makeText(this, config.msjOkInsert(), Toast.LENGTH_SHORT).show();
			}		
		} else {
			Toast.makeText(this, config.msjNoRegistro("producto"), Toast.LENGTH_SHORT).show();
		}
				
		Intent intent = new Intent(Presupuestos_Create.this, Presupuestos_Create.class);
		intent.putExtra("truncate", "no");
		intent.putExtra("nombre", cNombre);
		intent.putExtra("id_visita", cIdVisita);
				
		if(id_presupuesto == null){
			intent.putExtra("id_presupuesto", " ");
			intent.putExtra("id", " ");
		}else{
			intent.putExtra("id_presupuesto", ""+id_presupuesto);
			intent.putExtra("id", ""+id);
		}
		startActivity(intent);
	}
	
	
	
	public void limpiar_detalle(View view){
		Toast.makeText(this, config.msjOkDelete(), Toast.LENGTH_SHORT).show();
		
		Intent intent = new Intent(Presupuestos_Create.this, Presupuestos_Create.class);
		
		intent.putExtra("nombre", cNombre);
		intent.putExtra("id_visita", cIdVisita);
		intent.putExtra("truncate", "truncate");
		
		startActivity(intent);
	}
	
	
	
	
	public void guardar_presupuesto(View view){
		db = openOrCreateDatabase(config.getDatabase(), Context.MODE_PRIVATE, null);
		
		TextView txtTotal = (TextView) findViewById(R.id.txt_pTotal);
		String total = txtTotal.getText().toString();
		
		Clientes_model mCliente = new Clientes_model(db);
		
		Cursor cursorCliente = mCliente.getID(cNombre);
		
		String id_cliente = "1";
		
		if(cursorCliente.getCount() > 0){
			while(cursorCliente.moveToNext()){
				id_cliente = cursorCliente.getString(0); 
			}
		}
		
		Vendedores_model mVendedor = new Vendedores_model(db);
		Cursor cursorVendedor = mVendedor.getRegistros();
		String id_vendedor = "1";
		
		if(cursorVendedor.getCount() > 0){
			while(cursorVendedor.moveToNext()){
				id_vendedor = cursorVendedor.getString(1); 
			}
		}
		
		Presupuestos_model mPresupuestos = new Presupuestos_model(db);
		Lineas_Presupuestos_model mLineas = new Lineas_Presupuestos_model(db);
		
		mPresupuestos.createTable();
		
		if(id_presupuesto.length() == 1){
			Log.e("mPresupuestos.createTable() ENTRO", "id_presupuesto "+id_presupuesto);
			Log.e("mPresupuestos.createTable() ENTRO", "id_presupuesto.length() "+id_presupuesto.length());
			
			mPresupuestos.insert(
				"0", 			// id_back
				cIdVisita, 		// id_visita 
				id_cliente,		// id_cliente 
				id_vendedor,	// id_vendedor 
				"1",			// id_estado_presupuesto 
				total,			// total 
				"0",			// fecha 
				"1",			// id_origen
				"0",			// aprobado_back 
				"0",			// aprobado_front
				"0",			// visto_back 
				"1",			// visto_front
				null,			// date_add
				null,			// date_upd 
				null,			// eliminado 
				null,			// user_add 
				null			// user_upd
			);
		} else {
			mPresupuestos.update(total, id_presupuesto);
			Log.e("mPresupuestos.createTable() NO", "id_presupuesto "+id_presupuesto);
			Log.e("mPresupuestos.createTable() NO", "id_presupuesto.length() "+id_presupuesto.length());
		}
		
		
		
		Cursor last_insert = mPresupuestos.getLastInsert();
		
		if(last_insert.getCount() > 0){
			while(last_insert.moveToNext()){
				mLineas.setPresupuesto(last_insert.getString(0)); 
			}
		}	
		
		
		Toast.makeText(this, config.msjOkInsert(), Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(Presupuestos_Create.this, MainActivity.class);
		startActivity(intent);
	}
	
	
	
	public void aprobar_presupuesto(View view){
		db = openOrCreateDatabase(config.getDatabase(), Context.MODE_PRIVATE, null);
		
		TextView txtTotal = (TextView) findViewById(R.id.txt_pTotal);
		String total = txtTotal.getText().toString();
		
		Clientes_model mCliente = new Clientes_model(db);
		Cursor cursorCliente = mCliente.getID(cNombre);
		String id_cliente = "1";
		
		if(cursorCliente.getCount() > 0){
			while(cursorCliente.moveToNext()){
				id_cliente = cursorCliente.getString(0); 
			}
		}
		
		Vendedores_model mVendedor = new Vendedores_model(db);
		Cursor cursorVendedor = mVendedor.getRegistros();
		String id_vendedor = "1";
		
		if(cursorVendedor.getCount() > 0){
			while(cursorVendedor.moveToNext()){
				id_vendedor = cursorVendedor.getString(1); 
			}
		}
		
		Presupuestos_model mPresupuestos = new Presupuestos_model(db);
		Lineas_Presupuestos_model mLineas = new Lineas_Presupuestos_model(db);
		
		mPresupuestos.createTable();
		
		mPresupuestos.insert(
			"0", 			// id_back
			cIdVisita, 		// id_visita 
			id_cliente,		// id_cliente 
			id_vendedor,	// id_vendedor 
			"1",			// id_estado_presupuesto 
			total,			// total 
			"0",			// fecha 
			"1",			// id_origen
			"0",			// aprobado_back 
			"1",			// aprobado_front
			"0",			// visto_back 
			"1",			// visto_front
			null,			// date_add
			null,			// date_upd 
			null,			// eliminado 
			null,			// user_add 
			null			// user_upd
		);
		
		Cursor last_insert = mPresupuestos.getLastInsert();
		
		if(last_insert.getCount() > 0){
			while(last_insert.moveToNext()){
				mLineas.setPresupuesto(last_insert.getString(0)); 
			}
		}	
		
		Toast.makeText(this, config.msjOkInsert(), Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(Presupuestos_Create.this, MainActivity.class);
		startActivity(intent);
	}	
}

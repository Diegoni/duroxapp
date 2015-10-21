package com.durox.app.Productos;

import java.util.ArrayList;
import java.util.Locale;

import com.durox.app.Config_durox;
import com.durox.app.MenuActivity;
import com.durox.app.Models.Productos_model;
import com.durox.app.Productos.Productos;
import com.durox.app.Productos.Productos_ListView;
import com.example.durox_app.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class Productos_Main extends MenuActivity {
	
	// Declare Variables
	ListView list;
	Productos_ListView adapterp;
	EditText editsearch;
	
	String[] nombre;
	String[] id_back;
	String[] precio;
	String[] moneda;
	String[] codigo;
	int[] imagen;
	ArrayList<Productos> arraylistp = new ArrayList<Productos>();
	SQLiteDatabase db;
		
	String truncate;
	String sql;
	Cursor c;
	int j;	
		
	Productos_model mProductos;
	TextView content;
	
	Config_durox config;
	ProgressDialog pDialog;
	
	CharSequence orden;
	CharSequence filtro;
	
	private Button btn_orden;
	private Button btn_filtro; 
	
	String subjet;
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        config = new Config_durox();
        db = openOrCreateDatabase(config.getDatabase(), Context.MODE_PRIVATE, null);
        
        setTitle("Productos - Lista");
        getActionBar().setIcon(R.drawable.menuproductos);
        
        setContentView(R.layout.productos_listview);
        
        orden = "";
        filtro = "nombre";
        
        productos_lista(orden, filtro);
		
        btn_orden = (Button) findViewById(R.id.btn_orden);
        btn_orden.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				PopupMenu popup = new PopupMenu(Productos_Main.this, btn_orden);
				popup.getMenuInflater().inflate(R.menu.popup_producto, popup.getMenu());

				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					public boolean onMenuItemClick(MenuItem item) {
						orden = item.getTitle();
						
						productos_lista(orden, filtro);
						
						Toast.makeText(Productos_Main.this, "Orden : " + item.getTitle(), Toast.LENGTH_SHORT).show();
		                        return true;
					}
				});
		        
				popup.show();
			}
		});
        
        
        btn_filtro = (Button) findViewById(R.id.btn_filtro);
        btn_filtro.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				PopupMenu popup = new PopupMenu(Productos_Main.this, btn_filtro);
				popup.getMenuInflater().inflate(R.menu.popup_producto, popup.getMenu());

				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					public boolean onMenuItemClick(MenuItem item) {
						filtro = item.getTitle();
						
						productos_lista(orden, filtro);
						
						Toast.makeText(Productos_Main.this, "Filtro : " + item.getTitle(), Toast.LENGTH_SHORT).show();
		                        return true;
					}
				});
		        
				popup.show();
			}
		});
		
	}
	
	
	
	
	public void actualizar_productos(View view) {
		pDialog = new ProgressDialog(this);
        pDialog.setMessage("Actualizando....");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
		
		Productos_Update update = new Productos_Update(db, this);
		update.actualizar_registros();
		
		pDialog.dismiss();
		
		productos_lista(orden, filtro);  
	}
		
		
	
	
	
	
	public void productos_lista(CharSequence order, final CharSequence filtro){
		
		mProductos = new Productos_model(db);
		mProductos.createTable();
		c = mProductos.getRegistros(order);
		
		int cantidad_productos = c.getCount();
		
		nombre = new String[cantidad_productos];
		id_back = new String[cantidad_productos];
		precio = new String[cantidad_productos];
		moneda = new String[cantidad_productos];
		codigo = new String[cantidad_productos];
		imagen = new int[cantidad_productos];
		
		j = 0;
			
		if(c.getCount() > 0){
			while(c.moveToNext()){
				id_back[j] = c.getString(0);
				nombre[j] = c.getString(1);
				precio[j] = c.getString(2);
				moneda[j] = c.getString(4)+" "+c.getString(3);
				codigo[j] = c.getString(5);
				imagen[j] = R.drawable.productos; 
				j = j + 1;
			}	
		
			list = (ListView) findViewById(R.id.listview);
			arraylistp.clear();
			
			for (int i = 0; i < nombre.length; i++) {
				Productos wp = new Productos(
						id_back[i],
						nombre[i],
						precio[i], 
						moneda[i],
						codigo[i],
						imagen[i]
				);
				
				arraylistp.add(wp);
			}
			adapterp = new Productos_ListView(this, arraylistp);
			list.setAdapter(adapterp);
			
			editsearch = (EditText) findViewById(R.id.search);
			editsearch.setHint(filtro);
			editsearch.addTextChangedListener(new TextWatcher() {
			
				@Override
				public void afterTextChanged(Editable arg0) {
					String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
					adapterp.filter(text, filtro);
				}
			
				@Override
				public void beforeTextChanged(
						CharSequence arg0, 
						int arg1,
						int arg2, 
						int arg3) {
				}
			
				@Override
				public void onTextChanged(
						CharSequence arg0, 
						int arg1, 
						int arg2,
						int arg3) {
				}
			});
		}		
	}


}

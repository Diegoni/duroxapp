package com.durox.app.Presupuestos;

import java.util.ArrayList;
import java.util.Locale;

import com.androidbegin.filterlistviewimg.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Presupuestos_Create extends Activity 
{
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

	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.presupuestos_create);
		
		Intent intent = getIntent();
		String nombre = intent.getStringExtra("nombre");
		
		TextView txtNombre = (TextView) findViewById(R.id.txtNombre);
		txtNombre.setText(nombre);
		
		
		auto_producto = (AutoCompleteTextView) findViewById(R.id.autoProducto);
		etCantidad = (EditText) findViewById(R.id.etCantidad);
		etComentario = (EditText) findViewById(R.id.etComentario);
		
		db = openOrCreateDatabase("Duroxapp", Context.MODE_PRIVATE, null);
		
		c = db.rawQuery("SELECT * FROM productos", null);
		
		cantidad = c.getCount();
		
		p_nombre = new String[cantidad];
		
		int j = 0;
				
		if(c.getCount() > 0){
			while(c.moveToNext()){
				p_nombre[j] = c.getString(0);
    			j = j + 1;
    		}		
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, p_nombre);
		auto_producto.setThreshold(1);
		auto_producto.setAdapter(adapter);
		
		String truncate = "DROP TABLE IF EXISTS `linea_presupuestos`;";
		db.execSQL(truncate);
				
		db.execSQL("CREATE TABLE IF NOT EXISTS linea_presupuestos("
				+ "id_linea_presupuesto INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "producto VARCHAR,"
				+ "cantidad INT,"
				+ "comentario VARCHAR"
				+ ");");
		
		Cursor cursor = db.rawQuery("SELECT * FROM linea_presupuestos", null);
		
		int cantidad_lineas = cursor.getCount();
		
		String[] producto_linea = new String[cantidad_lineas];
		String[] cantidad_linea = new String[cantidad_lineas];
		String[] comentario_linea = new String[cantidad_lineas];
		
		int x = 0;
				
		if(cursor.getCount() == 0)
		{
			//showMessage("Error", "No records found");
			//return;
		}
		else
		{
			
			while(cursor.moveToNext())
			{
				producto_linea[x] = cursor.getString(0);
				cantidad_linea[x] = cursor.getString(1);
				comentario_linea[x] = cursor.getString(2);
				x = x + 1;
			}	
			
			// Locate the ListView in listview_main.xml
			list = (ListView) findViewById(R.id.list_linea);

			for (int i = 0; i < producto_linea.length; i++) 
			{
				//WorldPopulation wp = new WorldPopulation(rank[i], country[i],
				Linea_Presupuestos wp = new Linea_Presupuestos(
						producto_linea[i],
						cantidad_linea[i], 
						comentario_linea[i]
				);
				
				// Binds all strings into an array
				arraylist.add(wp);
			}
			
			// Pass results to ListViewAdapter Class
			adapter_listView = new Linea_Presupuestos_ListView(this, arraylist);
			
			// Binds the Adapter to the ListView
			list.setAdapter(adapter_listView);
		}
			
	}
	
	public void guardar_detalle(View view){
		String producto = auto_producto.getText().toString();
		String cantidad = etCantidad.getText().toString();
		String comentario = etComentario.getText().toString();
		
		db = openOrCreateDatabase("Duroxapp", Context.MODE_PRIVATE, null);
		
		db.execSQL("CREATE TABLE IF NOT EXISTS linea_presupuestos("
				+ "producto VARCHAR, "
				+ "cantidad INT, "
				+ "comentario VARCHAR "
				+ ");");
		
		String sql = "INSERT INTO `linea_presupuestos` (`producto`, `cantidad`, `comentario`) VALUES"
	    		+ "('" + producto + "', '" + cantidad + "', '" + comentario + "');";
				
		db.execSQL(sql);
		
		Toast.makeText(this, "El registro se ha guardado con éxito", Toast.LENGTH_SHORT).show();
		
		Intent intent = new Intent(Presupuestos_Create.this, Presupuestos_Create.class);
		startActivity(intent);
	}
}

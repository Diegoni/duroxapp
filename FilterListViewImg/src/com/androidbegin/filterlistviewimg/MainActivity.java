package com.androidbegin.filterlistviewimg;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
 
public class MainActivity extends Activity {
	
	// Declare Variables
	ListView list;
	Clientes_ListView adapter;
	Productos_ListView adapterp;
	EditText editsearch;
	
	String[] c_nombre;
	String[] p_nombre;
	String[] direccion;
	String[] detalle;
	int[] foto;
	int[] imagen;
	ArrayList<Clientes> arraylist = new ArrayList<Clientes>();
	ArrayList<Productos> arraylistp = new ArrayList<Productos>();
	SQLiteDatabase db;
	
	String truncate;
	String sql;
	Cursor c;
	int j;		
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    } 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
     public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.item1:
        	
/****************************************************************************************************
 **************************************************************************************************** 
 *			Clientes      	
 ****************************************************************************************************
 ***************************************************************************************************/
        	
        	setContentView(R.layout.clientes_listview);
    		
    		db = openOrCreateDatabase("Duroxapp", Context.MODE_PRIVATE, null);
    		db.execSQL("CREATE TABLE IF NOT EXISTS clientes("
    						+ "nombre VARCHAR,"
    						+ "domicilio VARCHAR,"
    						+ "foto VARCHAR"
    						+ ");");
    		
    		truncate = "DELETE FROM `clientes`";
    		db.execSQL(truncate);
    		
    		String sql = "INSERT INTO `clientes` (`nombre`, `domicilio`, `foto`) VALUES"
    		+ "('David', 'Godoy Cruz', 'David'),"
    		+ "('Matias', 'Godoy Cruz', 'Matias'),"
    		+ "('Ales', 'Luján de cuyo', 'Ale'),"
    		+ "('Seba', 'Luján de cuyo', 'Seba'),"
    		+ "('Dario', 'Luján de cuyo', 'Dario'),"
    		+ "('Jose', 'Maipú', 'Jose'),"
    		+ "('Juan', 'Maipú', 'Juan'),"
    		+ "('Pedro', 'Las Heras', 'Pedro'),"
    		+ "('Pepe', 'Las Heras', 'Pepe'),"
    		+ "('Mario', 'Godoy Cruz', 'Mario');";
    		
    		db.execSQL(sql);
    		
    		c = db.rawQuery("SELECT * FROM clientes", null);
    		
    		c_nombre = new String[10];
    		direccion = new String[10];
    		
    		int j = 0;
    				
    		if(c.getCount() > 0)
    		{
    			while(c.moveToNext())
        		{
    				c_nombre[j] = c.getString(0);
        			direccion[j] = c.getString(1);
        			j = j + 1;
        		}		
    		}
    		    			
    		
    		foto = new int[] 
    		{ 
    			R.drawable.david, 
    			R.drawable.matias,
    			R.drawable.ale, 
    			R.drawable.seba,
    			R.drawable.dario, 
    			R.drawable.jose, 
    			R.drawable.juan,
    			R.drawable.pedro, 
    			R.drawable.pepe, 
    			R.drawable.mario 
    		};

    		// Locate the ListView in listview_main.xml
    		list = (ListView) findViewById(R.id.listview);

    		for (int i = 0; i < c_nombre.length; i++) 
    		{
    			//WorldPopulation wp = new WorldPopulation(rank[i], country[i],
    			Clientes wp = new Clientes(
    					c_nombre[i],
    					direccion[i], 
    					foto[i]
    			);
    			
    			// Binds all strings into an array
    			arraylist.add(wp);
    		}

    		// Pass results to ListViewAdapter Class
    		adapter = new Clientes_ListView(this, arraylist);
    		
    		// Binds the Adapter to the ListView
    		list.setAdapter(adapter);
    		
    		// Locate the EditText in listview_main.xml
    		editsearch = (EditText) findViewById(R.id.search);

    		// Capture Text in EditText
    		editsearch.addTextChangedListener(new TextWatcher() {

    			@Override
    			public void afterTextChanged(Editable arg0) 
    			{
    				String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
    				adapter.filter(text);
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
    		Toast.makeText(this, "Clientes", Toast.LENGTH_SHORT).show(); 
            return true;
    		
        case R.id.item2: 
        	
/****************************************************************************************************
 **************************************************************************************************** 
 *			Productos      	
 ****************************************************************************************************
 ***************************************************************************************************/
        	        	
        	setContentView(R.layout.clientes_listview);
    		
    		db = openOrCreateDatabase("Duroxapp", Context.MODE_PRIVATE, null);
    		db.execSQL("CREATE TABLE IF NOT EXISTS productos("
    						+ "nombre VARCHAR,"
    						+ "detalle VARCHAR,"
    						+ "imagen VARCHAR"
    						+ ");");
    		
    		truncate = "DELETE FROM `productos`";
    		db.execSQL(truncate);
    		
    		sql = "INSERT INTO `productos` (`nombre`, `detalle`, `imagen`) VALUES"
    		+ "('Producto 0', 'Detalle producto 0', 'Producto 1'),"
    		+ "('Producto 1', 'Detalle producto 1', 'Producto 1'),"
    		+ "('Producto 2', 'Detalle producto 2', 'Producto 1'),"
    		+ "('Producto 3', 'Detalle producto 3', 'Producto 1'),"
    		+ "('Producto 4', 'Detalle producto 4', 'Producto 1'),"
    		+ "('Producto 5', 'Detalle producto 5', 'Producto 1'),"
    		+ "('Producto 6', 'Detalle producto 6', 'Producto 1'),"
    		+ "('Producto 7', 'Detalle producto 7', 'Producto 1'),"
    		+ "('Producto 8', 'Detalle producto 8', 'Producto 1'),"
    		+ "('Producto 9', 'Detalle producto 9', 'Producto 1');";
    		
    		db.execSQL(sql);
    		
    		c = db.rawQuery("SELECT * FROM productos", null);
    		
    		p_nombre = new String[10];
    		detalle = new String[10];
    		
    		j = 0;
    				
    		if(c.getCount() > 0)
    		{
    			while(c.moveToNext())
        		{
        			p_nombre[j] = c.getString(0);
        			detalle[j] = c.getString(1);
        			j = j + 1;
        		}		
    		}	
    		
    		imagen = new int[] 
    		{ 
    			R.drawable.articulo, 
    			R.drawable.articulo,
    			R.drawable.articulo, 
    			R.drawable.articulo,
    			R.drawable.articulo, 
    			R.drawable.articulo,
    			R.drawable.articulo, 
    			R.drawable.articulo,
    			R.drawable.articulo, 
    			R.drawable.articulo 
    		};
    		// Locate the ListView in listview_main.xml
    		list = (ListView) findViewById(R.id.listview);

    		for (int i = 0; i < p_nombre.length; i++) 
    		{
    			//WorldPopulation wp = new WorldPopulation(rank[i], country[i],
    			Clientes wp = new Clientes(
    					p_nombre[i],
    					detalle[i], 
    					imagen[i]
    			);
    			
    			// Binds all strings into an array
    			arraylist.add(wp);
    		}

    		// Pass results to ListViewAdapter Class
    		adapter = new Clientes_ListView(this, arraylist);
    		
    		// Binds the Adapter to the ListView
    		list.setAdapter(adapter);
    		
    		// Locate the EditText in listview_main.xml
    		editsearch = (EditText) findViewById(R.id.search);

    		// Capture Text in EditText
    		editsearch.addTextChangedListener(new TextWatcher() {

    			@Override
    			public void afterTextChanged(Editable arg0) 
    			{
    				String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
    				adapter.filter(text);
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
    		
            Toast.makeText(this, "Productos", Toast.LENGTH_SHORT).show(); 
            return true;
        case R.id.item3: 
            Toast.makeText(this, "Visitas", Toast.LENGTH_SHORT).show(); 
            return true;            
        case R.id.item4: 
            Toast.makeText(this, "Presupestos", Toast.LENGTH_SHORT).show(); 
            return true;
        case R.id.item5: 
            Toast.makeText(this, "Pedidos", Toast.LENGTH_SHORT).show(); 
            return true;
        case R.id.item6: 
            Toast.makeText(this, "Mensajes", Toast.LENGTH_SHORT).show(); 
            return true;            
        default:
            return super.onOptionsItemSelected(item);
        }
 
    }
 
}
  
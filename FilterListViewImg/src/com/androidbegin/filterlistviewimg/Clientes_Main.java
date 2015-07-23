package com.androidbegin.filterlistviewimg;

import java.util.ArrayList;
import java.util.Locale;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

public class Clientes_Main extends Activity 
{

	// Declare Variables
	ListView list;
	Clientes_ListView adapter;
	EditText editsearch;
	
	String[] nombre;
	String[] direccion;
	int[] imagen;
	ArrayList<Clientes> arraylist = new ArrayList<Clientes>();
	SQLiteDatabase db;
	
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clientes_listview);
		
		db = openOrCreateDatabase("Duroxapp", Context.MODE_PRIVATE, null);
		db.execSQL("CREATE TABLE IF NOT EXISTS clientes("
						+ "nombre VARCHAR,"
						+ "domicilio VARCHAR,"
						+ "foto VARCHAR"
						+ ");");
		
		String truncate = "DELETE FROM `clientes`";
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
		
		Cursor c = db.rawQuery("SELECT * FROM clientes", null);
		
		String[] nombre = new String[10];
		String[] direccion = new String[10];
		
		int j = 0;
				
		if(c.getCount() == 0)
		{
			//showMessage("Error", "No records found");
			return;
		}
		
		while(c.moveToNext())
		{
			nombre[j] = c.getString(0);
			direccion[j] = c.getString(1);
			j = j + 1;
		}				
		
		imagen = new int[] 
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

		for (int i = 0; i < nombre.length; i++) 
		{
			//WorldPopulation wp = new WorldPopulation(rank[i], country[i],
			Clientes wp = new Clientes(
					nombre[i],
					direccion[i], 
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
	}
}

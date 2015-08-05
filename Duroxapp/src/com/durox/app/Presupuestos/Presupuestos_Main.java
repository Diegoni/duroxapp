package com.durox.app.Presupuestos;

import java.util.ArrayList;
import java.util.Locale;

import com.androidbegin.filterlistviewimg.R;
import com.durox.app.Visitas.Visitas;
import com.durox.app.Visitas.Visitas_ListView;

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

public class Presupuestos_Main extends Activity 
{

	// Declare Variables
	ListView list;
	Visitas_ListView adapter;
	EditText editsearch;
	
	String[] nombre;
	String[] direccion;
	int[] imagen;
	ArrayList<Visitas> arraylist = new ArrayList<Visitas>();
	SQLiteDatabase db;
	
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.visitas_listview);
		
		db = openOrCreateDatabase("Duroxapp", Context.MODE_PRIVATE, null);
		
		Cursor cVisitas = db.rawQuery("SELECT * FROM visitas", null);
		
		String[] nombre = new String[cVisitas.getCount()];
		String[] epoca = new String[cVisitas.getCount()];
		String[] fecha = new String[cVisitas.getCount()];
		
		int j = 0;
				
		if(cVisitas.getCount() == 0)
		{
			//showMessage("Error", "No records found");
			return;
		}
		
		while(cVisitas.moveToNext())
		{
			nombre[j] = cVisitas.getString(0);
			epoca[j] = cVisitas.getString(1);
			fecha[j] = cVisitas.getString(2);
			j = j + 1;
		}				
		
		// Locate the ListView in listview_main.xml
		list = (ListView) findViewById(R.id.listview);

		for (int i = 0; i < nombre.length; i++) 
		{
			Visitas wp = new Visitas(
					nombre[i],
					epoca[i], 
					fecha[i]
			);
			
			// Binds all strings into an array
			arraylist.add(wp);
		}

		// Pass results to ListViewAdapter Class
		adapter = new Visitas_ListView(this, arraylist);
		
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

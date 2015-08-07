package com.durox.app.Visitas;

import com.androidbegin.filterlistviewimg.R;
import com.androidbegin.filterlistviewimg.R.id;
import com.androidbegin.filterlistviewimg.R.layout;
import com.durox.app.MainActivity;
import com.durox.app.Clientes.Clientes_model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

public class Visitas_Main extends Activity {
	AutoCompleteTextView auto_cliente;
	AutoCompleteTextView auto_epoca;
	EditText etfecha;
	String[] c_nombre;
	String[] epocas;
	SQLiteDatabase db;
	
	String truncate;
	String sql;
	Cursor c;
	int cantidad;
	int j;
	Clientes_model mCliente;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);
		
		setContentView(R.layout.visitas_main);
		
		auto_cliente = (AutoCompleteTextView) findViewById(R.id.autoClientes);
		auto_epoca = (AutoCompleteTextView) findViewById(R.id.autoEpocas);
		etfecha = (EditText) findViewById(R.id.fechass);
		
		db = openOrCreateDatabase("Duroxapp", Context.MODE_PRIVATE, null);
		
		mCliente = new Clientes_model(db);
		
		c = mCliente.getRegistros();
		
		cantidad = c.getCount();
		
		c_nombre = new String[cantidad];
		
		j = 0;
				
		if(c.getCount() > 0){
			while(c.moveToNext()){
				c_nombre[j] = c.getString(1);
				j = j + 1;
    		}		
		}
		else{
			Toast.makeText(this, "No hay clientes cargados", Toast.LENGTH_SHORT).show();
		}
		
		db.execSQL("CREATE TABLE IF NOT EXISTS `epocas_visitas`("
				+ "id_epoca INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "epoca VARCHAR"
				+ ");");

		truncate = "DELETE FROM `epocas_visitas`";
		db.execSQL(truncate);
		
		sql = "INSERT INTO `epocas_visitas` (`epoca`) VALUES"
		+ "('Verano'),"
		+ "('Pre Cosecha');";
				
		db.execSQL(sql);
		
		c = db.rawQuery("SELECT * FROM `epocas_visitas`", null);
		
		
		cantidad = c.getCount();
		
		epocas = new String[cantidad];
		
		j = 0;
				
		if(c.getCount() > 0){
			while(c.moveToNext()){
				epocas[j] = c.getString(0);
    			j = j + 1;
    		}		
		}
		
		ArrayAdapter<String> adapterProductos = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, epocas);
		auto_epoca.setThreshold(1);
		auto_epoca.setAdapter(adapterProductos);
		
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, c_nombre);
		auto_cliente.setThreshold(1);
		auto_cliente.setAdapter(adapter);
		
		
		Intent i = getIntent();
		String nombre = i.getStringExtra("nombre");
		
		if(nombre != ""){
			auto_cliente.setText(nombre);
		}
		
	}

	public void agregar_comentarios(View view) {
		String nombre = auto_cliente.getText().toString();
		String epoca = auto_epoca.getText().toString();
		String fecha = etfecha.getText().toString();

		Intent intent = new Intent(Visitas_Main.this, Visitas_Detalle.class);
		
		intent.putExtra("nombre", nombre);
		intent.putExtra("epoca", epoca);
		intent.putExtra("fecha", fecha);
		
		startActivity(intent);
	}
	
	public void guardar(View view){
		String nombre = auto_cliente.getText().toString();
		String epoca = auto_epoca.getText().toString();
		String fecha = etfecha.getText().toString();
		
		db = openOrCreateDatabase("Duroxapp", Context.MODE_PRIVATE, null);
		
		
		truncate = "DROP TABLE IF EXISTS `visitas`;";
		
		db.execSQL(truncate);
		
		db.execSQL("CREATE TABLE IF NOT EXISTS  `visitas `("
				+ "id_visita INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "nombre VARCHAR, "
				+ "epoca VARCHAR, "
				+ "fecha VARCHAR, "
				+ "comentario VARCHAR"
				+ ");");

		
		sql = "INSERT INTO `visitas` (`nombre`, `epoca`, `fecha`) VALUES"
	    		+ "('" + nombre + "', '" + epoca + "', '" + fecha + "');";
				
		db.execSQL(sql);
		
		Toast.makeText(this, "El registro se ha guardado con éxito", Toast.LENGTH_SHORT).show();
		
		Intent intent = new Intent(Visitas_Main.this, MainActivity.class);
		startActivity(intent);
	}

}

package com.durox.app.Models;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class Visitas_model extends Activity{
	
	SQLiteDatabase db;
	String sql;
	Cursor c;
	
	
	public Visitas_model(SQLiteDatabase db) {
		this.db = db;
	}
	
	public void createTable(){
		sql = "CREATE TABLE IF NOT EXISTS  `visitas2`("
				+ "id_visita INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "nombre VARCHAR, "
				+ "epoca VARCHAR, "
				+ "fecha VARCHAR, "
				+ "comentario VARCHAR"
				+ ");";
		
		db.execSQL(sql);
	}
	
	public Cursor getRegistros(){
		sql = "SELECT * FROM `visitas2`";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	
	public void insert(String nombre, String  epoca, String  fecha, String comentario){
		sql = "INSERT INTO `visitas2` (`nombre`, `epoca`, `fecha`, `comentario`) VALUES"
 			    + "('"+nombre+"', '"+epoca+"', '"+fecha+"', '"+comentario+"');";
 		
		//return sql;
		db.execSQL(sql);
 	}
	
	public void truncate(){
		sql = "DELETE FROM `visitas2`";
		db.execSQL(sql);
	}

}

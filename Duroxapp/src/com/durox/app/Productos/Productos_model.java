package com.durox.app.Productos;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Productos_model extends Activity{
	
	SQLiteDatabase db;
	String sql;
	Cursor c;
	
	
	public Productos_model(SQLiteDatabase db) {
		this.db = db;
	}
	
	public void createTable(){
		sql = "CREATE TABLE IF NOT EXISTS `productos`("
				+ "id_producto INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "nombre VARCHAR,"
				+ "detalle VARCHAR,"
				+ "imagen VARCHAR"
				+ ");";
		
		db.execSQL(sql);
	}
	
	public Cursor getRegistros(){
		sql = "SELECT * FROM productos";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	
	public void insert(String nombre, String  detalle, String  imagen){
		sql = "INSERT INTO `productos` (`nombre`, `detalle`, `imagen`) VALUES"
 			    + "('"+nombre+"', '"+detalle+"', '"+imagen+"');";
 			    
 		db.execSQL(sql);
 	}
	
	public void truncate(){
		sql = "DELETE FROM `productos`";
		db.execSQL(sql);
	}

}

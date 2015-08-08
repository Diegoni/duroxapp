package com.durox.app.Models;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Clientes_model extends Activity{
	
	SQLiteDatabase db;
	String sql;
	Cursor c;
	
	
	public Clientes_model(SQLiteDatabase db) {
		this.db = db;
	}
	
	public void createTable(){
		sql = "CREATE TABLE IF NOT EXISTS `clientes`("
				+ "id_cliente INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "nombre VARCHAR,"
				+ "domicilio VARCHAR,"
				+ "foto VARCHAR"
				+ ");";
		
		db.execSQL(sql);
	}
	
	public Cursor getRegistros(){
		sql = "SELECT * FROM clientes";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	
	public void insert(String nombre, String  apellido, String  cuit){
		sql = "INSERT INTO `clientes` (`nombre`, `domicilio`, `foto`) VALUES"
 			    + "('"+nombre+"', '"+apellido+"', '"+cuit+"');";
 			    
 		db.execSQL(sql);
 	}
	
	public void truncate(){
		sql = "DELETE FROM `clientes`";
		db.execSQL(sql);
	}

}

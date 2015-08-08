package com.durox.app.Models;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Lineas_Presupuestos_model extends Activity{
	
	SQLiteDatabase db;
	String sql;
	Cursor c;
	
	
	public Lineas_Presupuestos_model(SQLiteDatabase db) {
		this.db = db;
	}
	
	public void createTable(){
		sql = "CREATE TABLE IF NOT EXISTS linea_presupuestos("
				+ "id_linea_presupuesto INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "producto VARCHAR,"
				+ "cantidad INT,"
				+ "comentario VARCHAR"
				+ ");";
		
		db.execSQL(sql);
	}
	
	public Cursor getRegistros(){
		sql = "SELECT * FROM linea_presupuestos";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	
	public void insert(String producto, String  cantidad, String  comentario){
		sql = "INSERT INTO `linea_presupuestos` (`producto`, `cantidad`, `comentario`) VALUES"
 			    + "('"+producto+"', '"+cantidad+"', '"+comentario+"');";
 			    
 		db.execSQL(sql);
 	}
	
	public void truncate(){
		sql = "DELETE FROM `linea_presupuestos`";
		db.execSQL(sql);
	}

}

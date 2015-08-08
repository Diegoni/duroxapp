package com.durox.app.Models;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Epocas_model extends Activity{
	
	SQLiteDatabase db;
	String sql;
	Cursor c;
	
	
	public Epocas_model(SQLiteDatabase db) {
		this.db = db;
	}
	
	public void createTable(){
		sql = "CREATE TABLE IF NOT EXISTS `epocas_visitas`("
				+ "id_epoca INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "epoca VARCHAR"
				+ ");";
		
		db.execSQL(sql);
	}
	
	public Cursor getRegistros(){
		sql = "SELECT * FROM `epocas_visitas`";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	
	public void insert(String nombre, String  epoca, String  fecha){
		sql = "INSERT INTO `epocas_visitas` (`nombre`, `epoca`, `fecha`) VALUES"
 			    + "('"+nombre+"', '"+epoca+"', '"+fecha+"');";
 			    
 		db.execSQL(sql);
 	}
	
	public void truncate(){
		sql = "DELETE FROM `epocas_visitas`";
		db.execSQL(sql);
	}

}

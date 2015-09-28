package com.durox.app.Models;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Provincias_model extends Activity{
	
	SQLiteDatabase db;
	String sql;
	Cursor c;
	
	
	public Provincias_model(SQLiteDatabase db) {
		this.db = db;
	}
	
	
	public void createTable(){
		sql = "CREATE TABLE IF NOT EXISTS `provincias`("
				+ "id_provincia INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "id_back VARCHAR, "
				+ "id_pais VARCHAR, "
				+ "nombre_provincia VARCHAR"
				+ ");";
		
		db.execSQL(sql);
		
		Log.e("Consulta  ", sql);
	}
	
	
	public Cursor getRegistros(){
		createTable();
		
		sql = "SELECT "
				+ " id_back, "
				+ " id_pais, "
				+ " nombre_provincia"
			+ " FROM "
				+ " `provincias` ";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	
	public Cursor getRegistro(String id){
		sql = "SELECT "
				+ " id_back, "
				+ " id_pais, "
				+ " nombre_provincia"
			+ " FROM "
				+ " `provincias` "
			+ " WHERE "
				+ " provincias.id_back = '"+id+"' ";
		
		c = db.rawQuery(sql, null);
		
		Log.e("Consulta  ", sql);
		
		return c;
	}
	
	
	public void insert(
			String id_back,
			String id_pais,
			String nombre_provincia){
		sql = "INSERT INTO `provincias` ("
				+ "`id_back`, "
				+ "`id_pais`, "
				+ "`nombre_provincia` "				
			+ ") VALUES ("
				+ "'"+id_back+"', "
				+ "'"+id_pais+"', "
				+ "'"+nombre_provincia+"' "
 			    + ");";
		
		Log.e("Consulta  ", sql);
 			    
		db.execSQL(sql);
 	}
	
	
	public void truncate(){
		sql = "DELETE FROM `provincias`";
		db.execSQL(sql);
	}
	
	
	public Cursor getID(String provincia){
		sql = "SELECT "
				+ "id_back"
				+ " FROM provincias"
				+ " WHERE nombre_provincia = '"+provincia+"'";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
}
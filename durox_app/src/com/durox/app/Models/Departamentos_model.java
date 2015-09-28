package com.durox.app.Models;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Departamentos_model extends Activity{
	
	SQLiteDatabase db;
	String sql;
	Cursor c;
	
	
	public Departamentos_model(SQLiteDatabase db) {
		this.db = db;
	}
	
	
	public void createTable(){
		sql = "CREATE TABLE IF NOT EXISTS `departamentos`("
				+ "id_departamento INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "id_back VARCHAR,"
				+ "id_provincia VARCHAR,"
				+ "nombre_departamento VARCHAR"
				+ ");";
		
		db.execSQL(sql);
		
		Log.e("Consulta  ", sql);
	}
	
	
	public Cursor getRegistros(String id_provincia){
		createTable();
		
		sql = "SELECT "
				+ " id_back, "
				+ " id_provincia, "
				+ " nombre_departamento"
			+ " FROM "
				+ " `departamentos` "
			+ " WHERE "
				+ "id_provincia = '"+id_provincia+"'";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	
	public Cursor getRegistro(String id){
		sql = "SELECT "
				+ " id_back"
				+ " id_provincia,"
				+ " nombre_departamento"
			+ " FROM "
				+ " `departamentos` "
			+ " WHERE "
				+ " departamentos.id_back = '"+id+"' ";
		
		c = db.rawQuery(sql, null);
		
		Log.e("Consulta  ", sql);
		
		return c;
	}
	
	
	public void insert(
			String id_back,
			String id_provincia,
			String nombre_departamento){
		sql = "INSERT INTO `departamentos` ("
				+ "`id_back`, "
				+ "`id_provincia`, "
				+ "`nombre_departamento` "				
			+ ") VALUES ("
				+ "'"+id_back+"', "
				+ "'"+id_provincia+"', "
				+ "'"+nombre_departamento+"' "
 			    + ");";
		
		Log.e("Consulta  ", sql);
 			    
		db.execSQL(sql);
 	}
	
	
	public void truncate(){
		sql = "DELETE FROM `departamentos`";
		db.execSQL(sql);
	}
	
	
	public Cursor getID(String departamento){
		sql = "SELECT "
				+ "id_back"
				+ " FROM departamentos"
				+ " WHERE nombre_departamento = '"+departamento+"'";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
}
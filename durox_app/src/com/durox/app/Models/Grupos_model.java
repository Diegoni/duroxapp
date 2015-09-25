package com.durox.app.Models;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Grupos_model extends Activity{
	
	SQLiteDatabase db;
	String sql;
	Cursor c;
	
	
	public Grupos_model(SQLiteDatabase db) {
		this.db = db;
	}
	
	public void createTable(){
		sql = "CREATE TABLE IF NOT EXISTS `grupos_clientes`("
				+ "id_grupo_cliente INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "id_back VARCHAR, "
				+ "grupo VARCHAR, "
				+ "date_add VARCHAR, "
				+ "date_upd VARCHAR, "
				+ "eliminado VARCHAR, "
				+ "user_add VARCHAR, "
				+ "user_upd VARCHAR"
				+ ");";
		
		db.execSQL(sql);
	}
	
	public Cursor getRegistros(){
		createTable();
		
		sql = "SELECT * FROM `grupos_clientes`";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	
	public void insert(
			String id_back,
			String grupo, 
			String date_add, 
			String date_upd, 
			String eliminado, 
			String user_add, 
			String user_upd
		){
		sql = "INSERT INTO `grupos_clientes` ("
				+ "`id_back`, "
				+ "`grupo`, "
				+ "`date_add`, "
				+ "`date_upd`, "
				+ "`eliminado`, "
				+ "`user_add`, "
				+ "`user_upd`"
			+ ") VALUES ("
 			    + "'"+id_back+"', "
 			    + "'"+grupo+"', "
 			    + "'"+date_add+"', "
 			    + "'"+date_upd+"', "
 			    + "'"+eliminado+"', "
 			    + "'"+user_add+"', "
 			    + "'"+user_upd+"'"
 			+ ");";
 			    
 		db.execSQL(sql);
 	}
	
	public void truncate(){
		sql = "DELETE FROM `grupos_clientes`";
		db.execSQL(sql);
	}
	
	
	public Cursor getID(String grupo){
		sql = "SELECT "
				+ "id_back"
				+ " FROM grupos_clientes"
				+ " WHERE grupo = '"+grupo+"'";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}

}

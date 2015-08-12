package com.durox.app.Models;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Iva_model extends Activity{
	
	SQLiteDatabase db;
	String sql;
	Cursor c;
	
	
	public Iva_model(SQLiteDatabase db) {
		this.db = db;
	}
	
	public void createTable(){
		sql = "CREATE TABLE IF NOT EXISTS `iva`("
				+ "id_iva INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "id_back VARCHAR, "
				+ "iva VARCHAR, "
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
		
		sql = "SELECT * FROM `iva`";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	
	public void insert(
			String id_back,
			String iva, 
			String date_add, 
			String date_upd, 
			String eliminado, 
			String user_add, 
			String user_upd
		){
		sql = "INSERT INTO `iva` ("
				+ "`id_back`, "
				+ "`iva`, "
				+ "`date_add`, "
				+ "`date_upd`, "
				+ "`eliminado`, "
				+ "`user_add`, "
				+ "`user_upd`"
			+ ") VALUES ("
 			    + "'"+id_back+"', "
 			    + "'"+iva+"', "
 			    + "'"+date_add+"', "
 			    + "'"+date_upd+"', "
 			    + "'"+eliminado+"', "
 			    + "'"+user_add+"', "
 			    + "'"+user_upd+"'"
 			+ ");";
 			    
 		db.execSQL(sql);
 	}
	
	public void truncate(){
		sql = "DELETE FROM `iva`";
		db.execSQL(sql);
	}

}

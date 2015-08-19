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
				+ "id_back VARCHAR, "
				+ "epoca VARCHAR, "
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
		
		sql = "SELECT * FROM `epocas_visitas`";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	
	public void insert(
			String id_back,
			String epoca, 
			String date_add, 
			String date_upd, 
			String eliminado, 
			String user_add, 
			String user_upd
		){
		sql = "INSERT INTO `epocas_visitas` ("
				+ "`id_back`, "
				+ "`epoca`, "
				+ "`date_add`, "
				+ "`date_upd`, "
				+ "`eliminado`, "
				+ "`user_add`, "
				+ "`user_upd`"
			+ ") VALUES ("
 			    + "'"+id_back+"', "
 			    + "'"+epoca+"', "
 			    + "'"+date_add+"', "
 			    + "'"+date_upd+"', "
 			    + "'"+eliminado+"', "
 			    + "'"+user_add+"', "
 			    + "'"+user_upd+"'"
 			+ ");";
 			    
 		db.execSQL(sql);
 	}
	
	public void truncate(){
		sql = "DELETE FROM `epocas_visitas`";
		db.execSQL(sql);
	}
	
	public Cursor getID(String epoca){
		sql = "SELECT "
				+ "id_back"
				+ " FROM epocas_visitas"
				+ " WHERE epoca = '"+epoca+"'";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}

}

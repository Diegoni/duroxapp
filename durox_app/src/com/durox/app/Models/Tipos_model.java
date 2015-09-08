package com.durox.app.Models;


import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Tipos_model extends Activity{
	
	SQLiteDatabase db;
	String sql;
	Cursor c;
	
	
	public Tipos_model(SQLiteDatabase db) {
		this.db = db;
	}
	
	public void createTable(){
		sql = "CREATE TABLE IF NOT EXISTS `tipos`("
				+ "id_tipo INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "id_back VARCHAR,"
				+ "tipo VARCHAR,"
				+ "date_add VARCHAR,"
				+ "date_upd VARCHAR,"
				+ "eliminado VARCHAR,"
				+ "user_add VARCHAR,"
				+ "user_upd VARCHAR"
				+ ");";
		
		db.execSQL(sql);
	}
	
	public Cursor getRegistros(){
		createTable();
		
		sql = "SELECT "
				+ "id_back,"
				+ "tipo"
				+ " FROM tipos";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	
	public Cursor getRegistro(String id){
		sql = "SELECT "
				+ "tipo"
			+ " FROM tipos "
			+ " WHERE tipos.id_back = '"+id+"' ";
			
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	
	public void insert(
			String id_back,
			String tipo,
			String date_add,
			String date_upd,
			String eliminado,
			String  user_add,
			String user_upd){
		sql = "INSERT INTO `tipos` ("
				+ "`id_back`, "
				+ "`tipo`, "
				+ "`date_add`, "
				+ "`date_upd`, "
				+ "`eliminado`, "
				+ "`user_add`, "
				+ "`user_upd`"
			+ ") VALUES ("
				+ "'"+id_back+"', "
				+ "'"+tipo+"', "
				+ "'"+date_add+"', "
				+ "'"+date_upd+"', "
				+ "'"+eliminado+"', "
				+ "'"+user_add+"', "
				+ "'"+user_upd+"' "
 			    + ");";
		
		Log.e("Consulta  ", sql);
 			    
 		db.execSQL(sql);
 	}
	
	public void truncate(){
		sql = "DELETE FROM `tipos`";
		db.execSQL(sql);
	}
	
	public Cursor getID(String tipo){
		sql = "SELECT "
				+ "id_back"
				+ " FROM tipos"
				+ " WHERE tipo = '"+tipo+"'";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}

}

package com.durox.app.Models;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Mensajes_model extends Activity{
	
	SQLiteDatabase db;
	String sql;
	Cursor c;
	String id;
	
	public Mensajes_model(SQLiteDatabase db) {
		this.db = db;
	}
	
	
	
	public void createTable(){
		sql = "CREATE TABLE IF NOT EXISTS `mensajes`("
				+ "id_mensaje INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "id_back INT, "
				+ "asunto VARCHAR, "
				+ "mensaje VARCHAR, "
				+ "id_origen VARCHAR, "
				+ "id_mensaje_padre VARCHAR, "
				+ "visto VARCHAR, "
				+ "date_add VARCHAR, "
				+ "date_upd VARCHAR, "
				+ "eliminado VARCHAR, "
				+ "user_add VARCHAR, "
				+ "user_upd VARCHAR "
				+ ");";
		db.execSQL(sql);
	}
	
	
	
	public Cursor getMensajes(String e){
		createTable();
		
		if(e.equals("entrada")){
			sql = "SELECT "
					+ "id_back, "
					+ "asunto, "
					+ "mensaje, "
					+ "date_add "
				+ " FROM "
					+ "`mensajes` "
				+ " WHERE "
					+ "	id_origen = 2";
		}else{
			sql = "SELECT "
					+ "id_back, "
					+ "asunto, "
					+ "mensaje, "
					+ "date_add "
				+ " FROM "
					+ "`mensajes` "
				+ " WHERE "
					+ "	id_origen = 1";
		}
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
		
	
	
	public Cursor getRegistro(String id){
		sql = "SELECT "
				+ "id_back, "
				+ "asunto, "
				+ "mensaje, "
				+ "date_add "
			+ " FROM mensajes "
			+ " WHERE mensajes.id_back = '"+id+"' ";
			
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
		
	
	public void insert(
			String id_back,
			String asunto,
			String mensaje,
			String id_origen,
			String id_mensaje_padre,
			String visto,
			String date_add,
			String date_upd,
			String eliminado,
			String user_add,
			String user_upd){
		
		createTable();
		
		sql = "INSERT INTO `mensajes` ("
				+ "`id_back`, "
				+ "`asunto`, "
				+ "`mensaje`, "
				+ "`id_origen`, "
				+ "`id_mensaje_padre`, "
				+ "`visto`, "
				+ "`date_add`, "
				+ "`date_upd`, "
				+ "`eliminado`, "
				+ "`user_add`, "
				+ "`user_upd` "
			+ ") VALUES ("
				+ "'"+id_back+"', "
				+ "'"+asunto+"', "
				+ "'"+mensaje+"', "
				+ "'"+id_origen+"', "
				+ "'"+id_mensaje_padre+"', "
				+ "'"+visto+"', "
				+ "'"+date_add+"', "
				+ "'"+date_upd+"', "
				+ "'"+eliminado+"', "
				+ "'"+user_add+"', "
				+ "'"+user_upd+"' "
			    + ");";
 			    
 		db.execSQL(sql);
 		
 		Log.e("Consulta ", sql);
 	}
	
	
	
	public void truncate(){
		sql = "DELETE FROM `mensajes`";
		db.execSQL(sql);
	}
	
	
	
	public String getID(){
		sql = "SELECT "
				+ " id_back "
				+ " FROM `mensajes`";
		
		c = db.rawQuery(sql, null);
		
		if(c.getCount() > 0){
			while(c.moveToNext()){
				id = c.getString(0);
    		}
		}else{
			id = "0";
		}
		
		return id;
	}
	
	
	
	public Cursor getNuevos(){
		createTable();
		
		sql = "SELECT "
				+ "	* FROM "
				+ " `mensajes` "
				+ " WHERE id_back = '0'";
		c = db.rawQuery(sql, null);
		
		Log.e("Consulta ", sql);
		
		return c;
	}
}

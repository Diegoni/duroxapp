package com.durox.app.Models;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Vendedores_model extends Activity{
	
	SQLiteDatabase db;
	String sql;
	Cursor c;
	String id;
	
	public Vendedores_model(SQLiteDatabase db) {
		this.db = db;
	}
	
	
	public void createTable(){
		sql = "CREATE TABLE IF NOT EXISTS `vendedores`("
				+ "id_vendedor INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "id_back INT, "
				+ "nombre VARCHAR, "
				+ "apellido VARCHAR, "
				+ "pass VARCHAR, "
				+ "imagen VARCHAR, "
				+ "id_origen VARCHAR, "
				+ "id_db VARCHAR, "
				+ "visto VARCHAR, "
				+ "date_add VARCHAR, "
				+ "date_upd VARCHAR, "
				+ "eliminado VARCHAR, "
				+ "user_add VARCHAR, "
				+ "user_upd VARCHAR "
				+ ");";
		db.execSQL(sql);
	}
	
	
	public Cursor getRegistros(){
		createTable();
		
		sql = "SELECT "
				+ " * "
				+ " FROM `vendedores`";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
		
	
	public Cursor getRegistro(String id){
		sql = "SELECT "
				+ " * "
			+ " FROM vendedores "
			+ " WHERE vendedores.id_back = '"+id+"' ";
			
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
		
	public void insert(
			String id_back,
			String nombre,
			String apellido,
			String pass,
			String imagen,
			String id_origen,
			String id_db,
			String visto,
			String date_add,
			String date_upd,
			String eliminado,
			String user_add,
			String user_upd){
		createTable();
		
		sql = "INSERT INTO `vendedores` ("
				+ "`id_back`, "
				+ "`nombre`, "
				+ "`apellido`, "
				+ "`pass`, "
				+ "`imagen`, "
				+ "`id_origen`, "
				+ "`id_db`, "
				+ "`visto`, "
				+ "`date_add`, "
				+ "`date_upd`, "
				+ "`eliminado`, "
				+ "`user_add`, "
				+ "`user_upd` "
			+ ") VALUES ("
				+ "'"+id_back+"', "
				+ "'"+nombre+"', "
				+ "'"+apellido+"', "
				+ "'"+pass+"', "
				+ "'"+imagen+"', "
				+ "'"+id_origen+"', "
				+ "'"+id_db+"', "
				+ "'"+visto+"', "
				+ "'"+date_add+"', "
				+ "'"+date_upd+"', "
				+ "'"+eliminado+"', "
				+ "'"+user_add+"', "
				+ "'"+user_upd+"' "
			    + ");";
 			    
 		db.execSQL(sql);
 	}
	
	
	public void truncate(){
		sql = "DELETE FROM `vendedores`";
		db.execSQL(sql);
	}
	
	
	public String getID(){
		sql = "SELECT "
				+ " id_back "
				+ " FROM `vendedores`";
		
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

}

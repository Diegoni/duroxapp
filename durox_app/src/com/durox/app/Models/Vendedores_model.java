package com.durox.app.Models;

import java.math.BigInteger;

import android.app.Activity;
import android.content.Context;
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
				+ "id_vendedor INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "id_back INT,"
				+ "nombre VARCHAR,"
				+ "pass VARCHAR"
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
			String pass){
		createTable();
		
		sql = "INSERT INTO `vendedores` ("
				+ "`id_back`, "
				+ "`nombre`, "
				+ "`pass` "
			+ ") VALUES ("
				+ "'"+id_back+"', "
				+ "'"+nombre+"', "
				+ "'"+pass+"' "
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

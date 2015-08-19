package com.durox.app.Models;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Documentos_model extends Activity{
	
	SQLiteDatabase db;
	String sql;
	Cursor c;
	
	
	public Documentos_model(SQLiteDatabase db) {
		this.db = db;
	}
	
	public void createTable(){
		sql = "CREATE TABLE IF NOT EXISTS `documentos`("
				+ "id_documento INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "id_back VARCHAR,"
				+ "documento VARCHAR,"
				+ "date_add VARCHAR,"
				+ "date_upd VARCHAR,"
				+ "eliminado INT,"
				+ "user_add VARCHAR,"
				+ "user_upd VARCHAR"
				+ ");";
		db.execSQL(sql);
	}
	
	public Cursor getRegistros(){
		createTable();
		
		sql = "SELECT "
				+ "id_back ,"
				+ "documento ,"
				+ "date_add ,"
				+ "date_upd ,"
				+ "eliminado ,"
				+ "user_add ,"
				+ "user_upd "
				+ " FROM documentos";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	public Cursor getRegistro(String id){
		sql = "SELECT "
				+ "id_back ,"
				+ "documento ,"
				+ "date_add ,"
				+ "date_upd ,"
				+ "eliminado ,"
				+ "user_add ,"
				+ "user_upd "
			+ " FROM documentos "
			+ " WHERE documentos.id_back = '"+id+"'";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	
	public void insert(
					String id_back,
					String documento,
					String date_add,
					String date_upd,
					String eliminado,
					String user_add,
					String user_upd
			){
		sql = "INSERT INTO documentos("
				+"id_back,"
				+"documento,"
				+"date_add,"
				+"date_upd,"
				+"eliminado,"
				+"user_add,"
				+"user_upd"
			+")VALUES("
				+"'"+id_back+"',"
				+"'"+documento+"',"
				+"'"+date_add+"',"
				+"'"+date_upd+"',"
				+"'"+eliminado+"',"
				+"'"+user_add+"',"
				+"'"+user_upd+"'"
				+");";
			
 		db.execSQL(sql);

 	}
	
	public void truncate(){
		sql = "DELETE FROM `documentos`";
		db.execSQL(sql);
	}

}

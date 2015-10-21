package com.durox.app.Models;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Modos_pago_model extends Activity{
	
	SQLiteDatabase db;
	String sql;
	Cursor c;
	
	
	public Modos_pago_model(SQLiteDatabase db) {
		this.db = db;
	}
	
	public void createTable(){
		sql = "CREATE TABLE IF NOT EXISTS `modos_pago`("
				+ "id_modo_pago INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "id_back VARCHAR,"
				+ "modo_pago VARCHAR,"
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
				+ "modo_pago ,"
				+ "date_add ,"
				+ "date_upd ,"
				+ "eliminado ,"
				+ "user_add ,"
				+ "user_upd "
				+ " FROM modos_pago";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	public Cursor getRegistro(String id){
		sql = "SELECT "
				+ "id_back ,"
				+ "modo_pago ,"
				+ "date_add ,"
				+ "date_upd ,"
				+ "eliminado ,"
				+ "user_add ,"
				+ "user_upd "
			+ " FROM modos_pago "
			+ " WHERE modos_pago.id_back = '"+id+"'";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	
	public void insert(
					String id_back,
					String modo_pago,
					String date_add,
					String date_upd,
					String eliminado,
					String user_add,
					String user_upd
			){
		sql = "INSERT INTO modos_pago("
				+"id_back,"
				+"modo_pago,"
				+"date_add,"
				+"date_upd,"
				+"eliminado,"
				+"user_add,"
				+"user_upd"
			+")VALUES("
				+"'"+id_back+"',"
				+"'"+modo_pago+"',"
				+"'"+date_add+"',"
				+"'"+date_upd+"',"
				+"'"+eliminado+"',"
				+"'"+user_add+"',"
				+"'"+user_upd+"'"
				+");";
			
 		db.execSQL(sql);

 	}
	
	public void truncate(){
		sql = "DELETE FROM `modos_pago`";
		db.execSQL(sql);
	}

}

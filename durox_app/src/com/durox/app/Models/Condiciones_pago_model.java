package com.durox.app.Models;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Condiciones_pago_model extends Activity{
	
	SQLiteDatabase db;
	String sql;
	Cursor c;
	
	
	public Condiciones_pago_model(SQLiteDatabase db) {
		this.db = db;
	}
	
	public void createTable(){
		sql = "CREATE TABLE IF NOT EXISTS `condiciones_pago`("
				+ "id_condicion_pago INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "id_back VARCHAR,"
				+ "condicion_pago VARCHAR,"
				+ "dias VARCHAR,"
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
				+ "condicion_pago ,"
				+ "dias ,"
				+ "date_add ,"
				+ "date_upd ,"
				+ "eliminado ,"
				+ "user_add ,"
				+ "user_upd "
				+ " FROM condiciones_pago";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	public Cursor getRegistro(String id){
		sql = "SELECT "
				+ "id_back ,"
				+ "condicion_pago ,"
				+ "dias ,"
				+ "date_add ,"
				+ "date_upd ,"
				+ "eliminado ,"
				+ "user_add ,"
				+ "user_upd "
			+ " FROM condiciones_pago "
			+ " WHERE condiciones_pago.id_back = '"+id+"'";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	
	public void insert(
					String id_back,
					String condicion_pago,
					String dias,
					String date_add,
					String date_upd,
					String eliminado,
					String user_add,
					String user_upd
			){
		sql = "INSERT INTO condiciones_pago("
				+"id_back,"
				+"condicion_pago,"
				+"dias,"
				+"date_add,"
				+"date_upd,"
				+"eliminado,"
				+"user_add,"
				+"user_upd"
			+")VALUES("
				+"'"+id_back+"',"
				+"'"+condicion_pago+"',"
				+"'"+dias+"',"
				+"'"+date_add+"',"
				+"'"+date_upd+"',"
				+"'"+eliminado+"',"
				+"'"+user_add+"',"
				+"'"+user_upd+"'"
				+");";
			
 		db.execSQL(sql);

 	}
	
	public void truncate(){
		sql = "DELETE FROM `condiciones_pago`";
		db.execSQL(sql);
	}
	
	
	public String getID(String condicion){
		sql = "SELECT "
				+ "id_back"
				+ " FROM condiciones_pago"
				+ " WHERE condicion_pago = '"+condicion+"'";
		
		c = db.rawQuery(sql, null);
		String id = "0";
		if(c.getCount() > 0){
			while(c.moveToNext()){
				id = c.getString(0);
			}
		} 
		return id;
	}

}

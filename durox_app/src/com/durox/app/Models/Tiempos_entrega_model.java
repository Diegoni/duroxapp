package com.durox.app.Models;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Tiempos_entrega_model extends Activity{
	
	SQLiteDatabase db;
	String sql;
	Cursor c;
	
	
	public Tiempos_entrega_model(SQLiteDatabase db) {
		this.db = db;
	}
	
	public void createTable(){
		sql = "CREATE TABLE IF NOT EXISTS `tiempos_entrega`("
				+ "id_tiempo_entrega INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "id_back VARCHAR,"
				+ "tiempo_entrega VARCHAR,"
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
				+ "tiempo_entrega ,"
				+ "date_add ,"
				+ "date_upd ,"
				+ "eliminado ,"
				+ "user_add ,"
				+ "user_upd "
				+ " FROM tiempos_entrega";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	public Cursor getRegistro(String id){
		sql = "SELECT "
				+ "id_back ,"
				+ "tiempo_entrega ,"
				+ "date_add ,"
				+ "date_upd ,"
				+ "eliminado ,"
				+ "user_add ,"
				+ "user_upd "
			+ " FROM tiempos_entrega "
			+ " WHERE tiempos_entrega.id_back = '"+id+"'";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	
	public void insert(
					String id_back,
					String tiempo_entrega,
					String date_add,
					String date_upd,
					String eliminado,
					String user_add,
					String user_upd
			){
		sql = "INSERT INTO tiempos_entrega("
				+"id_back,"
				+"tiempo_entrega,"
				+"date_add,"
				+"date_upd,"
				+"eliminado,"
				+"user_add,"
				+"user_upd"
			+")VALUES("
				+"'"+id_back+"',"
				+"'"+tiempo_entrega+"',"
				+"'"+date_add+"',"
				+"'"+date_upd+"',"
				+"'"+eliminado+"',"
				+"'"+user_add+"',"
				+"'"+user_upd+"'"
				+");";
			
 		db.execSQL(sql);

 	}
	
	public void truncate(){
		sql = "DELETE FROM `tiempos_entrega`";
		db.execSQL(sql);
	}
	
	public String getID(String tiempo){
		sql = "SELECT "
				+ "id_back"
				+ " FROM tiempos_entrega"
				+ " WHERE tiempo_entrega = '"+tiempo+"'";
		
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

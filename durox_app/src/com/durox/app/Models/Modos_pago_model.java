package com.durox.app.Models;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
		
		sql = "CREATE TABLE IF NOT EXISTS `sin_presupuestos_modos` ("
				+ " `id_sin_presupuesto_modo` INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ " `id_back` VARCHAR,"
				+ " `id_presupuesto` VARCHAR,"
				+ " `id_presupuesto_front` VARCHAR,"
				+ " `id_modo_pago` VARCHAR,"
				+ " `date_add` VARCHAR,"
				+ " `date_upd` VARCHAR,"
				+ " `eliminado` VARCHAR,"
				+ " `user_add` VARCHAR,"
				+ " `user_upd` VARCHAR"
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

		Log.e("Consulta", sql);
		
 		db.execSQL(sql);
 	}
	
	
	public void insertSin(
		String id_back,
		String id_presupuesto,
		int idPresupuesto,
		String id_modo_pago,
		String date_add,
		String date_upd,
		String eliminado,
		String user_add,
		String user_upd
			){
		sql = "INSERT INTO sin_presupuestos_modos("
				+"id_back, "
				+"id_presupuesto, "
				+"id_presupuesto_front, "
				+"id_modo_pago, "
				+"date_add, "
				+"date_upd, "
				+"eliminado, "
				+"user_add, "
				+"user_upd "
			+" )VALUES( "
				+"'"+id_back+"',"
				+"'"+id_presupuesto+"',"
				+"'"+idPresupuesto+"',"
				+"'"+id_modo_pago+"',"
				+"'"+date_add+"',"
				+"'"+date_upd+"',"
				+"'"+eliminado+"',"
				+"'"+user_add+"',"
				+"'"+user_upd+"'"
				+");";
		
		Log.e("Consulta", sql);
	
		db.execSQL(sql);
	}
	
	
	public String getID(String modo){
		sql = "SELECT "
				+ "id_back"
				+ " FROM modos_pago"
				+ " WHERE modo_pago = '"+modo+"'";
		
		c = db.rawQuery(sql, null);
		String id = "0";
		if(c.getCount() > 0){
			while(c.moveToNext()){
				id = c.getString(0);
			}
		} 
		return id;
	}
	
	
	public void truncate(){
		sql = "DELETE FROM `modos_pago`";
		db.execSQL(sql);
		
		sql = "DELETE FROM `sin_presupuestos_modos`";
		db.execSQL(sql);
	}
	
	
	public Cursor getNuevos(){
		sql = "SELECT "
				+ "	* "
			+ "FROM "
				+ " `sin_presupuestos_modos` "
			+ " WHERE "
				+ "id_back = '0' ";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}

}

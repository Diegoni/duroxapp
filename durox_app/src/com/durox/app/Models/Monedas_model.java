package com.durox.app.Models;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Monedas_model extends Activity{
	
	SQLiteDatabase db;
	String sql;
	Cursor c;
	
	
	public Monedas_model(SQLiteDatabase db) {
		this.db = db;
	}
	
	
	public void createTable(){
		sql = "CREATE TABLE IF NOT EXISTS `monedas`("
				+ "id_moneda INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "id_back VARCHAR,"
				+ "moneda VARCHAR,"
				+ "abreviatura VARCHAR,"
				+ "simbolo VARCHAR,"
				+ "valor VARCHAR,"
				+ "id_pais VARCHAR,"
				+ "por_defecto VARCHAR,"
				+ "date_add VARCHAR,"
				+ "date_upd VARCHAR,"
				+ "eliminado VARCHAR,"
				+ "user_add VARCHAR,"
				+ "user_upd VARCHAR"
				+ ");";
		
		db.execSQL(sql);
		
		Log.e("Consulta  ", sql);
	}
	
	
	public Cursor getRegistros(){
		createTable();
		
		sql = "SELECT "
				+ " id_back, "
				+ " abreviatura, "
				+ " valor"
			+ " FROM "
				+ " `monedas` ";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	
	public Cursor getRegistro(String id){
		sql = "SELECT "
				+ " id_back"
				+ " abreviatura,"
				+ " valor"
			+ " FROM "
				+ " `monedas` "
			+ " WHERE "
				+ " monedas.id_back = '"+id+"' ";
		
		c = db.rawQuery(sql, null);
		
		Log.e("Consulta  ", sql);
		
		return c;
	}
	
	
	public void insert(
			String id_back,
			String moneda,
			String abreviatura,
			String simbolo,
			String valor,
			String id_pais,
			String por_defecto,
			String date_add,
			String date_upd,
			String eliminado,
			String user_add,
			String user_upd){
		sql = "INSERT INTO `monedas` ("
				+ "`id_back`, "
				+ "`moneda`, "
				+ "`abreviatura`, "	
				+ "`simbolo`, "
				+ "`valor`, "
				+ "`id_pais`, "
				+ "`por_defecto`, "
				+ "`date_add`, "
				+ "`date_upd`, "
				+ "`eliminado`, "
				+ "`user_add`, "
				+ "`user_upd` "
			+ ") VALUES ("
				+ "'"+id_back+"', "
				+ "'"+moneda+"', "
				+ "'"+abreviatura+"', "
				+ "'"+simbolo+"', "
				+ "'"+valor+"', "
				+ "'"+id_pais+"', "
				+ "'"+por_defecto+"', "
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
		sql = "DELETE FROM `monedas`";
		db.execSQL(sql);
	}
	
	
	public Cursor getID(String abreviatura){
		sql = "SELECT "
				+ "id_back"
				+ " FROM monedas"
				+ " WHERE abreviatura = '"+abreviatura+"'";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	
	public String  getPorDefecto(){
		sql = "SELECT "
				+ "abreviatura, "
				+ "simbolo "
				+ " FROM monedas"
				+ " WHERE por_defecto = 1";
		
		c = db.rawQuery(sql, null);
		
		String moneda = "";
		
		if(c.getCount() > 0){
			while(c.moveToNext()){
				moneda = c.getString(0)+" "+c.getString(1);
			}
		}
		
		return moneda;
	}
}
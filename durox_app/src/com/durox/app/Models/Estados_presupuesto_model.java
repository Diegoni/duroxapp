package com.durox.app.Models;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Estados_presupuesto_model extends Activity{
	
	SQLiteDatabase db;
	String sql;
	Cursor c;
	
	
	public Estados_presupuesto_model(SQLiteDatabase db) {
		this.db = db;
	}
	
	
	public void createTable(){
		sql = "CREATE TABLE IF NOT EXISTS `estados_presupuestos`("
				+ "id_estado_presupuesto INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "id_back VARCHAR, "
				+ "estado VARCHAR, "
				+ "text VARCHAR, "
				+ "eliminar_cliente VARCHAR, "
				+ "eliminar_vendedor VARCHAR, "
				+ "eliminar_visita VARCHAR, "
				+ "date_add VARCHAR, "
				+ "date_upd VARCHAR, "
				+ "eliminado VARCHAR, "
				+ "user_add VARCHAR, "
				+ "user_upd VARCHAR"
				+ ");";

		db.execSQL(sql);
	}
	
	
	public Cursor getRegistros(){
		createTable();
		
		sql = "SELECT * FROM `estados_presupuestos`";
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	
	public void insert(
			String id_back,
			String estado, 
			String text, 
			String eliminar_cliente, 
			String eliminar_vendedor, 
			String eliminar_visita, 
			String date_add, 
			String date_upd, 
			String eliminado, 
			String user_add, 
			String user_upd
		){
		sql = "INSERT INTO `estados_presupuestos` ("
				+ "`id_back`, "
				+ "`estado`, " 
				+ "`text`, " 
				+ "`eliminar_cliente`, " 
				+ "`eliminar_vendedor`, "
				+ "`eliminar_visita`, " 
				+ "`date_add`, "
				+ "`date_upd`, "
				+ "`eliminado`, "
				+ "`user_add`, "
				+ "`user_upd`"
			+ ") VALUES ("
 			    + "'"+id_back+"', "
 			    + "'"+estado+"', " 
 			   	+ "'"+text+"', " 
 			  	+ "'"+eliminar_cliente+"', " 
 			 	+ "'"+eliminar_vendedor+"', " 
 				+ "'"+eliminar_visita+"', " 
 			    + "'"+date_add+"', "
 			    + "'"+date_upd+"', "
 			    + "'"+eliminado+"', "
 			    + "'"+user_add+"', "
 			    + "'"+user_upd+"'"
 			+ ");";
 			    
 		db.execSQL(sql);
 	}
	
	
	public void truncate(){
		createTable();
		
		sql = "DELETE FROM `estados_presupuestos`";
		db.execSQL(sql);
	}
	
	
	public void delete(){
		createTable();
		
		sql = "DELETE FROM  `estados_presupuestos` ";
		db.execSQL(sql);
	}
	
	
	public Cursor getID(String estado){
		sql = "SELECT "
				+ "id_back"
				+ " FROM estados_presupuestos"
				+ " WHERE estado = '"+estado+"'";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}

}

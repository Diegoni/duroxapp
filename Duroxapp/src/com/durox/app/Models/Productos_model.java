package com.durox.app.Models;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Productos_model extends Activity{
	
	SQLiteDatabase db;
	String sql;
	Cursor c;
	
	
	public Productos_model(SQLiteDatabase db) {
		this.db = db;
	}
	
	public void createTable(){
		sql = "CREATE TABLE IF NOT EXISTS `productos`("
				+ "id_producto INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "id_back VARCHAR,"
				+ "codigo VARCHAR,"
				+ "codigo_lote VARCHAR,"
				+ "nombre VARCHAR,"
				+ "precio VARCHAR,"
				+ "precio_iva VARCHAR,"
				+ "precio_min VARCHAR,"
				+ "precio_min_iva VARCHAR,"
				+ "id_iva INT,"
				+ "ficha_tecnica VARCHAR,"
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
		
		sql = "SELECT * FROM productos";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	public Cursor getRegistro(String id){
		sql = "SELECT * FROM productos WHERE id_back = '"+id+"'";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	
	public void insert(
					String id_back,
					String codigo,
					String codigo_lote,
					String nombre,
					String precio,
					String precio_iva,
					String precio_min,
					String precio_min_iva,
					String id_iva,
					String ficha_tecnica,
					String date_add,
					String date_upd,
					String eliminado,
					String user_add,
					String user_upd
			){
		sql = "INSERT INTO productos("
				+"id_back,"
				+"codigo,"
				+"codigo_lote,"
				+"nombre,"
				+"precio,"
				+"precio_iva,"
				+"precio_min,"
				+"precio_min_iva,"
				+"id_iva,"
				+"ficha_tecnica,"
				+"date_add,"
				+"date_upd,"
				+"eliminado,"
				+"user_add,"
				+"user_upd"
			+")VALUES("
				+"'"+id_back+"',"
				+"'"+codigo+"',"
				+"'"+codigo_lote+"',"
				+"'"+nombre+"',"
				+"'"+precio+"',"
				+"'"+precio_iva+"',"
				+"'"+precio_min+"',"
				+"'"+precio_min_iva+"',"
				+"'"+id_iva+"',"
				+"'"+ficha_tecnica+"',"
				+"'"+date_add+"',"
				+"'"+date_upd+"',"
				+"'"+eliminado+"',"
				+"'"+user_add+"',"
				+"'"+user_upd+"'"
				+");";
			
 		db.execSQL(sql);
 	}
	
	public void truncate(){
		sql = "DELETE FROM `productos`";
		db.execSQL(sql);
	}

}

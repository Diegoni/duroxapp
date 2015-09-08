package com.durox.app.Models;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Lineas_Presupuestos_model extends Activity{
	
	SQLiteDatabase db;
	String sql;
	Cursor c;
	
	
	public Lineas_Presupuestos_model(SQLiteDatabase db) {
		this.db = db;
	}
	
	public void createTable(){
		sql = "CREATE TABLE IF NOT EXISTS linea_productos_presupuestos("
				+ "id_linea_presupuesto INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "id_back VARCHAR, "
				+ "id_temporario VARCHAR, "
				+ "id_presupuesto VARCHAR, "
				+ "id_producto VARCHAR, "
				+ "precio VARCHAR, "
				+ "cantidad VARCHAR, "
				+ "subtotal VARCHAR, "
				+ "id_estado_producto_presupuesto VARCHAR, "
				+ "comentario VARCHAR, "
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
				+ " linea_productos_presupuestos.cantidad, "
				+ " productos.nombre, "
				+ " linea_productos_presupuestos.precio, "
				+ " linea_productos_presupuestos.subtotal "
				+ " FROM linea_productos_presupuestos"
				+ " INNER JOIN productos ON(linea_productos_presupuestos.id_producto = productos.id_back)"
				+ " WHERE linea_productos_presupuestos.id_back = '0' AND linea_productos_presupuestos.id_temporario = '0'";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	
	public Cursor getRegistro(String id){
		createTable();
		
		sql = "SELECT "
				+ " linea_productos_presupuestos.cantidad, "
				+ " productos.nombre, "
				+ " linea_productos_presupuestos.precio, "
				+ " linea_productos_presupuestos.subtotal "
				+ " FROM linea_productos_presupuestos"
				+ " INNER JOIN productos ON(linea_productos_presupuestos.id_producto = productos.id_back)"
				+ " WHERE id_presupuesto = '"+id+"'";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	
	public Cursor getIDRegistro(String id){
		createTable();
		
		sql = "SELECT "
				+ " linea_productos_presupuestos.cantidad, "
				+ " productos.nombre, "
				+ " linea_productos_presupuestos.precio, "
				+ " linea_productos_presupuestos.subtotal "
				+ " FROM linea_productos_presupuestos"
				+ " INNER JOIN productos ON(linea_productos_presupuestos.id_producto = productos.id_back)"
				+ " WHERE id_temporario = '"+id+"'";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	
	public void insert(
			String id_back,
			String id_temporario,
			String id_presupuesto,
			String id_producto,
			String precio,
			String cantidad,
			String subtotal,
			String id_estado_producto_presupuesto,
			String comentario,
			String date_add,
			String date_upd,
			String eliminado,
			String user_add,
			String user_upd
		){
		
		sql = "INSERT INTO `linea_productos_presupuestos` ("
				+ "`id_back`, "
				+ "`id_temporario`, "
				+ "`id_presupuesto`, "
				+ "`id_producto`, "
				+ "`precio`, "
				+ "`cantidad`, "
				+ "`subtotal`, "
				+ "`id_estado_producto_presupuesto`, "
				+ "`comentario`, "
				+ "`date_add`, "
				+ "`date_upd`, "
				+ "`eliminado`, "
				+ "`user_add`, "
				+ "`user_upd`"
			+ ") VALUES ("
 			    + "'"+id_back+"', "
 			    + "'"+id_temporario+"', "
 			    + "'"+id_presupuesto+"', "
 			    + "'"+id_producto+"', "
 			    + "'"+precio+"', "
 			    + "'"+cantidad+"', "
 			    + "'"+subtotal+"', "
 			    + "'"+id_estado_producto_presupuesto+"', "
 			    + "'"+comentario+"', "
 			    + "'"+date_add+"', "
 			    + "'"+date_upd+"', "
 			    + "'"+eliminado+"', "
 			    + "'"+user_add+"', "
 			    + "'"+user_upd+"' "
 			+ ");";
 			    
 		db.execSQL(sql);
 	}
	
	public void truncate(){
		createTable();
		
		sql = "DELETE FROM `linea_productos_presupuestos` WHERE id_back = '0' AND id_temporario = '0'";
		db.execSQL(sql);
	}
	
	public void delete(){
		createTable();
		
		sql = "DELETE FROM "
				+ " `linea_productos_presupuestos` ";
				//+ " WHERE id_back = '0";
		db.execSQL(sql);
	}
	
	
	public void setPresupuesto(String id_presupuesto){
		createTable();
		
		sql = "UPDATE `linea_productos_presupuestos` SET id_temporario='"+id_presupuesto+"' WHERE id_back = '0' AND id_temporario = '0'";
		db.execSQL(sql);
	}
	
	
	public Cursor getLineasProceso(){
		sql =  "SELECT "
				+ " linea_productos_presupuestos.subtotal "
				+ " FROM linea_productos_presupuestos"
				+ " WHERE linea_productos_presupuestos.id_back = '0' AND linea_productos_presupuestos.id_temporario = '0'";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	public Cursor getNuevos(){
		createTable();
		
		sql = "SELECT "
				+ "	* FROM "
				+ " `linea_productos_presupuestos` "
				+ " WHERE id_back = '0' ";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
}

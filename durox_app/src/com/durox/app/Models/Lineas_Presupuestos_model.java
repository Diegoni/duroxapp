package com.durox.app.Models;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
				+ "id_moneda VARCHAR, "
				+ "valor_moneda VARCHAR, "
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
				+ " linea_productos_presupuestos.subtotal, "
				+ " linea_productos_presupuestos.id_presupuesto, "
				+ " linea_productos_presupuestos.id_temporario, "
				+ " linea_productos_presupuestos.id_linea_presupuesto, "
				+ " monedas.abreviatura, "
				+ " monedas.simbolo, "
				+ " linea_productos_presupuestos.valor_moneda, "
				+ " monedas.id_back "
			+ " FROM "
				+ "linea_productos_presupuestos"
			+ " INNER JOIN "
				+ "productos ON(linea_productos_presupuestos.id_producto = productos.id_back)"
			+ " INNER JOIN "
				+ "monedas ON(linea_productos_presupuestos.id_moneda = monedas.id_back)"
			+ " WHERE "
				+ "linea_productos_presupuestos.id_back = '0' AND linea_productos_presupuestos.id_temporario = '0'";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	
	public Cursor getRegistro(String id){
		createTable();
		
		sql = "SELECT "
				+ " linea_productos_presupuestos.cantidad, "
				+ " productos.nombre, "
				+ " linea_productos_presupuestos.precio, "
				+ " linea_productos_presupuestos.subtotal, "
				+ " linea_productos_presupuestos.id_presupuesto, "
				+ " linea_productos_presupuestos.id_temporario, "
				+ " linea_productos_presupuestos.id_linea_presupuesto, "
				+ " monedas.abreviatura, "
				+ " monedas.simbolo, "
				+ " linea_productos_presupuestos.valor_moneda, "
				+ " monedas.id_back "
			+ " FROM "
				+ "linea_productos_presupuestos"
			+ " INNER JOIN "
				+ "productos ON(linea_productos_presupuestos.id_producto = productos.id_back)"
			+ " INNER JOIN "
				+ "monedas ON(linea_productos_presupuestos.id_moneda = monedas.id_back)"
			+ " WHERE "
				+ "id_presupuesto = '"+id+"'";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	
	public Cursor getIDRegistro(String id){
		createTable();
		
		sql = "SELECT "
				+ " linea_productos_presupuestos.cantidad, "
				+ " productos.nombre, "
				+ " linea_productos_presupuestos.precio, "
				+ " linea_productos_presupuestos.subtotal, "
				+ " linea_productos_presupuestos.id_presupuesto, "
				+ " linea_productos_presupuestos.id_temporario, "
				+ " linea_productos_presupuestos.id_linea_presupuesto, "
				+ " monedas.abreviatura, "
				+ " monedas.simbolo, "
				+ " linea_productos_presupuestos.valor_moneda, "
				+ " monedas.id_back "
			+ " FROM "
				+ "linea_productos_presupuestos"
			+ " INNER JOIN "
				+ "productos ON(linea_productos_presupuestos.id_producto = productos.id_back)"
			+ " INNER JOIN "
				+ "monedas ON(linea_productos_presupuestos.id_moneda = monedas.id_back)"
			+ " WHERE "
				+ "id_temporario = '"+id+"'";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	
	public void insert(
			String id_back,
			String id_temporario,
			String id_presupuesto,
			String id_producto,
			String precio,
			String id_moneda,
			String valor_moneda,
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
				+ "`id_moneda`, "
				+ "`valor_moneda`, "
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
 			    + "'"+id_moneda+"', "
 			    + "'"+valor_moneda+"', "
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
		
		Log.e("sql ", sql);
 			    
 		db.execSQL(sql);
 	}
	
	public void truncate(){
		createTable();
		
		sql = "DELETE FROM "
				+ "`linea_productos_presupuestos` "
			+ "WHERE "
				+ "id_back = '0' AND id_temporario = '0'";
		db.execSQL(sql);
	}
	
	public void delete(){
		createTable();
		
		sql = "SELECT id_presupuesto, id_back FROM `presupuestos` WHERE aprobado_front = '1' AND visto_front = '1'";
		c = db.rawQuery(sql, null);
		
		if(c.getCount() > 0){
			while(c.moveToNext()){
				sql = "DELETE FROM `linea_productos_presupuestos` WHERE `id_temporario` = '"+c.getString(0)+"' ";
				db.execSQL(sql);
				sql = "DELETE FROM `linea_productos_presupuestos` WHERE `id_presupuesto` = '"+c.getString(1)+"' ";
				db.execSQL(sql);
			}
		}
		
		sql = "SELECT id_presupuesto, id_back FROM `presupuestos` WHERE visto_front = '0'"; 
		c = db.rawQuery(sql, null);
		
		
		if(c.getCount() > 0){
			while(c.moveToNext()){
				sql = "DELETE FROM `linea_productos_presupuestos` WHERE `id_temporario` = '"+c.getString(0)+"' ";
				db.execSQL(sql);
				sql = "DELETE FROM `linea_productos_presupuestos` WHERE `id_presupuesto` = '"+c.getString(1)+"' ";
				db.execSQL(sql);
			}
		}
	}
	
	public void deleteLinea(String linea){
		createTable();
		
		sql = "DELETE FROM "
				+ " `linea_productos_presupuestos` "
			+ " WHERE "
				+ " id_linea_presupuesto = '"+linea+"'";
		
		db.execSQL(sql);
	}
	
	
	public void setPresupuesto(String id_presupuesto){
		createTable();
		
		sql = "UPDATE "
				+ "`linea_productos_presupuestos` "
			+ "SET "
				+ "id_temporario='"+id_presupuesto+"' "
			+ "WHERE "
				+ "id_back = '0' AND id_temporario = '0'";
		db.execSQL(sql);
	}
	
	
	public Cursor getLineasProceso(){
		sql =  "SELECT "
				+ " linea_productos_presupuestos.subtotal "
			+ " FROM "
				+ "linea_productos_presupuestos"
			+ " WHERE "
				+ "linea_productos_presupuestos.id_back = '0' AND "
				+ "linea_productos_presupuestos.id_temporario = '0'";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	public Cursor getNuevos(){
		createTable();
		
		sql = "SELECT "
				+ "	* "
			+ "FROM "
				+ " `linea_productos_presupuestos` "
			+ " INNER JOIN "
				+ " `presupuestos`"
				+ " ON (linea_productos_presupuestos.id_temporario = presupuestos.id_presupuesto)"
			+ " WHERE "
				+ " `presupuestos`.`id_back` = '0' AND "
				+ " `presupuestos`.`aprobado_front` = '1' ";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	public void eliminarAnterior(String id){
		sql = "DELETE FROM `linea_productos_presupuestos` WHERE id_temporario = '"+id+"' ";
		db.execSQL(sql);
	}
	
}

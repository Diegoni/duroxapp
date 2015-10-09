package com.durox.app.Models;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
				+ "id_iva VARCHAR,"
				+ "id_moneda VARCHAR,"
				+ "ficha_tecnica VARCHAR,"
				+ "date_add VARCHAR,"
				+ "date_upd VARCHAR,"
				+ "eliminado VARCHAR,"
				+ "user_add VARCHAR,"
				+ "user_upd VARCHAR"
				+ ");";
		db.execSQL(sql);
	}
	
	public Cursor getRegistros(CharSequence order){
		createTable();
		
		if(order.equals("")){
			sql = "SELECT "
					+ "productos.id_back, "
					+ "productos.nombre, "
					+ "productos.precio, "
					+ "monedas.simbolo, "
					+ "monedas.abreviatura, "
					+ "productos.codigo "
				+ "FROM "
					+ "productos "
				+ "INNER JOIN "
					+ "monedas ON(productos.id_moneda = monedas.id_back)";
		}else if(order.equals("id")){
			sql = "SELECT "
					+ "productos.id_back, "
					+ "productos.nombre, "
					+ "productos.precio, "
					+ "monedas.simbolo, "
					+ "monedas.abreviatura, "
					+ "productos.codigo "
				+ "FROM "
					+ "productos "
				+ "INNER JOIN "
					+ "monedas ON(productos.id_moneda = monedas.id_back)"
				+ " ORDER BY "
					+ "id_back ";
			
		}else{
			sql = "SELECT "
					+ "productos.id_back, "
					+ "productos.nombre, "
					+ "productos.precio, "
					+ "monedas.simbolo, "
					+ "monedas.abreviatura, "
					+ "productos.codigo "
				+ "FROM "
					+ "productos "
				+ "INNER JOIN "
					+ "monedas ON(productos.id_moneda = monedas.id_back)"
				+ " ORDER BY "
					+order+"";
		}
		
		Log.e("Consulta  ", sql);
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	public Cursor getRegistro(String id){
		sql = "SELECT "
				+ "productos.id_back, "
				+ "productos.codigo, "
				+ "productos.codigo_lote, "
				+ "productos.nombre, "
				+ "productos.precio, "
				+ "productos.precio_iva, "
				+ "productos.precio_min, "
				+ "productos.precio_min_iva, "
				+ "productos.id_iva, "
				+ "productos.ficha_tecnica, "
				+ "productos.date_add, "
				+ "productos.date_upd, "
				+ "productos.eliminado, "
				+ "productos.user_add, "
				+ "productos.user_upd, "
				+ "monedas.moneda, "
				+ "monedas.abreviatura, "
				+ "monedas.simbolo "
			+ "FROM "
				+ "productos "
			+ "INNER JOIN "
				+ "monedas ON(productos.id_moneda = monedas.id_back)"
			+ " WHERE productos.id_back = '"+id+"'";
		
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
					String id_moneda,
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
				+"id_moneda,"
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
				+"'"+id_moneda+"',"
				+"'"+ficha_tecnica+"',"
				+"'"+date_add+"',"
				+"'"+date_upd+"',"
				+"'"+eliminado+"',"
				+"'"+user_add+"',"
				+"'"+user_upd+"'"
				+");";
		
		Log.e("Consulta ", sql);
			
 		db.execSQL(sql);

 	}
	
	public void truncate(){
		sql = "DELETE FROM `productos`";
		db.execSQL(sql);
	}
	
	public Cursor getAutocomplete(String producto){
		sql = "SELECT "
				+ "productos.id_back, "
				+ "productos.nombre, "
				+ "productos.precio, "
				+ "productos.id_moneda, "
				+ "monedas.valor "
			+ "FROM "
				+ "productos "
			+ "INNER JOIN "
				+ "monedas ON(productos.id_moneda = monedas.id_back)"
			+ "WHERE "
				+ "nombre = '"+producto+"' ";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	public Cursor getID(String nombre){
		sql = "SELECT "
				+ "id_back"
				+ " FROM productos"
				+ " WHERE nombre = '"+nombre+"'";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}

}

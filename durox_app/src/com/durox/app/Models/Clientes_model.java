package com.durox.app.Models;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Clientes_model extends Activity{
	
	SQLiteDatabase db;
	String sql;
	Cursor c;
	
	
	public Clientes_model(SQLiteDatabase db) {
		this.db = db;
	}
	
	public void createTable(){
		sql = "CREATE TABLE IF NOT EXISTS `clientes`("
				+ "id_cliente INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "id_back VARCHAR,"
				+ "modificado VARCHAR,"
				+ "nombre VARCHAR,"
				+ "apellido VARCHAR,"
				+ "cuit VARCHAR,"
				+ "id_grupo_cliente VARCHAR,"
				+ "id_iva VARCHAR,"
				+ "imagen VARCHAR,"
				+ "nombre_fantasia VARCHAR,"
				+ "razon_social VARCHAR,"
				+ "web VARCHAR,"
				+ "date_add VARCHAR,"
				+ "date_upd VARCHAR,"
				+ "eliminado VARCHAR,"
				+ "user_add VARCHAR,"
				+ "user_upd VARCHAR"
				+ ");";
		
		db.execSQL(sql);
	}
	
	public Cursor getRegistros(CharSequence orden){
		createTable();
		
		if(orden.equals("") || orden.equals("razon social") || orden.equals("Razón Social")){
			sql = "SELECT "
					+ "id_cliente,"
					+ "id_back,"
					+ "nombre,"
					+ "apellido,"
					+ "modificado, "
					+ "cuit,"
					+ "id_grupo_cliente,"
					+ "id_iva,"
					+ "imagen,"
					+ "nombre_fantasia,"
					+ "razon_social,"
					+ "web,"
					+ "date_add,"
					+ "date_upd,"
					+ "eliminado,"
					+ "user_add,"
					+ "user_upd"
				+ " FROM clientes "
					+ " ORDER BY razon_social";
		} else if(orden.equals("id") || orden.equals("ID")){
			sql = "SELECT "
					+ "id_cliente,"
					+ "id_back,"
					+ "nombre,"
					+ "apellido,"
					+ "modificado, "
					+ "cuit,"
					+ "id_grupo_cliente,"
					+ "id_iva,"
					+ "imagen,"
					+ "nombre_fantasia,"
					+ "razon_social,"
					+ "web,"
					+ "date_add,"
					+ "date_upd,"
					+ "eliminado,"
					+ "user_add,"
					+ "user_upd"
				+ " FROM clientes "
					+ " ORDER BY id_back ";
		} else {
			sql = "SELECT "
					+ "id_cliente,"
					+ "id_back,"
					+ "nombre,"
					+ "apellido,"
					+ "modificado, "
					+ "cuit,"
					+ "id_grupo_cliente,"
					+ "id_iva,"
					+ "imagen,"
					+ "nombre_fantasia,"
					+ "razon_social,"
					+ "web,"
					+ "date_add,"
					+ "date_upd,"
					+ "eliminado,"
					+ "user_add,"
					+ "user_upd"
				+ " FROM clientes "
					+ " ORDER BY "+orden+" ";
		}
		
		Log.e("Consulta", sql);		
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	
	public Cursor getClientesVisitas(){
		createTable();
		
		sql = "SELECT "
				+ "id_cliente,"
				+ "id_back,"
				+ "nombre,"
				+ "apellido,"
				+ "modificado, "
				+ "cuit,"
				+ "id_grupo_cliente,"
				+ "id_iva,"
				+ "imagen,"
				+ "nombre_fantasia,"
				+ "razon_social,"
				+ "web,"
				+ "date_add,"
				+ "date_upd,"
				+ "eliminado,"
				+ "user_add,"
				+ "user_upd"
			+ " FROM clientes "
			+ " WHERE "
				+ "id_back != '0'";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	
	public Cursor getRegistro(String id){
		sql = "SELECT "
				+ "clientes.id_cliente,"
				+ "clientes.id_back,"
				+ "clientes.nombre,"
				+ "clientes.apellido,"
				+ "clientes.modificado,"
				+ "clientes.cuit,"
				+ "grupos_clientes.grupo,"
				+ "iva.iva,"
				+ "clientes.imagen,"
				+ "clientes.nombre_fantasia,"
				+ "clientes.razon_social,"
				+ "clientes.web,"
				+ "clientes.date_add,"
				+ "clientes.date_upd,"
				+ "clientes.eliminado,"
				+ "clientes.user_add,"
				+ "clientes.user_upd"
			+ " FROM clientes "
			+ " INNER JOIN grupos_clientes ON(grupos_clientes.id_back = clientes.id_grupo_cliente)"
			+ " INNER JOIN iva ON(iva.id_back = clientes.id_iva)"
			+ " WHERE clientes.id_back = '"+id+"' ";
			
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	
	public void insert(
			String id_back,
			String nombre,
			String apellido,
			String modificado,
			String cuit,
			String id_grupo_cliente,
			String id_iva,
			String imagen,
			String nombre_fantasia,
			String razon_social,
			String web,
			String date_add,
			String date_upd,
			String eliminado,
			String  user_add,
			String user_upd){
		sql = "INSERT INTO `clientes` ("
				+ "`id_back`, "
				+ "`modificado`, "
				+ "`nombre`, "
				+ "`apellido`, "
				+ "`cuit`, "
				+ "`id_grupo_cliente`, "
				+ "`id_iva`, "
				+ "`imagen`, "
				+ "`nombre_fantasia`, "
				+ "`razon_social`, "
				+ "`web`, "
				+ "`date_add`, "
				+ "`date_upd`, "
				+ "`eliminado`, "
				+ "`user_add`, "
				+ "`user_upd`"
			+ ") VALUES ("
				+ "'"+id_back+"', "
				+ "'"+modificado+"', "
				+ "'"+nombre+"', "
				+ "'"+apellido+"', "
				+ "'"+cuit+"', "
				+ "'"+id_grupo_cliente+"', "
				+ "'"+id_iva+"', "
				+ "'"+imagen+"', "
				+ "'"+nombre_fantasia+"', "
				+ "'"+razon_social+"', "
				+ "'"+web+"', "
				+ "'"+date_add+"', "
				+ "'"+date_upd+"', "
				+ "'"+eliminado+"', "
				+ "'"+user_add+"', "
				+ "'"+user_upd+"' "
 			    + ");";
		
		Log.e("SQL ", sql);
 			    
 		db.execSQL(sql);
 	}
	
	
	public void truncate(){
		sql = "DELETE FROM `clientes`";
		db.execSQL(sql);
	}
	
	
	public Cursor getID(String razon_social){
		sql = "SELECT "
				+ "id_back"
				+ " FROM clientes"
				+ " WHERE razon_social = '"+razon_social+"'";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}

	
	public void edit(
			String id_back,
			String razon_social,
			String nombre_fantasia,
			String nombre,
			String apellido,
			String id_grupo_cliente,
			String id_iva,
			String web,
			String cuit
		){
		
		sql = "UPDATE `clientes`"
			+ " SET "
				+ " modificado 		= '1',"
				+ " razon_social 	= '"+razon_social+"',"
				+ " nombre_fantasia = '"+nombre_fantasia+"',"
				+ " nombre 			= '"+nombre+"',"
				+ " apellido 		= '"+apellido+"',"
				+ " id_grupo_cliente = '"+id_grupo_cliente+"',"
				+ " id_iva 			= '"+id_iva+"',"
				+ " web 			= '"+web+"',"
				+ " cuit 			= '"+cuit+"'"
			+ " WHERE "
				+ " id_back = '"+id_back+"'";
		
		db.execSQL(sql);
	}
	
	
	public Cursor getNuevos(){
		sql = "SELECT "				
				+ "id_back, "
				+ "nombre, "
				+ "apellido, "
				+ "cuit, "
				+ "id_grupo_cliente, "
				+ "id_iva, "
				+ "nombre_fantasia, "
				+ "razon_social, "
				+ "web "
			+ " FROM clientes"
			+ " WHERE modificado = '1'";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
}
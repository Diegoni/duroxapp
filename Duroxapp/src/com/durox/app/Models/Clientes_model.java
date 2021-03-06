package com.durox.app.Models;

import java.math.BigInteger;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
				+ "id_back INT,"
				+ "nombre VARCHAR,"
				+ "apellido VARCHAR,"
				+ "domicilio VARCHAR,"
				+ "cuit BIGINT,"
				+ "id_grupo_cliente INT,"
				+ "id_iva INT,"
				+ "imagen VARCHAR,"
				+ "nombre_fantasia VARCHAR,"
				+ "razon_social VARCHAR,"
				+ "web VARCHAR,"
				+ "date_add DATETIME,"
				+ "date_upd DATETIME,"
				+ "eliminado INT,"
				+ "user_add INT,"
				+ "user_upd INT"
				+ ");";
		
		db.execSQL(sql);
	}
	
	public Cursor getRegistros(){
		createTable();
		
		sql = "SELECT "
				+ "id_cliente,"
				+ "id_back,"
				+ "nombre,"
				+ "apellido,"
				+ "domicilio,"
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
				+ " FROM clientes";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	
	public Cursor getRegistro(String id){
		sql = "SELECT "
				+ "clientes.id_cliente,"
				+ "clientes.id_back,"
				+ "clientes.nombre,"
				+ "clientes.apellido,"
				+ "clientes.domicilio,"
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
			int id_back,
			String nombre,
			String apellido,
			String domicilio,
			String cuit,
			int id_grupo_cliente,
			int id_iva,
			String imagen,
			String nombre_fantasia,
			String razon_social,
			String web,
			String date_add,
			String date_upd,
			String eliminado,
			int  user_add,
			int user_upd){
		sql = "INSERT INTO `clientes` ("
				+ "`id_back`, "
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
 			    
 		db.execSQL(sql);
 	}
	
	public void truncate(){
		sql = "DELETE FROM `clientes`";
		db.execSQL(sql);
	}

}

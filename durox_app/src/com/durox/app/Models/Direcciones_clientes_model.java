package com.durox.app.Models;

import java.math.BigInteger;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Direcciones_clientes_model extends Activity{
	
	SQLiteDatabase db;
	String sql;
	Cursor c;
	
	
	public Direcciones_clientes_model(SQLiteDatabase db) {
		this.db = db;
	}
	
	public void createTable(){
		sql = "CREATE TABLE IF NOT EXISTS `direcciones`("
				+ "id_direccion INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "id_back VARCHAR,"
				+ "id_tipo VARCHAR,"
				+ "direccion VARCHAR,"
				+ "id_departamento VARCHAR,"
				+ "id_provincia VARCHAR,"
				+ "id_pais VARCHAR,"
				+ "cod_postal VARCHAR,"
				+ "date_add VARCHAR,"
				+ "date_upd VARCHAR,"
				+ "eliminado VARCHAR,"
				+ "user_add VARCHAR,"
				+ "user_upd VARCHAR"
				+ ");";
		
		db.execSQL(sql);
		
		Log.e("Consulta  ", sql);
		
		
		sql = "CREATE TABLE IF NOT EXISTS `sin_clientes_direcciones`("
				+ "id_sin_cliente_direccion INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "id_back VARCHAR,"
				+ "id_cliente VARCHAR,"
				+ "id_direccion VARCHAR,"
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
				+ " sin_clientes_direcciones.id_cliente,"
				+ " direcciones.id_back, "
				+ " direcciones.direccion, "
				+ " direcciones.id_departamento,"
				+ " direcciones.id_provincia,"
				+ " direcciones.id_pais,"
				+ " direcciones.cod_postal,"
				+ " tipos.tipo "
			+ " FROM "
				+ " `sin_clientes_direcciones` "
			+ " INNER JOIN "
				+ " direcciones ON(sin_clientes_direcciones.id_direccion = direcciones.id_back) "
			+ " INNER JOIN "
				+ " tipos ON(direcciones.id_tipo = tipos.id_back)";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	
	public Cursor getRegistro(String id){
		sql = "SELECT "
				+ " sin_clientes_direcciones.id_cliente,"
				+ " direcciones.id_back, "
				+ " direcciones.direccion, "
				+ " direcciones.id_departamento,"
				+ " direcciones.id_provincia,"
				+ " direcciones.id_pais,"
				+ " direcciones.cod_postal,"
				+ " tipos.tipo "
			+ " FROM "
				+ " `sin_clientes_direcciones` "
			+ " INNER JOIN "
				+ " direcciones ON(sin_clientes_direcciones.id_direccion = direcciones.id_back) "
			+ " INNER JOIN "
				+ " tipos ON(direcciones.id_tipo = tipos.id_back)"
			+ " WHERE "
				+ " sin_clientes_direcciones.id_cliente = '"+id+"' ";
			
		Log.e("Consulta  ", sql);
		
		c = db.rawQuery(sql, null);
		
		Log.e("Consulta  ", ""+c.getCount());
		
		return c;
	}
	
	
	public void insert(
			String id_back,
			String id_tipo,
			String direccion,
			String id_departamento,
			String id_provincia,
			String id_pais,
			String cod_postal,
			String date_add,
			String date_upd,
			String eliminado,
			String user_add,
			String user_upd){
		sql = "INSERT INTO `direcciones` ("
				+ "`id_back`, "
				+ "`id_tipo`, "
				+ "`direccion`, "
				+ "`id_departamento`, "
				+ "`id_provincia`, "
				+ "`id_pais`, "
				+ "`cod_postal`, "
				+ "`date_add`, "
				+ "`date_upd`, "
				+ "`eliminado`, "
				+ "`user_add`, "
				+ "`user_upd`"
			+ ") VALUES ("
				+ "'"+id_back+"', "
				+ "'"+id_tipo+"', "
				+ "'"+direccion+"', "
				+ "'"+id_departamento+"', "
				+ "'"+id_provincia+"', "
				+ "'"+id_pais+"', "
				+ "'"+cod_postal+"', "
				+ "'"+date_add+"', "
				+ "'"+date_upd+"', "
				+ "'"+eliminado+"', "
				+ "'"+user_add+"', "
				+ "'"+user_upd+"' "
 			    + ");";
		
		Log.e("Consulta  ", sql);
 			    
		db.execSQL(sql);
 	}
	
	
	public void insertSin(
			String id_back,
			String id_cliente,
			String id_direccion,
			String date_add,
			String date_upd,
			String eliminado,
			String  user_add,
			String user_upd){
		sql = "INSERT INTO `sin_clientes_direcciones` ("
				+ "`id_back`, "
				+ "`id_cliente`, "
				+ "`id_direccion`, "
				+ "`date_add`, "
				+ "`date_upd`, "
				+ "`eliminado`, "
				+ "`user_add`, "
				+ "`user_upd`"
			+ ") VALUES ("
				+ "'"+id_back+"', "
				+ "'"+id_cliente+"', "
				+ "'"+id_direccion+"', "
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
		sql = "DELETE FROM `direcciones`";
		db.execSQL(sql);
	}
	
	public void truncateSin(){
		sql = "DELETE FROM `sin_clientes_direcciones`";
		db.execSQL(sql);
	}
	
	
	public Cursor getID(String direccion){
		sql = "SELECT "
				+ "id_back"
				+ " FROM direcciones"
				+ " WHERE direccion = '"+direccion+"'";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}

}

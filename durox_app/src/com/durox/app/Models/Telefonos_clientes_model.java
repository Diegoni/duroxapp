package com.durox.app.Models;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Telefonos_clientes_model extends Activity{
	
	SQLiteDatabase db;
	String sql;
	Cursor c;
	
	
	public Telefonos_clientes_model(SQLiteDatabase db) {
		this.db = db;
	}
	
	public void createTable(){
		sql = "CREATE TABLE IF NOT EXISTS `telefonos`("
				+ "id_telefono INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "id_back VARCHAR,"
				+ "modificado VARCHAR,"
				+ "id_tipo VARCHAR,"
				+ "telefono VARCHAR,"
				+ "cod_area VARCHAR,"
				+ "fax VARCHAR,"
				+ "date_add VARCHAR,"
				+ "date_upd VARCHAR,"
				+ "eliminado VARCHAR,"
				+ "user_add VARCHAR,"
				+ "user_upd VARCHAR"
				+ ");";
		
		db.execSQL(sql);
		
		
		sql = "CREATE TABLE IF NOT EXISTS `sin_clientes_telefonos`("
				+ "id_sin_cliente_telefono INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "id_back VARCHAR,"
				+ "id_cliente VARCHAR,"
				+ "id_telefono VARCHAR,"
				+ "date_add VARCHAR,"
				+ "date_upd VARCHAR,"
				+ "eliminado VARCHAR,"
				+ "user_add VARCHAR,"
				+ "user_upd VARCHAR"
				+ ");";
		
		db.execSQL(sql);
	}
	
	public Cursor getRegistros(){
		createTable();
		
		sql = "SELECT "
				+ " sin_clientes_telefonos.id_cliente,"
				+ " telefonos.id_back, "
				+ " telefonos.telefono,"
				+ " telefonos.cod_area,"
				+ " telefonos.fax, "
				+ " tipos.tipo "
			+ " FROM "
				+ " `sin_clientes_telefonos` "
			+ " INNER JOIN "
				+ " telefonos ON(sin_clientes_telefonos.id_telefono = telefonos.id_back) "
			+ " INNER JOIN "
				+ " tipos ON(telefonos.id_tipo = tipos.id_back)";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	
	public Cursor getTelefonosCliente(String id){
		sql = "SELECT "
				+ " sin_clientes_telefonos.id_cliente,"
				+ " telefonos.id_back,"
				+ " telefonos.telefono,"
				+ " telefonos.cod_area,"
				+ " telefonos.fax, "
				+ " tipos.tipo "
			+ " FROM "
				+ " `sin_clientes_telefonos` "
			+ " INNER JOIN "
				+ " telefonos ON(sin_clientes_telefonos.id_telefono = telefonos.id_back) "
			+ " INNER JOIN "
				+ " tipos ON(telefonos.id_tipo = tipos.id_back)"
			+ " WHERE "
				+ " sin_clientes_telefonos.id_cliente = '"+id+"' ";
			
		Log.e("Consulta  ", sql);
		
		c = db.rawQuery(sql, null);
		
		
		return c;
	}
	
	
	public void insert(
			String id_back,
			String id_tipo,
			String telefono,
			String cod_area,
			String fax,
			String date_add,
			String date_upd,
			String eliminado,
			String user_add,
			String user_upd){
		sql = "INSERT INTO `telefonos` ("
				+ "`id_back`, "
				+ "`id_tipo`, "
				+ "`telefono`, "
				+ "`cod_area`, "
				+ "`fax`, "
				+ "`date_add`, "
				+ "`date_upd`, "
				+ "`eliminado`, "
				+ "`user_add`, "
				+ "`user_upd`"
			+ ") VALUES ("
				+ "'"+id_back+"', "
				+ "'"+id_tipo+"', "
				+ "'"+telefono+"', "
				+ "'"+cod_area+"', "
				+ "'"+fax+"', "
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
			String id_telefono,
			String date_add,
			String date_upd,
			String eliminado,
			String  user_add,
			String user_upd){
		sql = "INSERT INTO `sin_clientes_telefonos` ("
				+ "`id_back`, "
				+ "`id_cliente`, "
				+ "`id_telefono`, "
				+ "`date_add`, "
				+ "`date_upd`, "
				+ "`eliminado`, "
				+ "`user_add`, "
				+ "`user_upd`"
			+ ") VALUES ("
				+ "'"+id_back+"', "
				+ "'"+id_cliente+"', "
				+ "'"+id_telefono+"', "
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
		sql = "DELETE FROM `telefonos`";
		db.execSQL(sql);
	}
	
	public void truncateSin(){
		sql = "DELETE FROM `sin_clientes_telefonos`";
		db.execSQL(sql);
	}
	
	
	public Cursor getID(String telefono){
		sql = "SELECT "
				+ "id_back"
				+ " FROM telefonos"
				+ " WHERE telefono = '"+telefono+"'";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	
	public void edit(
			String id_back,
			String telefono,
			String cod_area,
			String id_tipo
		){
		
		sql = "UPDATE `telefonos`"
			+ " SET "
				+ " modificado 		= '1',"
				+ " telefono 		= '"+telefono+"',"
				+ " cod_area		= '"+cod_area+"',"
				+ " id_tipo 		= '"+id_tipo+"'"
			+ " WHERE "
				+ " id_back = '"+id_back+"'";
		
		db.execSQL(sql);
	}
	
	
	public Cursor getNuevos(){
		sql = "SELECT "				
				+ "id_back, "
				+ "telefono, "
				+ "cod_area, "
				+ "id_tipo "
			+ " FROM telefonos"
			+ " WHERE modificado = '1'";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}

}

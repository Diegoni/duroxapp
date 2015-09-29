package com.durox.app.Models;


import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Mails_clientes_model extends Activity{
	
	SQLiteDatabase db;
	String sql;
	Cursor c;
	
	
	public Mails_clientes_model(SQLiteDatabase db) {
		this.db = db;
	}
	
	public void createTable(){
		sql = "CREATE TABLE IF NOT EXISTS `mails`("
				+ "id_mail INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "id_back VARCHAR,"
				+ "modificado VARCHAR,"
				+ "id_tipo VARCHAR,"
				+ "mail VARCHAR,"
				+ "date_add VARCHAR,"
				+ "date_upd VARCHAR,"
				+ "eliminado VARCHAR,"
				+ "user_add VARCHAR,"
				+ "user_upd VARCHAR"
				+ ");";
		
		db.execSQL(sql);
		
		
		sql = "CREATE TABLE IF NOT EXISTS `sin_clientes_mails`("
				+ "id_sin_cliente_mail INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "id_back VARCHAR,"
				+ "id_cliente VARCHAR,"
				+ "id_mail VARCHAR,"
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
				+ " sin_clientes_mails.id_cliente,"
				+ " mails.id_back, "
				+ " mails.mail,"
				+ " tipos.tipo "
			+ " FROM "
				+ " `sin_clientes_mails` "
			+ " INNER JOIN "
				+ " mails ON(sin_clientes_mails.id_mail = mails.id_back) "
			+ " INNER JOIN "
				+ " tipos ON(mails.id_tipo = tipos.id_back)";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	
	public Cursor getRegistro(String id){
		sql = "SELECT "
				+ " sin_clientes_mails.id_cliente,"
				+ " mails.id_back, "
				+ " mails.mail,"
				+ " tipos.tipo "
			+ " FROM "
				+ " `sin_clientes_mails` "
			+ " INNER JOIN "
				+ " mails ON(sin_clientes_mails.id_mail = mails.id_back) "
			+ " INNER JOIN "
				+ " tipos ON(mails.id_tipo = tipos.id_back)"
			+ " WHERE "
				+ " sin_clientes_mails.id_cliente = '"+id+"' ";
		
		Log.e("Consulta  ", sql);
		
		c = db.rawQuery(sql, null);
		
		Log.e("Consulta  ", ""+c.getCount());
		
		return c;
	}
	
	
	public void insert(
			String id_back,
			String id_tipo,
			String mail,
			String date_add,
			String date_upd,
			String eliminado,
			String user_add,
			String user_upd){
		sql = "INSERT INTO `mails` ("
				+ "`id_back`, "
				+ "`id_tipo`, "
				+ "`mail`, "
				+ "`date_add`, "
				+ "`date_upd`, "
				+ "`eliminado`, "
				+ "`user_add`, "
				+ "`user_upd`"
			+ ") VALUES ("
				+ "'"+id_back+"', "
				+ "'"+id_tipo+"', "
				+ "'"+mail+"', "
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
			String id_mail,
			String date_add,
			String date_upd,
			String eliminado,
			String  user_add,
			String user_upd){
		sql = "INSERT INTO `sin_clientes_mails` ("
				+ "`id_back`, "
				+ "`id_cliente`, "
				+ "`id_mail`, "
				+ "`date_add`, "
				+ "`date_upd`, "
				+ "`eliminado`, "
				+ "`user_add`, "
				+ "`user_upd`"
			+ ") VALUES ("
				+ "'"+id_back+"', "
				+ "'"+id_cliente+"', "
				+ "'"+id_mail+"', "
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
		sql = "DELETE FROM `mails`";
		db.execSQL(sql);
	}
	
	public void truncateSin(){
		sql = "DELETE FROM `sin_clientes_mails`";
		db.execSQL(sql);
	}
	
	
	public Cursor getID(String mail){
		sql = "SELECT "
				+ "id_back"
				+ " FROM mails"
				+ " WHERE mail = '"+mail+"'";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	public void edit(
			String id_back,
			String mail,
			String id_tipo
		){
		
		sql = "UPDATE `mails`"
			+ " SET "
				+ " modificado 		= '1',"
				+ " mail 			= '"+mail+"',"
				+ " id_tipo 		= '"+id_tipo+"'"
			+ " WHERE "
				+ " id_back = '"+id_back+"'";
		
		db.execSQL(sql);
	}

}

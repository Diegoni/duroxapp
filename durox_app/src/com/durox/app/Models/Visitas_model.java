package com.durox.app.Models;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Visitas_model extends Activity{
	
	SQLiteDatabase db;
	String sql;
	Cursor c;
	
	
	public Visitas_model(SQLiteDatabase db) {
		this.db = db;
	}
	
	public void createTable(){
		sql = "CREATE TABLE IF NOT EXISTS  `visitas`("
				+ "id_visita INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "id_back VARCHAR, "
				+ "id_vendedor VARCHAR, "
				+ "id_cliente VARCHAR, "
				+ "descripcion VARCHAR, "
				+ "id_epoca_visita VARCHAR, "
				+ "valoracion VARCHAR, "
				+ "fecha VARCHAR, "
				+ "id_origen VARCHAR, "
				+ "visto VARCHAR, "
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
		
		sql = "SELECT * FROM `visitas`";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	public Cursor getVisitas(){
		createTable();
		
		sql = "SELECT "
				+ "visitas.id_back, "
				+ "clientes.razon_social, "
				+ "epocas_visitas.epoca, "
				+ "visitas.fecha "
			+ " FROM "
				+ " `visitas` "
			+ " INNER JOIN "
				+ " clientes ON(visitas.id_cliente = clientes.id_back) "
			+ " INNER JOIN "
				+ " epocas_visitas ON(visitas.id_epoca_visita = epocas_visitas.id_back) "
			+ " ORDER BY "
				+ "	visitas.fecha DESC";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	
	public Cursor getNuevos(){
		sql = "SELECT * FROM `visitas` WHERE id_back = '0' ";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	
	public void insert(
				String id_visita,
				String id_vendedor,
				String id_cliente,
				String descripcion,
				String id_epoca_visita,
				String valoracion,
				String fecha,
				String id_origen,
				String visto,
				String date_add,
				String date_upd,
				String eliminado,
				String user_add,
				String user_upd
			){
		sql = "INSERT INTO `visitas` ("
				+ "`id_back`, "
				+ "`id_vendedor`, "
				+ "`id_cliente`, "
				+ "`descripcion`, "
				+ "`id_epoca_visita`, "
				+ "`valoracion`, "
				+ "`fecha`, "
				+ "`id_origen`, "
				+ "`visto`, "
				+ "`date_add`, "
				+ "`date_upd`, "
				+ "`eliminado`, "
				+ "`user_add`, "
				+ "`user_upd`"
			+ ") VALUES ("
				+ "'"+id_visita+"', "
				+ "'"+id_vendedor+"', "
				+ "'"+id_cliente+"', "
				+ "'"+descripcion+"', "
				+ "'"+id_epoca_visita+"', "
				+ "'"+valoracion+"', "
				+ "'"+fecha+"', "
				+ "'"+id_origen+"', "
				+ "'"+visto+"', "
				+ "'"+date_add+"', "
				+ "'"+date_upd+"', "
				+ "'"+eliminado+"', "
				+ "'"+user_add+"', "
				+ "'"+user_upd+"' "
			+ ");";
 		
		Log.e("Consulta ", sql);
		db.execSQL(sql);
 	}
	
	public void truncate(){
		sql = "DELETE FROM `visitas`";
		db.execSQL(sql);
	}

}

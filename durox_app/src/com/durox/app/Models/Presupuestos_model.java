package com.durox.app.Models;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Presupuestos_model extends Activity{
	
	SQLiteDatabase db;
	String sql;
	Cursor c;
	
	
	public Presupuestos_model(SQLiteDatabase db) {
		this.db = db;
	}
	
	public void createTable(){
		sql = "CREATE TABLE IF NOT EXISTS presupuestos("
				+ "id_linea_presupuesto INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "id_back VARCHAR, "
				+ "id_visita VARCHAR, "
				+ "id_cliente VARCHAR, "
				+ "id_vendedor VARCHAR, "
				+ "id_estado_presupuesto VARCHAR, "
				+ "total VARCHAR, "
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
		
		sql = "SELECT "
				+ " presupuestos.id_linea_presupuesto, "
				+ " presupuestos.id_visita, "
				+ " clientes.razon_social, "
				+ " presupuestos.total VARCHAR, "
				+ " presupuestos.fecha VARCHAR "
				+ " FROM presupuestos"
				+ " INNER JOIN clientes ON(presupuestos.id_cliente = clientes.id_back)";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	public Cursor getRegistro(String id){
		createTable();
		
		sql = "SELECT "
				+ " presupuestos.id_linea_presupuesto, "
				+ " presupuestos.id_visita, "
				+ " presupuestos.total VARCHAR, "
				+ " clientes.razon_social, "
				+ " clientes.nombre, "
				+ " clientes.apellido, "
				+ " clientes.cuit "
				+ " FROM presupuestos"
				+ " INNER JOIN clientes ON(presupuestos.id_cliente = clientes.id_back)"
				+ " WHERE id_linea_presupuesto = '"+id+"'";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	
	public Cursor insert(
			String id_back,
			String id_visita,
			String id_cliente,
			String id_vendedor,
			String id_estado_presupuesto,
			String total,
			String fecha,
			String id_origen,
			String visto,
			String date_add,
			String date_upd,
			String eliminado,
			String user_add,
			String user_upd
		){
		sql = "INSERT INTO `presupuestos` ("
				+ "`id_back`, "
				+ "`id_visita`, "
				+ "`id_cliente`, "
				+ "`id_vendedor`, "
				+ "`id_estado_presupuesto`, "
				+ "`total`, "
				+ "`fecha`, "
				+ "`id_origen`, "
				+ "`visto`, "
				+ "`date_add`, "
				+ "`date_upd`, "
				+ "`eliminado`, "
				+ "`user_add`, "
				+ "`user_upd`"
			+ " ) VALUES ( "
 			    + "'"+id_back+"', "
 			    + "'"+id_visita+"', "
 			    + "'"+id_cliente+"', "
 			    + "'"+id_vendedor+"', "
 			    + "'"+id_estado_presupuesto+"', "
 			    + "'"+total+"', "
 			    + "'"+fecha+"', "
 			    + "'"+id_origen+"', "
 			    + "'"+visto+"', "
 			    + "'"+date_add+"', "
 			    + "'"+date_upd+"', "
 			    + "'"+eliminado+"', "
 			    + "'"+user_add+"', "
 			    + "'"+user_upd+"' "
 			+ ");";
 			    
 		db.execSQL(sql);
 		
 		return c;
 	}
	
	
	public void truncate(){
		sql = "DELETE FROM `presupuestos`";
		db.execSQL(sql);
	}

}

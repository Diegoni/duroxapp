package com.durox.app.Models;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Presupuestos_model extends Activity{
	
	SQLiteDatabase db;
	String sql;
	Cursor c;
	
	
	public Presupuestos_model(SQLiteDatabase db) {
		this.db = db;
	}
	
	public void createTable(){
		sql = "CREATE TABLE IF NOT EXISTS presupuestos("
				+ "id_presupuesto INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "`id_back` VARCHAR, "
				+ "`id_visita` VARCHAR, "
				+ "`id_cliente` VARCHAR, "
				+ "`id_vendedor` VARCHAR, "
				+ "`id_estado_presupuesto` VARCHAR, "
				+ "`id_condicion_pago` VARCHAR, "
				+ "`id_tiempo_entrega` VARCHAR, "
				+ "`nota_publica` VARCHAR, "
				+ "`total` VARCHAR, "
				+ "`fecha` VARCHAR, "
				+ "`id_origen` VARCHAR, "
				+ "`aprobado_back` VARCHAR, "
				+ "`aprobado_front` VARCHAR, "
				+ "`visto_back` VARCHAR, "
				+ "`visto_front` VARCHAR, "
				+ "`date_add` VARCHAR, "
				+ "`date_upd` VARCHAR, "
				+ "`eliminado` VARCHAR, "
				+ "`user_add` VARCHAR, "
				+ "`user_upd` VARCHAR "	
				+ ");";
		
		Log.e("Consulta ", sql);
		
		db.execSQL(sql);
	}
	
	public Cursor getRegistros(CharSequence orden){
		createTable();
		
		if(orden.equals("") || orden.equals("Fecha")){
			orden = "presupuestos.fecha DESC";
		}else if(orden.equals("Razón Social")){
			orden = "clientes.razon_social";
		}else if(orden.equals("Estado")){
			orden = "estados_presupuestos.text";
		}else if(orden.equals("Total")){
			orden = "presupuestos.total DESC";
		}else if(orden.equals("ID")){
			orden = "presupuestos.id_presupuesto DESC";
		}
		
		sql = "SELECT "
				+ " presupuestos.id_back, "
				+ " presupuestos.id_visita, "
				+ " clientes.razon_social, "
				+ " presupuestos.total, "
				+ " presupuestos.fecha, "
				+ " presupuestos.id_presupuesto, "
				+ " estados_presupuestos.text "
			+ " FROM "
				+ " presupuestos"
			+ " INNER JOIN "
				+ " clientes ON(presupuestos.id_cliente = clientes.id_back)"
			+ " INNER JOIN "
				+ " estados_presupuestos ON(presupuestos.id_estado_presupuesto = estados_presupuestos.id_back)"
			+ " ORDER BY "
				+ "	"+orden+" ";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	public Cursor getRegistro(String id){
		createTable();
		
		sql = "SELECT "
				+ " presupuestos.id_back, "
				+ " presupuestos.id_visita, "
				+ " presupuestos.total VARCHAR, "
				+ " clientes.razon_social, "
				+ " clientes.nombre, "
				+ " clientes.apellido, "
				+ " clientes.cuit, "
				+ " clientes.nombre_fantasia, "
				+ " presupuestos.fecha "
				+ " FROM presupuestos"
				+ " INNER JOIN clientes ON(presupuestos.id_cliente = clientes.id_back)"
				+ " WHERE presupuestos.id_back = '"+id+"'";
		
		c = db.rawQuery(sql, null);
		
		Log.e("Consulta ", sql);
		
		return c;
	}
	
	
	public Cursor getIDRegistro(String id){
		createTable();
		
		sql = "SELECT "
				+ " presupuestos.id_back, "
				+ " presupuestos.id_visita, "
				+ " presupuestos.total VARCHAR, "
				+ " clientes.razon_social, "
				+ " clientes.nombre, "
				+ " clientes.apellido, "
				+ " clientes.cuit, "
				+ " clientes.nombre_fantasia, "
				+ " presupuestos.fecha "
				+ " FROM presupuestos"
				+ " INNER JOIN clientes ON(presupuestos.id_cliente = clientes.id_back)"
				+ " WHERE presupuestos.id_presupuesto = '"+id+"'";
		
		c = db.rawQuery(sql, null);
		
		Log.e("Consulta ", sql);
		
		return c;
	}
	
	
	public void aprobar(String id){
		createTable();
		
		sql = "UPDATE "
				+ "	`presupuestos` "
			+ " SET "
				+ " `aprobado_front` = '1', "
				+ " `id_estado_presupuesto` = '5' "
			+ " WHERE "
				+ " `presupuestos`.`id_presupuesto` = '"+id+"' ";
		
		Log.e("Consulta ", sql);
		
		db.execSQL(sql);
	}
	
	
	public Cursor insert(
			String id_back,
			String id_visita,
			String id_cliente,
			String id_vendedor,
			String id_estado_presupuesto,
			String id_condicion_pago,
			String id_tiempo_entrega,
			String nota_publica,
			String total,
			String fecha,
			String id_origen,
			String aprobado_back,
			String aprobado_front,
			String visto_back,
			String visto_front,
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
				+ "`id_condicion_pago`, "
				+ "`id_tiempo_entrega`, "
				+ "`nota_publica`, "
				+ "`total`, "
				+ "`fecha`, "
				+ "`id_origen`, "
				+ "`aprobado_back`, "
				+ "`aprobado_front`, "
				+ "`visto_back`, "
				+ "`visto_front`, "
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
 			    + "'"+id_condicion_pago+"', "
 				+ "'"+id_tiempo_entrega+"', "
 				+ "'"+nota_publica+"', "
 			    + "'"+total+"', "
 			    + "'"+fecha+"', "
 			    + "'"+id_origen+"', "
 			    + "'"+aprobado_back+"', "
 			    + "'"+aprobado_front+"', "
 			    + "'"+visto_back+"', "
 			    + "'"+visto_front+"', "
 			    + "'"+date_add+"', "
 			    + "'"+date_upd+"', "
 			    + "'"+eliminado+"', "
 			    + "'"+user_add+"', "
 			    + "'"+user_upd+"' "
 			+ ");";
 			    
 		db.execSQL(sql);
 		
 		Log.e("Consulta ", sql);
 		
 		return c;
 	}
	
	public int lastInsert() {
	    final String MY_QUERY = "SELECT last_insert_rowid()";
	    Cursor cur = db.rawQuery(MY_QUERY, null);
	    cur.moveToFirst();
	    int ID = cur.getInt(0);
	    cur.close();
	    return ID;
	}
	
	
	public void truncate(){
		sql = "DELETE FROM `presupuestos` WHERE aprobado_front = '1' AND visto_front = '1'";
		db.execSQL(sql);
		
		Log.e("Consulta ", sql);
		
		sql = "DELETE FROM `presupuestos` WHERE visto_front = '0'"; 
		db.execSQL(sql);
		
		Log.e("Consulta ", sql);
	}
	
	
	public Cursor getNuevos(){
		createTable();
		
		sql = "SELECT "
				+ "	* FROM "
				+ " `presupuestos` "
				+ " WHERE id_back = '0' AND "
				+ " aprobado_front = '1' ";
		
		c = db.rawQuery(sql, null);
		
		Log.e("Consulta ", sql);
		
		return c;
	}
	
	
	
	public Cursor getLastInsert(){
		sql = "SELECT MAX(id_presupuesto) FROM presupuestos";
		
		c = db.rawQuery(sql, null);
		
		Log.e("Consulta ", sql);
		
		return c;
	}
	
	
	public void update(String total, String id_presupuesto){
		sql = "UPDATE presupuestos"
				+ " SET total = '"+total+"' "
				+ " WHERE id_presupuesto = '"+id_presupuesto+"'";
		
		Log.e("Consulta ", sql);
		
		db.execSQL(sql);
	}

}

package com.durox.app.Models;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Alarmas_model extends Activity{
	
	SQLiteDatabase db;
	String sql;
	Cursor c;
	
	
	public Alarmas_model(SQLiteDatabase db) {
		this.db = db;
	}
	
	public void createTable(){
		sql = "CREATE TABLE IF NOT EXISTS `alarmas`("
				+ "`id_alarma` INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "`id_back` VARCHAR,"
				+ "`id_tipo_alarma` VARCHAR,"
				+ "`mensaje` VARCHAR,"
				+ "`id_creador` VARCHAR,"
				+ "`id_origen` VARCHAR,"
				+ "`visto_back` VARCHAR,"
				+ "`visto_front` VARCHAR,"
				+ "`id_front` VARCHAR,"
				+ "`date_add` VARCHAR,"
				+ "`date_upd` VARCHAR,"
				+ "`eliminado` VARCHAR,"
				+ "`user_add` VARCHAR,"
				+ "`user_upd` VARCHAR"
				+ ");";
		
		db.execSQL(sql);
		
		// TIPOS DE ALARMA
		
		sql = "CREATE TABLE IF NOT EXISTS `tipos_alarmas`("
				+ "`id_tipo_alarma` INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "`id_back` VARCHAR,"
				+ "`nombre` VARCHAR,"
				+ "`tipo_alarma` VARCHAR,"
				+ "`color` VARCHAR,"
				+ "`date_add` VARCHAR,"
				+ "`date_upd` VARCHAR,"
				+ "`eliminado` VARCHAR,"
				+ "`user_add` VARCHAR,"
				+ "`user_upd` VARCHAR"
				+ ");";
		
		db.execSQL(sql);
		
		// CLIENTES
		
		sql = "CREATE TABLE IF NOT EXISTS `sin_alarmas_clientes`("
				+ "`id_sin_alarma_cliente` INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "`id_back` VARCHAR,"
				+ "`id_alarma` VARCHAR,"
				+ "`id_front_alarma` VARCHAR,"
				+ "`id_cliente` VARCHAR,"
				+ "`id_front_tabla` VARCHAR,"
				+ "`date_add` VARCHAR,"
				+ "`date_upd` VARCHAR,"
				+ "`eliminado` VARCHAR,"
				+ "`user_add` VARCHAR,"
				+ "`user_upd` VARCHAR"
				+ ");";
		
		db.execSQL(sql);

		// PEDIDOS
		
		sql = "CREATE TABLE IF NOT EXISTS `sin_alarmas_pedidos`("
				+ "`id_sin_alarma_pedido` INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "`id_back` VARCHAR,"
				+ "`id_alarma` VARCHAR,"
				+ "`id_front_alarma` VARCHAR,"
				+ "`id_pedido` VARCHAR,"
				+ "`id_front_tabla` VARCHAR,"
				+ "`date_add` VARCHAR,"
				+ "`date_upd` VARCHAR,"
				+ "`eliminado` VARCHAR,"
				+ "`user_add` VARCHAR,"
				+ "`user_upd` VARCHAR"
				+ ");";
				
		db.execSQL(sql);
		
		// PRODUCTOS
		
		sql = "CREATE TABLE IF NOT EXISTS `sin_alarmas_productos`("
				+ "`id_sin_alarma_producto` INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "`id_back` VARCHAR,"
				+ "`id_alarma` VARCHAR,"
				+ "`id_front_alarma` VARCHAR,"
				+ "`id_producto` VARCHAR,"
				+ "`id_front_tabla` VARCHAR,"
				+ "`date_add` VARCHAR,"
				+ "`date_upd` VARCHAR,"
				+ "`eliminado` VARCHAR,"
				+ "`user_add` VARCHAR,"
				+ "`user_upd` VARCHAR"
				+ ");";
				
		db.execSQL(sql);	
		
		// PRESUPUESTOS
		
		sql = "CREATE TABLE IF NOT EXISTS `sin_alarmas_presupuestos`("
				+ "`id_sin_alarma_presupuesto` INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "`id_back` VARCHAR,"
				+ "`id_alarma` VARCHAR,"
				+ "`id_front_alarma` VARCHAR,"
				+ "`id_presupuesto` VARCHAR,"
				+ "`id_front_tabla` VARCHAR,"
				+ "`date_add` VARCHAR,"
				+ "`date_upd` VARCHAR,"
				+ "`eliminado` VARCHAR,"
				+ "`user_add` VARCHAR,"
				+ "`user_upd` VARCHAR"
				+ ");";
		
		db.execSQL(sql);
		
		// VISITAS
		
		sql = "CREATE TABLE IF NOT EXISTS `sin_alarmas_visitas`("
				+ "`id_sin_alarma_visita` INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "`id_back` VARCHAR,"
				+ "`id_alarma` VARCHAR,"
				+ "`id_front_alarma` VARCHAR,"
				+ "`id_visita` VARCHAR,"
				+ "`id_front_tabla` VARCHAR,"
				+ "`date_add` VARCHAR,"
				+ "`date_upd` VARCHAR,"
				+ "`eliminado` VARCHAR,"
				+ "`user_add` VARCHAR,"
				+ "`user_upd` VARCHAR"
				+ ");";
				
		db.execSQL(sql);
		
		// VENDEDORES
		
		sql = "CREATE TABLE IF NOT EXISTS `sin_alarmas_vendedores`("
				+ "`id_sin_alarma_vendedor` INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "`id_back` VARCHAR,"
				+ "`id_alarma` VARCHAR,"
				+ "`id_front_alarma` VARCHAR,"
				+ "`id_vendedor` VARCHAR,"
				+ "`id_front_tabla` VARCHAR,"
				+ "`date_add` VARCHAR,"
				+ "`date_upd` VARCHAR,"
				+ "`eliminado` VARCHAR,"
				+ "`user_add` VARCHAR,"
				+ "`user_upd` VARCHAR"
				+ ");";
				
		db.execSQL(sql);
	}
	
	public Cursor getRegistros(String alarma){
		createTable();
				
		String tabla = "";
		String id_tabla = "";
		
		tabla = this.getTabla(alarma);
		id_tabla = this.getIdTabla(alarma);
		
		sql = "SELECT "
				+ "alarmas.mensaje, "
				+ "alarmas.id_origen, "
				+ "alarmas.visto_front, "
				+ ""+tabla+"."+id_tabla+", "
				+ "tipos_alarmas.nombre "
			+ "FROM "
				+ "alarmas "
			+ "INNER JOIN "
				+ "`"+tabla+"` ON(alarmas.id_back = "+tabla+".id_alarma) "
			+ "INNER JOIN "
				+ "tipos_alarmas ON(tipos_alarmas.id_back = alarmas.id_tipo_alarma)";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	
	public Cursor getAlarmas(String id, String alarma){
		
		createTable();
		
		String tabla = "";
		String id_tabla = "";
		
		tabla = this.getTabla(alarma);
		id_tabla = this.getIdTabla(alarma);
		
		sql = "SELECT "
				+ "alarmas.mensaje, "
				+ "alarmas.id_origen, "
				+ "alarmas.visto_front, "
				+ ""+tabla+"."+id_tabla+", "
				+ "tipos_alarmas.nombre "
			+ "FROM "
				+ "alarmas "
			+ "INNER JOIN "
				+ "`"+tabla+"` ON(alarmas.id_back = "+tabla+".id_alarma) "
			+ "INNER JOIN "
				+ "tipos_alarmas ON(tipos_alarmas.id_back = alarmas.id_tipo_alarma)"
			+ "WHERE "
			+ ""+tabla+"."+id_tabla+" = "+id+"";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	
	public void insert(
			String id_back,
			String id_tipo_alarma,
			String mensaje,
			String id_creador,
			String id_origen,
			String visto_back,
			String visto_front,
			String id_front,
			String date_add,
			String date_upd,
			String eliminado,
			String user_add,
			String user_upd
			){
		sql = "INSERT INTO `alarmas` ("
				+ "`id_back`, "
				+ "`id_tipo_alarma`, "
				+ "`mensaje`, "
				+ "`id_creador`, "
				+ "`id_origen`, "
				+ "`visto_back`, "
				+ "`visto_front`, "
				+ "`id_front`, "
				+ "`date_add`, "
				+ "`date_upd`, "
				+ "`eliminado`, "
				+ "`user_add`, "
				+ "`user_upd`"
			+ ") VALUES ("
				+ "'"+id_back+"', "
				+ "'"+id_tipo_alarma+"', "
				+ "'"+mensaje+"', "
				+ "'"+id_creador+"', "
				+ "'"+id_origen+"', "
				+ "'"+visto_back+"', "
				+ "'"+visto_front+"', "
				+ "'"+id_front+"', "
				+ "'"+date_add+"', "
				+ "'"+date_upd+"', "
				+ "'"+eliminado+"', "
				+ "'"+user_add+"', "
				+ "'"+user_upd+"' "
 			    + ");";
		
		Log.e("Consulta  ", sql);
 			    
		db.execSQL(sql);
 	}
	
	
	public int lastInsert() {
	    final String MY_QUERY = "SELECT last_insert_rowid()";
	    Cursor cur = db.rawQuery(MY_QUERY, null);
	    cur.moveToFirst();
	    int ID = cur.getInt(0);
	    cur.close();
	    return ID;
	}
	
	
	public void insertTipos(
			String id_back,
			String nombre,
			String tipo_alarma,
			String color,
			String date_add,
			String date_upd,
			String eliminado,
			String user_add,
			String user_upd
			){
		sql = "INSERT INTO `tipos_alarmas` ("
				+ "`id_back`, "
				+ "`nombre`, "
				+ "`tipo_alarma`, "
				+ "`color`, "
				+ "`date_add`, "
				+ "`date_upd`, "
				+ "`eliminado`, "
				+ "`user_add`, "
				+ "`user_upd`"
			+ ") VALUES ("
				+ "'"+id_back+"', "
				+ "'"+nombre+"', "
				+ "'"+tipo_alarma+"', "
				+ "'"+color+"', "
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
			String id_alarma,
			int idAlarma,
			String id_tabla,
			int idPresupuesto,
			String date_add,
			String date_upd,
			String eliminado,
			String user_add,
			String user_upd,
			String alarma){
		
		String tabla;
		String id;
		
		Log.e("Paso  ", alarma);
		
		tabla = this.getTabla(alarma);
		id = this.getIdTabla(alarma);
		
		Log.e("Paso  ", tabla);
		Log.e("Paso  ", id);
		
		sql = "INSERT INTO `"+tabla+"` ("
				+ "`id_back`, "
				+ "`id_alarma`, "
				+ "`id_front_alarma`,"
				+ "`"+id+"`, "
				+ "`id_front_tabla`,"
				+ "`date_add`, "
				+ "`date_upd`, "
				+ "`eliminado`, "
				+ "`user_add`, "
				+ "`user_upd`"
			+ ") VALUES ("
				+ "'"+id_back+"', "
				+ "'"+id_alarma+"', "
				+ "'"+idAlarma+"', "
				+ "'"+id_tabla+"', "
				+ "'"+idPresupuesto+"', "
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
		createTable();
		
		sql = "DELETE FROM `alarmas`";
		db.execSQL(sql);
		
		sql = "DELETE FROM `tipos_alarmas`";
		db.execSQL(sql);
	}
	
	public void truncateSin(String alarma){
		String tabla;
		tabla = this.getTabla(alarma);
		
		sql = "DELETE FROM `"+tabla+"`";
		db.execSQL(sql);
	}
	
	
	public Cursor getID(String alarma){
		sql = "SELECT "
				+ "id_back"
				+ " FROM alarmas"
				+ " WHERE alarma = '"+alarma+"'";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	
	public String getTabla(String alarma){
		String tabla = "";
		if(alarma.equals("clientes") || alarma.equals("sin_alarmas_clientes")){
			tabla		= "sin_alarmas_clientes";
		}else if(alarma.equals("pedidos") || alarma.equals("sin_alarmas_pedidos")){
			tabla		= "sin_alarmas_pedidos";
		}else if(alarma.equals("productos") || alarma.equals("sin_alarmas_productos")){
			tabla		= "sin_alarmas_productos";
		}else if(alarma.equals("presupuestos") || alarma.equals("sin_alarmas_presupuestos")){
			tabla		= "sin_alarmas_presupuestos";
		}else if(alarma.equals("visitas") || alarma.equals("sin_alarmas_visitas")){
			tabla		= "sin_alarmas_visitas";
		}else if(alarma.equals("vendedores") || alarma.equals("sin_alarmas_vendedores")){
			tabla		= "sin_alarmas_vendedores";
		}
		
		return tabla;
	}
	
	
	public String getIdTabla(String alarma){
		String id_tabla = "";
		
		if(alarma.equals("clientes") || alarma.equals("sin_alarmas_clientes")){
			id_tabla	= "id_cliente";
		}else if(alarma.equals("pedidos") || alarma.equals("sin_alarmas_pedidos")){
			id_tabla	= "id_pedido";
		}else if(alarma.equals("productos") || alarma.equals("sin_alarmas_productos")){
			id_tabla	= "id_producto";
		}else if(alarma.equals("presupuestos") || alarma.equals("sin_alarmas_presupuestos")){
			id_tabla	= "id_presupuesto";			
		}else if(alarma.equals("visitas") || alarma.equals("sin_alarmas_visitas")){
			id_tabla	= "id_visita";		
		}else if(alarma.equals("vendedores") || alarma.equals("sin_alarmas_vendedores")){
			id_tabla	= "id_vendedor";			
		}
		
		return id_tabla;
	}
	
	
	public Cursor getNuevos(){
		createTable();
		
		sql = "SELECT "
				+ "	* "
			+ "FROM "
				+ " `alarmas` "
			+ " WHERE "
				+ "id_back = '0' ";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	
	public Cursor getNuevosSin(String alarma){
		String tabla = this.getTabla(alarma);
		
		sql = "SELECT "
				+ "	* "
			+ "FROM "
				+ " `"+tabla+"` "
			+ " WHERE "
				+ "id_back = '0' ";
		
		c = db.rawQuery(sql, null);
		
		return c;
	}

}

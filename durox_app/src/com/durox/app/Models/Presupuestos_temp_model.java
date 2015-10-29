package com.durox.app.Models;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Presupuestos_temp_model extends Activity{
	
	SQLiteDatabase db;
	String sql;
	Cursor c;
	
	
	public Presupuestos_temp_model(SQLiteDatabase db) {
		this.db = db;
	}
	
	public void createTable(){
		sql = "CREATE TABLE IF NOT EXISTS temp_presupuestos("
				+ "`id_presupuesto` INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "`id_condicion_pago` VARCHAR, "
				+ "`id_modo_pago` VARCHAR, "
				+ "`id_tiempo_entrega` VARCHAR, "
				+ "`nota_publica` VARCHAR, "
				+ "`nota_privada` VARCHAR, "
				+ "`default` VARCHAR "
				+ ");";
		
		Log.e("Consulta ", sql);
		
		db.execSQL(sql);
		
		sql = "SELECT * FROM temp_presupuestos";
		c = db.rawQuery(sql, null);
				
		if(c.getCount() == 0){
			String condicion = "";
			String modo = "";
			String tiempo = "";
			
			sql = "SELECT condicion_pago FROM condiciones_pago LIMIT 0,1";
			Cursor cCondicion = db.rawQuery(sql, null);
			if(cCondicion.getCount() > 0){
				while(cCondicion.moveToNext()){
					condicion = cCondicion.getString(0);
				}
			}
			
			sql = "SELECT modo_pago FROM modos_pago LIMIT 0,1";
			Cursor cModos = db.rawQuery(sql, null);
			if(cModos.getCount() > 0){
				while(cModos.moveToNext()){
					modo = cModos.getString(0);
				}
			}
			
			sql = "SELECT tiempo_entrega FROM tiempos_entrega LIMIT 0,1";
			Cursor cTiempos = db.rawQuery(sql, null);
			if(cTiempos.getCount() > 0){
				while(cTiempos.moveToNext()){
					tiempo = cTiempos.getString(0);
				}
			}
			
			sql = "INSERT INTO `temp_presupuestos` ("
					+ "`id_condicion_pago` , "
					+ "`id_modo_pago` , "
					+ "`id_tiempo_entrega` , "
					+ "`nota_publica` , "
					+ "`nota_privada` , "
					+ "`default`  "
				+ " ) VALUES ( "
	 			    + "'"+condicion+"', "
	 			    + "'"+modo+"', "
	 			   	+ "'"+tiempo+"', "
	 			    + "'', "
	 			    + "'', "
	 			   + "'1' "
	 			+ ");";
			
			Log.e("Consulta ", sql);
			
			db.execSQL(sql);
		}
	}
	
	
	
	public Cursor getTemp(){
		sql = "SELECT "
				+ "`id_condicion_pago`, "
				+ "`id_modo_pago`, "
				+ "`id_tiempo_entrega`, "
				+ "`nota_publica`, "
				+ "`nota_privada` "
			+ " FROM "
				+ "`temp_presupuestos` "
			+ "WHERE `temp_presupuestos`.`default` = '0'";
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	
	public Cursor getDefault(){
		sql = "SELECT "
				+ "`id_condicion_pago`, "
				+ "`id_modo_pago`, "
				+ "`id_tiempo_entrega`, "
				+ "`nota_publica`, "
				+ "`nota_privada` "
			+ " FROM "
				+ "`temp_presupuestos` "
			+ "WHERE `temp_presupuestos`.`default` = '1'";
		c = db.rawQuery(sql, null);
		
		return c;
	}
	
	
	
	public String newOption(){
		sql = "SELECT * FROM `temp_presupuestos` WHERE `temp_presupuestos`.`default` = '1'";
		c = db.rawQuery(sql, null);
				
		if(c.getCount() > 0){
			while(c.moveToNext()){
				sql = "INSERT INTO `temp_presupuestos` ("
						+ "`id_condicion_pago` , "
						+ "`id_modo_pago` , "
						+ "`id_tiempo_entrega` , "
						+ "`nota_publica` , "
						+ "`nota_privada` , "
						+ "`default`  "
					+ " ) VALUES ( "
		 			    + "'"+c.getString(1)+"', "
		 			    + "'"+c.getString(2)+"', "
		 			    + "'"+c.getString(3)+"', "
		 			    + "'"+c.getString(4)+"', "
		 			    + "'"+c.getString(5)+"', "
		 			    + "'0' "
		 			+ ");";
			
				Log.e("Consulta ", sql);
			
				db.execSQL(sql);
			}
		}
		
		sql = "SELECT MAX(id_presupuesto) FROM temp_presupuestos";
		c = db.rawQuery(sql, null);
		String id = "";
		if(c.getCount() > 0){
			while(c.moveToNext()){
				id = c.getString(0);
			}
		}
		
		return id;
	}
	
	
	public void setOption(String campo, String dato, String id){
		sql = "UPDATE "
				+ "	`temp_presupuestos` "
			+ " SET "
				+ " `"+campo+"` = '"+dato+"' "
			+ " WHERE "
				+ " `temp_presupuestos`.`id_presupuesto` = '"+id+"' ";
		
		Log.e("Consulta ", sql);
		
		db.execSQL(sql);
	}
	
	
	public String getOption(String campo, String id){
		sql = "SELECT `"+campo+"` FROM temp_presupuestos"
				+ " WHERE "
				+ " `temp_presupuestos`.`id_presupuesto` = '"+id+"' ";
		c = db.rawQuery(sql, null);
		String dato = "";
		if(c.getCount() > 0){
			while(c.moveToNext()){
				dato = c.getString(0);
			}
		}
		
		return dato;
	}
	
	
	public void limpiarOption(){
		sql = "DELETE FROM `temp_presupuestos` WHERE `temp_presupuestos`.`default` = '0'";
		db.execSQL(sql);
		
		Log.e("Consulta ", sql);		
	}
}

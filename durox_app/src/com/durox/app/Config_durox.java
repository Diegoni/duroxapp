package com.durox.app;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Config_durox {
	private String database = "Durox_tres";
	
	private String real_ip;
	private String ip;
	private String documentos;
	private String fichaProductos;
	boolean actualizando = true;
	
	String sql;
	SQLiteDatabase db;
	Cursor c;
	
	public void createConfig(){
		sql = "CREATE TABLE IF NOT EXISTS `config`("
				+ "id_config INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "ip VARCHAR,"
				+ "documentos VARCHAR, "
				+ "fichaProductos VARCHAR, "
				+ "real_ip VARCHAR "
				+ ");";
		
		db.execSQL(sql);
		
		sql = "SELECT * FROM config";
					
		c = db.rawQuery(sql, null);
				
		if(c.getCount() == 0){
			/*
			ip =  "http://192.168.1.219/durox/index.php";
			documentos = "http://192.168.1.219/durox/documentos";
			fichaProductos = "http://192.168.1.219/durox/img/productos/documentos/";
			*/
			real_ip = "http://190.15.192.35:8090";	
			ip = "/durox/index.php";
			documentos = "/durox/documentos";
			fichaProductos = "/durox/img/productos/documentos/";


			sql = "INSERT INTO `config` ("
					+ "`ip`, "
					+ "`documentos`, "
					+ "`fichaProductos`, "
					+ "`real_ip` "
				+ ") VALUES ("
					+ "'"+ip+"', "
					+ "'"+documentos+"', "
					+ "'"+fichaProductos+"', "
					+ "'"+real_ip+"' "
					+ ");";
	 		db.execSQL(sql);
		}else{
			while(c.moveToNext()){
				ip = c.getString(1);
				documentos = c.getString(2);
				fichaProductos = c.getString(3);
				real_ip = c.getString(4);
    		}	
		}
	}
	
	public String getDatabase() {
		return this.database;
	}
	
	
	public String getIp(SQLiteDatabase db) {
		this.db = db;
		createConfig();
		
		String ip_return = this.real_ip+this.ip;
		return ip_return;
	}
	
	
	public void setIP(String ip, SQLiteDatabase db) {
		this.db = db;
		createConfig();
		
		sql = "UPDATE `config` SET ip = '"+ip+"'"; 
		Log.e("Consulta ", sql);
		
		db.execSQL(sql);
		
		this.ip = ip;
	}
	
	public String getRealIp(SQLiteDatabase db) {
		this.db = db;
		createConfig();
		return this.real_ip;
	}
	
	
	public void setRealIP(String real_ip, SQLiteDatabase db) {
		this.db = db;
		createConfig();
		
		sql = "UPDATE `config` SET real_ip = '"+real_ip+"'"; 
		Log.e("Consulta ", sql);
		
		db.execSQL(sql);
		this.real_ip = real_ip;
	}
	
	
	public String getFichaProductos(SQLiteDatabase db) {
		this.db = db;
		createConfig();
		
		String ip_return = this.real_ip+this.fichaProductos;
		return ip_return;
	}
	
	
	public void setFichaProductos(String ip, SQLiteDatabase db) {
		this.db = db;
		createConfig();
		
		sql = "UPDATE `config` SET fichaProductos = '"+ip+"'"; 
		Log.e("Consulta ", sql);
		
		db.execSQL(sql);
		
		this.ip = ip;
	}
	
	
	public String getDocumentos(SQLiteDatabase db) {
		this.db = db;
		createConfig();
		String ip_return = this.real_ip+this.documentos;
		return ip_return;
	}	
	
	
	public void setDocumentos(String documentos, SQLiteDatabase db) {
		this.db = db;
		createConfig();
		sql = "UPDATE `config` SET documentos = '"+documentos+"'"; 
		Log.e("Consulta ", sql);
		db.execSQL(sql);
		
		this.documentos = documentos;
	}
	
	
	public String msjOkInsert() {
		String msj = "El registro se inserto ok";
		return msj;
	}
	
	
	public String msjOkUpdate() {
		String msj = "El registro se modifico ok";
		return msj;
	}
	
	
	public String msjRegistrosActualizados(String cantidad){
		String msj = "Registros actualizados "+cantidad;
		return msj;
	}
	
	
	public String msjError(String error){
		String msj = "Error "+error;
		return msj;
	}
	
	
	public String msjActualizandoRegistros(){
		String msj = "Actualizando registros ";
		return msj;
	}
	
	
	public String msjNoRegistro(String registro){
		String msj = "El "+registro+" no se encuentra en la base de datos";
		return msj;
	}
	
	
	public String msjNoRegistros(String registro){
		String msj = "No hay "+registro+" cargados";
		return msj;
	}
	
	
	public String msjOkDelete(){
		String msj = "Registros eliminados";
		return msj;
	}
	
	
	public String msjOkLogin(String user){
		String msj = "Bienvenido "+user;
		return msj;
	}
	
	
	public String msjDuplicado(){
		String msj = "El Registros ya existe";
		return msj;
	}
	
	
	public String getMoneda(){
		String msj = "$";
		return msj;
	}
	
}
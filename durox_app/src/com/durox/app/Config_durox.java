package com.durox.app;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Config_durox {
	private String database = "Durox_tres";
	
	private String ip;
	private String documentos;
	boolean actualizando = true;
	
	String sql;
	SQLiteDatabase db;
	Cursor c;
	
	public void createConfig(){
		sql = "CREATE TABLE IF NOT EXISTS `config`("
				+ "id_config INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "ip VARCHAR,"
				+ "documentos VARCHAR"
				+ ");";
		
		db.execSQL(sql);
		
		sql = "SELECT * FROM config";
					
		c = db.rawQuery(sql, null);
				
		if(c.getCount() == 0){
			ip =  "http://192.168.1.207/durox/index.php";
			documentos = "http://192.168.1.207/durox/documentos";

			sql = "INSERT INTO `config` ("
					+ "`ip`, "
					+ "`documentos` "
				+ ") VALUES ("
					+ "'"+ip+"', "
					+ "'"+documentos+"' "
					+ ");";
	 		db.execSQL(sql);
		}else{
			while(c.moveToNext()){
				ip = c.getString(1);
				documentos = c.getString(2);
    		}	
		}
	}
	
	public String getDatabase() {
		return this.database;
	}
	
	public String getIp(SQLiteDatabase db) {
		this.db = db;
		createConfig();
		return this.ip;
	}
	
	public void setIP(String ip, SQLiteDatabase db) {
		this.db = db;
		createConfig();
		
		sql = "UPDATE `config` SET ip = '"+ip+"'"; 
		Log.e("Consulta ", sql);
		
		db.execSQL(sql);
		
		this.ip = ip;
	}
	
	public String getDocumentos(SQLiteDatabase db) {
		this.db = db;
		createConfig();
		return this.documentos;
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
	
}
package com.durox.app;

import com.durox.app.Documentos.Documentos_Main;
import com.durox.app.Presupuestos.Presupuestos_Main;
import com.durox.app.Visitas.Visitas_Main;
import com.example.durox_app.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class Config_durox {
	private String database = "Durox_app";
	private String ip = "http://192.168.1.207/durox/index.php";
	boolean actualizando = true;
	
	public void onCreate(Bundle savedInstanceState) {
		 
	}
	
	public String getDatabase() {
		return this.database;
	}
	
	public String getIp() {
		return this.ip;
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
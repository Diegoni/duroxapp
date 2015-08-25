package com.durox.app.Presupuestos;

import android.util.Log;

public class Presupuesto {
	private String id_back;
	private String nombre;
	private String direccion;
	private int imagen;
	private String id_presupuesto;
	
	public Presupuesto(
			String id_back,
			String nombre, 
			String direccion,
			int imagen,
			String id_presupuesto) {
		this.id_back = id_back;
		this.nombre = nombre;
		this.direccion = direccion;
		this.imagen = imagen;
		this.id_presupuesto = id_presupuesto;
		
		Log.e("Paso creacion del objeto ", id_back+nombre+direccion+id_presupuesto);
	}
	
	public String getID() 
	{
		return this.id_back;
	}


	public String getNombre() 
	{
		return this.nombre;
	}

	public String getDireccion() 
	{
		return this.direccion;
	}

	public int getImagen() 
	{
		return this.imagen;
	}
	
	public String getIDPresupuesto() 
	{
		return this.id_presupuesto;
	}

}

package com.durox.app.Presupuestos;

public class Presupuesto {
	private String id_back;
	private String nombre;
	private String direccion;
	private int imagen;
	
	public Presupuesto(
			String id_back,
			String nombre, 
			String direccion,
			int imagen) {
		this.id_back = id_back;
		this.nombre = nombre;
		this.direccion = direccion;
		this.imagen = imagen;
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
}

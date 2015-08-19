package com.durox.app.Productos;

public class Productos {
	private String id_back;
	private String nombre;
	private String detalle;
	private int imagen;

	public Productos(	
		String id_back,	
		String nombre, 
		String detalle,
		int imagen) 
	{
		this.id_back = id_back;
		this.nombre = nombre;
		this.detalle = detalle;
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

	public String getDetalle() 
	{
		return this.detalle;
	}

	public int getImagen() 
	{
		return this.imagen;
	}
}

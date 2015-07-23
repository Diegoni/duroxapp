package com.androidbegin.filterlistviewimg;

public class Productos {
	private String nombre;
	private String detalle;
	private int imagen;

	public Productos(	
		String nombre, 
		String detalle,
		int imagen) 
	{
		this.nombre = nombre;
		this.detalle = detalle;
		this.imagen = imagen;
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

package com.androidbegin.filterlistviewimg;

public class Clientes {
	private String nombre;
	private String direccion;
	private int imagen;
	
	public Clientes(	
			String nombre, 
			String direccion,
			int imagen) {
		this.nombre = nombre;
		this.direccion = direccion;
		this.imagen = imagen;
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

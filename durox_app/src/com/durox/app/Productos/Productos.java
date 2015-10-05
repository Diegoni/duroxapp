package com.durox.app.Productos;

public class Productos {
	private String id_back;
	private String nombre;
	private String precio;
	private int imagen;

	public Productos(	
		String id_back,	
		String nombre, 
		String precio,
		int imagen) 
	{
		this.id_back = id_back;
		this.nombre = nombre;
		this.precio = precio;
		this.imagen = imagen;
	}
	
	public String getID() {
		return this.id_back;
	}

	public String getNombre() {
		return this.nombre;
	}

	public String getPrecio() {
		return this.precio;
	}

	public int getImagen() {
		return this.imagen;
	}
}

package com.durox.app.Productos;

public class Productos {
	private String id_back;
	private String nombre;
	private String precio;
	private String moneda;
	private String codigo;
	private int imagen;

	public Productos(	
		String id_back,	
		String nombre, 
		String precio,
		String moneda,
		String codigo,
		int imagen) 
	{
		this.id_back = id_back;
		this.nombre = nombre;
		this.precio = precio;
		this.moneda = moneda;
		this.codigo = codigo;
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
	
	public String getMoneda() {
		return this.moneda;
	}
	
	public String getCodigo() {
		return this.codigo;
	}

	public int getImagen() {
		return this.imagen;
	}
}

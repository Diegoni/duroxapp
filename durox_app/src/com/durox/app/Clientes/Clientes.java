package com.durox.app.Clientes;

public class Clientes {
	private String id_back;
	private String razon_social;
	private String nombre;
	private int imagen;
	
	public Clientes(
			String id_back,
			String razon_social, 
			String nombre,
			int imagen) {
		this.id_back = id_back;
		this.razon_social = razon_social;
		this.nombre = nombre;
		this.imagen = imagen;
	}
	
	
	public String getID() {
		return this.id_back;
	}


	public String getRazonSocial() {
		return this.razon_social;
	}
	
	public String getNombre() {
		return this.nombre;
	}
	

	public int getImagen() {
		return this.imagen;
	}
}

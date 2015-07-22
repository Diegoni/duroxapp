package com.androidbegin.filterlistviewimg;

public class Clientes {
	private String nombre;
	private String direccion;
	private int imagen;

	//public WorldPopulation(String rank, String country, String population,
	public void cliente(
			String nombre, 
			String direccion,
			int imagen) {
		//this.rank = rank;
		this.nombre = nombre;
		this.direccion = direccion;
		this.imagen = imagen;
	}

	public String getNombre() {
		return this.nombre;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public int getImagen() {
		return this.imagen;
	}
}

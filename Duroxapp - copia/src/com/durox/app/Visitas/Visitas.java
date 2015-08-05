package com.durox.app.Visitas;

public class Visitas {
	private String nombre;
	private String epoca;
	private String fecha;
	
	public Visitas(	
			String nombre, 
			String epoca,
			String fecha) {
		this.nombre = nombre;
		this.epoca = epoca;
		this.fecha = fecha;
	}

	public String getNombre() 
	{
		return this.nombre;
	}

	public String getEpoca() 
	{
		return this.epoca;
	}

	public String getFecha() 
	{
		return this.fecha;
	}
}

package com.durox.app.Visitas;

public class Visitas {
	private String id_visita;
	private String nombre;
	private String epoca;
	private String fecha;
	private int imagen;
	
	public Visitas(	
			String id_visita,
			String nombre, 
			String epoca,
			String fecha,
			int imagen) {
		this.id_visita = id_visita;
		this.nombre = nombre;
		this.epoca = epoca;
		this.fecha = fecha;
		this.imagen = imagen;
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
	
	public int getImagen() 
	{
		return this.imagen;
	}
	
	public String getVisita() 
	{
		return this.id_visita;
	}
}
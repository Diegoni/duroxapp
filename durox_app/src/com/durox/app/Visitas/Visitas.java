package com.durox.app.Visitas;

public class Visitas {
	private String id_visita;
	private String nombre;
	private String epoca;
	private String fecha;
	private String comentario;
	private int imagen;
	private String id_front;
	
	public Visitas(	
			String id_visita,
			String nombre, 
			String epoca,
			String fecha,
			String comentario,
			int imagen,
			String id_front) {
		this.id_visita = id_visita;
		this.nombre = nombre;
		this.epoca = epoca;
		this.fecha = fecha;
		this.comentario = comentario;
		this.imagen = imagen;
		this.id_front = id_front;
	}

	public String getNombre() {
		return this.nombre;
	}

	public String getEpoca() {
		return this.epoca;
	}

	public String getFecha() {
		return this.fecha;
	}
	
	public String getComentario() {
		return this.comentario;
	}
	
	public int getImagen() {
		return this.imagen;
	}
	
	public String getVisita() {
		return this.id_visita;
	}
	
	public String getIdFront() {
		return this.id_front;
	}
}
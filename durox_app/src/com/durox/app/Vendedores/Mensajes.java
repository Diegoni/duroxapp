package com.durox.app.Vendedores;

public class Mensajes {
	private String id_back;
	private String asunto;
	private String mensaje;
	private String date_add;
	private String id_origen;
	private int imagen;

	public Mensajes(	
		String id_back,	
		String asunto, 
		String mensaje,
		String date_add,
		String id_origen,
		int imagen) 
	{
		this.id_back = id_back;
		this.asunto = asunto;
		this.mensaje = mensaje;
		this.date_add = date_add;
		this.id_origen = id_origen;
		this.imagen = imagen;
	}
	
	public String getID() {
		return this.id_back;
	}

	public String getAsunto() {
		return this.asunto;
	}

	public String getMensaje() {
		return this.mensaje;
	}
	
	public String getDate_add() {
		return this.date_add;
	}
	
	public String getIdorigen() {
		return this.id_origen;
	}

	public int getImagen() {
		return this.imagen;
	}
}

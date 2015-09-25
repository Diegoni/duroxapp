package com.durox.app.Clientes;

public class Telefonos {
	private String id_back;
	private String telefono;
	private String cod_area;
	private String tipo;
	private String id_cliente;
	private int imagen;
	
	public Telefonos(
			String id_back,
			String telefono, 
			String cod_area,
			String tipo,
			String id_cliente,
			int imagen) {
		this.id_back = id_back;
		this.telefono = telefono;
		this.cod_area = cod_area;
		this.tipo = tipo;
		this.id_cliente = id_cliente;
		this.imagen = imagen;
	}
	
	public String getID() {
		return this.id_back;
	}


	public String getTelefono() {
		return this.telefono;
	}

	public String getCodArea() {
		return this.cod_area;
	}
	
	public String getTipo() {
		return this.tipo;
	}
	
	public String getIdCliente() {
		return this.id_cliente;
	}

	public int getImagen(){
		return this.imagen;
	}
}

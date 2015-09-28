package com.durox.app.Clientes;

public class Direcciones {
	private String id_back;
	private String direccion;
	private String id_departamento;
	private String tipo;
	private String id_cliente;
	private int imagen;
	
	public Direcciones(
			String id_back,
			String direccion, 
			String id_departamento,
			String tipo,
			String id_cliente,
			int imagen) {
		this.id_back = id_back;
		this.direccion = direccion;
		this.id_departamento = id_departamento;
		this.tipo = tipo;
		this.id_cliente = id_cliente;
		this.imagen = imagen;
	}
	
	public String getID() {
		return this.id_back;
	}


	public String getDireccion() {
		return this.direccion;
	}

	public String getIdDepartamento() {
		return this.id_departamento;
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

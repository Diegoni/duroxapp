package com.durox.app.Clientes;

public class Mails {
	private String id_back;
	private String mail;
	private String tipo;
	private String id_cliente;
	private int imagen;
	
	public Mails(
			String id_back,
			String mail, 
			String tipo,
			String id_cliente,
			int imagen) {
		this.id_back = id_back;
		this.mail = mail;
		this.tipo = tipo;
		this.id_cliente = id_cliente;
		this.imagen = imagen;
	}
	
	
	public String getID() {
		return this.id_back;
	}

	public String getMail() {
		return this.mail;
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

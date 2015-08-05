package com.durox.app.Presupuestos;

public class Linea_Presupuestos {
	private String producto;
	private String cantidad;
	private String comentario;
	
	public Linea_Presupuestos(	
			String producto, 
			String cantidad,
			String comentario) {
		this.producto = producto;
		this.cantidad = cantidad;
		this.comentario = comentario;
	}

	public String getProducto() 
	{
		return this.producto;
	}

	public String getCantidad() 
	{
		return this.cantidad;
	}

	public String getComentario() 
	{
		return this.comentario;
	}
}

package com.durox.app.Presupuestos;

public class Linea_Presupuestos {
	private String producto;
	private String cantidad;
	private String precio;
	private String total;
	
	public Linea_Presupuestos(	
			String producto, 
			String cantidad,
			String precio,
			String total ) 
	{
		this.producto = producto;
		this.cantidad = cantidad;
		this.precio = precio;
		this.total = total;
	}

	public String getProducto() 
	{
		return this.producto;
	}

	public String getCantidad() 
	{
		return this.cantidad;
	}

	public String getPrecio() 
	{
		return this.precio;
	}
	
	public String getTotal() 
	{
		return this.total;
	}
}

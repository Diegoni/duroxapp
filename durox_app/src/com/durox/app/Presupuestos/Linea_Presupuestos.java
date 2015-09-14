package com.durox.app.Presupuestos;

public class Linea_Presupuestos {
	private String id_linea;
	private String producto;
	private String cantidad;
	private String precio;
	private String total;
	private String cNombre;
	private String cIdVisita;
	private String id_presupuesto;
	private String id;
	
	public Linea_Presupuestos(	
			String id_linea,
			String producto, 
			String cantidad,
			String precio,
			String total,
			String cNombre,
			String cIdVisita,
			String id_presupuesto,
			String id) 
	{
		this.id_linea = id_linea;
		this.producto = producto;
		this.cantidad = cantidad;
		this.precio = precio;
		this.total = total;
		this.cNombre = cNombre;
		this.cIdVisita = cIdVisita;
		this.id_presupuesto = id_presupuesto;
		this.id = id;
	}
	
	public String getIdLinea() 
	{
		return this.id_linea;
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
	
	public String getTotal() {
		return this.total;
	}
	
	public String getcNombre() {
		return this.cNombre;
	}
	
	public String getcIdVisita() {
		return this.cIdVisita;
	}
	
	public String getid_presupuesto() {
		return this.id_presupuesto;
	}
	
	public String getid() {
		return this.id;
	}
}

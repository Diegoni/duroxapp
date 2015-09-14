package com.durox.app.Presupuestos;

public class Presupuesto {
	private String id_back;
	private String nombre;
	private String total;
	private String estado;
	private int imagen;
	private String id_presupuesto;
	
	public Presupuesto(
			String id_back,
			String nombre, 
			String total,
			String estado,
			int imagen,
			String id_presupuesto) {
		this.id_back = id_back;
		this.nombre = nombre;
		this.total = total;
		this.estado = estado;
		this.imagen = imagen;
		this.id_presupuesto = id_presupuesto;
	}
	
	public String getID(){ 
		return this.id_back;
	}


	public String getNombre() {
		return this.nombre;
	}

	public String getTotal() {
		return this.total;
	}
	
	public String getEstado() {
		return this.estado;
	}

	public int getImagen() {
		return this.imagen;
	}
	
	public String getIDPresupuesto() {
		return this.id_presupuesto;
	}

}

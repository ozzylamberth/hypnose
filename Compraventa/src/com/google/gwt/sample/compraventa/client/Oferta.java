package com.google.gwt.sample.compraventa.client;

public class Oferta {
	
	private int monto;
	private int cuota;
	Usuario usuario;
	
	public Oferta(){
		setCuota(0);
		setMonto(0);
		
	}
	
	public Oferta(int cuota, int monto){
		setCuota(cuota);
		setMonto(monto);
	}

	public int getMonto() {
		return monto;
	}

	public void setMonto(int monto) {
		this.monto = monto;
	}

	public int getCuota() {
		return cuota;
	}

	public void setCuota(int cuota) {
		this.cuota = cuota;
	}

	@Override
	public String toString() {
		return "Oferta [monto=" + monto + ", cuota=" + cuota + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cuota;
		return result;
	}

	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Oferta other = (Oferta) obj;
		if (cuota != other.cuota)
			return false;
		return true;
	}
	
	
	
	
	
}

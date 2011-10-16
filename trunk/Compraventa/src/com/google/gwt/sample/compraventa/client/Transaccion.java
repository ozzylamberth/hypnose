package com.google.gwt.sample.compraventa.client;

public class Transaccion {
	
	Usuario comprador;
	Usuario vendedor;
	int monto;
	
	public Usuario getComprador() {
		return comprador;
	}
	public void setComprador(Usuario comprador) {
		this.comprador = comprador;
	}
	public Usuario getVendedor() {
		return vendedor;
	}
	public void setVendedor(Usuario vendedor) {
		this.vendedor = vendedor;
	}
	public int getMonto() {
		return monto;
	}
	public void setMonto(int monto) {
		this.monto = monto;
	}

	
}

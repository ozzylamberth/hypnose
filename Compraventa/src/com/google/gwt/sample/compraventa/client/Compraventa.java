package com.google.gwt.sample.compraventa.client;

import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Random;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Compraventa implements EntryPoint {

	private FlexTable ofertasFlexTable = new FlexTable();
	private VerticalPanel mainPanel = new VerticalPanel();
	private HorizontalPanel agregarPanel = new HorizontalPanel();
	private TextBox cuotaTextBox = new TextBox();
	private TextBox montoTextBox = new TextBox();
	private Button agregarButton = new Button("Agregar");
	private RadioButton comprarRadioButton=new RadioButton("agregarGrupo","comprar");
	private RadioButton venderRadioButton=new RadioButton("agregarGrupo","vender");
	private Label lastUpdatedLabel = new Label();
	private Button actualizarButton = new Button("Actualizar");
	private static final int REFRESH_INTERVAL = 5000;
	
	private ArrayList<Oferta> compras=new ArrayList<Oferta>();
	private ArrayList<Oferta> ventas=new ArrayList<Oferta>();
	private ArrayList<Transaccion> transacciones=new ArrayList<Transaccion>(); //Para el manejo de dinero
	private ArrayList<Usuario> usuarios=new ArrayList<Usuario>();
	  
	public void onModuleLoad() {
		
	    ofertasFlexTable.setText(0, 0, "Oferta Compra");
	    ofertasFlexTable.setText(0, 1, "Oferta Venta");
	        
	    System.out.println("Estoy adentro del onModuleLoad");
	    
//	    GWT.log("estoy en el onModuleLoad");
	    
	    agregarPanel.add(cuotaTextBox);
	    agregarPanel.add(montoTextBox);
	    agregarPanel.add(comprarRadioButton);
	    agregarPanel.add(venderRadioButton);
	    agregarPanel.add(agregarButton);
	    agregarPanel.add(actualizarButton);
	    

	    // Assemble Main panel.
	    
	    mainPanel.add(agregarPanel);
	    mainPanel.add(lastUpdatedLabel);
	    mainPanel.add(ofertasFlexTable);

    
	    RootPanel.get("ofertasList").add(mainPanel);
	    
	    agregarButton.setFocus(true);
	    comprarRadioButton.setEnabled(true);
	    
	    // Escuchamos por el evento de agregar
	    agregarButton.addClickHandler(new ClickHandler() {
	        public void onClick(ClickEvent event) {
	          agregarOferta();
	          actualizarDatos();
	        }
	      });
	    
	    actualizarButton.addClickHandler(new ClickHandler() {
	        public void onClick(ClickEvent event) {
	          actualizarDatos();
	        }
	      });

	    
	    montoTextBox.addKeyPressHandler(new KeyPressHandler() {
	        public void onKeyPress(KeyPressEvent event) {
	          if (event.getCharCode() == KeyCodes.KEY_ENTER) {
	            agregarOferta();
	          }
	        }
	      });
	    
	    cargarDatos();
	    Timer refreshTimer = new Timer() {
	        @Override
	        public void run() {
	          agregarDatosFantasia();
	          actualizarDatos();
	        }
	      };
	      refreshTimer.scheduleRepeating(REFRESH_INTERVAL);
	}
	
	private void agregarOferta(){
		
   
	    // Stock code must be between 1 and 10 chars that are numbers, letters, or dots.
	    if (!cuotaTextBox.getText().matches("^[0-9]*")|!montoTextBox.getText().matches("^[0-9]*")) {
	      Window.alert("'" + cuotaTextBox.getText() + "' o '"+ montoTextBox.getText()+"' no es un monto o cuota adecuada");
	      montoTextBox.selectAll();
	      cuotaTextBox.selectAll();
	      return;
	    }
	    else{
			int cuota = Integer.parseInt(cuotaTextBox.getText().toUpperCase().trim());
			int monto = Integer.parseInt(montoTextBox.getText().toUpperCase().trim());
			if(this.comprarRadioButton.getValue()){
				Oferta compra=new Oferta(cuota,monto);
				if(hayMatchingCompra(compra)){
					matchingCompra(compra);					
				}
				else{				
					this.agregarOfertaCompra(compra);
				}
			}
			else{
				Oferta venta=new Oferta(cuota,monto);
				if(hayMatchingVenta(venta)){
					matchingVenta(venta);
				}
				else{
					this.agregarOfertaVenta(venta);
				}
			}
	    }

	    montoTextBox.setText("");
	    cuotaTextBox.setText("");
		
	}
	
	private void agregarOfertaCompra(Oferta oferta){
		this.compras.add(oferta);
	}
	
	private void agregarOfertaVenta(Oferta oferta){
		this.ventas.add(oferta);
	}
	
	private int obtenerMejorCompra(){
		int largo=this.compras.size();
		int mejorOferta=0;
		mejorOferta=this.compras.get(0).getCuota();
		for(int i=0;i<largo;i++){
			if(this.compras.get(i).getCuota()>mejorOferta)
				mejorOferta=this.compras.get(i).getCuota();
		}
		return mejorOferta;
	}
	
	private int obtenerMejorVenta(){
		int largo=this.ventas.size();
		int mejorOferta=0;
		mejorOferta=this.ventas.get(0).getCuota();
		for(int i=0;i<largo;i++){
			if(this.ventas.get(i).getCuota()<mejorOferta)
				mejorOferta=this.ventas.get(i).getCuota();
		}
		return mejorOferta;		
	}
	
	private int obtenerSumaComprasPorCuota(int cuota){
		int suma=0;
		int largo=this.compras.size();
		for(int i=0;i<largo;i++){
			if(this.compras.get(i).getCuota()==cuota)
				suma=suma+this.compras.get(i).getMonto();
		}
		return suma;
	}
	
	private int obtenerSumaVentasPorCuota(int cuota){
		int suma=0;
		int largo=this.ventas.size();
		for(int i=0;i<largo;i++){
			if(this.ventas.get(i).getCuota()==cuota)
				suma=suma+this.ventas.get(i).getMonto();
		}
		return suma;
	}
	
	private void actualizarDatos(){
		int mejorCompra=this.obtenerMejorCompra();
		int mejorVenta=this.obtenerMejorVenta();
		int sumaCompra=this.obtenerSumaComprasPorCuota(mejorCompra);
		int sumaVenta=this.obtenerSumaVentasPorCuota(mejorVenta);
		this.ofertasFlexTable.setText(1, 0,Integer.toString(mejorCompra));
		this.ofertasFlexTable.setText(1, 1, Integer.toString(mejorVenta));
		this.ofertasFlexTable.setText(2, 0,Integer.toString(sumaCompra));
		this.ofertasFlexTable.setText(2, 1, Integer.toString(sumaVenta));
		
//		GWT.log("Mejor cuota compra:"+obtenerMejorCompra());
//		GWT.log("Mejor cuota venta:"+obtenerMejorVenta());
//		GWT.log("Suma de mejor cuota de compra:"+Integer.toString(sumaCompra));
//		GWT.log("Suma de mejor cuota de venta:"+Integer.toString(sumaVenta));
	}
	
	// Asumimos que existe un matching, por lo tanto este metodo sera llamado partiendo de la base
	// que existe el matching
	private void matchingVenta(Oferta venta){
		int cuotaMejorCompra=this.obtenerMejorCompra();
		int indiceMejorCompra=this.compras.indexOf(new Oferta(cuotaMejorCompra,0));
		Oferta mejorCompra=this.compras.get(indiceMejorCompra);
		int montoMejorCompra=mejorCompra.getMonto();
		if(cuotaMejorCompra!=venta.getCuota()){
			//Significa que ya no quedan ofertas con la misma cuota, pero sigue
			//siendo la mejora compra, entonces la agrego
			agregarOfertaVenta(venta);
			return;
		}
		if(venta.getMonto()<mejorCompra.getMonto()){
			mejorCompra.setMonto(montoMejorCompra-venta.getMonto());
			compras.remove(indiceMejorCompra);
			compras.add(indiceMejorCompra, mejorCompra);
			return;
		}
		else if(venta.getMonto()==montoMejorCompra){
			compras.remove(indiceMejorCompra);
			return;
		}
		else{
			compras.remove(indiceMejorCompra);
			matchingVenta(new Oferta(cuotaMejorCompra,venta.getMonto()-montoMejorCompra));
		}
	}
	
	// Asumimos que existe un matching, por lo tanto este metodo sera llamado partiendo de la base
	// que existe el matching
	private void matchingCompra(Oferta compra){
		int cuotaMejorVenta=this.obtenerMejorVenta();
		int indiceMejorVenta=this.ventas.indexOf(new Oferta(cuotaMejorVenta,0));
		Oferta mejorVenta=this.ventas.get(indiceMejorVenta);
		int montoMejorVenta=mejorVenta.getMonto();
		if(cuotaMejorVenta!=compra.getCuota()){
			//Significa que ya no quedan ofertas con la misma cuota, pero sigue
			//siendo la mejora compra, entonces la agrego
			agregarOfertaCompra(compra);
			return;
			
		}
		if(compra.getMonto()<mejorVenta.getMonto()){
			mejorVenta.setMonto(montoMejorVenta-compra.getMonto());
			ventas.remove(indiceMejorVenta);
			ventas.add(indiceMejorVenta, mejorVenta);
			return;
		}
		else if(compra.getMonto()==montoMejorVenta){
			ventas.remove(indiceMejorVenta);
			return;
		}
		else{
			ventas.remove(indiceMejorVenta);
			matchingCompra(new Oferta(cuotaMejorVenta,compra.getMonto()-montoMejorVenta));
		}
	}

	boolean hayMatchingVenta(Oferta venta){
		boolean retorno=false;
		int mejorCuotaCompra=this.obtenerMejorCompra();
		if (mejorCuotaCompra==venta.getCuota())
			retorno=true;
		return retorno;
	}
	
	boolean hayMatchingCompra(Oferta compra){
		boolean retorno=false;
		int mejorCuotaVenta=this.obtenerMejorVenta();
		if (mejorCuotaVenta==compra.getCuota())
			retorno=true;
		return retorno;
	}
	
	public void cargarDatos(){
		Usuario usuario1=new Usuario();
		usuario1.setBilletera(5000);
		usuario1.setNombre("Carlos");
		usuario1.setUsername("carlitox");
		
		Usuario usuario2=new Usuario();
		usuario2.setBilletera(8000);
		usuario2.setNombre("Rosalia");
		usuario2.setUsername("rosetex");
		
		Usuario usuario3=new Usuario();
		usuario3.setBilletera(90000);
		usuario3.setNombre("Marlene");
		usuario3.setUsername("marle");
		
		Usuario usuario4=new Usuario();
		usuario4.setBilletera(2000);
		usuario4.setNombre("Gaston");
		usuario4.setUsername("lagaston");
		
		this.usuarios.add(usuario1);
		this.usuarios.add(usuario2);
		this.usuarios.add(usuario3);
		this.usuarios.add(usuario4);
		
		
		Oferta compra1=new Oferta(2,200);
		compra1.setUsuario(usuario3);
		Oferta compra2=new Oferta(1,100);
		compra2.setUsuario(usuario2);
		Oferta compra3=new Oferta(1,300);
		compra3.setUsuario(usuario3);
		Oferta compra4=new Oferta(2,200);
		compra3.setUsuario(usuario2);
		
		this.agregarOfertaCompra(compra1);
		this.agregarOfertaCompra(compra2);
		this.agregarOfertaCompra(compra3);
		this.agregarOfertaCompra(compra4);
		
		Oferta venta1=new Oferta(5,200);
		venta1.setUsuario(usuario4);
		Oferta venta2=new Oferta(5,200);
		venta2.setUsuario(usuario1);
		Oferta venta3=new Oferta(6,300);
		venta3.setUsuario(usuario1);
		Oferta venta4=new Oferta(7,200);
		venta4.setUsuario(usuario4);
		
		this.agregarOfertaVenta(venta1);
		this.agregarOfertaVenta(venta2);
		this.agregarOfertaVenta(venta3);
		this.agregarOfertaVenta(venta4);
		
	}
	
	private void agregarDatosFantasia(){
		//Agregar oferta compra
		int cuotaCompra=obtenerNumeroAleatorio(5);
		int montoCompra=obtenerNumeroAleatorio(1000);
		Oferta compra=new Oferta(cuotaCompra,montoCompra);
		
		//Agregar oferta venta
		int cuotaVenta=obtenerNumeroAleatorio(5)+5;
		int montoVenta=obtenerNumeroAleatorio(1000);
		Oferta venta=new Oferta(cuotaVenta,montoVenta);
		
		if(hayMatchingCompra(compra)){
			matchingCompra(compra);
		}
		else{
			agregarOfertaCompra(compra);
			
		}
		
		if(hayMatchingVenta(venta)){
			matchingVenta(venta);
		}
		else{
			agregarOfertaVenta(venta);
			
		}
		
	}
	
	private int obtenerNumeroAleatorio(int max){
		int retorno=0;
		retorno=Random.nextInt(max);
		return retorno;
	}

	
	
}

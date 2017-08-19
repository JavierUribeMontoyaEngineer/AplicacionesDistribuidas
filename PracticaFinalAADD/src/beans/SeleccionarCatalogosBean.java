package beans;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;

import controlador.Controlador;
import modelo.Catalogo;
import modelo.Item;
import modelo.OrdenCriterio;
import modelo.TipoCriterio;
import modelo.Usuario;

@ManagedBean(name = "seleccionarCatalogosBean")
@SessionScoped
public class SeleccionarCatalogosBean {

	private List<Catalogo> catalogosSeleccionados;
	private List<Catalogo> catalogosBuscados;

	private String keyword = "";
	private String ordenarPor = "";
	// criterio = buscar por un criterio
	private String criterio = "";
	private String ascendente = "";
	// private List<Item> listaItems;
	private boolean visible = false;

	@PostConstruct
	public void init(){
		catalogosBuscados = new ArrayList<Catalogo>();
	}
	
	public List<Catalogo> getCatalogosBuscados() {
		return catalogosBuscados;
	}
	public void setCatalogosBuscados(List<Catalogo> catalogosBuscados) {
		this.catalogosBuscados = catalogosBuscados;
	}
	
	
	public List<Catalogo> getCatalogosSeleccionados() {
		return catalogosSeleccionados;
	}

	public void setCatalogosSeleccionados(List<Catalogo> catalogosSeleccionados) {
		this.catalogosSeleccionados = catalogosSeleccionados;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getOrdenarPor() {
		return ordenarPor;
	}

	public void setOrdenarPor(String ordenarPor) {
		this.ordenarPor = ordenarPor;
	}

	public String getCriterio() {
		return criterio;
	}

	public void setCriterio(String criterio) {
		this.criterio = criterio;
	}

	public String getAscendente() {
		return ascendente;
	}

	public void setAscendente(String ascendente) {
		this.ascendente = ascendente;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void conmutar() {
		setVisible(!visible);
	}

	public void filtrar(ActionEvent evento) {
		TipoCriterio buscarPor = TipoCriterio.values()[Integer.valueOf(getCriterio())];
		TipoCriterio ordenarPor = TipoCriterio.values()[Integer.valueOf(getOrdenarPor())];
		OrdenCriterio orden = OrdenCriterio.values()[Integer.valueOf(getAscendente())];

		if (getCriterio().equals("5"))
			keyword = keyword.toUpperCase();
		
		for (Catalogo catalogo : getCatalogosSeleccionados()) {
			List<Item> items = Controlador.getUnicaInstancia().buscarItemPorCriterio(buscarPor, orden, ordenarPor,
					catalogo, keyword);
			catalogo.setItemsFiltrados(items);
			catalogosBuscados.add(catalogo);
		}
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("busquedaCatalogos.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void clear() {
		keyword = "";
		ordenarPor = "";
		criterio = "";
		ascendente = "";
		catalogosSeleccionados = null;
		catalogosBuscados = new ArrayList<Catalogo>();
	}


	


}

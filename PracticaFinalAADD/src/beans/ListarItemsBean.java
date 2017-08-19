package beans;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import controlador.Controlador;
import modelo.Catalogo;
import modelo.Item;
import modelo.OrdenCriterio;
import modelo.TipoCriterio;

@ManagedBean(name = "listarItemsBean")
@SessionScoped
public class ListarItemsBean {
	private List<Item> items;
	private Item itemSeleccionado;
	private Catalogo catalogoActual;
	private String keyword = "";
	private String ordenarPor = "";
	// criterio = buscar por un criterio
	private String criterio = "";
	private String ascendente = "";

	private boolean itemEditable;
	private boolean filtrandoItems = false;

	private Catalogo catalogoBusquedaItems;

	public Catalogo getCatalogoBusquedaItems() {
		return catalogoBusquedaItems;
	}

	public void setCatalogoBusquedaItems(Catalogo catalogoBusquedaItems) {
		this.catalogoBusquedaItems = catalogoBusquedaItems;
	}

	public boolean isItemEditable() {
		return itemEditable;
	}

	public void setItemEditable(boolean itemEditable) {
		this.itemEditable = itemEditable;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public List<Item> getItems() {
		if (!filtrandoItems) {
			System.out.println("******************************");
			items = Controlador.getUnicaInstancia().buscarItemPorCatalogo(getCatalogoActual());
		} else{
			System.out.println("-------------------------------");
		}
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public Item getItemSeleccionado() {
		return itemSeleccionado;
	}

	public void setItemSeleccionado(Item itemSeleccionado) {
		this.itemSeleccionado = itemSeleccionado;
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

	public Catalogo getCatalogoActual() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String catalogoActualSesion = (String) request.getSession().getAttribute("catalogo_actual");
		catalogoActual = Controlador.getUnicaInstancia().buscarCatalogo(catalogoActualSesion);
		// catalogoActual = Controlador.getUnicaInstancia().getCatalogoActual();
		return catalogoActual;
	}

	public void setCatalogoActual(Catalogo catalogoActual) {
		this.catalogoActual = catalogoActual;
	}

	public void seleccionarItem(SelectEvent event) {
		FacesMessage msg = new FacesMessage("Item seleccionado", ((Item) event.getObject()).getNombre());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void deseleccionarItem(UnselectEvent event) {
		FacesMessage msg = new FacesMessage("Item deseleccionado", ((Item) event.getObject()).getNombre());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void borrar(ActionEvent evento) {
		UIParameter parametro = (UIParameter) evento.getComponent().findComponent("item");
		itemSeleccionado = (Item) parametro.getValue();
		Controlador.getUnicaInstancia().borrarItem(itemSeleccionado);
		
		// Si borramos un item desde la ventana busquedaCatalogo.xhtml, hay que
		// actualizar la lista
		// de items filtrados de ese catalogo. Recuperamos ese catalogo y
		// eliminamos el item de la lista.
		// Si borramos desde la ventana catalogo.xhtml todos los items seran del
		// catalogoActual, asi que
		// no hay que borrarlo de ninguna lista aparte.
		UIParameter parametro2 = (UIParameter) evento.getComponent().findComponent("catalogoFiltrado");
		if (parametro2 != null) {
			catalogoBusquedaItems = (Catalogo) parametro2.getValue();
			catalogoBusquedaItems.removeItemFiltrado(itemSeleccionado);
		}
		// Si lo borramos desde la ventana catalogos.xhtml lo borramos de la lista "items"
		else {
			System.out.println(items);
			items.remove(itemSeleccionado);
			System.out.println("Item borrado:\n" + itemSeleccionado);
		}
	}

	public void filtrar(ActionEvent evento) {
		System.out.println("ListarItemsBean.filtrar()");
		TipoCriterio buscarPor = TipoCriterio.values()[Integer.valueOf(getCriterio())];
		TipoCriterio ordenarPor = TipoCriterio.values()[Integer.valueOf(getOrdenarPor())];
		OrdenCriterio orden = OrdenCriterio.values()[Integer.valueOf(getAscendente())];

		if (getCriterio().equals("5"))
			keyword = keyword.toUpperCase();
		items = Controlador.getUnicaInstancia().buscarItemPorCriterio(buscarPor, orden, ordenarPor, catalogoActual,
				keyword);

		filtrandoItems = !keyword.isEmpty();
	}

	public void editarItem() {
		System.out.println("ListarItemsBean.editarItem()");
		setItemEditable(!itemEditable);
	}

	public void guardarItem() {
		System.out.println("ListarItemsBean.guardarItem()");
		setItemEditable(!itemEditable);
		Controlador.getUnicaInstancia().updateItem(itemSeleccionado, itemSeleccionado.getUrlProducto(),
				itemSeleccionado.getNombre(), itemSeleccionado.getUrlImagen(), itemSeleccionado.getNombreCompleto(),
				itemSeleccionado.getPrecioRebajado(), itemSeleccionado.getPrecioOriginal());
	}

	public void clear() {
		keyword = "";
		ordenarPor = "";
		criterio = "";
		ascendente = "";
		itemEditable = false;
		filtrandoItems = false;
	}

}

package beans;

import java.io.IOException;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import controlador.Controlador;
import modelo.Catalogo;
import modelo.Item;
import modelo.Usuario;

@ManagedBean(name = "listarCatalogosBean")
@SessionScoped
public class ListarCatalogosBean {

	private List<Catalogo> catalogos;
	private Catalogo catalogoSeleccionado;
	
	public List<Catalogo> getCatalogos() {
		//Usuario usuarioActual = Controlador.getUnicaInstancia().getUsuarioActual();
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String usuarioLogueado = (String) request.getSession().getAttribute("usuario_actual");
		System.out.println("++++++ " + usuarioLogueado);
		Usuario usuarioActual = Controlador.getUnicaInstancia().buscarUsuario(usuarioLogueado);
		catalogos = Controlador.getUnicaInstancia().buscarCatalogosUsuario(usuarioActual);
		return catalogos;
	}
	
	public void setCatalogos(List<Catalogo> catalogos) {
		this.catalogos = catalogos;
	}

	public Catalogo getCatalogoSeleccionado() {
		return catalogoSeleccionado;
	}

	public void setCatalogoSeleccionado(Catalogo catalogoSeleccionado) {
		this.catalogoSeleccionado = catalogoSeleccionado;
	}

	public void seleccionarCatalogo(SelectEvent event) {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		request.getSession().setAttribute("catalogo_actual",catalogoSeleccionado.getNombre());
		try {
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect("catalogo.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(",,,,,,,,,,,,,," + catalogoSeleccionado);
		FacesMessage msg = new FacesMessage("Catálogo seleccionado", ((Catalogo) event.getObject()).getNombre());
		FacesContext.getCurrentInstance().addMessage(null, msg);
		
	}

	public void deseleccionarCatalogo(UnselectEvent event) {
		FacesMessage msg = new FacesMessage("Catálogo deseleccionado", ((Catalogo) event.getObject()).getNombre());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void borrar(ActionEvent evento) {
		System.out.println("ListarCatalogosBean.borrar()");
		// Buscamos el parametro con id "catalogo"
		UIParameter parametro = (UIParameter) evento.getComponent().findComponent("catalogo");
		catalogoSeleccionado = (Catalogo) parametro.getValue();
	
		System.out.println("Catalogo borrado");
		System.out.println(catalogoSeleccionado);
		Controlador.getUnicaInstancia().borrarCatalogo(catalogoSeleccionado);
		
	}
	
	public void clear() {
		catalogoSeleccionado = null;
	}

}
